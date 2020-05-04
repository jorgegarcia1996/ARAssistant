package com.iescampanillas.arassistant.model;

public class Category {

    private String id, name;

    public Category() {
    }

    public Category(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Category: " + this.name;
    }
}
