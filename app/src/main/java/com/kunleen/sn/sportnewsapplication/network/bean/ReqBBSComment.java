package com.kunleen.sn.sportnewsapplication.network.bean;

/**
 * Created by ysy on 2018/3/26.
 */

public class ReqBBSComment {
    private String userId;
    private String cid;
    private String nid;
    private String ntitle;
    private String inputTxt;

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

    public String getNid() {
        return nid;
    }

    public void setNid(String nid) {
        this.nid = nid;
    }

    public String getNtitle() {
        return ntitle;
    }

    public void setNtitle(String ntitle) {
        this.ntitle = ntitle;
    }

    public String getInputTxt() {
        return inputTxt;
    }

    public void setInputTxt(String inputTxt) {
        this.inputTxt = inputTxt;
    }
}
