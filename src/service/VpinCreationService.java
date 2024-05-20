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

import com.fasterxml.jackson.databind.JsonNode;
import com.osparks.vpin.bot.dto.CreateVpinRequest;
import com.osparks.vpin.bot.dto.UpdateVpinRequest;
import com.osparks.vpin.bot.dto.VpinCreationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.http.HttpHeaders;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

/**
 * Service for creating and managing Vpins.
 * 
 * @Author Christopher Leu (
 *         <a href= "mailto:chrisleu9@gmail.com">chrisleu9@gmail.com</a>)
 */
@Service
public class VpinCreationService {

    @Value("${vpin.server-uri}")
    private String apiServerUrl;
    @Value("${vpin.yt-info-api.url}")
    private String ytInfoApiUrl;

    @Autowired
    private RemoteExchangeService remoteExchangeService;
    @Autowired
    private OpenAIService openAIService;
    @Autowired
    private SimpMessagingTemplate template;

    /**
     * Creates a Vpin using the given access token and video URL.
     *
     * @param accessToken the access token
     * @param videoUrl    the video URL
     * @return the created Vpin
     * @throws IOException if an error occurs during Vpin creation
     */
    public VpinCreationResponse.Vpin createVpin(String accessToken, String videoUrl) throws IOException {
        String externalApiUrl = ytInfoApiUrl + videoUrl;
        JsonNode videoInfo = remoteExchangeService.exchangeFromRemoteServerForGet(accessToken, externalApiUrl,
                JsonNode.class);

        String thumbnail = videoInfo.get("thumbnail").asText();
        byte[] thumbnailBytes = downloadImage(thumbnail);

        CreateVpinRequest vpinRequest = buildCreateVpinRequest(videoInfo);

        logVpinCreationDetails(vpinRequest, videoUrl);

        LinkedMultiValueMap<String, Object> request = new LinkedMultiValueMap<>();
        request.add("coverFile", createHttpEntityForImage(thumbnailBytes, thumbnail));
        request.add("createVpinRequest", vpinRequest);

        String vpinCreationEndpoint = apiServerUrl + "/vpin";

        return remoteExchangeService.exchangeFromRemoteServerWithFormParams(accessToken, vpinCreationEndpoint,
                HttpMethod.POST, MediaType.MULTIPART_FORM_DATA, request, VpinCreationResponse.Vpin.class);
    }

    /**
     * Downloads an image from the given URL.
     *
     * @param imageUrl the image URL
     * @return the image bytes
     */
    private byte[] downloadImage(String imageUrl) {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<byte[]> response = restTemplate.getForEntity(imageUrl, byte[].class);
        return response.getBody();
    }

    /**
     * Builds a CreateVpinRequest object from the given video information.
     *
     * @param videoInfo the video information
     * @return the CreateVpinRequest object
     */
    private CreateVpinRequest buildCreateVpinRequest(JsonNode videoInfo) {
        CreateVpinRequest vpinRequest = new CreateVpinRequest();
        vpinRequest.setId(UUID.randomUUID().toString());
        vpinRequest.setTextContent(videoInfo.get("videoTitle").asText());
        vpinRequest.setVideoUrl(videoInfo.get("url").asText());
        vpinRequest.setVideoPlatform(videoInfo.get("videoPlatform").asText());
        vpinRequest.setTags(new ArrayList<>());
        vpinRequest.setState("PUBLIC");
        vpinRequest.setTemplate("NORMAL");
        vpinRequest.setThumbnailIndex(0);
        return vpinRequest;
    }

    /**
     * Logs the details of the Vpin creation.
     *
     * @param vpinRequest the Vpin request
     * @param videoUrl    the video URL
     */
    private void logVpinCreationDetails(CreateVpinRequest vpinRequest, String videoUrl) {
        System.out.println("CreateVpinRequest: textContent = " + vpinRequest.getTextContent());
        System.out.println("CreateVpinRequest: VideoURL = " + vpinRequest.getVideoUrl());
        template.convertAndSend("/topic/botlogs",
                "Creating Vpin with YT Video: " + vpinRequest.getTextContent() + " (" + videoUrl + ").");
    }

    /**
     * Creates an HttpEntity for the image upload.
     *
     * @param imageBytes the image bytes
     * @param fileName   the file name
     * @return the HttpEntity
     */
    private HttpEntity<byte[]> createHttpEntityForImage(byte[] imageBytes, String fileName) {
        MultiValueMap<String, String> fileMap = new LinkedMultiValueMap<>();
        ContentDisposition contentDisposition = ContentDisposition.builder("form-data")
                .name("coverFile")
                .filename(fileName)
                .build();
        fileMap.add(HttpHeaders.CONTENT_DISPOSITION, contentDisposition.toString());
        return new HttpEntity<>(imageBytes, fileMap);
    }

    /**
     * Fetches tags for the given Vpin.
     *
     * @param accessToken the access token
     * @param vpin        the Vpin
     * @return the tags
     * @throws IOException if an error occurs during tag fetching
     */
    public String[] fetchTagsForVpin(String accessToken, VpinCreationResponse.Vpin vpin) throws IOException {
        String id = vpin.getId();
        String url = apiServerUrl + "/ai/tag/vpin/" + id;
        return remoteExchangeService.exchangeFromRemoteServerForGet(accessToken, url, String[].class);
    }

    /**
     * Updates the tags for the given Vpin.
     *
     * @param accessToken the access token
     * @param vpin        the Vpin
     * @param tags        the tags
     * @return the updated Vpin
     */
    public VpinCreationResponse.Vpin updateCreateVpinTags(String accessToken, VpinCreationResponse.Vpin vpin,
            String[] tags) {
        String url = apiServerUrl + "/vpin/" + vpin.getId();

        UpdateVpinRequest updateVpinReq = new UpdateVpinRequest();
        updateVpinReq.setTags(Arrays.asList(tags));
        updateVpinReq.setTextContent(vpin.getTextContent());
        updateVpinReq.setDescription("");
        updateVpinReq.setState("PUBLIC");

        return remoteExchangeService.exchangeFromRemoteServer(accessToken, url, HttpMethod.PUT,
                MediaType.APPLICATION_JSON, updateVpinReq, VpinCreationResponse.Vpin.class);
    }
}