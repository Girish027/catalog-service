package com.tfs.dp2.catalog.exporters;

import com.tfs.dp2.catalog.viewcolumndefinition.ViewColumnDefinition;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bikesh.singh on 09-01-2019.
 */

@Data
public class ExporterDefinition {

    private String viewName;
    private String clientName;
    private List<ExportersEnabled> exporters = new ArrayList<>();
    private String outputPath;
    private String uniqueColumn;
    private String inputDataFormat;
    private String materializationEnabled;
    private String timeDimensionColumn;
    private List<ViewColumnDefinition> columnList = new ArrayList<>();
}
