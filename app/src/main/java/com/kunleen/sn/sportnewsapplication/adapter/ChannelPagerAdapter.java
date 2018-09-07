package com.kunleen.sn.sportnewsapplication.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.github.jdsjlzx.ItemDecoration.SpacesItemDecoration;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.google.gson.Gson;
import com.kunleen.sn.sportnewsapplication.R;
import com.kunleen.sn.sportnewsapplication.activity.ForumListActivity;
import com.kunleen.sn.sportnewsapplication.activity.VideoActivity;
import com.kunleen.sn.sportnewsapplication.activity.WebView1Activity;
import com.kunleen.sn.sportnewsapplication.activity.WebViewActivity;
import com.kunleen.sn.sportnewsapplication.app.SNApplication;
import com.kunleen.sn.sportnewsapplication.base.BaseActivity;
import com.kunleen.sn.sportnewsapplication.base.BaseFragment;
import com.kunleen.sn.sportnewsapplication.custom.CopyImageView;
import com.kunleen.sn.sportnewsapplication.custom.HeadBannerView;
import com.kunleen.sn.sportnewsapplication.custom.MyLRecyclerView;
import com.kunleen.sn.sportnewsapplication.custom.MyViewPager;
import com.kunleen.sn.sportnewsapplication.network.RetrofitClient;
import com.kunleen.sn.sportnewsapplication.network.bean.Channel;
import com.kunleen.sn.sportnewsapplication.network.bean.ReqEmpty;
import com.kunleen.sn.sportnewsapplication.network.bean.ReqNewsList;
import com.kunleen.sn.sportnewsapplication.network.bean.ReqReleaseId;
import com.kunleen.sn.sportnewsapplication.network.bean.Response_Carouse;
import com.kunleen.sn.sportnewsapplication.network.bean.Response_FirstCircle;
import com.kunleen.sn.sportnewsapplication.network.bean.TRequest;
import com.kunleen.sn.sportnewsapplication.network.bean.TResponse_NewsList;
import com.kunleen.sn.sportnewsapplication.network.service.HttpService;
import com.kunleen.sn.sportnewsapplication.utils.AppUtils;
import com.kunleen.sn.sportnewsapplication.utils.DevicesUtils;
import com.kunleen.sn.sportnewsapplication.utils.JsonUtils;
import com.kunleen.sn.sportnewsapplication.utils.ShareUtils;
import com.kunleen.sn.sportnewsapplication.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;


/**
 * Created by ysy on 2017/9/25.
 */

public class ChannelPagerAdapter extends PagerAdapter {
    public static final String TAG = "ChannelPagerAdapter_LOG";
    List<Channel> keyWords;
    ArrayList<Integer> PAGE_INDEX;
    Activity mContext;
    BaseFragment mfragment;
    private static final int PAGE_SIZE = 20;
    private int AD_INDEX = 0;
    //0：足球 1：篮球
    private int TYPE = 0;
    private ArrayList<CopyImageView> headImageList = new ArrayList();

    public ChannelPagerAdapter(Activity context, BaseFragment fragment) {
        keyWords = new ArrayList<>();
        PAGE_INDEX = new ArrayList<>();
        mContext = context;
        mfragment = fragment;
    }

