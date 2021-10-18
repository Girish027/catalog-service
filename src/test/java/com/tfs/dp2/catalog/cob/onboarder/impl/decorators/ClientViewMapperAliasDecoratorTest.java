package com.tfs.dp2.catalog.cob.onboarder.impl.decorators;

import com.tfs.dp2.catalog.baseexceptions.COBInternalException;
import com.tfs.dp2.catalog.cob.onboarder.impl.ClientViewMapper;
import com.tfs.dp2.catalog.cob.request.entity.AccountProperties;
import com.tfs.dp2.catalog.cob.request.entity.COBValidateRequestBody;
import com.tfs.dp2.catalog.cob.request.entity.ValidateRequestBody;
import com.tfs.dp2.catalog.viewoutputgran.ClientViewMapping;
import org.junit.Assert;
import org.junit.Test;

import java.util.Optional;

public class ClientViewMapperAliasDecoratorTest {

    private String clientName = "premiumbank";
    private String accountName = "premiumbank";
    private String viewName = "premiumview";


    private ValidateRequestBody provisionRequestBody = null;
    private ClientViewMapper.DefClientViewMap defClientViewMap = new ClientViewMapper.DefClientViewMap(viewName, "");

    @Test
    public void testMapClientViewMapping() throws COBInternalException {

        Optional<ClientViewMapping> optionalClientViewMapping = Optional.of(new ClientViewMapping());
        ClientViewMapperAliasDecorator clientViewMapperAliasDecorator = new ClientViewMapperAliasDecorator(null) {
            @Override
            protected Optional<ClientViewMapping> getClientViewMappingFromParent(ValidateRequestBody provisionRequestBody, ClientViewMapper.DefClientViewMap defClientViewMap) {

                return optionalClientViewMapping;
            }

        };

        Assert.assertEquals(clientViewMapperAliasDecorator.map(getProvisionRequestBodyInstance(), defClientViewMap).get().getAlias().size(), 4);
    }

    @Test
    public void testMapEmptyClientViewMapping() throws COBInternalException {

        Optional<ClientViewMapping> optionalClientViewMapping = Optional.empty();
        ClientViewMapperAliasDecorator clientViewMapperAliasDecorator = new ClientViewMapperAliasDecorator(null) {
            @Override
            protected Optional<ClientViewMapping> getClientViewMappingFromParent(ValidateRequestBody provisionRequestBody, DefClientViewMap defClientViewMap) {

                return optionalClientViewMapping;
            }

        };

        Assert.assertEquals(clientViewMapperAliasDecorator.map(getProvisionRequestBodyInstance(), defClientViewMap), optionalClientViewMapping);
    }

    private ValidateRequestBody getProvisionRequestBodyInstance() {
        if (provisionRequestBody == null) {
            COBValidateRequestBody cobValidateRequestBody = new COBValidateRequestBody();
            cobValidateRequestBody.setProduct("Default");
            provisionRequestBody = new ValidateRequestBody(clientName, accountName, Optional.of(cobValidateRequestBody));
        }
        return provisionRequestBody;
    }
}
