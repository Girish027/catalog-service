package com.tfs.dp2.catalog.cob.onboarder.impl.decorators;

import com.tfs.dp2.catalog.cob.onboarder.impl.ClientViewMapper;
import com.tfs.dp2.catalog.cob.request.entity.ValidateRequestBody;
import com.tfs.dp2.catalog.viewoutputgran.ClientViewMapping;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Slf4j
public class BasicClientViewMapper extends ClientViewMapper {

    @Override
    public Optional<ClientViewMapping> map(ValidateRequestBody provisionRequestBody, DefClientViewMap defClientViewMap) {
        log.info(String.format("Initialing client:%s and view:%s mapping for product:%s",provisionRequestBody.getClientName(),defClientViewMap.getViewName(),getProduct(provisionRequestBody)));
        ClientViewMapping clientViewMapping = new ClientViewMapping();
        clientViewMapping.setClientName(provisionRequestBody.getAccountName());
        clientViewMapping.setViewName(defClientViewMap.getViewName());
        return Optional.of(clientViewMapping);
    }
}
