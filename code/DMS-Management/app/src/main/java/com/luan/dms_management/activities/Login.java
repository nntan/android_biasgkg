package com.luan.dms_management.activities;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.luan.dms_management.R;
import com.luan.dms_management.models.Channel;
import com.luan.dms_management.models.Customer;
import com.luan.dms_management.models.CustomerGroup;
import com.luan.dms_management.models.CustomerTravel;
import com.luan.dms_management.models.NormalNoteType;
import com.luan.dms_management.models.NoteType;
import com.luan.dms_management.models.Orders;
import com.luan.dms_management.models.Product;
import com.luan.dms_management.models.Route;
import com.luan.dms_management.models.Staff;
import com.luan.dms_management.models.StatusOrder;
import com.luan.dms_management.models.TypeOrder;
import com.luan.dms_management.models.TypeVisit;
import com.luan.dms_management.models.WareHouse;
import com.luan.dms_management.reaml.RealmController;
import com.luan.dms_management.service.ApiService;
import com.luan.dms_management.utils.BaseActivity;
import com.luan.dms_management.utils.CommonUtils;
import com.luan.dms_management.utils.PermissionUtils;
import com.luan.dms_management.utils.PrefUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;

import static com.luan.dms_management.utils.CommonUtils.getCurrentdate;
import static com.luan.dms_management.utils.Constant.LANGUAGE_PREF_KEY;
import static com.luan.dms_management.utils.Constant.PASSWORD_PREF_KEY;
import static com.luan.dms_management.utils.Constant.SALEID_PREF_KEY;
import static com.luan.dms_management.utils.Constant.USERNAME_PREF_KEY;
import static com.luan.dms_management.utils.Constant.WORKING_PREF_KEY;
import static java.security.AccessController.getContext;

public class Login extends BaseActivity {
    @BindView(R.id.edtEmail)
    protected EditText edtUsername;
    @BindView(R.id.edtPass)
    protected EditText edtPass;
    @BindView(R.id.btnLogin)
    protected Button btnLogin;
    @BindView(R.id.login_layout)
    protected LinearLayout view;
    @BindView(R.id.txtLanguage)
    protected TextView txtLanguage;
    @BindView(R.id.ictriangle)
    protected TextView ictriangle;
    @BindView(R.id.icconfig)
    protected TextView icconfig;
    @BindView(R.id.copyright)
    protected TextView copyright;

