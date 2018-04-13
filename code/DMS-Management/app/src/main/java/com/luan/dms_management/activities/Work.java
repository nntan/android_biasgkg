package com.luan.dms_management.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import com.luan.dms_management.R;
import com.luan.dms_management.adapter.TimeKeepingAdapter;
import com.luan.dms_management.models.TimeSheet;
import com.luan.dms_management.service.ApiService;
import com.luan.dms_management.service.TimerWork;
import com.luan.dms_management.utils.BaseActivity;
import com.luan.dms_management.utils.CommonUtils;
import com.luan.dms_management.utils.PrefUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.luan.dms_management.utils.Constant.STARTEND_PREF_KEY;
import static com.luan.dms_management.utils.Constant.TIMEID_PREF_KEY;
import static com.luan.dms_management.utils.Constant.USERNAME_PREF_KEY;

public class Work extends BaseActivity {

    @BindView(R.id.work_CurrentDate)
    protected TextView workTime;
    @BindView(R.id.icStartEnd)
    protected TextView icStartEnd;
    @BindView(R.id.titleStartEnd)
    protected TextView titleStartEnd;
    @BindView(R.id.Timer)
    protected TextView timer;
    @BindView(R.id.lv_Timekeeping)
    protected ListView listView;

    private TimeKeepingAdapter adapter;
    private BroadcastReceiver myReceiver;
    private ApiService mService;
    private String sUsername, sDeviceID, sTimeID;
    private boolean isWorking = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.timekeeping));
        ButterKnife.bind(this);
        CommonUtils.makeTextViewFont(this, icStartEnd);
        mService = ApiService.getInstance(Work.this);
        loadTimeSheetLog();
        checkStartEnd();
    }

    private void checkStartEnd() {
        if (PrefUtils.getPreferenceBool(Work.this, STARTEND_PREF_KEY)) {
            icStartEnd.setText(getString(R.string.icend));
            titleStartEnd.setText(getString(R.string.End));
            isWorking = true;
        } else {
            icStartEnd.setText(getString(R.string.icstart));
            titleStartEnd.setText(getString(R.string.Start));
            isWorking = false;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter(TimerWork.MY_ACTION);
        registerReceiver(broadcastReceiver, intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.unregisterReceiver(broadcastReceiver);
    }

    private void loadData(List<TimeSheet> data) {
        workTime.setText(CommonUtils.getDate() + "/" + CommonUtils.getMonth() + "/" + CommonUtils.getYear());
        adapter = new TimeKeepingAdapter(this, data);
        listView.setAdapter(adapter);
        hideLoading();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.request, menu);
        return true;
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
        if (id == R.id.action_request) {
            Intent intent = new Intent(this, RestActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.startEndClick)
    protected void startEndClick() {
        if (!isWorking) {
            showDialogStart();
        } else {
            showDialogEnd();
        }
    }

    private void showDialogStart() {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        doStartTime();
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        dialog.dismiss();
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle(getString(R.string.punchin));
        builder.setMessage(getString(R.string.punchinmes))
                .setPositiveButton(getString(R.string.button_ok), dialogClickListener)
                .setNegativeButton(getString(R.string.button_cancel), dialogClickListener).show();
    }

    private void doStartWork() {
        icStartEnd.setText(getString(R.string.icend));
        titleStartEnd.setText(getString(R.string.End));
        isWorking = true;
        Intent intent = new Intent(this, TimerWork.class);
        startService(intent);
        PrefUtils.savePreferenceBool(Work.this, STARTEND_PREF_KEY, true);
        CommonUtils.makeToast(this,
                "Hello " + PrefUtils.getPreference(this, USERNAME_PREF_KEY) + ", " + getString(R.string.punchinsuccess));
    }

    private void showDialogEnd() {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        doEndTime();
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        dialog.dismiss();
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle(getString(R.string.punchout));
        builder.setMessage(getString(R.string.punchoutmes))
                .setPositiveButton(getString(R.string.button_ok), dialogClickListener)
                .setNegativeButton(getString(R.string.button_cancel), dialogClickListener).show();
    }

    private void doEndWork() {
        isWorking = false;
        icStartEnd.setText(getString(R.string.icstart));
        titleStartEnd.setText(getString(R.string.Start));
        timer.setText("00:00:00");
        Intent intent = new Intent(this, TimerWork.class);
        stopService(intent);
        PrefUtils.savePreferenceBool(Work.this, STARTEND_PREF_KEY, false);
        CommonUtils.makeToast(this, getString(R.string.punchoutsuccess));
    }

    private void doStartTime() {
        if (!CommonUtils.isInternetAvailable(Work.this)) {
            showErrorDialog(getString(R.string.missconnect));
            return;
        }
        hideLoading();
        showLoading(getString(R.string.punchin));
        sUsername = PrefUtils.getPreference(Work.this, USERNAME_PREF_KEY);
        sDeviceID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        mService.addTimeSheet(sUsername, sDeviceID, "", "", new ApiService.AddTimeSheetCallback() {
            @Override
            public void onSuccess(final String data) {
                Work.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            PrefUtils.savePreference(Work.this, TIMEID_PREF_KEY, data);
                            doStartWork();
                            loadTimeSheetLog();
                        } catch (Exception e) {
                            hideLoading();
                            showErrorDialog(e.getMessage());
                        }
                    }
                });
            }

            @Override
            public void onFail(final String error) {
                Work.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        hideLoading();
                        if (error.equalsIgnoreCase("")) {
                            showErrorDialog(getString(R.string.add_time_sheet));
                        } else {
                            showErrorDialog(error);
                        }
                    }
                });
            }
        });
    }

    private void doEndTime() {
        if (!CommonUtils.isInternetAvailable(Work.this)) {
            showErrorDialog(getString(R.string.missconnect));
            return;
        }
        hideLoading();
        showLoading(getString(R.string.punchout));
        sTimeID = PrefUtils.getPreference(Work.this, TIMEID_PREF_KEY);
        mService.updateTimeSheet(sTimeID, "", "", new ApiService.UpdateTimeSheetCallback() {
            @Override
            public void onSuccess(final String data) {
                Work.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            doEndWork();
                            loadTimeSheetLog();
                        } catch (Exception e) {
                            hideLoading();
                            showErrorDialog(e.getMessage());
                        }
                    }
                });
            }

            @Override
            public void onFail(final String error) {
                Work.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        hideLoading();
                        if (error.equalsIgnoreCase("")) {
                            showErrorDialog(getString(R.string.update_time_sheet));
                        } else {
                            showErrorDialog(error);
                        }
                    }
                });
            }
        });
    }

    private void loadTimeSheetLog() {
        if (!CommonUtils.isInternetAvailable(Work.this)) {
            showErrorDialog(getString(R.string.missconnect));
            return;
        }
        hideLoading();
        showLoading();
        sUsername = PrefUtils.getPreference(Work.this, USERNAME_PREF_KEY);
        mService.getTimeSheetLog(sUsername, new ApiService.LogTimeSheetCallback() {
            @Override
            public void onSuccess(final List<TimeSheet> data) {
                Work.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            loadData(data);
                        } catch (Exception e) {
                            hideLoading();
                            showErrorDialog(e.getMessage());
                        }
                    }
                });
            }

            @Override
            public void onFail(final String error) {
                Work.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        hideLoading();
                        if (error.equalsIgnoreCase("")) {
                            showErrorDialog(getString(R.string.load_time_sheet));
                        } else {
                            showErrorDialog(error);
                        }
                    }
                });
            }
        });
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int time = intent.getIntExtra("MESSAGE", 0) * 1000;
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
            sdf.setTimeZone(TimeZone.getTimeZone("GMT+0"));
            String sTime = sdf.format(new Date(time));
            timer.setText(sTime);
        }
    };
}
