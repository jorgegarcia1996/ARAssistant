package com.iescampanillas.arassistant.model;

import android.content.ContentValues;

import com.iescampanillas.arassistant.database.CategoriesContract;

public class Category {

    private String id;
    private String name;
    private String nameEN;
    private String color;
    private int icon;

    public Category(String id, String name, String nameEN, String color, int icon) {
        this.id = id;
        this.name = name;
        this.nameEN = nameEN;
        this.color = color;
        this.icon = icon;
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

    public String getNameEN() {
        return nameEN;
    }

    public void setNameEN(String nameEN) {
        this.nameEN = nameEN;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    // Method to convert a category to a ContentValues object to store the data in the DB
    public ContentValues toContentValues() {
        ContentValues values = new ContentValues();
        values.put(CategoriesContract.CategoriesEntry.CAT_ID, this.id);
        values.put(CategoriesContract.CategoriesEntry.CAT_NAME, this.name);
        values.put(CategoriesContract.CategoriesEntry.CAT_EN, this.nameEN);
        values.put(CategoriesContract.CategoriesEntry.CAT_COLOR, this.color);
        values.put(CategoriesContract.CategoriesEntry.CAT_ICON, this.icon);
        return values;
    }
}
