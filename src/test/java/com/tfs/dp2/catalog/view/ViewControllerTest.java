package com.tfs.dp2.catalog.view;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tfs.dp2.catalog.baseexceptions.*;
import com.tfs.dp2.catalog.clientinformation.ClientInformationRepository;
import com.tfs.dp2.catalog.cob.onboarder.PipelineOnBoarderUtils;
import com.tfs.dp2.catalog.dataset.DatasetRepository;
import com.tfs.dp2.catalog.datasetRules.RulesRepository;
import com.tfs.dp2.catalog.druidingestdata.DruidIngestData;
import com.tfs.dp2.catalog.druidingestdata.DruidIngestDataRepository;
import com.tfs.dp2.catalog.entities.ViewGroup;
import com.tfs.dp2.catalog.exporters.ExporterConfigRepository;
import com.tfs.dp2.catalog.exporters.ExporterMappingRepository;
import com.tfs.dp2.catalog.exporters.ExporterOverrridesRepository;
import com.tfs.dp2.catalog.exporters.ExporterRepository;
import com.tfs.dp2.catalog.repositories.ViewGroupRepository;
import com.tfs.dp2.catalog.services.Validator;
import com.tfs.dp2.catalog.services.ViewGroupService;
import com.tfs.dp2.catalog.sourceAdapter.SourceAdapterRepository;
import com.tfs.dp2.catalog.util.Response;
import com.tfs.dp2.catalog.viewcolumndefinition.ViewColumnDefinition;
import com.tfs.dp2.catalog.viewcolumndefinition.ViewColumnRepository;
import com.tfs.dp2.catalog.viewhdfssource.ViewHDFSSourceRepository;
import com.tfs.dp2.catalog.viewoutputgran.ViewOutputGranRepository;
import com.tfs.dp2.catalog.viewoutputgran.ViewOutputGranularity;
import com.tfs.dp2.catalog.viewsource.ViewSourceRepository;
import com.tfs.dp2.catalog.viewsqldefinition.ViewSQLDefinitionRepository;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.*;

/**
 * Created by bikesh.singh on 12-10-2018.
 */

