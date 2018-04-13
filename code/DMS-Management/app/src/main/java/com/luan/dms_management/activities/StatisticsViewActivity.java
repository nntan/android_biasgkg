package com.luan.dms_management.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.luan.dms_management.R;
import com.luan.dms_management.adapter.StatisticAdapter;
import com.luan.dms_management.models.Statistic;
import com.luan.dms_management.service.ApiService;
import com.luan.dms_management.utils.BaseActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StatisticsViewActivity extends BaseActivity {

    @BindView(R.id.rv_statistics)
    protected RecyclerView mRecyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);
        ButterKnife.bind(this);

        String name = getIntent().getStringExtra("name");
        getSupportActionBar().setTitle(name);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        loadData();
    }

    private void loadData() {
        String userCode = ApiService.getInstance(StatisticsViewActivity.this).getUserCode();
        String custCode = getIntent().getStringExtra("customer");

        showLoading();
        ApiService.getInstance(StatisticsViewActivity.this).getStatistics(userCode, custCode, new ApiService.GetStatisticsCallback() {
            @Override
            public void onSuccess(final List<Statistic> data) {
                StatisticsViewActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        hideLoading();
                        StatisticAdapter adapter = new StatisticAdapter(StatisticsViewActivity.this, data);
                        mRecyclerView.setAdapter(adapter);
                    }
                });
            }

            @Override
            public void onFail(final String error) {
                StatisticsViewActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        hideLoading();
                        //showErrorDialog(getString(R.string.get_statistics_fail));
                        showErrorDialog(error);
                    }
                });
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.normal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == android.R.id.home) {
            // finish the activity
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
