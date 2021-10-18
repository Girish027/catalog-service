package com.tfs.dp2.catalog.cob.validator.impl;

import com.tfs.dp2.catalog.clientinformation.ClientInformation;
import com.tfs.dp2.catalog.clientinformation.ClientInformationRepository;
import com.tfs.dp2.catalog.cob.request.entity.COBValidateRequestBody;
import com.tfs.dp2.catalog.cob.request.entity.ValidateRequestBody;
import com.tfs.dp2.catalog.cob.response.entity.COBValidationExceptionResponse;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

@RunWith(SpringRunner.class)
@WebMvcTest(value = ExistingMappingValidator.class)
public class ExistingMappingValidatorTest {

    @Autowired
    ExistingMappingValidator existingMappingValidator;
    @MockBean
    private ClientInformationRepository clientInformationRepository;

    private String clientName = "premiumbank";
    private String accountName = "premiumbank";
    String productName = "Default";

    private COBValidateRequestBody cobValidateRequestBody = new COBValidateRequestBody();

    @Test
    public void testValidateRequestSuccessWithSameExistingMapping() {
        cobValidateRequestBody.setProduct(productName);
        ValidateRequestBody validateRequestBody = new ValidateRequestBody(clientName, accountName, Optional.of(cobValidateRequestBody));
        COBValidationExceptionResponse.Builder errorResponseBuilder = new COBValidationExceptionResponse.Builder();
        ClientInformation clientInfo = new ClientInformation();
        clientInfo.setInfoName(clientName);
        Mockito.when(clientInformationRepository.findClientInfoByAccountName(accountName)).thenReturn(Optional.of(clientInfo));
        existingMappingValidator.validateRequest(validateRequestBody, errorResponseBuilder);
        Assert.assertTrue(!errorResponseBuilder.build().isPresent());

    }


    @Test
    public void testValidateRequestSuccessWithNoExistingMapping() {
        cobValidateRequestBody.setProduct(productName);
        ValidateRequestBody validateRequestBody = new ValidateRequestBody(clientName, accountName, Optional.of(cobValidateRequestBody));
        COBValidationExceptionResponse.Builder errorResponseBuilder = new COBValidationExceptionResponse.Builder();
        ClientInformation clientInfo = new ClientInformation();
        clientInfo.setInfoName(clientName);
        Mockito.when(clientInformationRepository.findClientInfoByAccountName(accountName)).thenReturn(Optional.empty());
        existingMappingValidator.validateRequest(validateRequestBody, errorResponseBuilder);
        Assert.assertTrue(!errorResponseBuilder.build().isPresent());

    }


    @Test
    public void testValidateRequestSuccessWithDifferentExistingMapping() {
        cobValidateRequestBody.setProduct(productName);
        ValidateRequestBody validateRequestBody = new ValidateRequestBody(clientName, accountName, Optional.of(cobValidateRequestBody));
        COBValidationExceptionResponse.Builder errorResponseBuilder = new COBValidationExceptionResponse.Builder();
        ClientInformation clientInfo = new ClientInformation();
        clientInfo.setInfoName(clientName + "1");
        Mockito.when(clientInformationRepository.findClientInfoByAccountName(accountName)).thenReturn(Optional.of(clientInfo));
        existingMappingValidator.validateRequest(validateRequestBody, errorResponseBuilder);
        Assert.assertTrue(errorResponseBuilder.build().isPresent());

    }


}
