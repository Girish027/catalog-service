package com.tfs.dp2.catalog.view;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bikesh.singh on 27-02-2018.
 */
@Data
public class ClientViewList {
    private String clientName;
    private List<ClientViews> views = new ArrayList<>();
}
