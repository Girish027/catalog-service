package com.tfs.dp2.catalog.view;


import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bikesh.singh on 27-02-2018.
 */

@Getter
@Setter
public class OrchestratorSnapshot {
    private String viewName;
    private List<ClientCronExpression> clientCronExpressionList = new ArrayList<>();
    private String userName;
    private String jobStartTime;
    private String jobEndTime;
    private String complexity;
}
