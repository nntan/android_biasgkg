package com.luan.dms_management.models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by luann on 30/10/2017.
 */

public class WareHouse extends RealmObject {
    @PrimaryKey
    private String WhsCode;
    private String WhsName;

    public String getWhsCode() {
        return WhsCode;
    }

    public void setWhsCode(String whsCode) {
        WhsCode = whsCode;
    }

    public String getWhsName() {
        return WhsName;
    }

    public void setWhsName(String whsName) {
        WhsName = whsName;
    }
}
