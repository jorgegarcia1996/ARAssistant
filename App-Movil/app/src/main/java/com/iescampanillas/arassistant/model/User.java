package com.iescampanillas.arassistant.model;


import java.io.Serializable;

public class User implements Serializable {

    private String name;
    private String surname;
    private String email;
    private String connectID;

    public User() {
    }

    public User(String name, String surname, String email, String connectID) {
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.connectID = connectID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getConnectID() {return connectID;}

    public void setConnectID(String connectID) {this.connectID = connectID;}

    @Override
    public String toString() {
        return (this.name + " " + this.surname);
    }
}
