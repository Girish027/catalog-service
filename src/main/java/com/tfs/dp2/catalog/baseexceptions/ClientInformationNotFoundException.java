package com.tfs.dp2.catalog.baseexceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by bikesh.singh on 27-02-2018.
 */

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason="not able to get client information")
public class ClientInformationNotFoundException extends RuntimeException {
    public ClientInformationNotFoundException(){}
    public ClientInformationNotFoundException(String message) {
        super(message);
    }
}
