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

import com.osparks.vpin.bot.config.BrowseBehaviorConfig;
import com.osparks.vpin.bot.model.BotFunction;
import com.osparks.vpin.bot.model.BotModel;
import com.osparks.vpin.bot.model.VpinModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service to execute bot actions asynchronously.
 * 
 * @Author Christopher Leu (
 *         <a href= "mailto:chrisleu9@gmail.com">chrisleu9@gmail.com</a>)
 */
@Service
public class BotActionExecutor {
    private final BrowseBehaviorConfig browseBehaviorConfig;
    private final BotCommandService botCommandService;
    @Autowired
    private SimpMessagingTemplate template;

    @Autowired
    public BotActionExecutor(BrowseBehaviorConfig browseBehaviorConfig, BotCommandService botCommandService) {
        this.browseBehaviorConfig = browseBehaviorConfig;
        this.botCommandService = botCommandService;
    }

    /**
     * Executes actions for the given bot asynchronously.
     *
     * @param bot the bot for which to execute actions
     * @throws Exception if an error occurs during action execution
     */
    @Async("taskExecutor")
    public void executeAction(BotModel bot) throws Exception {
        List<BotFunction> functions = bot.getFunctions();

        try {
            bot.setActive(true);
            template.convertAndSend("/topic/botstatus", bot.getId() + ":Online");
            for (BotFunction function : functions) {
                if (bot.shouldStop()) {
                    break;
                }
                executeFunction(bot, function);
            }
        } finally {
            bot.setActive(false);
            template.convertAndSend("/topic/botstatus", bot.getId() + ":Offline");
        }
    }

    private void executeFunction(BotModel bot, BotFunction function) throws Exception {
        if (function.getFunction().equalsIgnoreCase("CREATE_VPIN")) {
            botCommandService.executeCommands(bot, null);
        } else {
            VpinModel selectedVpin = browseBehaviorConfig.determineBrowseStrategy(bot, "READS", 5);
            if (selectedVpin != null) {
                botCommandService.executeCommands(bot, selectedVpin);
            }
        }
    }
}