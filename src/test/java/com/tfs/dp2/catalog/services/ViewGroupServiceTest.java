package com.tfs.dp2.catalog.services;


import com.tfs.dp2.catalog.baseexceptions.InvalidExecutionPropertiesException;
import com.tfs.dp2.catalog.baseexceptions.ViewGroupConstraintsException;
import com.tfs.dp2.catalog.baseexceptions.ViewGroupException;
import com.tfs.dp2.catalog.entities.ViewGroup;
import com.tfs.dp2.catalog.repositories.ViewGroupRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.SQLException;
import java.util.HashMap;

@Slf4j
@RunWith(SpringRunner.class)
@WebMvcTest(value = ViewGroupService.class)
public class ViewGroupServiceTest {

    @Autowired
    private ViewGroupService viewGroupService;
    @MockBean
    private ViewGroupRepository viewGroupRepository;
    @MockBean
    private Validator validator;


    /**
     * Verifies if the view group is added.
     * @throws ViewGroupException
     * @throws ViewGroupConstraintsException
     */
    @Test
    public void addViewGroupSuccess() throws ViewGroupException, ViewGroupConstraintsException, InvalidExecutionPropertiesException {
        ViewGroup viewGroup = new ViewGroup();
        viewGroup.getExecutionProperties().put("queue", "test");
        ViewGroup savedEntity = new ViewGroup();
        Mockito.when(viewGroupRepository.save(viewGroup)).thenReturn(savedEntity);
        ViewGroup viewGroup1 = viewGroupService.addViewGroup(viewGroup);
        Assert.assertTrue(viewGroup1 == savedEntity);
    }

    /**
     * Verifies if the correct exception is being thrown
     */
    @Test
    public void addViewGroupFailure1() {
        ViewGroup viewGroup = new ViewGroup();
        viewGroup.setExecutionProperties(new HashMap<>());
        DataIntegrityViolationException dataIntegrityViolationException = new DataIntegrityViolationException("Test message", new SQLException());
        Mockito.when(viewGroupRepository.save(viewGroup)).thenThrow(dataIntegrityViolationException);

        try {
            viewGroupService.addViewGroup(viewGroup);
        } catch (Exception e) {
            Assert.assertTrue(e instanceof ViewGroupConstraintsException);
        }
    }

    /**
     * Verifies if the correct exception is being thrown
     */
    @Test
    public void addViewGroupFailure2() {
        ViewGroup viewGroup = new ViewGroup();
        viewGroup.getExecutionProperties().put("queue", "test");
        Mockito.when(viewGroupRepository.save(viewGroup)).thenThrow(new RuntimeException());

        try {
            viewGroupService.addViewGroup(viewGroup);
        } catch (Exception e) {
            Assert.assertTrue(e instanceof ViewGroupException);
        }
    }

}
