package com.kunleen.sn.sportnewsapplication.network.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ysy on 2018/1/16.
 */

public class ReqRegistInfo implements Parcelable {
    private String userName;
    private String mobile;
    private String password;
    private String code;

    public ReqRegistInfo() {
    }

    protected ReqRegistInfo(Parcel in) {
        userName = in.readString();
        mobile = in.readString();
        password = in.readString();
        code = in.readString();
    }

    public static final Creator<ReqRegistInfo> CREATOR = new Creator<ReqRegistInfo>() {
        @Override
        public ReqRegistInfo createFromParcel(Parcel in) {
            return new ReqRegistInfo(in);
        }

        @Override
        public ReqRegistInfo[] newArray(int size) {
            return new ReqRegistInfo[size];
        }
    };

    public String getUsername() {
        return userName;
    }

    public void setUsername(String username) {
        this.userName = username;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userName);
        dest.writeString(mobile);
        dest.writeString(password);
        dest.writeString(code);
    }
}
