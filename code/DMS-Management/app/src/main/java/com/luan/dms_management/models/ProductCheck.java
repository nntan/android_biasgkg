package com.luan.dms_management.models;

/**
 * Created by luan.nt on 7/31/2017.
 */

public class ProductCheck {
    private Product item;
    private boolean check;
    private String price;

    public ProductCheck(Product item) {
        this.item = item;
        this.check = false;
    }

    public ProductCheck(Product item, boolean check) {
        this.item = item;
        this.check = check;
    }

    public Product getItem() {
        return item;
    }

    public void setItem(Product item) {
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
}
