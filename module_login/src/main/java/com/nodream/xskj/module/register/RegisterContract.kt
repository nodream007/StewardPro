package com.nodream.xskj.module.register

import com.nodream.xskj.commonlib.base.BasePresenter
import com.nodream.xskj.commonlib.base.BaseView
import com.nodream.xskj.module.login.model.LoginRequest
import com.nodream.xskj.module.register.model.RegisterRequest

/**
 * Created by nodream on 2017/11/27.
 */
interface RegisterContract {
    interface View : BaseView {
        fun getPhoneNumber(): String
        fun getVerCode(): String
        fun getPassword(): String
        fun getInviteCode(): String
    }
    interface Presenter: BasePresenter {
        fun register()
    }
    interface Model {
        fun postRegister(registerRequest: RegisterRequest): Boolean
    }
}