package com.kunleen.sn.sportnewsapplication.app;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.multidex.MultiDexApplication;
import android.support.v7.app.AppCompatDelegate;

import com.google.gson.Gson;
import com.kunleen.sn.sportnewsapplication.AppConfig;
import com.kunleen.sn.sportnewsapplication.R;
import com.kunleen.sn.sportnewsapplication.custom.LoadingDialog;
import com.kunleen.sn.sportnewsapplication.network.bean.Channel;
import com.kunleen.sn.sportnewsapplication.network.bean.Response_Carouse;
import com.kunleen.sn.sportnewsapplication.network.bean.Response_FirstChannel;
import com.kunleen.sn.sportnewsapplication.network.bean.TRespon_Carousel;
import com.kunleen.sn.sportnewsapplication.network.bean.TResponse_NewsList;
import com.kunleen.sn.sportnewsapplication.network.bean.UserInfo;
import com.kunleen.sn.sportnewsapplication.utils.AppUtils;
import com.kunleen.sn.sportnewsapplication.utils.DevicesUtils;
import com.kunleen.sn.sportnewsapplication.utils.DynamicTimeFormat;
import com.kunleen.sn.sportnewsapplication.utils.ShareUtils;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.FormatStrategy;
import com.orhanobut.logger.LogcatLogStrategy;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.PrettyFormatStrategy;

import com.sina.weibo.sdk.WbSdk;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.squareup.leakcanary.LeakCanary;
import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ysy on 2018/1/11.
 */

public class SNApplication extends MultiDexApplication {
    private static final String TAG = "SNApplication";
    protected static Context context;
    protected static Handler handler;
    protected static int mainThreadId;
    public static LoadingDialog loadingDialog;
    /**
     * UserInFo
     */
    public static UserInfo userInfo;
    /**
     * 用户顶部频道
     */
    public static List<Response_FirstChannel.Rows> channels = new ArrayList<>();
    /**
     * 置顶新闻
     */
    public static ArrayList<TResponse_NewsList.row> top_news_list = new ArrayList();
    /**
     * 广告列表
     */
    public static ArrayList<TResponse_NewsList.row> ad_list = new ArrayList();
    /**
     * 轮播图
     */
    public static Response_Carouse carousel;
    /**
     * WIFI状态
     */
    public static boolean WIFIState = false;
    /**
     * 微信登录
     */
    public static boolean WechatLogin = false;
    /**
     * 友盟分享AppKey
     */
    public static final String YOUMENG_KEY = "5ae3d81da40fa37625000182";

    /**
     * 状态栏高度
     */
    public static int statusBar;
    /**
     * 键盘弹出高度
     */
    public static int supportSoftInputHeight = 0;
    /**
     * 圈子是否能刷新
     */
    public static boolean ForumRefrsh = false;
    /**
     * 渠道ID
     */
    public static String CPID;

    public void getUserInfo() {
        String userJson = ShareUtils.getString("userInfo");
        if (userJson != null) {
            userInfo = new Gson().fromJson(userJson, UserInfo.class);
        } else {
            userInfo = new UserInfo();
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        handler = new Handler();
        mainThreadId = android.os.Process.myTid();
        ShareUtils.init(this);
        initRelease();
        initStetho();
        getUserInfo();
        getCPID();
        setMobclickAgent();
        WIFIState = AppUtils.isWifiConnected(this);
        statusBar = DevicesUtils.getStatusBar(this);
        WbSdk.install(this, new AuthInfo(this, AppConfig.APP_KEY, AppConfig.REDIRECT_URL, AppConfig.SCOPE));
    }

    private void initStetho() {
//        Stetho.initialize(
//                Stetho.newInitializerBuilder(this)
//                        .enableDumpapp(
//                                Stetho.defaultDumperPluginsProvider(this))
//                        .enableWebKitInspector(
//                                Stetho.defaultInspectorModulesProvider(this))
//                        .build());build
        FormatStrategy formatStrategy = PrettyFormatStrategy.newBuilder()
                .showThreadInfo(false)  // (Optional) Whether to show thread info or not. Default true
                .methodCount(0)         // (Optional) How many method line to show. Default 2
                .methodOffset(7)        // (Optional) Hides internal method calls up to offset. Default 5
                .logStrategy(new LogcatLogStrategy()) // (Optional) Changes the log strategy to print out. Default LogCat
                .tag("OkHttp")   // (Optional) Global tag for every log. Default PRETTY_LOGGER
                .build();

        Logger.addLogAdapter(new AndroidLogAdapter(formatStrategy));

    }

    private void initRelease() {
        if (!(AppConfig.ONLINE_SERVER == AppConfig.RELEASE)) {
            if (LeakCanary.isInAnalyzerProcess(this)) {
                return;
            }
            LeakCanary.install(this);
        }
    }

    //根据metaData信息获取渠道号
    private void getCPID() {
        try {
            ApplicationInfo appinfo = this.getPackageManager().getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA);
            String str = appinfo.metaData.getString("UMENG_CHANNEL");
            CPID = str.substring(1, str.length());
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static Context getContext() {
        return context;
    }

    /**
     * 获取全局handler
     *
     * @return 全局handler
     */
    public static Handler getHandler() {
        return handler;
    }

    /**
     * 获取主线程id
     *
     * @return 主线程id
     */
    public static int getMainThreadId() {
        return mainThreadId;
    }

    /**
     * 友盟统计初始化
     */
    private void setMobclickAgent() {
        //设置统计类型，渠道号，和普通统计类型
        UMConfigure.init(getContext(), YOUMENG_KEY, CPID, UMConfigure.DEVICE_TYPE_PHONE, null);
        UMConfigure.setLogEnabled(false);
    }
}
