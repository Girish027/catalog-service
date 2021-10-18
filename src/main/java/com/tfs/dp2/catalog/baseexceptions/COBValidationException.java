package com.tfs.dp2.catalog.baseexceptions;

import com.tfs.dp2.catalog.cob.response.entity.COBValidationExceptionResponse;

public class COBValidationException extends Exception {
    private COBValidationExceptionResponse cobExceptionResponse;

    public COBValidationException(COBValidationExceptionResponse cobExceptionResponse) {
        this.cobExceptionResponse = cobExceptionResponse;
    }

    public COBValidationExceptionResponse getCobExceptionResponse() {
        return cobExceptionResponse;
    }
}
