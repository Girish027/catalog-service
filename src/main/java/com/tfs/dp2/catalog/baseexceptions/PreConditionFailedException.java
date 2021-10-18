package com.tfs.dp2.catalog.baseexceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by bikesh.singh on 18-03-2018.
 */

@ResponseStatus(value = HttpStatus.PRECONDITION_FAILED)
public class PreConditionFailedException extends RuntimeException {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public PreConditionFailedException(){}
    public PreConditionFailedException(String message){super(message);}
}
