package com.tfs.dp2.catalog.baseexceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by bikesh.singh on 29-03-2018.
 */
@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Number of unique columns is less than or more than one")
public class InvalidNumberOfUniqueColumnsException extends RuntimeException{
    public InvalidNumberOfUniqueColumnsException(){}
    public InvalidNumberOfUniqueColumnsException(String mesage){
        super(mesage);
    }
}
