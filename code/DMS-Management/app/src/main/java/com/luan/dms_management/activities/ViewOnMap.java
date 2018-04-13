package com.luan.dms_management.activities;

import android.Manifest;
import android.app.Dialog;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;

import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.DirectionsApi;
import com.google.maps.GeoApiContext;
import com.google.maps.android.PolyUtil;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.TravelMode;
import com.luan.dms_management.R;
import com.luan.dms_management.models.Customer;
import com.luan.dms_management.models.StaffLocation;
import com.luan.dms_management.service.BasicService;
import com.luan.dms_management.service.ParserTask;
import com.luan.dms_management.utils.BaseActivity;
import com.luan.dms_management.utils.PrefUtils;

import org.joda.time.DateTime;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.luan.dms_management.utils.Constant.LAT_PREF_KEY;
import static com.luan.dms_management.utils.Constant.LONG_PREF_KEY;

public class ViewOnMap extends BaseActivity {
    public static final String PRESENT_MODE_KEY = "PRESENT_MODE_KEY";
    public static final String PRESENT_MODE_STAFF = "PRESENT_MODE_STAFF";
    public static final String PRESENT_MODE_STORE = "PRESENT_MODE_STORE";

    public enum PRESENT_MODE {
        STAFF,
        STORE
    }

    private LatLng position, currentLocation;
    private PRESENT_MODE presentMode;
    private ParserTask parserTask;

