package com.kunleen.sn.sportnewsapplication.network;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;

/**
 * Created by xianglanzuo on 2018/1/2.
 */

public class PublicParamsInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        return chain.proceed(chain.request());
    }
}
