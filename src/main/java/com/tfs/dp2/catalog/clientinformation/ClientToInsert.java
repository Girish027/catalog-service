package com.tfs.dp2.catalog.clientinformation;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;
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
public class ClientToInsert {

    @NotNull
    @NotEmpty
    @Size(max = 50, message = "clientName length should not be greater than 50")
    private String clientName;
    
    @NotNull
    @NotBlank (message = "client timezone can not be empty")
    private String timezone;
    
    @NotEmpty
    @NotNull
    @Valid
    public List<Child> child = new ArrayList<>();
}
