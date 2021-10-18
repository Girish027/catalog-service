package com.tfs.dp2.catalog.view;

import com.codahale.metrics.annotation.Timed;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.tfs.dp2.catalog.baseexceptions.*;
import com.tfs.dp2.catalog.clientinformation.Child;
import com.tfs.dp2.catalog.clientinformation.ClientInformation;
import com.tfs.dp2.catalog.clientinformation.ClientInformationRepository;
import com.tfs.dp2.catalog.clientinformation.ClientToInsert;
import com.tfs.dp2.catalog.cob.onboarder.PipelineOnBoarderUtils;
import com.tfs.dp2.catalog.dataset.Dataset;
import com.tfs.dp2.catalog.dataset.DatasetRepository;
import com.tfs.dp2.catalog.datasetRules.Rules;
import com.tfs.dp2.catalog.datasetRules.RulesRepository;
import com.tfs.dp2.catalog.dq.DQDataset;
import com.tfs.dp2.catalog.dq.ViewDataset;
import com.tfs.dp2.catalog.druidingestdata.DruidIngestData;
import com.tfs.dp2.catalog.druidingestdata.DruidIngestDataRepository;
import com.tfs.dp2.catalog.entities.ExecutionAttributes;
import com.tfs.dp2.catalog.entities.ViewGroup;
import com.tfs.dp2.catalog.exporters.*;
import com.tfs.dp2.catalog.repositories.ViewGroupRepository;
import com.tfs.dp2.catalog.rule.RuleScripts;
import com.tfs.dp2.catalog.services.Validator;
import com.tfs.dp2.catalog.services.ViewGroupService;
import com.tfs.dp2.catalog.sourceAdapter.SourceAdapterConfig;
import com.tfs.dp2.catalog.sourceAdapter.SourceAdapterRepository;
import com.tfs.dp2.catalog.util.CatalogAttributeConverter;
import com.tfs.dp2.catalog.util.Response;
import com.tfs.dp2.catalog.viewcolumndefinition.ViewColumnDefinition;
import com.tfs.dp2.catalog.viewcolumndefinition.ViewColumnRepository;
import com.tfs.dp2.catalog.viewhdfssource.ViewHDFSSource;
import com.tfs.dp2.catalog.viewhdfssource.ViewHDFSSourceRepository;
import com.tfs.dp2.catalog.viewoutputgran.*;
import com.tfs.dp2.catalog.viewsource.ViewSource;
import com.tfs.dp2.catalog.viewsource.ViewSourceRepository;
import com.tfs.dp2.catalog.viewsqldefinition.ViewSQLDefinition;
import com.tfs.dp2.catalog.viewsqldefinition.ViewSQLDefinitionRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import com.google.gson.*;

import javax.validation.Valid;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Rest end points for all CRUD operations on catalog views and clients
 * <p>
 * Created by bikesh.singh on 27-02-2018.
 */
@Slf4j
@EnableTransactionManagement
@RestController
@RequestMapping("/catalog")
public class ViewController {

    @Autowired
    private ViewRepository viewRepository;
    @Autowired
    private ViewColumnRepository viewColumnRepository;
    @Autowired
    private ClientInformationRepository clientInformationRepository;
    @Autowired
    private ViewOutputGranRepository viewOutputGranRepository;
    @Autowired
    private ViewHDFSSourceRepository viewHDFSSourceRepository;
    @Autowired
    private DruidIngestDataRepository druidIngestDataRepository;
    @Autowired
    private ViewSQLDefinitionRepository viewSQLDefinitionRepository;
    @Autowired
    private ViewSourceRepository viewSourceRepository;
    @Autowired
    private ViewInformationRepository viewInformationRepository;
    @Autowired
    private DatasetRepository datasetRepository;
    @Autowired
    private RulesRepository rulesRepository;
    @Autowired
    private ViewGroupRepository viewGroupRepository;
    @Autowired
    private ViewGroupService viewGroupService;
    @Autowired
    private SourceAdapterRepository sourceAdapterRepository;
    @Autowired
    private ExporterConfigRepository exporterConfigRepository;
    @Autowired
    private ExporterMappingRepository exporterMappingRepository;
    @Autowired
    private ExporterRepository exporterRepository;
    @Autowired
    private ExporterOverrridesRepository exporterOverrridesRepository;
    @Autowired
    private PipelineOnBoarderUtils pipelineOnBoarderUtils;
    @Autowired
    private Validator validator;

    private static final String CLIENT_REPLACE = "<client>";
    private static final String INSERTED = "inserted";
    private static final String CLIENT = "Client";
    private static final String HDFS_FOLDER = "HDFSFolder";
    private static final String NOT_ACTIVE_VIEW = " is not a active view";
    private static final String ACCOUNT = "Account";
    private static final String FUTURE_DATED_EFF_DATE_END_TIME = "Effective data range time is in future for the source views:";
    private static final String ESNotEnabled = "0";
    private static final String DruidNotEnabled = "0";
    private static final String SLA_BREACH_TIME = "slaBreachTime";

    private static final String QUEUE_KEY = ExecutionAttributes.QUEUE.getValue();

    private CatalogAttributeConverter converter = new CatalogAttributeConverter();

    @RequestMapping(path = "/health")
    @Timed
    public String health() {
        log.info("Calling Health API ::");
        String response = "{\"status\": \"ok\", \"Service\": \"catalog\"}";
        return response;
    }

    /* Returns details of all the views */
    @RequestMapping(path = "/views")
    @Timed
    public List<View> getViews() {
        log.info("Calling of Views API ::");
        List<View> views = viewRepository.findAll();
        try {
            if (null == views) {
                log.info("No views exists.");
                throw new ViewInformationNotFoundException("No View Information found");
            }
        } catch (ViewInformationNotFoundException ex) {
            log.info(ex.getMessage());
        }
        return views;
    }

    /* Returns details of particular view identified by View Id */
    @RequestMapping(path = "/views/{id}")
    @Timed
    public View getViews(@PathVariable("id") String viewId) {
        log.info("Calling of Views API for particular View Id ::");
        View view = viewRepository.findOne(viewId);
        try {
            if (null == view) {
                log.info("No View Information found for View Id : " + viewId);
                throw new ViewInformationNotFoundException("No View Information found for View Id : " + viewId);
            } else {
                log.info(view.getViewName());
            }
        } catch (ViewInformationNotFoundException ex) {
            log.info(ex.getMessage());
        }
        return view;
    }

    /* Returns details of all active views (where isActive is set and ViewType = fact/dim) */
    @RequestMapping(path = "/views/active")
    @Timed
    public List<View> getActiveViews() {
        log.info("Calling of Active Views API ::");
        List<String> viewType = ViewType.getValues();
        List<View> views = viewRepository.findByIsActiveAndViewTypeIn(1, viewType);
        if (views.isEmpty()) {
            log.info("No active views found");
        }
        return views;
    }

    /* Return all views for a client */
    @RequestMapping(value = "/client/views", method = RequestMethod.GET)
    @Timed
    public ClientViewLists getClientViewLists() {
        log.info("Calling of Client Views API");
        ClientViewLists clientViewLists = new ClientViewLists();
        try {
            List<ClientViewList> clientList = new ArrayList<>();
            List<String> clientIdList = viewOutputGranRepository.findDistinctClientInfoId();

            if (clientIdList.isEmpty()) {
                log.error("No Client Information found");
                throw new ClientInformationNotFoundException("No Client Information found");
            } else {
                clientIdList.parallelStream().forEach(record1 -> {
                    ClientViewList clientViewList = new ClientViewList();
                    List<ClientViews> viewsList = new ArrayList<>();
                    ClientInformation clientInformation = clientInformationRepository.findByInfoId(record1);
                    clientViewList.setClientName(clientInformation.getInfoName());

                    List<Object[]> result1 = viewOutputGranRepository.findDistinctViewNameandIsDQ(record1);
                    result1.parallelStream().forEach(record -> {
                        ClientViews clientViews = new ClientViews();
                        clientViews.setViewName(record[0].toString());
                        clientViews.setIsDQ(String.valueOf(record[1]));
                        viewsList.add(clientViews);
                    });
                    clientViewList.setViews(viewsList);
                    clientList.add(clientViewList);
                });
                clientViewLists.setClients(clientList);
            }
        } catch (ClientInformationNotFoundException ex) {
            throw new ClientInformationNotFoundException();
        }

        return clientViewLists;
    }

    /* Return a view with definitions, source and all the columns */
    @RequestMapping(value = "/source-time-range", method = RequestMethod.GET)
    @Timed
    public ViewSourceTimeRange getSourceTimeRange(@RequestParam(value = "viewname") String viewName,
                                                  @RequestParam(value = "clientname") String clientName,
                                                  @RequestParam(value = "scheduletime") String scheduleTime,
                                                  @RequestParam(value = "replay") Boolean isReplay) {
        try {
            if (!isActiveView(viewName)) {
                log.error(viewName + NOT_ACTIVE_VIEW);
                throw new IsNotActiveViewException(viewName + NOT_ACTIVE_VIEW);
            }

            log.info("Calling of Source Time Range API::");
            log.info("Parameters passed :: view name : {0} :: client name : {1} :: schedule time : :{2} :: replay : {3}" + viewName + " " + clientName + " " + scheduleTime + " " + isReplay);

            List<SourceTimeRange> sourceTimeRangeList = new ArrayList<>();
            long reference = 0;
            long granularity = 0;

            long scheduleTimeEpoch = ceilEpocToHour(scheduleTime);
            ViewSourceTimeRange viewSourceTimeRange = new ViewSourceTimeRange();
            viewSourceTimeRange.setViewName(viewName);
            viewSourceTimeRange.setClientName(clientName);
            long processStartTime;
            long processEndTime;

            Object[] result = viewOutputGranRepository.findTimingsByViewNameAndInfoName(viewName, clientName).get(0);
            reference = Long.parseLong(result[0].toString());
            final String viewId = result[3].toString();
            granularity = Long.parseLong(result[4].toString());
            final String clientId = result[5].toString();
            String finalJobExecutionExpression = "";
            String jobExecutionExpressionViewLevel = (result[8] == null) ? "" : result[8].toString();
            String jobExecutionExpressionClientViewLevel = (result[9] == null) ? "" : result[9].toString();
            if (jobExecutionExpressionClientViewLevel == null || jobExecutionExpressionClientViewLevel.isEmpty() || jobExecutionExpressionClientViewLevel.equals("")) {
                finalJobExecutionExpression = jobExecutionExpressionViewLevel.toString();
            } else {
                finalJobExecutionExpression = jobExecutionExpressionClientViewLevel.toString();
            }
            viewSourceTimeRange.setJobExecutionExpression(finalJobExecutionExpression);

            viewSourceTimeRange.setTimezone(result[10] == null ? null : result[10].toString());
            viewSourceTimeRange.setGranularity(result[11] == null ? null : result[11].toString());

            if (isReplay) {
                processStartTime = scheduleTimeEpoch;
            } else {
                processStartTime = getTime(scheduleTimeEpoch, reference);
            }
            processEndTime = getTime(processStartTime, granularity);
            viewSourceTimeRange.setScheduleStartTime("" + processStartTime);
            viewSourceTimeRange.setScheduleEndTime("" + processEndTime);
            viewSourceTimeRange.setExecutionProperties(fetchEffectiveExecutionProperties(result[6], result[7]));
            List<String> jarPath;
            List<String> jarPathsResultSet = viewOutputGranRepository.findProcessorJarLocationByViewName(viewName);

            jarPath = jarPathsResultSet;
            
            if (jarPath.isEmpty() || jarPath == null || jarPath.equals(""))
            {
                jarPath = null;
            }

            viewSourceTimeRange.setProcessorPlugInJarLoc(jarPath);

            List<Object[]> resultset1 = viewHDFSSourceRepository.findHDFSSourcePathDataRangeByFinalViewIdAndViewId(viewId, viewId);
            resultset1.parallelStream().forEach(record -> {

                SourceTimeRange sourceTimeRange = new SourceTimeRange();
                sourceTimeRange.setHdfsSourceViewID(record[0].toString());
                String corePath = record[1].toString();
                int dataRangeStart = Integer.parseInt(record[2].toString());
                int dataRangeEnd = Integer.parseInt(record[3].toString());
                int dataRange = (dataRangeEnd - dataRangeStart) / 60;//--//make function

                sourceTimeRange.setEndTime("" + getTime(processEndTime, dataRangeEnd));
                long startTime = getTime(processStartTime, dataRangeStart);
                sourceTimeRange.setStartTime("" + startTime);

                String clientNameToInsert = viewOutputGranRepository.findClientNameToInsertInPath(viewId, sourceTimeRange.getHdfsSourceViewID(), clientId);
                corePath = corePath.replace(CLIENT_REPLACE, clientNameToInsert).trim();

                long time = startTime;
                for (int i = 0; i <= dataRange; i++) {
                    String path = getPath(corePath, time);
                    List<String> pathList = sourceTimeRange.getPathList();
                    pathList.add(path);
                    sourceTimeRange.setPathList(pathList);
                    time = getTime(time, 60);
                }
                sourceTimeRangeList.add(sourceTimeRange);
            });

            viewSourceTimeRange.setSourceTimeRangeList(sourceTimeRangeList);
            List<String> exporterPluginJarLocationList = exporterMappingRepository.findAllExporterJarLocByClientIdAndViewId(clientId, viewId);
            viewSourceTimeRange.setExporterPluginJarLocList(exporterPluginJarLocationList);

            return viewSourceTimeRange;
        } catch (IsNotActiveViewException e) {
            throw new IsNotActiveViewException();
        }
    }

