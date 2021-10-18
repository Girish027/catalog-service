package com.tfs.dp2.catalog.cob.validator.impl;

import com.tfs.dp2.catalog.cob.request.entity.COBValidateRequestBody;
import com.tfs.dp2.catalog.cob.request.entity.ValidateRequestBody;
import com.tfs.dp2.catalog.cob.request.entity.handler.COBServiceHandler;
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
@WebMvcTest(value = ProductValidator.class)
public class ProductValidatorTest {

    private String clientName = "premiumbank";
    private String accountName = "premiumbank";
    private COBValidateRequestBody cobValidateRequestBody = new COBValidateRequestBody();

    @Autowired
    private ProductValidator productValidator;
    @MockBean
    private ProductRegistryValidator productRegistryValidator;


    @Test
    public void testValidateRequestSuccess() {
        cobValidateRequestBody.setProduct("Default");
        ValidateRequestBody validateRequestBody = new ValidateRequestBody(clientName, accountName, Optional.of(cobValidateRequestBody));
        COBValidationExceptionResponse.Builder errorResponseBuilder = new COBValidationExceptionResponse.Builder();
        productValidator.validateRequest(validateRequestBody, errorResponseBuilder);
        productValidator.setNextChain(productRegistryValidator);
        Mockito.doNothing().when(productRegistryValidator).validateRequest(validateRequestBody, errorResponseBuilder);
        Assert.assertTrue(!errorResponseBuilder.build().isPresent());

    }

    @Test
    public void testValidateRequestFailureWhenProductIsEmpty() {
        cobValidateRequestBody.setProduct("");
        ValidateRequestBody validateRequestBody = new ValidateRequestBody(clientName, accountName, Optional.of(cobValidateRequestBody));
        COBValidationExceptionResponse.Builder errorResponseBuilder = new COBValidationExceptionResponse.Builder();
        productValidator.validateRequest(validateRequestBody, errorResponseBuilder);
        Assert.assertTrue(errorResponseBuilder.build().isPresent());

    }

    @Test
    public void testValidateRequestFailureWhenProductIsNotDefine() {
        cobValidateRequestBody.setProduct("");
        ValidateRequestBody validateRequestBody = new ValidateRequestBody(clientName, accountName, Optional.empty());
        COBValidationExceptionResponse.Builder errorResponseBuilder = new COBValidationExceptionResponse.Builder();
        productValidator.validateRequest(validateRequestBody, errorResponseBuilder);
        Assert.assertTrue(errorResponseBuilder.build().isPresent());

    }
}
