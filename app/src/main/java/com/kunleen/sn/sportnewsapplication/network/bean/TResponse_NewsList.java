package com.kunleen.sn.sportnewsapplication.network.bean;

import com.google.gson.Gson;

import java.util.List;

/**
 * Created by ysy on 2018/1/23.
 */

public class TResponse_NewsList extends TResponse {
    public Pageinfo getPageInfo() {
        return pageInfo;
    }

    public void setPageInfo(Pageinfo pageInfo) {
        this.pageInfo = pageInfo;
    }

    private Pageinfo pageInfo;

    public List<row> getRows() {
        return rows;
    }

    public void setRows(List<row> rows) {
        this.rows = rows;
    }

    private List<row> rows;

    public class Pageinfo {
        public int getTotal() {
            return total;
        }

        public List<row> getRows() {
            return rows;
        }

        int total;
        List<row> rows;

        @Override
        public String toString() {
            return "Pageinfo{" +
                    "total=" + total +
                    ", rows=" + rows +
                    '}';
        }
    }

    public class row {

        public String getCreatTime() {
            return creatTime;
        }

        public String getTitle() {
            return title;
        }

        public int getWeight() {
            return weight;
        }

        public int getStatus() {
            return status;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public int getClassfyId() {
            return classfyId;
        }

        public String getSecTitle() {
            return secTitle;
        }

        public String getKeyword() {
            return keyword;
        }

        public int getShowType() {
            return showType;
        }

        public int getTitleId() {
            return titleId;
        }

        public int getCharacter() {
            return character;
        }

        public boolean isIstop() {
            return istop;
        }

        public void setIstop(boolean istop) {
            this.istop = istop;
        }

        boolean istop;
        String creatTime;
        String title;
        int weight;
        int status;
        String imageUrl;
        int classfyId;
        String secTitle;
        String keyword;
        int showType;
        int titleId;
        int character;
        int discussCount;

        public int getDiscussCount() {
            return discussCount;
        }

        public void setDiscussCount(int discussCount) {
            this.discussCount = discussCount;
        }


        @Override
        public String toString() {
            return "row{" +
                    "istop=" + istop +
                    ", creatTime='" + creatTime + '\'' +
                    ", title='" + title + '\'' +
                    ", weight=" + weight +
                    ", status=" + status +
                    ", imageUrl='" + imageUrl + '\'' +
                    ", classfyId=" + classfyId +
                    ", secTitle='" + secTitle + '\'' +
                    ", keyword='" + keyword + '\'' +
                    ", showType=" + showType +
                    ", titleId=" + titleId +
                    ", character=" + character +
                    '}';
        }
    }

    @Override
    public String toString() {
        Gson gson = new Gson();
        return gson.toJson(pageInfo);
    }
}
