package com.kunleen.sn.sportnewsapplication.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebSettings;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.kunleen.sn.sportnewsapplication.R;
import com.kunleen.sn.sportnewsapplication.app.SNApplication;
import com.kunleen.sn.sportnewsapplication.base.BaseActivity;
import com.kunleen.sn.sportnewsapplication.custom.SharePopupWindow;
import com.kunleen.sn.sportnewsapplication.custom.TitleBarView;
import com.kunleen.sn.sportnewsapplication.login.WechatLoginHelper;
//import com.kunleen.sn.sportnewsapplication.login.WeiboLoginHelper;
import com.kunleen.sn.sportnewsapplication.utils.AppUtils;
import com.kunleen.sn.sportnewsapplication.utils.DevicesUtils;
import com.kunleen.sn.sportnewsapplication.utils.FileUtils;
import com.kunleen.sn.sportnewsapplication.utils.GlideCacheUtil;
import com.kunleen.sn.sportnewsapplication.utils.ShareUtils;
import com.kunleen.sn.sportnewsapplication.utils.ToastUtils;
import com.kunleen.sn.sportnewsapplication.utils.Utils;
import com.sina.weibo.sdk.api.TextObject;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
//import com.sina.weibo.sdk.api.share.BaseResponse;
//import com.sina.weibo.sdk.api.share.IWeiboHandler;
//import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
//import com.sina.weibo.sdk.api.share.SendMultiMessageToWeiboRequest;
//import com.sina.weibo.sdk.api.share.WeiboShareSDK;
import com.sina.weibo.sdk.constant.WBConstants;
import com.sina.weibo.sdk.share.WbShareCallback;
import com.sina.weibo.sdk.share.WbShareHandler;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

