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
import com.osparks.vpin.bot.dao.BotRepository;
import com.osparks.vpin.bot.model.BotFunction;
import com.osparks.vpin.bot.model.BotModel;
import com.osparks.vpin.bot.model.VpinModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Service for managing and executing bot commands.
 * 
 * @Author Christopher Leu (
 *         <a href= "mailto:chrisleu9@gmail.com">chrisleu9@gmail.com</a>)
 */
@Service
public class BotCommandService {
    private final Map<String, BotCommand> botCommands = new HashMap<>();
    private final BrowseBehaviorConfig browseBehaviorConfig;
    private final BotRepository botRepo;

    @Autowired
    public BotCommandService(List<BotCommand> commandList, BrowseBehaviorConfig browseBehaviorConfig,
            BotRepository botRepo) {
        for (BotCommand command : commandList) {
            botCommands.put(command.getCommandName(), command);
        }
        this.browseBehaviorConfig = browseBehaviorConfig;
        this.botRepo = botRepo;
    }

    /**
     * Executes the commands for the given bot and Vpin.
     *
     * @param bot                     the bot model
     * @param vpinModelToInteractWith the Vpin model to interact with
     * @throws Exception if an error occurs during command execution
     */
    public void executeCommands(BotModel bot, VpinModel vpinModelToInteractWith) throws Exception {
        for (BotFunction function : bot.getFunctions()) {
            if (bot.shouldStop()) {
                break;
            }
            BotCommand command = getCommand(function);
            command.execute(bot, vpinModelToInteractWith);
            vpinModelToInteractWith = command.getInteractedVpin();
        }
    }

    /**
     * Retrieves the commands for the given bot.
     *
     * @param botModel the bot model
     * @return a list of bot commands
     */
    public List<BotCommand> getCommands(BotModel botModel) {
        List<BotCommand> commands = new ArrayList<>();
        for (BotFunction function : botModel.getFunctions()) {
            BotCommand command = botCommands.get(function.getFunction().toUpperCase());
            if (command != null) {
                commands.add(command);
            }
        }
        return commands;
    }

    /**
     * Retrieves a command based on the bot function.
     *
     * @param function the bot function
     * @return the corresponding bot command
     * @throws IllegalStateException if no command is found for the function
     */
    private BotCommand getCommand(BotFunction function) {
        BotCommand command = botCommands.get(function.getFunction().toUpperCase());
        if (command == null) {
            throw new IllegalStateException("No command found for function: " + function.getFunction());
        }
        return command;
    }
}