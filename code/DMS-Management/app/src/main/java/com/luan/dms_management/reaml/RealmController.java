package com.luan.dms_management.reaml;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.support.v4.app.Fragment;

import com.luan.dms_management.models.Channel;
import com.luan.dms_management.models.Customer;
import com.luan.dms_management.models.CustomerGroup;
import com.luan.dms_management.models.CustomerTravel;
import com.luan.dms_management.models.NoteType;
import com.luan.dms_management.models.Orders;
import com.luan.dms_management.models.Product;
import com.luan.dms_management.models.Route;
import com.luan.dms_management.models.Staff;
import com.luan.dms_management.models.StaffLocation;
import com.luan.dms_management.models.StatusOrder;
import com.luan.dms_management.models.TypeOrder;
import com.luan.dms_management.models.TypeVisit;
import com.luan.dms_management.models.WareHouse;

import io.realm.Realm;
import io.realm.RealmResults;

import static android.R.attr.id;


/**
 * Created by luan.nt on 9/1/2017.
 */

public class RealmController {
    private static RealmController instance;
    private final Realm realm;

    public RealmController(Application application) {
        realm = Realm.getDefaultInstance();
    }

    public RealmController(Context context) {
        realm = Realm.getDefaultInstance();
    }

    public static RealmController with(Fragment fragment) {
        if (instance == null) {
            instance = new RealmController(fragment.getActivity().getApplication());
        }
        return instance;
    }

    public static RealmController with(Activity activity) {
        if (instance == null) {
            instance = new RealmController(activity.getApplication());
        }
        return instance;
    }

    public static RealmController with(Context context) {
        if (instance == null) {
            instance = new RealmController(context);
        }
        return instance;
    }

    public static RealmController with(Application application) {
        if (instance == null) {
            instance = new RealmController(application);
        }
        return instance;
    }

    public static RealmController getInstance() {
        return instance;
    }

    public Realm getRealm() {
        return realm;
    }

    //Refresh the realm istance
    public void refresh() {
        realm.refresh();
    }

    public void clearAll() {
        realm.beginTransaction();
        realm.delete(Product.class);
        realm.delete(Staff.class);
        realm.delete(Customer.class);
        realm.delete(Route.class);
        realm.delete(Channel.class);
        realm.delete(CustomerGroup.class);
        realm.delete(CustomerTravel.class);
        realm.delete(Orders.class);
        realm.delete(TypeOrder.class);
        realm.delete(TypeVisit.class);
        realm.delete(WareHouse.class);
        realm.delete(StatusOrder.class);
        realm.commitTransaction();
    }

    //find all objects in the Book.class
    public RealmResults<Product> getAllProduct() {
        return realm.where(Product.class).findAll();
    }

    //query a single item with the given nameContact
    public Product getProduct(String id) {
        return realm.where(Product.class).equalTo("ItemCode", id).findFirst();
    }

    //query a single item with the given nameContact
    public RealmResults<Product> findProduct(String search) {
        return realm.where(Product.class).contains("ItemName", search).findAll();
    }

    public RealmResults<Staff> getAllStaff() {
        return realm.where(Staff.class).findAll();
    }


    public RealmResults<StaffLocation> getAllStaffLocation() {
        return realm.where(StaffLocation.class).findAll();
    }

    public Staff getStaff(String id) {
        return realm.where(Staff.class).equalTo("SlpCode", id).findFirst();
    }

    public RealmResults<Customer> getAllCustomer() {
        return realm.where(Customer.class).findAll();
    }

    public Customer getCustomer(String id) {
        return realm.where(Customer.class).equalTo("CardCode", id).findFirst();
    }

    public RealmResults<Route> getAllRoute() {
        return realm.where(Route.class).findAll();
    }

    public RealmResults<NoteType> getAllNoteType() {
        return realm.where(NoteType.class).findAll();
    }

    public RealmResults<Channel> getAllChannel() {
        return realm.where(Channel.class).findAll();
    }

    public RealmResults<CustomerGroup> getAllCusGrp() {
        return realm.where(CustomerGroup.class).findAll();
    }

    public CustomerGroup getCusGrpByID(String id) {
        return realm.where(CustomerGroup.class).equalTo("GrpCode", id).findFirst();
    }

    public RealmResults<TypeVisit> getTypeVisit() {
        return realm.where(TypeVisit.class).findAll();
    }

    public RealmResults<TypeOrder> getTypeOrder() {
        return realm.where(TypeOrder.class).findAll();
    }

    public RealmResults<StatusOrder> getStatusOrder() {
        return realm.where(StatusOrder.class).findAll();
    }

    public RealmResults<Orders> getAllOrder() {
        return realm.where(Orders.class).findAll();
    }

    public RealmResults<WareHouse> getWareHouse() {
        return realm.where(WareHouse.class).findAll();
    }
}
