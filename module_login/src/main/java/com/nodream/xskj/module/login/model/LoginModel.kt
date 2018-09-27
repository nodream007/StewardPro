package com.nodream.xskj.module.login.model

import android.content.Context
import com.nodream.xskj.commonlib.net.BaseObserver
import com.nodream.xskj.commonlib.net.NetClient
import com.nodream.xskj.commonlib.utils.SharedPreferencesUtils
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
        parmsMap["username"] = loginRequest.mobile
        parmsMap["password"] = loginRequest.passWord
        NetClient.getInstance(context).create(LoginService::class.java)
                .login("medicalStaff/login", parmsMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : BaseObserver<LoginBean>(context) {
                    override fun onSuccess(loginResponse: LoginBean) {
                        modelCallBack.onSuccess(loginResponse)
                    }
                })
    }

    override fun saveUserId(context: Context, id: Int) {
        val sp = SharedPreferencesUtils(context)
        sp.put("user_id", id)
    }

    override fun saveToken(context: Context, token: String) {
        val sp = SharedPreferencesUtils(context)
        sp.put("token", token)
    }
    override fun saveUserName(context: Context, name: String) {
    }

    interface ModelCallBack {
        fun onSuccess(loginResponse: LoginBean)
    }
}