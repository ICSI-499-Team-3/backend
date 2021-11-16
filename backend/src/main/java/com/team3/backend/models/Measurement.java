package com.team3.backend.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "measurements")
public class Measurement {

    @Id
    private String id;

    private String x;

    private String y;

    private double dateTimeMeasured;

    public Measurement(String id, String x, String y, double dateTimeMeasured) {
        super();
        this.id = id;
        this.x = x;
        this.y = y;
        this.dateTimeMeasured = dateTimeMeasured;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getX() {
        return x;
    }

    public void setX(String x) {
        this.x = x;
    }

    public String getY() {
        return y;
    }

    public void setY(String y) {
        this.y = y;
    }

    public double getDateTimeMeasured() {
        return dateTimeMeasured;
    }

    public void setDateTimeMeasured(double dateTimeMeasured) {
        this.dateTimeMeasured = dateTimeMeasured;
    }
}
