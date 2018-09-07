package com.kunleen.sn.sportnewsapplication.activity;


import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Environment;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.dou361.ijkplayer.widget.PlayStateParams;
import com.dou361.ijkplayer.widget.PlayerView;
import com.github.jdsjlzx.ItemDecoration.SpacesItemDecoration;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.google.gson.Gson;
import com.kunleen.sn.sportnewsapplication.AppConfig;
import com.kunleen.sn.sportnewsapplication.R;
import com.kunleen.sn.sportnewsapplication.adapter.CommentAdapter;
import com.kunleen.sn.sportnewsapplication.app.SNApplication;
import com.kunleen.sn.sportnewsapplication.base.BaseActivity;
import com.kunleen.sn.sportnewsapplication.custom.SharePopupWindow;
import com.kunleen.sn.sportnewsapplication.custom.SlidingLayout;
import com.kunleen.sn.sportnewsapplication.custom.goodview.GoodView;
import com.kunleen.sn.sportnewsapplication.login.WechatLoginHelper;
import com.kunleen.sn.sportnewsapplication.network.RetrofitClient;
import com.kunleen.sn.sportnewsapplication.network.bean.ReqComment;
import com.kunleen.sn.sportnewsapplication.network.bean.ReqCommentGood;
import com.kunleen.sn.sportnewsapplication.network.bean.ReqNewsComment;
import com.kunleen.sn.sportnewsapplication.network.bean.ReqNewsDetail;
import com.kunleen.sn.sportnewsapplication.network.bean.TRequest;
import com.kunleen.sn.sportnewsapplication.network.bean.TRespon_Comment;
import com.kunleen.sn.sportnewsapplication.network.bean.TResponse;
import com.kunleen.sn.sportnewsapplication.network.bean.TResponse_NewsDetail;
import com.kunleen.sn.sportnewsapplication.network.service.HttpService;
import com.kunleen.sn.sportnewsapplication.utils.AppUtils;
import com.kunleen.sn.sportnewsapplication.utils.DevicesUtils;
import com.kunleen.sn.sportnewsapplication.utils.FileUtils;
import com.kunleen.sn.sportnewsapplication.utils.GlideCacheUtil;
import com.kunleen.sn.sportnewsapplication.utils.StringUtils;
import com.kunleen.sn.sportnewsapplication.utils.ToastUtils;
import com.kunleen.sn.sportnewsapplication.utils.videoutils.MediaUtils;
import com.sina.weibo.sdk.api.TextObject;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.share.WbShareCallback;
import com.sina.weibo.sdk.share.WbShareHandler;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import java.io.File;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;


