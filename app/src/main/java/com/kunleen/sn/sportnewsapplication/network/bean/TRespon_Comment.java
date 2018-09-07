package com.kunleen.sn.sportnewsapplication.network.bean;

import java.util.List;

/**
 * Created by ysy on 2018/2/26.
 */

public class TRespon_Comment extends TResponse {
    PageInfo pageInfo;

    public PageInfo getPageInfo() {
        return pageInfo;
    }

    public void setPageInfo(PageInfo pageInfo) {
        this.pageInfo = pageInfo;
    }

    public class PageInfo {
        int total;
        List<Rows> rows;

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public List<Rows> getRows() {
            return rows;
        }

        public void setRows(List<Rows> rows) {
            this.rows = rows;
        }
    }

    public class Rows {
        int discussId;
        String userName;
        String headImage;
        String createTime;
        int good;
        String discussTxt;

        public int getDiscussId() {
            return discussId;
        }

        public void setDiscussId(int discussId) {
            this.discussId = discussId;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getHeadImage() {
            return headImage;
        }

        public void setHeadImage(String headImage) {
            this.headImage = headImage;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public int getGood() {
            return good;
        }

        public void setGood(int good) {
            this.good = good;
        }

        public String getDiscussTxt() {
            return discussTxt;
        }

        public void setDiscussTxt(String discussTxt) {
            this.discussTxt = discussTxt;
        }
    }
}
