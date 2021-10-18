package com.tfs.dp2.catalog.cob.request.entity.handler;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tfs.dp2.catalog.baseexceptions.COBInternalException;
import com.tfs.dp2.catalog.baseexceptions.COBMalformedRequestException;
import com.tfs.dp2.catalog.baseexceptions.COBValidationException;
import com.tfs.dp2.catalog.cob.onboarder.PipelineOnBoarder;
import com.tfs.dp2.catalog.cob.onboarder.PipelineOnBoarderUtils;
import com.tfs.dp2.catalog.cob.onboarder.impl.ClientOnBoarder;
import com.tfs.dp2.catalog.cob.onboarder.impl.ClientViewMappingOnBoarder;
import com.tfs.dp2.catalog.cob.onboarder.impl.ExporterOnBoarder;
import com.tfs.dp2.catalog.cob.request.entity.COBValidateRequestBody;
import com.tfs.dp2.catalog.cob.request.entity.ValidateRequestBody;
import com.tfs.dp2.catalog.cob.response.entity.COBExceptionResponse;
import com.tfs.dp2.catalog.cob.response.entity.COBResponse;
import com.tfs.dp2.catalog.cob.response.entity.COBValidationExceptionResponse;
import com.tfs.dp2.catalog.cob.validator.ProvisionValidator;
import com.tfs.dp2.catalog.cob.validator.impl.ExistingMappingValidator;
import com.tfs.dp2.catalog.cob.validator.impl.ProductRegistryValidator;
import com.tfs.dp2.catalog.cob.validator.impl.ProductValidator;
import com.tfs.dp2.catalog.cob.validator.impl.TimeZoneValidator;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.Optional;

@Slf4j
@Service
public class COBServiceHandler {

    @Autowired
    private ProductValidator productValidator;
    @Autowired
    private ProductRegistryValidator productRegistryValidator;
    @Autowired
    private ExistingMappingValidator existingMappingValidator;
    @Autowired
    private TimeZoneValidator timeZoneValidator;

    @Autowired
    private ClientOnBoarder clientOnBoarder;

    @Autowired
    private ClientViewMappingOnBoarder clientViewMappingOnBoarder;

    @Autowired
    private ExporterOnBoarder exporterOnBoarder;

    @Autowired
    private PipelineOnBoarderUtils pipelineOnBoarderUtils;

    private PipelineOnBoarder pipelineOnBoarder;


    @PostConstruct
    public void initializeOnBoarders() {
        log.info("Initializing onboarders.");
        pipelineOnBoarder = clientOnBoarder;
        pipelineOnBoarder.setNextChain(clientViewMappingOnBoarder);
        clientViewMappingOnBoarder.setNextChain(exporterOnBoarder);
    }

    public COBResponse validateClientProvisionRequest(String clientName, String accountName, String body) throws COBValidationException, COBMalformedRequestException, COBInternalException {
        try {
            ValidateRequestBody validateRequestBody = retrieveRequestBodyPOJO(clientName, accountName, body);
            Optional<COBValidationExceptionResponse> optionalValidationResponse = validateProvisionRequest(validateRequestBody).build();
            if (optionalValidationResponse.isPresent()) {
                log.error("Validation failed.");
                throw new COBValidationException(optionalValidationResponse.get());
            }
        } catch (RuntimeException e) {
            String message = "Unable to validate COB request.";
            log.error(message, e);
            COBExceptionResponse cobExceptionResponse = new COBExceptionResponse(message, COBExceptionResponse.Code.NO_RETRY.toString());
            throw new COBInternalException(cobExceptionResponse);
        }
        return new COBResponse("validation successful. Ready for provisioning");
    }

    COBValidationExceptionResponse.Builder validateProvisionRequest(ValidateRequestBody validateRequestBody) {
        COBValidationExceptionResponse.Builder errorResponseBuilder = validationExceptionResponseBuilder();
        ProvisionValidator provisionValidator = chainProvisionValidators();
        provisionValidator.validate(validateRequestBody, errorResponseBuilder);
        return errorResponseBuilder;
    }

    ProvisionValidator chainProvisionValidators() {
        productValidator.setNextChain(productRegistryValidator);
        productRegistryValidator.setNextChain(existingMappingValidator);
        existingMappingValidator.setNextChain(timeZoneValidator);
        return productValidator;
    }

    ValidateRequestBody retrieveRequestBodyPOJO(String clientName, String accountName, String body) throws COBMalformedRequestException {
        return new ValidateRequestBody(clientName, accountName, parseCOBValidateRequest(body));
    }

    private Optional<COBValidateRequestBody> parseCOBValidateRequest(String body) throws COBMalformedRequestException {
        if (StringUtils.isBlank(body)) {
            COBResponse cobResponse = new COBResponse("request body cannot be null or empty");
            throw new COBMalformedRequestException(cobResponse);
        }
        Optional<COBValidateRequestBody> cobValidateRequestBody = Optional.empty();
        ObjectMapper mapper = pipelineOnBoarderUtils.getObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        try {
            cobValidateRequestBody = Optional.of(mapper.readValue(body, COBValidateRequestBody.class));
        } catch (IOException e) {
            String message = "Unable to parse request.";
            log.error(message, e);
            COBResponse exceptionResponse = new COBResponse(message);
            throw new COBMalformedRequestException(exceptionResponse);
        }
        return cobValidateRequestBody;
    }

    COBValidationExceptionResponse.Builder validationExceptionResponseBuilder() {
        return new COBValidationExceptionResponse.Builder();
    }

    public ResponseEntity<COBResponse> provisionClient(String clientName, String accountName, String body) throws COBMalformedRequestException, COBInternalException {
        PipelineOnBoarder.ProvisionStatus provisionStatus = PipelineOnBoarder.ProvisionStatus.SUCCESSFULLY_PROVISIONED;
        try {
            ValidateRequestBody provisionRequestBody = retrieveRequestBodyPOJO(clientName, accountName, body);
            provisionStatus = pipelineOnBoarder.onBoard(provisionRequestBody);
        } catch (RuntimeException e) {
            String message = "Unable to provision COB request.";
            log.error(message, e);
            COBExceptionResponse cobExceptionResponse = new COBExceptionResponse(message, COBExceptionResponse.Code.NO_RETRY.toString());
            throw new COBInternalException(cobExceptionResponse);
        }
        return provisionStatus.getResponse();
    }
}

