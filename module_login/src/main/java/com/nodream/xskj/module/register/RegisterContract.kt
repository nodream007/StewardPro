package com.nodream.xskj.module.register

import android.content.Context
import com.nodream.xskj.commonlib.base.IBaseView
import com.nodream.xskj.module.register.model.RegisterModel
import com.nodream.xskj.module.register.model.RegisterRequest

/**
 * Created by nodream on 2017/11/27.
 */
interface RegisterContract {
    interface View : IBaseView {
        fun getPhoneNumber(): String
        fun getVerCode(): String
        fun getPassword(): String
        fun getInviteCode(): String
        fun setRegBtnBg(boolean: Boolean)
        fun finish()
    }
    interface Presenter {
        fun register()
    }
    interface Model {
        fun postRegister(registerRequest: RegisterRequest,
                         modelCallBack: RegisterModel.ModelCallBack,
                         context: Context)
    }
}