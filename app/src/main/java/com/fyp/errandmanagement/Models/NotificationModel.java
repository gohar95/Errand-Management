package com.fyp.errandmanagement.Models;

public class NotificationModel {
    private String notiTitle, notiBody, providerID, customerID, dateTime;

    public NotificationModel(String notiTitle, String notiBody, String providerID, String customerID, String dateTime) {
        this.notiTitle = notiTitle;
        this.notiBody = notiBody;
        this.providerID = providerID;
        this.customerID = customerID;
        this.dateTime = dateTime;
    }

    public String getNotiTitle() {
        return notiTitle;
    }

    public void setNotiTitle(String notiTitle) {
        this.notiTitle = notiTitle;
    }

    public String getNotiBody() {
        return notiBody;
    }

    public void setNotiBody(String notiBody) {
        this.notiBody = notiBody;
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

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }
}
