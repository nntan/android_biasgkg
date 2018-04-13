package com.luan.dms_management.service;

import com.luan.dms_management.models.Customer;
import com.luan.dms_management.models.NormalProduct;
import com.luan.dms_management.models.OrderbyDoc;
import com.luan.dms_management.models.Orders;
import com.luan.dms_management.models.Product;
import com.luan.dms_management.models.ProductCheck;
import com.luan.dms_management.models.ProductCheckForOrder;
import com.luan.dms_management.models.PromotionProduct;
import com.luan.dms_management.models.StaffLocation;
import com.luan.dms_management.models.StaffMonitor;
import com.luan.dms_management.models.TimeKeeping;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by luan.nt on 8/2/2017.
 */

public class BasicService {
    public static List<NormalProduct> currentProductList;
    public static Product justAddedProduct;

    public static List<ProductCheckForOrder> currentChosenOrderProduct;
    public static List<PromotionProduct> currentPromotionProduct;
    public static List<ProductCheckForOrder> justAddedProductForOrder;
    public static ArrayList<Customer> cusList = new ArrayList<>();
    public static ArrayList<ProductCheck> productChecks = new ArrayList<>();
    public static List<Product> proList = new ArrayList<>();
    public static List<StaffMonitor> staffMonitorList;
    public static List<StaffLocation> staffLocationList;
    public static ArrayList<Orders> orderList = new ArrayList<>();
    public static ArrayList<OrderbyDoc> orderbyDocs = new ArrayList<>();
    public static ArrayList<Product> proChooseList = new ArrayList<>();
    public static ArrayList<String> imgList = new ArrayList<>();
    public static ArrayList<TimeKeeping> timeKeepings = new ArrayList<>();
    public static Customer currentCus;
    public static StaffLocation currentStaff;
    public static Orders orders;
    public static ArrayList<String> imgpath=new ArrayList<String>();

}
