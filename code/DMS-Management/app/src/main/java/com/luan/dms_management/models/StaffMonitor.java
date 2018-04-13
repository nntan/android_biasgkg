package com.luan.dms_management.models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by luan.nt on 9/19/2017.
 */

public class StaffMonitor extends RealmObject {
    @PrimaryKey
    private long MyId;
    private int EmpId;
    private String EmpFullName;
    private String PicLink;
    private String MyDate;
    private short CheckInPlan;
    private short CheckInFinish;
    private short NoOrder;
    private String LastCustName;
    private String LastCustAdd;
    private String NoofMetter;

    public StaffMonitor() {
        this.MyId = 0;
        this.EmpId = 0;
        this.EmpFullName = "";
        this.PicLink = "";
        this.MyDate = "";
        this.CheckInPlan = 0;
        this.CheckInFinish = 0;
        this.NoOrder = 0;
        this.LastCustName = "";
        this.LastCustAdd = "";
        this.NoofMetter = "";
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

    public String getMyDate() {
        return MyDate;
    }

    public void setMyDate(String myDate) {
        MyDate = myDate;
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

    public String getLastCustName() {
        return LastCustName;
    }

    public void setLastCustName(String lastCustName) {
        LastCustName = lastCustName;
    }

    public String getLastCustAdd() {
        return LastCustAdd;
    }

    public void setLastCustAdd(String lastCustAdd) {
        LastCustAdd = lastCustAdd;
    }

    public String getNoofMetter() {
        return NoofMetter;
    }

    public void setNoofMetter(String noofMetter) {
        NoofMetter = noofMetter;
    }
}
