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

import com.osparks.vpin.bot.service.VpinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * VpinController handles operations related to browsing Vpins.
 * 
 * @Author Christopher Leu (
 *         <a href= "mailto:chrisleu9@gmail.com">chrisleu9@gmail.com</a>)
 */
@RestController
@RequestMapping("/vpin/browse")
public class VpinController {

    private final OAuth2AuthorizedClientService authorizedClientService;
    private final VpinService vpinService;

    @Autowired
    public VpinController(OAuth2AuthorizedClientService authorizedClientService, VpinService vpinService) {
        this.authorizedClientService = authorizedClientService;
        this.vpinService = vpinService;
    }
}