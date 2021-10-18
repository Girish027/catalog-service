package com.tfs.dp2.catalog.cob.request.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

public class AccountProperties {

    @JsonProperty("timezone")
    @Getter
    @Setter
    private String timezone;
}
