package com.kunleen.sn.sportnewsapplication.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.util.Log;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.util.Util;
import com.dou361.ijkplayer.utils.NetworkUtils;
import com.github.jdsjlzx.ItemDecoration.SpacesItemDecoration;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.google.gson.Gson;
import com.kunleen.sn.sportnewsapplication.R;
import com.kunleen.sn.sportnewsapplication.adapter.DataAdapter;
import com.kunleen.sn.sportnewsapplication.app.SNApplication;
import com.kunleen.sn.sportnewsapplication.base.BaseActivity;
import com.kunleen.sn.sportnewsapplication.custom.TitleBarView;
import com.kunleen.sn.sportnewsapplication.network.RetrofitClient;
import com.kunleen.sn.sportnewsapplication.network.bean.ReqSearchNews;
import com.kunleen.sn.sportnewsapplication.network.bean.TRequest;
import com.kunleen.sn.sportnewsapplication.network.bean.TResponse;
import com.kunleen.sn.sportnewsapplication.network.bean.TResponse_NewsList;
import com.kunleen.sn.sportnewsapplication.network.service.HttpService;
import com.kunleen.sn.sportnewsapplication.utils.AppUtils;
import com.kunleen.sn.sportnewsapplication.utils.DevicesUtils;
import com.kunleen.sn.sportnewsapplication.utils.ToastUtils;
import com.sina.weibo.sdk.net.NetUtils;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class News_ListActivity extends BaseActivity {
    String list_name;
    LRecyclerView mRecyclerView;
    DataAdapter mDataAdapter;
    LRecyclerViewAdapter mLRecyclerViewAdapter;
    private int PAGE_INDEX = 1;
    private int PAGE_SIZE = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_list);
        DevicesUtils.setWindowStatusBarColor(this, R.color.white);//设置状态栏颜色
        Intent intent = getIntent();
        list_name = intent.getStringExtra("list_name");
        initTitle();
        initView();
        initWindow(this);
        bindActivity();
        initData();
    }

    private void initTitle() {
        TitleBarView titleBarView = findViewById(R.id.titlebar);
        titleBarView.setActivity(this);
        titleBarView.setTitle("搜索结果");
    }

    private void initData() {
        TRequest<ReqSearchNews> request = new TRequest<>();
        ReqSearchNews searchNews = new ReqSearchNews();
        searchNews.setTitle(list_name);
        searchNews.setPage(PAGE_INDEX++);
        searchNews.setRows(PAGE_SIZE);
        request.setParam(searchNews, "1119", "1");
        Observable<TResponse_NewsList> search_news = RetrofitClient.getInstance().getService(HttpService.class).SearchNews(request);
        sendRequest(search_news, tResponse_searchNews -> {
            if (tResponse_searchNews.getPageInfo().getRows().size() == 0) {
                mRecyclerView.setPullRefreshEnabled(false);
                mRecyclerView.setLoadMoreEnabled(false);
                mRecyclerView.setVisibility(View.GONE);
                findViewById(R.id.tv_empty).setVisibility(View.VISIBLE);
            } else {
                mDataAdapter.addAll(tResponse_searchNews.getPageInfo().getRows());
            }
            mRecyclerView.refreshComplete(tResponse_searchNews.getPageInfo().getRows().size());
        }, throwable -> ToastUtils.showToast(throwable.getMessage()));
    }

    private void initView() {
        mRecyclerView = findViewById(R.id.rec_goods_list);
        GridLayoutManager manager = new GridLayoutManager(this, 1);
        mRecyclerView.setLayoutManager(manager);
        mDataAdapter = new DataAdapter(this);
        mLRecyclerViewAdapter = new LRecyclerViewAdapter(mDataAdapter);
        mRecyclerView.setAdapter(mLRecyclerViewAdapter);
        //设置头部加载颜色
        mRecyclerView.setHeaderViewColor(R.color.theme_green, R.color.darkgrey, android.R.color.white);
        //设置底部加载颜色
        mRecyclerView.setFooterViewColor(R.color.theme_green, R.color.darkgrey, android.R.color.white);
        //设置底部加载文字提示
        mRecyclerView.setFooterViewHint("拼命加载中", "没有更多新闻了", "网络不给力啊，点击再试一次吧");
        int spacing = getResources().getDimensionPixelSize(R.dimen.dp_066);
        mRecyclerView.addItemDecoration(SpacesItemDecoration.newInstance(spacing, spacing, manager.getSpanCount(), Color.parseColor("#D3D3D3")));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setOnRefreshListener(() -> {
            mDataAdapter.clear();
            PAGE_INDEX = 1;
            initData();
        });
        mRecyclerView.setOnLoadMoreListener(() -> {
            {
                TRequest<ReqSearchNews> request = new TRequest<>();
                ReqSearchNews searchNews = new ReqSearchNews();
                searchNews.setTitle(list_name);
                searchNews.setPage(PAGE_INDEX++);
                searchNews.setRows(PAGE_SIZE);
                request.setParam(searchNews, "1119", "1");
                Observable<TResponse_NewsList> search_news = RetrofitClient.getInstance().getService(HttpService.class).SearchNews(request);
                sendRequest(search_news, tResponse_searchNews -> {
                    mDataAdapter.addAll(tResponse_searchNews.getPageInfo().getRows());
                    if (tResponse_searchNews.getPageInfo().getRows().size() < PAGE_SIZE) {
                        mRecyclerView.setNoMore(true);
                    }
                    mRecyclerView.refreshComplete(tResponse_searchNews.getPageInfo().getRows().size());
                }, throwable -> ToastUtils.showToast(throwable.getMessage()));
            }
        });
        mRecyclerView.setPullRefreshEnabled(false);
        mLRecyclerViewAdapter.setOnItemClickListener((view, position) -> {
            if (DevicesUtils.isNetworkAvailable(this)) {
                TResponse_NewsList.row row = mDataAdapter.getDataList().get(position);
                if (row.getShowType() == 7 || row.getShowType() == 8) {
                    AppUtils.OutToSysBrowser(row.getSecTitle().split(";")[0], this);
                } else if (row.getShowType() == 4 || row.getShowType() == 6) {
                    Intent intent = new Intent(this, VideoActivity.class);
                    String[] split = row.getImageUrl().split(";");
                    if (split.length > 1) {
                        intent.putExtra("title", row.getTitle());
                        intent.putExtra("titleid", row.getTitleId());
                        intent.putExtra("url", split[1]);
                        intent.putExtra("type", 2);
                        startActivity(intent);
                    } else {
                        ToastUtils.showToast("视频源有误，暂时无法观看");
                    }
                } else {
                    Intent intent = new Intent(this, WebViewActivity.class);
                    intent.putExtra("title", row.getTitle());
                    intent.putExtra("titleid", row.getTitleId());
                    intent.putExtra("type", 1);
                    startActivity(intent);
                }
            } else {
                ToastUtils.showToast("网络状况不佳，请连接网络后再试！");
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (Util.isOnMainThread() && !this.isFinishing()) {
            Glide.with(this).pauseRequests();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