    @BindView(R.id.viewLocaitonStaff)
    protected MapView mapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewon_map);

        String mode = getIntent().getStringExtra(PRESENT_MODE_KEY);
        if (mode.equalsIgnoreCase(PRESENT_MODE_STAFF)) {
            presentMode = PRESENT_MODE.STAFF;
        } else {
            presentMode = PRESENT_MODE.STORE;
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (presentMode == PRESENT_MODE.STAFF) {
            getSupportActionBar().setTitle(BasicService.currentStaff.getEmpFullName());
        } else {
            getSupportActionBar().setTitle(BasicService.currentCus.getCardName());
        }

        ButterKnife.bind(this);
        mapView.onCreate(savedInstanceState);
        configMap();
    }

    private GeoApiContext getGeoContext() {
        GeoApiContext geoApiContext = new GeoApiContext();
        return geoApiContext
                .setQueryRateLimit(3)
                .setApiKey(getString(R.string.directionsApiKey))
                .setConnectTimeout(1, TimeUnit.SECONDS)
                .setReadTimeout(1, TimeUnit.SECONDS)
                .setWriteTimeout(1, TimeUnit.SECONDS);
    }

    private void configMap() {
        if (presentMode == PRESENT_MODE.STAFF) {
            position = new LatLng(Double.parseDouble(BasicService.currentStaff.getCurrLat()),
                    Double.parseDouble(BasicService.currentStaff.getCurrLong()));
            mapView.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap mapboxMap) {
                    if (ActivityCompat.checkSelfPermission(ViewOnMap.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                            && ActivityCompat.checkSelfPermission(ViewOnMap.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    mapboxMap.setMyLocationEnabled(true);
                    MarkerOptions markerOptions = new MarkerOptions()
                            .position(position);

                    mapboxMap.addMarker(markerOptions);

                    CameraPosition camera = new CameraPosition.Builder()
                            .target(position)// Sets the new camera position
                            .zoom(12) // Sets the zoom
                            .build(); // Creates a CameraPosition from the builder

                    mapboxMap.animateCamera(CameraUpdateFactory
                            .newCameraPosition(camera));

                    mapboxMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                        @Override
                        public boolean onMarkerClick(@NonNull Marker marker) {
                            if (presentMode == PRESENT_MODE.STAFF) {
                                showDialog(BasicService.currentStaff);
                            } else {
                                showDialog(BasicService.currentCus);
                            }

                            return true;
                        }
                    });

                    mapView.onResume();
                }
            });

        } else {
            position = new LatLng(Double.parseDouble(BasicService.currentCus.getLatitudeValue()),
                    Double.parseDouble(BasicService.currentCus.getLongitudeValue()));

            currentLocation = new LatLng(Double.parseDouble(PrefUtils.getPreference(this, LAT_PREF_KEY)),
                    Double.parseDouble(PrefUtils.getPreference(this, LONG_PREF_KEY)));

            mapView.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(final GoogleMap mapboxMap) {
                    if (ActivityCompat.checkSelfPermission(ViewOnMap.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                            && ActivityCompat.checkSelfPermission(ViewOnMap.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    String url = getDirectionsUrl(currentLocation, position);
                    DownloadTask downloadTask = new DownloadTask();
                    downloadTask.execute(url);

                    downloadTask.callback = new DownCallback() {
                        @Override
                        public void Success(String result) {
                            runParse(result, mapboxMap);
                        }
                    };
                    mapboxMap.setMyLocationEnabled(true);
                    MarkerOptions markerOptions = new MarkerOptions()
                            .position(position);

                    mapboxMap.addMarker(markerOptions);

                    CameraPosition camera = new CameraPosition.Builder()
                            .target(position)// Sets the new camera position
                            .zoom(10) // Sets the zoom
                            .build(); // Creates a CameraPosition from the builder

                    mapboxMap.animateCamera(CameraUpdateFactory
                            .newCameraPosition(camera));

                    mapboxMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                        @Override
                        public boolean onMarkerClick(@NonNull Marker marker) {
                            if (presentMode == PRESENT_MODE.STAFF) {
                                showDialog(BasicService.currentStaff);
                            } else {
                                showDialog(BasicService.currentCus);
                            }

                            return true;
                        }
                    });

                    mapView.onResume();
                }
            });
        }
    }

    private void showDialog(StaffLocation staffLocation) {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_location);
        final ImageView avatar = (ImageView) dialog.findViewById(R.id.avatarMarkerStaff);
        final TextView name = (TextView) dialog.findViewById(R.id.nameMarkerStaff);
        final TextView phone = (TextView) dialog.findViewById(R.id.phoneMarkerStaff);
        final TextView checkinPlan = (TextView) dialog.findViewById(R.id.checkinPlanMarkerStaff);
        final TextView checkinFinish = (TextView) dialog.findViewById(R.id.checkinFinishMarkerStaff);
        final TextView total = (TextView) dialog.findViewById(R.id.totalOrderMarkerStaff);

        name.setText(staffLocation.getEmpFullName());
        //phone.setText(getString(R.string.phone) + ": ");
        phone.setText("-");
        checkinPlan.setText(staffLocation.getCheckInPlan() + "");
        checkinFinish.setText(staffLocation.getCheckInFinish() + "");
        total.setText(staffLocation.getNoOrder() + "");

        Glide.with(this).load(staffLocation.getPicLink())
                .crossFade()
                .thumbnail(0.5f)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(avatar);

        dialog.show();
    }

    private void showDialog(Customer customer) {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_store_location);
        final TextView name = (TextView) dialog.findViewById(R.id.store_name);
        final TextView phone = (TextView) dialog.findViewById(R.id.store_phone);
        final TextView address = (TextView) dialog.findViewById(R.id.store_address);
        final TextView code = (TextView) dialog.findViewById(R.id.store_code);

        name.setText(customer.getCardName());
        phone.setText(customer.getTel());
        address.setText(customer.getAddress());
        code.setText(customer.getCardCode());

        dialog.show();
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

    private String getDirectionsUrl(LatLng origin, LatLng destination) {

        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        String waypoint = "waypoints=";
        String dest = "destination=" + destination.latitude + "," + destination.longitude;
        ;

        // Sensor enabled
        String sensor = "sensor=true";

        // Building the parameters to the web service
        String parameters = str_origin + "&" + waypoint + "&" + dest + "&" + sensor;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;

        return url;
    }

    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        } catch (Exception e) {
            e.toString();
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }
    // Fetches data from url passed
    private class DownloadTask extends AsyncTask<String, Void, String> {

        DownCallback callback;
        // Downloading data in non-ui thread
        @Override
        protected String doInBackground(String... url) {
            String data = "";

            try {
                // Fetching the data from web service
                data = downloadUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        // Executes in UI thread, after the execution of
        // doInBackground()
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            callback.Success(result);
        }
    }

    private void runParse(String parse, final GoogleMap mMap) {
        parserTask = new ParserTask();
        parserTask.setCallback(new ParserTask.TaskCallBack() {
            @Override
            public void successCallBack(PolylineOptions polylineOptions) {
                mMap.addPolyline(polylineOptions);
            }
        });
        parserTask.execute(parse);
    }

    public interface DownCallback{
        void Success(String result);
    }

}
