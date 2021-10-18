package com.tfs.dp2.catalog.cob.validator.impl;

import com.tfs.dp2.catalog.cob.request.entity.COBValidateRequestBody;
import com.tfs.dp2.catalog.cob.request.entity.ValidateRequestBody;
import com.tfs.dp2.catalog.cob.response.entity.COBValidationExceptionResponse;
import com.tfs.dp2.catalog.cob.response.entity.MissingProperty;
import com.tfs.dp2.catalog.cob.validator.ProvisionValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class ProductValidator extends ProvisionValidator {

    @Override
    public void validateRequest(ValidateRequestBody validateRequestBody, COBValidationExceptionResponse.Builder errorResponseBuilder) {
        Optional<COBValidateRequestBody> cobValidateRequestBody = validateRequestBody.getCobValidateRequestBody();
        if ((cobValidateRequestBody.isPresent() && (cobValidateRequestBody.get().getProduct() == null || cobValidateRequestBody.get().getProduct().equals(""))) || !cobValidateRequestBody.isPresent()) {
            log.error("Product cannot be null or empty.");
            errorResponseBuilder.productError(new MissingProperty());
        }
    }

}
