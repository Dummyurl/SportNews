package com.kunleen.sn.sportnewsapplication.network;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

/**
 * Created by xianglanzuo on 2018/1/2.
 */

public final class HttpClient {

    private final OkHttpClient mOkHttpClient;

    private HttpClient() {
        mOkHttpClient = new OkHttpClient.Builder()
                .readTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .connectTimeout(15, TimeUnit.SECONDS)
                .addInterceptor(new PublicParamsInterceptor())
                .addNetworkInterceptor(HttpLogFactory.BodyInterceptor(true))
                .addNetworkInterceptor(HttpLogFactory.stethoInterceptor())
                .retryOnConnectionFailure(true)
                .build();
    }

    private static final class HttpClientHolder {
        private static final HttpClient HTTP_CLIENT = new HttpClient();
    }

    public static HttpClient getInstance() {
        return HttpClientHolder.HTTP_CLIENT;
    }

    public OkHttpClient getOkHttpClient() {
        return mOkHttpClient;
    }
}
