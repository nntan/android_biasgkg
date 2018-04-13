package com.luan.dms_management.activities;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.luan.dms_management.R;
import com.luan.dms_management.fragments.HomeFragment;
import com.luan.dms_management.utils.CommonUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements
        HomeFragment.OnFragmentInteractionListener{
    @BindView(R.id.tool_bar)
    protected Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        loadHomeFragment();
    }

    private void loadHomeFragment() {
        HomeFragment homeFragment = new HomeFragment();
        CommonUtils.replaceFragment(this, homeFragment);
    }


    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
