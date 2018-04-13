package com.luan.dms_management.service;

import android.content.Context;
import android.util.Base64;
import android.util.Log;

import com.google.gson.Gson;
import com.luan.dms_management.utils.PrefUtils;

import java.util.ArrayList;
import java.util.List;

public class SyncService {
    private static final String CHECKIN = "CHECKIN";
    private static final String CHECKIN_NOTE = "CHECKIN_NOTE";
    private static final String CHECKIN_IN_STOCK = "CHECKIN_IN_STOCK";
    private static final String CHECKOUT = "CHECKOUT";
    private static final String ADD_ORDER = "ADD_ORDER";

    private static final String CHECKIMAGE="CHECKIMAGE";


    private static final String CHECKIN_ID_PREF = "CHECKIN_ID_PREF";

    public static class SyncObjects {
        private List<SyncObject> data;

        public SyncObjects() {
            data = new ArrayList<>();
        }

        public void addSyncObject(SyncObject object) {
            data.add(object);
        }

        public void addSyncObjects(List<SyncObject> objects) {
            data.addAll(objects);
        }

        public List<SyncObject> getData() {
            return data;
        }
    }
    public static class SyncObject {
        public String name;
        private List<SyncParam> paramsData;

        public SyncObject() {
            paramsData = new ArrayList<>();
        }

        public void addParam(SyncParam param) {
            paramsData.add(param);
        }

        public void addParams(List<SyncParam> params) {
            paramsData.addAll(params);
        }

        public List<SyncParam> getParamsData() {
            return paramsData;
        }
    }
    public static class SyncParam {
        public String key;
        public String value;

        public SyncParam() {}
    }

    private final String SYNC_OBJECTS_PREF = "SYNC_OBJECTS_PREF";

    private Context mContext;
    private static SyncService instance;
    private SyncService(Context context) {
        mContext = context;
    }

    public static SyncService getInstance(Context context) {
        if (instance == null) {
            instance = new SyncService(context);
        }

        return instance;
    }

    private SyncObjects getSyncObjectsFromSP() {
        String data = PrefUtils.getPreference(mContext, SYNC_OBJECTS_PREF);
        if (data == null || data.isEmpty()) {
            return new SyncObjects();
        }

        Gson gson = new Gson();

        SyncObjects returnData = gson.fromJson(data, SyncObjects.class);
        return returnData;
    }
    private void saveSyncObjectsToSP(SyncObjects data) {
        Gson gson = new Gson();
        String jsonData = gson.toJson(data);

        PrefUtils.savePreference(mContext, SYNC_OBJECTS_PREF, jsonData);
    }

    private void addSyncObject(SyncObject data) {
        SyncObjects rootData = getSyncObjectsFromSP();
        rootData.addSyncObject(data);
        saveSyncObjectsToSP(rootData);
    }
    private void addSyncObjects(List<SyncObject> data) {
        SyncObjects rootData = getSyncObjectsFromSP();
        rootData.addSyncObjects(data);
        saveSyncObjectsToSP(rootData);
    }


    public void sync(final ApiService.CommonCallback callback) {
        processSyncWith(callback);
    }

    private void processSyncWith(final ApiService.CommonCallback callback) {
        final List<SyncObject> data = getSyncObjectsFromSP().data;
        if (data.size() == 0) {
            callback.onSuccess();
            return;
        }

        SyncObject syncObject = data.get(0);
        processSyncWith(syncObject, new ApiService.CommonCallback() {
            @Override
            public void onSuccess() {
                data.remove(0);

                SyncObjects s = new SyncObjects();
                s.addSyncObjects(data);
                saveSyncObjectsToSP(s);
                processSyncWith(callback);
            }

            @Override
            public void onFail(String error) {
                callback.onFail("");
            }
        });
    }

