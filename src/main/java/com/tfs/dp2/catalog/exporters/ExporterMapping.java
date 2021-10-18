package com.tfs.dp2.catalog.exporters;

import lombok.Data;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by bikesh.singh on 07-01-2019.
 */

@Data
@Entity
@Table(name = "client_view_exporter_mapping")
@EntityListeners(AuditingEntityListener.class)
public class ExporterMapping implements Serializable {

    @Id
    @Column(name = "client_view_exporter_mapping_id")
    private String exporterMappingId;

    @Column(name = "client_id")
    private String clientId;

    @Column(name = "view_id")
    private String viewId;

    @Column(name = "exporter_id")
    private String exporterId;

    @Column(columnDefinition = "tinyint", name = "is_active")
    private int isActive;
}
