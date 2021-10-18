package com.tfs.dp2.catalog.entities;

import com.tfs.dp2.catalog.util.CatalogAttributeConverter;
import lombok.Data;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.*;

@Data
@Entity
@Table(name = "view_group")
@EntityListeners(AuditingEntityListener.class)
public class ViewGroup implements Serializable,ExecutionAttributesEntities {
    @Id
    private String id;

    @NotNull
    private String name;

    private String description;

    @NotNull
    private String owner;

    @NotNull
    private String product;

    @NotNull
    @Convert(converter = CatalogAttributeConverter.class)
    @Column(columnDefinition = "json", name = "execution_properties")
    Map<String, String> executionProperties = new HashMap<>();

    @Column(name = "created_unixtimestamps")
    private Long createdAt;

    @Column(name = "modified_unixtimestamps")
    private Long updatedAt;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "modified_by")
    private String modifiedBy;

    @Convert(converter = CatalogAttributeConverter.class)
    @Column(columnDefinition = "json", name = "environment_properties")
    Map<String, String> environmentProperties = new HashMap<>();


    @Override
    public Optional<String> getProperty(ExecutionAttributes val)
    {
        return Optional.ofNullable(executionProperties.get(val.getValue()));
    }

    @Override
    public Set<String> executionKeys() {
        return new HashSet<>(executionProperties.keySet());
    }
}
