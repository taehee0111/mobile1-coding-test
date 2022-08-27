package com.rsupport.mobile1.test.application;

import android.app.Application;
import android.content.Intent;

import com.rsupport.mobile1.test.service.NetworkService;

public class BaseApplication extends Application {

    public BaseApplication() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Intent intent = new Intent(this, NetworkService.class);
        startService(intent);
    }
}
