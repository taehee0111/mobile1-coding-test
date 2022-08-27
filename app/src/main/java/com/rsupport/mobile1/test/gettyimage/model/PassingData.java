package com.rsupport.mobile1.test.gettyimage.model;

public class PassingData {
    public static final int TYPE_NONE = 0;
    public static final int TYPE_URL = 1;
    public static final int TYPE_PROGRESS = 2;

    public String url;
    public int dataType = TYPE_NONE;

    public PassingData(String url, int dataType) {
        this.url = url;
        this.dataType = dataType;
    }
}
