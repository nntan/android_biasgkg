package com.luan.dms_management.models;

public class InventoryProduct {
    public String getBarcode() {
        return Barcode;
    }

    public void setBarcode(String barcode) {
        Barcode = barcode;
    }

    public String getItemCode() {
        return ItemCode;
    }

    public void setItemCode(String itemCode) {
        ItemCode = itemCode;
    }

    public String getItemName() {
        return ItemName;
    }

    public void setItemName(String itemName) {
        ItemName = itemName;
    }

    public String getUom() {
        return Uom;
    }

    public void setUom(String uom) {
        Uom = uom;
    }

    public String getInvUom() {
        return InvUom;
    }

    public void setInvUom(String invUom) {
        InvUom = invUom;
    }

    public String getExpDate() {
        return ExpDate;
    }

    public void setExpDate(String expDate) {
        ExpDate = expDate;
    }

    private String Barcode;
    private String ItemCode;
    private String ItemName;
    private String Uom;
    private String InvUom;
    private String ExpDate;


    public InventoryProduct() {}


}
