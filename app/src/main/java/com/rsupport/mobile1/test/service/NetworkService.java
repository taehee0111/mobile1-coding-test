package com.rsupport.mobile1.test.service;

import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import com.rsupport.mobile1.test.utils.NetworkConnectionCheck;


public class NetworkService extends Service {

    public static NetworkConnectionCheck networkConnectionCheck;
    private String TAG = NetworkService.class.getSimpleName();

    public NetworkService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }// onBind()..

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG,"onStartCommand");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {    //  LOLLIPOP Version 이상..
            if (networkConnectionCheck == null) {
                networkConnectionCheck = new NetworkConnectionCheck(getApplicationContext());
                networkConnectionCheck.register();
            }
        }
        return START_STICKY;    // START_STICKY : 시스템에 의해 종료 되어도 다시 생성 시켜주는 것
    }// onStartCommand() ..

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG,"onDestroy");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {     //  LOLLIPOP Version 이상..
            if (networkConnectionCheck != null) networkConnectionCheck.unregister();
        }
    }// onDestroy()..
}
