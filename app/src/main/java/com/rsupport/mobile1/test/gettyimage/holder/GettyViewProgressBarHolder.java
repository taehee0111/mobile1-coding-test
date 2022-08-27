package com.rsupport.mobile1.test.gettyimage.holder;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rsupport.mobile1.test.R;

public class GettyViewProgressBarHolder extends RecyclerView.ViewHolder {
    public final View parent;

    public GettyViewProgressBarHolder(@NonNull View itemView, int viewType) {
        super(itemView);
        parent= itemView.findViewById(R.id.parent);
    }
}
