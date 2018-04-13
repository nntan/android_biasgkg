package com.luan.dms_management.service;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.luan.dms_management.models.Channel;
import com.luan.dms_management.models.Customer;
import com.luan.dms_management.models.CustomerGroup;
import com.luan.dms_management.models.InventoryProduct;
import com.luan.dms_management.models.LeaveLog;
import com.luan.dms_management.models.LeaveReason;
import com.luan.dms_management.models.LeaveType;
import com.luan.dms_management.models.NormalNoteType;
import com.luan.dms_management.models.NormalProduct;
import com.luan.dms_management.models.NoteType;
import com.luan.dms_management.models.OrderbyDoc;
import com.luan.dms_management.models.Orders;
import com.luan.dms_management.models.Product;
import com.luan.dms_management.models.PromotionProduct;
import com.luan.dms_management.models.Route;
import com.luan.dms_management.models.Staff;
import com.luan.dms_management.models.StaffLocation;
import com.luan.dms_management.models.StaffMonitor;
import com.luan.dms_management.models.Statistic;
import com.luan.dms_management.models.StatusOrder;
import com.luan.dms_management.models.TimeSheet;
import com.luan.dms_management.models.TypeOrder;
import com.luan.dms_management.models.TypeVisit;
import com.luan.dms_management.models.WareHouse;
import com.luan.dms_management.utils.CommonUtils;
import com.luan.dms_management.utils.PrefUtils;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import static com.luan.dms_management.service.Config.URL_SAVE_KEY;

/**
 * Created by luan.nt on 4/12/2017.
 */

public class ApiService {
    private static ApiService instance;

    private ApiService(Context context) {
        String oldURL = PrefUtils.getPreference(context, Config.URL_SAVE_KEY);
        if (oldURL != null) {
            userURL = oldURL;
        }
    }

    //Callback definition
    public interface CommonCallback {
        void onSuccess();

        void onFail(String error);
    }

    public interface LoginCallBack {
        void onSuccess(String datetime, String salemanid);

        void onFail(String error);
    }

    public interface GetListStaff {
        void onSuccess(List<Staff> data);

        void onFail(String error);
    }

    public interface GetListProduct {
        void onSuccess(List<Product> data);

        void onFail(String error);
    }

    public interface GetListNormalProduct {
        void onSuccess(List<NormalProduct> data);

        void onFail(String error);
    }

    public interface GetListCustomer {
        void onSuccess(List<Customer> data);

        void onFail(String error);
    }

    public interface GetListCustomerTravel {
        void onSuccess(List<Customer> data);

        void onFail(String error);
    }

    public interface GetListStaffMonitor {
        void onSuccess(List<StaffMonitor> data);

        void onFail(String error);
    }

    public interface GetListStaffLocation {
        void onSuccess(List<StaffLocation> data);

        void onFail(String error);
    }

    public interface GetListRoute {
        void onSuccess(List<Route> data);

        void onFail(String error);
    }

    public interface GetListChannel {
        void onSuccess(List<Channel> data);

        void onFail(String error);
    }

    public interface GetListCusGrp {
        void onSuccess(List<CustomerGroup> data);

        void onFail(String error);
    }

    public interface GetListTypeVisit {
        void onSuccess(List<TypeVisit> data);

        void onFail(String error);
    }

    public interface GetListTypeOrder {
        void onSuccess(List<TypeOrder> data);

        void onFail(String error);
    }

    public interface GetListStatusOrder {
        void onSuccess(List<StatusOrder> data);

        void onFail(String error);
    }

    public interface GetListWareHouse {
        void onSuccess(List<WareHouse> data);

        void onFail(String error);
    }

    public interface GetListOrder {
        void onSuccess(List<Orders> data);

        void onFail(String error);
    }

    public interface GetListOrderbyDoc {
        void onSuccess(ArrayList<OrderbyDoc> data);

        void onFail(String error);
    }

    public interface GetListNoteType {
        void onSuccess(List<NoteType> data);

        void onFail(String error);
    }

    public interface GetListCustGroup {
        void onSuccess(List<NoteType> data);

        void onFail(String error);
    }

    public interface CheckinCallback {
        void onSuccess(String checkinID);

        void onFail(String error);
    }

    public interface GetNoteCallback {
        void onSuccess(String data);

        void onFail(String error);
    }

    public interface AddCustCallback {
        void onSuccess(String data);

        void onFail(String error);
    }

    public interface GetPromotionProductsCallback {
        void onSuccess(List<PromotionProduct> data);

        void onFail(String error);
    }

    public interface GetPriceCallback {
        void onSuccess(String price, String vat);

        void onFail(String error);
    }

    public interface GetStatisticsCallback {
        void onSuccess(List<Statistic> data);

        void onFail(String error);
    }

    public interface GetInventoryProductCallback {
        void onSuccess(InventoryProduct data);

        void onFail(String error);
    }

    public interface GetLeaveReasonCallback {
        void onSuccess(List<LeaveReason> data);

        void onFail(String error);
    }

    public interface GetLeaveTypeCallback {
        void onSuccess(List<LeaveType> data);

        void onFail(String error);
    }

    public interface AddLeaveCallback {
        void onSuccess(String data);

        void onFail(String error);
    }

    public interface GetLeaveLogCallback {
        void onSuccess(List<LeaveLog> data);

        void onFail(String error);
    }

    public interface ApproveLeaveCallback {
        void onSuccess(String data);

        void onFail(String error);
    }

    public interface AddTimeSheetCallback {
        void onSuccess(String data);

        void onFail(String error);
    }

    public interface LogTimeSheetCallback {
        void onSuccess(List<TimeSheet> data);

        void onFail(String error);
    }

    public interface UpdateTimeSheetCallback {
        void onSuccess(String data);

        void onFail(String error);
    }

    //Variable definition
    Network mNetwork = Network.getInstance();
    String userURL = null;
    String mUserCode;

    public void setUserCode(String data) {
        mUserCode = data;
    }

    public String getUserCode() {
        return mUserCode;
    }

    public static ApiService getInstance(Context context) {
        if (instance == null) {
            instance = new ApiService(context);
        }

        return instance;
    }

    public void saveNewURL(Context context, String newURL) {
        PrefUtils.savePreference(context, URL_SAVE_KEY, newURL);
        userURL = newURL;
//        reasonsData = null;
//        wareHousesData = null;
    }

    public String getURL() {
        if (userURL == null) {
            return Config.HOST;
        }

        return userURL;
    }

    public void login(String username, String password, String deviceID, final LoginCallBack callback) {
        String link = String.format(getURL() + Config.LOGIN);

        mUserCode = username;
        List<Param> params = new ArrayList<>();
        Param param = new Param("UserCode", username);
        params.add(param);
        param = new Param("Password", password);
        params.add(param);
        param = new Param("DeviceID", deviceID);
        params.add(param);

        mNetwork.executePost(link, params, new Network.Callback() {
            @Override
            public void onCallBack(String response) {
                XmlPullParserFactory factory = null;
                try {
                    boolean readyToGetResult = false;
                    boolean readyToGetUser = false;
                    boolean readyToGetWorking = false;
                    boolean readyToGetSaleID = false;

                    String currentResult = null;
                    String currentUser = null;
                    String currentWorking = null;
                    String currentSaleID = null;

                    factory = XmlPullParserFactory.newInstance();
                    factory.setNamespaceAware(true);
                    XmlPullParser xpp = factory.newPullParser();

                    xpp.setInput(new StringReader(response));
                    int eventType = xpp.getEventType();
                    while (eventType != XmlPullParser.END_DOCUMENT) {
                        if (eventType == XmlPullParser.START_DOCUMENT) {
                            //TODO: do nothing
                        } else if (eventType == XmlPullParser.START_TAG) {
                            if (xpp.getName().equalsIgnoreCase("Result")) {
                                readyToGetResult = true;
                            }

                            if (xpp.getName().equalsIgnoreCase("UserCode")) {
                                readyToGetUser = true;
                            }

                            if (xpp.getName().equalsIgnoreCase("WorkingDate")) {
                                readyToGetWorking = true;
                            }
                            if (xpp.getName().equalsIgnoreCase("SalesManId")) {
                                readyToGetSaleID = true;
                            }

                        } else if (eventType == XmlPullParser.TEXT) {
                            String data = xpp.getText();

                            if (readyToGetResult) {
                                currentResult = data;
                                readyToGetResult = false;
                                if (!TextUtils.isEmpty(currentResult) &&
                                        !TextUtils.isEmpty(currentUser) &&
                                        !TextUtils.isEmpty(currentWorking) &&
                                        !TextUtils.isEmpty(currentSaleID)) {
                                    if (currentResult.equalsIgnoreCase("1")) {
                                        callback.onSuccess(currentWorking, currentSaleID);
                                    } else {
                                        callback.onFail("");
                                    }

                                    currentResult = null;
                                    currentUser = null;
                                    currentWorking = null;
                                    currentSaleID = null;
                                }
                            }

                            if (readyToGetUser) {
                                currentUser = data;
                                readyToGetUser = false;
                                if (!TextUtils.isEmpty(currentResult) &&
                                        !TextUtils.isEmpty(currentUser) &&
                                        !TextUtils.isEmpty(currentWorking) &&
                                        !TextUtils.isEmpty(currentSaleID)) {
                                    if (currentResult.equalsIgnoreCase("1")) {
                                        callback.onSuccess(currentWorking, currentSaleID);
                                    } else {
                                        callback.onFail("");
                                    }

                                    currentResult = null;
                                    currentUser = null;
                                    currentWorking = null;
                                    currentSaleID = null;
                                }
                            }

                            if (readyToGetWorking) {
                                currentWorking = data;
                                readyToGetWorking = false;
                                if (!TextUtils.isEmpty(currentResult) &&
                                        !TextUtils.isEmpty(currentUser) &&
                                        !TextUtils.isEmpty(currentWorking) &&
                                        !TextUtils.isEmpty(currentSaleID)) {
                                    if (currentResult.equalsIgnoreCase("1")) {
                                        callback.onSuccess(currentWorking, currentSaleID);
                                    } else {
                                        callback.onFail("");
                                    }

                                    currentResult = null;
                                    currentUser = null;
                                    currentWorking = null;
                                    currentSaleID = null;
                                }
                            }

                            if (readyToGetSaleID) {
                                currentSaleID = data;
                                readyToGetSaleID = false;
                                if (!TextUtils.isEmpty(currentResult) &&
                                        !TextUtils.isEmpty(currentUser) &&
                                        !TextUtils.isEmpty(currentWorking) &&
                                        !TextUtils.isEmpty(currentSaleID)) {
                                    if (currentResult.equalsIgnoreCase("1")) {
                                        callback.onSuccess(currentWorking, currentSaleID);
                                    } else {
                                        callback.onFail("");
                                    }

                                    currentResult = null;
                                    currentUser = null;
                                    currentWorking = null;
                                    currentSaleID = null;
                                }
                            }
                        }
                        eventType = xpp.next();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    callback.onFail(e.getMessage());
                }
            }

            @Override
            public void onFail(String error) {
                callback.onFail(error);
            }
        });
    }

    public void loadListStaff(String UserCode, final GetListStaff callback) {
        String link = String.format(getURL() + Config.LISTSTAFF);

        List<Param> params = new ArrayList<>();
        Param param = new Param("UserCode", UserCode);
        params.add(param);
        mNetwork.executePost(link, params, new Network.Callback() {
            @Override
            public void onCallBack(String response) {
                XmlPullParserFactory factory = null;
                List<Staff> staffList = new ArrayList<>();
                try {
                    boolean readyToGetCode = false;
                    boolean readyToGetName = false;
                    boolean readyToGetMemo = false;

                    String currentCode = null;
                    String currentName = null;
                    String currentMemo = null;

                    factory = XmlPullParserFactory.newInstance();
                    factory.setNamespaceAware(true);
                    XmlPullParser xpp = factory.newPullParser();

                    xpp.setInput(new StringReader(response));
                    int eventType = xpp.getEventType();
                    while (eventType != XmlPullParser.END_DOCUMENT) {
                        if (eventType == XmlPullParser.START_DOCUMENT) {
                            //TODO: do nothing
                        } else if (eventType == XmlPullParser.START_TAG) {
                            if (xpp.getName().equalsIgnoreCase("SlpCode")) {
                                readyToGetCode = true;
                            }

                            if (xpp.getName().equalsIgnoreCase("SlpName")) {
                                readyToGetName = true;
                            }

                            if (xpp.getName().equalsIgnoreCase("Memo")) {
                                readyToGetMemo = true;
                            }

                        } else if (eventType == XmlPullParser.TEXT) {
                            String data = xpp.getText();

                            if (readyToGetCode == true) {
                                currentCode = data;
                                readyToGetCode = false;
                                if (!TextUtils.isEmpty(currentCode) &&
                                        !TextUtils.isEmpty(currentName) &&
                                        !TextUtils.isEmpty(currentMemo)) {
                                    Staff staff = new Staff();
                                    staff.setSlpCode(Integer.valueOf(currentCode));
                                    staff.setSlpName(currentName);
                                    staff.setMemo(currentMemo);
                                    staffList.add(staff);

                                    currentCode = null;
                                    currentName = null;
                                    currentMemo = null;
                                }
                            }

                            if (readyToGetName == true) {
                                currentName = data;
                                readyToGetName = false;
                                if (!TextUtils.isEmpty(currentCode) &&
                                        !TextUtils.isEmpty(currentName) &&
                                        !TextUtils.isEmpty(currentMemo)) {
                                    Staff staff = new Staff();
                                    staff.setSlpCode(Integer.valueOf(currentCode));
                                    staff.setSlpName(currentName);
                                    staff.setMemo(currentMemo);
                                    staffList.add(staff);

                                    currentCode = null;
                                    currentName = null;
                                    currentMemo = null;
                                }
                            }

                            if (readyToGetMemo == true) {
                                currentMemo = data;
                                readyToGetMemo = false;
                                if (!TextUtils.isEmpty(currentCode) &&
                                        !TextUtils.isEmpty(currentName) &&
                                        !TextUtils.isEmpty(currentMemo)) {
                                    Staff staff = new Staff();
                                    staff.setSlpCode(Integer.valueOf(currentCode));
                                    staff.setSlpName(currentName);
                                    staff.setMemo(currentMemo);
                                    staffList.add(staff);

                                    currentCode = null;
                                    currentName = null;
                                    currentMemo = null;
                                }
                            }
                        }
                        eventType = xpp.next();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    callback.onFail(e.getMessage());
                }
                callback.onSuccess(staffList);
            }

            @Override
            public void onFail(String error) {
                callback.onFail(error);
            }
        });
    }

    public void loadListProduct(String UserCode, String filter, final GetListProduct callback) {
        String link = String.format(getURL() + Config.LISTPRODUCT);

        List<Param> params = new ArrayList<>();
        Param param = new Param("UserCode", UserCode);
        params.add(param);
        param = new Param("FreeText", filter);
        params.add(param);
        mNetwork.executePost(link, params, new Network.Callback() {
            @Override
            public void onCallBack(String response) {
                XmlPullParserFactory factory = null;
                List<Product> productList = new ArrayList<>();
                try {
                    boolean readyToGetBars = false;
                    boolean readyToGetItem = false;
                    boolean readyToGetFrgn = false;
                    boolean readyToGetCode = false;
                    boolean readyToGetBuy = false;
                    boolean readyToGetInv = false;

                    String currentBars = null;
                    String currentItem = null;
                    String currentFrgn = null;
                    String currentCode = null;
                    String currentBuy = null;
                    String currentInv = null;

                    factory = XmlPullParserFactory.newInstance();
                    factory.setNamespaceAware(true);
                    XmlPullParser xpp = factory.newPullParser();

                    xpp.setInput(new StringReader(response));
                    int eventType = xpp.getEventType();
                    while (eventType != XmlPullParser.END_DOCUMENT) {
                        if (eventType == XmlPullParser.START_DOCUMENT) {
                            //TODO: do nothing
                        } else if (eventType == XmlPullParser.START_TAG) {
                            if (xpp.getName().equalsIgnoreCase("CodeBars")) {
                                readyToGetBars = true;
                            }

                            if (xpp.getName().equalsIgnoreCase("ItemName")) {
                                readyToGetItem = true;
                            }

                            if (xpp.getName().equalsIgnoreCase("FrgnName")) {
                                readyToGetFrgn = true;
                            }

                            if (xpp.getName().equalsIgnoreCase("ItemCode")) {
                                readyToGetCode = true;
                            }

                            if (xpp.getName().equalsIgnoreCase("BuyUoM")) {
                                readyToGetBuy = true;
                            }

                            if (xpp.getName().equalsIgnoreCase("InvUom")) {
                                readyToGetInv = true;
                            }

                        } else if (eventType == XmlPullParser.TEXT) {
                            String data = xpp.getText();

                            if (readyToGetBars == true) {
                                currentBars = data;
                                readyToGetBars = false;
                                if (!TextUtils.isEmpty(currentBars) && !TextUtils.isEmpty(currentItem) &&
                                        !TextUtils.isEmpty(currentFrgn) && !TextUtils.isEmpty(currentCode) &&
                                        !TextUtils.isEmpty(currentBuy) && !TextUtils.isEmpty(currentInv)) {
                                    Product product = new Product();
                                    product.setCodeBars(currentBars);
                                    product.setItemName(currentItem);
                                    product.setFrgnName(currentFrgn);
                                    product.setItemCode(currentCode);
                                    product.setBuyUoM(currentBuy);
                                    product.setInvUom(currentInv);
                                    productList.add(product);

                                    currentBars = null;
                                    currentItem = null;
                                    currentFrgn = null;
                                    currentCode = null;
                                    currentBuy = null;
                                    currentInv = null;
                                }
                            }

                            if (readyToGetItem == true) {
                                currentItem = data;
                                readyToGetItem = false;
                                if (!TextUtils.isEmpty(currentBars) && !TextUtils.isEmpty(currentItem) &&
                                        !TextUtils.isEmpty(currentFrgn) && !TextUtils.isEmpty(currentCode) &&
                                        !TextUtils.isEmpty(currentBuy) && !TextUtils.isEmpty(currentInv)) {
                                    Product product = new Product();
                                    product.setCodeBars(currentBars);
                                    product.setItemName(currentItem);
                                    product.setFrgnName(currentFrgn);
                                    product.setItemCode(currentCode);
                                    product.setBuyUoM(currentBuy);
                                    product.setInvUom(currentInv);
                                    productList.add(product);

                                    currentBars = null;
                                    currentItem = null;
                                    currentFrgn = null;
                                    currentCode = null;
                                    currentBuy = null;
                                    currentInv = null;
                                }
                            }

                            if (readyToGetFrgn == true) {
                                currentFrgn = data;
                                readyToGetFrgn = false;
                                if (!TextUtils.isEmpty(currentBars) && !TextUtils.isEmpty(currentItem) &&
                                        !TextUtils.isEmpty(currentFrgn) && !TextUtils.isEmpty(currentCode) &&
                                        !TextUtils.isEmpty(currentBuy) && !TextUtils.isEmpty(currentInv)) {
                                    Product product = new Product();
                                    product.setCodeBars(currentBars);
                                    product.setItemName(currentItem);
                                    product.setFrgnName(currentFrgn);
                                    product.setItemCode(currentCode);
                                    product.setBuyUoM(currentBuy);
                                    product.setInvUom(currentInv);
                                    productList.add(product);

                                    currentBars = null;
                                    currentItem = null;
                                    currentFrgn = null;
                                    currentCode = null;
                                    currentBuy = null;
                                    currentInv = null;
                                }
                            }

                            if (readyToGetCode == true) {
                                currentCode = data;
                                readyToGetCode = false;
                                if (!TextUtils.isEmpty(currentBars) && !TextUtils.isEmpty(currentItem) &&
                                        !TextUtils.isEmpty(currentFrgn) && !TextUtils.isEmpty(currentCode) &&
                                        !TextUtils.isEmpty(currentBuy) && !TextUtils.isEmpty(currentInv)) {
                                    Product product = new Product();
                                    product.setCodeBars(currentBars);
                                    product.setItemName(currentItem);
                                    product.setFrgnName(currentFrgn);
                                    product.setItemCode(currentCode);
                                    product.setBuyUoM(currentBuy);
                                    product.setInvUom(currentInv);
                                    productList.add(product);

                                    currentBars = null;
                                    currentItem = null;
                                    currentFrgn = null;
                                    currentCode = null;
                                    currentBuy = null;
                                    currentInv = null;
                                }
                            }

                            if (readyToGetBuy == true) {
                                currentBuy = data;
                                readyToGetBuy = false;
                                if (!TextUtils.isEmpty(currentBars) && !TextUtils.isEmpty(currentItem) &&
                                        !TextUtils.isEmpty(currentFrgn) && !TextUtils.isEmpty(currentCode) &&
                                        !TextUtils.isEmpty(currentBuy) && !TextUtils.isEmpty(currentInv)) {
                                    Product product = new Product();
                                    product.setCodeBars(currentBars);
                                    product.setItemName(currentItem);
                                    product.setFrgnName(currentFrgn);
                                    product.setItemCode(currentCode);
                                    product.setBuyUoM(currentBuy);
                                    product.setInvUom(currentInv);
                                    productList.add(product);

                                    currentBars = null;
                                    currentItem = null;
                                    currentFrgn = null;
                                    currentCode = null;
                                    currentBuy = null;
                                    currentInv = null;
                                }
                            }

                            if (readyToGetInv == true) {
                                currentInv = data;
                                readyToGetInv = false;
                                if (!TextUtils.isEmpty(currentBars) && !TextUtils.isEmpty(currentItem) &&
                                        !TextUtils.isEmpty(currentFrgn) && !TextUtils.isEmpty(currentCode) &&
                                        !TextUtils.isEmpty(currentBuy) && !TextUtils.isEmpty(currentInv)) {
                                    Product product = new Product();
                                    product.setCodeBars(currentBars);
                                    product.setItemName(currentItem);
                                    product.setFrgnName(currentFrgn);
                                    product.setItemCode(currentCode);
                                    product.setBuyUoM(currentBuy);
                                    product.setInvUom(currentInv);
                                    productList.add(product);

                                    currentBars = null;
                                    currentItem = null;
                                    currentFrgn = null;
                                    currentCode = null;
                                    currentBuy = null;
                                    currentInv = null;
                                }
                            }
                        }
                        eventType = xpp.next();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    callback.onFail(e.getMessage());
                }
                callback.onSuccess(productList);
            }

            @Override
            public void onFail(String error) {
                callback.onFail(error);
            }
        });
    }

    public void loadListCustomer(String UserCode, String route, String channel, String group, int id, String text,
                                 String workdate, final GetListCustomer callback) {
        String link = String.format(getURL() + Config.SEARCHCUSTOMER);

        List<Param> params = new ArrayList<>();
        Param param = new Param("UserCode", UserCode);
        params.add(param);
        param = new Param("Route", route);
        params.add(param);
        param = new Param("Channel", channel);
        params.add(param);
        param = new Param("Group", group);
        params.add(param);
        param = new Param("SalesManId", id + "");
        params.add(param);
        param = new Param("FreeText", text);
        params.add(param);
        param = new Param("WorkingDate", workdate);
        params.add(param);
        mNetwork.executePost(link, params, new Network.Callback() {
            @Override
            public void onCallBack(String response) {
                XmlPullParserFactory factory = null;
                List<Customer> customerList = new ArrayList<>();
                try {
                    boolean readyToGetCode = false;
                    boolean readyToGetName = false;
                    boolean readyToGetAddr = false;
                    boolean readyToGetCont = false;
                    boolean readyToGetTel = false;
                    boolean readyToGetLat = false;
                    boolean readyToGetLong = false;
                    boolean readyToGetRoute = false;
                    boolean readyToNameChan = false;
                    boolean readyToAddrGrp = false;
                    boolean readyToContLast = false;

                    String currentCode = null;
                    String currentName = null;
                    String currentAddr = null;
                    String currentCont = null;
                    String currentTel = null;
                    String currentLat = null;
                    String currentLong = null;
                    String currentRoute = null;
                    String currentChan = null;
                    String currentGrp = null;
                    String currentLast = null;

                    factory = XmlPullParserFactory.newInstance();
                    factory.setNamespaceAware(true);
                    XmlPullParser xpp = factory.newPullParser();

                    xpp.setInput(new StringReader(response));
                    int eventType = xpp.getEventType();
                    while (eventType != XmlPullParser.END_DOCUMENT) {
                        if (eventType == XmlPullParser.START_DOCUMENT) {
                            //TODO: do nothing
                        } else if (eventType == XmlPullParser.START_TAG) {
                            if (xpp.getName().equalsIgnoreCase("CardCode")) {
                                readyToGetCode = true;
                            }

                            if (xpp.getName().equalsIgnoreCase("CardName")) {
                                readyToGetName = true;
                            }

                            if (xpp.getName().equalsIgnoreCase("Address")) {
                                readyToGetAddr = true;
                            }

                            if (xpp.getName().equalsIgnoreCase("ContactPerson")) {
                                readyToGetCont = true;
                            }

                            if (xpp.getName().equalsIgnoreCase("Tel")) {
                                readyToGetTel = true;
                            }

                            if (xpp.getName().equalsIgnoreCase("LatitudeValue")) {
                                readyToGetLat = true;
                            }

                            if (xpp.getName().equalsIgnoreCase("LongitudeValue")) {
                                readyToGetLong = true;
                            }

                            if (xpp.getName().equalsIgnoreCase("Route")) {
                                readyToGetRoute = true;
                            }

                            if (xpp.getName().equalsIgnoreCase("Channel")) {
                                readyToNameChan = true;
                            }

                            if (xpp.getName().equalsIgnoreCase("CardGroup")) {
                                readyToAddrGrp = true;
                            }

                            if (xpp.getName().equalsIgnoreCase("LastVisited")) {
                                readyToContLast = true;
                            }

                        } else if (eventType == XmlPullParser.TEXT) {
                            String data = xpp.getText();

                            if (readyToGetCode == true) {
                                currentCode = data;
                                readyToGetCode = false;
                                if (!TextUtils.isEmpty(currentCode) && !TextUtils.isEmpty(currentName) &&
                                        !TextUtils.isEmpty(currentAddr) && !TextUtils.isEmpty(currentCont) &&
                                        !TextUtils.isEmpty(currentTel) && !TextUtils.isEmpty(currentLat) &&
                                        !TextUtils.isEmpty(currentLong) && !TextUtils.isEmpty(currentRoute) &&
                                        !TextUtils.isEmpty(currentChan) && !TextUtils.isEmpty(currentGrp) && !TextUtils.isEmpty(currentLast)) {
                                    Customer customer = new Customer();

                                    customer.setCardCode(currentCode);
                                    customer.setCardName(currentName);
                                    customer.setAddress(currentAddr);
                                    customer.setContactPerson(currentCont);
                                    customer.setTel(currentTel);
                                    customer.setLatitudeValue(currentLat);
                                    customer.setLongitudeValue(currentLong);
                                    customer.setRoute(currentRoute);
                                    customer.setChannel(currentChan);
                                    customer.setCustGrp(currentGrp);
                                    customer.setLastVisit(currentLast);

                                    customerList.add(customer);

                                    currentCode = null;
                                    currentName = null;
                                    currentAddr = null;
                                    currentCont = null;
                                    currentTel = null;
                                    currentLat = null;
                                    currentLong = null;
                                    currentRoute = null;
                                    currentChan = null;
                                    currentGrp = null;
                                    currentLast = null;
                                }
                            }

                            if (readyToGetName == true) {
                                currentName = data;
                                readyToGetName = false;
                                if (!TextUtils.isEmpty(currentCode) && !TextUtils.isEmpty(currentName) &&
                                        !TextUtils.isEmpty(currentAddr) && !TextUtils.isEmpty(currentCont) &&
                                        !TextUtils.isEmpty(currentTel) && !TextUtils.isEmpty(currentLat) &&
                                        !TextUtils.isEmpty(currentLong) && !TextUtils.isEmpty(currentRoute) &&
                                        !TextUtils.isEmpty(currentChan) && !TextUtils.isEmpty(currentGrp) && !TextUtils.isEmpty(currentLast)) {
                                    Customer customer = new Customer();

                                    customer.setCardCode(currentCode);
                                    customer.setCardName(currentName);
                                    customer.setAddress(currentAddr);
                                    customer.setContactPerson(currentCont);
                                    customer.setTel(currentTel);
                                    customer.setLatitudeValue(currentLat);
                                    customer.setLongitudeValue(currentLong);
                                    customer.setRoute(currentRoute);
                                    customer.setChannel(currentChan);
                                    customer.setCustGrp(currentGrp);
                                    customer.setLastVisit(currentLast);

                                    customerList.add(customer);

                                    currentCode = null;
                                    currentName = null;
                                    currentAddr = null;
                                    currentCont = null;
                                    currentTel = null;
                                    currentLat = null;
                                    currentLong = null;
                                    currentRoute = null;
                                    currentChan = null;
                                    currentGrp = null;
                                    currentLast = null;
                                }
                            }

                            if (readyToGetAddr == true) {
                                currentAddr = data;
                                readyToGetAddr = false;
                                if (!TextUtils.isEmpty(currentCode) && !TextUtils.isEmpty(currentName) &&
                                        !TextUtils.isEmpty(currentAddr) && !TextUtils.isEmpty(currentCont) &&
                                        !TextUtils.isEmpty(currentTel) && !TextUtils.isEmpty(currentLat) &&
                                        !TextUtils.isEmpty(currentLong) && !TextUtils.isEmpty(currentRoute) &&
                                        !TextUtils.isEmpty(currentChan) && !TextUtils.isEmpty(currentGrp) && !TextUtils.isEmpty(currentLast)) {
                                    Customer customer = new Customer();

                                    customer.setCardCode(currentCode);
                                    customer.setCardName(currentName);
                                    customer.setAddress(currentAddr);
                                    customer.setContactPerson(currentCont);
                                    customer.setTel(currentTel);
                                    customer.setLatitudeValue(currentLat);
                                    customer.setLongitudeValue(currentLong);
                                    customer.setRoute(currentRoute);
                                    customer.setChannel(currentChan);
                                    customer.setCustGrp(currentGrp);
                                    customer.setLastVisit(currentLast);

                                    customerList.add(customer);

                                    currentCode = null;
                                    currentName = null;
                                    currentAddr = null;
                                    currentCont = null;
                                    currentTel = null;
                                    currentLat = null;
                                    currentLong = null;
                                    currentRoute = null;
                                    currentChan = null;
                                    currentGrp = null;
                                    currentLast = null;
                                }
                            }

                            if (readyToGetCont == true) {
                                currentCont = data;
                                readyToGetCont = false;
                                if (!TextUtils.isEmpty(currentCode) && !TextUtils.isEmpty(currentName) &&
                                        !TextUtils.isEmpty(currentAddr) && !TextUtils.isEmpty(currentCont) &&
                                        !TextUtils.isEmpty(currentTel) && !TextUtils.isEmpty(currentLat) &&
                                        !TextUtils.isEmpty(currentLong) && !TextUtils.isEmpty(currentRoute) &&
                                        !TextUtils.isEmpty(currentChan) && !TextUtils.isEmpty(currentGrp) && !TextUtils.isEmpty(currentLast)) {
                                    Customer customer = new Customer();

                                    customer.setCardCode(currentCode);
                                    customer.setCardName(currentName);
                                    customer.setAddress(currentAddr);
                                    customer.setContactPerson(currentCont);
                                    customer.setTel(currentTel);
                                    customer.setLatitudeValue(currentLat);
                                    customer.setLongitudeValue(currentLong);
                                    customer.setRoute(currentRoute);
                                    customer.setChannel(currentChan);
                                    customer.setCustGrp(currentGrp);
                                    customer.setLastVisit(currentLast);

                                    customerList.add(customer);

                                    currentCode = null;
                                    currentName = null;
                                    currentAddr = null;
                                    currentCont = null;
                                    currentTel = null;
                                    currentLat = null;
                                    currentLong = null;
                                    currentRoute = null;
                                    currentChan = null;
                                    currentGrp = null;
                                    currentLast = null;
                                }
                            }

                            if (readyToGetTel == true) {
                                currentTel = data;
                                readyToGetTel = false;
                                if (!TextUtils.isEmpty(currentCode) && !TextUtils.isEmpty(currentName) &&
                                        !TextUtils.isEmpty(currentAddr) && !TextUtils.isEmpty(currentCont) &&
                                        !TextUtils.isEmpty(currentTel) && !TextUtils.isEmpty(currentLat) &&
                                        !TextUtils.isEmpty(currentLong) && !TextUtils.isEmpty(currentRoute) &&
                                        !TextUtils.isEmpty(currentChan) && !TextUtils.isEmpty(currentGrp) && !TextUtils.isEmpty(currentLast)) {
                                    Customer customer = new Customer();

                                    customer.setCardCode(currentCode);
                                    customer.setCardName(currentName);
                                    customer.setAddress(currentAddr);
                                    customer.setContactPerson(currentCont);
                                    customer.setTel(currentTel);
                                    customer.setLatitudeValue(currentLat);
                                    customer.setLongitudeValue(currentLong);
                                    customer.setRoute(currentRoute);
                                    customer.setChannel(currentChan);
                                    customer.setCustGrp(currentGrp);
                                    customer.setLastVisit(currentLast);

                                    customerList.add(customer);

                                    currentCode = null;
                                    currentName = null;
                                    currentAddr = null;
                                    currentCont = null;
                                    currentTel = null;
                                    currentLat = null;
                                    currentLong = null;
                                    currentRoute = null;
                                    currentChan = null;
                                    currentGrp = null;
                                    currentLast = null;
                                }
                            }

                            if (readyToGetLat == true) {
                                currentLat = data;
                                readyToGetLat = false;
                                if (!TextUtils.isEmpty(currentCode) && !TextUtils.isEmpty(currentName) &&
                                        !TextUtils.isEmpty(currentAddr) && !TextUtils.isEmpty(currentCont) &&
                                        !TextUtils.isEmpty(currentTel) && !TextUtils.isEmpty(currentLat) &&
                                        !TextUtils.isEmpty(currentLong) && !TextUtils.isEmpty(currentRoute) &&
                                        !TextUtils.isEmpty(currentChan) && !TextUtils.isEmpty(currentGrp) && !TextUtils.isEmpty(currentLast)) {
                                    Customer customer = new Customer();

                                    customer.setCardCode(currentCode);
                                    customer.setCardName(currentName);
                                    customer.setAddress(currentAddr);
                                    customer.setContactPerson(currentCont);
                                    customer.setTel(currentTel);
                                    customer.setLatitudeValue(currentLat);
                                    customer.setLongitudeValue(currentLong);
                                    customer.setRoute(currentRoute);
                                    customer.setChannel(currentChan);
                                    customer.setCustGrp(currentGrp);
                                    customer.setLastVisit(currentLast);

                                    customerList.add(customer);

                                    currentCode = null;
                                    currentName = null;
                                    currentAddr = null;
                                    currentCont = null;
                                    currentTel = null;
                                    currentLat = null;
                                    currentLong = null;
                                    currentRoute = null;
                                    currentChan = null;
                                    currentGrp = null;
                                    currentLast = null;
                                }
                            }

                            if (readyToGetLong == true) {
                                currentLong = data;
                                readyToGetLong = false;
                                if (!TextUtils.isEmpty(currentCode) && !TextUtils.isEmpty(currentName) &&
                                        !TextUtils.isEmpty(currentAddr) && !TextUtils.isEmpty(currentCont) &&
                                        !TextUtils.isEmpty(currentTel) && !TextUtils.isEmpty(currentLat) &&
                                        !TextUtils.isEmpty(currentLong) && !TextUtils.isEmpty(currentRoute) &&
                                        !TextUtils.isEmpty(currentChan) && !TextUtils.isEmpty(currentGrp) && !TextUtils.isEmpty(currentLast)) {
                                    Customer customer = new Customer();

                                    customer.setCardCode(currentCode);
                                    customer.setCardName(currentName);
                                    customer.setAddress(currentAddr);
                                    customer.setContactPerson(currentCont);
                                    customer.setTel(currentTel);
                                    customer.setLatitudeValue(currentLat);
                                    customer.setLongitudeValue(currentLong);
                                    customer.setRoute(currentRoute);
                                    customer.setChannel(currentChan);
                                    customer.setCustGrp(currentGrp);
                                    customer.setLastVisit(currentLast);

                                    customerList.add(customer);

                                    currentCode = null;
                                    currentName = null;
                                    currentAddr = null;
                                    currentCont = null;
                                    currentTel = null;
                                    currentLat = null;
                                    currentLong = null;
                                    currentRoute = null;
                                    currentChan = null;
                                    currentGrp = null;
                                    currentLast = null;
                                }
                            }

                            if (readyToGetRoute == true) {
                                currentRoute = data;
                                readyToGetRoute = false;
                                if (!TextUtils.isEmpty(currentCode) && !TextUtils.isEmpty(currentName) &&
                                        !TextUtils.isEmpty(currentAddr) && !TextUtils.isEmpty(currentCont) &&
                                        !TextUtils.isEmpty(currentTel) && !TextUtils.isEmpty(currentLat) &&
                                        !TextUtils.isEmpty(currentLong) && !TextUtils.isEmpty(currentRoute) &&
                                        !TextUtils.isEmpty(currentChan) && !TextUtils.isEmpty(currentGrp) && !TextUtils.isEmpty(currentLast)) {
                                    Customer customer = new Customer();

                                    customer.setCardCode(currentCode);
                                    customer.setCardName(currentName);
                                    customer.setAddress(currentAddr);
                                    customer.setContactPerson(currentCont);
                                    customer.setTel(currentTel);
                                    customer.setLatitudeValue(currentLat);
                                    customer.setLongitudeValue(currentLong);
                                    customer.setRoute(currentRoute);
                                    customer.setChannel(currentChan);
                                    customer.setCustGrp(currentGrp);
                                    customer.setLastVisit(currentLast);

                                    customerList.add(customer);

                                    currentCode = null;
                                    currentName = null;
                                    currentAddr = null;
                                    currentCont = null;
                                    currentTel = null;
                                    currentLat = null;
                                    currentLong = null;
                                    currentRoute = null;
                                    currentChan = null;
                                    currentGrp = null;
                                    currentLast = null;
                                }
                            }

                            if (readyToNameChan == true) {
                                currentChan = data;
                                readyToNameChan = false;
                                if (!TextUtils.isEmpty(currentCode) && !TextUtils.isEmpty(currentName) &&
                                        !TextUtils.isEmpty(currentAddr) && !TextUtils.isEmpty(currentCont) &&
                                        !TextUtils.isEmpty(currentTel) && !TextUtils.isEmpty(currentLat) &&
                                        !TextUtils.isEmpty(currentLong) && !TextUtils.isEmpty(currentRoute) &&
                                        !TextUtils.isEmpty(currentChan) && !TextUtils.isEmpty(currentGrp) && !TextUtils.isEmpty(currentLast)) {
                                    Customer customer = new Customer();

                                    customer.setCardCode(currentCode);
                                    customer.setCardName(currentName);
                                    customer.setAddress(currentAddr);
                                    customer.setContactPerson(currentCont);
                                    customer.setTel(currentTel);
                                    customer.setLatitudeValue(currentLat);
                                    customer.setLongitudeValue(currentLong);
                                    customer.setRoute(currentRoute);
                                    customer.setChannel(currentChan);
                                    customer.setCustGrp(currentGrp);
                                    customer.setLastVisit(currentLast);

                                    customerList.add(customer);

                                    currentCode = null;
                                    currentName = null;
                                    currentAddr = null;
                                    currentCont = null;
                                    currentTel = null;
                                    currentLat = null;
                                    currentLong = null;
                                    currentRoute = null;
                                    currentChan = null;
                                    currentGrp = null;
                                    currentLast = null;
                                }
                            }

                            if (readyToAddrGrp == true) {
                                currentGrp = data;
                                readyToAddrGrp = false;
                                if (!TextUtils.isEmpty(currentCode) && !TextUtils.isEmpty(currentName) &&
                                        !TextUtils.isEmpty(currentAddr) && !TextUtils.isEmpty(currentCont) &&
                                        !TextUtils.isEmpty(currentTel) && !TextUtils.isEmpty(currentLat) &&
                                        !TextUtils.isEmpty(currentLong) && !TextUtils.isEmpty(currentRoute) &&
                                        !TextUtils.isEmpty(currentChan) && !TextUtils.isEmpty(currentGrp) && !TextUtils.isEmpty(currentLast)) {
                                    Customer customer = new Customer();

                                    customer.setCardCode(currentCode);
                                    customer.setCardName(currentName);
                                    customer.setAddress(currentAddr);
                                    customer.setContactPerson(currentCont);
                                    customer.setTel(currentTel);
                                    customer.setLatitudeValue(currentLat);
                                    customer.setLongitudeValue(currentLong);
                                    customer.setRoute(currentRoute);
                                    customer.setChannel(currentChan);
                                    customer.setCustGrp(currentGrp);
                                    customer.setLastVisit(currentLast);

                                    customerList.add(customer);

                                    currentCode = null;
                                    currentName = null;
                                    currentAddr = null;
                                    currentCont = null;
                                    currentTel = null;
                                    currentLat = null;
                                    currentLong = null;
                                    currentRoute = null;
                                    currentChan = null;
                                    currentGrp = null;
                                    currentLast = null;
                                }
                            }

                            if (readyToContLast == true) {
                                currentLast = data;
                                readyToContLast = false;
                                if (!TextUtils.isEmpty(currentCode) && !TextUtils.isEmpty(currentName) &&
                                        !TextUtils.isEmpty(currentAddr) && !TextUtils.isEmpty(currentCont) &&
                                        !TextUtils.isEmpty(currentTel) && !TextUtils.isEmpty(currentLat) &&
                                        !TextUtils.isEmpty(currentLong) && !TextUtils.isEmpty(currentRoute) &&
                                        !TextUtils.isEmpty(currentChan) && !TextUtils.isEmpty(currentGrp) && !TextUtils.isEmpty(currentLast)) {
                                    Customer customer = new Customer();

                                    customer.setCardCode(currentCode);
                                    customer.setCardName(currentName);
                                    customer.setAddress(currentAddr);
                                    customer.setContactPerson(currentCont);
                                    customer.setTel(currentTel);
                                    customer.setLatitudeValue(currentLat);
                                    customer.setLongitudeValue(currentLong);
                                    customer.setRoute(currentRoute);
                                    customer.setChannel(currentChan);
                                    customer.setCustGrp(currentGrp);
                                    customer.setLastVisit(currentLast);

                                    customerList.add(customer);

                                    currentCode = null;
                                    currentName = null;
                                    currentAddr = null;
                                    currentCont = null;
                                    currentTel = null;
                                    currentLat = null;
                                    currentLong = null;
                                    currentRoute = null;
                                    currentChan = null;
                                    currentGrp = null;
                                    currentLast = null;
                                }
                            }
                        }
                        eventType = xpp.next();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    callback.onFail(e.getMessage());
                }
                callback.onSuccess(customerList);
            }

            @Override
            public void onFail(String error) {
                callback.onFail(error);
            }
        });
    }

    public void loadListCustomerTravel(String UserCode, final GetListCustomerTravel callback) {
        String link = String.format(getURL() + Config.LISTCUSTOMER);

        List<Param> params = new ArrayList<>();
        Param param = new Param("UserCode", UserCode);
        params.add(param);
        mNetwork.executePost(link, params, new Network.Callback() {
            @Override
            public void onCallBack(String response) {
                XmlPullParserFactory factory = null;
                List<Customer> customerList = new ArrayList<>();
                try {
                    boolean readyToGetCode = false;
                    boolean readyToGetName = false;
                    boolean readyToGetAddr = false;
                    boolean readyToGetCont = false;
                    boolean readyToGetTel = false;
                    boolean readyToGetLat = false;
                    boolean readyToGetLong = false;
                    boolean readyToLatLong = false;
                    boolean readyToRoute = false;
                    boolean readyToChannel = false;
                    boolean readyToCardGroup = false;

                    String currentCode = null;
                    String currentName = null;
                    String currentAddr = null;
                    String currentCont = null;
                    String currentTel = null;
                    String currentLat = null;
                    String currentLong = null;
                    String currentLatLong = null;
                    String currentRoute = null;
                    String currentChannel = null;
                    String currentCardGroup = null;

                    factory = XmlPullParserFactory.newInstance();
                    factory.setNamespaceAware(true);
                    XmlPullParser xpp = factory.newPullParser();

                    xpp.setInput(new StringReader(response));
                    int eventType = xpp.getEventType();
                    while (eventType != XmlPullParser.END_DOCUMENT) {
                        if (eventType == XmlPullParser.START_DOCUMENT) {
                            //TODO: do nothing
                        } else if (eventType == XmlPullParser.START_TAG) {
                            if (xpp.getName().equalsIgnoreCase("CardCode")) {
                                readyToGetCode = true;
                            }

                            if (xpp.getName().equalsIgnoreCase("CardName")) {
                                readyToGetName = true;
                            }

                            if (xpp.getName().equalsIgnoreCase("Address")) {
                                readyToGetAddr = true;
                            }

                            if (xpp.getName().equalsIgnoreCase("ContactPerson")) {
                                readyToGetCont = true;
                            }

                            if (xpp.getName().equalsIgnoreCase("Tel")) {
                                readyToGetTel = true;
                            }

                            if (xpp.getName().equalsIgnoreCase("LatitudeValue")) {
                                readyToGetLat = true;
                            }

                            if (xpp.getName().equalsIgnoreCase("LongitudeValue")) {
                                readyToGetLong = true;
                            }

                            if (xpp.getName().equalsIgnoreCase("LatLongString")) {
                                readyToLatLong = true;
                            }

                            if (xpp.getName().equalsIgnoreCase("Route")) {
                                readyToRoute = true;
                            }

                            if (xpp.getName().equalsIgnoreCase("Channel")) {
                                readyToChannel = true;
                            }

                            if (xpp.getName().equalsIgnoreCase("CardGroup")) {
                                readyToCardGroup = true;
                            }

                        } else if (eventType == XmlPullParser.TEXT) {
                            String data = xpp.getText();

                            if (readyToGetCode) {
                                currentCode = data;
                                readyToGetCode = false;
                                if (!TextUtils.isEmpty(currentCode) && !TextUtils.isEmpty(currentName) &&
                                        !TextUtils.isEmpty(currentAddr) && !TextUtils.isEmpty(currentCont) &&
                                        !TextUtils.isEmpty(currentTel) && !TextUtils.isEmpty(currentLat) &&
                                        !TextUtils.isEmpty(currentLong) && !TextUtils.isEmpty(currentLatLong) &&
                                        !TextUtils.isEmpty(currentRoute) && !TextUtils.isEmpty(currentChannel) &&
                                        !TextUtils.isEmpty(currentCardGroup)) {

                                    Customer customer = new Customer();
                                    customer.setCardCode(currentCode);
                                    customer.setCardName(currentName);
                                    customer.setAddress(currentAddr);
                                    customer.setTel(currentTel);
                                    customer.setContactPerson(currentCont);
                                    customer.setLatitudeValue(currentLat);
                                    customer.setLongitudeValue(currentLong);
                                    customer.setLatLongString(currentLatLong);
                                    customer.setRoute(currentRoute);
                                    customer.setChannel(currentChannel);
                                    customer.setCustGrp(currentCardGroup);

                                    customerList.add(customer);

                                    currentCode = null;
                                    currentName = null;
                                    currentAddr = null;
                                    currentCont = null;
                                    currentTel = null;
                                    currentLat = null;
                                    currentLong = null;
                                    currentLatLong = null;
                                    currentRoute = null;
                                    currentChannel = null;
                                    currentCardGroup = null;
                                }
                            }

                            if (readyToGetName) {
                                currentName = data;
                                readyToGetName = false;
                                if (!TextUtils.isEmpty(currentCode) && !TextUtils.isEmpty(currentName) &&
                                        !TextUtils.isEmpty(currentAddr) && !TextUtils.isEmpty(currentCont) &&
                                        !TextUtils.isEmpty(currentTel) && !TextUtils.isEmpty(currentLat) &&
                                        !TextUtils.isEmpty(currentLong) && !TextUtils.isEmpty(currentLatLong) &&
                                        !TextUtils.isEmpty(currentRoute) && !TextUtils.isEmpty(currentChannel) &&
                                        !TextUtils.isEmpty(currentCardGroup)) {

                                    Customer customer = new Customer();
                                    customer.setCardCode(currentCode);
                                    customer.setCardName(currentName);
                                    customer.setAddress(currentAddr);
                                    customer.setTel(currentTel);
                                    customer.setContactPerson(currentCont);
                                    customer.setLatitudeValue(currentLat);
                                    customer.setLongitudeValue(currentLong);
                                    customer.setLatLongString(currentLatLong);
                                    customer.setRoute(currentRoute);
                                    customer.setChannel(currentChannel);
                                    customer.setCustGrp(currentCardGroup);

                                    customerList.add(customer);

                                    currentCode = null;
                                    currentName = null;
                                    currentAddr = null;
                                    currentCont = null;
                                    currentTel = null;
                                    currentLat = null;
                                    currentLong = null;
                                    currentLatLong = null;
                                    currentRoute = null;
                                    currentChannel = null;
                                    currentCardGroup = null;
                                }
                            }

                            if (readyToGetAddr) {
                                currentAddr = data;
                                readyToGetAddr = false;
                                if (!TextUtils.isEmpty(currentCode) && !TextUtils.isEmpty(currentName) &&
                                        !TextUtils.isEmpty(currentAddr) && !TextUtils.isEmpty(currentCont) &&
                                        !TextUtils.isEmpty(currentTel) && !TextUtils.isEmpty(currentLat) &&
                                        !TextUtils.isEmpty(currentLong) && !TextUtils.isEmpty(currentLatLong) &&
                                        !TextUtils.isEmpty(currentRoute) && !TextUtils.isEmpty(currentChannel) &&
                                        !TextUtils.isEmpty(currentCardGroup)) {

                                    Customer customer = new Customer();
                                    customer.setCardCode(currentCode);
                                    customer.setCardName(currentName);
                                    customer.setAddress(currentAddr);
                                    customer.setTel(currentTel);
                                    customer.setContactPerson(currentCont);
                                    customer.setLatitudeValue(currentLat);
                                    customer.setLongitudeValue(currentLong);
                                    customer.setLatLongString(currentLatLong);
                                    customer.setRoute(currentRoute);
                                    customer.setChannel(currentChannel);
                                    customer.setCustGrp(currentCardGroup);

                                    customerList.add(customer);

                                    currentCode = null;
                                    currentName = null;
                                    currentAddr = null;
                                    currentCont = null;
                                    currentTel = null;
                                    currentLat = null;
                                    currentLong = null;
                                    currentLatLong = null;
                                    currentRoute = null;
                                    currentChannel = null;
                                    currentCardGroup = null;
                                }
                            }

                            if (readyToGetCont) {
                                currentCont = data;
                                readyToGetCont = false;
                                if (!TextUtils.isEmpty(currentCode) && !TextUtils.isEmpty(currentName) &&
                                        !TextUtils.isEmpty(currentAddr) && !TextUtils.isEmpty(currentCont) &&
                                        !TextUtils.isEmpty(currentTel) && !TextUtils.isEmpty(currentLat) &&
                                        !TextUtils.isEmpty(currentLong) && !TextUtils.isEmpty(currentLatLong) &&
                                        !TextUtils.isEmpty(currentRoute) && !TextUtils.isEmpty(currentChannel) &&
                                        !TextUtils.isEmpty(currentCardGroup)) {

                                    Customer customer = new Customer();
                                    customer.setCardCode(currentCode);
                                    customer.setCardName(currentName);
                                    customer.setAddress(currentAddr);
                                    customer.setTel(currentTel);
                                    customer.setContactPerson(currentCont);
                                    customer.setLatitudeValue(currentLat);
                                    customer.setLongitudeValue(currentLong);
                                    customer.setLatLongString(currentLatLong);
                                    customer.setRoute(currentRoute);
                                    customer.setChannel(currentChannel);
                                    customer.setCustGrp(currentCardGroup);

                                    customerList.add(customer);

                                    currentCode = null;
                                    currentName = null;
                                    currentAddr = null;
                                    currentCont = null;
                                    currentTel = null;
                                    currentLat = null;
                                    currentLong = null;
                                    currentLatLong = null;
                                    currentRoute = null;
                                    currentChannel = null;
                                    currentCardGroup = null;
                                }
                            }

                            if (readyToGetTel) {
                                currentTel = data;
                                readyToGetTel = false;
                                if (!TextUtils.isEmpty(currentCode) && !TextUtils.isEmpty(currentName) &&
                                        !TextUtils.isEmpty(currentAddr) && !TextUtils.isEmpty(currentCont) &&
                                        !TextUtils.isEmpty(currentTel) && !TextUtils.isEmpty(currentLat) &&
                                        !TextUtils.isEmpty(currentLong) && !TextUtils.isEmpty(currentLatLong) &&
                                        !TextUtils.isEmpty(currentRoute) && !TextUtils.isEmpty(currentChannel) &&
                                        !TextUtils.isEmpty(currentCardGroup)) {

                                    Customer customer = new Customer();
                                    customer.setCardCode(currentCode);
                                    customer.setCardName(currentName);
                                    customer.setAddress(currentAddr);
                                    customer.setTel(currentTel);
                                    customer.setContactPerson(currentCont);
                                    customer.setLatitudeValue(currentLat);
                                    customer.setLongitudeValue(currentLong);
                                    customer.setLatLongString(currentLatLong);
                                    customer.setRoute(currentRoute);
                                    customer.setChannel(currentChannel);
                                    customer.setCustGrp(currentCardGroup);

                                    customerList.add(customer);

                                    currentCode = null;
                                    currentName = null;
                                    currentAddr = null;
                                    currentCont = null;
                                    currentTel = null;
                                    currentLat = null;
                                    currentLong = null;
                                    currentLatLong = null;
                                    currentRoute = null;
                                    currentChannel = null;
                                    currentCardGroup = null;
                                }
                            }

                            if (readyToGetLat) {
                                currentLat = data;
                                readyToGetLat = false;
                                if (!TextUtils.isEmpty(currentCode) && !TextUtils.isEmpty(currentName) &&
                                        !TextUtils.isEmpty(currentAddr) && !TextUtils.isEmpty(currentCont) &&
                                        !TextUtils.isEmpty(currentTel) && !TextUtils.isEmpty(currentLat) &&
                                        !TextUtils.isEmpty(currentLong) && !TextUtils.isEmpty(currentLatLong) &&
                                        !TextUtils.isEmpty(currentRoute) && !TextUtils.isEmpty(currentChannel) &&
                                        !TextUtils.isEmpty(currentCardGroup)) {

                                    Customer customer = new Customer();
                                    customer.setCardCode(currentCode);
                                    customer.setCardName(currentName);
                                    customer.setAddress(currentAddr);
                                    customer.setTel(currentTel);
                                    customer.setContactPerson(currentCont);
                                    customer.setLatitudeValue(currentLat);
                                    customer.setLongitudeValue(currentLong);
                                    customer.setLatLongString(currentLatLong);
                                    customer.setRoute(currentRoute);
                                    customer.setChannel(currentChannel);
                                    customer.setCustGrp(currentCardGroup);

                                    customerList.add(customer);

                                    currentCode = null;
                                    currentName = null;
                                    currentAddr = null;
                                    currentCont = null;
                                    currentTel = null;
                                    currentLat = null;
                                    currentLong = null;
                                    currentLatLong = null;
                                    currentRoute = null;
                                    currentChannel = null;
                                    currentCardGroup = null;
                                }
                            }

                            if (readyToGetLong) {
                                currentLong = data;
                                readyToGetLong = false;
                                if (!TextUtils.isEmpty(currentCode) && !TextUtils.isEmpty(currentName) &&
                                        !TextUtils.isEmpty(currentAddr) && !TextUtils.isEmpty(currentCont) &&
                                        !TextUtils.isEmpty(currentTel) && !TextUtils.isEmpty(currentLat) &&
                                        !TextUtils.isEmpty(currentLong) && !TextUtils.isEmpty(currentLatLong) &&
                                        !TextUtils.isEmpty(currentRoute) && !TextUtils.isEmpty(currentChannel) &&
                                        !TextUtils.isEmpty(currentCardGroup)) {

                                    Customer customer = new Customer();
                                    customer.setCardCode(currentCode);
                                    customer.setCardName(currentName);
                                    customer.setAddress(currentAddr);
                                    customer.setTel(currentTel);
                                    customer.setContactPerson(currentCont);
                                    customer.setLatitudeValue(currentLat);
                                    customer.setLongitudeValue(currentLong);
                                    customer.setLatLongString(currentLatLong);
                                    customer.setRoute(currentRoute);
                                    customer.setChannel(currentChannel);
                                    customer.setCustGrp(currentCardGroup);

                                    customerList.add(customer);

                                    currentCode = null;
                                    currentName = null;
                                    currentAddr = null;
                                    currentCont = null;
                                    currentTel = null;
                                    currentLat = null;
                                    currentLong = null;
                                    currentLatLong = null;
                                    currentRoute = null;
                                    currentChannel = null;
                                    currentCardGroup = null;
                                }
                            }

                            if (readyToLatLong) {
                                currentLatLong = data;
                                readyToLatLong = false;
                                if (!TextUtils.isEmpty(currentCode) && !TextUtils.isEmpty(currentName) &&
                                        !TextUtils.isEmpty(currentAddr) && !TextUtils.isEmpty(currentCont) &&
                                        !TextUtils.isEmpty(currentTel) && !TextUtils.isEmpty(currentLat) &&
                                        !TextUtils.isEmpty(currentLong) && !TextUtils.isEmpty(currentLatLong) &&
                                        !TextUtils.isEmpty(currentRoute) && !TextUtils.isEmpty(currentChannel) &&
                                        !TextUtils.isEmpty(currentCardGroup)) {

                                    Customer customer = new Customer();
                                    customer.setCardCode(currentCode);
                                    customer.setCardName(currentName);
                                    customer.setAddress(currentAddr);
                                    customer.setTel(currentTel);
                                    customer.setContactPerson(currentCont);
                                    customer.setLatitudeValue(currentLat);
                                    customer.setLongitudeValue(currentLong);
                                    customer.setLatLongString(currentLatLong);
                                    customer.setRoute(currentRoute);
                                    customer.setChannel(currentChannel);
                                    customer.setCustGrp(currentCardGroup);

                                    customerList.add(customer);

                                    currentCode = null;
                                    currentName = null;
                                    currentAddr = null;
                                    currentCont = null;
                                    currentTel = null;
                                    currentLat = null;
                                    currentLong = null;
                                    currentLatLong = null;
                                    currentRoute = null;
                                    currentChannel = null;
                                    currentCardGroup = null;
                                }
                            }

                            if (readyToRoute) {
                                currentRoute = data;
                                readyToRoute = false;
                                if (!TextUtils.isEmpty(currentCode) && !TextUtils.isEmpty(currentName) &&
                                        !TextUtils.isEmpty(currentAddr) && !TextUtils.isEmpty(currentCont) &&
                                        !TextUtils.isEmpty(currentTel) && !TextUtils.isEmpty(currentLat) &&
                                        !TextUtils.isEmpty(currentLong) && !TextUtils.isEmpty(currentLatLong) &&
                                        !TextUtils.isEmpty(currentRoute) && !TextUtils.isEmpty(currentChannel) &&
                                        !TextUtils.isEmpty(currentCardGroup)) {

                                    Customer customer = new Customer();
                                    customer.setCardCode(currentCode);
                                    customer.setCardName(currentName);
                                    customer.setAddress(currentAddr);
                                    customer.setTel(currentTel);
                                    customer.setContactPerson(currentCont);
                                    customer.setLatitudeValue(currentLat);
                                    customer.setLongitudeValue(currentLong);
                                    customer.setLatLongString(currentLatLong);
                                    customer.setRoute(currentRoute);
                                    customer.setChannel(currentChannel);
                                    customer.setCustGrp(currentCardGroup);

                                    customerList.add(customer);

                                    currentCode = null;
                                    currentName = null;
                                    currentAddr = null;
                                    currentCont = null;
                                    currentTel = null;
                                    currentLat = null;
                                    currentLong = null;
                                    currentLatLong = null;
                                    currentRoute = null;
                                    currentChannel = null;
                                    currentCardGroup = null;
                                }
                            }

                            if (readyToChannel) {
                                currentChannel = data;
                                readyToChannel = false;
                                if (!TextUtils.isEmpty(currentCode) && !TextUtils.isEmpty(currentName) &&
                                        !TextUtils.isEmpty(currentAddr) && !TextUtils.isEmpty(currentCont) &&
                                        !TextUtils.isEmpty(currentTel) && !TextUtils.isEmpty(currentLat) &&
                                        !TextUtils.isEmpty(currentLong) && !TextUtils.isEmpty(currentLatLong) &&
                                        !TextUtils.isEmpty(currentRoute) && !TextUtils.isEmpty(currentChannel) &&
                                        !TextUtils.isEmpty(currentCardGroup)) {

                                    Customer customer = new Customer();
                                    customer.setCardCode(currentCode);
                                    customer.setCardName(currentName);
                                    customer.setAddress(currentAddr);
                                    customer.setTel(currentTel);
                                    customer.setContactPerson(currentCont);
                                    customer.setLatitudeValue(currentLat);
                                    customer.setLongitudeValue(currentLong);
                                    customer.setLatLongString(currentLatLong);
                                    customer.setRoute(currentRoute);
                                    customer.setChannel(currentChannel);
                                    customer.setCustGrp(currentCardGroup);

                                    customerList.add(customer);

                                    currentCode = null;
                                    currentName = null;
                                    currentAddr = null;
                                    currentCont = null;
                                    currentTel = null;
                                    currentLat = null;
                                    currentLong = null;
                                    currentLatLong = null;
                                    currentRoute = null;
                                    currentChannel = null;
                                    currentCardGroup = null;
                                }
                            }

                            if (readyToCardGroup) {
                                currentCardGroup = data;
                                readyToCardGroup = false;
                                if (!TextUtils.isEmpty(currentCode) && !TextUtils.isEmpty(currentName) &&
                                        !TextUtils.isEmpty(currentAddr) && !TextUtils.isEmpty(currentCont) &&
                                        !TextUtils.isEmpty(currentTel) && !TextUtils.isEmpty(currentLat) &&
                                        !TextUtils.isEmpty(currentLong) && !TextUtils.isEmpty(currentLatLong) &&
                                        !TextUtils.isEmpty(currentRoute) && !TextUtils.isEmpty(currentChannel) &&
                                        !TextUtils.isEmpty(currentCardGroup)) {

                                    Customer customer = new Customer();
                                    customer.setCardCode(currentCode);
                                    customer.setCardName(currentName);
                                    customer.setAddress(currentAddr);
                                    customer.setTel(currentTel);
                                    customer.setContactPerson(currentCont);
                                    customer.setLatitudeValue(currentLat);
                                    customer.setLongitudeValue(currentLong);
                                    customer.setLatLongString(currentLatLong);
                                    customer.setRoute(currentRoute);
                                    customer.setChannel(currentChannel);
                                    customer.setCustGrp(currentCardGroup);

                                    customerList.add(customer);

                                    currentCode = null;
                                    currentName = null;
                                    currentAddr = null;
                                    currentCont = null;
                                    currentTel = null;
                                    currentLat = null;
                                    currentLong = null;
                                    currentLatLong = null;
                                    currentRoute = null;
                                    currentChannel = null;
                                    currentCardGroup = null;
                                }
                            }
                        }
                        eventType = xpp.next();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    callback.onFail(e.getMessage());
                }
                callback.onSuccess(customerList);
            }

            @Override
            public void onFail(String error) {
                callback.onFail(error);
            }
        });
    }

    public void loadListStaffMonitor(String UserCode, final GetListStaffMonitor callback) {
        String link = String.format(getURL() + Config.LISTSTAFFMONITOR);

        List<Param> params = new ArrayList<>();
        Param param = new Param("UserCode", UserCode);
        params.add(param);
        param = new Param("MonitorDate", CommonUtils.getYear() + "-" + CommonUtils.getMonth() + "-" + CommonUtils.getDate());
        params.add(param);
        mNetwork.executePost(link, params, new Network.Callback() {
            @Override
            public void onCallBack(String response) {
                XmlPullParserFactory factory = null;
                List<StaffMonitor> staffMonitors = new ArrayList<>();
                try {
                    boolean MyId = false;
                    boolean EmpId = false;
                    boolean EmpFullName = false;
                    boolean PicLink = false;
                    boolean MyDate = false;
                    boolean CheckInPlan = false;
                    boolean CheckInFinish = false;
                    boolean NoOrder = false;
                    boolean LastCustName = false;
                    boolean LastCustAdd = false;
                    boolean NoofMetter = false;

                    String currentMyId = null;
                    String currentEmpId = null;
                    String currentEmpFullName = null;
                    String currentPicLink = null;
                    String currentMyDate = null;
                    String currentCheckInPlan = null;
                    String currentCheckInFinish = null;
                    String currentNoOrder = null;
                    String currentLastCustName = null;
                    String currentLastCustAdd = null;
                    String currentNoofMetter = null;


                    factory = XmlPullParserFactory.newInstance();
                    factory.setNamespaceAware(true);
                    XmlPullParser xpp = factory.newPullParser();

                    xpp.setInput(new StringReader(response));
                    int eventType = xpp.getEventType();
                    while (eventType != XmlPullParser.END_DOCUMENT) {
                        if (eventType == XmlPullParser.START_DOCUMENT) {
                            //TODO: do nothing
                        } else if (eventType == XmlPullParser.START_TAG) {
                            if (xpp.getName().equalsIgnoreCase("MyId")) {
                                MyId = true;
                            }

                            if (xpp.getName().equalsIgnoreCase("EmpId")) {
                                EmpId = true;
                            }

                            if (xpp.getName().equalsIgnoreCase("EmpFullName")) {
                                EmpFullName = true;
                            }

                            if (xpp.getName().equalsIgnoreCase("PicLink")) {
                                PicLink = true;
                            }

                            if (xpp.getName().equalsIgnoreCase("MyDate")) {
                                MyDate = true;
                            }

                            if (xpp.getName().equalsIgnoreCase("CheckInPlan")) {
                                CheckInPlan = true;
                            }

                            if (xpp.getName().equalsIgnoreCase("CheckInFinish")) {
                                CheckInFinish = true;
                            }

                            if (xpp.getName().equalsIgnoreCase("NoOrder")) {
                                NoOrder = true;
                            }

                            if (xpp.getName().equalsIgnoreCase("LastCustName")) {
                                LastCustName = true;
                            }

                            if (xpp.getName().equalsIgnoreCase("LastCustAdd")) {
                                LastCustAdd = true;
                            }

                            if (xpp.getName().equalsIgnoreCase("NoofMetter")) {
                                NoofMetter = true;
                            }

                        } else if (eventType == XmlPullParser.TEXT) {
                            String data = xpp.getText();

                            if (MyId == true) {
                                currentMyId = data;
                                MyId = false;
                                if (currentMyId != null && !currentMyId.isEmpty() &&
                                        currentEmpId != null && !currentEmpId.isEmpty() &&
                                        currentEmpFullName != null && !currentEmpFullName.isEmpty() &&
                                        currentPicLink != null && !currentPicLink.isEmpty() &&
                                        currentMyDate != null && !currentMyDate.isEmpty() &&
                                        currentCheckInPlan != null && !currentCheckInPlan.isEmpty() &&
                                        currentCheckInFinish != null && !currentCheckInFinish.isEmpty() &&
                                        currentNoOrder != null && !currentNoOrder.isEmpty() &&
                                        currentLastCustName != null && !currentLastCustName.isEmpty() &&
                                        currentLastCustAdd != null && !currentLastCustAdd.isEmpty()) {

                                    StaffMonitor staffMonitor = new StaffMonitor();
                                    staffMonitor.setMyId(Long.parseLong(currentMyId));
                                    staffMonitor.setEmpId(Integer.parseInt(currentEmpId));
                                    staffMonitor.setEmpFullName(currentEmpFullName);
                                    staffMonitor.setPicLink(currentPicLink);
                                    staffMonitor.setMyDate(currentMyDate);
                                    staffMonitor.setCheckInPlan(Short.parseShort(currentCheckInPlan));
                                    staffMonitor.setCheckInFinish(Short.parseShort(currentCheckInFinish));
                                    staffMonitor.setNoOrder(Short.parseShort(currentNoOrder));
                                    staffMonitors.add(staffMonitor);

                                    currentMyId = null;
                                    currentEmpId = null;
                                    currentEmpFullName = null;
                                    currentPicLink = null;
                                    currentMyDate = null;
                                    currentCheckInPlan = null;
                                    currentCheckInFinish = null;
                                    currentNoOrder = null;
                                    currentLastCustName = null;
                                    currentLastCustAdd = null;
                                    currentNoofMetter = null;
                                }
                            }
                            ///
                            if (EmpId == true) {
                                currentEmpId = data;
                                EmpId = false;
                                if (currentMyId != null && !currentMyId.isEmpty() &&
                                        currentEmpId != null && !currentEmpId.isEmpty() &&
                                        currentEmpFullName != null && !currentEmpFullName.isEmpty() &&
                                        currentPicLink != null && !currentPicLink.isEmpty() &&
                                        currentMyDate != null && !currentMyDate.isEmpty() &&
                                        currentCheckInPlan != null && !currentCheckInPlan.isEmpty() &&
                                        currentCheckInFinish != null && !currentCheckInFinish.isEmpty() &&
                                        currentNoOrder != null && !currentNoOrder.isEmpty() &&
                                        currentLastCustName != null && !currentLastCustName.isEmpty() &&
                                        currentLastCustAdd != null && !currentLastCustAdd.isEmpty()) {

                                    StaffMonitor staffMonitor = new StaffMonitor();
                                    staffMonitor.setMyId(Long.parseLong(currentMyId));
                                    staffMonitor.setEmpId(Integer.parseInt(currentEmpId));
                                    staffMonitor.setEmpFullName(currentEmpFullName);
                                    staffMonitor.setPicLink(currentPicLink);
                                    staffMonitor.setMyDate(currentMyDate);
                                    staffMonitor.setCheckInPlan(Short.parseShort(currentCheckInPlan));
                                    staffMonitor.setCheckInFinish(Short.parseShort(currentCheckInFinish));
                                    staffMonitor.setNoOrder(Short.parseShort(currentNoOrder));
                                    staffMonitors.add(staffMonitor);

                                    currentMyId = null;
                                    currentEmpId = null;
                                    currentEmpFullName = null;
                                    currentPicLink = null;
                                    currentMyDate = null;
                                    currentCheckInPlan = null;
                                    currentCheckInFinish = null;
                                    currentNoOrder = null;
                                    currentLastCustName = null;
                                    currentLastCustAdd = null;
                                    currentNoofMetter = null;
                                }
                            }
                            ///
                            if (EmpFullName == true) {
                                currentEmpFullName = data;
                                EmpFullName = false;
                                if (currentMyId != null && !currentMyId.isEmpty() &&
                                        currentEmpId != null && !currentEmpId.isEmpty() &&
                                        currentEmpFullName != null && !currentEmpFullName.isEmpty() &&
                                        currentPicLink != null && !currentPicLink.isEmpty() &&
                                        currentMyDate != null && !currentMyDate.isEmpty() &&
                                        currentCheckInPlan != null && !currentCheckInPlan.isEmpty() &&
                                        currentCheckInFinish != null && !currentCheckInFinish.isEmpty() &&
                                        currentNoOrder != null && !currentNoOrder.isEmpty() &&
                                        currentLastCustName != null && !currentLastCustName.isEmpty() &&
                                        currentLastCustAdd != null && !currentLastCustAdd.isEmpty()) {

                                    StaffMonitor staffMonitor = new StaffMonitor();
                                    staffMonitor.setMyId(Long.parseLong(currentMyId));
                                    staffMonitor.setEmpId(Integer.parseInt(currentEmpId));
                                    staffMonitor.setEmpFullName(currentEmpFullName);
                                    staffMonitor.setPicLink(currentPicLink);
                                    staffMonitor.setMyDate(currentMyDate);
                                    staffMonitor.setCheckInPlan(Short.parseShort(currentCheckInPlan));
                                    staffMonitor.setCheckInFinish(Short.parseShort(currentCheckInFinish));
                                    staffMonitor.setNoOrder(Short.parseShort(currentNoOrder));
                                    staffMonitors.add(staffMonitor);

                                    currentMyId = null;
                                    currentEmpId = null;
                                    currentEmpFullName = null;
                                    currentPicLink = null;
                                    currentMyDate = null;
                                    currentCheckInPlan = null;
                                    currentCheckInFinish = null;
                                    currentNoOrder = null;
                                    currentLastCustName = null;
                                    currentLastCustAdd = null;
                                    currentNoofMetter = null;
                                }
                            }
                            //
                            if (PicLink == true) {
                                currentPicLink = data;
                                PicLink = false;
                                if (currentMyId != null && !currentMyId.isEmpty() &&
                                        currentEmpId != null && !currentEmpId.isEmpty() &&
                                        currentEmpFullName != null && !currentEmpFullName.isEmpty() &&
                                        currentPicLink != null && !currentPicLink.isEmpty() &&
                                        currentMyDate != null && !currentMyDate.isEmpty() &&
                                        currentCheckInPlan != null && !currentCheckInPlan.isEmpty() &&
                                        currentCheckInFinish != null && !currentCheckInFinish.isEmpty() &&
                                        currentNoOrder != null && !currentNoOrder.isEmpty() &&
                                        currentLastCustName != null && !currentLastCustName.isEmpty() &&
                                        currentLastCustAdd != null && !currentLastCustAdd.isEmpty()) {

                                    StaffMonitor staffMonitor = new StaffMonitor();
                                    staffMonitor.setMyId(Long.parseLong(currentMyId));
                                    staffMonitor.setEmpId(Integer.parseInt(currentEmpId));
                                    staffMonitor.setEmpFullName(currentEmpFullName);
                                    staffMonitor.setPicLink(currentPicLink);
                                    staffMonitor.setMyDate(currentMyDate);
                                    staffMonitor.setCheckInPlan(Short.parseShort(currentCheckInPlan));
                                    staffMonitor.setCheckInFinish(Short.parseShort(currentCheckInFinish));
                                    staffMonitor.setNoOrder(Short.parseShort(currentNoOrder));
                                    staffMonitors.add(staffMonitor);

                                    currentMyId = null;
                                    currentEmpId = null;
                                    currentEmpFullName = null;
                                    currentPicLink = null;
                                    currentMyDate = null;
                                    currentCheckInPlan = null;
                                    currentCheckInFinish = null;
                                    currentNoOrder = null;
                                    currentLastCustName = null;
                                    currentLastCustAdd = null;
                                    currentNoofMetter = null;
                                }
                            }
                            ///
                            if (MyDate == true) {
                                currentMyDate = data;
                                MyDate = false;
                                if (currentMyId != null && !currentMyId.isEmpty() &&
                                        currentEmpId != null && !currentEmpId.isEmpty() &&
                                        currentEmpFullName != null && !currentEmpFullName.isEmpty() &&
                                        currentPicLink != null && !currentPicLink.isEmpty() &&
                                        currentMyDate != null && !currentMyDate.isEmpty() &&
                                        currentCheckInPlan != null && !currentCheckInPlan.isEmpty() &&
                                        currentCheckInFinish != null && !currentCheckInFinish.isEmpty() &&
                                        currentNoOrder != null && !currentNoOrder.isEmpty() &&
                                        currentLastCustName != null && !currentLastCustName.isEmpty() &&
                                        currentLastCustAdd != null && !currentLastCustAdd.isEmpty()) {

                                    StaffMonitor staffMonitor = new StaffMonitor();
                                    staffMonitor.setMyId(Long.parseLong(currentMyId));
                                    staffMonitor.setEmpId(Integer.parseInt(currentEmpId));
                                    staffMonitor.setEmpFullName(currentEmpFullName);
                                    staffMonitor.setPicLink(currentPicLink);
                                    staffMonitor.setMyDate(currentMyDate);
                                    staffMonitor.setCheckInPlan(Short.parseShort(currentCheckInPlan));
                                    staffMonitor.setCheckInFinish(Short.parseShort(currentCheckInFinish));
                                    staffMonitor.setNoOrder(Short.parseShort(currentNoOrder));
                                    staffMonitors.add(staffMonitor);

                                    currentMyId = null;
                                    currentEmpId = null;
                                    currentEmpFullName = null;
                                    currentPicLink = null;
                                    currentMyDate = null;
                                    currentCheckInPlan = null;
                                    currentCheckInFinish = null;
                                    currentNoOrder = null;
                                    currentLastCustName = null;
                                    currentLastCustAdd = null;
                                    currentNoofMetter = null;
                                }
                            }
                            ///
                            if (CheckInPlan == true) {
                                currentCheckInPlan = data;
                                CheckInPlan = false;
                                if (currentMyId != null && !currentMyId.isEmpty() &&
                                        currentEmpId != null && !currentEmpId.isEmpty() &&
                                        currentEmpFullName != null && !currentEmpFullName.isEmpty() &&
                                        currentPicLink != null && !currentPicLink.isEmpty() &&
                                        currentMyDate != null && !currentMyDate.isEmpty() &&
                                        currentCheckInPlan != null && !currentCheckInPlan.isEmpty() &&
                                        currentCheckInFinish != null && !currentCheckInFinish.isEmpty() &&
                                        currentNoOrder != null && !currentNoOrder.isEmpty() &&
                                        currentLastCustName != null && !currentLastCustName.isEmpty() &&
                                        currentLastCustAdd != null && !currentLastCustAdd.isEmpty()) {

                                    StaffMonitor staffMonitor = new StaffMonitor();
                                    staffMonitor.setMyId(Long.parseLong(currentMyId));
                                    staffMonitor.setEmpId(Integer.parseInt(currentEmpId));
                                    staffMonitor.setEmpFullName(currentEmpFullName);
                                    staffMonitor.setPicLink(currentPicLink);
                                    staffMonitor.setMyDate(currentMyDate);
                                    staffMonitor.setCheckInPlan(Short.parseShort(currentCheckInPlan));
                                    staffMonitor.setCheckInFinish(Short.parseShort(currentCheckInFinish));
                                    staffMonitor.setNoOrder(Short.parseShort(currentNoOrder));
                                    staffMonitors.add(staffMonitor);

                                    currentMyId = null;
                                    currentEmpId = null;
                                    currentEmpFullName = null;
                                    currentPicLink = null;
                                    currentMyDate = null;
                                    currentCheckInPlan = null;
                                    currentCheckInFinish = null;
                                    currentNoOrder = null;
                                    currentLastCustName = null;
                                    currentLastCustAdd = null;
                                    currentNoofMetter = null;
                                }
                            }
                            ///
                            if (CheckInFinish == true) {
                                currentCheckInFinish = data;
                                CheckInFinish = false;
                                if (currentMyId != null && !currentMyId.isEmpty() &&
                                        currentEmpId != null && !currentEmpId.isEmpty() &&
                                        currentEmpFullName != null && !currentEmpFullName.isEmpty() &&
                                        currentPicLink != null && !currentPicLink.isEmpty() &&
                                        currentMyDate != null && !currentMyDate.isEmpty() &&
                                        currentCheckInPlan != null && !currentCheckInPlan.isEmpty() &&
                                        currentCheckInFinish != null && !currentCheckInFinish.isEmpty() &&
                                        currentNoOrder != null && !currentNoOrder.isEmpty() &&
                                        currentLastCustName != null && !currentLastCustName.isEmpty() &&
                                        currentLastCustAdd != null && !currentLastCustAdd.isEmpty()) {

                                    StaffMonitor staffMonitor = new StaffMonitor();
                                    staffMonitor.setMyId(Long.parseLong(currentMyId));
                                    staffMonitor.setEmpId(Integer.parseInt(currentEmpId));
                                    staffMonitor.setEmpFullName(currentEmpFullName);
                                    staffMonitor.setPicLink(currentPicLink);
                                    staffMonitor.setMyDate(currentMyDate);
                                    staffMonitor.setCheckInPlan(Short.parseShort(currentCheckInPlan));
                                    staffMonitor.setCheckInFinish(Short.parseShort(currentCheckInFinish));
                                    staffMonitor.setNoOrder(Short.parseShort(currentNoOrder));
                                    staffMonitors.add(staffMonitor);

                                    currentMyId = null;
                                    currentEmpId = null;
                                    currentEmpFullName = null;
                                    currentPicLink = null;
                                    currentMyDate = null;
                                    currentCheckInPlan = null;
                                    currentCheckInFinish = null;
                                    currentNoOrder = null;
                                    currentLastCustName = null;
                                    currentLastCustAdd = null;
                                    currentNoofMetter = null;
                                }
                            }
                            ///
                            if (NoOrder == true) {
                                currentNoOrder = data;
                                NoOrder = false;
                                if (currentMyId != null && !currentMyId.isEmpty() &&
                                        currentEmpId != null && !currentEmpId.isEmpty() &&
                                        currentEmpFullName != null && !currentEmpFullName.isEmpty() &&
                                        currentPicLink != null && !currentPicLink.isEmpty() &&
                                        currentMyDate != null && !currentMyDate.isEmpty() &&
                                        currentCheckInPlan != null && !currentCheckInPlan.isEmpty() &&
                                        currentCheckInFinish != null && !currentCheckInFinish.isEmpty() &&
                                        currentNoOrder != null && !currentNoOrder.isEmpty() &&
                                        currentLastCustName != null && !currentLastCustName.isEmpty() &&
                                        currentLastCustAdd != null && !currentLastCustAdd.isEmpty()) {

                                    StaffMonitor staffMonitor = new StaffMonitor();
                                    staffMonitor.setMyId(Long.parseLong(currentMyId));
                                    staffMonitor.setEmpId(Integer.parseInt(currentEmpId));
                                    staffMonitor.setEmpFullName(currentEmpFullName);
                                    staffMonitor.setPicLink(currentPicLink);
                                    staffMonitor.setMyDate(currentMyDate);
                                    staffMonitor.setCheckInPlan(Short.parseShort(currentCheckInPlan));
                                    staffMonitor.setCheckInFinish(Short.parseShort(currentCheckInFinish));
                                    staffMonitor.setNoOrder(Short.parseShort(currentNoOrder));
                                    staffMonitors.add(staffMonitor);

                                    currentMyId = null;
                                    currentEmpId = null;
                                    currentEmpFullName = null;
                                    currentPicLink = null;
                                    currentMyDate = null;
                                    currentCheckInPlan = null;
                                    currentCheckInFinish = null;
                                    currentNoOrder = null;
                                    currentLastCustName = null;
                                    currentLastCustAdd = null;
                                    currentNoofMetter = null;
                                }
                            }
                            ///
                            if (LastCustName == true) {
                                currentLastCustName = data;
                                LastCustName = false;
                                if (currentMyId != null && !currentMyId.isEmpty() &&
                                        currentEmpId != null && !currentEmpId.isEmpty() &&
                                        currentEmpFullName != null && !currentEmpFullName.isEmpty() &&
                                        currentPicLink != null && !currentPicLink.isEmpty() &&
                                        currentMyDate != null && !currentMyDate.isEmpty() &&
                                        currentCheckInPlan != null && !currentCheckInPlan.isEmpty() &&
                                        currentCheckInFinish != null && !currentCheckInFinish.isEmpty() &&
                                        currentNoOrder != null && !currentNoOrder.isEmpty() &&
                                        currentLastCustName != null && !currentLastCustName.isEmpty() &&
                                        currentLastCustAdd != null && !currentLastCustAdd.isEmpty()) {

                                    StaffMonitor staffMonitor = new StaffMonitor();
                                    staffMonitor.setMyId(Long.parseLong(currentMyId));
                                    staffMonitor.setEmpId(Integer.parseInt(currentEmpId));
                                    staffMonitor.setEmpFullName(currentEmpFullName);
                                    staffMonitor.setPicLink(currentPicLink);
                                    staffMonitor.setMyDate(currentMyDate);
                                    staffMonitor.setCheckInPlan(Short.parseShort(currentCheckInPlan));
                                    staffMonitor.setCheckInFinish(Short.parseShort(currentCheckInFinish));
                                    staffMonitor.setNoOrder(Short.parseShort(currentNoOrder));
                                    staffMonitors.add(staffMonitor);

                                    currentMyId = null;
                                    currentEmpId = null;
                                    currentEmpFullName = null;
                                    currentPicLink = null;
                                    currentMyDate = null;
                                    currentCheckInPlan = null;
                                    currentCheckInFinish = null;
                                    currentNoOrder = null;
                                    currentLastCustName = null;
                                    currentLastCustAdd = null;
                                    currentNoofMetter = null;
                                }
                            }
                            ///
                            if (LastCustAdd == true) {
                                currentLastCustAdd = data;
                                LastCustAdd = false;
                                if (currentMyId != null && !currentMyId.isEmpty() &&
                                        currentEmpId != null && !currentEmpId.isEmpty() &&
                                        currentEmpFullName != null && !currentEmpFullName.isEmpty() &&
                                        currentPicLink != null && !currentPicLink.isEmpty() &&
                                        currentMyDate != null && !currentMyDate.isEmpty() &&
                                        currentCheckInPlan != null && !currentCheckInPlan.isEmpty() &&
                                        currentCheckInFinish != null && !currentCheckInFinish.isEmpty() &&
                                        currentNoOrder != null && !currentNoOrder.isEmpty() &&
                                        currentLastCustName != null && !currentLastCustName.isEmpty() &&
                                        currentLastCustAdd != null && !currentLastCustAdd.isEmpty() &&
                                        currentLastCustAdd.contains("\n")) {

                                    StaffMonitor staffMonitor = new StaffMonitor();
                                    staffMonitor.setMyId(Long.parseLong(currentMyId));
                                    staffMonitor.setEmpId(Integer.parseInt(currentEmpId));
                                    staffMonitor.setEmpFullName(currentEmpFullName);
                                    staffMonitor.setPicLink(currentPicLink);
                                    staffMonitor.setMyDate(currentMyDate);
                                    staffMonitor.setCheckInPlan(Short.parseShort(currentCheckInPlan));
                                    staffMonitor.setCheckInFinish(Short.parseShort(currentCheckInFinish));
                                    staffMonitor.setNoOrder(Short.parseShort(currentNoOrder));
                                    staffMonitors.add(staffMonitor);

                                    currentMyId = null;
                                    currentEmpId = null;
                                    currentEmpFullName = null;
                                    currentPicLink = null;
                                    currentMyDate = null;
                                    currentCheckInPlan = null;
                                    currentCheckInFinish = null;
                                    currentNoOrder = null;
                                    currentLastCustName = null;
                                    currentLastCustAdd = null;
                                    currentNoofMetter = null;
                                }
                            }
                            ///
                            if (NoofMetter == true) {
                                currentNoofMetter = data;
                                NoofMetter = false;
                                if (currentMyId != null && !currentMyId.isEmpty() &&
                                        currentEmpId != null && !currentEmpId.isEmpty() &&
                                        currentEmpFullName != null && !currentEmpFullName.isEmpty() &&
                                        currentPicLink != null && !currentPicLink.isEmpty() &&
                                        currentMyDate != null && !currentMyDate.isEmpty() &&
                                        currentCheckInPlan != null && !currentCheckInPlan.isEmpty() &&
                                        currentCheckInFinish != null && !currentCheckInFinish.isEmpty() &&
                                        currentNoOrder != null && !currentNoOrder.isEmpty() &&
                                        currentLastCustName != null && !currentLastCustName.isEmpty() &&
                                        currentLastCustAdd != null && !currentLastCustAdd.isEmpty() &&
                                        currentNoofMetter != null && !currentNoofMetter.isEmpty()) {

                                    StaffMonitor staffMonitor = new StaffMonitor();
                                    staffMonitor.setMyId(Long.parseLong(currentMyId));
                                    staffMonitor.setEmpId(Integer.parseInt(currentEmpId));
                                    staffMonitor.setEmpFullName(currentEmpFullName);
                                    staffMonitor.setPicLink(currentPicLink);
                                    staffMonitor.setMyDate(currentMyDate);
                                    staffMonitor.setCheckInPlan(Short.parseShort(currentCheckInPlan));
                                    staffMonitor.setCheckInFinish(Short.parseShort(currentCheckInFinish));
                                    staffMonitor.setNoOrder(Short.parseShort(currentNoOrder));
                                    staffMonitor.setLastCustName(currentLastCustName);
                                    staffMonitor.setLastCustAdd(currentLastCustAdd);
                                    staffMonitor.setNoofMetter(currentNoofMetter);
                                    staffMonitors.add(staffMonitor);

                                    currentMyId = null;
                                    currentEmpId = null;
                                    currentEmpFullName = null;
                                    currentPicLink = null;
                                    currentMyDate = null;
                                    currentCheckInPlan = null;
                                    currentCheckInFinish = null;
                                    currentNoOrder = null;
                                    currentLastCustName = null;
                                    currentLastCustAdd = null;
                                    currentNoofMetter = null;
                                }
                            }
                        }
                        eventType = xpp.next();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    callback.onFail(e.getMessage());
                }
                callback.onSuccess(staffMonitors);
            }

            @Override
            public void onFail(String error) {
                callback.onFail(error);
            }
        });
    }

    public void loadListStaffLocation(String UserCode, final GetListStaffLocation callback) {
        String link = String.format(getURL() + Config.LISTSTAFFLOCATION);

        List<Param> params = new ArrayList<>();
        Param param = new Param("UserCode", UserCode);
        params.add(param);
        mNetwork.executePost(link, params, new Network.Callback() {
            @Override
            public void onCallBack(String response) {
                XmlPullParserFactory factory = null;
                List<StaffLocation> staffLocations = new ArrayList<>();
                try {
                    boolean MyId = false;
                    boolean EmpId = false;
                    boolean EmpFullName = false;
                    boolean PicLink = false;
                    boolean CheckInPlan = false;
                    boolean CheckInFinish = false;
                    boolean NoOrder = false;
                    boolean CurrLat = false;
                    boolean CurrLong = false;
                    boolean LastUpdate = false;

                    String currentMyId = null;
                    String currentEmpId = null;
                    String currentEmpFullName = null;
                    String currentPicLink = null;
                    String currentCheckInPlan = null;
                    String currentCheckInFinish = null;
                    String currentNoOrder = null;
                    String currentCurrLat = null;
                    String currentCurrLong = null;
                    String currentLastUpdate = null;


                    factory = XmlPullParserFactory.newInstance();
                    factory.setNamespaceAware(true);
                    XmlPullParser xpp = factory.newPullParser();

                    xpp.setInput(new StringReader(response));
                    int eventType = xpp.getEventType();
                    while (eventType != XmlPullParser.END_DOCUMENT) {
                        if (eventType == XmlPullParser.START_DOCUMENT) {
                            //TODO: do nothing
                        } else if (eventType == XmlPullParser.START_TAG) {
                            if (xpp.getName().equalsIgnoreCase("MyId")) {
                                MyId = true;
                            }

                            if (xpp.getName().equalsIgnoreCase("EmpId")) {
                                EmpId = true;
                            }

                            if (xpp.getName().equalsIgnoreCase("EmpFullName")) {
                                EmpFullName = true;
                            }

                            if (xpp.getName().equalsIgnoreCase("PicLink")) {
                                PicLink = true;
                            }

                            if (xpp.getName().equalsIgnoreCase("CheckInPlan")) {
                                CheckInPlan = true;
                            }

                            if (xpp.getName().equalsIgnoreCase("CheckInFinish")) {
                                CheckInFinish = true;
                            }

                            if (xpp.getName().equalsIgnoreCase("NoOrder")) {
                                NoOrder = true;
                            }

                            if (xpp.getName().equalsIgnoreCase("CurrLat")) {
                                CurrLat = true;
                            }

                            if (xpp.getName().equalsIgnoreCase("CurrLong")) {
                                CurrLong = true;
                            }

                            if (xpp.getName().equalsIgnoreCase("LastUpdate")) {
                                LastUpdate = true;
                            }

                        } else if (eventType == XmlPullParser.TEXT) {
                            String data = xpp.getText();

                            if (MyId == true) {
                                currentMyId = data;
                                MyId = false;
                                if (currentMyId != null && !currentMyId.isEmpty() &&
                                        currentEmpId != null && !currentEmpId.isEmpty() &&
                                        currentEmpFullName != null && !currentEmpFullName.isEmpty() &&
                                        currentPicLink != null && !currentPicLink.isEmpty() &&
                                        currentCheckInPlan != null && !currentCheckInPlan.isEmpty() &&
                                        currentCheckInFinish != null && !currentCheckInFinish.isEmpty() &&
                                        currentNoOrder != null && !currentNoOrder.isEmpty() &&
                                        currentCurrLat != null && !currentCurrLat.isEmpty() &&
                                        currentCurrLong != null && !currentCurrLong.isEmpty() &&
                                        currentLastUpdate != null && !currentLastUpdate.isEmpty()) {

                                    StaffLocation staffLocation = new StaffLocation();
                                    staffLocation.setMyId(Long.parseLong(currentMyId));
                                    staffLocation.setEmpId(Integer.parseInt(currentEmpId));
                                    staffLocation.setEmpFullName(currentEmpFullName);
                                    staffLocation.setPicLink(currentPicLink);
                                    staffLocation.setCheckInPlan(Short.parseShort(currentCheckInPlan));
                                    staffLocation.setCheckInFinish(Short.parseShort(currentCheckInFinish));
                                    staffLocation.setNoOrder(Short.parseShort(currentNoOrder));
                                    staffLocation.setCurrLat(currentCurrLat);
                                    staffLocation.setCurrLong(currentCurrLong);
                                    staffLocation.setLastUpdate(currentLastUpdate);
                                    staffLocations.add(staffLocation);

                                    currentMyId = null;
                                    currentEmpId = null;
                                    currentEmpFullName = null;
                                    currentPicLink = null;
                                    currentCheckInPlan = null;
                                    currentCheckInFinish = null;
                                    currentNoOrder = null;
                                    currentCurrLat = null;
                                    currentCurrLong = null;
                                    currentLastUpdate = null;
                                }
                            }
                            ///
                            if (EmpId == true) {
                                currentEmpId = data;
                                EmpId = false;
                                if (currentMyId != null && !currentMyId.isEmpty() &&
                                        currentEmpId != null && !currentEmpId.isEmpty() &&
                                        currentEmpFullName != null && !currentEmpFullName.isEmpty() &&
                                        currentPicLink != null && !currentPicLink.isEmpty() &&
                                        currentCheckInPlan != null && !currentCheckInPlan.isEmpty() &&
                                        currentCheckInFinish != null && !currentCheckInFinish.isEmpty() &&
                                        currentNoOrder != null && !currentNoOrder.isEmpty() &&
                                        currentCurrLat != null && !currentCurrLat.isEmpty() &&
                                        currentCurrLong != null && !currentCurrLong.isEmpty() &&
                                        currentLastUpdate != null && !currentLastUpdate.isEmpty()) {

                                    StaffLocation staffLocation = new StaffLocation();
                                    staffLocation.setMyId(Long.parseLong(currentMyId));
                                    staffLocation.setEmpId(Integer.parseInt(currentEmpId));
                                    staffLocation.setEmpFullName(currentEmpFullName);
                                    staffLocation.setPicLink(currentPicLink);
                                    staffLocation.setCheckInPlan(Short.parseShort(currentCheckInPlan));
                                    staffLocation.setCheckInFinish(Short.parseShort(currentCheckInFinish));
                                    staffLocation.setNoOrder(Short.parseShort(currentNoOrder));
                                    staffLocation.setCurrLat(currentCurrLat);
                                    staffLocation.setCurrLong(currentCurrLong);
                                    staffLocation.setLastUpdate(currentLastUpdate);
                                    staffLocations.add(staffLocation);

                                    currentMyId = null;
                                    currentEmpId = null;
                                    currentEmpFullName = null;
                                    currentPicLink = null;
                                    currentCheckInPlan = null;
                                    currentCheckInFinish = null;
                                    currentNoOrder = null;
                                    currentCurrLat = null;
                                    currentCurrLong = null;
                                    currentLastUpdate = null;
                                }
                            }
                            ///
                            if (EmpFullName == true) {
                                currentEmpFullName = data;
                                EmpFullName = false;
                                if (currentMyId != null && !currentMyId.isEmpty() &&
                                        currentEmpId != null && !currentEmpId.isEmpty() &&
                                        currentEmpFullName != null && !currentEmpFullName.isEmpty() &&
                                        currentPicLink != null && !currentPicLink.isEmpty() &&
                                        currentCheckInPlan != null && !currentCheckInPlan.isEmpty() &&
                                        currentCheckInFinish != null && !currentCheckInFinish.isEmpty() &&
                                        currentNoOrder != null && !currentNoOrder.isEmpty() &&
                                        currentCurrLat != null && !currentCurrLat.isEmpty() &&
                                        currentCurrLong != null && !currentCurrLong.isEmpty() &&
                                        currentLastUpdate != null && !currentLastUpdate.isEmpty()) {

                                    StaffLocation staffLocation = new StaffLocation();
                                    staffLocation.setMyId(Long.parseLong(currentMyId));
                                    staffLocation.setEmpId(Integer.parseInt(currentEmpId));
                                    staffLocation.setEmpFullName(currentEmpFullName);
                                    staffLocation.setPicLink(currentPicLink);
                                    staffLocation.setCheckInPlan(Short.parseShort(currentCheckInPlan));
                                    staffLocation.setCheckInFinish(Short.parseShort(currentCheckInFinish));
                                    staffLocation.setNoOrder(Short.parseShort(currentNoOrder));
                                    staffLocation.setCurrLat(currentCurrLat);
                                    staffLocation.setCurrLong(currentCurrLong);
                                    staffLocation.setLastUpdate(currentLastUpdate);
                                    staffLocations.add(staffLocation);

                                    currentMyId = null;
                                    currentEmpId = null;
                                    currentEmpFullName = null;
                                    currentPicLink = null;
                                    currentCheckInPlan = null;
                                    currentCheckInFinish = null;
                                    currentNoOrder = null;
                                    currentCurrLat = null;
                                    currentCurrLong = null;
                                    currentLastUpdate = null;
                                }
                            }
                            //
                            if (PicLink == true) {
                                currentPicLink = data;
                                PicLink = false;
                                if (currentMyId != null && !currentMyId.isEmpty() &&
                                        currentEmpId != null && !currentEmpId.isEmpty() &&
                                        currentEmpFullName != null && !currentEmpFullName.isEmpty() &&
                                        currentPicLink != null && !currentPicLink.isEmpty() &&
                                        currentCheckInPlan != null && !currentCheckInPlan.isEmpty() &&
                                        currentCheckInFinish != null && !currentCheckInFinish.isEmpty() &&
                                        currentNoOrder != null && !currentNoOrder.isEmpty() &&
                                        currentCurrLat != null && !currentCurrLat.isEmpty() &&
                                        currentCurrLong != null && !currentCurrLong.isEmpty() &&
                                        currentLastUpdate != null && !currentLastUpdate.isEmpty()) {

                                    StaffLocation staffLocation = new StaffLocation();
                                    staffLocation.setMyId(Long.parseLong(currentMyId));
                                    staffLocation.setEmpId(Integer.parseInt(currentEmpId));
                                    staffLocation.setEmpFullName(currentEmpFullName);
                                    staffLocation.setPicLink(currentPicLink);
                                    staffLocation.setCheckInPlan(Short.parseShort(currentCheckInPlan));
                                    staffLocation.setCheckInFinish(Short.parseShort(currentCheckInFinish));
                                    staffLocation.setNoOrder(Short.parseShort(currentNoOrder));
                                    staffLocation.setCurrLat(currentCurrLat);
                                    staffLocation.setCurrLong(currentCurrLong);
                                    staffLocation.setLastUpdate(currentLastUpdate);
                                    staffLocations.add(staffLocation);

                                    currentMyId = null;
                                    currentEmpId = null;
                                    currentEmpFullName = null;
                                    currentPicLink = null;
                                    currentCheckInPlan = null;
                                    currentCheckInFinish = null;
                                    currentNoOrder = null;
                                    currentCurrLat = null;
                                    currentCurrLong = null;
                                    currentLastUpdate = null;
                                }
                            }
                            ///
                            if (CheckInPlan == true) {
                                currentCheckInPlan = data;
                                CheckInPlan = false;
                                if (currentMyId != null && !currentMyId.isEmpty() &&
                                        currentEmpId != null && !currentEmpId.isEmpty() &&
                                        currentEmpFullName != null && !currentEmpFullName.isEmpty() &&
                                        currentPicLink != null && !currentPicLink.isEmpty() &&
                                        currentCheckInPlan != null && !currentCheckInPlan.isEmpty() &&
                                        currentCheckInFinish != null && !currentCheckInFinish.isEmpty() &&
                                        currentNoOrder != null && !currentNoOrder.isEmpty() &&
                                        currentCurrLat != null && !currentCurrLat.isEmpty() &&
                                        currentCurrLong != null && !currentCurrLong.isEmpty() &&
                                        currentLastUpdate != null && !currentLastUpdate.isEmpty()) {

                                    StaffLocation staffLocation = new StaffLocation();
                                    staffLocation.setMyId(Long.parseLong(currentMyId));
                                    staffLocation.setEmpId(Integer.parseInt(currentEmpId));
                                    staffLocation.setEmpFullName(currentEmpFullName);
                                    staffLocation.setPicLink(currentPicLink);
                                    staffLocation.setCheckInPlan(Short.parseShort(currentCheckInPlan));
                                    staffLocation.setCheckInFinish(Short.parseShort(currentCheckInFinish));
                                    staffLocation.setNoOrder(Short.parseShort(currentNoOrder));
                                    staffLocation.setCurrLat(currentCurrLat);
                                    staffLocation.setCurrLong(currentCurrLong);
                                    staffLocation.setLastUpdate(currentLastUpdate);
                                    staffLocations.add(staffLocation);

                                    currentMyId = null;
                                    currentEmpId = null;
                                    currentEmpFullName = null;
                                    currentPicLink = null;
                                    currentCheckInPlan = null;
                                    currentCheckInFinish = null;
                                    currentNoOrder = null;
                                    currentCurrLat = null;
                                    currentCurrLong = null;
                                    currentLastUpdate = null;
                                }
                            }
                            ///
                            if (CheckInFinish == true) {
                                currentCheckInFinish = data;
                                CheckInFinish = false;
                                if (currentMyId != null && !currentMyId.isEmpty() &&
                                        currentEmpId != null && !currentEmpId.isEmpty() &&
                                        currentEmpFullName != null && !currentEmpFullName.isEmpty() &&
                                        currentPicLink != null && !currentPicLink.isEmpty() &&
                                        currentCheckInPlan != null && !currentCheckInPlan.isEmpty() &&
                                        currentCheckInFinish != null && !currentCheckInFinish.isEmpty() &&
                                        currentNoOrder != null && !currentNoOrder.isEmpty() &&
                                        currentCurrLat != null && !currentCurrLat.isEmpty() &&
                                        currentCurrLong != null && !currentCurrLong.isEmpty() &&
                                        currentLastUpdate != null && !currentLastUpdate.isEmpty()) {

                                    StaffLocation staffLocation = new StaffLocation();
                                    staffLocation.setMyId(Long.parseLong(currentMyId));
                                    staffLocation.setEmpId(Integer.parseInt(currentEmpId));
                                    staffLocation.setEmpFullName(currentEmpFullName);
                                    staffLocation.setPicLink(currentPicLink);
                                    staffLocation.setCheckInPlan(Short.parseShort(currentCheckInPlan));
                                    staffLocation.setCheckInFinish(Short.parseShort(currentCheckInFinish));
                                    staffLocation.setNoOrder(Short.parseShort(currentNoOrder));
                                    staffLocation.setCurrLat(currentCurrLat);
                                    staffLocation.setCurrLong(currentCurrLong);
                                    staffLocation.setLastUpdate(currentLastUpdate);
                                    staffLocations.add(staffLocation);

                                    currentMyId = null;
                                    currentEmpId = null;
                                    currentEmpFullName = null;
                                    currentPicLink = null;
                                    currentCheckInPlan = null;
                                    currentCheckInFinish = null;
                                    currentNoOrder = null;
                                    currentCurrLat = null;
                                    currentCurrLong = null;
                                    currentLastUpdate = null;
                                }
                            }
                            ///
                            if (NoOrder == true) {
                                currentNoOrder = data;
                                NoOrder = false;
                                if (currentMyId != null && !currentMyId.isEmpty() &&
                                        currentEmpId != null && !currentEmpId.isEmpty() &&
                                        currentEmpFullName != null && !currentEmpFullName.isEmpty() &&
                                        currentPicLink != null && !currentPicLink.isEmpty() &&
                                        currentCheckInPlan != null && !currentCheckInPlan.isEmpty() &&
                                        currentCheckInFinish != null && !currentCheckInFinish.isEmpty() &&
                                        currentNoOrder != null && !currentNoOrder.isEmpty() &&
                                        currentCurrLat != null && !currentCurrLat.isEmpty() &&
                                        currentCurrLong != null && !currentCurrLong.isEmpty() &&
                                        currentLastUpdate != null && !currentLastUpdate.isEmpty()) {

                                    StaffLocation staffLocation = new StaffLocation();
                                    staffLocation.setMyId(Long.parseLong(currentMyId));
                                    staffLocation.setEmpId(Integer.parseInt(currentEmpId));
                                    staffLocation.setEmpFullName(currentEmpFullName);
                                    staffLocation.setPicLink(currentPicLink);
                                    staffLocation.setCheckInPlan(Short.parseShort(currentCheckInPlan));
                                    staffLocation.setCheckInFinish(Short.parseShort(currentCheckInFinish));
                                    staffLocation.setNoOrder(Short.parseShort(currentNoOrder));
                                    staffLocation.setCurrLat(currentCurrLat);
                                    staffLocation.setCurrLong(currentCurrLong);
                                    staffLocation.setLastUpdate(currentLastUpdate);
                                    staffLocations.add(staffLocation);

                                    currentMyId = null;
                                    currentEmpId = null;
                                    currentEmpFullName = null;
                                    currentPicLink = null;
                                    currentCheckInPlan = null;
                                    currentCheckInFinish = null;
                                    currentNoOrder = null;
                                    currentCurrLat = null;
                                    currentCurrLong = null;
                                    currentLastUpdate = null;
                                }
                            }
                            ///
                            if (CurrLat == true) {
                                currentCurrLat = data;
                                CurrLat = false;
                                if (currentMyId != null && !currentMyId.isEmpty() &&
                                        currentEmpId != null && !currentEmpId.isEmpty() &&
                                        currentEmpFullName != null && !currentEmpFullName.isEmpty() &&
                                        currentPicLink != null && !currentPicLink.isEmpty() &&
                                        currentCheckInPlan != null && !currentCheckInPlan.isEmpty() &&
                                        currentCheckInFinish != null && !currentCheckInFinish.isEmpty() &&
                                        currentNoOrder != null && !currentNoOrder.isEmpty() &&
                                        currentCurrLat != null && !currentCurrLat.isEmpty() &&
                                        currentCurrLong != null && !currentCurrLong.isEmpty() &&
                                        currentLastUpdate != null && !currentLastUpdate.isEmpty()) {

                                    StaffLocation staffLocation = new StaffLocation();
                                    staffLocation.setMyId(Long.parseLong(currentMyId));
                                    staffLocation.setEmpId(Integer.parseInt(currentEmpId));
                                    staffLocation.setEmpFullName(currentEmpFullName);
                                    staffLocation.setPicLink(currentPicLink);
                                    staffLocation.setCheckInPlan(Short.parseShort(currentCheckInPlan));
                                    staffLocation.setCheckInFinish(Short.parseShort(currentCheckInFinish));
                                    staffLocation.setNoOrder(Short.parseShort(currentNoOrder));
                                    staffLocation.setCurrLat(currentCurrLat);
                                    staffLocation.setCurrLong(currentCurrLong);
                                    staffLocation.setLastUpdate(currentLastUpdate);
                                    staffLocations.add(staffLocation);

                                    currentMyId = null;
                                    currentEmpId = null;
                                    currentEmpFullName = null;
                                    currentPicLink = null;
                                    currentCheckInPlan = null;
                                    currentCheckInFinish = null;
                                    currentNoOrder = null;
                                    currentCurrLat = null;
                                    currentCurrLong = null;
                                    currentLastUpdate = null;
                                }
                            }
                            ///
                            if (CurrLong == true) {
                                currentCurrLong = data;
                                CurrLong = false;
                                if (currentMyId != null && !currentMyId.isEmpty() &&
                                        currentEmpId != null && !currentEmpId.isEmpty() &&
                                        currentEmpFullName != null && !currentEmpFullName.isEmpty() &&
                                        currentPicLink != null && !currentPicLink.isEmpty() &&
                                        currentCheckInPlan != null && !currentCheckInPlan.isEmpty() &&
                                        currentCheckInFinish != null && !currentCheckInFinish.isEmpty() &&
                                        currentNoOrder != null && !currentNoOrder.isEmpty() &&
                                        currentCurrLat != null && !currentCurrLat.isEmpty() &&
                                        currentCurrLong != null && !currentCurrLong.isEmpty() &&
                                        currentLastUpdate != null && !currentLastUpdate.isEmpty()) {

                                    StaffLocation staffLocation = new StaffLocation();
                                    staffLocation.setMyId(Long.parseLong(currentMyId));
                                    staffLocation.setEmpId(Integer.parseInt(currentEmpId));
                                    staffLocation.setEmpFullName(currentEmpFullName);
                                    staffLocation.setPicLink(currentPicLink);
                                    staffLocation.setCheckInPlan(Short.parseShort(currentCheckInPlan));
                                    staffLocation.setCheckInFinish(Short.parseShort(currentCheckInFinish));
                                    staffLocation.setNoOrder(Short.parseShort(currentNoOrder));
                                    staffLocation.setCurrLat(currentCurrLat);
                                    staffLocation.setCurrLong(currentCurrLong);
                                    staffLocation.setLastUpdate(currentLastUpdate);
                                    staffLocations.add(staffLocation);

                                    currentMyId = null;
                                    currentEmpId = null;
                                    currentEmpFullName = null;
                                    currentPicLink = null;
                                    currentCheckInPlan = null;
                                    currentCheckInFinish = null;
                                    currentNoOrder = null;
                                    currentCurrLat = null;
                                    currentCurrLong = null;
                                    currentLastUpdate = null;
                                }
                            }
                            ///
                            if (LastUpdate == true) {
                                currentLastUpdate = data;
                                LastUpdate = false;
                                if (currentMyId != null && !currentMyId.isEmpty() &&
                                        currentEmpId != null && !currentEmpId.isEmpty() &&
                                        currentEmpFullName != null && !currentEmpFullName.isEmpty() &&
                                        currentPicLink != null && !currentPicLink.isEmpty() &&
                                        currentCheckInPlan != null && !currentCheckInPlan.isEmpty() &&
                                        currentCheckInFinish != null && !currentCheckInFinish.isEmpty() &&
                                        currentNoOrder != null && !currentNoOrder.isEmpty() &&
                                        currentCurrLat != null && !currentCurrLat.isEmpty() &&
                                        currentCurrLong != null && !currentCurrLong.isEmpty() &&
                                        currentLastUpdate != null && !currentLastUpdate.isEmpty()) {

                                    StaffLocation staffLocation = new StaffLocation();
                                    staffLocation.setMyId(Long.parseLong(currentMyId));
                                    staffLocation.setEmpId(Integer.parseInt(currentEmpId));
                                    staffLocation.setEmpFullName(currentEmpFullName);
                                    staffLocation.setPicLink(currentPicLink);
                                    staffLocation.setCheckInPlan(Short.parseShort(currentCheckInPlan));
                                    staffLocation.setCheckInFinish(Short.parseShort(currentCheckInFinish));
                                    staffLocation.setNoOrder(Short.parseShort(currentNoOrder));
                                    staffLocation.setCurrLat(currentCurrLat);
                                    staffLocation.setCurrLong(currentCurrLong);
                                    staffLocation.setLastUpdate(currentLastUpdate);
                                    staffLocations.add(staffLocation);

                                    currentMyId = null;
                                    currentEmpId = null;
                                    currentEmpFullName = null;
                                    currentPicLink = null;
                                    currentCheckInPlan = null;
                                    currentCheckInFinish = null;
                                    currentNoOrder = null;
                                    currentCurrLat = null;
                                    currentCurrLong = null;
                                    currentLastUpdate = null;
                                }
                            }
                        }
                        eventType = xpp.next();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    callback.onFail(e.getMessage());
                }
                callback.onSuccess(staffLocations);
            }

            @Override
            public void onFail(String error) {
                callback.onFail(error);
            }
        });
    }

    public void loadListRoute(String UserCode, final GetListRoute callback) {
        String link = String.format(getURL() + Config.ROUTE);

        List<Param> params = new ArrayList<>();
        Param param = new Param("UserCode", UserCode);
        params.add(param);
        mNetwork.executePost(link, params, new Network.Callback() {
            @Override
            public void onCallBack(String response) {
                XmlPullParserFactory factory = null;
                List<Route> routeList = new ArrayList<>();
                try {
                    boolean readyToGetCode = false;
                    boolean readyToGetName = false;

                    String currentCode = null;
                    String currentName = null;

                    factory = XmlPullParserFactory.newInstance();
                    factory.setNamespaceAware(true);
                    XmlPullParser xpp = factory.newPullParser();

                    xpp.setInput(new StringReader(response));
                    int eventType = xpp.getEventType();
                    while (eventType != XmlPullParser.END_DOCUMENT) {
                        if (eventType == XmlPullParser.START_DOCUMENT) {
                            //TODO: do nothing
                        } else if (eventType == XmlPullParser.START_TAG) {
                            if (xpp.getName().equalsIgnoreCase("RouteCode")) {
                                readyToGetCode = true;
                            }

                            if (xpp.getName().equalsIgnoreCase("RouteName")) {
                                readyToGetName = true;
                            }

                        } else if (eventType == XmlPullParser.TEXT) {
                            String data = xpp.getText();

                            if (readyToGetCode == true) {
                                currentCode = data;
                                readyToGetCode = false;
                                if (currentCode != null && !currentCode.isEmpty() &&
                                        currentName != null && !currentName.isEmpty()) {
                                    Route route = new Route();
                                    route.setRouteCode(currentCode);
                                    route.setRouteName(currentName);
                                    routeList.add(route);

                                    currentCode = null;
                                    currentName = null;
                                }
                            }

                            if (readyToGetName == true) {
                                currentName = data;
                                readyToGetName = false;
                                if (currentCode != null && !currentCode.isEmpty() &&
                                        currentName != null && !currentName.isEmpty()) {
                                    Route route = new Route();
                                    route.setRouteCode(currentCode);
                                    route.setRouteName(currentName);
                                    routeList.add(route);

                                    currentCode = null;
                                    currentName = null;
                                }
                            }
                        }
                        eventType = xpp.next();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    callback.onFail(e.getMessage());
                }
                callback.onSuccess(routeList);
            }

            @Override
            public void onFail(String error) {
                callback.onFail(error);
            }
        });
    }

    public void loadListChannel(String UserCode, final GetListChannel callback) {
        String link = String.format(getURL() + Config.CHANNEL);

        List<Param> params = new ArrayList<>();
        Param param = new Param("UserCode", UserCode);
        params.add(param);
        mNetwork.executePost(link, params, new Network.Callback() {
            @Override
            public void onCallBack(String response) {
                XmlPullParserFactory factory = null;
                List<Channel> channelList = new ArrayList<>();
                try {
                    boolean readyToGetCode = false;
                    boolean readyToGetName = false;

                    String currentCode = null;
                    String currentName = null;

                    factory = XmlPullParserFactory.newInstance();
                    factory.setNamespaceAware(true);
                    XmlPullParser xpp = factory.newPullParser();

                    xpp.setInput(new StringReader(response));
                    int eventType = xpp.getEventType();
                    while (eventType != XmlPullParser.END_DOCUMENT) {
                        if (eventType == XmlPullParser.START_DOCUMENT) {
                            //TODO: do nothing
                        } else if (eventType == XmlPullParser.START_TAG) {
                            if (xpp.getName().equalsIgnoreCase("ChannelCode")) {
                                readyToGetCode = true;
                            }

                            if (xpp.getName().equalsIgnoreCase("ChannelName")) {
                                readyToGetName = true;
                            }

                        } else if (eventType == XmlPullParser.TEXT) {
                            String data = xpp.getText();

                            if (readyToGetCode == true) {
                                currentCode = data;
                                readyToGetCode = false;
                                if (!TextUtils.isEmpty(currentCode) && !TextUtils.isEmpty(currentName)) {
                                    Channel channel = new Channel();
                                    channel.setChannelCode(currentCode);
                                    channel.setChannelName(currentName);
                                    channelList.add(channel);

                                    currentCode = null;
                                    currentName = null;
                                }
                            }

                            if (readyToGetName == true) {
                                currentName = data;
                                readyToGetName = false;
                                if (!TextUtils.isEmpty(currentCode) && !TextUtils.isEmpty(currentName)) {
                                    Channel channel = new Channel();
                                    channel.setChannelCode(currentCode);
                                    channel.setChannelName(currentName);
                                    channelList.add(channel);

                                    currentCode = null;
                                    currentName = null;
                                }
                            }
                        }
                        eventType = xpp.next();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    callback.onFail(e.getMessage());
                }
                callback.onSuccess(channelList);
            }

            @Override
            public void onFail(String error) {
                callback.onFail(error);
            }
        });
    }

    public void loadListCusGrp(String UserCode, final GetListCusGrp callback) {
        String link = String.format(getURL() + Config.CUSTGRP);

        List<Param> params = new ArrayList<>();
        Param param = new Param("UserCode", UserCode);
        params.add(param);
        mNetwork.executePost(link, params, new Network.Callback() {
            @Override
            public void onCallBack(String response) {
                XmlPullParserFactory factory = null;
                List<CustomerGroup> customerGroups = new ArrayList<>();
                try {
                    boolean readyToGetCode = false;
                    boolean readyToGetName = false;

                    String currentCode = null;
                    String currentName = null;

                    factory = XmlPullParserFactory.newInstance();
                    factory.setNamespaceAware(true);
                    XmlPullParser xpp = factory.newPullParser();

                    xpp.setInput(new StringReader(response));
                    int eventType = xpp.getEventType();
                    while (eventType != XmlPullParser.END_DOCUMENT) {
                        if (eventType == XmlPullParser.START_DOCUMENT) {
                            //TODO: do nothing
                        } else if (eventType == XmlPullParser.START_TAG) {
                            if (xpp.getName().equalsIgnoreCase("GrpCode")) {
                                readyToGetCode = true;
                            }

                            if (xpp.getName().equalsIgnoreCase("GrpName")) {
                                readyToGetName = true;
                            }

                        } else if (eventType == XmlPullParser.TEXT) {
                            String data = xpp.getText();

                            if (readyToGetCode == true) {
                                currentCode = data;
                                readyToGetCode = false;
                                if (!TextUtils.isEmpty(currentCode) && !TextUtils.isEmpty(currentName)) {
                                    CustomerGroup customerGroup = new CustomerGroup();
                                    customerGroup.setGrpCode(currentCode);
                                    customerGroup.setGrpName(currentName);
                                    customerGroups.add(customerGroup);

                                    currentCode = null;
                                    currentName = null;
                                }
                            }

                            if (readyToGetName == true) {
                                currentName = data;
                                readyToGetName = false;
                                if (!TextUtils.isEmpty(currentCode) && !TextUtils.isEmpty(currentName)) {
                                    CustomerGroup customerGroup = new CustomerGroup();
                                    customerGroup.setGrpCode(currentCode);
                                    customerGroup.setGrpName(currentName);
                                    customerGroups.add(customerGroup);

                                    currentCode = null;
                                    currentName = null;
                                }
                            }
                        }
                        eventType = xpp.next();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    callback.onFail(e.getMessage());
                }
                callback.onSuccess(customerGroups);
            }

            @Override
            public void onFail(String error) {
                callback.onFail(error);
            }
        });
    }

    public void loadListTypeVisit(String UserCode, final GetListTypeVisit callback) {
        String link = String.format(getURL() + Config.TYPEVISIT);

        List<Param> params = new ArrayList<>();
        Param param = new Param("UserCode", UserCode);
        params.add(param);
        mNetwork.executePost(link, params, new Network.Callback() {
            @Override
            public void onCallBack(String response) {
                XmlPullParserFactory factory = null;
                List<TypeVisit> typeVisits = new ArrayList<>();
                try {
                    boolean readyToGetCode = false;
                    boolean readyToGetName = false;

                    String currentCode = null;
                    String currentName = null;

                    factory = XmlPullParserFactory.newInstance();
                    factory.setNamespaceAware(true);
                    XmlPullParser xpp = factory.newPullParser();

                    xpp.setInput(new StringReader(response));
                    int eventType = xpp.getEventType();
                    while (eventType != XmlPullParser.END_DOCUMENT) {
                        if (eventType == XmlPullParser.START_DOCUMENT) {
                            //TODO: do nothing
                        } else if (eventType == XmlPullParser.START_TAG) {
                            if (xpp.getName().equalsIgnoreCase("StatusCode")) {
                                readyToGetCode = true;
                            }

                            if (xpp.getName().equalsIgnoreCase("StatusName")) {
                                readyToGetName = true;
                            }

                        } else if (eventType == XmlPullParser.TEXT) {
                            String data = xpp.getText();

                            if (readyToGetCode == true) {
                                currentCode = data;
                                readyToGetCode = false;
                                if (!TextUtils.isEmpty(currentCode) && !TextUtils.isEmpty(currentName)) {
                                    TypeVisit typeVisit = new TypeVisit();
                                    typeVisit.setStatusCode(currentCode);
                                    typeVisit.setStatusName(currentName);
                                    typeVisits.add(typeVisit);

                                    currentCode = null;
                                    currentName = null;
                                }
                            }

                            if (readyToGetName == true) {
                                currentName = data;
                                readyToGetName = false;
                                if (!TextUtils.isEmpty(currentCode) && !TextUtils.isEmpty(currentName)) {
                                    TypeVisit typeVisit = new TypeVisit();
                                    typeVisit.setStatusCode(currentCode);
                                    typeVisit.setStatusName(currentName);
                                    typeVisits.add(typeVisit);

                                    currentCode = null;
                                    currentName = null;
                                }
                            }
                        }
                        eventType = xpp.next();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    callback.onFail(e.getMessage());
                }
                callback.onSuccess(typeVisits);
            }

            @Override
            public void onFail(String error) {
                callback.onFail(error);
            }
        });
    }

    public void loadListTypeOrder(String UserCode, final GetListTypeOrder callback) {
        String link = String.format(getURL() + Config.TYPEORDER);

        List<Param> params = new ArrayList<>();
        Param param = new Param("UserCode", UserCode);
        params.add(param);
        mNetwork.executePost(link, params, new Network.Callback() {
            @Override
            public void onCallBack(String response) {
                XmlPullParserFactory factory = null;
                List<TypeOrder> typeOrders = new ArrayList<>();
                try {
                    boolean readyToGetCode = false;
                    boolean readyToGetName = false;

                    String currentCode = null;
                    String currentName = null;

                    factory = XmlPullParserFactory.newInstance();
                    factory.setNamespaceAware(true);
                    XmlPullParser xpp = factory.newPullParser();

                    xpp.setInput(new StringReader(response));
                    int eventType = xpp.getEventType();
                    while (eventType != XmlPullParser.END_DOCUMENT) {
                        if (eventType == XmlPullParser.START_DOCUMENT) {
                            //TODO: do nothing
                        } else if (eventType == XmlPullParser.START_TAG) {
                            if (xpp.getName().equalsIgnoreCase("TypeCode")) {
                                readyToGetCode = true;
                            }

                            if (xpp.getName().equalsIgnoreCase("TypeName")) {
                                readyToGetName = true;
                            }

                        } else if (eventType == XmlPullParser.TEXT) {
                            String data = xpp.getText();

                            if (readyToGetCode == true) {
                                currentCode = data;
                                readyToGetCode = false;
                                if (!TextUtils.isEmpty(currentCode) && !TextUtils.isEmpty(currentName)) {
                                    TypeOrder typeOrder = new TypeOrder();
                                    typeOrder.setTypeCode(currentCode);
                                    typeOrder.setTypeName(currentName);
                                    typeOrders.add(typeOrder);

                                    currentCode = null;
                                    currentName = null;
                                }
                            }

                            if (readyToGetName == true) {
                                currentName = data;
                                readyToGetName = false;
                                if (!TextUtils.isEmpty(currentCode) && !TextUtils.isEmpty(currentName)) {
                                    TypeOrder typeOrder = new TypeOrder();
                                    typeOrder.setTypeCode(currentCode);
                                    typeOrder.setTypeName(currentName);
                                    typeOrders.add(typeOrder);

                                    currentCode = null;
                                    currentName = null;
                                }
                            }
                        }
                        eventType = xpp.next();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    callback.onFail(e.getMessage());
                }
                callback.onSuccess(typeOrders);
            }

            @Override
            public void onFail(String error) {
                callback.onFail(error);
            }
        });
    }

    public void loadListStatusOrder(String UserCode, final GetListStatusOrder callback) {
        String link = String.format(getURL() + Config.STATUSORDER);

        List<Param> params = new ArrayList<>();
        Param param = new Param("UserCode", UserCode);
        params.add(param);
        mNetwork.executePost(link, params, new Network.Callback() {
            @Override
            public void onCallBack(String response) {
                XmlPullParserFactory factory = null;
                List<StatusOrder> statusOrders = new ArrayList<>();
                try {
                    boolean readyToGetCode = false;
                    boolean readyToGetName = false;

                    String currentCode = null;
                    String currentName = null;

                    factory = XmlPullParserFactory.newInstance();
                    factory.setNamespaceAware(true);
                    XmlPullParser xpp = factory.newPullParser();

                    xpp.setInput(new StringReader(response));
                    int eventType = xpp.getEventType();
                    while (eventType != XmlPullParser.END_DOCUMENT) {
                        if (eventType == XmlPullParser.START_DOCUMENT) {
                            //TODO: do nothing
                        } else if (eventType == XmlPullParser.START_TAG) {
                            if (xpp.getName().equalsIgnoreCase("StatusCode")) {
                                readyToGetCode = true;
                            }

                            if (xpp.getName().equalsIgnoreCase("StatusName")) {
                                readyToGetName = true;
                            }

                        } else if (eventType == XmlPullParser.TEXT) {
                            String data = xpp.getText();

                            if (readyToGetCode == true) {
                                currentCode = data;
                                readyToGetCode = false;
                                if (!TextUtils.isEmpty(currentCode) && !TextUtils.isEmpty(currentName)) {
                                    StatusOrder statusOrder = new StatusOrder();
                                    statusOrder.setStatusCode(currentCode);
                                    statusOrder.setStatusName(currentName);
                                    statusOrders.add(statusOrder);

                                    currentCode = null;
                                    currentName = null;
                                }
                            }

                            if (readyToGetName == true) {
                                currentName = data;
                                readyToGetName = false;
                                if (!TextUtils.isEmpty(currentCode) && !TextUtils.isEmpty(currentName)) {
                                    StatusOrder statusOrder = new StatusOrder();
                                    statusOrder.setStatusCode(currentCode);
                                    statusOrder.setStatusName(currentName);
                                    statusOrders.add(statusOrder);

                                    currentCode = null;
                                    currentName = null;
                                }
                            }
                        }
                        eventType = xpp.next();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    callback.onFail(e.getMessage());
                }
                callback.onSuccess(statusOrders);
            }

            @Override
            public void onFail(String error) {
                callback.onFail(error);
            }
        });
    }

    public void loadListWareHouse(String UserCode, final GetListWareHouse callback) {
        String link = String.format(getURL() + Config.WAREHOUSE);

        List<Param> params = new ArrayList<>();
        Param param = new Param("UserCode", UserCode);
        params.add(param);
        mNetwork.executePost(link, params, new Network.Callback() {
            @Override
            public void onCallBack(String response) {
                XmlPullParserFactory factory = null;
                List<WareHouse> wareHouses = new ArrayList<>();
                try {
                    boolean readyToGetCode = false;
                    boolean readyToGetName = false;

                    String currentCode = null;
                    String currentName = null;

                    factory = XmlPullParserFactory.newInstance();
                    factory.setNamespaceAware(true);
                    XmlPullParser xpp = factory.newPullParser();

                    xpp.setInput(new StringReader(response));
                    int eventType = xpp.getEventType();
                    while (eventType != XmlPullParser.END_DOCUMENT) {
                        if (eventType == XmlPullParser.START_DOCUMENT) {
                            //TODO: do nothing
                        } else if (eventType == XmlPullParser.START_TAG) {
                            if (xpp.getName().equalsIgnoreCase("WhsCode")) {
                                readyToGetCode = true;
                            }

                            if (xpp.getName().equalsIgnoreCase("WhsName")) {
                                readyToGetName = true;
                            }

                        } else if (eventType == XmlPullParser.TEXT) {
                            String data = xpp.getText();

                            if (readyToGetCode == true) {
                                currentCode = data;
                                readyToGetCode = false;
                                if (!TextUtils.isEmpty(currentCode) && !TextUtils.isEmpty(currentName)) {
                                    WareHouse wareHouse = new WareHouse();
                                    wareHouse.setWhsCode(currentCode);
                                    wareHouse.setWhsName(currentName);
                                    wareHouses.add(wareHouse);

                                    currentCode = null;
                                    currentName = null;
                                }
                            }

                            if (readyToGetName == true) {
                                currentName = data;
                                readyToGetName = false;
                                if (!TextUtils.isEmpty(currentCode) && !TextUtils.isEmpty(currentName)) {
                                    WareHouse wareHouse = new WareHouse();
                                    wareHouse.setWhsCode(currentCode);
                                    wareHouse.setWhsName(currentName);
                                    wareHouses.add(wareHouse);

                                    currentCode = null;
                                    currentName = null;
                                }
                            }
                        }
                        eventType = xpp.next();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    callback.onFail(e.getMessage());
                }
                callback.onSuccess(wareHouses);
            }

            @Override
            public void onFail(String error) {
                callback.onFail(error);
            }
        });
    }

    public void loadListOrder(String UserCode, String fromdate, String todate, String docStatus,
                              String visitStatus, String orderType, String route, String text, final GetListOrder callback) {
        String link = String.format(getURL() + Config.LISTORDER);

        List<Param> params = new ArrayList<>();
        Param param = new Param("UserCode", UserCode);
        params.add(param);
        param = new Param("FromDate", fromdate);
        params.add(param);
        param = new Param("ToDate", todate);
        params.add(param);
        param = new Param("DocStatus", docStatus);
        params.add(param);
        param = new Param("VisitStatus", visitStatus);
        params.add(param);
        param = new Param("OrderType", orderType);
        params.add(param);
        param = new Param("Route", route);
        params.add(param);
        param = new Param("FreeText", text);
        params.add(param);
        mNetwork.executePost(link, params, new Network.Callback() {
            @Override
            public void onCallBack(String response) {
                XmlPullParserFactory factory = null;
                List<Orders> ordersList = new ArrayList<>();
                try {
                    boolean readyToDocEntry = false;
                    boolean readyToCardCode = false;
                    boolean readyToCardName = false;
                    boolean readyToAddress = false;
                    boolean readyToRoute = false;
                    boolean readyToOrderType = false;
                    boolean readyToOrderTypeName = false;
                    boolean readyToPONo = false;
                    boolean readyToPODate = false;
                    boolean readyToDeliveryNo = false;
                    boolean readyToDeliveryDate = false;
                    boolean readyToDocStatus = false;
                    boolean readyToDocStatusName = false;
                    boolean readyToSalesEmp = false;
                    boolean readyToDeliveryEmp = false;
                    boolean readyToDocTotal = false;
                    boolean readyToDocVATTotal = false;
                    boolean readyToDocGToal = false;
                    boolean readyToRemark = false;
                    boolean readyToCreateDate = false;

                    String DocEntry = null;
                    String CardCode = null;
                    String CardName = null;
                    String Address = null;
                    String Route = null;
                    String OrderType = null;
                    String OrderTypeName = null;
                    String PONo = null;
                    String PODate = null;
                    String DeliveryNo = null;
                    String DeliveryDate = null;
                    String DocStatus = null;
                    String DocStatusName = null;
                    String SalesEmp = null;
                    String DeliveryEmp = null;
                    String DocTotal = null;
                    String DocVATTotal = null;
                    String DocGToal = null;
                    String Remark = null;
                    String CreateDate = null;

                    factory = XmlPullParserFactory.newInstance();
                    factory.setNamespaceAware(true);
                    XmlPullParser xpp = factory.newPullParser();

                    xpp.setInput(new StringReader(response));
                    int eventType = xpp.getEventType();
                    while (eventType != XmlPullParser.END_DOCUMENT) {
                        if (eventType == XmlPullParser.START_DOCUMENT) {
                            //TODO: do nothing
                        } else if (eventType == XmlPullParser.START_TAG) {
                            if (xpp.getName().equalsIgnoreCase("DocEntry")) {
                                readyToDocEntry = true;
                            }

                            if (xpp.getName().equalsIgnoreCase("CardCode")) {
                                readyToCardCode = true;
                            }

                            if (xpp.getName().equalsIgnoreCase("CardName")) {
                                readyToCardName = true;
                            }

                            if (xpp.getName().equalsIgnoreCase("Address")) {
                                readyToAddress = true;
                            }

                            if (xpp.getName().equalsIgnoreCase("Route")) {
                                readyToRoute = true;
                            }

                            if (xpp.getName().equalsIgnoreCase("OrderType")) {
                                readyToOrderType = true;
                            }

                            if (xpp.getName().equalsIgnoreCase("OrderTypeName")) {
                                readyToOrderTypeName = true;
                            }

                            if (xpp.getName().equalsIgnoreCase("PONo")) {
                                readyToPONo = true;
                            }

                            if (xpp.getName().equalsIgnoreCase("PODate")) {
                                readyToPODate = true;
                            }

                            if (xpp.getName().equalsIgnoreCase("DeliveryNo")) {
                                readyToDeliveryNo = true;
                            }

                            if (xpp.getName().equalsIgnoreCase("DeliveryDate")) {
                                readyToDeliveryDate = true;
                            }

                            if (xpp.getName().equalsIgnoreCase("DocStatus")) {
                                readyToDocStatus = true;
                            }

                            if (xpp.getName().equalsIgnoreCase("DocStatusName")) {
                                readyToDocStatusName = true;
                            }

                            if (xpp.getName().equalsIgnoreCase("SalesEmp")) {
                                readyToSalesEmp = true;
                            }

                            if (xpp.getName().equalsIgnoreCase("DeliveryEmp")) {
                                readyToDeliveryEmp = true;
                            }

                            if (xpp.getName().equalsIgnoreCase("DocTotal")) {
                                readyToDocTotal = true;
                            }

                            if (xpp.getName().equalsIgnoreCase("DocVATTotal")) {
                                readyToDocVATTotal = true;
                            }

                            if (xpp.getName().equalsIgnoreCase("DocGToal")) {
                                readyToDocGToal = true;
                            }

                            if (xpp.getName().equalsIgnoreCase("Remark")) {
                                readyToRemark = true;
                            }

                            if (xpp.getName().equalsIgnoreCase("CreateDate")) {
                                readyToCreateDate = true;
                            }

                        } else if (eventType == XmlPullParser.TEXT) {
                            String data = xpp.getText();

                            if (readyToDocEntry) {
                                DocEntry = data;
                                readyToDocEntry = false;
                                if (!TextUtils.isEmpty(DocEntry) && !TextUtils.isEmpty(CardCode) &&
                                        !TextUtils.isEmpty(CardName) && !TextUtils.isEmpty(Address) &&
                                        !TextUtils.isEmpty(Route) && !TextUtils.isEmpty(OrderType) &&
                                        !TextUtils.isEmpty(OrderTypeName) && !TextUtils.isEmpty(PONo) &&
                                        !TextUtils.isEmpty(PODate) && !TextUtils.isEmpty(DeliveryNo) &&
                                        !TextUtils.isEmpty(DeliveryDate) && !TextUtils.isEmpty(DocStatus) &&
                                        !TextUtils.isEmpty(DocStatusName) && !TextUtils.isEmpty(SalesEmp) &&
                                        !TextUtils.isEmpty(DeliveryEmp) && !TextUtils.isEmpty(DocTotal) &&
                                        !TextUtils.isEmpty(DocVATTotal) && !TextUtils.isEmpty(DocGToal) &&
                                        !TextUtils.isEmpty(Remark) && !TextUtils.isEmpty(CreateDate)) {

                                    Orders orders = new Orders();
                                    orders.setDocEntry(DocEntry);
                                    orders.setCardCode(CardCode);
                                    orders.setCardName(CardName);
                                    orders.setAddress(Address);
                                    orders.setRoute(Route);
                                    orders.setOrderType(OrderType);
                                    orders.setOrderTypeName(OrderTypeName);
                                    orders.setPONo(PONo);
                                    orders.setPODate(PODate);
                                    orders.setDeliveryNo(DeliveryNo);
                                    orders.setDeliveryDate(DeliveryDate);
                                    orders.setDocStatus(DocStatus);
                                    orders.setDocStatusName(DocStatusName);
                                    orders.setSalesEmp(SalesEmp);
                                    orders.setDeliveryEmp(DeliveryEmp);
                                    orders.setDocTotal(DocTotal);
                                    orders.setDocVATTotal(DocVATTotal);
                                    orders.setDocGToal(DocGToal);
                                    orders.setRemark(Remark);
                                    orders.setCreateDate(CreateDate);
                                    ordersList.add(orders);

                                    DocEntry = null;
                                    CardCode = null;
                                    CardName = null;
                                    Address = null;
                                    Route = null;
                                    OrderType = null;
                                    OrderTypeName = null;
                                    PONo = null;
                                    PODate = null;
                                    DeliveryNo = null;
                                    DeliveryDate = null;
                                    DocStatus = null;
                                    DocStatusName = null;
                                    SalesEmp = null;
                                    DeliveryEmp = null;
                                    DocTotal = null;
                                    DocVATTotal = null;
                                    DocGToal = null;
                                    Remark = null;
                                    CreateDate = null;
                                }
                            }

                            if (readyToCardCode) {
                                CardCode = data;
                                readyToCardCode = false;
                                if (!TextUtils.isEmpty(DocEntry) && !TextUtils.isEmpty(CardCode) &&
                                        !TextUtils.isEmpty(CardName) && !TextUtils.isEmpty(Address) &&
                                        !TextUtils.isEmpty(Route) && !TextUtils.isEmpty(OrderType) &&
                                        !TextUtils.isEmpty(OrderTypeName) && !TextUtils.isEmpty(PONo) &&
                                        !TextUtils.isEmpty(PODate) && !TextUtils.isEmpty(DeliveryNo) &&
                                        !TextUtils.isEmpty(DeliveryDate) && !TextUtils.isEmpty(DocStatus) &&
                                        !TextUtils.isEmpty(DocStatusName) && !TextUtils.isEmpty(SalesEmp) &&
                                        !TextUtils.isEmpty(DeliveryEmp) && !TextUtils.isEmpty(DocTotal) &&
                                        !TextUtils.isEmpty(DocVATTotal) && !TextUtils.isEmpty(DocGToal) &&
                                        !TextUtils.isEmpty(Remark) && !TextUtils.isEmpty(CreateDate)) {

                                    Orders orders = new Orders();
                                    orders.setDocEntry(DocEntry);
                                    orders.setCardCode(CardCode);
                                    orders.setCardName(CardName);
                                    orders.setAddress(Address);
                                    orders.setRoute(Route);
                                    orders.setOrderType(OrderType);
                                    orders.setOrderTypeName(OrderTypeName);
                                    orders.setPONo(PONo);
                                    orders.setPODate(PODate);
                                    orders.setDeliveryNo(DeliveryNo);
                                    orders.setDeliveryDate(DeliveryDate);
                                    orders.setDocStatus(DocStatus);
                                    orders.setDocStatusName(DocStatusName);
                                    orders.setSalesEmp(SalesEmp);
                                    orders.setDeliveryEmp(DeliveryEmp);
                                    orders.setDocTotal(DocTotal);
                                    orders.setDocVATTotal(DocVATTotal);
                                    orders.setDocGToal(DocGToal);
                                    orders.setRemark(Remark);
                                    orders.setCreateDate(CreateDate);
                                    ordersList.add(orders);

                                    DocEntry = null;
                                    CardCode = null;
                                    CardName = null;
                                    Address = null;
                                    Route = null;
                                    OrderType = null;
                                    OrderTypeName = null;
                                    PONo = null;
                                    PODate = null;
                                    DeliveryNo = null;
                                    DeliveryDate = null;
                                    DocStatus = null;
                                    DocStatusName = null;
                                    SalesEmp = null;
                                    DeliveryEmp = null;
                                    DocTotal = null;
                                    DocVATTotal = null;
                                    DocGToal = null;
                                    Remark = null;
                                    CreateDate = null;
                                }
                            }

                            if (readyToCardName) {
                                CardName = data;
                                readyToCardName = false;
                                if (!TextUtils.isEmpty(DocEntry) && !TextUtils.isEmpty(CardCode) &&
                                        !TextUtils.isEmpty(CardName) && !TextUtils.isEmpty(Address) &&
                                        !TextUtils.isEmpty(Route) && !TextUtils.isEmpty(OrderType) &&
                                        !TextUtils.isEmpty(OrderTypeName) && !TextUtils.isEmpty(PONo) &&
                                        !TextUtils.isEmpty(PODate) && !TextUtils.isEmpty(DeliveryNo) &&
                                        !TextUtils.isEmpty(DeliveryDate) && !TextUtils.isEmpty(DocStatus) &&
                                        !TextUtils.isEmpty(DocStatusName) && !TextUtils.isEmpty(SalesEmp) &&
                                        !TextUtils.isEmpty(DeliveryEmp) && !TextUtils.isEmpty(DocTotal) &&
                                        !TextUtils.isEmpty(DocVATTotal) && !TextUtils.isEmpty(DocGToal) &&
                                        !TextUtils.isEmpty(Remark) && !TextUtils.isEmpty(CreateDate)) {

                                    Orders orders = new Orders();
                                    orders.setDocEntry(DocEntry);
                                    orders.setCardCode(CardCode);
                                    orders.setCardName(CardName);
                                    orders.setAddress(Address);
                                    orders.setRoute(Route);
                                    orders.setOrderType(OrderType);
                                    orders.setOrderTypeName(OrderTypeName);
                                    orders.setPONo(PONo);
                                    orders.setPODate(PODate);
                                    orders.setDeliveryNo(DeliveryNo);
                                    orders.setDeliveryDate(DeliveryDate);
                                    orders.setDocStatus(DocStatus);
                                    orders.setDocStatusName(DocStatusName);
                                    orders.setSalesEmp(SalesEmp);
                                    orders.setDeliveryEmp(DeliveryEmp);
                                    orders.setDocTotal(DocTotal);
                                    orders.setDocVATTotal(DocVATTotal);
                                    orders.setDocGToal(DocGToal);
                                    orders.setRemark(Remark);
                                    orders.setCreateDate(CreateDate);
                                    ordersList.add(orders);

                                    DocEntry = null;
                                    CardCode = null;
                                    CardName = null;
                                    Address = null;
                                    Route = null;
                                    OrderType = null;
                                    OrderTypeName = null;
                                    PONo = null;
                                    PODate = null;
                                    DeliveryNo = null;
                                    DeliveryDate = null;
                                    DocStatus = null;
                                    DocStatusName = null;
                                    SalesEmp = null;
                                    DeliveryEmp = null;
                                    DocTotal = null;
                                    DocVATTotal = null;
                                    DocGToal = null;
                                    Remark = null;
                                    CreateDate = null;
                                }
                            }

                            if (readyToAddress) {
                                Address = data;
                                readyToAddress = false;
                                if (!TextUtils.isEmpty(DocEntry) && !TextUtils.isEmpty(CardCode) &&
                                        !TextUtils.isEmpty(CardName) && !TextUtils.isEmpty(Address) &&
                                        !TextUtils.isEmpty(Route) && !TextUtils.isEmpty(OrderType) &&
                                        !TextUtils.isEmpty(OrderTypeName) && !TextUtils.isEmpty(PONo) &&
                                        !TextUtils.isEmpty(PODate) && !TextUtils.isEmpty(DeliveryNo) &&
                                        !TextUtils.isEmpty(DeliveryDate) && !TextUtils.isEmpty(DocStatus) &&
                                        !TextUtils.isEmpty(DocStatusName) && !TextUtils.isEmpty(SalesEmp) &&
                                        !TextUtils.isEmpty(DeliveryEmp) && !TextUtils.isEmpty(DocTotal) &&
                                        !TextUtils.isEmpty(DocVATTotal) && !TextUtils.isEmpty(DocGToal) &&
                                        !TextUtils.isEmpty(Remark) && !TextUtils.isEmpty(CreateDate)) {

                                    Orders orders = new Orders();
                                    orders.setDocEntry(DocEntry);
                                    orders.setCardCode(CardCode);
                                    orders.setCardName(CardName);
                                    orders.setAddress(Address);
                                    orders.setRoute(Route);
                                    orders.setOrderType(OrderType);
                                    orders.setOrderTypeName(OrderTypeName);
                                    orders.setPONo(PONo);
                                    orders.setPODate(PODate);
                                    orders.setDeliveryNo(DeliveryNo);
                                    orders.setDeliveryDate(DeliveryDate);
                                    orders.setDocStatus(DocStatus);
                                    orders.setDocStatusName(DocStatusName);
                                    orders.setSalesEmp(SalesEmp);
                                    orders.setDeliveryEmp(DeliveryEmp);
                                    orders.setDocTotal(DocTotal);
                                    orders.setDocVATTotal(DocVATTotal);
                                    orders.setDocGToal(DocGToal);
                                    orders.setRemark(Remark);
                                    orders.setCreateDate(CreateDate);
                                    ordersList.add(orders);

                                    DocEntry = null;
                                    CardCode = null;
                                    CardName = null;
                                    Address = null;
                                    Route = null;
                                    OrderType = null;
                                    OrderTypeName = null;
                                    PONo = null;
                                    PODate = null;
                                    DeliveryNo = null;
                                    DeliveryDate = null;
                                    DocStatus = null;
                                    DocStatusName = null;
                                    SalesEmp = null;
                                    DeliveryEmp = null;
                                    DocTotal = null;
                                    DocVATTotal = null;
                                    DocGToal = null;
                                    Remark = null;
                                    CreateDate = null;
                                }
                            }

                            if (readyToRoute) {
                                Route = data;
                                readyToRoute = false;
                                if (!TextUtils.isEmpty(DocEntry) && !TextUtils.isEmpty(CardCode) &&
                                        !TextUtils.isEmpty(CardName) && !TextUtils.isEmpty(Address) &&
                                        !TextUtils.isEmpty(Route) && !TextUtils.isEmpty(OrderType) &&
                                        !TextUtils.isEmpty(OrderTypeName) && !TextUtils.isEmpty(PONo) &&
                                        !TextUtils.isEmpty(PODate) && !TextUtils.isEmpty(DeliveryNo) &&
                                        !TextUtils.isEmpty(DeliveryDate) && !TextUtils.isEmpty(DocStatus) &&
                                        !TextUtils.isEmpty(DocStatusName) && !TextUtils.isEmpty(SalesEmp) &&
                                        !TextUtils.isEmpty(DeliveryEmp) && !TextUtils.isEmpty(DocTotal) &&
                                        !TextUtils.isEmpty(DocVATTotal) && !TextUtils.isEmpty(DocGToal) &&
                                        !TextUtils.isEmpty(Remark) && !TextUtils.isEmpty(CreateDate)) {

                                    Orders orders = new Orders();
                                    orders.setDocEntry(DocEntry);
                                    orders.setCardCode(CardCode);
                                    orders.setCardName(CardName);
                                    orders.setAddress(Address);
                                    orders.setRoute(Route);
                                    orders.setOrderType(OrderType);
                                    orders.setOrderTypeName(OrderTypeName);
                                    orders.setPONo(PONo);
                                    orders.setPODate(PODate);
                                    orders.setDeliveryNo(DeliveryNo);
                                    orders.setDeliveryDate(DeliveryDate);
                                    orders.setDocStatus(DocStatus);
                                    orders.setDocStatusName(DocStatusName);
                                    orders.setSalesEmp(SalesEmp);
                                    orders.setDeliveryEmp(DeliveryEmp);
                                    orders.setDocTotal(DocTotal);
                                    orders.setDocVATTotal(DocVATTotal);
                                    orders.setDocGToal(DocGToal);
                                    orders.setRemark(Remark);
                                    orders.setCreateDate(CreateDate);
                                    ordersList.add(orders);

                                    DocEntry = null;
                                    CardCode = null;
                                    CardName = null;
                                    Address = null;
                                    Route = null;
                                    OrderType = null;
                                    OrderTypeName = null;
                                    PONo = null;
                                    PODate = null;
                                    DeliveryNo = null;
                                    DeliveryDate = null;
                                    DocStatus = null;
                                    DocStatusName = null;
                                    SalesEmp = null;
                                    DeliveryEmp = null;
                                    DocTotal = null;
                                    DocVATTotal = null;
                                    DocGToal = null;
                                    Remark = null;
                                    CreateDate = null;
                                }
                            }

                            if (readyToOrderType) {
                                OrderType = data;
                                readyToOrderType = false;
                                if (!TextUtils.isEmpty(DocEntry) && !TextUtils.isEmpty(CardCode) &&
                                        !TextUtils.isEmpty(CardName) && !TextUtils.isEmpty(Address) &&
                                        !TextUtils.isEmpty(Route) && !TextUtils.isEmpty(OrderType) &&
                                        !TextUtils.isEmpty(OrderTypeName) && !TextUtils.isEmpty(PONo) &&
                                        !TextUtils.isEmpty(PODate) && !TextUtils.isEmpty(DeliveryNo) &&
                                        !TextUtils.isEmpty(DeliveryDate) && !TextUtils.isEmpty(DocStatus) &&
                                        !TextUtils.isEmpty(DocStatusName) && !TextUtils.isEmpty(SalesEmp) &&
                                        !TextUtils.isEmpty(DeliveryEmp) && !TextUtils.isEmpty(DocTotal) &&
                                        !TextUtils.isEmpty(DocVATTotal) && !TextUtils.isEmpty(DocGToal) &&
                                        !TextUtils.isEmpty(Remark) && !TextUtils.isEmpty(CreateDate)) {

                                    Orders orders = new Orders();
                                    orders.setDocEntry(DocEntry);
                                    orders.setCardCode(CardCode);
                                    orders.setCardName(CardName);
                                    orders.setAddress(Address);
                                    orders.setRoute(Route);
                                    orders.setOrderType(OrderType);
                                    orders.setOrderTypeName(OrderTypeName);
                                    orders.setPONo(PONo);
                                    orders.setPODate(PODate);
                                    orders.setDeliveryNo(DeliveryNo);
                                    orders.setDeliveryDate(DeliveryDate);
                                    orders.setDocStatus(DocStatus);
                                    orders.setDocStatusName(DocStatusName);
                                    orders.setSalesEmp(SalesEmp);
                                    orders.setDeliveryEmp(DeliveryEmp);
                                    orders.setDocTotal(DocTotal);
                                    orders.setDocVATTotal(DocVATTotal);
                                    orders.setDocGToal(DocGToal);
                                    orders.setRemark(Remark);
                                    orders.setCreateDate(CreateDate);
                                    ordersList.add(orders);

                                    DocEntry = null;
                                    CardCode = null;
                                    CardName = null;
                                    Address = null;
                                    Route = null;
                                    OrderType = null;
                                    OrderTypeName = null;
                                    PONo = null;
                                    PODate = null;
                                    DeliveryNo = null;
                                    DeliveryDate = null;
                                    DocStatus = null;
                                    DocStatusName = null;
                                    SalesEmp = null;
                                    DeliveryEmp = null;
                                    DocTotal = null;
                                    DocVATTotal = null;
                                    DocGToal = null;
                                    Remark = null;
                                    CreateDate = null;
                                }
                            }

                            if (readyToOrderTypeName) {
                                OrderTypeName = data;
                                readyToOrderTypeName = false;
                                if (!TextUtils.isEmpty(DocEntry) && !TextUtils.isEmpty(CardCode) &&
                                        !TextUtils.isEmpty(CardName) && !TextUtils.isEmpty(Address) &&
                                        !TextUtils.isEmpty(Route) && !TextUtils.isEmpty(OrderType) &&
                                        !TextUtils.isEmpty(OrderTypeName) && !TextUtils.isEmpty(PONo) &&
                                        !TextUtils.isEmpty(PODate) && !TextUtils.isEmpty(DeliveryNo) &&
                                        !TextUtils.isEmpty(DeliveryDate) && !TextUtils.isEmpty(DocStatus) &&
                                        !TextUtils.isEmpty(DocStatusName) && !TextUtils.isEmpty(SalesEmp) &&
                                        !TextUtils.isEmpty(DeliveryEmp) && !TextUtils.isEmpty(DocTotal) &&
                                        !TextUtils.isEmpty(DocVATTotal) && !TextUtils.isEmpty(DocGToal) &&
                                        !TextUtils.isEmpty(Remark) && !TextUtils.isEmpty(CreateDate)) {

                                    Orders orders = new Orders();
                                    orders.setDocEntry(DocEntry);
                                    orders.setCardCode(CardCode);
                                    orders.setCardName(CardName);
                                    orders.setAddress(Address);
                                    orders.setRoute(Route);
                                    orders.setOrderType(OrderType);
                                    orders.setOrderTypeName(OrderTypeName);
                                    orders.setPONo(PONo);
                                    orders.setPODate(PODate);
                                    orders.setDeliveryNo(DeliveryNo);
                                    orders.setDeliveryDate(DeliveryDate);
                                    orders.setDocStatus(DocStatus);
                                    orders.setDocStatusName(DocStatusName);
                                    orders.setSalesEmp(SalesEmp);
                                    orders.setDeliveryEmp(DeliveryEmp);
                                    orders.setDocTotal(DocTotal);
                                    orders.setDocVATTotal(DocVATTotal);
                                    orders.setDocGToal(DocGToal);
                                    orders.setRemark(Remark);
                                    orders.setCreateDate(CreateDate);
                                    ordersList.add(orders);

                                    DocEntry = null;
                                    CardCode = null;
                                    CardName = null;
                                    Address = null;
                                    Route = null;
                                    OrderType = null;
                                    OrderTypeName = null;
                                    PONo = null;
                                    PODate = null;
                                    DeliveryNo = null;
                                    DeliveryDate = null;
                                    DocStatus = null;
                                    DocStatusName = null;
                                    SalesEmp = null;
                                    DeliveryEmp = null;
                                    DocTotal = null;
                                    DocVATTotal = null;
                                    DocGToal = null;
                                    Remark = null;
                                    CreateDate = null;
                                }
                            }

                            if (readyToPONo) {
                                PONo = data;
                                readyToPONo = false;
                                if (!TextUtils.isEmpty(DocEntry) && !TextUtils.isEmpty(CardCode) &&
                                        !TextUtils.isEmpty(CardName) && !TextUtils.isEmpty(Address) &&
                                        !TextUtils.isEmpty(Route) && !TextUtils.isEmpty(OrderType) &&
                                        !TextUtils.isEmpty(OrderTypeName) && !TextUtils.isEmpty(PONo) &&
                                        !TextUtils.isEmpty(PODate) && !TextUtils.isEmpty(DeliveryNo) &&
                                        !TextUtils.isEmpty(DeliveryDate) && !TextUtils.isEmpty(DocStatus) &&
                                        !TextUtils.isEmpty(DocStatusName) && !TextUtils.isEmpty(SalesEmp) &&
                                        !TextUtils.isEmpty(DeliveryEmp) && !TextUtils.isEmpty(DocTotal) &&
                                        !TextUtils.isEmpty(DocVATTotal) && !TextUtils.isEmpty(DocGToal) &&
                                        !TextUtils.isEmpty(Remark) && !TextUtils.isEmpty(CreateDate)) {

                                    Orders orders = new Orders();
                                    orders.setDocEntry(DocEntry);
                                    orders.setCardCode(CardCode);
                                    orders.setCardName(CardName);
                                    orders.setAddress(Address);
                                    orders.setRoute(Route);
                                    orders.setOrderType(OrderType);
                                    orders.setOrderTypeName(OrderTypeName);
                                    orders.setPONo(PONo);
                                    orders.setPODate(PODate);
                                    orders.setDeliveryNo(DeliveryNo);
                                    orders.setDeliveryDate(DeliveryDate);
                                    orders.setDocStatus(DocStatus);
                                    orders.setDocStatusName(DocStatusName);
                                    orders.setSalesEmp(SalesEmp);
                                    orders.setDeliveryEmp(DeliveryEmp);
                                    orders.setDocTotal(DocTotal);
                                    orders.setDocVATTotal(DocVATTotal);
                                    orders.setDocGToal(DocGToal);
                                    orders.setRemark(Remark);
                                    orders.setCreateDate(CreateDate);
                                    ordersList.add(orders);

                                    DocEntry = null;
                                    CardCode = null;
                                    CardName = null;
                                    Address = null;
                                    Route = null;
                                    OrderType = null;
                                    OrderTypeName = null;
                                    PONo = null;
                                    PODate = null;
                                    DeliveryNo = null;
                                    DeliveryDate = null;
                                    DocStatus = null;
                                    DocStatusName = null;
                                    SalesEmp = null;
                                    DeliveryEmp = null;
                                    DocTotal = null;
                                    DocVATTotal = null;
                                    DocGToal = null;
                                    Remark = null;
                                    CreateDate = null;
                                }
                            }

                            if (readyToPODate) {
                                PODate = data;
                                readyToPODate = false;
                                if (!TextUtils.isEmpty(DocEntry) && !TextUtils.isEmpty(CardCode) &&
                                        !TextUtils.isEmpty(CardName) && !TextUtils.isEmpty(Address) &&
                                        !TextUtils.isEmpty(Route) && !TextUtils.isEmpty(OrderType) &&
                                        !TextUtils.isEmpty(OrderTypeName) && !TextUtils.isEmpty(PONo) &&
                                        !TextUtils.isEmpty(PODate) && !TextUtils.isEmpty(DeliveryNo) &&
                                        !TextUtils.isEmpty(DeliveryDate) && !TextUtils.isEmpty(DocStatus) &&
                                        !TextUtils.isEmpty(DocStatusName) && !TextUtils.isEmpty(SalesEmp) &&
                                        !TextUtils.isEmpty(DeliveryEmp) && !TextUtils.isEmpty(DocTotal) &&
                                        !TextUtils.isEmpty(DocVATTotal) && !TextUtils.isEmpty(DocGToal) &&
                                        !TextUtils.isEmpty(Remark) && !TextUtils.isEmpty(CreateDate)) {

                                    Orders orders = new Orders();
                                    orders.setDocEntry(DocEntry);
                                    orders.setCardCode(CardCode);
                                    orders.setCardName(CardName);
                                    orders.setAddress(Address);
                                    orders.setRoute(Route);
                                    orders.setOrderType(OrderType);
                                    orders.setOrderTypeName(OrderTypeName);
                                    orders.setPONo(PONo);
                                    orders.setPODate(PODate);
                                    orders.setDeliveryNo(DeliveryNo);
                                    orders.setDeliveryDate(DeliveryDate);
                                    orders.setDocStatus(DocStatus);
                                    orders.setDocStatusName(DocStatusName);
                                    orders.setSalesEmp(SalesEmp);
                                    orders.setDeliveryEmp(DeliveryEmp);
                                    orders.setDocTotal(DocTotal);
                                    orders.setDocVATTotal(DocVATTotal);
                                    orders.setDocGToal(DocGToal);
                                    orders.setRemark(Remark);
                                    orders.setCreateDate(CreateDate);
                                    ordersList.add(orders);

                                    DocEntry = null;
                                    CardCode = null;
                                    CardName = null;
                                    Address = null;
                                    Route = null;
                                    OrderType = null;
                                    OrderTypeName = null;
                                    PONo = null;
                                    PODate = null;
                                    DeliveryNo = null;
                                    DeliveryDate = null;
                                    DocStatus = null;
                                    DocStatusName = null;
                                    SalesEmp = null;
                                    DeliveryEmp = null;
                                    DocTotal = null;
                                    DocVATTotal = null;
                                    DocGToal = null;
                                    Remark = null;
                                    CreateDate = null;
                                }
                            }

                            if (readyToDeliveryNo) {
                                DeliveryNo = data;
                                readyToDeliveryNo = false;
                                if (!TextUtils.isEmpty(DocEntry) && !TextUtils.isEmpty(CardCode) &&
                                        !TextUtils.isEmpty(CardName) && !TextUtils.isEmpty(Address) &&
                                        !TextUtils.isEmpty(Route) && !TextUtils.isEmpty(OrderType) &&
                                        !TextUtils.isEmpty(OrderTypeName) && !TextUtils.isEmpty(PONo) &&
                                        !TextUtils.isEmpty(PODate) && !TextUtils.isEmpty(DeliveryNo) &&
                                        !TextUtils.isEmpty(DeliveryDate) && !TextUtils.isEmpty(DocStatus) &&
                                        !TextUtils.isEmpty(DocStatusName) && !TextUtils.isEmpty(SalesEmp) &&
                                        !TextUtils.isEmpty(DeliveryEmp) && !TextUtils.isEmpty(DocTotal) &&
                                        !TextUtils.isEmpty(DocVATTotal) && !TextUtils.isEmpty(DocGToal) &&
                                        !TextUtils.isEmpty(Remark) && !TextUtils.isEmpty(CreateDate)) {

                                    Orders orders = new Orders();
                                    orders.setDocEntry(DocEntry);
                                    orders.setCardCode(CardCode);
                                    orders.setCardName(CardName);
                                    orders.setAddress(Address);
                                    orders.setRoute(Route);
                                    orders.setOrderType(OrderType);
                                    orders.setOrderTypeName(OrderTypeName);
                                    orders.setPONo(PONo);
                                    orders.setPODate(PODate);
                                    orders.setDeliveryNo(DeliveryNo);
                                    orders.setDeliveryDate(DeliveryDate);
                                    orders.setDocStatus(DocStatus);
                                    orders.setDocStatusName(DocStatusName);
                                    orders.setSalesEmp(SalesEmp);
                                    orders.setDeliveryEmp(DeliveryEmp);
                                    orders.setDocTotal(DocTotal);
                                    orders.setDocVATTotal(DocVATTotal);
                                    orders.setDocGToal(DocGToal);
                                    orders.setRemark(Remark);
                                    orders.setCreateDate(CreateDate);
                                    ordersList.add(orders);

                                    DocEntry = null;
                                    CardCode = null;
                                    CardName = null;
                                    Address = null;
                                    Route = null;
                                    OrderType = null;
                                    OrderTypeName = null;
                                    PONo = null;
                                    PODate = null;
                                    DeliveryNo = null;
                                    DeliveryDate = null;
                                    DocStatus = null;
                                    DocStatusName = null;
                                    SalesEmp = null;
                                    DeliveryEmp = null;
                                    DocTotal = null;
                                    DocVATTotal = null;
                                    DocGToal = null;
                                    Remark = null;
                                    CreateDate = null;
                                }
                            }

                            if (readyToDeliveryDate) {
                                DeliveryDate = data;
                                readyToDeliveryDate = false;
                                if (!TextUtils.isEmpty(DocEntry) && !TextUtils.isEmpty(CardCode) &&
                                        !TextUtils.isEmpty(CardName) && !TextUtils.isEmpty(Address) &&
                                        !TextUtils.isEmpty(Route) && !TextUtils.isEmpty(OrderType) &&
                                        !TextUtils.isEmpty(OrderTypeName) && !TextUtils.isEmpty(PONo) &&
                                        !TextUtils.isEmpty(PODate) && !TextUtils.isEmpty(DeliveryNo) &&
                                        !TextUtils.isEmpty(DeliveryDate) && !TextUtils.isEmpty(DocStatus) &&
                                        !TextUtils.isEmpty(DocStatusName) && !TextUtils.isEmpty(SalesEmp) &&
                                        !TextUtils.isEmpty(DeliveryEmp) && !TextUtils.isEmpty(DocTotal) &&
                                        !TextUtils.isEmpty(DocVATTotal) && !TextUtils.isEmpty(DocGToal) &&
                                        !TextUtils.isEmpty(Remark) && !TextUtils.isEmpty(CreateDate)) {

                                    Orders orders = new Orders();
                                    orders.setDocEntry(DocEntry);
                                    orders.setCardCode(CardCode);
                                    orders.setCardName(CardName);
                                    orders.setAddress(Address);
                                    orders.setRoute(Route);
                                    orders.setOrderType(OrderType);
                                    orders.setOrderTypeName(OrderTypeName);
                                    orders.setPONo(PONo);
                                    orders.setPODate(PODate);
                                    orders.setDeliveryNo(DeliveryNo);
                                    orders.setDeliveryDate(DeliveryDate);
                                    orders.setDocStatus(DocStatus);
                                    orders.setDocStatusName(DocStatusName);
                                    orders.setSalesEmp(SalesEmp);
                                    orders.setDeliveryEmp(DeliveryEmp);
                                    orders.setDocTotal(DocTotal);
                                    orders.setDocVATTotal(DocVATTotal);
                                    orders.setDocGToal(DocGToal);
                                    orders.setRemark(Remark);
                                    orders.setCreateDate(CreateDate);
                                    ordersList.add(orders);

                                    DocEntry = null;
                                    CardCode = null;
                                    CardName = null;
                                    Address = null;
                                    Route = null;
                                    OrderType = null;
                                    OrderTypeName = null;
                                    PONo = null;
                                    PODate = null;
                                    DeliveryNo = null;
                                    DeliveryDate = null;
                                    DocStatus = null;
                                    DocStatusName = null;
                                    SalesEmp = null;
                                    DeliveryEmp = null;
                                    DocTotal = null;
                                    DocVATTotal = null;
                                    DocGToal = null;
                                    Remark = null;
                                    CreateDate = null;
                                }
                            }

                            if (readyToDocStatus) {
                                DocStatus = data;
                                readyToDocStatus = false;
                                if (!TextUtils.isEmpty(DocEntry) && !TextUtils.isEmpty(CardCode) &&
                                        !TextUtils.isEmpty(CardName) && !TextUtils.isEmpty(Address) &&
                                        !TextUtils.isEmpty(Route) && !TextUtils.isEmpty(OrderType) &&
                                        !TextUtils.isEmpty(OrderTypeName) && !TextUtils.isEmpty(PONo) &&
                                        !TextUtils.isEmpty(PODate) && !TextUtils.isEmpty(DeliveryNo) &&
                                        !TextUtils.isEmpty(DeliveryDate) && !TextUtils.isEmpty(DocStatus) &&
                                        !TextUtils.isEmpty(DocStatusName) && !TextUtils.isEmpty(SalesEmp) &&
                                        !TextUtils.isEmpty(DeliveryEmp) && !TextUtils.isEmpty(DocTotal) &&
                                        !TextUtils.isEmpty(DocVATTotal) && !TextUtils.isEmpty(DocGToal) &&
                                        !TextUtils.isEmpty(Remark) && !TextUtils.isEmpty(CreateDate)) {

                                    Orders orders = new Orders();
                                    orders.setDocEntry(DocEntry);
                                    orders.setCardCode(CardCode);
                                    orders.setCardName(CardName);
                                    orders.setAddress(Address);
                                    orders.setRoute(Route);
                                    orders.setOrderType(OrderType);
                                    orders.setOrderTypeName(OrderTypeName);
                                    orders.setPONo(PONo);
                                    orders.setPODate(PODate);
                                    orders.setDeliveryNo(DeliveryNo);
                                    orders.setDeliveryDate(DeliveryDate);
                                    orders.setDocStatus(DocStatus);
                                    orders.setDocStatusName(DocStatusName);
                                    orders.setSalesEmp(SalesEmp);
                                    orders.setDeliveryEmp(DeliveryEmp);
                                    orders.setDocTotal(DocTotal);
                                    orders.setDocVATTotal(DocVATTotal);
                                    orders.setDocGToal(DocGToal);
                                    orders.setRemark(Remark);
                                    orders.setCreateDate(CreateDate);
                                    ordersList.add(orders);

                                    DocEntry = null;
                                    CardCode = null;
                                    CardName = null;
                                    Address = null;
                                    Route = null;
                                    OrderType = null;
                                    OrderTypeName = null;
                                    PONo = null;
                                    PODate = null;
                                    DeliveryNo = null;
                                    DeliveryDate = null;
                                    DocStatus = null;
                                    DocStatusName = null;
                                    SalesEmp = null;
                                    DeliveryEmp = null;
                                    DocTotal = null;
                                    DocVATTotal = null;
                                    DocGToal = null;
                                    Remark = null;
                                    CreateDate = null;
                                }
                            }

                            if (readyToDocStatusName) {
                                DocStatusName = data;
                                readyToDocStatusName = false;
                                if (!TextUtils.isEmpty(DocEntry) && !TextUtils.isEmpty(CardCode) &&
                                        !TextUtils.isEmpty(CardName) && !TextUtils.isEmpty(Address) &&
                                        !TextUtils.isEmpty(Route) && !TextUtils.isEmpty(OrderType) &&
                                        !TextUtils.isEmpty(OrderTypeName) && !TextUtils.isEmpty(PONo) &&
                                        !TextUtils.isEmpty(PODate) && !TextUtils.isEmpty(DeliveryNo) &&
                                        !TextUtils.isEmpty(DeliveryDate) && !TextUtils.isEmpty(DocStatus) &&
                                        !TextUtils.isEmpty(DocStatusName) && !TextUtils.isEmpty(SalesEmp) &&
                                        !TextUtils.isEmpty(DeliveryEmp) && !TextUtils.isEmpty(DocTotal) &&
                                        !TextUtils.isEmpty(DocVATTotal) && !TextUtils.isEmpty(DocGToal) &&
                                        !TextUtils.isEmpty(Remark) && !TextUtils.isEmpty(CreateDate)) {

                                    Orders orders = new Orders();
                                    orders.setDocEntry(DocEntry);
                                    orders.setCardCode(CardCode);
                                    orders.setCardName(CardName);
                                    orders.setAddress(Address);
                                    orders.setRoute(Route);
                                    orders.setOrderType(OrderType);
                                    orders.setOrderTypeName(OrderTypeName);
                                    orders.setPONo(PONo);
                                    orders.setPODate(PODate);
                                    orders.setDeliveryNo(DeliveryNo);
                                    orders.setDeliveryDate(DeliveryDate);
                                    orders.setDocStatus(DocStatus);
                                    orders.setDocStatusName(DocStatusName);
                                    orders.setSalesEmp(SalesEmp);
                                    orders.setDeliveryEmp(DeliveryEmp);
                                    orders.setDocTotal(DocTotal);
                                    orders.setDocVATTotal(DocVATTotal);
                                    orders.setDocGToal(DocGToal);
                                    orders.setRemark(Remark);
                                    orders.setCreateDate(CreateDate);
                                    ordersList.add(orders);

                                    DocEntry = null;
                                    CardCode = null;
                                    CardName = null;
                                    Address = null;
                                    Route = null;
                                    OrderType = null;
                                    OrderTypeName = null;
                                    PONo = null;
                                    PODate = null;
                                    DeliveryNo = null;
                                    DeliveryDate = null;
                                    DocStatus = null;
                                    DocStatusName = null;
                                    SalesEmp = null;
                                    DeliveryEmp = null;
                                    DocTotal = null;
                                    DocVATTotal = null;
                                    DocGToal = null;
                                    Remark = null;
                                    CreateDate = null;
                                }
                            }

                            if (readyToSalesEmp) {
                                SalesEmp = data;
                                readyToSalesEmp = false;
                                if (!TextUtils.isEmpty(DocEntry) && !TextUtils.isEmpty(CardCode) &&
                                        !TextUtils.isEmpty(CardName) && !TextUtils.isEmpty(Address) &&
                                        !TextUtils.isEmpty(Route) && !TextUtils.isEmpty(OrderType) &&
                                        !TextUtils.isEmpty(OrderTypeName) && !TextUtils.isEmpty(PONo) &&
                                        !TextUtils.isEmpty(PODate) && !TextUtils.isEmpty(DeliveryNo) &&
                                        !TextUtils.isEmpty(DeliveryDate) && !TextUtils.isEmpty(DocStatus) &&
                                        !TextUtils.isEmpty(DocStatusName) && !TextUtils.isEmpty(SalesEmp) &&
                                        !TextUtils.isEmpty(DeliveryEmp) && !TextUtils.isEmpty(DocTotal) &&
                                        !TextUtils.isEmpty(DocVATTotal) && !TextUtils.isEmpty(DocGToal) &&
                                        !TextUtils.isEmpty(Remark) && !TextUtils.isEmpty(CreateDate)) {

                                    Orders orders = new Orders();
                                    orders.setDocEntry(DocEntry);
                                    orders.setCardCode(CardCode);
                                    orders.setCardName(CardName);
                                    orders.setAddress(Address);
                                    orders.setRoute(Route);
                                    orders.setOrderType(OrderType);
                                    orders.setOrderTypeName(OrderTypeName);
                                    orders.setPONo(PONo);
                                    orders.setPODate(PODate);
                                    orders.setDeliveryNo(DeliveryNo);
                                    orders.setDeliveryDate(DeliveryDate);
                                    orders.setDocStatus(DocStatus);
                                    orders.setDocStatusName(DocStatusName);
                                    orders.setSalesEmp(SalesEmp);
                                    orders.setDeliveryEmp(DeliveryEmp);
                                    orders.setDocTotal(DocTotal);
                                    orders.setDocVATTotal(DocVATTotal);
                                    orders.setDocGToal(DocGToal);
                                    orders.setRemark(Remark);
                                    orders.setCreateDate(CreateDate);
                                    ordersList.add(orders);

                                    DocEntry = null;
                                    CardCode = null;
                                    CardName = null;
                                    Address = null;
                                    Route = null;
                                    OrderType = null;
                                    OrderTypeName = null;
                                    PONo = null;
                                    PODate = null;
                                    DeliveryNo = null;
                                    DeliveryDate = null;
                                    DocStatus = null;
                                    DocStatusName = null;
                                    SalesEmp = null;
                                    DeliveryEmp = null;
                                    DocTotal = null;
                                    DocVATTotal = null;
                                    DocGToal = null;
                                    Remark = null;
                                    CreateDate = null;
                                }
                            }

                            if (readyToDeliveryEmp) {
                                DeliveryEmp = data;
                                readyToDeliveryEmp = false;
                                if (!TextUtils.isEmpty(DocEntry) && !TextUtils.isEmpty(CardCode) &&
                                        !TextUtils.isEmpty(CardName) && !TextUtils.isEmpty(Address) &&
                                        !TextUtils.isEmpty(Route) && !TextUtils.isEmpty(OrderType) &&
                                        !TextUtils.isEmpty(OrderTypeName) && !TextUtils.isEmpty(PONo) &&
                                        !TextUtils.isEmpty(PODate) && !TextUtils.isEmpty(DeliveryNo) &&
                                        !TextUtils.isEmpty(DeliveryDate) && !TextUtils.isEmpty(DocStatus) &&
                                        !TextUtils.isEmpty(DocStatusName) && !TextUtils.isEmpty(SalesEmp) &&
                                        !TextUtils.isEmpty(DeliveryEmp) && !TextUtils.isEmpty(DocTotal) &&
                                        !TextUtils.isEmpty(DocVATTotal) && !TextUtils.isEmpty(DocGToal) &&
                                        !TextUtils.isEmpty(Remark) && !TextUtils.isEmpty(CreateDate)) {

                                    Orders orders = new Orders();
                                    orders.setDocEntry(DocEntry);
                                    orders.setCardCode(CardCode);
                                    orders.setCardName(CardName);
                                    orders.setAddress(Address);
                                    orders.setRoute(Route);
                                    orders.setOrderType(OrderType);
                                    orders.setOrderTypeName(OrderTypeName);
                                    orders.setPONo(PONo);
                                    orders.setPODate(PODate);
                                    orders.setDeliveryNo(DeliveryNo);
                                    orders.setDeliveryDate(DeliveryDate);
                                    orders.setDocStatus(DocStatus);
                                    orders.setDocStatusName(DocStatusName);
                                    orders.setSalesEmp(SalesEmp);
                                    orders.setDeliveryEmp(DeliveryEmp);
                                    orders.setDocTotal(DocTotal);
                                    orders.setDocVATTotal(DocVATTotal);
                                    orders.setDocGToal(DocGToal);
                                    orders.setRemark(Remark);
                                    orders.setCreateDate(CreateDate);
                                    ordersList.add(orders);

                                    DocEntry = null;
                                    CardCode = null;
                                    CardName = null;
                                    Address = null;
                                    Route = null;
                                    OrderType = null;
                                    OrderTypeName = null;
                                    PONo = null;
                                    PODate = null;
                                    DeliveryNo = null;
                                    DeliveryDate = null;
                                    DocStatus = null;
                                    DocStatusName = null;
                                    SalesEmp = null;
                                    DeliveryEmp = null;
                                    DocTotal = null;
                                    DocVATTotal = null;
                                    DocGToal = null;
                                    Remark = null;
                                    CreateDate = null;
                                }
                            }

                            if (readyToDocTotal) {
                                DocTotal = data;
                                readyToDocTotal = false;
                                if (!TextUtils.isEmpty(DocEntry) && !TextUtils.isEmpty(CardCode) &&
                                        !TextUtils.isEmpty(CardName) && !TextUtils.isEmpty(Address) &&
                                        !TextUtils.isEmpty(Route) && !TextUtils.isEmpty(OrderType) &&
                                        !TextUtils.isEmpty(OrderTypeName) && !TextUtils.isEmpty(PONo) &&
                                        !TextUtils.isEmpty(PODate) && !TextUtils.isEmpty(DeliveryNo) &&
                                        !TextUtils.isEmpty(DeliveryDate) && !TextUtils.isEmpty(DocStatus) &&
                                        !TextUtils.isEmpty(DocStatusName) && !TextUtils.isEmpty(SalesEmp) &&
                                        !TextUtils.isEmpty(DeliveryEmp) && !TextUtils.isEmpty(DocTotal) &&
                                        !TextUtils.isEmpty(DocVATTotal) && !TextUtils.isEmpty(DocGToal) &&
                                        !TextUtils.isEmpty(Remark) && !TextUtils.isEmpty(CreateDate)) {

                                    Orders orders = new Orders();
                                    orders.setDocEntry(DocEntry);
                                    orders.setCardCode(CardCode);
                                    orders.setCardName(CardName);
                                    orders.setAddress(Address);
                                    orders.setRoute(Route);
                                    orders.setOrderType(OrderType);
                                    orders.setOrderTypeName(OrderTypeName);
                                    orders.setPONo(PONo);
                                    orders.setPODate(PODate);
                                    orders.setDeliveryNo(DeliveryNo);
                                    orders.setDeliveryDate(DeliveryDate);
                                    orders.setDocStatus(DocStatus);
                                    orders.setDocStatusName(DocStatusName);
                                    orders.setSalesEmp(SalesEmp);
                                    orders.setDeliveryEmp(DeliveryEmp);
                                    orders.setDocTotal(DocTotal);
                                    orders.setDocVATTotal(DocVATTotal);
                                    orders.setDocGToal(DocGToal);
                                    orders.setRemark(Remark);
                                    orders.setCreateDate(CreateDate);
                                    ordersList.add(orders);

                                    DocEntry = null;
                                    CardCode = null;
                                    CardName = null;
                                    Address = null;
                                    Route = null;
                                    OrderType = null;
                                    OrderTypeName = null;
                                    PONo = null;
                                    PODate = null;
                                    DeliveryNo = null;
                                    DeliveryDate = null;
                                    DocStatus = null;
                                    DocStatusName = null;
                                    SalesEmp = null;
                                    DeliveryEmp = null;
                                    DocTotal = null;
                                    DocVATTotal = null;
                                    DocGToal = null;
                                    Remark = null;
                                    CreateDate = null;
                                }
                            }

                            if (readyToDocVATTotal) {
                                DocVATTotal = data;
                                readyToDocVATTotal = false;
                                if (!TextUtils.isEmpty(DocEntry) && !TextUtils.isEmpty(CardCode) &&
                                        !TextUtils.isEmpty(CardName) && !TextUtils.isEmpty(Address) &&
                                        !TextUtils.isEmpty(Route) && !TextUtils.isEmpty(OrderType) &&
                                        !TextUtils.isEmpty(OrderTypeName) && !TextUtils.isEmpty(PONo) &&
                                        !TextUtils.isEmpty(PODate) && !TextUtils.isEmpty(DeliveryNo) &&
                                        !TextUtils.isEmpty(DeliveryDate) && !TextUtils.isEmpty(DocStatus) &&
                                        !TextUtils.isEmpty(DocStatusName) && !TextUtils.isEmpty(SalesEmp) &&
                                        !TextUtils.isEmpty(DeliveryEmp) && !TextUtils.isEmpty(DocTotal) &&
                                        !TextUtils.isEmpty(DocVATTotal) && !TextUtils.isEmpty(DocGToal) &&
                                        !TextUtils.isEmpty(Remark) && !TextUtils.isEmpty(CreateDate)) {

                                    Orders orders = new Orders();
                                    orders.setDocEntry(DocEntry);
                                    orders.setCardCode(CardCode);
                                    orders.setCardName(CardName);
                                    orders.setAddress(Address);
                                    orders.setRoute(Route);
                                    orders.setOrderType(OrderType);
                                    orders.setOrderTypeName(OrderTypeName);
                                    orders.setPONo(PONo);
                                    orders.setPODate(PODate);
                                    orders.setDeliveryNo(DeliveryNo);
                                    orders.setDeliveryDate(DeliveryDate);
                                    orders.setDocStatus(DocStatus);
                                    orders.setDocStatusName(DocStatusName);
                                    orders.setSalesEmp(SalesEmp);
                                    orders.setDeliveryEmp(DeliveryEmp);
                                    orders.setDocTotal(DocTotal);
                                    orders.setDocVATTotal(DocVATTotal);
                                    orders.setDocGToal(DocGToal);
                                    orders.setRemark(Remark);
                                    orders.setCreateDate(CreateDate);
                                    ordersList.add(orders);

                                    DocEntry = null;
                                    CardCode = null;
                                    CardName = null;
                                    Address = null;
                                    Route = null;
                                    OrderType = null;
                                    OrderTypeName = null;
                                    PONo = null;
                                    PODate = null;
                                    DeliveryNo = null;
                                    DeliveryDate = null;
                                    DocStatus = null;
                                    DocStatusName = null;
                                    SalesEmp = null;
                                    DeliveryEmp = null;
                                    DocTotal = null;
                                    DocVATTotal = null;
                                    DocGToal = null;
                                    Remark = null;
                                    CreateDate = null;
                                }
                            }

                            if (readyToDocGToal) {
                                DocGToal = data;
                                readyToDocGToal = false;
                                if (!TextUtils.isEmpty(DocEntry) && !TextUtils.isEmpty(CardCode) &&
                                        !TextUtils.isEmpty(CardName) && !TextUtils.isEmpty(Address) &&
                                        !TextUtils.isEmpty(Route) && !TextUtils.isEmpty(OrderType) &&
                                        !TextUtils.isEmpty(OrderTypeName) && !TextUtils.isEmpty(PONo) &&
                                        !TextUtils.isEmpty(PODate) && !TextUtils.isEmpty(DeliveryNo) &&
                                        !TextUtils.isEmpty(DeliveryDate) && !TextUtils.isEmpty(DocStatus) &&
                                        !TextUtils.isEmpty(DocStatusName) && !TextUtils.isEmpty(SalesEmp) &&
                                        !TextUtils.isEmpty(DeliveryEmp) && !TextUtils.isEmpty(DocTotal) &&
                                        !TextUtils.isEmpty(DocVATTotal) && !TextUtils.isEmpty(DocGToal) &&
                                        !TextUtils.isEmpty(Remark) && !TextUtils.isEmpty(CreateDate)) {

                                    Orders orders = new Orders();
                                    orders.setDocEntry(DocEntry);
                                    orders.setCardCode(CardCode);
                                    orders.setCardName(CardName);
                                    orders.setAddress(Address);
                                    orders.setRoute(Route);
                                    orders.setOrderType(OrderType);
                                    orders.setOrderTypeName(OrderTypeName);
                                    orders.setPONo(PONo);
                                    orders.setPODate(PODate);
                                    orders.setDeliveryNo(DeliveryNo);
                                    orders.setDeliveryDate(DeliveryDate);
                                    orders.setDocStatus(DocStatus);
                                    orders.setDocStatusName(DocStatusName);
                                    orders.setSalesEmp(SalesEmp);
                                    orders.setDeliveryEmp(DeliveryEmp);
                                    orders.setDocTotal(DocTotal);
                                    orders.setDocVATTotal(DocVATTotal);
                                    orders.setDocGToal(DocGToal);
                                    orders.setRemark(Remark);
                                    orders.setCreateDate(CreateDate);
                                    ordersList.add(orders);

                                    DocEntry = null;
                                    CardCode = null;
                                    CardName = null;
                                    Address = null;
                                    Route = null;
                                    OrderType = null;
                                    OrderTypeName = null;
                                    PONo = null;
                                    PODate = null;
                                    DeliveryNo = null;
                                    DeliveryDate = null;
                                    DocStatus = null;
                                    DocStatusName = null;
                                    SalesEmp = null;
                                    DeliveryEmp = null;
                                    DocTotal = null;
                                    DocVATTotal = null;
                                    DocGToal = null;
                                    Remark = null;
                                    CreateDate = null;
                                }
                            }

                            if (readyToRemark) {
                                Remark = data;
                                readyToRemark = false;
                                if (!TextUtils.isEmpty(DocEntry) && !TextUtils.isEmpty(CardCode) &&
                                        !TextUtils.isEmpty(CardName) && !TextUtils.isEmpty(Address) &&
                                        !TextUtils.isEmpty(Route) && !TextUtils.isEmpty(OrderType) &&
                                        !TextUtils.isEmpty(OrderTypeName) && !TextUtils.isEmpty(PONo) &&
                                        !TextUtils.isEmpty(PODate) && !TextUtils.isEmpty(DeliveryNo) &&
                                        !TextUtils.isEmpty(DeliveryDate) && !TextUtils.isEmpty(DocStatus) &&
                                        !TextUtils.isEmpty(DocStatusName) && !TextUtils.isEmpty(SalesEmp) &&
                                        !TextUtils.isEmpty(DeliveryEmp) && !TextUtils.isEmpty(DocTotal) &&
                                        !TextUtils.isEmpty(DocVATTotal) && !TextUtils.isEmpty(DocGToal) &&
                                        !TextUtils.isEmpty(Remark) && !TextUtils.isEmpty(CreateDate)) {

                                    Orders orders = new Orders();
                                    orders.setDocEntry(DocEntry);
                                    orders.setCardCode(CardCode);
                                    orders.setCardName(CardName);
                                    orders.setAddress(Address);
                                    orders.setRoute(Route);
                                    orders.setOrderType(OrderType);
                                    orders.setOrderTypeName(OrderTypeName);
                                    orders.setPONo(PONo);
                                    orders.setPODate(PODate);
                                    orders.setDeliveryNo(DeliveryNo);
                                    orders.setDeliveryDate(DeliveryDate);
                                    orders.setDocStatus(DocStatus);
                                    orders.setDocStatusName(DocStatusName);
                                    orders.setSalesEmp(SalesEmp);
                                    orders.setDeliveryEmp(DeliveryEmp);
                                    orders.setDocTotal(DocTotal);
                                    orders.setDocVATTotal(DocVATTotal);
                                    orders.setDocGToal(DocGToal);
                                    orders.setRemark(Remark);
                                    orders.setCreateDate(CreateDate);
                                    ordersList.add(orders);

                                    DocEntry = null;
                                    CardCode = null;
                                    CardName = null;
                                    Address = null;
                                    Route = null;
                                    OrderType = null;
                                    OrderTypeName = null;
                                    PONo = null;
                                    PODate = null;
                                    DeliveryNo = null;
                                    DeliveryDate = null;
                                    DocStatus = null;
                                    DocStatusName = null;
                                    SalesEmp = null;
                                    DeliveryEmp = null;
                                    DocTotal = null;
                                    DocVATTotal = null;
                                    DocGToal = null;
                                    Remark = null;
                                    CreateDate = null;
                                }
                            }

                            if (readyToCreateDate) {
                                CreateDate = data;
                                readyToCreateDate = false;
                                if (!TextUtils.isEmpty(DocEntry) && !TextUtils.isEmpty(CardCode) &&
                                        !TextUtils.isEmpty(CardName) && !TextUtils.isEmpty(Address) &&
                                        !TextUtils.isEmpty(Route) && !TextUtils.isEmpty(OrderType) &&
                                        !TextUtils.isEmpty(OrderTypeName) && !TextUtils.isEmpty(PONo) &&
                                        !TextUtils.isEmpty(PODate) && !TextUtils.isEmpty(DeliveryNo) &&
                                        !TextUtils.isEmpty(DeliveryDate) && !TextUtils.isEmpty(DocStatus) &&
                                        !TextUtils.isEmpty(DocStatusName) && !TextUtils.isEmpty(SalesEmp) &&
                                        !TextUtils.isEmpty(DeliveryEmp) && !TextUtils.isEmpty(DocTotal) &&
                                        !TextUtils.isEmpty(DocVATTotal) && !TextUtils.isEmpty(DocGToal) &&
                                        !TextUtils.isEmpty(Remark) && !TextUtils.isEmpty(CreateDate)) {

                                    Orders orders = new Orders();
                                    orders.setDocEntry(DocEntry);
                                    orders.setCardCode(CardCode);
                                    orders.setCardName(CardName);
                                    orders.setAddress(Address);
                                    orders.setRoute(Route);
                                    orders.setOrderType(OrderType);
                                    orders.setOrderTypeName(OrderTypeName);
                                    orders.setPONo(PONo);
                                    orders.setPODate(PODate);
                                    orders.setDeliveryNo(DeliveryNo);
                                    orders.setDeliveryDate(DeliveryDate);
                                    orders.setDocStatus(DocStatus);
                                    orders.setDocStatusName(DocStatusName);
                                    orders.setSalesEmp(SalesEmp);
                                    orders.setDeliveryEmp(DeliveryEmp);
                                    orders.setDocTotal(DocTotal);
                                    orders.setDocVATTotal(DocVATTotal);
                                    orders.setDocGToal(DocGToal);
                                    orders.setRemark(Remark);
                                    orders.setCreateDate(CreateDate);
                                    ordersList.add(orders);

                                    DocEntry = null;
                                    CardCode = null;
                                    CardName = null;
                                    Address = null;
                                    Route = null;
                                    OrderType = null;
                                    OrderTypeName = null;
                                    PONo = null;
                                    PODate = null;
                                    DeliveryNo = null;
                                    DeliveryDate = null;
                                    DocStatus = null;
                                    DocStatusName = null;
                                    SalesEmp = null;
                                    DeliveryEmp = null;
                                    DocTotal = null;
                                    DocVATTotal = null;
                                    DocGToal = null;
                                    Remark = null;
                                    CreateDate = null;
                                }
                            }
                        }
                        eventType = xpp.next();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    callback.onFail(e.getMessage());
                }
                callback.onSuccess(ordersList);
            }

            @Override
            public void onFail(String error) {
                callback.onFail(error);
            }
        });
    }

    public void loadListOrderbyDoc(String DocEntry, final GetListOrderbyDoc callback) {
        String link = String.format(getURL() + Config.ORDERBYDOC);

        List<Param> params = new ArrayList<>();
        Param param = new Param("DocEntry", DocEntry);
        params.add(param);
        mNetwork.executePost(link, params, new Network.Callback() {
            @Override
            public void onCallBack(String response) {
                XmlPullParserFactory factory = null;
                ArrayList<OrderbyDoc> orderbyDocs = new ArrayList<>();
                try {
                    boolean readyToMyId = false;
                    boolean readyToDocEntry = false;
                    boolean readyToItemCode = false;
                    boolean readyToBarCode = false;
                    boolean readyToItemName = false;
                    boolean readyToQuantity = false;
                    boolean readyToUoM = false;
                    boolean readyToPrice = false;
                    boolean readyToDiscount = false;
                    boolean readyToPriceAfterDiscount = false;
                    boolean readyToVATAmt = false;
                    boolean readyToVATPercent = false;
                    boolean readyToGTotal = false;
                    boolean readyToDocEntry1 = false;
                    boolean readyToCardCode = false;
                    boolean readyToCardName = false;
                    boolean readyToAddress = false;
                    boolean readyToRoute = false;
                    boolean readyToOrderType = false;
                    boolean readyToOrderTypeName = false;
                    boolean readyToPONo = false;
                    boolean readyToPODate = false;
                    boolean readyToDeliveryNo = false;
                    boolean readyToDeliveryDate = false;
                    boolean readyToDocStatus = false;
                    boolean readyToDocStatusName = false;
                    boolean readyToSalesEmp = false;
                    boolean readyToDeliveryEmp = false;
                    boolean readyToDocTotal = false;
                    boolean readyToDocVATTotal = false;
                    boolean readyToDocGToal = false;
                    boolean readyToRemark = false;
                    boolean readyToCreateDate = false;

                    String MyId = null;
                    String DocEntry = null;
                    String ItemCode = null;
                    String BarCode = null;
                    String ItemName = null;
                    String Quantity = null;
                    String UoM = null;
                    String Price = null;
                    String Discount = null;
                    String PriceAfterDiscount = null;
                    String VATAmt = null;
                    String VATPercent = null;
                    String GTotal = null;
                    String DocEntry1 = null;
                    String CardCode = null;
                    String CardName = null;
                    String Address = null;
                    String Route = null;
                    String OrderType = null;
                    String OrderTypeName = null;
                    String PONo = null;
                    String PODate = null;
                    String DeliveryNo = null;
                    String DeliveryDate = null;
                    String DocStatus = null;
                    String DocStatusName = null;
                    String SalesEmp = null;
                    String DeliveryEmp = null;
                    String DocTotal = null;
                    String DocVATTotal = null;
                    String DocGToal = null;
                    String Remark = null;
                    String CreateDate = null;

                    factory = XmlPullParserFactory.newInstance();
                    factory.setNamespaceAware(true);
                    XmlPullParser xpp = factory.newPullParser();

                    xpp.setInput(new StringReader(response));
                    int eventType = xpp.getEventType();
                    while (eventType != XmlPullParser.END_DOCUMENT) {
                        if (eventType == XmlPullParser.START_DOCUMENT) {
                            //TODO: do nothing
                        } else if (eventType == XmlPullParser.START_TAG) {
                            if (xpp.getName().equalsIgnoreCase("MyId")) {
                                readyToMyId = true;
                            }

                            if (xpp.getName().equalsIgnoreCase("DocEntry")) {
                                readyToDocEntry = true;
                            }

                            if (xpp.getName().equalsIgnoreCase("ItemCode")) {
                                readyToItemCode = true;
                            }

                            if (xpp.getName().equalsIgnoreCase("BarCode")) {
                                readyToBarCode = true;
                            }

                            if (xpp.getName().equalsIgnoreCase("ItemName")) {
                                readyToItemName = true;
                            }

                            if (xpp.getName().equalsIgnoreCase("Quantity")) {
                                readyToQuantity = true;
                            }

                            if (xpp.getName().equalsIgnoreCase("UoM")) {
                                readyToUoM = true;
                            }

                            if (xpp.getName().equalsIgnoreCase("Price")) {
                                readyToPrice = true;
                            }

                            if (xpp.getName().equalsIgnoreCase("Discount")) {
                                readyToDiscount = true;
                            }

                            if (xpp.getName().equalsIgnoreCase("PriceAfterDiscount")) {
                                readyToPriceAfterDiscount = true;
                            }

                            if (xpp.getName().equalsIgnoreCase("VATAmt")) {
                                readyToVATAmt = true;
                            }

                            if (xpp.getName().equalsIgnoreCase("VATPercent")) {
                                readyToVATPercent = true;
                            }

                            if (xpp.getName().equalsIgnoreCase("GTotal")) {
                                readyToGTotal = true;
                            }

                            if (xpp.getName().equalsIgnoreCase("DocEntry1")) {
                                readyToDocEntry1 = true;
                            }

                            if (xpp.getName().equalsIgnoreCase("CardCode")) {
                                readyToCardCode = true;
                            }

                            if (xpp.getName().equalsIgnoreCase("CardName")) {
                                readyToCardName = true;
                            }

                            if (xpp.getName().equalsIgnoreCase("Address")) {
                                readyToAddress = true;
                            }

                            if (xpp.getName().equalsIgnoreCase("Route")) {
                                readyToRoute = true;
                            }

                            if (xpp.getName().equalsIgnoreCase("OrderType")) {
                                readyToOrderType = true;
                            }

                            if (xpp.getName().equalsIgnoreCase("OrderTypeName")) {
                                readyToOrderTypeName = true;
                            }

                            if (xpp.getName().equalsIgnoreCase("PONo")) {
                                readyToPONo = true;
                            }

                            if (xpp.getName().equalsIgnoreCase("PODate")) {
                                readyToPODate = true;
                            }

                            if (xpp.getName().equalsIgnoreCase("DeliveryNo")) {
                                readyToDeliveryNo = true;
                            }

                            if (xpp.getName().equalsIgnoreCase("DeliveryDate")) {
                                readyToDeliveryDate = true;
                            }

                            if (xpp.getName().equalsIgnoreCase("DocStatus")) {
                                readyToDocStatus = true;
                            }

                            if (xpp.getName().equalsIgnoreCase("DocStatusName")) {
                                readyToDocStatusName = true;
                            }

                            if (xpp.getName().equalsIgnoreCase("SalesEmp")) {
                                readyToSalesEmp = true;
                            }

                            if (xpp.getName().equalsIgnoreCase("DeliveryEmp")) {
                                readyToDeliveryEmp = true;
                            }

                            if (xpp.getName().equalsIgnoreCase("DocTotal")) {
                                readyToDocTotal = true;
                            }

                            if (xpp.getName().equalsIgnoreCase("DocVATTotal")) {
                                readyToDocVATTotal = true;
                            }

                            if (xpp.getName().equalsIgnoreCase("DocGToal")) {
                                readyToDocGToal = true;
                            }

                            if (xpp.getName().equalsIgnoreCase("Remark")) {
                                readyToRemark = true;
                            }

                            if (xpp.getName().equalsIgnoreCase("CreateDate")) {
                                readyToCreateDate = true;
                            }

                        } else if (eventType == XmlPullParser.TEXT) {
                            String data = xpp.getText();

                            if (readyToMyId) {
                                MyId = data;
                                readyToMyId = false;
                                if (!TextUtils.isEmpty(MyId) && !TextUtils.isEmpty(DocEntry) &&
                                        !TextUtils.isEmpty(ItemCode) && !TextUtils.isEmpty(BarCode) &&
                                        !TextUtils.isEmpty(ItemName) && !TextUtils.isEmpty(Quantity) &&
                                        !TextUtils.isEmpty(UoM) && !TextUtils.isEmpty(Price) &&
                                        !TextUtils.isEmpty(Discount) && !TextUtils.isEmpty(PriceAfterDiscount) &&
                                        !TextUtils.isEmpty(VATAmt) && !TextUtils.isEmpty(VATPercent) &&
                                        !TextUtils.isEmpty(GTotal) && !TextUtils.isEmpty(DocEntry1) &&
                                        !TextUtils.isEmpty(CardCode) && !TextUtils.isEmpty(CardName) &&
                                        !TextUtils.isEmpty(Address) && !TextUtils.isEmpty(Route) &&
                                        !TextUtils.isEmpty(OrderType) && !TextUtils.isEmpty(OrderTypeName) &&
                                        !TextUtils.isEmpty(PONo) && !TextUtils.isEmpty(PODate) &&
                                        !TextUtils.isEmpty(DeliveryNo) && !TextUtils.isEmpty(DeliveryDate) &&
                                        !TextUtils.isEmpty(DocStatus) && !TextUtils.isEmpty(DocStatusName) &&
                                        !TextUtils.isEmpty(SalesEmp) && !TextUtils.isEmpty(DeliveryEmp) &&
                                        !TextUtils.isEmpty(DocTotal) && !TextUtils.isEmpty(DocVATTotal) &&
                                        !TextUtils.isEmpty(DocGToal) && !TextUtils.isEmpty(Remark) && !TextUtils.isEmpty(CreateDate)) {

                                    OrderbyDoc orders = new OrderbyDoc();
                                    orders.setMyId(MyId);
                                    orders.setDocEntry(DocEntry);
                                    orders.setItemCode(ItemCode);
                                    orders.setBarCode(BarCode);
                                    orders.setItemName(ItemName);
                                    orders.setQuantity(Quantity);
                                    orders.setUoM(UoM);
                                    orders.setPrice(Price);
                                    orders.setDiscount(Discount);
                                    orders.setPriceAfterDiscount(PriceAfterDiscount);
                                    orders.setVATAmt(VATAmt);
                                    orders.setVATPercent(VATPercent);
                                    orders.setGTotal(GTotal);
                                    orders.setDocEntry1(DocEntry);
                                    orders.setCardCode(CardCode);
                                    orders.setCardName(CardName);
                                    orders.setAddress(Address);
                                    orders.setRoute(OrderTypeName);
                                    orders.setOrderType(PONo);
                                    orders.setOrderTypeName(PODate);
                                    orders.setPONo(DeliveryNo);
                                    orders.setPODate(DeliveryDate);
                                    orders.setDeliveryNo(DocStatus);
                                    orders.setDeliveryDate(DocStatusName);
                                    orders.setDocStatus(SalesEmp);
                                    orders.setDocStatusName(DeliveryEmp);
                                    orders.setSalesEmp(DocTotal);
                                    orders.setDeliveryEmp(DocEntry);
                                    orders.setDocTotal(CardCode);
                                    orders.setDocVATTotal(DocVATTotal);
                                    orders.setDocGToal(DocGToal);
                                    orders.setRemark(Remark);
                                    orders.setCreateDate(CreateDate);
                                    orderbyDocs.add(orders);

                                    MyId = null;
                                    DocEntry = null;
                                    ItemCode = null;
                                    BarCode = null;
                                    ItemName = null;
                                    Quantity = null;
                                    UoM = null;
                                    Price = null;
                                    Discount = null;
                                    PriceAfterDiscount = null;
                                    VATAmt = null;
                                    VATPercent = null;
                                    GTotal = null;
                                    DocEntry1 = null;
                                    CardCode = null;
                                    CardName = null;
                                    Address = null;
                                    Route = null;
                                    OrderType = null;
                                    OrderTypeName = null;
                                    PONo = null;
                                    PODate = null;
                                    DeliveryNo = null;
                                    DeliveryDate = null;
                                    DocStatus = null;
                                    DocStatusName = null;
                                    SalesEmp = null;
                                    DeliveryEmp = null;
                                    DocTotal = null;
                                    DocVATTotal = null;
                                    DocGToal = null;
                                    Remark = null;
                                    CreateDate = null;
                                }
                            }

                            if (readyToDocEntry) {
                                DocEntry = data;
                                readyToDocEntry = false;
                                if (!TextUtils.isEmpty(MyId) && !TextUtils.isEmpty(DocEntry) &&
                                        !TextUtils.isEmpty(ItemCode) && !TextUtils.isEmpty(BarCode) &&
                                        !TextUtils.isEmpty(ItemName) && !TextUtils.isEmpty(Quantity) &&
                                        !TextUtils.isEmpty(UoM) && !TextUtils.isEmpty(Price) &&
                                        !TextUtils.isEmpty(Discount) && !TextUtils.isEmpty(PriceAfterDiscount) &&
                                        !TextUtils.isEmpty(VATAmt) && !TextUtils.isEmpty(VATPercent) &&
                                        !TextUtils.isEmpty(GTotal) && !TextUtils.isEmpty(DocEntry1) &&
                                        !TextUtils.isEmpty(CardCode) && !TextUtils.isEmpty(CardName) &&
                                        !TextUtils.isEmpty(Address) && !TextUtils.isEmpty(Route) &&
                                        !TextUtils.isEmpty(OrderType) && !TextUtils.isEmpty(OrderTypeName) &&
                                        !TextUtils.isEmpty(PONo) && !TextUtils.isEmpty(PODate) &&
                                        !TextUtils.isEmpty(DeliveryNo) && !TextUtils.isEmpty(DeliveryDate) &&
                                        !TextUtils.isEmpty(DocStatus) && !TextUtils.isEmpty(DocStatusName) &&
                                        !TextUtils.isEmpty(SalesEmp) && !TextUtils.isEmpty(DeliveryEmp) &&
                                        !TextUtils.isEmpty(DocTotal) && !TextUtils.isEmpty(DocVATTotal) &&
                                        !TextUtils.isEmpty(DocGToal) && !TextUtils.isEmpty(Remark) && !TextUtils.isEmpty(CreateDate)) {

                                    OrderbyDoc orders = new OrderbyDoc();
                                    orders.setMyId(MyId);
                                    orders.setDocEntry(DocEntry);
                                    orders.setItemCode(ItemCode);
                                    orders.setBarCode(BarCode);
                                    orders.setItemName(ItemName);
                                    orders.setQuantity(Quantity);
                                    orders.setUoM(UoM);
                                    orders.setPrice(Price);
                                    orders.setDiscount(Discount);
                                    orders.setPriceAfterDiscount(PriceAfterDiscount);
                                    orders.setVATAmt(VATAmt);
                                    orders.setVATPercent(VATPercent);
                                    orders.setGTotal(GTotal);
                                    orders.setDocEntry1(DocEntry);
                                    orders.setCardCode(CardCode);
                                    orders.setCardName(CardName);
                                    orders.setAddress(Address);
                                    orders.setRoute(OrderTypeName);
                                    orders.setOrderType(PONo);
                                    orders.setOrderTypeName(PODate);
                                    orders.setPONo(DeliveryNo);
                                    orders.setPODate(DeliveryDate);
                                    orders.setDeliveryNo(DocStatus);
                                    orders.setDeliveryDate(DocStatusName);
                                    orders.setDocStatus(SalesEmp);
                                    orders.setDocStatusName(DeliveryEmp);
                                    orders.setSalesEmp(DocTotal);
                                    orders.setDeliveryEmp(DocEntry);
                                    orders.setDocTotal(CardCode);
                                    orders.setDocVATTotal(DocVATTotal);
                                    orders.setDocGToal(DocGToal);
                                    orders.setRemark(Remark);
                                    orders.setCreateDate(CreateDate);
                                    orderbyDocs.add(orders);

                                    MyId = null;
                                    DocEntry = null;
                                    ItemCode = null;
                                    BarCode = null;
                                    ItemName = null;
                                    Quantity = null;
                                    UoM = null;
                                    Price = null;
                                    Discount = null;
                                    PriceAfterDiscount = null;
                                    VATAmt = null;
                                    VATPercent = null;
                                    GTotal = null;
                                    DocEntry1 = null;
                                    CardCode = null;
                                    CardName = null;
                                    Address = null;
                                    Route = null;
                                    OrderType = null;
                                    OrderTypeName = null;
                                    PONo = null;
                                    PODate = null;
                                    DeliveryNo = null;
                                    DeliveryDate = null;
                                    DocStatus = null;
                                    DocStatusName = null;
                                    SalesEmp = null;
                                    DeliveryEmp = null;
                                    DocTotal = null;
                                    DocVATTotal = null;
                                    DocGToal = null;
                                    Remark = null;
                                    CreateDate = null;
                                }
                            }

                            if (readyToItemCode) {
                                ItemCode = data;
                                readyToItemCode = false;
                                if (!TextUtils.isEmpty(MyId) && !TextUtils.isEmpty(DocEntry) &&
                                        !TextUtils.isEmpty(ItemCode) && !TextUtils.isEmpty(BarCode) &&
                                        !TextUtils.isEmpty(ItemName) && !TextUtils.isEmpty(Quantity) &&
                                        !TextUtils.isEmpty(UoM) && !TextUtils.isEmpty(Price) &&
                                        !TextUtils.isEmpty(Discount) && !TextUtils.isEmpty(PriceAfterDiscount) &&
                                        !TextUtils.isEmpty(VATAmt) && !TextUtils.isEmpty(VATPercent) &&
                                        !TextUtils.isEmpty(GTotal) && !TextUtils.isEmpty(DocEntry1) &&
                                        !TextUtils.isEmpty(CardCode) && !TextUtils.isEmpty(CardName) &&
                                        !TextUtils.isEmpty(Address) && !TextUtils.isEmpty(Route) &&
                                        !TextUtils.isEmpty(OrderType) && !TextUtils.isEmpty(OrderTypeName) &&
                                        !TextUtils.isEmpty(PONo) && !TextUtils.isEmpty(PODate) &&
                                        !TextUtils.isEmpty(DeliveryNo) && !TextUtils.isEmpty(DeliveryDate) &&
                                        !TextUtils.isEmpty(DocStatus) && !TextUtils.isEmpty(DocStatusName) &&
                                        !TextUtils.isEmpty(SalesEmp) && !TextUtils.isEmpty(DeliveryEmp) &&
                                        !TextUtils.isEmpty(DocTotal) && !TextUtils.isEmpty(DocVATTotal) &&
                                        !TextUtils.isEmpty(DocGToal) && !TextUtils.isEmpty(Remark) && !TextUtils.isEmpty(CreateDate)) {

                                    OrderbyDoc orders = new OrderbyDoc();
                                    orders.setMyId(MyId);
                                    orders.setDocEntry(DocEntry);
                                    orders.setItemCode(ItemCode);
                                    orders.setBarCode(BarCode);
                                    orders.setItemName(ItemName);
                                    orders.setQuantity(Quantity);
                                    orders.setUoM(UoM);
                                    orders.setPrice(Price);
                                    orders.setDiscount(Discount);
                                    orders.setPriceAfterDiscount(PriceAfterDiscount);
                                    orders.setVATAmt(VATAmt);
                                    orders.setVATPercent(VATPercent);
                                    orders.setGTotal(GTotal);
                                    orders.setDocEntry1(DocEntry);
                                    orders.setCardCode(CardCode);
                                    orders.setCardName(CardName);
                                    orders.setAddress(Address);
                                    orders.setRoute(OrderTypeName);
                                    orders.setOrderType(PONo);
                                    orders.setOrderTypeName(PODate);
                                    orders.setPONo(DeliveryNo);
                                    orders.setPODate(DeliveryDate);
                                    orders.setDeliveryNo(DocStatus);
                                    orders.setDeliveryDate(DocStatusName);
                                    orders.setDocStatus(SalesEmp);
                                    orders.setDocStatusName(DeliveryEmp);
                                    orders.setSalesEmp(DocTotal);
                                    orders.setDeliveryEmp(DocEntry);
                                    orders.setDocTotal(CardCode);
                                    orders.setDocVATTotal(DocVATTotal);
                                    orders.setDocGToal(DocGToal);
                                    orders.setRemark(Remark);
                                    orders.setCreateDate(CreateDate);
                                    orderbyDocs.add(orders);

                                    MyId = null;
                                    DocEntry = null;
                                    ItemCode = null;
                                    BarCode = null;
                                    ItemName = null;
                                    Quantity = null;
                                    UoM = null;
                                    Price = null;
                                    Discount = null;
                                    PriceAfterDiscount = null;
                                    VATAmt = null;
                                    VATPercent = null;
                                    GTotal = null;
                                    DocEntry1 = null;
                                    CardCode = null;
                                    CardName = null;
                                    Address = null;
                                    Route = null;
                                    OrderType = null;
                                    OrderTypeName = null;
                                    PONo = null;
                                    PODate = null;
                                    DeliveryNo = null;
                                    DeliveryDate = null;
                                    DocStatus = null;
                                    DocStatusName = null;
                                    SalesEmp = null;
                                    DeliveryEmp = null;
                                    DocTotal = null;
                                    DocVATTotal = null;
                                    DocGToal = null;
                                    Remark = null;
                                    CreateDate = null;
                                }
                            }

                            if (readyToBarCode) {
                                BarCode = data;
                                readyToBarCode = false;
                                if (!TextUtils.isEmpty(MyId) && !TextUtils.isEmpty(DocEntry) &&
                                        !TextUtils.isEmpty(ItemCode) && !TextUtils.isEmpty(BarCode) &&
                                        !TextUtils.isEmpty(ItemName) && !TextUtils.isEmpty(Quantity) &&
                                        !TextUtils.isEmpty(UoM) && !TextUtils.isEmpty(Price) &&
                                        !TextUtils.isEmpty(Discount) && !TextUtils.isEmpty(PriceAfterDiscount) &&
                                        !TextUtils.isEmpty(VATAmt) && !TextUtils.isEmpty(VATPercent) &&
                                        !TextUtils.isEmpty(GTotal) && !TextUtils.isEmpty(DocEntry1) &&
                                        !TextUtils.isEmpty(CardCode) && !TextUtils.isEmpty(CardName) &&
                                        !TextUtils.isEmpty(Address) && !TextUtils.isEmpty(Route) &&
                                        !TextUtils.isEmpty(OrderType) && !TextUtils.isEmpty(OrderTypeName) &&
                                        !TextUtils.isEmpty(PONo) && !TextUtils.isEmpty(PODate) &&
                                        !TextUtils.isEmpty(DeliveryNo) && !TextUtils.isEmpty(DeliveryDate) &&
                                        !TextUtils.isEmpty(DocStatus) && !TextUtils.isEmpty(DocStatusName) &&
                                        !TextUtils.isEmpty(SalesEmp) && !TextUtils.isEmpty(DeliveryEmp) &&
                                        !TextUtils.isEmpty(DocTotal) && !TextUtils.isEmpty(DocVATTotal) &&
                                        !TextUtils.isEmpty(DocGToal) && !TextUtils.isEmpty(Remark) && !TextUtils.isEmpty(CreateDate)) {

                                    OrderbyDoc orders = new OrderbyDoc();
                                    orders.setMyId(MyId);
                                    orders.setDocEntry(DocEntry);
                                    orders.setItemCode(ItemCode);
                                    orders.setBarCode(BarCode);
                                    orders.setItemName(ItemName);
                                    orders.setQuantity(Quantity);
                                    orders.setUoM(UoM);
                                    orders.setPrice(Price);
                                    orders.setDiscount(Discount);
                                    orders.setPriceAfterDiscount(PriceAfterDiscount);
                                    orders.setVATAmt(VATAmt);
                                    orders.setVATPercent(VATPercent);
                                    orders.setGTotal(GTotal);
                                    orders.setDocEntry1(DocEntry);
                                    orders.setCardCode(CardCode);
                                    orders.setCardName(CardName);
                                    orders.setAddress(Address);
                                    orders.setRoute(OrderTypeName);
                                    orders.setOrderType(PONo);
                                    orders.setOrderTypeName(PODate);
                                    orders.setPONo(DeliveryNo);
                                    orders.setPODate(DeliveryDate);
                                    orders.setDeliveryNo(DocStatus);
                                    orders.setDeliveryDate(DocStatusName);
                                    orders.setDocStatus(SalesEmp);
                                    orders.setDocStatusName(DeliveryEmp);
                                    orders.setSalesEmp(DocTotal);
                                    orders.setDeliveryEmp(DocEntry);
                                    orders.setDocTotal(CardCode);
                                    orders.setDocVATTotal(DocVATTotal);
                                    orders.setDocGToal(DocGToal);
                                    orders.setRemark(Remark);
                                    orders.setCreateDate(CreateDate);
                                    orderbyDocs.add(orders);

                                    MyId = null;
                                    DocEntry = null;
                                    ItemCode = null;
                                    BarCode = null;
                                    ItemName = null;
                                    Quantity = null;
                                    UoM = null;
                                    Price = null;
                                    Discount = null;
                                    PriceAfterDiscount = null;
                                    VATAmt = null;
                                    VATPercent = null;
                                    GTotal = null;
                                    DocEntry1 = null;
                                    CardCode = null;
                                    CardName = null;
                                    Address = null;
                                    Route = null;
                                    OrderType = null;
                                    OrderTypeName = null;
                                    PONo = null;
                                    PODate = null;
                                    DeliveryNo = null;
                                    DeliveryDate = null;
                                    DocStatus = null;
                                    DocStatusName = null;
                                    SalesEmp = null;
                                    DeliveryEmp = null;
                                    DocTotal = null;
                                    DocVATTotal = null;
                                    DocGToal = null;
                                    Remark = null;
                                    CreateDate = null;
                                }
                            }

                            if (readyToItemName) {
                                ItemName = data;
                                readyToItemName = false;
                                if (!TextUtils.isEmpty(MyId) && !TextUtils.isEmpty(DocEntry) &&
                                        !TextUtils.isEmpty(ItemCode) && !TextUtils.isEmpty(BarCode) &&
                                        !TextUtils.isEmpty(ItemName) && !TextUtils.isEmpty(Quantity) &&
                                        !TextUtils.isEmpty(UoM) && !TextUtils.isEmpty(Price) &&
                                        !TextUtils.isEmpty(Discount) && !TextUtils.isEmpty(PriceAfterDiscount) &&
                                        !TextUtils.isEmpty(VATAmt) && !TextUtils.isEmpty(VATPercent) &&
                                        !TextUtils.isEmpty(GTotal) && !TextUtils.isEmpty(DocEntry1) &&
                                        !TextUtils.isEmpty(CardCode) && !TextUtils.isEmpty(CardName) &&
                                        !TextUtils.isEmpty(Address) && !TextUtils.isEmpty(Route) &&
                                        !TextUtils.isEmpty(OrderType) && !TextUtils.isEmpty(OrderTypeName) &&
                                        !TextUtils.isEmpty(PONo) && !TextUtils.isEmpty(PODate) &&
                                        !TextUtils.isEmpty(DeliveryNo) && !TextUtils.isEmpty(DeliveryDate) &&
                                        !TextUtils.isEmpty(DocStatus) && !TextUtils.isEmpty(DocStatusName) &&
                                        !TextUtils.isEmpty(SalesEmp) && !TextUtils.isEmpty(DeliveryEmp) &&
                                        !TextUtils.isEmpty(DocTotal) && !TextUtils.isEmpty(DocVATTotal) &&
                                        !TextUtils.isEmpty(DocGToal) && !TextUtils.isEmpty(Remark) && !TextUtils.isEmpty(CreateDate)) {

                                    OrderbyDoc orders = new OrderbyDoc();
                                    orders.setMyId(MyId);
                                    orders.setDocEntry(DocEntry);
                                    orders.setItemCode(ItemCode);
                                    orders.setBarCode(BarCode);
                                    orders.setItemName(ItemName);
                                    orders.setQuantity(Quantity);
                                    orders.setUoM(UoM);
                                    orders.setPrice(Price);
                                    orders.setDiscount(Discount);
                                    orders.setPriceAfterDiscount(PriceAfterDiscount);
                                    orders.setVATAmt(VATAmt);
                                    orders.setVATPercent(VATPercent);
                                    orders.setGTotal(GTotal);
                                    orders.setDocEntry1(DocEntry);
                                    orders.setCardCode(CardCode);
                                    orders.setCardName(CardName);
                                    orders.setAddress(Address);
                                    orders.setRoute(OrderTypeName);
                                    orders.setOrderType(PONo);
                                    orders.setOrderTypeName(PODate);
                                    orders.setPONo(DeliveryNo);
                                    orders.setPODate(DeliveryDate);
                                    orders.setDeliveryNo(DocStatus);
                                    orders.setDeliveryDate(DocStatusName);
                                    orders.setDocStatus(SalesEmp);
                                    orders.setDocStatusName(DeliveryEmp);
                                    orders.setSalesEmp(DocTotal);
                                    orders.setDeliveryEmp(DocEntry);
                                    orders.setDocTotal(CardCode);
                                    orders.setDocVATTotal(DocVATTotal);
                                    orders.setDocGToal(DocGToal);
                                    orders.setRemark(Remark);
                                    orders.setCreateDate(CreateDate);
                                    orderbyDocs.add(orders);

                                    MyId = null;
                                    DocEntry = null;
                                    ItemCode = null;
                                    BarCode = null;
                                    ItemName = null;
                                    Quantity = null;
                                    UoM = null;
                                    Price = null;
                                    Discount = null;
                                    PriceAfterDiscount = null;
                                    VATAmt = null;
                                    VATPercent = null;
                                    GTotal = null;
                                    DocEntry1 = null;
                                    CardCode = null;
                                    CardName = null;
                                    Address = null;
                                    Route = null;
                                    OrderType = null;
                                    OrderTypeName = null;
                                    PONo = null;
                                    PODate = null;
                                    DeliveryNo = null;
                                    DeliveryDate = null;
                                    DocStatus = null;
                                    DocStatusName = null;
                                    SalesEmp = null;
                                    DeliveryEmp = null;
                                    DocTotal = null;
                                    DocVATTotal = null;
                                    DocGToal = null;
                                    Remark = null;
                                    CreateDate = null;
                                }
                            }

                            if (readyToQuantity) {
                                Quantity = data;
                                readyToQuantity = false;
                                if (!TextUtils.isEmpty(MyId) && !TextUtils.isEmpty(DocEntry) &&
                                        !TextUtils.isEmpty(ItemCode) && !TextUtils.isEmpty(BarCode) &&
                                        !TextUtils.isEmpty(ItemName) && !TextUtils.isEmpty(Quantity) &&
                                        !TextUtils.isEmpty(UoM) && !TextUtils.isEmpty(Price) &&
                                        !TextUtils.isEmpty(Discount) && !TextUtils.isEmpty(PriceAfterDiscount) &&
                                        !TextUtils.isEmpty(VATAmt) && !TextUtils.isEmpty(VATPercent) &&
                                        !TextUtils.isEmpty(GTotal) && !TextUtils.isEmpty(DocEntry1) &&
                                        !TextUtils.isEmpty(CardCode) && !TextUtils.isEmpty(CardName) &&
                                        !TextUtils.isEmpty(Address) && !TextUtils.isEmpty(Route) &&
                                        !TextUtils.isEmpty(OrderType) && !TextUtils.isEmpty(OrderTypeName) &&
                                        !TextUtils.isEmpty(PONo) && !TextUtils.isEmpty(PODate) &&
                                        !TextUtils.isEmpty(DeliveryNo) && !TextUtils.isEmpty(DeliveryDate) &&
                                        !TextUtils.isEmpty(DocStatus) && !TextUtils.isEmpty(DocStatusName) &&
                                        !TextUtils.isEmpty(SalesEmp) && !TextUtils.isEmpty(DeliveryEmp) &&
                                        !TextUtils.isEmpty(DocTotal) && !TextUtils.isEmpty(DocVATTotal) &&
                                        !TextUtils.isEmpty(DocGToal) && !TextUtils.isEmpty(Remark) && !TextUtils.isEmpty(CreateDate)) {

                                    OrderbyDoc orders = new OrderbyDoc();
                                    orders.setMyId(MyId);
                                    orders.setDocEntry(DocEntry);
                                    orders.setItemCode(ItemCode);
                                    orders.setBarCode(BarCode);
                                    orders.setItemName(ItemName);
                                    orders.setQuantity(Quantity);
                                    orders.setUoM(UoM);
                                    orders.setPrice(Price);
                                    orders.setDiscount(Discount);
                                    orders.setPriceAfterDiscount(PriceAfterDiscount);
                                    orders.setVATAmt(VATAmt);
                                    orders.setVATPercent(VATPercent);
                                    orders.setGTotal(GTotal);
                                    orders.setDocEntry1(DocEntry);
                                    orders.setCardCode(CardCode);
                                    orders.setCardName(CardName);
                                    orders.setAddress(Address);
                                    orders.setRoute(OrderTypeName);
                                    orders.setOrderType(PONo);
                                    orders.setOrderTypeName(PODate);
                                    orders.setPONo(DeliveryNo);
                                    orders.setPODate(DeliveryDate);
                                    orders.setDeliveryNo(DocStatus);
                                    orders.setDeliveryDate(DocStatusName);
                                    orders.setDocStatus(SalesEmp);
                                    orders.setDocStatusName(DeliveryEmp);
                                    orders.setSalesEmp(DocTotal);
                                    orders.setDeliveryEmp(DocEntry);
                                    orders.setDocTotal(CardCode);
                                    orders.setDocVATTotal(DocVATTotal);
                                    orders.setDocGToal(DocGToal);
                                    orders.setRemark(Remark);
                                    orders.setCreateDate(CreateDate);
                                    orderbyDocs.add(orders);

                                    MyId = null;
                                    DocEntry = null;
                                    ItemCode = null;
                                    BarCode = null;
                                    ItemName = null;
                                    Quantity = null;
                                    UoM = null;
                                    Price = null;
                                    Discount = null;
                                    PriceAfterDiscount = null;
                                    VATAmt = null;
                                    VATPercent = null;
                                    GTotal = null;
                                    DocEntry1 = null;
                                    CardCode = null;
                                    CardName = null;
                                    Address = null;
                                    Route = null;
                                    OrderType = null;
                                    OrderTypeName = null;
                                    PONo = null;
                                    PODate = null;
                                    DeliveryNo = null;
                                    DeliveryDate = null;
                                    DocStatus = null;
                                    DocStatusName = null;
                                    SalesEmp = null;
                                    DeliveryEmp = null;
                                    DocTotal = null;
                                    DocVATTotal = null;
                                    DocGToal = null;
                                    Remark = null;
                                    CreateDate = null;
                                }
                            }

                            if (readyToUoM) {
                                UoM = data;
                                readyToUoM = false;
                                if (!TextUtils.isEmpty(MyId) && !TextUtils.isEmpty(DocEntry) &&
                                        !TextUtils.isEmpty(ItemCode) && !TextUtils.isEmpty(BarCode) &&
                                        !TextUtils.isEmpty(ItemName) && !TextUtils.isEmpty(Quantity) &&
                                        !TextUtils.isEmpty(UoM) && !TextUtils.isEmpty(Price) &&
                                        !TextUtils.isEmpty(Discount) && !TextUtils.isEmpty(PriceAfterDiscount) &&
                                        !TextUtils.isEmpty(VATAmt) && !TextUtils.isEmpty(VATPercent) &&
                                        !TextUtils.isEmpty(GTotal) && !TextUtils.isEmpty(DocEntry1) &&
                                        !TextUtils.isEmpty(CardCode) && !TextUtils.isEmpty(CardName) &&
                                        !TextUtils.isEmpty(Address) && !TextUtils.isEmpty(Route) &&
                                        !TextUtils.isEmpty(OrderType) && !TextUtils.isEmpty(OrderTypeName) &&
                                        !TextUtils.isEmpty(PONo) && !TextUtils.isEmpty(PODate) &&
                                        !TextUtils.isEmpty(DeliveryNo) && !TextUtils.isEmpty(DeliveryDate) &&
                                        !TextUtils.isEmpty(DocStatus) && !TextUtils.isEmpty(DocStatusName) &&
                                        !TextUtils.isEmpty(SalesEmp) && !TextUtils.isEmpty(DeliveryEmp) &&
                                        !TextUtils.isEmpty(DocTotal) && !TextUtils.isEmpty(DocVATTotal) &&
                                        !TextUtils.isEmpty(DocGToal) && !TextUtils.isEmpty(Remark) && !TextUtils.isEmpty(CreateDate)) {

                                    OrderbyDoc orders = new OrderbyDoc();
                                    orders.setMyId(MyId);
                                    orders.setDocEntry(DocEntry);
                                    orders.setItemCode(ItemCode);
                                    orders.setBarCode(BarCode);
                                    orders.setItemName(ItemName);
                                    orders.setQuantity(Quantity);
                                    orders.setUoM(UoM);
                                    orders.setPrice(Price);
                                    orders.setDiscount(Discount);
                                    orders.setPriceAfterDiscount(PriceAfterDiscount);
                                    orders.setVATAmt(VATAmt);
                                    orders.setVATPercent(VATPercent);
                                    orders.setGTotal(GTotal);
                                    orders.setDocEntry1(DocEntry);
                                    orders.setCardCode(CardCode);
                                    orders.setCardName(CardName);
                                    orders.setAddress(Address);
                                    orders.setRoute(OrderTypeName);
                                    orders.setOrderType(PONo);
                                    orders.setOrderTypeName(PODate);
                                    orders.setPONo(DeliveryNo);
                                    orders.setPODate(DeliveryDate);
                                    orders.setDeliveryNo(DocStatus);
                                    orders.setDeliveryDate(DocStatusName);
                                    orders.setDocStatus(SalesEmp);
                                    orders.setDocStatusName(DeliveryEmp);
                                    orders.setSalesEmp(DocTotal);
                                    orders.setDeliveryEmp(DocEntry);
                                    orders.setDocTotal(CardCode);
                                    orders.setDocVATTotal(DocVATTotal);
                                    orders.setDocGToal(DocGToal);
                                    orders.setRemark(Remark);
                                    orders.setCreateDate(CreateDate);
                                    orderbyDocs.add(orders);

                                    MyId = null;
                                    DocEntry = null;
                                    ItemCode = null;
                                    BarCode = null;
                                    ItemName = null;
                                    Quantity = null;
                                    UoM = null;
                                    Price = null;
                                    Discount = null;
                                    PriceAfterDiscount = null;
                                    VATAmt = null;
                                    VATPercent = null;
                                    GTotal = null;
                                    DocEntry1 = null;
                                    CardCode = null;
                                    CardName = null;
                                    Address = null;
                                    Route = null;
                                    OrderType = null;
                                    OrderTypeName = null;
                                    PONo = null;
                                    PODate = null;
                                    DeliveryNo = null;
                                    DeliveryDate = null;
                                    DocStatus = null;
                                    DocStatusName = null;
                                    SalesEmp = null;
                                    DeliveryEmp = null;
                                    DocTotal = null;
                                    DocVATTotal = null;
                                    DocGToal = null;
                                    Remark = null;
                                    CreateDate = null;
                                }
                            }

                            if (readyToPrice) {
                                Price = data;
                                readyToPrice = false;
                                if (!TextUtils.isEmpty(MyId) && !TextUtils.isEmpty(DocEntry) &&
                                        !TextUtils.isEmpty(ItemCode) && !TextUtils.isEmpty(BarCode) &&
                                        !TextUtils.isEmpty(ItemName) && !TextUtils.isEmpty(Quantity) &&
                                        !TextUtils.isEmpty(UoM) && !TextUtils.isEmpty(Price) &&
                                        !TextUtils.isEmpty(Discount) && !TextUtils.isEmpty(PriceAfterDiscount) &&
                                        !TextUtils.isEmpty(VATAmt) && !TextUtils.isEmpty(VATPercent) &&
                                        !TextUtils.isEmpty(GTotal) && !TextUtils.isEmpty(DocEntry1) &&
                                        !TextUtils.isEmpty(CardCode) && !TextUtils.isEmpty(CardName) &&
                                        !TextUtils.isEmpty(Address) && !TextUtils.isEmpty(Route) &&
                                        !TextUtils.isEmpty(OrderType) && !TextUtils.isEmpty(OrderTypeName) &&
                                        !TextUtils.isEmpty(PONo) && !TextUtils.isEmpty(PODate) &&
                                        !TextUtils.isEmpty(DeliveryNo) && !TextUtils.isEmpty(DeliveryDate) &&
                                        !TextUtils.isEmpty(DocStatus) && !TextUtils.isEmpty(DocStatusName) &&
                                        !TextUtils.isEmpty(SalesEmp) && !TextUtils.isEmpty(DeliveryEmp) &&
                                        !TextUtils.isEmpty(DocTotal) && !TextUtils.isEmpty(DocVATTotal) &&
                                        !TextUtils.isEmpty(DocGToal) && !TextUtils.isEmpty(Remark) && !TextUtils.isEmpty(CreateDate)) {

                                    OrderbyDoc orders = new OrderbyDoc();
                                    orders.setMyId(MyId);
                                    orders.setDocEntry(DocEntry);
                                    orders.setItemCode(ItemCode);
                                    orders.setBarCode(BarCode);
                                    orders.setItemName(ItemName);
                                    orders.setQuantity(Quantity);
                                    orders.setUoM(UoM);
                                    orders.setPrice(Price);
                                    orders.setDiscount(Discount);
                                    orders.setPriceAfterDiscount(PriceAfterDiscount);
                                    orders.setVATAmt(VATAmt);
                                    orders.setVATPercent(VATPercent);
                                    orders.setGTotal(GTotal);
                                    orders.setDocEntry1(DocEntry);
                                    orders.setCardCode(CardCode);
                                    orders.setCardName(CardName);
                                    orders.setAddress(Address);
                                    orders.setRoute(OrderTypeName);
                                    orders.setOrderType(PONo);
                                    orders.setOrderTypeName(PODate);
                                    orders.setPONo(DeliveryNo);
                                    orders.setPODate(DeliveryDate);
                                    orders.setDeliveryNo(DocStatus);
                                    orders.setDeliveryDate(DocStatusName);
                                    orders.setDocStatus(SalesEmp);
                                    orders.setDocStatusName(DeliveryEmp);
                                    orders.setSalesEmp(DocTotal);
                                    orders.setDeliveryEmp(DocEntry);
                                    orders.setDocTotal(CardCode);
                                    orders.setDocVATTotal(DocVATTotal);
                                    orders.setDocGToal(DocGToal);
                                    orders.setRemark(Remark);
                                    orders.setCreateDate(CreateDate);
                                    orderbyDocs.add(orders);

                                    MyId = null;
                                    DocEntry = null;
                                    ItemCode = null;
                                    BarCode = null;
                                    ItemName = null;
                                    Quantity = null;
                                    UoM = null;
                                    Price = null;
                                    Discount = null;
                                    PriceAfterDiscount = null;
                                    VATAmt = null;
                                    VATPercent = null;
                                    GTotal = null;
                                    DocEntry1 = null;
                                    CardCode = null;
                                    CardName = null;
                                    Address = null;
                                    Route = null;
                                    OrderType = null;
                                    OrderTypeName = null;
                                    PONo = null;
                                    PODate = null;
                                    DeliveryNo = null;
                                    DeliveryDate = null;
                                    DocStatus = null;
                                    DocStatusName = null;
                                    SalesEmp = null;
                                    DeliveryEmp = null;
                                    DocTotal = null;
                                    DocVATTotal = null;
                                    DocGToal = null;
                                    Remark = null;
                                    CreateDate = null;
                                }
                            }

                            if (readyToDiscount) {
                                Discount = data;
                                readyToDiscount = false;
                                if (!TextUtils.isEmpty(MyId) && !TextUtils.isEmpty(DocEntry) &&
                                        !TextUtils.isEmpty(ItemCode) && !TextUtils.isEmpty(BarCode) &&
                                        !TextUtils.isEmpty(ItemName) && !TextUtils.isEmpty(Quantity) &&
                                        !TextUtils.isEmpty(UoM) && !TextUtils.isEmpty(Price) &&
                                        !TextUtils.isEmpty(Discount) && !TextUtils.isEmpty(PriceAfterDiscount) &&
                                        !TextUtils.isEmpty(VATAmt) && !TextUtils.isEmpty(VATPercent) &&
                                        !TextUtils.isEmpty(GTotal) && !TextUtils.isEmpty(DocEntry1) &&
                                        !TextUtils.isEmpty(CardCode) && !TextUtils.isEmpty(CardName) &&
                                        !TextUtils.isEmpty(Address) && !TextUtils.isEmpty(Route) &&
                                        !TextUtils.isEmpty(OrderType) && !TextUtils.isEmpty(OrderTypeName) &&
                                        !TextUtils.isEmpty(PONo) && !TextUtils.isEmpty(PODate) &&
                                        !TextUtils.isEmpty(DeliveryNo) && !TextUtils.isEmpty(DeliveryDate) &&
                                        !TextUtils.isEmpty(DocStatus) && !TextUtils.isEmpty(DocStatusName) &&
                                        !TextUtils.isEmpty(SalesEmp) && !TextUtils.isEmpty(DeliveryEmp) &&
                                        !TextUtils.isEmpty(DocTotal) && !TextUtils.isEmpty(DocVATTotal) &&
                                        !TextUtils.isEmpty(DocGToal) && !TextUtils.isEmpty(Remark) && !TextUtils.isEmpty(CreateDate)) {

                                    OrderbyDoc orders = new OrderbyDoc();
                                    orders.setMyId(MyId);
                                    orders.setDocEntry(DocEntry);
                                    orders.setItemCode(ItemCode);
                                    orders.setBarCode(BarCode);
                                    orders.setItemName(ItemName);
                                    orders.setQuantity(Quantity);
                                    orders.setUoM(UoM);
                                    orders.setPrice(Price);
                                    orders.setDiscount(Discount);
                                    orders.setPriceAfterDiscount(PriceAfterDiscount);
                                    orders.setVATAmt(VATAmt);
                                    orders.setVATPercent(VATPercent);
                                    orders.setGTotal(GTotal);
                                    orders.setDocEntry1(DocEntry);
                                    orders.setCardCode(CardCode);
                                    orders.setCardName(CardName);
                                    orders.setAddress(Address);
                                    orders.setRoute(OrderTypeName);
                                    orders.setOrderType(PONo);
                                    orders.setOrderTypeName(PODate);
                                    orders.setPONo(DeliveryNo);
                                    orders.setPODate(DeliveryDate);
                                    orders.setDeliveryNo(DocStatus);
                                    orders.setDeliveryDate(DocStatusName);
                                    orders.setDocStatus(SalesEmp);
                                    orders.setDocStatusName(DeliveryEmp);
                                    orders.setSalesEmp(DocTotal);
                                    orders.setDeliveryEmp(DocEntry);
                                    orders.setDocTotal(CardCode);
                                    orders.setDocVATTotal(DocVATTotal);
                                    orders.setDocGToal(DocGToal);
                                    orders.setRemark(Remark);
                                    orders.setCreateDate(CreateDate);
                                    orderbyDocs.add(orders);

                                    MyId = null;
                                    DocEntry = null;
                                    ItemCode = null;
                                    BarCode = null;
                                    ItemName = null;
                                    Quantity = null;
                                    UoM = null;
                                    Price = null;
                                    Discount = null;
                                    PriceAfterDiscount = null;
                                    VATAmt = null;
                                    VATPercent = null;
                                    GTotal = null;
                                    DocEntry1 = null;
                                    CardCode = null;
                                    CardName = null;
                                    Address = null;
                                    Route = null;
                                    OrderType = null;
                                    OrderTypeName = null;
                                    PONo = null;
                                    PODate = null;
                                    DeliveryNo = null;
                                    DeliveryDate = null;
                                    DocStatus = null;
                                    DocStatusName = null;
                                    SalesEmp = null;
                                    DeliveryEmp = null;
                                    DocTotal = null;
                                    DocVATTotal = null;
                                    DocGToal = null;
                                    Remark = null;
                                    CreateDate = null;
                                }
                            }

                            if (readyToPriceAfterDiscount) {
                                PriceAfterDiscount = data;
                                readyToPriceAfterDiscount = false;
                                if (!TextUtils.isEmpty(MyId) && !TextUtils.isEmpty(DocEntry) &&
                                        !TextUtils.isEmpty(ItemCode) && !TextUtils.isEmpty(BarCode) &&
                                        !TextUtils.isEmpty(ItemName) && !TextUtils.isEmpty(Quantity) &&
                                        !TextUtils.isEmpty(UoM) && !TextUtils.isEmpty(Price) &&
                                        !TextUtils.isEmpty(Discount) && !TextUtils.isEmpty(PriceAfterDiscount) &&
                                        !TextUtils.isEmpty(VATAmt) && !TextUtils.isEmpty(VATPercent) &&
                                        !TextUtils.isEmpty(GTotal) && !TextUtils.isEmpty(DocEntry1) &&
                                        !TextUtils.isEmpty(CardCode) && !TextUtils.isEmpty(CardName) &&
                                        !TextUtils.isEmpty(Address) && !TextUtils.isEmpty(Route) &&
                                        !TextUtils.isEmpty(OrderType) && !TextUtils.isEmpty(OrderTypeName) &&
                                        !TextUtils.isEmpty(PONo) && !TextUtils.isEmpty(PODate) &&
                                        !TextUtils.isEmpty(DeliveryNo) && !TextUtils.isEmpty(DeliveryDate) &&
                                        !TextUtils.isEmpty(DocStatus) && !TextUtils.isEmpty(DocStatusName) &&
                                        !TextUtils.isEmpty(SalesEmp) && !TextUtils.isEmpty(DeliveryEmp) &&
                                        !TextUtils.isEmpty(DocTotal) && !TextUtils.isEmpty(DocVATTotal) &&
                                        !TextUtils.isEmpty(DocGToal) && !TextUtils.isEmpty(Remark) && !TextUtils.isEmpty(CreateDate)) {

                                    OrderbyDoc orders = new OrderbyDoc();
                                    orders.setMyId(MyId);
                                    orders.setDocEntry(DocEntry);
                                    orders.setItemCode(ItemCode);
                                    orders.setBarCode(BarCode);
                                    orders.setItemName(ItemName);
                                    orders.setQuantity(Quantity);
                                    orders.setUoM(UoM);
                                    orders.setPrice(Price);
                                    orders.setDiscount(Discount);
                                    orders.setPriceAfterDiscount(PriceAfterDiscount);
                                    orders.setVATAmt(VATAmt);
                                    orders.setVATPercent(VATPercent);
                                    orders.setGTotal(GTotal);
                                    orders.setDocEntry1(DocEntry);
                                    orders.setCardCode(CardCode);
                                    orders.setCardName(CardName);
                                    orders.setAddress(Address);
                                    orders.setRoute(OrderTypeName);
                                    orders.setOrderType(PONo);
                                    orders.setOrderTypeName(PODate);
                                    orders.setPONo(DeliveryNo);
                                    orders.setPODate(DeliveryDate);
                                    orders.setDeliveryNo(DocStatus);
                                    orders.setDeliveryDate(DocStatusName);
                                    orders.setDocStatus(SalesEmp);
                                    orders.setDocStatusName(DeliveryEmp);
                                    orders.setSalesEmp(DocTotal);
                                    orders.setDeliveryEmp(DocEntry);
                                    orders.setDocTotal(CardCode);
                                    orders.setDocVATTotal(DocVATTotal);
                                    orders.setDocGToal(DocGToal);
                                    orders.setRemark(Remark);
                                    orders.setCreateDate(CreateDate);
                                    orderbyDocs.add(orders);

                                    MyId = null;
                                    DocEntry = null;
                                    ItemCode = null;
                                    BarCode = null;
                                    ItemName = null;
                                    Quantity = null;
                                    UoM = null;
                                    Price = null;
                                    Discount = null;
                                    PriceAfterDiscount = null;
                                    VATAmt = null;
                                    VATPercent = null;
                                    GTotal = null;
                                    DocEntry1 = null;
                                    CardCode = null;
                                    CardName = null;
                                    Address = null;
                                    Route = null;
                                    OrderType = null;
                                    OrderTypeName = null;
                                    PONo = null;
                                    PODate = null;
                                    DeliveryNo = null;
                                    DeliveryDate = null;
                                    DocStatus = null;
                                    DocStatusName = null;
                                    SalesEmp = null;
                                    DeliveryEmp = null;
                                    DocTotal = null;
                                    DocVATTotal = null;
                                    DocGToal = null;
                                    Remark = null;
                                    CreateDate = null;
                                }
                            }

                            if (readyToVATAmt) {
                                VATAmt = data;
                                readyToVATAmt = false;
                                if (!TextUtils.isEmpty(MyId) && !TextUtils.isEmpty(DocEntry) &&
                                        !TextUtils.isEmpty(ItemCode) && !TextUtils.isEmpty(BarCode) &&
                                        !TextUtils.isEmpty(ItemName) && !TextUtils.isEmpty(Quantity) &&
                                        !TextUtils.isEmpty(UoM) && !TextUtils.isEmpty(Price) &&
                                        !TextUtils.isEmpty(Discount) && !TextUtils.isEmpty(PriceAfterDiscount) &&
                                        !TextUtils.isEmpty(VATAmt) && !TextUtils.isEmpty(VATPercent) &&
                                        !TextUtils.isEmpty(GTotal) && !TextUtils.isEmpty(DocEntry1) &&
                                        !TextUtils.isEmpty(CardCode) && !TextUtils.isEmpty(CardName) &&
                                        !TextUtils.isEmpty(Address) && !TextUtils.isEmpty(Route) &&
                                        !TextUtils.isEmpty(OrderType) && !TextUtils.isEmpty(OrderTypeName) &&
                                        !TextUtils.isEmpty(PONo) && !TextUtils.isEmpty(PODate) &&
                                        !TextUtils.isEmpty(DeliveryNo) && !TextUtils.isEmpty(DeliveryDate) &&
                                        !TextUtils.isEmpty(DocStatus) && !TextUtils.isEmpty(DocStatusName) &&
                                        !TextUtils.isEmpty(SalesEmp) && !TextUtils.isEmpty(DeliveryEmp) &&
                                        !TextUtils.isEmpty(DocTotal) && !TextUtils.isEmpty(DocVATTotal) &&
                                        !TextUtils.isEmpty(DocGToal) && !TextUtils.isEmpty(Remark) && !TextUtils.isEmpty(CreateDate)) {

                                    OrderbyDoc orders = new OrderbyDoc();
                                    orders.setMyId(MyId);
                                    orders.setDocEntry(DocEntry);
                                    orders.setItemCode(ItemCode);
                                    orders.setBarCode(BarCode);
                                    orders.setItemName(ItemName);
                                    orders.setQuantity(Quantity);
                                    orders.setUoM(UoM);
                                    orders.setPrice(Price);
                                    orders.setDiscount(Discount);
                                    orders.setPriceAfterDiscount(PriceAfterDiscount);
                                    orders.setVATAmt(VATAmt);
                                    orders.setVATPercent(VATPercent);
                                    orders.setGTotal(GTotal);
                                    orders.setDocEntry1(DocEntry);
                                    orders.setCardCode(CardCode);
                                    orders.setCardName(CardName);
                                    orders.setAddress(Address);
                                    orders.setRoute(OrderTypeName);
                                    orders.setOrderType(PONo);
                                    orders.setOrderTypeName(PODate);
                                    orders.setPONo(DeliveryNo);
                                    orders.setPODate(DeliveryDate);
                                    orders.setDeliveryNo(DocStatus);
                                    orders.setDeliveryDate(DocStatusName);
                                    orders.setDocStatus(SalesEmp);
                                    orders.setDocStatusName(DeliveryEmp);
                                    orders.setSalesEmp(DocTotal);
                                    orders.setDeliveryEmp(DocEntry);
                                    orders.setDocTotal(CardCode);
                                    orders.setDocVATTotal(DocVATTotal);
                                    orders.setDocGToal(DocGToal);
                                    orders.setRemark(Remark);
                                    orders.setCreateDate(CreateDate);
                                    orderbyDocs.add(orders);

                                    MyId = null;
                                    DocEntry = null;
                                    ItemCode = null;
                                    BarCode = null;
                                    ItemName = null;
                                    Quantity = null;
                                    UoM = null;
                                    Price = null;
                                    Discount = null;
                                    PriceAfterDiscount = null;
                                    VATAmt = null;
                                    VATPercent = null;
                                    GTotal = null;
                                    DocEntry1 = null;
                                    CardCode = null;
                                    CardName = null;
                                    Address = null;
                                    Route = null;
                                    OrderType = null;
                                    OrderTypeName = null;
                                    PONo = null;
                                    PODate = null;
                                    DeliveryNo = null;
                                    DeliveryDate = null;
                                    DocStatus = null;
                                    DocStatusName = null;
                                    SalesEmp = null;
                                    DeliveryEmp = null;
                                    DocTotal = null;
                                    DocVATTotal = null;
                                    DocGToal = null;
                                    Remark = null;
                                    CreateDate = null;
                                }
                            }

                            if (readyToVATPercent) {
                                VATPercent = data;
                                readyToVATPercent = false;
                                if (!TextUtils.isEmpty(MyId) && !TextUtils.isEmpty(DocEntry) &&
                                        !TextUtils.isEmpty(ItemCode) && !TextUtils.isEmpty(BarCode) &&
                                        !TextUtils.isEmpty(ItemName) && !TextUtils.isEmpty(Quantity) &&
                                        !TextUtils.isEmpty(UoM) && !TextUtils.isEmpty(Price) &&
                                        !TextUtils.isEmpty(Discount) && !TextUtils.isEmpty(PriceAfterDiscount) &&
                                        !TextUtils.isEmpty(VATAmt) && !TextUtils.isEmpty(VATPercent) &&
                                        !TextUtils.isEmpty(GTotal) && !TextUtils.isEmpty(DocEntry1) &&
                                        !TextUtils.isEmpty(CardCode) && !TextUtils.isEmpty(CardName) &&
                                        !TextUtils.isEmpty(Address) && !TextUtils.isEmpty(Route) &&
                                        !TextUtils.isEmpty(OrderType) && !TextUtils.isEmpty(OrderTypeName) &&
                                        !TextUtils.isEmpty(PONo) && !TextUtils.isEmpty(PODate) &&
                                        !TextUtils.isEmpty(DeliveryNo) && !TextUtils.isEmpty(DeliveryDate) &&
                                        !TextUtils.isEmpty(DocStatus) && !TextUtils.isEmpty(DocStatusName) &&
                                        !TextUtils.isEmpty(SalesEmp) && !TextUtils.isEmpty(DeliveryEmp) &&
                                        !TextUtils.isEmpty(DocTotal) && !TextUtils.isEmpty(DocVATTotal) &&
                                        !TextUtils.isEmpty(DocGToal) && !TextUtils.isEmpty(Remark) && !TextUtils.isEmpty(CreateDate)) {

                                    OrderbyDoc orders = new OrderbyDoc();
                                    orders.setMyId(MyId);
                                    orders.setDocEntry(DocEntry);
                                    orders.setItemCode(ItemCode);
                                    orders.setBarCode(BarCode);
                                    orders.setItemName(ItemName);
                                    orders.setQuantity(Quantity);
                                    orders.setUoM(UoM);
                                    orders.setPrice(Price);
                                    orders.setDiscount(Discount);
                                    orders.setPriceAfterDiscount(PriceAfterDiscount);
                                    orders.setVATAmt(VATAmt);
                                    orders.setVATPercent(VATPercent);
                                    orders.setGTotal(GTotal);
                                    orders.setDocEntry1(DocEntry);
                                    orders.setCardCode(CardCode);
                                    orders.setCardName(CardName);
                                    orders.setAddress(Address);
                                    orders.setRoute(OrderTypeName);
                                    orders.setOrderType(PONo);
                                    orders.setOrderTypeName(PODate);
                                    orders.setPONo(DeliveryNo);
                                    orders.setPODate(DeliveryDate);
                                    orders.setDeliveryNo(DocStatus);
                                    orders.setDeliveryDate(DocStatusName);
                                    orders.setDocStatus(SalesEmp);
                                    orders.setDocStatusName(DeliveryEmp);
                                    orders.setSalesEmp(DocTotal);
                                    orders.setDeliveryEmp(DocEntry);
                                    orders.setDocTotal(CardCode);
                                    orders.setDocVATTotal(DocVATTotal);
                                    orders.setDocGToal(DocGToal);
                                    orders.setRemark(Remark);
                                    orders.setCreateDate(CreateDate);
                                    orderbyDocs.add(orders);

                                    MyId = null;
                                    DocEntry = null;
                                    ItemCode = null;
                                    BarCode = null;
                                    ItemName = null;
                                    Quantity = null;
                                    UoM = null;
                                    Price = null;
                                    Discount = null;
                                    PriceAfterDiscount = null;
                                    VATAmt = null;
                                    VATPercent = null;
                                    GTotal = null;
                                    DocEntry1 = null;
                                    CardCode = null;
                                    CardName = null;
                                    Address = null;
                                    Route = null;
                                    OrderType = null;
                                    OrderTypeName = null;
                                    PONo = null;
                                    PODate = null;
                                    DeliveryNo = null;
                                    DeliveryDate = null;
                                    DocStatus = null;
                                    DocStatusName = null;
                                    SalesEmp = null;
                                    DeliveryEmp = null;
                                    DocTotal = null;
                                    DocVATTotal = null;
                                    DocGToal = null;
                                    Remark = null;
                                    CreateDate = null;
                                }
                            }

                            if (readyToGTotal) {
                                GTotal = data;
                                readyToGTotal = false;
                                if (!TextUtils.isEmpty(MyId) && !TextUtils.isEmpty(DocEntry) &&
                                        !TextUtils.isEmpty(ItemCode) && !TextUtils.isEmpty(BarCode) &&
                                        !TextUtils.isEmpty(ItemName) && !TextUtils.isEmpty(Quantity) &&
                                        !TextUtils.isEmpty(UoM) && !TextUtils.isEmpty(Price) &&
                                        !TextUtils.isEmpty(Discount) && !TextUtils.isEmpty(PriceAfterDiscount) &&
                                        !TextUtils.isEmpty(VATAmt) && !TextUtils.isEmpty(VATPercent) &&
                                        !TextUtils.isEmpty(GTotal) && !TextUtils.isEmpty(DocEntry1) &&
                                        !TextUtils.isEmpty(CardCode) && !TextUtils.isEmpty(CardName) &&
                                        !TextUtils.isEmpty(Address) && !TextUtils.isEmpty(Route) &&
                                        !TextUtils.isEmpty(OrderType) && !TextUtils.isEmpty(OrderTypeName) &&
                                        !TextUtils.isEmpty(PONo) && !TextUtils.isEmpty(PODate) &&
                                        !TextUtils.isEmpty(DeliveryNo) && !TextUtils.isEmpty(DeliveryDate) &&
                                        !TextUtils.isEmpty(DocStatus) && !TextUtils.isEmpty(DocStatusName) &&
                                        !TextUtils.isEmpty(SalesEmp) && !TextUtils.isEmpty(DeliveryEmp) &&
                                        !TextUtils.isEmpty(DocTotal) && !TextUtils.isEmpty(DocVATTotal) &&
                                        !TextUtils.isEmpty(DocGToal) && !TextUtils.isEmpty(Remark) && !TextUtils.isEmpty(CreateDate)) {

                                    OrderbyDoc orders = new OrderbyDoc();
                                    orders.setMyId(MyId);
                                    orders.setDocEntry(DocEntry);
                                    orders.setItemCode(ItemCode);
                                    orders.setBarCode(BarCode);
                                    orders.setItemName(ItemName);
                                    orders.setQuantity(Quantity);
                                    orders.setUoM(UoM);
                                    orders.setPrice(Price);
                                    orders.setDiscount(Discount);
                                    orders.setPriceAfterDiscount(PriceAfterDiscount);
                                    orders.setVATAmt(VATAmt);
                                    orders.setVATPercent(VATPercent);
                                    orders.setGTotal(GTotal);
                                    orders.setDocEntry1(DocEntry);
                                    orders.setCardCode(CardCode);
                                    orders.setCardName(CardName);
                                    orders.setAddress(Address);
                                    orders.setRoute(OrderTypeName);
                                    orders.setOrderType(PONo);
                                    orders.setOrderTypeName(PODate);
                                    orders.setPONo(DeliveryNo);
                                    orders.setPODate(DeliveryDate);
                                    orders.setDeliveryNo(DocStatus);
                                    orders.setDeliveryDate(DocStatusName);
                                    orders.setDocStatus(SalesEmp);
                                    orders.setDocStatusName(DeliveryEmp);
                                    orders.setSalesEmp(DocTotal);
                                    orders.setDeliveryEmp(DocEntry);
                                    orders.setDocTotal(CardCode);
                                    orders.setDocVATTotal(DocVATTotal);
                                    orders.setDocGToal(DocGToal);
                                    orders.setRemark(Remark);
                                    orders.setCreateDate(CreateDate);
                                    orderbyDocs.add(orders);

                                    MyId = null;
                                    DocEntry = null;
                                    ItemCode = null;
                                    BarCode = null;
                                    ItemName = null;
                                    Quantity = null;
                                    UoM = null;
                                    Price = null;
                                    Discount = null;
                                    PriceAfterDiscount = null;
                                    VATAmt = null;
                                    VATPercent = null;
                                    GTotal = null;
                                    DocEntry1 = null;
                                    CardCode = null;
                                    CardName = null;
                                    Address = null;
                                    Route = null;
                                    OrderType = null;
                                    OrderTypeName = null;
                                    PONo = null;
                                    PODate = null;
                                    DeliveryNo = null;
                                    DeliveryDate = null;
                                    DocStatus = null;
                                    DocStatusName = null;
                                    SalesEmp = null;
                                    DeliveryEmp = null;
                                    DocTotal = null;
                                    DocVATTotal = null;
                                    DocGToal = null;
                                    Remark = null;
                                    CreateDate = null;
                                }
                            }

                            if (readyToDocEntry1) {
                                DocEntry1 = data;
                                readyToDocEntry1 = false;
                                if (!TextUtils.isEmpty(MyId) && !TextUtils.isEmpty(DocEntry) &&
                                        !TextUtils.isEmpty(ItemCode) && !TextUtils.isEmpty(BarCode) &&
                                        !TextUtils.isEmpty(ItemName) && !TextUtils.isEmpty(Quantity) &&
                                        !TextUtils.isEmpty(UoM) && !TextUtils.isEmpty(Price) &&
                                        !TextUtils.isEmpty(Discount) && !TextUtils.isEmpty(PriceAfterDiscount) &&
                                        !TextUtils.isEmpty(VATAmt) && !TextUtils.isEmpty(VATPercent) &&
                                        !TextUtils.isEmpty(GTotal) && !TextUtils.isEmpty(DocEntry1) &&
                                        !TextUtils.isEmpty(CardCode) && !TextUtils.isEmpty(CardName) &&
                                        !TextUtils.isEmpty(Address) && !TextUtils.isEmpty(Route) &&
                                        !TextUtils.isEmpty(OrderType) && !TextUtils.isEmpty(OrderTypeName) &&
                                        !TextUtils.isEmpty(PONo) && !TextUtils.isEmpty(PODate) &&
                                        !TextUtils.isEmpty(DeliveryNo) && !TextUtils.isEmpty(DeliveryDate) &&
                                        !TextUtils.isEmpty(DocStatus) && !TextUtils.isEmpty(DocStatusName) &&
                                        !TextUtils.isEmpty(SalesEmp) && !TextUtils.isEmpty(DeliveryEmp) &&
                                        !TextUtils.isEmpty(DocTotal) && !TextUtils.isEmpty(DocVATTotal) &&
                                        !TextUtils.isEmpty(DocGToal) && !TextUtils.isEmpty(Remark) && !TextUtils.isEmpty(CreateDate)) {

                                    OrderbyDoc orders = new OrderbyDoc();
                                    orders.setMyId(MyId);
                                    orders.setDocEntry(DocEntry);
                                    orders.setItemCode(ItemCode);
                                    orders.setBarCode(BarCode);
                                    orders.setItemName(ItemName);
                                    orders.setQuantity(Quantity);
                                    orders.setUoM(UoM);
                                    orders.setPrice(Price);
                                    orders.setDiscount(Discount);
                                    orders.setPriceAfterDiscount(PriceAfterDiscount);
                                    orders.setVATAmt(VATAmt);
                                    orders.setVATPercent(VATPercent);
                                    orders.setGTotal(GTotal);
                                    orders.setDocEntry1(DocEntry);
                                    orders.setCardCode(CardCode);
                                    orders.setCardName(CardName);
                                    orders.setAddress(Address);
                                    orders.setRoute(OrderTypeName);
                                    orders.setOrderType(PONo);
                                    orders.setOrderTypeName(PODate);
                                    orders.setPONo(DeliveryNo);
                                    orders.setPODate(DeliveryDate);
                                    orders.setDeliveryNo(DocStatus);
                                    orders.setDeliveryDate(DocStatusName);
                                    orders.setDocStatus(SalesEmp);
                                    orders.setDocStatusName(DeliveryEmp);
                                    orders.setSalesEmp(DocTotal);
                                    orders.setDeliveryEmp(DocEntry);
                                    orders.setDocTotal(CardCode);
                                    orders.setDocVATTotal(DocVATTotal);
                                    orders.setDocGToal(DocGToal);
                                    orders.setRemark(Remark);
                                    orders.setCreateDate(CreateDate);
                                    orderbyDocs.add(orders);

                                    MyId = null;
                                    DocEntry = null;
                                    ItemCode = null;
                                    BarCode = null;
                                    ItemName = null;
                                    Quantity = null;
                                    UoM = null;
                                    Price = null;
                                    Discount = null;
                                    PriceAfterDiscount = null;
                                    VATAmt = null;
                                    VATPercent = null;
                                    GTotal = null;
                                    DocEntry1 = null;
                                    CardCode = null;
                                    CardName = null;
                                    Address = null;
                                    Route = null;
                                    OrderType = null;
                                    OrderTypeName = null;
                                    PONo = null;
                                    PODate = null;
                                    DeliveryNo = null;
                                    DeliveryDate = null;
                                    DocStatus = null;
                                    DocStatusName = null;
                                    SalesEmp = null;
                                    DeliveryEmp = null;
                                    DocTotal = null;
                                    DocVATTotal = null;
                                    DocGToal = null;
                                    Remark = null;
                                    CreateDate = null;
                                }
                            }

                            if (readyToCardCode) {
                                CardCode = data;
                                readyToCardCode = false;
                                if (!TextUtils.isEmpty(MyId) && !TextUtils.isEmpty(DocEntry) &&
                                        !TextUtils.isEmpty(ItemCode) && !TextUtils.isEmpty(BarCode) &&
                                        !TextUtils.isEmpty(ItemName) && !TextUtils.isEmpty(Quantity) &&
                                        !TextUtils.isEmpty(UoM) && !TextUtils.isEmpty(Price) &&
                                        !TextUtils.isEmpty(Discount) && !TextUtils.isEmpty(PriceAfterDiscount) &&
                                        !TextUtils.isEmpty(VATAmt) && !TextUtils.isEmpty(VATPercent) &&
                                        !TextUtils.isEmpty(GTotal) && !TextUtils.isEmpty(DocEntry1) &&
                                        !TextUtils.isEmpty(CardCode) && !TextUtils.isEmpty(CardName) &&
                                        !TextUtils.isEmpty(Address) && !TextUtils.isEmpty(Route) &&
                                        !TextUtils.isEmpty(OrderType) && !TextUtils.isEmpty(OrderTypeName) &&
                                        !TextUtils.isEmpty(PONo) && !TextUtils.isEmpty(PODate) &&
                                        !TextUtils.isEmpty(DeliveryNo) && !TextUtils.isEmpty(DeliveryDate) &&
                                        !TextUtils.isEmpty(DocStatus) && !TextUtils.isEmpty(DocStatusName) &&
                                        !TextUtils.isEmpty(SalesEmp) && !TextUtils.isEmpty(DeliveryEmp) &&
                                        !TextUtils.isEmpty(DocTotal) && !TextUtils.isEmpty(DocVATTotal) &&
                                        !TextUtils.isEmpty(DocGToal) && !TextUtils.isEmpty(Remark) && !TextUtils.isEmpty(CreateDate)) {

                                    OrderbyDoc orders = new OrderbyDoc();
                                    orders.setMyId(MyId);
                                    orders.setDocEntry(DocEntry);
                                    orders.setItemCode(ItemCode);
                                    orders.setBarCode(BarCode);
                                    orders.setItemName(ItemName);
                                    orders.setQuantity(Quantity);
                                    orders.setUoM(UoM);
                                    orders.setPrice(Price);
                                    orders.setDiscount(Discount);
                                    orders.setPriceAfterDiscount(PriceAfterDiscount);
                                    orders.setVATAmt(VATAmt);
                                    orders.setVATPercent(VATPercent);
                                    orders.setGTotal(GTotal);
                                    orders.setDocEntry1(DocEntry);
                                    orders.setCardCode(CardCode);
                                    orders.setCardName(CardName);
                                    orders.setAddress(Address);
                                    orders.setRoute(OrderTypeName);
                                    orders.setOrderType(PONo);
                                    orders.setOrderTypeName(PODate);
                                    orders.setPONo(DeliveryNo);
                                    orders.setPODate(DeliveryDate);
                                    orders.setDeliveryNo(DocStatus);
                                    orders.setDeliveryDate(DocStatusName);
                                    orders.setDocStatus(SalesEmp);
                                    orders.setDocStatusName(DeliveryEmp);
                                    orders.setSalesEmp(DocTotal);
                                    orders.setDeliveryEmp(DocEntry);
                                    orders.setDocTotal(CardCode);
                                    orders.setDocVATTotal(DocVATTotal);
                                    orders.setDocGToal(DocGToal);
                                    orders.setRemark(Remark);
                                    orders.setCreateDate(CreateDate);
                                    orderbyDocs.add(orders);

                                    MyId = null;
                                    DocEntry = null;
                                    ItemCode = null;
                                    BarCode = null;
                                    ItemName = null;
                                    Quantity = null;
                                    UoM = null;
                                    Price = null;
                                    Discount = null;
                                    PriceAfterDiscount = null;
                                    VATAmt = null;
                                    VATPercent = null;
                                    GTotal = null;
                                    DocEntry1 = null;
                                    CardCode = null;
                                    CardName = null;
                                    Address = null;
                                    Route = null;
                                    OrderType = null;
                                    OrderTypeName = null;
                                    PONo = null;
                                    PODate = null;
                                    DeliveryNo = null;
                                    DeliveryDate = null;
                                    DocStatus = null;
                                    DocStatusName = null;
                                    SalesEmp = null;
                                    DeliveryEmp = null;
                                    DocTotal = null;
                                    DocVATTotal = null;
                                    DocGToal = null;
                                    Remark = null;
                                    CreateDate = null;
                                }
                            }

                            if (readyToCardName) {
                                CardName = data;
                                readyToCardName = false;
                                if (!TextUtils.isEmpty(MyId) && !TextUtils.isEmpty(DocEntry) &&
                                        !TextUtils.isEmpty(ItemCode) && !TextUtils.isEmpty(BarCode) &&
                                        !TextUtils.isEmpty(ItemName) && !TextUtils.isEmpty(Quantity) &&
                                        !TextUtils.isEmpty(UoM) && !TextUtils.isEmpty(Price) &&
                                        !TextUtils.isEmpty(Discount) && !TextUtils.isEmpty(PriceAfterDiscount) &&
                                        !TextUtils.isEmpty(VATAmt) && !TextUtils.isEmpty(VATPercent) &&
                                        !TextUtils.isEmpty(GTotal) && !TextUtils.isEmpty(DocEntry1) &&
                                        !TextUtils.isEmpty(CardCode) && !TextUtils.isEmpty(CardName) &&
                                        !TextUtils.isEmpty(Address) && !TextUtils.isEmpty(Route) &&
                                        !TextUtils.isEmpty(OrderType) && !TextUtils.isEmpty(OrderTypeName) &&
                                        !TextUtils.isEmpty(PONo) && !TextUtils.isEmpty(PODate) &&
                                        !TextUtils.isEmpty(DeliveryNo) && !TextUtils.isEmpty(DeliveryDate) &&
                                        !TextUtils.isEmpty(DocStatus) && !TextUtils.isEmpty(DocStatusName) &&
                                        !TextUtils.isEmpty(SalesEmp) && !TextUtils.isEmpty(DeliveryEmp) &&
                                        !TextUtils.isEmpty(DocTotal) && !TextUtils.isEmpty(DocVATTotal) &&
                                        !TextUtils.isEmpty(DocGToal) && !TextUtils.isEmpty(Remark) && !TextUtils.isEmpty(CreateDate)) {

                                    OrderbyDoc orders = new OrderbyDoc();
                                    orders.setMyId(MyId);
                                    orders.setDocEntry(DocEntry);
                                    orders.setItemCode(ItemCode);
                                    orders.setBarCode(BarCode);
                                    orders.setItemName(ItemName);
                                    orders.setQuantity(Quantity);
                                    orders.setUoM(UoM);
                                    orders.setPrice(Price);
                                    orders.setDiscount(Discount);
                                    orders.setPriceAfterDiscount(PriceAfterDiscount);
                                    orders.setVATAmt(VATAmt);
                                    orders.setVATPercent(VATPercent);
                                    orders.setGTotal(GTotal);
                                    orders.setDocEntry1(DocEntry);
                                    orders.setCardCode(CardCode);
                                    orders.setCardName(CardName);
                                    orders.setAddress(Address);
                                    orders.setRoute(OrderTypeName);
                                    orders.setOrderType(PONo);
                                    orders.setOrderTypeName(PODate);
                                    orders.setPONo(DeliveryNo);
                                    orders.setPODate(DeliveryDate);
                                    orders.setDeliveryNo(DocStatus);
                                    orders.setDeliveryDate(DocStatusName);
                                    orders.setDocStatus(SalesEmp);
                                    orders.setDocStatusName(DeliveryEmp);
                                    orders.setSalesEmp(DocTotal);
                                    orders.setDeliveryEmp(DocEntry);
                                    orders.setDocTotal(CardCode);
                                    orders.setDocVATTotal(DocVATTotal);
                                    orders.setDocGToal(DocGToal);
                                    orders.setRemark(Remark);
                                    orders.setCreateDate(CreateDate);
                                    orderbyDocs.add(orders);

                                    MyId = null;
                                    DocEntry = null;
                                    ItemCode = null;
                                    BarCode = null;
                                    ItemName = null;
                                    Quantity = null;
                                    UoM = null;
                                    Price = null;
                                    Discount = null;
                                    PriceAfterDiscount = null;
                                    VATAmt = null;
                                    VATPercent = null;
                                    GTotal = null;
                                    DocEntry1 = null;
                                    CardCode = null;
                                    CardName = null;
                                    Address = null;
                                    Route = null;
                                    OrderType = null;
                                    OrderTypeName = null;
                                    PONo = null;
                                    PODate = null;
                                    DeliveryNo = null;
                                    DeliveryDate = null;
                                    DocStatus = null;
                                    DocStatusName = null;
                                    SalesEmp = null;
                                    DeliveryEmp = null;
                                    DocTotal = null;
                                    DocVATTotal = null;
                                    DocGToal = null;
                                    Remark = null;
                                    CreateDate = null;
                                }
                            }

                            if (readyToAddress) {
                                Address = data;
                                readyToAddress = false;
                                if (!TextUtils.isEmpty(MyId) && !TextUtils.isEmpty(DocEntry) &&
                                        !TextUtils.isEmpty(ItemCode) && !TextUtils.isEmpty(BarCode) &&
                                        !TextUtils.isEmpty(ItemName) && !TextUtils.isEmpty(Quantity) &&
                                        !TextUtils.isEmpty(UoM) && !TextUtils.isEmpty(Price) &&
                                        !TextUtils.isEmpty(Discount) && !TextUtils.isEmpty(PriceAfterDiscount) &&
                                        !TextUtils.isEmpty(VATAmt) && !TextUtils.isEmpty(VATPercent) &&
                                        !TextUtils.isEmpty(GTotal) && !TextUtils.isEmpty(DocEntry1) &&
                                        !TextUtils.isEmpty(CardCode) && !TextUtils.isEmpty(CardName) &&
                                        !TextUtils.isEmpty(Address) && !TextUtils.isEmpty(Route) &&
                                        !TextUtils.isEmpty(OrderType) && !TextUtils.isEmpty(OrderTypeName) &&
                                        !TextUtils.isEmpty(PONo) && !TextUtils.isEmpty(PODate) &&
                                        !TextUtils.isEmpty(DeliveryNo) && !TextUtils.isEmpty(DeliveryDate) &&
                                        !TextUtils.isEmpty(DocStatus) && !TextUtils.isEmpty(DocStatusName) &&
                                        !TextUtils.isEmpty(SalesEmp) && !TextUtils.isEmpty(DeliveryEmp) &&
                                        !TextUtils.isEmpty(DocTotal) && !TextUtils.isEmpty(DocVATTotal) &&
                                        !TextUtils.isEmpty(DocGToal) && !TextUtils.isEmpty(Remark) && !TextUtils.isEmpty(CreateDate)) {

                                    OrderbyDoc orders = new OrderbyDoc();
                                    orders.setMyId(MyId);
                                    orders.setDocEntry(DocEntry);
                                    orders.setItemCode(ItemCode);
                                    orders.setBarCode(BarCode);
                                    orders.setItemName(ItemName);
                                    orders.setQuantity(Quantity);
                                    orders.setUoM(UoM);
                                    orders.setPrice(Price);
                                    orders.setDiscount(Discount);
                                    orders.setPriceAfterDiscount(PriceAfterDiscount);
                                    orders.setVATAmt(VATAmt);
                                    orders.setVATPercent(VATPercent);
                                    orders.setGTotal(GTotal);
                                    orders.setDocEntry1(DocEntry);
                                    orders.setCardCode(CardCode);
                                    orders.setCardName(CardName);
                                    orders.setAddress(Address);
                                    orders.setRoute(OrderTypeName);
                                    orders.setOrderType(PONo);
                                    orders.setOrderTypeName(PODate);
                                    orders.setPONo(DeliveryNo);
                                    orders.setPODate(DeliveryDate);
                                    orders.setDeliveryNo(DocStatus);
                                    orders.setDeliveryDate(DocStatusName);
                                    orders.setDocStatus(SalesEmp);
                                    orders.setDocStatusName(DeliveryEmp);
                                    orders.setSalesEmp(DocTotal);
                                    orders.setDeliveryEmp(DocEntry);
                                    orders.setDocTotal(CardCode);
                                    orders.setDocVATTotal(DocVATTotal);
                                    orders.setDocGToal(DocGToal);
                                    orders.setRemark(Remark);
                                    orders.setCreateDate(CreateDate);
                                    orderbyDocs.add(orders);

                                    MyId = null;
                                    DocEntry = null;
                                    ItemCode = null;
                                    BarCode = null;
                                    ItemName = null;
                                    Quantity = null;
                                    UoM = null;
                                    Price = null;
                                    Discount = null;
                                    PriceAfterDiscount = null;
                                    VATAmt = null;
                                    VATPercent = null;
                                    GTotal = null;
                                    DocEntry1 = null;
                                    CardCode = null;
                                    CardName = null;
                                    Address = null;
                                    Route = null;
                                    OrderType = null;
                                    OrderTypeName = null;
                                    PONo = null;
                                    PODate = null;
                                    DeliveryNo = null;
                                    DeliveryDate = null;
                                    DocStatus = null;
                                    DocStatusName = null;
                                    SalesEmp = null;
                                    DeliveryEmp = null;
                                    DocTotal = null;
                                    DocVATTotal = null;
                                    DocGToal = null;
                                    Remark = null;
                                    CreateDate = null;
                                }
                            }

                            if (readyToRoute) {
                                Route = data;
                                readyToRoute = false;
                                if (!TextUtils.isEmpty(MyId) && !TextUtils.isEmpty(DocEntry) &&
                                        !TextUtils.isEmpty(ItemCode) && !TextUtils.isEmpty(BarCode) &&
                                        !TextUtils.isEmpty(ItemName) && !TextUtils.isEmpty(Quantity) &&
                                        !TextUtils.isEmpty(UoM) && !TextUtils.isEmpty(Price) &&
                                        !TextUtils.isEmpty(Discount) && !TextUtils.isEmpty(PriceAfterDiscount) &&
                                        !TextUtils.isEmpty(VATAmt) && !TextUtils.isEmpty(VATPercent) &&
                                        !TextUtils.isEmpty(GTotal) && !TextUtils.isEmpty(DocEntry1) &&
                                        !TextUtils.isEmpty(CardCode) && !TextUtils.isEmpty(CardName) &&
                                        !TextUtils.isEmpty(Address) && !TextUtils.isEmpty(Route) &&
                                        !TextUtils.isEmpty(OrderType) && !TextUtils.isEmpty(OrderTypeName) &&
                                        !TextUtils.isEmpty(PONo) && !TextUtils.isEmpty(PODate) &&
                                        !TextUtils.isEmpty(DeliveryNo) && !TextUtils.isEmpty(DeliveryDate) &&
                                        !TextUtils.isEmpty(DocStatus) && !TextUtils.isEmpty(DocStatusName) &&
                                        !TextUtils.isEmpty(SalesEmp) && !TextUtils.isEmpty(DeliveryEmp) &&
                                        !TextUtils.isEmpty(DocTotal) && !TextUtils.isEmpty(DocVATTotal) &&
                                        !TextUtils.isEmpty(DocGToal) && !TextUtils.isEmpty(Remark) && !TextUtils.isEmpty(CreateDate)) {

                                    OrderbyDoc orders = new OrderbyDoc();
                                    orders.setMyId(MyId);
                                    orders.setDocEntry(DocEntry);
                                    orders.setItemCode(ItemCode);
                                    orders.setBarCode(BarCode);
                                    orders.setItemName(ItemName);
                                    orders.setQuantity(Quantity);
                                    orders.setUoM(UoM);
                                    orders.setPrice(Price);
                                    orders.setDiscount(Discount);
                                    orders.setPriceAfterDiscount(PriceAfterDiscount);
                                    orders.setVATAmt(VATAmt);
                                    orders.setVATPercent(VATPercent);
                                    orders.setGTotal(GTotal);
                                    orders.setDocEntry1(DocEntry);
                                    orders.setCardCode(CardCode);
                                    orders.setCardName(CardName);
                                    orders.setAddress(Address);
                                    orders.setRoute(OrderTypeName);
                                    orders.setOrderType(PONo);
                                    orders.setOrderTypeName(PODate);
                                    orders.setPONo(DeliveryNo);
                                    orders.setPODate(DeliveryDate);
                                    orders.setDeliveryNo(DocStatus);
                                    orders.setDeliveryDate(DocStatusName);
                                    orders.setDocStatus(SalesEmp);
                                    orders.setDocStatusName(DeliveryEmp);
                                    orders.setSalesEmp(DocTotal);
                                    orders.setDeliveryEmp(DocEntry);
                                    orders.setDocTotal(CardCode);
                                    orders.setDocVATTotal(DocVATTotal);
                                    orders.setDocGToal(DocGToal);
                                    orders.setRemark(Remark);
                                    orders.setCreateDate(CreateDate);
                                    orderbyDocs.add(orders);

                                    MyId = null;
                                    DocEntry = null;
                                    ItemCode = null;
                                    BarCode = null;
                                    ItemName = null;
                                    Quantity = null;
                                    UoM = null;
                                    Price = null;
                                    Discount = null;
                                    PriceAfterDiscount = null;
                                    VATAmt = null;
                                    VATPercent = null;
                                    GTotal = null;
                                    DocEntry1 = null;
                                    CardCode = null;
                                    CardName = null;
                                    Address = null;
                                    Route = null;
                                    OrderType = null;
                                    OrderTypeName = null;
                                    PONo = null;
                                    PODate = null;
                                    DeliveryNo = null;
                                    DeliveryDate = null;
                                    DocStatus = null;
                                    DocStatusName = null;
                                    SalesEmp = null;
                                    DeliveryEmp = null;
                                    DocTotal = null;
                                    DocVATTotal = null;
                                    DocGToal = null;
                                    Remark = null;
                                    CreateDate = null;
                                }
                            }

                            if (readyToOrderType) {
                                OrderType = data;
                                readyToOrderType = false;
                                if (!TextUtils.isEmpty(MyId) && !TextUtils.isEmpty(DocEntry) &&
                                        !TextUtils.isEmpty(ItemCode) && !TextUtils.isEmpty(BarCode) &&
                                        !TextUtils.isEmpty(ItemName) && !TextUtils.isEmpty(Quantity) &&
                                        !TextUtils.isEmpty(UoM) && !TextUtils.isEmpty(Price) &&
                                        !TextUtils.isEmpty(Discount) && !TextUtils.isEmpty(PriceAfterDiscount) &&
                                        !TextUtils.isEmpty(VATAmt) && !TextUtils.isEmpty(VATPercent) &&
                                        !TextUtils.isEmpty(GTotal) && !TextUtils.isEmpty(DocEntry1) &&
                                        !TextUtils.isEmpty(CardCode) && !TextUtils.isEmpty(CardName) &&
                                        !TextUtils.isEmpty(Address) && !TextUtils.isEmpty(Route) &&
                                        !TextUtils.isEmpty(OrderType) && !TextUtils.isEmpty(OrderTypeName) &&
                                        !TextUtils.isEmpty(PONo) && !TextUtils.isEmpty(PODate) &&
                                        !TextUtils.isEmpty(DeliveryNo) && !TextUtils.isEmpty(DeliveryDate) &&
                                        !TextUtils.isEmpty(DocStatus) && !TextUtils.isEmpty(DocStatusName) &&
                                        !TextUtils.isEmpty(SalesEmp) && !TextUtils.isEmpty(DeliveryEmp) &&
                                        !TextUtils.isEmpty(DocTotal) && !TextUtils.isEmpty(DocVATTotal) &&
                                        !TextUtils.isEmpty(DocGToal) && !TextUtils.isEmpty(Remark) && !TextUtils.isEmpty(CreateDate)) {

                                    OrderbyDoc orders = new OrderbyDoc();
                                    orders.setMyId(MyId);
                                    orders.setDocEntry(DocEntry);
                                    orders.setItemCode(ItemCode);
                                    orders.setBarCode(BarCode);
                                    orders.setItemName(ItemName);
                                    orders.setQuantity(Quantity);
                                    orders.setUoM(UoM);
                                    orders.setPrice(Price);
                                    orders.setDiscount(Discount);
                                    orders.setPriceAfterDiscount(PriceAfterDiscount);
                                    orders.setVATAmt(VATAmt);
                                    orders.setVATPercent(VATPercent);
                                    orders.setGTotal(GTotal);
                                    orders.setDocEntry1(DocEntry);
                                    orders.setCardCode(CardCode);
                                    orders.setCardName(CardName);
                                    orders.setAddress(Address);
                                    orders.setRoute(OrderTypeName);
                                    orders.setOrderType(PONo);
                                    orders.setOrderTypeName(PODate);
                                    orders.setPONo(DeliveryNo);
                                    orders.setPODate(DeliveryDate);
                                    orders.setDeliveryNo(DocStatus);
                                    orders.setDeliveryDate(DocStatusName);
                                    orders.setDocStatus(SalesEmp);
                                    orders.setDocStatusName(DeliveryEmp);
                                    orders.setSalesEmp(DocTotal);
                                    orders.setDeliveryEmp(DocEntry);
                                    orders.setDocTotal(CardCode);
                                    orders.setDocVATTotal(DocVATTotal);
                                    orders.setDocGToal(DocGToal);
                                    orders.setRemark(Remark);
                                    orders.setCreateDate(CreateDate);
                                    orderbyDocs.add(orders);

                                    MyId = null;
                                    DocEntry = null;
                                    ItemCode = null;
                                    BarCode = null;
                                    ItemName = null;
                                    Quantity = null;
                                    UoM = null;
                                    Price = null;
                                    Discount = null;
                                    PriceAfterDiscount = null;
                                    VATAmt = null;
                                    VATPercent = null;
                                    GTotal = null;
                                    DocEntry1 = null;
                                    CardCode = null;
                                    CardName = null;
                                    Address = null;
                                    Route = null;
                                    OrderType = null;
                                    OrderTypeName = null;
                                    PONo = null;
                                    PODate = null;
                                    DeliveryNo = null;
                                    DeliveryDate = null;
                                    DocStatus = null;
                                    DocStatusName = null;
                                    SalesEmp = null;
                                    DeliveryEmp = null;
                                    DocTotal = null;
                                    DocVATTotal = null;
                                    DocGToal = null;
                                    Remark = null;
                                    CreateDate = null;
                                }
                            }

                            if (readyToOrderTypeName) {
                                OrderTypeName = data;
                                readyToOrderTypeName = false;
                                if (!TextUtils.isEmpty(MyId) && !TextUtils.isEmpty(DocEntry) &&
                                        !TextUtils.isEmpty(ItemCode) && !TextUtils.isEmpty(BarCode) &&
                                        !TextUtils.isEmpty(ItemName) && !TextUtils.isEmpty(Quantity) &&
                                        !TextUtils.isEmpty(UoM) && !TextUtils.isEmpty(Price) &&
                                        !TextUtils.isEmpty(Discount) && !TextUtils.isEmpty(PriceAfterDiscount) &&
                                        !TextUtils.isEmpty(VATAmt) && !TextUtils.isEmpty(VATPercent) &&
                                        !TextUtils.isEmpty(GTotal) && !TextUtils.isEmpty(DocEntry1) &&
                                        !TextUtils.isEmpty(CardCode) && !TextUtils.isEmpty(CardName) &&
                                        !TextUtils.isEmpty(Address) && !TextUtils.isEmpty(Route) &&
                                        !TextUtils.isEmpty(OrderType) && !TextUtils.isEmpty(OrderTypeName) &&
                                        !TextUtils.isEmpty(PONo) && !TextUtils.isEmpty(PODate) &&
                                        !TextUtils.isEmpty(DeliveryNo) && !TextUtils.isEmpty(DeliveryDate) &&
                                        !TextUtils.isEmpty(DocStatus) && !TextUtils.isEmpty(DocStatusName) &&
                                        !TextUtils.isEmpty(SalesEmp) && !TextUtils.isEmpty(DeliveryEmp) &&
                                        !TextUtils.isEmpty(DocTotal) && !TextUtils.isEmpty(DocVATTotal) &&
                                        !TextUtils.isEmpty(DocGToal) && !TextUtils.isEmpty(Remark) && !TextUtils.isEmpty(CreateDate)) {

                                    OrderbyDoc orders = new OrderbyDoc();
                                    orders.setMyId(MyId);
                                    orders.setDocEntry(DocEntry);
                                    orders.setItemCode(ItemCode);
                                    orders.setBarCode(BarCode);
                                    orders.setItemName(ItemName);
                                    orders.setQuantity(Quantity);
                                    orders.setUoM(UoM);
                                    orders.setPrice(Price);
                                    orders.setDiscount(Discount);
                                    orders.setPriceAfterDiscount(PriceAfterDiscount);
                                    orders.setVATAmt(VATAmt);
                                    orders.setVATPercent(VATPercent);
                                    orders.setGTotal(GTotal);
                                    orders.setDocEntry1(DocEntry);
                                    orders.setCardCode(CardCode);
                                    orders.setCardName(CardName);
                                    orders.setAddress(Address);
                                    orders.setRoute(OrderTypeName);
                                    orders.setOrderType(PONo);
                                    orders.setOrderTypeName(PODate);
                                    orders.setPONo(DeliveryNo);
                                    orders.setPODate(DeliveryDate);
                                    orders.setDeliveryNo(DocStatus);
                                    orders.setDeliveryDate(DocStatusName);
                                    orders.setDocStatus(SalesEmp);
                                    orders.setDocStatusName(DeliveryEmp);
                                    orders.setSalesEmp(DocTotal);
                                    orders.setDeliveryEmp(DocEntry);
                                    orders.setDocTotal(CardCode);
                                    orders.setDocVATTotal(DocVATTotal);
                                    orders.setDocGToal(DocGToal);
                                    orders.setRemark(Remark);
                                    orders.setCreateDate(CreateDate);
                                    orderbyDocs.add(orders);

                                    MyId = null;
                                    DocEntry = null;
                                    ItemCode = null;
                                    BarCode = null;
                                    ItemName = null;
                                    Quantity = null;
                                    UoM = null;
                                    Price = null;
                                    Discount = null;
                                    PriceAfterDiscount = null;
                                    VATAmt = null;
                                    VATPercent = null;
                                    GTotal = null;
                                    DocEntry1 = null;
                                    CardCode = null;
                                    CardName = null;
                                    Address = null;
                                    Route = null;
                                    OrderType = null;
                                    OrderTypeName = null;
                                    PONo = null;
                                    PODate = null;
                                    DeliveryNo = null;
                                    DeliveryDate = null;
                                    DocStatus = null;
                                    DocStatusName = null;
                                    SalesEmp = null;
                                    DeliveryEmp = null;
                                    DocTotal = null;
                                    DocVATTotal = null;
                                    DocGToal = null;
                                    Remark = null;
                                    CreateDate = null;
                                }
                            }

                            if (readyToPONo) {
                                PONo = data;
                                readyToPONo = false;
                                if (!TextUtils.isEmpty(MyId) && !TextUtils.isEmpty(DocEntry) &&
                                        !TextUtils.isEmpty(ItemCode) && !TextUtils.isEmpty(BarCode) &&
                                        !TextUtils.isEmpty(ItemName) && !TextUtils.isEmpty(Quantity) &&
                                        !TextUtils.isEmpty(UoM) && !TextUtils.isEmpty(Price) &&
                                        !TextUtils.isEmpty(Discount) && !TextUtils.isEmpty(PriceAfterDiscount) &&
                                        !TextUtils.isEmpty(VATAmt) && !TextUtils.isEmpty(VATPercent) &&
                                        !TextUtils.isEmpty(GTotal) && !TextUtils.isEmpty(DocEntry1) &&
                                        !TextUtils.isEmpty(CardCode) && !TextUtils.isEmpty(CardName) &&
                                        !TextUtils.isEmpty(Address) && !TextUtils.isEmpty(Route) &&
                                        !TextUtils.isEmpty(OrderType) && !TextUtils.isEmpty(OrderTypeName) &&
                                        !TextUtils.isEmpty(PONo) && !TextUtils.isEmpty(PODate) &&
                                        !TextUtils.isEmpty(DeliveryNo) && !TextUtils.isEmpty(DeliveryDate) &&
                                        !TextUtils.isEmpty(DocStatus) && !TextUtils.isEmpty(DocStatusName) &&
                                        !TextUtils.isEmpty(SalesEmp) && !TextUtils.isEmpty(DeliveryEmp) &&
                                        !TextUtils.isEmpty(DocTotal) && !TextUtils.isEmpty(DocVATTotal) &&
                                        !TextUtils.isEmpty(DocGToal) && !TextUtils.isEmpty(Remark) && !TextUtils.isEmpty(CreateDate)) {

                                    OrderbyDoc orders = new OrderbyDoc();
                                    orders.setMyId(MyId);
                                    orders.setDocEntry(DocEntry);
                                    orders.setItemCode(ItemCode);
                                    orders.setBarCode(BarCode);
                                    orders.setItemName(ItemName);
                                    orders.setQuantity(Quantity);
                                    orders.setUoM(UoM);
                                    orders.setPrice(Price);
                                    orders.setDiscount(Discount);
                                    orders.setPriceAfterDiscount(PriceAfterDiscount);
                                    orders.setVATAmt(VATAmt);
                                    orders.setVATPercent(VATPercent);
                                    orders.setGTotal(GTotal);
                                    orders.setDocEntry1(DocEntry);
                                    orders.setCardCode(CardCode);
                                    orders.setCardName(CardName);
                                    orders.setAddress(Address);
                                    orders.setRoute(OrderTypeName);
                                    orders.setOrderType(PONo);
                                    orders.setOrderTypeName(PODate);
                                    orders.setPONo(DeliveryNo);
                                    orders.setPODate(DeliveryDate);
                                    orders.setDeliveryNo(DocStatus);
                                    orders.setDeliveryDate(DocStatusName);
                                    orders.setDocStatus(SalesEmp);
                                    orders.setDocStatusName(DeliveryEmp);
                                    orders.setSalesEmp(DocTotal);
                                    orders.setDeliveryEmp(DocEntry);
                                    orders.setDocTotal(CardCode);
                                    orders.setDocVATTotal(DocVATTotal);
                                    orders.setDocGToal(DocGToal);
                                    orders.setRemark(Remark);
                                    orders.setCreateDate(CreateDate);
                                    orderbyDocs.add(orders);

                                    MyId = null;
                                    DocEntry = null;
                                    ItemCode = null;
                                    BarCode = null;
                                    ItemName = null;
                                    Quantity = null;
                                    UoM = null;
                                    Price = null;
                                    Discount = null;
                                    PriceAfterDiscount = null;
                                    VATAmt = null;
                                    VATPercent = null;
                                    GTotal = null;
                                    DocEntry1 = null;
                                    CardCode = null;
                                    CardName = null;
                                    Address = null;
                                    Route = null;
                                    OrderType = null;
                                    OrderTypeName = null;
                                    PONo = null;
                                    PODate = null;
                                    DeliveryNo = null;
                                    DeliveryDate = null;
                                    DocStatus = null;
                                    DocStatusName = null;
                                    SalesEmp = null;
                                    DeliveryEmp = null;
                                    DocTotal = null;
                                    DocVATTotal = null;
                                    DocGToal = null;
                                    Remark = null;
                                    CreateDate = null;
                                }
                            }

                            if (readyToPODate) {
                                PODate = data;
                                readyToPODate = false;
                                if (!TextUtils.isEmpty(MyId) && !TextUtils.isEmpty(DocEntry) &&
                                        !TextUtils.isEmpty(ItemCode) && !TextUtils.isEmpty(BarCode) &&
                                        !TextUtils.isEmpty(ItemName) && !TextUtils.isEmpty(Quantity) &&
                                        !TextUtils.isEmpty(UoM) && !TextUtils.isEmpty(Price) &&
                                        !TextUtils.isEmpty(Discount) && !TextUtils.isEmpty(PriceAfterDiscount) &&
                                        !TextUtils.isEmpty(VATAmt) && !TextUtils.isEmpty(VATPercent) &&
                                        !TextUtils.isEmpty(GTotal) && !TextUtils.isEmpty(DocEntry1) &&
                                        !TextUtils.isEmpty(CardCode) && !TextUtils.isEmpty(CardName) &&
                                        !TextUtils.isEmpty(Address) && !TextUtils.isEmpty(Route) &&
                                        !TextUtils.isEmpty(OrderType) && !TextUtils.isEmpty(OrderTypeName) &&
                                        !TextUtils.isEmpty(PONo) && !TextUtils.isEmpty(PODate) &&
                                        !TextUtils.isEmpty(DeliveryNo) && !TextUtils.isEmpty(DeliveryDate) &&
                                        !TextUtils.isEmpty(DocStatus) && !TextUtils.isEmpty(DocStatusName) &&
                                        !TextUtils.isEmpty(SalesEmp) && !TextUtils.isEmpty(DeliveryEmp) &&
                                        !TextUtils.isEmpty(DocTotal) && !TextUtils.isEmpty(DocVATTotal) &&
                                        !TextUtils.isEmpty(DocGToal) && !TextUtils.isEmpty(Remark) && !TextUtils.isEmpty(CreateDate)) {

                                    OrderbyDoc orders = new OrderbyDoc();
                                    orders.setMyId(MyId);
                                    orders.setDocEntry(DocEntry);
                                    orders.setItemCode(ItemCode);
                                    orders.setBarCode(BarCode);
                                    orders.setItemName(ItemName);
                                    orders.setQuantity(Quantity);
                                    orders.setUoM(UoM);
                                    orders.setPrice(Price);
                                    orders.setDiscount(Discount);
                                    orders.setPriceAfterDiscount(PriceAfterDiscount);
                                    orders.setVATAmt(VATAmt);
                                    orders.setVATPercent(VATPercent);
                                    orders.setGTotal(GTotal);
                                    orders.setDocEntry1(DocEntry);
                                    orders.setCardCode(CardCode);
                                    orders.setCardName(CardName);
                                    orders.setAddress(Address);
                                    orders.setRoute(OrderTypeName);
                                    orders.setOrderType(PONo);
                                    orders.setOrderTypeName(PODate);
                                    orders.setPONo(DeliveryNo);
                                    orders.setPODate(DeliveryDate);
                                    orders.setDeliveryNo(DocStatus);
                                    orders.setDeliveryDate(DocStatusName);
                                    orders.setDocStatus(SalesEmp);
                                    orders.setDocStatusName(DeliveryEmp);
                                    orders.setSalesEmp(DocTotal);
                                    orders.setDeliveryEmp(DocEntry);
                                    orders.setDocTotal(CardCode);
                                    orders.setDocVATTotal(DocVATTotal);
                                    orders.setDocGToal(DocGToal);
                                    orders.setRemark(Remark);
                                    orders.setCreateDate(CreateDate);
                                    orderbyDocs.add(orders);

                                    MyId = null;
                                    DocEntry = null;
                                    ItemCode = null;
                                    BarCode = null;
                                    ItemName = null;
                                    Quantity = null;
                                    UoM = null;
                                    Price = null;
                                    Discount = null;
                                    PriceAfterDiscount = null;
                                    VATAmt = null;
                                    VATPercent = null;
                                    GTotal = null;
                                    DocEntry1 = null;
                                    CardCode = null;
                                    CardName = null;
                                    Address = null;
                                    Route = null;
                                    OrderType = null;
                                    OrderTypeName = null;
                                    PONo = null;
                                    PODate = null;
                                    DeliveryNo = null;
                                    DeliveryDate = null;
                                    DocStatus = null;
                                    DocStatusName = null;
                                    SalesEmp = null;
                                    DeliveryEmp = null;
                                    DocTotal = null;
                                    DocVATTotal = null;
                                    DocGToal = null;
                                    Remark = null;
                                    CreateDate = null;
                                }
                            }

                            if (readyToDeliveryNo) {
                                DeliveryNo = data;
                                readyToDeliveryNo = false;
                                if (!TextUtils.isEmpty(MyId) && !TextUtils.isEmpty(DocEntry) &&
                                        !TextUtils.isEmpty(ItemCode) && !TextUtils.isEmpty(BarCode) &&
                                        !TextUtils.isEmpty(ItemName) && !TextUtils.isEmpty(Quantity) &&
                                        !TextUtils.isEmpty(UoM) && !TextUtils.isEmpty(Price) &&
                                        !TextUtils.isEmpty(Discount) && !TextUtils.isEmpty(PriceAfterDiscount) &&
                                        !TextUtils.isEmpty(VATAmt) && !TextUtils.isEmpty(VATPercent) &&
                                        !TextUtils.isEmpty(GTotal) && !TextUtils.isEmpty(DocEntry1) &&
                                        !TextUtils.isEmpty(CardCode) && !TextUtils.isEmpty(CardName) &&
                                        !TextUtils.isEmpty(Address) && !TextUtils.isEmpty(Route) &&
                                        !TextUtils.isEmpty(OrderType) && !TextUtils.isEmpty(OrderTypeName) &&
                                        !TextUtils.isEmpty(PONo) && !TextUtils.isEmpty(PODate) &&
                                        !TextUtils.isEmpty(DeliveryNo) && !TextUtils.isEmpty(DeliveryDate) &&
                                        !TextUtils.isEmpty(DocStatus) && !TextUtils.isEmpty(DocStatusName) &&
                                        !TextUtils.isEmpty(SalesEmp) && !TextUtils.isEmpty(DeliveryEmp) &&
                                        !TextUtils.isEmpty(DocTotal) && !TextUtils.isEmpty(DocVATTotal) &&
                                        !TextUtils.isEmpty(DocGToal) && !TextUtils.isEmpty(Remark) && !TextUtils.isEmpty(CreateDate)) {

                                    OrderbyDoc orders = new OrderbyDoc();
                                    orders.setMyId(MyId);
                                    orders.setDocEntry(DocEntry);
                                    orders.setItemCode(ItemCode);
                                    orders.setBarCode(BarCode);
                                    orders.setItemName(ItemName);
                                    orders.setQuantity(Quantity);
                                    orders.setUoM(UoM);
                                    orders.setPrice(Price);
                                    orders.setDiscount(Discount);
                                    orders.setPriceAfterDiscount(PriceAfterDiscount);
                                    orders.setVATAmt(VATAmt);
                                    orders.setVATPercent(VATPercent);
                                    orders.setGTotal(GTotal);
                                    orders.setDocEntry1(DocEntry);
                                    orders.setCardCode(CardCode);
                                    orders.setCardName(CardName);
                                    orders.setAddress(Address);
                                    orders.setRoute(OrderTypeName);
                                    orders.setOrderType(PONo);
                                    orders.setOrderTypeName(PODate);
                                    orders.setPONo(DeliveryNo);
                                    orders.setPODate(DeliveryDate);
                                    orders.setDeliveryNo(DocStatus);
                                    orders.setDeliveryDate(DocStatusName);
                                    orders.setDocStatus(SalesEmp);
                                    orders.setDocStatusName(DeliveryEmp);
                                    orders.setSalesEmp(DocTotal);
                                    orders.setDeliveryEmp(DocEntry);
                                    orders.setDocTotal(CardCode);
                                    orders.setDocVATTotal(DocVATTotal);
                                    orders.setDocGToal(DocGToal);
                                    orders.setRemark(Remark);
                                    orders.setCreateDate(CreateDate);
                                    orderbyDocs.add(orders);

                                    MyId = null;
                                    DocEntry = null;
                                    ItemCode = null;
                                    BarCode = null;
                                    ItemName = null;
                                    Quantity = null;
                                    UoM = null;
                                    Price = null;
                                    Discount = null;
                                    PriceAfterDiscount = null;
                                    VATAmt = null;
                                    VATPercent = null;
                                    GTotal = null;
                                    DocEntry1 = null;
                                    CardCode = null;
                                    CardName = null;
                                    Address = null;
                                    Route = null;
                                    OrderType = null;
                                    OrderTypeName = null;
                                    PONo = null;
                                    PODate = null;
                                    DeliveryNo = null;
                                    DeliveryDate = null;
                                    DocStatus = null;
                                    DocStatusName = null;
                                    SalesEmp = null;
                                    DeliveryEmp = null;
                                    DocTotal = null;
                                    DocVATTotal = null;
                                    DocGToal = null;
                                    Remark = null;
                                    CreateDate = null;
                                }
                            }

                            if (readyToDeliveryDate) {
                                DeliveryDate = data;
                                readyToDeliveryDate = false;
                                if (!TextUtils.isEmpty(MyId) && !TextUtils.isEmpty(DocEntry) &&
                                        !TextUtils.isEmpty(ItemCode) && !TextUtils.isEmpty(BarCode) &&
                                        !TextUtils.isEmpty(ItemName) && !TextUtils.isEmpty(Quantity) &&
                                        !TextUtils.isEmpty(UoM) && !TextUtils.isEmpty(Price) &&
                                        !TextUtils.isEmpty(Discount) && !TextUtils.isEmpty(PriceAfterDiscount) &&
                                        !TextUtils.isEmpty(VATAmt) && !TextUtils.isEmpty(VATPercent) &&
                                        !TextUtils.isEmpty(GTotal) && !TextUtils.isEmpty(DocEntry1) &&
                                        !TextUtils.isEmpty(CardCode) && !TextUtils.isEmpty(CardName) &&
                                        !TextUtils.isEmpty(Address) && !TextUtils.isEmpty(Route) &&
                                        !TextUtils.isEmpty(OrderType) && !TextUtils.isEmpty(OrderTypeName) &&
                                        !TextUtils.isEmpty(PONo) && !TextUtils.isEmpty(PODate) &&
                                        !TextUtils.isEmpty(DeliveryNo) && !TextUtils.isEmpty(DeliveryDate) &&
                                        !TextUtils.isEmpty(DocStatus) && !TextUtils.isEmpty(DocStatusName) &&
                                        !TextUtils.isEmpty(SalesEmp) && !TextUtils.isEmpty(DeliveryEmp) &&
                                        !TextUtils.isEmpty(DocTotal) && !TextUtils.isEmpty(DocVATTotal) &&
                                        !TextUtils.isEmpty(DocGToal) && !TextUtils.isEmpty(Remark) && !TextUtils.isEmpty(CreateDate)) {

                                    OrderbyDoc orders = new OrderbyDoc();
                                    orders.setMyId(MyId);
                                    orders.setDocEntry(DocEntry);
                                    orders.setItemCode(ItemCode);
                                    orders.setBarCode(BarCode);
                                    orders.setItemName(ItemName);
                                    orders.setQuantity(Quantity);
                                    orders.setUoM(UoM);
                                    orders.setPrice(Price);
                                    orders.setDiscount(Discount);
                                    orders.setPriceAfterDiscount(PriceAfterDiscount);
                                    orders.setVATAmt(VATAmt);
                                    orders.setVATPercent(VATPercent);
                                    orders.setGTotal(GTotal);
                                    orders.setDocEntry1(DocEntry);
                                    orders.setCardCode(CardCode);
                                    orders.setCardName(CardName);
                                    orders.setAddress(Address);
                                    orders.setRoute(OrderTypeName);
                                    orders.setOrderType(PONo);
                                    orders.setOrderTypeName(PODate);
                                    orders.setPONo(DeliveryNo);
                                    orders.setPODate(DeliveryDate);
                                    orders.setDeliveryNo(DocStatus);
                                    orders.setDeliveryDate(DocStatusName);
                                    orders.setDocStatus(SalesEmp);
                                    orders.setDocStatusName(DeliveryEmp);
                                    orders.setSalesEmp(DocTotal);
                                    orders.setDeliveryEmp(DocEntry);
                                    orders.setDocTotal(CardCode);
                                    orders.setDocVATTotal(DocVATTotal);
                                    orders.setDocGToal(DocGToal);
                                    orders.setRemark(Remark);
                                    orders.setCreateDate(CreateDate);
                                    orderbyDocs.add(orders);

                                    MyId = null;
                                    DocEntry = null;
                                    ItemCode = null;
                                    BarCode = null;
                                    ItemName = null;
                                    Quantity = null;
                                    UoM = null;
                                    Price = null;
                                    Discount = null;
                                    PriceAfterDiscount = null;
                                    VATAmt = null;
                                    VATPercent = null;
                                    GTotal = null;
                                    DocEntry1 = null;
                                    CardCode = null;
                                    CardName = null;
                                    Address = null;
                                    Route = null;
                                    OrderType = null;
                                    OrderTypeName = null;
                                    PONo = null;
                                    PODate = null;
                                    DeliveryNo = null;
                                    DeliveryDate = null;
                                    DocStatus = null;
                                    DocStatusName = null;
                                    SalesEmp = null;
                                    DeliveryEmp = null;
                                    DocTotal = null;
                                    DocVATTotal = null;
                                    DocGToal = null;
                                    Remark = null;
                                    CreateDate = null;
                                }
                            }

                            if (readyToDocStatus) {
                                DocStatus = data;
                                readyToDocStatus = false;
                                if (!TextUtils.isEmpty(MyId) && !TextUtils.isEmpty(DocEntry) &&
                                        !TextUtils.isEmpty(ItemCode) && !TextUtils.isEmpty(BarCode) &&
                                        !TextUtils.isEmpty(ItemName) && !TextUtils.isEmpty(Quantity) &&
                                        !TextUtils.isEmpty(UoM) && !TextUtils.isEmpty(Price) &&
                                        !TextUtils.isEmpty(Discount) && !TextUtils.isEmpty(PriceAfterDiscount) &&
                                        !TextUtils.isEmpty(VATAmt) && !TextUtils.isEmpty(VATPercent) &&
                                        !TextUtils.isEmpty(GTotal) && !TextUtils.isEmpty(DocEntry1) &&
                                        !TextUtils.isEmpty(CardCode) && !TextUtils.isEmpty(CardName) &&
                                        !TextUtils.isEmpty(Address) && !TextUtils.isEmpty(Route) &&
                                        !TextUtils.isEmpty(OrderType) && !TextUtils.isEmpty(OrderTypeName) &&
                                        !TextUtils.isEmpty(PONo) && !TextUtils.isEmpty(PODate) &&
                                        !TextUtils.isEmpty(DeliveryNo) && !TextUtils.isEmpty(DeliveryDate) &&
                                        !TextUtils.isEmpty(DocStatus) && !TextUtils.isEmpty(DocStatusName) &&
                                        !TextUtils.isEmpty(SalesEmp) && !TextUtils.isEmpty(DeliveryEmp) &&
                                        !TextUtils.isEmpty(DocTotal) && !TextUtils.isEmpty(DocVATTotal) &&
                                        !TextUtils.isEmpty(DocGToal) && !TextUtils.isEmpty(Remark) && !TextUtils.isEmpty(CreateDate)) {

                                    OrderbyDoc orders = new OrderbyDoc();
                                    orders.setMyId(MyId);
                                    orders.setDocEntry(DocEntry);
                                    orders.setItemCode(ItemCode);
                                    orders.setBarCode(BarCode);
                                    orders.setItemName(ItemName);
                                    orders.setQuantity(Quantity);
                                    orders.setUoM(UoM);
                                    orders.setPrice(Price);
                                    orders.setDiscount(Discount);
                                    orders.setPriceAfterDiscount(PriceAfterDiscount);
                                    orders.setVATAmt(VATAmt);
                                    orders.setVATPercent(VATPercent);
                                    orders.setGTotal(GTotal);
                                    orders.setDocEntry1(DocEntry);
                                    orders.setCardCode(CardCode);
                                    orders.setCardName(CardName);
                                    orders.setAddress(Address);
                                    orders.setRoute(OrderTypeName);
                                    orders.setOrderType(PONo);
                                    orders.setOrderTypeName(PODate);
                                    orders.setPONo(DeliveryNo);
                                    orders.setPODate(DeliveryDate);
                                    orders.setDeliveryNo(DocStatus);
                                    orders.setDeliveryDate(DocStatusName);
                                    orders.setDocStatus(SalesEmp);
                                    orders.setDocStatusName(DeliveryEmp);
                                    orders.setSalesEmp(DocTotal);
                                    orders.setDeliveryEmp(DocEntry);
                                    orders.setDocTotal(CardCode);
                                    orders.setDocVATTotal(DocVATTotal);
                                    orders.setDocGToal(DocGToal);
                                    orders.setRemark(Remark);
                                    orders.setCreateDate(CreateDate);
                                    orderbyDocs.add(orders);

                                    MyId = null;
                                    DocEntry = null;
                                    ItemCode = null;
                                    BarCode = null;
                                    ItemName = null;
                                    Quantity = null;
                                    UoM = null;
                                    Price = null;
                                    Discount = null;
                                    PriceAfterDiscount = null;
                                    VATAmt = null;
                                    VATPercent = null;
                                    GTotal = null;
                                    DocEntry1 = null;
                                    CardCode = null;
                                    CardName = null;
                                    Address = null;
                                    Route = null;
                                    OrderType = null;
                                    OrderTypeName = null;
                                    PONo = null;
                                    PODate = null;
                                    DeliveryNo = null;
                                    DeliveryDate = null;
                                    DocStatus = null;
                                    DocStatusName = null;
                                    SalesEmp = null;
                                    DeliveryEmp = null;
                                    DocTotal = null;
                                    DocVATTotal = null;
                                    DocGToal = null;
                                    Remark = null;
                                    CreateDate = null;
                                }
                            }

                            if (readyToDocStatusName) {
                                DocStatusName = data;
                                readyToDocStatusName = false;
                                if (!TextUtils.isEmpty(MyId) && !TextUtils.isEmpty(DocEntry) &&
                                        !TextUtils.isEmpty(ItemCode) && !TextUtils.isEmpty(BarCode) &&
                                        !TextUtils.isEmpty(ItemName) && !TextUtils.isEmpty(Quantity) &&
                                        !TextUtils.isEmpty(UoM) && !TextUtils.isEmpty(Price) &&
                                        !TextUtils.isEmpty(Discount) && !TextUtils.isEmpty(PriceAfterDiscount) &&
                                        !TextUtils.isEmpty(VATAmt) && !TextUtils.isEmpty(VATPercent) &&
                                        !TextUtils.isEmpty(GTotal) && !TextUtils.isEmpty(DocEntry1) &&
                                        !TextUtils.isEmpty(CardCode) && !TextUtils.isEmpty(CardName) &&
                                        !TextUtils.isEmpty(Address) && !TextUtils.isEmpty(Route) &&
                                        !TextUtils.isEmpty(OrderType) && !TextUtils.isEmpty(OrderTypeName) &&
                                        !TextUtils.isEmpty(PONo) && !TextUtils.isEmpty(PODate) &&
                                        !TextUtils.isEmpty(DeliveryNo) && !TextUtils.isEmpty(DeliveryDate) &&
                                        !TextUtils.isEmpty(DocStatus) && !TextUtils.isEmpty(DocStatusName) &&
                                        !TextUtils.isEmpty(SalesEmp) && !TextUtils.isEmpty(DeliveryEmp) &&
                                        !TextUtils.isEmpty(DocTotal) && !TextUtils.isEmpty(DocVATTotal) &&
                                        !TextUtils.isEmpty(DocGToal) && !TextUtils.isEmpty(Remark) && !TextUtils.isEmpty(CreateDate)) {

                                    OrderbyDoc orders = new OrderbyDoc();
                                    orders.setMyId(MyId);
                                    orders.setDocEntry(DocEntry);
                                    orders.setItemCode(ItemCode);
                                    orders.setBarCode(BarCode);
                                    orders.setItemName(ItemName);
                                    orders.setQuantity(Quantity);
                                    orders.setUoM(UoM);
                                    orders.setPrice(Price);
                                    orders.setDiscount(Discount);
                                    orders.setPriceAfterDiscount(PriceAfterDiscount);
                                    orders.setVATAmt(VATAmt);
                                    orders.setVATPercent(VATPercent);
                                    orders.setGTotal(GTotal);
                                    orders.setDocEntry1(DocEntry);
                                    orders.setCardCode(CardCode);
                                    orders.setCardName(CardName);
                                    orders.setAddress(Address);
                                    orders.setRoute(OrderTypeName);
                                    orders.setOrderType(PONo);
                                    orders.setOrderTypeName(PODate);
                                    orders.setPONo(DeliveryNo);
                                    orders.setPODate(DeliveryDate);
                                    orders.setDeliveryNo(DocStatus);
                                    orders.setDeliveryDate(DocStatusName);
                                    orders.setDocStatus(SalesEmp);
                                    orders.setDocStatusName(DeliveryEmp);
                                    orders.setSalesEmp(DocTotal);
                                    orders.setDeliveryEmp(DocEntry);
                                    orders.setDocTotal(CardCode);
                                    orders.setDocVATTotal(DocVATTotal);
                                    orders.setDocGToal(DocGToal);
                                    orders.setRemark(Remark);
                                    orders.setCreateDate(CreateDate);
                                    orderbyDocs.add(orders);

                                    MyId = null;
                                    DocEntry = null;
                                    ItemCode = null;
                                    BarCode = null;
                                    ItemName = null;
                                    Quantity = null;
                                    UoM = null;
                                    Price = null;
                                    Discount = null;
                                    PriceAfterDiscount = null;
                                    VATAmt = null;
                                    VATPercent = null;
                                    GTotal = null;
                                    DocEntry1 = null;
                                    CardCode = null;
                                    CardName = null;
                                    Address = null;
                                    Route = null;
                                    OrderType = null;
                                    OrderTypeName = null;
                                    PONo = null;
                                    PODate = null;
                                    DeliveryNo = null;
                                    DeliveryDate = null;
                                    DocStatus = null;
                                    DocStatusName = null;
                                    SalesEmp = null;
                                    DeliveryEmp = null;
                                    DocTotal = null;
                                    DocVATTotal = null;
                                    DocGToal = null;
                                    Remark = null;
                                    CreateDate = null;
                                }
                            }

                            if (readyToSalesEmp) {
                                SalesEmp = data;
                                readyToSalesEmp = false;
                                if (!TextUtils.isEmpty(MyId) && !TextUtils.isEmpty(DocEntry) &&
                                        !TextUtils.isEmpty(ItemCode) && !TextUtils.isEmpty(BarCode) &&
                                        !TextUtils.isEmpty(ItemName) && !TextUtils.isEmpty(Quantity) &&
                                        !TextUtils.isEmpty(UoM) && !TextUtils.isEmpty(Price) &&
                                        !TextUtils.isEmpty(Discount) && !TextUtils.isEmpty(PriceAfterDiscount) &&
                                        !TextUtils.isEmpty(VATAmt) && !TextUtils.isEmpty(VATPercent) &&
                                        !TextUtils.isEmpty(GTotal) && !TextUtils.isEmpty(DocEntry1) &&
                                        !TextUtils.isEmpty(CardCode) && !TextUtils.isEmpty(CardName) &&
                                        !TextUtils.isEmpty(Address) && !TextUtils.isEmpty(Route) &&
                                        !TextUtils.isEmpty(OrderType) && !TextUtils.isEmpty(OrderTypeName) &&
                                        !TextUtils.isEmpty(PONo) && !TextUtils.isEmpty(PODate) &&
                                        !TextUtils.isEmpty(DeliveryNo) && !TextUtils.isEmpty(DeliveryDate) &&
                                        !TextUtils.isEmpty(DocStatus) && !TextUtils.isEmpty(DocStatusName) &&
                                        !TextUtils.isEmpty(SalesEmp) && !TextUtils.isEmpty(DeliveryEmp) &&
                                        !TextUtils.isEmpty(DocTotal) && !TextUtils.isEmpty(DocVATTotal) &&
                                        !TextUtils.isEmpty(DocGToal) && !TextUtils.isEmpty(Remark) && !TextUtils.isEmpty(CreateDate)) {

                                    OrderbyDoc orders = new OrderbyDoc();
                                    orders.setMyId(MyId);
                                    orders.setDocEntry(DocEntry);
                                    orders.setItemCode(ItemCode);
                                    orders.setBarCode(BarCode);
                                    orders.setItemName(ItemName);
                                    orders.setQuantity(Quantity);
                                    orders.setUoM(UoM);
                                    orders.setPrice(Price);
                                    orders.setDiscount(Discount);
                                    orders.setPriceAfterDiscount(PriceAfterDiscount);
                                    orders.setVATAmt(VATAmt);
                                    orders.setVATPercent(VATPercent);
                                    orders.setGTotal(GTotal);
                                    orders.setDocEntry1(DocEntry);
                                    orders.setCardCode(CardCode);
                                    orders.setCardName(CardName);
                                    orders.setAddress(Address);
                                    orders.setRoute(OrderTypeName);
                                    orders.setOrderType(PONo);
                                    orders.setOrderTypeName(PODate);
                                    orders.setPONo(DeliveryNo);
                                    orders.setPODate(DeliveryDate);
                                    orders.setDeliveryNo(DocStatus);
                                    orders.setDeliveryDate(DocStatusName);
                                    orders.setDocStatus(SalesEmp);
                                    orders.setDocStatusName(DeliveryEmp);
                                    orders.setSalesEmp(DocTotal);
                                    orders.setDeliveryEmp(DocEntry);
                                    orders.setDocTotal(CardCode);
                                    orders.setDocVATTotal(DocVATTotal);
                                    orders.setDocGToal(DocGToal);
                                    orders.setRemark(Remark);
                                    orders.setCreateDate(CreateDate);
                                    orderbyDocs.add(orders);

                                    MyId = null;
                                    DocEntry = null;
                                    ItemCode = null;
                                    BarCode = null;
                                    ItemName = null;
                                    Quantity = null;
                                    UoM = null;
                                    Price = null;
                                    Discount = null;
                                    PriceAfterDiscount = null;
                                    VATAmt = null;
                                    VATPercent = null;
                                    GTotal = null;
                                    DocEntry1 = null;
                                    CardCode = null;
                                    CardName = null;
                                    Address = null;
                                    Route = null;
                                    OrderType = null;
                                    OrderTypeName = null;
                                    PONo = null;
                                    PODate = null;
                                    DeliveryNo = null;
                                    DeliveryDate = null;
                                    DocStatus = null;
                                    DocStatusName = null;
                                    SalesEmp = null;
                                    DeliveryEmp = null;
                                    DocTotal = null;
                                    DocVATTotal = null;
                                    DocGToal = null;
                                    Remark = null;
                                    CreateDate = null;
                                }
                            }

                            if (readyToDeliveryEmp) {
                                DeliveryEmp = data;
                                readyToDeliveryEmp = false;
                                if (!TextUtils.isEmpty(MyId) && !TextUtils.isEmpty(DocEntry) &&
                                        !TextUtils.isEmpty(ItemCode) && !TextUtils.isEmpty(BarCode) &&
                                        !TextUtils.isEmpty(ItemName) && !TextUtils.isEmpty(Quantity) &&
                                        !TextUtils.isEmpty(UoM) && !TextUtils.isEmpty(Price) &&
                                        !TextUtils.isEmpty(Discount) && !TextUtils.isEmpty(PriceAfterDiscount) &&
                                        !TextUtils.isEmpty(VATAmt) && !TextUtils.isEmpty(VATPercent) &&
                                        !TextUtils.isEmpty(GTotal) && !TextUtils.isEmpty(DocEntry1) &&
                                        !TextUtils.isEmpty(CardCode) && !TextUtils.isEmpty(CardName) &&
                                        !TextUtils.isEmpty(Address) && !TextUtils.isEmpty(Route) &&
                                        !TextUtils.isEmpty(OrderType) && !TextUtils.isEmpty(OrderTypeName) &&
                                        !TextUtils.isEmpty(PONo) && !TextUtils.isEmpty(PODate) &&
                                        !TextUtils.isEmpty(DeliveryNo) && !TextUtils.isEmpty(DeliveryDate) &&
                                        !TextUtils.isEmpty(DocStatus) && !TextUtils.isEmpty(DocStatusName) &&
                                        !TextUtils.isEmpty(SalesEmp) && !TextUtils.isEmpty(DeliveryEmp) &&
                                        !TextUtils.isEmpty(DocTotal) && !TextUtils.isEmpty(DocVATTotal) &&
                                        !TextUtils.isEmpty(DocGToal) && !TextUtils.isEmpty(Remark) && !TextUtils.isEmpty(CreateDate)) {

                                    OrderbyDoc orders = new OrderbyDoc();
                                    orders.setMyId(MyId);
                                    orders.setDocEntry(DocEntry);
                                    orders.setItemCode(ItemCode);
                                    orders.setBarCode(BarCode);
                                    orders.setItemName(ItemName);
                                    orders.setQuantity(Quantity);
                                    orders.setUoM(UoM);
                                    orders.setPrice(Price);
                                    orders.setDiscount(Discount);
                                    orders.setPriceAfterDiscount(PriceAfterDiscount);
                                    orders.setVATAmt(VATAmt);
                                    orders.setVATPercent(VATPercent);
                                    orders.setGTotal(GTotal);
                                    orders.setDocEntry1(DocEntry);
                                    orders.setCardCode(CardCode);
                                    orders.setCardName(CardName);
                                    orders.setAddress(Address);
                                    orders.setRoute(OrderTypeName);
                                    orders.setOrderType(PONo);
                                    orders.setOrderTypeName(PODate);
                                    orders.setPONo(DeliveryNo);
                                    orders.setPODate(DeliveryDate);
                                    orders.setDeliveryNo(DocStatus);
                                    orders.setDeliveryDate(DocStatusName);
                                    orders.setDocStatus(SalesEmp);
                                    orders.setDocStatusName(DeliveryEmp);
                                    orders.setSalesEmp(DocTotal);
                                    orders.setDeliveryEmp(DocEntry);
                                    orders.setDocTotal(CardCode);
                                    orders.setDocVATTotal(DocVATTotal);
                                    orders.setDocGToal(DocGToal);
                                    orders.setRemark(Remark);
                                    orders.setCreateDate(CreateDate);
                                    orderbyDocs.add(orders);

                                    MyId = null;
                                    DocEntry = null;
                                    ItemCode = null;
                                    BarCode = null;
                                    ItemName = null;
                                    Quantity = null;
                                    UoM = null;
                                    Price = null;
                                    Discount = null;
                                    PriceAfterDiscount = null;
                                    VATAmt = null;
                                    VATPercent = null;
                                    GTotal = null;
                                    DocEntry1 = null;
                                    CardCode = null;
                                    CardName = null;
                                    Address = null;
                                    Route = null;
                                    OrderType = null;
                                    OrderTypeName = null;
                                    PONo = null;
                                    PODate = null;
                                    DeliveryNo = null;
                                    DeliveryDate = null;
                                    DocStatus = null;
                                    DocStatusName = null;
                                    SalesEmp = null;
                                    DeliveryEmp = null;
                                    DocTotal = null;
                                    DocVATTotal = null;
                                    DocGToal = null;
                                    Remark = null;
                                    CreateDate = null;
                                }
                            }

                            if (readyToDocTotal) {
                                DocTotal = data;
                                readyToDocTotal = false;
                                if (!TextUtils.isEmpty(MyId) && !TextUtils.isEmpty(DocEntry) &&
                                        !TextUtils.isEmpty(ItemCode) && !TextUtils.isEmpty(BarCode) &&
                                        !TextUtils.isEmpty(ItemName) && !TextUtils.isEmpty(Quantity) &&
                                        !TextUtils.isEmpty(UoM) && !TextUtils.isEmpty(Price) &&
                                        !TextUtils.isEmpty(Discount) && !TextUtils.isEmpty(PriceAfterDiscount) &&
                                        !TextUtils.isEmpty(VATAmt) && !TextUtils.isEmpty(VATPercent) &&
                                        !TextUtils.isEmpty(GTotal) && !TextUtils.isEmpty(DocEntry1) &&
                                        !TextUtils.isEmpty(CardCode) && !TextUtils.isEmpty(CardName) &&
                                        !TextUtils.isEmpty(Address) && !TextUtils.isEmpty(Route) &&
                                        !TextUtils.isEmpty(OrderType) && !TextUtils.isEmpty(OrderTypeName) &&
                                        !TextUtils.isEmpty(PONo) && !TextUtils.isEmpty(PODate) &&
                                        !TextUtils.isEmpty(DeliveryNo) && !TextUtils.isEmpty(DeliveryDate) &&
                                        !TextUtils.isEmpty(DocStatus) && !TextUtils.isEmpty(DocStatusName) &&
                                        !TextUtils.isEmpty(SalesEmp) && !TextUtils.isEmpty(DeliveryEmp) &&
                                        !TextUtils.isEmpty(DocTotal) && !TextUtils.isEmpty(DocVATTotal) &&
                                        !TextUtils.isEmpty(DocGToal) && !TextUtils.isEmpty(Remark) && !TextUtils.isEmpty(CreateDate)) {

                                    OrderbyDoc orders = new OrderbyDoc();
                                    orders.setMyId(MyId);
                                    orders.setDocEntry(DocEntry);
                                    orders.setItemCode(ItemCode);
                                    orders.setBarCode(BarCode);
                                    orders.setItemName(ItemName);
                                    orders.setQuantity(Quantity);
                                    orders.setUoM(UoM);
                                    orders.setPrice(Price);
                                    orders.setDiscount(Discount);
                                    orders.setPriceAfterDiscount(PriceAfterDiscount);
                                    orders.setVATAmt(VATAmt);
                                    orders.setVATPercent(VATPercent);
                                    orders.setGTotal(GTotal);
                                    orders.setDocEntry1(DocEntry);
                                    orders.setCardCode(CardCode);
                                    orders.setCardName(CardName);
                                    orders.setAddress(Address);
                                    orders.setRoute(OrderTypeName);
                                    orders.setOrderType(PONo);
                                    orders.setOrderTypeName(PODate);
                                    orders.setPONo(DeliveryNo);
                                    orders.setPODate(DeliveryDate);
                                    orders.setDeliveryNo(DocStatus);
                                    orders.setDeliveryDate(DocStatusName);
                                    orders.setDocStatus(SalesEmp);
                                    orders.setDocStatusName(DeliveryEmp);
                                    orders.setSalesEmp(DocTotal);
                                    orders.setDeliveryEmp(DocEntry);
                                    orders.setDocTotal(CardCode);
                                    orders.setDocVATTotal(DocVATTotal);
                                    orders.setDocGToal(DocGToal);
                                    orders.setRemark(Remark);
                                    orders.setCreateDate(CreateDate);
                                    orderbyDocs.add(orders);

                                    MyId = null;
                                    DocEntry = null;
                                    ItemCode = null;
                                    BarCode = null;
                                    ItemName = null;
                                    Quantity = null;
                                    UoM = null;
                                    Price = null;
                                    Discount = null;
                                    PriceAfterDiscount = null;
                                    VATAmt = null;
                                    VATPercent = null;
                                    GTotal = null;
                                    DocEntry1 = null;
                                    CardCode = null;
                                    CardName = null;
                                    Address = null;
                                    Route = null;
                                    OrderType = null;
                                    OrderTypeName = null;
                                    PONo = null;
                                    PODate = null;
                                    DeliveryNo = null;
                                    DeliveryDate = null;
                                    DocStatus = null;
                                    DocStatusName = null;
                                    SalesEmp = null;
                                    DeliveryEmp = null;
                                    DocTotal = null;
                                    DocVATTotal = null;
                                    DocGToal = null;
                                    Remark = null;
                                    CreateDate = null;
                                }
                            }

                            if (readyToDocVATTotal) {
                                DocVATTotal = data;
                                readyToDocVATTotal = false;
                                if (!TextUtils.isEmpty(MyId) && !TextUtils.isEmpty(DocEntry) &&
                                        !TextUtils.isEmpty(ItemCode) && !TextUtils.isEmpty(BarCode) &&
                                        !TextUtils.isEmpty(ItemName) && !TextUtils.isEmpty(Quantity) &&
                                        !TextUtils.isEmpty(UoM) && !TextUtils.isEmpty(Price) &&
                                        !TextUtils.isEmpty(Discount) && !TextUtils.isEmpty(PriceAfterDiscount) &&
                                        !TextUtils.isEmpty(VATAmt) && !TextUtils.isEmpty(VATPercent) &&
                                        !TextUtils.isEmpty(GTotal) && !TextUtils.isEmpty(DocEntry1) &&
                                        !TextUtils.isEmpty(CardCode) && !TextUtils.isEmpty(CardName) &&
                                        !TextUtils.isEmpty(Address) && !TextUtils.isEmpty(Route) &&
                                        !TextUtils.isEmpty(OrderType) && !TextUtils.isEmpty(OrderTypeName) &&
                                        !TextUtils.isEmpty(PONo) && !TextUtils.isEmpty(PODate) &&
                                        !TextUtils.isEmpty(DeliveryNo) && !TextUtils.isEmpty(DeliveryDate) &&
                                        !TextUtils.isEmpty(DocStatus) && !TextUtils.isEmpty(DocStatusName) &&
                                        !TextUtils.isEmpty(SalesEmp) && !TextUtils.isEmpty(DeliveryEmp) &&
                                        !TextUtils.isEmpty(DocTotal) && !TextUtils.isEmpty(DocVATTotal) &&
                                        !TextUtils.isEmpty(DocGToal) && !TextUtils.isEmpty(Remark) && !TextUtils.isEmpty(CreateDate)) {

                                    OrderbyDoc orders = new OrderbyDoc();
                                    orders.setMyId(MyId);
                                    orders.setDocEntry(DocEntry);
                                    orders.setItemCode(ItemCode);
                                    orders.setBarCode(BarCode);
                                    orders.setItemName(ItemName);
                                    orders.setQuantity(Quantity);
                                    orders.setUoM(UoM);
                                    orders.setPrice(Price);
                                    orders.setDiscount(Discount);
                                    orders.setPriceAfterDiscount(PriceAfterDiscount);
                                    orders.setVATAmt(VATAmt);
                                    orders.setVATPercent(VATPercent);
                                    orders.setGTotal(GTotal);
                                    orders.setDocEntry1(DocEntry);
                                    orders.setCardCode(CardCode);
                                    orders.setCardName(CardName);
                                    orders.setAddress(Address);
                                    orders.setRoute(OrderTypeName);
                                    orders.setOrderType(PONo);
                                    orders.setOrderTypeName(PODate);
                                    orders.setPONo(DeliveryNo);
                                    orders.setPODate(DeliveryDate);
                                    orders.setDeliveryNo(DocStatus);
                                    orders.setDeliveryDate(DocStatusName);
                                    orders.setDocStatus(SalesEmp);
                                    orders.setDocStatusName(DeliveryEmp);
                                    orders.setSalesEmp(DocTotal);
                                    orders.setDeliveryEmp(DocEntry);
                                    orders.setDocTotal(CardCode);
                                    orders.setDocVATTotal(DocVATTotal);
                                    orders.setDocGToal(DocGToal);
                                    orders.setRemark(Remark);
                                    orders.setCreateDate(CreateDate);
                                    orderbyDocs.add(orders);

                                    MyId = null;
                                    DocEntry = null;
                                    ItemCode = null;
                                    BarCode = null;
                                    ItemName = null;
                                    Quantity = null;
                                    UoM = null;
                                    Price = null;
                                    Discount = null;
                                    PriceAfterDiscount = null;
                                    VATAmt = null;
                                    VATPercent = null;
                                    GTotal = null;
                                    DocEntry1 = null;
                                    CardCode = null;
                                    CardName = null;
                                    Address = null;
                                    Route = null;
                                    OrderType = null;
                                    OrderTypeName = null;
                                    PONo = null;
                                    PODate = null;
                                    DeliveryNo = null;
                                    DeliveryDate = null;
                                    DocStatus = null;
                                    DocStatusName = null;
                                    SalesEmp = null;
                                    DeliveryEmp = null;
                                    DocTotal = null;
                                    DocVATTotal = null;
                                    DocGToal = null;
                                    Remark = null;
                                    CreateDate = null;
                                }
                            }

                            if (readyToDocGToal) {
                                DocGToal = data;
                                readyToDocGToal = false;
                                if (!TextUtils.isEmpty(MyId) && !TextUtils.isEmpty(DocEntry) &&
                                        !TextUtils.isEmpty(ItemCode) && !TextUtils.isEmpty(BarCode) &&
                                        !TextUtils.isEmpty(ItemName) && !TextUtils.isEmpty(Quantity) &&
                                        !TextUtils.isEmpty(UoM) && !TextUtils.isEmpty(Price) &&
                                        !TextUtils.isEmpty(Discount) && !TextUtils.isEmpty(PriceAfterDiscount) &&
                                        !TextUtils.isEmpty(VATAmt) && !TextUtils.isEmpty(VATPercent) &&
                                        !TextUtils.isEmpty(GTotal) && !TextUtils.isEmpty(DocEntry1) &&
                                        !TextUtils.isEmpty(CardCode) && !TextUtils.isEmpty(CardName) &&
                                        !TextUtils.isEmpty(Address) && !TextUtils.isEmpty(Route) &&
                                        !TextUtils.isEmpty(OrderType) && !TextUtils.isEmpty(OrderTypeName) &&
                                        !TextUtils.isEmpty(PONo) && !TextUtils.isEmpty(PODate) &&
                                        !TextUtils.isEmpty(DeliveryNo) && !TextUtils.isEmpty(DeliveryDate) &&
                                        !TextUtils.isEmpty(DocStatus) && !TextUtils.isEmpty(DocStatusName) &&
                                        !TextUtils.isEmpty(SalesEmp) && !TextUtils.isEmpty(DeliveryEmp) &&
                                        !TextUtils.isEmpty(DocTotal) && !TextUtils.isEmpty(DocVATTotal) &&
                                        !TextUtils.isEmpty(DocGToal) && !TextUtils.isEmpty(Remark) && !TextUtils.isEmpty(CreateDate)) {

                                    OrderbyDoc orders = new OrderbyDoc();
                                    orders.setMyId(MyId);
                                    orders.setDocEntry(DocEntry);
                                    orders.setItemCode(ItemCode);
                                    orders.setBarCode(BarCode);
                                    orders.setItemName(ItemName);
                                    orders.setQuantity(Quantity);
                                    orders.setUoM(UoM);
                                    orders.setPrice(Price);
                                    orders.setDiscount(Discount);
                                    orders.setPriceAfterDiscount(PriceAfterDiscount);
                                    orders.setVATAmt(VATAmt);
                                    orders.setVATPercent(VATPercent);
                                    orders.setGTotal(GTotal);
                                    orders.setDocEntry1(DocEntry);
                                    orders.setCardCode(CardCode);
                                    orders.setCardName(CardName);
                                    orders.setAddress(Address);
                                    orders.setRoute(OrderTypeName);
                                    orders.setOrderType(PONo);
                                    orders.setOrderTypeName(PODate);
                                    orders.setPONo(DeliveryNo);
                                    orders.setPODate(DeliveryDate);
                                    orders.setDeliveryNo(DocStatus);
                                    orders.setDeliveryDate(DocStatusName);
                                    orders.setDocStatus(SalesEmp);
                                    orders.setDocStatusName(DeliveryEmp);
                                    orders.setSalesEmp(DocTotal);
                                    orders.setDeliveryEmp(DocEntry);
                                    orders.setDocTotal(CardCode);
                                    orders.setDocVATTotal(DocVATTotal);
                                    orders.setDocGToal(DocGToal);
                                    orders.setRemark(Remark);
                                    orders.setCreateDate(CreateDate);
                                    orderbyDocs.add(orders);

                                    MyId = null;
                                    DocEntry = null;
                                    ItemCode = null;
                                    BarCode = null;
                                    ItemName = null;
                                    Quantity = null;
                                    UoM = null;
                                    Price = null;
                                    Discount = null;
                                    PriceAfterDiscount = null;
                                    VATAmt = null;
                                    VATPercent = null;
                                    GTotal = null;
                                    DocEntry1 = null;
                                    CardCode = null;
                                    CardName = null;
                                    Address = null;
                                    Route = null;
                                    OrderType = null;
                                    OrderTypeName = null;
                                    PONo = null;
                                    PODate = null;
                                    DeliveryNo = null;
                                    DeliveryDate = null;
                                    DocStatus = null;
                                    DocStatusName = null;
                                    SalesEmp = null;
                                    DeliveryEmp = null;
                                    DocTotal = null;
                                    DocVATTotal = null;
                                    DocGToal = null;
                                    Remark = null;
                                    CreateDate = null;
                                }
                            }

                            if (readyToRemark) {
                                Remark = data;
                                readyToRemark = false;
                                if (!TextUtils.isEmpty(MyId) && !TextUtils.isEmpty(DocEntry) &&
                                        !TextUtils.isEmpty(ItemCode) && !TextUtils.isEmpty(BarCode) &&
                                        !TextUtils.isEmpty(ItemName) && !TextUtils.isEmpty(Quantity) &&
                                        !TextUtils.isEmpty(UoM) && !TextUtils.isEmpty(Price) &&
                                        !TextUtils.isEmpty(Discount) && !TextUtils.isEmpty(PriceAfterDiscount) &&
                                        !TextUtils.isEmpty(VATAmt) && !TextUtils.isEmpty(VATPercent) &&
                                        !TextUtils.isEmpty(GTotal) && !TextUtils.isEmpty(DocEntry1) &&
                                        !TextUtils.isEmpty(CardCode) && !TextUtils.isEmpty(CardName) &&
                                        !TextUtils.isEmpty(Address) && !TextUtils.isEmpty(Route) &&
                                        !TextUtils.isEmpty(OrderType) && !TextUtils.isEmpty(OrderTypeName) &&
                                        !TextUtils.isEmpty(PONo) && !TextUtils.isEmpty(PODate) &&
                                        !TextUtils.isEmpty(DeliveryNo) && !TextUtils.isEmpty(DeliveryDate) &&
                                        !TextUtils.isEmpty(DocStatus) && !TextUtils.isEmpty(DocStatusName) &&
                                        !TextUtils.isEmpty(SalesEmp) && !TextUtils.isEmpty(DeliveryEmp) &&
                                        !TextUtils.isEmpty(DocTotal) && !TextUtils.isEmpty(DocVATTotal) &&
                                        !TextUtils.isEmpty(DocGToal) && !TextUtils.isEmpty(Remark) && !TextUtils.isEmpty(CreateDate)) {

                                    OrderbyDoc orders = new OrderbyDoc();
                                    orders.setMyId(MyId);
                                    orders.setDocEntry(DocEntry);
                                    orders.setItemCode(ItemCode);
                                    orders.setBarCode(BarCode);
                                    orders.setItemName(ItemName);
                                    orders.setQuantity(Quantity);
                                    orders.setUoM(UoM);
                                    orders.setPrice(Price);
                                    orders.setDiscount(Discount);
                                    orders.setPriceAfterDiscount(PriceAfterDiscount);
                                    orders.setVATAmt(VATAmt);
                                    orders.setVATPercent(VATPercent);
                                    orders.setGTotal(GTotal);
                                    orders.setDocEntry1(DocEntry);
                                    orders.setCardCode(CardCode);
                                    orders.setCardName(CardName);
                                    orders.setAddress(Address);
                                    orders.setRoute(OrderTypeName);
                                    orders.setOrderType(PONo);
                                    orders.setOrderTypeName(PODate);
                                    orders.setPONo(DeliveryNo);
                                    orders.setPODate(DeliveryDate);
                                    orders.setDeliveryNo(DocStatus);
                                    orders.setDeliveryDate(DocStatusName);
                                    orders.setDocStatus(SalesEmp);
                                    orders.setDocStatusName(DeliveryEmp);
                                    orders.setSalesEmp(DocTotal);
                                    orders.setDeliveryEmp(DocEntry);
                                    orders.setDocTotal(CardCode);
                                    orders.setDocVATTotal(DocVATTotal);
                                    orders.setDocGToal(DocGToal);
                                    orders.setRemark(Remark);
                                    orders.setCreateDate(CreateDate);
                                    orderbyDocs.add(orders);

                                    MyId = null;
                                    DocEntry = null;
                                    ItemCode = null;
                                    BarCode = null;
                                    ItemName = null;
                                    Quantity = null;
                                    UoM = null;
                                    Price = null;
                                    Discount = null;
                                    PriceAfterDiscount = null;
                                    VATAmt = null;
                                    VATPercent = null;
                                    GTotal = null;
                                    DocEntry1 = null;
                                    CardCode = null;
                                    CardName = null;
                                    Address = null;
                                    Route = null;
                                    OrderType = null;
                                    OrderTypeName = null;
                                    PONo = null;
                                    PODate = null;
                                    DeliveryNo = null;
                                    DeliveryDate = null;
                                    DocStatus = null;
                                    DocStatusName = null;
                                    SalesEmp = null;
                                    DeliveryEmp = null;
                                    DocTotal = null;
                                    DocVATTotal = null;
                                    DocGToal = null;
                                    Remark = null;
                                    CreateDate = null;
                                }
                            }

                            if (readyToCreateDate) {
                                CreateDate = data;
                                readyToCreateDate = false;
                                if (!TextUtils.isEmpty(MyId) && !TextUtils.isEmpty(DocEntry) &&
                                        !TextUtils.isEmpty(ItemCode) && !TextUtils.isEmpty(BarCode) &&
                                        !TextUtils.isEmpty(ItemName) && !TextUtils.isEmpty(Quantity) &&
                                        !TextUtils.isEmpty(UoM) && !TextUtils.isEmpty(Price) &&
                                        !TextUtils.isEmpty(Discount) && !TextUtils.isEmpty(PriceAfterDiscount) &&
                                        !TextUtils.isEmpty(VATAmt) && !TextUtils.isEmpty(VATPercent) &&
                                        !TextUtils.isEmpty(GTotal) && !TextUtils.isEmpty(DocEntry1) &&
                                        !TextUtils.isEmpty(CardCode) && !TextUtils.isEmpty(CardName) &&
                                        !TextUtils.isEmpty(Address) && !TextUtils.isEmpty(Route) &&
                                        !TextUtils.isEmpty(OrderType) && !TextUtils.isEmpty(OrderTypeName) &&
                                        !TextUtils.isEmpty(PONo) && !TextUtils.isEmpty(PODate) &&
                                        !TextUtils.isEmpty(DeliveryNo) && !TextUtils.isEmpty(DeliveryDate) &&
                                        !TextUtils.isEmpty(DocStatus) && !TextUtils.isEmpty(DocStatusName) &&
                                        !TextUtils.isEmpty(SalesEmp) && !TextUtils.isEmpty(DeliveryEmp) &&
                                        !TextUtils.isEmpty(DocTotal) && !TextUtils.isEmpty(DocVATTotal) &&
                                        !TextUtils.isEmpty(DocGToal) && !TextUtils.isEmpty(Remark) && !TextUtils.isEmpty(CreateDate)) {

                                    OrderbyDoc orders = new OrderbyDoc();
                                    orders.setMyId(MyId);
                                    orders.setDocEntry(DocEntry);
                                    orders.setItemCode(ItemCode);
                                    orders.setBarCode(BarCode);
                                    orders.setItemName(ItemName);
                                    orders.setQuantity(Quantity);
                                    orders.setUoM(UoM);
                                    orders.setPrice(Price);
                                    orders.setDiscount(Discount);
                                    orders.setPriceAfterDiscount(PriceAfterDiscount);
                                    orders.setVATAmt(VATAmt);
                                    orders.setVATPercent(VATPercent);
                                    orders.setGTotal(GTotal);
                                    orders.setDocEntry1(DocEntry);
                                    orders.setCardCode(CardCode);
                                    orders.setCardName(CardName);
                                    orders.setAddress(Address);
                                    orders.setRoute(OrderTypeName);
                                    orders.setOrderType(PONo);
                                    orders.setOrderTypeName(PODate);
                                    orders.setPONo(DeliveryNo);
                                    orders.setPODate(DeliveryDate);
                                    orders.setDeliveryNo(DocStatus);
                                    orders.setDeliveryDate(DocStatusName);
                                    orders.setDocStatus(SalesEmp);
                                    orders.setDocStatusName(DeliveryEmp);
                                    orders.setSalesEmp(DocTotal);
                                    orders.setDeliveryEmp(DocEntry);
                                    orders.setDocTotal(CardCode);
                                    orders.setDocVATTotal(DocVATTotal);
                                    orders.setDocGToal(DocGToal);
                                    orders.setRemark(Remark);
                                    orders.setCreateDate(CreateDate);
                                    orderbyDocs.add(orders);

                                    MyId = null;
                                    DocEntry = null;
                                    ItemCode = null;
                                    BarCode = null;
                                    ItemName = null;
                                    Quantity = null;
                                    UoM = null;
                                    Price = null;
                                    Discount = null;
                                    PriceAfterDiscount = null;
                                    VATAmt = null;
                                    VATPercent = null;
                                    GTotal = null;
                                    DocEntry1 = null;
                                    CardCode = null;
                                    CardName = null;
                                    Address = null;
                                    Route = null;
                                    OrderType = null;
                                    OrderTypeName = null;
                                    PONo = null;
                                    PODate = null;
                                    DeliveryNo = null;
                                    DeliveryDate = null;
                                    DocStatus = null;
                                    DocStatusName = null;
                                    SalesEmp = null;
                                    DeliveryEmp = null;
                                    DocTotal = null;
                                    DocVATTotal = null;
                                    DocGToal = null;
                                    Remark = null;
                                    CreateDate = null;
                                }
                            }
                        }
                        eventType = xpp.next();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    callback.onFail(e.getMessage());
                }
                callback.onSuccess(orderbyDocs);
            }

            @Override
            public void onFail(String error) {
                callback.onFail(error);
            }
        });
    }

    public void loadNoteTypes(String userCode, final GetListNoteType callback) {
        String link = String.format(getURL() + Config.LISTNOTESTYPE);

        List<Param> params = new ArrayList<>();
        Param param = new Param("UserCode", userCode);
        params.add(param);
        mNetwork.executePost(link, params, new Network.Callback() {
            @Override
            public void onCallBack(String response) {
                XmlPullParserFactory factory = null;
                List<NoteType> notesTypeList = new ArrayList<>();
                try {
                    boolean readyToGetCode = false;
                    boolean readyToGetName = false;

                    String currentCode = null;
                    String currentName = null;

                    factory = XmlPullParserFactory.newInstance();
                    factory.setNamespaceAware(true);
                    XmlPullParser xpp = factory.newPullParser();

                    xpp.setInput(new StringReader(response));
                    int eventType = xpp.getEventType();
                    while (eventType != XmlPullParser.END_DOCUMENT) {
                        if (eventType == XmlPullParser.START_DOCUMENT) {
                            //TODO: do nothing
                        } else if (eventType == XmlPullParser.START_TAG) {
                            if (xpp.getName().equalsIgnoreCase("NotesGroup")) {
                                readyToGetCode = true;
                            }

                            if (xpp.getName().equalsIgnoreCase("NotesGroupName")) {
                                readyToGetName = true;
                            }

                        } else if (eventType == XmlPullParser.TEXT) {
                            String data = xpp.getText();

                            if (readyToGetCode == true) {
                                currentCode = data;
                                readyToGetCode = false;
                                if (!TextUtils.isEmpty(currentCode) && !TextUtils.isEmpty(currentName)) {
                                    NoteType noteType = new NoteType();
                                    noteType.setNotesGroup(currentCode);
                                    noteType.setNotesGroupName(currentName);
                                    notesTypeList.add(noteType);

                                    currentCode = null;
                                    currentName = null;
                                }
                            }

                            if (readyToGetName == true) {
                                currentName = data;
                                readyToGetName = false;
                                if (!TextUtils.isEmpty(currentCode) && !TextUtils.isEmpty(currentName)) {
                                    NoteType noteType = new NoteType();
                                    noteType.setNotesGroup(currentCode);
                                    noteType.setNotesGroupName(currentName);
                                    notesTypeList.add(noteType);

                                    currentCode = null;
                                    currentName = null;
                                }
                            }
                        }
                        eventType = xpp.next();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    callback.onFail(e.getMessage());
                }
                callback.onSuccess(notesTypeList);
            }

            @Override
            public void onFail(String error) {
                callback.onFail(error);
            }
        });
    }

    public void loadProductsFromCustomer(String customerCode, String userCode, final GetListNormalProduct callback) {
        String link = String.format(getURL() + Config.GETLISTPRODUCTFROMCUST);

        List<Param> params = new ArrayList<>();
        Param param = new Param("UserCode", userCode);
        params.add(param);
        param = new Param("CustCode", customerCode);
        params.add(param);
        mNetwork.executePost(link, params, new Network.Callback() {
            @Override
            public void onCallBack(String response) {
                XmlPullParserFactory factory = null;
                List<NormalProduct> productList = new ArrayList<>();
                try {
                    boolean readyToGetBars = false;
                    boolean readyToGetItem = false;
                    boolean readyToGetFrgn = false;
                    boolean readyToGetCode = false;
                    boolean readyToGetBuy = false;
                    boolean readyToGetInv = false;

                    String currentBars = null;
                    String currentItem = null;
                    String currentFrgn = null;
                    String currentCode = null;
                    String currentBuy = null;
                    String currentInv = null;

                    factory = XmlPullParserFactory.newInstance();
                    factory.setNamespaceAware(true);
                    XmlPullParser xpp = factory.newPullParser();

                    xpp.setInput(new StringReader(response));
                    int eventType = xpp.getEventType();
                    while (eventType != XmlPullParser.END_DOCUMENT) {
                        if (eventType == XmlPullParser.START_DOCUMENT) {
                            //TODO: do nothing
                        } else if (eventType == XmlPullParser.START_TAG) {
                            if (xpp.getName().equalsIgnoreCase("CodeBars")) {
                                readyToGetBars = true;
                            }

                            if (xpp.getName().equalsIgnoreCase("ItemName")) {
                                readyToGetItem = true;
                            }

                            if (xpp.getName().equalsIgnoreCase("FrgnName")) {
                                readyToGetFrgn = true;
                            }

                            if (xpp.getName().equalsIgnoreCase("ItemCode")) {
                                readyToGetCode = true;
                            }

                            if (xpp.getName().equalsIgnoreCase("BuyUoM")) {
                                readyToGetBuy = true;
                            }

                            if (xpp.getName().equalsIgnoreCase("InvUom")) {
                                readyToGetInv = true;
                            }

                        } else if (eventType == XmlPullParser.TEXT) {
                            String data = xpp.getText();

                            if (readyToGetBars == true) {
                                currentBars = data;
                                readyToGetBars = false;
                                if (!TextUtils.isEmpty(currentBars) && !TextUtils.isEmpty(currentItem) &&
                                        !TextUtils.isEmpty(currentFrgn) && !TextUtils.isEmpty(currentCode) &&
                                        !TextUtils.isEmpty(currentBuy) && !TextUtils.isEmpty(currentInv)) {
                                    NormalProduct product = new NormalProduct();
                                    product.setCodeBars(currentBars);
                                    product.setItemName(currentItem);
                                    product.setFrgnName(currentFrgn);
                                    product.setItemCode(currentCode);
                                    product.setBuyUoM(currentBuy);
                                    product.setInvUom(currentInv);
                                    productList.add(product);

                                    currentBars = null;
                                    currentItem = null;
                                    currentFrgn = null;
                                    currentCode = null;
                                    currentBuy = null;
                                    currentInv = null;
                                }
                            }

                            if (readyToGetItem == true) {
                                currentItem = data;
                                readyToGetItem = false;
                                if (!TextUtils.isEmpty(currentBars) && !TextUtils.isEmpty(currentItem) &&
                                        !TextUtils.isEmpty(currentFrgn) && !TextUtils.isEmpty(currentCode) &&
                                        !TextUtils.isEmpty(currentBuy) && !TextUtils.isEmpty(currentInv)) {
                                    NormalProduct product = new NormalProduct();
                                    product.setCodeBars(currentBars);
                                    product.setItemName(currentItem);
                                    product.setFrgnName(currentFrgn);
                                    product.setItemCode(currentCode);
                                    product.setBuyUoM(currentBuy);
                                    product.setInvUom(currentInv);
                                    productList.add(product);

                                    currentBars = null;
                                    currentItem = null;
                                    currentFrgn = null;
                                    currentCode = null;
                                    currentBuy = null;
                                    currentInv = null;
                                }
                            }

                            if (readyToGetFrgn == true) {
                                currentFrgn = data;
                                readyToGetFrgn = false;
                                if (!TextUtils.isEmpty(currentBars) && !TextUtils.isEmpty(currentItem) &&
                                        !TextUtils.isEmpty(currentFrgn) && !TextUtils.isEmpty(currentCode) &&
                                        !TextUtils.isEmpty(currentBuy) && !TextUtils.isEmpty(currentInv)) {
                                    NormalProduct product = new NormalProduct();
                                    product.setCodeBars(currentBars);
                                    product.setItemName(currentItem);
                                    product.setFrgnName(currentFrgn);
                                    product.setItemCode(currentCode);
                                    product.setBuyUoM(currentBuy);
                                    product.setInvUom(currentInv);
                                    productList.add(product);

                                    currentBars = null;
                                    currentItem = null;
                                    currentFrgn = null;
                                    currentCode = null;
                                    currentBuy = null;
                                    currentInv = null;
                                }
                            }

                            if (readyToGetCode == true) {
                                currentCode = data;
                                readyToGetCode = false;
                                if (!TextUtils.isEmpty(currentBars) && !TextUtils.isEmpty(currentItem) &&
                                        !TextUtils.isEmpty(currentFrgn) && !TextUtils.isEmpty(currentCode) &&
                                        !TextUtils.isEmpty(currentBuy) && !TextUtils.isEmpty(currentInv)) {
                                    NormalProduct product = new NormalProduct();
                                    product.setCodeBars(currentBars);
                                    product.setItemName(currentItem);
                                    product.setFrgnName(currentFrgn);
                                    product.setItemCode(currentCode);
                                    product.setBuyUoM(currentBuy);
                                    product.setInvUom(currentInv);
                                    productList.add(product);

                                    currentBars = null;
                                    currentItem = null;
                                    currentFrgn = null;
                                    currentCode = null;
                                    currentBuy = null;
                                    currentInv = null;
                                }
                            }

                            if (readyToGetBuy == true) {
                                currentBuy = data;
                                readyToGetBuy = false;
                                if (!TextUtils.isEmpty(currentBars) && !TextUtils.isEmpty(currentItem) &&
                                        !TextUtils.isEmpty(currentFrgn) && !TextUtils.isEmpty(currentCode) &&
                                        !TextUtils.isEmpty(currentBuy) && !TextUtils.isEmpty(currentInv)) {
                                    NormalProduct product = new NormalProduct();
                                    product.setCodeBars(currentBars);
                                    product.setItemName(currentItem);
                                    product.setFrgnName(currentFrgn);
                                    product.setItemCode(currentCode);
                                    product.setBuyUoM(currentBuy);
                                    product.setInvUom(currentInv);
                                    productList.add(product);

                                    currentBars = null;
                                    currentItem = null;
                                    currentFrgn = null;
                                    currentCode = null;
                                    currentBuy = null;
                                    currentInv = null;
                                }
                            }

                            if (readyToGetInv == true) {
                                currentInv = data;
                                readyToGetInv = false;
                                if (!TextUtils.isEmpty(currentBars) && !TextUtils.isEmpty(currentItem) &&
                                        !TextUtils.isEmpty(currentFrgn) && !TextUtils.isEmpty(currentCode) &&
                                        !TextUtils.isEmpty(currentBuy) && !TextUtils.isEmpty(currentInv)) {
                                    NormalProduct product = new NormalProduct();
                                    product.setCodeBars(currentBars);
                                    product.setItemName(currentItem);
                                    product.setFrgnName(currentFrgn);
                                    product.setItemCode(currentCode);
                                    product.setBuyUoM(currentBuy);
                                    product.setInvUom(currentInv);
                                    productList.add(product);

                                    currentBars = null;
                                    currentItem = null;
                                    currentFrgn = null;
                                    currentCode = null;
                                    currentBuy = null;
                                    currentInv = null;
                                }
                            }
                        }
                        eventType = xpp.next();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    callback.onFail(e.getMessage());
                }
                callback.onSuccess(productList);
            }

            @Override
            public void onFail(String error) {
                callback.onFail(error);
            }
        });
    }

    public void checkin(String customerCode, String userCode, String deviceID, final CheckinCallback callback) {
        String link = String.format(getURL() + Config.CHECKIN);

        List<Param> params = new ArrayList<>();
        Param param = new Param("CustCode", customerCode);
        params.add(param);
        param = new Param("UserCode", userCode);
        params.add(param);
        param = new Param("DeviceID", deviceID);
        params.add(param);

        mNetwork.executePost(link, params, new Network.Callback() {
            @Override
            public void onCallBack(String response) {
                XmlPullParserFactory factory = null;
                try {
                    boolean readyToGetResult = false;

                    factory = XmlPullParserFactory.newInstance();
                    factory.setNamespaceAware(true);
                    XmlPullParser xpp = factory.newPullParser();

                    xpp.setInput(new StringReader(response));
                    Log.d("ResponeKQ",""+new StringReader(response));

                    int eventType = xpp.getEventType();
                    while (eventType != XmlPullParser.END_DOCUMENT) {
                        if (eventType == XmlPullParser.START_DOCUMENT) {
                            //TODO: do nothing
                        } else if (eventType == XmlPullParser.START_TAG) {
                            if (xpp.getName().equalsIgnoreCase("ResultID")) {
                                readyToGetResult = true;
                            }
                        } else if (eventType == XmlPullParser.TEXT) {
                            if (readyToGetResult) {
                                String data = xpp.getText();
                                callback.onSuccess(data);
                                return;
                            }
                        }
                        eventType = xpp.next();
                    }

                    callback.onFail("");

                } catch (Exception e) {
                    e.printStackTrace();
                    callback.onFail(e.getMessage());
                }
            }

            @Override
            public void onFail(String error) {
                callback.onFail(error);
            }
        });
    }

    public void checkout(String customerCode, String userCode, String checkInID, final CommonCallback callback) {
        String link = String.format(getURL() + Config.CHECKOUT);

        List<Param> params = new ArrayList<>();
        Param param = new Param("CustCode", customerCode);
        params.add(param);
        param = new Param("UserCode", userCode);
        params.add(param);
        param = new Param("CheckInID", checkInID);
        params.add(param);

        mNetwork.executePost(link, params, new Network.Callback() {
            @Override
            public void onCallBack(String response) {
                XmlPullParserFactory factory = null;
                try {
                    boolean readyToGetResult = false;

                    factory = XmlPullParserFactory.newInstance();
                    factory.setNamespaceAware(true);
                    XmlPullParser xpp = factory.newPullParser();

                    xpp.setInput(new StringReader(response));
                    int eventType = xpp.getEventType();
                    while (eventType != XmlPullParser.END_DOCUMENT) {
                        if (eventType == XmlPullParser.START_DOCUMENT) {
                            //TODO: do nothing
                        } else if (eventType == XmlPullParser.START_TAG) {
                            if (xpp.getName().equalsIgnoreCase("ResultID")) {
                                readyToGetResult = true;
                            }
                        } else if (eventType == XmlPullParser.TEXT) {
                            if (readyToGetResult) {
                                callback.onSuccess();
//                                String data = xpp.getText();
//                                if (data.trim().equalsIgnoreCase("1")) {
//                                    callback.onSuccess();
//                                }
//                                else {
//                                    callback.onFail("");
//                                }
                                return;
                            }
                        }
                        eventType = xpp.next();
                    }

                    callback.onFail("");

                } catch (Exception e) {
                    e.printStackTrace();
                    callback.onFail(e.getMessage());
                }
            }

            @Override
            public void onFail(String error) {
                callback.onFail(error);
            }
        });
    }
    public void CheckInAddPicture(String customerCode, String userCode, String checkInID,String Pic1, String Pic2, String Pic3,final CommonCallback callback)
    {
        String link=String.format(getURL()+Config.CHECKINPIC);
        Log.d("CheckInAddPicture","CheckInAddPicture");
        List<Param> params = new ArrayList<>();
        Param param = new Param("CustCode", customerCode); Log.d("logkq",""+customerCode);
        params.add(param);
        param = new Param("UserCode", userCode);Log.d("logkq",""+userCode);
        params.add(param);
        param = new Param("CheckInID", checkInID); Log.d("logkq",""+checkInID);
        params.add(param);
        param = new Param("Pics1", Pic1); Log.d("logkq",""+Pic1);
        params.add(param);
        param = new Param("Pics2", Pic2); Log.d("logkq",""+Pic2);
        params.add(param);
        param = new Param("Pics3", Pic3); Log.d("logkq",""+Pic3);
        params.add(param);

        mNetwork.executePost(link, params, new Network.Callback() {
            @Override
            public void onCallBack(String response) {
                XmlPullParserFactory factory = null;
                try {
                    boolean readyToGetResult = false;

                    factory = XmlPullParserFactory.newInstance();
                    factory.setNamespaceAware(true);
                    XmlPullParser xpp = factory.newPullParser();

                    xpp.setInput(new StringReader(response));
                    int eventType = xpp.getEventType();
                    while (eventType != XmlPullParser.END_DOCUMENT) {
                        if (eventType == XmlPullParser.START_DOCUMENT) {
                            //TODO: do nothing
                        } else if (eventType == XmlPullParser.START_TAG) {
                            if (xpp.getName().equalsIgnoreCase("ResultID")) {
                                readyToGetResult = true;
                                Log.d("giatripic",""+xpp.getName().equalsIgnoreCase("ResultID"));
                            }
                        } else if (eventType == XmlPullParser.TEXT) {
                            if (readyToGetResult) {
                                callback.onSuccess();
                                return;
                            }
                        }
                        eventType = xpp.next();
                    }

                    callback.onFail("");

                } catch (Exception e) {
                    e.printStackTrace();
                    callback.onFail(e.getMessage());
                }
            }

            @Override
            public void onFail(String error) {
                callback.onFail(error);
            }
        });
    }
    public void checkinAddLog(String customerCode, String userCode, String checkInID, String notesGroup, String remark, final CommonCallback callback) {

        String link = String.format(getURL() + Config.CHECKINNOTE);
        Log.d("checkinAddLog","checkinAddLog0");
        List<Param> params = new ArrayList<>();
        Param param = new Param("CustCode", customerCode);
        params.add(param);
        param = new Param("UserCode", userCode);
        params.add(param);
        param = new Param("CheckInID", checkInID);
        params.add(param);
        param = new Param("NotesGroup", notesGroup);
        params.add(param);
        param = new Param("NotesRemark", remark);
        params.add(param);

        mNetwork.executePost(link, params, new Network.Callback() {
            @Override
            public void onCallBack(String response) {
                XmlPullParserFactory factory = null;
                try {
                    boolean readyToGetResult = false;

                    factory = XmlPullParserFactory.newInstance();
                    factory.setNamespaceAware(true);
                    XmlPullParser xpp = factory.newPullParser();

                    xpp.setInput(new StringReader(response));
                    int eventType = xpp.getEventType();
                    while (eventType != XmlPullParser.END_DOCUMENT) {
                        if (eventType == XmlPullParser.START_DOCUMENT) {
                            //TODO: do nothing
                        } else if (eventType == XmlPullParser.START_TAG) {
                            if (xpp.getName().equalsIgnoreCase("ResultID")) {
                                readyToGetResult = true;
                                Log.d("giatritrave",""+xpp.getName().equalsIgnoreCase("ResultID"));
                            }
                        } else if (eventType == XmlPullParser.TEXT) {
                            if (readyToGetResult) {
                                callback.onSuccess();
//                                String data = xpp.getText();
//                                if (data.trim().equalsIgnoreCase("1")) {
//                                    callback.onSuccess();
//                                }
//                                else {
//                                    callback.onFail("");
//                                }
                                return;
                            }
                        }
                        eventType = xpp.next();
                    }

                    callback.onFail("");

                } catch (Exception e) {
                    e.printStackTrace();
                    callback.onFail(e.getMessage());
                }
            }

            @Override
            public void onFail(String error) {
                callback.onFail(error);
            }
        });
    }

    public void checkinInstock(String customerCode, String userCode, String checkInID, String itemCode, String barCode, String caseInStock, String bottleInStock, String remark, final CommonCallback callback) {
        String link = String.format(getURL() + Config.CHECKINSTOCK);

        List<Param> params = new ArrayList<>();
        Param param = new Param("CheckInID", checkInID);
        params.add(param);
        param = new Param("CustCode", customerCode);
        params.add(param);
        param = new Param("UserCode", userCode);
        params.add(param);
        param = new Param("ItemCode", itemCode);
        params.add(param);
        param = new Param("BarCode", barCode);
        params.add(param);
        param = new Param("CaseInStock", caseInStock);
        params.add(param);
        param = new Param("BottleInStock", bottleInStock);
        params.add(param);
        param = new Param("Remarks", remark);
        params.add(param);

        mNetwork.executePost(link, params, new Network.Callback() {
            @Override
            public void onCallBack(String response) {
                XmlPullParserFactory factory = null;
                try {
                    boolean readyToGetResult = false;

                    factory = XmlPullParserFactory.newInstance();
                    factory.setNamespaceAware(true);
                    XmlPullParser xpp = factory.newPullParser();

                    xpp.setInput(new StringReader(response));
                    int eventType = xpp.getEventType();
                    while (eventType != XmlPullParser.END_DOCUMENT) {
                        if (eventType == XmlPullParser.START_DOCUMENT) {
                            //TODO: do nothing
                        } else if (eventType == XmlPullParser.START_TAG) {
                            if (xpp.getName().equalsIgnoreCase("ResultID")) {
                                readyToGetResult = true;
                            }
                        } else if (eventType == XmlPullParser.TEXT) {
                            if (readyToGetResult) {
                                callback.onSuccess();
//                                String data = xpp.getText();
//                                if (data.trim().equalsIgnoreCase("1")) {
//                                    callback.onSuccess();
//                                }
//                                else {
//                                    callback.onFail("");
//                                }
                                return;
                            }
                        }
                        eventType = xpp.next();
                    }

                    callback.onFail("");

                } catch (Exception e) {
                    e.printStackTrace();
                    callback.onFail(e.getMessage());
                }
            }

            @Override
            public void onFail(String error) {
                callback.onFail(error);
            }
        });
    }

    public void getNoteFromUser(String customerCode, String userCode, final GetNoteCallback callback) {
        String link = String.format(getURL() + Config.GETNOTE);

        List<Param> params = new ArrayList<>();
        Param param = new Param("CardCode", customerCode);
        params.add(param);
        param = new Param("UserCode", userCode);
        params.add(param);

        mNetwork.executePost(link, params, new Network.Callback() {
            @Override
            public void onCallBack(String response) {
                XmlPullParserFactory factory = null;
                try {
                    boolean readyToGetResult = false;

                    factory = XmlPullParserFactory.newInstance();
                    factory.setNamespaceAware(true);
                    XmlPullParser xpp = factory.newPullParser();

                    xpp.setInput(new StringReader(response));
                    int eventType = xpp.getEventType();
                    while (eventType != XmlPullParser.END_DOCUMENT) {
                        if (eventType == XmlPullParser.START_DOCUMENT) {
                            //TODO: do nothing
                        } else if (eventType == XmlPullParser.START_TAG) {
                            if (xpp.getName().equalsIgnoreCase("NoteString")) {
                                readyToGetResult = true;
                            }
                        } else if (eventType == XmlPullParser.TEXT) {
                            if (readyToGetResult) {
                                String data = xpp.getText();
                                callback.onSuccess(data);
                                return;
                            }
                        }
                        eventType = xpp.next();
                    }

                    callback.onFail("");

                } catch (Exception e) {
                    e.printStackTrace();
                    callback.onFail(e.getMessage());
                }
            }

            @Override
            public void onFail(String error) {
                callback.onFail(error);
            }
        });
    }

    public void AddNewCustomer(String cardName, String contactPerson, String tel, String lat, String lon, String group, String remark, String address, final AddCustCallback callback) {
        String link = String.format(getURL() + Config.ADDCUST);

        List<Param> params = new ArrayList<>();
        Param param = new Param("CardName", cardName);
        params.add(param);
        param = new Param("ContactPerson", contactPerson);
        params.add(param);
        param = new Param("Tel", tel);
        params.add(param);
        param = new Param("LatitudeValue", lat);
        params.add(param);
        param = new Param("LongitudeValue", lon);
        params.add(param);
        param = new Param("CardGroup", group);
        params.add(param);
        param = new Param("Reamarks", remark);
        params.add(param);
        param = new Param("Address", address);
        params.add(param);

        mNetwork.executePost(link, params, new Network.Callback() {
            @Override
            public void onCallBack(String response) {
                XmlPullParserFactory factory = null;
                try {
                    boolean readyToGetResult = false;

                    factory = XmlPullParserFactory.newInstance();
                    factory.setNamespaceAware(true);
                    XmlPullParser xpp = factory.newPullParser();

                    xpp.setInput(new StringReader(response));
                    int eventType = xpp.getEventType();
                    while (eventType != XmlPullParser.END_DOCUMENT) {
                        if (eventType == XmlPullParser.START_DOCUMENT) {
                            //TODO: do nothing
                        } else if (eventType == XmlPullParser.START_TAG) {
                            if (xpp.getName().equalsIgnoreCase("ResultString")) {
                                readyToGetResult = true;
                            }
                        } else if (eventType == XmlPullParser.TEXT) {
                            if (readyToGetResult) {
                                String data = xpp.getText();
                                callback.onSuccess(data);
                                return;
                            }
                        }
                        eventType = xpp.next();
                    }

                    callback.onFail("");

                } catch (Exception e) {
                    e.printStackTrace();
                    callback.onFail(e.getMessage());
                }
            }

            @Override
            public void onFail(String error) {
                callback.onFail(error);
            }
        });
    }

    public void GetPromotionProducts(String custCode, String userCode, String data, final GetPromotionProductsCallback callback) {
        String link = String.format(getURL() + Config.GETPROMOTIONPRODUCTS);

        List<Param> params = new ArrayList<>();
        Param param = new Param("CustCode", custCode);
        params.add(param);
        param = new Param("UserCode", userCode);
        params.add(param);
        param = new Param("CurrDate", data);
        params.add(param);
        mNetwork.executePost(link, params, new Network.Callback() {
            @Override
            public void onCallBack(String response) {
                XmlPullParserFactory factory = null;
                List<PromotionProduct> returnValue = new ArrayList<>();
                try {
                    boolean readyToGetCodeBars = false;
                    boolean readyToGetItemName = false;
                    boolean readyToGetItemCode = false;

                    String currentCodeBars = null;
                    String currentItemName = null;
                    String currentItemCode = null;

                    factory = XmlPullParserFactory.newInstance();
                    factory.setNamespaceAware(true);
                    XmlPullParser xpp = factory.newPullParser();

                    xpp.setInput(new StringReader(response));
                    int eventType = xpp.getEventType();
                    while (eventType != XmlPullParser.END_DOCUMENT) {
                        if (eventType == XmlPullParser.START_DOCUMENT) {
                            //TODO: do nothing
                        } else if (eventType == XmlPullParser.START_TAG) {
                            if (xpp.getName().equalsIgnoreCase("CodeBars")) {
                                readyToGetCodeBars = true;
                            }

                            if (xpp.getName().equalsIgnoreCase("ItemName")) {
                                readyToGetItemName = true;
                            }

                            if (xpp.getName().equalsIgnoreCase("ItemCode")) {
                                readyToGetItemCode = true;
                            }

                        } else if (eventType == XmlPullParser.TEXT) {
                            String data = xpp.getText();

                            if (readyToGetCodeBars) {
                                currentCodeBars = data;
                                readyToGetCodeBars = false;
                                if (!TextUtils.isEmpty(currentCodeBars) && !TextUtils.isEmpty(currentItemName) &&
                                        !TextUtils.isEmpty(currentItemCode)) {

                                    PromotionProduct promotionProduct = new PromotionProduct();
                                    promotionProduct.setCodeBars(currentCodeBars);
                                    promotionProduct.setItemName(currentItemName);
                                    promotionProduct.setItemCode(currentItemCode);

                                    returnValue.add(promotionProduct);

                                    currentCodeBars = null;
                                    currentItemName = null;
                                    currentItemCode = null;
                                }
                            }

                            if (readyToGetItemName) {
                                currentItemName = data;
                                readyToGetItemName = false;
                                if (!TextUtils.isEmpty(currentCodeBars) && !TextUtils.isEmpty(currentItemName) &&
                                        !TextUtils.isEmpty(currentItemCode)) {

                                    PromotionProduct promotionProduct = new PromotionProduct();
                                    promotionProduct.setCodeBars(currentCodeBars);
                                    promotionProduct.setItemName(currentItemName);
                                    promotionProduct.setItemCode(currentItemCode);

                                    returnValue.add(promotionProduct);

                                    currentCodeBars = null;
                                    currentItemName = null;
                                    currentItemCode = null;
                                }
                            }

                            if (readyToGetItemCode) {
                                currentItemCode = data;
                                readyToGetItemCode = false;
                                if (!TextUtils.isEmpty(currentCodeBars) && !TextUtils.isEmpty(currentItemName) &&
                                        !TextUtils.isEmpty(currentItemCode)) {

                                    PromotionProduct promotionProduct = new PromotionProduct();
                                    promotionProduct.setCodeBars(currentCodeBars);
                                    promotionProduct.setItemName(currentItemName);
                                    promotionProduct.setItemCode(currentItemCode);

                                    returnValue.add(promotionProduct);

                                    currentCodeBars = null;
                                    currentItemName = null;
                                    currentItemCode = null;
                                }
                            }
                        }
                        eventType = xpp.next();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    callback.onFail(e.getMessage());
                }
                callback.onSuccess(returnValue);
            }

            @Override
            public void onFail(String error) {
                callback.onFail(error);
            }
        });
    }

    public void GetPriceOfProduct(String custCode, String itemCode, String uom, String date, final GetPriceCallback callback) {
        String link = String.format(getURL() + Config.GETPRICE);

        List<Param> params = new ArrayList<>();
        Param param = new Param("CustCode", custCode);
        params.add(param);
        param = new Param("ItemCode", itemCode);
        params.add(param);
        param = new Param("UoM", uom);
        params.add(param);
        param = new Param("CurrDate", date);
        params.add(param);

        mNetwork.executePost(link, params, new Network.Callback() {
            @Override
            public void onCallBack(String response) {
                XmlPullParserFactory factory = null;
                try {
                    boolean readyToGetResult = false;
                    boolean readyToGetVAT = false;

                    String currentResult = null;
                    String currentVAT = null;

                    factory = XmlPullParserFactory.newInstance();
                    factory.setNamespaceAware(true);
                    XmlPullParser xpp = factory.newPullParser();

                    xpp.setInput(new StringReader(response));
                    int eventType = xpp.getEventType();
                    while (eventType != XmlPullParser.END_DOCUMENT) {
                        if (eventType == XmlPullParser.START_DOCUMENT) {
                            //TODO: do nothing
                        } else if (eventType == XmlPullParser.START_TAG) {
                            if (xpp.getName().equalsIgnoreCase("ItemPrice")) {
                                readyToGetResult = true;
                            }

                            if (xpp.getName().equalsIgnoreCase("VAT")) {
                                readyToGetVAT = true;
                            }
                        } else if (eventType == XmlPullParser.TEXT) {
                            if (readyToGetResult) {
                                readyToGetResult = false;

                                String data = xpp.getText();
                                currentResult = data;

                                if (currentVAT != null) {
                                    callback.onSuccess(currentResult, currentVAT);
                                    return;
                                }
                            }

                            if (readyToGetVAT) {
                                readyToGetVAT = false;

                                String data = xpp.getText();
                                currentVAT = data;

                                if (currentResult != null) {
                                    callback.onSuccess(currentResult, currentVAT);
                                    return;
                                }
                            }
                        }
                        eventType = xpp.next();
                    }

                    callback.onFail("");

                } catch (Exception e) {
                    e.printStackTrace();
                    callback.onFail(e.getMessage());
                }
            }

            @Override
            public void onFail(String error) {
                callback.onFail(error);
            }
        });
    }

    public void addOrder(String cardCode, String userCode, String orderDate, String deliveryDate, String cardName, String itemCode, String itemName, String itemType, String quantity, String uom, String price, String vat, final CommonCallback callback) {
        String link = String.format(getURL() + Config.ADDORDER);

        List<Param> params = new ArrayList<>();
        Param param = new Param("Cardcode", cardCode);
        params.add(param);
        param = new Param("UserCode", userCode);
        params.add(param);
        param = new Param("OrderDate", orderDate);
        params.add(param);
        param = new Param("DeliveryDate", deliveryDate);
        params.add(param);
        param = new Param("CardName", cardName);
        params.add(param);
        param = new Param("ItemCode", itemCode);
        params.add(param);
        param = new Param("ItemName", itemName);
        params.add(param);
        param = new Param("ItemType", itemType);
        params.add(param);
        param = new Param("Quantity", quantity);
        params.add(param);
        param = new Param("Uom", uom);
        params.add(param);
        param = new Param("Price", price);
        params.add(param);
        param = new Param("VAT", vat);
        params.add(param);

        mNetwork.executePost(link, params, new Network.Callback() {
            @Override
            public void onCallBack(String response) {
                XmlPullParserFactory factory = null;
                try {
                    boolean readyToGetResult = false;

                    factory = XmlPullParserFactory.newInstance();
                    factory.setNamespaceAware(true);
                    XmlPullParser xpp = factory.newPullParser();

                    xpp.setInput(new StringReader(response));
                    int eventType = xpp.getEventType();
                    while (eventType != XmlPullParser.END_DOCUMENT) {
                        if (eventType == XmlPullParser.START_DOCUMENT) {
                            //TODO: do nothing
                        } else if (eventType == XmlPullParser.START_TAG) {
                            if (xpp.getName().equalsIgnoreCase("ResultID")) {
                                readyToGetResult = true;
                            }
                        } else if (eventType == XmlPullParser.TEXT) {
                            if (readyToGetResult) {
                                callback.onSuccess();
//                                String data = xpp.getText();
//                                if (data.trim().equalsIgnoreCase("1")) {
//                                    callback.onSuccess();
//                                }
//                                else {
//                                    callback.onFail("");
//                                }
                                return;
                            }
                        }
                        eventType = xpp.next();
                    }

                    callback.onFail("");

                } catch (Exception e) {
                    e.printStackTrace();
                    callback.onFail(e.getMessage());
                }
            }

            @Override
            public void onFail(String error) {
                callback.onFail(error);
            }
        });
    }

    public void getStatistics(String userCode, String custCode, final GetStatisticsCallback callback) {
        String link = String.format(getURL() + Config.STATISTICS);

        List<Param> params = new ArrayList<>();
        Param param = new Param("CustCode", custCode);
        params.add(param);
        param = new Param("UserCode", userCode);
        params.add(param);

        mNetwork.executePost(link, params, new Network.Callback() {
            @Override
            public void onCallBack(String response) {
                XmlPullParserFactory factory = null;
                List<Statistic> returnValues = new ArrayList<>();
                try {
                    boolean readyToGetCustCode = false;
                    boolean readyToGetCheckInTime = false;
                    boolean readyToGetCheckOutTime = false;
                    boolean readyToGetNoOrder = false;
                    boolean readyToGetNoPic = false;

                    String currentCustCode = null;
                    String currentCheckInTime = null;
                    String currentCheckOutTime = null;
                    String currentNoOrder = null;
                    String currentNoPic = null;

                    factory = XmlPullParserFactory.newInstance();
                    factory.setNamespaceAware(true);
                    XmlPullParser xpp = factory.newPullParser();

                    xpp.setInput(new StringReader(response));
                    int eventType = xpp.getEventType();
                    while (eventType != XmlPullParser.END_DOCUMENT) {
                        if (eventType == XmlPullParser.START_DOCUMENT) {
                            //TODO: do nothing
                        } else if (eventType == XmlPullParser.START_TAG) {
                            if (xpp.getName().equalsIgnoreCase("CustCode")) {
                                readyToGetCustCode = true;
                            }

                            if (xpp.getName().equalsIgnoreCase("CheckInTime")) {
                                readyToGetCheckInTime = true;
                            }

                            if (xpp.getName().equalsIgnoreCase("CheckOutTime")) {
                                readyToGetCheckOutTime = true;
                            }

                            if (xpp.getName().equalsIgnoreCase("NoCustOrder")) {
                                readyToGetNoOrder = true;
                            }

                            if (xpp.getName().equalsIgnoreCase("NoPicTake")) {
                                readyToGetNoPic = true;
                            }

                        } else if (eventType == XmlPullParser.TEXT) {
                            String data = xpp.getText();

                            if (readyToGetCustCode) {
                                currentCustCode = data;
                                readyToGetCustCode = false;
                                if (!TextUtils.isEmpty(currentCustCode) && !TextUtils.isEmpty(currentCheckInTime) &&
                                        !TextUtils.isEmpty(currentNoOrder) && !TextUtils.isEmpty(currentNoPic)) {
                                    Statistic statistic = new Statistic();
                                    statistic.setCustCode(currentCustCode);
                                    statistic.setCheckInTime(currentCheckInTime);
                                    statistic.setCheckOutTime(currentCheckOutTime);
                                    statistic.setNoCustOrder(currentNoOrder);
                                    statistic.setNoPicTake(currentNoPic);

                                    returnValues.add(statistic);

                                    currentCustCode = null;
                                    currentCheckInTime = null;
                                    currentCheckOutTime = null;
                                    currentNoOrder = null;
                                    currentNoPic = null;
                                }
                            }

                            if (readyToGetCheckInTime) {
                                currentCheckInTime = data;
                                readyToGetCheckInTime = false;
                                if (!TextUtils.isEmpty(currentCustCode) && !TextUtils.isEmpty(currentCheckInTime) &&
                                        !TextUtils.isEmpty(currentNoOrder) && !TextUtils.isEmpty(currentNoPic)) {
                                    Statistic statistic = new Statistic();
                                    statistic.setCustCode(currentCustCode);
                                    statistic.setCheckInTime(currentCheckInTime);
                                    statistic.setCheckOutTime(currentCheckOutTime);
                                    statistic.setNoCustOrder(currentNoOrder);
                                    statistic.setNoPicTake(currentNoPic);

                                    returnValues.add(statistic);

                                    currentCustCode = null;
                                    currentCheckInTime = null;
                                    currentCheckOutTime = null;
                                    currentNoOrder = null;
                                    currentNoPic = null;
                                }
                            }

                            if (readyToGetCheckInTime) {
                                currentCheckOutTime = data;
                                readyToGetCheckInTime = false;
                                if (!TextUtils.isEmpty(currentCustCode) && !TextUtils.isEmpty(currentCheckInTime) &&
                                        !TextUtils.isEmpty(currentNoOrder) && !TextUtils.isEmpty(currentNoPic)) {
                                    Statistic statistic = new Statistic();
                                    statistic.setCustCode(currentCustCode);
                                    statistic.setCheckInTime(currentCheckInTime);
                                    statistic.setCheckOutTime(currentCheckOutTime);
                                    statistic.setNoCustOrder(currentNoOrder);
                                    statistic.setNoPicTake(currentNoPic);

                                    returnValues.add(statistic);

                                    currentCustCode = null;
                                    currentCheckInTime = null;
                                    currentCheckOutTime = null;
                                    currentNoOrder = null;
                                    currentNoPic = null;
                                }
                            }
                            if (readyToGetCheckOutTime) {
                                currentCheckOutTime = data;
                                readyToGetCheckOutTime = false;
                                if (!TextUtils.isEmpty(currentCustCode) && !TextUtils.isEmpty(currentCheckInTime) &&
                                        !TextUtils.isEmpty(currentNoOrder) && !TextUtils.isEmpty(currentNoPic)) {
                                    Statistic statistic = new Statistic();
                                    statistic.setCustCode(currentCustCode);
                                    statistic.setCheckInTime(currentCheckInTime);
                                    statistic.setCheckOutTime(currentCheckOutTime);
                                    statistic.setNoCustOrder(currentNoOrder);
                                    statistic.setNoPicTake(currentNoPic);

                                    returnValues.add(statistic);

                                    currentCustCode = null;
                                    currentCheckInTime = null;
                                    currentCheckOutTime = null;
                                    currentNoOrder = null;
                                    currentNoPic = null;
                                }
                            }

                            if (readyToGetNoOrder) {
                                currentNoOrder = data;
                                readyToGetNoOrder = false;
                                if (!TextUtils.isEmpty(currentCustCode) && !TextUtils.isEmpty(currentCheckInTime) &&
                                        !TextUtils.isEmpty(currentNoOrder) && !TextUtils.isEmpty(currentNoPic)) {
                                    Statistic statistic = new Statistic();
                                    statistic.setCustCode(currentCustCode);
                                    statistic.setCheckInTime(currentCheckInTime);
                                    statistic.setCheckOutTime(currentCheckOutTime);
                                    statistic.setNoCustOrder(currentNoOrder);
                                    statistic.setNoPicTake(currentNoPic);

                                    returnValues.add(statistic);

                                    currentCustCode = null;
                                    currentCheckInTime = null;
                                    currentCheckOutTime = null;
                                    currentNoOrder = null;
                                    currentNoPic = null;
                                }
                            }

                            if (readyToGetNoPic) {
                                currentNoPic = data;
                                readyToGetNoPic = false;
                                if (!TextUtils.isEmpty(currentCustCode) && !TextUtils.isEmpty(currentCheckInTime) &&
                                        !TextUtils.isEmpty(currentNoOrder) && !TextUtils.isEmpty(currentNoPic)) {
                                    Statistic statistic = new Statistic();
                                    statistic.setCustCode(currentCustCode);
                                    statistic.setCheckInTime(currentCheckInTime);
                                    statistic.setCheckOutTime(currentCheckOutTime);
                                    statistic.setNoCustOrder(currentNoOrder);
                                    statistic.setNoPicTake(currentNoPic);

                                    returnValues.add(statistic);

                                    currentCustCode = null;
                                    currentCheckInTime = null;
                                    currentCheckOutTime = null;
                                    currentNoOrder = null;
                                    currentNoPic = null;
                                }
                            }
                        }
                        eventType = xpp.next();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    callback.onFail(e.getMessage());
                }
                callback.onSuccess(returnValues);
            }

            @Override
            public void onFail(String error) {
                callback.onFail(error);
            }
        });
    }

    public void getInventoryProduct(String barCode, final GetInventoryProductCallback callback) {
        String link = String.format(getURL() + Config.INVENTORYPRODUCT);

        List<Param> params = new ArrayList<>();
        Param param = new Param("BarCode", barCode);
        params.add(param);

        mNetwork.executePost(link, params, new Network.Callback() {
            @Override
            public void onCallBack(String response) {
                XmlPullParserFactory factory = null;
                try {
                    boolean readyToGetBarcode = false;
                    boolean readyToGetItemCode = false;
                    boolean readyToGetItemName = false;
                    boolean readyToGetUoM = false;
                    boolean readyToGetInvUom = false;
                    boolean readyToGetExpDate = false;

                    String currentBarcode = null;
                    String currentItemCode = null;
                    String currentItemName = null;
                    String currentUoM = null;
                    String currentInvUom = null;
                    String currentExpDate = null;

                    factory = XmlPullParserFactory.newInstance();
                    factory.setNamespaceAware(true);
                    XmlPullParser xpp = factory.newPullParser();

                    xpp.setInput(new StringReader(response));
                    int eventType = xpp.getEventType();
                    while (eventType != XmlPullParser.END_DOCUMENT) {
                        if (eventType == XmlPullParser.START_DOCUMENT) {
                            //TODO: do nothing
                        } else if (eventType == XmlPullParser.START_TAG) {
                            if (xpp.getName().equalsIgnoreCase("Barcode")) {
                                readyToGetBarcode = true;
                            }

                            if (xpp.getName().equalsIgnoreCase("ItemCode")) {
                                readyToGetItemCode = true;
                            }

                            if (xpp.getName().equalsIgnoreCase("ItemName")) {
                                readyToGetItemName = true;
                            }

                            if (xpp.getName().equalsIgnoreCase("UoM")) {
                                readyToGetUoM = true;
                            }

                            if (xpp.getName().equalsIgnoreCase("InvUom")) {
                                readyToGetInvUom = true;
                            }

                            if (xpp.getName().equalsIgnoreCase("ExpDate")) {
                                readyToGetExpDate = true;
                            }

                        } else if (eventType == XmlPullParser.TEXT) {
                            String data = xpp.getText();

                            if (readyToGetBarcode == true) {
                                currentBarcode = data;
                                readyToGetBarcode = false;
                                if (!TextUtils.isEmpty(currentBarcode) && !TextUtils.isEmpty(currentItemCode) && !TextUtils.isEmpty(currentItemName) && !TextUtils.isEmpty(currentInvUom) && !TextUtils.isEmpty(currentUoM) && !TextUtils.isEmpty(currentExpDate)) {
                                    InventoryProduct inventoryProduct = new InventoryProduct();
                                    inventoryProduct.setBarcode(currentBarcode);
                                    inventoryProduct.setItemCode(currentItemCode);
                                    inventoryProduct.setItemName(currentItemName);
                                    inventoryProduct.setUom(currentUoM);
                                    inventoryProduct.setInvUom(currentInvUom);
                                    inventoryProduct.setExpDate(currentExpDate);

                                    callback.onSuccess(inventoryProduct);
                                    return;

//                                    currentBarcode = null;
//                                    currentItemCode = null;
//                                    currentItemName = null;
//                                    currentUoM = null;
//                                    currentInvUom = null;
//                                    currentExpDate = null;
                                }
                            }

                            if (readyToGetItemCode == true) {
                                currentItemCode = data;
                                readyToGetItemCode = false;
                                if (!TextUtils.isEmpty(currentBarcode) && !TextUtils.isEmpty(currentItemCode) && !TextUtils.isEmpty(currentItemName) && !TextUtils.isEmpty(currentInvUom) && !TextUtils.isEmpty(currentUoM) && !TextUtils.isEmpty(currentExpDate)) {
                                    InventoryProduct inventoryProduct = new InventoryProduct();
                                    inventoryProduct.setBarcode(currentBarcode);
                                    inventoryProduct.setItemCode(currentItemCode);
                                    inventoryProduct.setItemName(currentItemName);
                                    inventoryProduct.setUom(currentUoM);
                                    inventoryProduct.setInvUom(currentInvUom);
                                    inventoryProduct.setExpDate(currentExpDate);

                                    callback.onSuccess(inventoryProduct);
                                    return;

//                                    currentBarcode = null;
//                                    currentItemCode = null;
//                                    currentItemName = null;
//                                    currentUoM = null;
//                                    currentInvUom = null;
//                                    currentExpDate = null;
                                }
                            }

                            if (readyToGetItemName == true) {
                                currentItemName = data;
                                readyToGetItemName = false;
                                if (!TextUtils.isEmpty(currentBarcode) && !TextUtils.isEmpty(currentItemCode) && !TextUtils.isEmpty(currentItemName) && !TextUtils.isEmpty(currentInvUom) && !TextUtils.isEmpty(currentUoM) && !TextUtils.isEmpty(currentExpDate)) {
                                    InventoryProduct inventoryProduct = new InventoryProduct();
                                    inventoryProduct.setBarcode(currentBarcode);
                                    inventoryProduct.setItemCode(currentItemCode);
                                    inventoryProduct.setItemName(currentItemName);
                                    inventoryProduct.setUom(currentUoM);
                                    inventoryProduct.setInvUom(currentInvUom);
                                    inventoryProduct.setExpDate(currentExpDate);

                                    callback.onSuccess(inventoryProduct);
                                    return;

//                                    currentBarcode = null;
//                                    currentItemCode = null;
//                                    currentItemName = null;
//                                    currentUoM = null;
//                                    currentInvUom = null;
//                                    currentExpDate = null;
                                }
                            }
                            if (readyToGetUoM == true) {
                                currentUoM = data;
                                readyToGetUoM = false;
                                if (!TextUtils.isEmpty(currentBarcode) && !TextUtils.isEmpty(currentItemCode) && !TextUtils.isEmpty(currentItemName) && !TextUtils.isEmpty(currentInvUom) && !TextUtils.isEmpty(currentUoM) && !TextUtils.isEmpty(currentExpDate)) {
                                    InventoryProduct inventoryProduct = new InventoryProduct();
                                    inventoryProduct.setBarcode(currentBarcode);
                                    inventoryProduct.setItemCode(currentItemCode);
                                    inventoryProduct.setItemName(currentItemName);
                                    inventoryProduct.setUom(currentUoM);
                                    inventoryProduct.setInvUom(currentInvUom);
                                    inventoryProduct.setExpDate(currentExpDate);

                                    callback.onSuccess(inventoryProduct);
                                    return;

//                                    currentBarcode = null;
//                                    currentItemCode = null;
//                                    currentItemName = null;
//                                    currentUoM = null;
//                                    currentInvUom = null;
//                                    currentExpDate = null;
                                }
                            }

                            if (readyToGetInvUom == true) {
                                currentInvUom = data;
                                readyToGetInvUom = false;
                                if (!TextUtils.isEmpty(currentBarcode) && !TextUtils.isEmpty(currentItemCode) && !TextUtils.isEmpty(currentItemName) && !TextUtils.isEmpty(currentInvUom) && !TextUtils.isEmpty(currentUoM) && !TextUtils.isEmpty(currentExpDate)) {
                                    InventoryProduct inventoryProduct = new InventoryProduct();
                                    inventoryProduct.setBarcode(currentBarcode);
                                    inventoryProduct.setItemCode(currentItemCode);
                                    inventoryProduct.setItemName(currentItemName);
                                    inventoryProduct.setUom(currentUoM);
                                    inventoryProduct.setInvUom(currentInvUom);
                                    inventoryProduct.setExpDate(currentExpDate);

                                    callback.onSuccess(inventoryProduct);
                                    return;

//                                    currentBarcode = null;
//                                    currentItemCode = null;
//                                    currentItemName = null;
//                                    currentUoM = null;
//                                    currentInvUom = null;
//                                    currentExpDate = null;
                                }
                            }

                            if (readyToGetExpDate == true) {
                                currentExpDate = data;
                                readyToGetExpDate = false;
                                if (!TextUtils.isEmpty(currentBarcode) && !TextUtils.isEmpty(currentItemCode) && !TextUtils.isEmpty(currentItemName) && !TextUtils.isEmpty(currentInvUom) && !TextUtils.isEmpty(currentUoM) && !TextUtils.isEmpty(currentExpDate)) {
                                    InventoryProduct inventoryProduct = new InventoryProduct();
                                    inventoryProduct.setBarcode(currentBarcode);
                                    inventoryProduct.setItemCode(currentItemCode);
                                    inventoryProduct.setItemName(currentItemName);
                                    inventoryProduct.setUom(currentUoM);
                                    inventoryProduct.setInvUom(currentInvUom);
                                    inventoryProduct.setExpDate(currentExpDate);

                                    callback.onSuccess(inventoryProduct);
                                    return;

//                                    currentBarcode = null;
//                                    currentItemCode = null;
//                                    currentItemName = null;
//                                    currentUoM = null;
//                                    currentInvUom = null;
//                                    currentExpDate = null;
                                }
                            }
                        }
                        eventType = xpp.next();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    callback.onFail(e.getMessage());
                }

                callback.onFail("");
            }

            @Override
            public void onFail(String error) {
                callback.onFail(error);
            }
        });
    }

    public void getLeaveReason(final GetLeaveReasonCallback callback) {
        String link = String.format(getURL() + Config.LEAVEREASON);

        mNetwork.executeGet(link, new Network.Callback() {
            @Override
            public void onCallBack(String response) {
                XmlPullParserFactory factory = null;
                List<LeaveReason> reasonList = new ArrayList<>();
                try {
                    boolean readyToGetCode = false;
                    boolean readyToGetName = false;

                    String currentCode = null;
                    String currentName = null;

                    factory = XmlPullParserFactory.newInstance();
                    factory.setNamespaceAware(true);
                    XmlPullParser xpp = factory.newPullParser();

                    xpp.setInput(new StringReader(response));
                    int eventType = xpp.getEventType();
                    while (eventType != XmlPullParser.END_DOCUMENT) {
                        if (eventType == XmlPullParser.START_DOCUMENT) {
                            //TODO: do nothing
                        } else if (eventType == XmlPullParser.START_TAG) {
                            if (xpp.getName().equalsIgnoreCase("LCode")) {
                                readyToGetCode = true;
                            }

                            if (xpp.getName().equalsIgnoreCase("LName")) {
                                readyToGetName = true;
                            }

                        } else if (eventType == XmlPullParser.TEXT) {
                            String data = xpp.getText();

                            if (readyToGetCode) {
                                currentCode = data;
                                readyToGetCode = false;
                                if (!TextUtils.isEmpty(currentCode) && !TextUtils.isEmpty(currentName)) {
                                    LeaveReason leaveReason = new LeaveReason();
                                    leaveReason.setLcode(currentCode);
                                    leaveReason.setLname(currentName);

                                    reasonList.add(leaveReason);

                                    currentCode = null;
                                    currentName = null;
                                }
                            }

                            if (readyToGetName) {
                                currentName = data;
                                readyToGetName = false;
                                if (!TextUtils.isEmpty(currentCode) && !TextUtils.isEmpty(currentName)) {
                                    LeaveReason leaveReason = new LeaveReason();
                                    leaveReason.setLcode(currentCode);
                                    leaveReason.setLname(currentName);

                                    reasonList.add(leaveReason);

                                    currentCode = null;
                                    currentName = null;
                                }
                            }
                        }
                        eventType = xpp.next();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    callback.onFail(e.getMessage());
                }

                callback.onSuccess(reasonList);
            }

            @Override
            public void onFail(String error) {
                callback.onFail(error);
            }
        });
    }

    public void getLeaveType(final GetLeaveTypeCallback callback) {
        String link = String.format(getURL() + Config.LEAVETYPE);

        mNetwork.executeGet(link, new Network.Callback() {
            @Override
            public void onCallBack(String response) {
                XmlPullParserFactory factory = null;
                List<LeaveType> typeList = new ArrayList<>();
                try {
                    boolean readyToGetCode = false;
                    boolean readyToGetName = false;

                    String currentCode = null;
                    String currentName = null;

                    factory = XmlPullParserFactory.newInstance();
                    factory.setNamespaceAware(true);
                    XmlPullParser xpp = factory.newPullParser();

                    xpp.setInput(new StringReader(response));
                    int eventType = xpp.getEventType();
                    while (eventType != XmlPullParser.END_DOCUMENT) {
                        if (eventType == XmlPullParser.START_DOCUMENT) {
                            //TODO: do nothing
                        } else if (eventType == XmlPullParser.START_TAG) {
                            if (xpp.getName().equalsIgnoreCase("LCode")) {
                                readyToGetCode = true;
                            }

                            if (xpp.getName().equalsIgnoreCase("LName")) {
                                readyToGetName = true;
                            }

                        } else if (eventType == XmlPullParser.TEXT) {
                            String data = xpp.getText();

                            if (readyToGetCode) {
                                currentCode = data;
                                readyToGetCode = false;
                                if (!TextUtils.isEmpty(currentCode) && !TextUtils.isEmpty(currentName)) {
                                    LeaveType leaveType = new LeaveType();
                                    leaveType.setLcode(currentCode);
                                    leaveType.setLname(currentName);

                                    typeList.add(leaveType);

                                    currentCode = null;
                                    currentName = null;
                                }
                            }

                            if (readyToGetName) {
                                currentName = data;
                                readyToGetName = false;
                                if (!TextUtils.isEmpty(currentCode) && !TextUtils.isEmpty(currentName)) {
                                    LeaveType leaveType = new LeaveType();
                                    leaveType.setLcode(currentCode);
                                    leaveType.setLname(currentName);

                                    typeList.add(leaveType);

                                    currentCode = null;
                                    currentName = null;
                                }
                            }
                        }
                        eventType = xpp.next();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    callback.onFail(e.getMessage());
                }

                callback.onSuccess(typeList);
            }

            @Override
            public void onFail(String error) {
                callback.onFail(error);
            }
        });
    }

    public void addLeave(String userCode, String deviceID, String fromDate, String toDate,
                         String leaveType, String reason, String remark, final AddLeaveCallback callback) {
        String link = String.format(getURL() + Config.ADDLEAVE);

        List<Param> params = new ArrayList<>();
        Param param = new Param("UserCode", userCode);
        params.add(param);
        param = new Param("DeviceID", deviceID);
        params.add(param);
        param = new Param("FromDate", fromDate);
        params.add(param);
        param = new Param("ToDate", toDate);
        params.add(param);
        param = new Param("LeaveType", leaveType);
        params.add(param);
        param = new Param("Reason", reason);
        params.add(param);
        param = new Param("Reamarks", remark);
        params.add(param);

        mNetwork.executePost(link, params, new Network.Callback() {
            @Override
            public void onCallBack(String response) {
                XmlPullParserFactory factory = null;
                try {
                    boolean readyToGetCode = false;

                    String currentCode = null;

                    factory = XmlPullParserFactory.newInstance();
                    factory.setNamespaceAware(true);
                    XmlPullParser xpp = factory.newPullParser();

                    xpp.setInput(new StringReader(response));
                    int eventType = xpp.getEventType();
                    while (eventType != XmlPullParser.END_DOCUMENT) {
                        if (eventType == XmlPullParser.START_DOCUMENT) {
                            //TODO: do nothing
                        } else if (eventType == XmlPullParser.START_TAG) {
                            if (xpp.getName().equalsIgnoreCase("ResultID")) {
                                readyToGetCode = true;
                            }

                        } else if (eventType == XmlPullParser.TEXT) {
                            String data = xpp.getText();

                            if (readyToGetCode) {
                                currentCode = data;
                                readyToGetCode = false;
                                if (!TextUtils.isEmpty(currentCode)) {

                                    callback.onSuccess(currentCode);

                                    currentCode = null;
                                }
                            }
                        }
                        eventType = xpp.next();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    callback.onFail(e.getMessage());
                }
            }

            @Override
            public void onFail(String error) {
                callback.onFail(error);
            }
        });
    }

    public void getLeaveLog(String userCode, final GetLeaveLogCallback callback) {
        String link = String.format(getURL() + Config.LEAVELOG);

        List<Param> params = new ArrayList<>();
        Param param = new Param("UserCode", userCode);
        params.add(param);

        mNetwork.executePost(link, params, new Network.Callback() {
            @Override
            public void onCallBack(String response) {
                List<LeaveLog> leaveLogs = new ArrayList<>();
                XmlPullParserFactory factory = null;
                try {
                    boolean readyToGetID = false;
                    boolean readyToGetUserCode = false;
                    boolean readyToGetDeviceID = false;
                    boolean readyToGetFromDate = false;
                    boolean readyToGetToDate = false;
                    boolean readyToGetType = false;
                    boolean readyToGetReason = false;
                    boolean readyToGetRemarks = false;
                    boolean readyToGetStatus = false;
                    boolean readyToGetCreateDate = false;
                    boolean readyToGetApproveDate = false;
                    boolean readyToGetApproveUser = false;

                    String currentID = null;
                    String currentUserCode = null;
                    String currentDeviceID = null;
                    String currentFromDate = null;
                    String currentToDate = null;
                    String currentType = null;
                    String currentReason = null;
                    String currentRemarks = null;
                    String currentStatus = null;
                    String currentCreateDate = null;
                    String currentApproveDate = null;
                    String currentApproveUser = null;


                    factory = XmlPullParserFactory.newInstance();
                    factory.setNamespaceAware(true);
                    XmlPullParser xpp = factory.newPullParser();

                    xpp.setInput(new StringReader(response));
                    int eventType = xpp.getEventType();
                    while (eventType != XmlPullParser.END_DOCUMENT) {
                        if (eventType == XmlPullParser.START_DOCUMENT) {
                            //TODO: do nothing
                        } else if (eventType == XmlPullParser.START_TAG) {
                            if (xpp.getName().equalsIgnoreCase("LeaveID")) {
                                readyToGetID = true;
                            }

                            if (xpp.getName().equalsIgnoreCase("UserCode")) {
                                readyToGetUserCode = true;
                            }

                            if (xpp.getName().equalsIgnoreCase("DeviceID")) {
                                readyToGetDeviceID = true;
                            }

                            if (xpp.getName().equalsIgnoreCase("FromDate")) {
                                readyToGetFromDate = true;
                            }

                            if (xpp.getName().equalsIgnoreCase("ToDate")) {
                                readyToGetToDate = true;
                            }

                            if (xpp.getName().equalsIgnoreCase("LeaveType")) {
                                readyToGetType = true;
                            }

                            if (xpp.getName().equalsIgnoreCase("Reason")) {
                                readyToGetReason = true;
                            }

                            if (xpp.getName().equalsIgnoreCase("Remarks")) {
                                readyToGetRemarks = true;
                            }

                            if (xpp.getName().equalsIgnoreCase("Status")) {
                                readyToGetStatus = true;
                            }

                            if (xpp.getName().equalsIgnoreCase("CreateDate")) {
                                readyToGetCreateDate = true;
                            }

                            if (xpp.getName().equalsIgnoreCase("ApproveDate")) {
                                readyToGetApproveDate = true;
                            }
                            if (xpp.getName().equalsIgnoreCase("ApproveUser")) {
                                readyToGetApproveUser = true;
                            }


                        } else if (eventType == XmlPullParser.TEXT) {
                            String data = xpp.getText();
                            if (readyToGetID) {
                                currentID = data;
                                readyToGetID = false;
                                if (!TextUtils.isEmpty(currentID) && !TextUtils.isEmpty(currentUserCode) &&
                                        !TextUtils.isEmpty(currentDeviceID) && !TextUtils.isEmpty(currentFromDate) &&
                                        !TextUtils.isEmpty(currentToDate) && !TextUtils.isEmpty(currentType) &&
                                        !TextUtils.isEmpty(currentReason) && !TextUtils.isEmpty(currentRemarks) &&
                                        !TextUtils.isEmpty(currentStatus) && !TextUtils.isEmpty(currentCreateDate)) {
                                    LeaveLog leaveLog = new LeaveLog();
                                    leaveLog.setLeaveId(currentID);
                                    leaveLog.setUserCode(currentUserCode);
                                    leaveLog.setDeveiceId(currentDeviceID);
                                    leaveLog.setFromDate(currentFromDate);
                                    leaveLog.setToDate(currentToDate);
                                    leaveLog.setLeaveType(currentType);
                                    leaveLog.setReason(currentReason);
                                    leaveLog.setRemark(currentRemarks);
                                    leaveLog.setStatus(currentStatus);
                                    leaveLog.setCreateDate(currentCreateDate);
                                    leaveLog.setApproveDate(currentApproveDate);
                                    leaveLog.setApproveUser(currentApproveUser);

                                    leaveLogs.add(leaveLog);

                                    currentID = null;
                                    currentUserCode = null;
                                    currentDeviceID = null;
                                    currentFromDate = null;
                                    currentToDate = null;
                                    currentType = null;
                                    currentReason = null;
                                    currentRemarks = null;
                                    currentStatus = null;
                                    currentCreateDate = null;
                                    currentApproveDate = null;
                                    currentApproveUser = null;
                                }
                            }

                            if (readyToGetUserCode) {
                                currentUserCode = data;
                                readyToGetUserCode = false;
                                if (!TextUtils.isEmpty(currentID) && !TextUtils.isEmpty(currentUserCode) &&
                                        !TextUtils.isEmpty(currentDeviceID) && !TextUtils.isEmpty(currentFromDate) &&
                                        !TextUtils.isEmpty(currentToDate) && !TextUtils.isEmpty(currentType) &&
                                        !TextUtils.isEmpty(currentReason) && !TextUtils.isEmpty(currentRemarks) &&
                                        !TextUtils.isEmpty(currentStatus) && !TextUtils.isEmpty(currentCreateDate)) {
                                    LeaveLog leaveLog = new LeaveLog();
                                    leaveLog.setLeaveId(currentID);
                                    leaveLog.setUserCode(currentUserCode);
                                    leaveLog.setDeveiceId(currentDeviceID);
                                    leaveLog.setFromDate(currentFromDate);
                                    leaveLog.setToDate(currentToDate);
                                    leaveLog.setLeaveType(currentType);
                                    leaveLog.setReason(currentReason);
                                    leaveLog.setRemark(currentRemarks);
                                    leaveLog.setStatus(currentStatus);
                                    leaveLog.setCreateDate(currentCreateDate);
                                    leaveLog.setApproveDate(currentApproveDate);
                                    leaveLog.setApproveUser(currentApproveUser);

                                    leaveLogs.add(leaveLog);

                                    currentID = null;
                                    currentUserCode = null;
                                    currentDeviceID = null;
                                    currentFromDate = null;
                                    currentToDate = null;
                                    currentType = null;
                                    currentReason = null;
                                    currentRemarks = null;
                                    currentStatus = null;
                                    currentCreateDate = null;
                                    currentApproveDate = null;
                                    currentApproveUser = null;
                                }
                            }

                            if (readyToGetDeviceID) {
                                currentDeviceID = data;
                                readyToGetDeviceID = false;
                                if (!TextUtils.isEmpty(currentID) && !TextUtils.isEmpty(currentUserCode) &&
                                        !TextUtils.isEmpty(currentDeviceID) && !TextUtils.isEmpty(currentFromDate) &&
                                        !TextUtils.isEmpty(currentToDate) && !TextUtils.isEmpty(currentType) &&
                                        !TextUtils.isEmpty(currentReason) && !TextUtils.isEmpty(currentRemarks) &&
                                        !TextUtils.isEmpty(currentStatus) && !TextUtils.isEmpty(currentCreateDate)) {
                                    LeaveLog leaveLog = new LeaveLog();
                                    leaveLog.setLeaveId(currentID);
                                    leaveLog.setUserCode(currentUserCode);
                                    leaveLog.setDeveiceId(currentDeviceID);
                                    leaveLog.setFromDate(currentFromDate);
                                    leaveLog.setToDate(currentToDate);
                                    leaveLog.setLeaveType(currentType);
                                    leaveLog.setReason(currentReason);
                                    leaveLog.setRemark(currentRemarks);
                                    leaveLog.setStatus(currentStatus);
                                    leaveLog.setCreateDate(currentCreateDate);
                                    leaveLog.setApproveDate(currentApproveDate);
                                    leaveLog.setApproveUser(currentApproveUser);

                                    leaveLogs.add(leaveLog);

                                    currentID = null;
                                    currentUserCode = null;
                                    currentDeviceID = null;
                                    currentFromDate = null;
                                    currentToDate = null;
                                    currentType = null;
                                    currentReason = null;
                                    currentRemarks = null;
                                    currentStatus = null;
                                    currentCreateDate = null;
                                    currentApproveDate = null;
                                    currentApproveUser = null;
                                }
                            }

                            if (readyToGetFromDate) {
                                currentFromDate = data;
                                readyToGetFromDate = false;
                                if (!TextUtils.isEmpty(currentID) && !TextUtils.isEmpty(currentUserCode) &&
                                        !TextUtils.isEmpty(currentDeviceID) && !TextUtils.isEmpty(currentFromDate) &&
                                        !TextUtils.isEmpty(currentToDate) && !TextUtils.isEmpty(currentType) &&
                                        !TextUtils.isEmpty(currentReason) && !TextUtils.isEmpty(currentRemarks) &&
                                        !TextUtils.isEmpty(currentStatus) && !TextUtils.isEmpty(currentCreateDate)) {
                                    LeaveLog leaveLog = new LeaveLog();
                                    leaveLog.setLeaveId(currentID);
                                    leaveLog.setUserCode(currentUserCode);
                                    leaveLog.setDeveiceId(currentDeviceID);
                                    leaveLog.setFromDate(currentFromDate);
                                    leaveLog.setToDate(currentToDate);
                                    leaveLog.setLeaveType(currentType);
                                    leaveLog.setReason(currentReason);
                                    leaveLog.setRemark(currentRemarks);
                                    leaveLog.setStatus(currentStatus);
                                    leaveLog.setCreateDate(currentCreateDate);
                                    leaveLog.setApproveDate(currentApproveDate);
                                    leaveLog.setApproveUser(currentApproveUser);

                                    leaveLogs.add(leaveLog);

                                    currentID = null;
                                    currentUserCode = null;
                                    currentDeviceID = null;
                                    currentFromDate = null;
                                    currentToDate = null;
                                    currentType = null;
                                    currentReason = null;
                                    currentRemarks = null;
                                    currentStatus = null;
                                    currentCreateDate = null;
                                    currentApproveDate = null;
                                    currentApproveUser = null;
                                }
                            }

                            if (readyToGetToDate) {
                                currentToDate = data;
                                readyToGetToDate = false;
                                if (!TextUtils.isEmpty(currentID) && !TextUtils.isEmpty(currentUserCode) &&
                                        !TextUtils.isEmpty(currentDeviceID) && !TextUtils.isEmpty(currentFromDate) &&
                                        !TextUtils.isEmpty(currentToDate) && !TextUtils.isEmpty(currentType) &&
                                        !TextUtils.isEmpty(currentReason) && !TextUtils.isEmpty(currentRemarks) &&
                                        !TextUtils.isEmpty(currentStatus) && !TextUtils.isEmpty(currentCreateDate)) {
                                    LeaveLog leaveLog = new LeaveLog();
                                    leaveLog.setLeaveId(currentID);
                                    leaveLog.setUserCode(currentUserCode);
                                    leaveLog.setDeveiceId(currentDeviceID);
                                    leaveLog.setFromDate(currentFromDate);
                                    leaveLog.setToDate(currentToDate);
                                    leaveLog.setLeaveType(currentType);
                                    leaveLog.setReason(currentReason);
                                    leaveLog.setRemark(currentRemarks);
                                    leaveLog.setStatus(currentStatus);
                                    leaveLog.setCreateDate(currentCreateDate);
                                    leaveLog.setApproveDate(currentApproveDate);
                                    leaveLog.setApproveUser(currentApproveUser);

                                    leaveLogs.add(leaveLog);

                                    currentID = null;
                                    currentUserCode = null;
                                    currentDeviceID = null;
                                    currentFromDate = null;
                                    currentToDate = null;
                                    currentType = null;
                                    currentReason = null;
                                    currentRemarks = null;
                                    currentStatus = null;
                                    currentCreateDate = null;
                                    currentApproveDate = null;
                                    currentApproveUser = null;
                                }
                            }

                            if (readyToGetType) {
                                currentType = data;
                                readyToGetType = false;
                                if (!TextUtils.isEmpty(currentID) && !TextUtils.isEmpty(currentUserCode) &&
                                        !TextUtils.isEmpty(currentDeviceID) && !TextUtils.isEmpty(currentFromDate) &&
                                        !TextUtils.isEmpty(currentToDate) && !TextUtils.isEmpty(currentType) &&
                                        !TextUtils.isEmpty(currentReason) && !TextUtils.isEmpty(currentRemarks) &&
                                        !TextUtils.isEmpty(currentStatus) && !TextUtils.isEmpty(currentCreateDate)) {
                                    LeaveLog leaveLog = new LeaveLog();
                                    leaveLog.setLeaveId(currentID);
                                    leaveLog.setUserCode(currentUserCode);
                                    leaveLog.setDeveiceId(currentDeviceID);
                                    leaveLog.setFromDate(currentFromDate);
                                    leaveLog.setToDate(currentToDate);
                                    leaveLog.setLeaveType(currentType);
                                    leaveLog.setReason(currentReason);
                                    leaveLog.setRemark(currentRemarks);
                                    leaveLog.setStatus(currentStatus);
                                    leaveLog.setCreateDate(currentCreateDate);
                                    leaveLog.setApproveDate(currentApproveDate);
                                    leaveLog.setApproveUser(currentApproveUser);

                                    leaveLogs.add(leaveLog);

                                    currentID = null;
                                    currentUserCode = null;
                                    currentDeviceID = null;
                                    currentFromDate = null;
                                    currentToDate = null;
                                    currentType = null;
                                    currentReason = null;
                                    currentRemarks = null;
                                    currentStatus = null;
                                    currentCreateDate = null;
                                    currentApproveDate = null;
                                    currentApproveUser = null;
                                }
                            }

                            if (readyToGetReason) {
                                currentReason = data;
                                readyToGetReason = false;
                                if (!TextUtils.isEmpty(currentID) && !TextUtils.isEmpty(currentUserCode) &&
                                        !TextUtils.isEmpty(currentDeviceID) && !TextUtils.isEmpty(currentFromDate) &&
                                        !TextUtils.isEmpty(currentToDate) && !TextUtils.isEmpty(currentType) &&
                                        !TextUtils.isEmpty(currentReason) && !TextUtils.isEmpty(currentRemarks) &&
                                        !TextUtils.isEmpty(currentStatus) && !TextUtils.isEmpty(currentCreateDate)) {
                                    LeaveLog leaveLog = new LeaveLog();
                                    leaveLog.setLeaveId(currentID);
                                    leaveLog.setUserCode(currentUserCode);
                                    leaveLog.setDeveiceId(currentDeviceID);
                                    leaveLog.setFromDate(currentFromDate);
                                    leaveLog.setToDate(currentToDate);
                                    leaveLog.setLeaveType(currentType);
                                    leaveLog.setReason(currentReason);
                                    leaveLog.setRemark(currentRemarks);
                                    leaveLog.setStatus(currentStatus);
                                    leaveLog.setCreateDate(currentCreateDate);
                                    leaveLog.setApproveDate(currentApproveDate);
                                    leaveLog.setApproveUser(currentApproveUser);

                                    leaveLogs.add(leaveLog);

                                    currentID = null;
                                    currentUserCode = null;
                                    currentDeviceID = null;
                                    currentFromDate = null;
                                    currentToDate = null;
                                    currentType = null;
                                    currentReason = null;
                                    currentRemarks = null;
                                    currentStatus = null;
                                    currentCreateDate = null;
                                    currentApproveDate = null;
                                    currentApproveUser = null;
                                }
                            }

                            if (readyToGetRemarks) {
                                currentRemarks = data;
                                readyToGetRemarks = false;
                                if (!TextUtils.isEmpty(currentID) && !TextUtils.isEmpty(currentUserCode) &&
                                        !TextUtils.isEmpty(currentDeviceID) && !TextUtils.isEmpty(currentFromDate) &&
                                        !TextUtils.isEmpty(currentToDate) && !TextUtils.isEmpty(currentType) &&
                                        !TextUtils.isEmpty(currentReason) && !TextUtils.isEmpty(currentRemarks) &&
                                        !TextUtils.isEmpty(currentStatus) && !TextUtils.isEmpty(currentCreateDate)) {
                                    LeaveLog leaveLog = new LeaveLog();
                                    leaveLog.setLeaveId(currentID);
                                    leaveLog.setUserCode(currentUserCode);
                                    leaveLog.setDeveiceId(currentDeviceID);
                                    leaveLog.setFromDate(currentFromDate);
                                    leaveLog.setToDate(currentToDate);
                                    leaveLog.setLeaveType(currentType);
                                    leaveLog.setReason(currentReason);
                                    leaveLog.setRemark(currentRemarks);
                                    leaveLog.setStatus(currentStatus);
                                    leaveLog.setCreateDate(currentCreateDate);
                                    leaveLog.setApproveDate(currentApproveDate);
                                    leaveLog.setApproveUser(currentApproveUser);

                                    leaveLogs.add(leaveLog);

                                    currentID = null;
                                    currentUserCode = null;
                                    currentDeviceID = null;
                                    currentFromDate = null;
                                    currentToDate = null;
                                    currentType = null;
                                    currentReason = null;
                                    currentRemarks = null;
                                    currentStatus = null;
                                    currentCreateDate = null;
                                    currentApproveDate = null;
                                    currentApproveUser = null;
                                }
                            }

                            if (readyToGetStatus) {
                                currentStatus = data;
                                readyToGetStatus = false;
                                if (!TextUtils.isEmpty(currentID) && !TextUtils.isEmpty(currentUserCode) &&
                                        !TextUtils.isEmpty(currentDeviceID) && !TextUtils.isEmpty(currentFromDate) &&
                                        !TextUtils.isEmpty(currentToDate) && !TextUtils.isEmpty(currentType) &&
                                        !TextUtils.isEmpty(currentReason) && !TextUtils.isEmpty(currentRemarks) &&
                                        !TextUtils.isEmpty(currentStatus) && !TextUtils.isEmpty(currentCreateDate)) {
                                    LeaveLog leaveLog = new LeaveLog();
                                    leaveLog.setLeaveId(currentID);
                                    leaveLog.setUserCode(currentUserCode);
                                    leaveLog.setDeveiceId(currentDeviceID);
                                    leaveLog.setFromDate(currentFromDate);
                                    leaveLog.setToDate(currentToDate);
                                    leaveLog.setLeaveType(currentType);
                                    leaveLog.setReason(currentReason);
                                    leaveLog.setRemark(currentRemarks);
                                    leaveLog.setStatus(currentStatus);
                                    leaveLog.setCreateDate(currentCreateDate);
                                    leaveLog.setApproveDate(currentApproveDate);
                                    leaveLog.setApproveUser(currentApproveUser);

                                    leaveLogs.add(leaveLog);

                                    currentID = null;
                                    currentUserCode = null;
                                    currentDeviceID = null;
                                    currentFromDate = null;
                                    currentToDate = null;
                                    currentType = null;
                                    currentReason = null;
                                    currentRemarks = null;
                                    currentStatus = null;
                                    currentCreateDate = null;
                                    currentApproveDate = null;
                                    currentApproveUser = null;
                                }
                            }

                            if (readyToGetCreateDate) {
                                currentCreateDate = data;
                                readyToGetCreateDate = false;
                                if (!TextUtils.isEmpty(currentID) && !TextUtils.isEmpty(currentUserCode) &&
                                        !TextUtils.isEmpty(currentDeviceID) && !TextUtils.isEmpty(currentFromDate) &&
                                        !TextUtils.isEmpty(currentToDate) && !TextUtils.isEmpty(currentType) &&
                                        !TextUtils.isEmpty(currentReason) && !TextUtils.isEmpty(currentRemarks) &&
                                        !TextUtils.isEmpty(currentStatus) && !TextUtils.isEmpty(currentCreateDate)) {
                                    LeaveLog leaveLog = new LeaveLog();
                                    leaveLog.setLeaveId(currentID);
                                    leaveLog.setUserCode(currentUserCode);
                                    leaveLog.setDeveiceId(currentDeviceID);
                                    leaveLog.setFromDate(currentFromDate);
                                    leaveLog.setToDate(currentToDate);
                                    leaveLog.setLeaveType(currentType);
                                    leaveLog.setReason(currentReason);
                                    leaveLog.setRemark(currentRemarks);
                                    leaveLog.setStatus(currentStatus);
                                    leaveLog.setCreateDate(currentCreateDate);
                                    leaveLog.setApproveDate(currentApproveDate);
                                    leaveLog.setApproveUser(currentApproveUser);

                                    leaveLogs.add(leaveLog);

                                    currentID = null;
                                    currentUserCode = null;
                                    currentDeviceID = null;
                                    currentFromDate = null;
                                    currentToDate = null;
                                    currentType = null;
                                    currentReason = null;
                                    currentRemarks = null;
                                    currentStatus = null;
                                    currentCreateDate = null;
                                    currentApproveDate = null;
                                    currentApproveUser = null;
                                }
                            }
                        }
                        eventType = xpp.next();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    callback.onFail(e.getMessage());
                }
                callback.onSuccess(leaveLogs);
            }

            @Override
            public void onFail(String error) {
                callback.onFail(error);
            }
        });
    }

    public void approveLeave(String leaveID, final ApproveLeaveCallback callback) {
        String link = String.format(getURL() + Config.LEAVEAPPROVE);

        List<Param> params = new ArrayList<>();
        Param param = new Param("LeaveID", leaveID);
        params.add(param);

        mNetwork.executePost(link, params, new Network.Callback() {
            @Override
            public void onCallBack(String response) {
                XmlPullParserFactory factory = null;
                try {
                    boolean readyToGetCode = false;

                    String currentCode = null;

                    factory = XmlPullParserFactory.newInstance();
                    factory.setNamespaceAware(true);
                    XmlPullParser xpp = factory.newPullParser();

                    xpp.setInput(new StringReader(response));
                    int eventType = xpp.getEventType();
                    while (eventType != XmlPullParser.END_DOCUMENT) {
                        if (eventType == XmlPullParser.START_DOCUMENT) {
                            //TODO: do nothing
                        } else if (eventType == XmlPullParser.START_TAG) {
                            if (xpp.getName().equalsIgnoreCase("ResultID")) {
                                readyToGetCode = true;
                            }

                        } else if (eventType == XmlPullParser.TEXT) {
                            String data = xpp.getText();
                            callback.onSuccess("success");
                        }
                        eventType = xpp.next();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    callback.onFail(e.getMessage());
                }
            }

            @Override
            public void onFail(String error) {
                callback.onFail(error);
            }
        });
    }

    public void addTimeSheet(String userCode, String deviceID, String lat, String lang, final AddTimeSheetCallback callback) {
        String link = String.format(getURL() + Config.ADDTIMESHEET);

        List<Param> params = new ArrayList<>();
        Param param = new Param("UserCode", userCode);
        params.add(param);
        param = new Param("DeviceID", deviceID);
        params.add(param);
        param = new Param("LatitudeValue_Start", lat);
        params.add(param);
        param = new Param("LongitudeValue_Start", lang);
        params.add(param);

        mNetwork.executePost(link, params, new Network.Callback() {
            @Override
            public void onCallBack(String response) {
                XmlPullParserFactory factory = null;
                try {
                    boolean readyToGetCode = false;

                    String currentCode = null;

                    factory = XmlPullParserFactory.newInstance();
                    factory.setNamespaceAware(true);
                    XmlPullParser xpp = factory.newPullParser();

                    xpp.setInput(new StringReader(response));
                    int eventType = xpp.getEventType();
                    while (eventType != XmlPullParser.END_DOCUMENT) {
                        if (eventType == XmlPullParser.START_DOCUMENT) {
                            //TODO: do nothing
                        } else if (eventType == XmlPullParser.START_TAG) {
                            if (xpp.getName().equalsIgnoreCase("ResultID")) {
                                readyToGetCode = true;
                            }

                        } else if (eventType == XmlPullParser.TEXT) {
                            String data = xpp.getText();

                            if (readyToGetCode) {
                                currentCode = data;
                                readyToGetCode = false;
                                if (!TextUtils.isEmpty(currentCode)) {

                                    callback.onSuccess(currentCode);

                                    currentCode = null;
                                }
                            }
                        }
                        eventType = xpp.next();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    callback.onFail(e.getMessage());
                }
            }

            @Override
            public void onFail(String error) {
                callback.onFail(error);
            }
        });
    }

    public void updateTimeSheet(String timeID, String lat, String lang, final UpdateTimeSheetCallback callback) {
        String link = String.format(getURL() + Config.UPDATETIMESHEET);

        List<Param> params = new ArrayList<>();
        Param param = new Param("TSId", timeID);
        params.add(param);
        param = new Param("LatitudeValue_End", lat);
        params.add(param);
        param = new Param("LongitudeValue_end", lang);
        params.add(param);

        mNetwork.executePost(link, params, new Network.Callback() {
            @Override
            public void onCallBack(String response) {
                XmlPullParserFactory factory = null;
                try {
                    boolean readyToGetCode = false;

                    String currentCode = null;

                    factory = XmlPullParserFactory.newInstance();
                    factory.setNamespaceAware(true);
                    XmlPullParser xpp = factory.newPullParser();

                    xpp.setInput(new StringReader(response));
                    int eventType = xpp.getEventType();
                    while (eventType != XmlPullParser.END_DOCUMENT) {
                        if (eventType == XmlPullParser.START_DOCUMENT) {
                            //TODO: do nothing
                        } else if (eventType == XmlPullParser.START_TAG) {
                            if (xpp.getName().equalsIgnoreCase("ResultID")) {
                                readyToGetCode = true;
                            }

                        } else if (eventType == XmlPullParser.TEXT) {
                            String data = xpp.getText();

                            if (readyToGetCode) {
                                currentCode = data;
                                readyToGetCode = false;
                                if (!TextUtils.isEmpty(currentCode) && currentCode.equals("1")) {

                                    callback.onSuccess(currentCode);

                                    currentCode = null;
                                }
                            }
                        }
                        eventType = xpp.next();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    callback.onFail(e.getMessage());
                }
            }

            @Override
            public void onFail(String error) {
                callback.onFail(error);
            }
        });
    }

    public void getTimeSheetLog(String userCode, final LogTimeSheetCallback callback) {
        String link = String.format(getURL() + Config.TIMESHEETLOG);

        List<Param> params = new ArrayList<>();
        Param param = new Param("UserCode", userCode);
        params.add(param);

        mNetwork.executePost(link, params, new Network.Callback() {
            @Override
            public void onCallBack(String response) {
                XmlPullParserFactory factory = null;
                List<TimeSheet> sheetList = new ArrayList<>();
                try {
                    boolean readyToGetTSid = false;
                    boolean readyToGetUserCode = false;
                    boolean readyToGetDeviceID = false;
                    boolean readyToGetStartTime = false;
                    boolean readyToGetLatStart = false;
                    boolean readyToGetLongStart = false;
                    boolean readyToGetEndTime = false;
                    boolean readyToGetLatEnd = false;
                    boolean readyToGetLongEnd = false;

                    String currentTSid = null;
                    String currentUserCode = null;
                    String currentDeviceID = null;
                    String currentStartTime = null;
                    String currentLatStart = null;
                    String currentLongStart = null;
                    String currentEndTime = null;
                    String currentLatEnd = null;
                    String currentLongEnd = null;

                    factory = XmlPullParserFactory.newInstance();
                    factory.setNamespaceAware(true);
                    XmlPullParser xpp = factory.newPullParser();

                    xpp.setInput(new StringReader(response));
                    int eventType = xpp.getEventType();
                    while (eventType != XmlPullParser.END_DOCUMENT) {
                        if (eventType == XmlPullParser.START_DOCUMENT) {
                            //TODO: do nothing
                        } else if (eventType == XmlPullParser.START_TAG) {
                            if (xpp.getName().equalsIgnoreCase("TSId")) {
                                readyToGetTSid = true;
                            }

                            if (xpp.getName().equalsIgnoreCase("UserCode")) {
                                readyToGetUserCode = true;
                            }

                            if (xpp.getName().equalsIgnoreCase("DeviceID")) {
                                readyToGetDeviceID = true;
                            }

                            if (xpp.getName().equalsIgnoreCase("StartTime")) {
                                readyToGetStartTime = true;
                            }

                            if (xpp.getName().equalsIgnoreCase("LatitudeValue_Start")) {
                                readyToGetLatStart = true;
                            }

                            if (xpp.getName().equalsIgnoreCase("LongitudeValue_Start")) {
                                readyToGetLongStart = true;
                            }

                            if (xpp.getName().equalsIgnoreCase("EndTime")) {
                                readyToGetEndTime = true;
                            }

                            if (xpp.getName().equalsIgnoreCase("LatitudeValue_End")) {
                                readyToGetLatEnd = true;
                            }

                            if (xpp.getName().equalsIgnoreCase("LongitudeValue_end")) {
                                readyToGetLongEnd = true;
                            }

                        } else if (eventType == XmlPullParser.TEXT) {
                            String data = xpp.getText();

                            if (readyToGetTSid) {
                                currentTSid = data;
                                readyToGetTSid = false;
                                if (!TextUtils.isEmpty(currentTSid) && !TextUtils.isEmpty(currentUserCode) &&
                                        !TextUtils.isEmpty(currentDeviceID) && !TextUtils.isEmpty(currentStartTime) &&
                                        !TextUtils.isEmpty(currentEndTime)) {
                                    TimeSheet timeSheet = new TimeSheet();
                                    timeSheet.setTsid(currentTSid);
                                    timeSheet.setUsercode(currentUserCode);
                                    timeSheet.setDeviceid(currentDeviceID);
                                    timeSheet.setStarttime(currentStartTime);
                                    timeSheet.setEndtime(currentEndTime);

                                    sheetList.add(timeSheet);

                                    currentTSid = null;
                                    currentUserCode = null;
                                    currentDeviceID = null;
                                    currentStartTime = null;
                                    currentEndTime = null;
                                }
                            }

                            if (readyToGetUserCode) {
                                currentUserCode = data;
                                readyToGetUserCode = false;
                                if (!TextUtils.isEmpty(currentTSid) && !TextUtils.isEmpty(currentUserCode) &&
                                        !TextUtils.isEmpty(currentDeviceID) && !TextUtils.isEmpty(currentStartTime) &&
                                        !TextUtils.isEmpty(currentEndTime)) {
                                    TimeSheet timeSheet = new TimeSheet();
                                    timeSheet.setTsid(currentTSid);
                                    timeSheet.setUsercode(currentUserCode);
                                    timeSheet.setDeviceid(currentDeviceID);
                                    timeSheet.setStarttime(currentStartTime);
                                    timeSheet.setEndtime(currentEndTime);

                                    sheetList.add(timeSheet);

                                    currentTSid = null;
                                    currentUserCode = null;
                                    currentDeviceID = null;
                                    currentStartTime = null;
                                    currentEndTime = null;
                                }
                            }

                            if (readyToGetDeviceID) {
                                currentDeviceID = data;
                                readyToGetDeviceID = false;
                                if (!TextUtils.isEmpty(currentTSid) && !TextUtils.isEmpty(currentUserCode) &&
                                        !TextUtils.isEmpty(currentDeviceID) && !TextUtils.isEmpty(currentStartTime) &&
                                        !TextUtils.isEmpty(currentEndTime)) {
                                    TimeSheet timeSheet = new TimeSheet();
                                    timeSheet.setTsid(currentTSid);
                                    timeSheet.setUsercode(currentUserCode);
                                    timeSheet.setDeviceid(currentDeviceID);
                                    timeSheet.setStarttime(currentStartTime);
                                    timeSheet.setEndtime(currentEndTime);

                                    sheetList.add(timeSheet);

                                    currentTSid = null;
                                    currentUserCode = null;
                                    currentDeviceID = null;
                                    currentStartTime = null;
                                    currentEndTime = null;
                                }
                            }

                            if (readyToGetStartTime) {
                                currentStartTime = data;
                                readyToGetStartTime = false;
                                if (!TextUtils.isEmpty(currentTSid) && !TextUtils.isEmpty(currentUserCode) &&
                                        !TextUtils.isEmpty(currentDeviceID) && !TextUtils.isEmpty(currentStartTime) &&
                                        !TextUtils.isEmpty(currentEndTime)) {
                                    TimeSheet timeSheet = new TimeSheet();
                                    timeSheet.setTsid(currentTSid);
                                    timeSheet.setUsercode(currentUserCode);
                                    timeSheet.setDeviceid(currentDeviceID);
                                    timeSheet.setStarttime(currentStartTime);
                                    timeSheet.setEndtime(currentEndTime);

                                    sheetList.add(timeSheet);

                                    currentTSid = null;
                                    currentUserCode = null;
                                    currentDeviceID = null;
                                    currentStartTime = null;
                                    currentEndTime = null;
                                }
                            }

                            if (readyToGetEndTime) {
                                currentEndTime = data;
                                readyToGetEndTime = false;
                                if (!TextUtils.isEmpty(currentTSid) && !TextUtils.isEmpty(currentUserCode) &&
                                        !TextUtils.isEmpty(currentDeviceID) && !TextUtils.isEmpty(currentStartTime) &&
                                        !TextUtils.isEmpty(currentEndTime)) {
                                    TimeSheet timeSheet = new TimeSheet();
                                    timeSheet.setTsid(currentTSid);
                                    timeSheet.setUsercode(currentUserCode);
                                    timeSheet.setDeviceid(currentDeviceID);
                                    timeSheet.setStarttime(currentStartTime);
                                    timeSheet.setEndtime(currentEndTime);

                                    sheetList.add(timeSheet);

                                    currentTSid = null;
                                    currentUserCode = null;
                                    currentDeviceID = null;
                                    currentStartTime = null;
                                    currentEndTime = null;
                                }
                            } else {
                                if (!TextUtils.isEmpty(currentTSid) && !TextUtils.isEmpty(currentUserCode) &&
                                        !TextUtils.isEmpty(currentDeviceID) && !TextUtils.isEmpty(currentStartTime)) {
                                    TimeSheet timeSheet = new TimeSheet();
                                    timeSheet.setTsid(currentTSid);
                                    timeSheet.setUsercode(currentUserCode);
                                    timeSheet.setDeviceid(currentDeviceID);
                                    timeSheet.setStarttime(currentStartTime);

                                    sheetList.add(timeSheet);

                                    currentTSid = null;
                                    currentUserCode = null;
                                    currentDeviceID = null;
                                    currentStartTime = null;
                                    currentEndTime = null;
                                }
                            }
                        }
                        eventType = xpp.next();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    callback.onFail(e.getMessage());
                }
                callback.onSuccess(sheetList);
            }

            @Override
            public void onFail(String error) {
                callback.onFail(error);
            }
        });
    }

    public void changePassword(String userCode, String oldPass, String newPass, String deviceID, final CommonCallback callback) {
        String link = String.format(getURL() + Config.CHANGEPASSWORD);

        List<Param> params = new ArrayList<>();
        Param param = new Param("UserCode", userCode);
        params.add(param);

        param = new Param("OldPass", oldPass);
        params.add(param);

        param = new Param("NewPassword", newPass);
        params.add(param);

        param = new Param("DeviceID", deviceID);
        params.add(param);

        mNetwork.executePost(link, params, new Network.Callback() {
            @Override
            public void onCallBack(String response) {
                XmlPullParserFactory factory = null;
                try {
                    boolean readyToGetResult = false;

                    factory = XmlPullParserFactory.newInstance();
                    factory.setNamespaceAware(true);
                    XmlPullParser xpp = factory.newPullParser();

                    xpp.setInput(new StringReader(response));
                    int eventType = xpp.getEventType();
                    while (eventType != XmlPullParser.END_DOCUMENT) {
                        if (eventType == XmlPullParser.START_DOCUMENT) {
                            //TODO: do nothing
                        } else if (eventType == XmlPullParser.START_TAG) {
                            if (xpp.getName().equalsIgnoreCase("AlertSystem")) {
                                readyToGetResult = true;
                            }

                        } else if (eventType == XmlPullParser.TEXT) {
                            String data = xpp.getText();

                            if (readyToGetResult) {
                                if (data.contains("UnSuccessfully")) {
                                    callback.onFail(data);
                                    return;
                                }

                                callback.onSuccess();
                                return;
                            }
                        }
                        eventType = xpp.next();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    callback.onFail(e.getMessage());
                }
            }

            @Override
            public void onFail(String error) {
                callback.onFail(error);
            }
        });
    }

}
