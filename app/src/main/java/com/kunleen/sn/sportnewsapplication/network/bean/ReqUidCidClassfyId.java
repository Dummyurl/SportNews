package com.kunleen.sn.sportnewsapplication.network.bean;

/**
 * Created by ysy on 2018/3/23.
 */

public class ReqUidCidClassfyId {
    private String userId;
    private int cid;
    private String classfyId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getCid() {
        return cid;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }

    public String getClassfyId() {
        return classfyId;
    }

    public void setClassfyId(String classfyId) {
        this.classfyId = classfyId;
    }
}
