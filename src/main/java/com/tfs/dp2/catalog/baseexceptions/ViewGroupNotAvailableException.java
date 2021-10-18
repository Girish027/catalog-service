package com.tfs.dp2.catalog.baseexceptions;

import com.tfs.dp2.catalog.util.Messages;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by bikesh.singh on 26-03-2018.
 */

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class ViewGroupNotAvailableException extends Exception{
    public ViewGroupNotAvailableException(){}
    public ViewGroupNotAvailableException(String message){
        super(message);
    }
}
