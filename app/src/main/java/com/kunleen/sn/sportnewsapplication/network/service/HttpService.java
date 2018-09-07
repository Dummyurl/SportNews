package com.kunleen.sn.sportnewsapplication.network.service;

import com.kunleen.sn.sportnewsapplication.network.bean.ReqAdList;
import com.kunleen.sn.sportnewsapplication.network.bean.ReqBBSComment;
import com.kunleen.sn.sportnewsapplication.network.bean.ReqCarouse;
import com.kunleen.sn.sportnewsapplication.network.bean.ReqChangeAddress;
import com.kunleen.sn.sportnewsapplication.network.bean.ReqChangeImg;
import com.kunleen.sn.sportnewsapplication.network.bean.ReqChangeName;
import com.kunleen.sn.sportnewsapplication.network.bean.ReqCidPageRows;
import com.kunleen.sn.sportnewsapplication.network.bean.ReqCidUid;
import com.kunleen.sn.sportnewsapplication.network.bean.ReqComment;
import com.kunleen.sn.sportnewsapplication.network.bean.ReqCommentGood;
import com.kunleen.sn.sportnewsapplication.network.bean.ReqCreateForum;
import com.kunleen.sn.sportnewsapplication.network.bean.ReqEmpty;
import com.kunleen.sn.sportnewsapplication.network.bean.ReqForumChannel;
import com.kunleen.sn.sportnewsapplication.network.bean.ReqKeywordPR;
import com.kunleen.sn.sportnewsapplication.network.bean.ReqLogin;
import com.kunleen.sn.sportnewsapplication.network.bean.ReqModifyPassword;
import com.kunleen.sn.sportnewsapplication.network.bean.ReqMyTopic;
import com.kunleen.sn.sportnewsapplication.network.bean.ReqNewBBS;
import com.kunleen.sn.sportnewsapplication.network.bean.ReqNewsComment;
import com.kunleen.sn.sportnewsapplication.network.bean.ReqNewsDetail;
import com.kunleen.sn.sportnewsapplication.network.bean.ReqNewsList;
import com.kunleen.sn.sportnewsapplication.network.bean.ReqNid;
import com.kunleen.sn.sportnewsapplication.network.bean.ReqNidPageRows;
import com.kunleen.sn.sportnewsapplication.network.bean.ReqNidType;
import com.kunleen.sn.sportnewsapplication.network.bean.ReqPhoneCode;
import com.kunleen.sn.sportnewsapplication.network.bean.ReqRegist;
import com.kunleen.sn.sportnewsapplication.network.bean.ReqReleaseId;
import com.kunleen.sn.sportnewsapplication.network.bean.ReqSearchNews;
import com.kunleen.sn.sportnewsapplication.network.bean.ReqUerIdFuserId;
import com.kunleen.sn.sportnewsapplication.network.bean.ReqUid;
import com.kunleen.sn.sportnewsapplication.network.bean.ReqUidCidClassfyId;
import com.kunleen.sn.sportnewsapplication.network.bean.ReqUidFuid;
import com.kunleen.sn.sportnewsapplication.network.bean.ReqUidInt;
import com.kunleen.sn.sportnewsapplication.network.bean.ReqUidTypePR;
import com.kunleen.sn.sportnewsapplication.network.bean.Respon_Image;
import com.kunleen.sn.sportnewsapplication.network.bean.Respon_Wechat;
import com.kunleen.sn.sportnewsapplication.network.bean.Respon_WechatUserInfo;
import com.kunleen.sn.sportnewsapplication.network.bean.Respon_Weibo;
import com.kunleen.sn.sportnewsapplication.network.bean.Response_BBSComment;
import com.kunleen.sn.sportnewsapplication.network.bean.Response_Carouse;
import com.kunleen.sn.sportnewsapplication.network.bean.Response_CheckFollow;
import com.kunleen.sn.sportnewsapplication.network.bean.Response_FirstChannel;
import com.kunleen.sn.sportnewsapplication.network.bean.Response_FirstCircle;
import com.kunleen.sn.sportnewsapplication.network.bean.Response_FollowBeFollow;
import com.kunleen.sn.sportnewsapplication.network.bean.Response_ForumItem;
import com.kunleen.sn.sportnewsapplication.network.bean.Response_ForumList;
import com.kunleen.sn.sportnewsapplication.network.bean.Response_ForumTop;
import com.kunleen.sn.sportnewsapplication.network.bean.Response_ForumType;
import com.kunleen.sn.sportnewsapplication.network.bean.Response_SearchCircle;
import com.kunleen.sn.sportnewsapplication.network.bean.TReqThirdLogin;
import com.kunleen.sn.sportnewsapplication.network.bean.TRequest;
import com.kunleen.sn.sportnewsapplication.network.bean.TRespon_BBSDetail;
import com.kunleen.sn.sportnewsapplication.network.bean.TRespon_Carousel;
import com.kunleen.sn.sportnewsapplication.network.bean.TRespon_Comment;
import com.kunleen.sn.sportnewsapplication.network.bean.TResponse;
import com.kunleen.sn.sportnewsapplication.network.bean.TResponse_Channel;
import com.kunleen.sn.sportnewsapplication.network.bean.TResponse_Empty;
import com.kunleen.sn.sportnewsapplication.network.bean.TResponse_ForumChannel;
import com.kunleen.sn.sportnewsapplication.network.bean.TResponse_ForumPage;
import com.kunleen.sn.sportnewsapplication.network.bean.TResponse_Login;
import com.kunleen.sn.sportnewsapplication.network.bean.TResponse_MyReply;
import com.kunleen.sn.sportnewsapplication.network.bean.TResponse_MyTopic;
import com.kunleen.sn.sportnewsapplication.network.bean.TResponse_NewsDetail;
import com.kunleen.sn.sportnewsapplication.network.bean.TResponse_NewsList;
import com.kunleen.sn.sportnewsapplication.network.bean.TResponse_SearchNews;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

