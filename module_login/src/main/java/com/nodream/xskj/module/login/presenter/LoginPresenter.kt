package com.nodream.xskj.module.login.presenter

import android.content.Context
import android.util.Log
import com.nodream.xskj.commonlib.base.BasePresenter
import com.nodream.xskj.module.login.LoginContract
import com.nodream.xskj.module.login.model.LoginBean
import com.nodream.xskj.module.login.model.LoginModel
import com.nodream.xskj.module.login.model.LoginRequest
import com.nodream.xskj.module.login.model.LoginResponse

/**
 * Created by nodream on 2017/11/27.
 */
class LoginPresenter(private val context: Context) : LoginContract.Presenter,
        BasePresenter<LoginContract.View>(),
        LoginModel.ModelCallBack {


    override fun onSuccess(loginResponse: LoginBean) {
        mLoginModel.saveUserId(context, loginResponse.id)
        mLoginModel.saveUserName(context,loginResponse.name)
        mLoginModel.saveToken(context,loginResponse.token)
        Log.e("token:", loginResponse.token)
        loginSuccess()
    }

    private val mLoginModel: LoginModel = LoginModel()


    override fun login() {
//        view.showProgress(true)
        val mLoginRequest = LoginRequest(view.getUserNameFormView(),
                view.getPassWordFormView())
        mLoginModel.postLogin(mLoginRequest, this, context)
    }

    override fun loginSuccess() {
        view.goToHomeView()
    }

    override fun loginFail(str: String) {
        view.showFailTip(str)
    }

}