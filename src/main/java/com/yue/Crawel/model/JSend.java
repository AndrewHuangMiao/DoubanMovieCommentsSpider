package com.yue.Crawel.model;

import java.util.Map;

/**
 * Created by Andrew on 16/5/6.
 */
public class JSend {

    private String status;

    private Map<String, Object> data;

    private String message;

    public JSend(String status, Map<String, Object> data, String message) {
        this.status = status;
        this.data = data;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }
}
