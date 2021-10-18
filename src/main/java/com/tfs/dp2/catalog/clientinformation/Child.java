package com.tfs.dp2.catalog.clientinformation;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by bikesh.singh on 14-03-2018.
 */

@Data
public class Child {
    @NotNull
    @NotEmpty
    @Size(max = 50, message = "clientName length should not be greater than 50")
    private String childName;

    @NotEmpty
    @Valid
    public List<String> hdfsFolder = new ArrayList<>();
}
