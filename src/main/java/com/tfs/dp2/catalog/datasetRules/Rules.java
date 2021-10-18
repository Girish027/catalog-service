package com.tfs.dp2.catalog.datasetRules;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by bikesh.singh on 27-08-2018.
 */
@Data
@Entity
@Table(name = "rules")
@EntityListeners(AuditingEntityListener.class)
public class Rules implements Serializable {

    @Id
    @Column(name = "rule_id")
    private String ruleId;

    @Column(name = "rule_name")
    private String ruleName;

    @Column(columnDefinition = "longtext", name = "rule_sql")
    private String ruleSql;

    @Column(name = "description")
    private String description;

    @Column(name = "dataset_id")
    @JsonIgnore
    private String datasetId;

    @Column(columnDefinition = "bigint", name = "created_unixtimestamps")
    private long creatdUnixtimestamps = System.currentTimeMillis();

    @Column(columnDefinition = "bigint", name = "modified_unixtimestamps")
    private long modifiedUnixtimestamps = System.currentTimeMillis();
}
