package com.luan.dms_management.activities;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.luan.dms_management.R;
import com.luan.dms_management.adapter.ProductOfOrderAdapter;
import com.luan.dms_management.adapter.ProductOrderAdapter;
import com.luan.dms_management.models.Customer;
import com.luan.dms_management.models.CustomerGroup;
import com.luan.dms_management.models.OrderbyDoc;
import com.luan.dms_management.models.ProductCheckForOrder;
import com.luan.dms_management.models.PromotionProduct;
import com.luan.dms_management.models.Route;
import com.luan.dms_management.models.TypeOrder;
import com.luan.dms_management.models.WareHouse;
import com.luan.dms_management.reaml.RealmController;
import com.luan.dms_management.service.ApiService;
import com.luan.dms_management.service.BasicService;
import com.luan.dms_management.service.SyncService;
import com.luan.dms_management.utils.BaseActivity;
import com.luan.dms_management.utils.CommonUtils;
import com.luan.dms_management.utils.Constant;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemSelected;

import static com.luan.dms_management.utils.Constant.ADD_PROMOTION;

public class CreateOrderDetail extends BaseActivity implements DatePickerDialog.OnDateSetListener {

    @BindView(R.id.orderdetail_cus)
    protected TextView txtName;
    @BindView(R.id.btn_choose_ship_warehouse)
    protected Spinner spiWarehouse;
    @BindView(R.id.orderdetail_phone)
    protected TextView txtPhone;
    @BindView(R.id.orderdetail_groupcus)
    protected TextView txtGroupCus;
    @BindView(R.id.btn_choose_ship_date)
    protected Button btnShipDate;
    @BindView(R.id.orderdetail_costpay)
    protected TextView txtCostPay;

    @BindView(R.id.lv_products)
    protected ListView lvProducts;

    @BindView(R.id.bottom_navigation)
    protected BottomNavigationView navigationView;

