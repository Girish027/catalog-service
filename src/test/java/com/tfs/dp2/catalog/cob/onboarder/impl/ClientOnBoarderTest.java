package com.tfs.dp2.catalog.cob.onboarder.impl;

import com.tfs.dp2.catalog.baseexceptions.COBInternalException;
import com.tfs.dp2.catalog.baseexceptions.ClientAlreadyExistException;
import com.tfs.dp2.catalog.clientinformation.Child;
import com.tfs.dp2.catalog.clientinformation.ClientToInsert;
import com.tfs.dp2.catalog.cob.onboarder.PipelineOnBoarder;
import com.tfs.dp2.catalog.cob.onboarder.PipelineOnBoarderUtils;
import com.tfs.dp2.catalog.cob.request.entity.AccountProperties;
import com.tfs.dp2.catalog.cob.request.entity.COBValidateRequestBody;
import com.tfs.dp2.catalog.cob.request.entity.ValidateRequestBody;
import com.tfs.dp2.catalog.view.ViewController;
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
@WebMvcTest(value = ClientOnBoarder.class)
public class ClientOnBoarderTest {

    @MockBean
    private ViewController viewController;

    @MockBean
    private PipelineOnBoarderUtils pipelineOnBoarderUtils;

    @Autowired
    private ClientOnBoarder clientOnBoarder;

    private String clientName = "premiumbank";
    private String accountName = "premiumbank";
    private String timezone = "UTC";

    private COBValidateRequestBody cobValidateRequestBody = new COBValidateRequestBody();

    @Test
    public void testOnBoardRequestNewProvision() throws COBInternalException {
        AccountProperties accountProperties = new AccountProperties();
        accountProperties.setTimezone(timezone);
        cobValidateRequestBody.setProduct("Default");
        cobValidateRequestBody.setAccountProperties(accountProperties);
        ValidateRequestBody provisionRequestBody = new ValidateRequestBody(clientName, accountName, Optional.of(cobValidateRequestBody));
        ClientToInsert clientToInsert = formClientOnBoardingRequest(clientName, accountName, timezone);
        Mockito.when(pipelineOnBoarderUtils.formClientOnBoardingRequest(clientName, accountName, timezone)).thenReturn(clientToInsert);
        Mockito.when(viewController.insertClient(clientToInsert)).thenReturn("inserted");
        PipelineOnBoarder.ProvisionStatus provisionStatus = clientOnBoarder.onBoard(provisionRequestBody);
        Assert.assertEquals(provisionStatus, PipelineOnBoarder.ProvisionStatus.SUCCESSFULLY_PROVISIONED);
    }

    @Test
    public void testOnBoardRequestProvisioned() throws COBInternalException {
        AccountProperties accountProperties = new AccountProperties();
        accountProperties.setTimezone(timezone);
        cobValidateRequestBody.setProduct("Default");
        cobValidateRequestBody.setAccountProperties(accountProperties);
        ValidateRequestBody provisionRequestBody = new ValidateRequestBody(clientName, accountName, Optional.of(cobValidateRequestBody));
        ClientToInsert clientToInsert = formClientOnBoardingRequest(clientName, accountName, timezone);
        Mockito.when(pipelineOnBoarderUtils.formClientOnBoardingRequest(clientName, accountName, timezone)).thenReturn(clientToInsert);
        Mockito.when(viewController.insertClient(clientToInsert)).thenThrow(new ClientAlreadyExistException());
        PipelineOnBoarder.ProvisionStatus provisionStatus = clientOnBoarder.onBoard(provisionRequestBody);
        Assert.assertEquals(provisionStatus, PipelineOnBoarder.ProvisionStatus.ALREADY_PROVISIONED);
    }

    private ClientToInsert formClientOnBoardingRequest(String clientName, String accountName, String timezone) {

        ClientToInsert clientToInsert = new ClientToInsert();
        clientToInsert.setClientName(clientName);
        clientToInsert.setTimezone(timezone);
        Child child = new Child();
        child.setChildName(accountName);
        child.hdfsFolder.add(accountName);
        child.hdfsFolder.add("nemo-client-" + clientName);
        clientToInsert.getChild().add(child);

        return clientToInsert;

    }


}
