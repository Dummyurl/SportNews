package com.kunleen.sn.sportnewsapplication.network.bean;

/**
 * Created by ysy on 2018/2/26.
 */

public class ReqNewsComment {
    int id;
    int type;
    int page;
    int rows;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
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
