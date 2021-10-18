package com.tfs.dp2.catalog.view;

import lombok.Data;


@Data
public class ViewExporterList {

    private String viewName;
    private String viewId;
    private String scheduleStartTime;
    private String scheduleEndTime;
    private String granularity;
    private String cronString;
}
