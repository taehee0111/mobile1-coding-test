package com.rsupport.mobile1.test.gettyimage.holder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import android.widget.TextView
import android.widget.ProgressBar
import com.rsupport.mobile1.test.R

class GettyViewRefreshHolder(itemView: View, viewType: Int) : RecyclerView.ViewHolder(itemView) {
    val parent: View
    val tv_refresh: TextView
    var pb: ProgressBar

    init {
        parent = itemView.findViewById(R.id.parent)
        tv_refresh = itemView.findViewById(R.id.tv_refresh)
        pb = itemView.findViewById(R.id.pb)
    }
}