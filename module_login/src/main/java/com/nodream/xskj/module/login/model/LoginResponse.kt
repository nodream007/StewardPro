package com.nodream.xskj.module.login.model

import com.nodream.xskj.commonlib.net.BaseResponse

/**
 * Created by nodream on 2017/11/30.
 */

class LoginResponse {

    private var mToken: String? = null

    fun getmToken(): String? {
        return mToken
    }

    fun setmToken(mToken: String) {
        this.mToken = mToken
    }
}
