package com.luan.dms_management.activities;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.luan.dms_management.R;
import com.luan.dms_management.fragments.ImageFragment;
import com.luan.dms_management.fragments.InventoryFragment;
import com.luan.dms_management.fragments.LocationFragment;
import com.luan.dms_management.models.NormalNoteType;
import com.luan.dms_management.models.NoteType;
import com.luan.dms_management.reaml.RealmController;
import com.luan.dms_management.service.ApiService;
import com.luan.dms_management.service.BasicService;
import com.luan.dms_management.service.SyncService;
import com.luan.dms_management.service.TimerCheckin;
import com.luan.dms_management.utils.BaseActivity;
import com.luan.dms_management.utils.CommonUtils;
import com.luan.dms_management.utils.PrefUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.luan.dms_management.utils.Constant.CUSTOMER_PREF_KEY;

public class Checkin extends BaseActivity implements
        InventoryFragment.OnFragmentInteractionListener,
        ImageFragment.OnFragmentInteractionListener,
        LocationFragment.OnFragmentInteractionListener {

    @BindView(R.id.checkin_name)
    protected TextView txtName;
    @BindView(R.id.checkin_contact)
    protected TextView txtContact;
    @BindView(R.id.icon_checkin_address)
    protected TextView icAddress;
    @BindView(R.id.checkin_address)
    protected TextView txtAddress;
    @BindView(R.id.icon_checkin_phone)
    protected TextView icPhone;
    @BindView(R.id.checkin_phone)
    protected TextView txtPhone;
    @BindView(R.id.tabhost_table)
    protected FragmentTabHost tabHost;
    @BindView(R.id.bottom_navigation)
    protected BottomNavigationView bottomNavigationView;

    private boolean visited;
    private Timer timer;
    private int checkInTimeCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkin);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setTitle(getString(R.string.checkintime) + " " + countToTime(checkInTimeCount));
        ButterKnife.bind(this);

        CommonUtils.makeTextViewFont(this, icAddress);
        CommonUtils.makeTextViewFont(this, icPhone);

        visited = getIntent().getBooleanExtra("visited", false);
        setService();
        setupUI();
        loadData();
        bottomNavigationEvent();
        setupTimer();
    }

    private void setService() {
//        if (!visited) {
//            Intent intent = new Intent(this, TimerCheckin.class);
//            startService(intent);
//        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter(TimerCheckin.MY_ACTION);
        registerReceiver(broadcastReceiver, intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.unregisterReceiver(broadcastReceiver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        timer.cancel();
        timer.purge();
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int time = intent.getIntExtra("MESSAGE", 0);
            int hour = time / 3600;
            String sHour = (hour > 9) ? String.valueOf(hour) : "0" + hour;
            int minute = time / 60 - (hour * 60);
            String sMinute = (minute > 9) ? String.valueOf(minute) : "0" + minute;
            int second = time - hour * 3600 - minute * 60;
            String sSecond = (second > 9) ? String.valueOf(second) : "0" + second;
            getSupportActionBar().setTitle(getString(R.string.visittime) + " " + sHour + ":" + sMinute + ":" + sSecond);
        }
    };

    private void bottomNavigationEvent() {
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_addproduct:
                        requestOrder();
                        break;
                    case R.id.action_note:
                        requestUpdateNote();
                        break;
                    case R.id.action_checkout:
                        requestCheckout();
                        break;
                    }
                    return false;
                }
            });
    }

    private void requestOrder() {
        //PrefUtils.savePreference(context, CUSTOMER_PREF_KEY, customer.getCardCode());
        //BasicService.currentCus = customer;
        Intent intent = new Intent(Checkin.this, CreateOrderDetail.class);
        startActivity(intent);
    }

    private void requestCheckout() {
        final AlertDialog dialog = new AlertDialog.Builder(Checkin.this)
                .setTitle(R.string.app_name)
                .setMessage(R.string.checkin_checkout)
                .setPositiveButton(R.string.button_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        doCheckout();
                        dialogInterface.dismiss();
                    }
                }).create();
        if (!dialog.isShowing()) {
            dialog.show();
        }
    }

    private void doCheckout() {
        Log.d("doCheckout1","doCheckout");
        String custCode = BasicService.currentCus.getCardCode();
        String userCode = ApiService.getInstance(Checkin.this).getUserCode();
        SyncService.getInstance(Checkin.this).offlineCheckout(custCode, userCode);
        showLoading();
        SyncService.getInstance(Checkin.this).sync(new ApiService.CommonCallback() {
            @Override
            public void onSuccess() {
                Checkin.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        hideLoading();
                        Checkin.this.finish();
                    }
                });
            }

            @Override
            public void onFail(String error) {
                Checkin.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        hideLoading();
                        Checkin.this.finish();
                    }
                });
            }
        });
    }

    private void loadData() {
            //BasicService.getInstance().currentCus = RealmController.with(this).getCustomer(PrefUtils.getPreference(this, CUSTOMER_PREF_KEY));
            txtName.setText(BasicService.currentCus.getCardName());
            txtAddress.setText(BasicService.currentCus.getAddress());
            txtContact.setText(getString(R.string.contactPer) + ": " + BasicService.currentCus.getContactPerson());
            txtPhone.setText(BasicService.currentCus.getTel());
            }

    private void setupUI() {
            tabHost.setup(this, getSupportFragmentManager(), android.R.id.tabcontent);
            TabHost.TabSpec tabSpec = tabHost.newTabSpec("inventory");
            tabSpec.setIndicator(getString(R.string.inventory));

            TabHost.TabSpec tabSpec1 = tabHost.newTabSpec("image");
            tabSpec1.setIndicator(getString(R.string.image));

            TabHost.TabSpec tabSpec2 = tabHost.newTabSpec("location");
            tabSpec2.setIndicator(getString(R.string.location));

            tabHost.addTab(tabSpec, InventoryFragment.class, null);
            tabHost.addTab(tabSpec1, ImageFragment.class, null);
            tabHost.addTab(tabSpec2, LocationFragment.class, null);
            tabHost.setCurrentTab(0);
    }

    private void setupTimer() {
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        getSupportActionBar().setTitle(getString(R.string.checkintime) + " " + countToTime(checkInTimeCount));
                        checkInTimeCount++;
                    }
                });
            }
        }, 1000, 1000);
    }

    private String countToTime(int count) {
        int h = count / 3600;
        int m = count / 60;
        int s = count % 60;
        return trueTimeFormat(h) + ":" + trueTimeFormat(m) + ":" + trueTimeFormat(s);
    }

    private String trueTimeFormat(int value) {
        if (value < 10) {
            return "0" + value;
        }

        return value + "";
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

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    private void requestUpdateNote() {
        List<NoteType> data = RealmController.with(this).getAllNoteType();
        openDialogWith(data);
    }

    private void openDialogWith(final List<NoteType> data) {
        final Dialog dialog = new Dialog(Checkin.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_update_note);

        ImageButton close = (ImageButton) dialog.findViewById(R.id.ib_close);
        Button clear = (Button) dialog.findViewById(R.id.btn_clear);
        Button ok = (Button) dialog.findViewById(R.id.btn_ok);
        final EditText content = (EditText) dialog.findViewById(R.id.et_note_content);
        final Spinner typeSpinner = (Spinner) dialog.findViewById(R.id.spn_note_types);

        List<String> types = new ArrayList<>();
        for (NoteType type : data) {
            types.add(type.getNotesGroupName());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter(Checkin.this, android.R.layout.simple_spinner_dropdown_item, types);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setAdapter(adapter);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                content.setText("");
            }
        });

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String custCode = BasicService.currentCus.getCardCode();
                Log.d("tan_memo: ",""+custCode);
                String userCode = ApiService.getInstance(Checkin.this).getUserCode();
                Log.d("tan_memo: ",""+userCode);
                String remarks = content.getText().toString();
                SyncService.getInstance(Checkin.this).offlineCheckinAddNote(custCode, userCode, data.get(typeSpinner.getSelectedItemPosition()).getNotesGroup(), remarks);
                dialog.dismiss();
            }
        });

        dialog.show();
    }
}
