package com.rsupport.mobile1.test.gettyimage.manager;

import android.content.Context;
import android.os.Parcelable;
import android.util.AttributeSet;

import androidx.recyclerview.widget.GridLayoutManager;

public class CustomGridLayoutManager extends GridLayoutManager {
    public CustomGridLayoutManager(Context context, int spanCount) {
        super(context, spanCount);
    }

}
