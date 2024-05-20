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

package com.osparks.vpin.bot.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.osparks.vpin.bot.model.BotFunction;
import com.osparks.vpin.bot.model.InterestModel;

import java.util.List;

/**
 * @Author Christopher Leu (
 *         <a href= "mailto:chrisleu9@gmail.com">chrisleu9@gmail.com</a>)
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class BotDashFormData {
    @JsonProperty("interests")
    private List<InterestModel> interests;
    private String name;
    private String location;
    private String occupation;
    private String nickname;
    private String username;
    private String gender;
    private String locale;
    private int age;
    private List<IntervalDTO> intervals;
    private List<BotFunction> functions;

    public BotDashFormData(@JsonProperty("name") String name,
            @JsonProperty("location") String location,
            @JsonProperty("occupation") String occupation,
            @JsonProperty("nickname") String nickname,
            @JsonProperty("username") String username,
            @JsonProperty("gender") String gender,
            @JsonProperty("locale") String locale,
            @JsonProperty("age") int age,
            @JsonProperty("interests") List<InterestModel> interests,
            @JsonProperty("schedule") List<IntervalDTO> intervals,
            @JsonProperty("functions") List<BotFunction> functions) {
        this.name = name;
        this.location = location;
        this.occupation = occupation;
        this.nickname = nickname;
        this.username = username;
        this.gender = gender;
        this.locale = locale;
        this.age = age;
        this.interests = interests;
        this.intervals = intervals;
        this.functions = functions;
    }

    /**
     * @return List<InterestModel>
     */
    public List<InterestModel> getInterests() {
        return interests;
    }

    /**
     * @param interests
     */
    public void setInterests(List<InterestModel> interests) {
        this.interests = interests;
    }

    /**
     * @return String
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return String
     */
    public String getLocation() {
        return location;
    }

    /**
     * @param location
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * @return String
     */
    public String getOccupation() {
        return occupation;
    }

    /**
     * @param occupation
     */
    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    /**
     * @return String
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * @param nickname
     */
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    /**
     * @return String
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return String
     */
    public String getGender() {
        return gender;
    }

    /**
     * @param gender
     */
    public void setGender(String gender) {
        this.gender = gender;
    }

    /**
     * @return String
     */
    public String getLocale() {
        return locale;
    }

    /**
     * @param locale
     */
    public void setLocale(String locale) {
        this.locale = locale;
    }

    /**
     * @return int
     */
    public int getAge() {
        return age;
    }

    /**
     * @param age
     */
    public void setAge(int age) {
        this.age = age;
    }

    /**
     * @return List<IntervalDTO>
     */
    public List<IntervalDTO> getIntervals() {
        return intervals;
    }

    /**
     * @param intervals
     */
    public void setIntervals(List<IntervalDTO> intervals) {
        this.intervals = intervals;
    }

    /**
     * @return List<BotFunction>
     */
    public List<BotFunction> getFunctions() {
        return functions;
    }

    /**
     * @param functions
     */
    public void setFunctions(List<BotFunction> functions) {
        this.functions = functions;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class IntervalDTO {
        private String startTime;
        private String endTime;
        private int actionsPerHour;

        public String getStartTime() {
            return startTime;
        }

        public void setStartTime(String startTime) {
            this.startTime = startTime;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }

        public int getActionsPerHour() {
            return actionsPerHour;
        }

        public void setActionsPerHour(int actionsPerHour) {
            this.actionsPerHour = actionsPerHour;
        }
    }
}
