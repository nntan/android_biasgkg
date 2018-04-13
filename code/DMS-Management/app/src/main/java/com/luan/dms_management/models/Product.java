package com.luan.dms_management.models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by luan.nt on 7/26/2017.
 */

public class Product extends RealmObject {
    @PrimaryKey
    private String ItemCode;
    private String CodeBars;
    private String ItemName;
    private String FrgnName;
    private String BuyUoM;
    private String InvUom;
    private int even;
    private int retail;
    private String barcode;

    public Product(String codeBars, String itemName, String frgnName, String itemCode, String buyUoM, String invUom) {
        CodeBars = codeBars;
        ItemName = itemName;
        FrgnName = frgnName;
        ItemCode = itemCode;
        BuyUoM = buyUoM;
        InvUom = invUom;
        even = 0;
        retail = 0;
        barcode = itemCode;
    }

    public Product() {
        CodeBars = "";
        ItemName = "";
        FrgnName = "";
        ItemCode = "";
        BuyUoM = "";
        InvUom = "";
        even = 0;
        retail = 0;
        barcode = "";
    }

    public NormalProduct getNormalProduct() {
        return new NormalProduct(CodeBars, ItemName, FrgnName, ItemCode, BuyUoM, InvUom, "");
    }

    public String getCodeBars() {
        return CodeBars;
    }

    public void setCodeBars(String codeBars) {
        CodeBars = codeBars;
    }

    public String getItemName() {
        return ItemName;
    }

    public void setItemName(String itemName) {
        ItemName = itemName;
    }

    public String getFrgnName() {
        return FrgnName;
    }

    public void setFrgnName(String frgnName) {
        FrgnName = frgnName;
    }

    public String getItemCode() {
        return ItemCode;
    }

    public void setItemCode(String itemCode) {
        ItemCode = itemCode;
    }

    public String getBuyUoM() {
        return BuyUoM;
    }

    public void setBuyUoM(String buyUoM) {
        BuyUoM = buyUoM;
    }

    public String getInvUom() {
        return InvUom;
    }

    public void setInvUom(String invUom) {
        InvUom = invUom;
    }

    public int getEven() {
        return even;
    }

    public void setEven(int even) {
        this.even = even;
    }

    public int getRetail() {
        return retail;
    }

    public void setRetail(int retail) {
        this.retail = retail;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }
}
