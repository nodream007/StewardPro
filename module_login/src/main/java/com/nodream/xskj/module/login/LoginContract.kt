package com.nodream.xskj.module.login

import android.content.Context
import com.nodream.xskj.commonlib.base.IBaseView
import com.nodream.xskj.module.login.model.LoginModel
import com.nodream.xskj.module.login.model.LoginRequest

/**
 * Created by nodream on 2017/11/27.
 */
interface LoginContract {
    interface View : IBaseView {
        fun isActive():Boolean
        fun getUserNameFormView():String
        fun getPassWordFormView():String
        fun goToHomeView()
    }
    interface Presenter {
        fun login()
        fun loginSuccess()
        fun loginFail(str: String)
    }
    interface Model {
        fun saveUserId(id: Int)
        fun saveToken(token: String)
        fun postLogin(loginRequest: LoginRequest, modelCallBack: LoginModel.ModelCallBack, context: Context)
    }
}