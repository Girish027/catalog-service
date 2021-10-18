package com.tfs.dp2.catalog.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tfs.dp2.catalog.baseexceptions.CatalogInternalException;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.io.IOException;
import java.util.Map;

@Slf4j
@Converter(autoApply = true)
public class CatalogAttributeConverter implements AttributeConverter<Map, String> {

    @Override
    public String convertToDatabaseColumn(Map entityValue) {
        if (entityValue == null)
            return null;

        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(entityValue);
        } catch (Exception e) {
            String message = e.getMessage();
            log.error(message, e);
            throw new CatalogInternalException(message);
        }
    }

    @Override
    public Map<String,String> convertToEntityAttribute(String databaseValue) {
        if (databaseValue == null)
            return null;

        ObjectMapper mapper = new ObjectMapper();

        try {
            return mapper.readValue(databaseValue, Map.class);
        } catch (Exception e) {
            String message = e.getMessage();
            log.error(message, e);
            throw new CatalogInternalException(message);
        }

    }
}
