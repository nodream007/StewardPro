package com.nodream.xskj.module.register.model

/**
 * Created by nodream on 2017/12/4.
 */

data class RegisterRequest(
    val mobile: String? = null,
    val verCode: String? = null,
    val passWord: String? = null,
    val inviteCode: String
)
