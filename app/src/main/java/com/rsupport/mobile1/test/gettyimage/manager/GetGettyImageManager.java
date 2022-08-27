package com.rsupport.mobile1.test.gettyimage.manager;

import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.Nullable;

import com.rsupport.mobile1.test.gettyimage.model.PassingData;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class GetGettyImageManager {

    private String TAG = GetGettyImageManager.class.getSimpleName();
    @Nullable
    private ArrayList<PassingData> resultLists;

    private GetGettyImageCallback getGettyImageCallback;
    private int position;

    public void getGettyImage(int position, GetGettyImageCallback getGettyImageCallback) {
        this.getGettyImageCallback = getGettyImageCallback;
        this.position = position;

        new GetGettyImageAsync().execute();
    }

    public interface GetGettyImageCallback {
        void end(ArrayList<PassingData> resultLists);

        void error(String errorMsg);
    }

    class GetGettyImageAsync extends AsyncTask<Void, Void, Void> {


        @Override
        protected Void doInBackground(Void... voids) {
            try {
                resultLists = getGettyImage(position);
                Log.d(TAG, "");
            } catch (Exception e) {
                Log.e(TAG, "e");
                //todo FirebaseCrashlytics 예외처리 추가 필요
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            if (getGettyImageCallback != null) {
                if (resultLists != null && resultLists.size() > 0) {
                    getGettyImageCallback.end(resultLists);
                } else {
                    getGettyImageCallback.error("상품 정보를 받아오지 못했습니다.");
                }
            }
            super.onPostExecute(result);
        }
    }

    //position: 페이지 위치
    //return: 이미지 리스트
    private ArrayList<PassingData> getGettyImage(int position) throws IOException {
        ArrayList<PassingData> passingDataLists = new ArrayList<>();

        String url = "https://www.gettyimages.com/photos/collaboration?assettype=image&sort=mostpopular&phrase=collaboration&license=rf%2Crm&page=" + position;
//        String url = "";

        if (TextUtils.isEmpty(url)) {
            return null;
        }
        try {
            //get 방식
            Document doc = Jsoup.connect(url).get();
//            Elements mosaicAsset_module__figure___qJh1Q = doc.getElementsByClass("MosaicAsset-module__figure___qJh1Q");
            Elements mosaicAsset_module__figure___qJh1Q = doc.select(".MosaicAsset-module__figure___qJh1Q");
            for (int i = 0; i < mosaicAsset_module__figure___qJh1Q.size(); i++) {
                Element element = mosaicAsset_module__figure___qJh1Q.get(i);
                //
                Elements pictureElements = element.getElementsByTag("picture");
                if (pictureElements == null || pictureElements.size() <= 0) {
                    continue;
                }
                Element pictureElement = pictureElements.get(0);
                //origin
                Elements sourceElements = pictureElement.getElementsByTag("source");
                //thumb
//                Elements img = pictureElement.getElementsByTag("img");
                if (sourceElements == null || sourceElements.size() <= 0) {
                    continue;
                }
                Element source = sourceElements.get(0);
                Elements srcSet = source.getElementsByAttribute("srcset");
                //Elements getElementsByClass = source.getElementsByClass("MosaicAsset-module__thumb___yvFP5");
                if (srcSet == null || srcSet.size() <= 0) {
                    continue;
                }

                String srcSetText = srcSet.attr("srcset");
                PassingData passingData = new PassingData(srcSetText, PassingData.TYPE_URL);
                passingDataLists.add(passingData);
                Log.d(TAG, "");
            }
        } catch (IOException e) {
            throw e;
        }
        return passingDataLists;
    }
}
