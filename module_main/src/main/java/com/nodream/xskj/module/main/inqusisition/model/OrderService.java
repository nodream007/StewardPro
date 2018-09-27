package com.nodream.xskj.module.main.inqusisition.model;

import com.nodream.xskj.commonlib.net.BaseResponse;
import com.nodream.xskj.module.main.work.model.TaskBean;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Url;


public interface OrderService {
    /**
     * 测试类
     */
    @FormUrlEncoded
    @POST
    Observable<BaseResponse<List<OrderBean>>> getOrderList(@Url String url,
                                                         //  @Header("") String authorization,
                                                         @FieldMap Map<String, String> maps);

    @FormUrlEncoded
    @POST
    Observable<BaseResponse<OrderBean>> getOrderDetail(@Url String url,
                                                     //  @Header("") String authorization,
                                                     @FieldMap Map<String, String> maps);

    @FormUrlEncoded
    @POST
    Observable<BaseResponse<String>> completeOrder(@Url String url,
                                                  //  @Header("") String authorization,
                                                  @FieldMap Map<String, String> maps);
}
