package com.nodream.xskj.module.register

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.nodream.xskj.commonlib.base.BaseActivity
import com.nodream.xskj.module.R
import kotlinx.android.synthetic.main.activity_register.*


/**
 * Created by nodream on 2017/11/30.
 */
@Route(path = "/login/registeractivity")
class RegisterActivity: BaseActivity(), RegisterContract.View {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
    }
    override fun getPhoneNumber(): String {
        return reg_phone_text.text.toString()
    }

    override fun getVerCode(): String {
        return reg_ver_text.text.toString()
    }

    override fun getPassword(): String {
        return reg_password_text.text.toString()
    }

    override fun getInviteCode(): String {
        return reg_invite_text.text.toString()
    }

}