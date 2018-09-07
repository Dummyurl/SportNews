package com.kunleen.sn.sportnewsapplication.network.bean;

import java.io.Serializable;

/**
 * 频道实体类
 * <p>
 *
 * @author yushengyang
 */
public class Channel implements Serializable {
    private static final long serialVersionUID = -7415501530039818852L;
    private String channelName;
    //    private int channelPic;
    private int channelPic_checked;
    private int classfy;

    public int getClassfy() {
        return classfy;
    }

    public void setClassfy(int classfy) {
        this.classfy = classfy;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public int getChannelPic_checked() {
        return channelPic_checked;
    }

    public void setChannelPic_checked(int channelPic_checked) {
        this.channelPic_checked = channelPic_checked;
    }
}
