package com.tfs.dp2.catalog.cob.response.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.ToString;

@ToString(callSuper = true,includeFieldNames=true)
public class COBExceptionResponse extends COBResponse {

    @JsonProperty("code")
    private String code = null;


    public COBExceptionResponse(String message, String code) {
        super(message);
        this.code = code;
    }

    /**
     * Get code
     *
     * @return code
     **/
    @ApiModelProperty(example = "RETRY/NO_RETRY", value = "")
    public String getCode() {
        return code;
    }

    public enum Code {
        RETRY,
        NO_RETRY
    }

}

