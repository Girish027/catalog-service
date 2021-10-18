package com.tfs.dp2.catalog.view;

import com.tfs.dp2.catalog.clientinformation.ClientInformationRepository;
import com.tfs.dp2.catalog.cob.onboarder.PipelineOnBoarderUtils;
import com.tfs.dp2.catalog.dataset.DatasetRepository;
import com.tfs.dp2.catalog.datasetRules.RulesRepository;
import com.tfs.dp2.catalog.druidingestdata.DruidIngestDataRepository;
import com.tfs.dp2.catalog.exporters.ExporterConfigRepository;
import com.tfs.dp2.catalog.exporters.ExporterMappingRepository;
import com.tfs.dp2.catalog.exporters.ExporterOverrridesRepository;
import com.tfs.dp2.catalog.exporters.ExporterRepository;
import com.tfs.dp2.catalog.repositories.ViewGroupRepository;
import com.tfs.dp2.catalog.services.Validator;
import com.tfs.dp2.catalog.services.ViewGroupService;
import com.tfs.dp2.catalog.sourceAdapter.SourceAdapterRepository;
import com.tfs.dp2.catalog.view.ViewController;
import com.tfs.dp2.catalog.view.ViewDefinition;
import com.tfs.dp2.catalog.view.ViewRepository;
import com.tfs.dp2.catalog.viewcolumndefinition.ViewColumnRepository;
import com.tfs.dp2.catalog.viewhdfssource.ViewHDFSSourceRepository;
import com.tfs.dp2.catalog.viewoutputgran.ViewOutputGranRepository;
import com.tfs.dp2.catalog.viewsource.ViewSourceRepository;
import com.tfs.dp2.catalog.viewsqldefinition.ViewSQLDefinitionRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.Assert;
import org.mockito.Mockito;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by palak.data on 8/8/2019.
 */

