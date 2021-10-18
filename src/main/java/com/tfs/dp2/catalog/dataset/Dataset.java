package com.tfs.dp2.catalog.dataset;

import lombok.Data;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by bikesh.singh on 28-08-2018.
 */

@Data
@Entity
@Table(name = "dataset")
@EntityListeners(AuditingEntityListener.class)
public class Dataset implements Serializable {

    @Id
    @Column(name = "dataset_id")
    private String datasetId;

    @Column(name = "dataset_name")
    private String datasetName;

    @Column(columnDefinition = "longtext", name = "dataset_sql")
    private String datasetSql;

    @Column(name = "description")
    private String description;

    @Column(name = "level")
    private String level;

    @Column(name = "record_identifier")
    private String recordIdentifier;

    @Column(name = "view_id")
    private String viewId;

    @Column(columnDefinition = "bigint", name = "created_unixtimestamps")
    private long creatdUnixtimestamps = System.currentTimeMillis();

    @Column(columnDefinition = "bigint", name = "modified_unixtimestamps")
    private long modifiedUnixtimestamps = System.currentTimeMillis();
}
