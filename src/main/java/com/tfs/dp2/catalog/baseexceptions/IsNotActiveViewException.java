package com.tfs.dp2.catalog.baseexceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by bikesh.singh on 27-02-2018.
 */

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason="Invalid view or view is not active")
public class IsNotActiveViewException extends RuntimeException {
    public IsNotActiveViewException(){}
    public IsNotActiveViewException(String message){
        super(message);
    }

}
