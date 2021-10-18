package com.tfs.dp2.catalog.cob.onboarder.impl.decorators;

import com.tfs.dp2.catalog.baseexceptions.COBInternalException;
import com.tfs.dp2.catalog.cob.onboarder.PipelineOnBoarderUtils;
import com.tfs.dp2.catalog.cob.onboarder.impl.ClientViewMapper;
import com.tfs.dp2.catalog.cob.request.entity.ValidateRequestBody;
import com.tfs.dp2.catalog.cob.response.entity.COBExceptionResponse;
import com.tfs.dp2.catalog.viewoutputgran.ClientViewMapping;
import com.tfs.dp2.catalog.viewoutputgran.DefaultClientViewMapping;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.Optional;

@Slf4j
public class ClientViewMapperDefaultDecorator extends ClientViewMapper {

    private PipelineOnBoarderUtils pipelineOnBoarderUtils;

    private ClientViewMapper clientViewMapper;

    public ClientViewMapperDefaultDecorator(ClientViewMapper clientViewMapper) {
        this.clientViewMapper = clientViewMapper;
        this.pipelineOnBoarderUtils = new PipelineOnBoarderUtils();
    }

    @Override
    public Optional<ClientViewMapping> map(ValidateRequestBody provisionRequestBody, ClientViewMapper.DefClientViewMap defClientViewMap) throws COBInternalException {
        log.info(String.format("Enriching mapping for client:%s and view:%s for product:%s",provisionRequestBody.getClientName(),defClientViewMap.getViewName(),getProduct(provisionRequestBody)));
        Optional<DefaultClientViewMapping> optionalDefaultClientViewMapping = mapDefaultMappingToPojo(provisionRequestBody, defClientViewMap);
        if(optionalDefaultClientViewMapping.isPresent()){
            Optional<ClientViewMapping> optionalClientViewMapping = this.clientViewMapper.map(provisionRequestBody, defClientViewMap);
            if(optionalClientViewMapping.isPresent())
            {
                ClientViewMapping clientViewMapping = optionalClientViewMapping.get();
                DefaultClientViewMapping defaultClientViewMapping = optionalDefaultClientViewMapping.get();
                enrichClientViewMappingWithDefaults(clientViewMapping, defaultClientViewMapping);
                return optionalClientViewMapping;

            }
        }
        return Optional.empty();
    }

    private void enrichClientViewMappingWithDefaults(ClientViewMapping clientViewMapping, DefaultClientViewMapping defaultClientViewMapping) throws COBInternalException {
        try {
            System.out.println("Copying properties from fromBean to toBean");
            BeanUtils.copyProperties(clientViewMapping, defaultClientViewMapping);
        } catch (IllegalAccessException | InvocationTargetException e) {
            String message = "Unable to map default client view mapping.";
            log.error(message,e);
            COBExceptionResponse cobExceptionResponse = new COBExceptionResponse(message,COBExceptionResponse.Code.NO_RETRY.toString());
            throw new COBInternalException(cobExceptionResponse);
        }

    }

    Optional<DefaultClientViewMapping> mapDefaultMappingToPojo(ValidateRequestBody provisionRequestBody, ClientViewMapper.DefClientViewMap defClientViewMap) throws COBInternalException {
        return pipelineOnBoarderUtils.getDefaultClientViewMapping(provisionRequestBody.getClientName(),defClientViewMap.getClientViewMapping());
    }

}
