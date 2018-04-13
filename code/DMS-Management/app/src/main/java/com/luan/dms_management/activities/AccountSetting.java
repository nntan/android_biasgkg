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

import static com.luan.dms_management.utils.Constant.LANGUAGE_PREF_KEY;
import static com.luan.dms_management.utils.Constant.PASSWORD_PREF_KEY;
import static com.luan.dms_management.utils.Constant.SALEID_PREF_KEY;
import static com.luan.dms_management.utils.Constant.USERNAME_PREF_KEY;
import static com.luan.dms_management.utils.Constant.WORKING_PREF_KEY;

public class AccountSetting extends BaseActivity {
    @BindView(R.id.login_layout)
    protected LinearLayout view;
    @BindView(R.id.edtOldPass)
    protected EditText edtOldPass;
    @BindView(R.id.edtNewPass)
    protected EditText edtNewPass;
    @BindView(R.id.edtRePass)
    protected EditText edtRePass;

    private ApiService mService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_setting);

        mService = ApiService.getInstance(AccountSetting.this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setTitle(R.string.account_setting_change_pass);

        CommonUtils.setLocale(this);
        ButterKnife.bind(this);
        CommonUtils.setupUI(view, this);
    }

    @OnClick(R.id.btnChange)
    protected void onRequestChangePass(View view) {
        String newPass = edtNewPass.getText().toString();
        String rePass = edtRePass.getText().toString();
        String oldPass = edtOldPass.getText().toString();

        if (oldPass.trim().isEmpty()) {
            String title = getString(R.string.button_error);
            String messa = getString(R.string.account_setting_change_pass_old_pass_empty);
            CommonUtils.showDialog(AccountSetting.this, title, messa);
            return;
        }

        if (!newPass.equalsIgnoreCase(rePass)) {
            String title = getString(R.string.button_error);
            String messa = getString(R.string.account_setting_change_pass_pass_not_match);
            CommonUtils.showDialog(AccountSetting.this, title, messa);
            return;
        }

        if (newPass.trim().isEmpty()) {
            String title = getString(R.string.button_error);
            String messa = getString(R.string.account_setting_change_pass_pass_empty);
            CommonUtils.showDialog(AccountSetting.this, title, messa);
            return;
        }

        String sUsername = mService.getUserCode();
        String deviceID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        showLoading();
        mService.changePassword(sUsername, oldPass, newPass, deviceID, new ApiService.CommonCallback() {
            @Override
            public void onSuccess() {
                AccountSetting.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        hideLoading();
                        String title = getString(R.string.button_error);
                        String messa = getString(R.string.account_setting_change_pass_completed);
                        CommonUtils.showDialog(AccountSetting.this, title, messa);

                        edtNewPass.setText("");
                        edtRePass.setText("");
                        edtOldPass.setText("");

                        return;
                    }
                });
            }

            @Override
            public void onFail(final String error) {
                AccountSetting.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        hideLoading();
                        String title = getString(R.string.button_error);
                        CommonUtils.showDialog(AccountSetting.this, title, error);
                        return;
                    }
                });
            }
        });
    }
}
