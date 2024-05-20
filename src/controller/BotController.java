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

package com.osparks.vpin.bot.controller;

import com.osparks.vpin.bot.dto.BotDashFormData;
import com.osparks.vpin.bot.dto.GenerateRandBotResponse;
import com.osparks.vpin.bot.exceptions.ActionExecuteException;
import com.osparks.vpin.bot.exceptions.OpenAIAPIException;
import com.osparks.vpin.bot.model.BotModel;
import com.osparks.vpin.bot.service.BotCreateService;
import com.osparks.vpin.bot.service.BotService;
import com.osparks.vpin.bot.repository.BotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * BotController handles operations related to bots, such as creation, listing,
 * and starting/stopping bot actions.
 * 
 * @Author Christopher Leu (
 *         <a href= "mailto:chrisleu9@gmail.com">chrisleu9@gmail.com</a>)
 */
@Controller
@RequestMapping("/bots")
public class BotController {
    private final BotCreateService botCreateService;
    private final BotRepository botRepo;
    private final BotService botService;

    @Autowired
    public BotController(BotCreateService botCreateService, BotRepository botRepo, BotService botService) {
        this.botCreateService = botCreateService;
        this.botRepo = botRepo;
        this.botService = botService;
    }

    /**
     * Displays the bot dashboard.
     *
     * @param model the model to pass attributes to the view
     * @return the name of the view to render
     */
    @GetMapping("/statusManager")
    public String botDashboard(Model model) {
        List<BotModel> bots = botRepo.findAll();
        model.addAttribute("bots", bots);
        return "statusManager";
    }

    /**
     * Generates a random bot.
     *
     * @return a GenerateRandBotResponse containing the details of the generated bot
     * @throws OpenAIAPIException if there is an error generating the bot
     */
    @GetMapping("/generateRandBot")
    public @ResponseBody GenerateRandBotResponse generateRandBot() throws OpenAIAPIException {
        return botCreateService.generateRandBot();
    }

    /**
     * Creates a new bot.
     *
     * @param authentication the OAuth2 authentication token
     * @param botData        the data for the new bot
     * @return a ResponseEntity containing the bot data if creation is successful
     * @throws Exception if there is an error creating the bot
     */
    @PostMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity<BotDashFormData> createBot(OAuth2AuthenticationToken authentication,
            @RequestBody BotDashFormData botData) throws Exception {
        BotModel botModel = botCreateService.createBot(authentication, botData);
        if (botModel != null) {
            return new ResponseEntity<>(botData, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Lists all bots.
     *
     * @return a ResponseEntity containing a list of all bots
     */
    @GetMapping(value = "/list", produces = "application/json")
    public @ResponseBody ResponseEntity<List<BotModel>> listBots() {
        List<BotModel> botModels = botRepo.findAll();
        return ResponseEntity.ok(botModels);
    }

    /**
     * Starts a bot action.
     *
     * @param botId the ID of the bot to start
     * @return a ResponseEntity containing a success message
     * @throws Exception if there is an error starting the bot
     */
    @PostMapping("/start/{botId}")
    public @ResponseBody ResponseEntity<String> startBot(@PathVariable String botId) throws Exception {
        try {
            botService.startBot(botId);
        } catch (Exception ex) {
            throw new ActionExecuteException("Failed to manually execute next bot action.");
        }
        return ResponseEntity.ok("Bot action executed successfully");
    }

    /**
     * Stops a bot.
     *
     * @param botId the ID of the bot to stop
     * @return a ResponseEntity containing a success message
     */
    @PostMapping("/stop/{botId}")
    public @ResponseBody ResponseEntity<String> stopBot(@PathVariable String botId) {
        botService.stopBot(botId);
        return ResponseEntity.ok("Bot stopped successfully");
    }

    /**
     * Updates a bot.
     *
     * @param botId the ID of the bot to update
     * @return null (not yet implemented)
     */
    @PutMapping("/edit/{botId}")
    public String updateBot(@PathVariable String botId) {
        return null;
    }
}