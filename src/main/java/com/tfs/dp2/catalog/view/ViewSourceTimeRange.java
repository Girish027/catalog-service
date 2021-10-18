package com.tfs.dp2.catalog.view;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by bikesh.singh on 27-02-2018.
 */
//@Entity
//@EntityListeners(AuditingEntityListener.class)
@Data
public class ViewSourceTimeRange implements Serializable {
    private String viewName;
    private String clientName;
    private String scheduleStartTime;
    private String scheduleEndTime;
    private String jobExecutionExpression;
    private Map<String,String> executionProperties = new HashMap();
    private String timezone;
    private String granularity;
    private List<String> processorPlugInJarLoc = new ArrayList<>();
    private List<String> exporterPluginJarLocList = new ArrayList<>();
    private List<SourceTimeRange> sourceTimeRangeList = new ArrayList<>();
}
