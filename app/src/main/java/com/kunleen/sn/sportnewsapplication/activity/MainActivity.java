package com.kunleen.sn.sportnewsapplication.activity;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.kunleen.sn.sportnewsapplication.R;
import com.kunleen.sn.sportnewsapplication.app.SNApplication;
import com.kunleen.sn.sportnewsapplication.base.BaseActivity;
import com.kunleen.sn.sportnewsapplication.network.RetrofitClient;
import com.kunleen.sn.sportnewsapplication.network.bean.ReqAdList;
import com.kunleen.sn.sportnewsapplication.network.bean.ReqEmpty;
import com.kunleen.sn.sportnewsapplication.network.bean.ReqReleaseId;
import com.kunleen.sn.sportnewsapplication.network.bean.Response_Carouse;
import com.kunleen.sn.sportnewsapplication.network.bean.Response_FirstChannel;
import com.kunleen.sn.sportnewsapplication.network.bean.TRequest;
import com.kunleen.sn.sportnewsapplication.network.bean.TResponse_NewsList;
import com.kunleen.sn.sportnewsapplication.network.service.HttpService;
import com.kunleen.sn.sportnewsapplication.rx.RxHelper;
import com.kunleen.sn.sportnewsapplication.utils.DevicesUtils;
import com.kunleen.sn.sportnewsapplication.utils.JsonUtils;
import com.kunleen.sn.sportnewsapplication.utils.ShareUtils;
import com.kunleen.sn.sportnewsapplication.utils.ToastUtils;
import com.orhanobut.logger.Logger;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class MainActivity extends BaseActivity implements View.OnClickListener {
    public static String TAG = "MainActivity_Log";
    private int isFirst;//判断是否是第一次安装应用
    private Button btn_skip;
    private ImageView iv_splash;
    private int mTime = 3;
    private boolean isJump = false;
    private int successCount = 0;
    private boolean load;
    private View rl_splash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);
        super.onCreate(savedInstanceState);
        requestPermissions();
        rl_splash = findViewById(R.id.rl_splash);
        isFirst = ShareUtils.getInt("isFirst");//去本地读取数据来
        btn_skip = findViewById(R.id.btn_splash_skip);
        iv_splash = findViewById(R.id.iv_splash);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        if (DevicesUtils.isNetworkAvailable(this)) {
            getChannel();
            getTopNews();
            getAdList();
            getSplash();
        } else {
            rl_splash.setVisibility(View.VISIBLE);
            String channel = ShareUtils.getString("channel");
            String splash = ShareUtils.getString("splash");
            if (channel != null && splash != null) {
                SNApplication.channels = JsonUtils.getJsonArray(channel, Response_FirstChannel.Rows.class);
                SNApplication.carousel = new Gson().fromJson(splash, Response_Carouse.class);
                setSplash();
                jumpToHomePage();
            } else {
                Toast.makeText(this, "当前网络状态异常，应用无法正常使用，请检查网络状态后再使用！", Toast.LENGTH_SHORT).show();
//                finish();
            }
        }
        btn_skip.setOnClickListener(this);
    }

    private void getSplash() {
        TRequest<ReqReleaseId> req = new TRequest<>();
        ReqReleaseId carouse = new ReqReleaseId();
        carouse.setReleaseId(SNApplication.CPID);
        req.setParam(carouse, "1120", "1");
        Observable<Response_Carouse> ysy = RetrofitClient.getInstance().getService(HttpService.class).Carouse(req);
        sendRequest(ysy, tRespon_carousel -> {
            Logger.t("networks").d("car:" + new Gson().toJson(tRespon_carousel));
            if (tRespon_carousel.getSignCoce() == 1) {
                List<Response_Carouse.Carousel> carousel = tRespon_carousel.getCarousel();
                for (int i = carousel.size() - 1; i >= 0; i--) {
                    if (carousel.get(i).getCarouselType() == 1) {
                        carousel.remove(i);
                    }
                }
                tRespon_carousel.setCarousel(carousel);
            }
            SNApplication.carousel = tRespon_carousel;
            load = false;
            setSplash();
            if (!load) {
                //没有开屏图，加载默认开屏
                rl_splash.setVisibility(View.VISIBLE);
            }
        }, throwable -> {
            Logger.t("networks").d("car:" + throwable.getMessage());
            ToastUtils.showToast(throwable.getMessage());
            String splash = ShareUtils.getString("splash");
            if (splash != null) {
                SNApplication.carousel = new Gson().fromJson(splash, Response_Carouse.class);
                setSplash();
            } else {
            }
            successCount++;
            jumpToHome();
        });
    }

    public void jumpToHome() {
        if (successCount == 3) {
            jumpToHomePage();
        }
    }

    public void setSplash() {
        if (SNApplication.carousel.getCarousel() != null) {
            for (int i = 0; i < SNApplication.carousel.getCarousel().size(); i++) {
                if (SNApplication.carousel.getCarousel().get(i).getCarouselId() == 1) {
                    Glide.with(MainActivity.this).load(SNApplication.carousel.getCarousel().get(i).getCarouselImage()).into(iv_splash);
                    rl_splash.setVisibility(View.VISIBLE);
                    String url = SNApplication.carousel.getCarousel().get(i).getCarouselValue();
                    iv_splash.setOnClickListener(v -> {
                        Intent intent = new Intent();
                        intent.setAction("android.intent.action.VIEW");
                        Uri content_url = Uri.parse(url);
                        intent.setData(content_url);
                        startActivity(intent);
                    });
                    load = true;
                }
            }
            ShareUtils.putString("splash", new Gson().toJson(SNApplication.carousel));
        }
    }

    private void requestPermissions() {
        RxPermissions rxPermissions = new RxPermissions(MainActivity.this);
        rxPermissions.request(
                Manifest.permission.CAMERA,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.INTERNET,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS,
                Manifest.permission.WAKE_LOCK
//                Manifest.permission.READ_EXTERNAL_STORAGE,
//                Manifest.permission.ACCESS_COARSE_LOCATION
        ).subscribe(granted -> {
            if (!granted) {
//                ToastUtils.showToast("App未能获取全部需要的相关权限，部分功能可能不能正常使用.");
            }
        });
    }

    private void jumpToHomePage() {
        Observable.interval(1, TimeUnit.SECONDS).take(3).map(aLong -> mTime - aLong).compose(RxHelper.<Long>rxSchedulerHelper()).subscribe(new Observer<Long>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Long value) {
                if (btn_skip != null) {
                    btn_skip.setText("跳过(" + value + ")");
                }
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {
                if (!isJump) {
                    startActivity(new Intent(MainActivity.this, HomeActivity.class));
                    finish();
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                }
            }
        });
    }


    @Override
    public void onClick(View v) {
        isJump = true;
        Intent intent = new Intent(this, HomeActivity.class);//直接进入应用
        startActivity(intent);
        finish();
    }

    /**
     * 本版本未实现引导页面所以未设置
     */
    public void StartSetting() {
        if (isFirst == -1) {
            isFirst = 0;
            ShareUtils.putBoolean("isFirst", false);//第一次进入应用后写入共享参数
            //Intent intent = new Intent(this, GuideActivity.class);//进入guide导航界面
            // startActivity(intent);
        } else {
            Intent intent = new Intent(this, HomeActivity.class);//直接进入应用
            startActivity(intent);
        }
        this.finish();
    }

    @Override
    public void onBackPressed() {
        isJump = true;
        Intent intent = new Intent(this, HomeActivity.class);//直接进入应用
        startActivity(intent);
        finish();
    }

    public void getChannel() {
        SNApplication.channels.clear();
        Gson gson = new Gson();
        TRequest<ReqEmpty> req = new TRequest<>();
        req.setParam(new ReqEmpty(), "1132", "1");
        sendRequest(RetrofitClient.getInstance().getService(HttpService.class).NewFristPage(req),
                response_firstChannel -> {
                    SNApplication.channels = response_firstChannel.getRows();
                    ShareUtils.putString("channel", gson.toJson(SNApplication.channels));
                    successCount++;
                    jumpToHome();
                }, throwable -> {
                    String channel = ShareUtils.getString("channel");
                    if (channel != null) {
                        SNApplication.channels = JsonUtils.getJsonArray(channel, Response_FirstChannel.Rows.class);
                    } else {
                        ToastUtils.showToast("网络连接异常");
                        finish();
                    }
                    successCount++;
                    jumpToHome();
                });
    }

    public void getTopNews() {
        SNApplication.top_news_list.clear();
        TRequest<ReqEmpty> req = new TRequest<>();
        req.setParam(new ReqEmpty(), "1113", "1");
        Observable<TResponse_NewsList> ysy = RetrofitClient.getInstance().getService(HttpService.class).topNewsList(req);
        sendRequest(ysy, tResponse_newsList -> {
            Gson gson = new Gson();
            Log.i(TAG, "top:" + gson.toJson(tResponse_newsList));
            SNApplication.top_news_list.addAll(tResponse_newsList.getRows());
            successCount++;
            jumpToHome();
        }, throwable -> {
            Log.i("networks", 3 + throwable.toString());
            ToastUtils.showToast(throwable.getMessage());
            successCount++;
            jumpToHome();
        });
    }

    public void getAdList() {
        SNApplication.ad_list.clear();
        TRequest<ReqAdList> req = new TRequest<>();
        ReqAdList reqAdList = new ReqAdList();
        reqAdList.setPage(1);
        reqAdList.setRows(20);
        req.setParam(reqAdList, "1114", "1");
        Observable<TResponse_NewsList> ysy = RetrofitClient.getInstance().getService(HttpService.class).adList(req);
        sendRequest(ysy, tResponse -> {
            SNApplication.ad_list.addAll(tResponse.getPageInfo().getRows());
            successCount++;
            jumpToHome();
        }, throwable -> {
            Log.i("networks", 4 + throwable.toString());
            successCount++;
            jumpToHome();
        });
    }
}