    private void processSyncWith(SyncObject object, final ApiService.CommonCallback callback) {
        if (object.name.equalsIgnoreCase(CHECKIN)) {
            doCheckin(object, callback);
            Log.d("doCheckinsyn","doCheckin");
            return;
        }

        if (object.name.equalsIgnoreCase(CHECKIN_IN_STOCK)) {
            doCheckinInStock(object, callback);
            return;
        }

        if (object.name.equalsIgnoreCase(CHECKIN_NOTE)) {
            doCheckinNote(object, callback);
            Log.d("doCheckinNote","doCheckinNote");
            return;
        }

        if (object.name.equalsIgnoreCase(CHECKOUT)) {
            Log.e("CHECKOUT","CHECKOUT");
            doCheckOutNote(object, callback);
        }

        if (object.name.equalsIgnoreCase(ADD_ORDER)) {
            doAddOrder(object, callback);
        }
        if (object.name.equalsIgnoreCase(CHECKIMAGE))
        {
            Log.d("checkimageimage","image");
            doCheckinPic(object,callback);
        }
    }


    private void doCheckin(SyncObject object, final ApiService.CommonCallback callback) {
        Log.d("doCheckin","doCheckin33");
        String customerCode = null;
        String userCode = null;
        String deviceID = null;

        for (SyncParam param : object.getParamsData()) {
            if (param.key.equalsIgnoreCase("CustCode")) {
                customerCode = param.value;
            }
            else if (param.key.equalsIgnoreCase("UserCode")) {
                userCode = param.value;
            }
            else if (param.key.equalsIgnoreCase("DeviceID")) {
                deviceID = param.value;
            }
        }

        if (customerCode == null || userCode == null || deviceID == null) {
            callback.onFail("");
            return;
        }

        ApiService.getInstance(mContext).checkin(customerCode, userCode, deviceID, new ApiService.CheckinCallback() {
            @Override
            public void onSuccess(String checkinID) {
                PrefUtils.savePreference(mContext, CHECKIN_ID_PREF, checkinID);
                callback.onSuccess();
            }

            @Override
            public void onFail(String error) {
                callback.onFail(error);
            }
        });
    }

    private void doCheckinInStock(SyncObject object, final ApiService.CommonCallback callback) {
        //Log.d("chaytoi","docheckin");
        String customerCode = null;
        String userCode = null;
        String checkInID = PrefUtils.getPreference(mContext, CHECKIN_ID_PREF);
        String itemCode = null;
        String barCode = null;
        String caseInStock = null;
        String bottleInStock = null;
        String remark = null;

        for (SyncParam param : object.getParamsData()) {
            if (param.key.equalsIgnoreCase("CustCode")) {
                customerCode = param.value;
            }
            else if (param.key.equalsIgnoreCase("UserCode")) {
                userCode = param.value;
            }
            else if (param.key.equalsIgnoreCase("ItemCode")) {
                itemCode = param.value;
            }
            else if (param.key.equalsIgnoreCase("BarCode")) {
                barCode = param.value;
            }
            else if (param.key.equalsIgnoreCase("CaseInStock")) {
                caseInStock = param.value;
            }
            else if (param.key.equalsIgnoreCase("BottleInStock")) {
                bottleInStock = param.value;
            }
            else if (param.key.equalsIgnoreCase("Remarks")) {
                remark = param.value;
            }
        }

        ApiService.getInstance(mContext).checkinInstock(customerCode, userCode, checkInID, itemCode, barCode, caseInStock, bottleInStock, remark, new ApiService.CommonCallback() {
            @Override
            public void onSuccess() {
                callback.onSuccess();
            }

            @Override
            public void onFail(String error) {
                callback.onFail(error);
            }
        });
    }
    private void doCheckinPic(SyncObject object, final ApiService.CommonCallback callback)
    {
        Log.d("doCheckinNote","doCheckinNote");
        String customerCode = null;
        String userCode = null;
        String checkInID = PrefUtils.getPreference(mContext, CHECKIN_ID_PREF);
        String pic1 = null;
        String pic2 = null;
        String pic3 = null;

        for (SyncParam param : object.getParamsData()) {
            if (param.key.equalsIgnoreCase("CustCode")) {
                customerCode = param.value;
            }
            else if (param.key.equalsIgnoreCase("UserCode")) {
                userCode = param.value;
            }
            else if (param.key.equalsIgnoreCase("Pics1")) {
                pic1 = param.value;
            }
            else if (param.key.equalsIgnoreCase("Pics2")) {
                pic2 = param.value;
            }
            else if (param.key.equalsIgnoreCase("Pics3")) {
                pic3 = param.value;
            }
        }

        ApiService.getInstance(mContext).CheckInAddPicture(customerCode, userCode, checkInID,pic1,pic2,pic3 , new ApiService.CommonCallback() {
            @Override
            public void onSuccess() {
                callback.onSuccess();
            }

            @Override
            public void onFail(String error) {
                callback.onFail(error);
            }
        });
    }
    private void doCheckinNote(SyncObject object, final ApiService.CommonCallback callback) {
        Log.d("doCheckinNote","doCheckinNote");
        String customerCode = null;
        String userCode = null;
        String checkInID = PrefUtils.getPreference(mContext, CHECKIN_ID_PREF);
        String remark = null;
        String notesGroup = null;

        for (SyncParam param : object.getParamsData()) {
            if (param.key.equalsIgnoreCase("CustCode")) {
                customerCode = param.value;
            }
            else if (param.key.equalsIgnoreCase("UserCode")) {
                userCode = param.value;
            }
            else if (param.key.equalsIgnoreCase("NotesGroup")) {
                notesGroup = param.value;
            }
            else if (param.key.equalsIgnoreCase("NotesRemark")) {
                remark = param.value;
            }
        }

        ApiService.getInstance(mContext).checkinAddLog(customerCode, userCode, checkInID, notesGroup, remark, new ApiService.CommonCallback() {
            @Override
            public void onSuccess() {
                callback.onSuccess();
            }

            @Override
            public void onFail(String error) {
                callback.onFail(error);
            }
        });
    }

