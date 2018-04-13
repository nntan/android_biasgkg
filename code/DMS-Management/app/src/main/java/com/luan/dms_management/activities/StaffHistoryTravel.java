package com.luan.dms_management.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.luan.dms_management.R;
import com.luan.dms_management.adapter.StaffHisAdapter;
import com.luan.dms_management.models.Customer;
import com.luan.dms_management.utils.CommonUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StaffHistoryTravel extends AppCompatActivity {
    @BindView(R.id.lv_historyStaff)
    protected ListView listView;

    private StaffHisAdapter adapter;
    private ArrayList<Customer> arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_history_travel);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        ButterKnife.bind(this);
        loadData();
    }

    private void loadData() {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
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

}
