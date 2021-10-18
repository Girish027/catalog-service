package com.tfs.dp2.catalog.cob.onboarder.impl.decorators;

import com.tfs.dp2.catalog.baseexceptions.COBInternalException;
import com.tfs.dp2.catalog.cob.onboarder.impl.ClientViewMapper;
import com.tfs.dp2.catalog.cob.request.entity.AccountProperties;
import com.tfs.dp2.catalog.cob.request.entity.COBValidateRequestBody;
import com.tfs.dp2.catalog.cob.request.entity.ValidateRequestBody;
import com.tfs.dp2.catalog.viewoutputgran.ClientViewMapping;
import com.tfs.dp2.catalog.viewoutputgran.DefaultClientViewMapping;
import org.junit.Assert;
import org.junit.Test;

import java.util.Optional;

public class ClientViewMapperDefaultDecoratorTest {

    private String clientName = "premiumbank";
    private String accountName = "premiumbank";
    private String product = "Default";

    private String viewName = "premiumview";
    private String clientViewMapping = "{\n" +
            "\t \"cronExpression\": \"17 6 * * *\",\n" +
            "\t \"reference\": -60,\n" +
            "\t \"granularity\": \"Daily\",\n" +
            "\t \"outputPath\": \"/Reports/prod/\",\n" +
            "\t \"weekendEnabled\": \"0\",\n" +
            "\t \"owner\": \"SUV\"\n" +
            "\t}";

    private String invalidClientViewMapping = "{";

    private ValidateRequestBody provisionRequestBody;
    private ClientViewMapper.DefClientViewMap defClientViewMap = new ClientViewMapper.DefClientViewMap(viewName, clientViewMapping);
    private ClientViewMapper.DefClientViewMap defClientViewMapEmpty = new ClientViewMapper.DefClientViewMap(viewName, "");
    private ClientViewMapper.DefClientViewMap defClientViewMapInvalid = new ClientViewMapper.DefClientViewMap(viewName, invalidClientViewMapping);

    private ClientViewMapper clientViewMapper = new BasicClientViewMapper();

    @Test
    public void testMapProvisionRequest() throws COBInternalException {

        ClientViewMapperDefaultDecorator clientViewMapperDefaultDecorator = new ClientViewMapperDefaultDecorator(clientViewMapper);
        Optional<ClientViewMapping> optionalClientViewMapping = clientViewMapperDefaultDecorator.map(getValidateRequestBodyInstance(), defClientViewMap);

        Assert.assertTrue(optionalClientViewMapping.isPresent());
        Assert.assertEquals(optionalClientViewMapping.get().getOwner(), "SUV");
        Assert.assertEquals(optionalClientViewMapping.get().getCronExpression(), "17 6 * * *");
        Assert.assertEquals(optionalClientViewMapping.get().getReference(), "-60");
        Assert.assertEquals(optionalClientViewMapping.get().getGranularity(), "Daily");
        Assert.assertEquals(optionalClientViewMapping.get().getOutputPath(), "/Reports/prod/");
        Assert.assertEquals(optionalClientViewMapping.get().getWeekendEnabled(), "0");


    }

    @Test
    public void testMapDefaultMappingAbsent() throws COBInternalException {
        ClientViewMapperDefaultDecorator clientViewMapperDefaultDecorator = new ClientViewMapperDefaultDecorator(clientViewMapper);
        Optional<ClientViewMapping> optionalClientViewMapping = clientViewMapperDefaultDecorator.map(getValidateRequestBodyInstance(), defClientViewMapEmpty);
        Assert.assertTrue(!optionalClientViewMapping.isPresent());
    }


    @Test
    public void testMapDefaultMappingToPojo() throws COBInternalException {
        ClientViewMapperDefaultDecorator clientViewMapperDefaultDecorator = new ClientViewMapperDefaultDecorator(clientViewMapper);
        Optional<DefaultClientViewMapping> optionalDefaultClientViewMapping = clientViewMapperDefaultDecorator.mapDefaultMappingToPojo(getValidateRequestBodyInstance(), defClientViewMap);
        Assert.assertTrue(optionalDefaultClientViewMapping.isPresent());
        Assert.assertEquals(optionalDefaultClientViewMapping.get().getOwner(), "SUV");
        Assert.assertEquals(optionalDefaultClientViewMapping.get().getCronExpression(), "17 6 * * *");
        Assert.assertEquals(optionalDefaultClientViewMapping.get().getReference(), "-60");
        Assert.assertEquals(optionalDefaultClientViewMapping.get().getGranularity(), "Daily");
        Assert.assertEquals(optionalDefaultClientViewMapping.get().getOutputPath(), "/Reports/prod/");
        Assert.assertEquals(optionalDefaultClientViewMapping.get().getWeekendEnabled(), "0");
    }

    @Test
    public void testMapDefaultMappingToPojoInvalid() {
        ClientViewMapperDefaultDecorator clientViewMapperDefaultDecorator = new ClientViewMapperDefaultDecorator(clientViewMapper);
        try {
            Optional<DefaultClientViewMapping> optionalDefaultClientViewMapping = clientViewMapperDefaultDecorator.mapDefaultMappingToPojo(getValidateRequestBodyInstance(), defClientViewMapInvalid);
        } catch (COBInternalException e) {
            Assert.assertTrue(true);
        }

    }

    @Test
    public void testMapDefaultMappingToPojoEmpty() throws COBInternalException {
        ClientViewMapperDefaultDecorator clientViewMapperDefaultDecorator = new ClientViewMapperDefaultDecorator(clientViewMapper);
        Optional<DefaultClientViewMapping> optionalDefaultClientViewMapping = clientViewMapperDefaultDecorator.mapDefaultMappingToPojo(getValidateRequestBodyInstance(), defClientViewMapEmpty);
        Assert.assertTrue(!optionalDefaultClientViewMapping.isPresent());
    }

    private ValidateRequestBody getValidateRequestBodyInstance() {
        if (provisionRequestBody == null) {
            COBValidateRequestBody cobValidateRequestBody = new COBValidateRequestBody();
            cobValidateRequestBody.setProduct(product);
            AccountProperties accountProperties = new AccountProperties();
            accountProperties.setTimezone("IST");
            cobValidateRequestBody.setAccountProperties(accountProperties);
            provisionRequestBody = new ValidateRequestBody(clientName, accountName, Optional.of(cobValidateRequestBody));

        }
        return provisionRequestBody;
    }
}
