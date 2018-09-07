package com.kunleen.sn.sportnewsapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.os.Handler;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.interfaces.OnRefreshListener;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.kunleen.sn.sportnewsapplication.R;
import com.kunleen.sn.sportnewsapplication.adapter.CircleSearchAdapter;
import com.kunleen.sn.sportnewsapplication.adapter.ForumDataAdapter;
import com.kunleen.sn.sportnewsapplication.adapter.TypeDataAdapter;
import com.kunleen.sn.sportnewsapplication.base.BaseActivity;
import com.kunleen.sn.sportnewsapplication.custom.MyLRecyclerView;
import com.kunleen.sn.sportnewsapplication.custom.TitleBarView;
import com.kunleen.sn.sportnewsapplication.network.RetrofitClient;
import com.kunleen.sn.sportnewsapplication.network.bean.ReqEmpty;
import com.kunleen.sn.sportnewsapplication.network.bean.ReqKeywordPR;
import com.kunleen.sn.sportnewsapplication.network.bean.Response_ForumType;
import com.kunleen.sn.sportnewsapplication.network.bean.Response_SearchCircle;
import com.kunleen.sn.sportnewsapplication.network.bean.TRequest;
import com.kunleen.sn.sportnewsapplication.network.service.HttpService;
import com.kunleen.sn.sportnewsapplication.utils.DevicesUtils;
import com.kunleen.sn.sportnewsapplication.utils.ToastUtils;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;

public class TypeActivity extends BaseActivity {
    MyLRecyclerView rec_type;
    private TypeDataAdapter dataAdapter;
    private static final int PAGE_SIZE = 20;
    private int PAGE_INDEX = 1;
    private LRecyclerViewAdapter mLRecyclerViewAdapter;
    private CircleSearchAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_type);
        initView();
        bindActivity();
        initWindow(this);
    }

    private void initView() {
        TitleBarView titleBarView = findViewById(R.id.titlebar);
        if (getIntent().getStringExtra("type").equals("type")) {
            titleBarView.setTitle("圈子分类");
        } else {
            titleBarView.setTitle("搜索结果");
        }
        titleBarView.setActivity(this);
        rec_type = findViewById(R.id.rec_type);
        GridLayoutManager manager = new GridLayoutManager(this, 4);
        rec_type.setLayoutManager(manager);
        rec_type.setNestedScrollingEnabled(false);
        rec_type.setOverScrollMode(View.OVER_SCROLL_NEVER);
        if (getIntent().getStringExtra("type").equals("type")) {
            dataAdapter = new TypeDataAdapter(this);
            mLRecyclerViewAdapter = new LRecyclerViewAdapter(dataAdapter);
        } else {
            adapter = new CircleSearchAdapter(this);
            mLRecyclerViewAdapter = new LRecyclerViewAdapter(adapter);
        }
//        rec_type.setEmptyView(LayoutInflater.from(this).inflate(R.layout.layout_empty, null));
        rec_type.setAdapter(mLRecyclerViewAdapter);
        rec_type.setHeaderViewColor(R.color.theme_green, R.color.darkgrey, android.R.color.white);
        rec_type.setFooterViewColor(R.color.theme_green, R.color.darkgrey, android.R.color.white);
        rec_type.setPullRefreshEnabled(false);
        rec_type.setFooterViewHint("拼命加载中", "没有更多圈子了", "网络不给力啊，点击再试一次吧");
        if (getIntent().getStringExtra("type").equals("type")) {
            initData();
            rec_type.setLoadMoreEnabled(false);
            mLRecyclerViewAdapter.setOnItemClickListener((view, position) -> {
                Intent intent = new Intent();
                intent.putExtra("cid", dataAdapter.getDataList().get(position).getCid());
                intent.putExtra("cname", dataAdapter.getDataList().get(position).getCname());
                setResult(RESULT_OK, intent);
                finish();
            });
        } else {
            getSearchData(PAGE_INDEX++);
            mLRecyclerViewAdapter.setOnItemClickListener((view, position) -> {
                if (DevicesUtils.isNetworkAvailable(this)) {
                    Intent intent = new Intent(TypeActivity.this, ForumListActivity.class);
                    intent.putExtra("cid", adapter.getDataList().get(position).getcId() + "");
                    intent.putExtra("classfyId", adapter.getDataList().get(position).getClassfyId() + "");
                    startActivity(intent);
                } else {
                    ToastUtils.showToast("网络状况不佳，请连接网络后再试！");
                }
            });
            rec_type.setOnLoadMoreListener(() -> getSearchData(PAGE_INDEX++));
        }
    }

    private void initData() {
        TRequest<ReqEmpty> request = new TRequest<>();
        request.setParam(new ReqEmpty(), "1130", "1");
        Observable<Response_ForumType> observable = RetrofitClient.getInstance().getService(HttpService.class).ForumType(request);
        sendRequest(observable, response_forumType -> dataAdapter.addAll(response_forumType.getList()), throwable -> ToastUtils.showToast(throwable.getMessage()));
    }

    private void getSearchData(int page) {
        TRequest<ReqKeywordPR> request = new TRequest<>();
        ReqKeywordPR keywordPR = new ReqKeywordPR();
        keywordPR.setKeyword(getIntent().getStringExtra("keyword"));
        keywordPR.setPage(page);
        keywordPR.setRows(PAGE_SIZE);
        request.setParam(keywordPR, "2105", "1");
        sendRequest(RetrofitClient.getInstance().getService(HttpService.class).Circle_Search(request), new Consumer<Response_SearchCircle>() {
            @Override
            public void accept(Response_SearchCircle response_searchCircle) throws Exception {
                adapter.addAll(response_searchCircle.getPageInfo().getRows());
                rec_type.refreshComplete(response_searchCircle.getPageInfo().getRows().size());
                if (page == 1 && response_searchCircle.getPageInfo().getRows().size() == 0) {
                    rec_type.setVisibility(View.GONE);
                    findViewById(R.id.iv_empty).setVisibility(View.VISIBLE);
                    rec_type.setLoadMoreEnabled(false);
                }
                if (response_searchCircle.getPageInfo().getRows().size() < PAGE_SIZE) {
                    rec_type.setNoMore(true);
                    new Handler().postDelayed(() -> rec_type.hideFooterView(false), 500);
                }
            }
        }, throwable -> ToastUtils.showToast(throwable.getMessage()));
    }
}