public class SettingActivity extends BaseActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener, WbShareCallback {
    public static final String TAG = "SettingActivity_LOG";
    TitleBarView titleBar;
    View rl_share, rl_clear_cache, rl_font;
    SharePopupWindow mPopwindow;
    CheckBox cb_set_notify, cb_set_imgload;
    TextView tv_font;
    private WbShareHandler shareHandler;
//    private IWeiboShareAPI weiboAPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        initView();
        getCacheSize();
        bindActivity();
        initWindow(this);
    }

    private void getCacheSize() {
        ((TextView) findViewById(R.id.tv_cachesize)).setText(GlideCacheUtil.getInstance().getCacheSize(this));
    }

    private void initView() {
        titleBar = findViewById(R.id.titlebar);
        titleBar.setPadding(0, SNApplication.statusBar, 0, 0);
        titleBar.setBackgroundColor(Color.WHITE);
        titleBar.setTitle("设置");
        titleBar.hideRightButton(true);
        titleBar.setActivity(this);
        rl_share = findViewById(R.id.rl_set_share);
        rl_clear_cache = findViewById(R.id.rl_clear_cache);
        rl_clear_cache.setOnClickListener(this);
        rl_share.setOnClickListener(this);
        cb_set_notify = findViewById(R.id.cb_set_notify);
        cb_set_imgload = findViewById(R.id.cb_set_imgload);
        cb_set_notify.setChecked(SNApplication.userInfo.isNofifyMode());
        cb_set_imgload.setChecked(SNApplication.userInfo.isWifiLoadImg());
        cb_set_notify.setOnCheckedChangeListener(this);
        cb_set_imgload.setOnCheckedChangeListener(this);
        rl_font = findViewById(R.id.rl_font);
        rl_font.setOnClickListener(this);
        tv_font = findViewById(R.id.tv_font);
        switch (SNApplication.userInfo.getFontMode()) {
            case 0: {
                tv_font.setText("小");
                break;
            }
            case 1: {
                tv_font.setText("中");
                break;
            }
            case 2: {
                tv_font.setText("大");
                break;
            }
            case 3: {
                tv_font.setText("超大");
                break;
            }
        }
    }

    private View.OnClickListener itemsOnClick = v -> {
        switch (v.getId()) {
            case R.id.ll_weichat: {
                WechatShare(false);
                break;
            }
            case R.id.ll_wechatf: {
                WechatShare(true);
                break;
            }
            case R.id.ll_weibo: {
                if (AppUtils.isWeiboInstalled(SettingActivity.this)) {
                    sendMultiMessage();
                } else {
                    ToastUtils.showToast("请先安装微博客户端！");
                }
                break;
            }
            case R.id.ll_qq: {
                ToastUtils.showToast("QQ分享功能暂未开放！");
                break;
            }
        }
        mPopwindow.dismiss();
    };

    private void WechatShare(boolean isFriend) {
        WechatLoginHelper.weixinAPI = WXAPIFactory.createWXAPI(SettingActivity.this, null);
        WechatLoginHelper.weixinAPI.registerApp(WechatLoginHelper.APP_ID);
        if (!WechatLoginHelper.weixinAPI.isWXAppInstalled()) {
            Toast.makeText(SettingActivity.this, "您还未安装微信客户端，无法进行微信分享！",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = "https://www.baidu.com";
        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = "爱球君";
        msg.description = "爱球君爱球君爱球君爱球君爱球君爱球君爱球君爱球君爱球君爱球君";
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        msg.thumbData = FileUtils.getBitmapByte(bitmap);
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("webpage");
        req.message = msg;
        if (isFriend) {
            req.scene = SendMessageToWX.Req.WXSceneSession;
        } else {
            req.scene = SendMessageToWX.Req.WXSceneTimeline;
        }
        WechatLoginHelper.weixinAPI.sendReq(req);
    }

    private void sendMultiMessage() {
        shareHandler = new WbShareHandler(this);
        shareHandler.registerApp();
//        weiboAPI = WeiboShareSDK.createWeiboAPI(SettingActivity.this, WeiboLoginHelper.APP_KEY);
//        weiboAPI.registerApp();
        TextObject textObject = new TextObject();
        textObject.text = "爱球君爱球https://www.baidu.com";
        WeiboMultiMessage weiboMessage = new WeiboMultiMessage();//初始化微博的分享消息
        weiboMessage.textObject = textObject;
        shareHandler.shareMessage(weiboMessage, false);
//        SendMultiMessageToWeiboRequest request = new SendMultiMessageToWeiboRequest();
//        request.transaction = String.valueOf(System.currentTimeMillis());
//        request.multiMessage = weiboMessage;
//        weiboAPI.sendRequest(this, request);//发送请求消息到微博，唤起微博分享界面
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_set_share: {
                mPopwindow = new SharePopupWindow(SettingActivity.this, itemsOnClick);
                mPopwindow.showAtLocation(v,
                        Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                break;
            }
            case R.id.rl_clear_cache: {
                if (!((TextView) findViewById(R.id.tv_cachesize)).getText().toString().equals("0.0Byte")) {
                    GlideCacheUtil.getInstance().clearImageAllCache(SettingActivity.this);
                    ((TextView) findViewById(R.id.tv_cachesize)).setText("0.0Byte");
                    ToastUtils.showToast("缓存清理完毕！");
                }
                break;
            }
            case R.id.rl_font: {
                showFontDialog();
                break;
            }
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.cb_set_notify: {
                if (isChecked) {
                    SNApplication.userInfo.setNofifyMode(true);
                } else {
                    SNApplication.userInfo.setNofifyMode(false);
                }
                break;
            }
            case R.id.cb_set_imgload: {
                if (isChecked) {
                    SNApplication.userInfo.setWifiLoadImg(true);
                } else {
                    SNApplication.userInfo.setWifiLoadImg(false);
                }
                break;
            }
        }
    }

    private void showFontDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        View v = inflater.inflate(R.layout.layout_font, null);
        final Dialog dialog = builder.create();
        dialog.show();
        dialog.getWindow().setContentView(v);//自定义布局应该在这里添加，要在dialog.show()的后面
        dialog.getWindow().setGravity(Gravity.CENTER);//可以设置显示的位置
        RadioGroup rg = v.findViewById(R.id.rg_font);
        View v_cancel = v.findViewById(R.id.tv_cancel);
        v_cancel.setOnClickListener(v1 -> dialog.dismiss());
        switch (SNApplication.userInfo.getFontMode()) {
            case 0: {
                rg.check(R.id.rb_fonts);
                break;
            }
            case 1: {
                rg.check(R.id.rb_fontm);
                break;
            }
            case 2: {
                rg.check(R.id.rb_fontl);
                break;
            }
            case 3: {
                rg.check(R.id.rb_fontxl);
                break;
            }
        }
        rg.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.rb_fonts: {
                    SNApplication.userInfo.setFontMode(0);
                    tv_font.setText("小");
                    break;
                }
                case R.id.rb_fontm: {
                    SNApplication.userInfo.setFontMode(1);
                    tv_font.setText("中");
                    break;
                }
                case R.id.rb_fontl: {
                    SNApplication.userInfo.setFontMode(2);
                    tv_font.setText("大");
                    break;
                }
                case R.id.rb_fontxl: {
                    SNApplication.userInfo.setFontMode(3);
                    tv_font.setText("超大");
                    break;
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ShareUtils.putString("userInfo", new Gson().toJson(SNApplication.userInfo));
    }

    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        shareHandler.doResultIntent(intent, this);
    }

    @Override
    public void onWbShareSuccess() {
        ToastUtils.showToast("分享成功");
    }

    @Override
    public void onWbShareCancel() {
        ToastUtils.showToast("分享取消");
    }

    @Override
    public void onWbShareFail() {
        ToastUtils.showToast("分享失败");
    }
}