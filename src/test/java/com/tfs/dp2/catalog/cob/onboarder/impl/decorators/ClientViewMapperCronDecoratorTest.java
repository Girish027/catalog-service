package com.tfs.dp2.catalog.cob.onboarder.impl.decorators;

import com.tfs.dp2.catalog.baseexceptions.COBInternalException;
import com.tfs.dp2.catalog.cob.onboarder.impl.ClientViewMapper;
import com.tfs.dp2.catalog.cob.request.entity.COBValidateRequestBody;
import com.tfs.dp2.catalog.cob.request.entity.ValidateRequestBody;
import com.tfs.dp2.catalog.viewoutputgran.ClientViewMapping;
import org.junit.Assert;
import org.junit.Test;

import java.util.Optional;

public class ClientViewMapperCronDecoratorTest {

    private String clientName = "premiumbank";
    private String accountName = "premiumbank";
    private String viewName = "premiumview";


    private ValidateRequestBody provisionRequestBody = null;

    @Test
    public void testUpdateHourAndMinuteCron1() throws COBInternalException {
        ClientViewMapperCronDecorator clientViewMapperCronDecorator = new ClientViewMapperCronDecorator(null);
        ClientViewMapperCronDecorator.OozieCron oozieCron = clientViewMapperCronDecorator.parseCronExpression("* 6 * * 1");
        ClientViewMapperCronDecorator.TimeZoneOffset timeZoneOffset = clientViewMapperCronDecorator.getOffSet("Asia/Calcutta");
        ClientViewMapperCronDecorator.NonHourlyGrains.Weekly.updateCronToClientTimeZone(oozieCron, timeZoneOffset);
        Assert.assertEquals(oozieCron.toString(), "30 0 * * 1");
    }

    @Test
    public void testUpdateHourAndMinuteCron2() throws COBInternalException {
        ClientViewMapperCronDecorator clientViewMapperCronDecorator = new ClientViewMapperCronDecorator(null);
        ClientViewMapperCronDecorator.OozieCron oozieCron = clientViewMapperCronDecorator.parseCronExpression("15 4 * * 1");
        ClientViewMapperCronDecorator.TimeZoneOffset timeZoneOffset = clientViewMapperCronDecorator.getOffSet("IST");
        ClientViewMapperCronDecorator.NonHourlyGrains.Weekly.updateCronToClientTimeZone(oozieCron, timeZoneOffset);
        Assert.assertEquals(oozieCron.toString(), "45 22 * * 7");
    }

    @Test
    public void testUpdateHourAndMinuteCron3() throws COBInternalException {
        ClientViewMapperCronDecorator clientViewMapperCronDecorator = new ClientViewMapperCronDecorator(null);
        ClientViewMapperCronDecorator.OozieCron oozieCron = clientViewMapperCronDecorator.parseCronExpression("45 4 * * 2");
        ClientViewMapperCronDecorator.TimeZoneOffset timeZoneOffset = clientViewMapperCronDecorator.getOffSet("IST");
        ClientViewMapperCronDecorator.NonHourlyGrains.Weekly.updateCronToClientTimeZone(oozieCron, timeZoneOffset);
        Assert.assertEquals(oozieCron.toString(), "15 23 * * 1");
    }

    @Test
    public void testUpdateHourAndMinuteCron4() throws COBInternalException {
        ClientViewMapperCronDecorator clientViewMapperCronDecorator = new ClientViewMapperCronDecorator(null);
        ClientViewMapperCronDecorator.OozieCron oozieCron = clientViewMapperCronDecorator.parseCronExpression("30 5 * * 3");
        ClientViewMapperCronDecorator.TimeZoneOffset timeZoneOffset = clientViewMapperCronDecorator.getOffSet("IST");
        ClientViewMapperCronDecorator.NonHourlyGrains.Weekly.updateCronToClientTimeZone(oozieCron, timeZoneOffset);
        Assert.assertEquals(oozieCron.toString(), "0 0 * * 3");
    }

