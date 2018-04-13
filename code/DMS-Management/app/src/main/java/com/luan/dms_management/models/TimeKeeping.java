package com.luan.dms_management.models;

/**
 * Created by luan.nt on 8/18/2017.
 */

public class TimeKeeping {

    private String datetime;
    private String address;
    private int Status;

    public TimeKeeping(String datetime, String address, int status) {
        this.datetime = datetime;
        this.address = address;
        Status = status;
    }

    public TimeKeeping() {
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        Status = status;
    }
}
