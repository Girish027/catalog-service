package com.tfs.dp2.catalog.cob.request.entity.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tfs.dp2.catalog.baseexceptions.COBInternalException;
import com.tfs.dp2.catalog.baseexceptions.COBMalformedRequestException;
import com.tfs.dp2.catalog.baseexceptions.COBValidationException;
import com.tfs.dp2.catalog.cob.onboarder.PipelineOnBoarderUtils;
import com.tfs.dp2.catalog.cob.onboarder.impl.ClientOnBoarder;
import com.tfs.dp2.catalog.cob.onboarder.impl.ClientViewMappingOnBoarder;
import com.tfs.dp2.catalog.cob.onboarder.impl.ExporterOnBoarder;
import com.tfs.dp2.catalog.cob.request.entity.ValidateRequestBody;
import com.tfs.dp2.catalog.cob.response.entity.COBValidationExceptionResponse;
import com.tfs.dp2.catalog.cob.response.entity.MissingProperty;
import com.tfs.dp2.catalog.cob.validator.ProvisionValidator;
import com.tfs.dp2.catalog.cob.validator.impl.ExistingMappingValidator;
import com.tfs.dp2.catalog.cob.validator.impl.ProductRegistryValidator;
import com.tfs.dp2.catalog.cob.validator.impl.ProductValidator;
import com.tfs.dp2.catalog.cob.validator.impl.TimeZoneValidator;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

@Slf4j
@RunWith(SpringRunner.class)
@WebMvcTest(value = COBServiceHandler.class)
public class COBServiceHandlerTest {

    @Autowired
    COBServiceHandler cobServiceHandler;
    @MockBean
    private ProductValidator productValidator;
    @MockBean
    private ProductRegistryValidator productRegistryValidator;
    @MockBean
    private ExistingMappingValidator existingMappingValidator;
    @MockBean
    private TimeZoneValidator timeZoneValidator;

    @MockBean
    private ClientOnBoarder clientOnBoarder;

    @MockBean
    private ClientViewMappingOnBoarder clientViewMappingOnBoarder;

    @MockBean
    private ExporterOnBoarder exporterOnBoarder;

    @MockBean
    private PipelineOnBoarderUtils pipelineOnBoarderUtils;

    private String clientName = "premiumbank";
    private String accountName = "premiumbank";

