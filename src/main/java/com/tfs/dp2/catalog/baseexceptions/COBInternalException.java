package com.tfs.dp2.catalog.baseexceptions;

import com.tfs.dp2.catalog.cob.response.entity.COBExceptionResponse;

public class COBInternalException extends Exception {
    private COBExceptionResponse cobInternalExceptionResponse;

    public COBInternalException(COBExceptionResponse cobInternalExceptionResponse) {
        this.cobInternalExceptionResponse = cobInternalExceptionResponse;
    }

    public COBExceptionResponse getCobInternalExceptionResponse() {
        return cobInternalExceptionResponse;
    }
}
