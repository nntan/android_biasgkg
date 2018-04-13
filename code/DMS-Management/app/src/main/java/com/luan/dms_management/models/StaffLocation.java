package com.luan.dms_management.models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by luan.nt on 9/19/2017.
 */

public class StaffLocation extends RealmObject {
    @PrimaryKey
    private long MyId;
    private int EmpId;
    private String EmpFullName;
    private String PicLink;
    private short CheckInPlan;
    private short CheckInFinish;
    private short NoOrder;
    private String CurrLat;
    private String CurrLong;
    private String LastUpdate;

    public StaffLocation() {
        this.MyId = 0;
        this.EmpId = 0;
        this.EmpFullName = "";
        this.PicLink = "";
        this.CheckInPlan = 0;
        this.CheckInFinish = 0;
        this.NoOrder = 0;
        this.CurrLat = "";
        this.CurrLong = "";
        this.LastUpdate = "";
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

    public String getEmpFullName() {
        return EmpFullName;
    }

    public void setEmpFullName(String empFullName) {
        EmpFullName = empFullName;
    }

    public String getPicLink() {
        return PicLink;
    }

    public void setPicLink(String picLink) {
        PicLink = picLink;
    }

    public short getCheckInPlan() {
        return CheckInPlan;
    }

    public void setCheckInPlan(short checkInPlan) {
        CheckInPlan = checkInPlan;
    }

    public short getCheckInFinish() {
        return CheckInFinish;
    }

    public void setCheckInFinish(short checkInFinish) {
        CheckInFinish = checkInFinish;
    }

    public short getNoOrder() {
        return NoOrder;
    }

    public void setNoOrder(short noOrder) {
        NoOrder = noOrder;
    }

    public String getCurrLat() {
        return CurrLat;
    }

    public void setCurrLat(String currLat) {
        CurrLat = currLat;
    }

    public String getCurrLong() {
        return CurrLong;
    }

    public void setCurrLong(String currLong) {
        CurrLong = currLong;
    }

    public String getLastUpdate() {
        return LastUpdate;
    }

    public void setLastUpdate(String lastUpdate) {
        LastUpdate = lastUpdate;
    }
}
