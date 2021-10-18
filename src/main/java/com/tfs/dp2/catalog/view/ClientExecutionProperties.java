package com.tfs.dp2.catalog.view;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class ClientExecutionProperties {

    @JsonProperty("name")
    private String clientName;
    private Map<String,String> executionProperties = new HashMap<>();
    private Map<String,String> environmentProperties =  new HashMap<>();
    private String jobStartTime;
    private String jobEndTime;
}
