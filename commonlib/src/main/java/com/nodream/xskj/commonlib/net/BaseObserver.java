package com.nodream.xskj.commonlib.net;

import android.app.Activity;
import android.content.Context;
import android.os.Looper;
import android.util.Log;

import com.nodream.xskj.commonlib.utils.ProgressDialogUtil;
import com.nodream.xskj.commonlib.utils.ToastUtil;
import com.orhanobut.logger.Logger;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by nodream on 2017/11/21.
 */

public abstract class BaseObserver<T> implements Observer<BaseResponse<T>> {

    private static final String TAG = "BaseObserver";
    private Context context;

    protected BaseObserver(Context context) {
        this.context = context.getApplicationContext();
    }

    @Override
    public void onSubscribe(Disposable d) {
//        ProgressDialogUtil.showProgressDialog(context);
    }

    @Override
    public void onNext(BaseResponse<T> tBaseResponse) {
        ProgressDialogUtil.closeProgressDialog();
        if (tBaseResponse.isOK()) {
            onSuccess(tBaseResponse.getData());
        } else {
            onErrorShow(tBaseResponse.getResultMsg());
        }
    }

    @Override
    public void onError(Throwable e) {
        ProgressDialogUtil.closeProgressDialog();
        Logger.e(e,e.getMessage());
        onErrorHandle(e);
    }

    @Override
    public void onComplete() {

    }

    protected abstract void onSuccess(T object);

    public void onErrorHandle(Throwable e) {
        ExceptionHandle.ResponseThrowable responseThrowable = ExceptionHandle.handleException(e);
        ToastUtil.showToast(context,responseThrowable.message);
    }

    protected void onErrorShow(String msg) {
        ToastUtil.showToast(context,msg);
    }
}
