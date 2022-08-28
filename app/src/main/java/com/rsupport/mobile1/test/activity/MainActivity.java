package com.rsupport.mobile1.test.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.ProgressBar;

import com.rsupport.mobile1.test.R;
import com.rsupport.mobile1.test.gettyimage.adapter.GetGettyAdapter;
import com.rsupport.mobile1.test.utils.ToastUtils;
import com.rsupport.mobile1.test.utils.manager.NetworkManager;

public class MainActivity extends AppCompatActivity {
    public RecyclerView rv;
    private String TAG = MainActivity.class.getSimpleName();
    private static String RECYCLER_VIEW_POSITION = "RECYCLER_VIEW_POSITION";
    private static String RECYCLER_VIEW_LIST = "RECYCLER_VIEW_POSITION";

    private MainActivity mActivity;
    private GetGettyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mActivity = this;

        rv = findViewById(R.id.rv);
        ProgressBar pb = findViewById(R.id.pb);

        //서비스 실행전 네트워크 상태설정
        NetworkManager.getNetworkStatus(mActivity);

        //리스트 뷰 설정
        adapter = new GetGettyAdapter(mActivity);

        pb.setVisibility(View.VISIBLE);
        //parcelable 예외처리
        if (savedInstanceState == null) {
            //네트워크 연결 X
            if (!NetworkManager.isConnect) {
                ToastUtils.showToast(mActivity, getResources().getString(R.string.weak_network));
                pb.setVisibility(View.GONE);
            }
            //네트워크 연결 O
            else {
                //초기 페이지 로딩 호출
                adapter.loadPage(null, new GetGettyAdapter.EndLoad() {
                    @Override
                    public void end() {
                        pb.setVisibility(View.GONE);
                    }
                });
            }//end else 네트워크 연결
        } else {
            pb.setVisibility(View.GONE);
        }


    }//end onCreate


    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (adapter != null && adapter.gridLayoutManager != null) {
            Parcelable rvParcel = adapter.gridLayoutManager.onSaveInstanceState();
            outState.putParcelable(RECYCLER_VIEW_POSITION, rvParcel);
            outState.putParcelableArrayList(RECYCLER_VIEW_LIST, adapter.resultLists);
        }
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (adapter != null && adapter.gridLayoutManager != null) {
            Parcelable rvParcel = savedInstanceState.getParcelable(RECYCLER_VIEW_POSITION);
            adapter.resultLists = savedInstanceState.getParcelableArrayList(RECYCLER_VIEW_LIST);
            adapter.gridLayoutManager.onRestoreInstanceState(rvParcel);
        }
    }
}