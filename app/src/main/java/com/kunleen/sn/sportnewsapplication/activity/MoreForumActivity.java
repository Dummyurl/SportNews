package com.kunleen.sn.sportnewsapplication.activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.app.Activity;
import android.os.Handler;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.github.jdsjlzx.ItemDecoration.SpacesItemDecoration;
import com.github.jdsjlzx.interfaces.OnItemClickListener;
import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.google.gson.Gson;
import com.kunleen.sn.sportnewsapplication.R;
import com.kunleen.sn.sportnewsapplication.adapter.DataAdapter;
import com.kunleen.sn.sportnewsapplication.adapter.ForumDataAdapter;
import com.kunleen.sn.sportnewsapplication.app.SNApplication;
import com.kunleen.sn.sportnewsapplication.base.BaseActivity;
import com.kunleen.sn.sportnewsapplication.custom.MyLRecyclerView;
import com.kunleen.sn.sportnewsapplication.custom.SlidingLayout;
import com.kunleen.sn.sportnewsapplication.custom.TitleBarView;
import com.kunleen.sn.sportnewsapplication.network.RetrofitClient;
import com.kunleen.sn.sportnewsapplication.network.bean.ReqForumChannel;
import com.kunleen.sn.sportnewsapplication.network.bean.ReqNewsList;
import com.kunleen.sn.sportnewsapplication.network.bean.ReqUid;
import com.kunleen.sn.sportnewsapplication.network.bean.ReqUidInt;
import com.kunleen.sn.sportnewsapplication.network.bean.Response_ForumItem;
import com.kunleen.sn.sportnewsapplication.network.bean.TRequest;
import com.kunleen.sn.sportnewsapplication.network.bean.TResponse_ForumChannel;
import com.kunleen.sn.sportnewsapplication.network.service.HttpService;
import com.kunleen.sn.sportnewsapplication.utils.AppUtils;
import com.kunleen.sn.sportnewsapplication.utils.DevicesUtils;
import com.kunleen.sn.sportnewsapplication.utils.ToastUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;

