/**
 * Copyright (c) 2023 Osparks AMG Inc. All rights reserved.
 * 
 * All information and code contained herein is the property of Osparks AMG Inc.
 * 
 * Permission is granted to view this material for personal use only.
 * 
 * Any unauthorized modification, publication, distribution, sublicensing, or sale of this
 * material without written permission from Osparks AMG Inc. is strictly prohibited.
 * 
 * Employers who have received this software through a job application process
 * are granted full access to view, modify, distribute, and use the software for
 * evaluation purposes only.
 */

package com.osparks.vpin.bot.service.browseBehaviors;

import com.osparks.vpin.bot.dto.OAuth2TokenResponse;
import com.osparks.vpin.bot.dto.OpenAIGeneralResponse;
import com.osparks.vpin.bot.models.BotModel;
import com.osparks.vpin.bot.models.VpinModel;
import com.osparks.vpin.bot.service.*;
import com.osparks.vpin.bot.util.BrowseVpinPagination;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Service for browsing Vpins.
 * 
 * @Author Christopher Leu (
 *         <a href= "mailto:chrisleu9@gmail.com">chrisleu9@gmail.com</a>)
 */
@Service
public class BrowseVpinsBehavior extends BrowseBehavior {

    private final VpinService vpinService;
    private final InterestService interestService;
    private final OpenAIService openAIService;
    private final VpinAuthenticationService vpinAuthenticationService;
    @Autowired
    private SimpMessagingTemplate template;

    @Autowired
    public BrowseVpinsBehavior(BrowseVpinPagination browseVpinPagination, VpinService vpinService,
                               InterestService interestService, OpenAIService openAIService,
                               VpinAuthenticationService vpinAuthenticationService) {
        super(browseVpinPagination);
        this.vpinService = vpinService;
        this.interestService = interestService;
        this.openAIService = openAIService;
        this.vpinAuthenticationService = vpinAuthenticationService;
    }

    /**
     * Browses Vpins based on the specified sort criteria and page size.
     * 
     * @param botModel the bot model
     * @param sort the sort criteria
     * @param pageSize the number of Vpins per page
     * @return the selected VpinModel
     */
    @Override
    public VpinModel browse(BotModel botModel, String sort, int pageSize) {
        int maxAttempts = 15;
        int attempts = 0;

        while (attempts < maxAttempts) {
            List<VpinModel> vpinList = fetchVpins(sort, pageSize);
            String vpinInfoString = convertVpinsToString(vpinList);
            String interests = getInterests(botModel);
            String vpinIndex = getOpenAIComparison(botModel, interests, vpinInfoString).toLowerCase();

            if (!"none".equals(vpinIndex)) {
                return fetchVpinFromSelectedIndex(vpinList, vpinIndex, vpinInfoString);
            } else {
                template.convertAndSend("/topic/botlogs", "Page " + browseVpinPagination.getCurrentPage() + " resulted in no interesting Vpins. Continuing to next page.\n");
                browseVpinPagination.incrementPage();
                attempts++;
            }
        }

        return attempts == 0 ? browseVpinPagination.getLastInteractedVpin() : null;
    }

    /**
     * Fetches Vpins from the service.
     * 
     * @param sort the sort criteria
     * @param pageSize the number of Vpins per page
     * @return a list of VpinModel
     */
    private List<VpinModel> fetchVpins(String sort, int pageSize) {
        int page = browseVpinPagination.getCurrentPage();
        OAuth2TokenResponse tokenResponse = vpinAuthenticationService.getClientToken();
        return vpinService.getVpins(tokenResponse, sort, page, pageSize);
    }

    /**
     * Converts a list of Vpins to a string representation.
     * 
     * @param vpinModelList the list of Vpins
     * @return the string representation
     */
    private String convertVpinsToString(List<VpinModel> vpinModelList) {
        List<Map<String, Object>> vpinInfoList = vpinService.getVpinInfo(vpinModelList);
        return vpinService.convertVpinInfoToText(vpinInfoList);
    }

    /**
     * Retrieves the interests of the bot.
     * 
     * @param botModel the bot model
     * @return the interests string
     */
    private String getInterests(BotModel botModel) {
        return interestService.getInterests(botModel);
    }

    /**
     * Gets a comparison from OpenAI service.
     * 
     * @param botModel the bot model
     * @param interests the bot's interests
     * @param vpinInfoString the Vpin information string
     * @return the comparison result
     */
    private String getOpenAIComparison(BotModel botModel, String interests, String vpinInfoString) {
        OpenAIGeneralResponse response = openAIService.browseVpins(botModel, interests, vpinInfoString);
        return response.getChoices().get(0).getMessage().getContent();
    }

    /**
     * Fetches the Vpin from the selected index.
     * 
     * @param vpinList the list of Vpins
     * @param vpinIndex the index string
     * @param vpinInfoString the Vpin information string
     * @return the selected VpinModel
     */
    private VpinModel fetchVpinFromSelectedIndex(List<VpinModel> vpinList, String vpinIndex, String vpinInfoString) {
        int index = getIndexFromResponse(vpinIndex, vpinInfoString);
        return index != -1 ? vpinList.get(index) : null;
    }
}