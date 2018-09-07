package com.kunleen.sn.sportnewsapplication.network.bean;


/**
 * Created by ysy on 2018/2/9.
 */

public class ReqComment {

    private int contentId;
    private String title;
    private String userId;
    private String discussInputtxt;
    private String parentid;


    public void setContentid(int contentid) {
        this.contentId = contentid;
    }

    public int getContentid() {
        return contentId;
    }


    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }


    public void setUserid(String userid) {
        this.userId = userid;
    }

    public String getUserid() {
        return userId;
    }


    public void setDiscussinputtxt(String discussinputtxt) {
        this.discussInputtxt = discussinputtxt;
    }

    public String getDiscussinputtxt() {
        return discussInputtxt;
    }


    public void setParentid(String parentid) {
        this.parentid = parentid;
    }

    public String getParentid() {
        return parentid;
    }

}