package com.tfs.dp2.catalog.dq;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bikesh.singh on 27-08-2018.
 */

@Data
public class ViewDataset {
    private List<DQDataset> dqDatasetList = new ArrayList<>();
}