    private String sUsername, sPass, sWorkingDate, sSalemaId;
    private ApiService mService;
    private Realm realm;
    private boolean isEng = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        autoLogin();
        PrefUtils.savePreference(this, LANGUAGE_PREF_KEY, "vi");
        CommonUtils.setLocale(this);
        ButterKnife.bind(this);
        CommonUtils.setupUI(view, this);
        CommonUtils.makeTextViewFont(this, ictriangle);
        CommonUtils.makeTextViewFont(this, icconfig);
        CommonUtils.makeTextViewFont(this, copyright);
    }

    @OnClick(R.id.icconfig)
    protected void btnconfig(View view) {
        showDialogAPI();
    }


    @OnClick(R.id.btnLogin)
    protected void btnLogin(View view) {
        validateInput();
    }

    private void validateInput() {
        sUsername = edtUsername.getText().toString();
        sPass = edtPass.getText().toString();

        if (sUsername.equals("") || sPass.equals("")) {
            CommonUtils.makeToast(this, getString(R.string.missingfield));
            return;
        }

        doLogin();
    }

    @OnClick(R.id.changeLang)
    protected void changeLanguage(View view) {
        showDialogLanguage();
    }

    private void showDialogAPI() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_config);
        final EditText api = (EditText) dialog.findViewById(R.id.edtAPIlink);
        final Button ok = (Button) dialog.findViewById(R.id.linkapi_ok);
        api.setText(mService.getURL());

        dialog.show();
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mService.saveNewURL(Login.this, api.getText().toString());
                dialog.dismiss();
            }
        });
    }

    private void showDialogLanguage() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_choose_lang);
        final RadioGroup group = (RadioGroup) dialog.findViewById(R.id.choose_lang);
        final Button ok = (Button) dialog.findViewById(R.id.choose_lang_ok);
        final RadioButton eng = (RadioButton) dialog.findViewById(R.id.lang_eng);
        final RadioButton vi = (RadioButton) dialog.findViewById(R.id.lang_vi);
        if (isEng) {
            eng.setChecked(true);
        } else {
            vi.setChecked(true);
        }

        dialog.show();
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedId = group.getCheckedRadioButtonId();
                RadioButton gender = (RadioButton) dialog.findViewById(selectedId);
                dialog.dismiss();
                txtLanguage.setText(gender.getText().toString());
                String sLang = gender.getText().toString();
                if (sLang.equals("Tiếng Anh/English")) {
                    isEng = true;
                } else {
                    isEng = false;
                }
            }
        });
    }

    private void autoLogin() {
        if (PrefUtils.getPreference(Login.this, USERNAME_PREF_KEY) != null) {
            if (!PrefUtils.getPreference(Login.this, USERNAME_PREF_KEY).equalsIgnoreCase("")) {
                mService = ApiService.getInstance(Login.this);
                mService.setUserCode(PrefUtils.getPreference(Login.this, USERNAME_PREF_KEY));
                Intent intent = new Intent(Login.this, MainActivity.class);
                startActivity(intent);
                finish();
            } else {
                mService = ApiService.getInstance(Login.this);
                this.realm = RealmController.with(this).getRealm();
                RealmController.with(this).clearAll();
                setPermission();
            }
        } else {
            mService = ApiService.getInstance(Login.this);
            this.realm = RealmController.with(this).getRealm();
            RealmController.with(this).clearAll();
            setPermission();
        }
    }

    private void doLogin() {
        sUsername = edtUsername.getText().toString();
        sPass = edtPass.getText().toString();

        showLoading();

        String deviceID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        Log.e("deviceID", deviceID + "");

        mService.login(sUsername, sPass, deviceID, new ApiService.LoginCallBack() {

            @Override
            public void onSuccess(String datetime, String salemanid) {
                PrefUtils.savePreference(Login.this, WORKING_PREF_KEY, datetime);
                PrefUtils.savePreference(Login.this, SALEID_PREF_KEY, salemanid);
                Login.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        syncData();
                    }
                });
            }

            @Override
            public void onFail(final String error) {
                Login.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        hideLoading();
                        if ("".equalsIgnoreCase(error)) {
                            showErrorDialog(getString(R.string.login_fail_message));
                        } else {
                            showErrorDialog(error);
                        }
                    }
                });
            }
        });
    }


    private void syncData() {
        realm.beginTransaction();
        realm.deleteAll();
        realm.commitTransaction();
        loadListProduct();
    }

    private void loadListStaff() {
        mService.loadListStaff(sUsername, new ApiService.GetListStaff() {
            @Override
            public void onSuccess(final List<Staff> data) {
                Login.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            realm.beginTransaction();
                            realm.copyToRealm(data);
                            realm.commitTransaction();
                            loadListProduct();
                        } catch (Exception e) {
                            hideLoading();
                            showErrorDialog(e.getMessage());
                        }
                    }
                });
            }

            @Override
            public void onFail(final String error) {
                Login.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        hideLoading();
                        if (error.equalsIgnoreCase("")) {
                            showErrorDialog(getString(R.string.load_list_staff));
                        } else {
                            showErrorDialog(error);
                        }
                    }
                });
            }
        });
    }

    private void loadListProduct() {
        hideLoading();
        showLoading(getString(R.string.load_products_message));
        mService.loadListProduct(sUsername, "", new ApiService.GetListProduct() {
            @Override
            public void onSuccess(final List<Product> data) {
                Login.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            realm.beginTransaction();
                            realm.copyToRealm(data);
                            realm.commitTransaction();
                            loadListCustomer();
                        } catch (Exception e) {
                            hideLoading();
                            showErrorDialog(e.getMessage());
                        }
                    }
                });
            }

            @Override
            public void onFail(final String error) {
                Login.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        hideLoading();
                        if (error.equalsIgnoreCase("")) {
                            showErrorDialog(getString(R.string.load_list_product));
                        } else {
                            showErrorDialog(error);
                        }
                    }
                });
            }
        });
    }

    private void loadListCustomer() {
        hideLoading();
        showLoading(getString(R.string.load_customers_message));
        int saleid = Integer.parseInt(PrefUtils.getPreference(this, SALEID_PREF_KEY));
        String workdate = PrefUtils.getPreference(this, WORKING_PREF_KEY);
        mService.loadListCustomer(sUsername, "00", "00", "00", saleid, "", workdate, new ApiService.GetListCustomer() {
            @Override
            public void onSuccess(final List<Customer> data) {
                Login.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            realm.beginTransaction();
                            realm.copyToRealm(data);
                            realm.commitTransaction();
                            loadListChannel();
                        } catch (Exception e) {
                            hideLoading();
                            showErrorDialog(e.getMessage());
                        }
                    }
                });
            }

            @Override
            public void onFail(final String error) {
                Login.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        hideLoading();
                        if (error.equalsIgnoreCase("")) {
                            showErrorDialog(getString(R.string.load_list_cus));
                        } else {
                            showErrorDialog(error);
                        }
                    }
                });
            }
        });
    }

    private void loadListChannel() {
        mService.loadListChannel(sUsername, new ApiService.GetListChannel() {
            @Override
            public void onSuccess(final List<Channel> data) {
                Login.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            realm.beginTransaction();
                            realm.copyToRealm(data);
                            realm.commitTransaction();
                            loadListGrpCus();
                        } catch (Exception e) {
                            hideLoading();
                            showErrorDialog(e.getMessage());
                        }
                    }
                });
            }

            @Override
            public void onFail(final String error) {
                Login.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        hideLoading();
                        if (error.equalsIgnoreCase("")) {
                            showErrorDialog(getString(R.string.load_list_cus));
                        } else {
                            showErrorDialog(error);
                        }
                    }
                });
            }
        });
    }

    private void loadListGrpCus() {
        mService.loadListCusGrp(sUsername, new ApiService.GetListCusGrp() {
            @Override
            public void onSuccess(final List<CustomerGroup> data) {
                Login.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            realm.beginTransaction();
                            realm.copyToRealm(data);
                            realm.commitTransaction();
                            loadListOrder();
                        } catch (Exception e) {
                            hideLoading();
                            showErrorDialog(e.getMessage());
                        }
                    }
                });
            }

            @Override
            public void onFail(final String error) {
                Login.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        hideLoading();
                        if (error.equalsIgnoreCase("")) {
                            showErrorDialog(getString(R.string.load_list_cus));
                        } else {
                            showErrorDialog(error);
                        }
                    }
                });
            }
        });
    }

    private void loadListCustomerTravel() {
        hideLoading();
        showLoading(getString(R.string.load_customers_message));
        mService.loadListCustomerTravel(sUsername, new ApiService.GetListCustomerTravel() {
            @Override
            public void onSuccess(final List<Customer> data) {
                Login.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            realm.beginTransaction();
                            realm.copyToRealm(data);
                            realm.commitTransaction();
                            loadListChannel();
                        } catch (Exception e) {
                            hideLoading();
                            showErrorDialog(e.getMessage());
                        }
                    }
                });
            }

            @Override
            public void onFail(final String error) {
                Login.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        hideLoading();
                        if (error.equalsIgnoreCase("")) {
                            showErrorDialog(getString(R.string.load_list_cus));
                        } else {
                            showErrorDialog(error);
                        }
                    }
                });
            }
        });
    }

    private void loadListRoute() {
        hideLoading();
        showLoading(getString(R.string.load_route_message));
        mService.loadListRoute(sUsername, new ApiService.GetListRoute() {
            @Override
            public void onSuccess(final List<Route> data) {
                Login.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            realm.beginTransaction();
                            realm.copyToRealm(data);
                            realm.commitTransaction();
                            loadListNotesGroup();
                        } catch (Exception e) {
                            hideLoading();
                            showErrorDialog(e.getMessage());
                        }
                    }
                });
            }

            @Override
            public void onFail(final String error) {
                Login.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        hideLoading();
                        if (error.equalsIgnoreCase("")) {
                            showErrorDialog(getString(R.string.load_list_cus));
                        } else {
                            showErrorDialog(error);
                        }
                    }
                });
            }
        });
    }

    private void loadListNotesGroup() {
        mService.loadNoteTypes(sUsername, new ApiService.GetListNoteType() {
            @Override
            public void onSuccess(final List<NoteType> data) {
                Login.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            realm.beginTransaction();
                            realm.copyToRealm(data);
                            realm.commitTransaction();
                            doAfterLoginCompleted();
                        } catch (Exception e) {
                            hideLoading();
                            showErrorDialog(e.getMessage());
                        }
                    }
                });
            }

            @Override
            public void onFail(final String error) {
                Login.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        hideLoading();
                        if (error.equalsIgnoreCase("")) {
                            showErrorDialog(getString(R.string.load_list_note_group));
                        } else {
                            showErrorDialog(error);
                        }
                    }
                });
            }
        });
    }

    private void loadListOrder() {
        hideLoading();
        showLoading(getString(R.string.load_order_message));
        mService.loadListOrder(sUsername, "2016/01/01", CommonUtils.getCurrentdate(), "01", "01", "01", "01", "", new ApiService.GetListOrder() {
            @Override
            public void onSuccess(final List<Orders> data) {
                Login.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            realm.beginTransaction();
                            realm.copyToRealm(data);
                            realm.commitTransaction();
                            loadListTypeVisit();
                        } catch (Exception e) {
                            hideLoading();
                            showErrorDialog(e.getMessage());
                        }
                    }
                });
            }

            @Override
            public void onFail(final String error) {
                Login.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        hideLoading();
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

    private void loadListTypeVisit() {
        mService.loadListTypeVisit(sUsername, new ApiService.GetListTypeVisit() {
            @Override
            public void onSuccess(final List<TypeVisit> data) {
                Login.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            realm.beginTransaction();
                            realm.copyToRealm(data);
                            realm.commitTransaction();
                            loadListTypeOrder();
                        } catch (Exception e) {
                            hideLoading();
                            showErrorDialog(e.getMessage());
                        }
                    }
                });
            }

            @Override
            public void onFail(final String error) {
                Login.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        hideLoading();
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

    private void loadListTypeOrder() {
        mService.loadListTypeOrder(sUsername, new ApiService.GetListTypeOrder() {
            @Override
            public void onSuccess(final List<TypeOrder> data) {
                Login.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            realm.beginTransaction();
                            realm.copyToRealm(data);
                            realm.commitTransaction();
                            loadListStatusOrder();
                        } catch (Exception e) {
                            hideLoading();
                            showErrorDialog(e.getMessage());
                        }
                    }
                });
            }

            @Override
            public void onFail(final String error) {
                Login.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        hideLoading();
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

    private void loadListStatusOrder() {
        mService.loadListStatusOrder(sUsername, new ApiService.GetListStatusOrder() {
            @Override
            public void onSuccess(final List<StatusOrder> data) {
                Login.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            realm.beginTransaction();
                            realm.copyToRealm(data);
                            realm.commitTransaction();
                            loadListWareHouse();
                        } catch (Exception e) {
                            hideLoading();
                            showErrorDialog(e.getMessage());
                        }
                    }
                });
            }

            @Override
            public void onFail(final String error) {
                Login.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        hideLoading();
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

    private void loadListWareHouse() {
        mService.loadListWareHouse(sUsername, new ApiService.GetListWareHouse() {
            @Override
            public void onSuccess(final List<WareHouse> data) {
                Login.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            realm.beginTransaction();
                            realm.copyToRealm(data);
                            realm.commitTransaction();
                            loadListRoute();
                        } catch (Exception e) {
                            hideLoading();
                            showErrorDialog(e.getMessage());
                        }
                    }
                });
            }

            @Override
            public void onFail(final String error) {
                Login.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        hideLoading();
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

    void doAfterLoginCompleted() {
        hideLoading();
        PrefUtils.savePreference(Login.this, USERNAME_PREF_KEY, sUsername);
        PrefUtils.savePreference(Login.this, PASSWORD_PREF_KEY, sPass);
        if (isEng) {
            PrefUtils.savePreference(this, LANGUAGE_PREF_KEY, "en");
        } else {
            PrefUtils.savePreference(this, LANGUAGE_PREF_KEY, "vi");
        }

        CommonUtils.setLocale(this);

        Intent intent = new Intent(Login.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void setPermission() {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String[] neededPermission = PermissionUtils.getMissingPermission(Login.this);
            if (neededPermission.length > 0) {
                pleaseSetPermission();
            }
        }
    }

    void pleaseSetPermission() {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        PermissionUtils.checkPermission(Login.this);
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
        builder.setCancelable(false);
        builder.setMessage(getString(R.string.setpermission))
                .setPositiveButton(getString(R.string.button_ok), dialogClickListener)
                .setNegativeButton(getString(R.string.button_cancel), dialogClickListener).show();
    }
}
