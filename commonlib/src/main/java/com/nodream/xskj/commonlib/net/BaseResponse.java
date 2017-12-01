package com.nodream.xskj.commonlib.net;

/**
 * Created by nodream on 2017/11/15.
 */

public class BaseResponse<T> {

    private int code;
    private String msg;
    private T data;

    public int getResultCode() {
        return code;
    }

    public void setResultCode(int resultCode) {
        this.code = resultCode;
    }

    public String getResultMsg() {
        return msg;
    }

    public void setResultMsg(String resultMsg) {
        this.msg = resultMsg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public boolean isOK() {
        return code == 5000;
    }
}
