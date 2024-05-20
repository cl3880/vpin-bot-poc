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

import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.RelationshipProperties;
import org.springframework.data.neo4j.core.schema.TargetNode;

import java.time.LocalDateTime;

/**
 * @Author Christopher Leu (
 *         <a href= "mailto:chrisleu9@gmail.com">chrisleu9@gmail.com</a>)
 */
@RelationshipProperties
public class RepliedToRelationship {
    @Id
    @GeneratedValue
    private Long id;
    private BotModel botModel;
    @TargetNode
    private CommentModel comment;
    private LocalDateTime interactionDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BotModel getBot() {
        return botModel;
    }

    public void setBot(BotModel botModel) {
        this.botModel = botModel;
    }

    public CommentModel getComment() {
        return comment;
    }

    public void setComment(CommentModel comment) {
        this.comment = comment;
    }

    public LocalDateTime getInteractionDate() {
        return interactionDate;
    }

    public void setInteractionDate(LocalDateTime interactionDate) {
        this.interactionDate = interactionDate;
    }
}
