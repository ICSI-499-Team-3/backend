package com.team3.backend.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "logs")
public class Log {

    @Id
    private String id;

    private int datetimeOfActivity;

    private String notes;

    private List<String> categories;

    private List<String> mood;

    public Log(String id, int datetimeOfActivity, String notes, List<String> categories, List<String> mood) {
        super();
        this.id = id;
        this.datetimeOfActivity = datetimeOfActivity;
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

    public int getDateimeOfActivity() {
        return datetimeOfActivity;
    }

    public void setDateimeOfActivity(int dateimeOfActivity) {
        this.datetimeOfActivity = dateimeOfActivity;
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
