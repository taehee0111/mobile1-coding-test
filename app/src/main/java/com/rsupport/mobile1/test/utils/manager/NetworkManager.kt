package com.rsupport.mobile1.test.utils.manager

import android.content.Context
import com.rsupport.mobile1.test.utils.manager.NetworkManager
import android.os.Build
import android.net.ConnectivityManager
import android.net.NetworkInfo

class NetworkManager {
    override fun toString(): String {
        return " method isConnect" + isConnect
    }

    companion object {
        const val LTE = "LTE"
        const val WIFI = "WIFI"
        var method: String? = null
        var isConnect = false
        @JvmStatic
        fun isConnect(context: Context): Boolean {
            //롤리팝 이상 콜백으로 네트워크 연결상태 설정
            //롤리팝 미만 네트워크 연결상태 확인
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                getNetworkStatus(context)
            }
            return isConnect
        }

        //네트워크 연결상태 설정
        @JvmStatic
        fun getNetworkStatus(context: Context) {
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val isMetered = cm.isActiveNetworkMetered
            if (isMetered) {
                method = LTE
            } else {
                method = WIFI
            }
            val activeNetwork = cm.activeNetworkInfo
            isConnect = activeNetwork != null && activeNetwork.isConnectedOrConnecting
            if (!isConnect) {
                method = ""
            }
        }
    }
}