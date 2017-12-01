package com.nodream.xskj.commonlib.net;

import android.app.Activity;
import android.content.Context;
import android.os.Looper;
import android.util.Log;

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

    public BaseObserver() {

    }
    protected BaseObserver(Context context) {
        this.context = context.getApplicationContext();
    }

    @Override
    public void onSubscribe(Disposable d) {

    }

    @Override
    public void onNext(BaseResponse<T> tBaseResponse) {
        if (tBaseResponse.isOK()) {
            onSuccess(tBaseResponse.getData());
        } else {
            onErrorShow(tBaseResponse.getResultMsg());
        }
    }
    //    @Override
//    public void onNext(T response) {
//        if (response.isOK()) {
////            String t = response.getResultMsg();
//            onSuccess(response.getData());
//        } else {
//            onErrorShow(response.getResultMsg());
//        }
//    }

    @Override
    public void onError(Throwable e) {
        Logger.e(e,e.getMessage());
        if (e instanceof ExceptionHandle.ResponseThrowable) {
            onErrorHandle((ExceptionHandle.ResponseThrowable) e);
        } else {
            onErrorHandle(new ExceptionHandle.ResponseThrowable(e, ExceptionHandle.ERROR.UNKNOWN));
        }

    }

    @Override
    public void onComplete() {

    }

    protected abstract void onSuccess(T object);

    public void onErrorHandle(ExceptionHandle.ResponseThrowable e) {
        ExceptionHandle.handleException(e);
    };

    protected void onErrorShow(String msg) {
        ToastUtil.showToast(context,msg);
    }
}
