package com.tfs.dp2.catalog.view;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by mukesh.kumawat on 11/14/2018.
 */
public class CatalogConstants {

    //Task Properties
    public static final String TIMEZONE_UTC = "UTC";
    public static final String VIEWINPUT_LOAD_STRATEGY_RELATIVE = "RELATIVE";
    public static final String SUCCESS = "success";
    public static final Set<String> executionPropertiesKeys = new HashSet<>(Arrays.asList("queue", "num-executors", "executor-cores", "executor-memory", "driver-memory",
            "retryCount", "retryInterval", "isPostProcessingEnabled","slaBreachTime","spark-configurations"));
}
