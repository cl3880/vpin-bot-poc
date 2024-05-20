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
import com.osparks.vpin.bot.dto.BotDashFormData;
import org.springframework.data.annotation.Transient;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Represents a Bot model with various attributes and relationships.
 * 
 * @Author Christopher Leu (
 *         <a href= "mailto:chrisleu9@gmail.com">chrisleu9@gmail.com</a>)
 */
@Node("Bot")
public class BotModel {
    @Id
    private final String id;
    private String username;
    private String password;
    private String nickname;
    private String name;
    private String gender;
    private String location;
    private Integer age;
    private String occupation;
    private String locale;
    private String vpinPlatformId;
    private LocalDateTime createDate;
    private Boolean isActive;
    private String lastInteractedVpinId;

    @Relationship(type = "HAS_INTERESTS", direction = Relationship.Direction.OUTGOING)
    private List<InterestModel> interests;

    @Relationship(type = "HAS_INTERVALS", direction = Relationship.Direction.OUTGOING)
    private List<IntervalModel> intervals;

    @Relationship(type = "HAS_COMMANDS", direction = Relationship.Direction.OUTGOING)
    private List<BotFunction> functions;

    @Relationship(type = "LIKED", direction = Relationship.Direction.OUTGOING)
    private List<LikedRelationship> likedRelationships;

    @Relationship(type = "COMMENTED_ON")
    private List<CommentedOnRelationship> commentedOnRelationships;

    @Relationship(type = "INTERACTED_WITH_COMMENT_UNDER")
    private List<InteractedWCommentUnderRelationship> interactedWCommentUnderRelationships;

    @Relationship(type = "REPLIED_TO")
    private List<RepliedToRelationship> repliedToRelationships;

    @Relationship(type = "CREATED")
    private List<CreatedRelationship> createdRelationships;

    @Relationship(type = "SELECTED_TAGS", direction = Relationship.Direction.OUTGOING)
    private List<TagModel> tagsSelected = new ArrayList<>();

    private volatile boolean shouldStop = false;

    public BotModel(String id, String name, Integer age, String gender, String location, String occupation,
            LocalDateTime createDate, List<InterestModel> interests, String username, String password,
            String nickname, String locale, String vpinPlatformId, List<IntervalModel> intervals,
            List<BotFunction> functions,
            Boolean isActive, List<LikedRelationship> likedRelationships,
            List<CommentedOnRelationship> commentedOnRelationships,
            List<InteractedWCommentUnderRelationship> interactedWCommentUnderRelationships,
            List<RepliedToRelationship> repliedToRelationships,
            List<CreatedRelationship> createdRelationships, List<TagModel> tagsSelected) {
        this.id = id == null ? UUID.randomUUID().toString() : id;
        this.name = name;
        this.gender = gender;
        this.age = age;
        this.location = location;
        this.occupation = occupation;
        this.createDate = createDate;
        this.interests = interests;
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.locale = locale;
        this.vpinPlatformId = vpinPlatformId;
        this.intervals = intervals;
        this.functions = functions;
        this.isActive = false;
        this.likedRelationships = likedRelationships;
        this.commentedOnRelationships = commentedOnRelationships;
        this.interactedWCommentUnderRelationships = interactedWCommentUnderRelationships;
        this.repliedToRelationships = repliedToRelationships;
        this.createdRelationships = createdRelationships;
        this.tagsSelected = tagsSelected;
    }

