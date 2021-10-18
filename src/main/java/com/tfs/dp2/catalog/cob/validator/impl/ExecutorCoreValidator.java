package com.tfs.dp2.catalog.cob.validator.impl;

import com.tfs.dp2.catalog.cob.validator.AbstractExecutionPropertyValidator;
import com.tfs.dp2.catalog.entities.ExecutionAttributes;
import org.springframework.stereotype.Component;

import java.util.Map;

import static com.tfs.dp2.catalog.view.CatalogConstants.SUCCESS;

@Component
public class ExecutorCoreValidator extends AbstractExecutionPropertyValidator {

    @Override
    public String validate(Map<String,String> execProps) {

        try {
            if (execProps.containsKey(ExecutionAttributes.EXECUTOR_CORES.getValue()) &&
                    Integer.parseInt(execProps.get(ExecutionAttributes.EXECUTOR_CORES.getValue()))  < 1) {
                return "Executor cores has invalid value";
            }
        }catch (NumberFormatException ex)
        {
            return "Executor cores has invalid number";
        }
        return SUCCESS;
    }
}
