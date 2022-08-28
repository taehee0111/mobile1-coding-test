package com.rsupport.mobile1.test.application;

import android.app.Application;
import android.content.ComponentCallbacks;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GoogleApiAvailabilityLight;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.security.ProviderInstaller;
import com.rsupport.mobile1.test.service.NetworkService;
import com.rsupport.mobile1.test.utils.NetworkConnectionCheck;
import com.rsupport.mobile1.test.utils.manager.NetworkManager;

public class BaseApplication extends Application {
    public static NetworkConnectionCheck networkConnectionCheck;
    private String TAG = BaseApplication.class.getSimpleName();

    public BaseApplication() {
    }

    @Override
    public void onCreate() {
        super.onCreate();


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (networkConnectionCheck == null) {
                networkConnectionCheck = new NetworkConnectionCheck(getApplicationContext());
                networkConnectionCheck.register();
            }
        }
    }

}
