package com.kunleen.sn.sportnewsapplication.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.kunleen.sn.sportnewsapplication.R;
import com.kunleen.sn.sportnewsapplication.adapter.HomePagerAdapter;
import com.kunleen.sn.sportnewsapplication.app.SNApplication;
import com.kunleen.sn.sportnewsapplication.base.BaseActivity;
import com.kunleen.sn.sportnewsapplication.custom.CircleImageView;
import com.kunleen.sn.sportnewsapplication.custom.CustomViewPager;
import com.kunleen.sn.sportnewsapplication.custom.MyRadioGroup;
import com.kunleen.sn.sportnewsapplication.fragment.FirstPageFragment;
import com.kunleen.sn.sportnewsapplication.fragment.ForumFragment;
import com.kunleen.sn.sportnewsapplication.fragment.GameFragment;
import com.kunleen.sn.sportnewsapplication.fragment.MallFragment;
import com.kunleen.sn.sportnewsapplication.fragment.MineFragment;
import com.kunleen.sn.sportnewsapplication.fragment.NoNameFragment;
import com.kunleen.sn.sportnewsapplication.utils.AppUtils;
import com.kunleen.sn.sportnewsapplication.utils.DevicesUtils;
import com.kunleen.sn.sportnewsapplication.utils.SelectorUtil;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends BaseActivity implements ViewPager.OnPageChangeListener, MyRadioGroup.OnCheckedChangeListener {
    public static final String TAG = "HomeActivity_log";
    private CustomViewPager viewPager;//5个Fragment内容页的容器
    private MyRadioGroup tabGroup;//导航栏
    private HomePagerAdapter adapter;//viewPager的适配器
    private List<Fragment> fragmentList;//装Fragment的集合
    private boolean isAppExit; // app退出标志位
    private String Img_3_checked, Img_3_unchecked, Img_4_checked, Img_4_unchecked, title_3, title_4, title_football;
    private RadioButton rb_tab_match, rb_tab_market;
    private boolean canRefrsh = true;
    public static String Url_game, Url_market, Url_football;
    public static int last_position_id = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        //启动全局定时器
        initView();
        initWindow(this);
        boolean goto_first_page = getIntent().getBooleanExtra("goto_first_page", false);
        if (goto_first_page) {
            goToFirstPage();
        }
//        if (DevicesUtils.checkDeviceHasNavigationBar(this)) {
//            SNApplication.navigationBar = DevicesUtils.getVirtualBarHeigh(this);
//        }
    }


    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    /**
     * 初始化控件
     */
    private void initView() {
        viewPager = findViewById(R.id.home_content_viewpager);
        viewPager.setScanScroll(false);
        setTab();
        fragmentList = new ArrayList<>();
        fragmentList.add(new FirstPageFragment());
        fragmentList.add(new GameFragment());
        fragmentList.add(new NoNameFragment());
        if (SNApplication.carousel != null && SNApplication.carousel.getSignCoce() == 0) {
            fragmentList.add(new MallFragment());
            findViewById(R.id.rb_tab_market).setVisibility(View.VISIBLE);
        }
        fragmentList.add(new ForumFragment());
        adapter = new HomePagerAdapter(getSupportFragmentManager(), fragmentList);
        viewPager.setAdapter(adapter);
        viewPager.setOnPageChangeListener(this);
        viewPager.setOffscreenPageLimit(5);//ViewPager的预加载
        tabGroup = findViewById(R.id.home_tab_group);
        tabGroup.setOnCheckedChangeListener(this);
        tabGroup.check(R.id.home_tab_first_page);
    }

    private void setTab() {
        rb_tab_match = findViewById(R.id.rb_tab_match);
        rb_tab_market = findViewById(R.id.rb_tab_market);
        if (SNApplication.carousel != null && SNApplication.carousel.getCarousel() != null) {
            for (int i = 0; i < SNApplication.carousel.getCarousel().size(); i++) {
                if (SNApplication.carousel.getCarousel().get(i).getCarouselId() == 2) {
                    title_football = SNApplication.carousel.getCarousel().get(i).getCarouselName();
                    Url_football = SNApplication.carousel.getCarousel().get(i).getCarouselValue();
                    ((TextView) findViewById(R.id.btn_football)).setText(title_football);
                } else if (SNApplication.carousel.getCarousel().get(i).getCarouselId() == 3) {
                    Img_3_checked = SNApplication.carousel.getCarousel().get(i).getCarouselImage();
                    title_3 = SNApplication.carousel.getCarousel().get(i).getCarouselName();
                    Url_game = SNApplication.carousel.getCarousel().get(i).getCarouselValue();
                } else if (SNApplication.carousel.getCarousel().get(i).getCarouselId() == 4) {
                    Img_3_unchecked = SNApplication.carousel.getCarousel().get(i).getCarouselImage();
                } else if (SNApplication.carousel.getCarousel().get(i).getCarouselId() == 5) {
                    Img_4_checked = SNApplication.carousel.getCarousel().get(i).getCarouselImage();
                    title_4 = SNApplication.carousel.getCarousel().get(i).getCarouselName();
                } else if (SNApplication.carousel.getCarousel().get(i).getCarouselId() == 6) {
                    Img_4_unchecked = SNApplication.carousel.getCarousel().get(i).getCarouselImage();
                    Url_market = SNApplication.carousel.getCarousel().get(i).getCarouselValue();
                }
            }
        }
//        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.button_xw_shangcheng2);
        if (Img_3_checked != null && Img_3_unchecked != null) {
            Log.i(TAG, Img_3_checked + title_3);
            rb_tab_match.setText(title_3);
//            SelectorUtil.addSeletorFromNet(Img_3_checked, Img_3_unchecked, rb_tab_match, bitmap.getWidth(), bitmap.getHeight());
        }
        if (Img_4_checked != null && Img_4_unchecked != null) {
            Log.i(TAG, Img_4_checked + title_4);
            rb_tab_market.setText(title_4);
//            SelectorUtil.addSeletorFromNet(Img_4_checked, Img_4_unchecked, rb_tab_market, bitmap.getWidth(), bitmap.getHeight());
        }
    }

    /**
     * ViewPager的滑动监听事件
     *
     * @param position
     * @param positionOffset
     * @param positionOffsetPixels
     */
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        tabGroup.check(tabGroup.getChildAt(position).getId());
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        adapter.getData(4).onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onCheckedChanged(MyRadioGroup group, int checkedId) {
        for (int i = 0; i < tabGroup.getChildCount(); i++) {
            if (tabGroup.getChildAt(i).getId() == checkedId) {
                viewPager.setCurrentItem(i, false);
//                BaseFragment _fragment = (BaseFragment) fragmentList.get(i);
//                if (!_fragment.isInitDatas) {
//                    _fragment.initDatas();
                if (SNApplication.carousel != null && SNApplication.carousel.getSignCoce() == 0 && ((RadioButton) tabGroup.getChildAt(i)).getText().toString().equals("彩票")) {
                    group.setVisibility(View.GONE);
                    findViewById(R.id.line1).setVisibility(View.GONE);
                } else {
                    last_position_id = checkedId;
                }
                if (i == 0 || i == 2) {
                    canRefrsh = false;
                }
            }
        }
    }


