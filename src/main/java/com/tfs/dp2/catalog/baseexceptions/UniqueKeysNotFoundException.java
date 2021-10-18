package com.tfs.dp2.catalog.baseexceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by bikesh.singh on 27-02-2018.
 */

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason="unique keys were not found")
public class UniqueKeysNotFoundException extends RuntimeException {
    public UniqueKeysNotFoundException(){}
    public UniqueKeysNotFoundException(String s){
        super(s);
    }
}
