package com.tfs.dp2.catalog.entities;

import org.junit.Assert;
import org.junit.Test;

import java.util.Map;

public class ViewGroupTest {

    /**
     * Validates the queue value set as a part of the execution properties.
     */
    @Test
    public void getQueueSuccess(){
        ViewGroup viewGroup = new ViewGroup();
        viewGroup.getExecutionProperties().put("queue","test");
        Assert.assertEquals(viewGroup.getProperty(ExecutionAttributes.QUEUE).get(),"test");
    }


    /**
     * getQueue method return optional empty as no queue is set as a part of the execution properties.
     */
    @Test
    public void getQueueFailure1(){
        ViewGroup viewGroup = new ViewGroup();
        Assert.assertTrue(!viewGroup.getProperty(ExecutionAttributes.QUEUE).isPresent());
    }

    /**
     * getQueue method return optional empty as invalid queue is set as a part of the execution properties.
     */
    @Test
    public void getQueueFailure2(){
        ViewGroup viewGroup = new ViewGroup();
        viewGroup.getExecutionProperties().put("QueuE","test");
        Assert.assertTrue(!viewGroup.getProperty(ExecutionAttributes.QUEUE).isPresent());
    }
}
