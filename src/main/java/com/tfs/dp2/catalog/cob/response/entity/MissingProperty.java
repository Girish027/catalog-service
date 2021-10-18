package com.tfs.dp2.catalog.cob.response.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.ToString;

/**
 * MissingProperty
 */
@ToString(includeFieldNames=true)
public class MissingProperty implements ErrorProperty {
    @JsonProperty("error_type")
    private String errorType = "missing";

    @ApiModelProperty(example = "missing", value = "")
    public String getErrorType() {
        return errorType;
    }
}