    /**
     * fetches the execution properties from view group and client view mapping level while client view mapping has precedence over group level values
     *
     * @param executionProperties
     * @param viewGroupName
     * @return final execution properties applicable
     */
    private Map<String, String> fetchEffectiveExecutionProperties(Object executionProperties, Object viewGroupName) {
        Map<String, String> clientViewMappingExecutionProps = new HashMap<>();
        Map<String, String> viewGroupExecutionProps = new HashMap<>();
        if (executionProperties != null) {
            clientViewMappingExecutionProps = converter.convertToEntityAttribute(executionProperties.toString());
        }
        if (viewGroupName != null) {
            ViewGroup viewGroup = viewGroupRepository.findById(viewGroupName.toString());
            if (viewGroup != null)
                viewGroupExecutionProps = viewGroup.getExecutionProperties();
        }
        viewGroupExecutionProps.putAll(clientViewMappingExecutionProps);
        return viewGroupExecutionProps;
    }

/*    private Optional<String> fetchEffectiveQueue(Object executionProperties, Object viewGroup) {
        Optional<String> queue = Optional.empty();
        if (executionProperties != null) {
            queue = getValueFromExecutionProperties(executionProperties, QUEUE_KEY);
        }
        if (!queue.isPresent() && viewGroup != null) {
            queue = viewGroupRepository.findById(viewGroup.toString()).getQueue();
        }
        return queue;
    }*/


    private long ceilEpocToHour(String timeEpoc) {
        long scheduleTimeEpoch = Long.parseLong(timeEpoc);
        Calendar cal = new GregorianCalendar();
        cal.setTimeZone(TimeZone.getTimeZone(CatalogConstants.TIMEZONE_UTC));
        cal.setTimeInMillis(scheduleTimeEpoch);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTimeInMillis();
    }

    /* Return a view with description, source and all the columns */
    @GetMapping(value = "/view-definitions/{viewname}")
    @Timed
    public List<ViewDefinition> getViewDefinitionByViewName(@PathVariable("viewname") List<String> viewNameList) {
        log.info("Calling of View Definition API::");
        List<ViewDefinition> viewDefinitionList = new ArrayList<>();
        viewNameList.parallelStream().forEach(viewName -> {
            try {
                if (!isActiveView(viewName)) {
                    throw new IsNotActiveViewException(viewName + NOT_ACTIVE_VIEW);
                }
                ViewDefinition viewDefinition = new ViewDefinition();
                List<UltimateSource> ultimateSourceList = new ArrayList<>();
                List<Object[]> result = viewRepository.findViewDefinitionByViewName(viewName);
                result.stream().forEach(record -> {
                    viewDefinition.setViewId(record[0].toString());
                    viewDefinition.setViewName(record[1].toString());
                    viewDefinition.setViewDescription(record[2].toString());
                    viewDefinition.setLayerId(record[3].toString());
                    viewDefinition.setLayerName(record[4].toString());
                    viewDefinition.setDefinitionName(record[5].toString());
                    viewDefinition.setSqlDefinition(record[6].toString());
                    viewDefinition.setTtlDays(record[7].toString());
                    viewDefinition.setEsIndexEnabled(record[8].toString());
                    viewDefinition.setDruidIndexEnabled(record[9].toString());
                    viewDefinition.setOutputPath(record[10].toString());
                    viewDefinition.setTimeDimensionColumn(record[11] == null ? "" : record[11].toString());
                    viewDefinition.setInputDataFormat(record[12].toString());
                    if ("0".equals(record[13].toString())) {
                        viewDefinition.setEsIndexEnabled(ESNotEnabled);
                        viewDefinition.setDruidIndexEnabled(DruidNotEnabled);
                    }
                    viewDefinition.setLoadStrategy(record[14].toString());
                    viewDefinition.setOwner(record[15].toString());

                    Optional<String> queue = fetchEffectiveQueue(record[16]);
                    if (queue.isPresent())
                        viewDefinition.getExecutionProperties().
                                put(QUEUE_KEY,
                                        queue.get());


                    Map<String, String> customParams=null;
                    if(record[17]!=null) {
                        customParams =  converter.convertToEntityAttribute(record[17].toString());
                    }
                    viewDefinition.setCustomParams(customParams);

                    viewDefinition.setDynamicBucketPath(record[18] == null ? "" : record[18].toString());
                });


                List<DruidIngestData> druidIngestDataList = druidIngestDataRepository.findByViewId(viewDefinition.getViewId());
                log.info("druidIngestDataList size - " + druidIngestDataList.size());
                if (!druidIngestDataList.isEmpty()) {
                    DruidIngestData druidIngestData = druidIngestDataList.get(0);
                    viewDefinition.setDruidIngestData(druidIngestData);
                }

                List<ViewColumnDefinition> viewColumnDefinitions = viewColumnRepository.findByViewId(viewDefinition.getViewId());
                for (ViewColumnDefinition viewColumnDefinition : viewColumnDefinitions) {
                    if (viewColumnDefinition.getUniqueKeyFlag() == 1 && viewColumnDefinition.getUniqueKeyOrder() == 1) {
                        viewDefinition.setUniqueColumn(viewColumnDefinition.getColumnName());
                    }
                }
                viewDefinition.setColumnList(viewColumnDefinitions);

                List<Object[]> ultimateSources = viewHDFSSourceRepository.findHDFSSourceNameAndPathByViewId(viewDefinition.getViewId());
                ultimateSources.parallelStream().forEach(record ->
                {
                    UltimateSource ultimateSource = new UltimateSource();
                    ultimateSource.setViewName(record[0].toString());
                    ultimateSource.setPath(record[1].toString());
                    ultimateSourceList.add(ultimateSource);

                });
                viewDefinition.setUltimateSourceList(ultimateSourceList);


                viewDefinitionList.add(viewDefinition);

            } catch (IsNotActiveViewException e) {
                log.error(e.getMessage());
                throw new IsNotActiveViewException();
            }
        });
        return viewDefinitionList;
    }

    @GetMapping(path = "/exporter-definitions/client/{client-id}")
    @Timed
    public Map<String,Object> getViewByClientAndExporterType(@PathVariable("client-id") String clientId, @RequestParam(value = "exporter-type") List<String> exporterType) {
        Map<String,Object> exporterMap = new HashMap<>();
        String clientName=viewRepository.getClientName(clientId);
        exporterMap.put("client-name",clientName);
        exporterType.stream().forEach(type ->
        {
            Map<String, Object> map = new HashMap<>();
            List<Object[]> viewExporterData = viewRepository.findViewByClientAndExporterType(clientId, type);
            List<Object[]> exporterConfig = viewRepository.findExporterConfig(type);
            Map<String, String> configMap = new HashMap<>();
            exporterConfig.stream().forEach(config -> configMap.put(config[0].toString(), config[1].toString())
            );
            List<ViewExporterList> viewExporterLists = new ArrayList<>();
            for (Object[] obj : viewExporterData) {
                ViewExporterList viewExporter = new ViewExporterList();
                try {
                    viewExporter.setViewName(obj[0].toString());
                    viewExporter.setViewId(obj[1].toString());
                    viewExporter.setScheduleStartTime(obj[2].toString());
                    viewExporter.setScheduleEndTime(obj[3].toString());
                    viewExporter.setGranularity(obj[4].toString());
                    viewExporter.setCronString(obj[5].toString());
                    viewExporterLists.add(viewExporter);
                } catch (Exception ex) {
                    log.warn("Incorrect data for view" + obj[0].toString());
                }
            }
            map.put("view", viewExporterLists);
            map.put("config", configMap);
            exporterMap.put(type, map);
        });
        return exporterMap;
    }


    @GetMapping(value = "/exporter-definitions/client/{client-name}/view/{view-name}")
    public ExporterDefinition getExporterDefinition(@PathVariable("view-name") String viewName, @PathVariable("client-name") String clientName) {

        if (!isActiveView(viewName)) {
            throw new ViewNotAvailableException();
        }
        String clientId = clientInformationRepository.findClientIdByClientNameAndAccountType(clientName, ACCOUNT);
        if (null == clientId) {
            throw new ClientNotAvailableException();
        }
        ExporterDefinition exporterDefinition = new ExporterDefinition();
        ViewInformation viewInformation = viewInformationRepository.findByViewName(viewName);
        String viewId = viewInformation.getViewId();
        exporterDefinition.setClientName(clientName);
        exporterDefinition.setViewName(viewName);
        exporterDefinition.setExporters(getEnabledExporters(viewId, clientId));
        exporterDefinition.setInputDataFormat(viewInformation.getFormat());
        exporterDefinition.setMaterializationEnabled("" + viewInformation.getMaterializationEnabled());
        ViewColumnDefinition timeDimensionColumn = viewColumnRepository.findOne(viewInformation.getTimeDimensionColumnName());
        exporterDefinition.setTimeDimensionColumn(timeDimensionColumn.getColumnName());
        exporterDefinition.setOutputPath(viewInformation.getViewOutputPath());
        List<ViewColumnDefinition> viewColumnDefinitions = viewColumnRepository.findByViewId(viewId);
        for (ViewColumnDefinition viewColumnDefinition : viewColumnDefinitions) {
            if (viewColumnDefinition.getUniqueKeyFlag() == 1 && viewColumnDefinition.getUniqueKeyOrder() == 1) {
                exporterDefinition.setUniqueColumn(viewColumnDefinition.getColumnName());
            }
        }
        exporterDefinition.setColumnList(viewColumnDefinitions);
        return exporterDefinition;
    }

    private List<ExportersEnabled> getEnabledExporters(String viewId, String clientId) {
        List<ExportersEnabled> exportersEnabledList = new ArrayList<>();
        List<Object[]> exporterList = exporterMappingRepository.findExporterMappingByClientIdAndViewId(clientId, viewId);
        exporterList.parallelStream().forEach(exporter -> {
            ExportersEnabled exportersEnabled = new ExportersEnabled();
            exportersEnabled.setName(exporter[0].toString());
            if (null != exporter[2]) {
                exportersEnabled.setExporterJarLoc(exporter[2].toString());
            }
            exportersEnabled.setExporterClassName(exporter[3].toString());
            List<ExporterConfig> exporterConfigList = exporterConfigRepository.findByExporterId(exporter[1].toString());
            Map<String, String> configMap = new HashMap<>();
            exporterConfigList.parallelStream().forEach(exporterConfig -> {
                configMap.put(exporterConfig.getConfigKey(), exporterConfig.getConfigValue());
            });
            List<ExporterOverrides> exporterOverridesList = exporterOverrridesRepository.findByExporterMappingId(exporter[4].toString());
            exporterOverridesList.parallelStream().forEach(exporterOverrides -> {
                configMap.put(exporterOverrides.getConfigKey(), exporterOverrides.getConfigValue());
            });
            exportersEnabled.setExporterConfig(configMap);
            exportersEnabledList.add(exportersEnabled);
        });
        return exportersEnabledList;
    }

    @GetMapping(value = "/exporters")
    public List<ExportersEnabled> getAllExporters() {
        List<ExportersEnabled> exportersEnabledList = new ArrayList<>();
        List<Exporter> exporterList = exporterRepository.findAll();
        exporterList.parallelStream().forEach(exporter -> {
            ExportersEnabled exportersEnabled = new ExportersEnabled();
            exportersEnabled.setName(exporter.getExporterName());
            if (null != exporter.getExporterJarLoc()) {
                exportersEnabled.setExporterJarLoc(exporter.getExporterJarLoc());
            }
            exportersEnabled.setExporterClassName(exporter.getExporterMainClass());
            List<ExporterConfig> exporterConfigList = exporterConfigRepository.findByExporterId(exporter.getExporterId());
            Map<String, String> configMap = new HashMap<>();
            exporterConfigList.parallelStream().forEach(exporterConfig -> {
                configMap.put(exporterConfig.getConfigKey(), exporterConfig.getConfigValue());
            });
            exportersEnabled.setExporterConfig(configMap);
            exportersEnabledList.add(exportersEnabled);
        });

        return exportersEnabledList;
    }

