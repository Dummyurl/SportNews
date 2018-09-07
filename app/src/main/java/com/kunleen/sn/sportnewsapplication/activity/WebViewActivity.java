package com.kunleen.sn.sportnewsapplication.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.GridLayoutManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.github.jdsjlzx.ItemDecoration.SpacesItemDecoration;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.kunleen.sn.sportnewsapplication.AppConfig;
import com.kunleen.sn.sportnewsapplication.R;
import com.kunleen.sn.sportnewsapplication.adapter.CommentAdapter;
import com.kunleen.sn.sportnewsapplication.app.SNApplication;
import com.kunleen.sn.sportnewsapplication.base.BaseActivity;
import com.kunleen.sn.sportnewsapplication.custom.MyLRecyclerView;
import com.kunleen.sn.sportnewsapplication.custom.SharePopupWindow;
import com.kunleen.sn.sportnewsapplication.custom.TitleBarView;
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
import com.kunleen.sn.sportnewsapplication.webtools.MJavascriptInterface;
import com.kunleen.sn.sportnewsapplication.webtools.MyWebViewClient;
import com.kunleen.sn.sportnewsapplication.webtools.WebImgUtils;
import com.mabeijianxi.jianxiexpression.ExpressionGridFragment;
import com.mabeijianxi.jianxiexpression.ExpressionShowFragment;
import com.sina.weibo.sdk.api.TextObject;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.share.WbShareCallback;
import com.sina.weibo.sdk.share.WbShareHandler;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;

import static com.kunleen.sn.sportnewsapplication.utils.DevicesUtils.hideKeyboard;


