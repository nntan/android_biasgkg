package com.luan.dms_management.activities;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.luan.dms_management.R;
import com.luan.dms_management.adapter.AddProductAdapter;
import com.luan.dms_management.adapter.AddProductForOrderAdapter;
import com.luan.dms_management.models.NormalProduct;
import com.luan.dms_management.models.Product;
import com.luan.dms_management.models.ProductCheck;
import com.luan.dms_management.models.ProductCheckForOrder;
import com.luan.dms_management.reaml.RealmController;
import com.luan.dms_management.service.ApiService;
import com.luan.dms_management.service.BasicService;
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
import butterknife.OnItemClick;
import butterknife.OnTextChanged;
import io.realm.Realm;

public class AddProductForOrder extends BaseActivity {

    @BindView(R.id.lv_addproduct)
    protected ListView listView;
    @BindView(R.id.ic_search_product)
    protected TextView icsearch;
    @BindView(R.id.inputSearchProduct)
    protected EditText edtSearch;

    private AddProductForOrderAdapter adapter;
    private ArrayList<View> ViewItem;
    private Realm realm;
    private String mode;

    private List<Product> mAllProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product_for_order);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.addproduct));
        ButterKnife.bind(this);
        CommonUtils.makeTextViewFont(this, icsearch);
        edtSearch.setImeOptions(EditorInfo.IME_ACTION_DONE);
        this.realm = RealmController.with(this).getRealm();
        BasicService.proChooseList = new ArrayList<>();
        RealmController.with(this).refresh();
        loadData(RealmController.with(this).getAllProduct());
    }

    @OnTextChanged(R.id.inputSearchProduct)
    protected void search() {
        searchProduct(edtSearch.getText().toString());
    }

    @OnItemClick(R.id.lv_addproduct)
    protected void lvItemClick(View view, int position) {
        adapter.toggleCheckFor(position);
    }

    @OnClick(R.id.btn_ok)
    protected void onConfirmClick() {
        List<ProductCheckForOrder> data = adapter.getCheckedItem();
        BasicService.justAddedProductForOrder = data;
        AddProductForOrder.this.setResult(RESULT_OK);
        AddProductForOrder.this.finish();
    }

    private void processAddNewProduct(Product selected) {
        BasicService.justAddedProduct = selected;
    }

    private void searchProduct(String search) {
        loadData(RealmController.with(this).findProduct(search));
    }

    private void loadData(List<Product> productList) {
        showLoading();
        mAllProduct = productList;

        ViewItem = new ArrayList<>();
        List<ProductCheckForOrder> productChecks = new ArrayList<>();
        for (Product item : productList) {
            ProductCheckForOrder pro = new ProductCheckForOrder(item.getNormalProduct(), false);
            productChecks.add(pro);
        }

        getPriceForProductAndBindToAdapter(productChecks);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void getPriceForProductAndBindToAdapter(List<ProductCheckForOrder> data) {
        List<ProductCheckForOrder> wowProductChecks = new ArrayList<>();

        getPriceForProducts(data, wowProductChecks, new GetPricesCallback() {
            @Override
            public void onSuccess(final List<ProductCheckForOrder> data) {
                AddProductForOrder.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        hideLoading();
                        for (ProductCheckForOrder p : data) {
                            if (isChecked(p)) {
                                p.setCheck(true);
                            }
                        }

                        adapter = new AddProductForOrderAdapter(AddProductForOrder.this, data);
                        listView.setAdapter(adapter);
                    }
                });
            }

            @Override
            public void onFail() {
                AddProductForOrder.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        hideLoading();
                    }
                });
                //TODO
            }
        });
    }

    private boolean isChecked(ProductCheckForOrder data) {
        List<ProductCheckForOrder> rootData = BasicService.currentChosenOrderProduct;

        for (ProductCheckForOrder p : rootData) {
            if (p.getItem().getItemCode().equalsIgnoreCase(data.getItem().getItemCode())) {
                return true;
            }
        }

        return false;
    }

    private interface GetPricesCallback {
        void onSuccess(List<ProductCheckForOrder> data);
        void onFail();
    }

    private void getPriceForProducts(final List<ProductCheckForOrder> rootData, final List<ProductCheckForOrder> returnValue, final GetPricesCallback callback) {
        if (rootData.size() == 0) {
            callback.onSuccess(returnValue);
            return;
        }

        String custCode = getIntent().getStringExtra("custCode");
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        String date = df.format(c.getTime());

        final ProductCheckForOrder product = rootData.get(0);
        ApiService.getInstance(AddProductForOrder.this).GetPriceOfProduct(custCode, product.getItem().getItemCode(), product.getItem().getBuyUoM(), date, new ApiService.GetPriceCallback() {
            @Override
            public void onSuccess(String price, String vat) {
                product.setPrice(price);
                product.setVAT(vat);
                returnValue.add(product);
                rootData.remove(0);
                getPriceForProducts(rootData, returnValue, callback);
            }

            @Override
            public void onFail(String error) {
                callback.onFail();
            }
        });
    }
}