    private Optional<String> fetchEffectiveQueue(Object o) {
        if (o == null) {
            return Optional.empty();
        }
        return getValueFromExecutionProperties(o, QUEUE_KEY);
    }

    private Optional<String> getValueFromExecutionProperties(Object o, String key) {
        Map<String, String> executionProperties = converter.convertToEntityAttribute(o.toString());
        return Optional.ofNullable(executionProperties.get(key));
    }

    /* Return all views with description, source and all the columns */
    @GetMapping(value = "/view-definitions")
    @Timed
    public List<ViewDefinition> getViewDefinition() {
        List<ViewDefinition> viewDefinitionList = new ArrayList<>();
        List<String> viewNameList = new ArrayList<>();
        log.info("Calling of View Definition API for all Views");

        /* Calling function to find Active Views */
        List<View> activeViews = getActiveViews();
        String viewName = "";
        for (View viewInformation : activeViews) {
            viewName = viewInformation.getViewName();
            viewNameList.add(viewName);
        }
        try {
            /* Calling View Definition function for a particular view and storing in List of View Definition */
            viewDefinitionList = getViewDefinitionByViewName(viewNameList);
            log.info("View Definition List size : " + viewDefinitionList.size());
        } catch (Exception e) {
            log.info(e.getMessage());
        }
        return viewDefinitionList;
    }

    /* Returns details of specific view <input view name> having unique column, with id,name and column path details */
    @RequestMapping(value = "/dedup-view/{viewname}")
    @Timed
    public List<DedupView> getDedupView(@PathVariable("viewname") List<String> viewNameList) {
        List<DedupView> dedupViewList = new ArrayList<>();
        log.info("Calling of Dedup View API for particular view ::");

        viewNameList.parallelStream().forEach(viewName -> {
            try {
                if (!isActiveView(viewName)) {
                    throw new IsNotActiveViewException(viewName + NOT_ACTIVE_VIEW);
                }
                View view = viewRepository.findByViewName(viewName);
                DedupView dedupView = new DedupView();
                dedupView.setViewId(view.getViewId());
                dedupView.setViewName(view.getViewName());

                /* finding views with unique columns where Unique Key Flag is set active */
                List<ViewColumnDefinition> viewColumnDefinitionList =
                        viewColumnRepository.findByViewIdAndUniqueKeyFlagOrderByUniqueKeyOrder(dedupView.getViewId(), 1);
                if (!viewColumnDefinitionList.isEmpty()) {
                    viewColumnDefinitionList.stream().forEach(viewColumnDefinition ->
                    {
                        List<String> uniqueColumnPathList = dedupView.getUniqueColumnPathList();
                        uniqueColumnPathList.add(viewColumnDefinition.getColumnPath());
                        dedupView.setUniqueColumnPathList(uniqueColumnPathList);
                    });
                }
                dedupViewList.add(dedupView);
            } catch (IsNotActiveViewException e) {
                log.error(e.getMessage());
                throw new IsNotActiveViewException();
            }
        });
        return dedupViewList;
    }

    /* Returns all active views having unique columns, with id,name and column path details */
    @RequestMapping(value = "/dedup-view")
    @Timed
    public List<DedupView> getDedupViewAll() {
        log.info("Calling Dedup View API for all Views ::");
        List<String> viewNameList = new ArrayList<>();
        List<DedupView> dedupViewList;

        /* Calling function to find Active Views */
        List<View> activeViews = getActiveViews();
        log.info("active views size " + activeViews.size());
        String viewName = "";
        for (View views : activeViews) {
            viewName = views.getViewName();
            viewNameList.add(viewName);
        }
        log.info("view name list size " + viewNameList.size());
        dedupViewList = getDedupView(viewNameList);
        return dedupViewList;
    }

    /* Returns a view with orchestrator snapshot */
    @GetMapping("/orchestrator/snapshot/{viewname}")
    @Timed
    public List<OrchestratorSnapshot> getOrchestratorSnapshotByViewName(@PathVariable(value = "viewname") List<String> viewNameList) {
        log.info("Calling Orchestrator Snapshot API ::" + viewNameList.get(0));
        List<OrchestratorSnapshot> orchestratorSnapshotList = new ArrayList<>();
        viewNameList.stream().forEach(viewName -> {
            try {
                if (!isActiveView(viewName)) {
                    throw new IsNotActiveViewException(viewName + NOT_ACTIVE_VIEW);
                }
                List<ClientCronExpression> clientCronExpressionList = new ArrayList<>();
                View view = viewRepository.findByViewName(viewName);
                OrchestratorSnapshot orchestratorSnapshot = getOrchestratorSnapshot(view);
                ViewGroup viewGroup = viewGroupRepository.findById(view.getViewGroup());
                Map<String,String> groupExecProps = viewGroup.getExecutionProperties();
                Map<String, String> environmentProps = new HashMap<>();
                environmentProps.putAll(viewGroup.getEnvironmentProperties());
                environmentProps.putAll(view.environmentProperties);

                List<String> uniqueCronExpressions = viewOutputGranRepository.findUniqueCronString(view.getViewId());
                uniqueCronExpressions.stream().forEach(record -> {
                    getClientCronExpression(view, groupExecProps, record, clientCronExpressionList, environmentProps);
                });
                orchestratorSnapshot.setClientCronExpressionList(clientCronExpressionList);
                orchestratorSnapshotList.add(orchestratorSnapshot);
            } catch (IsNotActiveViewException isNotActiveViewException) {
                log.error(isNotActiveViewException.getMessage());
                throw new IsNotActiveViewException();
            }
        });
        return orchestratorSnapshotList;
    }

    public List<OrchestratorSnapshot> getOrchestratorSnapshotByViewID(Map<String, View> viewNameList) {
        log.info("Calling Orchestrator Snapshot API ::" + viewNameList.get(0));
        
        List<OrchestratorSnapshot> orchestratorSnapshotList = new ArrayList<>();
        Map<String, ViewGroup> viewGroupMap = new HashMap<>();
        List<String> viewIdList = getActiveViewID(viewNameList);
        List<ViewGroup> viewGroupList = viewGroupRepository.findByViewID(viewIdList);
        viewGroupList.stream().forEach(viewGroup -> viewGroupMap.put(viewGroup.getId(), viewGroup));
        Map<String, List<String>> viewToCronMap = getUniqueCronString(viewIdList);
        List<Object[]> orchestratorSnapshotClientListByViewId = getOrchestratorSnapshotClientListByViewId(viewIdList);
        viewNameList.keySet().stream().forEach(viewName -> {
            try {
                List<ClientCronExpression> clientCronExpressionList = new ArrayList<>();
                View view = viewNameList.get(viewName);
                OrchestratorSnapshot orchestratorSnapshot = getOrchestratorSnapshot(view);
                ViewGroup viewGroup = viewGroupMap.get(view.getViewGroup());
                Map<String,String> execPropsFromGroup = viewGroup.getExecutionProperties();
                Map<String, String> environmentProps = new HashMap<>();
                if (null != viewGroup) {
                    environmentProps.putAll(viewGroup.getEnvironmentProperties());
                }
                environmentProps.putAll(view.environmentProperties);
                List<String> uniqueCronExpressions = viewToCronMap.get(view.getViewId());
                if (null != uniqueCronExpressions) {
                    uniqueCronExpressions.stream().forEach(record -> {
                        getClientCronExpression(view, execPropsFromGroup, record, clientCronExpressionList, environmentProps, orchestratorSnapshotClientListByViewId);
                    });
                }
                orchestratorSnapshot.setClientCronExpressionList(clientCronExpressionList);
                orchestratorSnapshotList.add(orchestratorSnapshot);
            } catch (IsNotActiveViewException isNotActiveViewException) {
                log.error(isNotActiveViewException.getMessage());
                throw new IsNotActiveViewException();
            }
        });
        return orchestratorSnapshotList;
    }

    private Map<String, List<String>> getUniqueCronString(List<String> viewID) {
        Map<String, List<String>> viewGranularity = new HashMap<>();
        List<ViewOutputGranularity> viewOutputGranularity = viewOutputGranRepository.findAllUniqueCronString(viewID);
        viewOutputGranularity.stream().forEach(view -> {
            if (viewGranularity.containsKey(view.getViewId())) {
                List<String> list = viewGranularity.get(view.getViewId());
                list.add(view.getCronExpression());
                list = list.stream().distinct().collect(Collectors.toList());
                viewGranularity.replace(view.getViewId(), list);
            } else {
                List<String> list = new ArrayList<String>();
                list.add(view.getCronExpression());
                viewGranularity.put(view.getViewId(), list);
            }
        });
        return viewGranularity;
    }

    private List<Object[]> getOrchestratorSnapshotClientListByViewId(List<String> viewID) {
        List<Object[]> clients = viewOutputGranRepository.findAllOrchestratorSnapshotClientListByViewId(viewID);
        return clients;
    }

    private List<String> getActiveViewID(Map<String, View> viewNameList) {
        List<String> viewID = new ArrayList<>();
        if (null != viewNameList) {
            viewNameList.values().stream().forEach(viewId -> viewID.add(viewId.getViewId()));
        }
        return viewID;
    }

    private OrchestratorSnapshot getOrchestratorSnapshot(View view) {
        OrchestratorSnapshot orchestratorSnapshot = new OrchestratorSnapshot();
        orchestratorSnapshot.setViewName(view.getViewName());
        orchestratorSnapshot.setUserName(view.getOwner());
        orchestratorSnapshot.setJobStartTime(view.getJobStartTime());
        orchestratorSnapshot.setJobEndTime(view.getJobEndTime());
        orchestratorSnapshot.setComplexity(view.getComplexity());
        return orchestratorSnapshot;
    }

    private void getClientCronExpression(View view, Map<String,String> execPropsViewGroup, String record, List<ClientCronExpression> clientCronExpressionList,
                                         Map<String, String> environmentProps, List<Object[]> orchestratorSnapshotClientListByViewId) {
        ClientCronExpression clientCronExpression = new ClientCronExpression();
        clientCronExpression.setCronExpression(record);
        List<Object[]> clients = getorchestratorSnapshotClientList(view.getViewId(), record, orchestratorSnapshotClientListByViewId);
        List<ClientExecutionProperties> clientExecutionPropertiesList = new ArrayList<>();
        populateClientExecutionProperties(execPropsViewGroup, clients, clientExecutionPropertiesList, environmentProps);
        clientCronExpression.setClientExecProps(clientExecutionPropertiesList);
        clientCronExpressionList.add(clientCronExpression);
    }

    private List<Object[]> getorchestratorSnapshotClientList(String viewID, String cronExpression, List<Object[]> orchestratorSnapshotClientListByViewId) {
        List<Object[]> cronFilter = new ArrayList<>();
        orchestratorSnapshotClientListByViewId.stream().forEach(objects -> {
            if (objects[6].equals(viewID) && objects[7].equals(cronExpression)) {
                cronFilter.add(objects);
            }
        });
        return cronFilter;
    }

    private void getClientCronExpression(View view, Map<String,String> execProps, String record, List<ClientCronExpression> clientCronExpressionList,
                                         Map<String, String> environmentProps) {
        ClientCronExpression clientCronExpression = new ClientCronExpression();
        clientCronExpression.setCronExpression(record);
        List<Object[]> clients = viewOutputGranRepository.findOrchestratorSnapshotClientListByViewId(view.getViewId(), record);
        List<ClientExecutionProperties> clientExecutionPropertiesList = new ArrayList<>();
        populateClientExecutionProperties(execProps, clients, clientExecutionPropertiesList, environmentProps);
        clientCronExpression.setClientExecProps(clientExecutionPropertiesList);
        clientCronExpressionList.add(clientCronExpression);
    }

    private String fetchQueueFromViewGroup(ViewGroup viewGroup) {
        return (viewGroup == null) ? null : (viewGroup.getProperty(ExecutionAttributes.QUEUE).isPresent() ? viewGroup.getProperty(ExecutionAttributes.QUEUE).get() : null);
    }

