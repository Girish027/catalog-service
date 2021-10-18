package com.tfs.dp2.ingestion.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
public class StreamInfo implements Serializable {

    @NotNull
    @NotEmpty
    @JsonProperty("stream_name")
    private String streamName;

    @JsonProperty("contact_email")
    private String contact_email;

    @Valid
    @JsonProperty("parser_config")
    private ParserConfig parserConfig;

    @JsonProperty(value = "tags", required = true)
    private List<String> tags = new ArrayList<String>();
}
