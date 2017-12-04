package com.nodream.xskj.module.register.model

import com.nodream.xskj.commonlib.net.BaseObserver
import com.nodream.xskj.commonlib.net.NetClient
import com.nodream.xskj.module.login.model.LoginRequest
import com.nodream.xskj.module.login.model.LoginResponse
import com.nodream.xskj.module.register.RegisterContract

/**
 * Created by nodream on 2017/12/4.
 */
class RegisterModel: RegisterContract.Model{
    override fun postRegister(registerRequest: RegisterRequest): Boolean {
        NetClient.getInstance().createBaseApi().httpPost(
                "",
                null,
                object : BaseObserver<RegisterResponse>() {
                    override fun onSuccess(`object`: RegisterResponse) {

                    }
                })

        return true
    }

}