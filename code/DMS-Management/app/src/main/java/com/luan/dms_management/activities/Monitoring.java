package com.luan.dms_management.activities;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentTabHost;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TabHost;

import com.luan.dms_management.R;
import com.luan.dms_management.fragments.ListStaffFragment;
import com.luan.dms_management.fragments.MapFragment;
import com.luan.dms_management.models.StaffLocation;
import com.luan.dms_management.models.StaffMonitor;
import com.luan.dms_management.service.ApiService;
import com.luan.dms_management.service.BasicService;
import com.luan.dms_management.utils.BaseActivity;
import com.luan.dms_management.utils.CommonUtils;
import com.luan.dms_management.utils.PrefUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.luan.dms_management.utils.Constant.USERNAME_PREF_KEY;

public class Monitoring extends BaseActivity implements
        ListStaffFragment.OnFragmentInteractionListener,
        MapFragment.OnFragmentInteractionListener {

    @BindView(R.id.tabhost_monitoring)
    protected FragmentTabHost tabHost;
    @BindView(R.id.fab_refresh)
    protected FloatingActionButton floatingActionButton;

    private ApiService mService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monitoring);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.monitoring));
        ButterKnife.bind(this);
        mService = ApiService.getInstance(Monitoring.this);
        loadListStaffMonitor(false);
    }

    private void loadListStaffMonitor(final boolean isRequest) {
        showLoading();
        mService.loadListStaffMonitor(PrefUtils.getPreference(this, USERNAME_PREF_KEY), new ApiService.GetListStaffMonitor() {
            @Override
            public void onSuccess(final List<StaffMonitor> data) {
                Monitoring.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            BasicService.staffMonitorList = data;
                            loadListStaffLocation(isRequest);
                        } catch (Exception e) {
                            hideLoading();
                            showErrorDialog(e.getMessage());
                        }
                    }
                });
            }

            @Override
            public void onFail(final String error) {
                Monitoring.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        hideLoading();
                        if (error.equalsIgnoreCase("")) {
                            showErrorDialog(getString(R.string.load_list_stafflocation));
                        } else {
                            showErrorDialog(error);
                        }
                    }
                });
            }
        });
    }

    private void loadListStaffLocation(final boolean isRequest) {
        mService.loadListStaffLocation(PrefUtils.getPreference(this, USERNAME_PREF_KEY), new ApiService.GetListStaffLocation() {
            @Override
            public void onSuccess(final List<StaffLocation> data) {
                Monitoring.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            hideLoading();
                            BasicService.staffLocationList = data;
                            setupUI(isRequest);
                        } catch (Exception e) {
                            hideLoading();
                            showErrorDialog(e.getMessage());
                        }
                    }
                });
            }

            @Override
            public void onFail(final String error) {
                Monitoring.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        hideLoading();
                        if (error.equalsIgnoreCase("")) {
                            showErrorDialog(getString(R.string.load_list_stafflocation));
                        } else {
                            showErrorDialog(error);
                        }
                    }
                });
            }
        });
    }

    @OnClick(R.id.fab_refresh)
    protected void refresh() {
        loadListStaffMonitor(true);
    }

    private void setupUI(boolean isRequest) {
        if (!isRequest) {
            tabHost.setup(this, getSupportFragmentManager(), android.R.id.tabcontent);
            TabHost.TabSpec tabSpec = tabHost.newTabSpec("list");
            tabSpec.setIndicator(getString(R.string.list));

            TabHost.TabSpec tabSpec1 = tabHost.newTabSpec("map");
            tabSpec1.setIndicator(getString(R.string.map));

            tabHost.addTab(tabSpec, ListStaffFragment.class, null);
            tabHost.addTab(tabSpec1, MapFragment.class, null);
            tabHost.setCurrentTab(0);
        } else {
            int currentTab = tabHost.getCurrentTab();
            if (currentTab == 0) { // first tab is currently selected
                ListStaffFragment ni = (ListStaffFragment) getSupportFragmentManager().findFragmentByTag("list");
                ni.refresh();
            } else {
                MapFragment tnui = (MapFragment) getSupportFragmentManager().findFragmentByTag("map");
                tnui.configMap();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);

        MenuItem filterItem = menu.findItem(R.id.action_filter);
        filterItem.setVisible(false);
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

        if (id == R.id.action_filter) {
            CommonUtils.makeToast(this, "ok");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
