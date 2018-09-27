package com.nodream.xskj.module.register.model

import android.content.Context
import com.nodream.xskj.commonlib.net.BaseObserver
import com.nodream.xskj.commonlib.net.NetClient
import com.nodream.xskj.module.register.RegisterContract
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Created by nodream on 2017/12/4.
 */
class RegisterModel : RegisterContract.Model {

    override fun postRegister(registerRequest: RegisterRequest, modelCallBack: ModelCallBack,
                              context: Context) {
        val parmsMap = mutableMapOf<String, String?>()
        parmsMap["mobile"] = registerRequest.mobile
        parmsMap["password"] = registerRequest.passWord
        parmsMap["verifyCode"] = registerRequest.verCode
        NetClient.getInstance(context).create(RegisterService::class.java)
                .register("user/register", parmsMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : BaseObserver<RegisterResponse>(context) {
                    override fun onSuccess(registerResponse: RegisterResponse) {
                        modelCallBack.onSuccess(registerResponse)
                    }
                })
    }

    interface ModelCallBack {
        fun onSuccess(registerResponse: RegisterResponse)
    }

}