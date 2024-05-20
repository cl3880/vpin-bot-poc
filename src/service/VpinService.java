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

import com.osparks.vpin.bot.dto.*;
import com.osparks.vpin.bot.exceptions.ActionExecuteException;
import com.osparks.vpin.bot.models.*;
import com.osparks.vpin.bot.util.IndexCommentPair;
import com.osparks.vpin.bot.util.TagConverter;
import com.osparks.vpin.bot.util.VpinConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Service for handling operations related to Vpins.
 * 
 * @Author Christopher Leu (
 *         <a href= "mailto:chrisleu9@gmail.com">chrisleu9@gmail.com</a>)
 */
@Service
public class VpinService {
    private final RestTemplate restTemplate;
    private final RemoteExchangeService remoteExchangeService;
    private final VpinAuthenticationService vpinAuthenticationService;
    private final OpenAIService openAIService;
    private final InterestService interestService;

    @Autowired
    private SimpMessagingTemplate template;

    @Value("${vpin.server-uri}")
    private String apiStgUrl;

    public VpinService(RestTemplate restTemplate, RemoteExchangeService remoteExchangeService,
            VpinAuthenticationService vpinAuthenticationService, OpenAIService openAIService,
            InterestService interestService) {
        this.restTemplate = restTemplate;
        this.remoteExchangeService = remoteExchangeService;
        this.vpinAuthenticationService = vpinAuthenticationService;
        this.openAIService = openAIService;
        this.interestService = interestService;
    }

