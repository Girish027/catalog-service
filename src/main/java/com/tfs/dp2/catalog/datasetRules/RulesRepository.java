package com.tfs.dp2.catalog.datasetRules;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by bikesh.singh on 27-08-2018.
 */

@Repository
public interface RulesRepository extends JpaRepository<Rules, String> {

    @Query(value = "SELECT * FROM catalog.rules rs WHERE rs.dataset_id = ?1", nativeQuery = true)
    List<Rules> findByDatasetId(String datasetId);

    @Query(value = "SELECT rs.rule_id FROM catalog.rules rs WHERE rs.dataset_id = ?1", nativeQuery = true)
    List<String> findRuleIdsByDatasetId(String datasetId);

    @Query(value = "SELECT * FROM catalog.rules rs WHERE rs.dataset_id = ?1 AND rs.rule_name = ?2", nativeQuery = true)
    Rules findByDatasetIdAndRuleName(String datasetId,String ruleName);

    @Modifying
    @Query(value = "DELETE FROM catalog.rules WHERE dataset_id IN ?1", nativeQuery = true)
    void deleteByDatasetIds(List<String> datasetIds);
}