    private void populateClientExecutionProperties(Map<String,String> execPropsViewGroup, List<Object[]> clients, List<ClientExecutionProperties> clientExecutionPropertiesList, Map<String, String> environmentProps) {
        clients.stream().forEach(client -> {
            ClientExecutionProperties clientExecutionProperties = new ClientExecutionProperties();
            clientExecutionProperties.setClientName(client[0].toString());
            populateProperties(execPropsViewGroup, client[1], clientExecutionProperties);
            String slaBreachTime = client[2].toString();
            Map<String, String> clientViewLevelEnvironmentProps = new HashMap<>();
            clientViewLevelEnvironmentProps.putAll(environmentProps);
            populateSlaBreachProperties(slaBreachTime, clientExecutionProperties);
            populateEnvironmentProperties(client[3], clientViewLevelEnvironmentProps);
            clientExecutionProperties.setEnvironmentProperties(clientViewLevelEnvironmentProps);
            clientExecutionProperties.setJobStartTime(client[4] == null ? null : client[4].toString());
            clientExecutionProperties.setJobEndTime(client[5] == null ? null : client[5].toString());
            clientExecutionPropertiesList.add(clientExecutionProperties);
        });
    }

    private void populateEnvironmentProperties(Object o, Map<String, String> environmentProps) {
        environmentProps.putAll(converter.convertToEntityAttribute(o.toString()));
    }

    private void populateSlaBreachProperties(String slaBreachTime, ClientExecutionProperties clientExecutionProperties) {

        clientExecutionProperties.getExecutionProperties().put(SLA_BREACH_TIME, slaBreachTime);
    }

    private void populateProperties(Map<String,String> execPropsViewGroup, Object outputGranularityAttribute, ClientExecutionProperties clientExecutionProperties) {
        if (execPropsViewGroup != null && execPropsViewGroup.size() >0) {
            clientExecutionProperties.getExecutionProperties().putAll(execPropsViewGroup);
        }
        if (outputGranularityAttribute != null) {
            Map<String, String> execPropsClientViewMapping = converter.convertToEntityAttribute(outputGranularityAttribute.toString());
            if (execPropsClientViewMapping.size()>0)
                clientExecutionProperties.getExecutionProperties().putAll(execPropsClientViewMapping);
        }
        validateExecProps(clientExecutionProperties.getExecutionProperties());
    }

    private void validateExecProps(Map<String,String> execProps) {
        Map<String,String> temp = new HashMap<>();
        temp.putAll(execProps);
        for(String key : temp.keySet())
        {
            if(!CatalogConstants.executionPropertiesKeys.contains(key)){
                log.info("removed execution property as it is invalid "+key);
                execProps.remove(key);
            }
        }
    }

    /* Returns all views with orchestrator snapshot */
    @GetMapping(value = "/orchestrator/snapshot")
    @Timed
    public List<OrchestratorSnapshot> getOrchestratorSnapshot() {
        log.info("Calling orchestrator snapshot API for all views ::");
        Map<String, View> viewNameList = new HashMap<>();
        List<OrchestratorSnapshot> orchestratorSnapshotList;

        /* Calling function to find Active Views */
        List<View> activeViews = getActiveViews();
        String name = "";
        for (View view : activeViews) {
            name = view.getViewName();
            viewNameList.put(name, view);
        }

        /* Calling getOrchestratorSnapshotByViewName function to get Orchestrator Snapshot for a particular view */
        orchestratorSnapshotList = getOrchestratorSnapshotByViewID(viewNameList);
        return orchestratorSnapshotList;
    }


    /* Returns a view with processor snapshot */
    @GetMapping(value = "/processor/snapshot")
    @Timed
    public ProcessorSnapshot getProcessorSnapshot(
            @RequestParam(value = "viewname") String viewName, @RequestParam(value = "clientname") String clientName,
            @RequestParam(value = "scheduletime", required = false) String scheduleTime, @RequestParam(value = "scheduleStartTime",
            required = false) String scheduleStartTime, @RequestParam(value = "scheduleEndTime", required = false)
                    String scheduleEndTime, @RequestParam(value = "finalview") String finalView) {
        log.info("Calling Processor Snapshot API ::");
        log.info("Parameters passed in processor snapshot--view name---" + viewName + "--client--" + clientName +
                "--scheduleStartTime--" + scheduleStartTime + "--scheduleEndTime--" + scheduleEndTime +
                "--final view name---" + finalView);
        //ScheduleTime requestParam to be removed in next release
        if(null == scheduleStartTime){
            scheduleStartTime = scheduleTime;
        }
        ProcessorSnapshot processorSnapshot = null;
        List<Object[]> resultSet;
        try {
            boolean isView = true;
            String aliasName = null;
            if (!isActiveView(viewName)) {
                throw new IsNotActiveViewException(viewName + NOT_ACTIVE_VIEW);
            }
            long scheduleStartTimeEpoch = Long.parseLong(scheduleStartTime);
            String viewId = null;
            long granularity = 0;
            String clientHDFSFolderId = "";
            long processStartTime;
            long processEndTime;
            /* Calling query to find ProcessorSnapshot By ClientName, ViewName And Final View */
            resultSet = viewOutputGranRepository.findProcessorSnapshotByClientNameViewNameAndFinalView(clientName, viewName, finalView);

            if (null == resultSet || resultSet.size() == 0) {
                isView = false;
                String finalViewForAlias = null;
                aliasName = viewName.split("__").length > 1 ? viewName.split("__")[0] : viewName;
                finalViewForAlias = viewName.split("__").length > 1 ? viewName.split("__")[1] : viewName;
                /* Calling query to find ProcessorSnapshot By ClientName, Alias And Final View*/
                resultSet = viewOutputGranRepository.findProcessorSnapshotByClientNameAliasAndFinalView(clientName, aliasName, finalViewForAlias, finalView);
                log.info("Alias-- " + aliasName + " is called for");
            }
            log.info("resultset" + resultSet.size());
            for (Object[] record : resultSet) {
                processorSnapshot = new ProcessorSnapshot();
                processorSnapshot.setViewName(record[0].toString());
                processorSnapshot.setPath(record[1].toString());
                processorSnapshot.setReferenceTime(record[2].toString());
                processorSnapshot.setFormat(record[3].toString());
                processorSnapshot.setTimeDimensionColumn(record[4] == null ? "" : record[4].toString());
                viewId = record[5].toString();
                granularity = Long.parseLong(record[6].toString());
                clientHDFSFolderId = record[7].toString();
                boolean dqEnabled = BooleanUtils.toBoolean(record[8].toString(), "1", "0");
                boolean materializationEnabled = BooleanUtils.toBoolean(record[9].toString(), "1", "0");
                processorSnapshot.setMaterializationEnabled(materializationEnabled);
                if (viewName.equals(finalView)) {
                    processorSnapshot.setDqEnabled(dqEnabled);
                }
                processorSnapshot.setProcessingPlugInType(null == record[10] ? null : record[10].toString());
                processorSnapshot.setProcessorPlugInMainClass(null == record[11] ? null : record[11].toString());
                boolean preferReadMaterializedData = BooleanUtils.toBoolean(record[12].toString(), "1", "0");
                processorSnapshot.setPreferReadMaterializedData(preferReadMaterializedData);
                processorSnapshot.setSourcePluginClass(null == record[13] ? null : record[13].toString());
                processorSnapshot.setLoadStrategy(record[14].toString());

                Map<String, String> customParams=null;
                if(record[15]!=null) {
                    customParams =  converter.convertToEntityAttribute(record[15].toString());
                }
                processorSnapshot.setCustomParams(customParams);
                processorSnapshot.setDynamicBucketPath(record[16] == null ? "" : record[16].toString());

            }

            log.info("Processor View Name--" + processorSnapshot.getViewName() + "--viewId--" + viewId);

            String finalViewId = viewRepository.findViewIdByViewName(finalView);
            String path = viewHDFSSourceRepository.findHDFSSourcePathByViewIdAndFinalViewId(finalViewId, viewId);
            log.info("path --- " + path + "  --final view id--- " + finalViewId);
            if (path != null && !path.isEmpty()) {
                if (path.contains(CLIENT_REPLACE)) {
                    ClientInformation clientInformation = clientInformationRepository.findByInfoId(clientHDFSFolderId);
                    path = path.replace(CLIENT_REPLACE, clientInformation.getInfoName());
                }
                processorSnapshot.setPath(path);
            }

            List<SourceAdapterConfig> sourceAdapterConfigList = sourceAdapterRepository.findByViewId(viewId);
            HashMap<String, String> sourceConfigHashMap = new HashMap<>();
            if (!sourceAdapterConfigList.isEmpty()) {
                for (SourceAdapterConfig sourceAdapterConfig : sourceAdapterConfigList) {
                    sourceConfigHashMap.put(sourceAdapterConfig.getKeyName(), sourceAdapterConfig.getValueName());
                }
                processorSnapshot.setSourceConfigList(sourceConfigHashMap);
            }

            List<ViewColumnDefinition> viewColumnDefinitionList = viewColumnRepository.findByViewIdAndUniqueKeyFlagOrderByUniqueKeyOrder
                    (viewId, 1);
            if (!viewColumnDefinitionList.isEmpty()) {
                for (ViewColumnDefinition viewColumnDefinition : viewColumnDefinitionList) {
                    List<String> uniqueFields = processorSnapshot.getUniqueFields();
                    uniqueFields.add(viewColumnDefinition.getColumnPath());
                    processorSnapshot.setUniqueFields(uniqueFields);
                }
            }

            List<ViewColumnDefinition> viewColumnDefinitions = viewColumnRepository.findByViewId(viewId);
            processorSnapshot.setColumns(viewColumnDefinitions);

            processStartTime = scheduleStartTimeEpoch;

            /*
            * Fixed Bug to take End time form Parents End Time instead of Final View's End time
            * Else clause if there for backward compatibility where scheduleEndTime is not being passed from Spartan while caling this API.*/
            if(scheduleEndTime!=null){
                long scheduleEndTimeEpoch = Long.parseLong(scheduleEndTime);
                processEndTime = scheduleEndTimeEpoch;
            } else{
                processEndTime = getTime(scheduleStartTimeEpoch, granularity);
            }

            log.info("processor start time-- " + processStartTime + " processor end time-- " + processEndTime);

            /* Calling query to find attributes of Imports entity by ViewId and View Name */
            List<Object[]> importList = viewRepository.findImportsByViewIdAndViewName(viewId, finalView);
            if (null != importList) {
                for (Object[] record : importList) {
                    Imports imports = new Imports();
                    String viAlName = record[0].toString();
                    imports.setViewName(viAlName);
                    int dataRangeStart = Integer.parseInt(record[1].toString());
                    int dataRangeEnd = Integer.parseInt(record[2].toString());
                    imports.setStartTime("" + getTime(processStartTime, dataRangeStart));
                    imports.setEndTime("" + getTime(processEndTime, dataRangeEnd));
                    processorSnapshot.importsList.add(imports);
                }
            }


            String sqlStringKey = null;
            String sqlStringValue = null;

            /* Calling query to find Processor Snapshot SQLs by View Id */
            int dynamicSql = 0;
            String result4 = viewRepository.findIsDynamicSQL(viewId);
            dynamicSql = Integer.parseInt(result4);
            /* Calling query to find Processor Snapshot SQLs by View Id */
            if ((processorSnapshot.getViewName().contains("DQ_") || processorSnapshot.getViewName().contains("_DQ")) && dynamicSql == 1) {
                List<String> viewIdList = new ArrayList<>();
                String soruceViewId = null;
                List<Object[]> result5 = viewRepository.findProcessorSnapshotSourceViewIdByDqViewId(processorSnapshot.getViewName());
                soruceViewId = result5.toString();
                viewIdList.add(result5.toString());

                String sourceViewName = viewRepository.findViewNameByViewId(soruceViewId);
                HashMap<String, String> sqlString = new HashMap<>();
                sqlStringKey = processorSnapshot.getViewName();
                sqlStringValue = getViewRuleScripts(viewIdList);
                sqlStringValue = sqlStringValue.replace("_viewName", sourceViewName);
                sqlString.put(sqlStringKey, sqlStringValue);
                processorSnapshot.sqls.add(sqlString);

            } else {
                List<String> result3 = viewRepository.findProcessorSnapshotSQLsByViewId(viewId);
                for (String record : result3) {
                    HashMap<String, String> sqlString = new HashMap<>();
                    sqlStringKey = isView ? viewName : aliasName;
                    sqlStringValue = record;
                    sqlString.put(sqlStringKey, sqlStringValue);
                    processorSnapshot.sqls.add(sqlString);
                }
            }
        } catch (IsNotActiveViewException e) {
            log.error(e.toString());
            throw new IsNotActiveViewException();
        } catch (NumberFormatException e) {
            log.error(e.toString());
            throw new NumberFormatException();
        }

        return processorSnapshot;
    }

