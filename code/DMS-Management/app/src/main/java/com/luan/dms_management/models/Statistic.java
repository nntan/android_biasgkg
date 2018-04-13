package com.luan.dms_management.models;

import com.luan.dms_management.activities.Checkin;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;


public class Statistic extends RealmObject {

    @PrimaryKey
    private String CustCode;
    private String CheckInTime;
    private String CheckOutTime;
    private String NoCustOrder;
    private String NoPicTake;

    public void setCustCode(String data) {
        CustCode = data;
    }
    public void setCheckInTime(String data) {
        CheckInTime = data;
    }
    public void setCheckOutTime(String data) {
        CheckOutTime = data;
    }
    public void setNoCustOrder(String data) {
        NoCustOrder = data;
    }
    public void setNoPicTake(String data) {
        NoPicTake = data;
    }

    public String getCustCode() {
        return CustCode;
    }
    public String getCheckInTime() {
        return CheckInTime;
    }
    public String getCheckOutTime() {
        return CheckOutTime;
    }
    public String getNoCustOrder() {
        return NoCustOrder;
    }
    public String getNoPicTake() {
        return NoPicTake;
    }
}
