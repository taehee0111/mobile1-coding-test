package com.rsupport.mobile1.test.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.util.TypedValue;
import android.view.Display;

public class SizeUtil {

    public static Point getScreenSize(Activity activity) {
        Display display = activity.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        return size;
    }

    public static float getDpOfScreen(Activity activity, final float screenRatio) {
        if (activity == null || activity.isFinishing()) {
            return screenRatio;
        }
        Point ScreenSize = getScreenSize(activity);
        float density = activity.getResources().getDisplayMetrics().density;

        int standardSize_X = (int) (ScreenSize.x / density);
        float galaxyS10_Ratio = (411 / screenRatio);
        return Math.abs(standardSize_X / galaxyS10_Ratio);
    }

    public static int getPixel_of_dp(Activity activity, final float screenDp) {
        float spW = getDpOfScreen(activity, screenDp);
        return dpToPx(activity, spW);
    }

    public static int dpToPx(Context ctx, float dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, ctx.getResources().getDisplayMetrics());
    }
}
