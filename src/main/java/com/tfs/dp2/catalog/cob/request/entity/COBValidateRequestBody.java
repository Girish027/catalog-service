package com.tfs.dp2.catalog.cob.request.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

public class COBValidateRequestBody {

    @JsonProperty("product")
    @Getter
    @Setter
    private String product;

    @Getter
    @Setter
    private AccountProperties accountProperties;

}
