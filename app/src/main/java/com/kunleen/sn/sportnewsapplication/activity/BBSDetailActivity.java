package com.kunleen.sn.sportnewsapplication.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.jdsjlzx.ItemDecoration.SpacesItemDecoration;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.kunleen.sn.sportnewsapplication.R;
import com.kunleen.sn.sportnewsapplication.adapter.BBSCommentAdapter;
import com.kunleen.sn.sportnewsapplication.app.SNApplication;
import com.kunleen.sn.sportnewsapplication.base.BaseActivity;
import com.kunleen.sn.sportnewsapplication.custom.MyLRecyclerView;
import com.kunleen.sn.sportnewsapplication.custom.TitleBarView;
import com.kunleen.sn.sportnewsapplication.custom.goodview.GoodView;
import com.kunleen.sn.sportnewsapplication.network.RetrofitClient;
import com.kunleen.sn.sportnewsapplication.network.bean.ReqBBSComment;
import com.kunleen.sn.sportnewsapplication.network.bean.ReqNid;
import com.kunleen.sn.sportnewsapplication.network.bean.ReqNidPageRows;
import com.kunleen.sn.sportnewsapplication.network.bean.ReqNidType;
import com.kunleen.sn.sportnewsapplication.network.bean.TRequest;
import com.kunleen.sn.sportnewsapplication.network.bean.TResponse;
import com.kunleen.sn.sportnewsapplication.network.service.HttpService;
import com.kunleen.sn.sportnewsapplication.utils.AppUtils;
import com.kunleen.sn.sportnewsapplication.utils.DevicesUtils;
import com.kunleen.sn.sportnewsapplication.utils.GlideCacheUtil;
import com.kunleen.sn.sportnewsapplication.utils.ToastUtils;
import com.mabeijianxi.jianxiexpression.ExpressionGridFragment;
import com.mabeijianxi.jianxiexpression.ExpressionShowFragment;
import com.mabeijianxi.jianxiexpression.widget.ExpressionEditText;

import io.reactivex.functions.Consumer;

import static com.kunleen.sn.sportnewsapplication.utils.DevicesUtils.hideKeyboard;
import static com.kunleen.sn.sportnewsapplication.utils.DevicesUtils.showKeyboard;

