package com.tfs.dp2.catalog.cob.validator.impl;

import com.tfs.dp2.catalog.cob.validator.AbstractExecutionPropertyValidator;
import com.tfs.dp2.catalog.entities.ExecutionAttributes;
import org.springframework.stereotype.Component;

import java.util.Map;

import static com.tfs.dp2.catalog.view.CatalogConstants.SUCCESS;

@Component
public class DriverMemoryValidator extends AbstractExecutionPropertyValidator {

    @Override
    public String validate(Map<String,String> execProps)
    {
        if (execProps.containsKey(ExecutionAttributes.DRIVER_MEMORY.getValue())) {
            String driverMemory = execProps.get(ExecutionAttributes.DRIVER_MEMORY.getValue());
            if(!isValidMemoryspecifictaion(driverMemory))
            {
                return "Driver memory has invalid value";
            }
        }
        return SUCCESS;
    }

}
