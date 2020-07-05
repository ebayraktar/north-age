package com.nage.north_age.models;

public class SliderItem {
    int productID;
    String imageUrl;

    public int getProductID() {
        return productID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public SliderItem(int productID, String imageUrl) {
        this.productID = productID;
        this.imageUrl = imageUrl;
    }

    public SliderItem() {
    }
}
