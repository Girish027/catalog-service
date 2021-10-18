package com.tfs.dp2.catalog.cob.response.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.ToString;

@ToString(includeFieldNames=true)
public class COBResponse {
    @JsonProperty("message")
    private String message = null;

    public COBResponse(String message) {
        this.message = message;
    }

    /**
     * Get message
     *
     * @return message
     **/
    @ApiModelProperty(value = "")
    public String getMessage() {
        return message;
    }

}

