package com.tfs.dp2.ingestion.db.mysql.entities;

import com.tfs.dp2.ingestion.model.StreamInfo;
import lombok.Data;
import lombok.Getter;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Getter
@Table(name = "stream_info")
public class StreamInfoEntity implements Serializable {

    @Id
    @Column(name = "stream_id")
    private String stream_id;

    @Column(name = "stream_info", columnDefinition = "json")
    @Convert(converter = StreamInfoConverter.class)
    private StreamInfo stream_Info;

}