    /**
     * Retrieves a list of Vpins.
     *
     * @param tokenResponse the OAuth2 token response
     * @param sort          the sort order
     * @param page          the page number
     * @param pageSize      the page size
     * @return the Vpin list response
     */
    public VpinListResponse getVpinList(OAuth2TokenResponse tokenResponse, String sort, int page, int pageSize) {
        String url = apiStgUrl + "/list?sort=" + sort + "&page=" + page + "&pageSize=" + pageSize;
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(tokenResponse.getAccessToken());
        HttpEntity<String> entity = new HttpEntity<>("body", headers);
        ResponseEntity<VpinListResponse> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<>() {
                });
        return response.getBody();
    }

    /**
     * Retrieves a list of Vpin models.
     *
     * @param tokenResponse the OAuth2 token response
     * @param sort          the sort order
     * @param page          the page number
     * @param pageSize      the page size
     * @return the list of Vpin models
     */
    public List<VpinModel> getVpins(OAuth2TokenResponse tokenResponse, String sort, int page, int pageSize) {
        VpinListResponse vpinListResponse = this.getVpinList(tokenResponse, sort, page, pageSize);

        if (vpinListResponse == null || vpinListResponse.getContent() == null
                || vpinListResponse.getContent().isEmpty()) {
            System.out.println("Empty Vpin List");
            return new ArrayList<>();
        }

        System.out.println("Vpin List retrieved.\n");
        return vpinListResponse.getContent().stream()
                .map(vpinContent -> {
                    if (vpinContent == null || vpinContent.getVpin() == null) {
                        return null;
                    }
                    return VpinConverter.toModel(vpinContent.getVpin());
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves information about Vpins.
     *
     * @param vpinModels the list of Vpin models
     * @return the list of Vpin information maps
     */
    public List<Map<String, Object>> getVpinInfo(List<VpinModel> vpinModels) {
        return vpinModels.stream()
                .map(vpin -> Map.of(
                        "id", vpin.getId(),
                        "textContent", vpin.getTextContent(),
                        "tags", vpin.getTags().stream().map(TagModel::getTag).collect(Collectors.toList())))
                .collect(Collectors.toList());
    }

    /**
     * Converts Vpin information to a text representation.
     *
     * @param vpinInfoList the list of Vpin information maps
     * @return the text representation of Vpin information
     */
    public String convertVpinInfoToText(List<Map<String, Object>> vpinInfoList) {
        return IntStream.range(0, vpinInfoList.size())
                .mapToObj(i -> {
                    Map<String, Object> vpinInfo = vpinInfoList.get(i);
                    String tags = ((List<String>) vpinInfo.get("tags")).stream()
                            .map(tag -> "\"" + tag + "\"")
                            .collect(Collectors.joining(", "));
                    return String.format("%d. title: %s, tags: %s",
                            i, vpinInfo.get("textContent"), tags);
                })
                .collect(Collectors.joining(System.lineSeparator()));
    }

    /**
     * Retrieves a list of tags.
     *
     * @param tokenResponse the OAuth2 token response
     * @return the list of tags
     */
    public List<TagModel> getTags(OAuth2TokenResponse tokenResponse) {
        String url = apiStgUrl + "/tag";
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(tokenResponse.getAccessToken());
        HttpEntity<String> entity = new HttpEntity<>("body", headers);
        ResponseEntity<TagResponse> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<>() {
                });
        return response.getBody().getContent().stream()
                .map(TagConverter::toModel)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves a list of Vpins by tags.
     *
     * @param tag the tag to filter Vpins by
     * @return the list of Vpins filtered by the given tag
     */
    public List<VpinModel> getVpinsByTags(String tag) {
        String url = apiStgUrl + "/list?tags=" + tag;
        OAuth2TokenResponse tokenResponse = vpinAuthenticationService.getClientToken();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(tokenResponse.getAccessToken());
        HttpEntity<String> entity = new HttpEntity<>("body", headers);
        ResponseEntity<VpinListResponse> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<>() {
                });
        return Objects.requireNonNull(response.getBody()).getContent().stream()
                .map(vpinContent -> VpinConverter.toModel(vpinContent.getVpin()))
                .collect(Collectors.toList());
    }

    /**
     * Retrieves a list of recommended Vpins based on a given Vpin ID.
     *
     * @param accessToken the access token
     * @param vpinId      the ID of the Vpin
     * @return the list of recommended Vpins
     */
    public List<VpinModel> getRecommendedVpins(String accessToken, String vpinId) {
        String url = apiStgUrl + "/vpin/" + vpinId + "/my/recommendation/top20";
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        System.out.println("Getting Recommended Vpins based on Vpin ID: " + vpinId);
        template.convertAndSend("/topic/botlogs", "Retrieving recommended Vpins based on last vpin interacted.\n");
        HttpEntity<String> entity = new HttpEntity<>("body", headers);

        ResponseEntity<List<VpinListResponse.VpinContent>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<>() {
                });

        return Objects.requireNonNull(response.getBody()).stream()
                .map(vpinContent -> VpinConverter.toModel(vpinContent.getVpin()))
                .collect(Collectors.toList());
    }

    /**
     * Retrieves comments for a given Vpin ID.
     *
     * @param vpinId the ID of the Vpin
     * @return the Vpin comment response
     */
    public VpinCommentResponse getComments(String vpinId) {
        String url = apiStgUrl + "/comment/vpin/" + vpinId;
        OAuth2TokenResponse tokenResponse = vpinAuthenticationService.getClientToken();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(tokenResponse.getAccessToken());
        HttpEntity<String> entity = new HttpEntity<>("body", headers);
        ResponseEntity<VpinCommentResponse> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<>() {
                });
        return response.getBody();
    }

    /**
     * Generates a reply to a Vpin.
     *
     * @param botModel the bot model
     * @param vpinId   the ID of the Vpin
     * @return the generated reply
     */
    public String getVpinReply(BotModel botModel, String vpinId) {
        String url = apiStgUrl + "/vpin/" + vpinId;
        OAuth2TokenResponse tokenResponse = vpinAuthenticationService.getClientToken();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(tokenResponse.getAccessToken());
        HttpEntity<String> entity = new HttpEntity<>("body", headers);
        ResponseEntity<VpinModel> responseVpin = restTemplate.exchange(url, HttpMethod.GET, entity, VpinModel.class);
        VpinModel vpinModel = responseVpin.getBody();
        String interests = interestService.getInterests(botModel);
        assert vpinModel != null;
        String vpinTitle = vpinModel.getTextContent();
        String vpinTags = vpinModel.getTags().stream().map(TagModel::getTag).collect(Collectors.joining(", "));
        String vpinInfo = "VpinModel Title: " + vpinTitle + "\n VpinModel Tags: " + vpinTags;

        OpenAIGeneralResponse response = openAIService.commentVpin(botModel, interests, vpinInfo);
        System.out.println(response.getChoices().get(0).getMessage().getContent());
        return response.getChoices().get(0).getMessage().getContent();
    }

    /**
     * Retrieves comments for a given Vpin model.
     *
     * @param vpinModel the Vpin model
     * @return the Vpin comment response
     */
    public VpinCommentResponse getVpinComments(VpinModel vpinModel) {
        System.out.println(
                "Vpin: " + vpinModel.getId() + " has commentCount of " + vpinModel.getCommentCount().toString());
        if (vpinModel.getCommentCount() > 0) {
            VpinCommentResponse vpinCommentResponse = this.getComments(vpinModel.getId());
            System.out.println("Comments under VpinModel: " + vpinModel.getId() + "\n"
                    + new ArrayList<>(vpinCommentResponse.getContent()));
            return vpinCommentResponse;
        }
        return null;
    }

    /**
     * Generates a reply to a comment on a Vpin.
     *
     * @param botModel            the bot model
     * @param vpinCommentResponse the Vpin comment response
     * @return the index and comment pair
     */
    public IndexCommentPair getReplyToVpinComment(BotModel botModel, VpinCommentResponse vpinCommentResponse) {
        if (vpinCommentResponse != null) {
            List<String> comments = vpinCommentResponse.getContent().stream()
                    .map(VpinCommentResponse.CommentContent::getTextContent)
                    .collect(Collectors.toList());

            String commentString = IntStream.range(0, comments.size())
                    .mapToObj(i -> (i + 1) + ". " + comments.get(i))
                    .collect(Collectors.joining("\n"));

            String interests = botModel.getInterests().stream().map(InterestModel::getInterest)
                    .collect(Collectors.joining(", "));
            OpenAIGeneralResponse response = openAIService.replyComment(botModel, interests, commentString);
            System.out.println(response.getChoices().get(0).getMessage().getContent());
            IndexCommentPair parsedResponse = parseResponse(response.getChoices().get(0).getMessage().getContent());
            System.out.print("getReplyToVpinComment: IndexCommentPair.getComment() - " + parsedResponse.getComment());
            System.out.println("getReplyToVpinComment: IndexCommentPair.getIndex() - " + parsedResponse.getIndex());
            return parsedResponse;
        }
        return null;
    }

    /**
     * Parses the response to extract the index and comment.
     *
     * @param response the response string
     * @return the index and comment pair
     */
    private IndexCommentPair parseResponse(String response) {
        String[] splitResponse = response.split(" - ", 2);
        String[] excessResponse = splitResponse[1].split("\n");
        System.out.println("OpenAI returned response: " + response);
        if (splitResponse.length != 2) {
            throw new IllegalArgumentException("Invalid response format: " + response);
        }

        int index = Integer.parseInt(splitResponse[0].trim()) - 1;
        String comment = excessResponse[0].trim();
        return new IndexCommentPair(index, comment);
    }

    /**
     * Retrieves the comment ID based on the index.
     *
     * @param openAIReturnedIndex the index returned by OpenAI
     * @param vpinCommentResponse the Vpin comment response
     * @return the comment ID
     */
    public String getCommentId(int openAIReturnedIndex, VpinCommentResponse vpinCommentResponse) {
        List<String> commentIds = vpinCommentResponse.getContent().stream()
                .map(VpinCommentResponse.CommentContent::getId)
                .collect(Collectors.toList());
        System.out.println("Comment Ids: " + commentIds.stream() + ". GPT returned index: " + openAIReturnedIndex);

        if (openAIReturnedIndex < 0 || openAIReturnedIndex >= commentIds.size()) {
            throw new IllegalArgumentException("Invalid index: " + openAIReturnedIndex);
        }

        return commentIds.get(openAIReturnedIndex);
    }

    /**
     * Likes a Vpin.
     *
     * @param accessToken the access token
     * @param vpinId      the ID of the Vpin
     */
    public void likeVpin(String accessToken, String vpinId) {
        String url = apiStgUrl + "/vpin/" + vpinId + "/like";
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        HttpEntity<?> entity = new HttpEntity<>(headers);
        try {
            restTemplate.exchange(url, HttpMethod.POST, entity, Void.class, vpinId);
            System.out.println("Vpin id: " + vpinId + " successfully liked.\n");
            template.convertAndSend("/topic/botlogs", "Vpin id: " + vpinId + " liked successfully.\n");
        } catch (ActionExecuteException e) {
            throw new ActionExecuteException("Error liking Vpin.");
        }
    }

    /**
     * Comments on a Vpin.
     *
     * @param accessToken the access token
     * @param vpinId      the ID of the Vpin
     * @param botComment  the comment to be posted
     */
    public void commentVpin(String accessToken, String vpinId, String botComment) {
        String url = apiStgUrl + "/comment/vpin/" + vpinId;
        Map<String, Object> comment = new HashMap<>();
        comment.put("textContent", botComment);
        System.out.println("Replying to vpin: " + vpinId + ", with reply: " + botComment);
        template.convertAndSend("/topic/botLogs", "Commenting on Vpin: " + vpinId + ", with reply: " + botComment);
        try {
            remoteExchangeService.exchangeFromRemoteServer(accessToken, url, HttpMethod.POST,
                    MediaType.APPLICATION_JSON, comment, Void.class);
            template.convertAndSend("/topic/botlogs", "Successfully commented on Vpin.\n");
        } catch (ActionExecuteException e) {
            throw new ActionExecuteException("Error commenting on VpinModel.");
        }
    }

    /**
     * Replies to a comment.
     *
     * @param accessToken the access token
     * @param commentId   the ID of the comment
     * @param botComment  the reply to be posted
     */
    public void replyComment(String accessToken, String commentId, String botComment) {
        String url = apiStgUrl + "/comment/" + commentId;
        Map<String, Object> comment = new HashMap<>();
        comment.put("textContent", botComment);
        System.out.println("Replying to comment: " + commentId + ", with reply: " + botComment);
        template.convertAndSend("/topic/botlogs", "Replying to comment: " + commentId + ", with reply: " + botComment);
        try {
            remoteExchangeService.exchangeFromRemoteServer(accessToken, url, HttpMethod.POST,
                    MediaType.APPLICATION_JSON, comment, Void.class);
        } catch (ActionExecuteException e) {
            throw new ActionExecuteException("Error replying to comment.");
        }
    }
}