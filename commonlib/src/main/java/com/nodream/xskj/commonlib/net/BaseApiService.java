package com.nodream.xskj.commonlib.net;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * Created by nodream on 2017/11/15.
 */

public interface BaseApiService {

    public static final String Base_URL = "http://api.douban.com/v2/movie/";
    @GET("{url}")
    Observable<BaseResponse> executeGet(
            @Path("url") String url,
            @QueryMap Map<String, String> maps
    );
    @GET("{url}")
    Observable<BaseResponse> executeGet(
            @Path("url") String url
    );


    @POST("{url}")
    Observable<ResponseBody> executePost(
            @Path("url") String url,
            //  @Header("") String authorization,
            @FieldMap Map<String, String> maps);

    @Multipart
    @POST("{url}")
    Observable<ResponseBody> upLoadFile(
            @Path("url") String url,
            @Part("image\"; filename=\"image.jpg") RequestBody requestBody);

    @POST("{url}")
    Call<ResponseBody> uploadFiles(
            @Path("url") String url,
            @Path("headers") Map<String, String> headers,
            @Part("filename") String description,
            @PartMap() Map<String, RequestBody> maps);

    @Streaming
    @GET
    Observable<ResponseBody> downloadFile(@Url String fileUrl);

    @Multipart
    @POST()
    Observable<ResponseBody> uploads(
            @Url String url,
            @Part() MultipartBody.Part file);

}
