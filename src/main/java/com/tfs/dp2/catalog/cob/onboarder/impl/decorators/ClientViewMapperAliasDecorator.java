package com.tfs.dp2.catalog.cob.onboarder.impl.decorators;

import com.tfs.dp2.catalog.baseexceptions.COBInternalException;
import com.tfs.dp2.catalog.cob.onboarder.impl.ClientViewMapper;
import com.tfs.dp2.catalog.cob.request.entity.ValidateRequestBody;
import com.tfs.dp2.catalog.viewoutputgran.Alias;
import com.tfs.dp2.catalog.viewoutputgran.ClientViewMapping;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
public class ClientViewMapperAliasDecorator extends ClientViewMapperDefaultDecorator {

    public ClientViewMapperAliasDecorator(ClientViewMapper clientViewMapper) {
        super(clientViewMapper);
    }

    @Override
    public Optional<ClientViewMapping> map(ValidateRequestBody provisionRequestBody, ClientViewMapper.DefClientViewMap defClientViewMap) throws COBInternalException {
        log.info(String.format("Adding aliases for client:%s and view:%s mapping for product:%s",provisionRequestBody.getClientName(),defClientViewMap.getViewName(),getProduct(provisionRequestBody)));
        Optional<ClientViewMapping> optionalClientViewMapping = getClientViewMappingFromParent(provisionRequestBody,defClientViewMap);
        if (optionalClientViewMapping.isPresent()) {
            ClientViewMapping clientViewMapping = optionalClientViewMapping.get();
            clientViewMapping.setAlias(formAliases(provisionRequestBody.getClientName(), provisionRequestBody.getAccountName()));
        }
        return optionalClientViewMapping;
    }

    protected Optional<ClientViewMapping> getClientViewMappingFromParent(ValidateRequestBody provisionRequestBody, DefClientViewMap defClientViewMap) throws COBInternalException {
        return super.map(provisionRequestBody, defClientViewMap);
    }

    private List<Alias> formAliases(String clientName, String accountName) {
        List<Alias> aliases = new ArrayList<>();
        String aliasName = String.format("nemo-client-%s", clientName);
        for (AliasKeys value : AliasKeys.values()) {
            aliases.add(new Alias(aliasName,value.name()));
        }
        aliases.add(new Alias(accountName,"RawIDM"));
        return aliases;
    }

    private enum AliasKeys {
        AssistInteractions,
        AssistTickets,
        AssistTranscripts;

    }

}
