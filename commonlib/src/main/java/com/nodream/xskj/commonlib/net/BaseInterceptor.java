package com.nodream.xskj.commonlib.net;

import android.text.TextUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;

/**
 * 设置基础请求头
 * Created by nodream on 2017/11/15.
 */

public class BaseInterceptor implements Interceptor {

    private Map<String, String> headers = new HashMap<>();
    private Map<String, String> params = new HashMap<>();

    public BaseInterceptor() {

    }

    public BaseInterceptor(Map<String, String> headers) {
        this.headers = headers;
    }

    public BaseInterceptor(Map<String, String> headers, Map<String, String> params) {
        this.headers = headers;
        this.params = params;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Request.Builder builder = request.newBuilder();
        if (headers != null && headers.size() > 0) {
            Set<String> keys = headers.keySet();
            for (String headerKey : keys) {
                builder.addHeader(headerKey,   headers.get(headerKey)).build();
            }
        }
        if (params != null && params.size() > 0) {
            if (canInjectIntoBody(request)) {
                FormBody.Builder formBodyBuilder = new FormBody.Builder();
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    formBodyBuilder.add(entry.getKey(), entry.getValue());
                }

                RequestBody formBody = formBodyBuilder.build();
                String postBodyString = bodyToString(request.body());
                postBodyString += ((postBodyString.length() > 0) ? "&" : "") + bodyToString(formBody);
                builder.post(RequestBody.create(MediaType.parse("application/x-www-form-urlencoded;charset=UTF-8"), postBodyString));
            }
        }
        return chain.proceed(builder.build());
    }

    private boolean canInjectIntoBody(Request request) {
        if (request == null) {
            return false;
        }
        if (!TextUtils.equals(request.method(), "POST")) {
            return false;
        }
        RequestBody body = request.body();
        if (body == null) {
            return false;
        }
        MediaType mediaType = body.contentType();
        if (mediaType == null) {
            return false;
        }
        if (!TextUtils.equals(mediaType.subtype(), "x-www-form-urlencoded")) {
            return false;
        }
        return true;
    }
    private String bodyToString(final RequestBody request) {
        try {
            final RequestBody copy = request;
            final Buffer buffer = new Buffer();
            if (copy != null)
                copy.writeTo(buffer);
            else
                return "";
            return buffer.readUtf8();
        } catch (final IOException e) {
            return "did not work";
        }
    }

    public static class Builder {

        BaseInterceptor interceptor;

        public Builder() {
            interceptor = new BaseInterceptor();
        }

        public Builder addParam(String key, String value) {
            interceptor.params.put(key, value);
            return this;
        }

        public Builder addParamsMap(Map<String, String> paramsMap) {
            interceptor.params.putAll(paramsMap);
            return this;
        }

        public Builder addHeaderParam(String key, String value) {
            interceptor.headers.put(key, value);
            return this;
        }

        public Builder addHeaderParamsMap(Map<String, String> headerParamsMap) {
            interceptor.headers.putAll(headerParamsMap);
            return this;
        }

//        public Builder addHeaderLine(String headerLine) {
//            int index = headerLine.indexOf(":");
//            if (index == -1) {
//                throw new IllegalArgumentException("Unexpected header: " + headerLine);
//            }
//            interceptor.headerLinesList.add(headerLine);
//            return this;
//        }
//
//        public Builder addHeaderLinesList(List<String> headerLinesList) {
//            for (String headerLine : headerLinesList) {
//                int index = headerLine.indexOf(":");
//                if (index == -1) {
//                    throw new IllegalArgumentException("Unexpected header: " + headerLine);
//                }
//                interceptor.headerLinesList.add(headerLine);
//            }
//            return this;
//        }
//
//        public Builder addQueryParam(String key, String value) {
//            interceptor.queryParamsMap.put(key, value);
//            return this;
//        }
//
//        public Builder addQueryParamsMap(Map<String, String> queryParamsMap) {
//            interceptor.queryParamsMap.putAll(queryParamsMap);
//            return this;
//        }
//
        public BaseInterceptor build() {
            return interceptor;
        }

    }
}
