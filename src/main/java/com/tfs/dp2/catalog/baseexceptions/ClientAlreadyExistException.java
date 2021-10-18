package com.tfs.dp2.catalog.baseexceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by bikesh.singh on 18-03-2018.
 */
@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason="Client Already Exist")
public class ClientAlreadyExistException extends RuntimeException{
    public ClientAlreadyExistException(){}
    public ClientAlreadyExistException(String message){ super(message);}
}
