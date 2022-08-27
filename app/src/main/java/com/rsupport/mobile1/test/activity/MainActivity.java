package com.rsupport.mobile1.test.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.res.Configuration;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.rsupport.mobile1.test.R;
import com.rsupport.mobile1.test.gettyimage.adapter.GetGettyAdapter;
import com.rsupport.mobile1.test.gettyimage.manager.GetGettyImageManager;
import com.rsupport.mobile1.test.gettyimage.model.PassingData;
import com.rsupport.mobile1.test.utils.SizeUtil;
import com.rsupport.mobile1.test.utils.ToastUtils;
import com.rsupport.mobile1.test.utils.manager.NetworkManager;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private String TAG = MainActivity.class.getSimpleName();


    public ArrayList<PassingData> resultLists;

    int pageIndex = 1;
    private MainActivity mActivity;
    private int screenW;
    private GridLayoutManager gridLayoutManager;
    public RecyclerView rv;
    private GetGettyAdapter adapter;
    private int gettyImagePos = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = this;
        //서비스 실행전 네트워크 상태설정
        NetworkManager.getNetworkStatus(mActivity);

        Point screenSize = SizeUtil.getScreenSize(mActivity);
        screenW = screenSize.x;
        adapter = new GetGettyAdapter(mActivity, screenW);

        setContentView(R.layout.activity_main);
        rv = findViewById(R.id.rv);
        gridLayoutManager = new GridLayoutManager(mActivity, 3);
        rv.setLayoutManager(gridLayoutManager);
        rv.setAdapter(adapter);


        resultLists = new ArrayList<>();
        //네트워크 연결 X
        if (!NetworkManager.isConnect) {
            ToastUtils.showToast(mActivity, getResources().getString(R.string.weak_network));
        }
        //네트워크 연결 O
        else {
            //초기 0 페이지 호출
            getLoadPage(pageIndex);
        }//end else 네트워크 연결

    }//end onCreate

    private void getLoadPage(int pageIndex) {
        adapter.isLoading = true;
        new GetGettyImageManager().getGettyImage(pageIndex, new GetGettyImageManager.GetGettyImageCallback() {
            @Override
            public void end(ArrayList<PassingData> resultLists_) {
                resultLists.addAll(resultLists_);
                resultLists.add(new PassingData("", PassingData.TYPE_PROGRESS));
                adapter.notifyDataSetChanged();
                adapter.isLoading = false;
            }

            @Override
            public void error(String e) {
                ToastUtils.showToast(mActivity, e);
                adapter.isLoading = false;
            }
        });
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        //세로 전환시
        if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            gridLayoutManager.scrollToPositionWithOffset(gettyImagePos, 0);
        }
        //가로전환시
        else if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            gridLayoutManager.scrollToPositionWithOffset(gettyImagePos, 0);
        }
        //
        else {

        }
    }
}