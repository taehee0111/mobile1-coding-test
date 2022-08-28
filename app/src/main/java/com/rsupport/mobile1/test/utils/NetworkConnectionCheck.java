package com.rsupport.mobile1.test.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.NetworkRequest;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.rsupport.mobile1.test.utils.manager.NetworkManager;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class NetworkConnectionCheck extends ConnectivityManager.NetworkCallback {
    private Context context;
    @Nullable
    private NetworkRequest networkRequest;
    private ConnectivityManager connectivityManager;

    public NetworkConnectionCheck(Context context) {
        this.context = context;
        this.connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE); // CONNECTIVITY_SERVICE : 네트워크 연결 관리 처리를 검색

        networkRequest = new NetworkRequest.Builder()                                        // addTransportType : 주어진 전송 요구 사항을 빌더에 추가
                .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)   // TRANSPORT_CELLULAR : 이 네트워크가 셀룰러 전송을 사용함을 나타냅니다.
                .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)       // TRANSPORT_WIFI : 이 네트워크가 Wi-Fi 전송을 사용함을 나타냅니다.
                .build();
    }

    public void register() {
        connectivityManager.registerNetworkCallback(networkRequest, this);
    }

    public void unregister() {
        this.connectivityManager.unregisterNetworkCallback(this);
    }

    @Override
    public void onAvailable(@NonNull Network network) {
        super.onAvailable(network);

        //네트워크 상태 설정
        NetworkManager.getNetworkStatus(context);
        // 네트워크가 연결되었을 때 할 동작

        if (NetworkManager.isConnect) {
//            ToastUtils.showToast(context, context.getString(R.string.network_connected));
        }
    }

    @Override
    public void onLost(@NonNull Network network) {
        super.onLost(network);
        //네트워크 상태 설정
        NetworkManager.getNetworkStatus(context);
        // 네트워크 연결이 끊겼을 때 할 동작

        if (!NetworkManager.isConnect) {
//            ToastUtils.showToast(context, context.getString(R.string.network_discoonected));
        }
    }
}