package com.luan.dms_management.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.luan.dms_management.R;
import com.luan.dms_management.adapter.ProductOrderAdapter;
import com.luan.dms_management.models.OrderbyDoc;
import com.luan.dms_management.models.TypeOrder;
import com.luan.dms_management.models.WareHouse;
import com.luan.dms_management.reaml.RealmController;
import com.luan.dms_management.service.ApiService;
import com.luan.dms_management.service.BasicService;
import com.luan.dms_management.utils.BaseActivity;
import com.luan.dms_management.utils.CommonUtils;
import com.luan.dms_management.utils.Constant;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnItemSelected;

public class OrderDetail extends BaseActivity {

    @BindView(R.id.orderdetail_cus)
    protected TextView txtName;
    @BindView(R.id.orderdetail_addr)
    protected TextView txtAddress;
    @BindView(R.id.orderdetail_contact)
    protected TextView txtContact;
    @BindView(R.id.orderdetail_phone)
    protected TextView txtPhone;
    @BindView(R.id.orderdetail_groupcus)
    protected TextView txtGroupCus;
    @BindView(R.id.orderdetail_typecus)
    protected TextView txtTypeCus;
    @BindView(R.id.orderdetail_cast)
    protected TextView txtCast;
    @BindView(R.id.orderdetail_dept)
    protected TextView txtDept;
    @BindView(R.id.orderdetail_dateship)
    protected TextView txtDateShip;
    @BindView(R.id.orderdetail_costpay)
    protected TextView txtCostPay;
    @BindView(R.id.orderdetail_typeorder)
    protected Spinner typeOrder;
    @BindView(R.id.orderdetail_warehouse)
    protected Spinner wareHouse;
    @BindView(R.id.orderdetail_discount)
    protected TextView txtDiscount;
    @BindView(R.id.orderdetail_note)
    protected TextView txtNote;
    @BindView(R.id.lv_product_order)
    protected ListView listView;
    @BindView(R.id.bottom_navigation)
    protected BottomNavigationView navigationView;

    private ProductOrderAdapter adapter;

    private String[] strings;
    private ApiService mService;

    private ArrayList<String> listOrderType, listWareHouse;
    private ArrayList<String> listOrderTypeValue, listWareHouseValue;
    int posWareHouse, posTypeOrder;
    private String mode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        ButterKnife.bind(this);
        mode = getIntent().getStringExtra("ModeDetail");
        strings = getResources().getStringArray(R.array.planets_array);
        mService = ApiService.getInstance(this);
        downloadData();
        configControlEvent();
    }

    private void loadData(ArrayList<OrderbyDoc> data) {
        BasicService.orderbyDocs = data;
        adapter = new ProductOrderAdapter(this, data);
        listView.setAdapter(adapter);
        CommonUtils.setListViewHeightBasedOnChildrenCart(this, listView);

        BasicService.currentCus = RealmController.with(this).getCustomer(BasicService.orders.getCardCode());

        txtName.setText(BasicService.orders.getCardName());
        txtAddress.setText(BasicService.orders.getAddress());
        txtContact.setText(BasicService.currentCus.getContactPerson());
        txtPhone.setText(BasicService.currentCus.getTel());
        txtGroupCus.setText(RealmController.with(this).getCusGrpByID(BasicService.currentCus.getCustGrp()).getGrpName());
        txtCast.setText(BasicService.orders.getDocTotal());
        txtDateShip.setText(CommonUtils.formatDate(BasicService.orders.getDeliveryDate()));
        txtCostPay.setText(BasicService.orders.getDocGToal());
        txtNote.setText(BasicService.orders.getRemark());
        loadOrderType();
        loadWareHouse();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                OrderDetail.this, android.R.layout.simple_spinner_item, listWareHouseValue);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        wareHouse.setAdapter(adapter);

        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(
                OrderDetail.this, android.R.layout.simple_spinner_item, listOrderTypeValue);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeOrder.setAdapter(adapter1);

        wareHouse.setSelection(0);
        typeOrder.setSelection(Integer.parseInt(BasicService.orders.getOrderType()));

    }

    private void downloadData() {
        mService.loadListOrderbyDoc(BasicService.orders.getDocEntry(), new ApiService.GetListOrderbyDoc() {
            @Override
            public void onSuccess(final ArrayList<OrderbyDoc> data) {
                OrderDetail.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            loadData(data);
                        } catch (Exception e) {
                            showErrorDialog(e.getMessage());
                        }
                    }
                });
            }

            @Override
            public void onFail(final String error) {
                OrderDetail.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (error.equalsIgnoreCase("")) {
                            showErrorDialog(getString(R.string.load_list_order));
                        } else {
                            showErrorDialog(error);
                        }
                    }
                });
            }
        });
    }

    private void loadOrderType() {
        listOrderType = new ArrayList<>();
        listOrderTypeValue = new ArrayList<>();
        for (TypeOrder typeOrder : RealmController.with(this).getTypeOrder()) {
            listOrderType.add(typeOrder.getTypeCode());
            listOrderTypeValue.add(typeOrder.getTypeName());
        }
    }

    private void loadWareHouse() {
        listWareHouse = new ArrayList<>();
        listWareHouseValue = new ArrayList<>();
        for (WareHouse wareHouse : RealmController.with(this).getWareHouse()) {
            listWareHouse.add(wareHouse.getWhsCode());
            listWareHouseValue.add(wareHouse.getWhsName());
        }
    }

    private void configControlEvent() {
        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_addproduct:
                        if (mode.equalsIgnoreCase("View")) {
                            CommonUtils.makeToast(OrderDetail.this, "Không thể thêm sản phẩm");
                        } else {
                            Intent intent = new Intent(OrderDetail.this, AddProduct.class);
                            intent.putExtra("ModeType", "OrderDetail");
                            startActivityForResult(intent, Constant.ADD_PRODUCT);
                        }
                        break;
                    case R.id.action_sale:

                        break;
                    case R.id.action_save:

                        break;
                }
                return false;
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constant.ADD_PRODUCT) {
            //loadData();
        }
    }

    @OnItemSelected(R.id.orderdetail_warehouse)
    void onSelectedWareHouse(int position) {
        posWareHouse = position;
    }

    @OnItemSelected(value = R.id.orderdetail_warehouse, callback = OnItemSelected.Callback.NOTHING_SELECTED)
    void onNothingSelectedWarehouse() {
        //posWareHouse = 0;
    }

    @OnItemSelected(R.id.orderdetail_typeorder)
    void onSelectedTypeOrder(int position) {
        posTypeOrder = position;
    }

    @OnItemSelected(value = R.id.orderdetail_typeorder, callback = OnItemSelected.Callback.NOTHING_SELECTED)
    void onSelectedTypeOrder() {
        //posTypeOrder = 0;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            // finish the activity
            onBackPressed();
            return true;
        }

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_filter) {
            CommonUtils.makeToast(this, "ok");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
