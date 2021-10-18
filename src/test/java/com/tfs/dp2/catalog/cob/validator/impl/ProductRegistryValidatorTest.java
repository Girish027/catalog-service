package com.tfs.dp2.catalog.cob.validator.impl;

import com.tfs.dp2.catalog.cob.request.entity.COBValidateRequestBody;
import com.tfs.dp2.catalog.cob.request.entity.ValidateRequestBody;
import com.tfs.dp2.catalog.cob.response.entity.COBValidationExceptionResponse;
import com.tfs.dp2.catalog.entities.ViewGroup;
import com.tfs.dp2.catalog.repositories.ViewGroupRepository;
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
@WebMvcTest(value = ProductRegistryValidator.class)
public class ProductRegistryValidatorTest {

    private String clientName = "premiumbank";
    private String accountName = "premiumbank";
    String productName = "Default";

    private COBValidateRequestBody cobValidateRequestBody = new COBValidateRequestBody();

    @Autowired
    private ProductRegistryValidator productRegistryValidator;
    @MockBean
    private ViewGroupRepository viewGroupRepository;


    @Test
    public void testValidateRequestSuccess() {
        cobValidateRequestBody.setProduct(productName);
        ValidateRequestBody validateRequestBody = new ValidateRequestBody(clientName, accountName, Optional.of(cobValidateRequestBody));
        COBValidationExceptionResponse.Builder errorResponseBuilder = new COBValidationExceptionResponse.Builder();
        Mockito.when(viewGroupRepository.findByName(productName)).thenReturn(new ViewGroup());
        productRegistryValidator.validateRequest(validateRequestBody, errorResponseBuilder);
        Assert.assertTrue(!errorResponseBuilder.build().isPresent());
    }

    @Test
    public void testValidateRequestFailureWhenProductNotRegistered() {
        cobValidateRequestBody.setProduct(productName);
        ValidateRequestBody validateRequestBody = new ValidateRequestBody(clientName, accountName, Optional.of(cobValidateRequestBody));
        COBValidationExceptionResponse.Builder errorResponseBuilder = new COBValidationExceptionResponse.Builder();
        Mockito.when(viewGroupRepository.findByName(productName)).thenReturn(null);
        productRegistryValidator.validateRequest(validateRequestBody, errorResponseBuilder);
        Assert.assertTrue(errorResponseBuilder.build().isPresent());
    }

}
