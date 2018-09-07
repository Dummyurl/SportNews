package com.kunleen.sn.sportnewsapplication.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.kunleen.sn.sportnewsapplication.login.WechatLoginHelper;

/**
 * Created by ysy on 2018/3/12.
 */

public class LoginBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("com.kunleen.wechat.login")) {
            String code = intent.getStringExtra("code");
//            WechatLoginHelper.getInstance().getToken(code);
        }
    }
}
