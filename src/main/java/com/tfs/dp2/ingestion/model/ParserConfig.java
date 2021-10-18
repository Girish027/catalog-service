package com.tfs.dp2.ingestion.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Map;

@Getter
public class ParserConfig implements Serializable {

    @NotNull
    @NotEmpty
    @JsonProperty("class")
    private String parserClass;

    @JsonProperty("properties")
    private Map <String,String> parserProperties;
}


