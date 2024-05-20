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
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.List;

/**
 * @Author Christopher Leu (
 *         <a href= "mailto:chrisleu9@gmail.com">chrisleu9@gmail.com</a>)
 */
@Node("Vpin")
public class VpinModel {
    @Id
    private String id;
    private String textContent;
    private String createdAt;
    private String videoUrl;
    private String videoPlatform;
    private Boolean liked;
    private Boolean disliked;
    @Relationship(type = "HAS_TAGS", direction = Relationship.Direction.OUTGOING)
    private List<TagModel> tagModels;
    private Integer likeCount;
    private Integer commentCount;
    @Relationship(type = "AUTHORED_BY", direction = Relationship.Direction.INCOMING)
    private AuthorModel authorModel;
    @Relationship(type = "HAS_COMMENT", direction = Relationship.Direction.OUTGOING)
    private List<CommentModel> comments;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getTextContent() {
        return textContent;
    }

    public void setTextContent(String textContent) {
        this.textContent = textContent;
    }

    public List<TagModel> getTags() {
        return tagModels;
    }

    public void setTags(List<TagModel> tagModels) {
        this.tagModels = tagModels;
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

    public Integer getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(Integer likeCount) {
        this.likeCount = likeCount;
    }

    public Integer getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(Integer commentCount) {
        this.commentCount = commentCount;
    }

    public AuthorModel getAuthor() {
        return authorModel;
    }

    public void setAuthor(AuthorModel authorModel) {
        this.authorModel = authorModel;
    }
}
