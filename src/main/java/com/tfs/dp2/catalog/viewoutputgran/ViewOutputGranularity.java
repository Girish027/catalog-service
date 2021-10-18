package com.tfs.dp2.catalog.viewoutputgran;

import com.tfs.dp2.catalog.entities.ExecutionAttributes;
import com.tfs.dp2.catalog.entities.ExecutionAttributesEntities;
import com.tfs.dp2.catalog.util.CatalogAttributeConverter;
import lombok.Data;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * Created by bikesh.singh on 27-02-2018.
 */

@Entity
@Table(name = "view_output_granularity")
@EntityListeners(AuditingEntityListener.class)
@Data
public class ViewOutputGranularity implements Serializable, ExecutionAttributesEntities {

    private static final String API = "API";

    @Id
    @Column(name = "output_id")
    private String id;

    @Column(name = "view_id")
    private String viewId;

    @Column(name = "client_info_id")
    private String clientInfoId;

    @Column(name = "final_view_id")
    private String finalViewId;

    @Column(name = "client_HDFS_folder_id")
    private String clientHdfsFolderId;

    @Column(name = "output_path")
    private String outputPath;

    @Column(name = "granularity")
    private String granularity;

    @Column(columnDefinition = "bigint", name = "granularity_in_min")
    private long granularityInMin;

    @Column(columnDefinition = "int", name = "reference_time_min")
    private int referenceTimeMin;

    @Column(name = "cron_string")
    private String cronExpression;

    @Column(columnDefinition = "tinyint", name = "is_active")
    private int isActive = 1;

    @Column(columnDefinition = "int", name = "weekend_enabled")
    private int weekendEnabled;

    @Column(columnDefinition = "bigint", name = "created_unixtimestamps")
    private long creatdUnixtimestamps = System.currentTimeMillis();

    @Column(columnDefinition = "bigint", name = "modified_unixtimestamps")
    private long modifiedUnixtimestamps = System.currentTimeMillis();

    @Column(name = "created_by")
    private String createdBy = API;

    @Column(name = "modified_by")
    private String modifiedBy = API;

    @Column(name = "sla_breach_time")
    private int slaBreachTime;

    @Column(name= "OWNER")
    private String owner;

    @Column(name = "owner_email_id")
    private String ownerEmailId;

    @NotNull
    @Convert(converter = CatalogAttributeConverter.class)
    @Column(columnDefinition = "json", name = "execution_properties")
    Map<String, String> executionProperties = new HashMap<>();

    @Column(name="job_execution_expression")
    private String jobExecutionExpression = "";

    @Column(columnDefinition = "enum",name = "load_strategy")
    private String loadStrategy;

    @Column( name = "job_start_time")
    private String jobStartTime;

    @Column(name = "job_end_time")
    private String jobEndTime;


    @Convert(converter = CatalogAttributeConverter.class)
    @Column(columnDefinition = "json", name = "environment_properties")
    Map<String, String> environmentProperties = new HashMap<>();

    @Override
    public Optional<String> getProperty(ExecutionAttributes val)
    {
        return Optional.ofNullable(executionProperties.get(val));
    }

    @Override
    public Set<String> executionKeys() {
        return executionProperties.keySet();
    }

    @Convert(converter = CatalogAttributeConverter.class)
    @Column(columnDefinition = "json", name = "custom_params")
    Map<String, String> customParams = new HashMap<>();

}
