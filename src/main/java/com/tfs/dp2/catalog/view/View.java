package com.tfs.dp2.catalog.view;

import com.tfs.dp2.catalog.util.CatalogAttributeConverter;
import lombok.Data;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by bikesh.singh on 27-02-2018.
 */
@Data
@Entity
@Table(name = "view_information")
@EntityListeners(AuditingEntityListener.class)
public class View implements Serializable {
    @Id
    @Column(name = "view_id")
    private String viewId;

    @Column(name = "view_name")
    private String viewName;

    @Column(name = "view_description", columnDefinition = "default 'null'")
    private String viewDescription;

    @Column(name = "layer_id")
    private String layerId;

    @Column(name = "definition_id")
    private String definitionId;

    @Column(name = "view_type")
    private String viewType;

    @Column(name = "owner")
    private String owner;

//    @Column(name = "cron_string")
//    private String cronExpression;

    @Column(columnDefinition = "tinyint", name = "dq_enabled")
    private int dqEnabled = 0;

    @Column(columnDefinition = "tinyint", name = "materialization_enabled")
    private int materializationEnabled = 0;

    @Column(columnDefinition = "BigInt", name = "schedule_start_time")
    private String jobStartTime;

    @Column(columnDefinition = "BigInt", name = "schedule_end_time")
    private String jobEndTime;

    @Column(name = "complexity")
    private String complexity;

    @Column(name = "created_unixtimestamps")
    private Long createdAt;

    @Column(name = "modified_unixtimestamps")
    private Long updatedAt;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "modified_by")
    private String modifiedBy;

    @Column(columnDefinition = "TinyInt", name = "is_active")
    private int isActive;

    @Column(name = "view_group")
    private String viewGroup;

    @Column(name = "def_client_view_map")
    private String defaultClientViewMapping;

    @Convert(converter = CatalogAttributeConverter.class)
    @Column(columnDefinition = "json", name = "environment_properties")
    Map<String, String> environmentProperties = new HashMap<>();

    @Convert(converter = CatalogAttributeConverter.class)
    @Column(columnDefinition = "json", name = "custom_params")
    private Map<String, String> customParams = new HashMap<>();
}