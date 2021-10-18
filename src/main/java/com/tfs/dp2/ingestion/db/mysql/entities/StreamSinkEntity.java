package com.tfs.dp2.ingestion.db.mysql.entities;

import com.tfs.dp2.ingestion.model.SinkConfig;
import lombok.Data;
import lombok.Getter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Data
@Entity
@Getter
@Table(name = "stream_sinks")
public class StreamSinkEntity implements Serializable {

    @Id
    @Column(name = "stream_id")
    private String stream_id;

    @Column(name = "sinks_configs", columnDefinition = "json")
    @Convert(converter = StreamSinkConverter.class)
    private List<SinkConfig> sinkConfigs;
}
