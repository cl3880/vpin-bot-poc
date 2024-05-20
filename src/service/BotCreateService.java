/**
 * Copyright (c) 2023 Osparks AMG Inc. All rights reserved.
 * <p>
 * All information and code contained herein is the property of Osparks AMG Inc.
 * <p>
 * Permission is granted to view this material for personal use only.
 * <p>
 * Any unauthorized modification, publication, distribution, sublicensing, or sale of this
 * material without written permission from Osparks AMG Inc. is strictly prohibited.
 * <p>
 * Employers who have received this software through a job application process
 * are granted full access to view, modify, distribute, and use the software for
 * evaluation purposes only.
 */

package com.osparks.vpin.bot.service;

import com.osparks.vpin.bot.dao.BotRepository;
import com.osparks.vpin.bot.dto.*;
import com.osparks.vpin.bot.exceptions.BotLoginException;
import com.osparks.vpin.bot.exceptions.OpenAIAPIException;
import com.osparks.vpin.bot.model.BotModel;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.net.http.HttpHeaders;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.Random;

/**
 * Service for creating bots and handling related operations.
 * 
 * @Author Christopher Leu (
 *         <a href= "mailto:chrisleu9@gmail.com">chrisleu9@gmail.com</a>)
 */
@Service
public class BotCreateService {
    private static final String RANDOM_DATA_API_URL = "placeholder";
    private static final String VPIN_CREATE_BOT_USER_URI = "placeholder";
    private final BotRepository botRepo;
    private final VpinAuthenticationService vpinAuthenticationService;
    private final RemoteExchangeService remoteExchangeService;
    private final RestTemplate restTemplate;
    private final BotSchedulerService botSchedulerService;

    @Autowired
    public BotCreateService(BotRepository botRepo, VpinAuthenticationService vpinAuthenticationService,
            RemoteExchangeService remoteExchangeService, BotSchedulerService botSchedulerService) {
        this.botRepo = botRepo;
        this.vpinAuthenticationService = vpinAuthenticationService;
        this.remoteExchangeService = remoteExchangeService;
        this.restTemplate = new RestTemplate();
        this.botSchedulerService = botSchedulerService;
    }

    private static String fetchRandomGender() {
        return new Random().nextInt(2) == 0 ? "Male" : "Female";
    }

    private static String fetchRandomLocale() {
        return new Random().nextInt(2) == 0 ? "en_US" : "zh_TW";
    }

    /**
     * Updates the activation status of a bot.
     *
     * @param botModel the bot model
     * @param isActive the activation status
     */
    @Transactional
    public void updateBotActivationStatus(BotModel botModel, boolean isActive) {
        botModel.setActive(isActive);
        this.botRepo.save(botModel);
    }

    /**
     * Creates a new bot based on the provided data.
     *
     * @param authentication  the OAuth2 authentication token
     * @param botDashFormData the bot creation data
     * @return the created bot model
     * @throws SchedulerException if an error occurs during bot scheduling
     */
    public BotModel createBot(OAuth2AuthenticationToken authentication, BotDashFormData botDashFormData)
            throws SchedulerException {
        BotModel botModel = this.transformDtoToBot(botDashFormData);
        String password = this.generateRandomPassword();
        LocalDateTime createDate = LocalDateTime.now();
        String vpinPlatformId = this.createVpinBotUserAndGetId(authentication, botModel, password);
        OAuth2TokenResponse tokenResponse = this.vpinAuthenticationService.getClientToken();

        this.populateBotProperties(botModel, password, createDate, vpinPlatformId);
        this.loginAndSaveBot(botModel);
        botSchedulerService.rescheduleBot(botModel);

        return botModel;
    }

    private BotModel transformDtoToBot(BotDashFormData dto) {
        return BotModel.fromDto(dto);
    }

    private String generateRandomPassword() {
        SecureRandom random = new SecureRandom();
        char[] chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < 10; i++) {
            char c = chars[random.nextInt(chars.length)];
            sb.append(c);
        }

        return sb.toString();
    }

    private String createVpinBotUserAndGetId(OAuth2AuthenticationToken authentication, BotModel botModel,
            String password) {
        VpinBotUser vpinBotUser = this.createUser(authentication, new BotCreateUserRequest(botModel.getLocale(),
                botModel.getNickname(), password, botModel.getUsername()));
        return vpinBotUser.getId();
    }

    private VpinBotUser createUser(OAuth2AuthenticationToken authenticationToken, BotCreateUserRequest createRequest) {
        HttpEntity<MultiValueMap<String, String>> request = createHttpRequestForUserCreation(createRequest);

        return remoteExchangeService.exchangeFromRemoteServerWithOAuth2AuthenticationToken(authenticationToken,
                VPIN_CREATE_BOT_USER_URI, HttpMethod.POST,
                MediaType.APPLICATION_FORM_URLENCODED, request.getBody(), VpinBotUser.class);
    }

    private HttpEntity<MultiValueMap<String, String>> createHttpRequestForUserCreation(
            BotCreateUserRequest createRequest) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("locale", createRequest.getLocale());
        map.add("nickname", createRequest.getNickname());
        map.add("password", createRequest.getPassword());
        map.add("username", createRequest.getUsername());

        return new HttpEntity<>(map, headers);
    }

    private void populateBotProperties(BotModel botModel, String password, LocalDateTime createDate, String vpinId) {
        botModel.setCreateDate(createDate);
        botModel.setPassword(password);
        botModel.setVpinPlatformId(vpinId);
        botModel.setLastInteractedVpinId("");
    }

    private void loginAndSaveBot(BotModel botModel) {
        try {
            BotLoginResponse loginResponse = this.vpinAuthenticationService.botLogin(botModel);
            this.botRepo.save(botModel);
        } catch (Exception e) {
            throw new BotLoginException("Failed to login with bot credentials.", e);
        }
    }

    /**
     * Generates a random bot using the OpenAI API.
     *
     * @return a GenerateRandBotResponse containing the generated bot data
     * @throws OpenAIAPIException if an error occurs while generating the bot
     */
    public GenerateRandBotResponse generateRandBot() throws OpenAIAPIException {
        HttpHeaders headersRand = new HttpHeaders();
        try {
            ResponseEntity<BotRandomAPIResponse> responseRand = this.restTemplate.exchange(RANDOM_DATA_API_URL,
                    HttpMethod.GET,
                    new HttpEntity<>(headersRand), BotRandomAPIResponse.class);

            if (responseRand.getStatusCode() == HttpStatus.OK) {
                return this.generateRandBotResponseFromApiData(responseRand.getBody());
            }
        } catch (Exception e) {
            throw new OpenAIAPIException("Failed to receive Random-Data API response.", e);
        }
        return null;
    }

    private GenerateRandBotResponse generateRandBotResponseFromApiData(BotRandomAPIResponse dataRand) {
        if (dataRand != null) {
            return new GenerateRandBotResponse(
                    dataRand.getFirstName() + " " + dataRand.getLastName(),
                    dataRand.getAddress() == null ? null : dataRand.getAddress().getCountry(),
                    dataRand.getEmployment() == null ? null : dataRand.getEmployment().getTitle(),
                    dataRand.getUsername(),
                    dataRand.getEmail(),
                    fetchRandomGender(),
                    fetchRandomLocale(),
                    calculateAgeFromBirthDate(dataRand.getDateOfBirth()));
        }
        return null;
    }

    private int calculateAgeFromBirthDate(String dateOfBirth) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate dob = LocalDate.parse(dateOfBirth, formatter);
        return Period.between(dob, LocalDate.now()).getYears();
    }
}