    Calendar myCalendar = Calendar.getInstance();
    ProductOfOrderAdapter adapter;
    private ArrayList<String> listWareHouse, listWareHouseValue;
    private int poswarehouse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_order_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.new_order_cast) + " --");
        ButterKnife.bind(this);

        loadCustomerData();
        configControlEvent();

        BasicService.currentChosenOrderProduct = new ArrayList<>();

        adapter = new ProductOfOrderAdapter(CreateOrderDetail.this, new ArrayList<ProductCheckForOrder>());
        lvProducts.setAdapter(adapter);

        adapter.setOnPriceChanged(new ProductOfOrderAdapter.OnPriceChanged() {
            @Override
            public void onPriceChanged() {
                getSupportActionBar().setTitle(getString(R.string.new_order_cast) + " " + adapter.getCurrentPrice());
                txtCostPay.setText(adapter.getCurrentPrice() + "");
            }
        });
    }

    private void loadWareHouse() {
        listWareHouse = new ArrayList<>();
        listWareHouseValue = new ArrayList<>();
        String first = getString(R.string.choosewarehouse);
        listWareHouse.add(first);
        listWareHouseValue.add("00");
        for (WareHouse wareHouse : RealmController.with(this).getWareHouse()) {
            listWareHouse.add(wareHouse.getWhsName());
            listWareHouseValue.add(wareHouse.getWhsCode());
        }
    }

    private void loadCustomerData() {
        RealmController.with(this).refresh();
        loadWareHouse();
        Customer customer = BasicService.currentCus;

        txtName.setText(customer.getCardName());
        txtPhone.setText(customer.getTel());
        txtGroupCus.setText(getGroupNameByCode(customer.getCustGrp()));

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                CreateOrderDetail.this, android.R.layout.simple_spinner_item, listWareHouse);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spiWarehouse.setAdapter(adapter);

        showLoading();
        String userCode = ApiService.getInstance(CreateOrderDetail.this).getUserCode();
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        String date = df.format(c.getTime());
        String custCode = BasicService.currentCus.getCardCode();
        ApiService.getInstance(CreateOrderDetail.this).GetPromotionProducts(custCode, userCode, date, new ApiService.GetPromotionProductsCallback() {
            @Override
            public void onSuccess(final List<PromotionProduct> data) {
                CreateOrderDetail.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        hideLoading();
                        BasicService.currentPromotionProduct = data;
                    }
                });
            }

            @Override
            public void onFail(String error) {
                CreateOrderDetail.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        hideLoading();
                        showErrorDialog(getString(R.string.promotion_get_fail));
                    }
                });
            }
        });
    }

    @OnItemSelected(R.id.btn_choose_ship_warehouse)
    void onSelectedWareHouse(int position) {
        poswarehouse = position;
    }

    @OnItemSelected(value = R.id.btn_choose_ship_warehouse, callback = OnItemSelected.Callback.NOTHING_SELECTED)
    void onNothingSelectedWarehouse() {
        poswarehouse = 0;
    }

    private String getGroupNameByCode(String code) {
        List<CustomerGroup> groups = RealmController.with(CreateOrderDetail.this).getAllCusGrp();

        for (CustomerGroup g : groups) {
            if (g.getGrpCode().equalsIgnoreCase(code)) {
                return g.getGrpName();
            }
        }

        return "";
    }

    @OnClick(R.id.btn_choose_ship_date)
    protected void onChooseShipDateClick() {
        new DatePickerDialog(CreateOrderDetail.this, CreateOrderDetail.this, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        myCalendar.set(Calendar.YEAR, year);
        myCalendar.set(Calendar.MONTH, month);
        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        //this.mStartDateByTM = myCalendar.getTimeInMillis();
        String myFormat = "MM/dd/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat);

        btnShipDate.setText(sdf.format(myCalendar.getTime()));
    }

    private void configControlEvent() {
        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_addproduct:
                        onAddProductClick();
                        break;
                    case R.id.action_sale:
                        onSaleClick();
                        break;
                    case R.id.action_save:
                        onSave();
                        break;
                }
                return false;
            }
        });
    }

    private void onAddProductClick() {
        Intent intent = new Intent(CreateOrderDetail.this, AddProductForOrder.class);
        intent.putExtra("custCode", BasicService.currentCus.getCardCode());
        startActivityForResult(intent, Constant.ADD_PRODUCT);
    }

    private void onSaleClick() {
        Intent intent = new Intent(CreateOrderDetail.this, Promotion.class);
        intent.putExtra("custCode", BasicService.currentCus.getCardCode());
        startActivityForResult(intent, ADD_PROMOTION);
    }

    private void onSave() {
        if (btnShipDate.getText().toString() == null || btnShipDate.getText().toString().trim().isEmpty()) {
            showErrorDialog(getString(R.string.add_order_product_missing_date));
            return;
        }

        if (adapter.getItems().size() == 0) {
            showErrorDialog(getString(R.string.add_order_product_missing_product));
            return;
        }

        final AlertDialog dialog = new AlertDialog.Builder(CreateOrderDetail.this)
                .setTitle(R.string.app_name)
                .setMessage(R.string.add_order_product_confirm_save)
                .setNegativeButton(R.string.button_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton(R.string.button_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        doSave();
                    }
                }).create();
        if (!dialog.isShowing()) {
            dialog.show();
        }
    }

    private void doSave() {
        SyncService syncService = SyncService.getInstance(CreateOrderDetail.this);
        Customer customer = BasicService.currentCus;
        String cardCode = customer.getCardCode();
        String cardName = customer.getCardName();
        String userCode = ApiService.getInstance(CreateOrderDetail.this).getUserCode();
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        String orderDate = df.format(c.getTime());

        for (ProductCheckForOrder p : adapter.getItems()) {
            String deliveryDate = btnShipDate.getText().toString();
            String itemCode = p.getItem().getItemCode();
            String itemName = p.getItem().getItemName();
            String itemType = isPromotionProduct(p) ? "1" : "0";
            String quantity = p.getQuantity() == null ? "0" : p.getQuantity();
            String uom = p.getItem().getBuyUoM();
            String price = p.getPrice();
            String vat = p.getVAT();
            syncService.offlineAddOrderProduct(cardCode, userCode, orderDate, deliveryDate, cardName, itemCode, itemName, itemType, quantity, uom, price, vat);
        }

        showLoading();
        syncService.sync(new ApiService.CommonCallback() {
            @Override
            public void onSuccess() {
                CreateOrderDetail.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        hideLoading();
                        CreateOrderDetail.this.finish();
                    }
                });
            }

            @Override
            public void onFail(String error) {
                CreateOrderDetail.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        hideLoading();
                        CreateOrderDetail.this.finish();
                    }
                });
            }
        });
    }

    private boolean isPromotionProduct(ProductCheckForOrder data) {
        List<PromotionProduct> rootData = BasicService.currentPromotionProduct;
        for (PromotionProduct p : rootData) {
            if (p.getItemCode().equalsIgnoreCase(data.getItem().getItemCode())) {
                return true;
            }
        }

        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constant.ADD_PRODUCT && resultCode == RESULT_OK) {
            adapter.updateData(BasicService.justAddedProductForOrder);

            getSupportActionBar().setTitle(getString(R.string.new_order_cast) + " " + adapter.getCurrentPrice());
            txtCostPay.setText(adapter.getCurrentPrice() + "");

            CommonUtils.setListViewHeightBasedOnChildrenCart(CreateOrderDetail.this, lvProducts);
        }

        if (requestCode == Constant.ADD_PROMOTION && resultCode == RESULT_OK) {
            adapter.updateData(BasicService.currentChosenOrderProduct);

            getSupportActionBar().setTitle(getString(R.string.new_order_cast) + " " + adapter.getCurrentPrice());
            txtCostPay.setText(adapter.getCurrentPrice() + "");

            CommonUtils.setListViewHeightBasedOnChildrenCart(CreateOrderDetail.this, lvProducts);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

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
