package com.kunleen.sn.sportnewsapplication.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.GridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.github.jdsjlzx.ItemDecoration.SpacesItemDecoration;
import com.github.jdsjlzx.interfaces.OnItemClickListener;
import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.google.gson.Gson;
import com.kunleen.sn.sportnewsapplication.R;
import com.kunleen.sn.sportnewsapplication.activity.BBSDetailActivity;
import com.kunleen.sn.sportnewsapplication.app.SNApplication;
import com.kunleen.sn.sportnewsapplication.base.BaseActivity;
import com.kunleen.sn.sportnewsapplication.custom.MyLRecyclerView;
import com.kunleen.sn.sportnewsapplication.network.RetrofitClient;
import com.kunleen.sn.sportnewsapplication.network.bean.ReqMyTopic;
import com.kunleen.sn.sportnewsapplication.network.bean.TRequest;
import com.kunleen.sn.sportnewsapplication.network.bean.TResponse_ForumPage;
import com.kunleen.sn.sportnewsapplication.network.bean.TResponse_MyReply;
import com.kunleen.sn.sportnewsapplication.network.bean.TResponse_MyTopic;
import com.kunleen.sn.sportnewsapplication.network.service.HttpService;
import com.kunleen.sn.sportnewsapplication.utils.DevicesUtils;
import com.kunleen.sn.sportnewsapplication.utils.ToastUtils;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;

/**
 * Created by ysy on 2018/3/14.
 */

public class ForumPageAdapter extends PagerAdapter {
    public static final String TAG = "ForumPageAdapter_log";
    Activity mContext;
    private MyListviewAdapter adapter;
    private int PAGE_SIZE = 10;
    private int PAGE_INDEX_TOPIC = 1;
    private int PAGE_INDEX_REPLY = 1;

