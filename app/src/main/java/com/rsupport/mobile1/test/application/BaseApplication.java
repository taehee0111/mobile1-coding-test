package com.rsupport.mobile1.test.application;

import android.app.Application;
import android.content.ComponentCallbacks;
import android.content.Intent;
import android.os.Build;

import com.rsupport.mobile1.test.service.NetworkService;
import com.rsupport.mobile1.test.utils.NetworkConnectionCheck;

public class BaseApplication extends Application {
    public static NetworkConnectionCheck networkConnectionCheck;

    public BaseApplication() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {    //  LOLLIPOP Version 이상..
            if (networkConnectionCheck == null) {
                networkConnectionCheck = new NetworkConnectionCheck(getApplicationContext());
                networkConnectionCheck.register();
            }
        }
//        Intent intent = new Intent(this, NetworkService.class);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            startForegroundService(intent);
//        } else {
//            startService(intent);
//        }
    }


}
