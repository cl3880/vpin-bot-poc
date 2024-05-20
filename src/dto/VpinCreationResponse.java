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
public class VpinCreationResponse {
    private static String videoUrl;
    private String id;
    private Author author;
    private String beginsAt;
    private Integer commentCount;
    private String coverUrl;
    private String createdAt;
    private String description;
    private Integer dislikeCount;
    private Boolean disliked;
    private Boolean isAnonymous;
    private Boolean isAuthor;
    private Boolean isBattle;
    private Boolean isDeleted;
    private Boolean isEmbedded;
    private Boolean isInFolder;
    private Boolean isMultiPin;
    private Integer likeCount;
    private Boolean liked;
    private String locale;
    private Integer readCount;
    private String state;
    private List<Tag> tags;
    private String template;
    private String textContent;
    private Integer thumbnailIndex;
    private String videoPlatform;
    private String videoTitle;
    private Integer votedOptionId;

    /**
     * @return the video URL.
     */
    public static String getVideoUrl() {
        return videoUrl;
    }

    /**
     * @param videoUrl the video URL to set.
     */
    public void setVideoUrl(String videoUrl) {
        VpinCreationResponse.videoUrl = videoUrl;
    }

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
     * @return the author of the Vpin.
     */
    public Author getAuthor() {
        return author;
    }

    /**
     * @param author the author to set for the Vpin.
     */
    public void setAuthor(Author author) {
        this.author = author;
    }

    /**
     * @return true if the Vpin is a battle, false otherwise.
     */
    public Boolean getBattle() {
        return isBattle;
    }

    /**
     * @param battle the battle status to set for the Vpin.
     */
    public void setBattle(Boolean battle) {
        isBattle = battle;
    }

    /**
     * @return true if the Vpin is deleted, false otherwise.
     */
    public Boolean getDeleted() {
        return isDeleted;
    }

    /**
     * @param deleted the deleted status to set for the Vpin.
     */
    public void setDeleted(Boolean deleted) {
        isDeleted = deleted;
    }

    /**
     * @return true if the Vpin is embedded, false otherwise.
     */
    public Boolean getEmbedded() {
        return isEmbedded;
    }

    /**
     * @param embedded the embedded status to set for the Vpin.
     */
    public void setEmbedded(Boolean embedded) {
        isEmbedded = embedded;
    }

    /**
     * @return true if the Vpin is in a folder, false otherwise.
     */
    public Boolean getInFolder() {
        return isInFolder;
    }

