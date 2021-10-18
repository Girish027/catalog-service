package com.tfs.dp2.catalog.cob.response.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.ToString;

import java.util.Optional;

@JsonInclude(JsonInclude.Include.NON_NULL)
@ToString(includeFieldNames=true)
public class COBValidationExceptionResponse {

    @JsonIgnore
    private Boolean modified = false;

    @JsonProperty("product")
    private ErrorProperty productError;

    @JsonProperty("accountProperties")
    private AccountPropertiesErrors accountPropertiesErrors;

    private COBValidationExceptionResponse(Builder builder) {
        this.productError = builder.productError;
        this.accountPropertiesErrors = builder.accountPropertiesErrors;
    }

    public ErrorProperty getProductError() {
        return productError;
    }

    public AccountPropertiesErrors getAccountPropertiesErrors() {
        return accountPropertiesErrors;
    }

    public static class Builder {

        private ErrorProperty productError;
        private AccountPropertiesErrors accountPropertiesErrors;

        public Builder productError(ErrorProperty val) {
            if (productError == null)
                productError = val;
            return this;
        }

        public Builder accountPropertiesNameError(ErrorProperty val) {
            createAccountPropertiesErrorsInstance();
            accountPropertiesErrors.setNameError(val);
            return this;
        }

        public Builder accountPropertiesTimeZoneError(ErrorProperty val) {
            createAccountPropertiesErrorsInstance();
            accountPropertiesErrors.setTimeZoneError(val);
            return this;
        }

        public Optional<COBValidationExceptionResponse> build() {
            if (productError != null || accountPropertiesErrors != null) {
                return Optional.of(new COBValidationExceptionResponse(this));
            }
            return Optional.empty();
        }

        private void createAccountPropertiesErrorsInstance() {
            if(accountPropertiesErrors == null)
                accountPropertiesErrors = new AccountPropertiesErrors();
        }
    }
}
