package com.luan.dms_management.models;

/**
 * Created by luan.nt on 7/31/2017.
 */

public class ProductCheckForOrder {
    private NormalProduct item;
    private boolean check;
    private String price;
    private String vat;
    private String quantity;

    public ProductCheckForOrder(NormalProduct item) {
        this.item = item;
        this.check = false;
    }

    public ProductCheckForOrder(NormalProduct item, boolean check) {
        this.item = item;
        this.check = check;
    }

    public NormalProduct getItem() {
        return item;
    }

    public void setItem(NormalProduct item) {
        this.item = item;
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

    public void setPrice(String value) {
        this.price = value;
    }

    public String getPrice() {
        return this.price;
    }

    public void setVAT(String value) {
        this.vat = value;
    }

    public String getVAT() {
        return this.vat;
    }

    public void setQuantity(String value) {
        this.quantity = value;
    }

    public String getQuantity() {
        return this.quantity;
    }
}
