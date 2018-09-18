package com.nodream.xskj.module.main.work.model;

import com.nodream.xskj.commonlib.net.BaseResponse;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Url;


public interface WorkService {
    /**
     * 测试类
     */
    @FormUrlEncoded
    @POST
    Observable<BaseResponse<List<TaskBean>>> getTaskList(@Url String url,
                                                         //  @Header("") String authorization,
                                                         @FieldMap Map<String, String> maps);

    @FormUrlEncoded
    @POST
    Observable<BaseResponse<TaskBean>> getTaskDetail(@Url String url,
                                                       //  @Header("") String authorization,
                                                       @FieldMap Map<String, String> maps);

    @FormUrlEncoded
    @POST
    Observable<BaseResponse<String>> receiveConsumables(@Url String url,
                                                     //  @Header("") String authorization,
                                                     @FieldMap Map<String, String> maps);
    @FormUrlEncoded
    @POST
    Observable<BaseResponse<String>> startTask(@Url String url,
                                                        //  @Header("") String authorization,
                                                        @FieldMap Map<String, String> maps);
    @FormUrlEncoded
    @POST
    Observable<BaseResponse<String>> completeTask(@Url String url,
                                                        //  @Header("") String authorization,
                                                        @FieldMap Map<String, String> maps);
}
