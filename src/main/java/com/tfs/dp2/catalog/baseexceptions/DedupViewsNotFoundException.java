package com.tfs.dp2.catalog.baseexceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by bikesh.singh on 27-02-2018.
 */

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason="Not able to find dedup views")
public class DedupViewsNotFoundException extends RuntimeException {
    public DedupViewsNotFoundException(){}
    public DedupViewsNotFoundException(String s){
        super(s);
    }
}
