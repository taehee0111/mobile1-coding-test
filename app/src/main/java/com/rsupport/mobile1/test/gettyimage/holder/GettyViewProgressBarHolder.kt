package com.rsupport.mobile1.test.gettyimage.holder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import android.widget.ProgressBar
import com.rsupport.mobile1.test.R

class GettyViewProgressBarHolder(itemView: View, viewType: Int) :
    RecyclerView.ViewHolder(itemView) {
    val parent: View
    val pb: ProgressBar

    init {
        parent = itemView.findViewById(R.id.parent)
        pb = itemView.findViewById(R.id.pb)
    }
}