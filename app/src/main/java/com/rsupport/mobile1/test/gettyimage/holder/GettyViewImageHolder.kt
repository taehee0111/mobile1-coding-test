package com.rsupport.mobile1.test.gettyimage.holder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import com.rsupport.mobile1.test.R

class GettyViewImageHolder(itemView: View, viewType: Int) : RecyclerView.ViewHolder(itemView) {
    val parent: ViewGroup
    val iv: ImageView
    var pb: ProgressBar

    init {
        parent = itemView.findViewById(R.id.parent)
        iv = itemView.findViewById(R.id.iv)
        pb = itemView.findViewById(R.id.pb)
    }
}