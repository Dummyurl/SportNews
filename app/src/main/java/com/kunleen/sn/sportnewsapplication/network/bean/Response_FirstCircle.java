package com.kunleen.sn.sportnewsapplication.network.bean;

import java.util.List;

/**
 * Created by ysy on 2018/3/30.
 */

public class Response_FirstCircle extends TResponse {
    private PageInfo pageInfo;

    public PageInfo getPageInfo() {
        return pageInfo;
    }

    public void setPageInfo(PageInfo pageInfo) {
        this.pageInfo = pageInfo;
    }

    public class PageInfo {
        private int total;
        private List<Rows> rows;

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
        private int cId;
        private String cname;
        private String description;
        private String littleImage;
        private String topImage;
        private int classfyId;
        private String classfyName;
        private int userId;
        private int status;
        private int operatId;
        private int isHot;
        private String createTime;
        private String note;
        private int cid;

        public int getcId() {
            return cId;
        }

        public void setcId(int cId) {
            this.cId = cId;
        }

        public String getCname() {
            return cname;
        }

        public void setCname(String cname) {
            this.cname = cname;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getLittleImage() {
            return littleImage;
        }

        public void setLittleImage(String littleImage) {
            this.littleImage = littleImage;
        }

        public String getTopImage() {
            return topImage;
        }

        public void setTopImage(String topImage) {
            this.topImage = topImage;
        }

        public int getClassfyId() {
            return classfyId;
        }

        public void setClassfyId(int classfyId) {
            this.classfyId = classfyId;
        }

        public String getClassfyName() {
            return classfyName;
        }

        public void setClassfyName(String classfyName) {
            this.classfyName = classfyName;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public int getOperatId() {
            return operatId;
        }

        public void setOperatId(int operatId) {
            this.operatId = operatId;
        }

        public int getIsHot() {
            return isHot;
        }

        public void setIsHot(int isHot) {
            this.isHot = isHot;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getNote() {
            return note;
        }

        public void setNote(String note) {
            this.note = note;
        }

        public int getCid() {
            return cid;
        }

        public void setCid(int cid) {
            this.cid = cid;
        }
    }
}