    private void doCheckOutNote(SyncObject object, final ApiService.CommonCallback callback) {
        Log.d("doCheckOutNote","doCheckOutNote");
        String customerCode = null;
        String userCode = null;
        String checkInID = PrefUtils.getPreference(mContext, CHECKIN_ID_PREF);

        for (SyncParam param : object.getParamsData()) {
            if (param.key.equalsIgnoreCase("CustCode")) {
                customerCode = param.value;
            }
            else if (param.key.equalsIgnoreCase("UserCode")) {
                userCode = param.value;
            }
        }

        ApiService.getInstance(mContext).checkout(customerCode, userCode, checkInID, new ApiService.CommonCallback() {
            @Override
            public void onSuccess() {
                callback.onSuccess();
            }

            @Override
            public void onFail(String error) {
                callback.onFail(error);
            }
        });
    }

    private void doAddOrder(SyncObject object, final ApiService.CommonCallback callback) {
        String cardCode = null;
        String userCode = null;
        String orderDate = null;
        String deliveryDate = null;
        String cardName = null;
        String itemCode = null;
        String itemName = null;
        String itemType = null;
        String quantity = null;
        String uom = null;
        String price = null;
        String vat = null;

        for (SyncParam param : object.getParamsData()) {
            if (param.key.equalsIgnoreCase("CardCode")) {
                cardCode = param.value;
            }
            else if (param.key.equalsIgnoreCase("UserCode")) {
                userCode = param.value;
            }
            if (param.key.equalsIgnoreCase("OrderDate")) {
                orderDate = param.value;
            }
            else if (param.key.equalsIgnoreCase("DeliveryDate")) {
                deliveryDate = param.value;
            }
            if (param.key.equalsIgnoreCase("CardName")) {
                cardName = param.value;
            }
            else if (param.key.equalsIgnoreCase("ItemCode")) {
                itemCode = param.value;
            }
            if (param.key.equalsIgnoreCase("ItemName")) {
                itemName = param.value;
            }
            else if (param.key.equalsIgnoreCase("ItemType")) {
                itemType = param.value;
            }
            if (param.key.equalsIgnoreCase("Quantity")) {
                quantity = param.value;
            }
            else if (param.key.equalsIgnoreCase("UoM")) {
                uom = param.value;
            }
            if (param.key.equalsIgnoreCase("Price")) {
                price = param.value;
            }
            else if (param.key.equalsIgnoreCase("VAT")) {
                vat = param.value;
            }
        }

        ApiService.getInstance(mContext).addOrder(cardCode, userCode, orderDate, deliveryDate, cardName, itemCode, itemName, itemType, quantity, uom, price, vat, new ApiService.CommonCallback() {
            @Override
            public void onSuccess() {
                callback.onSuccess();
            }

            @Override
            public void onFail(String error) {
                callback.onFail(error);
            }
        });
    }


