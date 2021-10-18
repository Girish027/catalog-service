package com.tfs.dp2.catalog.viewsource;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * Created by bikesh.singh on 09-03-2018.
 */

@Data
@Getter
@Setter
@Entity
@Table(name = "view_source")
@EntityListeners(AuditingEntityListener.class)
public class ViewSource {

    @Id
    @Column(name = "source_id")
    private String sourceId;

    @NotNull
    @Column(name = "source_view_id")
    private String sourceName;

    @Column(name = "view_id")
    private String viewId;

    @NotNull
    @NotEmpty
    @Pattern(regexp = "(?:^|(?<= ))(avro|tempView|View|ASSIST_INTERACTIONS|SEQ_JSON|JSON|PARQUET|CSV|REST|ASSIST_TRANSCRIPTS|ACTIVE_SHARE|SESSIONIZED_BINLOG|ASSIST_INTERACTIONS_V2|ASSIST_AGENTSTATS)(?:(?= )|$)",
            message = "sourceType should be one of the following: avro, tempView, View, ASSIST_INTERACTIONS, SEQ_JSON, JSON, PARQUET, CSV, REST, ASSIST_TRANSCRIPTS,ACTIVE_SHARE,SESSIONIZED_BINLOG , ASSIST_INTERACTIONS_V2 , ASSIST_AGENTSTATS")
    @Column(name = "source_type")
    private String sourceType;

    @Column(columnDefinition = "int", name = "source_order")
    private int sourceOrder = 1;

    @Column(name = "final_view_id")
    private String finalViewId;

    @NotNull
    @Column(columnDefinition = "int", name = "data_range_start_min")
    private int dataRangeStartMin;

    @NotNull
    @Column(columnDefinition = "int", name = "data_range_end_min")
    private int dataRangeEndMin;

    @Column(columnDefinition = "bigint", name = "created_unixtimestamps")
    private long createdUnixtimestamps = System.currentTimeMillis();

    @Column(columnDefinition = "bigint", name = "modified_unixtimestamps")
    private long modifiedUnixtimestamps = System.currentTimeMillis();

    @Column(name = "created_by")
    private String createdBy = "System";

    @Column(name = "modified_by")
    private String modifiedBy = "System";

    @Column(name = "view_alias")
    private String alias;
}