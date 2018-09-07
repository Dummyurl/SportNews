package com.kunleen.sn.sportnewsapplication.network.bean;

import java.util.List;

/**
 * Created by ysy on 2018/2/6.
 */

public class TResponse_SearchNews extends TResponse {
    private List<TResponse_NewsList.row> rows;

    public List<TResponse_NewsList.row> getRows() {
        return rows;
    }

    public void setRows(List<TResponse_NewsList.row> rows) {
        this.rows = rows;
    }
}
