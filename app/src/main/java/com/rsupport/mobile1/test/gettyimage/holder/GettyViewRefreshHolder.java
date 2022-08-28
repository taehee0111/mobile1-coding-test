package com.rsupport.mobile1.test.gettyimage.holder;

import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rsupport.mobile1.test.R;

public class GettyViewRefreshHolder extends RecyclerView.ViewHolder {

    public final View parent;
    public final TextView tv_refresh;
    public ProgressBar pb;

    public GettyViewRefreshHolder(@NonNull View itemView, int viewType) {
        super(itemView);

        parent = itemView.findViewById(R.id.parent);
        tv_refresh = itemView.findViewById(R.id.tv_refresh);
        pb = itemView.findViewById(R.id.pb);
    }
}
