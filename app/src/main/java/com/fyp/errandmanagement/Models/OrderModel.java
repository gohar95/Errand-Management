package com.fyp.errandmanagement.Models;

public class OrderModel {

    private String orderNO;
    private String DAY;
    private String DATE;
    private String TIME;
    private String INS;
    private String PRICE;
    private String ORDER;
    private String ADDR;
    private String NAME;
    private String PHONE;
    private String PDATE;
    private String ONAME;
    private String OSTATUS;
    private String ProviderID;
    private String ServiceID;
    private String CustomerID;
    private String image;
    private String servicePriceType;

    public OrderModel(String orderNO, String DAY, String DATE, String TIME, String INS, String PRICE, String ORDER, String ADDR, String NAME, String PHONE, String PDATE, String ONAME, String OSTATUS, String providerID, String serviceID, String customerID, String image, String servicePriceType) {
        this.orderNO = orderNO;
        this.DAY = DAY;
        this.DATE = DATE;
        this.TIME = TIME;
        this.INS = INS;
        this.PRICE = PRICE;
        this.ORDER = ORDER;
        this.ADDR = ADDR;
        this.NAME = NAME;
        this.PHONE = PHONE;
        this.PDATE = PDATE;
        this.ONAME = ONAME;
        this.OSTATUS = OSTATUS;
        ProviderID = providerID;
        ServiceID = serviceID;
        CustomerID = customerID;
        this.image = image;
        this.servicePriceType = servicePriceType;
    }

    public String getOrderNO() {
        return orderNO;
    }

    public void setOrderNO(String orderNO) {
        this.orderNO = orderNO;
    }

    public String getDAY() {
        return DAY;
    }

    public void setDAY(String DAY) {
        this.DAY = DAY;
    }

    public String getDATE() {
        return DATE;
    }

    public void setDATE(String DATE) {
        this.DATE = DATE;
    }

    public String getTIME() {
        return TIME;
    }

    public void setTIME(String TIME) {
        this.TIME = TIME;
    }

    public String getINS() {
        return INS;
    }

    public void setINS(String INS) {
        this.INS = INS;
    }

    public String getPRICE() {
        return PRICE;
    }

    public void setPRICE(String PRICE) {
        this.PRICE = PRICE;
    }

    public String getORDER() {
        return ORDER;
    }

    public void setORDER(String ORDER) {
        this.ORDER = ORDER;
    }

    public String getADDR() {
        return ADDR;
    }

    public void setADDR(String ADDR) {
        this.ADDR = ADDR;
    }

    public String getNAME() {
        return NAME;
    }

    public void setNAME(String NAME) {
        this.NAME = NAME;
    }

    public String getPHONE() {
        return PHONE;
    }

    public void setPHONE(String PHONE) {
        this.PHONE = PHONE;
    }

    public String getPDATE() {
        return PDATE;
    }

    public void setPDATE(String PDATE) {
        this.PDATE = PDATE;
    }

    public String getONAME() {
        return ONAME;
    }

    public void setONAME(String ONAME) {
        this.ONAME = ONAME;
    }

    public String getOSTATUS() {
        return OSTATUS;
    }

    public void setOSTATUS(String OSTATUS) {
        this.OSTATUS = OSTATUS;
    }

    public String getProviderID() {
        return ProviderID;
    }

    public void setProviderID(String providerID) {
        ProviderID = providerID;
    }

    public String getServiceID() {
        return ServiceID;
    }

    public void setServiceID(String serviceID) {
        ServiceID = serviceID;
    }

    public String getCustomerID() {
        return CustomerID;
    }

    public void setCustomerID(String customerID) {
        CustomerID = customerID;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getServicePriceType() {
        return servicePriceType;
    }

    public void setServicePriceType(String servicePriceType) {
        this.servicePriceType = servicePriceType;
    }
}
