package com.kunleen.sn.sportnewsapplication.activity;


import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.kunleen.sn.sportnewsapplication.R;
import com.kunleen.sn.sportnewsapplication.app.SNApplication;
import com.kunleen.sn.sportnewsapplication.base.BaseActivity;
import com.kunleen.sn.sportnewsapplication.custom.TitleBarView;
import com.kunleen.sn.sportnewsapplication.utils.DevicesUtils;
import com.kunleen.sn.sportnewsapplication.utils.StringUtils;
import com.kunleen.sn.sportnewsapplication.utils.ToastUtils;
import com.kunleen.sn.sportnewsapplication.utils.Utils;


/**
 * Created by lixingjun on 8/25/16.
 */
public class WebView1Activity extends BaseActivity {
    private WebView mWebView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview1);
        mWebView = findViewById(R.id.webview);
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // 如下方案可在非微信内部WebView的H5页面中调出微信支付
                if (!url.contains("http")) {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(url));
                    startActivity(intent);
                    return true;
                } else if (parseScheme(url)) {
                    try {
                        Intent intent;
                        intent = Intent.parseUri(url,
                                Intent.URI_INTENT_SCHEME);
                        intent.addCategory("android.intent.category.BROWSABLE");
                        intent.setComponent(null);
                        // intent.setSelector(null);
                        startActivity(intent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    view.loadUrl(url);
                }
                return true;
            }
        });
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        Intent intent = this.getIntent();
        String _title = intent.getStringExtra("title");
        String _url = intent.getStringExtra("url");
//        _url = " https://yfsdfuz.cmbc.com.cn/wxp/yfsd/page/service-enty.html?v=1.3.1";
        initTitleBar(_title);
        bindActivity();
        initWindow(this);
        // Use remote resource
        if (!StringUtils.isEmptyString(_url)) {
            mWebView.loadUrl(_url);
        } else {
            ToastUtils.showToast("没有更多数据");
        }
    }

    public boolean parseScheme(String url) {
        if (url.contains("platformapi/startapp")) {
            return true;
        } else if ((Build.VERSION.SDK_INT > Build.VERSION_CODES.M)
                && (url.contains("platformapi") && url.contains("startapp"))) {
            return true;
        } else {
            return false;
        }
    }

    // Prevent the back-button from closing the app
    @Override
    public void onBackPressed() {
        if (mWebView.canGoBack()) {
            mWebView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void finish() {
        //设置返回的数据
        Intent intent = new Intent();
        intent.putExtra("pay_result", true);
        setResult(RESULT_OK, intent);
//        finish();
        super.finish();
    }

    private void initTitleBar(String titile) {
        TitleBarView bar = findViewById(R.id.titlebar);
        bar.setActivity(this);
        bar.setTitle(titile);
        bar.hideRightButton(true);
    }
}
