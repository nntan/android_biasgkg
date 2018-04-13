package com.luan.dms_management.models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by luan.nt on 7/27/2017.
 */

public class Customer extends RealmObject {
    @PrimaryKey
    private String CardCode;
    private String CardName;
    private String Address;
    private String ContactPerson;
    private String Tel;
    private String LatitudeValue;
    private String LongitudeValue;
    private String LatLongString;
    private String Route;
    private String Channel;
    private String CustGrp;
    private String lastVisit;
    private int SalesManID;
    private String VisitStartDate;
    private String VisitEndDate;
    private String VisitTotal;
    private String CardName_Eng;
    private short VisitStatus;
    private String Address_Eng;
    private String ContactPerson_Eng;

    public Customer() {
        this.CardCode = "";
        this.CardName = "";
        this.Address = "";
        this.ContactPerson = "";
        this.Tel = "";
        this.LatitudeValue = "";
        this.LongitudeValue = "";
        this.LatLongString = "";
        this.Route = "";
        this.Channel = "";
        this.CustGrp = "";
        this.lastVisit = "";
        this.SalesManID = 0;
        this.CardName_Eng = "";
        this.Address_Eng = "";
        this.ContactPerson_Eng = "";
        this.VisitStartDate = "";
        this.VisitEndDate = "";
        this.VisitTotal = "";
        this.VisitStatus = 0;
    }

    public Customer(Customer customer) {
        this.CardCode = customer.getCardCode();
        this.CardName = customer.getCardName();
        this.Address = customer.getAddress();
        this.ContactPerson = customer.getContactPerson();
        this.Tel = customer.getTel();
        this.LatitudeValue = customer.getLatitudeValue();
        this.LongitudeValue = customer.getLongitudeValue();
        this.LatLongString = customer.getLatLongString();
        this.Route = customer.getRoute();
        this.Channel = customer.getChannel();
        this.CustGrp = customer.getCustGrp();
        this.SalesManID = customer.getSalesManID();
        this.CardName_Eng = customer.getCardName_Eng();
        this.Address_Eng = customer.getAddress_Eng();
        this.ContactPerson_Eng = customer.getContactPerson_Eng();
        this.VisitStartDate = customer.getVisitStartDate();
        this.VisitEndDate = customer.getVisitEndDate();
        this.VisitTotal = customer.getVisitTotal();
        this.VisitStatus = customer.getVisitStatus();
        this.lastVisit = customer.getLastVisit();
    }

    public String getCardCode() {
        return CardCode;
    }

    public void setCardCode(String cardCode) {
        CardCode = cardCode;
    }

    public String getCardName() {
        return CardName;
    }

    public void setCardName(String cardName) {
        CardName = cardName;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getContactPerson() {
        return ContactPerson;
    }

    public void setContactPerson(String contactPerson) {
        ContactPerson = contactPerson;
    }

    public String getTel() {
        return Tel;
    }

    public void setTel(String tel) {
        Tel = tel;
    }

    public String getLatitudeValue() {
        return LatitudeValue;
    }

    public void setLatitudeValue(String latitudeValue) {
        LatitudeValue = latitudeValue;
    }

    public String getLongitudeValue() {
        return LongitudeValue;
    }

    public void setLongitudeValue(String longitudeValue) {
        LongitudeValue = longitudeValue;
    }

    public String getLatLongString() {
        return LatLongString;
    }

    public void setLatLongString(String latLongString) {
        LatLongString = latLongString;
    }

    public String getRoute() {
        return Route;
    }

    public void setRoute(String route) {
        Route = route;
    }

    public String getChannel() {
        return Channel;
    }

    public void setChannel(String channel) {
        Channel = channel;
    }

    public String getCustGrp() {
        return CustGrp;
    }

    public void setCustGrp(String custGrp) {
        CustGrp = custGrp;
    }

    public int getSalesManID() {
        return SalesManID;
    }

    public void setSalesManID(int salesManID) {
        SalesManID = salesManID;
    }

    public String getCardName_Eng() {
        return CardName_Eng;
    }

    public void setCardName_Eng(String cardName_Eng) {
        CardName_Eng = cardName_Eng;
    }

    public String getAddress_Eng() {
        return Address_Eng;
    }

    public void setAddress_Eng(String address_Eng) {
        Address_Eng = address_Eng;
    }

    public String getContactPerson_Eng() {
        return ContactPerson_Eng;
    }

    public void setContactPerson_Eng(String contactPerson_Eng) {
        ContactPerson_Eng = contactPerson_Eng;
    }

    public String getVisitStartDate() {
        return VisitStartDate;
    }

    public void setVisitStartDate(String visitStartDate) {
        VisitStartDate = visitStartDate;
    }

    public String getVisitEndDate() {
        return VisitEndDate;
    }

    public void setVisitEndDate(String visitEndDate) {
        VisitEndDate = visitEndDate;
    }

    public String getVisitTotal() {
        return VisitTotal;
    }

    public void setVisitTotal(String visitTotal) {
        VisitTotal = visitTotal;
    }

    public short getVisitStatus() {
        return VisitStatus;
    }

    public void setVisitStatus(short visitStatus) {
        VisitStatus = visitStatus;
    }

    public String getLastVisit() {
        return lastVisit;
    }

    public void setLastVisit(String lastVisit) {
        this.lastVisit = lastVisit;
    }
}
