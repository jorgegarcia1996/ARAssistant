package com.iescampanillas.arassistant.model;

import java.io.Serializable;

public class Reminder implements Serializable {

    private String id, title, description;
    private long dateTime;

    public Reminder() {
    }

    public Reminder(String id, String title, long dateTime) {
        this.id = id;
        this.title = title;
        this.dateTime = dateTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getDateTime() {
        return dateTime;
    }

    public void setDate(long dateTime) {
        this.dateTime = dateTime;
    }

    @Override
    public String toString() {
        return this.title;
    }
}
