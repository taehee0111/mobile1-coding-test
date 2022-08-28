package com.rsupport.mobile1.test.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.security.ProviderInstaller;
import com.rsupport.mobile1.test.R;
import com.rsupport.mobile1.test.gettyimage.adapter.GetGettyAdapter;
import com.rsupport.mobile1.test.utils.ToastUtils;
import com.rsupport.mobile1.test.utils.manager.NetworkManager;

import java.util.Objects;


//테스트:
// 네트워크 연결 상태
// 페이지 로딩 실패 유무
// 화면 회전

public class MainActivity extends AppCompatActivity {
    public RecyclerView rv;
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String RECYCLER_VIEW_POSITION = "RECYCLER_VIEW_POSITION";
    private static final String RECYCLER_VIEW_LIST = "RECYCLER_VIEW_POSITION";

    private MainActivity mActivity;
    private GetGettyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mActivity = this;
        //보안패치 (API 19 통신 보안 업데이트 => ssl을 통한 보안 )
        try {
            ProviderInstaller.installIfNeeded(this);
        } catch (GooglePlayServicesRepairableException e) {
            checkGooglePlayServices();
            Log.e(TAG, e.toString());
        } catch (GooglePlayServicesNotAvailableException e) {
            Log.e(TAG, "Google Play Services not available.");
        }

        rv = findViewById(R.id.rv);
        ProgressBar pb = findViewById(R.id.pb);

        //서비스 실행전 네트워크 상태설정
        NetworkManager.getNetworkStatus(mActivity);

        //리스트 뷰 설정
        adapter = new GetGettyAdapter(mActivity);

        pb.setVisibility(View.VISIBLE);
        //parcelable 예외처리
        if (savedInstanceState == null) {
            //초기 페이지 로딩 호출
            adapter.loadPage(null, new GetGettyAdapter.EndLoadPageCallback() {
                @Override
                public void end() {
                    pb.setVisibility(View.GONE);
                }
            });
        } else {
            pb.setVisibility(View.GONE);
        }
    }//end onCreate

    //화면 회전시 데이터 유지
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (adapter != null && adapter.gridLayoutManager != null) {
            Parcelable rvParcel = adapter.gridLayoutManager.onSaveInstanceState();
            outState.putParcelable(RECYCLER_VIEW_POSITION, rvParcel);
            outState.putParcelableArrayList(RECYCLER_VIEW_LIST, adapter.resultLists);
        }
    }

    //화면 회전시 데이터 유지
    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (adapter != null && adapter.gridLayoutManager != null) {
            Parcelable rvParcel = savedInstanceState.getParcelable(RECYCLER_VIEW_POSITION);
            adapter.resultLists = savedInstanceState.getParcelableArrayList(RECYCLER_VIEW_LIST);
            adapter.gridLayoutManager.onRestoreInstanceState(rvParcel);
        }
    }

    //jellyBean 구글 플레이에서 지원 중단(API 16)
    private void checkGooglePlayServices() {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.JELLY_BEAN) {
            ToastUtils.showToast(mActivity,getResources().getString(R.string.jellybean_not_able));
        }
        switch (GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this)) {
            case ConnectionResult.SERVICE_MISSING:
                Objects.requireNonNull(GoogleApiAvailability.getInstance().getErrorDialog(this, ConnectionResult.SERVICE_MISSING, 0)).show();
                break;
            case ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED:
                Objects.requireNonNull(GoogleApiAvailability.getInstance().getErrorDialog(this, ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED, 0)).show();
                break;
            case ConnectionResult.SERVICE_DISABLED:
                Objects.requireNonNull(GoogleApiAvailability.getInstance().getErrorDialog(this, ConnectionResult.SERVICE_DISABLED, 0)).show();
                break;
        }
    }
}