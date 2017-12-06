package com.nodream.xskj.module.login.model

import android.content.Context
import com.nodream.xskj.commonlib.net.BaseObserver
import com.nodream.xskj.commonlib.net.NetClient
import com.nodream.xskj.module.login.LoginContract
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Created by nodream on 2017/11/27.
 */
class LoginModel : LoginContract.Model {

    override fun postLogin(loginRequest: LoginRequest, modelCallBack: ModelCallBack
    ,context: Context) {
        val parmsMap = mutableMapOf<String, String>()
        parmsMap["mobile"] = loginRequest.mobile
        parmsMap["password"] = loginRequest.passWord
        NetClient.getInstance().create(LoginService::class.java)
                .login("user/login", parmsMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : BaseObserver<LoginResponse>(context) {
                    override fun onSuccess(loginResponse: LoginResponse) {
                        modelCallBack.onSuccess(loginResponse)
                    }
                })
    }

    override fun saveUserId(id: Int) {
    }

    override fun saveToken(token: String) {
    }

    interface ModelCallBack {
        fun onSuccess(loginResponse: LoginResponse)
    }
}