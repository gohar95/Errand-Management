package com.fyp.errandmanagement.Models;

public class ReadReviewModel {
    private String reviewText, rating, serviceID, providerID, customerID, name, image;

    public ReadReviewModel(String reviewText, String rating, String serviceID, String providerID, String customerID, String name, String image) {
        this.reviewText = reviewText;
        this.rating = rating;
        this.serviceID = serviceID;
        this.providerID = providerID;
        this.customerID = customerID;
        this.name = name;
        this.image = image;
    }

    public String getReviewText() {
        return reviewText;
    }

    public void setReviewText(String reviewText) {
        this.reviewText = reviewText;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getServiceID() {
        return serviceID;
    }

    public void setServiceID(String serviceID) {
        this.serviceID = serviceID;
    }

    public String getProviderID() {
        return providerID;
    }

    public void setProviderID(String providerID) {
        this.providerID = providerID;
    }

    public String getCustomerID() {
        return customerID;
    }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
