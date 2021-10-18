package com.tfs.dp2.catalog.baseexceptions;

import com.tfs.dp2.catalog.cob.response.entity.COBResponse;

public class COBException extends Exception {
    private COBResponse exceptionResponse;

    public COBException(COBResponse exceptionResponse) {
        this.exceptionResponse = exceptionResponse;
    }

    public COBResponse getExceptionResponse() {
        return exceptionResponse;
    }
}
