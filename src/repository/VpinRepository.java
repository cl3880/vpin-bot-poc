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

import com.osparks.vpin.bot.models.VpinModel;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author Christopher Leu (
 *         <a href= "mailto:chrisleu9@gmail.com">chrisleu9@gmail.com</a>)
 */
@Repository
public interface VpinRepository extends Neo4jRepository<VpinModel, String> {
    @Query("MATCH (b:Bot {id: $botId})-[:INTERACTED_WITH]->(v:VpinModel) RETURN v")
    List<VpinModel> findAllVpinsInteractedWithByBot(String botId);

    @Query("MATCH (b:Bot {id: $botId})-[r:INTERACTED_WITH]->(v:VpinModel) " +
            "RETURN v ORDER BY r.interactionDate DESC LIMIT 1")
    VpinModel findMostRecentlyInteractedVpinByBot(String botId);
}
