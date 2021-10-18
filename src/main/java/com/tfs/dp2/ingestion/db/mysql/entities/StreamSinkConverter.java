package com.tfs.dp2.ingestion.db.mysql.entities;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tfs.dp2.ingestion.exceptions.IngestionConfigInternalException;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.List;

@Slf4j
@Converter(autoApply = true)
public class StreamSinkConverter implements AttributeConverter<List<StreamSinkEntity>, String> {

    private static ObjectMapper mapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(List<StreamSinkEntity> attribute) {
        if (attribute == null)
            return null;
        try {
            return mapper.writeValueAsString(attribute);
        } catch (Exception e) {
            String message = e.getMessage();
            log.error(message, e);
            throw new IngestionConfigInternalException(message);
        }
    }

    @Override
    public List<StreamSinkEntity> convertToEntityAttribute(String dbData) {
        if (dbData == null)
            return null;
        try {
            return mapper.readValue(dbData,List.class );
        } catch (Exception e) {
            String message = e.getMessage();
            log.error(message, e);
            throw new IngestionConfigInternalException(message);
        }
    }
}
