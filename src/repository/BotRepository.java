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

package com.osparks.vpin.bot.dao;

import com.osparks.vpin.bot.models.BotModel;
import com.osparks.vpin.bot.models.VpinModel;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

/**
 * @Author Christopher Leu (
 *         <a href= "mailto:chrisleu9@gmail.com">chrisleu9@gmail.com</a>)
 */
@Repository
public interface BotRepository extends Neo4jRepository<BotModel, String> {
    @Query("MATCH (b:Bot) RETURN b ORDER BY b.created_at DESC LIMIT 1")
    Optional<BotModel> findLastStored();

    @Query("MATCH (b:Bot {username: $username}) RETURN b")
    Optional<BotModel> findByUsername(String username);

    @Query("MATCH (b:Bot)-[:HAS_INTERVALS]->(i:Interval) WHERE $currentTime >= i.startTime AND $currentTime <= i.endTime RETURN b")
    List<BotModel> findUpcomingActiveBots(LocalTime currentTime);

    @Query("MATCH (b:Bot {id: $botId})-[r:LIKED|CREATED|COMMENTED_ON|REPLIED_TO]->(v:Vpin) RETURN v")
    List<VpinModel> findInteractedVpinsByBotId(String botId);
}
