package com.rsupport.mobile1.test.utils

import android.content.Context
import android.widget.Toast
import com.rsupport.mobile1.test.utils.ToastUtils

object ToastUtils {
    var toast: Toast? = null
    @JvmStatic
    fun showToast(context: Context?, str: String?) {
        if (context == null) {
            return
        }
        val toast = Toast.makeText(context, str, Toast.LENGTH_SHORT)
        toast.show()
    }

    //메시지 덮어서 표시
    @JvmStatic
    fun showToastS(context: Context?, str: String?) {
        if (context == null) {
            return
        }
        if (toast != null) {
            toast!!.cancel()
        }
        toast = Toast.makeText(context, str, Toast.LENGTH_SHORT)
        with(toast) { this?.show() }
    }
}