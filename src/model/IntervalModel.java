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

import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

import java.time.LocalTime;
import java.util.UUID;

/**
 * @Author Christopher Leu (
 *         <a href= "mailto:chrisleu9@gmail.com">chrisleu9@gmail.com</a>)
 */
@Node("Interval")
public class IntervalModel {
    @Id
    private String id;
    private LocalTime startTime;
    private LocalTime endTime;
    private int actionsPerHour;

    public IntervalModel(LocalTime startTime, LocalTime endTime, int actionsPerHour) {
        this.id = UUID.randomUUID().toString();
        this.startTime = startTime;
        this.endTime = endTime;
        this.actionsPerHour = actionsPerHour;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public int getActionsPerHour() {
        return actionsPerHour;
    }

    public void setActionsPerHour(int actionsPerHour) {
        this.actionsPerHour = actionsPerHour;
    }
}
