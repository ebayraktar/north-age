package com.nage.north_age.models;

public class HomeButton {
    int buttonID;
    String description;
    String imageUrl;

    public int getButtonID() {
        return buttonID;
    }

    public void setButtonID(int buttonID) {
        this.buttonID = buttonID;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public HomeButton(int buttonID, String description, String imageUrl) {
        this.buttonID = buttonID;
        this.description = description;
        this.imageUrl = imageUrl;
    }

    public HomeButton() {
    }
}