    public void offlineCheckin(String customerCode, String userCode, String deviceID) {
        SyncObject syncObject = new SyncObject();
        syncObject.name = CHECKIN;
        Log.d("deviceid",""+customerCode+"-"+userCode+"-"+deviceID);

        List<SyncParam> params = new ArrayList<>();
        SyncParam paramCustomerCode = new SyncParam();
        SyncParam paramUserCode = new SyncParam();
        SyncParam paramDeviceID = new SyncParam();

        paramCustomerCode.key = "CustCode";
        paramCustomerCode.value = customerCode;

        paramUserCode.key = "UserCode";
        paramUserCode.value = userCode;

        paramDeviceID.key = "DeviceID";
        paramDeviceID.value = deviceID;

        params.add(paramCustomerCode);
        params.add(paramUserCode);
        params.add(paramDeviceID);

        syncObject.addParams(params);

        addSyncObject(syncObject);
    }

    public void offlineCheckImage(String customerCode, String userCode, String Pics1, String Pics2, String Pics3)
    {
        Log.e("offlineCheckImage","offlineCheckImage");
        SyncObject syncObject = new SyncObject();
        syncObject.name = CHECKIMAGE;
        List<SyncParam> params = new ArrayList<>();

        SyncParam paramCustomerCode = new SyncParam();
        SyncParam paramUserCode = new SyncParam();
        SyncParam parampics1 = new SyncParam();
        SyncParam parampics2 = new SyncParam();
        SyncParam parampics3 = new SyncParam();

        paramCustomerCode.key = "CustCode";
        paramCustomerCode.value = customerCode;

        paramUserCode.key = "UserCode";
        paramUserCode.value = userCode;

        parampics1.key = "Pics1";
        parampics1.value = Pics1;

        parampics2.key = "Pics2";
        parampics2.value = Pics2;

        parampics3.key = "Pics3";
        parampics3.value = Pics3;

        params.add(paramCustomerCode);
        params.add(paramUserCode);
        params.add(parampics1);
        params.add(parampics2);
        params.add(parampics3);
        Log.e("offlineCheckImage",""+Pics1);
        Log.e("offlineCheckImage",""+Pics2);
        Log.e("offlineCheckImage",""+Pics3);
        syncObject.addParams(params);

        addSyncObject(syncObject);
    }

    public void offlineCheckinAddNote(String customerCode, String userCode, String notesGroup, String notesRemark) {
        Log.e("offlineCheckinAddNote","offlineCheckinAddNote");
        SyncObject syncObject = new SyncObject();
        syncObject.name = CHECKIN_NOTE;

        List<SyncParam> params = new ArrayList<>();
        SyncParam paramCustomerCode = new SyncParam();
        SyncParam paramUserCode = new SyncParam();
        SyncParam paramNotesGroup = new SyncParam();
        SyncParam paramNotesRemark = new SyncParam();

        paramCustomerCode.key = "CustCode";
        paramCustomerCode.value = customerCode;

        paramUserCode.key = "UserCode";
        paramUserCode.value = userCode;

        paramNotesGroup.key = "NotesGroup";
        paramNotesGroup.value = notesGroup;

        paramNotesRemark.key = "NotesRemark";
        paramNotesRemark.value = notesRemark;

        params.add(paramCustomerCode);
        params.add(paramUserCode);
        params.add(paramNotesGroup);
        params.add(paramNotesRemark);

        syncObject.addParams(params);

        addSyncObject(syncObject);


    }

