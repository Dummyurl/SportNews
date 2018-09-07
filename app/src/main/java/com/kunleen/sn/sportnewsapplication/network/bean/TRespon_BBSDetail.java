package com.kunleen.sn.sportnewsapplication.network.bean;

import java.util.List;

/**
 * Created by ysy on 2018/3/26.
 */

public class TRespon_BBSDetail extends TResponse {
    private CircleNote circleNote;

    public CircleNote getCircleNote() {
        return circleNote;
    }

    public void setCircleNote(CircleNote circleNote) {
        this.circleNote = circleNote;
    }

    public class CircleNote {
        private int nid;
        private String title;
        private String txt;
        private int good;
        private int bad;
        private int cid;
        private String createTime;
        private String userName;
        private int userId;
        private String headImage;
        private List<mList> list;

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

        public String getTxt() {
            return txt;
        }

        public void setTxt(String txt) {
            this.txt = txt;
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

        public int getCid() {
            return cid;
        }

        public void setCid(int cid) {
            this.cid = cid;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public String getHeadImage() {
            return headImage;
        }

        public void setHeadImage(String headImage) {
            this.headImage = headImage;
        }

        public List<mList> getList() {
            return list;
        }

        public void setList(List<mList> list) {
            this.list = list;
        }
    }

    public class mList {
        private int id;
        private int fromType;
        private int comefrom;
        private String imageUrl;
        private int userId;
        private String createTime;
        private String note;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getFromType() {
            return fromType;
        }

        public void setFromType(int fromType) {
            this.fromType = fromType;
        }

        public int getComefrom() {
            return comefrom;
        }

        public void setComefrom(int comefrom) {
            this.comefrom = comefrom;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
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
    }
}
