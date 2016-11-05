package com.artel.poc.indexer.service.bean;

/**
 * Created by dimapod on 05/11/16.
 */
public class TripJson {

    private String json;
    private long id;

    public TripJson(long id, String json) {
        this.json = json;
        this.id = id;
    }

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "TripJson{" +
                "id='" + id + '\'' +
                ", json=" + json +
                '}';
    }
}
