package com.tfs.dp2.catalog.view;

import lombok.Data;

import java.util.List;

/**
 * Created by bikesh.singh on 30-04-2018.
 */
@Data
public class ClientCronExpression {
    private List<ClientExecutionProperties> clientExecProps;
    private String cronExpression;
}

