package com.kunleen.sn.sportnewsapplication.network.bean;

/**
 * Created by ysy on 2018/2/6.
 */

public class ReqSearchNews {
    private int page;
    private int rows;
    private String title;

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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
