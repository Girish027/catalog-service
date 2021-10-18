package com.tfs.dp2.catalog.view;

import com.tfs.dp2.catalog.util.CatalogAttributeConverter;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by bikesh.singh on 09-03-2018.
 */

@Data
@Entity
@Table(name = "view_information")
@EntityListeners(AuditingEntityListener.class)
public class ViewInformation {
    @Id
    @Column(name = "view_id")
    private String viewId;

    @NotNull
    @NotEmpty
    @Column(name = "view_name")
    private String viewName;

    @NotNull
    @NotEmpty
    @Column(name = "view_description")
    private String viewDescription;

    @NotNull
    @NotEmpty
    //Please add both layer_name and layer_id whiile adding new layer options.
    @Pattern(regexp = "(?:^|(?<= ))(Layer 1|Layer 2|Layer 3|Layer 4|Layer1_1513089092|Layer2_1513089093|Layer3_1513089094|Layer4_1513089095)(?:(?= )|$)",
            message = "layerName should be one of these value: Layer 1, Layer 2, Layer 3, Layer 4")
    @Column(name = "layer_id")
    private String layerName;

    @Column(name = "definition_id")
    private String definitionId = null;

    @NotNull
    @NotEmpty
    @Column(name = "owner")
    private String owner;

    @NotNull
    @NotEmpty
    @Pattern(regexp = "(?:^|(?<= ))(fact|noFact|tempView|dim)(?:(?= )|$)",
            message = "viewType should be one of these value: fact, noFact, tempView, dim")
    @Column(name = "view_type")
    private String viewType;

    @NotNull
    @DecimalMax(value = "1000", message = "ttlDays should be between 1 to 1000")
    @DecimalMin(value = "1", message = "ttlDays should be between 1 to 1000")
    @Column(columnDefinition = "int", name = "ttl_days")
    private int ttlDays;

    @Column(columnDefinition = "tinyint", name = "is_active")
    private int isActive = 0;

    @NotNull
    @DecimalMax(value = "1", message = "esIndexEnabled should be 0 or 1")
    @DecimalMin(value = "0", message = "esIndexEnabled should be 0 or 1")
    @Column(columnDefinition = "tinyint", name = "es_index_enabled")
    private int esIndexEnabled;

    @NotNull
    @DecimalMax(value = "1", message = "druidIndexEnabled should be 0 or 1")
    @DecimalMin(value = "0", message = "druidIndexEnabled should be 0 or 1")
    @Column(columnDefinition = "tinyint", name = "druid_index_enabled")
    private int druidIndexEnabled;

    @NotNull
    @Column(columnDefinition = "bigInt", name = "schedule_start_time")
    @DecimalMin(value = "1000000000000", message = "please provide time in epoch millisecond")
    @Digits(integer = 13, fraction = 0, message = "please provide time in epoch millisecond")
    private long scheduleStartTime;

    @NotNull
    @Column(columnDefinition = "bigInt", name = "schedule_end_time")
    @DecimalMin(value = "1000000000000", message = "please provide time in epoch millisecond")
    @Digits(integer = 13, fraction = 0, message = "please provide time in epoch millisecond")
    private long scheduleEndTime;

    @NotNull
    @NotEmpty
    @Pattern(regexp = "[123]", message = "complexity should be between 1 to 3")
    @Column(name = "complexity")
    private String complexity;

    @NotNull
    @NotEmpty
    @Column(name = "view_output_path")
    private String viewOutputPath;

    @NotNull
    @NotEmpty
    @Column(name = "format")
    @Pattern(regexp = "(?:^|(?<= ))(csv|avro|json|parquet|ASSIST_INTERACTIONS|SEQ_JSON|ASSIST_TRANSCRIPTS|ACTIVE_SHARE|SESSIONIZED_BINLOG|ASSIST_INTERACTIONS_V2|ASSIST_AGENTSTATS)(?:(?= )|$)",
            message = "format should be one of these value: csv, avro, json, parquet, ASSIST_INTERACTIONS, SEQ_JSON, ASSIST_TRANSCRIPTS, ACTIVE_SHARE, SESSIONIZED_BINLOG ,ASSIST_INTERACTIONS_V2 ,ASSIST_AGENTSTATS")
    private String format;

    @NotNull
    @DecimalMax(value = "1", message = "dqEnabled should be 0 or 1")
    @DecimalMin(value = "0", message = "dqEnabled should be 0 or 1")
    @Column(columnDefinition = "tinyint", name = "dq_enabled")
    private int dqEnabled = 0;

    @NotNull
    @DecimalMax(value = "1", message = "materializationEnabled should be 0 or 1")
    @DecimalMin(value = "0", message = "materializationEnabled should be 0 or 1")
    @Column(columnDefinition = "tinyint", name = "materialization_enabled")
    private int materializationEnabled = 0;

    @Pattern(regexp = "(?:^|(?<= ))(SQL|SCALA)(?:(?= )|$)",
            message = "processingPlugInType should be one of these value: SQL, SCALA")
    @Column(columnDefinition = "enum", name = "processor_plugin_type")
    private String processingPlugInType = "SQL";

    @Column(name = "processor_plugin_main_class")
    private String processorPlugInMainClass = null;

    @Column(name = "processor_plugin_jar_loc")
    private String processorPlugInJarLoc = null;

    @Column(name = "source_plugin_class")
    private String sourcePluginClass;

    @Column(name = "time_dimension_column_id")
    private String timeDimensionColumnName;

    @Column(columnDefinition = "bigint", name = "created_unixtimestamps")
    private long createdUnixtimestamps = System.currentTimeMillis();

    @Column(columnDefinition = "bigint", name = "modified_unixtimestamps")
    private long modifiedUnixtimestamps = System.currentTimeMillis();

    @Column(name = "created_by")
    private String createdBy = "System";

    @Column(name = "modified_by")
    private String modifiedBy = "System";

    @NotNull
    @DecimalMax(value = "1", message = "isDq should be 0 or 1")
    @DecimalMin(value = "0", message = "isDq should be 0 or 1")
    @Column(columnDefinition = "tinyint", name = "is_dq")
    private int isDq;

    @Column(columnDefinition = "tinyint", name = "is_dynamicsql")
    private int isDynamicSql = 0;

    @NotNull
    @Column(name = "view_group")
    private String viewGroup;

    @Column(name = "job_execution_expression")
    private String jobExecutionExpression = "";

    @DecimalMax(value = "1", message = "prefer_read_materialized_data should be 0 or 1")
    @DecimalMin(value = "0", message = "prefer_read_materialized_data should be 0 or 1")
    @Column(columnDefinition = "tinyint", name = "prefer_read_materialized_data")
    private int preferReadMaterializedData = 0;

    @NotNull
    @Column(columnDefinition = "enum", name = "load_strategy")
    @Pattern(regexp = "(?:^|(?<= ))(RELATIVE|STATIC)(?:(?= )|$)",
            message = "loadStrategy should be one of these value: RELATIVE, STATIC")
    private String loadStrategy;

    @Column(name = "def_client_view_map")
    private String defaultClientViewMapping;

    @Convert(converter = CatalogAttributeConverter.class)
    @Column(columnDefinition = "json", name = "environment_properties")
    Map<String, String> environmentProperties = new HashMap<>();

    @Convert(converter = CatalogAttributeConverter.class)
    @Column(columnDefinition = "json", name = "custom_params")
    private Map<String, String> customParams = new HashMap<>();

    @Column(name = "dynamic_bucket_path")
    private String dynamicBucketPath;

}
