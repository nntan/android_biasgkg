package com.luan.dms_management.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.luan.dms_management.R;
import com.luan.dms_management.models.CustomerGroup;
import com.luan.dms_management.reaml.RealmController;
import com.luan.dms_management.service.ApiService;
import com.luan.dms_management.service.BasicService;
import com.luan.dms_management.utils.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;

public class AddNewCustomer extends BaseActivity {

    private final int PLACE_PICKER_REQUEST = 555;

    @BindView(R.id.spn_group)
    protected Spinner spnGroup;
    @BindView(R.id.mv_address)
    protected MapView mapView;
    @BindView(R.id.btn_address)
    protected Button btnAddress;
    @BindView(R.id.et_store)
    protected EditText etStore;
    @BindView(R.id.et_contact_person)
    protected EditText etPersonName;
    @BindView(R.id.et_phone)
    protected EditText etPhone;
    @BindView(R.id.et_remark)
    protected EditText etRemark;


    List<CustomerGroup> groupData;

    private Realm realm;
    private Double currentLat;
    private Double currentLon;
    private String currentGroup;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_customer);

        ButterKnife.bind(this);
        this.realm = RealmController.with(this).getRealm();
        loadData();

        mapView.onCreate(savedInstanceState);
        getCurrentLocationAndContinue();

        setupControlEvents();
    }

    private void loadData() {
        groupData = RealmController.with(AddNewCustomer.this).getAllCusGrp();

        List<String> stringData = new ArrayList<>();

        for (CustomerGroup g : groupData) {
            stringData.add(g.getGrpName());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter(AddNewCustomer.this, android.R.layout.simple_spinner_dropdown_item, stringData);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnGroup.setAdapter(adapter);

        currentGroup = groupData.get(0).getGrpCode();
    }

    private void setupControlEvents() {
        spnGroup.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                currentGroup = groupData.get(i).getGrpCode();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void configMap(final Double lat, final Double lon) {
        mapView.getMapAsync(new OnMapReadyCallback() {
            @SuppressLint("MissingPermission")
            @Override
            public void onMapReady(GoogleMap mapboxMap) {
                mapboxMap.setMyLocationEnabled(true);

                currentLat = lat;
                currentLon = lon;

                CameraPosition camPos = new CameraPosition.Builder()
                        .target(new LatLng(lat, lon))
                        .zoom(12.8f)
                        .build();

                CameraUpdate camUpdate = CameraUpdateFactory.newCameraPosition(camPos);

                mapboxMap.moveCamera(camUpdate);

                mapView.onResume();
            }
        });
    }

    @Override
    protected void afterGotCurrentLocation(double lat, double lon) {
        super.afterGotCurrentLocation(lat, lon);

        configMap(lat, lon);
    }

    @OnClick(R.id.btn_address)
    protected void addressClick() {
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        try {
            startActivityForResult(builder.build(AddNewCustomer.this), PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.btn_ok)
    protected void okClick() {
        String cardName = etStore.getText().toString();
        String contactPerson = etPersonName.getText().toString();
        String tel = etPhone.getText().toString();
        String lat = currentLat + "";
        String lon = currentLon + "";
        String group = currentGroup;
        String remark = etRemark.getText().toString();
        String address = btnAddress.getText().toString();

        if (cardName == null || cardName.trim().isEmpty()) {
            showErrorDialog(getString(R.string.add_customer_missing_card_name));
            return;
        }

        if (contactPerson == null || contactPerson.trim().isEmpty()) {
            showErrorDialog(getString(R.string.add_customer_missing_person_name));
            return;
        }

        if (tel == null || tel.trim().isEmpty()) {
            showErrorDialog(getString(R.string.add_customer_missing_tel));
            return;
        }

        if (address == null || address.trim().isEmpty()) {
            showErrorDialog(getString(R.string.add_customer_missing_address));
            return;
        }

        showLoading();
        ApiService.getInstance(AddNewCustomer.this).AddNewCustomer(cardName, contactPerson, tel, lat, lon, group, remark, address, new ApiService.AddCustCallback() {
            @Override
            public void onSuccess(final String data) {
                AddNewCustomer.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        hideLoading();
                        final AlertDialog dialog = new AlertDialog.Builder(AddNewCustomer.this)
                                .setTitle(R.string.app_name)
                                .setMessage(data)
                                .setPositiveButton(R.string.button_ok, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                        AddNewCustomer.this.finish();
                                    }
                                }).create();
                        if (!dialog.isShowing()) {
                            dialog.show();
                        }
                    }
                });
            }

            @Override
            public void onFail(String error) {
                AddNewCustomer.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        hideLoading();
                        showErrorDialog(getString(R.string.add_customer_fail));
                    }
                });
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                final Place place = PlacePicker.getPlace(data, this);
                btnAddress.setText(place.getAddress());

                mapView.getMapAsync(new OnMapReadyCallback() {
                    @SuppressLint("MissingPermission")
                    @Override
                    public void onMapReady(GoogleMap mapboxMap) {
                        mapboxMap.setMyLocationEnabled(true);

                        currentLat = place.getLatLng().latitude;
                        currentLon = place.getLatLng().longitude;

                        MarkerOptions markerOptions = new MarkerOptions()
                                .position(place.getLatLng());

                        mapboxMap.addMarker(markerOptions);

                        CameraPosition camPos = new CameraPosition.Builder()
                                .target(place.getLatLng())
                                .zoom(12.8f)
                                .build();

                        CameraUpdate camUpdate = CameraUpdateFactory.newCameraPosition(camPos);

                        mapboxMap.moveCamera(camUpdate);

                        mapView.onResume();
                    }
                });
            }
        }
    }
}
