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

import com.osparks.vpin.bot.dto.OpenAIChatRequest;
import com.osparks.vpin.bot.dto.OpenAIGeneralResponse;
import com.osparks.vpin.bot.dto.VpinCreationResponse;
import com.osparks.vpin.bot.exceptions.OpenAIAPIException;
import com.osparks.vpin.bot.model.BotModel;
import java.net.http.HttpHeaders;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * Service class for interacting with the OpenAI API. It handles creating and
 * sending requests to the API and processing the responses.
 * 
 * @Author Christopher Leu (
 *         <a href= "mailto:chrisleu9@gmail.com">chrisleu9@gmail.com</a>)
 */
@Service
public class OpenAIService {
    private final RestTemplate restTemplate;
    private final String openaiApiKey;
    @Value("${openai.api.uri}")
    private String openaiApiUrl;

    public OpenAIService(RestTemplate restTemplate, @Value("${openai.api.key}") String openaiApiKey) {
        this.restTemplate = restTemplate;
        this.openaiApiKey = openaiApiKey;
    }

    /**
     * Creates HTTP headers for the OpenAI API request.
     *
     * @return HttpHeaders containing the necessary headers for the request
     */
    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + openaiApiKey);
        return headers;
    }

    /**
     * Sends a POST request to the OpenAI API.
     *
     * @param request      the request payload
     * @param responseType the class type of the response
     * @param <T>          the type of the response
     * @return the response from the OpenAI API
     * @throws OpenAIAPIException if there is an error with the API request
     */
    private <T> T postRequest(OpenAIChatRequest request, Class<T> responseType) throws OpenAIAPIException {
        try {
            HttpHeaders headers = createHeaders();
            HttpEntity<OpenAIChatRequest> httpEntity = new HttpEntity<>(request, headers);
            ResponseEntity<T> response = restTemplate.postForEntity(openaiApiUrl, httpEntity, responseType);

            if (response.getStatusCode() == HttpStatus.OK) {
                return response.getBody();
            }
        } catch (OpenAIAPIException e) {
            throw new OpenAIAPIException("Error with OpenAI API request.");
        }
        return null;
    }

    /**
     * Generates interests based on age, gender, and occupation using OpenAI.
     *
     * @param age        the age of the user
     * @param gender     the gender of the user
     * @param occupation the occupation of the user
     * @return an OpenAIGeneralResponse containing the generated interests
     * @throws Exception if the response from OpenAI is invalid
     */
    public OpenAIGeneralResponse generateInterests(int age, String gender, String occupation) throws Exception {
        String prompt = String.format("""
                placeholder
                """, age, gender, occupation);
        OpenAIChatRequest reqInterests = new OpenAIChatRequest("gpt-3.5-turbo", prompt, 512, 0.5F);
        OpenAIGeneralResponse response = postRequest(reqInterests, OpenAIGeneralResponse.class);

        if (response == null || response.getChoices().isEmpty()) {
            throw new Exception("Invalid response from OpenAI.");
        }
        System.out.println(response.getChoices().get(0).getMessage().getContent());
        return response;
    }

    /**
     * Generates a response for browsing Vpins based on bot model and interests
     * using OpenAI.
     *
     * @param botModel     the bot model containing user details
     * @param interests    the interests of the user
     * @param vpinInfoText the information text about the Vpin
     * @return an OpenAIGeneralResponse containing the response from OpenAI
     */
    public OpenAIGeneralResponse browseVpins(BotModel botModel, String interests, String vpinInfoText) {
        String prompt = String.format("""
                placeholder
                """, botModel.getAge(), botModel.getGender(), botModel.getOccupation(), interests, vpinInfoText);
        OpenAIChatRequest reqOneVpin = new OpenAIChatRequest("gpt-3.5-turbo", prompt, 128, 0.5F);
        return postRequest(reqOneVpin, OpenAIGeneralResponse.class);
    }

    /**
     * Generates a response for browsing tags based on bot model and interests using
     * OpenAI.
     *
     * @param botModel  the bot model containing user details
     * @param interests the interests of the user
     * @param tags      the tags to browse
     * @return an OpenAIGeneralResponse containing the response from OpenAI
     */
    public OpenAIGeneralResponse browseTags(BotModel botModel, String interests, String tags) {
        String prompt = String.format("""
                placeholder
                """, botModel.getAge(), botModel.getGender(), botModel.getOccupation(), interests, tags);
        OpenAIChatRequest reqOneTag = new OpenAIChatRequest("gpt-3.5-turbo", prompt, 128, 0.5F);
        return postRequest(reqOneTag, OpenAIGeneralResponse.class);
    }

    /**
     * Generates a comment for a Vpin based on bot model and interests using OpenAI.
     *
     * @param botModel  the bot model containing user details
     * @param interests the interests of the user
     * @param vpinInfo  the information about the Vpin
     * @return an OpenAIGeneralResponse containing the generated comment
     */
    public OpenAIGeneralResponse commentVpin(BotModel botModel, String interests, String vpinInfo) {
        String prompt = String.format("""
                placeholder
                 """, botModel.getAge(), botModel.getGender(), botModel.getOccupation(), interests, vpinInfo);
        OpenAIChatRequest reqComment = new OpenAIChatRequest("gpt-3.5-turbo", prompt, 256, 0.5F);
        return postRequest(reqComment, OpenAIGeneralResponse.class);
    }

    /**
     * Generates a reply to a comment based on bot model and interests using OpenAI.
     *
     * @param botModel  the bot model containing user details
     * @param interests the interests of the user
     * @param comments  the comments to reply to
     * @return an OpenAIGeneralResponse containing the generated reply
     */
    public OpenAIGeneralResponse replyComment(BotModel botModel, String interests, String comments) {
        String prompt = String.format("""
                placeholder
                 """, botModel.getAge(), botModel.getGender(), botModel.getOccupation(), interests, comments);
        OpenAIChatRequest reqComment = new OpenAIChatRequest("gpt-3.5-turbo", prompt, 256, 0.5F);
        return postRequest(reqComment, OpenAIGeneralResponse.class);
    }
}