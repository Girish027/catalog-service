package com.tfs.dp2.catalog.baseexceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
public class ViewGroupException extends Exception {

    public ViewGroupException() {
    }

    public ViewGroupException(String message) {
        super(message);
    }
}
