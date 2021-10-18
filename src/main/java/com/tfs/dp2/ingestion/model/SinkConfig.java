package com.tfs.dp2.ingestion.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Map;

@Getter
@ToString
public class SinkConfig implements Serializable {

    @NotNull
    @NotEmpty
    @JsonProperty("sink_type")
    private String sinkType;

    @JsonProperty("sink_properties")
    private Map<String, String> sinkProperties ;
}
