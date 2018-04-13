package com.luan.dms_management.activities;

import android.app.Application;
import android.content.res.Configuration;
import android.util.Log;

import com.luan.dms_management.R;
import com.luan.dms_management.utils.CommonUtils;
import com.luan.dms_management.utils.PrefUtils;

import java.util.Locale;

import io.realm.Realm;
import io.realm.RealmConfiguration;

import static com.luan.dms_management.utils.Constant.LANGUAGE_PREF_KEY;

/**
 * Created by luan.nt on 8/17/2017.
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder().name("dmssale.realm").build();
        Realm.setDefaultConfiguration(config);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setLocale();
    }

    private void setLocale() {
        String lang = PrefUtils.getPreference(this, LANGUAGE_PREF_KEY);
        if(lang==null) lang="vi";
        Locale locale = new Locale(lang);

        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());
    }
}