//    @Override
//    protected void onSaveInstanceState(Bundle outState) {
////        super.onSaveInstanceState(outState);
////        Parcelable p = this.mFragments.saveAllState();
////        if(p != null) {
////            outState.putParcelable("android:support:fragments", p);
////        }
//
//    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK) {//
            if (tabGroup.getCheckedRadioButtonId() == R.id.rb_tab_market) {
                if (last_position_id != 0) {
                    tabGroup.check(last_position_id);
                } else {
                    tabGroup.check(R.id.home_tab_first_page);
                }
                tabGroup.setVisibility(View.VISIBLE);
                findViewById(R.id.line1).setVisibility(View.VISIBLE);
            } else {
                AppExit();
            }
            return false;
        } /*else if (keyCode == KeyEvent.KEYCODE_HOME) {
            return false;
        }*/ else {
            return super.onKeyDown(keyCode, event);
        }
    }


    public void goToFirstPage() {
//        viewPager.setCurrentItem(0);
    }


    //按返回键进行监听
    public void AppExit() {
        if (!isAppExit) {
            isAppExit = true;
            Toast.makeText(HomeActivity.this, "再按一次, 退出" + getResources().getString(R.string.app_name), Toast.LENGTH_SHORT).show();
            handler.sendEmptyMessageDelayed(100, 2000);
        } else {// 2s内再次按back时,isExit= true，执行以下操作，app退出
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            startActivity(intent);
            System.exit(0);
        }

    }

    Handler handler = new Handler() {

        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 100:
                    isAppExit = false;
                    break;
                default:
                    break;
            }
        }
    };

    public void goToFirst() {
        viewPager.setCurrentItem(0);
        tabGroup.setVisibility(View.VISIBLE);
        findViewById(R.id.line1).setVisibility(View.VISIBLE);
    }

    public void moveToMine() {
        if (AppUtils.isLogin(this)) {
            Intent intent = new Intent(HomeActivity.this, UserDisplayActivity.class);
            intent.putExtra("uid", SNApplication.userInfo.getUserInfo().getUid() + "");
            intent.putExtra("uName", SNApplication.userInfo.getUserInfo().getUserName());
            intent.putExtra("uImage", SNApplication.userInfo.getUserInfo().getUserInfo().getHeadImage());
            startActivity(intent);
        }
    }
}