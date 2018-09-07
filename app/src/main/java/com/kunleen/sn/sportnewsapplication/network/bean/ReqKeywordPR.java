package com.kunleen.sn.sportnewsapplication.network.bean;

/**
 * Created by ysy on 2018/4/10.
 */

public class ReqKeywordPR {
    private String keyword;
    private int page;
    private int rows;

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
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
