package com.iescampanillas.arassistant.model;

import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;

@IgnoreExtraProperties
public class Task implements Serializable {

    private String id, title,description;

    public Task() {
    }

    public Task(String id, String title, String desc) {
        this.id = id;
        this.title = title;
        this.description = desc;
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

    @Override
    public String toString() {
        return this.title;
    }
}