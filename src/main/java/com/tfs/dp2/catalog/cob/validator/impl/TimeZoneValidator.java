package com.tfs.dp2.catalog.cob.validator.impl;

import com.tfs.dp2.catalog.cob.request.entity.COBValidateRequestBody;
import com.tfs.dp2.catalog.cob.request.entity.ValidateRequestBody;
import com.tfs.dp2.catalog.cob.response.entity.AccountPropertiesErrors;
import com.tfs.dp2.catalog.cob.response.entity.COBValidationExceptionResponse;
import com.tfs.dp2.catalog.cob.response.entity.ErrorProperty;
import com.tfs.dp2.catalog.cob.response.entity.MissingProperty;
import com.tfs.dp2.catalog.cob.validator.ProvisionValidator;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class TimeZoneValidator extends ProvisionValidator {

    @Override
    public void validateRequest(ValidateRequestBody validateRequestBody, COBValidationExceptionResponse.Builder errorResponseBuilder) {
        Optional<COBValidateRequestBody> cobValidateRequestBody = validateRequestBody.getCobValidateRequestBody();
        if (cobValidateRequestBody.isPresent()) {
            if (cobValidateRequestBody.get().getAccountProperties() == null ||
                    StringUtils.isBlank(cobValidateRequestBody.get().getAccountProperties().getTimezone())) {
                log.error("TimeZone cannot be null or empty.");
                ErrorProperty missingProperty = new MissingProperty();
                AccountPropertiesErrors accountPropertiesErrors = new AccountPropertiesErrors();
                errorResponseBuilder.accountPropertiesTimeZoneError(missingProperty);
            }

        }
    }

}
