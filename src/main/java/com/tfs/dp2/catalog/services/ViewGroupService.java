package com.tfs.dp2.catalog.services;

import com.tfs.dp2.catalog.baseexceptions.InvalidExecutionPropertiesException;
import com.tfs.dp2.catalog.baseexceptions.ViewGroupConstraintsException;
import com.tfs.dp2.catalog.baseexceptions.ViewGroupException;
import com.tfs.dp2.catalog.entities.ViewGroup;
import com.tfs.dp2.catalog.repositories.ViewGroupRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
public class ViewGroupService {

    private static final String VIEW_GROUP = "viewGroup_";


    @Autowired
    private ViewGroupRepository viewGroupRepository;
    @Autowired
    private Validator validator;

    public ViewGroup addViewGroup(ViewGroup viewGroup) throws ViewGroupConstraintsException, ViewGroupException, InvalidExecutionPropertiesException {

        validator.validate(viewGroup);
        validator.validateQueue(viewGroup);
        validator.validateExecutionProperties(viewGroup.getExecutionProperties());

        UUID uuidClient = UUID.randomUUID();
        String viewGroupId = VIEW_GROUP + uuidClient.toString();
        viewGroup.setId(viewGroupId);
        ViewGroup savedEntity = null;
        try {
            savedEntity = viewGroupRepository.save(viewGroup);
        } catch (DataIntegrityViolationException exception) {
            String message = exception.getRootCause().getMessage();
            log.error(message, exception);
            throw new ViewGroupConstraintsException(message);
        } catch (Exception exception) {
            String message = exception.getMessage();
            log.error(message, exception);
            throw new ViewGroupException(message);
        }

        return savedEntity;
    }

}
