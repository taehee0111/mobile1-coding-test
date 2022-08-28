package com.rsupport.mobile1.test.gettyimage.holder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rsupport.mobile1.test.R;

public class GettyViewRefreshHolder extends RecyclerView.ViewHolder {

    public final View parent;
    private final TextView tv;

    public GettyViewRefreshHolder(@NonNull View itemView, int viewType) {
        super(itemView);

        parent = itemView.findViewById(R.id.parent);
        tv = itemView.findViewById(R.id.tv);
    }
}
