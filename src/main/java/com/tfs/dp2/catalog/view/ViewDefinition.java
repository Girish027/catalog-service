package com.tfs.dp2.catalog.view;

import com.tfs.dp2.catalog.druidingestdata.DruidIngestData;
import com.tfs.dp2.catalog.viewcolumndefinition.ViewColumnDefinition;
import lombok.Data;

import java.util.*;

/**
 * Created by bikesh.singh on 27-02-2018.
 */
@Data
public class ViewDefinition {

    private String viewId;
    private String viewName;
    private String viewDescription;
    private String layerId;
    private String layerName;
    private String definitionName;
    private String jobExecutionExpression;
    private String sqlDefinition;
    private String ttlDays;
    private String esIndexEnabled;
    private String druidIndexEnabled;
    //	private String cronString;
    private String outputPath;
    private String timeDimensionColumn;
    private String uniqueColumn;
    private String inputDataFormat;
    private String loadStrategy;
    private Map<String, String> executionProperties = new HashMap();
    private DruidIngestData druidIngestData;
    private List<ViewColumnDefinition> columnList = new ArrayList<>();
    private List<UltimateSource> ultimateSourceList = new ArrayList<>();
    private String owner;
    private Map<String, String> customParams = new HashMap();
    private String dynamicBucketPath;
}
