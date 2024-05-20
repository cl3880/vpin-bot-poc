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

package com.osparks.vpin.bot.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

import java.util.UUID;

/**
 * @Author Christopher Leu (
 *         <a href= "mailto:chrisleu9@gmail.com">chrisleu9@gmail.com</a>)
 */
@Node("BotFunction")
public class BotFunction {
    @Id
    private final String id;
    private String function;

    public BotFunction(@JsonProperty("id") String id,
            @JsonProperty("functions") String function) {
        this.id = (id != null) ? id : UUID.randomUUID().toString();
        this.function = function;
    }

    public String getId() {
        return id;
    }

    public String getFunction() {
        return function;
    }

    public void setFunction(String function) {
        this.function = function;
    }
}
