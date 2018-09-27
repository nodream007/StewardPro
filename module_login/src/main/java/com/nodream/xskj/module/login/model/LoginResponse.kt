package com.nodream.xskj.module.login.model


/**
 * Created by nodream on 2018/08/30.
 */

data class LoginResponse(
        var id: Int,
        var jobName: String,
        var username: String,
        var name: String,
        var mobile: String,
        var medicalStaffType: Int,
        var skills: String,
        var starLevel: Int,
        var level: Int,
        var token: String
)
