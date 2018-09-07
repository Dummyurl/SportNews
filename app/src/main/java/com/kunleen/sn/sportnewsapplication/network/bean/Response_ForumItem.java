package com.kunleen.sn.sportnewsapplication.network.bean;

import java.util.List;

/**
 * Created by ysy on 2018/3/20.
 */

public class Response_ForumItem extends TResponse {
    private PageInfo pageInfo;

    public PageInfo getPageInfo() {
        return pageInfo;
    }

    public void setPageInfo(PageInfo pageInfo) {
        this.pageInfo = pageInfo;
    }

    public class PageInfo {
        private int total;
        private List<TResponse_ForumPage.forumitem> rows;

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public List<TResponse_ForumPage.forumitem> getRows() {
            return rows;
        }

        public void setRows(List<TResponse_ForumPage.forumitem> rows) {
            this.rows = rows;
        }
    }
}
