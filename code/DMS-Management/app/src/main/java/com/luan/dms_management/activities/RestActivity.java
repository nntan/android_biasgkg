package com.luan.dms_management.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.luan.dms_management.R;
import com.luan.dms_management.models.LeaveReason;
import com.luan.dms_management.models.LeaveType;
import com.luan.dms_management.service.ApiService;
import com.luan.dms_management.utils.BaseActivity;
import com.luan.dms_management.utils.CommonUtils;
import com.luan.dms_management.utils.PrefUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemSelected;

import static com.luan.dms_management.utils.Constant.USERNAME_PREF_KEY;

public class RestActivity extends BaseActivity {

    @BindView(R.id.rest_type)
    protected Spinner leaveType;
    @BindView(R.id.rest_fromdate)
    protected EditText fromDate;
    @BindView(R.id.rest_todate)
    protected EditText toDate;
    @BindView(R.id.rest_reason)
    protected Spinner leaveReason;
    @BindView(R.id.rest_note)
    protected EditText leaveNote;
    @BindView(R.id.rest_cancel)
    protected Button cancel;
    @BindView(R.id.rest_send)
    protected Button send;
    @BindView(R.id.restView)
    protected LinearLayout view;

    private int year, month, day, postype, posreason;
    private String sType, sFromDate, sToDate, sReason, sNote, sUserid, sDeviceid;
    private Calendar calendar;
    private ApiService mService;
    private ArrayList<String> listLeaveType, listLeaveReason, listLeaveTypeValue, listLeaveReasonValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rest);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.rest));
        ButterKnife.bind(this);
        CommonUtils.makeButtonFont(this, cancel);
        CommonUtils.makeButtonFont(this, send);
        CommonUtils.setupUI(view, this);
        mService = ApiService.getInstance(RestActivity.this);
        downloadData();
        initDateData();
    }

    private void initDateData() {
        fromDate.setKeyListener(null);
        toDate.setKeyListener(null);
        cancel.setText(getString(R.string.ictrash) + " " + getString(R.string.restcancel));
        send.setText(getString(R.string.icsend) + " " + getString(R.string.restsend));

        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        showFromDate(year, month, day);
        showToDate(year, month, day);
        onFocusDatePicker();
    }

    private void showDialogFromDatePicker() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        showFromDate(year, monthOfYear, dayOfMonth);
                    }
                }, year, month, day);
        datePickerDialog.show();
    }

    private void showDialogToDatePicker() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        showToDate(year, monthOfYear, dayOfMonth);
                    }
                }, year, month, day);
        datePickerDialog.show();
    }

    private void onFocusDatePicker() {
        fromDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    showDialogFromDatePicker();
                }
            }
        });

        toDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    showDialogToDatePicker();
                }
            }
        });
    }

    @OnClick(R.id.rest_fromdate)
    protected void onClickFromDatePicker() {
        showDialogFromDatePicker();
    }

    @OnClick(R.id.rest_todate)
    protected void onClickToDatePicker() {
        showDialogToDatePicker();
    }

    @OnItemSelected(R.id.rest_type)
    void onSelectedType(int position) {
        postype = position;
    }

    @OnItemSelected(value = R.id.rest_type, callback = OnItemSelected.Callback.NOTHING_SELECTED)
    void onNothingSelectedType() {
        postype = 0;
    }

    @OnItemSelected(R.id.rest_reason)
    void onSelectedReason(int position) {
        posreason = position;
    }

    @OnItemSelected(value = R.id.rest_reason, callback = OnItemSelected.Callback.NOTHING_SELECTED)
    void onNothingSelectedReason() {
        posreason = 0;
    }

    @OnClick(R.id.rest_cancel)
    protected void onClickCancel() {
        finish();
    }

    @OnClick(R.id.rest_send)
    protected void onSendClickListener() {
        sType = listLeaveTypeValue.get(postype);
        sFromDate = fromDate.getText().toString();
        sToDate = toDate.getText().toString();
        sReason = listLeaveReasonValue.get(posreason);
        sNote = leaveNote.getText().toString();

        if (!CommonUtils.isInternetAvailable(this)) {
            CommonUtils.makeToast(this, getString(R.string.missconnect));
            return;
        }

        if (posreason == 0 || postype == 0 || TextUtils.isEmpty(sReason) || TextUtils.isEmpty(sType)) {
            CommonUtils.makeToast(this, getString(R.string.missfield));
            return;
        }
        doSendClick();

        CommonUtils.makeToast(this, getString(R.string.holiaysuccess));
    }

    private void doSendClick() {
        if (!CommonUtils.isInternetAvailable(RestActivity.this)) {
            showErrorDialog(getString(R.string.missconnect));
            return;
        }

        hideLoading();
        showLoading();
        sUserid = PrefUtils.getPreference(RestActivity.this, USERNAME_PREF_KEY);
        sDeviceid = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        sType = listLeaveType.get(postype);
        sReason = listLeaveReason.get(posreason);
        mService.addLeave(sUserid, sDeviceid, sFromDate, sToDate, sType, sReason, sNote, new ApiService.AddLeaveCallback() {
            @Override
            public void onSuccess(final String data) {
                RestActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            hideLoading();
                            CommonUtils.makeToast(RestActivity.this, getString(R.string.holiaysuccess));
                            finish();
                        } catch (Exception e) {
                            hideLoading();
                            showErrorDialog(e.getMessage());
                        }
                    }
                });
            }

            @Override
            public void onFail(final String error) {
                RestActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        hideLoading();
                        if (error.equalsIgnoreCase("")) {
                            showErrorDialog(getString(R.string.holidayfail));
                        } else {
                            showErrorDialog(error);
                        }
                    }
                });
            }
        });
    }

    private void showFromDate(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);

        String myFormat = "yyyy/MM/dd";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat);
        fromDate.setText(sdf.format(calendar.getTime()));
    }

    private void showToDate(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);

        String myFormat = "yyyy/MM/dd";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat);
        toDate.setText(sdf.format(calendar.getTime()));
    }

    private void downloadData() {
        if (!CommonUtils.isInternetAvailable(RestActivity.this)) {
            showErrorDialog(getString(R.string.missconnect));
            return;
        }

        hideLoading();
        showLoading();
        mService.getLeaveType(new ApiService.GetLeaveTypeCallback() {
            @Override
            public void onSuccess(final List<LeaveType> data) {
                RestActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            initLeaveType(data);
                        } catch (Exception e) {
                            hideLoading();
                            showErrorDialog(e.getMessage());
                        }
                    }
                });
            }

            @Override
            public void onFail(final String error) {
                RestActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        hideLoading();
                        if (error.equalsIgnoreCase("")) {
                            showErrorDialog(getString(R.string.load_leave_type));
                        } else {
                            showErrorDialog(error);
                        }
                    }
                });
            }
        });
    }

    private void initLeaveType(List<LeaveType> data) {
        listLeaveType = new ArrayList<>();
        listLeaveTypeValue = new ArrayList<>();
        String first = getString(R.string.chooseleavetype);
        listLeaveType.add("0");
        listLeaveTypeValue.add(first);
        for (LeaveType leaveType : data) {
            listLeaveType.add(leaveType.getLcode());
            listLeaveTypeValue.add(leaveType.getLname());
        }
        downloadLeaveReason();
    }

    private void downloadLeaveReason() {
        mService.getLeaveReason(new ApiService.GetLeaveReasonCallback() {
            @Override
            public void onSuccess(final List<LeaveReason> data) {
                RestActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            initLeaveReason(data);
                            hideLoading();
                        } catch (Exception e) {
                            hideLoading();
                            showErrorDialog(e.getMessage());
                        }
                    }
                });
            }

            @Override
            public void onFail(final String error) {
                RestActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        hideLoading();
                        if (error.equalsIgnoreCase("")) {
                            showErrorDialog(getString(R.string.load_leave_reason));
                        } else {
                            showErrorDialog(error);
                        }
                    }
                });
            }
        });
    }

    private void initLeaveReason(List<LeaveReason> data) {
        listLeaveReason = new ArrayList<>();
        listLeaveReasonValue = new ArrayList<>();
        String first = getString(R.string.chooseleavereason);
        listLeaveReason.add("0");
        listLeaveReasonValue.add(first);
        for (LeaveReason leaveReason : data) {
            listLeaveReason.add(leaveReason.getLcode());
            listLeaveReasonValue.add(leaveReason.getLname());
        }
        initSpinnerData();
    }

    private void initSpinnerData() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                RestActivity.this, android.R.layout.simple_spinner_item, listLeaveReasonValue);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        leaveReason.setAdapter(adapter);

        adapter = new ArrayAdapter<>(
                RestActivity.this, android.R.layout.simple_spinner_item, listLeaveTypeValue);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        leaveType.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.approve, menu);
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

        if (id == R.id.action_approve) {
            Intent intent = new Intent(RestActivity.this, LeaveLogActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
