package com.tfs.dp2.catalog.dq;

import com.tfs.dp2.catalog.datasetRules.Rules;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bikesh.singh on 27-08-2018.
 */
@Data
public class DQDataset {
    private String datasetId;
    private String datasetName;
    private String datasetSql;
    private String datasetDescription;
    private String level;
    private String recordIdentifier;
    List<Rules> ruleList = new ArrayList<>();
}
