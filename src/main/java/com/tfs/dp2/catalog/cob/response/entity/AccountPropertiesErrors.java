package com.tfs.dp2.catalog.cob.response.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.ToString;

@JsonInclude(JsonInclude.Include.NON_NULL)
@ToString(includeFieldNames=true)
public class AccountPropertiesErrors {
    @JsonProperty("name")
    private ErrorProperty nameError = null;

    @JsonProperty("timezone")
    private ErrorProperty timeZoneError = null;

    /**
     * @return name error details
     */
    @ApiModelProperty(value = "")
    public ErrorProperty getNameError() {
        return nameError;
    }

    /**
     * @param errorProperty
     */
    public void setNameError(ErrorProperty errorProperty) {
        this.nameError = errorProperty;
    }

    @ApiModelProperty(value = "")
    public ErrorProperty getTimeZoneError() {
        return timeZoneError;
    }

    public void setTimeZoneError(ErrorProperty errorProperty) {
        this.timeZoneError = errorProperty;
    }

}

