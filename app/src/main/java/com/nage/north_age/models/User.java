package com.nage.north_age.models;

public class User {
    String userID;
    String name;
    String surname;
    String userName;
    String email;

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public User() {
    }

    public User(String userID, String name, String surname, String userName, String email) {
        this.userID = userID;
        this.name = name;
        this.surname = surname;
        this.userName = userName;
        this.email = email;
    }
}
