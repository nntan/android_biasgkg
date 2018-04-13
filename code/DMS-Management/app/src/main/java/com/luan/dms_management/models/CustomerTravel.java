package com.luan.dms_management.models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by luann on 02/10/2017.
 */

public class CustomerTravel extends RealmObject {
    @PrimaryKey
    private long MyId;
    private int EmpId;
    private String CardCode;
    private String CardName;
    private String Address;
    private String ContactPerson;
    private String Tel;
    private String LatitudeValue;
    private String LongitudeValue;
    private String LatLongString;
    private short VisitOrder;
    private String CustPic;
    private int NoCustOrderPlan;
    private int NoCustOrderActual;
    private int NoCustDelPlan;
    private int NoCustDelActual;
    private String VisitStartDate;
    private String VisitEndDate;
    private String VisitTotal;
    private short VisitStatus;

    public CustomerTravel() {
        this.MyId = 0;
        this.EmpId = 0;
        this.CardCode = "";
        this.CardName = "";
        this.Address = "";
        this.ContactPerson = "";
        this.Tel = "";
        this.LatitudeValue = "";
        this.LongitudeValue = "";
        this.LatLongString = "";
        this.VisitOrder = 0;
        this.CustPic = "";
        this.NoCustOrderPlan = 0;
        this.NoCustOrderActual = 0;
        this.NoCustDelPlan = 0;
        this.NoCustDelActual = 0;
        this.VisitStartDate = "";
        this.VisitEndDate = "";
        this.VisitTotal = "";
        this.VisitStatus = 0;
    }

    public long getMyId() {
        return MyId;
    }

    public void setMyId(long myId) {
        MyId = myId;
    }

    public int getEmpId() {
        return EmpId;
    }

    public void setEmpId(int empId) {
        EmpId = empId;
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

    public short getVisitOrder() {
        return VisitOrder;
    }

    public void setVisitOrder(short visitOrder) {
        VisitOrder = visitOrder;
    }

    public String getCustPic() {
        return CustPic;
    }

    public void setCustPic(String custPic) {
        CustPic = custPic;
    }

    public int getNoCustOrderPlan() {
        return NoCustOrderPlan;
    }

    public void setNoCustOrderPlan(int noCustOrderPlan) {
        NoCustOrderPlan = noCustOrderPlan;
    }

    public int getNoCustOrderActual() {
        return NoCustOrderActual;
    }

    public void setNoCustOrderActual(int noCustOrderActual) {
        NoCustOrderActual = noCustOrderActual;
    }

    public int getNoCustDelPlan() {
        return NoCustDelPlan;
    }

    public void setNoCustDelPlan(int noCustDelPlan) {
        NoCustDelPlan = noCustDelPlan;
    }

    public int getNoCustDelActual() {
        return NoCustDelActual;
    }

    public void setNoCustDelActual(int noCustDelActual) {
        NoCustDelActual = noCustDelActual;
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
}