    private String getViewRuleScripts(List<String> viewIdList) {
        List<ViewRuleScripts> viewRuleScriptsList = new ArrayList<>();
        RuleScripts ruleScripts = new RuleScripts();
        String dqSql = "";
        for (String viewId : viewIdList) {
            ViewRuleScripts viewRuleScripts = new ViewRuleScripts();
            List<String> ruleScriptsList = new ArrayList<>();
            List<String> uniqueKeys;
            String viewName = viewRepository.findViewNameByViewId(viewId);
            viewRuleScripts.setViewName(viewName);
            List<Object[]> resultx = viewRepository.findColumnsForDq(viewId);

            resultx.stream().forEach(record -> {
                ruleScripts.sqlDefinition = record[2].toString().replace("_columnKey", record[0].toString());
                ruleScripts.sqlDefinition = ruleScripts.sqlDefinition.replace("_columnPath", record[4].toString());
                ruleScriptsList.add(ruleScripts.sqlDefinition);
            });

            String clientId = null;
            String eventTime = null;
            uniqueKeys = viewRepository.findUniqueKeyByView(viewId);
            eventTime = viewRepository.findEventTimeColumnPathByView(viewId);
            clientId = viewRepository.findClientIdColumnPathByView(viewId);
            for (String str : ruleScriptsList) {
                str = str.replace("_primaryKey", uniqueKeys.toString().replace("[", "concat(").replace("]", ")"));
                str = str.replace("epochTime_Path", eventTime);
                str = str.replace("clientId_Path", clientId);
                if (dqSql.equals("")) {
                    dqSql = str;
                } else {
                    dqSql = dqSql.concat(" UNION ALL " + str);
                }

            }
            viewRuleScriptsList.add(viewRuleScripts);
            ruleScripts.sqlDefinition = ruleScripts.sqlDefinition.replace("_primaryKey", uniqueKeys.toString().replace("[", "concat(").replace("]", ")"));
        }

        return dqSql;


    }

    @GetMapping(value = "/{viewname}/dqdatasets")
    @Timed
    public ViewDataset getDatasetAndRules(@PathVariable(value = "viewname") String viewName) {
        ViewDataset viewDataset = new ViewDataset();
        try {
            if (!isActiveView(viewName)) {
                throw new IsNotActiveViewException(viewName + NOT_ACTIVE_VIEW);
            }
            List<Dataset> datasetList = datasetRepository.findByViewName(viewName);
            if (datasetList == null || datasetList.isEmpty()) {
                throw new DatasetNotAvailableException("No dq dataset available for " + viewName);
            }
            datasetList.parallelStream().forEach(dataset -> {
                DQDataset dqDataset = new DQDataset();
                dqDataset.setDatasetId(dataset.getDatasetId());
                dqDataset.setDatasetName(dataset.getDatasetName());
                dqDataset.setDatasetDescription(dataset.getDescription());
                dqDataset.setDatasetSql(dataset.getDatasetSql());
                dqDataset.setLevel(dataset.getLevel());
                dqDataset.setRecordIdentifier(dataset.getRecordIdentifier());
                dqDataset.setRuleList(rulesRepository.findByDatasetId(dataset.getDatasetId()));
                viewDataset.getDqDatasetList().add(dqDataset);
            });
        } catch (IsNotActiveViewException isNotActiveViewException) {
            log.error(isNotActiveViewException.getMessage());
            throw new IsNotActiveViewException();
        } catch (DatasetNotAvailableException datasetNotAvailableException) {
            log.error(datasetNotAvailableException.getMessage());
            throw new DatasetNotAvailableException();
        }
        return viewDataset;
    }

    @GetMapping(value = "/pipeline-details")
    @Timed
    public List<PipelineOwnerDetails> findPipelineOwnerDetails() {
        log.info("calling api to fetch pipeline owner details");
        List<Object[]> pipelineOwnerDetailsList = viewOutputGranRepository.findPipelineOwnerDetails();
        return populatePipelineDetails(pipelineOwnerDetailsList);
    }

    private List<PipelineOwnerDetails> populatePipelineDetails(List<Object[]> pipelineOwnerDetailsList) {
        List<PipelineOwnerDetails> finalList = new ArrayList<>();
        pipelineOwnerDetailsList.stream().forEach(record ->
        {
            PipelineOwnerDetails pipelineOwnerDetails = new PipelineOwnerDetails();
            pipelineOwnerDetails.setViewName(record[0].toString());
            pipelineOwnerDetails.setOwner(record[1].toString());
            pipelineOwnerDetails.setClientName(record[2].toString());
            pipelineOwnerDetails.setSlaBreachTime(Integer.parseInt(record[3].toString()));
            pipelineOwnerDetails.setOwnerEmailId(record[4] == null ? "" : record[4].toString());
            finalList.add(pipelineOwnerDetails);
        });
        return finalList;
    }


    private ViewToInsert createAliasForViewName(ViewToInsert viewToInsert, final String ViewId) {
        final Set<String> aliases = new HashSet<String>();
        List<ViewSource> viewSourceList = new ArrayList<ViewSource>();
        try {

            viewToInsert.getSourceList().parallelStream().forEach(viewSource -> {
                if (null != viewSource.getAlias()
                        && viewSource.getAlias().trim().length() > 0) {
                    String checkForAliasNotSameAsViewName = viewRepository.findViewIdByViewName(viewToInsert.viewInformation.getViewName());
                    if (viewSource.getAlias().compareTo(checkForAliasNotSameAsViewName) == 0)
                        throw new ViewAlreadyExistException(String.format("Alias name cannot be same as existing View : %s", viewToInsert.viewInformation.getViewName()));
                    aliases.add(viewSource.getAlias());
                    if (hasSourceAliasExists(ViewId, viewSource.getSourceName(), viewSource.getAlias()).size() > 0)
                        throw new ViewAlreadyExistException(String.format("%s already exist in database for the Child View : %s and Parent View : %s", viewSource.getAlias(), viewSource.getSourceName(), viewToInsert.viewInformation.getViewName()));
                } else
                    throw new ViewAlreadyExistException(viewToInsert.viewInformation.getViewName() + " already exist in database");

                UUID uuidViewSource = UUID.randomUUID();
                String randomViewSourceId = "source_" + uuidViewSource.toString();
                viewSource.setSourceId(randomViewSourceId);
                String sourceViewId = viewRepository.findViewIdByViewName(viewSource.getSourceName());
                viewSource.setSourceName(sourceViewId);
                viewSource.setViewId(ViewId);
                viewSource.setFinalViewId(ViewId);
                viewSourceList.add(viewSource);
            });
            if (aliases.size() != viewToInsert.getSourceList().size())
                throw new ViewAlreadyExistException(String.format("Two aliases cannot be equal at same level:%s", viewToInsert.viewInformation.getViewName()));
            viewSourceRepository.save(viewSourceList);
        } finally {
            aliases.clear();
            viewSourceList.clear();
        }
        return viewToInsert;
    }


    @Transactional
    @PostMapping(value = "/view")
    @Timed
    public String insertView(@Valid @RequestBody ViewToInsert viewToInsert) throws ViewGroupNotAvailableException {

        try {
            final String viewAvailabilityCheck;
            String finaViewID = null;
            Map<String, String> customParams = new HashMap<>();
            ViewInformation vwInfo = viewToInsert.viewInformation;
            vwInfo.setEnvironmentProperties(viewToInsert.environmentProperties);
            vwInfo.setCustomParams(viewToInsert.customParams);
            viewAvailabilityCheck = viewRepository.findViewIdByViewName(vwInfo.getViewName());
            if (null != viewAvailabilityCheck) {
                createAliasForViewName(viewToInsert, viewAvailabilityCheck);
                log.warn(String.format("SQL for existing view : %s and source view already existing hence alias created without updating SQL", vwInfo.getViewName()));
                return INSERTED;
            } else {

                vwInfo.setViewGroup(validateViewGroup(vwInfo.getViewGroup()).getId());

                String timeDimensionColumnId = null;
                int uniqueColumn = 0;
                ViewSQLDefinition viewSQLDefinition = viewToInsert.viewSQLDefinition;
                UUID uuidViewSQL = UUID.randomUUID();
                String randomViewSQLDefinitionId = "view_def_" + uuidViewSQL.toString();
                viewSQLDefinition.setDefinitionId(randomViewSQLDefinitionId);

                ViewInformation viewInformation = vwInfo;
                UUID uuidView = UUID.randomUUID();
                String randomViewId = "view_" + uuidView.toString();
                viewInformation.setViewId(randomViewId);
                viewInformation.setDefinitionId(randomViewSQLDefinitionId);
                String layerId = viewRepository.findLayerIdByLayerName(viewInformation.getLayerName());
                viewInformation.setLayerName(layerId);
                try {
                    DefaultClientViewMapping defaultClientViewMapping = viewToInsert.getDefaultClientViewMapping();
                    viewInformation.setDefaultClientViewMapping(defaultClientViewMapping == null ? "" : pipelineOnBoarderUtils.getObjectMapper().writeValueAsString(defaultClientViewMapping));
                } catch (JsonProcessingException e) {
                    String message = "Unable to insert view";
                    log.error(message, e);
                    throw new CatalogInternalException(message);
                }

                viewSQLDefinition.setViewId(randomViewId);

                DruidIngestData druidIngestData = null;
                if (null != viewToInsert.druidIngestionData) {
                    druidIngestData = viewToInsert.druidIngestionData;
                    UUID uuidDruid = UUID.randomUUID();
                    String randomDruidIngestDataId = "druid_" + uuidDruid.toString();
                    druidIngestData.setDruidIngestionId(randomDruidIngestDataId);
                    druidIngestData.setViewId(randomViewId);
                }

                List<ViewSource> viewSourceListFinal = new ArrayList<>();
                List<ViewSource> viewSourceList = viewToInsert.sourceList;
                if (!viewInformation.getLayerName().contains("Layer1")) {
                    viewSourceList.parallelStream().forEach(viewSource -> {
                        UUID uuidViewSource = UUID.randomUUID();
                        String randomViewSourceId = "source_" + uuidViewSource.toString();
                        viewSource.setSourceId(randomViewSourceId);
                        String sourceViewId = viewRepository.findViewIdByViewName(viewSource.getSourceName());
                        viewSource.setSourceName(sourceViewId);
                        viewSource.setViewId(randomViewId);
                        viewSource.setFinalViewId(randomViewId);
                        viewSourceListFinal.add(viewSource);
                        List<ViewSource> viewSourceList1 = viewSourceRepository.findAllWithFinalViewId(sourceViewId);
                        log.info("viewSourceList1 -- " + viewSourceList1.size());
                        viewSourceList1.parallelStream().forEach(viewSource1 -> {
                            ViewSource viewSource2 = new ViewSource();
                            UUID uuidViewSource1 = UUID.randomUUID();
                            String randomViewSourceId1 = "source_" + uuidViewSource1.toString();
                            viewSource2.setSourceId(randomViewSourceId1);
                            viewSource2.setFinalViewId(randomViewId);
                            if (null == viewSource1.getSourceName()) {
                                viewSource2.setSourceName("");
                            } else {
                                viewSource2.setSourceName(viewSource1.getSourceName());
                            }
                            viewSource2.setViewId(viewSource1.getViewId());
                            viewSource2.setSourceType(viewSource1.getSourceType());
                            viewSource2.setDataRangeStartMin(viewSource1.getDataRangeStartMin());
                            viewSource2.setDataRangeEndMin(viewSource1.getDataRangeEndMin());
                            viewSource2.setSourceOrder(2);

                            viewSourceListFinal.add(viewSource2);
                        });
                    });
                } else {
                    viewSourceList.parallelStream().forEach(viewSource -> {
                        UUID uuidViewSource = UUID.randomUUID();
                        String randomViewSourceId = "source_" + uuidViewSource.toString();
                        viewSource.setSourceId(randomViewSourceId);
                        viewSource.setSourceName("");
                        viewSource.setViewId(randomViewId);
                        viewSource.setFinalViewId(randomViewId);
                        viewSourceListFinal.add(viewSource);


                        List<SourceAdapterConfig> sourceAdapterConfigListFinal = new ArrayList<>();
                        List<SourceAdapterConfig> sourceAdapterConfigList = viewToInsert.sourceAdapterConfig;
                        sourceAdapterConfigList.parallelStream().forEach(sourceAdapterConfig -> {
                            sourceAdapterConfig.setSourceConfigId("configId_" + UUID.randomUUID());
                            sourceAdapterConfig.setViewId(randomViewId);
                            sourceAdapterConfig.setSourceType(viewInformation.getFormat());
                            sourceAdapterConfigListFinal.add(sourceAdapterConfig);
                        });
                        sourceAdapterRepository.save(sourceAdapterConfigListFinal);
                    });
                }
                List<ViewHDFSSource> viewHDFSSourceList = viewToInsert.hdfsSourceList;
                viewHDFSSourceList.parallelStream().forEach(viewHDFSSource -> {
                    UUID uuidViewHDFSSource = UUID.randomUUID();
                    String randomViewHDFSSourceId = "hdfs_" + uuidViewHDFSSource.toString();
                    viewHDFSSource.setHdfsSourceId(randomViewHDFSSourceId);
                    viewHDFSSource.setViewId(randomViewId);
                    String hdfsSourceViewId = viewRepository.findViewIdByViewName(viewHDFSSource.getHdfsSourceName());
                    viewHDFSSource.setHdfsSourceName(hdfsSourceViewId);
                });

                List<ViewColumnDefinition> viewColumnDefinitionList = viewToInsert.columnList;
                for (ViewColumnDefinition viewColumnDefinition : viewColumnDefinitionList) {
                    UUID uuidColumn = UUID.randomUUID();
                    String randomViewColumnDefinitionId = "column_" + uuidColumn.toString();
                    viewColumnDefinition.setColumnId(randomViewColumnDefinitionId);
                    viewColumnDefinition.setDefinitionId(randomViewSQLDefinitionId);
                    viewColumnDefinition.setViewId(randomViewId);
                    if (null != viewColumnDefinition.getAggregationType()) {
                        viewColumnDefinition.setIsMetric(1);
                    }
                    if (viewColumnDefinition.getUniqueKeyFlag() == 1) {
                        uniqueColumn++;
                        viewColumnDefinition.setUniqueKeyOrder(1);
                    }
                    if (viewColumnDefinition.getColumnName().equals(vwInfo.getTimeDimensionColumnName())) {
                        timeDimensionColumnId = randomViewColumnDefinitionId;
                    }
                }
                if (uniqueColumn != 1) {
                    throw new InvalidNumberOfUniqueColumnsException("Number of unique columns is less than or more than one");
                }

                if (viewInformation.getLoadStrategy().equals(CatalogConstants.VIEWINPUT_LOAD_STRATEGY_RELATIVE) && timeDimensionColumnId == null) {
                    log.error("Load strategy is RELATIVE and View doesnot have a column with timeDimensionColumnName provided in viewInformation");
                    throw new ImproperDataException("If load strategy is relative, please provide value at timeDimensionColumnName");
                } else {
                    viewInformation.setTimeDimensionColumnName(timeDimensionColumnId);
                }

                viewInformation.setIsActive(1);


                if (null == viewAvailabilityCheck)
                    viewInformationRepository.save(viewInformation);

                viewSQLDefinitionRepository.save(viewSQLDefinition);

                if (null != druidIngestData) {
                    druidIngestDataRepository.save(druidIngestData);
                }
                log.info("viewSourceListFinal -- " + viewSourceListFinal.size());
                //if(!viewInformation.getLayerName().contains("Layer1")){
                viewSourceRepository.save(viewSourceListFinal);
                //}

                if (viewInformation.getViewType().equals("fact") || viewInformation.getViewType().equals("dim")) {
                    viewHDFSSourceRepository.save(viewHDFSSourceList);
                }

                viewColumnRepository.save(viewColumnDefinitionList);
            }
            return INSERTED;
        } catch (ViewAlreadyExistException viewAlreadyExistException) {
            log.error(viewAlreadyExistException.getMessage());
            throw new ViewAlreadyExistException();
        } catch (InvalidNumberOfUniqueColumnsException invalidNumberOfUniqueColumnsException) {
            log.error(invalidNumberOfUniqueColumnsException.getMessage());
            throw new InvalidNumberOfUniqueColumnsException();
        }
    }

