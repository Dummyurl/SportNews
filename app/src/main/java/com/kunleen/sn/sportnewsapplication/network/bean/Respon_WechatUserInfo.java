package com.kunleen.sn.sportnewsapplication.network.bean;

/**
 * Created by ysy on 2018/2/2.
 */

public class Respon_WechatUserInfo {
    private String nickname;
    private int sex;
    private String headimgurl;

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getHeadimgurl() {
        return headimgurl;
    }

    public void setHeadimgurl(String headimgurl) {
        this.headimgurl = headimgurl;
    }
}
