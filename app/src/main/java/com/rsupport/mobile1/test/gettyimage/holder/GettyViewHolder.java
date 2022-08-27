package com.rsupport.mobile1.test.gettyimage.holder;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rsupport.mobile1.test.R;

public class GettyViewHolder extends RecyclerView.ViewHolder {
    public final ViewGroup parent;
    public final ImageView iv;

    public GettyViewHolder(@NonNull View itemView, int viewType) {
        super(itemView);
        parent = itemView.findViewById(R.id.parent);
        iv = itemView.findViewById(R.id.iv);
    }
}
