package com.tfs.dp2.catalog.cob.onboarder.impl.decorators;

import com.tfs.dp2.catalog.cob.onboarder.impl.ClientViewMapper;
import com.tfs.dp2.catalog.cob.request.entity.COBValidateRequestBody;
import com.tfs.dp2.catalog.cob.request.entity.ValidateRequestBody;
import org.junit.Assert;
import org.junit.Test;

import java.util.Optional;

public class BasicClientViewMapperTest {

    private String clientName = "premiumbank";
    private String accountName = "premiumbank";
    private String viewName = "premiumview";

    private ValidateRequestBody provisionRequestBody = null;
    @Test
    public void testMap() {
        BasicClientViewMapper basicClientViewMapper = new BasicClientViewMapper();
        ClientViewMapper.DefClientViewMap defClientViewMap = new ClientViewMapper.DefClientViewMap(viewName, "");
        Assert.assertTrue(basicClientViewMapper.map(getProvisionRequestBodyInstance(), defClientViewMap).isPresent());
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
