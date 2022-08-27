package com.rsupport.mobile1.test.utils;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.rsupport.mobile1.test.R;


public class ToastUtils {
    static Toast toast;

    public static void showToast(Context context, String str) {
        if (context == null) {
            return;
        }
        Toast toast = Toast.makeText(context, str, Toast.LENGTH_SHORT);
        toast.show();
    }

    public static void showToast(Context context, int str) {
        if (context == null) {
            return;
        }
        Toast toast = Toast.makeText(context, str, Toast.LENGTH_SHORT);
        toast.show();
    }

    public static void showToastCenter_long(Context context, int str) {
        if (context == null) {
            return;
        }
        Toast toast = Toast.makeText(context, str, Toast.LENGTH_SHORT);
        toast.show();
    }


    public static void toastS(Context context, int str) {
        if (context == null) {
            return;
        }
        if (toast != null) {
            toast.cancel();
        }
        toast = Toast.makeText(context, str, Toast.LENGTH_SHORT);
        toast.show();
    }


}
