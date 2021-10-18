package com.tfs.dp2.catalog.baseexceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by bikesh.singh on 22-08-2018.
 */
@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason="Rule Doesn't Exist")
public class RuleNotAvailableException extends RuntimeException {
    public RuleNotAvailableException(){}
    public RuleNotAvailableException(String message){super(message);}
}
