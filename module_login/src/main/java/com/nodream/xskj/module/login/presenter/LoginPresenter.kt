package com.nodream.xskj.module.login.presenter

import android.content.Context
import com.nodream.xskj.commonlib.net.RetrofitClient
import com.nodream.xskj.module.login.LoginContract
import com.nodream.xskj.module.login.model.LoginModel
import com.nodream.xskj.module.login.model.LoginRequest
import com.nodream.xskj.module.login.model.Test
import org.jetbrains.annotations.NotNull

/**
 * Created by nodream on 2017/11/27.
 */
class LoginPresenter(val view: LoginContract.View): LoginContract.Presenter {

    private val mLoginModel: LoginModel = LoginModel()

    override fun start() {

    }

    override fun login() {
        view.showProgress(true)
        val mLoginRequest = LoginRequest(view.getUserNameFormView(),
                view.getPassWordFormView())
        LoginModel().postLogin(mLoginRequest)
//        if(mLoginModel.postLogin(mLoginRequest)) {
//            loginSuccess()
//        }

    }

    override fun loginSuccess() {
        view.goToHomeView()
    }

    override fun loginFail(str: String) {
        view.showFailTip(str)
    }
    private fun isPasswordValid(password: String): Boolean {
        //TODO: Replace this with your own logic
        return password.length > 4
    }
}