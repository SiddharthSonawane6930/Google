package com.notes.catalogue.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

/**
 * Class to get info about data connection.
 *
 * @see <a
 * href="http://stackoverflow.com/questions/2802472/detect-network-connection-type-on-android">
 * Detect network connection type on Android</a>
 */
public  class Connectivity {

    /**
     * Instantiates a new connectivity.
     */
    private Connectivity() {
    }

	/*
     * HACKISH: These constants aren't yet available in my API level (7), but I
	 * need to handle these cases if they come up, on newer versions
	 */
    /**
     * The Constant NETWORK_TYPE_EHRPD.
     */
    public static final int NETWORK_TYPE_EHRPD = 14; // Level 11

    /**
     * The Constant NETWORK_TYPE_EVDO_B.
     */
    public static final int NETWORK_TYPE_EVDO_B = 12; // Level 9

    /**
     * The Constant NETWORK_TYPE_HSPAP.
     */
    public static final int NETWORK_TYPE_HSPAP = 15; // Level 13

    /**
     * The Constant NETWORK_TYPE_IDEN.
     */
    public static final int NETWORK_TYPE_IDEN = 11; // Level 8

    /**
     * The Constant NETWORK_TYPE_LTE.
     */
    public static final int NETWORK_TYPE_LTE = 13; // Level 11

    /**
     * The Constant NETWORK_TYPE_HSDPA.
     */
    public static final int NETWORK_TYPE_HSDPA = 8;

    /**
     * The Constant NETWORK_TYPE_HSPA.
     */
    public static final int NETWORK_TYPE_HSPA = 10;

    /**
     * The Constant NETWORK_TYPE_HSUPA.
     */
    public static final int NETWORK_TYPE_HSUPA = 9;

    /**
     * Check if there is any connectivity.
     *
     * @param context Current application context
     * @return <b>True</b> if device network is connected
     */
    public static boolean isConnected(final Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        return (info != null && info.isConnected());
    }

    /**
     * Check if there is fast connectivity.
     *
     * @param context Current application context
     * @return <b>True</b> if the connection is considered fast, <b>False</b>
     * otherwise.
     * @see Connectivity#isConnectionFast(int, int) isConnectionFast
     */
    public static boolean isConnectedFast(final Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        return (info != null && info.isConnected() && Connectivity
                .isConnectionFast(info.getType(), info.getSubtype()));
    }

    /**
     * Check if the connection is fast.
     *
     * @param type    Type of the connection WIFI or MOBILE
     * @param subType Sub type of MOBILE type connection
     * @return <b>True</b> if device is connected to a fast speed network, such
     * as WIFI, HSDPA, HSPA, HSUPA, UMTS, EVDO_0, EVDO_A, EVDO_B, EHRPD,
     * LTE. <b>False></b> for other networks
     */
    private static boolean isConnectionFast(final int type, final int subType) {
        if (type == ConnectivityManager.TYPE_WIFI) {
            return true;
        } else if (type == ConnectivityManager.TYPE_MOBILE) {
            switch (subType) {
                case TelephonyManager.NETWORK_TYPE_1xRTT:
                    return false; // ~ 50-100 kbps
                case TelephonyManager.NETWORK_TYPE_CDMA:
                    return false; // ~ 14-64 kbps
                case TelephonyManager.NETWORK_TYPE_EDGE:
                    return false; // ~ 50-100 kbps
                case TelephonyManager.NETWORK_TYPE_EVDO_0:
                    return true; // ~ 400-1000 kbps
                case TelephonyManager.NETWORK_TYPE_EVDO_A:
                    return true; // ~ 600-1400 kbps
                case TelephonyManager.NETWORK_TYPE_GPRS:
                    return false; // ~ 100 kbps
                case Connectivity.NETWORK_TYPE_HSDPA:
                    return true; // ~ 2-14 Mbps
                case Connectivity.NETWORK_TYPE_HSPA:
                    return true; // ~ 700-1700 kbps
                case Connectivity.NETWORK_TYPE_HSUPA:
                    return true; // ~ 1-23 Mbps
                case TelephonyManager.NETWORK_TYPE_UMTS:
                    return true; // ~ 400-7000 kbps
                // NOT AVAILABLE YET IN API LEVEL 7
                case Connectivity.NETWORK_TYPE_EHRPD:
                    return true; // ~ 1-2 Mbps
                case Connectivity.NETWORK_TYPE_EVDO_B:
                    return true; // ~ 5 Mbps
                case Connectivity.NETWORK_TYPE_HSPAP:
                    return true; // ~ 10-20 Mbps
                case Connectivity.NETWORK_TYPE_IDEN:
                    return false; // ~25 kbps
                case Connectivity.NETWORK_TYPE_LTE:
                    return true; // ~ 10+ Mbps
                // Unknown
                case TelephonyManager.NETWORK_TYPE_UNKNOWN:
                    return false;
                default:
                    return false;
            }
        } else {
            return false;
        }
    }

    /**
     * Check if device is connected via WIFI.
     *
     * @param context Current application context
     * @return <b>True</b> if device is connected via WIFI
     * @see Connectivity#isConnectionViaWiFi(int) isConnectionViaWiFi
     */
    public static boolean isConnectedViaWifi(final Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        return (info != null && info.isConnected() && Connectivity
                .isConnectionViaWiFi(info.getType()));
    }

    /**
     * Check if device is connected via WiFi.
     *
     * @param type Type of current data connection
     * @return <b>True</b> if device is connected via WIFI
     */
    private static boolean isConnectionViaWiFi(final int type) {
        if (type == ConnectivityManager.TYPE_WIFI) {
            return true;
        }

        return false;
    }

}
