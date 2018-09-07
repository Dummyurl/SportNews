package com.kunleen.sn.sportnewsapplication.network.bean;

import java.util.List;

import javax.sql.RowSet;

/**
 * Created by ysy on 2018/1/23.
 */

public class TResponse_Channel extends TResponse {

    public List<row> getRows() {
        return rows;
    }

    public void setRows(List<row> rows) {
        this.rows = rows;
    }

    List<row> rows;

    public class row {
        public int getClassyId() {
            return classyId;
        }

        public void setClassyId(int classyId) {
            this.classyId = classyId;
        }

        int classyId;

        public String getClassyName() {
            return classyName;
        }

        public void setClassyName(String classyName) {
            this.classyName = classyName;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public int getParentId() {
            return parentId;
        }

        public void setParentId(int parentId) {
            this.parentId = parentId;
        }

        String classyName;
        String imageUrl;
        String createTime;
        int parentId;
    }
}
