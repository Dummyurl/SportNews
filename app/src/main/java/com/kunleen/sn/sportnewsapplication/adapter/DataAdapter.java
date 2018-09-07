package com.kunleen.sn.sportnewsapplication.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.kunleen.sn.sportnewsapplication.R;
import com.kunleen.sn.sportnewsapplication.network.bean.TResponse_NewsList;
import com.kunleen.sn.sportnewsapplication.utils.GlideCacheUtil;

import java.util.Random;


public class DataAdapter extends ListBaseAdapter<TResponse_NewsList.row> {
    public static final int NEWS_TYPE_ONE = 1;//纯文字
    public static final int NEWS_TYPE_TWO = 2;//左文字右图片
    public static final int NEWS_TYPE_THREE = 3;//三张图
    public static final int NEWS_TYPE_FOUR = 4;//上文字下视频
    public static final int NEWS_TYPE_FIVE = 5;//上文字下图片
    public static final int NEWS_TYPE_SIX = 6;//左文字右视频
    public static final int NEWS_TYPE_SEVEN = 7;//广告跳转H5
    public static final int NEWS_TYPE_EIGHT = 8; //广告跳转下载页
    public static final int NEWS_TYPE_NINE = 9;//上图片下文字

    public DataAdapter(Activity context) {
        super(context);
    }

    @Override
    public int getLayoutId(int viewType) {
        switch (viewType) {
            case NEWS_TYPE_ONE: {
                return R.layout.news_noimg_layout;
            }
            case NEWS_TYPE_TWO: {
                return R.layout.news_rightimg_layout;
            }
            case NEWS_TYPE_THREE: {
                return R.layout.news_thrimage_layout;
            }
            case NEWS_TYPE_FOUR: {
                return R.layout.news_buttonvideo_layout;
            }
            case NEWS_TYPE_FIVE: {
                return R.layout.news_buttonvideo_layout;
            }
            case NEWS_TYPE_SIX: {
                return R.layout.news_rightimg_layout;

            }
            case NEWS_TYPE_SEVEN:
            case NEWS_TYPE_EIGHT: {
                return R.layout.news_buttonvideo_layout;
            }
            case NEWS_TYPE_NINE: {
                return R.layout.news_buttonvideo_layout;
            }
        }
        return R.layout.news_noimg_layout;
    }

    @Override
    public int getItemViewType(int position) {
        return mDataList.get(position).getShowType();
    }

    @Override
    public void onBindItemHolder(SuperViewHolder holder, int position) {
        ((TextView) holder.getView(R.id.tv_news_title)).setText(mDataList.get(position).getTitle());
        Random random = new Random(System.currentTimeMillis());
        int a = random.nextInt(100000) % 10000;
        if (a > 10000) {
            float b = (float) a / 10000;
            ((TextView) holder.getView(R.id.tv_news_read_count)).setText(b + "万");
        } else {
            ((TextView) holder.getView(R.id.tv_news_read_count)).setText(a + "");
        }
        switch (mDataList.get(position).getShowType()) {
            case NEWS_TYPE_ONE: {
                break;
            }
            case NEWS_TYPE_FIVE:

            case NEWS_TYPE_NINE:
            case NEWS_TYPE_TWO: {
                String[] split = mDataList.get(position).getImageUrl().split(";");
                GlideCacheUtil.LoadImageInWifi(mContext, holder.getView(R.id.iv_news_img), split[0]);
                break;
            }
            case NEWS_TYPE_THREE: {
                String[] split = mDataList.get(position).getImageUrl().split(";");
                GlideCacheUtil.LoadImageInWifi(mContext, holder.getView(R.id.iv_news_img1), split[0]);
                GlideCacheUtil.LoadImageInWifi(mContext, holder.getView(R.id.iv_news_img2), split[1]);
                GlideCacheUtil.LoadImageInWifi(mContext, holder.getView(R.id.iv_news_img3), split[2]);
                break;
            }
            case NEWS_TYPE_SIX:
            case NEWS_TYPE_FOUR: {
                String[] split = mDataList.get(position).getImageUrl().split(";");
                GlideCacheUtil.LoadImageInWifi(mContext, holder.getView(R.id.iv_news_img), split[0]);
                holder.getView(R.id.iv_play_btn).setVisibility(View.VISIBLE);
                break;
            }
            case NEWS_TYPE_EIGHT:
            case NEWS_TYPE_SEVEN: {
                String[] split = mDataList.get(position).getImageUrl().split(";");
                holder.getView(R.id.tv_ad).setVisibility(View.VISIBLE);
                GlideCacheUtil.LoadImageInWifi(mContext, holder.getView(R.id.iv_news_img), split[0]);
                holder.getView(R.id.ll_rd_count).setVisibility(View.GONE);
                break;
            }
        }
    }
}
