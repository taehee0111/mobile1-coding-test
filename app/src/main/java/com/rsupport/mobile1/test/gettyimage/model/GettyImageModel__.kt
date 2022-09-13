package com.rsupport.mobile1.test.gettyimage.model

import android.os.Parcelable
import android.os.Parcel
import android.os.Parcelable.Creator

class GettyImageModel__ : Parcelable {

    var url: String?
    var dataType: Int

    constructor(url: String?, dataType: Int) {
        this.url = url
        this.dataType = dataType
    }

    companion object CREATOR : Creator<GettyImageModel__> {
        const val TYPE_NONE = 0

        //이미지 표시
        const val TYPE_URL = 1

        // 더보기 진행바
        const val TYPE_PROGRESS = 2

        // 더보기 새로고침
        const val TYPE_REFRESH = 3
        override fun createFromParcel(parcel: Parcel): GettyImageModel__ {
            return GettyImageModel__(parcel)
        }

        override fun newArray(size: Int): Array<GettyImageModel__?> {
            return arrayOfNulls(size)
        }
    }


    protected constructor(`in`: Parcel) {
        url = `in`.readString()
        dataType = `in`.readInt()
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(url)
        parcel.writeInt(dataType)
    }



}