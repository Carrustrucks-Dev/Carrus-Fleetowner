package com.carrus.fleetowner.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.inputmethod.InputMethodManager;

import com.carrus.fleetowner.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import retrofit.RetrofitError;
import retrofit.mime.TypedByteArray;

/**
 * Created by Sunny on 11/6/15 for Fleet Owner for Fleet Owner for Fleet Owner.
 */
public class Utils {

    private static ProgressDialog progressDialog = null;
    private static AlertDialog mAlertDialog = null;

    /**
     * Loading view
     *
     * @param c context from where the loading view is in voked
     */

    public static void loading_box(Context c) {
        try {
            if (progressDialog == null) {
                progressDialog = new ProgressDialog(c,
                        android.R.style.Theme_Translucent_NoTitleBar);
                progressDialog.show();
                progressDialog.setCancelable(false);

                progressDialog.setContentView(R.layout.loading_box);

            } else {
                if (!progressDialog.isShowing())
                    progressDialog.show();
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    /**
     * stop loading view
     */
    public static void loading_box_stop() {
        if (progressDialog != null)
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
                progressDialog = null;
            }
    }

    public static int getColor(Context context, int id) {
        final int version = Build.VERSION.SDK_INT;
        if (version >= 23) {
            return ContextCompat.getColor(context, id);
        } else {
            //noinspection deprecation
            return context.getResources().getColor(id);
        }
    }

    /**
     * Returns the consumer friendly device name
     */
    public static String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        }
        return capitalize(manufacturer) + " " + model;
    }

    private static String capitalize(String str) {
        if (TextUtils.isEmpty(str)) {
            return str;
        }
        char[] arr = str.toCharArray();
        boolean capitalizeNext = true;
        String phrase = "";
        for (char c : arr) {
            if (capitalizeNext && Character.isLetter(c)) {
                phrase += Character.toUpperCase(c);
                capitalizeNext = false;
                continue;
            } else if (Character.isWhitespace(c)) {
                capitalizeNext = true;
            }
            phrase += c;
        }
        return phrase;
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public static boolean isValidEmail(String target) {
        return TextUtils.isEmpty(target) || !android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    public static String getDate(String time) throws ParseException {
        Calendar cal = Calendar.getInstance();
        TimeZone tz = cal.getTimeZone();
        @SuppressLint("SimpleDateFormat") DateFormat f = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
//        f.setTimeZone(TimeZone.getTimeZone("ISO"));
        f.setTimeZone(tz);
        Date d = f.parse(String.valueOf(time));
        @SuppressLint("SimpleDateFormat") DateFormat date = new SimpleDateFormat("dd");
        return date.format(d);
    }

    public static String getDateMonth(String time) throws ParseException {
        Calendar cal = Calendar.getInstance();
        TimeZone tz = cal.getTimeZone();
        @SuppressLint("SimpleDateFormat") DateFormat f = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
//        f.setTimeZone(TimeZone.getTimeZone("ISO"));
        f.setTimeZone(tz);
        Date d = f.parse(String.valueOf(time));
        @SuppressLint("SimpleDateFormat") DateFormat date = new SimpleDateFormat("dd MMM");
        return date.format(d);
    }

    public static String getMonthYear(String time) throws ParseException {
        Calendar cal = Calendar.getInstance();
        TimeZone tz = cal.getTimeZone();
        @SuppressLint("SimpleDateFormat") DateFormat f = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
//        f.setTimeZone(TimeZone.getTimeZone("ISO"));
        f.setTimeZone(tz);
        Date d = f.parse(String.valueOf(time));
        @SuppressLint("SimpleDateFormat") DateFormat date = new SimpleDateFormat("MMM yyyy");
        return date.format(d);
    }

    public static String getMonth(String time) throws ParseException {
        Calendar cal = Calendar.getInstance();
        TimeZone tz = cal.getTimeZone();
        @SuppressLint("SimpleDateFormat") DateFormat f = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
//        f.setTimeZone(TimeZone.getTimeZone("ISO"));
        f.setTimeZone(tz);
        Date d = f.parse(String.valueOf(time));
        @SuppressLint("SimpleDateFormat") DateFormat month = new SimpleDateFormat("MMM");
        return month.format(d);
    }

    public static String getDay(String time) throws ParseException {
        Calendar cal = Calendar.getInstance();
        TimeZone tz = cal.getTimeZone();
        @SuppressLint("SimpleDateFormat") DateFormat f = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
//        f.setTimeZone(TimeZone.getTimeZone("ISO"));
        f.setTimeZone(tz);
        Date d = f.parse(String.valueOf(time));
        @SuppressLint("SimpleDateFormat") DateFormat day = new SimpleDateFormat("EEE");
        return day.format(d);
    }

    public static String getFullDateTime(String time) throws ParseException {
        Calendar cal = Calendar.getInstance();
        TimeZone tz = cal.getTimeZone();
        @SuppressLint("SimpleDateFormat") DateFormat f = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
//        f.setTimeZone(TimeZone.getTimeZone("ISO"));
        f.setTimeZone(tz);
        Date d = f.parse(String.valueOf(time));
        @SuppressLint("SimpleDateFormat") DateFormat day = new SimpleDateFormat("EEEE, dd MMMM yyyy");
        return day.format(d);
    }

    public static void shopAlterDialog(final Context myContext, String msg, final boolean isAuthroized) {
        if (mAlertDialog == null)
            mAlertDialog = new AlertDialog.Builder(myContext)
                    // Set Dialog Icon
//                .setIcon(R.drawable.androidhappy)
                    // Set Dialog Title
                    .setTitle("")
                            // Set Dialog Message
                    .setMessage(msg)
                    .setCancelable(false)

                            // Positive button
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // Do something else
                            dialog.dismiss();
                            if (isAuthroized)
                                new SessionManager(myContext).logoutUser();
                            mAlertDialog = null;
                        }
                    }).create();
        mAlertDialog.show();

    }
    /**
     * For Fetching Retrofit Error Message
     *
     * @param error The retrofit error reference
     *
     */
    public static String getErrorMsg(RetrofitError error) {
        String errorMessage = "";
        String str = new String(((TypedByteArray) error.getResponse().getBody()).getBytes());
        JSONObject json;
        try {
            json = new JSONObject(str);
            errorMessage = json.getString("message");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return errorMessage;
    }

    /**
     * hiding keyboard
     *
     * @param activity The activity reference
     */

    public static void hideSoftKeyboard(Activity activity) {
//        InputMethodManager inputMethodManager = (InputMethodManager)  activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
//        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        try {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        } catch (Exception ignored) {
            ignored.printStackTrace();
        }
    }

}
