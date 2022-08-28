package com.rsupport.mobile1.test.gettyimage.holder;

import android.app.MediaRouteButton;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rsupport.mobile1.test.R;

public class GettyViewImageHolder extends RecyclerView.ViewHolder {
    public final ViewGroup parent;
    public final ImageView iv;
    public ProgressBar pb;

    public GettyViewImageHolder(@NonNull View itemView, int viewType) {
        super(itemView);
        parent = itemView.findViewById(R.id.parent);
        iv = itemView.findViewById(R.id.iv);
        pb = itemView.findViewById(R.id.pb);
    }
}
