package com.nodream.xskj.module.login.model


/**
 * Created by nodream on 2017/11/30.
 */

data class LoginResponse(
        var id: Int,
        var mobile: String,
        var name: String,
        var nickname: String,
        var integral: Int,
        var gold: Double,
        var invitationCode: String,
        var locked: Boolean
)
