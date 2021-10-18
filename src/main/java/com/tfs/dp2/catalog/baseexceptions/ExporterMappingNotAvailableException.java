package com.tfs.dp2.catalog.baseexceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by bikesh.singh on 08-01-2019.
 */

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Exporter is not mapped to view client job")
public class ExporterMappingNotAvailableException extends RuntimeException{
    public ExporterMappingNotAvailableException(){}
    public ExporterMappingNotAvailableException(String message){
        super(message);
    }
}
