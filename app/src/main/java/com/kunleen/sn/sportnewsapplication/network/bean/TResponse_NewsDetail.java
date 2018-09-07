package com.kunleen.sn.sportnewsapplication.network.bean;

import java.util.List;

/**
 * Created by ysy on 2018/2/23.
 */

public class TResponse_NewsDetail extends TResponse {
    public TResponse_NewsDetail.data getData() {
        return data;
    }

    public void setData(TResponse_NewsDetail.data data) {
        this.data = data;
    }

    data data;

    public class data {
        int contentId;
        int titleId;
        String title;
        String keyword;
        String secTitle;
        String author;
        String comefrom;
        String inputTxt;
        String txt;
        int linkType;
        String link;
        int role;
        int admin;
        int operatid;
        int good;
        int bad;
        int discussCount;
        int viewCount;
        String createTime;
        List<Recommend> list;

        public int getContentId() {
            return contentId;
        }

        public void setContentId(int contentId) {
            this.contentId = contentId;
        }

        public int getTitleId() {
            return titleId;
        }

        public void setTitleId(int titleId) {
            this.titleId = titleId;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getKeyword() {
            return keyword;
        }

        public void setKeyword(String keyword) {
            this.keyword = keyword;
        }

        public String getSecTitle() {
            return secTitle;
        }

        public void setSecTitle(String secTitle) {
            this.secTitle = secTitle;
        }

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public String getComefrom() {
            return comefrom;
        }

        public void setComefrom(String comefrom) {
            this.comefrom = comefrom;
        }

        public String getInputTxt() {
            return inputTxt;
        }

        public void setInputTxt(String inputTxt) {
            this.inputTxt = inputTxt;
        }

        public String getTxt() {
            return txt;
        }

        public void setTxt(String txt) {
            this.txt = txt;
        }

        public int getLinkType() {
            return linkType;
        }

        public void setLinkType(int linkType) {
            this.linkType = linkType;
        }

        public String getLink() {
            return link;
        }

        public void setLink(String link) {
            this.link = link;
        }

        public int getRole() {
            return role;
        }

        public void setRole(int role) {
            this.role = role;
        }

        public int getAdmin() {
            return admin;
        }

        public void setAdmin(int admin) {
            this.admin = admin;
        }

        public int getOperatid() {
            return operatid;
        }

        public void setOperatid(int operatid) {
            this.operatid = operatid;
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

        public int getDiscussCount() {
            return discussCount;
        }

        public void setDiscussCount(int discussCount) {
            this.discussCount = discussCount;
        }

        public int getViewCount() {
            return viewCount;
        }

        public void setViewCount(int viewCount) {
            this.viewCount = viewCount;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public List<Recommend> getList() {
            return list;
        }

        public void setList(List<Recommend> list) {
            this.list = list;
        }
    }

    public class Recommend {
        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getTitleId() {
            return titleId;
        }

        public void setTitleId(String titleId) {
            this.titleId = titleId;
        }

        String title;
        String titleId;
        String imageUrl;

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }
    }
}
