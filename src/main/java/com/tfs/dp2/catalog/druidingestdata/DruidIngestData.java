package com.tfs.dp2.catalog.druidingestdata;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * Created by bikesh.singh on 24-02-2018.
 */

@Entity
@Table(name = "druid_ingestion_data")
@EntityListeners(AuditingEntityListener.class)
@Data
public class DruidIngestData implements Serializable {

    @Id
    @Column(name = "druid_ingestion_id")
    private String druidIngestionId;

    @NotNull
    @NotEmpty
    @Pattern(regexp = "(?:^|(?<= ))(hadoopyString)(?:(?= )|$)",
            message = "parserType be one of these value: hadoopyString")
    @Column(name = "parser_type")
    private String parserType;

    @NotNull
    @NotEmpty
    @Pattern(regexp = "(?:^|(?<= ))(index_hadoop)(?:(?= )|$)",
            message = "druidIndexSpecType be one of these value: index_hadoop")
    @Column(name = "druid_index_spec_type")
    private String druidIndexSpecType;

    @NotNull
    @NotEmpty
    @Pattern(regexp = "(?:^|(?<= ))(hadoop)(?:(?= )|$)",
            message = "ioConfigType be one of these value: hadoop")
    @Column(name = "io_config_type")
    private String ioConfigType;

    @Column(name = "view_id")
    private String viewId;

    @NotNull
    @NotEmpty
    @Pattern(regexp = "(?:^|(?<= ))(NONE)(?:(?= )|$)",
            message = "queryGranularity be one of these value: NONE")
    @Column(name = "query_granularity")
    private String queryGranularity;

    @NotNull
    @NotEmpty
    @Pattern(regexp = "(?:^|(?<= ))(HOUR)(?:(?= )|$)",
            message = "segmentGranularity be one of these value: HOUR")
    @Column(name = "segment_granularity")
    private String segmentGranularity;

    @NotNull
    @NotEmpty
    @Pattern(regexp = "(?:^|(?<= ))(uniform)(?:(?= )|$)",
            message = "granularityType be one of these value: uniform")
    @Column(name = "granularity_type")
    private String granularityType;

    @NotNull
    @NotEmpty
    @Pattern(regexp = "(?:^|(?<= ))(static)(?:(?= )|$)",
            message = "inputSpecType be one of these value: static")
    @Column(name = "input_spec_type")
    private String inputSpecType;

    @NotNull
    @NotEmpty
    @Pattern(regexp = "(?:^|(?<= ))(auto)(?:(?= )|$)",
            message = "timestampFormat be one of these value: auto")
    @Column(name = "timestamp_format")
    private String timestampFormat;

    @NotNull
    @NotEmpty
    @Pattern(regexp = "(?:^|(?<= ))(json|csv)(?:(?= )|$)",
            message = "parserSpecFormat be one of these value: json")
    @Column(name = "parser_spec_format")
    private String parserSpecFormat;

    @Column(columnDefinition = "bigint", name = "created_unixtimestamps")
    private long createdUnixtimestamps = System.currentTimeMillis();

    @Column(columnDefinition = "bigint", name = "modified_unixtimestamps")
    private long modifiedUnixtimestamps = System.currentTimeMillis();

    @Column(name = "created_by")
    private String createdBy = "System";

    @Column(name = "modified_by")
    private String modifiedBy = "System";
}
