package com.tfs.dp2.catalog.cob.onboarder.impl;

import com.tfs.dp2.catalog.baseexceptions.COBInternalException;
import com.tfs.dp2.catalog.cob.onboarder.PipelineOnBoarder;
import com.tfs.dp2.catalog.cob.onboarder.PipelineOnBoarderUtils;
import com.tfs.dp2.catalog.cob.request.entity.COBValidateRequestBody;
import com.tfs.dp2.catalog.cob.request.entity.ValidateRequestBody;
import com.tfs.dp2.catalog.view.ViewController;
import com.tfs.dp2.catalog.view.ViewRepository;
import com.tfs.dp2.catalog.viewoutputgran.DefaultClientViewMapping;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RunWith(SpringRunner.class)
@WebMvcTest(value = ExporterOnBoarder.class)
public class ExporterOnBoarderTest {

    @MockBean
    private ViewController viewController;

    @MockBean
    private ViewRepository viewRepository;

    @MockBean
    private PipelineOnBoarderUtils pipelineOnBoarderUtils;

    @Autowired
    private ExporterOnBoarder exporterOnBoarder;

    private String clientName = "premiumbank";
    private String accountName = "premiumbank";
    private String product = "Default";
    private COBValidateRequestBody cobValidateRequestBody = new COBValidateRequestBody();

    private String defaultClientViewMapping = "{\n" +
            "\t \"cronExpression\": \"17 6 * * *\",\n" +
            "\t \"reference\": -60,\n" +
            "\t \"granularity\": \"Daily\",\n" +
            "\t \"outputPath\": \"/Reports/prod/\",\n" +
            "\t \"weekendEnabled\": \"0\",\n" +
            "\t \"owner\": \"SUV\"\n" +
            "\t}";


    private String defaultClientViewMappingWithExporters = "{\n" +
            "\t \"cronExpression\": \"17 6 * * *\",\n" +
            "\t \"reference\": -60,\n" +
            "\t \"granularity\": \"Daily\",\n" +
            "\t \"outputPath\": \"/Reports/prod/\",\n" +
            "\t \"weekendEnabled\": \"0\",\n" +
            "\t \"owner\": \"SUV\",\n" +
            "\t \"exporterList\": [\"ElasticSearchExporter\",\"DruidExporter\"]\n" +
            "\t}";

    @Test
    public void testOnBoardRequestNewProvision() throws COBInternalException {
        cobValidateRequestBody.setProduct(product);
        ValidateRequestBody provisionRequestBody = new ValidateRequestBody(clientName, accountName, Optional.of(cobValidateRequestBody));

        Object[] obj1 = new Object[]{"View1", defaultClientViewMappingWithExporters};
        Object[] obj2 = new Object[]{"View2", defaultClientViewMappingWithExporters};

        List<Object[]> returnList = new ArrayList<>();
        returnList.add(obj1);
        returnList.add(obj2);
        Mockito.when(viewRepository.findByViewGroup(product)).thenReturn(returnList);
        Mockito.when(viewController.insertExporterMapping(clientName, "View1", getExporters())).thenReturn("inserted");
        Mockito.when(viewController.insertExporterMapping(clientName, "View2", getExporters())).thenReturn("inserted");
        Optional<DefaultClientViewMapping> defaultClientViewMappingInstance = getDefaultClientViewMappingInstance();
        Mockito.when(pipelineOnBoarderUtils.getDefaultClientViewMapping(clientName,defaultClientViewMappingWithExporters)).thenReturn(defaultClientViewMappingInstance);
        PipelineOnBoarder.ProvisionStatus provisionStatus = exporterOnBoarder.onBoard(provisionRequestBody);
        Assert.assertEquals(provisionStatus, PipelineOnBoarder.ProvisionStatus.SUCCESSFULLY_PROVISIONED);
    }

    @Test
    public void testOnBoardRequestButNoProvision() throws COBInternalException {
        cobValidateRequestBody.setProduct(product);
        ValidateRequestBody provisionRequestBody = new ValidateRequestBody(clientName, accountName, Optional.of(cobValidateRequestBody));
        List<Object[]> returnList = new ArrayList<>();
        Mockito.when(viewRepository.findByViewGroup(product)).thenReturn(returnList);
        PipelineOnBoarder.ProvisionStatus provisionStatus = exporterOnBoarder.onBoard(provisionRequestBody);
        Assert.assertEquals(provisionStatus, PipelineOnBoarder.ProvisionStatus.ALREADY_PROVISIONED);
    }


    @Test
    public void testOnBoardRequestAlreadyProvision() throws COBInternalException {
        cobValidateRequestBody.setProduct(product);
        ValidateRequestBody provisionRequestBody = new ValidateRequestBody(clientName, accountName, Optional.of(cobValidateRequestBody));
        Object[] obj1 = new Object[]{"View1", defaultClientViewMappingWithExporters};
        Object[] obj2 = new Object[]{"View2", defaultClientViewMappingWithExporters};
        List<Object[]> returnList = new ArrayList<>();
        returnList.add(obj1);
        returnList.add(obj2);
        Mockito.when(viewRepository.findByViewGroup(product)).thenReturn(returnList);
        Mockito.when(viewController.insertExporterMapping(clientName, "View1", getExporters())).thenReturn("mapping exists");
        Mockito.when(viewController.insertExporterMapping(clientName, "View2", getExporters())).thenReturn("mapping exists");
        Optional<DefaultClientViewMapping> defaultClientViewMappingInstance = getDefaultClientViewMappingInstance();
        Mockito.when(pipelineOnBoarderUtils.getDefaultClientViewMapping(clientName,defaultClientViewMappingWithExporters)).thenReturn(defaultClientViewMappingInstance);
        PipelineOnBoarder.ProvisionStatus provisionStatus = exporterOnBoarder.onBoard(provisionRequestBody);
        Assert.assertEquals(provisionStatus, PipelineOnBoarder.ProvisionStatus.ALREADY_PROVISIONED);
    }



    List<String> getExporters() {
        List<String> exporters = new ArrayList<>();
        exporters.add("ElasticSearchExporter");
        exporters.add("DruidExporter");
        return exporters;
    }

    Optional<DefaultClientViewMapping> getDefaultClientViewMappingInstance(){
        DefaultClientViewMapping defaultClientViewMapping = new DefaultClientViewMapping();
        List<String> exporters = new ArrayList<>();
        exporters.add("ElasticSearchExporter");
        exporters.add("DruidExporter");
        defaultClientViewMapping.setExporterList(exporters);
        return Optional.of(defaultClientViewMapping);
    }

}
