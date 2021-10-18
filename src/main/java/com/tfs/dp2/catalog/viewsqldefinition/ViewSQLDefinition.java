package com.tfs.dp2.catalog.viewsqldefinition;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Created by bikesh.singh on 09-03-2018.
 */

@Entity
@Table(name = "view_sql_definition")
@EntityListeners(AuditingEntityListener.class)
@Data
public class ViewSQLDefinition {
    @Id
    @Column(name = "definition_id")
    private String definitionId;

    @Column(name = "definition_name")
    private String definitionName;

    @Column(name = "view_id")
    private String viewId;

    @Column(columnDefinition = "longtext", name = "sql_Definition")
    private String sqlDefinition;

    @Column(columnDefinition = "tinyint", name = "is_editable")
    private int isEditable = 0;

    @Column(columnDefinition = "tinyint", name = "is_materialize")
    private int isMaterialize = 1;

    @Column(name = "input_parameters")
    private String inputParameters = null;

    @Column(name = "output_parameters")
    private String outputParameters = null;

    @Column(columnDefinition = "bigint", name = "created_unixtimestamps")
    private long createdUnixtimestamps = System.currentTimeMillis();

    @Column(columnDefinition = "bigint", name = "modified_unixtimestamps")
    private long modifiedUnixtimestamps = System.currentTimeMillis();

    @Column(name = "created_by")
    private String createdBy = "System";

    @Column(name = "modified_by")
    private String modifiedBy = "System";
}
