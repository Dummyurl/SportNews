package com.kunleen.sn.sportnewsapplication.network.bean;

/**
 * Created by ysy on 2018/3/27.
 */

public class Response_CheckFollow extends TResponse {
   private boolean isFollew;

    public boolean isFollew() {
        return isFollew;
    }

    public void setFollew(boolean follew) {
        isFollew = follew;
    }
}
