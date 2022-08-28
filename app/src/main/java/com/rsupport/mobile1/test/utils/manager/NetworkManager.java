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
        //롤리팝 이상 콜백으로 네트워크 연결상태 설정
        //롤리팝 미만 네트워크 연결상태 확인
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            getNetworkStatus(context);
        }
        return isConnect;
    }

    //네트워크 연결상태 설정
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

    @Override
    public String toString() {
        return " method isConnect" + isConnect;
    }

}