    public void offlineCheckinInStockAdd(String customerCode, String userCode, String itemCode, String barCode, String caseInStock, String bottleInStock, String remarks) {
        Log.d("offlineCheckinInStock","offlineCheckinInStockAdd");

        SyncObject syncObject = new SyncObject();
        syncObject.name = CHECKIN_IN_STOCK;

        List<SyncParam> params = new ArrayList<>();
        SyncParam paramCustomerCode = new SyncParam();
        SyncParam paramUserCode = new SyncParam();
        SyncParam paramItemCode = new SyncParam();
        SyncParam paramBarCode = new SyncParam();
        SyncParam paramCaseInStock = new SyncParam();
        SyncParam paramBottleInStock = new SyncParam();
        SyncParam paramRemark = new SyncParam();

        paramCustomerCode.key = "CustCode";
        paramCustomerCode.value = customerCode;

        paramUserCode.key = "UserCode";
        paramUserCode.value = userCode;

        paramItemCode.key = "ItemCode";
        paramItemCode.value = itemCode;

        paramBarCode.key = "BarCode";
        paramBarCode.value = barCode;

        paramCaseInStock.key = "CaseInStock";
        paramCaseInStock.value = caseInStock;

        paramBottleInStock.key = "BottleInStock";
        paramBottleInStock.value = bottleInStock;

        paramRemark.key = "Remarks";
        paramRemark.value = remarks;


        params.add(paramCustomerCode);
        params.add(paramUserCode);
        params.add(paramItemCode);
        params.add(paramBarCode);
        params.add(paramCaseInStock);
        params.add(paramBottleInStock);
        params.add(paramRemark);

        syncObject.addParams(params);

        addSyncObject(syncObject);

    }

    public void offlineCheckout(String customerCode, String userCode) {
        SyncObject syncObject = new SyncObject();
        syncObject.name = CHECKOUT;

        List<SyncParam> params = new ArrayList<>();
        SyncParam paramCustomerCode = new SyncParam();
        SyncParam paramUserCode = new SyncParam();

        paramCustomerCode.key = "CustCode";
        paramCustomerCode.value = customerCode;

        paramUserCode.key = "UserCode";
        paramUserCode.value = userCode;

        params.add(paramCustomerCode);
        params.add(paramUserCode);

        syncObject.addParams(params);

        addSyncObject(syncObject);
    }

    public void offlineAddOrderProduct(String cardCode, String userCode, String orderDate, String deliveryDate, String cardName, String itemCode, String itemName, String itemType, String quantity, String uom, String price, String vat) {
        SyncObject syncObject = new SyncObject();
        syncObject.name = ADD_ORDER;

        List<SyncParam> params = new ArrayList<>();
        SyncParam paramCardCode = new SyncParam();
        SyncParam paramUserCode = new SyncParam();
        SyncParam paramOrderDate = new SyncParam();
        SyncParam paramDeliveryDate = new SyncParam();
        SyncParam paramCardName = new SyncParam();
        SyncParam paramItemCode = new SyncParam();
        SyncParam paramItemName = new SyncParam();
        SyncParam paramItemType = new SyncParam();
        SyncParam paramQuantity = new SyncParam();
        SyncParam paramUoM = new SyncParam();
        SyncParam paramPrice = new SyncParam();
        SyncParam paramVAT = new SyncParam();

        paramCardCode.key = "Cardcode";
        paramCardCode.value = cardCode;

        paramUserCode.key = "UserCode";
        paramUserCode.value = userCode;

        paramOrderDate.key = "OrderDate";
        paramOrderDate.value = orderDate;

        paramDeliveryDate.key = "DeliveryDate";
        paramDeliveryDate.value = deliveryDate;

        paramCardName.key = "CardName";
        paramCardName.value = cardName;

        paramItemCode.key = "ItemCode";
        paramItemCode.value = itemCode;

        paramItemName.key = "ItemName";
        paramItemName.value = itemName;

        paramItemType.key = "ItemType";
        paramItemType.value = itemType;

        paramQuantity.key = "Quantity";
        paramQuantity.value = quantity;

        paramUoM.key = "UoM";
        paramUoM.value = uom;

        paramPrice.key = "Price";
        paramPrice.value = price;

        paramVAT.key = "VAT";
        paramVAT.value = vat;

        params.add(paramCardCode);
        params.add(paramUserCode);
        params.add(paramOrderDate);
        params.add(paramDeliveryDate);
        params.add(paramCardName);
        params.add(paramItemCode);
        params.add(paramItemName);
        params.add(paramItemType);
        params.add(paramQuantity);
        params.add(paramUoM);
        params.add(paramPrice);
        params.add(paramVAT);

        syncObject.addParams(params);

        addSyncObject(syncObject);
    }
}
