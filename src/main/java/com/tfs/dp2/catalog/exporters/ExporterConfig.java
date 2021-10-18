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
@Table(name = "exporter_configuration")
@EntityListeners(AuditingEntityListener.class)
public class ExporterConfig implements Serializable {

    @Id
    @Column(name = "exporter_configuration_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int exporterConfigId;

    @Column(name = "exporter_id")
    private String exporterId;

    @NotNull
    @Column
    private String configKey;

    @NotNull
    @Column
    private String configValue;
}
