package com.kunleen.sn.sportnewsapplication.network.bean;

/**
 * Created by ysy on 2018/3/23.
 */

public class ReqNewBBS {
    private String title;
    private String inputTxt;
    private String userId ;
    private String cid;
    private String imgType;
    private String imgCont;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getInputTxt() {
        return inputTxt;
    }

    public void setInputTxt(String inputTxt) {
        this.inputTxt = inputTxt;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getImgType() {
        return imgType;
    }

    public void setImgType(String imgType) {
        this.imgType = imgType;
    }

    public String getImgCont() {
        return imgCont;
    }

    public void setImgCont(String imgCont) {
        this.imgCont = imgCont;
    }
}
