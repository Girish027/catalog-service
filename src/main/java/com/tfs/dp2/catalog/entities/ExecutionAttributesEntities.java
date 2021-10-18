package com.tfs.dp2.catalog.entities;

import java.util.Optional;
import java.util.Set;

public interface ExecutionAttributesEntities {

    Optional<String> getProperty(ExecutionAttributes val);
    Set<String> executionKeys();

}
