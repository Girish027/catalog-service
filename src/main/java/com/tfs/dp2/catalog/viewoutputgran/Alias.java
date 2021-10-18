package com.tfs.dp2.catalog.viewoutputgran;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

/**
 * Created by bikesh.singh on 15-03-2018.
 */

@Data
public class Alias {

    public Alias() {
    }

    public Alias(String aliasName, String viewName) {
        this.aliasName = aliasName;
        this.viewName = viewName;
    }

    @NotNull
    @NotEmpty
    private String aliasName;

    @NotNull
    @NotEmpty
    private String viewName;
}