    public ForumPageAdapter(Activity context) {
        mContext = context;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View v;
        if (position == 0) {
            ListView listView = (ListView) LayoutInflater.from(mContext).inflate(R.layout.listview_forum, container, false);
            v = listView;
            initListView(listView);
            container.addView(listView);
        } else if (position == 1) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.myrec_layout, container, false);
            MyLRecyclerView mRecyclerView = view.findViewById(R.id.rec_first);
            v = view;
            initRecView(mRecyclerView);
            container.addView(view);
        } else {
            View view = LayoutInflater.from(mContext).inflate(R.layout.myrec_layout, container, false);
            MyLRecyclerView mRecyclerView = view.findViewById(R.id.rec_first);
            v = view;
            initRplyRecView(mRecyclerView);
            container.addView(view);
        }
        return v;
    }

    private void initRplyRecView(final MyLRecyclerView mRecyclerView) {
        FullyLinearLayoutManager manager = new FullyLinearLayoutManager(mContext);
        mRecyclerView.setLayoutManager(manager);
        final MyReplyAdapter mDataAdapter = new MyReplyAdapter(mContext);
        LRecyclerViewAdapter mLRecyclerViewAdapter = new LRecyclerViewAdapter(mDataAdapter);
        mRecyclerView.setAdapter(mLRecyclerViewAdapter);
        mLRecyclerViewAdapter.setOnItemClickListener((view, position) -> {
            Intent intent = new Intent(mContext, BBSDetailActivity.class);
            intent.putExtra("nid", mDataAdapter.getDataList().get(position).getNid() + "");
            intent.putExtra("cid", mDataAdapter.getDataList().get(position).getCid() + "");
            mContext.startActivity(intent);
        });
        //设置头部加载颜色
        mRecyclerView.setHeaderViewColor(R.color.theme_green, R.color.darkgrey, android.R.color.white);
        //设置底部加载颜色
        mRecyclerView.setFooterViewColor(R.color.theme_green, R.color.darkgrey, android.R.color.white);
        //设置底部加载文字提示
        mRecyclerView.setFooterViewHint("玩命加载中", "没有更多回复了！", "网络不给力啊，点击再试一次吧");
        int spacing = mContext.getResources().getDimensionPixelSize(R.dimen.dp_066);
//        mRecyclerView.addItemDecoration(SpacesItemDecoration.newInstance(spacing, spacing, manager.getSpanCount(), Color.parseColor("#D3D3D3")));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setNestedScrollingEnabled(false);
        getReplyData(mRecyclerView, mDataAdapter);
        mRecyclerView.setOnRefreshListener(() -> {
            PAGE_INDEX_REPLY = 1;
            mRecyclerView.hideFooterView(true);
            mDataAdapter.clear();
            getReplyData(mRecyclerView, mDataAdapter);
        });
        mRecyclerView.setOnLoadMoreListener(() -> getReplyData(mRecyclerView, mDataAdapter));
    }

    private void getReplyData(MyLRecyclerView mRecyclerView, MyReplyAdapter mDataAdapter) {
        if (DevicesUtils.isNetworkAvailable(mContext)) {
            TRequest<ReqMyTopic> req = new TRequest<>();
            ReqMyTopic topic = new ReqMyTopic();
            topic.setPage(PAGE_INDEX_REPLY++);
            topic.setRows(PAGE_SIZE);
            if (SNApplication.userInfo.getUserInfo() == null) {
                topic.setUid("-1");
                topic.setCuid("-1");
            } else {
                topic.setUid(SNApplication.userInfo.getUserInfo().getUid() + "");
                topic.setCuid(SNApplication.userInfo.getUserInfo().getUid() + "");
            }
            req.setParam(topic, "2103", "1");
            Observable<TResponse_MyReply> topicObservable = RetrofitClient.getInstance().getService(HttpService.class).MyReply(req);
            ((BaseActivity) mContext).sendRequest(topicObservable, tResponse_myReply -> {
                Log.i(TAG, new Gson().toJson(tResponse_myReply));
                mDataAdapter.addAll(tResponse_myReply.getPageInfo().getRows());
                mRecyclerView.refreshComplete(tResponse_myReply.getPageInfo().getRows().size());
                if (tResponse_myReply.getPageInfo().getRows().size() < PAGE_SIZE) {
                    mRecyclerView.setNoMore(true);
                    new Handler(Looper.getMainLooper()).postDelayed(() -> mRecyclerView.hideFooterView(false), 500);
                }
            }, throwable -> {
                ToastUtils.showToast(throwable.getMessage());
                mRecyclerView.refreshComplete(0);
            });
        }
        mRecyclerView.refreshComplete(PAGE_SIZE);
    }

    private void initRecView(final MyLRecyclerView mRecyclerView) {
        GridLayoutManager manager = new GridLayoutManager(mContext, 1);
        mRecyclerView.setLayoutManager(manager);
        final MyTopicAdapter mDataAdapter = new MyTopicAdapter(mContext);
        LRecyclerViewAdapter mLRecyclerViewAdapter = new LRecyclerViewAdapter(mDataAdapter);
        mLRecyclerViewAdapter.setOnItemClickListener((view, position12) -> {
            Intent intent = new Intent(mContext, BBSDetailActivity.class);
            intent.putExtra("nid", mDataAdapter.getDataList().get(position12).getNid() + "");
            intent.putExtra("cid", mDataAdapter.getDataList().get(position12).getCid() + "");
            mContext.startActivity(intent);
        });
        mRecyclerView.setAdapter(mLRecyclerViewAdapter);
        //设置头部加载颜色
        mRecyclerView.setHeaderViewColor(R.color.theme_green, R.color.darkgrey, android.R.color.white);
        //设置底部加载颜色
        mRecyclerView.setFooterViewColor(R.color.theme_green, R.color.darkgrey, android.R.color.white);
        //设置底部加载文字提示
        mRecyclerView.setFooterViewHint("玩命加载中", "没有更多话题了！", "网络不给力啊，点击再试一次吧");
        mRecyclerView.setLoadMoreEnabled(true);
        int spacing = mContext.getResources().getDimensionPixelSize(R.dimen.dp_066);
        mRecyclerView.addItemDecoration(SpacesItemDecoration.newInstance(spacing, spacing, manager.getSpanCount(), Color.parseColor("#D3D3D3")));
        mRecyclerView.setHasFixedSize(true);
        getData(mRecyclerView, mDataAdapter);
        mRecyclerView.setOnRefreshListener(() -> {
            mRecyclerView.hideFooterView(true);
            PAGE_INDEX_TOPIC = 1;
            mDataAdapter.clear();
            getData(mRecyclerView, mDataAdapter);
        });
        mRecyclerView.setOnLoadMoreListener(() -> getData(mRecyclerView, mDataAdapter));
    }

    private void getData(final MyLRecyclerView mRecyclerView, final MyTopicAdapter mDataAdapter) {
        if (DevicesUtils.isNetworkAvailable(mContext)) {
            TRequest<ReqMyTopic> req = new TRequest<>();
            ReqMyTopic topic = new ReqMyTopic();
            topic.setPage(PAGE_INDEX_TOPIC++);
            topic.setRows(PAGE_SIZE);
            if (SNApplication.userInfo.getUserInfo() == null) {
                topic.setUid("-1");
                topic.setCuid("-1");
            } else {
                topic.setUid(SNApplication.userInfo.getUserInfo().getUid() + "");
                topic.setCuid(SNApplication.userInfo.getUserInfo().getUid() + "");
            }
            req.setParam(topic, "2101", "1");
            Observable<TResponse_MyTopic> topicObservable = RetrofitClient.getInstance().getService(HttpService.class).MyTopic(req);
            ((BaseActivity) mContext).sendRequest(topicObservable, tResponse_myTopic -> {
                Log.i(TAG, new Gson().toJson(tResponse_myTopic));
                mDataAdapter.addAll(tResponse_myTopic.getPageInfo().getRows());
                mRecyclerView.refreshComplete(tResponse_myTopic.getPageInfo().getRows().size());
                if (tResponse_myTopic.getPageInfo().getRows().size() < PAGE_SIZE) {
                    mRecyclerView.setNoMore(true);
                    new Handler(Looper.getMainLooper()).postDelayed(() -> mRecyclerView.hideFooterView(false), 500);

                }
                mDataAdapter.notifyDataSetChanged();
            }, throwable -> {
                ToastUtils.showToast(throwable.getMessage());
                mRecyclerView.refreshComplete(0);
            });
        }
        mRecyclerView.refreshComplete(PAGE_SIZE);
    }

    private void initListView(ListView listview) {
        adapter = new MyListviewAdapter(mContext);
        listview.setAdapter(adapter);
        listview.setOverScrollMode(android.view.View.OVER_SCROLL_NEVER);
    }

    public void setFistPageData(TResponse_ForumPage pageData) {
        List<TResponse_ForumPage.forum> list = pageData.getList();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getCirclelist() == null || list.get(i).getCirclelist().size() == 0) {
                list.remove(i);
                i--;
            }
        }
        adapter.setData(list);
    }

    public void clear() {
        adapter.clear();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
