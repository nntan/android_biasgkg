package com.luan.dms_management.models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by luan.nt on 9/6/2017.
 */

public class CustomerGroup extends RealmObject {
    @PrimaryKey
    private String GrpCode;
    private String GrpName;

    public String getGrpCode() {
        return GrpCode;
    }

    public void setGrpCode(String grpCode) {
        GrpCode = grpCode;
    }

    public String getGrpName() {
        return GrpName;
    }

    public void setGrpName(String grpName) {
        GrpName = grpName;
    }
}
