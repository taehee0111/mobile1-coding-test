package com.rsupport.mobile1.test.utils.manager;


import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;

public class NetworkManager {
    public final static String LTE = "LTE";
    public final static String WIFI = "WIFI";
    public static String method;
    public static boolean isConnect;

    public static boolean isConnect(Context context) {
        //롤리팝 이상
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return isConnect;
        }
        //미만 직접 찾기
        else {
            getNetworkStatus(context);
            return isConnect;
        }
    }

    @Override
    public String toString() {
        return " method isConnect" + isConnect;
    }

    public static void getNetworkStatus(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        boolean isMetered = cm.isActiveNetworkMetered();
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
