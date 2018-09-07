package com.kunleen.sn.sportnewsapplication.network.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by xianglanzuo on 2018/1/2.
 */

public class User implements Parcelable {
    private int uid;
    private String userName;
    private String moblie;
    private String password;
    private int userLevel;
    private String createTime;
    private UserInfo userInfo;

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMoblie() {
        return moblie;
    }

    public void setMoblie(String moblie) {
        this.moblie = moblie;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getUserLevel() {
        return userLevel;
    }

    public void setUserLevel(int userLevel) {
        this.userLevel = userLevel;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    protected User(Parcel in) {
        uid = in.readInt();
        userName = in.readString();
        moblie = in.readString();
        password = in.readString();
        userLevel = in.readInt();
        createTime = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(uid);
        dest.writeString(userName);
        dest.writeString(moblie);
        dest.writeString(password);
        dest.writeInt(userLevel);
        dest.writeString(createTime);
    }

    private static class UserInfo {
        int uid;
        String realName;
        String idcard;
        String moblie;
        int age;
        int sex;
        String study;
        String interest;
        String headImage;
        String note;
        String email;
    }

}
