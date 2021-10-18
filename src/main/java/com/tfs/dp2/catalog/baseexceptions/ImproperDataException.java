package com.tfs.dp2.catalog.baseexceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by bikesh.singh on 27-03-2018.
 */
@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason="Data is not proper")
public class ImproperDataException extends RuntimeException{
    public ImproperDataException(){}
    public ImproperDataException(String message){super(message);}
}
