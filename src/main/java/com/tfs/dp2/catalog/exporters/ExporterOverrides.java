package com.tfs.dp2.catalog.exporters;

import lombok.Data;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Created by bikesh.singh on 07-01-2019.
 */
@Data
@Entity
@Table(name = "client_view_exporter_overrides")
@EntityListeners(AuditingEntityListener.class)
public class ExporterOverrides implements Serializable {

    @Id
    @Column(name = "client_view_exporter_overrides_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int exporterOverridesId;

    @Column(name = "client_view_exporter_mapping_id")
    private String exporterMappingId;

    @NotNull
    @Column
    private String configKey;

    @NotNull
    @Column
    private String configValue;
}
