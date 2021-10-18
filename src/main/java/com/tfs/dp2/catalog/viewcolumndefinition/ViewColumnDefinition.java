package com.tfs.dp2.catalog.viewcolumndefinition;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;

/**
 * Created by bikesh.singh on 27-02-2018.
 */


@Entity
@Table(name = "view_column_definition")
@EntityListeners(AuditingEntityListener.class)
@Data
@Getter
@Setter
public class ViewColumnDefinition implements Serializable {

    @Id
    @Column(name = "column_id")
    private String columnId;

    @Column(name = "definition_id")
    private String definitionId;

    @NotNull
    @NotEmpty
    @Column(name = "column_name")
    private String columnName;

    @NotNull
    @NotEmpty
    @Column(name = "column_display_name")
    private String columnDisplayName;

    @NotNull
    @NotEmpty
    @Column(name = "column_description")
    private String columnDescription;

    @Column(name = "view_id")
    private String viewId;

    @NotNull
    @NotEmpty
    @Column(name = "column_path")
    private String columnPath;

    @Column(name = "column_source")
    private String columnSource = "view";

    @NotNull
    @NotEmpty
    @Pattern(regexp = "(?:^|(?<= ))(String|date|binary|long|boolean|integer|array|struct|string|map|float)(?:(?= )|$)",
            message = "viewType should be one of the following: String, date, binary, long, boolean, integer, array, struct, string, map, float")
    @Column(name = "data_type")
    private String dataType;

    @Column(name = "data_length")
    private String dataLength = null;

    @NotNull
    @DecimalMax(value = "1", message = "isDimension should be 0 or 1")
    @DecimalMin(value = "0", message = "isDimension should be 0 or 1")
    @Column(columnDefinition="tinyint", name = "is_dimension")
    private int isDimension;

    @Column(columnDefinition="tinyint", name = "is_metrics")
    private int isMetric = 0;

//    @NotNull
//    @NotEmpty
//    @Pattern(regexp = "(?:^|(?<= ))(longSum|count|thetaSketch|InputThetaSketch|null)(?:(?= )|$)",
//            message = "aggregationType should be one of the following: longSum, count, thetaSketch, InputThetaSketch")
    @Column(name = "aggregation_type")
    private String aggregationType = null;

    @NotNull
//    @NotEmpty
//    @Pattern(regexp = "[01]")
    @DecimalMax(value = "1", message = "uniqueKeyFlag should be 0 or 1")
    @DecimalMin(value = "0", message = "uniqueKeyFlag should be 0 or 1")
    @Column(columnDefinition="tinyint", name = "unique_key_flag")
    private int uniqueKeyFlag;

    @Column(columnDefinition="tinyint", name = "unique_key_order")
    private int uniqueKeyOrder = 0;

    @Column(columnDefinition="int", name = "column_identifier")
    private int columnIdentifier = 0;

    @Column(columnDefinition="tinyint", name = "is_derived_column")
    private int isDerivedColumn = 0;

    @Column(columnDefinition = "default 'null'",name = "derived_syntax")
    private String derivedSyntax = null;

    @Column(columnDefinition="int", name = "index_set")
    private int indexSet = 0;

    @DecimalMin(value = "0", message = "indexOrder should not be negative")
    @Column(columnDefinition="int", name = "index_order")
    private int indexOrder = 0;

    @DecimalMax(value = "1", message = "isSensitive should be 0 or 1")
    @DecimalMin(value = "0", message = "isSensitive should be 0 or 1")
    @Column(columnDefinition="tinyint", name = "is_sensitive")
    private int isSensitive = 0;

    @Column(columnDefinition="bigint", name = "created_unixtimestamps")
    private long createdUnixtimestamps = System.currentTimeMillis();

    @Column(columnDefinition="bigint", name = "modified_unixtimestamps")
    private long modifiedUnixtimestamps = System.currentTimeMillis();

    @Column(name = "created_by")
    private String createdBy = "System";

    @Column(name = "modified_by")
    private String modifiedBy = "System";
}
