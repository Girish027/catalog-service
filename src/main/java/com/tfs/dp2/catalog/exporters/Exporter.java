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
@Table(name = "exporters")
@EntityListeners(AuditingEntityListener.class)
public class Exporter implements Serializable {

    @NotNull
    @Id
    @Column(name = "exporter_id")
    private String exporterId;

    @NotNull
    @Column(name = "exporter_name")
    private String exporterName;

    @Column(name = "exporter_jar_loc")
    private String exporterJarLoc = null;

    @NotNull
    @Column(name = "exporter_main_class")
    private String exporterMainClass;
}
