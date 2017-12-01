package com.nodream.xskj.commonlib.net;

/**
 * Created by nodream on 2017/11/20.
 */

public abstract class NetCallBack {

    public void onStart(){}

    public void onCompleted(){}

    abstract public void onError(Throwable e);

    public void onProgress(long fileSizeDownloaded){}

    abstract public void onSucess(String path, String name, long fileSize);
}
