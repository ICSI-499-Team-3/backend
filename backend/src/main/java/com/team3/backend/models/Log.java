package com.team3.backend.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * @author Tony Comanzo (entire class)
 */
@Document(collection = "logs")
public class Log {

    @Id
    private String id;

    private String userId;

    private double dateTimeOfActivity;

    private String notes;

    private List<String> categories;

    private List<String> mood;

    public Log(String id, String userId, double dateTimeOfActivity, String notes, List<String> categories, List<String> mood) {
        super();
        this.id = id;
        this.userId = userId;
        this.dateTimeOfActivity = dateTimeOfActivity;
        this.notes = notes;
        this.categories = categories;
        this.mood = mood;
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

    public double getDateTimeOfActivity() {
        return dateTimeOfActivity;
    }

    public void setDateTimeOfActivity(double dateTimeOfActivity) {
        this.dateTimeOfActivity = dateTimeOfActivity;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public List<String> getCategories() {
        return categories;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

    public List<String> getMood() {
        return mood;
    }

    public void setMood(List<String> mood) {
        this.mood = mood;
    }
}
