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
import com.rsupport.mobile1.test.gettyimage.holder.GettyViewImageHolder;
import com.rsupport.mobile1.test.gettyimage.holder.GettyViewProgressBarHolder;
import com.rsupport.mobile1.test.gettyimage.holder.GettyViewRefreshHolder;
import com.rsupport.mobile1.test.gettyimage.manager.CustomGridLayoutManager;
import com.rsupport.mobile1.test.gettyimage.manager.GetGettyImageManager;
import com.rsupport.mobile1.test.gettyimage.model.GettyData;
import com.rsupport.mobile1.test.utils.SizeUtil;
import com.rsupport.mobile1.test.utils.ToastUtils;
import com.rsupport.mobile1.test.utils.manager.NetworkManager;

import java.util.ArrayList;
import java.util.List;

public class GetGettyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private String TAG = GetGettyAdapter.class.getSimpleName();
    private final MainActivity mActivity;
    public static final String PAYLOAD_IMAGE = "PAYLOAD_IMAGE";

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
                if (loadingPos.dataType == GettyData.TYPE_PROGRESS ||
                        loadingPos.dataType == GettyData.TYPE_REFRESH) {
                    return 3;
                }
                //이미지
                else {
                    return 1;
                }
            }
        });
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // 이미지
        if (viewType == GettyData.TYPE_URL) {
            View view = LayoutInflater.from(mActivity).inflate(R.layout.getty_holder_item, parent, false);
            return new GettyViewImageHolder(view, viewType);
        }
        // 진행 바
        else if (viewType == GettyData.TYPE_PROGRESS) {
            View view = LayoutInflater.from(mActivity).inflate(R.layout.getty_holder_item_progress, parent, false);
            return new GettyViewProgressBarHolder(view, viewType);
        }
        //로드 실패시 새로고침
        else if (viewType == GettyData.TYPE_REFRESH) {
            View view = LayoutInflater.from(mActivity).inflate(R.layout.getty_holder_item_refresh, parent, false);
            return new GettyViewRefreshHolder(view, viewType);
        }
        // 예외
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position, @NonNull List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);
        for (int i = 0; i < payloads.size(); i++) {
            if (PAYLOAD_IMAGE.equals(payloads.get(i)) && holder instanceof GettyViewImageHolder) {
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
        // 새로고침
        else if (item.dataType == GettyData.TYPE_REFRESH) {
            GettyViewRefreshHolder holder = (GettyViewRefreshHolder) holder_;
            int itemH = screenW / 3;
            holder.parent.getLayoutParams().width = screenW;
            holder.parent.getLayoutParams().height = itemH;
            holder.pb.setVisibility(View.GONE);
            holder.parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mActivity.isFinishing()) return;
                    loadPage(holder, null); // 새로고침
                }
            });
        }
        // 진행 바 => 보일 경우 데이터 로딩
        else if (item.dataType == GettyData.TYPE_PROGRESS) {
            GettyViewProgressBarHolder holder = (GettyViewProgressBarHolder) holder_;
            int itemH = screenW / 3;
            holder.parent.getLayoutParams().width = screenW;
            holder.parent.getLayoutParams().height = itemH;
            loadPage(holder, null); // 진행바
        }
    }

    //메모리 해제
    @Override
    public void onViewDetachedFromWindow(@NonNull RecyclerView.ViewHolder holder_) {
        super.onViewDetachedFromWindow(holder_);
        if (holder_ instanceof GettyViewImageHolder) {
            ImageView imageView = ((GettyViewImageHolder) holder_).iv;
            Glide.with(mActivity).clear(imageView);
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

    private void payloadImage(RecyclerView.ViewHolder holder_, int position) {
        GettyViewImageHolder holder = (GettyViewImageHolder) holder_;
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

        //Preload 마지막 개수 6개 아이템이 보일 때 미리 로딩해서 성능 향상 시키기 (Glide cache)
        if (position == resultLists.size() - 6) {
            List<GettyData> list = resultLists.subList(position, resultLists.size());
            for (GettyData gettyData : list) {
                Glide.with(mActivity).load(gettyData.url).preload(itemW, itemW);
            }
        }
    }

    public interface EndLoadPageCallback {
        void end();
    }

    public void loadPage(@Nullable RecyclerView.ViewHolder holder_, @Nullable EndLoadPageCallback endLoadPageCallback) {
        if (isLoading) {
            return;
        }
        //새로고침 이미지 -> 로딩중으로 UI 변경
        if (holder_ instanceof GettyViewRefreshHolder) {
            ((GettyViewRefreshHolder) holder_).tv_refresh.setVisibility(View.GONE);
            ((GettyViewRefreshHolder) holder_).pb.setVisibility(View.VISIBLE);
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
                if (progressIndex >= 0) {
                    pbItem = resultLists.get(progressIndex);
                    pbItem.dataType = GettyData.TYPE_PROGRESS;
                    resultLists.remove(progressIndex);
                }
                //진행 바 존재 X => 진행 바 생성
                else {
                    pbItem = new GettyData("", GettyData.TYPE_PROGRESS);
                }

                //맨 끝 데이터에 새로운 리스트, 진행바 추가
                resultLists.addAll(resultLists_);
                resultLists.add(pbItem);

                int notifyLength = resultLists.size() - progressIndex;
                isLoading = false;
                notifyItemRangeChanged(progressIndex, notifyLength, PAYLOAD_IMAGE);
                if (endLoadPageCallback != null) {
                    endLoadPageCallback.end();
                }
            }

            //로딩 실패
            @Override
            public void error(String e) {
                //인터넷 연결 안되어있을시
                if (!NetworkManager.isConnect(mActivity)) {
                    ToastUtils.showToast(mActivity, mActivity.getResources().getString(R.string.weak_network));
                }
                //통신 에러
                else {
                    ToastUtils.showToastS(mActivity, e);
                }
                isLoading = false;
                pageIndex--;
                int progressIndex = resultLists.size() - 1;

                //새로고침 로딩 실패: UI만 원래대로 변경
                if (holder_ instanceof GettyViewRefreshHolder) {
                    ((GettyViewRefreshHolder) holder_).tv_refresh.setVisibility(View.VISIBLE);
                    ((GettyViewRefreshHolder) holder_).pb.setVisibility(View.GONE);
                    return;
                }

                GettyData gettyData;
                //진행바 존재 O => 새로고침 이미지로 교체
                if (progressIndex >= 0) {
                    gettyData = resultLists.get(progressIndex);
                    gettyData.dataType = GettyData.TYPE_REFRESH;
                }
                //진행바 존재 X => 새로고침 이미지 추가
                else {
                    gettyData = new GettyData("", GettyData.TYPE_REFRESH);
                    progressIndex = 0;
                    resultLists.add(gettyData);
                }

                notifyItemChanged(progressIndex);

                if (endLoadPageCallback != null) {
                    endLoadPageCallback.end();
                }
            }
        });
    }//end add page
}
