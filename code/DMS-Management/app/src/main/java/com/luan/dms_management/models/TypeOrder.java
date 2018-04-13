package com.luan.dms_management.models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by luann on 30/10/2017.
 */

public class TypeOrder extends RealmObject {
    @PrimaryKey
    private String TypeCode;
    private String TypeName;

    public String getTypeCode() {
        return TypeCode;
    }

    public void setTypeCode(String typeCode) {
        TypeCode = typeCode;
    }

    public String getTypeName() {
        return TypeName;
    }

    public void setTypeName(String typeName) {
        TypeName = typeName;
    }
}
