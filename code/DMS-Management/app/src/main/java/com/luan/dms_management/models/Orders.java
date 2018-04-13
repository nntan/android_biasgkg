package com.luan.dms_management.models;

import java.util.ArrayList;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by luan.nt on 7/25/2017.
 */

public class Orders extends RealmObject {
    @PrimaryKey
    private String DocEntry;
    private String CardCode;
    private String CardName;
    private String Address;
    private String Route;
    private String OrderType;
    private String OrderTypeName;
    private String PONo;
    private String PODate;
    private String DeliveryNo;
    private String DeliveryDate;
    private String DocStatus;
    private String DocStatusName;
    private String SalesEmp;
    private String DeliveryEmp;
    private String DocTotal;
    private String DocVATTotal;
    private String DocGToal;
    private String Remark;
    private String CreateDate;

    public String getDocEntry() {
        return DocEntry;
    }

    public void setDocEntry(String docEntry) {
        DocEntry = docEntry;
    }

    public String getCardCode() {
        return CardCode;
    }

    public void setCardCode(String cardCode) {
        CardCode = cardCode;
    }

    public String getCardName() {
        return CardName;
    }

    public void setCardName(String cardName) {
        CardName = cardName;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getRoute() {
        return Route;
    }

    public void setRoute(String route) {
        Route = route;
    }

    public String getOrderType() {
        return OrderType;
    }

    public void setOrderType(String orderType) {
        OrderType = orderType;
    }

    public String getOrderTypeName() {
        return OrderTypeName;
    }

    public void setOrderTypeName(String orderTypeName) {
        OrderTypeName = orderTypeName;
    }

    public String getPODate() {
        return PODate;
    }

    public void setPODate(String PODate) {
        this.PODate = PODate;
    }

    public String getDeliveryNo() {
        return DeliveryNo;
    }

    public void setDeliveryNo(String deliveryNo) {
        DeliveryNo = deliveryNo;
    }

    public String getDeliveryDate() {
        return DeliveryDate;
    }

    public void setDeliveryDate(String deliveryDate) {
        DeliveryDate = deliveryDate;
    }

    public String getDocStatus() {
        return DocStatus;
    }

    public void setDocStatus(String docStatus) {
        DocStatus = docStatus;
    }

    public String getDocStatusName() {
        return DocStatusName;
    }

    public void setDocStatusName(String docStatusName) {
        DocStatusName = docStatusName;
    }

    public String getSalesEmp() {
        return SalesEmp;
    }

    public void setSalesEmp(String salesEmp) {
        SalesEmp = salesEmp;
    }

    public String getDeliveryEmp() {
        return DeliveryEmp;
    }

    public void setDeliveryEmp(String deliveryEmp) {
        DeliveryEmp = deliveryEmp;
    }

    public String getDocTotal() {
        return DocTotal;
    }

    public void setDocTotal(String docTotal) {
        DocTotal = docTotal;
    }

    public String getDocVATTotal() {
        return DocVATTotal;
    }

    public void setDocVATTotal(String docVATTotal) {
        DocVATTotal = docVATTotal;
    }

    public String getDocGToal() {
        return DocGToal;
    }

    public void setDocGToal(String docGToal) {
        DocGToal = docGToal;
    }

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String remark) {
        Remark = remark;
    }

    public String getCreateDate() {
        return CreateDate;
    }

    public void setCreateDate(String createDate) {
        CreateDate = createDate;
    }

    public String getPONo() {
        return PONo;
    }

    public void setPONo(String PONo) {
        this.PONo = PONo;
    }
}
