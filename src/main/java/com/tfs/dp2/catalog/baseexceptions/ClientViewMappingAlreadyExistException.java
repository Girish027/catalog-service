package com.tfs.dp2.catalog.baseexceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by bikesh.singh on 18-03-2018.
 */

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason="Mapping of view and client already exist")
public class ClientViewMappingAlreadyExistException extends RuntimeException {
    public ClientViewMappingAlreadyExistException(){}
    public ClientViewMappingAlreadyExistException(String message){super(message);}
}
