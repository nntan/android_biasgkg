package com.luan.dms_management.models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by luan.nt on 9/1/2017.
 */

public class Staff extends RealmObject {
    @PrimaryKey
    private int SlpCode;
    private String SlpName;
    private String Memo;
    private String Telephone;
    private String Mobil;
    private String Email;
    private int Manager;

    public int getSlpCode() {
        return SlpCode;
    }

    public void setSlpCode(int slpCode) {
        SlpCode = slpCode;
    }

    public String getSlpName() {
        return SlpName;
    }

    public void setSlpName(String slpName) {
        SlpName = slpName;
    }

    public String getMemo() {
        return Memo;
    }

    public void setMemo(String memo) {
        Memo = memo;
    }

    public String getTelephone() {
        return Telephone;
    }

    public void setTelephone(String telephone) {
        Telephone = telephone;
    }

    public String getMobil() {
        return Mobil;
    }

    public void setMobil(String mobil) {
        Mobil = mobil;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public int getManager() {
        return Manager;
    }

    public void setManager(int manager) {
        Manager = manager;
    }
}
