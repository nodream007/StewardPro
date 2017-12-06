package com.nodream.xskj.module.register.presenter

import android.content.Context
import com.nodream.xskj.module.register.RegisterActivity
import com.nodream.xskj.module.register.RegisterContract
import com.nodream.xskj.module.register.model.RegisterModel
import com.nodream.xskj.module.register.model.RegisterRequest
import com.nodream.xskj.module.register.model.RegisterResponse

/**
 * Created by nodream on 2017/12/4.
 */
class RegisterPresenter(val view: RegisterContract.View, val context: Context): RegisterContract.Presenter , RegisterModel.ModelCallBack{

    override fun onSuccess(registerResponse: RegisterResponse) {
        view.finish()
    }

    private val mRegisterModel: RegisterModel = RegisterModel()

    override fun start(context: Context) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun register() {
        val mRegisterRequest = RegisterRequest(
                view.getPhoneNumber(),
                view.getVerCode(),
                view.getPassword(),
                view.getInviteCode()
        )
        mRegisterModel.postRegister(mRegisterRequest,this,context)
    }
}