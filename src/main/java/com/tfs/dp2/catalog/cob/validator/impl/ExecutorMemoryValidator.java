package com.tfs.dp2.catalog.cob.validator.impl;

import com.tfs.dp2.catalog.cob.validator.AbstractExecutionPropertyValidator;
import com.tfs.dp2.catalog.entities.ExecutionAttributes;
import org.springframework.stereotype.Component;

import java.util.Map;

import static com.tfs.dp2.catalog.view.CatalogConstants.SUCCESS;

@Component
public class ExecutorMemoryValidator extends AbstractExecutionPropertyValidator {

    @Override
    public String validate(Map<String,String> execProps)
    {
        if (execProps.containsKey(ExecutionAttributes.EXECUTOR_MEMORY.getValue())) {
            String executorMemory = execProps.get(ExecutionAttributes.EXECUTOR_MEMORY.getValue());
            if(!isValidMemoryspecifictaion(executorMemory))
            {
                return"Executor memory has invalid value";
            }
        }
        return SUCCESS;
    }
}