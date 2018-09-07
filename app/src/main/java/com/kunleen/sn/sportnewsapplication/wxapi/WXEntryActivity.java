package com.kunleen.sn.sportnewsapplication.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;


import com.kunleen.sn.sportnewsapplication.app.SNApplication;
import com.kunleen.sn.sportnewsapplication.base.BaseActivity;
import com.kunleen.sn.sportnewsapplication.login.WechatLoginHelper;
import com.kunleen.sn.sportnewsapplication.utils.ToastUtils;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;

import static com.kunleen.sn.sportnewsapplication.app.SNApplication.WechatLogin;


public class WXEntryActivity extends BaseActivity implements IWXAPIEventHandler {

    private Bundle bundle;

    //这个实体类是我自定义的实体类，用来保存第三方的数据的实体类
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//		AppManager. getAppManager().addActivity(WXEntryActivity. this);
        WechatLoginHelper.weixinAPI.handleIntent(getIntent(), this);  //必须调用此句话
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        WechatLoginHelper.weixinAPI.handleIntent(intent, this);//必须调用此句话
    }

    @Override
    public void onReq(BaseReq req) {
        System.out.println();
    }


    /**
     * Title: onResp
     * <p/>
     * API：https://open.weixin.qq.com/ cgi- bin/showdocument ?action=dir_list&t=resource/res_list&verify=1&id=open1419317853 &lang=zh_CN
     * Description:在此处得到Code之后调用https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code
     * 获取到token和openID。之后再调用https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID 获取用户个人信息
     *
     * @param arg0
     * @see
     */
    @Override
    public void onResp(BaseResp arg0) {
        bundle = getIntent().getExtras();
        SendAuth.Resp resp = new SendAuth.Resp(bundle);
        System.out.println("***************************************************************=" + resp.errCode);
        //获取到code之后，需要调用接口获取到access_token
        Log.i("wechat---", resp.getType() + "");
        if (resp.errCode == BaseResp.ErrCode.ERR_OK) {
            if (WechatLogin) {
                SNApplication.WechatLogin = false;
                String code = resp.code;
                Intent intent = new Intent();
                intent.setAction("com.kunleen.wechat.login");
                intent.putExtra("code", code);
                sendBroadcast(intent);
                finish();
            } else {
                ToastUtils.showToast("分享成功，谢谢您的支持！");
                finish();
            }
        } else {
            finish();
        }
    }
}