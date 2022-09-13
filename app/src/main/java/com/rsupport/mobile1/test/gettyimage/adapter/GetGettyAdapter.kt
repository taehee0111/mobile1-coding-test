package com.rsupport.mobile1.test.gettyimage.adapter

import com.rsupport.mobile1.test.utils.SizeUtil.getScreenSize
import com.rsupport.mobile1.test.utils.manager.NetworkManager.Companion.isConnect
import com.rsupport.mobile1.test.utils.ToastUtils.showToast
import com.rsupport.mobile1.test.utils.ToastUtils.showToastS
import com.rsupport.mobile1.test.activity.MainActivity
import androidx.recyclerview.widget.RecyclerView
import com.rsupport.mobile1.test.gettyimage.model.GettyImageModel__
import android.view.ViewGroup
import android.view.LayoutInflater
import com.rsupport.mobile1.test.R
import com.rsupport.mobile1.test.gettyimage.holder.GettyViewImageHolder
import com.rsupport.mobile1.test.gettyimage.holder.GettyViewProgressBarHolder
import com.rsupport.mobile1.test.gettyimage.holder.GettyViewRefreshHolder
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.rsupport.mobile1.test.gettyimage.manager.GetGettyImageManager
import com.rsupport.mobile1.test.gettyimage.manager.GetGettyImageManager.GetGettyImageCallback
import androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
import com.bumptech.glide.request.transition.Transition
import java.util.ArrayList

