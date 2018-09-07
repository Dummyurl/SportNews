package com.kunleen.sn.sportnewsapplication.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.kunleen.sn.sportnewsapplication.R;
import com.kunleen.sn.sportnewsapplication.activity.HomeActivity;
import com.kunleen.sn.sportnewsapplication.activity.SearchActivity;
import com.kunleen.sn.sportnewsapplication.adapter.ChannelPagerAdapter;
import com.kunleen.sn.sportnewsapplication.app.SNApplication;
import com.kunleen.sn.sportnewsapplication.base.BaseFragment;
import com.kunleen.sn.sportnewsapplication.custom.CircleImageView;
import com.kunleen.sn.sportnewsapplication.custom.HorizontalNavigationBar.HorizontalNavigationBar;
import com.kunleen.sn.sportnewsapplication.custom.HorizontalNavigationBar.MyHorizontalNavigationBar;
import com.kunleen.sn.sportnewsapplication.network.bean.Channel;
import com.kunleen.sn.sportnewsapplication.network.bean.Response_FirstChannel;
import com.kunleen.sn.sportnewsapplication.utils.DevicesUtils;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FirstPageFragment extends BaseFragment implements View.OnClickListener {
    public static final String TAG = "FirstPageFragment_log";
    CircleImageView cir_head;
    LinearLayout ll_first_head, ll_circle;
    HashMap<String, Integer> map = new HashMap<>();
    public static final int FOOTBALL = 0;
    public static final int BASKETBALL = 1;
    public RadioGroup rg_type;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_first_page, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.btn_search).setOnClickListener(this);
        view.findViewById(R.id.rl_cir).setOnClickListener(this);
        cir_head = view.findViewById(R.id.iv_user_incon);
        ll_first_head = view.findViewById(R.id.ll_first_head);
//        ll_first_head.setPadding(0, SNApplication.statusBar, 0, 0);
        ll_circle = view.findViewById(R.id.ll_circle);
        rg_type = view.findViewById(R.id.rg_type);
        rg_type.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.first_football) {
                view.findViewById(R.id.hs_channel_f).setVisibility(View.VISIBLE);
                view.findViewById(R.id.hs_channel_b).setVisibility(View.GONE);
                view.findViewById(R.id.vp_channel).setVisibility(View.VISIBLE);
                view.findViewById(R.id.vp_channel_b).setVisibility(View.GONE);
            } else {
                view.findViewById(R.id.hs_channel_f).setVisibility(View.GONE);
                view.findViewById(R.id.hs_channel_b).setVisibility(View.VISIBLE);
                view.findViewById(R.id.vp_channel).setVisibility(View.GONE);
                view.findViewById(R.id.vp_channel_b).setVisibility(View.VISIBLE);
            }
        });
        rg_type.check(R.id.first_football);
        initChannel(view, R.id.hs_channel_f, R.id.vp_channel, FOOTBALL);
        initChannel(view, R.id.hs_channel_b, R.id.vp_channel_b, BASKETBALL);
    }


    private void initChannel(View view, int channel_layout_id, int vp_layout_id, int type) {
        MyHorizontalNavigationBar mHorizontalNavigationBar = view.findViewById(channel_layout_id);
        mHorizontalNavigationBar.setChannelSplit(true);
        List<Response_FirstChannel.mList> list = SNApplication.channels.get(type).getList();
        List<Channel> channels = new ArrayList<>();
        for (Response_FirstChannel.mList lists : list) {
            Channel channel = new Channel();
            channel.setChannelName(lists.getClassyName());
            channel.setClassfy(lists.getClassyId());
            channels.add(channel);
        }
        mHorizontalNavigationBar.setItems(channels);
        mHorizontalNavigationBar.setCurrentChannelItem(0);
        ViewPager vp_channel = view.findViewById(vp_layout_id);
        ChannelPagerAdapter adapter = new ChannelPagerAdapter(getActivity(), this);
        vp_channel.setOffscreenPageLimit(3);
        vp_channel.setAdapter(adapter);
        vp_channel.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                mHorizontalNavigationBar.setCurrentChannelItem(position);
                vp_channel.requestLayout();
                if (map.get(vp_channel.getCurrentItem() + "") != null) {
                    Logger.t(TAG).d(map.get(vp_channel.getCurrentItem() + "") + "");
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mHorizontalNavigationBar.addOnHorizontalNavigationSelectListener(new HorizontalNavigationBar.OnHorizontalNavigationSelectListener() {
            @Override
            public void select(int index) {
                vp_channel.setCurrentItem(index);
            }
        });
        adapter.setKeyWords(channels, type);
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
        DevicesUtils.setWindowStatusBarColor(getActivity(), R.color.white);//设置状态栏颜色
        super.onResume();
        if (SNApplication.userInfo.getUserInfo() != null) {
            Glide.with(getActivity()).load(SNApplication.userInfo.getUserInfo().getUserInfo().getHeadImage()).apply(new RequestOptions().centerCrop().placeholder(R.mipmap.img_loading1).error(R.mipmap.button_head_image)).into(cir_head);
        } else {
            cir_head.setImageResource(R.mipmap.button_head_image);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_cir: {
                ((HomeActivity) getActivity()).moveToMine();
                break;
            }
            case R.id.btn_search: {
                Intent intent = new Intent(getActivity(), SearchActivity.class);
                intent.putExtra("key", "news");
                getActivity().startActivity(intent);
                break;
            }
        }
    }
}