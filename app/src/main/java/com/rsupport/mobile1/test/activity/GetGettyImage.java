package com.rsupport.mobile1.test.activity;

import android.os.AsyncTask;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


import java.io.IOException;
import java.util.ArrayList;

public class GetGettyImage {

    private String TAG = GetGettyImage.class.getSimpleName();

    public GetGettyImage() {
        new GetGettyImageAsync().execute();
    }

    class GetGettyImageAsync extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            getGettyImage();
            return null;
        }
    }

    private ArrayList<PassingData> getGettyImage() {
        ArrayList<PassingData> passingDataLists = new ArrayList<>();

        String URL = "https://www.gettyimages.com/photos/collaboration?assettype=image&sort=mostpopular&phrase=collaboration&license=rf%2Crm&page=1";
        Document doc;
        try {
            doc = Jsoup.connect(URL).get();
            //클래스명 가져오기
//            Elements itemLists = doc.select(".FixedAsset-module__galleryFixedAsset___fmDUm");
            Elements galleryItems_module__searchContent___DbMmK = doc.select(".GalleryItems-module__searchContent___DbMmK");
            for(int i=0; i<galleryItems_module__searchContent___DbMmK.size(); i++){
                Element element = galleryItems_module__searchContent___DbMmK.get(i);
                Log.d(TAG,"");
            }
            Elements Gallery_module__rowContainer___qyf9K = doc.select(".Gallery-module__rowContainer___qyf9K");
            Elements search_containerLists = doc.select(".search_container");

            Log.d(TAG,"");
//            for(int i=0 ;i <itemLists.size(); i++){
//                Element item = itemLists.get(i);
//                Elements elements = item.getAllElements();
//                for(int j =0 ; i<elements.size(); j++){
//                    Element element = elements.get(j);
//                }
//            }
//            Log.d(TAG, "main_body")
            //id명 가져오기
            Elements header_wrapper = doc.select("#header-wrapper");
            //※ class명 안의 태그 가져오기
            Elements tags = doc.select(".main_body a");

        } catch (IOException e) {
            e.printStackTrace();
        }
        return passingDataLists;
    }
}
