package com.rsupport.mobile1.test.application


import android.app.Application
import android.os.Build
import com.rsupport.mobile1.test.utils.NetworkConnectionCheck

class BaseApplication : Application() {
    private val TAG = BaseApplication::class.java.simpleName
    override fun onCreate() {
        super.onCreate()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (networkConnectionCheck == null) {
                networkConnectionCheck = NetworkConnectionCheck(
                    applicationContext
                )
                networkConnectionCheck!!.register()
            }
        }
    }

    companion object {
        var networkConnectionCheck: NetworkConnectionCheck? = null
    }
}