    protected ViewGroup validateViewGroup(String viewGroupName) throws ViewGroupNotAvailableException {
        ViewGroup viewGroup = viewGroupRepository.findByName(viewGroupName);
        if (viewGroup == null) {
            throw new ViewGroupNotAvailableException(String.format("Invalid view group:%s", viewGroupName));
        }
        return viewGroup;
    }

    private List<String> hasSourceAliasExists(String viewID, String sourceName, String alias) throws ViewAlreadyExistException {

        return viewRepository.findAllAliasesForViewNameSourceName(viewID, sourceName, alias);
    }

    @PostMapping(value = "/view-group")
    @Timed
    public Response insertViewGroup(@Valid @RequestBody ViewGroup viewGroup) throws ViewGroupConstraintsException, ViewGroupException, InvalidExecutionPropertiesException {
        ViewGroup savedEntity = viewGroupService.addViewGroup(viewGroup);
        Map<String, String> params = new HashMap<>();
        params.put("groupId", savedEntity.getId());
        params.put("groupName", savedEntity.getName());
        return new Response("Successfully created", params);
    }

    @PostMapping(value = "/client")
    @Timed
    public String insertClient(@Valid @RequestBody ClientToInsert clientToInsert) {
        log.info(String.format("client insertion with %s", clientToInsert.toString()));
        try {
            String clientId = null;
            clientId = clientInformationRepository.findClientIdByClientNameAndAccountType(clientToInsert.getClientName(), CLIENT);
            List<ClientInformation> clientInformationList = new ArrayList<>();
            UUID uuidClient = UUID.randomUUID();
            String accountParentInfoId = clientId;

            if (null == clientId) {
                String randomClientId = "client_" + uuidClient.toString();
                accountParentInfoId = randomClientId;
                ClientInformation clientInformation = new ClientInformation();
                clientInformation.setInfoId(randomClientId);
                clientInformation.setInfoName(clientToInsert.getClientName());
                clientInformation.setInfoDisplayName(clientToInsert.getClientName());
                clientInformation.setParentInfoId(null);
                clientInformation.setInfoType(CLIENT);
                clientInformation.setLastNodeFlag(0);
                clientInformation.setTimezone(clientToInsert.getTimezone());
                clientInformationList.add(clientInformation);
            }

            for (Child child : clientToInsert.child) {
                ClientInformation clientInformation1 = new ClientInformation();
                uuidClient = UUID.randomUUID();
                String randomClientId = "client_info_" + uuidClient.toString();
                String hdfsParentInfoId = randomClientId;
                clientInformation1.setInfoId(randomClientId);
                clientInformation1.setInfoType(ACCOUNT);
                clientInformation1.setLastNodeFlag(1);
                clientInformation1.setParentInfoId(accountParentInfoId);
                clientInformation1.setInfoName(child.getChildName());
                clientInformation1.setInfoDisplayName(child.getChildName());
                clientInformation1.setTimezone(clientToInsert.getTimezone());
                clientInformationList.add(clientInformation1);

                for (String hdfs : child.hdfsFolder) {
                    ClientInformation clientInformation2 = new ClientInformation();
                    uuidClient = UUID.randomUUID();
                    randomClientId = "client_" + uuidClient.toString();
                    clientInformation2.setInfoId(randomClientId);
                    clientInformation2.setInfoType(HDFS_FOLDER);
                    clientInformation2.setLastNodeFlag(2);
                    clientInformation2.setParentInfoId(hdfsParentInfoId);
                    clientInformation2.setInfoName(hdfs);
                    clientInformation2.setInfoDisplayName(hdfs);
                    clientInformation2.setTimezone(clientToInsert.getTimezone());
                    clientInformationList.add(clientInformation2);
                }
            }
            clientInformationRepository.save(clientInformationList);
            return INSERTED;
        } catch (DataIntegrityViolationException dataIntegrityViolationException) {
            log.error("Failed to insert client-account mapping", dataIntegrityViolationException);
            throw new ClientAlreadyExistException(String.format("Mapping already exists for the client:%s", clientToInsert.getClientName()));
        }

    }

    @Transactional
    @PostMapping(value = "/clientviewmapping")
    @Timed
    public String insertClientViewMapping(@Valid @RequestBody ClientViewMapping clientViewMapping) throws InvalidExecutionPropertiesException {
        try {
            log.info("here inserting client view mapping - " + clientViewMapping.toString());
            String finalViewIdToCheck = null;
            String clientAvailabilityCheck = null;
            String viewAvailabilityCheck = null;
            finalViewIdToCheck = viewOutputGranRepository.findFinalViewIdByViewNameAndClientName(clientViewMapping.getViewName(), clientViewMapping.getClientName());
            if (null != finalViewIdToCheck) {
                throw new ClientViewMappingAlreadyExistException("Mapping for client " + clientViewMapping.getClientName() +
                        " and view " + clientViewMapping.getViewName() + " already exist");
            }

            clientAvailabilityCheck = clientInformationRepository.findClientIdByClientNameAndAccountType(clientViewMapping.getClientName(), ACCOUNT);
            if (null == clientAvailabilityCheck) {
                throw new ClientNotAvailableException("Mapping is not possible as client with name " + clientViewMapping.getClientName() + " doesn't exist.");
            }

            viewAvailabilityCheck = viewRepository.findViewIdByViewName(clientViewMapping.getViewName());
            if (null == viewAvailabilityCheck) {
                throw new ViewNotAvailableException("Mapping is not possible as view with name " + clientViewMapping.getViewName() + " doesn't exist.");
            }

            validator.validateExecutionProperties(clientViewMapping.getExecutionProperties());

            String cronString = clientViewMapping.getCronExpression();
            String jobStartTime=clientViewMapping.getJobStartTime();
            String jobEndTime=clientViewMapping.getJobEndTime();
            List<ViewOutputGranularity> viewOutputGranularityList = new ArrayList<>();
            UUID uuidViewOutputGranularity = UUID.randomUUID();
            String randomViewOutputGranularityId = "vog_" + uuidViewOutputGranularity.toString();
            ViewOutputGranularity viewOutputGranularity = new ViewOutputGranularity();
            viewOutputGranularity.setId(randomViewOutputGranularityId);
            String finalViewId = viewAvailabilityCheck;
            String clientInfoId = clientAvailabilityCheck;
            String clientHDFSFolderId = clientInformationRepository.findClientHDFSFolderIdByParentInfoIdAndAccountType(clientInfoId, HDFS_FOLDER);


            viewOutputGranularity.setViewId(finalViewId);
            viewOutputGranularity.setFinalViewId(finalViewId);
            viewOutputGranularity.setClientInfoId(clientInfoId);
            viewOutputGranularity.setCronExpression(cronString);
            viewOutputGranularity.setClientHdfsFolderId(clientHDFSFolderId);
            viewOutputGranularity.setJobStartTime(jobStartTime);
            viewOutputGranularity.setJobEndTime(jobEndTime);
            setVOGformClientVIewMappings(viewOutputGranularity, clientViewMapping);
            Map<String, String> customParams = clientViewMapping.getCustomParams();
            viewOutputGranularity.setCustomParams(customParams);
            viewOutputGranularityList.add(viewOutputGranularity);


            log.info("alias size -- " + clientViewMapping.alias.size());
            for (Alias alias : clientViewMapping.alias) {
                ViewOutputGranularity viewOutputGranularity1 = new ViewOutputGranularity();
                uuidViewOutputGranularity = UUID.randomUUID();
                randomViewOutputGranularityId = "vog_" + uuidViewOutputGranularity.toString();
                viewOutputGranularity1.setId(randomViewOutputGranularityId);
                String viewId = viewRepository.findViewIdByViewName(alias.getViewName());
                String clientHDFSFolderIdAlias = clientInformationRepository.findClientIdByClientNameAndAccountTypeAndParentAccount(alias.getAliasName(), HDFS_FOLDER, clientInfoId);
                viewOutputGranularity1.setViewId(viewId);
                viewOutputGranularity1.setFinalViewId(finalViewId);
                viewOutputGranularity1.setClientInfoId(clientInfoId);
                viewOutputGranularity1.setCronExpression(cronString);
                viewOutputGranularity1.setClientHdfsFolderId(clientHDFSFolderIdAlias);
                viewOutputGranularity1.setCustomParams(customParams);
                setVOGformClientVIewMappings(viewOutputGranularity,clientViewMapping);
                viewOutputGranularityList.add(viewOutputGranularity1);
            }

            List<Object[]> viewSourceList = viewOutputGranRepository.findSourceListForClientViewMappingByFinalViewId(finalViewId);
            log.info("size of source -" + viewSourceList.size());
            List<String> viewSourceErrorList = new ArrayList<>();
            for (Object[] viewSource : viewSourceList) {
                String source_view_id = viewSource[0].toString();
                int granularity = getGranularityInMins(clientViewMapping.getGranularity());
                int reference = Integer.valueOf(clientViewMapping.getReference());
                int data_range_end = (int) viewSource[2];

                if (granularity + reference + data_range_end > 0) {
                    viewSourceErrorList.add(source_view_id);
                } else if (viewSourceErrorList.isEmpty()) {
                    ViewOutputGranularity viewOutputGranularity2 = new ViewOutputGranularity();
                    uuidViewOutputGranularity = UUID.randomUUID();
                    randomViewOutputGranularityId = "vog_" + uuidViewOutputGranularity.toString();
                    viewOutputGranularity2.setId(randomViewOutputGranularityId);

                    viewOutputGranularity2.setViewId(viewSource[1].toString());
                    viewOutputGranularity2.setFinalViewId(finalViewId);
                    viewOutputGranularity2.setClientInfoId(clientInfoId);
                    viewOutputGranularity2.setCronExpression(cronString);
                    viewOutputGranularity2.setClientHdfsFolderId(clientHDFSFolderId);
                    setVOGformClientVIewMappings(viewOutputGranularity2, clientViewMapping);
                    viewOutputGranularity2.setGranularityInMin(granularity);
                    viewOutputGranularity2.setCustomParams(customParams);

                    viewOutputGranularityList.add(viewOutputGranularity2);                                        
                }
            }
            if (!viewSourceErrorList.isEmpty()) {
                log.error(FUTURE_DATED_EFF_DATE_END_TIME + "{}", viewSourceErrorList);
                throw new PreConditionFailedException(FUTURE_DATED_EFF_DATE_END_TIME + viewSourceErrorList);
            }
            log.info("final size -- " + viewOutputGranularityList.size());
            viewOutputGranRepository.save(viewOutputGranularityList);
            return INSERTED;
        } catch (ClientViewMappingAlreadyExistException clientViewMappingAlreadyExistException) {
            log.error(clientViewMappingAlreadyExistException.getMessage());
            throw new ClientViewMappingAlreadyExistException();
        } catch (ClientNotAvailableException clientNotAvailableException) {
            log.error(clientNotAvailableException.getMessage());
            throw new ClientNotAvailableException();
        } catch (ViewNotAvailableException viewNotAvailableException) {
            log.error(viewNotAvailableException.getMessage());
            throw new ViewNotAvailableException();
        }
    }


