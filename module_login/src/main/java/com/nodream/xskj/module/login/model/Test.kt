package com.nodream.xskj.module.login.model

import android.util.Log

import com.nodream.xskj.commonlib.net.BaseObserver
import com.nodream.xskj.commonlib.net.BaseResponse
import com.nodream.xskj.commonlib.net.NetClient
import com.nodream.xskj.module.login.LoginContract
import com.orhanobut.logger.Logger

import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


/**
 * Created by nodream on 2017/11/29.
 */

class Test : LoginContract.Model {
    override fun setUserName() {

    }

    override fun setToken() {

    }

    override fun postLogin(loginRequest: LoginRequest): Boolean {
        NetClient.getInstance().createBaseApi().httpGet(
                "",
                object : BaseObserver<LoginResponse>() {

                    override fun onSuccess(`object`: LoginResponse) {

                    }
                })
        return false
    }
}
