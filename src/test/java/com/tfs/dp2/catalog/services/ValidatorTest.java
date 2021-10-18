package com.tfs.dp2.catalog.services;

import com.tfs.dp2.catalog.baseexceptions.InvalidExecutionPropertiesException;
import com.tfs.dp2.catalog.baseexceptions.ViewGroupConstraintsException;
import com.tfs.dp2.catalog.cob.validator.impl.DriverMemoryValidator;
import com.tfs.dp2.catalog.cob.validator.impl.ExecutorCoreValidator;
import com.tfs.dp2.catalog.cob.validator.impl.ExecutorMemoryValidator;
import com.tfs.dp2.catalog.cob.validator.impl.ExecutorValidator;
import com.tfs.dp2.catalog.entities.ViewGroup;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RunWith(SpringRunner.class)
public class ValidatorTest {

    @Autowired
    public Validator validator;

    @TestConfiguration
    static class ValidatorConfig {
        @Bean
        public Validator validator() {
         List validatorList = new ArrayList<>();
        validatorList.add(new DriverMemoryValidator());
        validatorList.add(new ExecutorValidator());
        validatorList.add(new ExecutorCoreValidator());
        validatorList.add(new ExecutorMemoryValidator());
            return new Validator(validatorList);
        }

    }
    /**
     * Verifies if the queue exists
     *
     * @throws ViewGroupConstraintsException
     */
    @Test
    public void validateQueueSuccess() throws ViewGroupConstraintsException {
        ViewGroup viewGroup = new ViewGroup();
        viewGroup.getExecutionProperties().put("queue", "test");
        Assert.assertTrue(validator.validateQueue(viewGroup).isPresent());
    }


    /**
     * Invalid queue attribute being stored.
     */
    @Test
    public void validateQueueFailure() {
        ViewGroup viewGroup = new ViewGroup();
        viewGroup.getExecutionProperties().put("Queue", "test");
        try {
            validator.validateQueue(viewGroup);
        } catch (Exception e) {
            Assert.assertTrue(e instanceof ViewGroupConstraintsException);
        }

    }


    /**
     * Validation of valid attributes
     *
     * @throws ViewGroupConstraintsException
     */
    @Test
    public void validateSuccess() throws ViewGroupConstraintsException {
        ViewGroup viewGroup = new ViewGroup();
        viewGroup.getExecutionProperties().put("queue", "test");
        validator.validate(viewGroup);
    }

    /**
     * Validation of invalid attributes.
     */
    @Test
    public void validateFailure() {
        ViewGroup viewGroup = new ViewGroup();
        viewGroup.getExecutionProperties().put("Queue1", "test");
        try {
            validator.validate(viewGroup);
        } catch (Exception e) {
            Assert.assertTrue(e instanceof ViewGroupConstraintsException);
        }
    }

    @Test
    public void validateExecProps() throws InvalidExecutionPropertiesException {
        HashMap<String,String> execProps = new HashMap<>();
        execProps.put("driver-memory","1800m");
        execProps.put("num-executors","8");
        execProps.put("executor-cores","5");
        execProps.put( "executor-memory","1g");
        validator.validateExecutionProperties(execProps);
    }

    @Test(expected = InvalidExecutionPropertiesException.class)
    public void validateExecPropsFailure() throws InvalidExecutionPropertiesException {
        HashMap<String,String> execProps = new HashMap<>();
        execProps.put("driver-memory","1800a");
        execProps.put("num-executors","-1");
        execProps.put("executor-cores","xyz");
        execProps.put( "executor-memory","15");
        validator.validateExecutionProperties(execProps);
    }
}
