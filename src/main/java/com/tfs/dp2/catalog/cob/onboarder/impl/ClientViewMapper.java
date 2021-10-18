package com.tfs.dp2.catalog.cob.onboarder.impl;

import com.tfs.dp2.catalog.baseexceptions.COBInternalException;
import com.tfs.dp2.catalog.cob.request.entity.ValidateRequestBody;
import com.tfs.dp2.catalog.viewoutputgran.ClientViewMapping;

import java.util.Optional;

public abstract class ClientViewMapper {

    public abstract Optional<ClientViewMapping> map(ValidateRequestBody provisionRequestBody, ClientViewMapper.DefClientViewMap defClientViewMap) throws COBInternalException;

    public static class DefClientViewMap {
        String viewName;
        String clientViewMapping;

        public String getViewName() {
            return viewName;
        }

        public String getClientViewMapping() {
            return clientViewMapping;
        }

        public DefClientViewMap(String viewName, String clientViewMapping) {
            this.viewName = viewName;
            this.clientViewMapping = clientViewMapping;
        }
    }

    protected String getProduct(ValidateRequestBody provisionRequestBody){
        return provisionRequestBody.getCobValidateRequestBody().get().getProduct();

    }
}
