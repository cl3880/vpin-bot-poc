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
@Node
public class CommentModel {
    @Id
    private String id;
    private String textContent;
    private String createdAt;
    private Integer likeCount;
    private Integer dislikeCount;
    private Boolean liked;
    private Boolean disliked;
    private Boolean isAuthor;
    private Boolean isDeleted;
    private Integer voteOptionId;
    private String parentCommentAuthorId;
    private String parentCommentAuthorNickname;
    private String parentPinId;
    private AuthorModel authorModel;

    @Relationship(type = "HAS_REPLY", direction = Relationship.Direction.OUTGOING)
    private List<CommentModel> childComments;

    public AuthorModel getAuthor() {
        return authorModel;
    }

    public void setAuthor(AuthorModel authorModel) {
        this.authorModel = authorModel;
    }

    public Boolean isAuthor() {
        return isAuthor;
    }

    public void setIsAuthor(Boolean isAuthor) {
        this.isAuthor = isAuthor;
    }

    public List<CommentModel> getChildComments() {
        return childComments;
    }

    public void setChildComments(List<CommentModel> childComments) {
        this.childComments = childComments;
    }

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

    public Boolean getDeleted() {
        return isDeleted;
    }

    public void setDeleted(Boolean deleted) {
        isDeleted = deleted;
    }

    public Integer getVoteOptionId() {
        return voteOptionId;
    }

    public void setVoteOptionId(Integer voteOptionId) {
        this.voteOptionId = voteOptionId;
    }

    public String getParentCommentAuthorId() {
        return parentCommentAuthorId;
    }

    public void setParentCommentAuthorId(String parentCommentAuthorId) {
        this.parentCommentAuthorId = parentCommentAuthorId;
    }

    public String getParentCommentAuthorNickname() {
        return parentCommentAuthorNickname;
    }

    public void setParentCommentAuthorNickname(String parentCommentAuthorNickname) {
        this.parentCommentAuthorNickname = parentCommentAuthorNickname;
    }

    public String getParentPinId() {
        return parentPinId;
    }

    public void setParentPinId(String parentPinId) {
        this.parentPinId = parentPinId;
    }
}
