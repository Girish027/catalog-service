package com.tfs.dp2.catalog.cob.validator.impl;

import com.tfs.dp2.catalog.cob.request.entity.COBValidateRequestBody;
import com.tfs.dp2.catalog.cob.request.entity.ValidateRequestBody;
import com.tfs.dp2.catalog.cob.response.entity.COBValidationExceptionResponse;
import com.tfs.dp2.catalog.cob.response.entity.ErrorProperty;
import com.tfs.dp2.catalog.cob.response.entity.InvalidProperty;
import com.tfs.dp2.catalog.cob.response.entity.MissingProperty;
import com.tfs.dp2.catalog.cob.validator.ProvisionValidator;
import com.tfs.dp2.catalog.entities.ViewGroup;
import com.tfs.dp2.catalog.repositories.ViewGroupRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class ProductRegistryValidator extends ProvisionValidator {

    @Autowired
    private ViewGroupRepository viewGroupRepository;

    @Override
    public void validateRequest(ValidateRequestBody validateRequestBody, COBValidationExceptionResponse.Builder errorResponseBuilder) {
        String productName= validateRequestBody.getCobValidateRequestBody().get().getProduct();
        ViewGroup viewGroup = viewGroupRepository.findByName(productName);
        if (viewGroup == null) {
            String message = String.format("Product:%s is not registered.", productName);
            log.error(message);
            ErrorProperty invalidProperty = new InvalidProperty(message);
            errorResponseBuilder.productError(invalidProperty);
        }
    }
}
