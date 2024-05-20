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
import com.osparks.vpin.bot.model.CommentedOnRelationship;
import com.osparks.vpin.bot.model.VpinModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

/**
 * @Author Christopher Leu (
 *         <a href= "mailto:chrisleu9@gmail.com">chrisleu9@gmail.com</a>)
 */
@Service
@Qualifier("COMMENT")
public class CommentVpinCommand implements BotCommand {
    private static final Logger log = LoggerFactory.getLogger(CommentVpinCommand.class);
    private final VpinAuthenticationService vpinAuthenticationService;
    private final VpinService vpinService;
    private final BotRepository botRepo;
    private VpinModel lastInteractedVpinModel;
    @Autowired
    private SimpMessagingTemplate template;

    public CommentVpinCommand(VpinAuthenticationService vpinAuthenticationService, VpinService vpinService,
            BotRepository botRepo) {
        this.vpinAuthenticationService = vpinAuthenticationService;
        this.vpinService = vpinService;
        this.botRepo = botRepo;
    }

    @Override
    public void execute(BotModel bot, VpinModel vpin) throws Exception {
        BotLoginResponse loginResponse = vpinAuthenticationService.botLogin(bot);
        String accessToken = loginResponse.getAccess_token();
        String botComment = vpinService.getVpinReply(bot, vpin.getId());

        if (vpin.getId() != null && !vpin.getId().isEmpty()) {
            performComment(bot, vpin, accessToken, botComment);
        }
    }

    private void performComment(BotModel bot, VpinModel vpin, String accessToken, String botComment) {
        try {
            template.convertAndSend("/topic/botlogs",
                    "Commenting on Vpin: " + vpin.getId() + " with comment: " + botComment);
            vpinService.commentVpin(accessToken, vpin.getId(), botComment);
            lastInteractedVpinModel = vpin;
            updateBotCommentedOnRelationships(bot);
        } catch (ActionExecuteException e) {
            log.error("Error commenting on the VpinModel: ", e);
        }
    }

    private void updateBotCommentedOnRelationships(BotModel bot) {
        CommentedOnRelationship commentedRel = new CommentedOnRelationship();
        commentedRel.setVpin(lastInteractedVpinModel);
        bot.getCommentedOnRelationships().add(commentedRel);
        bot.setLastInteractedVpinId(lastInteractedVpinModel.getId());
        botRepo.save(bot);
    }

    @Override
    public String getCommandName() {
        return "COMMENT";
    }

    @Override
    public VpinModel getInteractedVpin() {
        return lastInteractedVpinModel;
    }
}