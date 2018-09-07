package com.kunleen.sn.sportnewsapplication.network;

import android.util.Log;

import com.kunleen.sn.sportnewsapplication.AppConfig;
import com.kunleen.sn.sportnewsapplication.activity.MainActivity;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by xianglanzuo on 2018/1/2.
 */

public final class RetrofitClient {

    private static final String BASE_URL;

    static {
        switch (AppConfig.ONLINE_SERVER) {
            case AppConfig.OTHER: {
                BASE_URL = "http://192.168.1.50:8080/boss-web/";
                break;
            }
            case AppConfig.DEBUG:
                BASE_URL = "http://192.168.1.236:9988/";
                break;
            case AppConfig.RELEASE:
                BASE_URL = "http://gym.kunleen.com/boss-web/";
                break;
            default:
                BASE_URL = "https://www.baidu.com/";
                break;
        }
    }

    private final Retrofit mRetrofit;

    private Map<Class, Object> mServiceMap = new HashMap<>();


    private RetrofitClient() {
        mRetrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(HttpClient.getInstance().getOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createAsync())
                .build();
    }

    private static final class RetrofitClientHolder {
        private static final RetrofitClient RETROFIT_CLIENT = new RetrofitClient();
    }

    public static RetrofitClient getInstance() {
        return RetrofitClientHolder.RETROFIT_CLIENT;
    }

    public final <T> T getService(Class<T> tClass) {
        mServiceMap.get(tClass);
        Object service = mServiceMap.get(tClass);
        if (service == null) {
            service = mRetrofit.create(tClass);
            mServiceMap.put(tClass, service);
        }
        return ((T) service);
    }

    public static <T> T createApi(Class<T> clazz) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://www.baidu.com/")
                .client(HttpClient.getInstance().getOkHttpClient())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(clazz);
    }
}
