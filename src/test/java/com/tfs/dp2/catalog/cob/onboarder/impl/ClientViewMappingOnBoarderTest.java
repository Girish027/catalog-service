package com.tfs.dp2.catalog.cob.onboarder.impl;

import com.tfs.dp2.catalog.baseexceptions.COBInternalException;
import com.tfs.dp2.catalog.baseexceptions.ClientAlreadyExistException;
import com.tfs.dp2.catalog.baseexceptions.ClientViewMappingAlreadyExistException;
import com.tfs.dp2.catalog.baseexceptions.InvalidExecutionPropertiesException;
import com.tfs.dp2.catalog.clientinformation.ClientInformationRepository;
import com.tfs.dp2.catalog.cob.onboarder.PipelineOnBoarder;
import com.tfs.dp2.catalog.cob.request.entity.AccountProperties;
import com.tfs.dp2.catalog.cob.request.entity.COBValidateRequestBody;
import com.tfs.dp2.catalog.cob.request.entity.ValidateRequestBody;
import com.tfs.dp2.catalog.view.ViewController;
import com.tfs.dp2.catalog.view.ViewRepository;
import com.tfs.dp2.catalog.viewoutputgran.ClientViewMapping;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RunWith(SpringRunner.class)
@WebMvcTest(value = ClientViewMappingOnBoarder.class)
public class ClientViewMappingOnBoarderTest {

    @MockBean
    private ClientInformationRepository clientInformationRepository;

    @MockBean
    private ViewRepository viewRepository;

    @MockBean
    private ViewController viewController;

    @Autowired
    private ClientViewMappingOnBoarder clientViewMappingOnBoarder;

    private String client = "premiumbank";
    private String account = "premiumbank";
    private String product = "Default";

    private COBValidateRequestBody cobValidateRequestBody;
    private ValidateRequestBody validateRequestBody = new ValidateRequestBody(client, account, Optional.of(getCOBValidateRequestBodyInstance()));

    private Optional<ClientViewMapping> optionalClientViewMapping;

    private String defClientViewMapping = "{\n" +
            "\t \"cronExpression\": \"17 6 * * *\",\n" +
            "\t \"reference\": -60,\n" +
            "\t \"granularity\": \"Daily\",\n" +
            "\t \"outputPath\": \"/Reports/prod/\",\n" +
            "\t \"weekendEnabled\": \"0\",\n" +
            "\t \"owner\": \"SUV\"\n" +
            "\t}";

    @Test
    public void testOnBoardRequestSuccess() throws COBInternalException, InvalidExecutionPropertiesException {

        Object[] obj1 = new Object[]{"View1", defClientViewMapping};
        Object[] obj2 = new Object[]{"View2", ""};
        List<Object[]> returnList = new ArrayList<>();
        returnList.add(obj1);
        returnList.add(obj2);

        Mockito.when(viewRepository.findByViewGroup(product)).thenReturn(returnList);
        Mockito.when(viewController.insertClientViewMapping(Mockito.any())).thenReturn("inserted");
        PipelineOnBoarder.ProvisionStatus provisionStatus = clientViewMappingOnBoarder.onBoard(validateRequestBody);
        Assert.assertEquals(provisionStatus, PipelineOnBoarder.ProvisionStatus.SUCCESSFULLY_PROVISIONED);

    }


    @Test
    public void testOnBoardRequestAlreadyProvisioned() throws COBInternalException {
        Object[] obj1 = new Object[]{"View1", ""};
        Object[] obj2 = new Object[]{"View2", ""};
        List<Object[]> returnList = new ArrayList<>();
        returnList.add(obj1);
        returnList.add(obj2);

        Mockito.when(viewRepository.findByViewGroup(product)).thenReturn(returnList);
        PipelineOnBoarder.ProvisionStatus provisionStatus = clientViewMappingOnBoarder.onBoard(validateRequestBody);
        Assert.assertEquals(provisionStatus, PipelineOnBoarder.ProvisionStatus.ALREADY_PROVISIONED);

    }

    @Test
    public void testOnBoardRequestExisting() throws COBInternalException, InvalidExecutionPropertiesException {

        Object[] obj1 = new Object[]{"View1", defClientViewMapping};
        Object[] obj2 = new Object[]{"View2", ""};
        List<Object[]> returnList = new ArrayList<>();
        returnList.add(obj1);
        returnList.add(obj2);

        Mockito.when(viewRepository.findByViewGroup(product)).thenReturn(returnList);
        Mockito.when(viewController.insertClientViewMapping(Mockito.any())).thenThrow(new ClientViewMappingAlreadyExistException());
        PipelineOnBoarder.ProvisionStatus provisionStatus = clientViewMappingOnBoarder.onBoard(validateRequestBody);
        Assert.assertEquals(provisionStatus, PipelineOnBoarder.ProvisionStatus.ALREADY_PROVISIONED);

    }

    @Test
    public void testOnBoardRequestWithError() throws COBInternalException, InvalidExecutionPropertiesException {

        Object[] obj1 = new Object[]{"View1", defClientViewMapping};
        Object[] obj2 = new Object[]{"View2", ""};
        List<Object[]> returnList = new ArrayList<>();
        returnList.add(obj1);
        returnList.add(obj2);

        Mockito.when(viewRepository.findByViewGroup(product)).thenReturn(returnList);
        Mockito.when(viewController.insertClientViewMapping(Mockito.any())).thenThrow(new RuntimeException());
        PipelineOnBoarder.ProvisionStatus provisionStatus = clientViewMappingOnBoarder.onBoard(validateRequestBody);
        Assert.assertEquals(provisionStatus, PipelineOnBoarder.ProvisionStatus.PROVISIONED_WITH_ERRORS);

    }

    @Test
    public void testBuildClientViewMappingRequest() throws COBInternalException {

        ClientViewMapper.DefClientViewMap defClientViewMap = new ClientViewMapper.DefClientViewMap("View", defClientViewMapping);
        Optional<ClientViewMapping> clientViewMapping = clientViewMappingOnBoarder.buildClientViewMappingRequest(validateRequestBody, defClientViewMap);
        Assert.assertTrue(clientViewMapping.isPresent());

    }

    @Test
    public void testBuildClientViewMappingRequestNullDefaultMapping() throws COBInternalException {

        ClientViewMapper.DefClientViewMap defClientViewMap = new ClientViewMapper.DefClientViewMap("View", null);
        Optional<ClientViewMapping> clientViewMapping = clientViewMappingOnBoarder.buildClientViewMappingRequest(validateRequestBody, defClientViewMap);
        Assert.assertTrue(!clientViewMapping.isPresent());

    }

    private COBValidateRequestBody getCOBValidateRequestBodyInstance() {
        if (cobValidateRequestBody == null) {
            cobValidateRequestBody = new COBValidateRequestBody();
            cobValidateRequestBody.setProduct(product);
            AccountProperties accountProperties = new AccountProperties();
            accountProperties.setTimezone("IST");
            cobValidateRequestBody.setAccountProperties(accountProperties);
        }
        return cobValidateRequestBody;
    }


}
