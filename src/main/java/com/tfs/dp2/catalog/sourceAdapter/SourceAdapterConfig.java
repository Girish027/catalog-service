package com.tfs.dp2.catalog.sourceAdapter;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Getter
@Setter
@Entity
@Table(name = "source_adapter_config")
@EntityListeners(AuditingEntityListener.class)
public class SourceAdapterConfig implements Serializable
{
    @Id
    @Column(name = "source_config_id")
    private String sourceConfigId;

    @Column(name = "view_id")
    private String viewId;

    @Column(name = "source_type")
    private String sourceType;

    @Column(name = "key_name")
    private String keyName;

    @Column(name = "value_name")
    private String valueName;
}
