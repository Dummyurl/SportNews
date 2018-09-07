package com.kunleen.sn.sportnewsapplication.login;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.kunleen.sn.sportnewsapplication.base.BaseActivity;
import com.kunleen.sn.sportnewsapplication.network.RetrofitClient;
import com.kunleen.sn.sportnewsapplication.network.bean.Respon_Wechat;
import com.kunleen.sn.sportnewsapplication.network.bean.Respon_WechatUserInfo;
import com.kunleen.sn.sportnewsapplication.network.service.HttpService;
import com.kunleen.sn.sportnewsapplication.utils.AppUtils;
import com.kunleen.sn.sportnewsapplication.utils.StringUtils;
import com.kunleen.sn.sportnewsapplication.utils.ToastUtils;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import java.util.HashMap;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;

/**
 * Created by lixingjun on 7/31/16.
 */
public class WechatLoginHelper {
    // APP_ID 替换为你的应用从官方网站申请到的合法appId
    public static String APP_ID = "wx30c7d03b403e358a";
    public static String SECRET_KEY = "dc4ddb747ac4d04bee08c8f0761bac81";
    public static IWXAPI weixinAPI = null;
    public static String openid = null;
    public static String nickName = null;
    public static int sexType = 1;//1=男
    public static String headImgUrl = null;
    public static WechatLoginCallBack loginCallBack = null;

    private WechatLoginHelper() {
    }


    public static void doLogin(Context context, WechatLoginCallBack callBack) {
        weixinAPI = WXAPIFactory.createWXAPI(context, APP_ID, true);
        weixinAPI.registerApp(APP_ID);
        if (isWXAppInstalledAndSupported()) {
            loginCallBack = callBack;
            SendAuth.Req req = new SendAuth.Req();
            req.scope = "snsapi_userinfo";
            req.state = "carjob_wx_login";
            weixinAPI.sendReq(req);//执行完毕这句话之后，会在WXEntryActivity回调}
        } else {
            ToastUtils.showToast("未检测到微信应用，请使用其他登录方式！");
            AppUtils.hideLoadingDialog();
        }
//        helper = null;
    }

    private static boolean isWXAppInstalledAndSupported() {
        boolean sIsWXAppInstalledAndSupported = weixinAPI.isWXAppInstalled()
                && weixinAPI.isWXAppSupportAPI();
        return sIsWXAppInstalledAndSupported;
    }

//    //这个方法会取得accesstoken  和openID
//    public void getToken(String code) {
//        HashMap<String, String> map = new HashMap<>();
//        map.put("appid", WechatLoginHelper.APP_ID);
//        map.put("secret", SECRET_KEY);
//        map.put("code", code);
//        map.put("grant_type", "authorization_code");
//        Observable<Respon_Wechat> respon_weiboObservable = RetrofitClient.getInstance().getService(HttpService.class).WeChatInfo(map);
//        activity.sendRequest(respon_weiboObservable, respon_wechat -> {
//            WechatLoginHelper.openid = respon_wechat.getOpenid();
//            String _token = respon_wechat.getAccess_token();
//            getUserInfo(_token, WechatLoginHelper.openid);
//            destory();
//        });
//    }

//    //获取到token和openID之后，调用此接口得到身份信息
//    private void getUserInfo(String token, String openID) {
////        String url = "https://api.weixin.qq.com/sns/userinfo?access_token=" +token+"&openid=" +openID;
//        HashMap<String, String> map = new HashMap<>();
//        map.put("access_token", token);
//        map.put("openid", openID);
//        map.put("lang", "zh_CN");
//        Observable<Respon_WechatUserInfo> respon_weiboObservable = RetrofitClient.getInstance().getService(HttpService.class).WeChatUserInfo(map);
//        activity.sendRequest(respon_weiboObservable, respon_wechatUserInfo -> {
//            WechatLoginHelper.nickName = respon_wechatUserInfo.getNickname();
//            WechatLoginHelper.sexType = respon_wechatUserInfo.getSex();
//            WechatLoginHelper.headImgUrl = respon_wechatUserInfo.getHeadimgurl();
//            if (loginCallBack != null) {
//                loginCallBack.onSuccess();
//            }
//        });
//    }

    public interface WechatLoginCallBack {
        void onSuccess();

        void onFailure();
    }


}
