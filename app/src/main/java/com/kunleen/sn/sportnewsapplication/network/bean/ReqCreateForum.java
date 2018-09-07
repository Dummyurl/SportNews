package com.kunleen.sn.sportnewsapplication.network.bean;

/**
 * Created by ysy on 2018/3/21.
 */

public class ReqCreateForum {
    private String cname;
    private String description;
    private String classfyId;
    private String classfyName;
    private int userId;
    private String topImgName;
    private String topImgCont;
    private String topImgType;
    private String littleImgId;
    private String littleImgName;
    private String littleImgCont;
    private String littleImgtype;
    private String cId;

    public String getcId() {
        return cId;
    }

    public void setcId(String cId) {
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

//    public String getLittleImage() {
//        return littleImage;
//    }
//
//    public void setLittleImage(String littleImage) {
//        this.littleImage = littleImage;
//    }


    public String getClassfyId() {
        return classfyId;
    }

    public void setClassfyId(String classfyId) {
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

    public String getTopImgName() {
        return topImgName;
    }

    public void setTopImgName(String topImgName) {
        this.topImgName = topImgName;
    }

    public String getTopImgCont() {
        return topImgCont;
    }

    public void setTopImgCont(String topImgCont) {
        this.topImgCont = topImgCont;
    }

    public String getTopImgType() {
        return topImgType;
    }

    public void setTopImgType(String topImgType) {
        this.topImgType = topImgType;
    }

    public String getLittleImgId() {
        return littleImgId;
    }

    public void setLittleImgId(String littleImgId) {
        this.littleImgId = littleImgId;
    }

    public String getLittleImgName() {
        return littleImgName;
    }

    public void setLittleImgName(String littleImgName) {
        this.littleImgName = littleImgName;
    }

    public String getLittleImgCont() {
        return littleImgCont;
    }

    public void setLittleImgCont(String littleImgCont) {
        this.littleImgCont = littleImgCont;
    }

    public String getLittleImgtype() {
        return littleImgtype;
    }

    public void setLittleImgtype(String littleImgtype) {
        this.littleImgtype = littleImgtype;
    }
}

