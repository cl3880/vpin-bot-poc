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

import java.util.List;

/**
 * @Author Christopher Leu (
 *         <a href= "mailto:chrisleu9@gmail.com">chrisleu9@gmail.com</a>)
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CreateVpinRequest {
    private String id;
    private String beginsAt;
    private String description;
    private List<String> folders;
    private Boolean isMultiPin;
    private String locale;
    private String negativeOptionTitle;
    private List<Pin> pins;
    private String positiveOptionTitle;
    private String state;
    private List<String> tags;
    private String template;
    private String textContent;
    private Integer thumbnailIndex;
    private String videoPlatform;
    private String videoTitle;
    private String videoUrl;

    /**
     * @return the ID of the Vpin.
     */
    public String getId() {
        return id;
    }

    /**
     * @param id the ID to set for the Vpin.
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the beginning time of the Vpin.
     */
    public String getBeginsAt() {
        return beginsAt;
    }

    /**
     * @param beginsAt the beginning time to set for the Vpin.
     */
    public void setBeginsAt(String beginsAt) {
        this.beginsAt = beginsAt;
    }

    /**
     * @return the description of the Vpin.
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set for the Vpin.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the list of folders associated with the Vpin.
     */
    public List<String> getFolders() {
        return folders;
    }

    /**
     * @param folders the folders to set for the Vpin.
     */
    public void setFolders(List<String> folders) {
        this.folders = folders;
    }

    /**
     * @return true if the Vpin is a multi-pin, false otherwise.
     */
    public Boolean getMultiPin() {
        return isMultiPin;
    }

    /**
     * @param multiPin the multi-pin status to set for the Vpin.
     */
    public void setMultiPin(Boolean multiPin) {
        isMultiPin = multiPin;
    }

    /**
     * @return the locale of the Vpin.
     */
    public String getLocale() {
        return locale;
    }

    /**
     * @param locale the locale to set for the Vpin.
     */
    public void setLocale(String locale) {
        this.locale = locale;
    }

    /**
     * @return the negative option title of the Vpin.
     */
    public String getNegativeOptionTitle() {
        return negativeOptionTitle;
    }

    /**
     * @param negativeOptionTitle the negative option title to set for the Vpin.
     */
    public void setNegativeOptionTitle(String negativeOptionTitle) {
        this.negativeOptionTitle = negativeOptionTitle;
    }

    /**
     * @return the list of pins associated with the Vpin.
     */
    public List<Pin> getPins() {
        return pins;
    }

    /**
     * @param pins the pins to set for the Vpin.
     */
    public void setPins(List<Pin> pins) {
        this.pins = pins;
    }

    /**
     * @return the positive option title of the Vpin.
     */
    public String getPositiveOptionTitle() {
        return positiveOptionTitle;
    }

    /**
     * @param positiveOptionTitle the positive option title to set for the Vpin.
     */
    public void setPositiveOptionTitle(String positiveOptionTitle) {
        this.positiveOptionTitle = positiveOptionTitle;
    }

    /**
     * @return the state of the Vpin.
     */
    public String getState() {
        return state;
    }

    /**
     * @param state the state to set for the Vpin.
     */
    public void setState(String state) {
        this.state = state;
    }

    /**
     * @return the list of tags associated with the Vpin.
     */
    public List<String> getTags() {
        return tags;
    }

    /**
     * @param tags the tags to set for the Vpin.
     */
    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    /**
     * @return the template of the Vpin.
     */
    public String getTemplate() {
        return template;
    }

    /**
     * @param template the template to set for the Vpin.
     */
    public void setTemplate(String template) {
        this.template = template;
    }

    /**
     * @return the text content of the Vpin.
     */
    public String getTextContent() {
        return textContent;
    }

    /**
     * @param textContent the text content to set for the Vpin.
     */
    public void setTextContent(String textContent) {
        this.textContent = textContent;
    }

    /**
     * @return the thumbnail index of the Vpin.
     */
    public Integer getThumbnailIndex() {
        return thumbnailIndex;
    }

    /**
     * @param thumbnailIndex the thumbnail index to set for the Vpin.
     */
    public void setThumbnailIndex(Integer thumbnailIndex) {
        this.thumbnailIndex = thumbnailIndex;
    }

    /**
     * @return the video platform of the Vpin.
     */
    public String getVideoPlatform() {
        return videoPlatform;
    }

    /**
     * @param videoPlatform the video platform to set for the Vpin.
     */
    public void setVideoPlatform(String videoPlatform) {
        this.videoPlatform = videoPlatform;
    }

    /**
     * @return the video title of the Vpin.
     */
    public String getVideoTitle() {
        return videoTitle;
    }

    /**
     * @param videoTitle the video title to set for the Vpin.
     */
    public void setVideoTitle(String videoTitle) {
        this.videoTitle = videoTitle;
    }

    /**
     * @return the video URL of the Vpin.
     */
    public String getVideoUrl() {
        return videoUrl;
    }

    /**
     * @param videoUrl the video URL to set for the Vpin.
     */
    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class Pin {
        private String beginsAt;
        private String title;

        public String getBeginsAt() {
            return beginsAt;
        }

        public void setBeginsAt(String beginsAt) {
            this.beginsAt = beginsAt;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }
}
