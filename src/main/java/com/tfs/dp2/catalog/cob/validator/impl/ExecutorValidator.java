package com.tfs.dp2.catalog.cob.validator.impl;

import com.tfs.dp2.catalog.cob.validator.AbstractExecutionPropertyValidator;
import com.tfs.dp2.catalog.entities.ExecutionAttributes;
import org.springframework.stereotype.Component;

import java.util.Map;

import static com.tfs.dp2.catalog.view.CatalogConstants.SUCCESS;

@Component
public class ExecutorValidator extends AbstractExecutionPropertyValidator {

    @Override
    public String validate(Map<String,String> execProps) {
        try {
            if (execProps.containsKey(ExecutionAttributes.NUM_EXECUTORS.getValue()) &&
                    Integer.parseInt(execProps.get(ExecutionAttributes.NUM_EXECUTORS.getValue()))  < 1) {
                return "Number of executors has invalid value";
            }
        }catch (NumberFormatException ex)
        {
            return "Number of executors has invalid number";
        }
        return SUCCESS;
    }
}
