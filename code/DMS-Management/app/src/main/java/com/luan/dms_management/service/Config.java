package com.luan.dms_management.service;

/**
 * Created by luan.nt on 8/29/2017.
 */

public class Config {

    public static final String URL_SAVE_KEY = "URL_SAVE_KEY";

    public static final String HOST = "http://idn.com.vn/BSGKG_WebSrv/IDNWebSerives.asmx";
    //public static final String HOST = "http://kd.biasaigonkiengiang.com.vn/api/IDNWebSerives.asmx";
    public static final String LOGIN = "/IDN_SmartPhone_Login_DeviceID";
    public static final String LISTSTAFF = "/IDN_OSLP_Load";
    public static final String LISTPRODUCT = "/IDN_ItemListSales_Search";
    public static final String SEARCHCUSTOMER = "/IDN_DMS_OCRD_Search";
    public static final String LISTCUSTOMER = "/IDN_DMS_OCRD_Load";
    public static final String LISTSTAFFLOCATION = "/IDN_DMS_EmpCurrLocation_Load";
    public static final String LISTSTAFFMONITOR = "/IDN_DMS_Employee_Monitor_Load";
    public static final String ROUTE = "/IDN_ROUTE_Load";
    public static final String CUSTGRP = "/IDN_CUST_GRP_Load";
    public static final String CHANNEL = "/IDN_CHANNEL_Load";
    public static final String TYPEVISIT = "/IDN_Visit_List";
    public static final String TYPEORDER = "/IDN_OrderType_List";
    public static final String STATUSORDER = "/IDN_DMS_StatusOrder_List";
    public static final String WAREHOUSE = "/IDN_DMS_WhsList";
    public static final String LISTORDER = "/IDN_DMS_OrderList_Search";
    public static final String ORDERBYDOC = "/IDN_DMS_Order_ByDocEntry";
    public static final String LISTCUSTRAVEL = "/IDN_DMS_CustVisited_Emp_ByEmp";
    public static final String LISTNOTESTYPE = "/iD_NotesGroup_Load";
    public static final String GETLISTPRODUCTFROMCUST = "/IDN_ItemList_CheckIn_InStock";
    public static final String CHECKIN = "/iD_CheckInLog_Add";
    public static final String CHECKOUT = "/iD_CheckOutLog_Add";
    public static final String CHECKINNOTE = "/iD_CheckIn_Notes_Add";
    public static final String CHECKINSTOCK = "/iD_CheckIn_InStock_Add";
    public static final String GETNOTE = "/id_CheckIn_Notes_LoadbyCustCode";
    public static final String ADDCUST = "/iD_OCRD_Add";
    public static final String GETPROMOTIONPRODUCTS = "/iD_ItemPromotionList_byCust";
    public static final String GETPRICE = "/iD_ItemPrice_byCustCode_UoM";
    public static final String ADDORDER = "/iD_CORDR_CustomerOrder_Add";
    public static final String STATISTICS = "/id_CheckIn_Statistics_LoadbyCustCode";
    public static final String INVENTORYPRODUCT = "/IDN_DMS_ItemInfo_ByBarCode";
    public static final String LEAVEREASON = "/iD_LeaveReason_Load";
    public static final String LEAVETYPE = "/iD_LeaveType_Load";
    public static final String ADDLEAVE = "/iD_Leave_Add";
    public static final String LEAVELOG = "/iD_Leave_Log";
    public static final String LEAVEAPPROVE = "/iD_Leave_Approve";
    public static final String ADDTIMESHEET = "/iD_TimeSheet_Add";
    public static final String TIMESHEETLOG = "/iD_TimeSheet_Log";
    public static final String UPDATETIMESHEET = "/iD_TimeSheet_Update";
    public static final String CHANGEPASSWORD = "/iD_OUSR_ChangePassword";
    public static final String CHECKINPIC="/iD_CheckIn_Pics_Add";
}
