package com.nodream.xskj.module.login.model

import com.nodream.xskj.commonlib.net.BaseObserver
import com.nodream.xskj.commonlib.net.NetClient
import com.nodream.xskj.module.login.LoginContract

/**
 * Created by nodream on 2017/11/27.
 */
class LoginModel: LoginContract.Model {


    override fun postLogin(loginRequest: LoginRequest): Boolean {
        NetClient.getInstance().createBaseApi().httpGet(
                "",
                object : BaseObserver<LoginResponse>() {
                    override fun onSuccess(`object`: LoginResponse) {

                    }
                })
        return true
    }

    override fun setUserName() {
    }

    override fun setToken() {
    }
}