package com.notes.catalogue.utils;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.notes.catalogue.R;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


/**
 * For tools on the UI class, such as getting resources (color, string, drawable, etc.)
 * Screen width and height, dp and px conversion
 */

public class Utils {

//    private static Context getContext() {
//        return AppController.appl;
//    }


//    public static DisplayMetrics getDisplayMetrics() {
//        DisplayMetrics displayMetrics = new DisplayMetrics();
//        WindowManager windowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
//        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
//        return displayMetrics;
    //   }

    /**
     * Get the screen width pixel value
     */
//    public static int getScreenWidth() {
//        return getDisplayMetrics().widthPixels;
//    }
//
//    /**
//     * Gets the screen height pixel value
//     */
//    public static int getScreenHegith() {
//        return getDisplayMetrics().heightPixels;
//    }
    public static String getDayOfTheWeek(Date dates) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dates);
        int result = calendar.get(Calendar.DAY_OF_WEEK);
        switch (result) {
            case Calendar.MONDAY:
                return "Monday";
            case Calendar.TUESDAY:
                return "Tuesday";

            case Calendar.WEDNESDAY:
                return "Wednesday";

            case Calendar.THURSDAY:
                return "Thursday";

            case Calendar.FRIDAY:
                return "Friday";

            case Calendar.SATURDAY:
                return "Saturday";

            case Calendar.SUNDAY:
                return "Sunday";
        }
        return "";
    }

    public static Date stringToDate(String dates, String formats) {

        SimpleDateFormat format = new SimpleDateFormat(formats);
        try {
            return format.parse(dates);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new Date();
    }

    /**
     * Set the status bar color.
     */
    public static void setStatusBarColor(Activity activity) {
        Window window = activity.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(activity.getResources().getColor(R.color.purple_200));
        }
    }


    public static String roundOff(float input) {
        BigDecimal bigDecimal = new BigDecimal(input);
        bigDecimal = bigDecimal.setScale(2,
                BigDecimal.ROUND_HALF_UP);
        String dec = bigDecimal.toPlainString();

//        if (dec.contains(".0")) {
//            return dec.substring(0,dec.indexOf("."));
//        } else {
//            return dec;
//        }
        return dec;
    }

    /**
     * Fetch country name on behalf of SIM.
     */
    public static String getCountryName(Context context) {
        try {
            final TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            final String simCountry = tm.getSimCountryIso();
            if (simCountry != null && simCountry.length() == 2) { // SIM country code is available
                return simCountry.toLowerCase(Locale.US);
            } else if (tm.getPhoneType() != TelephonyManager.PHONE_TYPE_CDMA) { // device is not 3G (would be unreliable)
                String networkCountry = tm.getNetworkCountryIso();
                if (networkCountry != null && networkCountry.length() == 2) { // network country code is available
                    return networkCountry.toLowerCase(Locale.US);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String parseDate(Date date, String dateFormat) {
        SimpleDateFormat simpleDate = new SimpleDateFormat(dateFormat);
        String strDt = simpleDate.format(date);
        return strDt;
    }

    public static void closeKeyboard(Activity activity) {
        try {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {

        }

    }


}
