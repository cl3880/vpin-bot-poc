/**
 * Copyright (c) 2024 Osparks AMG Inc. All rights reserved.
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

package com.osparks.vpin.bot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author Christopher Leu ( <a href=
 *         "mailto:chrisleu9@gmail.com">chrisleu9@gmail.com</a>)
 */
@SpringBootApplication
@EnableScheduling
public class VpinBotApplication {
    public static void main(String[] args) {

        SpringApplication.run(VpinBotApplication.class, args);
    }
}
