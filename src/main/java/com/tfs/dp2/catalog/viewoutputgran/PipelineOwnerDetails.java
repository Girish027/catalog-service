package com.tfs.dp2.catalog.viewoutputgran;

import lombok.Data;
import java.io.Serializable;

/**
 * Created by Priyanka.N on 02-11-2018.
 */

@Data
public class PipelineOwnerDetails implements  Serializable{

    private String viewName;
    private String owner;
    private  String clientName;
    private int slaBreachTime;
    private String ownerEmailId;
}

