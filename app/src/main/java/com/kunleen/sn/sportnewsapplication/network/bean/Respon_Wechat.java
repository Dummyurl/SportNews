package com.kunleen.sn.sportnewsapplication.network.bean;

/**
 * Created by ysy on 2018/2/2.
 */

public class Respon_Wechat {
    private String openid;
    private String access_token;

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }
}
