package com.tfs.dp2.catalog.services;

import com.tfs.dp2.catalog.baseexceptions.InvalidExecutionPropertiesException;
import com.tfs.dp2.catalog.baseexceptions.ViewGroupConstraintsException;
import com.tfs.dp2.catalog.cob.validator.ExecutionPropertyValidator;
import com.tfs.dp2.catalog.entities.ExecutionAttributes;
import com.tfs.dp2.catalog.entities.ViewGroup;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.tfs.dp2.catalog.view.CatalogConstants.SUCCESS;

@Slf4j
@Component
public class Validator {

    List<ExecutionPropertyValidator> validators;

    List<String> errorResponse;

    @Autowired
    public Validator(List<ExecutionPropertyValidator> validators) {
        this.validators = validators;
    }

    public Validator() { }



    private static final String INVALID_ATTRIBUTE = "Invalid or non existent attribute:%s";
    private static final String INVALID_ATTRIBUTES = "Invalid attributes:%s";

    protected void validate(ViewGroup viewGroup) throws ViewGroupConstraintsException {
        List<String> attributeValues = Stream.of(ExecutionAttributes.values())
                .map(value -> value.getValue()).collect(Collectors.toList());
        Set<String> executionKeys = viewGroup.executionKeys();
        executionKeys.removeAll(attributeValues);
        if (!executionKeys.isEmpty()) {
            String message = String.format(INVALID_ATTRIBUTES, executionKeys);
            log.error(message);
            throw new ViewGroupConstraintsException(message);
        }
    }

    protected Optional<String> validateQueue(ViewGroup viewGroup) throws ViewGroupConstraintsException {
        Optional<String> queue = viewGroup.getProperty(ExecutionAttributes.QUEUE);
        if (!queue.isPresent()) {
            String message = String.format(INVALID_ATTRIBUTE, ExecutionAttributes.QUEUE.getValue());
            log.error(message);
            throw new ViewGroupConstraintsException(message);
        }
        return queue;
    }

    public void validateExecutionProperties(Map<String,String> executionPropertires)
            throws InvalidExecutionPropertiesException {

        errorResponse = new ArrayList<>();
        for (ExecutionPropertyValidator validator : validators)
        {
            String response = validator.validate(executionPropertires);
            if(!response.equals(SUCCESS))
            {
                errorResponse.add(response);
            }
        }
        if (errorResponse.size() > 0)
        {
            String message = "Invalid values for spark execution properties";
            log.error(message);
            for(String err : errorResponse)
                log.error(err);
            throw new InvalidExecutionPropertiesException(message);
        }
    }
}
