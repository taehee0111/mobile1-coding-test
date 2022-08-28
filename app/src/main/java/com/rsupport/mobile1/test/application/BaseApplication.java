package com.rsupport.mobile1.test.application;

import android.app.Application;
import android.os.Build;

import com.rsupport.mobile1.test.utils.NetworkConnectionCheck;

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
