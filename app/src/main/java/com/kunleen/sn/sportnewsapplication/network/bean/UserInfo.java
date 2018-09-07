package com.kunleen.sn.sportnewsapplication.network.bean;

/**
 * Created by ysy on 2018/1/30.
 */

public class UserInfo {
    private boolean wifiLoadImg = false;
    private boolean nofifyMode = false;
    private int fontMode = 1;
    private TResponse_Login.User userInfo;

    public UserInfo() {
        userInfo = null;
    }

    public TResponse_Login.User getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(TResponse_Login.User userInfo) {
        this.userInfo = userInfo;
    }

    public boolean isWifiLoadImg() {
        return wifiLoadImg;
    }

    public void setWifiLoadImg(boolean wifiLoadImg) {
        this.wifiLoadImg = wifiLoadImg;
    }

    public boolean isNofifyMode() {
        return nofifyMode;
    }

    public void setNofifyMode(boolean nofifyMode) {
        this.nofifyMode = nofifyMode;
    }

    public int getFontMode() {
        return fontMode;
    }

    public void setFontMode(int fontMode) {
        this.fontMode = fontMode;
    }
}
