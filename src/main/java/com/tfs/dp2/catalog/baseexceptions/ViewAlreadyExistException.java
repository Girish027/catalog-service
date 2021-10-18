package com.tfs.dp2.catalog.baseexceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by bikesh.singh on 26-03-2018.
 */

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason="view already exist in database")
public class ViewAlreadyExistException extends RuntimeException{
    public ViewAlreadyExistException(){}
    public ViewAlreadyExistException(String message){
        super(message);
    }
}
