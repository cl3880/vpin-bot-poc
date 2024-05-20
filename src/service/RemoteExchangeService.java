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

import com.osparks.vpin.bot.dto.OAuth2TokenResponse;
import org.springframework.http.*;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

/**
 * Service for making remote server exchanges with OAuth2 authentication.
 * 
 * @Author Christopher Leu (
 *         <a href= "mailto:chrisleu9@gmail.com">chrisleu9@gmail.com</a>)
 */
@Service
public class RemoteExchangeService {

    private final OAuth2AuthorizedClientService authorizedClientService;
    private final RestTemplate restTemplate;

    public RemoteExchangeService(OAuth2AuthorizedClientService authorizedClientService, RestTemplate restTemplate) {
        this.authorizedClientService = authorizedClientService;
        this.restTemplate = restTemplate;
    }

    /**
     * Makes a GET request to a remote server using an access token.
     *
     * @param accessToken   the access token
     * @param url           the URL to request
     * @param responseClass the class type of the response
     * @param <T>           the type of the response
     * @return the response from the server
     * @throws IOException if an error occurs during the request
     */
    public <T> T exchangeFromRemoteServerForGet(String accessToken, String url, Class<T> responseClass)
            throws IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        HttpEntity<?> entity = new HttpEntity<>(headers);

        ResponseEntity<T> response = restTemplate.exchange(url, HttpMethod.GET, entity, responseClass);
        if (response.getStatusCode().is2xxSuccessful()) {
            return response.getBody();
        } else {
            throw new IOException("Failed to retrieve data from " + url);
        }
    }

    /**
     * Makes a request to a remote server with form parameters.
     *
     * @param accessToken   the access token
     * @param url           the URL to request
     * @param method        the HTTP method
     * @param mediaType     the media type of the request
     * @param formParams    the form parameters
     * @param responseClass the class type of the response
     * @param <T>           the type of the response
     * @return the response from the server
     * @throws IOException if an error occurs during the request
     */
    public <T> T exchangeFromRemoteServerWithFormParams(String accessToken, String url, HttpMethod method,
            MediaType mediaType, MultiValueMap<String, Object> formParams, Class<T> responseClass) throws IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(mediaType);
        headers.setBearerAuth(accessToken);

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(formParams, headers);

        ResponseEntity<T> response = restTemplate.exchange(url, method, requestEntity, responseClass);

        if (response.getStatusCode().is2xxSuccessful()) {
            return response.getBody();
        } else {
            throw new IOException("Failed to post data to " + url);
        }
    }

    /**
     * Makes a request to a remote server using an OAuth2 token response.
     *
     * @param tokenResponse the OAuth2 token response
     * @param uri           the URI to request
     * @param method        the HTTP method
     * @param mediaType     the media type of the request
     * @param formParams    the form parameters
     * @param className     the class type of the response
     * @param <T>           the type of the response
     * @return the response from the server
     */
    public <T> T exchangeFromRemoteServerWithTokenResponse(OAuth2TokenResponse tokenResponse, String uri,
            HttpMethod method, MediaType mediaType, MultiValueMap<String, String> formParams, Class<T> className) {
        String accessToken = tokenResponse.getAccessToken();
        return exchangeFromRemoteServer(accessToken, uri, method, mediaType, formParams, className);
    }

    /**
     * Makes a request to a remote server using an OAuth2 authentication token.
     *
     * @param authenticationToken the OAuth2 authentication token
     * @param uri                 the URI to request
     * @param method              the HTTP method
     * @param mediaType           the media type of the request
     * @param formParams          the form parameters
     * @param className           the class type of the response
     * @param <T>                 the type of the response
     * @return the response from the server
     */
    public <T> T exchangeFromRemoteServerWithOAuth2AuthenticationToken(OAuth2AuthenticationToken authenticationToken,
            String uri, HttpMethod method, MediaType mediaType, MultiValueMap<String, String> formParams,
            Class<T> className) {
        OAuth2AuthorizedClient client = authorizedClientService.loadAuthorizedClient(
                authenticationToken.getAuthorizedClientRegistrationId(), authenticationToken.getName());
        String accessToken = client.getAccessToken().getTokenValue();
        return exchangeFromRemoteServer(accessToken, uri, method, mediaType, formParams, className);
    }

    /**
     * Makes a request to a remote server.
     *
     * @param accessToken    the access token
     * @param uri            the URI to request
     * @param method         the HTTP method
     * @param mediaType      the media type of the request
     * @param requestPayload the request payload
     * @param responseClass  the class type of the response
     * @param <T>            the type of the response
     * @param <R>            the type of the request payload
     * @return the response from the server
     */
    public <T, R> T exchangeFromRemoteServer(String accessToken, String uri, HttpMethod method, MediaType mediaType,
            R requestPayload, Class<T> responseClass) {
        HttpHeaders headers = new HttpHeaders();
        headers.setCacheControl(CacheControl.noCache());
        headers.setContentType(mediaType != null ? mediaType : MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + accessToken);

        ResponseEntity<T> response = restTemplate.exchange(uri, method, new HttpEntity<>(requestPayload, headers),
                responseClass);
        if (response.getStatusCode() == HttpStatus.OK) {
            return response.getBody();
        }
        return null;
    }
}