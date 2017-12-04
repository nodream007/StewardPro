package com.nodream.xskj.module.register.presenter

import com.nodream.xskj.module.register.RegisterContract
import com.nodream.xskj.module.register.model.RegisterModel
import com.nodream.xskj.module.register.model.RegisterRequest

/**
 * Created by nodream on 2017/12/4.
 */
class RegisterPresenter(val view: RegisterContract.View): RegisterContract.Presenter {

    private val mRegisterModel: RegisterModel = RegisterModel()

    override fun start() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun register() {
        view.showProgress(true)
        val mRegisterRequest = RegisterRequest()
        mRegisterRequest.phoneNumber = view.getPhoneNumber()
        mRegisterRequest.inviteCode = view.getInviteCode()
        mRegisterRequest.passWord = view.getPassword()
        mRegisterRequest.verCode = view.getVerCode()
        mRegisterModel.postRegister(mRegisterRequest)
    }
}