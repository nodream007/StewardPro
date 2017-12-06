package com.nodream.xskj.module.register.model

/**
 * Created by nodream on 2017/12/4.
 */
data class RegisterResponse(
        var id: Int,
        var mobile: String,
        var name: String,
        var nickname: String,
        var integral: Int,
        var gold: Double,
        var invitationCode: String,
        var locked: Boolean
)