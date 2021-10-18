package com.tfs.dp2.catalog.view;

import lombok.Data;
import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by bikesh.singh on 27-02-2018.
 */
@Data
public class DedupView implements Serializable {
    private String viewId;
    private String viewName;
    private List<String> uniqueColumnPathList = new ArrayList<>();
}
