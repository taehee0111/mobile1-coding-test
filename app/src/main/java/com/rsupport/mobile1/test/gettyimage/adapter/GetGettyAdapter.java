package com.rsupport.mobile1.test.gettyimage.adapter;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.rsupport.mobile1.test.R;
import com.rsupport.mobile1.test.activity.MainActivity;
import com.rsupport.mobile1.test.gettyimage.holder.GettyViewHolder;
import com.rsupport.mobile1.test.gettyimage.holder.GettyViewProgressBarHolder;
import com.rsupport.mobile1.test.gettyimage.model.PassingData;

import java.util.ArrayList;

public class GetGettyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final MainActivity mActivity;
    private final int screenW;
    public boolean isLoading;

    public GetGettyAdapter(MainActivity activity, int screenW) {
        this.mActivity = activity;
        this.screenW = screenW;

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // 이미지
        if (viewType == PassingData.TYPE_URL) {
            View view = LayoutInflater.from(mActivity).inflate(R.layout.getty_holder_item, parent, false);
            return new GettyViewHolder(view, viewType);

        }
        // 진행 바
        else if (viewType == PassingData.TYPE_PROGRESS) {
            View view = LayoutInflater.from(mActivity).inflate(R.layout.getty_holder_item_progress, parent, false);

            mActivity.
            mActivity.rv.addOnItemTouchListener(new RecyclerView.SimpleOnItemTouchListener() {
                @Override
                public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
                    return super.onInterceptTouchEvent(rv, e);
                }
            });
            return new GettyViewProgressBarHolder(view, viewType);
        }
        // 예외
        else {
            View view = LayoutInflater.from(mActivity).inflate(R.layout.getty_holder_item, parent, false);
            return new GettyViewHolder(view, viewType);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder_, int position) {

        if (mActivity.isFinishing()) return;
        ArrayList<PassingData> itemLists = mActivity.resultLists;
        PassingData item = mActivity.resultLists.get(position);
        // URL
        if (item.dataType == PassingData.TYPE_URL) {
            GettyViewHolder holder = (GettyViewHolder) holder_;
            int itemW = screenW / 3;
            holder.parent.getLayoutParams().width = itemW;
            holder.parent.getLayoutParams().height = itemW;
            Glide.with(mActivity)
                    .load(item.url)
                    .override(itemW)
                    .into(holder.iv);
        }
        // 진행바
        else if (item.dataType == PassingData.TYPE_PROGRESS) {
            GettyViewProgressBarHolder holder = (GettyViewProgressBarHolder) holder_;

            int itemW = screenW;
            int itemH = screenW / 3;
            holder.parent.getLayoutParams().width = itemW;
            holder.parent.getLayoutParams().height = itemH;
        }


        boolean isLastItem = itemLists.size() / 3 == position;
        if (isLastItem) {

        }

    }

    @Override
    public int getItemCount() {
        if (mActivity.isFinishing()) {
            return 0;
        } else {
            if (mActivity.resultLists != null) {
                return mActivity.resultLists.size();
            }
        }
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        if (!mActivity.isFinishing()) {
            if (mActivity.resultLists != null && mActivity.resultLists.size() > 0) {
                PassingData item = mActivity.resultLists.get(position);
                return item.dataType;
            }
        }
        return PassingData.TYPE_NONE;

    }
}
