package com.tfs.dp2.catalog.com.tfs.dp2.catalog.response.entity.cob;

import com.tfs.dp2.catalog.cob.response.entity.COBValidationExceptionResponse;
import com.tfs.dp2.catalog.cob.response.entity.MissingProperty;
import org.junit.Assert;
import org.junit.Test;

import java.util.Optional;

public class COBValidationExceptionResponseTest {

    /**
     * No errors encountered.
     */
    @Test
    public void noErrors() {
        Optional<COBValidationExceptionResponse> cobValidationExceptionResponse = new COBValidationExceptionResponse.Builder().build();
        Assert.assertTrue(!cobValidationExceptionResponse.isPresent());
    }

    /**
     * Errors encountered.
     */
    @Test
    public void errors() {
        COBValidationExceptionResponse.Builder builder = new COBValidationExceptionResponse.Builder();
        builder.productError(new MissingProperty());
        Assert.assertTrue(builder.build().isPresent());
    }

}
