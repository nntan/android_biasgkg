package com.luan.dms_management.activities;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ListView;

import com.luan.dms_management.R;
import com.luan.dms_management.adapter.LeaveLogAdapter;
import com.luan.dms_management.models.LeaveLog;
import com.luan.dms_management.models.LeaveType;
import com.luan.dms_management.service.ApiService;
import com.luan.dms_management.utils.BaseActivity;
import com.luan.dms_management.utils.CommonUtils;
import com.luan.dms_management.utils.PrefUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.luan.dms_management.utils.Constant.USERNAME_PREF_KEY;

public class LeaveLogActivity extends BaseActivity {

    @BindView(R.id.lv_leave_log)
    protected RecyclerView recyclerView;

    private ApiService mService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave_log);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.leavelog));
        ButterKnife.bind(this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        mService = ApiService.getInstance(this);
        downloadData();
    }

    private void downloadData() {
        if (!CommonUtils.isInternetAvailable(LeaveLogActivity.this)) {
            showErrorDialog(getString(R.string.missconnect));
            return;
        }

        hideLoading();
        showLoading();
        String sUserid = PrefUtils.getPreference(LeaveLogActivity.this, USERNAME_PREF_KEY);
        mService.getLeaveLog(sUserid, new ApiService.GetLeaveLogCallback() {
            @Override
            public void onSuccess(final List<LeaveLog> data) {
                LeaveLogActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            initData(data);
                        } catch (Exception e) {
                            Log.e("LogSuccess", e.toString());
                            hideLoading();
                            showErrorDialog(e.getMessage());
                        }
                    }
                });
            }

            @Override
            public void onFail(final String error) {
                LeaveLogActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        hideLoading();
                        if (error.equalsIgnoreCase("")) {
                            showErrorDialog(getString(R.string.load_leave_log));
                        } else {
                            showErrorDialog(error);
                        }
                    }
                });
            }
        });
    }

    private void initData(final List<LeaveLog> data) {
        hideLoading();
        LeaveLogAdapter adapter = new LeaveLogAdapter(this, data, new LeaveLogAdapter.ApproveCallback() {
            @Override
            public void onApprove(final LeaveLog leaveLog) {
                appoveLeave(leaveLog);
            }
        });
        recyclerView.setAdapter(adapter);
    }

    private void appoveLeave(final LeaveLog leaveLog) {
        if (!CommonUtils.isInternetAvailable(LeaveLogActivity.this)) {
            showErrorDialog(getString(R.string.missconnect));
            return;
        }
        hideLoading();
        showLoading();
        mService.approveLeave(leaveLog.getLeaveId(), new ApiService.ApproveLeaveCallback() {
            @Override
            public void onSuccess(String data) {
                LeaveLogActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            downloadData();
                        } catch (Exception e) {
                            hideLoading();
                            showErrorDialog(e.getMessage());
                        }
                    }
                });
            }

            @Override
            public void onFail(final String error) {
                LeaveLogActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        hideLoading();
                        if (error.equalsIgnoreCase("")) {
                            showErrorDialog(getString(R.string.approve_fail));
                        } else {
                            showErrorDialog(error);
                        }
                    }
                });
            }
        });
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

        return super.onOptionsItemSelected(item);
    }
}
