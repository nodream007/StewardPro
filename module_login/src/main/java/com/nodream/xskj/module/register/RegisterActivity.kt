package com.nodream.xskj.module.register

import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import com.alibaba.android.arouter.facade.annotation.Route
import com.nodream.xskj.commonlib.base.BaseActivity
import com.nodream.xskj.module.R
import com.nodream.xskj.module.register.presenter.RegisterPresenter
import kotlinx.android.synthetic.main.activity_register.*


/**
 * Created by nodream on 2017/11/30.
 */
@Route(path = "/login/registeractivity")
class RegisterActivity: BaseActivity(), RegisterContract.View {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 设置顶部状态栏为深色
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            this.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
        setContentView(R.layout.activity_register)
        setRegBtnBg(false)

        reg_getver_btn.setOnClickListener{
            setRegBtnBg(true)
            gattemptVerCode()
        }

        reg_ok_btn.setOnClickListener{
            attemptRegister()
        }

        back.setOnClickListener{
            finish()
        }


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

    override fun setRegBtnBg(boolean: Boolean) {
        reg_ok_btn.isEnabled = boolean
    }

    private fun attemptRegister() {
        // Reset errors.
        reg_phone_text.error = null
        reg_ver_text.error = null
        reg_password_text.error = null

        var cancel = false
        var focusView: View? = null

        if (TextUtils.isEmpty(reg_phone_text.text.toString())) {
            reg_phone_text.error = getString(R.string.error_field_phone)
            focusView = reg_phone_text
            cancel = true
        }
        if (TextUtils.isEmpty(reg_ver_text.text.toString())) {
            reg_ver_text.error = getString(R.string.error_field_ver)
            focusView = reg_ver_text
            cancel = true
        }
        if (TextUtils.isEmpty(reg_password_text.text.toString())) {
            reg_password_text.error = getString(R.string.error_field_password)
            focusView = reg_password_text
            cancel = true
        }
        if (cancel) {
            focusView?.requestFocus()
        }else {
            RegisterPresenter(this,this).register()
        }

    }
    private fun gattemptVerCode() {

    }

}