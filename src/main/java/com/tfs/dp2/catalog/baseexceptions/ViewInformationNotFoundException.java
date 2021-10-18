package com.tfs.dp2.catalog.baseexceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by bikesh.singh on 27-02-2018.
 */

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason="Not able to get view information")
public class ViewInformationNotFoundException extends RuntimeException {
    public ViewInformationNotFoundException(){}
    public ViewInformationNotFoundException(String s){
        super(s);
    }
}
