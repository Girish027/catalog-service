package com.tfs.dp2.catalog.cob.validator;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public abstract class AbstractExecutionPropertyValidator implements ExecutionPropertyValidator{

    public abstract String validate(Map<String,String> execProps);

    public boolean isValidMemoryspecifictaion(String memory)
    {
        //size unit suffix allowed  : "k", "m", "g" or "t"
        String pattern = "^(\\d+)[gkmt]$";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(memory);
        return m.matches();
    }
}
