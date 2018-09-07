package com.kunleen.sn.sportnewsapplication.network.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ysy on 2018/1/23.
 */

public class ReqNewsList implements Parcelable {
    private String uid;
    private String classfyId;
    private int page;
    private int rows;

    public ReqNewsList() {
    }

    protected ReqNewsList(Parcel in) {
        uid = in.readString();
        classfyId = in.readString();
        page = in.readInt();
        rows = in.readInt();
    }

    public static final Creator<ReqNewsList> CREATOR = new Creator<ReqNewsList>() {
        @Override
        public ReqNewsList createFromParcel(Parcel in) {
            return new ReqNewsList(in);
        }

        @Override
        public ReqNewsList[] newArray(int size) {
            return new ReqNewsList[size];
        }
    };

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getClassfyId() {
        return classfyId;
    }

    public void setClassfyId(String classfyId) {
        this.classfyId = classfyId;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(uid);
        dest.writeString(classfyId);
        dest.writeInt(page);
        dest.writeInt(rows);
    }
}
