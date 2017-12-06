package com.nodream.xskj.module.login.model

import com.nodream.xskj.commonlib.net.BaseApiService
import com.nodream.xskj.commonlib.net.BaseResponse

import io.reactivex.Observable
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import retrofit2.http.Url

/**
 * Created by nodream on 2017/12/5.
 */

interface LoginService {
    /**
     * 测试类
     */
    @FormUrlEncoded
    @POST
    fun login(@Url url: String,
            //  @Header("") String authorization,
              @FieldMap maps: Map<String, String>): Observable<BaseResponse<LoginResponse>>
}
