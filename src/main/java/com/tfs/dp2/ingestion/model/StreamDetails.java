package com.tfs.dp2.ingestion.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
public class StreamDetails implements Serializable {

    @NotNull
    @NotEmpty
    @JsonProperty("stream_id")
    private String streamId;

    @NotNull
    @NotEmpty
    @JsonProperty("stream_name")
    private String streamName;

    @JsonProperty("contact_email")
    private String contact_email;

    @JsonProperty("parser_config")
    private ParserConfig parserConfig;

    @JsonProperty("validator_config")
    private List<ValidatorConfig> validatorConfig = new ArrayList<ValidatorConfig>();

    @JsonProperty("sink_config")
    private List <SinkConfig> sinkConfig = new ArrayList <SinkConfig>();

    @JsonProperty(value = "tags", required = true)
    private List <String> tags = new ArrayList <String> ();
}
