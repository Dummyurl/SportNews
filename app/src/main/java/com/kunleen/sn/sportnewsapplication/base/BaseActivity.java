package com.kunleen.sn.sportnewsapplication.base;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.kunleen.sn.sportnewsapplication.R;
import com.kunleen.sn.sportnewsapplication.custom.SlidingLayout;
import com.kunleen.sn.sportnewsapplication.event.Event;
import com.kunleen.sn.sportnewsapplication.network.bean.TResponse;
import com.kunleen.sn.sportnewsapplication.rx.RxHelper;
import com.kunleen.sn.sportnewsapplication.utils.DevicesUtils;
import com.kunleen.sn.sportnewsapplication.utils.ToastUtils;
import com.kunleen.sn.sportnewsapplication.utils.Utils;
import com.umeng.analytics.MobclickAgent;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Created by xianglanzuo on 2018/1/2.
 */

public class BaseActivity extends FragmentActivity {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);

    }

    private RxHelper mSubscribeHelper;

    protected void cancelTask() {
        if (mSubscribeHelper != null) {
            mSubscribeHelper.disposeTask();
            mSubscribeHelper = null;
        }
    }

    protected void cancelEvent() {
        if (mSubscribeHelper != null) {
            mSubscribeHelper.disposeEvent();
            mSubscribeHelper = null;
        }
    }

    protected RxHelper getSubscribeHelper() {
        if (mSubscribeHelper == null) {
            mSubscribeHelper = new RxHelper();
        }
        return mSubscribeHelper;
    }


    public void initWindow(Activity activity) {
//        Utils.setStatusBar(this, false, false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Utils.setStatusBar(this, false, false);
        } else {
            DevicesUtils.setWindowStatusBarColor(activity, R.color.white);
        }
        Utils.setStatusTextColor(true, activity);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    public void initWindow(Activity activity, boolean black) {
//        Utils.setStatusBar(this, false, false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Utils.setStatusBar(this, false, false);
        } else {
            DevicesUtils.setWindowStatusBarColor(activity, R.color.white);
        }
        Utils.setStatusTextColor(black, activity);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    public void bindActivity() {
        SlidingLayout rootView = new SlidingLayout(this);
        rootView.bindActivity(this);
    }

    //
    public <T> void sendRequest(Observable<T> observable, Consumer<T> onSuccess) {
        getSubscribeHelper().execute(observable, onSuccess);
    }

    public <T extends TResponse> void sendRequest(Observable<T> observable, Consumer<T> onSuccess, Consumer<Throwable> onFailure) {
        getSubscribeHelper().execute(observable, onSuccess, onFailure == null ? this::defaultFailure : onFailure);
    }

    protected <E extends Event> Disposable subscribeEvent(
            Class<E> eventClass, final Consumer<? super E> onEvent) {
        return getSubscribeHelper().observeEvent(eventClass, onEvent, AndroidSchedulers.mainThread());
    }

    protected <E extends Event> Disposable subscribeEvent(Class<E> eventClass, final Consumer<? super E> onEvent, Scheduler scheduler) {
        return getSubscribeHelper().observeEvent(eventClass, onEvent, scheduler);
    }

    private void defaultFailure(Throwable throwable) {
        // TODO: 2018/1/2
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cancelTask();
        cancelEvent();
        MobclickAgent.onResume(this);
    }

    public void hideBottomUIMenu() {
        //隐藏虚拟按键，并且全屏
        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) { // lower api
            View v = this.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else if (Build.VERSION.SDK_INT >= 19) {
            //for new api versions.
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }
}