/**
 * Created by xianglanzuo on 2018/1/2.
 */

public interface HttpService {

//    @POST("sportsHandle/")
//    Observable<TResponse<User>> userRegist(@Body TRequest<ReqRegistInfo> request);

    @POST("sportsHandle/")
    Observable<TResponse_NewsList> newsList(@Body TRequest<ReqNewsList> request);

    @POST("sportsHandle/")
    Observable<TResponse_Channel> channel(@Body TRequest<ReqEmpty> request);

    @POST("sportsHandle/")
    Observable<TResponse_NewsList> topNewsList(@Body TRequest<ReqEmpty> request);

    @POST("sportsHandle/")
    Observable<TResponse_NewsList> adList(@Body TRequest<ReqAdList> request);


    @POST("sportsHandle/")
    Observable<TResponse> getLoginCode(@Body TRequest<ReqPhoneCode> request);

    @POST("sportsHandle/")
    Observable<TResponse_Login> Login(@Body TRequest<ReqLogin> request);

    @POST("sportsHandle/")
    Observable<TResponse_Login> Regist(@Body TRequest<ReqRegist> request);

    @GET("https://api.weibo.com/2/users/show.json")
    Observable<Respon_Weibo> WeiBoInfo(@QueryMap Map<String, String> params);

    @GET("https://api.weixin.qq.com/sns/oauth2/access_token")
    Observable<Respon_Wechat> WeChatInfo(@QueryMap Map<String, String> params);

    @GET("https://api.weixin.qq.com/sns/userinfo")
    Observable<Respon_WechatUserInfo> WeChatUserInfo(@QueryMap Map<String, String> params);

    @POST("sportsHandle/")
    Observable<TResponse> ChangeAddress(@Body TRequest<ReqChangeAddress> request);

    @POST("sportsHandle/")
    Observable<TRespon_Carousel> getSplash(@Body TRequest<ReqCarouse> request);


    @POST("sportsHandle/")
    Observable<TResponse> ChangeName(@Body TRequest<ReqChangeName> request);

    @POST("sportsHandle/")
    Observable<Respon_Image> ChangeImg(@Body TRequest<ReqChangeImg> request);

    @POST("sportsHandle/")
    Observable<TResponse_Login> ThirdLogin(@Body TRequest<TReqThirdLogin> request);

    @POST("sportsHandle/")
    Observable<TResponse> ModifyPassword(@Body TRequest<ReqModifyPassword> request);

    @POST("sportsHandle/")
    Observable<TResponse_NewsList> SearchNews(@Body TRequest<ReqSearchNews> request);

    @POST("sportsHandle/")
    Observable<TResponse> Comment(@Body TRequest<ReqComment> request);

