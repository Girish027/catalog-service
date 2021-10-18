package com.tfs.dp2.catalog.rule;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


import javax.transaction.Transactional;

/**
 * Created by bikesh.singh on 27-02-2018.
 */

@Repository
@Transactional
public interface RuleRepository extends JpaRepository<RuleScripts, String> {


    @Query(value="select rule_definition_id from catalog.rule_sql_definition where rule_definition_name= ?1",nativeQuery = true)
    String findRuleDefinitionIdByName(String ruleDefinitionName);
}
