package com.rsupport.mobile1.test.gettyimage.manager

import com.rsupport.mobile1.test.gettyimage.model.GettyImageModel__
import android.app.Activity
import android.os.AsyncTask
import com.rsupport.mobile1.test.R
import kotlin.Throws
import android.text.TextUtils
import android.util.Log
import com.rsupport.mobile1.test.viewmodel.GettyImageViewModel
import org.jsoup.Jsoup
import java.io.IOException
import java.lang.Exception
import java.util.ArrayList

class GetGettyImageManager {
    private val TAG = GetGettyImageManager::class.java.simpleName
    private var resultLists: ArrayList<GettyImageModel__>? = null
    private var getGettyImageCallback: GetGettyImageCallback? = null
    private var position = 0
    private var mActivity: Activity? = null

    fun getGettyImage(
        mActivity: Activity?,
        position: Int,
        onOkListener
    ) {
        this.mActivity = mActivity
        this.getGettyImageCallback = getGettyImageCallback
        this.position = position
        if (position < 1) {
            return
        }
        GetGettyImageAsync().execute()
    }


    fun getGettyCallback(end: (GettyImageViewModel) -> Unit) {

    }

    interface GetGettyImageCallback {
        fun end(resultLists: ArrayList<GettyImageModel__>?)
        fun error(errorMsg: String?)
    }

    internal inner class GetGettyImageAsync : AsyncTask<Void?, Void?, Void?>() {
        override fun doInBackground(vararg p0: Void?): Void? {
            try {
                resultLists = getGettyImage_Page(position)
            } catch (e: Exception) {
                Log.e(TAG, e.toString())
                //todo FirebaseCrashlytics 예외처리 추가 필요
            }
            return null
        }

        override fun onPostExecute(result: Void?) {
            if (getGettyImageCallback != null) {
                if (resultLists != null && resultLists!!.size > 0) {
                    getGettyImageCallback!!.end(resultLists)
                } else {
                    getGettyImageCallback!!.error(mActivity!!.resources.getString(R.string.getty_no_item))
                }
            }
            super.onPostExecute(result)
        }
    }

    //position: 페이지 위치
    //return: 이미지 리스트
    @Throws(IOException::class)
    private fun getGettyImage_Page(position: Int): ArrayList<GettyImageModel__>? {
        val gettyImageModelLists = ArrayList<GettyImageModel__>()
        val url =
            "https://www.gettyimages.com/photos/collaboration?assettype=image&sort=mostpopular&phrase=collaboration&license=rf%2Crm&page=$position"
        if (TextUtils.isEmpty(url)) {
            return null
        }
        try {
            val doc = Jsoup.connect(url).get()
            val mosaicAsset_module__figure___qJh1Q =
                doc.select(".MosaicAsset-module__figure___qJh1Q")
            for (i in mosaicAsset_module__figure___qJh1Q.indices) {
                val element = mosaicAsset_module__figure___qJh1Q[i]
                //
                val pictureElements = element.getElementsByTag("picture")
                if (pictureElements == null || pictureElements.size <= 0) {
                    continue
                }
                val pictureElement = pictureElements[0]
                //origin
                val sourceElements = pictureElement.getElementsByTag("source")
                //thumb
                if (sourceElements == null || sourceElements.size <= 0) {
                    continue
                }
                val source = sourceElements[0]
                val srcSet = source.getElementsByAttribute("srcset")
                if (srcSet == null || srcSet.size <= 0) {
                    continue
                }
                val srcSetText = srcSet.attr("srcset")
                val gettyImageModel = GettyImageModel__(srcSetText, GettyImageModel__.TYPE_URL)
                gettyImageModelLists.add(gettyImageModel)
            }
        } catch (e: IOException) {
            throw e
        }
        return gettyImageModelLists
    }
}