    @POST("sportsHandle/")
    Observable<TResponse_NewsDetail> NewsDetail(@Body TRequest<ReqNewsDetail> request);

    @POST("sportsHandle/")
    Observable<TRespon_Comment> GetComment(@Body TRequest<ReqNewsComment> request);

    //点赞评论
    @POST("sportsHandle/")
    Observable<TResponse> CommentGood(@Body TRequest<ReqCommentGood> request);

    //获取圈子首页
    @POST("sportsHandle/")
    Observable<TResponse_ForumPage> ForumPage(@Body TRequest<ReqUid> request);

    //获取圈子首页
    @POST("sportsHandle/")
    Observable<Response_ForumItem> MyForum(@Body TRequest<ReqMyTopic> request);

    //获取我的帖子
    @POST("sportsHandle/")
    Observable<TResponse_MyTopic> MyTopic(@Body TRequest<ReqMyTopic> request);

    //获取我的回复
    @POST("sportsHandle/")
    Observable<TResponse_MyReply> MyReply(@Body TRequest<ReqMyTopic> request);

    //获取圈子首页
    @POST("sportsHandle/")
    Observable<TResponse_ForumChannel> ForumChannel(@Body TRequest<ReqForumChannel> request);

    //根据分类查找圈子接口
    @POST("sportsHandle/")
    Observable<Response_ForumItem> ForumChannelDetail(@Body TRequest<ReqNewsList> request);

    //根据分类查找圈子接口
    @POST("sportsHandle/")
    Observable<Response_ForumType> ForumType(@Body TRequest<ReqEmpty> request);

    //创建圈子
    @POST("sportsHandle/")
    Observable<TResponse> CreatForum(@Body TRequest<ReqCreateForum> request);

    //圈子头部信息
    @POST("sportsHandle/")
    Observable<Response_ForumTop> ForumListTop(@Body TRequest<ReqCidUid> request);

    //帖子列表
    @POST("sportsHandle/")
    Observable<Response_ForumList> ForumList(@Body TRequest<ReqCidPageRows> request);

    //发布帖子
    @POST("sportsHandle/")
    Observable<TResponse> NewBBS(@Body TRequest<ReqNewBBS> request);

    //關注圈子
    @POST("sportsHandle/")
    Observable<TResponse> FollowForum(@Body TRequest<ReqUidCidClassfyId> request);

    //取消關注
    @POST("sportsHandle/")
    Observable<TResponse> UnFollowForum(@Body TRequest<ReqCidUid> request);

    //帖子詳情
    @POST("sportsHandle/")
    Observable<TRespon_BBSDetail> BBSDetail(@Body TRequest<ReqNid> request);

    //帖子评论列表
    @POST("sportsHandle/")
    Observable<Response_BBSComment> BBSComment(@Body TRequest<ReqNidPageRows> request);

    //帖子点赞或踩
    @POST("sportsHandle/")
    Observable<TResponse> BBSSuggest(@Body TRequest<ReqNidType> request);

    //回帖
    @POST("sportsHandle/")
    Observable<TResponse> BBSDoComment(@Body TRequest<ReqBBSComment> request);

    //回帖
    @POST("sportsHandle/")
    Observable<Response_CheckFollow> CheckFollow(@Body TRequest<ReqUidFuid> request);

    //回帖
    @POST("sportsHandle/")
    Observable<Response_FollowBeFollow> FollowBeFollow(@Body TRequest<ReqUidTypePR> request);

    //回帖
    @POST("sportsHandle/")
    Observable<Response_FirstChannel> NewFristPage(@Body TRequest<ReqEmpty> request);

    //回帖
    @POST("sportsHandle/")
    Observable<TResponse> FollowUser(@Body TRequest<ReqUerIdFuserId> request);

    @POST("sportsHandle/")
    Observable<TResponse> UnFollowUser(@Body TRequest<ReqUidFuid> request);

    @POST("sportsHandle/")
    Observable<Response_FirstCircle> FirstCircle(@Body TRequest<ReqEmpty> request);

    @POST("sportsHandle/")
    Observable<Response_Carouse> Carouse(@Body TRequest<ReqReleaseId> request);

    @POST("sportsHandle/")
    Observable<Response_SearchCircle> Circle_Search(@Body TRequest<ReqKeywordPR> request);
}
