package com.tfs.dp2.catalog.view;

import com.tfs.dp2.catalog.rule.RuleScripts;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bikesh.singh on 27-02-2018.
 */

@Data
public class ViewRuleScripts {
    private String viewName;
    private List<RuleScripts> ruleScriptsList = new ArrayList<>();
}
