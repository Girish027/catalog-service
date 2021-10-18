package com.tfs.dp2.catalog;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tfs.dp2.catalog.util.CatalogAttributeConverter;
import org.junit.Assert;
import org.junit.Test;

import javax.validation.constraints.AssertTrue;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

public class CatalogAttributeConverterTest {

    private CatalogAttributeConverter attributeConverter = new CatalogAttributeConverter();

    /**
     * Verifies conversion of Map to json equivalent.
     * @throws JsonProcessingException
     */
    @Test
    public void convertToDatabaseColumnSuccess(){
        Map<String, String> properties = new HashMap<>();
        properties.put("queue","dcfQueue");
        String output = attributeConverter.convertToDatabaseColumn(properties);
        String expected = "{\"queue\":\"dcfQueue\"}";
        Assert.assertEquals(expected,output);

    }

    /**
     * Verifies conversion of json to Map equivalent.
     */
    @Test
    public void convertToEntityAttributeSuccess(){
        Map<String, String> expected = new HashMap<>();
        expected.put("queue","dcfQueue");
        Map output = attributeConverter.convertToEntityAttribute("{\"queue\":\"dcfQueue\"}");
        Assert.assertEquals(expected,output);

    }
}
