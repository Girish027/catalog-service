package com.tfs.dp2.catalog.viewoutputgran;

import lombok.Data;
import lombok.ToString;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by bikesh.singh on 15-03-2018.
 */

@Data
@ToString(callSuper = true, includeFieldNames = true)
public class ClientViewMapping extends DefaultClientViewMapping {
    @NotNull
    @NotEmpty
    private String viewName;

    @NotNull
    @NotEmpty
    private String clientName;

    @NotNull
    @NotEmpty
    @Valid
    public List<Alias> alias = new ArrayList<>();

    public String owner_email_id;

    private String jobExecutionExpression = "";

    private String loadStrategy;

    private Map<String, String> environmentProperties = new HashMap<>();

    private Map<String, String> customParams;

    private String jobStartTime;

    private String jobEndTime;
}
