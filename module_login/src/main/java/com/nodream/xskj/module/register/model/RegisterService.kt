package com.nodream.xskj.module.register.model

import com.nodream.xskj.commonlib.net.BaseResponse
import com.nodream.xskj.module.login.model.LoginResponse
import io.reactivex.Observable
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import retrofit2.http.Url

/**
 * Created by nodream on 2017/12/5.
 */

interface RegisterService {
    /**
     * 测试类
     */
    @FormUrlEncoded
    @POST
    fun register(@Url url: String,
            //  @Header("") String authorization,
              @FieldMap maps: Map<String, String?>): Observable<BaseResponse<RegisterResponse>>
}