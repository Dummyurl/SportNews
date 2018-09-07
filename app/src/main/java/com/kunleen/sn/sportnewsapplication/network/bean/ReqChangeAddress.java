package com.kunleen.sn.sportnewsapplication.network.bean;

/**
 * Created by ysy on 2018/2/1.
 */

public class ReqChangeAddress {
    private int uid;
    private String province;
    private String city;

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
