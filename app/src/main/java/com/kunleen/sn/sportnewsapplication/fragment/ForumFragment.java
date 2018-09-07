package com.kunleen.sn.sportnewsapplication.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.ScrollView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.kunleen.sn.sportnewsapplication.R;
import com.kunleen.sn.sportnewsapplication.activity.HomeActivity;
import com.kunleen.sn.sportnewsapplication.activity.SearchActivity;
import com.kunleen.sn.sportnewsapplication.adapter.ForumPageAdapter;
import com.kunleen.sn.sportnewsapplication.app.SNApplication;
import com.kunleen.sn.sportnewsapplication.base.BaseFragment;
import com.kunleen.sn.sportnewsapplication.custom.CircleImageView;
import com.kunleen.sn.sportnewsapplication.custom.TitleBarView;
import com.kunleen.sn.sportnewsapplication.network.RetrofitClient;
import com.kunleen.sn.sportnewsapplication.network.bean.ReqMyTopic;
import com.kunleen.sn.sportnewsapplication.network.bean.ReqUid;
import com.kunleen.sn.sportnewsapplication.network.bean.Response_ForumItem;
import com.kunleen.sn.sportnewsapplication.network.bean.TRequest;
import com.kunleen.sn.sportnewsapplication.network.bean.TResponse_ForumPage;
import com.kunleen.sn.sportnewsapplication.network.service.HttpService;
import com.kunleen.sn.sportnewsapplication.utils.DevicesUtils;
import com.kunleen.sn.sportnewsapplication.utils.ToastUtils;
import com.orhanobut.logger.Logger;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;


public class ForumFragment extends BaseFragment implements View.OnClickListener {
    public static final String TAG = "ForumFragment_log";
    private ForumPageAdapter adapter;
    private RadioGroup rg_forum;
    TResponse_ForumPage.forum forum;
    private CircleImageView cir_head;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_forum, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        initMyCreate();
    }

    private void initMyCreate() {
        forum = new TResponse_ForumPage().new forum();
        TRequest<ReqMyTopic> request = new TRequest<>();
        ReqMyTopic topic = new ReqMyTopic();
        topic.setRows(10);
        topic.setPage(1);
        if (SNApplication.userInfo.getUserInfo() != null) {
            topic.setUid(SNApplication.userInfo.getUserInfo().getUid() + "");
        } else {
            topic.setUid("-1");
        }
        request.setParam(topic, "2104", "1");
        Observable<Response_ForumItem> observable = RetrofitClient.getInstance().getService(HttpService.class).MyForum(request);
        sendRequest(observable, response_forumItem -> {
                    List<TResponse_ForumPage.forumitem> rows = response_forumItem.getPageInfo().getRows();
                    if (rows.size() < 3) {
                        TResponse_ForumPage.forumitem forumitem = new TResponse_ForumPage().new forumitem();
                        forumitem.setcId(-1);
                        forumitem.setCname("创建新圈");
                        forumitem.setStatus(1);
                        rows.add(forumitem);
                    }
                    forum.setCirclelist(rows);
                    forum.setClassfyId(-1);
                    forum.setClassfyName("我的圈子");
                    initData();
                },
                throwable -> ToastUtils.showToast(throwable.getMessage()));
    }

    private void initView(View view) {
        cir_head = view.findViewById(R.id.iv_user_incon);
        ViewPager viewPager = view.findViewById(R.id.vp_forum);
        viewPager.setOffscreenPageLimit(3);
        adapter = new ForumPageAdapter(getActivity());
        viewPager.setAdapter(adapter);
        viewPager.setOverScrollMode(ScrollView.OVER_SCROLL_NEVER);
        View bar = view.findViewById(R.id.ll_forum_head);
//        bar.setPadding(0, SNApplication.statusBar, 0, 0);
        view.findViewById(R.id.btn_search).setOnClickListener(this);
        view.findViewById(R.id.rl_cir).setOnClickListener(this);
        rg_forum = view.findViewById(R.id.rg_forum);
        rg_forum.check(rg_forum.getChildAt(0).getId());
        rg_forum.setOnCheckedChangeListener((group, checkedId) -> {
            for (int i = 0; i < group.getChildCount(); i++) {
                if (group.getChildAt(i).getId() == checkedId) {
                    viewPager.setCurrentItem(i);
                }
            }
        });
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                rg_forum.check(rg_forum.getChildAt(position).getId());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void initData() {
        adapter.clear();
        TRequest<ReqUid> request = new TRequest<>();
        ReqUid reqUid = new ReqUid();
        if (SNApplication.userInfo.getUserInfo() != null) {
            reqUid.setUid(SNApplication.userInfo.getUserInfo().getUid() + "");
        } else {
            reqUid.setUid("-1");
        }
        request.setParam(reqUid, "2110", "1");
        Observable<TResponse_ForumPage> forumPage = RetrofitClient.getInstance().getService(HttpService.class).ForumPage(request);
        sendRequest(forumPage, tResponse_forumPage -> {
            Log.i(TAG, new Gson().toJson(tResponse_forumPage));
            tResponse_forumPage.getList().add(0, forum);
            adapter.setFistPageData(tResponse_forumPage);
            SNApplication.ForumRefrsh = false;
        }, throwable -> {
            Logger.t(TAG).d("error1:" + throwable.getMessage());
            ToastUtils.showToast(throwable.getMessage());
        });
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            DevicesUtils.setWindowStatusBarColor(getActivity(), R.color.white);//设置状态栏颜色
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (SNApplication.ForumRefrsh) {
            forum = new TResponse_ForumPage().new forum();
            initMyCreate();
        }
        if (SNApplication.userInfo.getUserInfo() != null) {
            Glide.with(getActivity()).load(SNApplication.userInfo.getUserInfo().getUserInfo().getHeadImage()).apply(new RequestOptions().centerCrop().placeholder(R.mipmap.img_loading1).error(R.mipmap.button_head_image)).into(cir_head);
        } else {
            cir_head.setImageResource(R.mipmap.button_head_image);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_search: {
                Intent intent = new Intent(getActivity(), SearchActivity.class);
                intent.putExtra("key", "forum");
                getActivity().startActivity(intent);
                break;
            }
            case R.id.rl_cir: {
                ((HomeActivity) getActivity()).moveToMine();
                break;
            }
        }
    }
}
