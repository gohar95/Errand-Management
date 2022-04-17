package com.fyp.errandmanagement.Models;

public class ProviderModel {
    private String Key,Name,Email,Password,Phone,Cnic,Address,Image,DateTime,latitude,longitude,fcmToken;

    public ProviderModel(String key, String name, String email, String password, String phone, String cnic, String address, String image, String dateTime, String latitude, String longitude, String fcmToken) {
        Key = key;
        Name = name;
        Email = email;
        Password = password;
        Phone = phone;
        Cnic = cnic;
        Address = address;
        Image = image;
        DateTime = dateTime;
        this.latitude = latitude;
        this.longitude = longitude;
        this.fcmToken = fcmToken;
    }

    public String getKey() {
        return Key;
    }

    public void setKey(String key) {
        Key = key;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getCnic() {
        return Cnic;
    }

    public void setCnic(String cnic) {
        Cnic = cnic;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getDateTime() {
        return DateTime;
    }

    public void setDateTime(String dateTime) {
        DateTime = dateTime;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getFcmToken() {
        return fcmToken;
    }

    public void setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }
}