    /**
     * @param inFolder the in-folder status to set for the Vpin.
     */
    public void setInFolder(Boolean inFolder) {
        isInFolder = inFolder;
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
     * @return the like count of the Vpin.
     */
    public Integer getLikeCount() {
        return likeCount;
    }

    /**
     * @param likeCount the like count to set for the Vpin.
     */
    public void setLikeCount(Integer likeCount) {
        this.likeCount = likeCount;
    }

    /**
     * @return true if the Vpin is liked, false otherwise.
     */
    public Boolean getLiked() {
        return liked;
    }

    /**
     * @param liked the liked status to set for the Vpin.
     */
    public void setLiked(Boolean liked) {
        this.liked = liked;
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
     * @return the read count of the Vpin.
     */
    public Integer getReadCount() {
        return readCount;
    }

    /**
     * @param readCount the read count to set for the Vpin.
     */
    public void setReadCount(Integer readCount) {
        this.readCount = readCount;
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
    public List<Tag> getTags() {
        return tags;
    }

    /**
     * @param tags the tags to set for the Vpin.
     */
    public void setTags(List<Tag> tags) {
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
     * @return the voted option ID of the Vpin.
     */
    public Integer getVotedOptionId() {
        return votedOptionId;
    }

    /**
     * @param votedOptionId the voted option ID to set for the Vpin.
     */
    public void setVotedOptionId(Integer votedOptionId) {
        this.votedOptionId = votedOptionId;
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
     * @return the comment count of the Vpin.
     */
    public Integer getCommentCount() {
        return commentCount;
    }

    /**
     * @param commentCount the comment count to set for the Vpin.
     */
    public void setCommentCount(Integer commentCount) {
        this.commentCount = commentCount;
    }

    /**
     * @return the cover URL of the Vpin.
     */
    public String getCoverUrl() {
        return coverUrl;
    }

    /**
     * @param coverUrl the cover URL to set for the Vpin.
     */
    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    /**
     * @return the creation time of the Vpin.
     */
    public String getCreatedAt() {
        return createdAt;
    }

    /**
     * @param createdAt the creation time to set for the Vpin.
     */
    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
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
     * @return the dislike count of the Vpin.
     */
    public Integer getDislikeCount() {
        return dislikeCount;
    }

    /**
     * @param dislikeCount the dislike count to set for the Vpin.
     */
    public void setDislikeCount(Integer dislikeCount) {
        this.dislikeCount = dislikeCount;
    }

    /**
     * @return true if the Vpin is disliked, false otherwise.
     */
    public Boolean getDisliked() {
        return disliked;
    }

    /**
     * @param disliked the disliked status to set for the Vpin.
     */
    public void setDisliked(Boolean disliked) {
        this.disliked = disliked;
    }

    /**
     * @return true if the Vpin is anonymous, false otherwise.
     */
    public Boolean getAnonymous() {
        return isAnonymous;
    }

    /**
     * @param anonymous the anonymous status to set for the Vpin.
     */
    public void setAnonymous(Boolean anonymous) {
        isAnonymous = anonymous;
    }

    /**
     * @return true if the Vpin is authored, false otherwise.
     */
    public Boolean isAuthor() {
        return isAuthor;
    }

    /**
     * @param isAuthor the authored status to set for the Vpin.
     */
    public void setIsAuthor(Boolean isAuthor) {
        this.isAuthor = isAuthor;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class Author {
        private String avatarUrl;
        private String id;
        private Boolean isFollowed;
        private String locale;
        private String nickname;
        private Integer totalFanFollows;
        private Integer totalUserFollows;
        private List<Vpin> vpins;

        public String getAvatarUrl() {
            return avatarUrl;
        }

        public void setAvatarUrl(String avatarUrl) {
            this.avatarUrl = avatarUrl;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public Boolean getFollowed() {
            return isFollowed;
        }

        public void setFollowed(Boolean followed) {
            isFollowed = followed;
        }

        public String getLocale() {
            return locale;
        }

        public void setLocale(String locale) {
            this.locale = locale;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public Integer getTotalFanFollows() {
            return totalFanFollows;
        }

        public void setTotalFanFollows(Integer totalFanFollows) {
            this.totalFanFollows = totalFanFollows;
        }

        public Integer getTotalUserFollows() {
            return totalUserFollows;
        }

        public void setTotalUserFollows(Integer totalUserFollows) {
            this.totalUserFollows = totalUserFollows;
        }

        public List<Vpin> getVpins() {
            return vpins;
        }

        public void setVpins(List<Vpin> vpins) {
            this.vpins = vpins;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class Tag {
        private Boolean isEmbedded;
        private String tag;

        public Boolean isEmbedded() {
            return isEmbedded;
        }

        public void setIsEmbedded(Boolean isEmbedded) {
            this.isEmbedded = isEmbedded;
        }

        public String getTag() {
            return tag;
        }

        public void setTag(String tag) {
            this.tag = tag;
        }

        public Boolean getEmbedded() {
            return isEmbedded;
        }

        public void setEmbedded(Boolean embedded) {
            isEmbedded = embedded;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class Vpin {
        private String id;
        private String textContent;
        private String createdAt;
        private String videoUrl;
        private String videoPlatform;
        private String videoTitle;
        private String beginsAt;
        private List<Tag> tags;
        private Integer likeCount;
        private Integer dislikeCount;
        private Integer readCount;
        private Integer commentCount;
        private Author author;
        private String state;
        private Boolean liked;
        private Boolean disliked;
        private Boolean isInFolder;
        private String template;
        private Boolean isAnonymous;
        private Boolean isBattle;
        private String description;
        private Integer thumbnailIndex;
        private Boolean isAuthor;
        private String locale;
        private Boolean isDeleted;
        private Boolean isEmbedded;
        private Boolean isMultiPin;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTextContent() {
            return textContent;
        }

        public void setTextContent(String textContent) {
            this.textContent = textContent;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public String getVideoUrl() {
            return videoUrl;
        }

        public void setVideoUrl(String videoUrl) {
            this.videoUrl = videoUrl;
        }

        public String getVideoPlatform() {
            return videoPlatform;
        }

        public void setVideoPlatform(String videoPlatform) {
            this.videoPlatform = videoPlatform;
        }

        public String getVideoTitle() {
            return videoTitle;
        }

        public void setVideoTitle(String videoTitle) {
            this.videoTitle = videoTitle;
        }

        public String getBeginsAt() {
            return beginsAt;
        }

        public void setBeginsAt(String beginsAt) {
            this.beginsAt = beginsAt;
        }

        public List<Tag> getTags() {
            return tags;
        }

        public void setTags(List<Tag> tags) {
            this.tags = tags;
        }

        public Integer getLikeCount() {
            return likeCount;
        }

        public void setLikeCount(Integer likeCount) {
            this.likeCount = likeCount;
        }

        public Integer getDislikeCount() {
            return dislikeCount;
        }

        public void setDislikeCount(Integer dislikeCount) {
            this.dislikeCount = dislikeCount;
        }

        public Integer getReadCount() {
            return readCount;
        }

        public void setReadCount(Integer readCount) {
            this.readCount = readCount;
        }

        public Integer getCommentCount() {
            return commentCount;
        }

        public void setCommentCount(Integer commentCount) {
            this.commentCount = commentCount;
        }

        public Author getAuthor() {
            return author;
        }

        public void setAuthor(Author author) {
            this.author = author;
        }

        public String getLocale() {
            return locale;
        }

        public void setLocale(String locale) {
            this.locale = locale;
        }

        public Boolean getDeleted() {
            return isDeleted;
        }

        public void setDeleted(Boolean deleted) {
            isDeleted = deleted;
        }

        public Boolean getEmbedded() {
            return isEmbedded;
        }

        public void setEmbedded(Boolean embedded) {
            isEmbedded = embedded;
        }

        public Boolean getMultiPin() {
            return isMultiPin;
        }

        public void setMultiPin(Boolean multiPin) {
            isMultiPin = multiPin;
        }

        public Boolean isAuthor() {
            return isAuthor;
        }

        public void setIsAuthor(Boolean isAuthor) {
            this.isAuthor = isAuthor;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public Boolean getLiked() {
            return liked;
        }

        public void setLiked(Boolean liked) {
            this.liked = liked;
        }

        public Boolean getDisliked() {
            return disliked;
        }

        public void setDisliked(Boolean disliked) {
            this.disliked = disliked;
        }

        public Boolean getInFolder() {
            return isInFolder;
        }

        public void setInFolder(Boolean inFolder) {
            isInFolder = inFolder;
        }

        public String getTemplate() {
            return template;
        }

        public void setTemplate(String template) {
            this.template = template;
        }

        public Boolean getAnonymous() {
            return isAnonymous;
        }

        public void setAnonymous(Boolean anonymous) {
            isAnonymous = anonymous;
        }

        public Boolean getBattle() {
            return isBattle;
        }

        public void setBattle(Boolean battle) {
            isBattle = battle;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public Integer getThumbnailIndex() {
            return thumbnailIndex;
        }

        public void setThumbnailIndex(Integer thumbnailIndex) {
            this.thumbnailIndex = thumbnailIndex;
        }

    }
}
