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
import com.osparks.vpin.bot.dto.BotLoginResponse;
import com.osparks.vpin.bot.exceptions.ActionExecuteException;
import com.osparks.vpin.bot.model.BotModel;
import com.osparks.vpin.bot.model.LikedRelationship;
import com.osparks.vpin.bot.model.VpinModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * @Author Christopher Leu (
 *         <a href= "mailto:chrisleu9@gmail.com">chrisleu9@gmail.com</a>)
 */
@Service
@Qualifier("LIKE")
public class LikeCommand implements BotCommand {
    private static final Logger log = LoggerFactory.getLogger(LikeCommand.class);
    private final VpinAuthenticationService vpinAuthenticationService;
    private final VpinService vpinService;
    private final BotRepository botRepo;
    private VpinModel lastInteractedVpin;

    public LikeCommand(VpinService vpinService, VpinAuthenticationService vpinAuthenticationService,
            BotRepository botRepo) {
        this.vpinService = vpinService;
        this.vpinAuthenticationService = vpinAuthenticationService;
        this.botRepo = botRepo;
    }

    @Override
    public void execute(BotModel bot, VpinModel vpin) throws Exception {
        BotLoginResponse loginResponse = vpinAuthenticationService.botLogin(bot);
        String accessToken = loginResponse.getAccess_token();
        if (vpin.getId() != null && !vpin.getId().isEmpty()) {
            performLike(bot, vpin, accessToken);
        }
    }

    private void performLike(BotModel bot, VpinModel vpin, String accessToken) {
        try {
            vpinService.likeVpin(accessToken, vpin.getId());
            lastInteractedVpin = vpin;
            updateBotLikedRelationships(bot);
        } catch (ActionExecuteException e) {
            log.error("Error liking the Vpin: ", e);
        }
    }

    private void updateBotLikedRelationships(BotModel bot) {
        LikedRelationship likedRelationshipRel = new LikedRelationship();
        likedRelationshipRel.setVpin(lastInteractedVpin);
        bot.getLikedRelationships().add(likedRelationshipRel);
        bot.setLastInteractedVpinId(lastInteractedVpin.getId());
        botRepo.save(bot);
    }

    @Override
    public String getCommandName() {
        return "LIKE";
    }

    @Override
    public VpinModel getInteractedVpin() {
        return lastInteractedVpin;
    }
}