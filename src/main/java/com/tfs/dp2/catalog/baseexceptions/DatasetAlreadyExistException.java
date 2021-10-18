package com.tfs.dp2.catalog.baseexceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by bikesh.singh on 30-08-2018.
 */
@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "dataset already exist for the view")
public class DatasetAlreadyExistException extends RuntimeException{
    public DatasetAlreadyExistException(){}
    public DatasetAlreadyExistException(String message){super(message);}
}
