package com.tfs.dp2.catalog.baseexceptions;

import com.tfs.dp2.catalog.cob.response.entity.COBResponse;

public class COBMalformedRequestException extends COBException {
    public COBMalformedRequestException(COBResponse exceptionResponse) {
        super(exceptionResponse);
    }
}
