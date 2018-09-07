package com.kunleen.sn.sportnewsapplication.base;

import android.support.v4.app.Fragment;

import com.kunleen.sn.sportnewsapplication.event.Event;
import com.kunleen.sn.sportnewsapplication.network.bean.TResponse;
import com.kunleen.sn.sportnewsapplication.rx.RxHelper;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

;

/**
 * Created by xianglanzuo on 2018/1/2.
 */

public class BaseFragment extends Fragment {

    private RxHelper mSubscribeHelper;

    protected void cancelTask() {
        if (mSubscribeHelper != null) {
            mSubscribeHelper.disposeTask();
        }
    }

    protected void cancelEvent() {
        if (mSubscribeHelper != null) {
            mSubscribeHelper.disposeEvent();
        }
    }

    protected RxHelper getSubscribeHelper() {
        if (mSubscribeHelper == null) {
            mSubscribeHelper = new RxHelper();
        }

        return mSubscribeHelper;
    }

    //    protected <T> void sendRequest(Observable<TResponse<T>> observable, Consumer<T> onSuccess) {
//        getSubscribeHelper().execute(observable, onSuccess, null);
//    }
//
//    public <T> void sendRequest(Observable<TResponse<T>> observable, Consumer<T> onSuccess, Consumer<Throwable> onFailure) {
//        getSubscribeHelper().execute(observable, onSuccess, onFailure == null ? this::defaultFailure : onFailure);
//    }
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
    public void onDestroy() {
        super.onDestroy();
        cancelTask();
        cancelEvent();
    }

}