    @Test
    public void testUpdateHourAndMinuteCron5() throws COBInternalException {
        ClientViewMapperCronDecorator clientViewMapperCronDecorator = new ClientViewMapperCronDecorator(null);
        ClientViewMapperCronDecorator.OozieCron oozieCron = clientViewMapperCronDecorator.parseCronExpression("30 23 * * 7");
        ClientViewMapperCronDecorator.TimeZoneOffset timeZoneOffset = clientViewMapperCronDecorator.getOffSet("PST");
        ClientViewMapperCronDecorator.NonHourlyGrains.Weekly.updateCronToClientTimeZone(oozieCron, timeZoneOffset);
        Assert.assertEquals(oozieCron.toString(), "30 7 * * 1");
    }

    @Test
    public void testUpdateHourAndMinuteCron6() throws COBInternalException {
        ClientViewMapperCronDecorator clientViewMapperCronDecorator = new ClientViewMapperCronDecorator(null);
        ClientViewMapperCronDecorator.OozieCron oozieCron = clientViewMapperCronDecorator.parseCronExpression("30 23 * * 7");
        ClientViewMapperCronDecorator.TimeZoneOffset timeZoneOffset = new ClientViewMapperCronDecorator.TimeZoneOffset(-10l, 30l);
        ClientViewMapperCronDecorator.NonHourlyGrains.Weekly.updateCronToClientTimeZone(oozieCron, timeZoneOffset);
        Assert.assertEquals(oozieCron.toString(), "0 10 * * 1");
    }

    @Test
    public void testUpdateHourAndMinuteCron7() throws COBInternalException {
        ClientViewMapperCronDecorator clientViewMapperCronDecorator = new ClientViewMapperCronDecorator(null);
        ClientViewMapperCronDecorator.OozieCron oozieCron = clientViewMapperCronDecorator.parseCronExpression("30 13 * * 4");
        ClientViewMapperCronDecorator.TimeZoneOffset timeZoneOffset = new ClientViewMapperCronDecorator.TimeZoneOffset(-10l, 30l);
        ClientViewMapperCronDecorator.NonHourlyGrains.Weekly.updateCronToClientTimeZone(oozieCron, timeZoneOffset);
        Assert.assertEquals(oozieCron.toString(), "0 0 * * 5");
    }

    @Test
    public void testUpdateHourAndMinuteCron8() throws COBInternalException {
        ClientViewMapperCronDecorator clientViewMapperCronDecorator = new ClientViewMapperCronDecorator(null);
        ClientViewMapperCronDecorator.OozieCron oozieCron = clientViewMapperCronDecorator.parseCronExpression("30 12 * * 2");
        ClientViewMapperCronDecorator.TimeZoneOffset timeZoneOffset = new ClientViewMapperCronDecorator.TimeZoneOffset(-10l, 30l);
        ClientViewMapperCronDecorator.NonHourlyGrains.Weekly.updateCronToClientTimeZone(oozieCron, timeZoneOffset);
        Assert.assertEquals(oozieCron.toString(), "0 23 * * 2");
    }


    @Test
    public void testUpdateHourAndMinuteCron9() throws COBInternalException {
        ClientViewMapperCronDecorator clientViewMapperCronDecorator = new ClientViewMapperCronDecorator(null);
        ClientViewMapperCronDecorator.OozieCron oozieCron = clientViewMapperCronDecorator.parseCronExpression("30 23 * * 5");
        ClientViewMapperCronDecorator.TimeZoneOffset timeZoneOffset = clientViewMapperCronDecorator.getOffSet("UTC");
        ClientViewMapperCronDecorator.NonHourlyGrains.Weekly.updateCronToClientTimeZone(oozieCron, timeZoneOffset);
        Assert.assertEquals(oozieCron.toString(), "30 23 * * 5");
    }

