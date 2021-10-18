package com.tfs.dp2.catalog.cob.onboarder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tfs.dp2.catalog.baseexceptions.COBInternalException;
import com.tfs.dp2.catalog.clientinformation.Child;
import com.tfs.dp2.catalog.clientinformation.ClientToInsert;
import com.tfs.dp2.catalog.cob.response.entity.COBExceptionResponse;
import com.tfs.dp2.catalog.viewoutputgran.DefaultClientViewMapping;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class PipelineOnBoarderUtils {

    private ObjectMapper objectMapper;

    private List<String> exporters;

    public PipelineOnBoarderUtils() {
        objectMapper = new ObjectMapper();
    }

    public ClientToInsert formClientOnBoardingRequest(String clientName, String accountName, String timezone) {

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

    public ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    public Optional<DefaultClientViewMapping> getDefaultClientViewMapping(String clientName, String defaultClientViewMappingString) throws COBInternalException {
        Optional<DefaultClientViewMapping> defaultClientViewMapping = Optional.empty();
        ObjectMapper objectMapper = getObjectMapper();
        try {
            if (StringUtils.isNotBlank(defaultClientViewMappingString))
                return Optional.of(objectMapper.readValue(defaultClientViewMappingString, DefaultClientViewMapping.class));
        } catch (IOException e) {
            String message = String.format("Unable to parse default mappings of view %s for client %s", defaultClientViewMappingString, clientName);
            log.error(message, e);
            COBExceptionResponse cobExceptionResponse = new COBExceptionResponse(message, COBExceptionResponse.Code.NO_RETRY.toString());
            throw new COBInternalException(cobExceptionResponse);
        }
        log.error(String.format("Default view client mapping of view %s is empty or null for client %s", defaultClientViewMappingString, clientName));
        return defaultClientViewMapping;
    }
}
