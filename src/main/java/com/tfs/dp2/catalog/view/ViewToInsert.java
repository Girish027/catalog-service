package com.tfs.dp2.catalog.view;

import com.tfs.dp2.catalog.druidingestdata.DruidIngestData;
import com.tfs.dp2.catalog.sourceAdapter.SourceAdapterConfig;
import com.tfs.dp2.catalog.viewcolumndefinition.ViewColumnDefinition;
import com.tfs.dp2.catalog.viewhdfssource.ViewHDFSSource;
import com.tfs.dp2.catalog.viewoutputgran.DefaultClientViewMapping;
import com.tfs.dp2.catalog.viewsource.ViewSource;
import com.tfs.dp2.catalog.viewsqldefinition.ViewSQLDefinition;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by bikesh.singh on 10-03-2018.
 */
@Data
public class ViewToInsert {

    @NotNull
    @Valid
    public ViewInformation viewInformation;

    @Valid
    public ViewSQLDefinition viewSQLDefinition;

    @Valid
    public DruidIngestData druidIngestionData;

    @NotEmpty
    @Valid
    public List<ViewSource> sourceList = new ArrayList<>();

    @Valid
    public List<SourceAdapterConfig> sourceAdapterConfig = new ArrayList<>();

    @Valid
    public List<ViewHDFSSource> hdfsSourceList = new ArrayList<>();

    @Valid
    public List<ViewColumnDefinition> columnList = new ArrayList<>();

    @Valid
    @NotNull
    public DefaultClientViewMapping defaultClientViewMapping;

    public Map<String, String> environmentProperties = new HashMap<>();

    public Map<String, String> customParams;

}