class GetGettyAdapter(private val mActivity: MainActivity) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val TAG = GetGettyAdapter::class.java.simpleName
    val gridLayoutManager: GridLayoutManager
    var resultLists: ArrayList<GettyImageModel__>?
    private val screenW: Int
    var isLoading = false
    var pageIndex = 0
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        // 이미지
        if (viewType == GettyImageModel__.TYPE_URL) {
            val view =
                LayoutInflater.from(mActivity).inflate(R.layout.getty_holder_item, parent, false)
            return GettyViewImageHolder(view, viewType)
        } else if (viewType == GettyImageModel__.TYPE_PROGRESS) {
            val view = LayoutInflater.from(mActivity)
                .inflate(R.layout.getty_holder_item_progress, parent, false)
            return GettyViewProgressBarHolder(view, viewType)
        } else if (viewType == GettyImageModel__.TYPE_REFRESH) {
            val view = LayoutInflater.from(mActivity)
                .inflate(R.layout.getty_holder_item_refresh, parent, false)
            return GettyViewRefreshHolder(view, viewType)
        }
        val view = LayoutInflater.from(mActivity).inflate(R.layout.getty_holder_item, parent, false)
        return GettyViewImageHolder(view, viewType)
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
        payloads: List<Any>
    ) {
        super.onBindViewHolder(holder, position, payloads)
        for (i in payloads.indices) {
            if (PAYLOAD_IMAGE == payloads[i] && holder is GettyViewImageHolder) {
                payloadImage(holder, position)
                return
            }
        }
        onBindViewHolder(holder, position)
    }

    override fun onBindViewHolder(holder_: RecyclerView.ViewHolder, position: Int) {
        if (mActivity.isFinishing) return
        val item = resultLists!![position]
        // 이미지
        if (item.dataType == GettyImageModel__.TYPE_URL) {
            payloadImage(holder_, position)
        } else if (item.dataType == GettyImageModel__.TYPE_REFRESH) {
            val holder = holder_ as GettyViewRefreshHolder
            val itemH = screenW / 3
            holder.parent.layoutParams.width = screenW
            holder.parent.layoutParams.height = itemH
            holder.pb.visibility = View.GONE
            holder.parent.setOnClickListener(View.OnClickListener {
                if (mActivity.isFinishing) return@OnClickListener
                loadPage(holder, null) // 새로고침
            })
        } else if (item.dataType == GettyImageModel__.TYPE_PROGRESS) {
            val holder = holder_ as GettyViewProgressBarHolder
            val itemH = screenW / 3
            holder.parent.layoutParams.width = screenW
            holder.parent.layoutParams.height = itemH
            loadPage(holder, null) // 진행바
        }
    }

    //메모리 해제
    override fun onViewDetachedFromWindow(holder_: RecyclerView.ViewHolder) {
        super.onViewDetachedFromWindow(holder_)
        if (holder_ is GettyViewImageHolder) {
            val imageView = holder_.iv
            Glide.with(mActivity).clear(imageView)
        }
    }

    override fun getItemCount(): Int {
        if (mActivity.isFinishing) {
            return 0
        } else {
            if (resultLists != null) {
                return resultLists!!.size
            }
        }
        return 0
    }

    override fun getItemViewType(position: Int): Int {
        if (!mActivity.isFinishing) {
            if (resultLists != null && resultLists!!.size > 0) {
                val item = resultLists!![position]
                return item.dataType
            }
        }
        return GettyImageModel__.TYPE_NONE
    }

    private fun payloadImage(holder_: RecyclerView.ViewHolder, position: Int) {
        val holder = holder_ as GettyViewImageHolder
        Glide.with(mActivity).clear(holder.iv)
        val item = resultLists!![position]
        val itemW = screenW / 3
        holder.parent.layoutParams.width = itemW
        holder.parent.layoutParams.height = itemW
        holder.iv.visibility = View.GONE
        holder.pb.visibility = View.VISIBLE
        Glide.with(mActivity)
            .load(item.url)
            .override(itemW)
            .into(object : CustomTarget<Drawable?>() {
                override fun onResourceReady(
                    resource: Drawable,
                    transition: Transition<in Drawable?>?
                ) {
                    val holderPosition = holder.adapterPosition
                    if (holderPosition == position) {
                        holder.iv.setImageDrawable(resource)
                        holder.pb.visibility = View.GONE
                        holder.iv.visibility = View.VISIBLE
                    } else {
                        Log.d(TAG, "holderPosition:" + holderPosition + "pageIndex:" + pageIndex)
                    }
                }

                override fun onLoadCleared(placeholder: Drawable?) {}
                override fun onLoadFailed(errorDrawable: Drawable?) {
                    super.onLoadFailed(errorDrawable)
                    if (holder.adapterPosition == position) {
                        holder.pb.visibility = View.GONE
                        holder.iv.visibility = View.VISIBLE
                    }
                }
            })

        //Preload 마지막 개수 6개 아이템이 보일 때 미리 로딩해서 성능 향상 시키기 (Glide cache)
        if (position == resultLists!!.size - 6) {
            val list: List<GettyImageModel__> = resultLists!!.subList(position, resultLists!!.size)
            for (gettyData in list) {
                Glide.with(mActivity).load(gettyData.url).preload(itemW, itemW)
            }
        }
    }

    interface EndLoadPageCallback {
        fun end()
    }

    fun loadPage(holder_: RecyclerView.ViewHolder?, endLoadPageCallback: EndLoadPageCallback?) {
        if (isLoading) {
            return
        }
        //새로고침 이미지 -> 로딩중으로 UI 변경
        if (holder_ is GettyViewRefreshHolder) {
            holder_.tv_refresh.visibility = View.GONE
            holder_.pb.visibility = View.VISIBLE
        }
        Log.d(TAG, "pageIndex:$pageIndex")
        isLoading = true
        pageIndex++
        GetGettyImageManager().getGettyImage(mActivity, pageIndex) {}
        GetGettyImageManager().getGettyImage(mActivity, pageIndex, GetGettyImageCallback {
            fun end(resultLists_: ArrayList<GettyImageModel__>) {
                val progressIndex = resultLists!!.size - 1
                val pbItem: GettyImageModel__
                //진행 바 존재 O => 존재 시 제거
                if (progressIndex >= 0) {
                    pbItem = resultLists!![progressIndex]
                    pbItem.dataType = GettyImageModel__.TYPE_PROGRESS
                    resultLists!!.removeAt(progressIndex)
                } else {
                    pbItem = GettyImageModel__("", GettyImageModel__.TYPE_PROGRESS)
                }

                //맨 끝 데이터에 새로운 리스트, 진행바 추가
                resultLists!!.addAll(resultLists_)
                resultLists!!.add(pbItem)
                val notifyLength = resultLists!!.size - progressIndex
                isLoading = false
                notifyItemRangeChanged(progressIndex, notifyLength, PAYLOAD_IMAGE)
                endLoadPageCallback?.end()
            }

            //로딩 실패
            fun error(e: String) {
                //인터넷 연결 안되어있을시
                if (!isConnect(mActivity)) {
                    showToast(mActivity, mActivity.resources.getString(R.string.weak_network))
                } else {
                    showToastS(mActivity, e)
                }
                isLoading = false
                pageIndex--
                var progressIndex = resultLists!!.size - 1

                //새로고침 로딩 실패: UI만 원래대로 변경
                if (holder_ is GettyViewRefreshHolder) {
                    holder_.tv_refresh.visibility = View.VISIBLE
                    holder_.pb.visibility = View.GONE
                    return
                }
                val gettyImageModel: GettyImageModel__
                //진행바 존재 O => 새로고침 이미지로 교체
                if (progressIndex >= 0) {
                    gettyImageModel = resultLists!![progressIndex]
                    gettyImageModel.dataType = GettyImageModel__.TYPE_REFRESH
                } else {
                    gettyImageModel = GettyImageModel__("", GettyImageModel__.TYPE_REFRESH)
                    progressIndex = 0
                    resultLists!!.add(gettyImageModel)
                }
                notifyItemChanged(progressIndex)
                endLoadPageCallback?.end()
            }
        })
    } //end add page

    companion object {
        const val PAYLOAD_IMAGE = "PAYLOAD_IMAGE"
    }

    init {
        resultLists = ArrayList()
        val screenSize = getScreenSize(mActivity)
        screenW = screenSize.x
        gridLayoutManager = GridLayoutManager(mActivity, 3)
        mActivity.rv!!.layoutManager = gridLayoutManager
        mActivity.rv!!.adapter = this
        gridLayoutManager.spanSizeLookup = object : SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                val loadingPos = resultLists!![position]
                //프로그래스바, 새로고침
                return if (loadingPos.dataType == GettyImageModel__.TYPE_PROGRESS ||
                    loadingPos.dataType == GettyImageModel__.TYPE_REFRESH
                ) {
                    3
                } else {
                    1
                }
            }
        }
    }
}