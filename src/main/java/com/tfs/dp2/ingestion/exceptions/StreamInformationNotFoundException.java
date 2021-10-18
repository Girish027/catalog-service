package com.tfs.dp2.ingestion.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by mukesh.kumawat.
 */

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Stream does not exists!!")
public class StreamInformationNotFoundException extends Exception {
    public StreamInformationNotFoundException(){}
    public StreamInformationNotFoundException(String s){
        super(s);
    }
}
