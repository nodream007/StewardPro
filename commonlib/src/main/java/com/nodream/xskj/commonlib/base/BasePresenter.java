package com.nodream.xskj.commonlib.base;

/**
 * Created by nodream on 2017/12/8.
 */

public class BasePresenter<V extends IBaseView> {

    private V view;

    /**
     * 绑定V层
     * @param view
     */
    public void attachView(V view){
        this.view = view;
    }

    /**
     * 解除绑定V层
     */
    public void detachView(){
        view = null;
    }

    /**
     * 获取V层
     * @return
     */
    public V getView() {
        return view;
    }
}
