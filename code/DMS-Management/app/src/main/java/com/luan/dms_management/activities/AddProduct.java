package com.luan.dms_management.activities;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.location.places.AddPlaceRequest;
import com.luan.dms_management.R;
import com.luan.dms_management.adapter.AddProductAdapter;
import com.luan.dms_management.models.NormalProduct;
import com.luan.dms_management.models.Product;
import com.luan.dms_management.models.ProductCheck;
import com.luan.dms_management.reaml.RealmController;
import com.luan.dms_management.service.ApiService;
import com.luan.dms_management.service.BasicService;
import com.luan.dms_management.utils.BaseActivity;
import com.luan.dms_management.utils.CommonUtils;
import com.luan.dms_management.utils.Constant;
import com.luan.dms_management.utils.PrefUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;
import butterknife.OnTextChanged;
import io.realm.Realm;

import static com.luan.dms_management.utils.Constant.USERNAME_PREF_KEY;

public class AddProduct extends BaseActivity {

    @BindView(R.id.lv_addproduct)
    protected ListView listView;
    @BindView(R.id.fab)
    protected FloatingActionButton fab;
    @BindView(R.id.ic_search_product)
    protected TextView icsearch;
    @BindView(R.id.inputSearchProduct)
    protected EditText edtSearch;

    private AddProductAdapter adapter;
    private ArrayList<View> ViewItem;
    private Realm realm;
    private String mode;

    private List<Product> mAllProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.addproduct));
        ButterKnife.bind(this);
        mode = getIntent().getStringExtra("ModeType");
        CommonUtils.makeTextViewFont(this, icsearch);
        edtSearch.setImeOptions(EditorInfo.IME_ACTION_DONE);
        this.realm = RealmController.with(this).getRealm();
        BasicService.proChooseList = new ArrayList<>();
        RealmController.with(this).refresh();
        loadData(RealmController.with(this).getAllProduct());
    }

    @OnClick(R.id.fab)
    protected void fabClick() {
        for (ProductCheck item : BasicService.productChecks) {
            if (item.isCheck()) {
                Product pro = item.getItem();
                BasicService.proChooseList.add(pro);
            }
        }
        setResult(Constant.ADD_PRODUCT);
        finish();
    }

    @OnTextChanged(R.id.inputSearchProduct)
    protected void search() {
        searchProduct(edtSearch.getText().toString());
    }

    @OnItemClick(R.id.lv_addproduct)
    protected void lvItemClick(View view, int position) {
        final Product selected = mAllProduct.get(position);

        for (NormalProduct p : BasicService.currentProductList) {
            if (p.getItemCode().equalsIgnoreCase(selected.getNormalProduct().getItemCode())) {
                showErrorDialog(getString(R.string.add_product_existed));
                return;
            }
        }

        final AlertDialog dialog = new AlertDialog.Builder(AddProduct.this)
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

    private void processAddNewProduct(Product selected) {
        BasicService.justAddedProduct = selected;
        AddProduct.this.setResult(RESULT_OK);
        AddProduct.this.finish();
    }

    private void searchProduct(String search) {
        loadData(RealmController.with(this).findProduct(search));
    }

    private void loadData(List<Product> productList) {
        mAllProduct = productList;

        ViewItem = new ArrayList<>();
        BasicService.productChecks = new ArrayList<>();
        boolean ischeck;
        for (Product item : productList) {
            ischeck = false;
            for (int i = 0; i < BasicService.proList.size(); i++) {
                if (BasicService.proList.get(i).getCodeBars().equalsIgnoreCase(item.getCodeBars())) {
                    ischeck = true;
                    break;
                }
            }
            ProductCheck pro = new ProductCheck(item, ischeck);
            BasicService.productChecks.add(pro);
        }

        adapter = new AddProductAdapter(this, BasicService.productChecks);
        listView.setAdapter(adapter);
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
