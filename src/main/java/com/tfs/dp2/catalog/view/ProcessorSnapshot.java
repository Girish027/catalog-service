package com.tfs.dp2.catalog.view;

import com.tfs.dp2.catalog.viewcolumndefinition.ViewColumnDefinition;
import lombok.Data;

import java.util.*;

/**
 * Created by bikesh.singh on 27-02-2018.
 */

@Data
public class ProcessorSnapshot {
    private String viewName;
    private String path;
    private String format;
    private String referenceTime;
    private String timeDimensionColumn;
    private Boolean dqEnabled = false;
    private Boolean materializationEnabled = false;
    private String processingPlugInType;
    private String processorPlugInMainClass;
    private Boolean preferReadMaterializedData = false;
    private String sourcePluginClass;
    private String loadStrategy;
    private HashMap<String, String> sourceConfigList = new HashMap<>();
    private List<String> uniqueFields = new ArrayList<>();
    public List<Imports> importsList = new ArrayList<>();
    public List<HashMap<String, String>> sqls = new ArrayList<>();
    private List<ViewColumnDefinition> columns = new ArrayList<>();
    private Map<String, String> customParams = new HashMap();
    private String dynamicBucketPath;
}
