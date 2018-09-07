package com.kunleen.sn.sportnewsapplication.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.kunleen.sn.sportnewsapplication.R;
import com.kunleen.sn.sportnewsapplication.base.BaseActivity;
import com.kunleen.sn.sportnewsapplication.custom.SearchView.SearchView;
import com.kunleen.sn.sportnewsapplication.custom.SlidingLayout;
import com.kunleen.sn.sportnewsapplication.utils.DevicesUtils;
import com.kunleen.sn.sportnewsapplication.utils.ToastUtils;


public class SearchActivity extends BaseActivity {
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        DevicesUtils.setWindowStatusBarColor(this, R.color.white);//设置状态栏颜色
        initView();
        initWindow(this);
        if (enableSliding()) {
            SlidingLayout rootView = new SlidingLayout(this);
            rootView.bindActivity(this);
        }
    }

    private void initView() {
        searchView = findViewById(R.id.search_view);
        if (getIntent().getStringExtra("key").equals("news")) {
            searchView.setHint("搜索新闻");
        } else {
            searchView.setHint("搜索圈子");
        }
        searchView.setOnClickSearch(string -> {
            if (DevicesUtils.isNetworkAvailable(this)) {
                Intent intent = new Intent(SearchActivity.this, News_ListActivity.class);
                intent.putExtra("list_name", string);
                startActivity(intent);
            } else {
                ToastUtils.showToast("网络状况不佳，请连接网络后再试！");
            }
        });
        searchView.setListViewOnItemClickListener((parent, view, position, id) -> {
            TextView textView = view.findViewById(android.R.id.text1);
            String name = textView.getText().toString();
            Intent intent;
            if (getIntent().getStringExtra("key").equals("news")) {
                intent = new Intent(SearchActivity.this, News_ListActivity.class);
                intent.putExtra("list_name", name);
            } else {
                intent = new Intent(SearchActivity.this, TypeActivity.class);
                intent.putExtra("type", "search");
                intent.putExtra("keyword", name);
            }
            startActivity(intent);
        });
        // 5. 设置点击返回按键后的操作（通过回调接口）
        searchView.setOnClickBack(() -> finish());
    }

    protected boolean enableSliding() {
        return true;
    }
}