@Slf4j
@RunWith(SpringRunner.class)
@WebMvcTest(value = ViewController.class)
public class ViewControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ViewController viewController;


    @MockBean
    private ViewRepository viewRepository;
    @MockBean
    private ViewOutputGranRepository viewOutputGranRepository;
    @MockBean
    private ViewHDFSSourceRepository viewHDFSSourceRepository;
    @MockBean
    private ViewColumnRepository viewColumnRepository;
    @MockBean
    private ClientInformationRepository clientInformationRepository;
    @MockBean
    private DruidIngestDataRepository druidIngestDataRepository;
    @MockBean
    private ViewSQLDefinitionRepository viewSQLDefinitionRepository;
    @MockBean
    private ViewSourceRepository viewSourceRepository;
    @MockBean
    private DatasetRepository datasetRepository;
    @MockBean
    private RulesRepository rulesRepository;
    @MockBean
    private ViewInformationRepository viewInformationRepository;
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

    String clientName = "premiumbank";
    String accountName = "premiumbank";
    String productName = "CHAT";
    Optional<String> prodName = Optional.of(productName);


    private String validateCOBBody = "{\n" +
            "  \"product\": \"CHAT\",\n" +
            "  \"clientProperties\": {\n" +
            "    \"name\": \"sears\",\n" +
            "    \"admins\": [\n" +
            "      {\n" +
            "        \"name\": \"John\",\n" +
            "        \"email\": \"john@sears.com\"\n" +
            "      }\n" +
            "    ]\n" +
            "  },\n" +
            "  \"accountProperties\": {\n" +
            "    \"name\": \"sears_online\",\n" +
            "    \"vertical\": \"sales\",\n" +
            "    \"timezone\": \"UTC\",\n" +
            "    \"admins\": [\n" +
            "      {\n" +
            "        \"name\": \"Rob\",\n" +
            "        \"email\": \"rob@searshs.com\"\n" +
            "      }\n" +
            "    ]\n" +
            "  },\n" +
            "  \"configuration\": {\n" +
            "    \"currency\": \"USD\",\n" +
            "    \"phoneNumber\": \"+18005494505\"\n" +
            "  },\n" +
            "  \"volumetrics\": {\n" +
            "    \"agentConcurrency\": 3,\n" +
            "    \"maxAgents\": 500,\n" +
            "    \"peakStartWeek\": 10000,\n" +
            "    \"peakEndWeek\": 10000,\n" +
            "    \"avgPageViews\": 1000000,\n" +
            "    \"avgVisitors\": 1000000,\n" +
            "    \"peakPageViews\": 1000000,\n" +
            "    \"peakVisitors\": 1000000\n" +
            "  }\n" +
            "}";

    @Test
    public void ViewTimeDependencyCascadeTest(){
        Object[] record = {"View_DCF","/dataplat/prod/views/","-120","JSON","journeyStartTime","viewinfo_1515000000","60","client_info_1600000002","1","0","SQL",null,0,null,"STATIC",null,null,null};
        List<Object[]> recordList = new ArrayList<>();
        recordList.add(record);
        ViewColumnDefinition viewColumnDefinition = new ViewColumnDefinition();
        viewColumnDefinition.setColumnPath("/prod/RawIDM");
        List<ViewColumnDefinition> viewColumnDefinitionList = new ArrayList<>();
        viewColumnDefinitionList.add(viewColumnDefinition);
        Object[] record1 = {"RawIDM","-60","120"};
        List<Object[]> recordList1 = new ArrayList<>();
        recordList1.add(record1);
        List<String> sqlList = new ArrayList<>();
        sqlList.add("view_dcf sql query");
        View view = new View();
        view.setViewName("View_DCF");
        view.setIsActive(1);
        try {
            Mockito.when(viewRepository.findByViewName(Mockito.anyString())).thenReturn(view);
            Mockito.when(viewOutputGranRepository.findProcessorSnapshotByClientNameViewNameAndFinalView(Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenReturn(recordList);
            Mockito.when(viewRepository.findViewIdByViewName(Mockito.anyString())).thenReturn("viewinfo_1515000000");
            Mockito.when(viewHDFSSourceRepository.findHDFSSourcePathByViewIdAndFinalViewId(Mockito.anyString(), Mockito.anyString())).thenReturn("/dataplat/prod/views/");
            Mockito.when(viewColumnRepository.findByViewIdAndUniqueKeyFlagOrderByUniqueKeyOrder(Mockito.anyString(), Mockito.anyInt())).thenReturn(viewColumnDefinitionList);
            Mockito.when(viewRepository.findImportsByViewIdAndViewName(Mockito.anyString(), Mockito.anyString())).thenReturn(recordList1);
            Mockito.when(viewRepository.findIsDynamicSQL(Mockito.anyString())).thenReturn("0");
            Mockito.when(viewRepository.findProcessorSnapshotSQLsByViewId(Mockito.anyString())).thenReturn(sqlList);

            RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/catalog/processor/snapshot?viewname=View_DCF&clientname=searsonline&scheduleStartTime=1568530800000&scheduleEndTime=1568617200000&finalview=View_DCF").accept(MediaType.APPLICATION_JSON);
            MvcResult result = mockMvc.perform(requestBuilder).andReturn();
            String expectedResponse = "{\"viewName\":\"View_DCF\",\"path\":\"/dataplat/prod/views/\",\"format\":\"JSON\",\"referenceTime\":\"-120\",\"timeDimensionColumn\":\"journeyStartTime\",\"dqEnabled\":true,\"materializationEnabled\":false,\"processingPlugInType\":\"SQL\",\"processorPlugInMainClass\":null,\"preferReadMaterializedData\":false,\"sourcePluginClass\":null,\"loadStrategy\":\"STATIC\",\"sourceConfigList\":{},\"uniqueFields\":[\"/prod/RawIDM\"],\"importsList\":[{\"viewName\":\"RawIDM\",\"startTime\":\"1568527200000\",\"endTime\":\"1568624400000\"}],\"sqls\":[{\"View_DCF\":\"view_dcf sql query\"}],\"columns\":[],\"customParams\":null,\"dynamicBucketPath\":\"\"}";
            Assert.assertEquals(expectedResponse, result.getResponse().getContentAsString());
        }catch (Exception ex){
            Assert.assertFalse(true);
        }
    }

    @Test
    public void retrieveProcessorSnapshotTest() {
        Object[] record = {"View_DCF", "/dataplat/prod/views/", "-120", "JSON", "journeyStartTime", "viewinfo_1515000000", "60", "client_info_1600000002", "1", "0", "SQL", null, false, null, "STATIC"};
        List<Object[]> recordList = new ArrayList<>();
        recordList.add(record);
        ViewColumnDefinition viewColumnDefinition = new ViewColumnDefinition();
        viewColumnDefinition.setColumnPath("/prod/RawIDM");
        List<ViewColumnDefinition> viewColumnDefinitionList = new ArrayList<>();
        viewColumnDefinitionList.add(viewColumnDefinition);
        Object[] record1 = {"RawIDM", "0", "0"};
        List<Object[]> recordList1 = new ArrayList<>();
        recordList1.add(record1);
        List<String> sqlList = new ArrayList<>();
        sqlList.add("view_dcf sql query");
        View view = new View();
        view.setViewName("View_DCF");
        view.setIsActive(1);
        try {
            Mockito.when(viewRepository.findByViewName(Mockito.anyString())).thenReturn(view);
            Mockito.when(viewOutputGranRepository.findProcessorSnapshotByClientNameViewNameAndFinalView(Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenReturn(recordList);
            Mockito.when(viewRepository.findViewIdByViewName(Mockito.anyString())).thenReturn("viewinfo_1515000000");
            Mockito.when(viewHDFSSourceRepository.findHDFSSourcePathByViewIdAndFinalViewId(Mockito.anyString(), Mockito.anyString())).thenReturn("/dataplat/prod/views/");
            Mockito.when(viewColumnRepository.findByViewIdAndUniqueKeyFlagOrderByUniqueKeyOrder(Mockito.anyString(), Mockito.anyInt())).thenReturn(viewColumnDefinitionList);
            Mockito.when(viewRepository.findImportsByViewIdAndViewName(Mockito.anyString(), Mockito.anyString())).thenReturn(recordList1);
            Mockito.when(viewRepository.findIsDynamicSQL(Mockito.anyString())).thenReturn("0");
            Mockito.when(viewRepository.findProcessorSnapshotSQLsByViewId(Mockito.anyString())).thenReturn(sqlList);

            RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/catalog/processor/snapshot?viewname=View_DCF&clientname=searsonline&scheduletime=1505458800000&finalview=View_DCF").accept(MediaType.APPLICATION_JSON);
            MvcResult result = mockMvc.perform(requestBuilder).andReturn();
            System.out.println("response " + result.getResponse().getContentAsString());
            String expectedResponse = "{\"viewName\":\"View_DCF\",\"path\":\"/dataplat/prod/views/\",\"format\":\"JSON\",\"referenceTime\":\"-120\",\"timeDimensionColumn\":\"journeyStartTime\",\"dqEnabled\":true,\"materializationEnabled\":false,\"processingPlugInType\": \"SQL\",\"processorPlugInMainClass\": null,\"preferReadMaterializedData\": false,\"sourcePluginClass\": null,\"loadStrategy\":\"STATIC\",\"uniqueFields\":[\"/prod/RawIDM\"],\"importsList\":[{\"viewName\":\"RawIDM\",\"startTime\":\"1505458800000\",\"endTime\":\"1505462400000\"}],\"sqls\":[{\"View_DCF\":\"view_dcf sql query\"}]}";
            Assert.assertEquals(expectedResponse, result.getResponse().getContentAsString());
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
    }

    @Test
    public void retrieveProcessorSnapshotAliasTest() {
        Object[] record = {"View_InteractiveChatCount_303", "/Reports/prod/", "-60", "JSON", "TimeField", "view_1c6196d6-88cc-465c-ba0d-8d428bb8beac", "60", "client_info_1513077758", "1", "0", "SQL", null, false, null, "RELATIVE"};
        List<Object[]> recordList = new ArrayList<>();
        recordList.add(record);
        ViewColumnDefinition viewColumnDefinition = new ViewColumnDefinition();
        viewColumnDefinition.setColumnPath("TimeField");
        List<ViewColumnDefinition> viewColumnDefinitionList = new ArrayList<>();
        viewColumnDefinitionList.add(viewColumnDefinition);
        Object[] record1 = {"AssistInteractions", "0", "0"};
        List<Object[]> recordList1 = new ArrayList<>();
        recordList1.add(record1);
        List<String> sqlList = new ArrayList<>();
        sqlList.add("select COUNT(DISTINCT interactionId) as InteractiveChats, from_unixtime(eventRaisedTimeMillis DIV 1000, 'yyyy-MM-dd HH:mm:ss') as reportday from AssistInteractions where eventType='interaction/status/interactive' group by reportday");
        View view = new View();
        view.setViewName("View_InteractiveChatCount_303");
        view.setIsActive(1);
        try {
            Mockito.when(viewRepository.findByViewName(Mockito.anyString())).thenReturn(view);
            Mockito.when(viewOutputGranRepository.findProcessorSnapshotByClientNameAliasAndFinalView(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenReturn(recordList);
            Mockito.when(viewRepository.findViewIdByViewName(Mockito.anyString())).thenReturn("view_21fee18d-0e2b-4d7b-b486-727cfe964f4e");
            Mockito.when(viewHDFSSourceRepository.findHDFSSourcePathByViewIdAndFinalViewId(Mockito.anyString(), Mockito.anyString())).thenReturn("/Reports/prod/");
            Mockito.when(viewColumnRepository.findByViewIdAndUniqueKeyFlagOrderByUniqueKeyOrder(Mockito.anyString(), Mockito.anyInt())).thenReturn(viewColumnDefinitionList);
            Mockito.when(viewRepository.findImportsByViewIdAndViewName(Mockito.anyString(), Mockito.anyString())).thenReturn(recordList1);
            Mockito.when(viewRepository.findIsDynamicSQL(Mockito.anyString())).thenReturn("0");
            Mockito.when(viewRepository.findProcessorSnapshotSQLsByViewId(Mockito.anyString())).thenReturn(sqlList);

            RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/catalog/processor/snapshot?viewname=A__View_InteractiveChatAnomalyDetection_303&clientname=dish&scheduletime=1540684800000&finalview=View_InteractiveChatAnomalyDetection_303").accept(MediaType.APPLICATION_JSON);
            MvcResult result = mockMvc.perform(requestBuilder).andReturn();
            System.out.println("response " + result.getResponse().getContentAsString());
            String expectedResponse = "{\"viewName\":\"View_InteractiveChatCount_303\",\"path\":\"/Reports/prod/\",\"format\":\"JSON\",\"referenceTime\":\"-60\",\"timeDimensionColumn\":\"TimeField\",\"dqEnabled\":false,\"materializationEnabled\":false,\"processingPlugInType\":\"SQL\",\"processorPlugInMainClass\": null,\"preferReadMaterializedData\": false,\"sourcePluginClass\": null,\"loadStrategy\":\"RELATIVE\",\"uniqueFields\":[\"TimeField\"],\"importsList\":[{\"viewName\":\"AssistInteractions\",\"startTime\":\"1540684800000\",\"endTime\":\"1540688400000\"}],\"sqls\":[{\"A\":\"select COUNT(DISTINCT interactionId) as InteractiveChats, from_unixtime(eventRaisedTimeMillis DIV 1000, 'yyyy-MM-dd HH:mm:ss') as reportday from AssistInteractions where eventType='interaction/status/interactive' group by reportday\"}]}";
            Assert.assertEquals(expectedResponse, result.getResponse().getContentAsString());
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
    }

    @Test
    public void checkHealthTest() {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/catalog/health");
        try {
            MvcResult result = mockMvc.perform(requestBuilder).andReturn();
            System.out.println("response111 - " + result.getResponse().getContentAsString());
            String expectedResponse = "{\"status\": \"ok\", \"Service\": \"catalog\"}";
            Assert.assertEquals(expectedResponse, result.getResponse().getContentAsString());
        } catch (Exception ex) {
            log.info(ex.getMessage());
        }
    }

    @Test
    public void updateDqEnabledForViewTest() {
        View view = new View();
        view.setViewName("View_DCF");
        view.setIsActive(1);
        try {
            Mockito.when(viewRepository.findByViewName(Mockito.anyString())).thenReturn(view);
            Mockito.when(viewRepository.updateDqEnabledValueForView(Mockito.anyString(), Mockito.anyInt())).thenReturn(1);
            RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/catalog/view/View_DCF/dq-enabled/false");
            MvcResult result = mockMvc.perform(requestBuilder).andReturn();
            System.out.println("response222 " + result.getResponse().getContentAsString());
            String expectedresponse = "updated dqEnabled value for View_DCF";
            Assert.assertEquals(expectedresponse, result.getResponse().getContentAsString());
        } catch (Exception ex) {
            log.info(ex.getMessage());
        }
    }

    @Test
    public void updateMaterializationEnabledForViewTest() {
        View view = new View();
        view.setViewName("View_DCF");
        view.setIsActive(1);
        try {
            Mockito.when(viewRepository.findByViewName(Mockito.anyString())).thenReturn(view);
            Mockito.when(viewRepository.updateMaterializationEnabledValueForView(Mockito.anyString(), Mockito.anyInt())).thenReturn(1);
            RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/catalog/view/View_DCF/materialization-enabled/false");
            MvcResult result = mockMvc.perform(requestBuilder).andReturn();
            System.out.println("response222 " + result.getResponse().getContentAsString());
            String expectedresponse = "updated materializationEnabled value for View_DCF";
            Assert.assertEquals(expectedresponse, result.getResponse().getContentAsString());
        } catch (Exception ex) {
            log.info(ex.getMessage());
        }
    }

    @Test
    public void getViewDefinitionTest() {
        Object[] record = {"viewinfo_1515000000", "View_DCF", "Final View for Digital Funnel", "Layer3_1513089094", "Layer 3", "View_DCF", "sql_definition", "800", "1", "0",
                "/dataplat/prod/views/", "journeyStartTime", "journeyStartTime", "JSON", "0"};
        List<Object[]> recordList = new ArrayList<>();
        recordList.add(record);
        DruidIngestData druidIngestData = new DruidIngestData();
        List<DruidIngestData> druidIngestDataList = new ArrayList<>();
        druidIngestDataList.add(druidIngestData);
        ViewColumnDefinition viewColumnDefinition = new ViewColumnDefinition();
        viewColumnDefinition.setColumnPath("/prod/RawIDM");
        viewColumnDefinition.setColumnDisplayName("journeyStartTime");
        viewColumnDefinition.setColumnName("journeyStartTime");
        viewColumnDefinition.setColumnId("view_column_1515000000");
        viewColumnDefinition.setUniqueKeyFlag(1);
        viewColumnDefinition.setDataType("string");
        viewColumnDefinition.setIsDimension(0);

        List<ViewColumnDefinition> viewColumnDefinitionList = new ArrayList<>();
        viewColumnDefinitionList.add(viewColumnDefinition);
        Object[] record1 = {"RawIDM", "/raw/prod/rtdp/idm/events/<client>"};
        Object[] record2 = {"AssistInteractions", "/raw/prod/<client>/pxassist/interactions"};
        List<Object[]> recordList1 = new ArrayList<>();
        recordList1.add(record1);
        recordList1.add(record2);
        View view = new View();
        view.setViewName("View_DCF");
        view.setIsActive(1);
        try {
            Mockito.when(viewRepository.findByViewName(Mockito.anyString())).thenReturn(view);
            Mockito.when(viewRepository.findViewDefinitionByViewName(Mockito.anyString())).thenReturn(recordList);
            Mockito.when(druidIngestDataRepository.findByViewId(Mockito.anyString())).thenReturn(druidIngestDataList);
            Mockito.when(viewColumnRepository.findByViewId(Mockito.anyString())).thenReturn(viewColumnDefinitionList);
            Mockito.when(viewHDFSSourceRepository.findHDFSSourceNameAndPathByViewId(Mockito.anyString())).thenReturn(recordList1);
            RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/catalog/view-definitions/View_DCF");
            MvcResult result = mockMvc.perform(requestBuilder).andReturn();
            System.out.println("response333 " + result.getResponse().getContentAsString());
            String expectedresponse = "[{\"viewId\":\"viewinfo_1515000000\",\"viewName\":\"View_DCF\",\"viewDescription\":\"Final View for Digital Funnel\",\"layerId\":\"Layer3_1513089094\",\"layerName\":\"Layer 3\",\"definitionName\":\"View_DCF\",\"sqlDefinition\":\"sql_definition\",\"ttlDays\":\"800\",\"esIndexEnabled\":\"0\",\"druidIndexEnabled\":\"0\",\"outputPath\":\"/dataplat/prod/views/\",\"timeDimensionColumn\":\"journeyStartTime\",\"uniqueColumn\":\"uniqueRowId\",\"inputDataFormat\":\"JSON\",\"druidIngestData\":{},\"columnList\":[{\"columnId\":\"view_column_1515000000\",\"definitionId\":null,\"columnName\":\"journeyStartTime\",\"columnDisplayName\":\"journeyStartTime\",\"columnDescription\":null,\"viewId\":\"viewinfo_1515000000\",\"columnPath\":\"/prod/RawIDM\",\"columnSource\":\"view\",\"dataType\":\"string\",\"dataLength\":null,\"isDimension\":0,\"isMetric\":0,\"aggregationType\":null,\"uniqueKeyFlag\":1,\"uniqueKeyOrder\":1,\"columnIdentifier\":0,\"isDerivedColumn\":0,\"derivedSyntax\":\"\",\"indexSet\":0,\"indexOrder\":0,\"createdUnixtimestamps\":1522317600000,\"modifiedUnixtimestamps\":1522317600000,\"createdBy\":\"DP2\",\"modifiedBy\":\"DP2\"}],\"ultimateSourceList\":[{\"viewName\":\"RawIDM\",\"path\":\"/raw/prod/rtdp/idm/events/<client>\"},{\"viewName\":\"AssistInteractions\",\"path\":\"/raw/prod/<client>/pxassist/interactions\"}]}]";
            Assert.assertEquals(expectedresponse, result.getResponse().getContentAsString());
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }

    }

    /**
     * New view group being added.
     *
     * @throws ViewGroupNotAvailableException
     */
    @Test
    public void validateViewGroupSuccess() throws ViewGroupNotAvailableException {

        ViewGroup viewGroup = new ViewGroup();
        Mockito.when(viewGroupRepository.findByName("test")).thenReturn(viewGroup);
        viewController.validateViewGroup("test");

    }

    /**
     * Validate exception of view addition failure.
     */
    @Test
    public void validateViewGroupFailure() {
        ViewGroup viewGroup = new ViewGroup();
        Map<String, String> map = new HashMap<>();
        map.put("queue", "test");
        viewGroup.setExecutionProperties(map);
        ObjectMapper mapper = new ObjectMapper();
        try {
            System.out.println(mapper.writeValueAsString(viewGroup));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        Mockito.when(viewGroupRepository.findByName("test")).thenReturn(null);
        try {
            viewController.validateViewGroup("test");
        } catch (Exception e) {
            Assert.assertTrue(e instanceof ViewGroupNotAvailableException);
        }

    }

    /**
     * Insert new view group and validate.
     *
     * @throws ViewGroupException
     * @throws ViewGroupConstraintsException
     */
    @Test
    public void insertViewGroup() throws ViewGroupException, ViewGroupConstraintsException, InvalidExecutionPropertiesException {
        ViewGroup viewGroup = new ViewGroup();
        ViewGroup savedEntity = new ViewGroup();
        String id = "123";
        savedEntity.setId(id);
        String name = "ABC";
        savedEntity.setName(name);
        Map map = new HashMap<String, String>();
        map.put("dataCenter", "sv2");
        savedEntity.setEnvironmentProperties(map);
        Mockito.when(viewGroupService.addViewGroup(viewGroup)).thenReturn(savedEntity);
        Response response = viewController.insertViewGroup(viewGroup);
        Assert.assertTrue(response.getParams().get("groupId").equals(id));
        Assert.assertTrue(response.getParams().get("groupName").equals(name));


    }

    @Test
    public void getSourceTimeRangeTest() throws Exception {
        View view = new View();
        view.setIsActive(1);
        List<Object[]> objects = new LinkedList<>();
        Mockito.when(viewRepository.findByViewName(Mockito.anyString())).thenReturn((view));
        objects.add("-60\t1548640800000\t1549955773000\tviewinfo_1515000000\t60\tclient_info_1513077760\t{\"queue\":\"LongRun\"}\tviewGroup_909d79cf-a8a9-4d18-98f0-f8fccd4fd944\t1=2\tCASE WHEN dayofmonth(NOW()) <= 10 then date_add(date_add(NOW(), INTERVAL (-1*dayofmonth(NOW()))+1 DAY), interval -1 MONTH) else date_add(NOW(), INTERVAL (-1*dayofmonth(NOW()))+1 DAY) end as checkdate\tUTC\tHourly".split("\t"));
        ArrayList<String> jarPath = new ArrayList<>();
        jarPath.add("null");
        Mockito.when(viewOutputGranRepository.findTimingsByViewNameAndInfoName(Mockito.anyString(), Mockito.anyString())).thenReturn(objects);
        Mockito.when(viewOutputGranRepository.findProcessorJarLocationByViewName(Mockito.anyString())).thenReturn(jarPath);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/catalog/source-time-range?viewname=View_DCF&clientname=dish&scheduletime=1534474800000&replay=true");
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        String expectedString = "{\"viewName\":\"View_DCF\",\"clientName\":\"dish\",\"scheduleStartTime\":\"1534474800000\",\"scheduleEndTime\":\"1534478400000\",\"jobExecutionExpression\":\"CASE WHEN dayofmonth(NOW()) <= 10 then date_add(date_add(NOW(), INTERVAL (-1*dayofmonth(NOW()))+1 DAY), interval -1 MONTH) else date_add(NOW(), INTERVAL (-1*dayofmonth(NOW()))+1 DAY) end as checkdate\",\"executionProperties\":{\"queue\":\"LongRun\"},\"timezone\":\"UTC\",\"granularity\":\"Hourly\",\"processorPlugInJarLoc\":[\"null\"],\"exporterPluginJarLocList\":[],\"sourceTimeRangeList\":[]}";
        Assert.assertEquals(expectedString, result.getResponse().getContentAsString());
    }


    @Test
    public void getSourceTimeRangeTest2() throws Exception{
        View view = new View();
        view.setIsActive(1);
        List<Object[]> objects = new LinkedList<>();

        Mockito.when(viewRepository.findByViewName(Mockito.anyString())).thenReturn((view));
        objects.add("-60\t1548640800000\t1549955773000\tviewinfo_1515000000\t60\tclient_info_1513077760\t{\"queue\":\"LongRun\"}\tviewGroup_909d79cf-a8a9-4d18-98f0-f8fccd4fd944\t1=2\tCASE WHEN dayofmonth(NOW()) <= 10 then date_add(date_add(NOW(), INTERVAL (-1*dayofmonth(NOW()))+1 DAY), interval -1 MONTH) else date_add(NOW(), INTERVAL (-1*dayofmonth(NOW()))+1 DAY) end as checkdate\tUTC\tHourly".split("\t"));
        ArrayList<String> jarPath = new ArrayList<>();
        jarPath.add("aivabilling.jar, transcriptConsolidated.jar");
        Mockito.when(viewOutputGranRepository.findTimingsByViewNameAndInfoName(Mockito.anyString(), Mockito.anyString())).thenReturn(objects);
        Mockito.when(viewOutputGranRepository.findProcessorJarLocationByViewName(Mockito.anyString())).thenReturn(jarPath);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/catalog/source-time-range?viewname=View_DCF&clientname=dish&scheduletime=1534474800000&replay=true");
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        String expectedString = "{\"viewName\":\"View_DCF\",\"clientName\":\"dish\",\"scheduleStartTime\":\"1534474800000\",\"scheduleEndTime\":\"1534478400000\",\"jobExecutionExpression\":\"CASE WHEN dayofmonth(NOW()) <= 10 then date_add(date_add(NOW(), INTERVAL (-1*dayofmonth(NOW()))+1 DAY), interval -1 MONTH) else date_add(NOW(), INTERVAL (-1*dayofmonth(NOW()))+1 DAY) end as checkdate\",\"executionProperties\":{\"queue\":\"LongRun\"},\"timezone\":\"UTC\",\"granularity\":\"Hourly\",\"processorPlugInJarLoc\":[\"aivabilling.jar, transcriptConsolidated.jar\"],\"exporterPluginJarLocList\":[],\"sourceTimeRangeList\":[]}";
        Assert.assertEquals(result.getResponse().getContentAsString(), expectedString);
    }


    @Test
    public void getSourceTimeRangeTest3() throws Exception{
        View view = new View();
        view.setIsActive(1);
        List<Object[]> objects = new LinkedList<>();
        Mockito.when(viewRepository.findByViewName(Mockito.anyString())).thenReturn((view));
        objects.add("-60\t1548640800000\t1549955773000\tviewinfo_1515000000\t60\tclient_info_1513077760\t{\"queue\": \"LongRun\"}\tviewGroup_909d79cf-a8a9-4d18-98f0-f8fccd4fd944\t1=2\tCASE WHEN dayofmonth(NOW()) <= 10 then date_add(date_add(NOW(), INTERVAL (-1*dayofmonth(NOW()))+1 DAY), interval -1 MONTH) else date_add(NOW(), INTERVAL (-1*dayofmonth(NOW()))+1 DAY) end as checkdate\tUTC\tHourly".split("\t"));
        ArrayList<String> jarPath = new ArrayList<>();
        jarPath.add("aivabilling.jar");
        Mockito.when(viewOutputGranRepository.findTimingsByViewNameAndInfoName(Mockito.anyString(), Mockito.anyString())).thenReturn(objects);
        Mockito.when(viewOutputGranRepository.findProcessorJarLocationByViewName(Mockito.anyString())).thenReturn(jarPath);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/catalog/source-time-range?viewname=View_DCF&clientname=dish&scheduletime=1534474800000&replay=true");
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        String expectedString = "{\"viewName\":\"View_DCF\",\"clientName\":\"dish\",\"scheduleStartTime\":\"1534474800000\",\"scheduleEndTime\":\"1534478400000\",\"jobExecutionExpression\":\"CASE WHEN dayofmonth(NOW()) <= 10 then date_add(date_add(NOW(), INTERVAL (-1*dayofmonth(NOW()))+1 DAY), interval -1 MONTH) else date_add(NOW(), INTERVAL (-1*dayofmonth(NOW()))+1 DAY) end as checkdate\",\"executionProperties\":{\"queue\":\"LongRun\"},\"timezone\":\"UTC\",\"granularity\":\"Hourly\",\"processorPlugInJarLoc\":[\"aivabilling.jar\"],\"exporterPluginJarLocList\":[],\"sourceTimeRangeList\":[]}";
        Assert.assertEquals(result.getResponse().getContentAsString(), expectedString);
    }


    /*@Test
    public void getSourceTimeRangeTest3() throws Exception{
        View view = new View();
        view.setIsActive(1);
        List<Object[]> objects = new LinkedList<>();
        Mockito.when(viewRepository.findByViewName(Mockito.anyString())).thenReturn((view));
        objects.add("-60\t1548640800000\t1549955773000\tviewinfo_1515000000\t60\tclient_info_1513077760\t{\"queue\": \"LongRun\"}\taivabilling.jar,,\ttest\ttest2\tviewGroup_909d79cf-a8a9-4d18-98f0-f8fccd4fd944\t1=2\tCASE WHEN dayofmonth(NOW()) <= 10 then date_add(date_add(NOW(), INTERVAL (-1*dayofmonth(NOW()))+1 DAY), interval -1 MONTH) else date_add(NOW(), INTERVAL (-1*dayofmonth(NOW()))+1 DAY) end as checkdate".split("\t"));
        Mockito.when(viewOutputGranRepository.findTimingsByViewNameAndInfoName(Mockito.anyString(), Mockito.anyString())).thenReturn(objects);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/catalog/source-time-range?viewname=View_DCF&clientname=dish&scheduletime=1534474800000&replay=true");
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        String expectedString = "{\"viewName\":\"View_DCF\",\"clientName\":\"dish\",\"scheduleStartTime\":\"1534474800000\",\"scheduleEndTime\":\"1534478400000\",\"jobExecutionExpression\":\"viewGroup_909d79cf-a8a9-4d18-98f0-f8fccd4fd944\",\"executionProperties\":{\"queue\":\"LongRun\"},\"timezone\":\"1=2\",\"granularity\":\"CASE WHEN dayofmonth(NOW()) <= 10 then date_add(date_add(NOW(), INTERVAL (-1*dayofmonth(NOW()))+1 DAY), interval -1 MONTH) else date_add(NOW(), INTERVAL (-1*dayofmonth(NOW()))+1 DAY) end as checkdate\",\"processorPlugInJarLoc\":\"aivabilling.jar\",\"exporterPluginJarLocList\":[],\"sourceTimeRangeList\":[]}";
        Assert.assertEquals(result.getResponse().getContentAsString(), expectedString);
    }*/
    
    /**
     * Assert when execution properties are present
     *
     * @throws Exception
     */
    @Test
    public void getSourceTimeRangeTest1() throws Exception {
        View view = new View();
        view.setIsActive(1);
        List<Object[]> objects = new LinkedList<>();
        Mockito.when(viewRepository.findByViewName(Mockito.anyString())).thenReturn((view));
        objects.add(("-60\t1548640800000\t1549955773000\tviewinfo_1515000000\t60\tclient_info_1513077760\t{\"queue\": \"LongRun\", \"retryCount\": \"1\", \"retryInterval\": \"1\", \"executor-cores\":\"5\"}" +
                "\tnull\tviewGroup_909d79cf-a8a9-4d18-98f0-f8fccd4fd944\t1=2\tCASE WHEN dayofmonth(NOW()) <= 10 then date_add(date_add(NOW(), INTERVAL (-1*dayofmonth(NOW()))+1 DAY), interval -1 MONTH) else date_add(NOW(), INTERVAL (-1*dayofmonth(NOW()))+1 DAY) end as checkdate\tUTC\tHourly").split("\t"));
        Mockito.when(viewOutputGranRepository.findTimingsByViewNameAndInfoName(Mockito.anyString(), Mockito.anyString())).thenReturn(objects);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/catalog/source-time-range?viewname=View_DCF&clientname=dish&scheduletime=1534474800000&replay=true");
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        Map<String, String> expectedexecProps = new HashMap<>();
        expectedexecProps.put("queue", "LongRun");
        expectedexecProps.put("retryCount", "1");
        expectedexecProps.put("retryInterval", "1");
        expectedexecProps.put("executor-cores", "5");
        JSONObject resultJson = new JSONObject(result.getResponse().getContentAsString());
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> resultExecProps = new HashMap<>();
        try {
            resultExecProps = mapper.readValue(resultJson.getJSONObject("executionProperties").toString(), new TypeReference<Map<String, Object>>() {
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        boolean isExecPropsEqual = resultExecProps.entrySet().stream().filter(
                value -> expectedexecProps.entrySet().stream().anyMatch(
                        value1 -> (value1.getKey().equals(value.getKey()) && value1.getValue().equals(value.getValue()))))
                .findAny().isPresent();
        Assert.assertTrue(isExecPropsEqual);
    }

    @Test
    public void getSourceTimeRangeNegativeTest() throws Exception {
        View view = new View();
        view.setIsActive(1);
        List<Object[]> objects = new LinkedList<>();
        Mockito.when(viewRepository.findByViewName(Mockito.anyString())).thenReturn((view));
        objects.add("-60\t1548640800000\t1549955773000\tviewinfo_1515000000\t60\tclient_info_1513077760\t{\"queue\": \"LongRun\"}\tviewGroup_909d79cf-a8a9-4d18-98f0-f8fccd4fd944\t \t \tUTC\tHourly".split("\t"));
        List<String> jarPath = new ArrayList<>();
        jarPath.add("null");
        Mockito.when(viewOutputGranRepository.findTimingsByViewNameAndInfoName(Mockito.anyString(), Mockito.anyString())).thenReturn(objects);
        Mockito.when(viewOutputGranRepository.findProcessorJarLocationByViewName(Mockito.anyString())).thenReturn(jarPath);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/catalog/source-time-range?viewname=View_DCF&clientname=dish&scheduletime=1534474800000&replay=true");
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        String expectedString = "{\"viewName\":\"View_DCF\",\"clientName\":\"dish\",\"scheduleStartTime\":\"1534474800000\",\"scheduleEndTime\":\"1534478400000\",\"jobExecutionExpression\":\" \",\"executionProperties\":{\"queue\":\"LongRun\"},\"timezone\":\"UTC\",\"granularity\":\"Hourly\",\"processorPlugInJarLoc\":[\"null\"],\"exporterPluginJarLocList\":[],\"sourceTimeRangeList\":[]}";
        Assert.assertEquals(expectedString, result.getResponse().getContentAsString());
    }

    @Test
    public void getOrchestratorSnapshot() throws Exception {
        View view = new View();
        view.setViewName("View_DCF");
        view.setIsActive(1);

        ViewGroup viewGroup = new ViewGroup();
        String id = "123";
        viewGroup.setId(id);
        String name = "View_DCF";
        viewGroup.setName(name);
        Map map = new HashMap<String, String>();
        map.put("dataCenter", "sv2");
        viewGroup.setEnvironmentProperties(map);
        List<String> uniqCronExpression = new ArrayList<>();
        uniqCronExpression.add("0 * * * *");
        List<Object[]> clients = new ArrayList<>();
        Object[] o = {"hilton", null, 2, "{\"dataCenter\":\"sv2\"}"};
        clients.add(o);

        Mockito.when(viewRepository.findByViewName(Mockito.anyString())).thenReturn(view);
        Mockito.when(viewGroupRepository.findById(Mockito.anyString())).thenReturn(viewGroup);
        Mockito.when(viewOutputGranRepository.findUniqueCronString(view.getViewId())).thenReturn(uniqCronExpression);
        Mockito.when(viewOutputGranRepository.findOrchestratorSnapshotClientListByViewId(Mockito.anyString(), Mockito.anyString())).thenReturn(clients);
        List<String> list = new ArrayList<>();
        list.add("ViewDCF");

        List<OrchestratorSnapshot> orchestratorSnapshots = viewController.getOrchestratorSnapshotByViewName(list);
        Assert.assertEquals(orchestratorSnapshots.get(0).getClientCronExpressionList().get(0)
                .getClientExecProps().get(0).getEnvironmentProperties().get("dataCenter"), "sv2");
    }

    @Test
    public void getViewDefinitionCustomParamsTest() {
        Object[] record = {"viewinfo_1515000000", "View_DCF", "Final View for Digital Funnel", "Layer3_1513089094", "Layer 3", "View_DCF", "sql_definition", "800", "1", "0",
                "/dataplat/prod/views/", "journeyStartTime", "journeyStartTime", "JSON", "0", "dp2", "{\"queue\":\"LongRun\"}", "{\"L1\":\"RawIDM\"}"};
        List<Object[]> recordList = new ArrayList<>();
        recordList.add(record);
        DruidIngestData druidIngestData = new DruidIngestData();
        List<DruidIngestData> druidIngestDataList = new ArrayList<>();
        druidIngestDataList.add(druidIngestData);
        ViewColumnDefinition viewColumnDefinition = new ViewColumnDefinition();
        viewColumnDefinition.setColumnPath("/prod/RawIDM");
        viewColumnDefinition.setColumnDisplayName("journeyStartTime");
        viewColumnDefinition.setColumnName("journeyStartTime");
        viewColumnDefinition.setColumnId("view_column_1515000000");
        viewColumnDefinition.setUniqueKeyFlag(1);
        viewColumnDefinition.setDataType("string");
        viewColumnDefinition.setIsDimension(0);


        List<ViewColumnDefinition> viewColumnDefinitionList = new ArrayList<>();
        viewColumnDefinitionList.add(viewColumnDefinition);
        Object[] record1 = {"RawIDM", "/raw/prod/rtdp/idm/events/<client>"};
        Object[] record2 = {"AssistInteractions", "/raw/prod/<client>/pxassist/interactions"};
        List<Object[]> recordList1 = new ArrayList<>();
        recordList1.add(record1);
        recordList1.add(record2);
        View view = new View();
        view.setViewName("View_DCF");
        view.setIsActive(1);

        ViewInformation viewInformation = new ViewInformation();
        viewInformation.setDefinitionId("view_sql_def_1515000000");
        Map<String, String> map = new HashMap<>();
        map.put("L1", "RawIDM");
        map.put("L2", "AssistInteractions");
        viewInformation.setCustomParams(map);


        try {
            Mockito.when(viewRepository.findByViewName(Mockito.anyString())).thenReturn(view);
            Mockito.when(viewRepository.findViewDefinitionByViewName(Mockito.anyString())).thenReturn(recordList);
            Mockito.when(druidIngestDataRepository.findByViewId(Mockito.anyString())).thenReturn(druidIngestDataList);
            Mockito.when(viewColumnRepository.findByViewId(Mockito.anyString())).thenReturn(viewColumnDefinitionList);
            Mockito.when(viewHDFSSourceRepository.findHDFSSourceNameAndPathByViewId(Mockito.anyString())).thenReturn(recordList1);
            Mockito.when(viewInformationRepository.findByViewName(Mockito.anyString())).thenReturn(viewInformation);
            RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/catalog/view-definitions/View_DCF");
            MvcResult result = mockMvc.perform(requestBuilder).andReturn();
            System.out.println("response333 " + result.getResponse().getContentAsString());
            JSONArray resultJsonArray = new JSONArray(result.getResponse().getContentAsString());
            JSONObject jsonObj = new JSONObject(resultJsonArray.get(0).toString());
            String actualReponse = jsonObj.get("customParams").toString();
            String expectedresponse = "{\"L1\":\"RawIDM\"}";
            Assert.assertEquals(expectedresponse, actualReponse);
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }

    }

    @Test
    public void getAllOrchestratorSnapshotByViewName() throws Exception {
        String viewName = "Message_Survey_Report_Details_test2";
        View view = new View();
        view.setIsActive(1);
        view.setViewName(viewName);
        view.setViewGroup("viewGroup_c53f2ae1-8829-4e7d-afdc-a90317995b26");
        view.setOwner("DATAPP");
        view.setJobStartTime("1551744000000");
        view.setJobEndTime("1553040000000");
        view.setComplexity("1");
        view.setViewId("view_75ba8c21-a1c5-424a-9911-33e75153bc68");
        Object[] hilton = {"hilton", "{\"queue\":\"bdp2\"}", -1, "{}", "view_75ba8c21-a1c5-424a-9911-33e75153bc68", "0 3 * * *"};
        List<Object[]> clients = new ArrayList<>();
        clients.add(hilton);
        List<String> viewType = ViewType.getValues();
        List<View> views = new ArrayList<>();
        views.add(view);
        ViewGroup viewGroup = new ViewGroup();
        viewGroup.setId("viewGroup_c53f2ae1-8829-4e7d-afdc-a90317995b26");
        List<ViewGroup> viewGroups = new ArrayList<>();
        viewGroups.add(viewGroup);
        Map<String,String> executionProperties = new HashMap<>();
        executionProperties.put("queue","bdp2");
        viewGroup.setExecutionProperties(executionProperties);
        viewGroup.setEnvironmentProperties(new HashMap<String,String>());
        List<ViewOutputGranularity> viewOutputGranularity = new ArrayList<>();
        ViewOutputGranularity granularity = new ViewOutputGranularity();
        granularity.setId("vog_3bc8acad-2d28-452e-924c-b7b6a0cc1ffa");
        granularity.setViewId("view_75ba8c21-a1c5-424a-9911-33e75153bc68");
        granularity.setClientInfoId("client_info_d9ee6a3d-c3ca-484b-84d5-92c803c5da47");
        granularity.setExecutionProperties(executionProperties);
        granularity.setSlaBreachTime(-1);
        granularity.setCronExpression("0 3 * * *");
        ViewOutputGranularity granularity1 = new ViewOutputGranularity();
        granularity1.setId("vog_6d283076-2699-44a5-82cd-e1122077a345");
        granularity1.setViewId("view_75ba8c21-a1c5-424a-9911-33e75153bc68");
        granularity1.setClientInfoId("client_info_1513077760");
        granularity1.setExecutionProperties(executionProperties);
        granularity1.setSlaBreachTime(-1);
        granularity1.setCronExpression("0 3 * * *");
        viewOutputGranularity.add(granularity);
        viewOutputGranularity.add(granularity1);
        Mockito.when(viewRepository.findByIsActiveAndViewTypeIn(1, viewType)).thenReturn(views);
        Mockito.when(viewRepository.findByViewName(viewName)).thenReturn(view);
        Mockito.when(viewGroupRepository.findByViewID(Arrays.asList(view.getViewId()))).thenReturn(viewGroups);
        Mockito.when(viewOutputGranRepository.findAllUniqueCronString(Arrays.asList(view.getViewId()))).thenReturn(viewOutputGranularity);
        Mockito.when(viewOutputGranRepository.findAllOrchestratorSnapshotClientListByViewId(Arrays.asList(view.getViewId()))).thenReturn(clients);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/catalog/orchestrator/snapshot").contentType(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        System.out.println("response " + result.getResponse().getContentAsString());
        String expectedResponse = "[{\"viewName\":\"Message_Survey_Report_Details_test2\",\"clientCronExpressionList\":[{\"clientExecProps\":[{\"executionProperties\":{\"slaBreachTime\":\"-1\",\"queue\":\"bdp2\"},\"environmentProperties\":{},\"name\":\"hilton\"}],\"cronExpression\":\"0 3 * * *\"}],\"userName\":\"DATAPP\",\"jobStartTime\":\"1551744000000\",\"jobEndTime\":\"1553040000000\",\"complexity\":\"1\"}]";
        Assert.assertEquals(expectedResponse, result.getResponse().getContentAsString());
    }
}
