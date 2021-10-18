package com.tfs.dp2.catalog.entities;

public enum ExecutionAttributes {
    QUEUE("queue"),
    NUM_EXECUTORS("num-executors"),
    EXECUTOR_CORES("executor-cores"),
    EXECUTOR_MEMORY("executor-memory"),
    DRIVER_MEMORY("driver-memory");

    private String value;

    ExecutionAttributes(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
