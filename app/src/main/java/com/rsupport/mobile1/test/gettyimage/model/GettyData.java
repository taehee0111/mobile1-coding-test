package com.rsupport.mobile1.test.gettyimage.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class GettyData implements Parcelable {
    public static final int TYPE_NONE = 0;
    //이미지 표시
    public static final int TYPE_URL = 1;
    // 더보기 진행바
    public static final int TYPE_PROGRESS = 2;
    // 더보기 새로고침
//    public static final int TYPE_REFRESH = 3;

    public String url;
    public int dataType;

    public GettyData(String url, int dataType) {
        this.url = url;
        this.dataType = dataType;
    }

    protected GettyData(Parcel in) {
        url = in.readString();
        dataType = in.readInt();
    }



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(url);
        parcel.writeInt(dataType);
    }

    public static final Creator<GettyData> CREATOR = new Creator<GettyData>() {
        @Override
        public GettyData createFromParcel(Parcel in) {
            return new GettyData(in);
        }

        @Override
        public GettyData[] newArray(int size) {
            return new GettyData[size];
        }
    };
}