    public static BotModel fromDto(BotDashFormData dto) {
        String id = UUID.randomUUID().toString();
        List<VpinModel> vpinsInteracted;
        List<IntervalModel> intervals = dto.getIntervals().stream()
                .map(intervalDTO -> new IntervalModel(
                        LocalTime.parse(intervalDTO.getStartTime()),
                        LocalTime.parse(intervalDTO.getEndTime()),
                        intervalDTO.getActionsPerHour()))
                .collect(Collectors.toList());
        return new BotModel(id, dto.getName(), dto.getAge(), dto.getGender(), dto.getLocation(),
                dto.getOccupation(), LocalDateTime.now(), dto.getInterests(), dto.getUsername(), null,
                dto.getNickname(), dto.getLocale(), null, intervals, dto.getFunctions(), false,
                null, null, null, null,
                null, null);
    }

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    @JsonProperty
    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    @JsonProperty
    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    @JsonProperty
    public void setAge(Integer age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    @JsonProperty
    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getLocation() {
        return location;
    }

    @JsonProperty
    public void setLocation(String location) {
        this.location = location;
    }

    public String getOccupation() {
        return occupation;
    }

    @JsonProperty
    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public LocalDateTime getCreateDate() {
        return this.createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    public List<InterestModel> getInterests() {
        return this.interests;
    }

    @JsonProperty
    public void setInterests(List<InterestModel> interests) {
        this.interests = interests;
    }

    public String getNickname() {
        return nickname;
    }

    @JsonProperty
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getLocale() {
        return locale;
    }

    @JsonProperty
    public void setLocale(String locale) {
        this.locale = locale;
    }

    public String getVpinPlatformId() {
        return vpinPlatformId;
    }

    public void setVpinPlatformId(String vpinId) {
        this.vpinPlatformId = vpinPlatformId;
    }

    public List<IntervalModel> getIntervals() {
        return intervals;
    }

    @JsonProperty
    public void setIntervals(List<IntervalModel> intervals) {
        this.intervals = intervals;
    }

    public List<BotFunction> getFunctions() {
        return functions;
    }

    @JsonProperty
    public void setFunctions(List<BotFunction> functions) {
        this.functions = functions;
    }

    public Boolean isActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    @Transient
    public String getStatus() {
        return isActive ? "Online" : "Offline";
    }

    public String getLastInteractedVpinId() {
        return lastInteractedVpinId;
    }

    public void setLastInteractedVpinId(String lastInteractedVpinId) {
        this.lastInteractedVpinId = lastInteractedVpinId;
    }

    public List<LikedRelationship> getLikedRelationships() {
        return likedRelationships;
    }

    public void setLikedRelationships(List<LikedRelationship> likedRelationshipRelationships) {
        this.likedRelationships = likedRelationshipRelationships;
    }

    public List<CommentedOnRelationship> getCommentedOnRelationships() {
        return commentedOnRelationships;
    }

    public void setCommentedOnRelationships(List<CommentedOnRelationship> commentedOnRelationshipRelationships) {
        this.commentedOnRelationships = commentedOnRelationshipRelationships;
    }

    public List<InteractedWCommentUnderRelationship> getInteractedWithCommentUnderRelationships() {
        return interactedWCommentUnderRelationships;
    }

    public void setInteractedWithCommentUnderRelationships(
            List<InteractedWCommentUnderRelationship> interactedWCommentUnderRelationshipRelationships) {
        this.interactedWCommentUnderRelationships = interactedWCommentUnderRelationshipRelationships;
    }

    public List<RepliedToRelationship> getRepliedToRelationships() {
        return repliedToRelationships;
    }

    public void setRepliedToRelationships(List<RepliedToRelationship> repliedToRelationshipRelationships) {
        this.repliedToRelationships = repliedToRelationshipRelationships;
    }

    public List<CreatedRelationship> getCreatedRelationships() {
        return createdRelationships;
    }

    public void setCreatedRelationships(List<CreatedRelationship> createdRelationshipRelationships) {
        this.createdRelationships = createdRelationshipRelationships;
    }

    public List<TagModel> getTagsSelected() {
        return tagsSelected;
    }

    public void setTagsSelected(List<TagModel> tagsSelected) {
        this.tagsSelected = tagsSelected;
    }

    public boolean shouldStop() {
        return shouldStop;
    }

    public void setShouldStop(boolean shouldStop) {
        this.shouldStop = shouldStop;
    }
}
