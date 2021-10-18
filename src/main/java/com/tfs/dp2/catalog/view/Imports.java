package com.tfs.dp2.catalog.view;

import lombok.Data;
import java.io.Serializable;

/**
 * Created by bikesh.singh on 27-02-2018.
 */
@Data
public class Imports implements Serializable{

    private String viewName;
    private String startTime;
    private String endTime;
}
