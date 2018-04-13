package com.luan.dms_management.activities;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.luan.dms_management.R;
import com.luan.dms_management.adapter.PromotionProductAdapter;
import com.luan.dms_management.models.NormalProduct;
import com.luan.dms_management.models.Product;
import com.luan.dms_management.models.ProductCheckForOrder;
import com.luan.dms_management.models.PromotionProduct;
import com.luan.dms_management.reaml.RealmController;
import com.luan.dms_management.service.ApiService;
import com.luan.dms_management.service.BasicService;
import com.luan.dms_management.utils.BaseActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemClick;


public class Promotion extends BaseActivity {

    @BindView(R.id.lv_promotion_products)
    protected ListView mProducts;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promotion);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.promotion_title));

        ButterKnife.bind(this);
        RealmController.with(this).refresh();
        downloadData();
    }

    private void downloadData() {
        PromotionProductAdapter adapter = new PromotionProductAdapter(Promotion.this, BasicService.currentPromotionProduct);
        mProducts.setAdapter(adapter);
    }

    @OnItemClick(R.id.lv_promotion_products)
    protected void lvItemClick(View view, int position) {
        final PromotionProduct selected = BasicService.currentPromotionProduct.get(position);

        if (isCheckProduct(selected)) {
            showErrorDialog(getString(R.string.add_product_existed));
            return;
        }

        final AlertDialog dialog = new AlertDialog.Builder(Promotion.this)
                .setTitle(R.string.addproduct)
                .setMessage(R.string.add_product_confirm_message)
                .setNegativeButton(R.string.button_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton(R.string.button_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        processAddNewProduct(selected);
                        dialogInterface.dismiss();
                    }
                }).create();
        if (!dialog.isShowing()) {
            dialog.show();
        }
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

    private boolean isCheckProduct(PromotionProduct data) {
        List<ProductCheckForOrder> rootData = BasicService.currentChosenOrderProduct;
        for (ProductCheckForOrder p : rootData) {
            if (p.getItem().getItemCode().equalsIgnoreCase(data.getItemCode())) {
                return true;
            }
        }

        return false;
    }

    private void processAddNewProduct(final PromotionProduct product) {
        Product productChoose = RealmController.with(this).getProduct(product.getItemCode());

        final ProductCheckForOrder pro = new ProductCheckForOrder(productChoose.getNormalProduct(), true);
        String custCode = getIntent().getStringExtra("custCode");
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        String date = df.format(c.getTime());

        ApiService.getInstance(this).GetPriceOfProduct(custCode, pro.getItem().getItemCode(), pro.getItem().getBuyUoM(), date, new ApiService.GetPriceCallback() {
            @Override
            public void onSuccess(String price, String vat) {
                pro.setPrice(price);
                pro.setVAT(vat);
                addProductToList(pro);
            }

            @Override
            public void onFail(String error) {
                showErrorDialog(error);
            }
        });
    }

    private void addProductToList(ProductCheckForOrder checkForOrder) {
        BasicService.currentChosenOrderProduct.add(checkForOrder);
        Promotion.this.setResult(RESULT_OK);
        Promotion.this.finish();
    }
}