    public void setVOGformClientVIewMappings(ViewOutputGranularity viewOutputGranularity, ClientViewMapping clientViewMapping) {

        viewOutputGranularity.setGranularityInMin(getGranularityInMins(clientViewMapping.getGranularity()));
        viewOutputGranularity.setGranularity(clientViewMapping.getGranularity());
        viewOutputGranularity.setReferenceTimeMin(Integer.parseInt(clientViewMapping.getReference()));
        viewOutputGranularity.setWeekendEnabled(Integer.parseInt(clientViewMapping.getWeekendEnabled()));
        viewOutputGranularity.setOutputPath(clientViewMapping.getOutputPath());
        viewOutputGranularity.setOwner(clientViewMapping.getOwner());
        viewOutputGranularity.setOwnerEmailId(clientViewMapping.getOwner_email_id());
        viewOutputGranularity.setSlaBreachTime(clientViewMapping.getSla_breach_time());
        viewOutputGranularity.setExecutionProperties(clientViewMapping.getExecutionProperties());
        viewOutputGranularity.setJobExecutionExpression(clientViewMapping.getJobExecutionExpression());
        viewOutputGranularity.setLoadStrategy(clientViewMapping.getLoadStrategy());
    }

    @Transactional
    @PostMapping(value = "/{view}/{dataset}")
    @Timed
    public String insertRules(@PathVariable(value = "view") String viewName, @PathVariable(value = "dataset") String datasetName, @Valid @RequestBody List<Rules> rulesList) {
        try {
            String viewId = viewRepository.findViewIdByViewName(viewName);
            if (viewId == null) {
                throw new ViewNotAvailableException("View with name " + viewName + " doesn't exist.");
            }
            Dataset dataset = datasetRepository.findByViewIdAndDatasetName(viewId, datasetName);
            if (dataset == null) {
                throw new DatasetNotAvailableException("datset with name " + datasetName + " doesn't exist for view " + viewName);
            }
            String datasetId = dataset.getDatasetId();
            rulesList.parallelStream().forEach(rules -> {
                        Rules ruleCheck = rulesRepository.findByDatasetIdAndRuleName(datasetId, rules.getRuleName());
                        if (ruleCheck != null) {
                            throw new RuleAlreadyExistException("rule " + rules.getRuleName() + " for view " + viewName + " already exist!");
                        }
                        UUID uuidRule = UUID.randomUUID();
                        String randomRuleId = "rule_" + uuidRule.toString();
                        rules.setRuleId(randomRuleId);
                        rules.setDatasetId(datasetId);
                        log.info("rules " + rules.toString());
                    }
            );
            log.info("size " + rulesList.size());
            rulesRepository.save(rulesList);
            return "inserted rules for dataset " + datasetName + " associated with view " + viewName;
        } catch (ViewNotAvailableException viewNotAvailableException) {
            log.error(viewNotAvailableException.getMessage());
            throw new ViewNotAvailableException();
        } catch (DatasetNotAvailableException datasetNotAvailableException) {
            log.error(datasetNotAvailableException.getMessage());
            throw new DatasetNotAvailableException();
        } catch (RuleAlreadyExistException ruleAlreadyExistException) {
            log.error(ruleAlreadyExistException.getMessage());
            throw new RuleAlreadyExistException();
        }
    }

    @Transactional
    @PostMapping(value = "/{view}")
    @Timed
    public String insertDatasetWithRules(@PathVariable(value = "view") String viewName, @Valid @RequestBody List<ViewDataset> viewDatasetList) {
        try {
            String viewId = viewRepository.findViewIdByViewName(viewName);
            if (viewId == null) {
                throw new ViewNotAvailableException("View with name " + viewName + " doesn't exist.");
            }
            List<Dataset> datasetList = new ArrayList<>();
            List<Rules> rulesList = new ArrayList<>();
            viewDatasetList.parallelStream().forEach(viewDataset -> {
                viewDataset.getDqDatasetList().parallelStream().forEach(dqDataset -> {
                    Dataset datasetCheck = datasetRepository.findByViewIdAndDatasetName(viewId, dqDataset.getDatasetName());
                    if (datasetCheck != null) {
                        throw new DatasetAlreadyExistException("datset with name " + dqDataset.getDatasetName() + " already exist for view " + viewName);
                    }
                    Dataset dataset = new Dataset();
                    UUID uuidDataset = UUID.randomUUID();
                    String datasetId = "dataset_" + uuidDataset.toString();
                    dataset.setDatasetId(datasetId);
                    dataset.setDatasetName(dqDataset.getDatasetName());
                    dataset.setDescription(dqDataset.getDatasetDescription());
                    dataset.setLevel(dqDataset.getLevel());
                    dataset.setDatasetSql(dqDataset.getDatasetSql());
                    dataset.setRecordIdentifier(dqDataset.getRecordIdentifier());
                    dataset.setViewId(viewId);
                    log.info("dataset - " + dataset.toString());
                    datasetList.add(dataset);
                    dqDataset.getRuleList().parallelStream().forEach(rules -> {
                        UUID uuidRule = UUID.randomUUID();
                        String randomRuleId = "rule_" + uuidRule.toString();
                        rules.setRuleId(randomRuleId);
                        rules.setDatasetId(datasetId);
                        log.info("rules - " + rules.toString());
                        rulesList.add(rules);
                    });
                });
            });
            log.info("datasetList size - " + datasetList.size() + " -- " + rulesList.size());
            datasetRepository.save(datasetList);
            rulesRepository.save(rulesList);
            return "inserted dataset along with rules for the view " + viewName;
        } catch (ViewNotAvailableException viewNotAvailableException) {
            log.error(viewNotAvailableException.getMessage());
            throw new ViewNotAvailableException();
        } catch (DatasetAlreadyExistException datasetAlreadyExistException) {
            log.error(datasetAlreadyExistException.getMessage());
            throw new DatasetAlreadyExistException();
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw new RuntimeException();
        }
    }

    @Transactional
    @PostMapping(value = "/exporters")
    public String insertExporters(@Valid @RequestBody List<Exporter> exporters) {
        exporterRepository.save(exporters);
        return "inserted";
    }

    @Transactional
    @PostMapping(value = "/{exporter}/config")
    public String insertExporterConfigurations(@PathVariable(value = "exporter") String exporterName, @Valid @RequestBody List<ExporterConfig> exporterConfigList) {
        try {
            String exporterId = exporterRepository.findExporterIdByExporterName(exporterName);
            if (null == exporterId) {
                throw new ExporterNotAvailableException("Exporter is not available. Please insert the exporter first.");
            }

            exporterConfigList.parallelStream().forEach(exporterConfig -> {
                exporterConfig.setExporterId(exporterId);
            });
            log.info("exporterConfigList" + exporterConfigList.toString());
            exporterConfigRepository.save(exporterConfigList);
        } catch (ExporterNotAvailableException enae) {
            log.error(enae.getMessage());
            throw new ExporterNotAvailableException();
        }
        return "inserted";
    }

    @Transactional
    @PostMapping(value = "client/{client-name}/view/{view-name}/exporter-mapping")
    public String insertExporterMapping(@PathVariable("client-name") String clientName, @PathVariable("view-name") String viewName, @Valid @RequestBody List<String> exporterList) {
        log.info(String.format("Mapping exporters:%s to client:%s and view:%s map", exporterList.toString(), clientName, viewName));
        String clientId = clientInformationRepository.findClientIdByClientNameAndAccountType(clientName, ACCOUNT);
        if (null == clientId) {
            throw new ClientNotAvailableException();
        }
        String viewId = viewRepository.findViewIdByViewName(viewName);
        if (null == viewId) {
            throw new ViewNotAvailableException();
        }
        Set<String> exporters = new HashSet<>(exporterMappingRepository.findByClientIdAndViewId(clientId, viewId));
        Set<String> exporterSet = new HashSet<>(exporterList);
        exporterSet.removeAll(exporters);
        if (exporterSet.isEmpty())
            return "mapping exists";
        List<ExporterMapping> exporterMappings = new ArrayList<>();
        exporterSet.parallelStream().forEach(exporter -> {
            String exporterId = exporterRepository.findExporterIdByExporterName(exporter);
            if (null == exporterId) {
                throw new ExporterNotAvailableException(exporter + "exporter is not available.");
            }
            String exporterMappingId = "expMapping_" + UUID.randomUUID();
            ExporterMapping exporterMapping = new ExporterMapping();
            exporterMapping.setExporterMappingId(exporterMappingId);
            exporterMapping.setClientId(clientId);
            exporterMapping.setViewId(viewId);
            exporterMapping.setExporterId(exporterId);
            exporterMapping.setIsActive(1);
            exporterMappings.add(exporterMapping);
        });
        exporterMappingRepository.save(exporterMappings);
        return "inserted";
    }

    @Transactional
    @PostMapping(value = "/client/{client-name}/view/{view-name}/exporter/{exporter-name}/config/overrides")
    public String insertOverridedConfigurations(@PathVariable("client-name") String clientName, @PathVariable("view-name") String viewName, @PathVariable("exporter-name") String exporterName, @Valid @RequestBody List<ExporterOverrides> exporterOverridesList) {
        String clientId = clientInformationRepository.findClientIdByClientNameAndAccountType(clientName, ACCOUNT);
        if (null == clientId) {
            throw new ClientNotAvailableException();
        }
        String viewId = viewRepository.findViewIdByViewName(viewName);
        if (null == viewId) {
            throw new ViewNotAvailableException();
        }
        String exporterId = exporterRepository.findExporterIdByExporterName(exporterName);
        if (null == exporterId) {
            throw new ExporterNotAvailableException();
        }
        ExporterMapping exporterMapping = exporterMappingRepository.findByClientIdAndViewIdAndExporterId(clientId, viewId, exporterId);
        if (null == exporterMapping) {
            throw new ExporterMappingNotAvailableException();
        }
        exporterOverridesList.parallelStream().forEach(exporterOverrides -> {
            exporterOverrides.setExporterMappingId(exporterMapping.getExporterMappingId());
        });

        exporterOverrridesRepository.save(exporterOverridesList);
        return "inserted";
    }

