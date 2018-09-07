package com.kunleen.sn.sportnewsapplication.network.bean;

import android.graphics.drawable.TransitionDrawable;

import java.util.List;

/**
 * Created by ysy on 2018/3/22.
 */

public class Response_ForumList extends TResponse {
    private PageInfo pageInfo;

    public PageInfo getPageInfo() {
        return pageInfo;
    }

    public void setPageInfo(PageInfo pageInfo) {
        this.pageInfo = pageInfo;
    }

    public class PageInfo {
        private int total;
        private List<Row> rows;

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public List<Row> getRows() {
            return rows;
        }

        public void setRows(List<Row> rows) {
            this.rows = rows;
        }
    }

    public class Row {
        private int nid;
        private String title;
        private int good;
        private int bad;
        private int reply;

        public int getNid() {
            return nid;
        }

        public void setNid(int nid) {
            this.nid = nid;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getGood() {
            return good;
        }

        public void setGood(int good) {
            this.good = good;
        }

        public int getBad() {
            return bad;
        }

        public void setBad(int bad) {
            this.bad = bad;
        }

        public int getReply() {
            return reply;
        }

        public void setReply(int reply) {
            this.reply = reply;
        }
    }
}