    public void setKeyWords(List<Channel> keyWords, int type) {
        this.keyWords.clear();
        this.keyWords = keyWords;
        PAGE_INDEX.clear();
        for (int i = 0; i < keyWords.size(); i++) {
            PAGE_INDEX.add(1);
        }
        TYPE = type;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return keyWords.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.myrec_layout, container, false);
        MyLRecyclerView mRecyclerView = view.findViewById(R.id.rec_first);
        initRecView(mRecyclerView, position);
        container.addView(view);
        return view;
    }

    private void initRecView(final MyLRecyclerView mRecyclerView, final int position) {
        GridLayoutManager manager = new GridLayoutManager(mContext, 1);
        mRecyclerView.setLayoutManager(manager);
        final DataAdapter mDataAdapter = new DataAdapter(mContext);
        LRecyclerViewAdapter mLRecyclerViewAdapter = new LRecyclerViewAdapter(mDataAdapter);
        mRecyclerView.setAdapter(mLRecyclerViewAdapter);
        //设置头部加载颜色
        mRecyclerView.setHeaderViewColor(R.color.theme_green, R.color.darkgrey, android.R.color.white);
        //设置底部加载颜色
        mRecyclerView.setFooterViewColor(R.color.theme_green, R.color.darkgrey, android.R.color.white);
        //设置底部加载文字提示
        mRecyclerView.setFooterViewHint("玩命加载中", "没有更多新闻了！", "网络不给力啊，点击再试一次吧");
        int spacing = mContext.getResources().getDimensionPixelSize(R.dimen.dp_066);
        mRecyclerView.addItemDecoration(SpacesItemDecoration.newInstance(spacing, spacing, manager.getSpanCount(), Color.parseColor("#D3D3D3")));
        mRecyclerView.setNestedScrollingEnabled(false);
        //        GridItemDecoration divider = new GridItemDecoration.Builder(this)
//                .setHorizontal(R.dimen.dp_4)
//                .setVertical(R.dimen.dp_4)
//                .setColorResource(R.color.split)
//                .build();
        mRecyclerView.setHasFixedSize(true);
        if (SNApplication.channels.get(TYPE).getList().get(position).getClassyId() < 10) {
            View v = LayoutInflater.from(mContext).inflate(R.layout.layout_banner_header, null);
            mLRecyclerViewAdapter.addHeaderView(v);
            HeadBannerView banner = v.findViewById(R.id.banner_first_page);
            List<Response_Carouse.Carousel> carousels = SNApplication.carousel.getCarousel();
            banner.setOnSingleTouchListener(new MyViewPager.OnSingleTouchListener() {
                @Override
                public void onSingleTouch() {
                    Response_Carouse.Carousel carousel;
                    int _point = banner.getPoint();
                    if (carousels.size() != 2) {
                        carousel = carousels.get(_point % headImageList.size());
                    } else {
                        carousel = carousels.get(_point % 2);
                    }
                    Log.i("okhttp", "carousel:" + new Gson().toJson(carousel));
                    if (carousel != null) {
                        if (carousel.getCarouselValue() != null && carousel.getCarouselValue().contains("http") && carousel.getCarouselValue().trim() != "") {
                            String url = carousel.getCarouselValue() + "?cid=" + SNApplication.CPID;
                            Intent intent = new Intent(mContext, WebView1Activity.class);
                            intent.putExtra("title", "");
                            intent.putExtra("url", url);
                            Log.i("okhttp", "url:" + url);
                            mContext.startActivity(intent);
                        }
                    }
                }
            });
            initHeadBanner(SNApplication.carousel.getCarousel(), banner);
            RecyclerView rv = v.findViewById(R.id.rc_forum);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
            linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            rv.setLayoutManager(linearLayoutManager);
            CircleAdapter adapter = new CircleAdapter(mContext, R.layout.layout_forum_rv_item);
            rv.setAdapter(adapter);
            adapter.setOnItemClickListener((view, position1) -> {
                Intent intent = new Intent(mContext, ForumListActivity.class);
                intent.putExtra("cid", adapter.getDatas().get(position1).getcId() + "");
                intent.putExtra("classfyId", adapter.getDatas().get(position1).getClassfyId() + "");
                mContext.startActivity(intent);
            });
            if (DevicesUtils.isNetworkAvailable(mContext)) {
                getCircle(adapter);
            } else {
                String circle = ShareUtils.getString("circle");
                if (circle != null && !circle.isEmpty()) {
                    Response_FirstCircle response_firstCircle = new Gson().fromJson(circle, Response_FirstCircle.class);
                    adapter.setDatas(response_firstCircle.getPageInfo().getRows());
                }
            }
        }
        getData(mRecyclerView, mDataAdapter, position);
        mRecyclerView.setOnRefreshListener(() -> {
            PAGE_INDEX.set(position, 1);
            getData(mRecyclerView, mDataAdapter, position);
        });
        mLRecyclerViewAdapter.setOnItemClickListener((view, position1) -> {
            if (DevicesUtils.isNetworkAvailable(mContext)) {
                TResponse_NewsList.row row = mDataAdapter.getDataList().get(position1);
                if (row.getShowType() == 7 || row.getShowType() == 8) {
                    AppUtils.OutToSysBrowser(row.getSecTitle().split(";")[0], mContext);
                } else if (row.getShowType() == 4 || row.getShowType() == 6) {
                    Intent intent = new Intent(mContext, VideoActivity.class);
                    String[] split = row.getImageUrl().split(";");
                    if (split.length > 1) {
                        intent.putExtra("title", row.getTitle());
                        intent.putExtra("titleid", row.getTitleId());
                        intent.putExtra("url", split[1]);
                        intent.putExtra("type", 2);
                        mContext.startActivity(intent);
                    } else {
                        ToastUtils.showToast("视频源有误，暂时无法观看");
                    }
                } else {
                    Intent intent = new Intent(mContext, WebViewActivity.class);
                    intent.putExtra("title", row.getTitle());
                    intent.putExtra("titleid", row.getTitleId());
                    intent.putExtra("type", 1);
                    mContext.startActivity(intent);
                }
            } else {
                ToastUtils.showToast("网络状况不佳，请连接网络后再试！");
            }
        });
        mRecyclerView.setOnLoadMoreListener(() -> {
            if (DevicesUtils.isNetworkAvailable(mContext)) {
                TRequest<ReqNewsList> req = new TRequest<>();
                ReqNewsList newsList = new ReqNewsList();
                if (SNApplication.channels.get(TYPE).getList().get(position).getClassyId() == 10000000) {
                    //添加置顶新闻
                } else {
                    newsList.setClassfyId(SNApplication.channels.get(TYPE).getList().get(position).getClassyId() + "");
                }
                newsList.setUid("0");
                newsList.setPage(PAGE_INDEX.get(position));
                newsList.setRows(PAGE_SIZE);
                req.setParam(newsList, "1110", "1");
                Observable<TResponse_NewsList> ysy = RetrofitClient.getInstance().getService(HttpService.class).newsList(req);
                ((BaseActivity) mContext).sendRequest(ysy, tResponse_newsList -> {
                    mDataAdapter.addAll(InsertAd(tResponse_newsList.getPageInfo().getRows()));
                    PAGE_INDEX.set(position, PAGE_INDEX.get(position) + 1);
                    mRecyclerView.refreshComplete(PAGE_SIZE);
                    if (tResponse_newsList.getPageInfo().getRows().size() < PAGE_SIZE) {
                        mRecyclerView.setNoMore(true);
                    }
                }, throwable -> {
                    ToastUtils.showToast(throwable.getMessage());
                    mRecyclerView.refreshComplete(0);
                });
            } else {
                ToastUtils.showToast("网络状况异常，请检查网络后再试！");
            }
        });
    }

    private void getData(final MyLRecyclerView mRecyclerView, final DataAdapter mDataAdapter, final int position) {
        mDataAdapter.clear();
        Gson gson = new Gson();
        if (DevicesUtils.isNetworkAvailable(mContext)) {
            TRequest<ReqNewsList> req = new TRequest<>();
            ReqNewsList newsList = new ReqNewsList();
//            if (SNApplication.channels.get(TYPE).getList().get(position).getClassyId() != 10000000) {
            newsList.setClassfyId(SNApplication.channels.get(TYPE).getList().get(position).getClassyId() + "");
//            }
            if (SNApplication.userInfo.getUserInfo() == null) {
                newsList.setUid("0");
            } else {
                newsList.setUid(SNApplication.userInfo.getUserInfo().getUid() + "");
            }
            newsList.setPage(1);
            newsList.setRows(PAGE_SIZE);
            req.setParam(newsList, "1110", "1");
            Observable<TResponse_NewsList> ysy = RetrofitClient.getInstance().getService(HttpService.class).newsList(req);
            ((BaseActivity) mContext).sendRequest(ysy, tResponse_newsList -> {
                if (SNApplication.channels.get(TYPE).getList().get(position).getClassyId() < 10) {
                    mDataAdapter.setDataList(new ArrayList<>(SNApplication.top_news_list));
                }
                mDataAdapter.addAll(InsertAd(tResponse_newsList.getPageInfo().getRows()));
                Log.i("networks", "gson" + gson.toJson(mDataAdapter.getDataList()));
                ShareUtils.putString(SNApplication.channels.get(TYPE).getList().get(position).getClassyId() + "", gson.toJson(mDataAdapter.getDataList()));
            }, throwable -> {
                ToastUtils.showToast("获取新闻异常，请确认网络状态后重试！");
                mRecyclerView.refreshComplete(0);
            });
        } else {
            String rows = ShareUtils.getString(SNApplication.channels.get(TYPE).getList().get(position).getClassyId() + "");
            if (rows != null) {
                mDataAdapter.addAll(JsonUtils.getJsonArray(rows, TResponse_NewsList.row.class));
            }
        }
        PAGE_INDEX.set(position, PAGE_INDEX.get(position) + 1);
        mRecyclerView.refreshComplete(PAGE_SIZE);
    }

    public List<TResponse_NewsList.row> InsertAd(List<TResponse_NewsList.row> rows) {
        if (SNApplication.ad_list.size() > 0) {
            int size = rows.size();
            if (rows.size() > 10) {
                int index_1 = size / 4 + (int) (Math.random() * size / 4);
                rows.add(index_1, SNApplication.ad_list.get(AD_INDEX++));
                check_index();
                int index_2 = size * 3 / 4 + (int) (Math.random() * size / 4) + 1;
                rows.add(index_2, SNApplication.ad_list.get(AD_INDEX++));
                check_index();
            } else if (rows.size() > 4) {
                int index_1 = size / 4 + (int) (Math.random() * size / 4);
                rows.add(index_1, SNApplication.ad_list.get(AD_INDEX++));
                check_index();
            }
        }
        return rows;
    }

    private void getCircle(CircleAdapter adapter) {
        TRequest<ReqEmpty> request = new TRequest<>();
        request.setParam(new ReqEmpty(), "2115", "1");
        ((BaseActivity) mContext).sendRequest(RetrofitClient.getInstance().getService(HttpService.class).FirstCircle(request), new Consumer<Response_FirstCircle>() {
            @Override
            public void accept(Response_FirstCircle response_firstCircle) throws Exception {
                adapter.setDatas(response_firstCircle.getPageInfo().getRows());
                ShareUtils.putString("circle", new Gson().toJson(response_firstCircle));
            }
        }, throwable -> ToastUtils.showToast(throwable.getMessage()));

    }

    private void initHeadBanner(List<Response_Carouse.Carousel> list, HeadBannerView banner) {
        headImageList = new ArrayList<>();
        int position = -1;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getCarouselId() == 1) {
                position = i;
            }
        }
        if (position != -1) {
            list.remove(position);
        }
        boolean isTwo = false;
        List<String> urls = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            CopyImageView iv = new CopyImageView(mContext);
            iv.setScaleType(ImageView.ScaleType.FIT_XY);
            headImageList.add(iv);
            urls.add(list.get(i).getCarouselImage());
        }
        if (list.size() == 2) {
            isTwo = true;
            for (int i = 0; i < list.size(); i++) {
                CopyImageView iv = new CopyImageView(mContext);
                iv.setScaleType(ImageView.ScaleType.FIT_XY);
                headImageList.add(iv);
                urls.add(list.get(i).getCarouselImage());
            }
        }
        if (list.size() != 0) {
            banner.setDatas(headImageList, urls, isTwo);
//            banner.setOnSingleTouchListener(imageClickListener);
        }
    }

    private void check_index() {
        if (AD_INDEX == SNApplication.ad_list.size()) {
            AD_INDEX = 0;
        }
    }
}