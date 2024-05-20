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
import com.osparks.vpin.bot.dto.VpinCommentResponse;
import com.osparks.vpin.bot.exceptions.ActionExecuteException;
import com.osparks.vpin.bot.model.BotModel;
import com.osparks.vpin.bot.model.CommentModel;
import com.osparks.vpin.bot.model.InteractedWCommentUnderRelationship;
import com.osparks.vpin.bot.model.RepliedToRelationship;
import com.osparks.vpin.bot.model.VpinModel;
import com.osparks.vpin.bot.util.IndexCommentPair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

/**
 * Command to reply to a comment on a Vpin.
 * 
 * @Author Christopher Leu (
 *         <a href= "mailto:chrisleu9@gmail.com">chrisleu9@gmail.com</a>)
 */
@Service
@Qualifier("REPLY")
public class ReplyCommentCommand implements BotCommand {
    private static final Logger log = LoggerFactory.getLogger(ReplyCommentCommand.class);
    private final VpinAuthenticationService vpinAuthenticationService;
    private final VpinService vpinService;
    private final BotRepository botRepo;
    private VpinModel lastInteractedVpinModel;
    @Value("REPLY")
    private String actionType;
    @Autowired
    private SimpMessagingTemplate template;

    public ReplyCommentCommand(VpinAuthenticationService vpinAuthenticationService, VpinService vpinService,
            BotRepository botRepo) {
        this.vpinAuthenticationService = vpinAuthenticationService;
        this.vpinService = vpinService;
        this.botRepo = botRepo;
    }

    /**
     * Executes the reply command on a Vpin.
     *
     * @param bot  the bot model
     * @param vpin the Vpin model
     * @throws Exception if an error occurs during execution
     */
    @Override
    public void execute(BotModel bot, VpinModel vpin) throws Exception {
        BotLoginResponse loginResponse = vpinAuthenticationService.botLogin(bot);
        String accessToken = loginResponse.getAccess_token();
        VpinCommentResponse vpinComments = vpinService.getVpinComments(vpin);

        if (vpinComments == null) {
            commentOnEmptyVpin(bot, vpin, accessToken);
        } else {
            replyToExistingComments(bot, vpin, accessToken, vpinComments);
        }
    }

    private void commentOnEmptyVpin(BotModel bot, VpinModel vpin, String accessToken) throws Exception {
        String vpinReply = vpinService.getVpinReply(bot, vpin.getId());
        vpinService.commentVpin(accessToken, vpin.getId(), vpinReply);
        actionType = "COMMENT";
        CommentedOnRelationship commentedRel = new CommentedOnRelationship();
        commentedRel.setVpin(vpin);
        bot.getCommentedOnRelationships().add(commentedRel);
        botRepo.save(bot);
        template.convertAndSend("/topic/botlogs",
                "Commenting on Vpin: " + vpin.getId() + ", since Vpin has no comments: " + vpin.getCommentCount());
    }

    private void replyToExistingComments(BotModel bot, VpinModel vpin, String accessToken,
            VpinCommentResponse vpinComments) {
        try {
            IndexCommentPair commentReplyPair = vpinService.getReplyToVpinComment(bot, vpinComments);
            String commentIdStr = vpinService.getCommentId(commentReplyPair.getIndex(), vpinComments);
            String commentReply = commentReplyPair.getComment();

            vpinService.replyComment(accessToken, commentIdStr, commentReply);

            updateBotReplyRelationships(bot, vpin, commentIdStr);
        } catch (ActionExecuteException e) {
            log.error("Error replying to the comment: ", e);
        }
    }

    private void updateBotReplyRelationships(BotModel bot, VpinModel vpin, String commentIdStr) {
        lastInteractedVpinModel = vpin;
        InteractedWCommentUnderRelationship commentInteraction = new InteractedWCommentUnderRelationship();
        commentInteraction.setVpin(vpin);
        bot.getInteractedWithCommentUnderRelationships().add(commentInteraction);

        RepliedToRelationship repliedRel = new RepliedToRelationship();
        CommentModel commentModel = new CommentModel();
        commentModel.setId(commentIdStr);
        repliedRel.setComment(commentModel);

        template.convertAndSend("/topic/botlogs", "Successfully replied to comment under Vpin.\n");
        bot.getRepliedToRelationships().add(repliedRel);
        bot.setLastInteractedVpinId(vpin.getId());
        botRepo.save(bot);
    }

    @Override
    public String getCommandName() {
        return actionType;
    }

    @Override
    public VpinModel getInteractedVpin() {
        return lastInteractedVpinModel;
    }
}