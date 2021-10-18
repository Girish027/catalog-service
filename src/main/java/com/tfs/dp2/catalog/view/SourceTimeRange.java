package com.tfs.dp2.catalog.view;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by bikesh.singh on 27-02-2018.
 */
//@Entity
//@EntityListeners(AuditingEntityListener.class)
@Data
public class SourceTimeRange implements Serializable {
    private String hdfsSourceViewID;
    private List<String> pathList = new ArrayList<>();
    private String startTime;
    private String endTime;
}
