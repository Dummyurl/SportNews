package com.kunleen.sn.sportnewsapplication.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.jdsjlzx.ItemDecoration.SpacesItemDecoration;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.kunleen.sn.sportnewsapplication.R;
import com.kunleen.sn.sportnewsapplication.adapter.ForumListAdapter;
import com.kunleen.sn.sportnewsapplication.app.SNApplication;
import com.kunleen.sn.sportnewsapplication.base.BaseActivity;
import com.kunleen.sn.sportnewsapplication.custom.MyLRecyclerView;
import com.kunleen.sn.sportnewsapplication.network.RetrofitClient;
import com.kunleen.sn.sportnewsapplication.network.bean.ReqCidPageRows;
import com.kunleen.sn.sportnewsapplication.network.bean.ReqCidUid;
import com.kunleen.sn.sportnewsapplication.network.bean.ReqUidCidClassfyId;
import com.kunleen.sn.sportnewsapplication.network.bean.Response_ForumList;
import com.kunleen.sn.sportnewsapplication.network.bean.Response_ForumTop;
import com.kunleen.sn.sportnewsapplication.network.bean.TRequest;
import com.kunleen.sn.sportnewsapplication.network.service.HttpService;
import com.kunleen.sn.sportnewsapplication.utils.AppUtils;
import com.kunleen.sn.sportnewsapplication.utils.GlideCacheUtil;
import com.kunleen.sn.sportnewsapplication.utils.ToastUtils;
import com.kunleen.sn.sportnewsapplication.utils.Utils;

import io.reactivex.Observable;

