package com.tfs.dp2.catalog.exporters;

import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by bikesh.singh on 09-01-2019.
 */

@Data
public class ExportersEnabled {
    private String name;
    private String exporterClassName;
    private String exporterJarLoc;
    private Map<String, String> exporterConfig = new HashMap<>();
}
