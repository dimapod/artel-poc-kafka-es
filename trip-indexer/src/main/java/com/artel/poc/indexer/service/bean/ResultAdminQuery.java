package com.artel.poc.indexer.service.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown=true)
public class ResultAdminQuery {

    @JsonProperty("result")
    private List<List<Result>> result = new ArrayList<>();

    @JsonProperty("exception")
    private Object exception;

    public List<List<Result>> getResult() {
        return result;
    }

    public void setResult(List<List<Result>> result) {
        this.result = result;
    }

    public Object getException() {
        return exception;
    }

    public void setException(Object exception) {
        this.exception = exception;
    }

    @Override
    public String toString() {
        return "ResultAdminQuery{" +
                "result=" + result +
                ", exception=" + exception +
                '}';
    }
}
