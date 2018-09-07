package com.kunleen.sn.sportnewsapplication.network.bean;


import java.util.List;

/**
 * Created by ysy on 2018/3/19.
 */

public class TResponse_ForumChannel extends TResponse {
    private List<list> list;

    public List<TResponse_ForumChannel.list> getList() {
        return list;
    }

    public void setList(List<TResponse_ForumChannel.list> list) {
        this.list = list;
    }

    public class list {
        private int classfyId;
        private String classfyName;
        private int personNum;

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

        public int getPersonNum() {
            return personNum;
        }

        public void setPersonNum(int personNum) {
            this.personNum = personNum;
        }
    }
}
