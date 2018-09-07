package com.kunleen.sn.sportnewsapplication.custom;

import android.content.Context;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;


import com.kunleen.sn.sportnewsapplication.R;
import com.kunleen.sn.sportnewsapplication.adapter.FirstPageBannerPagerAdapter;
import com.kunleen.sn.sportnewsapplication.utils.GlideCacheUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/11/23 0023.
 */
public class HeadBannerView extends FrameLayout implements ViewPager.OnPageChangeListener {
    private Handler handler;
    private MyViewPager viewPager;
    private ImgNavView inv;
    private FirstPageBannerPagerAdapter adapter;
    private List<String> urls;
    List<CopyImageView> datas = new ArrayList<>();
    boolean canMove = true;
    int position;
    SinglePagerAdapter singleadapter;
    boolean isTwo = false;

    public HeadBannerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.custom_banner_layout, this, true);
        viewPager = findViewById(R.id.vp_banner);
        adapter = new FirstPageBannerPagerAdapter(getContext());
        viewPager.setAdapter(adapter);
        inv = findViewById(R.id.img_nav);
    }

    public void setOnSingleTouchListener(MyViewPager.OnSingleTouchListener onSingleTouchListener) {
        viewPager.setOnSingleTouchListener(onSingleTouchListener);
    }


    public void setDatas(List<CopyImageView> datas, List<String> urls, boolean isTwo) {
        this.isTwo = isTwo;
        this.urls = urls;
        this.datas = datas;
        if (datas.size() != 1) {
            if (!isTwo) {
                inv.setCount(datas.size());
            } else {
                inv.setCount(2);
            }
            adapter.setUrls(this.urls);
            adapter.setDatas(this.datas);
            viewPager.setAdapter(adapter);
            position = 2500 - 2500 % datas.size();
            viewPager.setCurrentItem(position);
            viewPager.setOnPageChangeListener(this);
            viewPager.setOnTouchListener(new OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        canMove = false;
                    }
                    return false;
                }
            });
            if (handler == null) {
                handler = new Handler();
            } else {
            }
            handler.postDelayed(new Runnable() {

                @Override
                public void run() {
                    if (canMove) {
                        position++;
                        if (position == 5000) {
                            position = 0;
                            viewPager.setCurrentItem(position, false);
                        } else {
                            viewPager.setCurrentItem(position, true);
                        }
                    }
                    canMove = true;
                    handler.postDelayed(this, 5000);
                }
            }, 5000);
        } else {
            singleadapter = new SinglePagerAdapter(datas.get(0), urls.get(0));
            viewPager.setAdapter(singleadapter);
        }
    }

    public List<CopyImageView> getDatas() {
        return datas;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if (datas.size() != 1) {
            if (!isTwo) {
                inv.selectIndex(position % datas.size());
            } else {
                inv.selectIndex(position % (datas.size() / 2));
            }
        }
        this.position = position;
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    public int getPoint() {
        return position;
    }

    class SinglePagerAdapter extends PagerAdapter {
        CopyImageView civ;
        String urls;

        public SinglePagerAdapter(CopyImageView civ, String urls) {
            this.civ = civ;
            this.urls = urls;
        }

        @Override
        public int getCount() {
            return 1;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            GlideCacheUtil.LoadImage(getContext(), civ, urls, false);
//            NetUtils.disImageView(urls, civ, R.mipmap.image_loading, R.mipmap.image_loading);
            container.addView(civ);
            return civ;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(civ);
        }
    }
}