@RunWith(SpringRunner.class)
@WebMvcTest(value = ViewController.class, secure = false)
/*@RunWith(SpringJUnit4ClassRunner.class)
@WebMvcTest(value = ViewController.class, secure = false)*/
public class ViewByExporterTypeAPITest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ViewController viewController;
    @MockBean
    private ViewRepository viewRepository;
    @MockBean
    private ViewColumnRepository viewColumnRepository;
    @MockBean
    private ClientInformationRepository clientInformationRepository;
    @MockBean
    private ViewOutputGranRepository viewOutputGranRepository;
    @MockBean
    private ViewHDFSSourceRepository viewHDFSSourceRepository;
    @MockBean
    private DruidIngestDataRepository druidIngestDataRepository;
    @MockBean
    private ViewSQLDefinitionRepository viewSQLDefinitionRepository;
    @MockBean
    private ViewSourceRepository viewSourceRepository;
    @MockBean
    private ViewInformationRepository viewInformationRepository;
    @MockBean
    private DatasetRepository datasetRepository;
    @MockBean
    private RulesRepository rulesRepository;
    @MockBean
    private ViewGroupRepository viewGroupRepository;
    @MockBean
    private ViewGroupService viewGroupService;
    @MockBean
    private SourceAdapterRepository sourceAdapterRepository;
    @MockBean
    private ExporterConfigRepository exporterConfigRepository;
    @MockBean
    private ExporterMappingRepository exporterMappingRepository;
    @MockBean
    private ExporterRepository exporterRepository;
    @MockBean
    private ExporterOverrridesRepository exporterOverrridesRepository;
    @MockBean
    private PipelineOnBoarderUtils pipelineOnBoarderUtils;
    @MockBean
    private Validator validator;

    List<Object[]> mockViewExporterList = new ArrayList<Object[]>();


    @Test
    public void retrieveViewByExporterTypeTest() {
        String[] testArray = {"Digital_Test41", "view_09bdabf4-4ccd-4f73-8c55-2626502e51d6", "1541926800000", "1564599600000", "Hourly", "0 * * * *"};
        mockViewExporterList.add(0, testArray);
        List<String> exporterTypes = new ArrayList<>();
        exporterTypes.add("es");
        Map<String, String> mockExportConfig = new HashMap<>();
        mockExportConfig.put("outputPath", "/Reports/prod/csvoutput/");
        mockExportConfig.put("writeAsOneFile", "true");
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/catalog/exporter-definitions/client/client_info_6c85230a-a14a-4bb1-8d1e-8f93ab70af34").param("exporter-type", "es").accept(MediaType.APPLICATION_JSON);
        Map<String, Object> exporterMap = new HashMap<>();
        Map<String, Object> map = new HashMap<String, Object>();
        Mockito.when(viewRepository.findViewByClientAndExporterType("client_info_6c85230a-a14a-4bb1-8d1e-8f93ab70af34", "es")).thenReturn(mockViewExporterList);
        Mockito.when(viewRepository.findExporterConfig("es")).thenReturn(mockViewExporterList);
        Mockito.when(viewRepository.getClientName("client_info_6c85230a-a14a-4bb1-8d1e-8f93ab70af34")).thenReturn("hilton");
        Map<String, String> configMap = new HashMap<String, String>();
        try {
            MvcResult result = mockMvc.perform(requestBuilder).andReturn();
            String expectedResponse = "{\"es\":{\"view\":[{\"viewName\":\"Digital_Test41\",\"viewId\":\"view_09bdabf4-4ccd-4f73-8c55-2626502e51d6\",\"scheduleStartTime\":\"1541926800000\",\"scheduleEndTime\":\"1564599600000\",\"granularity\":\"Hourly\",\"cronString\":\"0 * * * *\"}],\"config\":{\"Digital_Test41\":\"view_09bdabf4-4ccd-4f73-8c55-2626502e51d6\"}},\"client-name\":\"hilton\"}";
            Assert.assertEquals(expectedResponse, result.getResponse().getContentAsString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void ViewExporterListTest() {
        ViewExporterList list=new ViewExporterList();
        list.setCronString("0 * * * *");
        list.setGranularity("hourly");
        list.setScheduleEndTime("1566811369000");
        list.setScheduleStartTime("1566811369000");
        list.setViewId("view_test");
        list.setViewName("View Test");
        Assert.assertEquals("hourly", list.getGranularity());
    }

    @Test
    public void ViewExporterListExceptionTest() {
        String[] testArray = {"Digital_Test41", "view_09bdabf4-4ccd-4f73-8c55-2626502e51d6", "1541926800000", "1564599600000", "Hourly"};
        mockViewExporterList.add(0, testArray);
        List<String> exporterTypes = new ArrayList<>();
        exporterTypes.add("es");
        Map<String, String> mockExportConfig = new HashMap<>();
        mockExportConfig.put("outputPath", "/Reports/prod/csvoutput/");
        mockExportConfig.put("writeAsOneFile", "true");
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/catalog/exporter-definitions/client/client_info_6c85230a-a14a-4bb1-8d1e-8f93ab70af34").param("exporter-type", "es").accept(MediaType.APPLICATION_JSON);
        Map<String, Object> exporterMap = new HashMap<>();
        Map<String, Object> map = new HashMap<String, Object>();
        Mockito.when(viewRepository.findViewByClientAndExporterType("client_info_6c85230a-a14a-4bb1-8d1e-8f93ab70af34", "es")).thenReturn(mockViewExporterList);
        Mockito.when(viewRepository.findExporterConfig("es")).thenReturn(mockViewExporterList);
        Mockito.when(viewRepository.getClientName("client_info_6c85230a-a14a-4bb1-8d1e-8f93ab70af34")).thenReturn("hilton");
        Map<String, String> configMap = new HashMap<String, String>();
        try {
            MvcResult result = mockMvc.perform(requestBuilder).andReturn();
            String expectedResponse = "{\"es\":{\"view\":[],\"config\":{\"Digital_Test41\":\"view_09bdabf4-4ccd-4f73-8c55-2626502e51d6\"}},\"client-name\":\"hilton\"}";
            Assert.assertEquals(expectedResponse, result.getResponse().getContentAsString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