public class BBSDetailActivity extends BaseActivity implements View.OnClickListener, ExpressionGridFragment.ExpressionClickListener, ExpressionGridFragment.ExpressionDeleteClickListener {
    public static final String TAG = "BBSDetailActivity_log";
    MyLRecyclerView rec_bbs_detail;
    private View v, ll_bottom, ll_comment, ll_bbs_detail;
    private static final int PAGE_SIZE = 10;
    private static int PAGE_INDEX = 1;
    private BBSCommentAdapter mDataAdapter;
    private View rl_act_bbs;
    private boolean isEmogiShow = false;
    private FrameLayout fl_emogi;
    private ExpressionShowFragment expressionShowFragment;
    private TranslateAnimation hideAnimation, showAnimation;
    ExpressionEditText et_comment;
    private float value;
    ImageView iv_emogi;
    private boolean keyboardShown;
    private int supportSoftInputHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bbsdetail);
        initView();
        initWindow(this);
        bindActivity();
        getBBSData();
        getComment(1);
    }

    private void getComment(int page) {
        TRequest<ReqNidPageRows> request = new TRequest<>();
        ReqNidPageRows nid = new ReqNidPageRows();
        nid.setNid(getIntent().getStringExtra("nid"));
        nid.setRows(PAGE_SIZE);
        nid.setPage(page);
        request.setParam(nid, "1128", "1");
        sendRequest(RetrofitClient.getInstance().getService(HttpService.class).BBSComment(request), response_bbsComment -> {
            rec_bbs_detail.setFooterViewHint("拼命加载中", "没有更多评论了！", "网络不给力啊，点击再试一次吧");
            mDataAdapter.addAll(response_bbsComment.getPageInfo().getRows());
            rec_bbs_detail.refreshComplete(response_bbsComment.getPageInfo().getRows().size());
            if (mDataAdapter.getDataList().size() == 0)
                rec_bbs_detail.setFooterViewHint("拼命加载中", "还没有评论，快来抢沙发！", "网络不给力啊，点击再试一次吧");
            if (response_bbsComment.getPageInfo().getRows().size() < PAGE_SIZE) {
                rec_bbs_detail.setNoMore(true);
                new Handler().postDelayed(() -> rec_bbs_detail.hideFooterView(false), 200);
            }

        }, throwable -> ToastUtils.showToast(throwable.getMessage()));
    }

    private void initView() {
        findViewById(R.id.tv_comment).setOnClickListener(this);
        ll_bottom = findViewById(R.id.ll_bottom);
        ll_comment = findViewById(R.id.ll_comment);
        rec_bbs_detail = findViewById(R.id.rec_bbs_detail);
        initRec();
        v.findViewById(R.id.tv_bbs_good).setOnClickListener(this);
        v.findViewById(R.id.tv_bbs_bad).setOnClickListener(this);
        TitleBarView view = findViewById(R.id.titlebar);
        view.setActivity(this);
        rl_act_bbs = findViewById(R.id.rl_act_bbs);
        et_comment = findViewById(R.id.et_comment);
        iv_emogi = findViewById(R.id.iv_emogi);
        iv_emogi.setOnClickListener(this);
        fl_emogi = findViewById(R.id.fl_emogi);
        rl_act_bbs.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
//                Rect r = new Rect();
//                //获取当前界面可视部分
//                BBSDetailActivity.this.getWindow().getDecorView().getWindowVisibleDisplayFrame(r);
//                int screenHeight = BBSDetailActivity.this.getWindow().getDecorView().getRootView().getHeight();
//                int heightDifference = screenHeight - r.bottom;
//                if (heightDifference == 0) {
//                    if (!isEmogiShow) {
//                        iv_emogi.setImageResource(R.mipmap.fabu_biaoqing_icon);
//                        fl_emogi.setVisibility(View.GONE);
//                        isEmogiShow = false;
//                    }
//                } else {
//                    if (SNApplication.supportSoftInputHeight != DevicesUtils.getSupportSoftInputHeight(BBSDetailActivity.this) + SNApplication.navigationBar) {
//                        rl_act_bbs.getViewTreeObserver().removeGlobalOnLayoutListener(this);
//                        SNApplication.supportSoftInputHeight = DevicesUtils.getSupportSoftInputHeight(BBSDetailActivity.this) + SNApplication.navigationBar;
//                        fl_emogi.getLayoutParams().height = SNApplication.supportSoftInputHeight;
//                        fl_emogi.requestLayout();
//                        rl_act_bbs.getViewTreeObserver().addOnGlobalLayoutListener(this);
//                        fl_emogi.setVisibility(View.GONE);
//                        iv_emogi.setImageResource(R.mipmap.fabu_biaoqing_icon);
//                        isEmogiShow = false;
//                    } else {
//                        fl_emogi.getLayoutParams().height = SNApplication.supportSoftInputHeight;
//                        fl_emogi.requestLayout();
//                    }
//                    if (isEmogiShow) {
//                        iv_emogi.setImageResource(R.mipmap.fabu_biaoqing_icon);
//                        fl_emogi.setVisibility(View.GONE);
//                        isEmogiShow = false;
//                    }
//                }
                keyboardShown = DevicesUtils.isKeyboardShown(rl_act_bbs);
                if (fl_emogi != null) {
                    if (keyboardShown) {
                        if (isEmogiShow) {
                            hideExpress();
                        }
                        if (supportSoftInputHeight != DevicesUtils.getSupportSoftInputHeight(BBSDetailActivity.this)) {
//                            iv_emogi.setImageResource(R.drawable.fabu_biaoqing_icon);
                            isEmogiShow = false;
                            rl_act_bbs.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                            supportSoftInputHeight = DevicesUtils.getSupportSoftInputHeight(BBSDetailActivity.this);
                            fl_emogi.getLayoutParams().height = supportSoftInputHeight;
                            fl_emogi.requestLayout();
                            fl_emogi.setVisibility(View.GONE);
                            rl_act_bbs.getViewTreeObserver().addOnGlobalLayoutListener(this);
                        }
                    } else {
                        if (!isEmogiShow) {
                            hideExpress();
                        }
                    }
                }
            }
        });
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }

    private void initRec() {
        GridLayoutManager manager = new GridLayoutManager(this, 1);
        rec_bbs_detail.setLayoutManager(manager);
        mDataAdapter = new BBSCommentAdapter(this);
        LRecyclerViewAdapter mLRecyclerViewAdapter = new LRecyclerViewAdapter(mDataAdapter);
        v = LayoutInflater.from(this).inflate(R.layout.layout_bbs_detail_header, null);
        mLRecyclerViewAdapter.addHeaderView(v);
        rec_bbs_detail.setAdapter(mLRecyclerViewAdapter);
        rec_bbs_detail.setHeaderViewColor(R.color.theme_green, R.color.darkgrey, android.R.color.white);
        rec_bbs_detail.setFooterViewColor(R.color.theme_green, R.color.darkgrey, android.R.color.white);
        rec_bbs_detail.setFooterViewHint("拼命加载中", "没有更多评论了", "网络不给力啊，点击再试一次吧");
        int spacing = getResources().getDimensionPixelSize(R.dimen.dp_066);
        rec_bbs_detail.addItemDecoration(SpacesItemDecoration.newInstance(spacing, spacing, manager.getSpanCount(), Color.parseColor("#D3D3D3")));
        rec_bbs_detail.setHasFixedSize(true);
        rec_bbs_detail.setOverScrollMode(View.OVER_SCROLL_NEVER);
        rec_bbs_detail.setNestedScrollingEnabled(false);
        rec_bbs_detail.setPullRefreshEnabled(false);
        rec_bbs_detail.setOnLoadMoreListener(() -> getComment(PAGE_INDEX++));
        mLRecyclerViewAdapter.setOnItemClickListener((view, position) -> {
            hideKeyboard(BBSDetailActivity.this);
            if (isEmogiShow) {
                hideExpress();
                isEmogiShow = false;
            }
        });

    }

    private void getBBSData() {
        TRequest<ReqNid> request = new TRequest<>();
        ReqNid nid = new ReqNid();
        nid.setNid(getIntent().getStringExtra("nid"));
        if (SNApplication.userInfo.getUserInfo() != null) {
            nid.setUid(SNApplication.userInfo.getUserInfo().getUid());
        } else {
            nid.setUid(0);
        }
        request.setParam(nid, "1127", "1");
        sendRequest(RetrofitClient.getInstance().getService(HttpService.class).BBSDetail(request), tRespon_bbsDetail -> {
            ((TextView) v.findViewById(R.id.tv_bbs_name)).setText(tRespon_bbsDetail.getCircleNote().getUserName());
            ((TextView) v.findViewById(R.id.tv_bbs_time)).setText(tRespon_bbsDetail.getCircleNote().getCreateTime());
            ((TextView) v.findViewById(R.id.tv_bbs_title)).setText(tRespon_bbsDetail.getCircleNote().getTitle());
            ((TextView) v.findViewById(R.id.tv_bbs_txt)).setText(tRespon_bbsDetail.getCircleNote().getTxt());
            v.findViewById(R.id.tv_bbs_txt).setOnClickListener(this);
            ((TextView) v.findViewById(R.id.tv_bbs_good)).setText(tRespon_bbsDetail.getCircleNote().getGood() + "");
            ((TextView) v.findViewById(R.id.tv_bbs_bad)).setText(tRespon_bbsDetail.getCircleNote().getBad() + "");
            v.findViewById(R.id.ll_bbs_detail).setOnClickListener(v -> {
                Intent intent = new Intent(BBSDetailActivity.this, UserDisplayActivity.class);
                intent.putExtra("uid", tRespon_bbsDetail.getCircleNote().getUserId() + "");
                intent.putExtra("uName", tRespon_bbsDetail.getCircleNote().getUserName());
                intent.putExtra("uImage", tRespon_bbsDetail.getCircleNote().getHeadImage());
                startActivity(intent);
            });
            GlideCacheUtil.LoadImage(BBSDetailActivity.this, v.findViewById(R.id.civ_bbs_head), tRespon_bbsDetail.getCircleNote().getHeadImage(), true);
            for (int i = 0; i < tRespon_bbsDetail.getCircleNote().getList().size(); i++) {
                if (i == 0) {
                    displayImage(v.findViewById(R.id.iv_bbs_image1), tRespon_bbsDetail.getCircleNote().getList().get(i).getImageUrl());
                } else if (i == 1) {
                    displayImage(v.findViewById(R.id.iv_bbs_image2), tRespon_bbsDetail.getCircleNote().getList().get(i).getImageUrl());
                } else if (i == 2) {
                    displayImage(v.findViewById(R.id.iv_bbs_image3), tRespon_bbsDetail.getCircleNote().getList().get(i).getImageUrl());
                }
            }
        }, throwable -> ToastUtils.showToast(throwable.getMessage()));
    }

    private void displayImage(ImageView iv, String url) {
        iv.setVisibility(View.VISIBLE);
        iv.setOnClickListener(this);
        GlideCacheUtil.LoadImage(BBSDetailActivity.this, iv, url, false);
    }

    @Override
    public void onBackPressed() {
        if (isEmogiShow) {
            hideExpress();
            hideKeyboard(this);
            isEmogiShow = false;
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_emogi: {
                if (isEmogiShow) {
                    showKeyboard(this, iv_emogi);
                    hideExpress();
                    isEmogiShow = false;
                } else {
                    hideKeyboard(this);
                    new Handler().postDelayed(() -> replaceEmogi(), 200);
                }
                break;
            }
            case R.id.tv_bbs_good: {
                if (AppUtils.isLogin(BBSDetailActivity.this)) {
                    doSuggest("1");
                }
                break;
            }
            case R.id.tv_bbs_bad: {
                if (AppUtils.isLogin(BBSDetailActivity.this)) {
                    doSuggest("0");
                }
                break;
            }
            case R.id.tv_comment: {
                if (AppUtils.isLogin(BBSDetailActivity.this)) {
                    if (((EditText) findViewById(R.id.et_comment)).getText().toString().trim().equals("")) {
                        ToastUtils.showToast("评论不能为空！");
                        return;
                    } else {
                        TRequest<ReqBBSComment> request = new TRequest<>();
                        ReqBBSComment comment = new ReqBBSComment();
                        comment.setCid(getIntent().getStringExtra("cid"));
                        comment.setNid(getIntent().getStringExtra("nid"));
                        comment.setInputTxt(((EditText) findViewById(R.id.et_comment)).getText().toString());
                        comment.setUserId(SNApplication.userInfo.getUserInfo().getUid() + "");
                        comment.setNtitle(((TextView) this.v.findViewById(R.id.tv_bbs_title)).getText().toString());
                        request.setParam(comment, "1129", "1");
                        v.setOnClickListener(null);
                        sendRequest(RetrofitClient.getInstance().getService(HttpService.class).BBSDoComment(request), new Consumer<TResponse>() {
                            @Override
                            public void accept(TResponse tResponse) throws Exception {
                                ToastUtils.showToast("评论成功！");
                                v.setOnClickListener(BBSDetailActivity.this);
                                hideExpress();
                                hideKeyboard(BBSDetailActivity.this);
                                PAGE_INDEX = 1;
                                mDataAdapter.clear();
                                getComment(PAGE_INDEX++);
                                ((EditText) findViewById(R.id.et_comment)).setText("");
                            }
                        }, throwable -> ToastUtils.showToast(throwable.getMessage()));
                    }
                }
            }
            default: {
                if (isEmogiShow) {
                    hideExpress();
                    hideKeyboard(this);
                    isEmogiShow = false;
                }
                break;
            }
        }
    }

    private void doSuggest(String type) {
        TRequest<ReqNidType> request = new TRequest<>();
        ReqNidType nidType = new ReqNidType();
        nidType.setNid(getIntent().getStringExtra("nid"));
        nidType.setType(type);
        request.setParam(nidType, "1133", "1");
        sendRequest(RetrofitClient.getInstance().getService(HttpService.class).BBSSuggest(request)
                , tResponse -> {
                    GoodView goodView = new GoodView(BBSDetailActivity.this);
                    goodView.setTextInfo("+1", Color.parseColor("#ff0bb731"), 14);
                    if (type.equals("1")) {
                        ((TextView) findViewById(R.id.tv_bbs_good)).setText(Integer.parseInt(((TextView) findViewById(R.id.tv_bbs_good)).getText().toString()) + 1 + "");
                        goodView.show(findViewById(R.id.tv_bbs_good));
                        ((TextView) findViewById(R.id.tv_bbs_good)).setTextColor(Color.parseColor("#ff0bb731"));
                        Drawable drawable = getResources().getDrawable(R.mipmap.icon_xqy_zan2);
                        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                        ((TextView) findViewById(R.id.tv_bbs_good)).setCompoundDrawables(drawable, null, null, null);
                    } else {
                        ((TextView) findViewById(R.id.tv_bbs_bad)).setText(Integer.parseInt(((TextView) findViewById(R.id.tv_bbs_bad)).getText().toString()) + 1 + "");
                        goodView.show(findViewById(R.id.tv_bbs_bad));
                        ((TextView) findViewById(R.id.tv_bbs_bad)).setTextColor(Color.parseColor("#ff0bb731"));
                        Drawable drawable = getResources().getDrawable(R.mipmap.icon_xqy_cai2);
                        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                        ((TextView) findViewById(R.id.tv_bbs_bad)).setCompoundDrawables(drawable, null, null, null);
                    }
                    findViewById(R.id.tv_bbs_good).setOnClickListener(null);
                    findViewById(R.id.tv_bbs_bad).setOnClickListener(null);
                }, throwable -> ToastUtils.showToast(throwable.getMessage()));
    }

    private void replaceEmogi() {
        isEmogiShow = true;
        showExpress();
        if (expressionShowFragment == null) {
            expressionShowFragment = ExpressionShowFragment.newInstance();
            getSupportFragmentManager().beginTransaction().replace(R.id.fl_emogi, ExpressionShowFragment.newInstance()).commit();
        }
    }

    private void showExpress() {
        fl_emogi.setVisibility(View.VISIBLE);
        iv_emogi.setImageResource(R.mipmap.fabu_keyboard_icon);
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
                    ll_bottom.clearAnimation();
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        }
        ll_bottom.startAnimation(showAnimation);
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


    @Override
    public void expressionDeleteClick(View v) {
        ExpressionShowFragment.delete(et_comment);
    }

    /**
     * 这里必须实现表情点击后才能把具体表情传入edittext
     *
     * @param str
     */
    @Override
    public void expressionClick(String str) {
        ExpressionShowFragment.input(et_comment, str);
    }

}
