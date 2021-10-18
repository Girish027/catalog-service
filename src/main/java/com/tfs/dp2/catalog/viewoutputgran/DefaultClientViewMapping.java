package com.tfs.dp2.catalog.viewoutputgran;

import lombok.Data;
import lombok.ToString;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by bikesh.singh on 15-03-2018.
 */

@Data
@ToString(includeFieldNames=true)
public class DefaultClientViewMapping {

    @NotNull
    @NotEmpty
    private String cronExpression;

    @NotNull
    @NotEmpty
    @Pattern(regexp = "-\\d+", message = "reference should be negative integer")
    private String reference;

    @NotNull
    @NotEmpty
    @Pattern(regexp = "(?:^|(?<= ))(Hourly|15Minutes|30Minutes|Daily|Weekly)(?:(?= )|$)",
            message = "granularity should be one of these value: \nHourly \n15Minutes \n30Minutes \nDaily \nWeekly")
    private String granularity;

    @NotNull
    @NotEmpty
    @Pattern(regexp = "[01]", message = "weekendEnabled should be 0 or 1")
    private String weekendEnabled;

    @NotNull
    @NotEmpty
    private String outputPath;

    private
    Map<String, String> executionProperties = new HashMap<>();

    public int sla_breach_time = -1;

    @NotNull
    @NotEmpty
    public String owner;

    public List<String> exporterList;

    private
    Map<String, String> customParams = new HashMap<>();

}
