package com.kunleen.sn.sportnewsapplication.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.kunleen.sn.sportnewsapplication.R;
import com.kunleen.sn.sportnewsapplication.activity.HomeActivity;
import com.kunleen.sn.sportnewsapplication.app.SNApplication;
import com.kunleen.sn.sportnewsapplication.base.BaseFragment;
import com.kunleen.sn.sportnewsapplication.custom.TitleBarView;
import com.kunleen.sn.sportnewsapplication.utils.DevicesUtils;
import com.kunleen.sn.sportnewsapplication.utils.StringUtils;


public class MallFragment extends BaseFragment {
    WebView webView;
    TitleBarView titlebar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_mall, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        webView = view.findViewById(R.id.web_game);
//        view.setPadding(0, SNApplication.statusBar, 0, 0);
        webView.setWebViewClient(new WebViewClient());
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        if (!StringUtils.isEmptyString(HomeActivity.Url_market)) {
            webView.loadUrl(HomeActivity.Url_market);
        } else {
            webView.loadUrl("http://119.90.40.96:8030/?cid=" + SNApplication.CPID);
        }
        titlebar = view.findViewById(R.id.titlebar);
        titlebar.setTitle("快彩票");
        titlebar.setClickCallBack(new TitleBarView.TitleClickCallBack() {
            @Override
            public void clickLeft() {
                ((HomeActivity) getActivity()).goToFirst();
            }

            @Override
            public void clickRight() {

            }
        });
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            DevicesUtils.setWindowStatusBarColor(getActivity(), R.color.white);//设置状态栏颜色
        }
    }
}
