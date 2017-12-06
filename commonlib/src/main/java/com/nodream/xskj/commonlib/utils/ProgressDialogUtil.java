package com.nodream.xskj.commonlib.utils;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * Created by nodream on 2017/12/6.
 */

public class ProgressDialogUtil {

    private static ProgressDialog progressDialog;

    public static void showProgressDialog(Context context) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("请稍后");
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();
    }

    public static void closeProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }
}
