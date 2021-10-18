package com.tfs.dp2.catalog.util;

import java.util.Map;

public class Response {
    String message;
    Map<String,String> params;

    public Response(String message, Map<String, String> params) {
        this.message = message;
        this.params = params;
    }

    public String getMessage() {
        return message;
    }

    public Map<String, String> getParams() {
        return params;
    }
}
