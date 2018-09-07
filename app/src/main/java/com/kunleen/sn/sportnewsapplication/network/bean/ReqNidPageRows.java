package com.kunleen.sn.sportnewsapplication.network.bean;

/**
 * Created by ysy on 2018/3/26.
 */

public class ReqNidPageRows {
    public String getNid() {
        return nid;
    }

    public void setNid(String nid) {
        this.nid = nid;
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

    private String nid;
    private int page;
    private int rows;
}
