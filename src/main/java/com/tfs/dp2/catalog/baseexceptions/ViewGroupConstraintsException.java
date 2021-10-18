package com.tfs.dp2.catalog.baseexceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class ViewGroupConstraintsException extends Exception {

    public ViewGroupConstraintsException() {
    }

    public ViewGroupConstraintsException(String message) {
        super(message);
    }
}
