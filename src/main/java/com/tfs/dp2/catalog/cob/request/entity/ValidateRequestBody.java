package com.tfs.dp2.catalog.cob.request.entity;

import lombok.Getter;

import java.util.Optional;

public class ValidateRequestBody {
    @Getter
    private String clientName;
    @Getter
    private String accountName;
    @Getter
    private Optional<COBValidateRequestBody> cobValidateRequestBody = Optional.empty();


    public ValidateRequestBody(String clientName, String accountName, Optional<COBValidateRequestBody> cobValidateRequestBody) {
        this.clientName = clientName;
        this.accountName = accountName;
        this.cobValidateRequestBody = cobValidateRequestBody;
    }

}
