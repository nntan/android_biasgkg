package com.luan.dms_management.models;

/**
 * Created by hanbiro on 12/12/2017.
 */

public class TimeSheet {
    private String tsid;
    private String usercode;
    private String username;
    private String deviceid;
    private String starttime;
    private String latstart;
    private String longstart;
    private String endtime;
    private String latend;
    private String longend;
    private String nohour;

    public String getTsid() {
        return tsid;
    }

    public void setTsid(String tsid) {
        this.tsid = tsid;
    }

    public String getUsercode() {
        return usercode;
    }

    public void setUsercode(String usercode) {
        this.usercode = usercode;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDeviceid() {
        return deviceid;
    }

    public void setDeviceid(String deviceid) {
        this.deviceid = deviceid;
    }

    public String getStarttime() {
        return starttime;
    }

    public void setStarttime(String starttime) {
        this.starttime = starttime;
    }

    public String getLatstart() {
        return latstart;
    }

    public void setLatstart(String latstart) {
        this.latstart = latstart;
    }

    public String getLongstart() {
        return longstart;
    }

    public void setLongstart(String longstart) {
        this.longstart = longstart;
    }

    public String getEndtime() {
        return endtime;
    }

    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }

    public String getLatend() {
        return latend;
    }

    public void setLatend(String latend) {
        this.latend = latend;
    }

    public String getLongend() {
        return longend;
    }

    public void setLongend(String longend) {
        this.longend = longend;
    }

    public String getNohour() {
        return nohour;
    }

    public void setNohour(String nohour) {
        this.nohour = nohour;
    }
}
