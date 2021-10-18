package com.tfs.dp2.ingestion.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Stream already exists!!")
public class StreamAlreadyExistsException  extends Exception{
    public StreamAlreadyExistsException(){}
    public StreamAlreadyExistsException(String s){
        super(s);
    }
}