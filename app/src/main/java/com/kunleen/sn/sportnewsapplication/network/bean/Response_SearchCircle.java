package com.kunleen.sn.sportnewsapplication.network.bean;

/**
 * Created by ysy on 2018/4/10.
 */

public class Response_SearchCircle extends TResponse {
    private PageInfoBean<CircleBean> pageInfo;

    public PageInfoBean<CircleBean> getPageInfo() {
        return pageInfo;
    }

    public void setPageInfo(PageInfoBean<CircleBean> pageInfo) {
        this.pageInfo = pageInfo;
    }
}
