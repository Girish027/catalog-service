package com.tfs.dp2.catalog.baseexceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by bikesh.singh on 27-08-2018.
 */

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason="Mapping of view and client does not exist")
public class ClientViewMappingNotAvailableException extends RuntimeException{
    public ClientViewMappingNotAvailableException(){}
    public ClientViewMappingNotAvailableException(String message){super(message);}
}
