package com.rsupport.mobile1.test.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rsupport.mobile1.test.gettyimage.model.GettyImageModel__
//https://developer.android.com/topic/libraries/architecture/viewmodel?hl=ko
class GettyImageViewModel : ViewModel() {

    private val gettyImage: MutableLiveData<List<GettyImageModel__>> by lazy {
//        MutableLiveData<List<GettyImageData>>()
        MutableLiveData<List<GettyImageModel__>>().also {
            loadGettyImage()
        }
    }

    fun getGettyImage(): LiveData<List<GettyImageModel__>> {
        return gettyImage
    }

    private fun loadGettyImage() {

    }
}