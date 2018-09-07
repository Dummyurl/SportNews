package com.kunleen.sn.sportnewsapplication.network.bean;

/**
 * Created by ysy on 2018/3/15.
 */

public class ReqMyTopic {
    private String uid;
    private String cuid;
    private int page;
    private int rows;

    public String getCuid() {
        return cuid;
    }

    public void setCuid(String cuid) {
        this.cuid = cuid;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }
}
