package com.tfs.dp2.catalog.cob.response.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.ToString;

/**
 * InvalidProperty
 */
@ToString(includeFieldNames=true)
public class InvalidProperty implements ErrorProperty {
    @JsonProperty("error_type")
    private String errorType = "invalid";

    @JsonProperty("reason")
    private String reason = null;

    public InvalidProperty(String reason) {
        this.reason = reason;
    }

    /**
     * Get errorType
     *
     * @return errorType
     **/
    @ApiModelProperty(example = "invalid", value = "")
    public String getErrorType() {
        return errorType;
    }


    /**
     * Get reason
     *
     * @return reason
     **/
    @ApiModelProperty(example = "invalid value", value = "")
    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

}

