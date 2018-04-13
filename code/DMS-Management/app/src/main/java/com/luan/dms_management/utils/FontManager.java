package com.luan.dms_management.utils;

import android.content.Context;
import android.graphics.Typeface;

/**
 * Created by luan.nt on 3/2/2017.
 */

public class FontManager {
    public static final String ROOT = "fonts/",
            FONTAWESOME = "fontawesome-webfont.ttf";

    public static Typeface getTypeface(Context context, String font) {
        String a =  context.getApplicationContext().getAssets().toString();
        String b =  context.getAssets().toString();
        return Typeface.createFromAsset(context.getApplicationContext().getAssets(), font);
    }

}
