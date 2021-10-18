package com.tfs.dp2.catalog.cob.validator.impl;

import com.tfs.dp2.catalog.clientinformation.ClientInformation;
import com.tfs.dp2.catalog.clientinformation.ClientInformationRepository;
import com.tfs.dp2.catalog.cob.request.entity.ValidateRequestBody;
import com.tfs.dp2.catalog.cob.response.entity.*;
import com.tfs.dp2.catalog.cob.validator.ProvisionValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class ExistingMappingValidator extends ProvisionValidator {

    @Autowired
    private ClientInformationRepository clientInformationRepository;

    @Override
    public void validateRequest(ValidateRequestBody validateRequestBody, COBValidationExceptionResponse.Builder errorResponseBuilder) {
        String accountName = validateRequestBody.getAccountName();
        String clientName = validateRequestBody.getClientName();
        Optional<ClientInformation> clientInfo = clientInformationRepository.findClientInfoByAccountName(accountName);
        if (clientInfo.isPresent() && !clientInfo.get().getInfoName().equals(clientName)) {
            String message = String.format("Account:%s is already mapped to Client:%s", new Object[]{accountName, clientInfo.get().getInfoName()});
            log.error(message);
            ErrorProperty invalidProperty = new InvalidProperty(message);
            errorResponseBuilder.accountPropertiesNameError(invalidProperty);
        }
    }
}
