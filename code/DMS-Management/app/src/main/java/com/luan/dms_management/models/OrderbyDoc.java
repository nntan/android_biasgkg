package com.luan.dms_management.models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by luann on 31/10/2017.
 */

public class OrderbyDoc extends RealmObject {

    @PrimaryKey
    private String MyId;
    private String DocEntry;
    private String ItemCode;
    private String BarCode;
    private String ItemName;
    private String Quantity;
    private String UoM;
    private String Price;
    private String Discount;
    private String PriceAfterDiscount;
    private String VATAmt;
    private String VATPercent;
    private String GTotal;
    private String DocEntry1;
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

    public OrderbyDoc(OrderbyDoc orderbyDoc) {
        this.MyId = orderbyDoc.getMyId();
        this.DocEntry = orderbyDoc.getDocEntry();
        this.ItemCode = orderbyDoc.getItemCode();
        this.BarCode = orderbyDoc.getBarCode();
        this.ItemName = orderbyDoc.getItemName();
        this.Quantity = orderbyDoc.getQuantity();
        this.UoM = orderbyDoc.getUoM();
        this.Price = orderbyDoc.getPrice();
        this.Discount = orderbyDoc.getDiscount();
        this.PriceAfterDiscount = orderbyDoc.getPriceAfterDiscount();
        this.VATAmt = orderbyDoc.getVATAmt();
        this.VATPercent = orderbyDoc.getVATPercent();
        this.GTotal = orderbyDoc.getGTotal();
        this.DocEntry1 = orderbyDoc.getDocEntry1();
        this.CardCode = orderbyDoc.getCardCode();
        this.CardName = orderbyDoc.getCardName();
        this.Address = orderbyDoc.getAddress();
        this.Route = orderbyDoc.getRoute();
        this.OrderType = orderbyDoc.getOrderType();
        this.OrderTypeName = orderbyDoc.getOrderTypeName();
        this.PONo = orderbyDoc.getPONo();
        this.PODate = orderbyDoc.getPODate();
        this.DeliveryNo = orderbyDoc.getDeliveryNo();
        this.DeliveryDate = orderbyDoc.getDeliveryDate();
        this.DocStatus = orderbyDoc.getDocStatus();
        this.DocStatusName = orderbyDoc.getDocStatusName();
        this.SalesEmp = orderbyDoc.getSalesEmp();
        this.DocTotal = orderbyDoc.getDocTotal();
        this.DocVATTotal = orderbyDoc.getDocVATTotal();
        this.DocGToal = orderbyDoc.getDocGToal();
        this.Remark = orderbyDoc.getRemark();
        this.CreateDate = orderbyDoc.getCreateDate();
    }

    public OrderbyDoc() {
    }

    public String getMyId() {
        return MyId;
    }

    public void setMyId(String myId) {
        MyId = myId;
    }

    public String getDocEntry() {
        return DocEntry;
    }

    public void setDocEntry(String docEntry) {
        DocEntry = docEntry;
    }

    public String getItemCode() {
        return ItemCode;
    }

    public void setItemCode(String itemCode) {
        ItemCode = itemCode;
    }

    public String getBarCode() {
        return BarCode;
    }

    public void setBarCode(String barCode) {
        BarCode = barCode;
    }

    public String getItemName() {
        return ItemName;
    }

    public void setItemName(String itemName) {
        ItemName = itemName;
    }

    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String quantity) {
        Quantity = quantity;
    }

    public String getUoM() {
        return UoM;
    }

    public void setUoM(String uoM) {
        UoM = uoM;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getDiscount() {
        return Discount;
    }

    public void setDiscount(String discount) {
        Discount = discount;
    }

    public String getPriceAfterDiscount() {
        return PriceAfterDiscount;
    }

    public void setPriceAfterDiscount(String priceAfterDiscount) {
        PriceAfterDiscount = priceAfterDiscount;
    }

    public String getVATAmt() {
        return VATAmt;
    }

    public void setVATAmt(String VATAmt) {
        this.VATAmt = VATAmt;
    }

    public String getVATPercent() {
        return VATPercent;
    }

    public void setVATPercent(String VATPercent) {
        this.VATPercent = VATPercent;
    }

    public String getGTotal() {
        return GTotal;
    }

    public void setGTotal(String GTotal) {
        this.GTotal = GTotal;
    }

    public String getDocEntry1() {
        return DocEntry1;
    }

    public void setDocEntry1(String docEntry1) {
        DocEntry1 = docEntry1;
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

    public String getPONo() {
        return PONo;
    }

    public void setPONo(String PONo) {
        this.PONo = PONo;
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
}
