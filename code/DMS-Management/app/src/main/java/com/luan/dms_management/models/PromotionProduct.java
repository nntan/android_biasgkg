package com.luan.dms_management.models;

import android.support.v7.widget.RecyclerView;

public class PromotionProduct {
    private String CodeBars;
    private String ItemName;
    private String ItemCode;
    private boolean isCheck;

    public PromotionProduct() {

    }

    public String getCodeBars() {
        return CodeBars;
    }

    public String getItemName() {
        return ItemName;
    }

    public String getItemCode() {
        return ItemCode;
    }

    public void setCodeBars(String data) {
        CodeBars = data;
    }

    public void setItemName(String data) {
        ItemName = data;
    }

    public void setItemCode(String data) {
        ItemCode = data;
    }
}
