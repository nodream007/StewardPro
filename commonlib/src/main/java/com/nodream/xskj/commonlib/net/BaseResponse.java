package com.nodream.xskj.commonlib.net;

/**
 * Created by nodream on 2017/11/15.
 */

public class BaseResponse<T> {

    private int resultCode;
    private String resultMsg;
    private T data;

    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

    public String getResultMsg() {
        return resultMsg;
    }

    public void setResultMsg(String resultMsg) {
        this.resultMsg = resultMsg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public boolean isOK() {
        return resultCode == 2000;
    }
}
