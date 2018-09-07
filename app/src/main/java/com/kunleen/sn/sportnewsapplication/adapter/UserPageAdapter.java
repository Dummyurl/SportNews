package com.kunleen.sn.sportnewsapplication.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.GridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.jdsjlzx.ItemDecoration.SpacesItemDecoration;
import com.github.jdsjlzx.interfaces.OnItemClickListener;
import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.interfaces.OnRefreshListener;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.google.gson.Gson;
import com.kunleen.sn.sportnewsapplication.R;
import com.kunleen.sn.sportnewsapplication.activity.BBSDetailActivity;
import com.kunleen.sn.sportnewsapplication.activity.UserDisplayActivity;
import com.kunleen.sn.sportnewsapplication.app.SNApplication;
import com.kunleen.sn.sportnewsapplication.base.BaseActivity;
import com.kunleen.sn.sportnewsapplication.custom.MyLRecyclerView;
import com.kunleen.sn.sportnewsapplication.network.RetrofitClient;
import com.kunleen.sn.sportnewsapplication.network.bean.ReqMyTopic;
import com.kunleen.sn.sportnewsapplication.network.bean.ReqUidTypePR;
import com.kunleen.sn.sportnewsapplication.network.bean.Response_FollowBeFollow;
import com.kunleen.sn.sportnewsapplication.network.bean.TRequest;
import com.kunleen.sn.sportnewsapplication.network.bean.TResponse_MyReply;
import com.kunleen.sn.sportnewsapplication.network.bean.TResponse_MyTopic;
import com.kunleen.sn.sportnewsapplication.network.service.HttpService;
import com.kunleen.sn.sportnewsapplication.utils.DevicesUtils;
import com.kunleen.sn.sportnewsapplication.utils.ToastUtils;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;

/**
 * Created by ysy on 2018/3/27.
 */

public class UserPageAdapter extends PagerAdapter {
    public static final String TAG = "ForumPageAdapter_log";
    Activity mContext;
    private MyListviewAdapter adapter;
    private int PAGE_SIZE = 10;
    private int PAGE_INDEX_TOPIC = 1;
    private int PAGE_INDEX_REPLY = 1;
    private int PAGE_INDEX_FOLLOW = 1;
    private int PAGE_INDEX_FANS = 1;
    private LoadReadyListener loadReadyListener;
    private String mUid;

