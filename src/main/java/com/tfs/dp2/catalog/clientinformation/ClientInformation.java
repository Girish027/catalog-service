package com.tfs.dp2.catalog.clientinformation;

import lombok.Data;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Created by bikesh.singh on 27-02-2018.
 */

@Data
@Entity
@Table(name = "client_information")
@EntityListeners(AuditingEntityListener.class)
public class ClientInformation implements Serializable {

    @Id
    @Column(name = "info_id")
    private String infoId;

    @Column(name = "parent_info_id")
    private String parentInfoId;

    @Column(name = "info_type")
    private String infoType;

    @Column(name = "info_name")
    private String infoName;

    @Column(name = "info_display_name")
    private String infoDisplayName;

    @Column(columnDefinition = "tinyint", name = "last_node_flag")
    private int lastNodeFlag;

    @Column(name = "timezone")
    private String timezone;

    @Column(columnDefinition = "bigint", name = "created_unixtimestamps")
    private long creatdUnixtimestamps = System.currentTimeMillis();

    @Column(columnDefinition = "bigint", name = "modified_unixtimestamps")
    private long modifiedUnixtimestamps = System.currentTimeMillis();

    @Column(name = "created_by")
    private String createdBy = "API";

    @Column(name = "modified_by")
    private String modifiedBy = "API";
}
