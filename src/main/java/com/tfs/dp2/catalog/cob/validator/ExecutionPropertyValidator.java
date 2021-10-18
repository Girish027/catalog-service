package com.tfs.dp2.catalog.cob.validator;

import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public interface ExecutionPropertyValidator {
    String validate(Map<String,String> execProps);
}


