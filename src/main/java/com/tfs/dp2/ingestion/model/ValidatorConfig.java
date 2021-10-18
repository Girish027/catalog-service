package com.tfs.dp2.ingestion.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@ToString
public class ValidatorConfig implements Serializable {

    @NotNull
    @NotEmpty
    @JsonProperty("validator_class")
    private String validatorClass;

    @JsonProperty("validator_rules")
    private List<String> validatorRules = new ArrayList<String>();
}
