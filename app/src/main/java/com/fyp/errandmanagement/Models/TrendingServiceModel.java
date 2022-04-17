package com.fyp.errandmanagement.Models;

public class TrendingServiceModel {
    private String serviceID, providerID, serviceName, serviceDescription, serviceType, servicePrice, servicePriceType, serviceImage, dateTime, Category, Booked;

    public TrendingServiceModel(String serviceID, String providerID, String serviceName, String serviceDescription, String serviceType, String servicePrice, String servicePriceType, String serviceImage, String dateTime, String category, String booked) {
        this.serviceID = serviceID;
        this.providerID = providerID;
        this.serviceName = serviceName;
        this.serviceDescription = serviceDescription;
        this.serviceType = serviceType;
        this.servicePrice = servicePrice;
        this.servicePriceType = servicePriceType;
        this.serviceImage = serviceImage;
        this.dateTime = dateTime;
        Category = category;
        Booked = booked;
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

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getServiceDescription() {
        return serviceDescription;
    }

    public void setServiceDescription(String serviceDescription) {
        this.serviceDescription = serviceDescription;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getServicePrice() {
        return servicePrice;
    }

    public void setServicePrice(String servicePrice) {
        this.servicePrice = servicePrice;
    }

    public String getServicePriceType() {
        return servicePriceType;
    }

    public void setServicePriceType(String servicePriceType) {
        this.servicePriceType = servicePriceType;
    }

    public String getServiceImage() {
        return serviceImage;
    }

    public void setServiceImage(String serviceImage) {
        this.serviceImage = serviceImage;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public String getBooked() {
        return Booked;
    }

    public void setBooked(String booked) {
        Booked = booked;
    }
}
