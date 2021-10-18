package com.tfs.dp2.catalog.baseexceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by bikesh.singh on 22-01-2019.
 */

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class InvalidActivityException extends RuntimeException {
    public InvalidActivityException(){}
    public InvalidActivityException(String message){super(message);}
}
