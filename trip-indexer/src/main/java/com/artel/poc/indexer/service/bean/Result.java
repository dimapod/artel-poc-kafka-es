package com.artel.poc.indexer.service.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown=true)
public class Result {

    @JsonProperty("colName")
    private String colName;

    @JsonProperty("value")
    private String value;

    public String getColName() {
        return colName;
    }

    public void setColName(String colName) {
        this.colName = colName;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Result{" +
                "colName='" + colName + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
