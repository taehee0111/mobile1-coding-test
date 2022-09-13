package com.rsupport.mobile1.test.utils

import android.app.Activity
import android.content.Context
import android.graphics.Point
import android.util.TypedValue
import com.rsupport.mobile1.test.utils.SizeUtil

object SizeUtil {
    @JvmStatic
    fun getScreenSize(activity: Activity): Point {
        val display = activity.windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        return size
    }

    fun getDpOfScreen(activity: Activity?, screenRatio: Float): Float {
        if (activity == null || activity.isFinishing) {
            return screenRatio
        }
        val ScreenSize = getScreenSize(activity)
        val density = activity.resources.displayMetrics.density
        val standardSize_X = (ScreenSize.x / density).toInt()
        val galaxyS10_Ratio = 411 / screenRatio
        return Math.abs(standardSize_X / galaxyS10_Ratio)
    }

    fun getPixel_of_dp(activity: Activity, screenDp: Float): Int {
        val spW = getDpOfScreen(activity, screenDp)
        return dpToPx(activity, spW)
    }

    fun dpToPx(ctx: Context, dp: Float): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp,
            ctx.resources.displayMetrics
        ).toInt()
    }
}