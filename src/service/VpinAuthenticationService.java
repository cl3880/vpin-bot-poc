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

import com.osparks.vpin.bot.dto.BotLoginResponse;
import com.osparks.vpin.bot.dto.OAuth2TokenResponse;
import com.osparks.vpin.bot.exceptions.BotLoginException;
import com.osparks.vpin.bot.exceptions.TokenFetchException;
import com.osparks.vpin.bot.model.BotModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import java.net.http.HttpHeaders;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * Service for managing OAuth2 authentication for Vpin.
 * 
 * @Author Christopher Leu (
 *         <a href= "mailto:chrisleu9@gmail.com">chrisleu9@gmail.com</a>)
 */
@Service
public class VpinAuthenticationService {
    private final String clientId;
    private final String clientSecret;
    private final String loginUrl;
    private final String botLoginUrl;
    private final RestTemplate restTemplate;
    private final RemoteExchangeService remoteExchangeService;
    private OAuth2TokenResponse currentTokenResponse;

    @Autowired
    public VpinAuthenticationService(@Value("${oauth2.client-id}") String clientId,
            @Value("${oauth2.client-secret}") String clientSecret, @Value("${vpin.loginUri}") String loginUrl,
            @Value("${vpin.botLoginUri}") String botLoginUrl, RestTemplate restTemplate,
            RemoteExchangeService remoteExchangeService) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.loginUrl = loginUrl;
        this.botLoginUrl = botLoginUrl;
        this.restTemplate = restTemplate;
        this.remoteExchangeService = remoteExchangeService;
    }

    private String getEncodedCredentials() {
        String clientCredentials = this.clientId + ":" + this.clientSecret;
        return Base64.getEncoder().encodeToString(clientCredentials.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Retrieves a client token using the client credentials.
     *
     * @return the OAuth2 token response
     */
    public OAuth2TokenResponse getClientToken() {
        if (currentTokenResponse == null || isTokenExpired(currentTokenResponse)) {
            MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
            body.add("grant_type", "client_credentials");
            HttpEntity<MultiValueMap<String, String>> request = createHttpRequestWithAuth(body);
            ResponseEntity<OAuth2TokenResponse> response = restTemplate.exchange(loginUrl, HttpMethod.POST, request,
                    OAuth2TokenResponse.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                currentTokenResponse = response.getBody();
            } else {
                throw new TokenFetchException("Failed to get client token");
            }
        }

        return currentTokenResponse;
    }

    /**
     * Checks if the token is expired.
     *
     * @param tokenResponse the OAuth2 token response
     * @return true if the token is expired, false otherwise
     */
    private boolean isTokenExpired(OAuth2TokenResponse tokenResponse) {
        // Placeholder for expiration logic
        return false;
    }

    /**
     * Logs in a bot using the provided credentials.
     *
     * @param botModel the bot model containing the credentials
     * @return the bot login response
     * @throws Exception if an error occurs during login
     */
    public BotLoginResponse botLogin(BotModel botModel) throws Exception {
        OAuth2TokenResponse tokenResponse = getClientToken();
        if (tokenResponse == null) {
            throw new TokenFetchException("Failed to fetch client token");
        }

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("password", botModel.getPassword());
        map.add("username", botModel.getUsername());

        try {
            return remoteExchangeService.exchangeFromRemoteServerWithTokenResponse(tokenResponse, botLoginUrl,
                    HttpMethod.POST, MediaType.APPLICATION_FORM_URLENCODED, map, BotLoginResponse.class);
        } catch (Exception e) {
            throw new BotLoginException("Failed to log in bot.", e);
        }
    }

    private HttpEntity<MultiValueMap<String, String>> createHttpRequestWithAuth(MultiValueMap<String, String> body) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Basic " + getEncodedCredentials());
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        return new HttpEntity<>(body, headers);
    }
}