    @Test
    public void testUpdateHourAndMinuteCron10() {
        ClientViewMapperCronDecorator clientViewMapperCronDecorator = new ClientViewMapperCronDecorator(null);
        ClientViewMapperCronDecorator.OozieCron oozieCron = clientViewMapperCronDecorator.parseCronExpression("30 23 * * *");
        ClientViewMapperCronDecorator.TimeZoneOffset timeZoneOffset = clientViewMapperCronDecorator.getOffSet("UTC");
        try {
            ClientViewMapperCronDecorator.NonHourlyGrains.Weekly.updateCronToClientTimeZone(oozieCron, timeZoneOffset);
        } catch (Exception e) {
            Assert.assertTrue(e instanceof COBInternalException);
        }
    }


    @Test
    public void testUpdateHourAndMinuteCron11() {
        ClientViewMapperCronDecorator clientViewMapperCronDecorator = new ClientViewMapperCronDecorator(null);
        ClientViewMapperCronDecorator.OozieCron oozieCron = clientViewMapperCronDecorator.parseCronExpression("30 * * * *");
        ClientViewMapperCronDecorator.TimeZoneOffset timeZoneOffset = clientViewMapperCronDecorator.getOffSet("UTC");
        try {
            ClientViewMapperCronDecorator.NonHourlyGrains.Daily.updateCronToClientTimeZone(oozieCron, timeZoneOffset);
        } catch (Exception e) {
            Assert.assertTrue(e instanceof COBInternalException);
        }
    }


    @Test
    public void testMapClientViewMapping() throws COBInternalException {

        Optional<ClientViewMapping> optionalClientViewMapping = Optional.of(new ClientViewMapping());
        ClientViewMapperCronDecorator clientViewMapperCronDecorator = new ClientViewMapperCronDecorator(null) {
            @Override
            protected Optional<ClientViewMapping> getClientViewMappingFromParent(ValidateRequestBody provisionRequestBody, DefClientViewMap defClientViewMap) {

                return optionalClientViewMapping;
            }

            @Override
            protected void enrichCronExpression(ValidateRequestBody provisionRequestBody, ClientViewMapping clientViewMapping) {
            }
        };

        Assert.assertEquals(clientViewMapperCronDecorator.map(getProvisionRequestBodyInstance(), new ClientViewMapper.DefClientViewMap(viewName, "")), optionalClientViewMapping);
    }

    @Test
    public void testMapEmptyClientViewMapping() throws COBInternalException {

        Optional<ClientViewMapping> optionalClientViewMapping = Optional.empty();
        ClientViewMapperCronDecorator clientViewMapperCronDecorator = new ClientViewMapperCronDecorator(null) {
            @Override
            protected Optional<ClientViewMapping> getClientViewMappingFromParent(ValidateRequestBody provisionRequestBody, DefClientViewMap defClientViewMap) {

                return optionalClientViewMapping;
            }

        };

        Assert.assertEquals(clientViewMapperCronDecorator.map(getProvisionRequestBodyInstance(), new ClientViewMapper.DefClientViewMap(viewName, "")), optionalClientViewMapping);
    }

    @Test
    public void testParseCronExpression(){
        String cronExpression = "0 6 * * *";
        ClientViewMapperCronDecorator clientViewMapperCronDecorator = new ClientViewMapperCronDecorator(null);
        ClientViewMapperCronDecorator.OozieCron oozieCron = clientViewMapperCronDecorator.parseCronExpression(cronExpression);
        Assert.assertEquals(oozieCron.minute,"0");
        Assert.assertEquals(oozieCron.hour,"6");
    }

    @Test
    public void testGetOffSet(){
        String timeZone = "IST";
        ClientViewMapperCronDecorator clientViewMapperCronDecorator = new ClientViewMapperCronDecorator(null);
        ClientViewMapperCronDecorator.TimeZoneOffset offSet = clientViewMapperCronDecorator.getOffSet(timeZone);
        Assert.assertEquals(offSet.getHours(),5l);
        Assert.assertEquals(offSet.getMinutes(),30l);
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