public class ForumListActivity extends BaseActivity implements View.OnClickListener {
    MyLRecyclerView rec_forum_list;
    private View v;
    private static final int PAGE_SIZE = 20;
    private int PAGE_INDEX = 1;
    private ForumListAdapter mDataAdapter;
    private boolean canFollow = true;
    private String cName, icon_url, background_url, description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum_list);
        initView();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        Utils.setStatusTextColor(false, this);
        bindActivity();
        initTopData();
        initListData(1);
    }

    private void initListData(int page_index) {
        TRequest<ReqCidPageRows> request_list = new TRequest<>();
        ReqCidPageRows reqCidPageRows = new ReqCidPageRows();
        reqCidPageRows.setCid(getIntent().getStringExtra("cid"));
        reqCidPageRows.setPage(page_index);
        reqCidPageRows.setRows(PAGE_SIZE);
        if (SNApplication.userInfo.getUserInfo() != null) {
            reqCidPageRows.setUid(SNApplication.userInfo.getUserInfo().getUid());
        } else {
            reqCidPageRows.setUid(0);
        }
        request_list.setParam(reqCidPageRows, "1131", "1");
        Observable<Response_ForumList> observable = RetrofitClient.getInstance().getService(HttpService.class).ForumList(request_list);
        sendRequest(observable, response_forumList -> {
            mDataAdapter.addAll(response_forumList.getPageInfo().getRows());
            rec_forum_list.refreshComplete(response_forumList.getPageInfo().getRows().size());
            if (response_forumList.getPageInfo().getRows().size() < PAGE_SIZE) {
                rec_forum_list.setNoMore(true);
                new Handler().postDelayed(() -> rec_forum_list.hideFooterView(false), 500);
            }
        }, throwable -> ToastUtils.showToast(throwable.getMessage()));
    }

    private void initTopData() {
        TRequest<ReqCidUid> request = new TRequest<>();
        ReqCidUid reqCidUid = new ReqCidUid();
        reqCidUid.setCid(getIntent().getStringExtra("cid"));
        if (SNApplication.userInfo.getUserInfo() != null) {
            reqCidUid.setUid(SNApplication.userInfo.getUserInfo().getUid() + "");
        } else {
            reqCidUid.setUid("-1");
        }
        request.setParam(reqCidUid, "1126", "1");
        Observable<Response_ForumTop> observable = RetrofitClient.getInstance().getService(HttpService.class).ForumListTop(request);
        sendRequest(observable, response_forumTop -> {
            ((TextView) v.findViewById(R.id.tv_forum_des)).setText("简介：" + response_forumTop.getCircleAndNoteVo().getDescrip());
            ((TextView) v.findViewById(R.id.tv_forum_fans)).setText("关注：" + response_forumTop.getCircleAndNoteVo().getTheNumber());
            ((TextView) v.findViewById(R.id.tv_forum_count)).setText("帖子：" + response_forumTop.getCircleAndNoteVo().getPostNumber());
            ((TextView) v.findViewById(R.id.tv_forum_reply)).setText("回复：" + response_forumTop.getCircleAndNoteVo().getReplyNumber());
            cName = response_forumTop.getCircleAndNoteVo().getcName();
            icon_url = response_forumTop.getCircleAndNoteVo().getLittleImgUrl();
            background_url = response_forumTop.getCircleAndNoteVo().getImgUrl();
            description = response_forumTop.getCircleAndNoteVo().getDescrip();
            ((TextView) findViewById(R.id.tv_forum_name)).setText(cName);
            findViewById(R.id.iv_forum_follow).setOnClickListener(ForumListActivity.this);
            findViewById(R.id.iv_forum_follow).setTag(response_forumTop.getCircleAndNoteVo().getFollow());
            switch (response_forumTop.getCircleAndNoteVo().getFollow()) {
                case 0: {
                    ((ImageView) findViewById(R.id.iv_forum_follow)).setImageResource(R.mipmap.button_zjm_guanzhu);
                    break;
                }
                case 1: {
                    ((ImageView) findViewById(R.id.iv_forum_follow)).setImageResource(R.mipmap.button_zjm_yiguanzhu);
                    break;
                }
                case 2: {
                    ((ImageView) findViewById(R.id.iv_forum_follow)).setImageResource(R.mipmap.button_zjm_guanli);
                    break;
                }
            }
            GlideCacheUtil.LoadImage(ForumListActivity.this, v.findViewById(R.id.iv_forum_top), response_forumTop.getCircleAndNoteVo().getImgUrl(), false);
        }, throwable -> ToastUtils.showToast(throwable.getMessage()));
    }

    private void initView() {
        findViewById(R.id.titlebar).setPadding(0, SNApplication.statusBar, 0, 0);
        findViewById(R.id.iv_new_forum).setOnClickListener(this);
        rec_forum_list = findViewById(R.id.rec_forum_list);
        initRec();

    }

    private void initRec() {
        GridLayoutManager manager = new GridLayoutManager(this, 1);
        rec_forum_list.setLayoutManager(manager);
        mDataAdapter = new ForumListAdapter(this);
        LRecyclerViewAdapter mLRecyclerViewAdapter = new LRecyclerViewAdapter(mDataAdapter);
        v = LayoutInflater.from(this).inflate(R.layout.layout_forum_list_header, null);
        mLRecyclerViewAdapter.addHeaderView(v);
        rec_forum_list.setAdapter(mLRecyclerViewAdapter);
        //设置头部加载颜色
        rec_forum_list.setHeaderViewColor(R.color.theme_green, R.color.darkgrey, android.R.color.white);
        //设置底部加载颜色
        rec_forum_list.setFooterViewColor(R.color.theme_green, R.color.darkgrey, android.R.color.white);
        //设置底部加载文字提示
        rec_forum_list.setFooterViewHint("拼命加载中", "没有更多帖子了", "网络不给力啊，点击再试一次吧");
        int spacing = getResources().getDimensionPixelSize(R.dimen.dp_066);
        rec_forum_list.addItemDecoration(SpacesItemDecoration.newInstance(spacing, spacing, manager.getSpanCount(), Color.parseColor("#D3D3D3")));
        rec_forum_list.setHasFixedSize(true);
        rec_forum_list.setNestedScrollingEnabled(false);
        rec_forum_list.setOnRefreshListener(() -> {
            mDataAdapter.clear();
            PAGE_INDEX = 1;
            initListData(PAGE_INDEX++);
        });
        rec_forum_list.setOnLoadMoreListener(() -> initListData(PAGE_INDEX++));
        mLRecyclerViewAdapter.setOnItemClickListener((view, position) -> {
            Intent intent = new Intent(ForumListActivity.this, BBSDetailActivity.class);
            intent.putExtra("nid", mDataAdapter.getDataList().get(position).getNid() + "");
            intent.putExtra("cid", getIntent().getStringExtra("cid"));
            startActivity(intent);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            rec_forum_list.forceToRefresh();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_new_forum: {
                Intent intent = new Intent(ForumListActivity.this, CreateBBSActivity.class);
                intent.putExtra("cid", getIntent().getStringExtra("cid"));
                startActivityForResult(intent, 100);
                break;
            }
            case R.id.iv_forum_follow: {
                if (AppUtils.isLogin(ForumListActivity.this) && canFollow) {
                    //0，未关注，1已关注，2管理
                    switch ((int) findViewById(R.id.iv_forum_follow).getTag()) {
                        case 0: {
                            TRequest<ReqUidCidClassfyId> request = new TRequest<>();
                            ReqUidCidClassfyId param = new ReqUidCidClassfyId();
                            param.setCid(Integer.parseInt(getIntent().getStringExtra("cid")));
                            param.setUserId(SNApplication.userInfo.getUserInfo().getUid() + "");
                            param.setClassfyId(getIntent().getStringExtra("classfyId"));
                            request.setParam(param, "2107", "1");
                            sendRequest(RetrofitClient.getInstance().getService(HttpService.class).FollowForum(request), tResponse -> {
                                canFollow = true;
                                ((ImageView) findViewById(R.id.iv_forum_follow)).setImageResource(R.mipmap.button_zjm_yiguanzhu);
                                findViewById(R.id.iv_forum_follow).setTag(1);
                                SNApplication.ForumRefrsh = true;
                            }, throwable -> {
                                canFollow = true;
                                ToastUtils.showToast(throwable.getMessage());
                            });
                            canFollow = false;
                            break;
                        }
                        case 1: {
                            canFollow = false;
                            TRequest<ReqCidUid> request = new TRequest<>();
                            ReqCidUid param = new ReqCidUid();
                            param.setCid(getIntent().getStringExtra("cid"));
                            param.setUid(SNApplication.userInfo.getUserInfo().getUid() + "");
                            request.setParam(param, "2108", "1");
                            sendRequest(RetrofitClient.getInstance().getService(HttpService.class).UnFollowForum(request), tResponse -> {
                                canFollow = true;
                                SNApplication.ForumRefrsh = true;
                                ((ImageView) findViewById(R.id.iv_forum_follow)).setImageResource(R.mipmap.button_zjm_guanzhu);
                                findViewById(R.id.iv_forum_follow).setTag(0);
                            }, throwable -> {
                                canFollow = true;
                                ToastUtils.showToast(throwable.getMessage());
                            });
                            break;
                        }
                        case 2: {
                            Intent intent = new Intent(ForumListActivity.this, NewForumActivity.class);
                            intent.putExtra("isChange", true);
                            intent.putExtra("cid", getIntent().getStringExtra("cid"));
                            intent.putExtra("cname", cName);
                            intent.putExtra("icon_url", icon_url);
                            intent.putExtra("background_url", background_url);
                            intent.putExtra("description", description);
                            startActivity(intent);
                        }
                    }
                }
            }
        }
    }
}
