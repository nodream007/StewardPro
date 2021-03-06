package com.nodream.xskj.commonlib.net;

import android.content.Context;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.util.Log;

import com.nodream.xskj.commonlib.utils.SharedPreferencesUtils;

import java.io.File;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by nodream on 2017/11/29.
 */

public class NetClient {

    private static final int DEFAULT_TIMEOUT = 20;

    private static OkHttpClient okHttpClient;

    private static Retrofit retrofit;

    private BaseApiService apiService;

    private SharedPreferencesUtils sharedPreferencesUtils;

    private static Context mContext;
    /**
     * 内部类实现单例模式
     * 延迟加载，减少内存开销
     */
    private static class NetClientHolder {
        private static NetClient instance = new NetClient(mContext);
    }

    private NetClient(Context context) {
        sharedPreferencesUtils = new SharedPreferencesUtils(context);
        String token = sharedPreferencesUtils.getSharedPreference("token","")
                .toString().trim();
        Log.e("token:", token);
        BaseInterceptor baseInterceptor = new BaseInterceptor.Builder()
                .addHeaderParam("token", token)
//                .addHeaderParam("token", "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJwYXNzd29yZCI6IjEyMzQ1NiIsImlzcyI6Im1lZGljYWxTdGFmZiIsImlhdCI6MTUzNzQ1MjAzNiwidXNlcm5hbWUiOiJ6aG91bmFuIn0.d6RUSfojW9XH5dfaIgonF1Vk8gS0RN2qCEeGzgpFOE4")
                .addParam("app_id", "C767115F-0ED0-0001-3451-1DC0D520ECB0")
                .addParam("app_key", "9aaa8e3fea97081839f7515cb3426359")
                .build();
        okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .addInterceptor(baseInterceptor)
                .addNetworkInterceptor(
                        new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
                            @Override
                            public void log(String message) {
                                //打印retrofit日志
                                Log.i("RetrofitLog","retrofitBack = "+message);
                            }
                        }).setLevel(HttpLoggingInterceptor.Level.BODY))
                .build();
        retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(BaseApiService.Base_URL)
                .build();

    }

    public static NetClient getInstance(Context context) {
        if (context != null) {
            mContext = context;
        }
        return NetClientHolder.instance;
    }

    public NetClient createBaseApi() {
        apiService = create(BaseApiService.class);
        return this;
    }

    public  <T> T create(final Class<T> service) {
        if (service == null) {
            throw new RuntimeException("Api service is null!");
        }
        return retrofit.create(service);
    }

    public void httpGet(String url, BaseObserver baseObserver) {
        Log.i("NetClient","httpGet ");
        Observable<BaseResponse> observable = apiService.executeGet(url);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(baseObserver);
    }
    public void httpGet(String url, @NonNull Map parameters, BaseObserver baseObserver) {
        Log.i("NetClient","httpGet ");
        Observable<BaseResponse> observable = apiService.executeGet(url,parameters);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(baseObserver);
    }
    public void httpPost(String url, Map parameters, BaseObserver baseObserver) {
        Log.i("NetClient","httpGet ");
        Observable<BaseResponse> observable = apiService.executePost(url, parameters);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(baseObserver);
    }
    public void httpPost(String url, Map parameters,BaseApiService service, BaseObserver baseObserver) {
        Log.i("NetClient","httpGet ");
        Observable<BaseResponse> observable = service.executePost(url, parameters);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(baseObserver);
    }
    public void upload(String url, BaseObserver baseObserver) {

        File file = new File(Environment.getExternalStorageDirectory(), "icon.png");
        RequestBody requestBody = RequestBody.create(MediaType.parse("image/jpg"),file);
        MultipartBody.Part boby =
                MultipartBody.Part.createFormData("picture",file.getName(),requestBody);
        Observable<ResponseBody> observable = apiService.uploads(url, boby);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(baseObserver);
    }
}
