package com.tfs.dp2.catalog.baseexceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by bikesh.singh on 21-08-2018.
 */

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason="Rule Already Exist")
public class RuleAlreadyExistException extends RuntimeException{
    public RuleAlreadyExistException(){}
    public RuleAlreadyExistException(String message){super(message);}
}
