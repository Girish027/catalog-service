package com.tfs.dp2.catalog.cob.validator;

import com.tfs.dp2.catalog.cob.request.entity.ValidateRequestBody;
import com.tfs.dp2.catalog.cob.response.entity.COBValidationExceptionResponse;

import java.util.Optional;

public abstract class ProvisionValidator {
    protected Optional<ProvisionValidator> nextChain = Optional.empty();

    public void validate(ValidateRequestBody validateRequestBody, COBValidationExceptionResponse.Builder errorResponseBuilder) {
        if (validateRequestBody == null || errorResponseBuilder == null)
            throw new IllegalArgumentException("validateRequestBody or errorResponseBuilder cannot be null.");
        validateRequest(validateRequestBody, errorResponseBuilder);
        forwardToNextChain(validateRequestBody, errorResponseBuilder);
    }

    private void forwardToNextChain(ValidateRequestBody validateRequestBody, COBValidationExceptionResponse.Builder errorResponseBuilder) {
        if (nextChain.isPresent()) {
            nextChain.get().validate(validateRequestBody, errorResponseBuilder);
        }
    }

    abstract protected void validateRequest(ValidateRequestBody validateRequestBody, COBValidationExceptionResponse.Builder errorResponseBuilder);

    public void setNextChain(ProvisionValidator provisionValidator){
        this.nextChain = Optional.of(provisionValidator);
    }

}