public class MoreForumActivity extends BaseActivity implements View.OnClickListener {
    public static final String TAG = "MoreForumActivity_log";
    private RadioGroup rg_forum_channel;
    MyLRecyclerView rv_forum_channel;
    private ForumDataAdapter mDataAdapter;
    private LRecyclerViewAdapter mLRecyclerViewAdapter;
    private int PAGE_INDEX = 1;
    private int mClassfy;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_forum);
        initView();
        initEvent();
        initWindow(this);
        initChannel();
        bindActivity();
    }

    private void initEvent() {
        rv_forum_channel.setOnLoadMoreListener(() -> getData(-1));
        findViewById(R.id.btn_login).setOnClickListener(this);
        findViewById(R.id.tv_forum_search).setOnClickListener(this);
        rg_forum_channel.setOnCheckedChangeListener((group, checkedId) -> {
            PAGE_INDEX = 0;
            mClassfy = 0;
            mDataAdapter.clear();
            rv_forum_channel.setNoMore(false);
            rv_forum_channel.hideFooterView(true);
            for (int i = 0; i < group.getChildCount(); i++) {
                if (group.getChildAt(i).getId() == checkedId) {
                    int classfy = (int) group.getChildAt(i).getTag();
                    if (classfy == 88 && SNApplication.userInfo.getUserInfo() == null) {
                        rv_forum_channel.setVisibility(View.GONE);
                        findViewById(R.id.ll_not_login).setVisibility(View.VISIBLE);
                        findViewById(R.id.ll_forum_empty).setVisibility(View.GONE);
                    } else {
                        rv_forum_channel.setVisibility(View.VISIBLE);
                        findViewById(R.id.ll_not_login).setVisibility(View.GONE);
                        findViewById(R.id.ll_forum_empty).setVisibility(View.GONE);
                        getData(classfy);
                    }
                }
            }
        });
    }

    private void getData(int classfy) {
        if (classfy != -1) {
            mClassfy = classfy;
        }
        TRequest<ReqNewsList> request = new TRequest<>();
        ReqNewsList newsList = new ReqNewsList();
        newsList.setRows(30);
        if (classfy == -1) {
            newsList.setClassfyId(mClassfy + "");
        } else {
            newsList.setClassfyId(classfy + "");
        }
        if (SNApplication.userInfo.getUserInfo() != null) {
            newsList.setUid(SNApplication.userInfo.getUserInfo().getUid() + "");
        } else {
            newsList.setUid(-1 + "");
        }
        newsList.setPage(PAGE_INDEX++);
        request.setParam(newsList, "2109", "1");
        Observable<Response_ForumItem> observable = RetrofitClient.getInstance().getService(HttpService.class).ForumChannelDetail(request);
        sendRequest(observable, response_forumItem -> {
            if (classfy == 88 && response_forumItem.getPageInfo().getRows().size() == 0 && PAGE_INDEX == 1) {
                rv_forum_channel.setVisibility(View.GONE);
                findViewById(R.id.ll_not_login).setVisibility(View.GONE);
                findViewById(R.id.ll_forum_empty).setVisibility(View.VISIBLE);
            }
            mDataAdapter.addAll(response_forumItem.getPageInfo().getRows());
            rv_forum_channel.refreshComplete(response_forumItem.getPageInfo().getRows().size());
            if (response_forumItem.getPageInfo().getRows().size() < 30) {
                rv_forum_channel.setNoMore(true);
                handler.postDelayed(() -> rv_forum_channel.hideFooterView(false), 500);
            }
        }, throwable -> ToastUtils.showToast(throwable.getMessage()));
    }

    private void initRec(MyLRecyclerView rv) {
        GridLayoutManager manager = new GridLayoutManager(this, 3);
        rv.setLayoutManager(manager);
        mDataAdapter = new ForumDataAdapter(this);
        mLRecyclerViewAdapter = new LRecyclerViewAdapter(mDataAdapter);
        rv.setAdapter(mLRecyclerViewAdapter);
        //设置头部加载颜色
        rv.setHeaderViewColor(R.color.theme_green, R.color.darkgrey, android.R.color.white);
        //设置底部加载颜色
        rv.setFooterViewColor(R.color.theme_green, R.color.darkgrey, android.R.color.white);
        //设置底部加载文字提示Z
        rv.setFooterViewHint("拼命加载中", "没有更多圈子了", "网络不给力啊，点击再试一次吧");
        rv.setHasFixedSize(true);
        rv.setOverScrollMode(View.OVER_SCROLL_NEVER);
        rv.setPullRefreshEnabled(false);
        mLRecyclerViewAdapter.setOnItemClickListener((view, position) -> {
            if (DevicesUtils.isNetworkAvailable(this)) {
                Intent intent = new Intent(MoreForumActivity.this, ForumListActivity.class);
                intent.putExtra("cid", mDataAdapter.getDataList().get(position).getcId() + "");
                intent.putExtra("classfyId", mDataAdapter.getDataList().get(position).getClassfyId() + "");
                startActivity(intent);
            } else {
                ToastUtils.showToast("网络状况不佳，请连接网络后再试！");
            }
        });
    }

    private void initView() {
        rg_forum_channel = findViewById(R.id.rg_forum_channel);
        rv_forum_channel = findViewById(R.id.rv_forum_channel);
        initRec(rv_forum_channel);
        TitleBarView bar = findViewById(R.id.titlebar);
        bar.setTitle("更多圈子");
        bar.setActivity(this);
    }

    private void initChannel() {
        int classy = getIntent().getIntExtra("classy", 88);
        TRequest<ReqForumChannel> request = new TRequest<>();
        ReqForumChannel uid = new ReqForumChannel();
        if (SNApplication.userInfo.getUserInfo() != null) {
            uid.setUid(SNApplication.userInfo.getUserInfo().getUid());
        } else {
            uid.setUid(-1);
        }
        uid.setClassfyId(classy);
        request.setParam(uid, "2111", "1");
        Observable<TResponse_ForumChannel> observable = RetrofitClient.getInstance().getService(HttpService.class).ForumChannel(request);
        sendRequest(observable, tResponse_forumChannel -> {
            List<TResponse_ForumChannel.list> list = tResponse_forumChannel.getList();
            for (int i = 0; i < list.size(); i++) {
                RadioButton radioButton = (RadioButton) LayoutInflater.from(MoreForumActivity.this).inflate(R.layout.radiobutton_forum_channel, rg_forum_channel, false);
                radioButton.setText(list.get(i).getClassfyName() + "(" + list.get(i).getPersonNum() + ")");
                radioButton.setId(i);
                if (classy == list.get(i).getClassfyId()) {
                    radioButton.setChecked(true);
                    getData(list.get(i).getClassfyId());
                }
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(dip2px(MoreForumActivity.this, 105), LinearLayout.LayoutParams.WRAP_CONTENT);
                radioButton.setTag(list.get(i).getClassfyId());
                rg_forum_channel.addView(radioButton, layoutParams);
                rg_forum_channel.addView(LayoutInflater.from(MoreForumActivity.this).inflate(R.layout.view_devide, rg_forum_channel, false));
            }
            Log.i(TAG, new Gson().toJson(tResponse_forumChannel));
        }, throwable -> ToastUtils.showToast(throwable.getMessage()));
    }

    public void bindActivity() {
        SlidingLayout rootView = new SlidingLayout(this);
        rootView.bindActivity(this);
    }

    private int dip2px(Context context, float dipValue) {
        Resources r = context.getResources();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dipValue, r.getDisplayMetrics());
    }

    @Override
    protected void onResume() {
        super.onResume();
        for (int i = 0; i < rg_forum_channel.getChildCount(); i++) {
            if (rg_forum_channel.getChildAt(i) instanceof RadioButton) {
                if (rg_forum_channel.getCheckedRadioButtonId() == rg_forum_channel.getChildAt(i).getId())
                    if (((RadioButton) rg_forum_channel.getChildAt(i)).getText().toString().contains("我的")) {
                        int classfy = (int) rg_forum_channel.getChildAt(i).getTag();
                        if (classfy == 88 && SNApplication.userInfo.getUserInfo() == null) {
                            rv_forum_channel.setVisibility(View.GONE);
                            findViewById(R.id.ll_not_login).setVisibility(View.VISIBLE);
                            findViewById(R.id.ll_forum_empty).setVisibility(View.GONE);
                        } else {
                            rv_forum_channel.setVisibility(View.VISIBLE);
                            findViewById(R.id.ll_not_login).setVisibility(View.GONE);
                            findViewById(R.id.ll_forum_empty).setVisibility(View.GONE);
                            getData(classfy);
                        }
                    }
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login: {
                startActivity(new Intent(this, LoginActivity.class));
                break;
            }
            case R.id.tv_forum_search: {
                Intent intent = new Intent(this, SearchActivity.class);
                intent.putExtra("key", "forum");
                startActivity(intent);
                break;
            }
        }
    }
}
