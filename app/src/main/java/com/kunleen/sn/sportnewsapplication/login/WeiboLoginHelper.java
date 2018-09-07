package com.kunleen.sn.sportnewsapplication.login;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.kunleen.sn.sportnewsapplication.base.BaseActivity;
import com.kunleen.sn.sportnewsapplication.network.RetrofitClient;
import com.kunleen.sn.sportnewsapplication.network.bean.Respon_Weibo;
import com.kunleen.sn.sportnewsapplication.network.service.HttpService;
import com.kunleen.sn.sportnewsapplication.utils.ToastUtils;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WbConnectErrorMessage;
import com.sina.weibo.sdk.auth.sso.SsoHandler;

import java.util.HashMap;

import io.reactivex.Observable;

/**
 * 微博登录协助类
 * Created by lixingjun on 8/4/16.
 */
public class WeiboLoginHelper {
    private WeiboLoginCallBack weiboLoginCallBack;
    private Activity mContext;
    private SsoHandler mSsoHandler;
    private Oauth2AccessToken mAccessToken;

    public WeiboLoginHelper(Activity context, WeiboLoginCallBack weiboLoginCallBack) {
        this.weiboLoginCallBack = weiboLoginCallBack;
        mContext = context;
        mSsoHandler = new SsoHandler(mContext);
    }

    public void login() {
        mSsoHandler.authorize(new SelfWbAuthListener());
    }

    public SsoHandler getSsoHandler() {
        return mSsoHandler;
    }

    private void getWeiboUserInfo(String access_token, String uid) {
        HashMap<String, String> map = new HashMap<>();
        map.put("access_token", access_token);
        map.put("uid", uid);
        Observable<Respon_Weibo> respon_weiboObservable = RetrofitClient.getInstance().getService(HttpService.class).WeiBoInfo(map);
        ((BaseActivity) mContext).sendRequest(respon_weiboObservable, respon_weibo -> {
            if (weiboLoginCallBack != null) {
                weiboLoginCallBack.onSuccess(respon_weibo.getId() + "", respon_weibo.getName(), respon_weibo.getAvatar_large());
            }
        });
    }


    public interface WeiboLoginCallBack {
        void onSuccess(String id, String nickName, String headUrl);

        void onFailure();
    }

    private class SelfWbAuthListener implements com.sina.weibo.sdk.auth.WbAuthListener {
        @Override
        public void onSuccess(final Oauth2AccessToken token) {
            mContext.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mAccessToken = token;
                    if (mAccessToken.isSessionValid()) {
                        getWeiboUserInfo(token.getToken(), token.getUid());
                        com.sina.weibo.sdk.auth.AccessTokenKeeper.writeAccessToken(mContext, mAccessToken);

                    }
                }
            });
        }

        @Override
        public void cancel() {
            ToastUtils.showToast("取消登录");
        }

        @Override
        public void onFailure(WbConnectErrorMessage errorMessage) {
            ToastUtils.showToast("登录异常" + errorMessage.getErrorMessage());
            weiboLoginCallBack.onFailure();
        }
    }

//    public class LoginBroadcastReceiver extends BroadcastReceiver {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            if (intent.getAction().equals("com.kunleen.wechat.login")) {
//                String code = intent.getStringExtra("code");
//                WechatLoginHelper.getInstance().getToken(code);
//            }
//        }
//    }
}
