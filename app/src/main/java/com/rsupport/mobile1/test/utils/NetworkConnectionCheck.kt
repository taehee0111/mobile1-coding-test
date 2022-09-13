package com.rsupport.mobile1.test.utils

import android.content.Context
import com.rsupport.mobile1.test.utils.manager.NetworkManager.Companion.getNetworkStatus
import androidx.annotation.RequiresApi
import android.os.Build
import android.net.ConnectivityManager.NetworkCallback
import android.net.NetworkRequest
import android.net.ConnectivityManager
import android.net.Network
import com.rsupport.mobile1.test.utils.manager.NetworkManager
import android.net.NetworkCapabilities

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
class NetworkConnectionCheck(private val context: Context) : NetworkCallback() {
    private val networkRequest: NetworkRequest? = NetworkRequest.Builder() // addTransportType : 주어진 전송 요구 사항을 빌더에 추가
        .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR) // TRANSPORT_CELLULAR : 이 네트워크가 셀룰러 전송을 사용함을 나타냅니다.
        .addTransportType(NetworkCapabilities.TRANSPORT_WIFI) // TRANSPORT_WIFI : 이 네트워크가 Wi-Fi 전송을 사용함을 나타냅니다.
        .build()
    private val connectivityManager: ConnectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    fun register() {
        connectivityManager.registerNetworkCallback(networkRequest!!, this)
    }

    fun unregister() {
        connectivityManager.unregisterNetworkCallback(this)
    }

    override fun onAvailable(network: Network) {
        super.onAvailable(network)

        //네트워크 상태 설정
        getNetworkStatus(context)
        // 네트워크가 연결되었을 때 할 동작
        if (NetworkManager.isConnect) {
//            ToastUtils.showToast(context, context.getString(R.string.network_connected));
        }
    }

    override fun onLost(network: Network) {
        super.onLost(network)
        //네트워크 상태 설정
        getNetworkStatus(context)
        // 네트워크 연결이 끊겼을 때 할 동작
        if (!NetworkManager.isConnect) {
//            ToastUtils.showToast(context, context.getString(R.string.network_discoonected));
        }
    }

    init {
        // CONNECTIVITY_SERVICE : 네트워크 연결 관리 처리를 검색
    }
}