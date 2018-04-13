package com.luan.dms_management.utils;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import com.luan.dms_management.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.internal.Utils;

import static com.luan.dms_management.utils.Constant.LAT_PREF_KEY;
import static com.luan.dms_management.utils.Constant.LONG_PREF_KEY;

/**
 * Created by luan.nt on 8/29/2017.
 */

public class BaseActivity extends AppCompatActivity {
    private int retryCount = 0;
    public static final int PERMISSIONS_LOCATION_REQUEST = 8760;

    @Override
    protected void onResume() {
        super.onResume();
        CommonUtils.hideKeyboard(BaseActivity.this);
    }

    ProgressDialog mProgressDialog;

    protected void showLoading() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(BaseActivity.this);
            mProgressDialog.setCancelable(false);
            mProgressDialog.setMessage(getString(R.string.progress_bar_message));
        } else {
            mProgressDialog.setMessage(getString(R.string.progress_bar_message));
        }


        mProgressDialog.show();
    }

    protected void showLoading(String message) {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(BaseActivity.this);
            mProgressDialog.setCancelable(false);
            mProgressDialog.setMessage(message);
        } else {
            mProgressDialog.setMessage(message);
        }

        mProgressDialog.show();
    }

    protected void hideLoading() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
        }
    }

    protected void showErrorDialog(String message) {
        CommonUtils.showErrorDialog(BaseActivity.this, message);
    }

    protected void showSuccessDialog(String message) {
        CommonUtils.showSuccessDialog(BaseActivity.this, message);
    }

    protected void showDialog(String title, String message) {
        CommonUtils.showDialog(BaseActivity.this, title, message);
    }

    protected void getCurrentLocationAndContinue() {
        checkLocationPermission();
    }

    private void checkLocationPermission() {
        List<String> permissions = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(BaseActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
        } else {
            doGetCurrentLocation();
            return;
        }

        if (ContextCompat.checkSelfPermission(BaseActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        } else {
            doGetCurrentLocation();
            return;
        }

        String[] perArr = new String[permissions.size()];
        perArr = permissions.toArray(perArr);
        ActivityCompat.requestPermissions(BaseActivity.this,
                perArr,
                PERMISSIONS_LOCATION_REQUEST);
    }

    private void doGetCurrentLocation() {
        if (retryCount > 5) {
            afterGotCurrentLocation(0, 0);
            return;
        }
        retryCount++;

        TrackerGPS mTrackerGPS = new TrackerGPS(BaseActivity.this);
        if (mTrackerGPS.canGetLocation()) {

            double longitude = mTrackerGPS.getLongitude();
            double latitude = mTrackerGPS.getLatitude();

            if (longitude == 0 && latitude == 0) {
                doGetCurrentLocation();
                return;
            }

            afterGotCurrentLocation(latitude, longitude);
        } else {
            mTrackerGPS.showSettingsAlert();
        }
    }

    protected void afterGotCurrentLocation(double lat, double lon) {
        PrefUtils.savePreference(this, LAT_PREF_KEY, lat + "");
        PrefUtils.savePreference(this, LONG_PREF_KEY, lon + "");
    }

    protected void afterGetCurrentLocationFail() {
        //TODO
    }
}
