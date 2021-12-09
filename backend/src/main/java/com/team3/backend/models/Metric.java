package com.team3.backend.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * @author Tony Comanzo (entire class)
 */
@Document(collection = "metrics")
public class Metric {

    @Id
    private String id;

    private String userId;

    private String title;

    private String xUnits;

    private String yUnits;

    private List<Measurement> data;

    public Metric(String id, String userId, String title, String xUnits, String yUnits, List<Measurement> data) {
        super();
        this.id = id;
        this.userId = userId;
        this.title = title;
        this.xUnits = xUnits;
        this.yUnits = yUnits;
        this.data = data;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getXUnits() {
        return xUnits;
    }

    public void setXUnits(String xUnits) {
        this.xUnits = xUnits;
    }

    public String getYUnits() {
        return yUnits;
    }

    public void setYUnits(String yUnits) {
        this.yUnits = yUnits;
    }

    public List<Measurement> getData() {
        return data;
    }

    public void setData(List<Measurement> data) {
        this.data = data;
    }
}
