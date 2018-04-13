package com.luan.dms_management.models;

public class NormalProduct {
    private String CodeBars;
    private String ItemName;
    private String FrgnName;
    private String ItemCode;
    private String BuyUoM;
    private String InvUom;
    private int even;
    private int retail;
    private String barcode;
    private String ExpDate;

    public NormalProduct(String codeBars, String itemName, String frgnName, String itemCode, String buyUoM, String invUom, String expDate) {
        CodeBars = codeBars;
        ItemName = itemName;
        FrgnName = frgnName;
        ItemCode = itemCode;
        BuyUoM = buyUoM;
        InvUom = invUom;
        even = 0;
        retail = 0;
        barcode = itemCode;
        ExpDate = expDate;
    }

    public NormalProduct() {
        CodeBars = "";
        ItemName = "";
        FrgnName = "";
        ItemCode = "";
        BuyUoM = "";
        InvUom = "";
        even = 0;
        retail = 0;
        barcode = "";
        ExpDate = "";
    }

    public NormalProduct clone() {
        return new NormalProduct(CodeBars, ItemName, FrgnName, ItemCode, BuyUoM, InvUom, ExpDate);
    }

    public boolean isDiffWith(NormalProduct data) {
        if (!CodeBars.equalsIgnoreCase(data.getCodeBars())) {
            return true;
        }

        if (!ItemName.equalsIgnoreCase(data.getItemName())) {
            return true;
        }

        if (!FrgnName.equalsIgnoreCase(data.getFrgnName())) {
            return true;
        }

        if (!ItemCode.equalsIgnoreCase(data.getItemCode())) {
            return true;
        }

        if (!BuyUoM.equalsIgnoreCase(data.getBuyUoM())) {
            return true;
        }

        if (!InvUom.equalsIgnoreCase(data.getInvUom())) {
            return true;
        }

        if (even != data.getEven()) {
            return true;
        }

        if (retail != data.getRetail()) {
            return true;
        }

        if (!barcode.equalsIgnoreCase(data.getBarcode())) {
            return true;
        }

        return false;
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

    public void setExpDate(String data) {
        this.ExpDate= data;
    }

    public String getExpDate() {
        return this.ExpDate;
    }
}
