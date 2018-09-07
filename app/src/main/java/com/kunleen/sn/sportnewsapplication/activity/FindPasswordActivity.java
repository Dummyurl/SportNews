package com.kunleen.sn.sportnewsapplication.activity;

import android.os.Bundle;

import com.kunleen.sn.sportnewsapplication.R;
import com.kunleen.sn.sportnewsapplication.base.BaseActivity;
import com.kunleen.sn.sportnewsapplication.custom.TitleBarView;
import com.kunleen.sn.sportnewsapplication.utils.DevicesUtils;

public class FindPasswordActivity extends BaseActivity {
    TitleBarView titleBarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_password);
        DevicesUtils.setWindowStatusBarColor(this, R.color.white);//设置状态栏颜色
        initTitlebar();
    }

    private void initTitlebar() {
        titleBarView = findViewById(R.id.titlebar);
        titleBarView.setTitle("修改密码");
        titleBarView.setActivity(this);
    }
}
