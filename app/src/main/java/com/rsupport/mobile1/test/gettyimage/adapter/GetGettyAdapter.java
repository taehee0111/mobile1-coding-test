package com.rsupport.mobile1.test.gettyimage.adapter;

import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.rsupport.mobile1.test.R;
import com.rsupport.mobile1.test.activity.MainActivity;
import com.rsupport.mobile1.test.gettyimage.holder.GettyViewHolder;
import com.rsupport.mobile1.test.gettyimage.holder.GettyViewProgressBarHolder;
import com.rsupport.mobile1.test.gettyimage.manager.CustomGridLayoutManager;
import com.rsupport.mobile1.test.gettyimage.manager.GetGettyImageManager;
import com.rsupport.mobile1.test.gettyimage.model.GettyData;
import com.rsupport.mobile1.test.utils.SizeUtil;
import com.rsupport.mobile1.test.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

public class GetGettyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private String TAG = GetGettyAdapter.class.getSimpleName();
    private final MainActivity mActivity;
    public static final String PAYLOAD_IMAGE = "PAYLOAD_IMAGE";
    private int LOAD_REFRESH = 1;
    private int LOAD_PAGE = 2;


    public final CustomGridLayoutManager gridLayoutManager;

    public ArrayList<GettyData> resultLists;
    private final int screenW;
    public boolean isLoading;
    public int pageIndex = 0;


    public GetGettyAdapter(MainActivity activity) {
        this.mActivity = activity;
        resultLists = new ArrayList<>();
        Point screenSize = SizeUtil.getScreenSize(mActivity);
        screenW = screenSize.x;

        gridLayoutManager = new CustomGridLayoutManager(mActivity, 3);
        activity.rv.setLayoutManager(gridLayoutManager);
        activity.rv.setAdapter(this);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                GettyData loadingPos = resultLists.get(position);
                //프로그래스바, 새로고침
                if (loadingPos.dataType == GettyData.TYPE_PROGRESS) {
//                        loadingPos.dataType == GettyData.TYPE_REFRESH) {
                    return 3;
                }
                //이미지
                else {
                    return 1;
                }
            }
        });
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull RecyclerView.ViewHolder holder_) {
        super.onViewDetachedFromWindow(holder_);
        if (holder_ instanceof GettyViewHolder) {
            ImageView imageView = ((GettyViewHolder) holder_).iv;
            Glide.with(mActivity).clear(imageView);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // 이미지
        if (viewType == GettyData.TYPE_URL) {
            View view = LayoutInflater.from(mActivity).inflate(R.layout.getty_holder_item, parent, false);
            return new GettyViewHolder(view, viewType);

        }
        // 진행 바
        else if (viewType == GettyData.TYPE_PROGRESS) {
            View view = LayoutInflater.from(mActivity).inflate(R.layout.getty_holder_item_progress, parent, false);
            return new GettyViewProgressBarHolder(view, viewType);
        }
//        //로드 실패시 새로고침
//        else if (viewType == GettyData.TYPE_REFRESH) {
//            View view = LayoutInflater.from(mActivity).inflate(R.layout.getty_holder_item_refresh, parent, false);
//            return new GettyViewRefreshHolder(view, viewType);
//        }
        // 예외
        else {
            View view = LayoutInflater.from(mActivity).inflate(R.layout.getty_holder_item, parent, false);
            return new GettyViewHolder(view, viewType);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position, @NonNull List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);
        for (int i = 0; i < payloads.size(); i++) {
            if (PAYLOAD_IMAGE.equals(payloads.get(i))) {
                payloadImage(holder, position);
                return;
            }
        }
        onBindViewHolder(holder, position);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder_, int position) {

        if (mActivity.isFinishing()) return;
        GettyData item = resultLists.get(position);
        // 이미지
        if (item.dataType == GettyData.TYPE_URL) {
            payloadImage(holder_, position);
        }
//        // 새로고침
//        else if (item.dataType == GettyData.TYPE_REFRESH) {
//            GettyViewRefreshHolder holder = (GettyViewRefreshHolder) holder_;
//            int itemH = screenW / 3;
//            holder.parent.getLayoutParams().width = screenW;
//            holder.parent.getLayoutParams().height = itemH;
//            holder.parent.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    loadPage(null); // 새로고침
//                }
//            });
//        }
        // 진행바 => 보일경우 데이터 로딩
        else if (item.dataType == GettyData.TYPE_PROGRESS) {
            GettyViewProgressBarHolder holder = (GettyViewProgressBarHolder) holder_;
            int itemH = screenW / 3;
            holder.parent.getLayoutParams().width = screenW;
            holder.parent.getLayoutParams().height = itemH;
            loadPage(holder, null); // 진행바
        }

    }

    private void payloadImage(RecyclerView.ViewHolder holder_, int position) {
        GettyViewHolder holder = (GettyViewHolder) holder_;
        Glide.with(mActivity).clear(holder.iv);
        GettyData item = resultLists.get(position);
        int itemW = screenW / 3;
        holder.parent.getLayoutParams().width = itemW;
        holder.parent.getLayoutParams().height = itemW;

        holder.iv.setVisibility(View.GONE);
        holder.pb.setVisibility(View.VISIBLE);
        Glide.with(mActivity)
                .load(item.url)
                .override(itemW)
                .into(new CustomTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        int holderPosition = holder.getAdapterPosition();
                        if (holderPosition == position) {
                            holder.iv.setImageDrawable(resource);
                            holder.pb.setVisibility(View.GONE);
                            holder.iv.setVisibility(View.VISIBLE);
                        } else {
                            Log.d(TAG, "holderPosition:" + holderPosition + "pageIndex:" + pageIndex);
                        }
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }

                    @Override
                    public void onLoadFailed(@Nullable Drawable errorDrawable) {
                        super.onLoadFailed(errorDrawable);
                        if (holder.getAdapterPosition() == position) {
                            holder.pb.setVisibility(View.GONE);
                            holder.iv.setVisibility(View.VISIBLE);
                        }
                    }
                });

        //Preload
        if (position == resultLists.size() - 6) {
            int endPosition = Math.min(position, resultLists.size());
            List<GettyData> list = resultLists.subList(position, endPosition);
            for (GettyData gettyData : list) {
                Glide.with(mActivity).load(gettyData.url).preload(itemW, itemW);
            }
        }
    }

    @Override
    public int getItemCount() {
        if (mActivity.isFinishing()) {
            return 0;
        } else {
            if (resultLists != null) {
                return resultLists.size();
            }
        }
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        if (!mActivity.isFinishing()) {
            if (resultLists != null && resultLists.size() > 0) {
                GettyData item = resultLists.get(position);
                return item.dataType;
            }
        }
        return GettyData.TYPE_NONE;
    }


    public interface EndLoad {
        void end();
    }


    public void loadPage(@Nullable GettyViewProgressBarHolder holder, @Nullable EndLoad endLoad) {
        if (isLoading) {
            return;
        }
        Log.d(TAG, "pageIndex:" + pageIndex);
        isLoading = true;
        pageIndex++;
        new GetGettyImageManager().getGettyImage(mActivity, pageIndex, new GetGettyImageManager.GetGettyImageCallback() {
            @Override
            public void end(ArrayList<GettyData> resultLists_) {
                int progressIndex = resultLists.size() - 1;

                GettyData pbItem;
                //진행 바 존재 O => 존재 시 제거
                if (progressIndex > 0) {
                    pbItem = resultLists.get(progressIndex);
                    resultLists.remove(progressIndex);
                }
                //진행 바 존재 X => 진행 바 생성
                else {
                    pbItem = new GettyData("", GettyData.TYPE_PROGRESS);
                }

                //진행바 추가
                resultLists.addAll(resultLists_);
                resultLists.add(pbItem);

                int notifyLength = resultLists.size() - progressIndex;
                isLoading = false;
                notifyItemRangeChanged(progressIndex, notifyLength, PAYLOAD_IMAGE);
                if (endLoad != null) {
                    endLoad.end();
                }
            }

            //로딩 실패
            @Override
            public void error(String e) {
                ToastUtils.showToast(mActivity, e);
                int progressIndex = resultLists.size() - 1;
                GettyData gettyData;


                if (progressIndex >= 0) {
                    gettyData = resultLists.get(progressIndex);
//                    gettyData.dataType = GettyData.TYPE_REFRESH;
                } else {
//                    gettyData = new GettyData("", GettyData.TYPE_REFRESH);
                    progressIndex = 0;
//                    resultLists.add(gettyData);
                }

                notifyItemChanged(progressIndex);

                isLoading = false;
                pageIndex--;
                if (endLoad != null) {
                    endLoad.end();
                }
            }
        });
    }//end add page
}