    /**
     * Invalid request body.
     */
    @Test
    public void readJsonRequestFailure() {
        String requestBody = "{\n" +
                "  \"product\":s \"Default\",\n" +
                "  \"clientProperties\": {\n" +
                "    \"name\": \"sears\",\n" +
                "    \"admins\": [\n" +
                "      {\n" +
                "        \"name\": \"John\",\n" +
                "        \"email\": \"john@sears.com\"\n" +
                "      }\n" +
                "    ]\n" +
                "  },\n" +
                "  \"accountProperties\": {\n" +
                "    \"name\": \"sears_online\",\n" +
                "    \"vertical\": \"sales\",\n" +
                "    \"timezone\": \"UTC\",\n" +
                "    \"admins\": [\n" +
                "      {\n" +
                "        \"name\": \"Rob\",\n" +
                "        \"email\": \"rob@searshs.com\"\n" +
                "      }\n" +
                "    ]\n" +
                "  },\n" +
                "  \"configuration\": {\n" +
                "    \"currency\": \"USD\",\n" +
                "    \"phoneNumber\": \"+18005494505\"\n" +
                "  },\n" +
                "  \"volumetrics\": {\n" +
                "    \"agentConcurrency\": 3,\n" +
                "    \"maxAgents\": 500,\n" +
                "    \"peakStartWeek\": 10000,\n" +
                "    \"peakEndWeek\": 10000,\n" +
                "    \"avgPageViews\": 1000000,\n" +
                "    \"avgVisitors\": 1000000,\n" +
                "    \"peakPageViews\": 1000000,\n" +
                "    \"peakVisitors\": 1000000\n" +
                "  }\n" +
                "}";

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Mockito.when(pipelineOnBoarderUtils.getObjectMapper()).thenReturn(objectMapper);
            cobServiceHandler.retrieveRequestBodyPOJO(clientName, accountName, requestBody);
        } catch (COBMalformedRequestException e) {
            Assert.assertTrue(true);
            return;
        }
        Assert.assertTrue(false);

    }

    @Test
    public void readJsonRequestSuccess() {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Mockito.when(pipelineOnBoarderUtils.getObjectMapper()).thenReturn(objectMapper);
            Assert.assertTrue(cobServiceHandler.retrieveRequestBodyPOJO(clientName, accountName, requestBody) != null);
        } catch (COBMalformedRequestException e) {
            Assert.assertTrue(false);
        }

    }

    @Test
    public void readEmptyJsonRequestFailure() {
        String requestBody = "";

        try {
            cobServiceHandler.retrieveRequestBodyPOJO(clientName, accountName, requestBody);
        } catch (COBMalformedRequestException ex) {
            Assert.assertTrue(true);
            return;
        }
        Assert.assertTrue(false);


    }

    @Test
    public void testValidateClientProvisionRequestSuccess() {
        COBServiceHandler cobServiceHandler = new COBServiceHandler() {
            @Override
            ValidateRequestBody retrieveRequestBodyPOJO(String clientName, String accountName, String body) throws COBMalformedRequestException {
                return new ValidateRequestBody(clientName, accountName, null);
            }

            @Override
            COBValidationExceptionResponse.Builder validateProvisionRequest(ValidateRequestBody validateRequestBody) {
                return new COBValidationExceptionResponse.Builder();
            }
        };
        try {
            cobServiceHandler.validateClientProvisionRequest(clientName, accountName, requestBody);
        } catch (COBValidationException e) {
            Assert.assertTrue(false);
            return;
        } catch (COBInternalException e) {
            Assert.assertTrue(false);
            return;
        } catch (COBMalformedRequestException e) {
            Assert.assertTrue(false);
            return;
        }
        Assert.assertTrue(true);


    }

    @Test
    public void testValidateClientProvisionRequestFailureWithEmptyOrInvalidRequest() {
        try {
            cobServiceHandler.validateClientProvisionRequest(clientName, accountName, "");
        } catch (COBValidationException e) {
            Assert.assertTrue(false);
        } catch (COBInternalException e) {
            Assert.assertTrue(false);
        } catch (COBMalformedRequestException e) {
            Assert.assertTrue(true);
        }

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Mockito.when(pipelineOnBoarderUtils.getObjectMapper()).thenReturn(objectMapper);
            cobServiceHandler.validateClientProvisionRequest(clientName, accountName, "{");
        } catch (COBValidationException e) {
            Assert.assertTrue(false);
        } catch (COBInternalException e) {
            Assert.assertTrue(false);
        } catch (COBMalformedRequestException e) {
            Assert.assertTrue(true);
        }
    }

    @Test
    public void testValidateClientProvisionRequestValidationFailure() {
        COBServiceHandler cobServiceHandler = new COBServiceHandler() {
            @Override
            ValidateRequestBody retrieveRequestBodyPOJO(String clientName, String accountName, String body) throws COBMalformedRequestException {
                return new ValidateRequestBody(clientName, accountName, null);
            }

            @Override
            COBValidationExceptionResponse.Builder validateProvisionRequest(ValidateRequestBody validateRequestBody) {
                return new COBValidationExceptionResponse.Builder().productError(new MissingProperty());
            }
        };
        try {
            cobServiceHandler.validateClientProvisionRequest(clientName, accountName, "");
        } catch (COBValidationException e) {
            Assert.assertTrue(true);
        } catch (COBInternalException e) {
            Assert.assertTrue(false);
        } catch (COBMalformedRequestException e) {
            Assert.assertTrue(false);
        }
    }

    @Test
    public void testValidateProvisionRequestSuccess() {
        COBValidationExceptionResponse.Builder builder = new COBValidationExceptionResponse.Builder();
        COBServiceHandler cobServiceHandler = new COBServiceHandler() {
            ProvisionValidator chainProvisionValidators() {
                return productValidator;
            }

            COBValidationExceptionResponse.Builder validationExceptionResponseBuilder() {
                return builder;
            }
        };
        ValidateRequestBody validateRequestBody = new ValidateRequestBody(clientName, accountName, Optional.empty());
        Mockito.doNothing().when(productValidator).validateRequest(validateRequestBody, builder);

    }

    private String requestBody = "{\n" +
            "  \"product\": \"Default\",\n" +
            "  \"clientProperties\": {\n" +
            "    \"name\": \"sears\",\n" +
            "    \"admins\": [\n" +
            "      {\n" +
            "        \"name\": \"John\",\n" +
            "        \"email\": \"john@sears.com\"\n" +
            "      }\n" +
            "    ]\n" +
            "  },\n" +
            "  \"accountProperties\": {\n" +
            "    \"name\": \"sears_online\",\n" +
            "    \"vertical\": \"sales\",\n" +
            "    \"timezone\": \"UTC\",\n" +
            "    \"admins\": [\n" +
            "      {\n" +
            "        \"name\": \"Rob\",\n" +
            "        \"email\": \"rob@searshs.com\"\n" +
            "      }\n" +
            "    ]\n" +
            "  },\n" +
            "  \"configuration\": {\n" +
            "    \"currency\": \"USD\",\n" +
            "    \"phoneNumber\": \"+18005494505\"\n" +
            "  },\n" +
            "  \"volumetrics\": {\n" +
            "    \"agentConcurrency\": 3,\n" +
            "    \"maxAgents\": 500,\n" +
            "    \"peakStartWeek\": 10000,\n" +
            "    \"peakEndWeek\": 10000,\n" +
            "    \"avgPageViews\": 1000000,\n" +
            "    \"avgVisitors\": 1000000,\n" +
            "    \"peakPageViews\": 1000000,\n" +
            "    \"peakVisitors\": 1000000\n" +
            "  }\n" +
            "}";
}