public class VideoActivity extends BaseActivity implements View.OnClickListener, WbShareCallback {
    public static final String TAG = "VideoActivity_LOG";
    private PlayerView player;
    private Context mContext;
    private PowerManager.WakeLock wakeLock;
    View rootView;
    String url;
    LRecyclerView rec_comment;
    private CommentAdapter mDataAdapter;
    View v;
    private int _title_id;
    private int PAGE;
    View ll_comment;
    LinearLayout ll_news;
    SharePopupWindow mPopwindow;
    private String _title;
    private String WebUrl;
    private WbShareHandler shareHandler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mContext = this;
        rootView = getLayoutInflater().from(this).inflate(R.layout.activity_video, null);
        setContentView(rootView);
        url = getIntent().getStringExtra("url");
        initWindow(this);
        initClose();
        initVideo();
        initRecyclerView();
        v.findViewById(R.id.webview).setVisibility(View.GONE);
        initView();
        initNews();
        initComment(false);
        initAd();
    }

    private void initAd() {
        View v = this.v.findViewById(R.id.detail_ad);
        if (SNApplication.ad_list.size() != 0) {
            v.setVisibility(View.VISIBLE);
            int ad = new Random().nextInt(SNApplication.ad_list.size());
            ((TextView) v.findViewById(R.id.tv_news_title)).setText(SNApplication.ad_list.get(ad).getTitle());
            GlideCacheUtil.LoadImageInWifi(VideoActivity.this, v.findViewById(R.id.iv_news_img), SNApplication.ad_list.get(ad).getImageUrl());
            v.findViewById(R.id.tv_ad).setVisibility(View.VISIBLE);
            v.setOnClickListener(v1 -> AppUtils.OutToSysBrowser(SNApplication.ad_list.get(ad).getSecTitle().split(";")[0], VideoActivity.this));
        }
    }

    private void initNews() {
        _title_id = getIntent().getIntExtra("titleid", 0);
        WebUrl = AppConfig.WEB_ADDRESS + "?id=" + _title_id + "&type=" + 1;
        _title = getIntent().getStringExtra("title");
        TRequest<ReqNewsDetail> request = new TRequest<>();
        ReqNewsDetail detail = new ReqNewsDetail();
        detail.setTitleId(getIntent().getIntExtra("titleid", 0));
        request.setParam(detail, "1112", "1");
        Observable<TResponse_NewsDetail> newsDetail = RetrofitClient.getInstance().getService(HttpService.class).NewsDetail(request);
        sendRequest(newsDetail, new Consumer<TResponse_NewsDetail>() {
            @Override
            public void accept(TResponse_NewsDetail tResponse_newsDetail) throws Exception {
                if (tResponse_newsDetail.getData().getDiscussCount() != 0) {
                    ((TextView) findViewById(R.id.tv_discuss)).setText(tResponse_newsDetail.getData().getDiscussCount() + "");
                    ((TextView) v.findViewById(R.id.tv_news_comment)).setText(tResponse_newsDetail.getData().getDiscussCount() + "");
                } else {
                    findViewById(R.id.tv_discuss).setVisibility(View.GONE);
                }
                ((TextView) v.findViewById(R.id.tv_detail_title)).setText(tResponse_newsDetail.getData().getTitle());
                ((TextView) v.findViewById(R.id.tv_detail_via)).setText(tResponse_newsDetail.getData().getAuthor());
                ((TextView) v.findViewById(R.id.tv_detail_date)).setText(tResponse_newsDetail.getData().getCreateTime());
                ((TextView) v.findViewById(R.id.tv_news_good)).setText(tResponse_newsDetail.getData().getGood() + "");
                v.findViewById(R.id.ll_news_good).setOnClickListener(VideoActivity.this);
                ((TextView) v.findViewById(R.id.tv_news_bad)).setText(tResponse_newsDetail.getData().getBad() + "");
                v.findViewById(R.id.ll_news_bad).setOnClickListener(VideoActivity.this);
                List<TResponse_NewsDetail.Recommend> list = tResponse_newsDetail.getData().getList();
                if (list.size() == 0) {
                    v.findViewById(R.id.ll_tuijian).setVisibility(View.GONE);
                } else {
                    for (int i = 0; i < list.size(); i++) {
                        String title = list.get(i).getTitle();
                        String title_id = list.get(i).getTitleId();
                        String imageUrl = list.get(i).getImageUrl();
                        View view = LayoutInflater.from(VideoActivity.this).inflate(R.layout.news_noimg_layout_recommend, ll_news, false);
                        ((TextView) view.findViewById(R.id.tv_news_title)).setText(title);
                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        ll_news.addView(view, layoutParams);
                        ll_news.addView(LayoutInflater.from(VideoActivity.this).inflate(R.layout.view_devide, ll_news, false));
                        view.setOnClickListener(v -> {
                            Intent intent = new Intent(VideoActivity.this, VideoActivity.class);
                            intent.putExtra("title", title);
                            intent.putExtra("titleid", Integer.parseInt(title_id));
                            intent.putExtra("url", imageUrl.split(";")[1]);
                            startActivity(intent);
                        });
                    }
                }
            }
        }, throwable -> ToastUtils.showToast(throwable.getMessage()));
    }

    private void initRecyclerView() {
        rec_comment = findViewById(R.id.rec_comment);
        GridLayoutManager manager = new GridLayoutManager(this, 1);
        rec_comment.setLayoutManager(manager);
        mDataAdapter = new CommentAdapter(this);
        LRecyclerViewAdapter mLRecyclerViewAdapter = new LRecyclerViewAdapter(mDataAdapter);
        v = LayoutInflater.from(this).inflate(R.layout.layout_news_header, null);
        mLRecyclerViewAdapter.addHeaderView(v);
        v.setVisibility(View.VISIBLE);
        rec_comment.setAdapter(mLRecyclerViewAdapter);
        //设置头部加载颜色
        rec_comment.setHeaderViewColor(R.color.theme_green, R.color.darkgrey, android.R.color.white);
        //设置底部加载颜色
        rec_comment.setFooterViewColor(R.color.theme_green, R.color.darkgrey, android.R.color.white);
        //设置底部加载文字提示
        rec_comment.setFooterViewHint("拼命加载中", "没有更多评论了", "网络不给力啊，点击再试一次吧");
        int spacing = getResources().getDimensionPixelSize(R.dimen.dp_066);
        rec_comment.addItemDecoration(SpacesItemDecoration.newInstance(spacing, spacing, manager.getSpanCount(), Color.parseColor("#D3D3D3")));
        rec_comment.setHasFixedSize(true);
        rec_comment.setNestedScrollingEnabled(false);
        rec_comment.setPullRefreshEnabled(false);
//        rec_comment.setOnRefreshListener(() -> {
//            mDataAdapter.clear();
//            PAGE = 1;
//            initComment();
//        });
        rec_comment.setOnLoadMoreListener(() -> initComment(true));
    }

    private void initComment(boolean loadMore) {
        TRequest<ReqNewsComment> request = new TRequest<>();
        ReqNewsComment comment = new ReqNewsComment();
        comment.setId(_title_id);
        comment.setPage(PAGE++);
        comment.setRows(10);
        comment.setType(1);
        request.setParam(comment, "1116", "1");
        Observable<TRespon_Comment> newsDetail = RetrofitClient.getInstance().getService(HttpService.class).GetComment(request);
        sendRequest(newsDetail, new Consumer<TRespon_Comment>() {
            @Override
            public void accept(TRespon_Comment tRespon_comment) throws Exception {
                mDataAdapter.addAll(tRespon_comment.getPageInfo().getRows());
                if (!loadMore && tRespon_comment.getPageInfo().getRows().size() == 0) {
                    rec_comment.setFooterViewHint("拼命加载中", "", "网络不给力啊，点击再试一次吧");
                    v.findViewById(R.id.ll_all_comment).setVisibility(View.GONE);
                    return;
                }
                if (tRespon_comment.getPageInfo().getRows().size() < 10) {
                    rec_comment.setNoMore(true);
                }
                rec_comment.refreshComplete(tRespon_comment.getPageInfo().getRows().size());
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                ToastUtils.showToast(throwable.getMessage());
            }
        });

    }

    private void initView() {
        ll_news = v.findViewById(R.id.ll_news);
        ll_comment = findViewById(R.id.ll_comment);
        findViewById(R.id.iv_share).setOnClickListener(this);
        findViewById(R.id.tv_comment).setOnClickListener(this);
        ll_comment.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
            Rect r = new Rect();
            //获取当前界面可视部分
            VideoActivity.this.getWindow().getDecorView().getWindowVisibleDisplayFrame(r);
            //获取屏幕的高度
            int screenHeight = VideoActivity.this.getWindow().getDecorView().getRootView().getHeight();
            //此处就是用来获取键盘的高度的， 在键盘没有弹出的时候 此高度为0 键盘弹出的时候为一个正数
            int heightDifference = screenHeight - r.bottom;
            Log.d("heightDiff", "Size: " + heightDifference);
            if (heightDifference == 0) {
                findViewById(R.id.rl_comment).setVisibility(View.VISIBLE);
                findViewById(R.id.iv_share).setVisibility(View.VISIBLE);
                findViewById(R.id.tv_comment).setVisibility(View.GONE);
            } else {
                findViewById(R.id.rl_comment).setVisibility(View.GONE);
                findViewById(R.id.iv_share).setVisibility(View.GONE);
                findViewById(R.id.tv_comment).setVisibility(View.VISIBLE);
            }
        });
    }

    private void initVideo() {
        /**虚拟按键的隐藏方法*/
        rootView.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
            //比较Activity根布局与当前布局的大小
            int heightDiff = rootView.getRootView().getHeight() - rootView.getHeight();
            if (heightDiff > 100) {
                //大小超过100时，一般为显示虚拟键盘事件
                rootView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
            } else {
                //大小小于100时，为不显示虚拟键盘或虚拟键盘隐藏
                rootView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

            }
        });

        /**常亮*/
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wakeLock = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "liveTAG");
        wakeLock.acquire();
        //有部分视频加载有问题，这个视频是有声音显示不出图像的，没有解决http://fzkt-biz.oss-cn-hangzhou.aliyuncs.com/vedio/2f58be65f43946c588ce43ea08491515.mp4
        //这里模拟一个本地视频的播放，视频需要将testvideo文件夹的视频放到安卓设备的内置sd卡根目录中

        player = new PlayerView(this, rootView) {
            @Override
            public PlayerView toggleProcessDurationOrientation() {
                hideSteam(getScreenOrientation() == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                return setProcessDurationOrientation(getScreenOrientation() == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT ? PlayStateParams.PROCESS_PORTRAIT : PlayStateParams.PROCESS_LANDSCAPE);
            }
        }
                .setTitle(_title)
                .setProcessDurationOrientation(PlayStateParams.PROCESS_PORTRAIT)
                .setScaleType(PlayStateParams.fillparent)
                .forbidTouch(false)
                .hideSteam(true)
                .hideCenterPlayer(true).setForbidHideControlPanl(false)
                .showThumbnail(ivThumbnail -> GlideCacheUtil.LoadImage(mContext, ivThumbnail, url, false));
        if (SNApplication.WIFIState) {
            player.startPlay();
        } else {
            player.netTieVisible();
        }
    }

    //配置侧滑删除参数

    private void initClose() {
//        findViewById(R.id.view_video).setPadding(0, SNApplication.statusBar, 0, 0);
        SlidingLayout rootView = new SlidingLayout(this);
        rootView.bindActivity(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_share: {
                mPopwindow = new SharePopupWindow(VideoActivity.this, itemsOnClick);
                mPopwindow.showAtLocation(v,
                        Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                break;
            }
            case R.id.tv_comment: {
                if (SNApplication.userInfo.getUserInfo() != null) {
                    TRequest<ReqComment> request = new TRequest<>();
                    ReqComment req = new ReqComment();
                    req.setContentid(_title_id);
                    req.setParentid(0 + "");
                    req.setTitle(_title);
                    req.setDiscussinputtxt(((EditText) findViewById(R.id.et_comment)).getText().toString());
                    req.setUserid(SNApplication.userInfo.getUserInfo().getUid() + "");
                    request.setParamComment(req, "1115", "1");
                    Log.i("comment", new Gson().toJson(request));
                    Observable<TResponse> comment = RetrofitClient.getInstance().getService(HttpService.class).Comment(request);
                    sendRequest(comment, new Consumer<TResponse>() {
                        @Override
                        public void accept(TResponse tResponse) throws Exception {
                            ToastUtils.showToast("评论成功！");
                            DevicesUtils.hideKeyboard(VideoActivity.this);
                            ((EditText) findViewById(R.id.et_comment)).setText("");
                            mDataAdapter.clear();
                            initComment(false);
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            ToastUtils.showToast(throwable.getMessage());
                        }
                    });
                } else {
                    ToastUtils.showToast("请先登录！");
                }
                break;
            }
            case R.id.ll_news_good: {
                doSuggest(true);
                break;
            }
            case R.id.ll_news_bad: {
                doSuggest(false);
                break;
            }
        }
    }

    private void doSuggest(boolean type) {
        TRequest<ReqCommentGood> request = new TRequest<>();
        ReqCommentGood reqCommentGood = new ReqCommentGood();
        reqCommentGood.setId(_title_id + "");
        if (type) {
            reqCommentGood.setType("1");
        } else {
            reqCommentGood.setType("2");
        }
        request.setParam(reqCommentGood, "1117", "1");
        Observable<TResponse> observable = RetrofitClient.getInstance().getService(HttpService.class).CommentGood(request);
        sendRequest(observable, new Consumer<TResponse>() {
            @Override
            public void accept(TResponse tResponse) throws Exception {
                ImageView iv;
                TextView tv;
                if (type) {
                    iv = v.findViewById(R.id.iv_news_good);
                    iv.setImageResource(R.mipmap.icon_xqy_zan2);
                    tv = v.findViewById(R.id.tv_news_good);
                } else {
                    tv = findViewById(R.id.tv_news_bad);
                    iv = findViewById(R.id.iv_news_bad);
                    iv.setImageResource(R.mipmap.icon_xqy_cai2);
                }
                GoodView goodView = new GoodView(VideoActivity.this);
                goodView.setTextInfo("+1", Color.parseColor("#ff0bb731"), 14);
                goodView.show(iv);
                tv.setText(Integer.parseInt(tv.getText().toString()) + 1 + "");
                tv.setTextColor(Color.parseColor("#ff0bb731"));
                v.findViewById(R.id.ll_news_good).setOnClickListener(null);
                v.findViewById(R.id.ll_news_bad).setOnClickListener(null);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                ToastUtils.showToast(throwable.getMessage());
            }
        });
    }

    private View.OnClickListener itemsOnClick = v -> {
        switch (v.getId()) {
            case R.id.ll_weichat: {
                WechatShare(false);
                break;
            }
            case R.id.ll_wechatf: {
                WechatShare(true);
                break;
            }
            case R.id.ll_weibo: {
                if (AppUtils.isWeiboInstalled(VideoActivity.this)) {
                    sendMultiMessage();
                } else {
                    ToastUtils.showToast("请先安装微博客户端！");
                }
                break;
            }
            case R.id.ll_qq: {
                ToastUtils.showToast("QQ分享功能暂未开放！");
                break;
            }
        }
        mPopwindow.dismiss();
    };

    private void sendMultiMessage() {
        shareHandler = new WbShareHandler(this);
        shareHandler.registerApp();
        TextObject textObject = new TextObject();
        getShareBitmap(bitmap1 -> runOnUiThread(() -> {
            textObject.text = _title + WebUrl + "(分享自爱球君)";
            WeiboMultiMessage weiboMessage = new WeiboMultiMessage();//初始化微博的分享消息
            weiboMessage.textObject = textObject;
            weiboMessage.mediaObject = AppUtils.getWebpageObj(bitmap1);
            shareHandler.shareMessage(weiboMessage, false);
        }));
    }


    private void WechatShare(boolean isFriend) {
        WechatLoginHelper.weixinAPI = WXAPIFactory.createWXAPI(VideoActivity.this, null);
        WechatLoginHelper.weixinAPI.registerApp(WechatLoginHelper.APP_ID);
        if (!WechatLoginHelper.weixinAPI.isWXAppInstalled()) {
            Toast.makeText(VideoActivity.this, "您还未安装微信客户端，无法进行微信分享！",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = WebUrl;
        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = "爱球君";
        msg.description = _title;
        getShareBitmap(bitmap1 -> runOnUiThread(() -> {
            msg.thumbData = FileUtils.bitmap2Bytes(bitmap1, 32);
            SendMessageToWX.Req req = new SendMessageToWX.Req();
            req.transaction = StringUtils.buildTransaction("webpage");
            req.message = msg;
            if (isFriend) {
                req.scene = SendMessageToWX.Req.WXSceneSession;
            } else {
                req.scene = SendMessageToWX.Req.WXSceneTimeline;
            }
            WechatLoginHelper.weixinAPI.sendReq(req);
        }));
    }

    /**
     * 播放本地视频
     */

    private String getLocalVideoPath(String name) {
        String sdCard = Environment.getExternalStorageDirectory().getPath();
        String uri = sdCard + File.separator + name;
        return uri;
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (player != null) {
            player.onPause();
        }
        /**demo的内容，恢复系统其它媒体的状态*/
        MediaUtils.muteAudioFocus(mContext, true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (player != null) {
            player.onResume();
        }
        /**demo的内容，暂停系统其它媒体的状态*/
        MediaUtils.muteAudioFocus(mContext, false);
        /**demo的内容，激活设备常亮状态*/
        if (wakeLock != null) {
            wakeLock.acquire();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (player != null) {
            player.onDestroy();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (player != null) {
            player.onConfigurationChanged(newConfig);
        }
    }

    @Override
    public void onBackPressed() {
        if (player != null && player.onBackPressed()) {
            return;
        }
        super.onBackPressed();
        /**demo的内容，恢复设备亮度状态*/
        if (wakeLock != null) {
            wakeLock.release();
        }
    }

    private void getShareBitmap(WebViewActivity.SaveResultCallback saveResultCallback) {
        new Thread(() -> {
            try {
                Bitmap bitmap = Glide.with(VideoActivity.this).asBitmap().load(url).into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL).get();
                saveResultCallback.onSavedSuccess(bitmap);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }).start();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        shareHandler.doResultIntent(intent, VideoActivity.this);
    }

    @Override
    public void onWbShareSuccess() {
        ToastUtils.showToast("分享成功");
    }

    @Override
    public void onWbShareCancel() {
        ToastUtils.showToast("分享取消");
    }

    @Override
    public void onWbShareFail() {
        ToastUtils.showToast("分享失败");
    }
}
