package com.tfs.dp2.catalog.baseexceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by bikesh.singh on 07-01-2019.
 */

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Exporter is not available.")
public class ExporterNotAvailableException extends RuntimeException{
    public ExporterNotAvailableException(){}
    public ExporterNotAvailableException(String message){
        super(message);
    }
}
