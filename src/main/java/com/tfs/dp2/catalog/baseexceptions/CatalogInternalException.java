package com.tfs.dp2.catalog.baseexceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by bikesh.singh on 18-03-2018.
 */
@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR, reason="Internal error.")
public class CatalogInternalException extends RuntimeException{
    public CatalogInternalException(){}
    public CatalogInternalException(String message){ super(message);}
}
