package com.nodream.xskj.commonlib.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by nodream on 2017/11/21.
 */

public class ToastUtil {

    private static Toast mToast = null;

    public static void showToast(Context context, int id, String str) {
        if (str == null || context == null) {
            return;
        }
        if (mToast == null) {
            mToast = Toast.makeText(context,context.getString(id) + str, Toast.LENGTH_SHORT);

        } else {
            mToast.setText(context.getString(id) + str);
        }
        mToast.show();
    }

    public static void showToast(Context context, String str) {
        if (str == null || context == null) {
            return;
        }
        if (mToast == null) {
            mToast = Toast.makeText(context, str, Toast.LENGTH_SHORT);

        } else {
            mToast.setText(str);
        }
        mToast.show();
    }
}