    @Transactional
    @PutMapping(value = "/view/{viewname}/dq-enabled/{status}")
    @Timed
    public String updateDqEnabled(@PathVariable(value = "viewname") String viewName, @PathVariable(value = "status") Boolean status) {
        try {
            if (!isActiveView(viewName)) {
                throw new IsNotActiveViewException(viewName + NOT_ACTIVE_VIEW);
            }
            log.info("updating dqEnabled value to " + status + " for view" + viewName);
            int dqEnabled = BooleanUtils.toInteger(status);
            viewRepository.updateDqEnabledValueForView(viewName, dqEnabled);
            return "updated dqEnabled value for " + viewName;
        } catch (IsNotActiveViewException ex) {
            log.error(ex.getMessage());
            throw new IsNotActiveViewException();
        }
    }

    @Transactional
    @PutMapping(value = "/view/{viewname}/materialization-enabled/{status}")
    @Timed
    public String updateMaterializationEnabled(@PathVariable(value = "viewname") String viewName, @PathVariable(value = "status") Boolean status) {
        try {
            if (!isActiveView(viewName)) {
                throw new IsNotActiveViewException(viewName + NOT_ACTIVE_VIEW);
            }
            log.info("updating materializationEnabled value to " + status + " for view" + viewName);
            int materializationEnabled = BooleanUtils.toInteger(status);
            viewRepository.updateMaterializationEnabledValueForView(viewName, materializationEnabled);
            return "updated materializationEnabled value for " + viewName;
        } catch (IsNotActiveViewException ex) {
            log.error(ex.getMessage());
            throw new IsNotActiveViewException();
        }
    }

    @Transactional
    @PutMapping(value = "/view/{viewname}/active/{status}")
    @Timed
    public String updateViewActivation(@PathVariable(value = "viewname") String viewName, @PathVariable(value = "status") Boolean status) {
        String viewId = viewRepository.findViewIdByViewName(viewName);
        if (null == viewId) {
            throw new ViewNotAvailableException();
        }
        log.info("updating isActive value to " + status + " for view" + viewName);
        int isActive = BooleanUtils.toInteger(status);
        viewRepository.updateIsActiveValueForView(viewName, isActive);
        viewOutputGranRepository.updateIsActiveValueForJobWithFinalView(viewId, isActive);
        return "updated isActive value for " + viewName + " to " + isActive;
    }

    @Transactional
    @PutMapping(value = "/client/{clientname}/active/{status}")
    @Timed
    public String updateClientActivation(@PathVariable(value = "clientname") String clientName, @PathVariable(value = "status") Boolean status) {
        String clientId = clientInformationRepository.findClientIdByClientNameAndAccountType(clientName, ACCOUNT);
        if (null == clientId) {
            throw new ClientNotAvailableException();
        }
        log.info("updating isActive value to " + status + " for client" + clientName);
        int isActive = BooleanUtils.toInteger(status);
        viewOutputGranRepository.updateIsActiveValueForJobWithClient(clientId, isActive);
        return "updated isActive value of all job for client " + clientName + " to " + isActive;
    }

    @Transactional
    @PutMapping(value = "/job/client/{clientname}/view/{viewname}/active/{status}")
    @Timed
    public String updateJobActivation(@PathVariable(value = "clientname") String clientName, @PathVariable(value = "viewname") String viewName, @PathVariable(value = "status") Boolean status) {
        String clientId = clientInformationRepository.findClientIdByClientNameAndAccountType(clientName, ACCOUNT);
        if (null == clientId) {
            throw new ClientNotAvailableException();
        }
        String viewId = viewRepository.findViewIdByViewName(viewName);
        if (null == viewId) {
            throw new ViewNotAvailableException();
        }
        log.info("updating isActive value to " + status + " for job with client" + clientName + " and view " + viewName);
        int isActive = BooleanUtils.toInteger(status);
        viewOutputGranRepository.updateIsActiveValueForJobWithClientAndFinalView(clientId, viewId, isActive);
        return "updated isActive value of all job for client " + clientName + " and view " + viewName + " to " + isActive;
    }

    @Transactional
    @DeleteMapping(value = "/{viewname}/{dataset}/{rules}")
    @Timed
    public String deleteRules(@PathVariable(value = "viewname") String viewName, @PathVariable(value = "dataset") String datasetName, @PathVariable(value = "rules") List<String> rulesList) {
        try {
            String viewId = viewRepository.findViewIdByViewName(viewName);
            if (viewId == null) {
                throw new ViewNotAvailableException("View with name " + viewName + " does not exist");
            }
            Dataset dataset = datasetRepository.findByViewIdAndDatasetName(viewId, datasetName);
            if (dataset == null) {
                throw new DatasetNotAvailableException("dataset " + datasetName + " for view " + viewName + " does not exist");
            }
            List<Rules> ruleList = new ArrayList<>();
            rulesList.parallelStream().forEach(ruleName -> {
                Rules rule = rulesRepository.findByDatasetIdAndRuleName(dataset.getDatasetId(), ruleName);
                if (rule == null) {
                    throw new RuleNotAvailableException("Rule with name " + ruleName + " does not exist for dataset " + datasetName);
                }
                ruleList.add(rule);
            });
            return deleteFromRules(ruleList);
        } catch (ViewNotAvailableException viewNotAvailableException) {
            log.error(viewNotAvailableException.getMessage());
            throw new ViewNotAvailableException();
        } catch (DatasetNotAvailableException datasetNotAvailableException) {
            log.error(datasetNotAvailableException.getMessage());
            throw new DatasetNotAvailableException();
        } catch (RuleNotAvailableException ruleNotAvailableException) {
            log.error(ruleNotAvailableException.getMessage());
            throw new RuleNotAvailableException();
        }
    }

    @Transactional
    @DeleteMapping(value = "/{viewname}/{dataset}")
    @Timed
    public String deleteDataset(@PathVariable(value = "viewname") String viewName, @PathVariable(value = "dataset") List<String> datasetNameList) {
        try {
            String viewId = viewRepository.findViewIdByViewName(viewName);
            if (viewId == null) {
                throw new ViewNotAvailableException("View with name " + viewName + " does not exist");
            }
            List<Dataset> datasetList = new ArrayList<>();
            datasetNameList.parallelStream().forEach(datasetName -> {
                Dataset dataset = datasetRepository.findByViewIdAndDatasetName(viewId, datasetName);
                if (dataset == null) {
                    throw new DatasetNotAvailableException("dataset with name " + datasetName + " does not exist for view " + viewName);
                }
                datasetList.add(dataset);
            });
            return deleteFromDataset(datasetList);
        } catch (ViewNotAvailableException viewNotAvailableException) {
            log.error(viewNotAvailableException.getMessage());
            throw new ViewNotAvailableException();
        } catch (DatasetNotAvailableException datasetNotAvailableException) {
            log.error(datasetNotAvailableException.getMessage());
            throw new DatasetNotAvailableException();
        }
    }

    @Transactional
    public String deleteFromDataset(List<Dataset> datasetList) {
        try {
            List<Rules> finalRuleList = new ArrayList<>();
            datasetList.parallelStream().forEach(dataset -> {
                List<Rules> ruleList = rulesRepository.findByDatasetId(dataset.getDatasetId());
                finalRuleList.addAll(ruleList);
            });
            datasetRepository.delete(datasetList);
            return deleteFromRules(finalRuleList);
        } catch (Exception exception) {
            log.error(exception.getMessage());
            throw new RuntimeException(exception);
        }
    }

    @Transactional
    public String deleteFromRules(List<Rules> ruleList) {
        try {
            rulesRepository.delete(ruleList);
            return "deleted";
        } catch (Exception exception) {
            log.error(exception.getMessage());
            throw new RuntimeException(exception);
        }
    }

    @Transactional
    @DeleteMapping(value = "/views")
    public String deleteViews(@RequestBody List<String> viewList) {
        List<String> viewIdList = new ArrayList<>();
        viewList.parallelStream().forEach(view -> {
            String viewId = viewRepository.findViewIdByViewName(view);
            if (null == viewId) {
                throw new ViewNotAvailableException();
            }
            viewIdList.add(viewId);
        });

        viewIdList.parallelStream().forEach(viewId -> {
            List<String> dependentViews = viewSourceRepository.findFinalViewsWithSourceViews(viewId);
            if (!dependentViews.isEmpty()) {
                if (!viewList.containsAll(dependentViews)) {
                    String viewName = viewRepository.findViewNameByViewId(viewId);
                    throw new InvalidActivityException(viewName + " is dependency for " + dependentViews.toString());
                }
            }
        });
        viewColumnRepository.deleteByViewIds(viewIdList);
        viewSourceRepository.deleteByFinalViewIds(viewIdList);
        viewSQLDefinitionRepository.deleteByViewIds(viewIdList);
        viewHDFSSourceRepository.deleteByViewIds(viewIdList);
        druidIngestDataRepository.deleteByViewIds(viewIdList);
        List<String> datasetIds = datasetRepository.findAllByViewIds(viewIdList);
        if (!datasetIds.isEmpty()) {
            rulesRepository.deleteByDatasetIds(datasetIds);
            datasetRepository.deleteByViewIds(viewIdList);
        }
        exporterMappingRepository.deleteByViewIds(viewIdList);
        viewOutputGranRepository.deleteByFinalViewIds(viewIdList);
        sourceAdapterRepository.deleteByViewIds(viewIdList);
        viewRepository.deleteByViewIds(viewIdList);
        return "deleted all the views and their corresponding data";
    }

    @Transactional
    @DeleteMapping(value = "/clients")
    public String deleteClients(@RequestBody List<String> clientList) {
        List<String> clientIdList = new ArrayList<>();
        clientList.parallelStream().forEach(client -> {
            String clientId = clientInformationRepository.findClientIdByClientNameAndAccountType(client, ACCOUNT);
            if (null == clientId) {
                throw new ClientNotAvailableException();
            }
            clientIdList.add(clientId);
        });
        exporterMappingRepository.deleteByClientIds(clientIdList);
        viewOutputGranRepository.deleteByClientIds(clientIdList);
        clientInformationRepository.deleteByClientIds(clientIdList);
        return "deleted all the clients and their corresponding data";
    }

    private Integer getGranularityInMins(String granularity) {
        Map<String, Integer> granularityMapping = new HashMap<>();
        granularityMapping.put("15Minutes", 15);
        granularityMapping.put("30Minutes", 30);
        granularityMapping.put("Hourly", 60);
        granularityMapping.put("Daily", 1440);
        granularityMapping.put("Weekly", 10080);
        int granularityInMin = granularityMapping.get(granularity);
        return granularityInMin;
    }

    private String listToString(List<String> stringList) {
        String convertedString = "";
        for (String str : stringList) {
            convertedString = convertedString + str + ",";
        }
        if (convertedString.endsWith(",")) {
            convertedString = convertedString.substring(0, convertedString.length() - 1);
        }
        return convertedString.trim();
    }

    public long getTime(long scheduleTime, long reference) {
        long timestamp;

        Instant instant = Instant.ofEpochSecond(scheduleTime / 1000);
        ZonedDateTime zonedDateTimeUTC = ZonedDateTime.ofInstant(instant, ZoneOffset.UTC);
        zonedDateTimeUTC = zonedDateTimeUTC.plusMinutes(reference);
        timestamp = zonedDateTimeUTC.toEpochSecond() * 1000;
        log.info("timestamp -- " + timestamp);
        return timestamp;
    }

    private String getPath(String corePath, long time) {

        Instant instant = Instant.ofEpochSecond(time / 1000);
        ZonedDateTime zonedDateTimeUTC = ZonedDateTime.ofInstant(instant, ZoneOffset.UTC);

        int year = zonedDateTimeUTC.getYear();
        int month = zonedDateTimeUTC.getMonthValue();
        int date = zonedDateTimeUTC.getDayOfMonth();
        int hour = zonedDateTimeUTC.getHour();
        return corePath + "/year=" + year + "/month=" + month + "/day=" + date + "/hour=" + hour + "/min=00";
    }

    public boolean isActiveView(String viewName) {
        boolean isActive = true;
        try {
            viewName = viewName.split("__").length > 1 ? viewName.split("__")[1] : viewName;
            View view = viewRepository.findByViewName(viewName);
            if (view == null || view.getIsActive() == 0) {
                isActive = false;
            }
        } catch (Exception e) {
            log.info(e.getMessage());
        }
        return isActive;
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Bad Request")
    public class BadRequestException extends RuntimeException {
    }

    @ResponseStatus(value = HttpStatus.EXPECTATION_FAILED, reason = "Ooops, something went wrong!!! Please try again")
    public class SomethingWentWrongException extends RuntimeException {
    }

}

enum ViewType {
    fact, dim;

    public static List<String> getValues() {
        List<String> vals = new ArrayList<>();
        for (ViewType type : values()) {
            vals.add(type.toString());
        }
        return vals;
    }
};