public class WebViewActivity extends BaseActivity implements View.OnClickListener, WbShareCallback, ExpressionGridFragment.ExpressionClickListener, ExpressionGridFragment.ExpressionDeleteClickListener {
    public static final String TAG = "WebViewActivity_log";
    private WebView mWebView;
    View ll_comment, rl_news_detail;
    LinearLayout ll_news;
    SharePopupWindow mPopwindow;
    private int _title_id;
    private String _title;
    private String[] imageUrls;
    MyLRecyclerView rec_comment;
    private CommentAdapter mDataAdapter;
    int PAGE = 1;
    private View v;
    private String WebUrl;
    private WbShareHandler shareHandler;
    private TitleBarView bar;
    private FrameLayout fl_emogi;
    private ExpressionShowFragment expressionShowFragment;
    private ImageView iv_emogi;
    private boolean isEmogiShow = false;
    private Handler handler = new Handler();
    private boolean keyboardShown;
    private int supportSoftInputHeight;
    private float value;
    private TranslateAnimation showAnimation;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        initRecyclerView();
        bindActivity();
        initWindow(this);
        initView();
        initTitleBar();
        initNews();
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
        sendRequest(newsDetail, tRespon_comment -> {
            rec_comment.setFooterViewHint("拼命加载中", "没有更多评论了！", "网络不给力啊，点击再试一次吧");
            mDataAdapter.addAll(tRespon_comment.getPageInfo().getRows());
            v.findViewById(R.id.ll_all_comment).setVisibility(View.VISIBLE);
            if (!loadMore && tRespon_comment.getPageInfo().getRows().size() == 0) {
                rec_comment.setFooterViewHint("拼命加载中", "还没有评论快来抢沙发！", "网络不给力啊，点击再试一次吧");
                rec_comment.hideFooterView(false);
            }
            if (tRespon_comment.getPageInfo().getRows().size() < 10) {
                rec_comment.setNoMore(true);
                rec_comment.hideFooterView(false);
            }
            rec_comment.refreshComplete(tRespon_comment.getPageInfo().getRows().size());
        }, throwable -> ToastUtils.showToast(throwable.getMessage()));
    }


    private void WechatShare(boolean isFriend) {
        getShareBitmap(bitmap -> runOnUiThread(() -> {
            WechatLoginHelper.weixinAPI = WXAPIFactory.createWXAPI(WebViewActivity.this, null);
            WechatLoginHelper.weixinAPI.registerApp(WechatLoginHelper.APP_ID);
            if (!WechatLoginHelper.weixinAPI.isWXAppInstalled()) {
                Toast.makeText(WebViewActivity.this, "您还未安装微信客户端，无法进行微信分享！",
                        Toast.LENGTH_SHORT).show();
                return;
            }
            WXWebpageObject webpage = new WXWebpageObject();
            webpage.webpageUrl = WebUrl;
            WXMediaMessage msg = new WXMediaMessage(webpage);
            msg.title = "爱球君";
            msg.description = _title;
            if (bitmap != null) {
                msg.thumbData = FileUtils.bitmap2Bytes(bitmap, 32);
            } else {
                Bitmap bitmap1 = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
                msg.thumbData = FileUtils.getBitmapByte(bitmap1);
            }
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

    private void getShareBitmap(SaveResultCallback saveResultCallback) {
        new Thread(() -> {
            try {
                if (imageUrls.length != 0) {
                    Bitmap bitmap = Glide.with(WebViewActivity.this).asBitmap().load(imageUrls[0]).into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL).get();
                    saveResultCallback.onSavedSuccess(bitmap);
                } else {
                    saveResultCallback.onSavedSuccess(null);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }).start();
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
                if (AppUtils.isWeiboInstalled(WebViewActivity.this)) {
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

    private void initAd() {
        View v = this.v.findViewById(R.id.detail_ad);
        if (SNApplication.ad_list.size() != 0) {
            int ad = new Random().nextInt(SNApplication.ad_list.size());
            ((TextView) v.findViewById(R.id.tv_news_title)).setText(SNApplication.ad_list.get(ad).getTitle());
            GlideCacheUtil.LoadImageInWifi(WebViewActivity.this, v.findViewById(R.id.iv_news_img), SNApplication.ad_list.get(ad).getImageUrl());
            v.findViewById(R.id.tv_ad).setVisibility(View.VISIBLE);
            v.setOnClickListener(v1 -> AppUtils.OutToSysBrowser(SNApplication.ad_list.get(ad).getSecTitle().split(";")[0], WebViewActivity.this));
            v.setVisibility(View.VISIBLE);
        }
    }

    private void initNews() {
        _title_id = getIntent().getIntExtra("titleid", 0);
        WebUrl = AppConfig.WEB_ADDRESS + "?id=" + _title_id + "&type=" + 1;
        _title = getIntent().getStringExtra("title");
        TRequest<ReqNewsDetail> request = new TRequest<>();
        ReqNewsDetail detail = new ReqNewsDetail();
        detail.setTitleId(_title_id);
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
                WebSettings settings = mWebView.getSettings();
                settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);//把html中的内容放大webview等宽的一列中
                settings.setJavaScriptEnabled(true);//支持js
                switch (SNApplication.userInfo.getFontMode()) {
                    case 0: {
                        settings.setTextZoom(90);
                        break;
                    }
                    case 1: {
                        settings.setTextZoom(110);
                        break;
                    }
                    case 2: {
                        settings.setTextZoom(130);
                        break;
                    }
                    case 3: {
                        settings.setTextZoom(150);
                        break;
                    }
                }
                v.findViewById(R.id.tv_via).setVisibility(View.VISIBLE);
                ((TextView) v.findViewById(R.id.tv_detail_title)).setText(tResponse_newsDetail.getData().getTitle());
                ((TextView) v.findViewById(R.id.tv_detail_via)).setText(tResponse_newsDetail.getData().getAuthor());
                ((TextView) v.findViewById(R.id.tv_detail_date)).setText(tResponse_newsDetail.getData().getCreateTime());
                ((TextView) v.findViewById(R.id.tv_news_good)).setText(tResponse_newsDetail.getData().getGood() + "");
                v.findViewById(R.id.ll_news_good).setOnClickListener(WebViewActivity.this);
                ((TextView) v.findViewById(R.id.tv_news_bad)).setText(tResponse_newsDetail.getData().getBad() + "");
                v.findViewById(R.id.ll_news_bad).setOnClickListener(WebViewActivity.this);
                settings.setSupportZoom(true);
                mWebView.loadData(getNewContent(tResponse_newsDetail.getData().getTxt()), "text/html;charset=utf-8", "UTF-8");
                imageUrls = WebImgUtils.returnImageUrlsFromHtml(tResponse_newsDetail.getData().getTxt());
                mWebView.addJavascriptInterface(new MJavascriptInterface(WebViewActivity.this, imageUrls), "imagelistener");
                mWebView.setWebViewClient(new MyWebViewClient() {
                    @Override
                    public void onPageFinished(WebView vie, String url) {
                        super.onPageFinished(vie, url);
                        initComment(false);
                        initAd();
                        load();
                        List<TResponse_NewsDetail.Recommend> list = tResponse_newsDetail.getData().getList();
                        if (list.size() == 0) {
                            v.findViewById(R.id.ll_tuijian).setVisibility(View.GONE);
                        } else {
                            v.findViewById(R.id.ll_tuijian).setVisibility(View.VISIBLE);
                            for (int i = 0; i < list.size() && ll_news.getChildCount() <= 5; i++) {
                                String title = list.get(i).getTitle();
                                String title_id = list.get(i).getTitleId();
                                View view = LayoutInflater.from(WebViewActivity.this).inflate(R.layout.news_noimg_layout_recommend, ll_news, false);
                                ((TextView) view.findViewById(R.id.tv_news_title)).setText(title);
                                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                ll_news.addView(view, layoutParams);
                                ll_news.addView(LayoutInflater.from(WebViewActivity.this).inflate(R.layout.view_devide, ll_news, false));
                                view.setOnClickListener(v -> {
                                    if (DevicesUtils.isNetworkAvailable(WebViewActivity.this)) {
                                        Intent intent = new Intent(WebViewActivity.this, WebViewActivity.class);
                                        intent.putExtra("title", title);
                                        intent.putExtra("titleid", Integer.parseInt(title_id));
                                        startActivity(intent);
                                    } else {
                                        ToastUtils.showToast("网络状况不佳，请连接网络后再试！");
                                    }
                                });
                            }
                        }
                    }
                });
            }
        }, throwable -> ToastUtils.showToast(throwable.getMessage()));

    }

    private void load() {
        v.findViewById(R.id.ll_news_pingjia).setVisibility(View.VISIBLE);
        v.findViewById(R.id.line_1).setVisibility(View.VISIBLE);
        v.findViewById(R.id.line_2).setVisibility(View.VISIBLE);
        v.findViewById(R.id.line_3).setVisibility(View.VISIBLE);
    }


    private void initView() {
        rl_news_detail = findViewById(R.id.rl_news_detail);
        iv_emogi = findViewById(R.id.iv_emogi);
        iv_emogi.setOnClickListener(this);
        fl_emogi = findViewById(R.id.fl_emogi);
        mWebView = v.findViewById(R.id.webview);
        ll_news = v.findViewById(R.id.ll_news);
        ll_comment = findViewById(R.id.ll_comment);
        findViewById(R.id.iv_share).setOnClickListener(this);
        findViewById(R.id.et_comment).setOnClickListener(this);
        findViewById(R.id.tv_comment).setOnClickListener(this);
        if (SNApplication.supportSoftInputHeight != 0) {
            fl_emogi.getLayoutParams().height = SNApplication.supportSoftInputHeight;
            fl_emogi.requestLayout();
            fl_emogi.setVisibility(View.GONE);
            isEmogiShow = false;
            iv_emogi.setImageResource(R.mipmap.button_cjxt_bq);
        }
        rl_news_detail.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
//                Rect r = new Rect();
//                //获取当前界面可视部分
//                WebViewActivity.this.getWindow().getDecorView().getWindowVisibleDisplayFrame(r);
//                //获取屏幕的高度
//                int screenHeight = WebViewActivity.this.getWindow().getDecorView().getRootView().getHeight();
//                //此处就是用来获取键盘的高度的， 在键盘没有弹出的时候 此高度为0 键盘弹出的时候为一个正数
//                int heightDifference = screenHeight - r.bottom;
//                Log.d("heightDiff", "Size: " + heightDifference);
//                if (heightDifference == 0) {
//                    if (!isEmogiShow) {
//                        fl_emogi.setVisibility(View.GONE);
//                        isEmogiShow = false;
//                        iv_emogi.setImageResource(R.mipmap.fabu_biaoqing_icon);
//                    }
//                } else {
//                    if (SNApplication.supportSoftInputHeight != DevicesUtils.getSupportSoftInputHeight(WebViewActivity.this) + SNApplication.navigationBar) {
//                        ll_comment.getViewTreeObserver().removeGlobalOnLayoutListener(this);
//                        SNApplication.supportSoftInputHeight = DevicesUtils.getSupportSoftInputHeight(WebViewActivity.this) + SNApplication.navigationBar;
//                        fl_emogi.getLayoutParams().height = SNApplication.supportSoftInputHeight;
//                        fl_emogi.requestLayout();
//                        ll_comment.getViewTreeObserver().addOnGlobalLayoutListener(this);
//                        fl_emogi.setVisibility(View.GONE);
//                        isEmogiShow = false;
//                        iv_emogi.setImageResource(R.mipmap.fabu_biaoqing_icon);
//                    }
//                    hideComment(false);
//                }
                keyboardShown = DevicesUtils.isKeyboardShown(rl_news_detail);
                if (fl_emogi != null) {
                    if (keyboardShown) {
                        if (isEmogiShow) {
                            hideExpress();
                        }
                        hideComment(false);
                        if (supportSoftInputHeight != DevicesUtils.getSupportSoftInputHeight(WebViewActivity.this)) {
//                            iv_emogi.setImageResource(R.drawable.fabu_biaoqing_icon);
                            isEmogiShow = false;
                            rl_news_detail.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                            supportSoftInputHeight = DevicesUtils.getSupportSoftInputHeight(WebViewActivity.this);
                            fl_emogi.getLayoutParams().height = supportSoftInputHeight;
                            fl_emogi.requestLayout();
                            fl_emogi.setVisibility(View.GONE);
                            rl_news_detail.getViewTreeObserver().addOnGlobalLayoutListener(this);
                        }
                    } else {
                        if (!isEmogiShow) {
                            hideExpress();
                            hideComment(true);
                        }
                    }
                }
            }
        });
    }

    private void hideComment(boolean isHide) {
        if (isHide) {
            findViewById(R.id.rl_comment).setVisibility(View.VISIBLE);
            findViewById(R.id.iv_share).setVisibility(View.VISIBLE);
            findViewById(R.id.tv_comment).setVisibility(View.GONE);
        } else {
            findViewById(R.id.rl_comment).setVisibility(View.GONE);
            findViewById(R.id.iv_share).setVisibility(View.GONE);
            findViewById(R.id.tv_comment).setVisibility(View.VISIBLE);
        }
    }

    private void hideExpress() {
        iv_emogi.setImageResource(R.mipmap.button_cjxt_bq);
        fl_emogi.setVisibility(View.GONE);
        isEmogiShow = false;
//        if (hideAnimation == null) {
//            hideAnimation = new TranslateAnimation(
//                    Animation.RELATIVE_TO_SELF,
//                    0,
//                    Animation.RELATIVE_TO_SELF,
//                    0,
//                    Animation.RELATIVE_TO_SELF,
//                    0,
//                    Animation.RELATIVE_TO_SELF,
//                    value);
//            hideAnimation.setFillAfter(false);
//            hideAnimation.setDuration(200);
//            hideAnimation.setInterpolator(new LinearInterpolator());
//            hideAnimation.setAnimationListener(new Animation.AnimationListener() {
//                @Override
//                public void onAnimationStart(Animation animation) {
//
//                }
//
//                @Override
//                public void onAnimationEnd(Animation animation) {
//                    fl_emogi.setVisibility(View.GONE);
//                    ll_bottom.clearAnimation();
//
//                }
//
//                @Override
//                public void onAnimationRepeat(Animation animation) {
//
//                }
//            });
//        }
//        if (isEmogiShow) {
//            ll_bottom.startAnimation(hideAnimation);
//        }
    }

    private void showExpress() {
        fl_emogi.setVisibility(View.VISIBLE);
        iv_emogi.setImageResource(R.mipmap.fabu_keyboard_icon);
        hideComment(false);
        value = (float) (fl_emogi.getHeight() * 1.0 / (fl_emogi.getHeight() + ll_comment.getHeight()));
        if (showAnimation == null) {
            showAnimation = new TranslateAnimation(
                    Animation.RELATIVE_TO_SELF,
                    0,
                    Animation.RELATIVE_TO_SELF,
                    0,
                    Animation.RELATIVE_TO_SELF,
                    value,
                    Animation.RELATIVE_TO_SELF,
                    0);
            showAnimation.setFillAfter(false);
            showAnimation.setDuration(200);
            showAnimation.setInterpolator(new LinearInterpolator());
            showAnimation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    ll_comment.clearAnimation();
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        }
        ll_comment.startAnimation(showAnimation);
    }

    private void initRecyclerView() {
        rec_comment = findViewById(R.id.rec_comment);
        GridLayoutManager manager = new GridLayoutManager(this, 1);
        rec_comment.setLayoutManager(manager);
        mDataAdapter = new CommentAdapter(this);
        LRecyclerViewAdapter mLRecyclerViewAdapter = new LRecyclerViewAdapter(mDataAdapter);
        v = LayoutInflater.from(this).inflate(R.layout.layout_news_header, null);
        mLRecyclerViewAdapter.addHeaderView(v);
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
        rec_comment.setOnLoadMoreListener(() -> initComment(true));
    }


    // Prevent the back-button from closing the app
    @Override
    public void onBackPressed() {
        if (isEmogiShow) {
            fl_emogi.setVisibility(View.GONE);
            hideKeyboard(this);
            hideComment(true);
            isEmogiShow = false;
        } else {
            if (mWebView.canGoBack()) {
                mWebView.goBack();
            } else {
                super.onBackPressed();
            }
        }
    }


    @Override
    public void finish() {
        //设置返回的数据
        Intent intent = new Intent();
        intent.putExtra("pay_result", true);
        setResult(RESULT_OK, intent);
        super.finish();
    }

    private void initTitleBar() {
        bar = findViewById(R.id.titlebar);
        bar.setActivity(this);
        bar.hideRightButton(true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.et_comment: {
                isEmogiShow = false;
                iv_emogi.setImageResource(R.mipmap.button_cjxt_bq);
                break;
            }
            case R.id.iv_share: {
                mPopwindow = new SharePopupWindow(WebViewActivity.this, itemsOnClick);
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
                    try {
                        req.setDiscussinputtxt(URLEncoder.encode(((EditText) findViewById(R.id.et_comment)).getText().toString(), "UTF-8"));
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    req.setUserid(SNApplication.userInfo.getUserInfo().getUid() + "");
                    request.setParamComment(req, "1115", "1");
                    Observable<TResponse> comment = RetrofitClient.getInstance().getService(HttpService.class).Comment(request);
                    findViewById(R.id.tv_comment).setOnClickListener(null);
                    sendRequest(comment, tResponse -> {
                        findViewById(R.id.tv_comment).setOnClickListener(WebViewActivity.this);
                        ToastUtils.showToast("评论成功！");
                        hideKeyboard(WebViewActivity.this);
                        hideComment(true);
                        fl_emogi.setVisibility(View.GONE);
                        iv_emogi.setImageResource(R.mipmap.button_cjxt_bq);
                        isEmogiShow = false;
                        ((EditText) findViewById(R.id.et_comment)).setText("");
                        mDataAdapter.clear();
                        PAGE = 1;
                        initComment(false);
                    }, throwable -> {
                        findViewById(R.id.tv_comment).setOnClickListener(WebViewActivity.this);
                        Log.i(TAG, throwable.getMessage());
                        ToastUtils.showToast(throwable.getMessage());
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
            case R.id.iv_emogi: {
                if (isEmogiShow) {
                    isEmogiShow = false;
                    DevicesUtils.showKeyboard(this, findViewById(R.id.et_comment));
                    iv_emogi.setImageResource(R.mipmap.button_cjxt_bq);
                } else {
                    hideKeyboard(this);
                    hideComment(false);
                    handler.postDelayed(() -> {

                        replaceEmogi();
                    }, 500);
                }
                break;
            }
        }
    }

    /**
     * 表情显示
     */
    private void replaceEmogi() {
        isEmogiShow = true;
        showExpress();
        if (expressionShowFragment == null) {
            expressionShowFragment = ExpressionShowFragment.newInstance();
            getSupportFragmentManager().beginTransaction().replace(R.id.fl_emogi, ExpressionShowFragment.newInstance()).commit();
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
        sendRequest(observable, tResponse -> {
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
            GoodView goodView = new GoodView(WebViewActivity.this);
            goodView.setTextInfo("+1", Color.parseColor("#ff0bb731"), 14);
            goodView.show(iv);
            tv.setText(Integer.parseInt(tv.getText().toString()) + 1 + "");
            tv.setTextColor(Color.parseColor("#ff0bb731"));
            v.findViewById(R.id.ll_news_good).setOnClickListener(null);
            v.findViewById(R.id.ll_news_bad).setOnClickListener(null);
        }, throwable -> ToastUtils.showToast(throwable.getMessage()));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        hideKeyboard(this);
    }

    public static String getNewContent(String htmltext) {
        try {
            Document doc = Jsoup.parse(htmltext);
            Elements elements = doc.getElementsByTag("img");
            for (Element element : elements) {
                element.attr("width", "100%").attr("height", "auto");
            }
            return doc.toString();
        } catch (Exception e) {
            return htmltext;
        }
    }

    public interface SaveResultCallback {
        void onSavedSuccess(Bitmap bitmap);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        shareHandler.doResultIntent(intent, this);
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

    @Override
    public void expressionDeleteClick(View v) {
        ExpressionShowFragment.delete(findViewById(R.id.et_comment));
    }

    /**
     * 这里必须实现表情点击后才能把具体表情传入edittext
     *
     * @param str
     */
    @Override
    public void expressionClick(String str) {
        ExpressionShowFragment.input(findViewById(R.id.et_comment), str);
    }


}
