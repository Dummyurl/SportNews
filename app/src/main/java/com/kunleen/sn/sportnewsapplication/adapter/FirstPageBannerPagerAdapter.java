package com.kunleen.sn.sportnewsapplication.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;


import com.kunleen.sn.sportnewsapplication.custom.CopyImageView;
import com.kunleen.sn.sportnewsapplication.utils.GlideCacheUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/11/23 0023.
 */
public class FirstPageBannerPagerAdapter extends PagerAdapter {

    private List<CopyImageView> datas;
    private List<String> urls;
    private Context mContext;

    public FirstPageBannerPagerAdapter(Context context) {
        datas = new ArrayList<>();
        mContext = context;
    }

    public void setDatas(List<CopyImageView> datas) {
        this.datas.clear();
        this.datas.addAll(datas);
        this.notifyDataSetChanged();
    }


    public void setUrls(List<String> urls) {
        this.urls = urls;

    }

    @Override
    public int getCount() {
        if (datas.size() == 1) {
            return 1;
        } else {
            return 5000;
        }
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        if (datas.size() != 0 && urls != null) {
            CopyImageView clone = datas.get(position % datas.size()).clone();
            GlideCacheUtil.LoadImage(mContext, clone, urls.get(position % datas.size()), false);
            container.addView(clone);
            return clone;
        }
        return null;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(((View) object));
    }

}
