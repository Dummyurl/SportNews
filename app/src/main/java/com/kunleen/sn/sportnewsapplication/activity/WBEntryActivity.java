package com.kunleen.sn.sportnewsapplication.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.ThemedSpinnerAdapter;

//import com.kunleen.sn.sportnewsapplication.login.WeiboLoginHelper;
import com.kunleen.sn.sportnewsapplication.utils.ToastUtils;
//import com.sina.weibo.sdk.api.share.BaseResponse;
//import com.sina.weibo.sdk.api.share.IWeiboHandler;
//import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
//import com.sina.weibo.sdk.api.share.WeiboShareSDK;
import com.sina.weibo.sdk.constant.WBConstants;
import com.sina.weibo.sdk.share.WbShareCallback;

public class WBEntryActivity extends Activity implements WbShareCallback {
//        implements IWeiboHandler.Response {

    //sian微博分享
//    private IWeiboShareAPI mWeiboShareAPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        // 创建微博分享接口实例
//        mWeiboShareAPI = WeiboShareSDK.createWeiboAPI(this, WeiboLoginHelper.APP_KEY);
//        mWeiboShareAPI.registerApp();
//        mWeiboShareAPI.handleWeiboResponse(getIntent(), this);
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
//
//    /**
//     * @see {@link Activity#onNewIntent}
//     */
//    @Override
//    protected void onNewIntent(Intent intent) {
//        super.onNewIntent(intent);
//        setIntent(intent);
//        // 从当前应用唤起微博并进行分享后，返回到当前应用时，需要在此处调用该函数
//        // 来接收微博客户端返回的数据；执行成功，返回 true，并调用
//        // {@link IWeiboHandler.Response#onResponse}；失败返回 false，不调用上述回调
//        mWeiboShareAPI.handleWeiboResponse(intent, this);
//    }
//
//    @Override
//    public void onResponse(BaseResponse baseResponse) {
//        switch (baseResponse.errCode) {
//            case WBConstants.ErrorCode.ERR_OK:
//                ToastUtils.showToast("分享成功");
//                break;
//
//            case WBConstants.ErrorCode.ERR_CANCEL:
//                ToastUtils.showToast("分享取消");
//                break;
//
//            case WBConstants.ErrorCode.ERR_FAIL:
//                ToastUtils.showToast("分享失败");
//                break;
//        }
//        finish();
//    }


}