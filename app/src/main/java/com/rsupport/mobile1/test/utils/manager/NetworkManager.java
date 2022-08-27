package com.rsupport.mobile1.test.utils.manager;


import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkManager {
    public final static String LTE = "LTE";
    public final static String WIFI = "WIFI";
    public static String method;
    public static boolean isConnect;

    @Override
    public String toString() {
        return " method isConnect" + isConnect;
    }

    public static void getNetworkStatus(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        boolean isMetered = cm.isActiveNetworkMetered();
        method = "";
        if (isMetered) {
            method = LTE;
        } else {
            method = WIFI;
        }

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        isConnect = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        if (!isConnect) {
            method = "";
        }
    }

}
