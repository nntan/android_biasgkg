package com.luan.dms_management.utils;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.luan.dms_management.R;
import com.luan.dms_management.activities.Login;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.luan.dms_management.utils.Constant.LANGUAGE_PREF_KEY;
import static com.luan.dms_management.utils.Constant.USERNAME_PREF_KEY;

/**
 * Created by luan.nt on 7/24/2017.
 */

public class CommonUtils {
    private static Toast mToast;

    public static boolean isEmailValid(String email) {
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN =
                "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                        + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static boolean isInternetAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo i = connectivityManager.getActiveNetworkInfo();
        if (i == null)
            return false;
        if (!i.isConnected())
            return false;
        if (!i.isAvailable())
            return false;
        return true;
    }

    public static final String MD5(final String s) {
        final String MD5 = "MD5";
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest
                    .getInstance(MD5);
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(0xFF & aMessageDigest);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static void hideSoftKeyboard(Activity activity, View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        try {
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void hideKeyboard(@NonNull Activity activity) {
        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    public static String formatDate(String dateold) {
        if (!dateold.equals("")) {
            String date = dateold;
            SimpleDateFormat spf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            Date newDate = null;
            try {
                newDate = spf.parse(date);
            } catch (Exception e) {
                e.printStackTrace();
            }
            spf = new SimpleDateFormat("yyyy/MM/dd");
            return spf.format(newDate);
        } else {
            return dateold;
        }
    }

    public static void showDialog(Context context, String title, String message) {
        final AlertDialog dialog = new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(R.string.button_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).create();
        if (!dialog.isShowing()) {
            dialog.show();
        }
    }

    public static void showErrorDialog(Context context, String message) {
        final AlertDialog dialog = new AlertDialog.Builder(context)
                .setTitle(R.string.button_error)
                .setMessage(message)
                .setPositiveButton(R.string.button_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).create();
        if (!dialog.isShowing()) {
            dialog.show();
        }
    }

    public static void showSuccessDialog(Context context, String message) {
        final AlertDialog dialog = new AlertDialog.Builder(context)
                .setTitle(R.string.success)
                .setMessage(message)
                .setPositiveButton(R.string.button_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).create();
        if (!dialog.isShowing()) {
            dialog.show();
        }
    }

    public static void setLocale(Context context) {
        String lang = PrefUtils.getPreference(context, LANGUAGE_PREF_KEY);
        Locale myLocale = new Locale(lang);
        Resources res = context.getResources();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, null);
    }

    public static void setupUI(final View view, final Activity activity) {

        //Set up touch listener for non-text box views to hide keyboard.
        if (!(view instanceof EditText)) {

            view.setOnTouchListener(new View.OnTouchListener() {

                public boolean onTouch(View v, MotionEvent event) {
                    hideSoftKeyboard(activity, view);
                    return false;
                }

            });
        }

        //If a layout container, iterate over children and seed recursion.
        if (view instanceof ViewGroup) {

            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {

                View innerView = ((ViewGroup) view).getChildAt(i);

                setupUI(innerView, activity);
            }
        }
    }

    public static void replaceFragment(FragmentActivity activity, Fragment someFragment) {
        FragmentTransaction fragmentTransaction = activity.getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left,
                android.R.anim.slide_out_right);
        fragmentTransaction.replace(R.id.frame, someFragment);
        fragmentTransaction.commitAllowingStateLoss();
    }

    public static void makeTextViewFont(Context context, TextView view) {
        Typeface iconFont = FontManager.getTypeface(context, FontManager.FONTAWESOME);
        view.setTypeface(iconFont);
    }

    public static void makeButtonFont(Context context, Button view) {
        Typeface iconFont = FontManager.getTypeface(context, FontManager.FONTAWESOME);
        view.setTypeface(iconFont);
    }

    public static void makeToast(Context context, String toast) {
        if (context == null)
            return;

        if (mToast == null) {
            mToast = Toast.makeText(context, toast, Toast.LENGTH_SHORT);
        }

        mToast.setText(toast);
        mToast.setGravity(android.view.Gravity.CENTER, 0, 0);
        mToast.show();
    }

    public static int pxToDp(Context context, int px) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return Math.round(px / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    public static int dpToPx(Context context, int dp) {
        Resources r = context.getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    public static void setListViewHeightBasedOnChildrenCart(Context context, ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += (listItem.getMeasuredHeight()) + 62;
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight;
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    public static String getCurrentdate() {
        return getYear() + "/" + getMonth() + "/" + getDate();
    }

    public static String getDate() {
        Calendar cal = new GregorianCalendar();
        return cal.get(cal.DAY_OF_MONTH) < 10 ? "0" + cal.get(cal.DAY_OF_MONTH) : "" + cal.get(cal.DAY_OF_MONTH);
    }

    public static String getMonth() {
        Calendar cal = new GregorianCalendar();
        return (1 + cal.get(cal.MONTH)) < 10 ? "0" + (1 + cal.get(cal.MONTH)) : "" + (1 + cal.get(cal.MONTH));
    }

    public static String getYear() {
        Calendar cal = new GregorianCalendar();
        return cal.get(cal.YEAR) + "";
    }

    public static String getHour() {
        Calendar cal = new GregorianCalendar();
        return cal.get(cal.HOUR) < 10 ? "0" + cal.get(cal.HOUR) : "" + cal.get(cal.HOUR);
    }

    public static String getMinute() {
        Calendar cal = new GregorianCalendar();
        return cal.get(cal.MINUTE) < 10 ? "0" + cal.get(cal.MINUTE) : "" + cal.get(cal.MINUTE);
    }

    public static String getSecond() {
        Calendar cal = new GregorianCalendar();
        return cal.get(cal.SECOND) < 10 ? "0" + cal.get(cal.SECOND) : "" + cal.get(cal.SECOND);
    }

    public static String getCurrentTime() {
        DateFormat df = new SimpleDateFormat("HH:mm:ss a");
        return df.format(Calendar.getInstance().getTime());
    }
    public static String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri,  proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }
}
