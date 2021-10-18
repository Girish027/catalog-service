package com.tfs.dp2.catalog.baseexceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason="Invalid values for execution properties")
public class InvalidExecutionPropertiesException extends Exception{

    public InvalidExecutionPropertiesException(){};
    public InvalidExecutionPropertiesException(String message){super(message);}
}