    public UserPageAdapter(Activity context, String uid) {
        mContext = context;
        mUid = uid;
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View v;
        if (position == 0) {
            View inflate = LayoutInflater.from(mContext).inflate(R.layout.myrec_layout, container, false);
            MyLRecyclerView mRecyclerView = inflate.findViewById(R.id.rec_first);
            v = inflate;
            final MyTopicAdapter mDataAdapter = new MyTopicAdapter(mContext);
            initRecView(1, mRecyclerView, mDataAdapter, () -> {
                mRecyclerView.hideFooterView(true);
                PAGE_INDEX_TOPIC = 1;
                mDataAdapter.clear();
                getData(mRecyclerView, mDataAdapter);
            }, () -> getData(mRecyclerView, mDataAdapter), new OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    Intent intent = new Intent(mContext, BBSDetailActivity.class);
                    intent.putExtra("nid", mDataAdapter.getDataList().get(position).getNid() + "");
                    intent.putExtra("cid", mDataAdapter.getDataList().get(position).getCid() + "");
                    mContext.startActivity(intent);
                }
            });
            getData(mRecyclerView, mDataAdapter);
            container.addView(inflate);
        } else if (position == 1) {
            View inflate = LayoutInflater.from(mContext).inflate(R.layout.myrec_layout, container, false);
            MyLRecyclerView mRecyclerView = inflate.findViewById(R.id.rec_first);
            v = inflate;
            final MyReplyAdapter mDataAdapter = new MyReplyAdapter(mContext);
            initRecView(1, mRecyclerView, mDataAdapter, () -> {
                PAGE_INDEX_REPLY = 1;
                mRecyclerView.hideFooterView(true);
                mDataAdapter.clear();
                getReplyData(mRecyclerView, mDataAdapter);
            }, () -> getReplyData(mRecyclerView, mDataAdapter), new OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    Intent intent = new Intent(mContext, BBSDetailActivity.class);
                    intent.putExtra("nid", mDataAdapter.getDataList().get(position).getNid() + "");
                    intent.putExtra("cid", mDataAdapter.getDataList().get(position).getCid() + "");
                    mContext.startActivity(intent);
                }
            });
            getReplyData(mRecyclerView, mDataAdapter);
            container.addView(inflate);
        } else if (position == 2) {
            View inflate = LayoutInflater.from(mContext).inflate(R.layout.myrec_layout, container, false);
            MyLRecyclerView mRecyclerView = inflate.findViewById(R.id.rec_first);
            v = inflate;
            FollowAdapter followAdapter = new FollowAdapter(mContext);
            initRecView(4, mRecyclerView, followAdapter, () -> {
                PAGE_INDEX_FOLLOW = 1;
                mRecyclerView.hideFooterView(true);
                followAdapter.clear();
                getFollowData(PAGE_INDEX_FOLLOW++, 0, followAdapter, mRecyclerView);
            }, () -> getFollowData(PAGE_INDEX_FOLLOW++, 0, followAdapter, mRecyclerView), (view, position1) -> {
                Intent intent = new Intent(mContext, UserDisplayActivity.class);
                intent.putExtra("uid", followAdapter.getDataList().get(position1).getFuserId() + "");
                intent.putExtra("uName", followAdapter.getDataList().get(position1).getUserName());
                intent.putExtra("uImage", followAdapter.getDataList().get(position1).getHeadImage());
                mContext.startActivity(intent);
            });
            getFollowData(PAGE_INDEX_FOLLOW++, 0, followAdapter, mRecyclerView);
            container.addView(inflate);
        } else {
            View inflate = LayoutInflater.from(mContext).inflate(R.layout.myrec_layout, container, false);
            MyLRecyclerView mRecyclerView = inflate.findViewById(R.id.rec_first);
            v = inflate;
            FollowAdapter followAdapter = new FollowAdapter(mContext);
            initRecView(4, mRecyclerView, followAdapter, () -> {
                PAGE_INDEX_FANS = 1;
                mRecyclerView.hideFooterView(true);
                followAdapter.clear();
                getFollowData(PAGE_INDEX_FANS++, 1, followAdapter, mRecyclerView);
            }, () -> getFollowData(PAGE_INDEX_FANS++, 1, followAdapter, mRecyclerView), (view, position12) -> {
                Intent intent = new Intent(mContext, UserDisplayActivity.class);
                intent.putExtra("uid", followAdapter.getDataList().get(position12).getFuserId() + "");
                intent.putExtra("uName", followAdapter.getDataList().get(position12).getUserName());
                intent.putExtra("uImage", followAdapter.getDataList().get(position12).getHeadImage());
                mContext.startActivity(intent);
            });
            getFollowData(PAGE_INDEX_FANS++, 1, followAdapter, mRecyclerView);
            container.addView(inflate);
        }
        return v;
    }

    private void initRecView(int spancount, final MyLRecyclerView mRecyclerView, ListBaseAdapter adapter, OnRefreshListener listener, OnLoadMoreListener loadMoreListener, OnItemClickListener itemClickListener) {
        GridLayoutManager manager = new GridLayoutManager(mContext, spancount);
        mRecyclerView.setLayoutManager(manager);
        LRecyclerViewAdapter mLRecyclerViewAdapter = new LRecyclerViewAdapter(adapter);
        mRecyclerView.setAdapter(mLRecyclerViewAdapter);
        mLRecyclerViewAdapter.setOnItemClickListener(itemClickListener);
        //设置头部加载颜色
        mRecyclerView.setHeaderViewColor(R.color.theme_green, R.color.darkgrey, android.R.color.white);
        //设置底部加载颜色
        mRecyclerView.setFooterViewColor(R.color.theme_green, R.color.darkgrey, android.R.color.white);
        //设置底部加载文字提示
        mRecyclerView.setFooterViewHint("玩命加载中", "没有更多内容了！", "网络不给力啊，点击再试一次吧");
        mRecyclerView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        if (spancount != 4) {
            int spacing = mContext.getResources().getDimensionPixelSize(R.dimen.dp_066);
            mRecyclerView.addItemDecoration(SpacesItemDecoration.newInstance(spacing, spacing, manager.getSpanCount(), Color.parseColor("#D3D3D3")));
        }
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setOnRefreshListener(listener);
        mRecyclerView.setOnLoadMoreListener(loadMoreListener);
    }

    private void getReplyData(MyLRecyclerView mRecyclerView, MyReplyAdapter mDataAdapter) {
        if (DevicesUtils.isNetworkAvailable(mContext)) {
            TRequest<ReqMyTopic> req = new TRequest<>();
            ReqMyTopic topic = new ReqMyTopic();
            topic.setPage(PAGE_INDEX_REPLY++);
            topic.setRows(PAGE_SIZE);
            topic.setCuid(mUid);
            if (SNApplication.userInfo.getUserInfo() == null) {
                topic.setUid("-1");
            } else {
                topic.setUid(SNApplication.userInfo.getUserInfo().getUid() + "");
            }
            req.setParam(topic, "2103", "1");
            Observable<TResponse_MyReply> topicObservable = RetrofitClient.getInstance().getService(HttpService.class).MyReply(req);
            ((BaseActivity) mContext).sendRequest(topicObservable, tResponse_myReply -> {
                Log.i(TAG, new Gson().toJson(tResponse_myReply));
                mDataAdapter.addAll(tResponse_myReply.getPageInfo().getRows());
                mRecyclerView.refreshComplete(tResponse_myReply.getPageInfo().getRows().size());
                if (loadReadyListener != null) {
                    loadReadyListener.LoadFinish(2, tResponse_myReply.getPageInfo().getTotal());
                }
                if (tResponse_myReply.getPageInfo().getRows().size() < PAGE_SIZE) {
                    mRecyclerView.setNoMore(true);
                    new Handler(mContext.getMainLooper()).postDelayed(() -> mRecyclerView.hideFooterView(false), 500);
                }
            }, throwable -> {
                ToastUtils.showToast(throwable.getMessage());
                mRecyclerView.refreshComplete(0);
            });
        }
        mRecyclerView.refreshComplete(PAGE_SIZE);
    }

    private void getData(final MyLRecyclerView mRecyclerView, final MyTopicAdapter mDataAdapter) {
        if (DevicesUtils.isNetworkAvailable(mContext)) {
            TRequest<ReqMyTopic> req = new TRequest<>();
            ReqMyTopic topic = new ReqMyTopic();
            topic.setPage(PAGE_INDEX_TOPIC++);
            topic.setRows(PAGE_SIZE);
            topic.setCuid(mUid);
            if (SNApplication.userInfo.getUserInfo() == null) {
                topic.setUid("-1");
            } else {
                topic.setUid(SNApplication.userInfo.getUserInfo().getUid() + "");
            }
            req.setParam(topic, "2101", "1");
            Observable<TResponse_MyTopic> topicObservable = RetrofitClient.getInstance().getService(HttpService.class).MyTopic(req);
            ((BaseActivity) mContext).sendRequest(topicObservable, tResponse_myTopic -> {
                mDataAdapter.addAll(tResponse_myTopic.getPageInfo().getRows());
                mRecyclerView.refreshComplete(tResponse_myTopic.getPageInfo().getRows().size());
                if (loadReadyListener != null) {
                    loadReadyListener.LoadFinish(1, tResponse_myTopic.getPageInfo().getTotal());
                }
                if (tResponse_myTopic.getPageInfo().getRows().size() < PAGE_SIZE) {
                    mRecyclerView.setNoMore(true);
                    new Handler(mContext.getMainLooper()).postDelayed(() -> mRecyclerView.hideFooterView(false), 500);
                }
                mDataAdapter.notifyDataSetChanged();
            }, throwable -> {
                ToastUtils.showToast(throwable.getMessage());
                mRecyclerView.refreshComplete(0);
            });
        }
        mRecyclerView.refreshComplete(PAGE_SIZE);
    }

    private void getFollowData(int Index, int type, FollowAdapter adapter, MyLRecyclerView recyclerView) {
        TRequest<ReqUidTypePR> request = new TRequest<>();
        ReqUidTypePR pr = new ReqUidTypePR();
        pr.setPage(Index);
        pr.setRows(20);
        pr.setType(type);
        pr.setUid(Integer.parseInt(mUid));
        request.setParam(pr, "2112", "1");
        ((BaseActivity) mContext).sendRequest(RetrofitClient.getInstance().getService(HttpService.class).FollowBeFollow(request), new Consumer<Response_FollowBeFollow>() {
            @Override
            public void accept(Response_FollowBeFollow response_followBeFollow) throws Exception {
                adapter.addAll(response_followBeFollow.getPageInfo().getRows());
                if (loadReadyListener != null) {
                    if (type == 0) {
                        loadReadyListener.LoadFinish(3, response_followBeFollow.getPageInfo().getTotal());
                    } else {
                        loadReadyListener.LoadFinish(4, response_followBeFollow.getPageInfo().getTotal());
                    }
                }
                recyclerView.refreshComplete(response_followBeFollow.getPageInfo().getRows().size());
                if (response_followBeFollow.getPageInfo().getRows().size() < 20) {
                    recyclerView.setNoMore(true);
                    new Handler(mContext.getMainLooper()).postDelayed(() -> recyclerView.hideFooterView(false), 500);
                }
            }
        }, throwable -> ToastUtils.showToast(throwable.getMessage()));
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    public void setLoadReadyListener(LoadReadyListener listener) {
        loadReadyListener = listener;
    }

    public interface LoadReadyListener {
        void LoadFinish(int position, int Count);
    }
}
