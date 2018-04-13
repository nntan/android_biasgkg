package com.luan.dms_management.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by luan.nt on 8/29/2017.
 */

public class PrefUtils {
    private static final String PREFS_NAME = "PREFS_NAME";

    public static void savePreferenceBool(Context context, String key, boolean data) {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
        editor.putBoolean(key, data);
        editor.commit();
    }

    public static boolean getPreferenceBool(Context context, String key) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        boolean restoredText = prefs.getBoolean(key, false);
        return restoredText;
    }

    public static void savePreference(Context context, String key, String data) {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
        editor.putString(key, data);
        Log.e("tan_save",""+data);
        editor.commit();
    }

    public static String getPreference(Context context, String key) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String restoredText = prefs.getString(key, null);
        Log.e("tan_checkInID",""+restoredText);
        return restoredText;
    }
}
