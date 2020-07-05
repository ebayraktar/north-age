package com.nage.north_age.models;

public class UserLogin {
    String name;
    String surname;
    String email;
    String password;
    String deviceID;

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDeviceID() {
        return deviceID;
    }

    public void setDeviceID(String deviceID) {
        this.deviceID = deviceID;
    }

    public UserLogin() {

    }

    public UserLogin(String name, String surname, String email, String password, String deviceID) {
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.password = password;
        this.deviceID = deviceID;
    }
}
