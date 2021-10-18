package com.tfs.dp2.ingestion.db.mysql.entities;

import com.tfs.dp2.ingestion.model.ValidatorConfig;
import lombok.Data;
import lombok.Getter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Data
@Entity
@Getter
@Table(name = "stream_validators")
public class StreamValidatorEntity implements Serializable {

    @Id
    @Column(name = "stream_id")
    private String stream_id;

    @Column(name = "validator_configs", columnDefinition = "json")
    @Convert(converter = StreamValidatorConverter.class)
    private List<ValidatorConfig> validatorConfigs;
}
