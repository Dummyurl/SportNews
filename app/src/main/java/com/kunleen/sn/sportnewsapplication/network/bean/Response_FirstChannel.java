package com.kunleen.sn.sportnewsapplication.network.bean;

import java.util.List;

/**
 * Created by ysy on 2018/3/29.
 */

public class Response_FirstChannel extends TResponse {
    private List<Rows> rows;

    public List<Rows> getRows() {
        return rows;
    }

    public void setRows(List<Rows> rows) {
        this.rows = rows;
    }

    public class Rows {
        private int classyId;
        private String classyName;
        private List<mList> list;

        public int getClassyId() {
            return classyId;
        }

        public void setClassyId(int classyId) {
            this.classyId = classyId;
        }

        public String getClassyName() {
            return classyName;
        }

        public void setClassyName(String classyName) {
            this.classyName = classyName;
        }

        public List<mList> getList() {
            return list;
        }

        public void setList(List<mList> list) {
            this.list = list;
        }
    }

    public class mList {
        private int classyId;
        private String classyName;
        private String imageUrl;
        private String createTime;
        private int parentId;

        public int getClassyId() {
            return classyId;
        }

        public void setClassyId(int classyId) {
            this.classyId = classyId;
        }

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
    }
}
