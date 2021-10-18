package com.tfs.dp2.catalog.rule;


import lombok.Data;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

/**
 * Created by bikesh.singh on 27-02-2018.
 */

@Data
@Entity
@Table(name = "rule_sql_definition")
@EntityListeners(AuditingEntityListener.class)
public class RuleScripts {
    @Id
    @Column(name="rule_definition_id")
    public String ruleDefinitionID;
    @Column(name="rule_definition_name")
    public String ruleDefinitionName;

    @Lob
    @Column(name="sql_definition",length = 512)
    public String sqlDefinition;

    @Column(name="column_list")
    public String columnList;
}
