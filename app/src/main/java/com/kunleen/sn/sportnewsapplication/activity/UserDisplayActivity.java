package com.kunleen.sn.sportnewsapplication.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.kunleen.sn.sportnewsapplication.AppConfig;
import com.kunleen.sn.sportnewsapplication.R;
import com.kunleen.sn.sportnewsapplication.adapter.UserPageAdapter;
import com.kunleen.sn.sportnewsapplication.app.SNApplication;
import com.kunleen.sn.sportnewsapplication.base.BaseActivity;
import com.kunleen.sn.sportnewsapplication.custom.CircleImageView;
import com.kunleen.sn.sportnewsapplication.login.WechatLoginHelper;
import com.kunleen.sn.sportnewsapplication.login.WeiboLoginHelper;
import com.kunleen.sn.sportnewsapplication.network.RetrofitClient;
import com.kunleen.sn.sportnewsapplication.network.bean.ReqUerIdFuserId;
import com.kunleen.sn.sportnewsapplication.network.bean.ReqUidFuid;
import com.kunleen.sn.sportnewsapplication.network.bean.Respon_Wechat;
import com.kunleen.sn.sportnewsapplication.network.bean.Respon_WechatUserInfo;
import com.kunleen.sn.sportnewsapplication.network.bean.Response_CheckFollow;
import com.kunleen.sn.sportnewsapplication.network.bean.TReqThirdLogin;
import com.kunleen.sn.sportnewsapplication.network.bean.TRequest;
import com.kunleen.sn.sportnewsapplication.network.bean.TResponse;
import com.kunleen.sn.sportnewsapplication.network.bean.TResponse_Login;
import com.kunleen.sn.sportnewsapplication.network.service.HttpService;
import com.kunleen.sn.sportnewsapplication.utils.AppUtils;
import com.kunleen.sn.sportnewsapplication.utils.GlideCacheUtil;
import com.kunleen.sn.sportnewsapplication.utils.ShareUtils;
import com.kunleen.sn.sportnewsapplication.utils.ToastUtils;
import com.kunleen.sn.sportnewsapplication.utils.Utils;

import java.util.HashMap;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;

public class UserDisplayActivity extends BaseActivity implements View.OnClickListener {
    View iv_setting, bt_changepass, rl_mine_head;
    CircleImageView iv_head;
    TextView tv_user_name, tv_bindph, tv_bindwechat, tv_bindqq, tv_bindweibo;
    private WeiboLoginHelper weiboLoginHelper;
    private String uid;
    private String uName;
    private String uImage;
    private ImageView iv_follow, iv_arrow, iv_arrow1;
    private ViewPager vp_user;
    private RadioGroup rg_user, rg_menu;
    private boolean isMySelf = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_display);
        uid = getIntent().getStringExtra("uid");
        if (SNApplication.userInfo.getUserInfo() != null && SNApplication.userInfo.getUserInfo().getUserInfo().getUid() == Integer.parseInt(uid)) {
            isMySelf = true;
        }
        uName = getIntent().getStringExtra("uName");
        uImage = getIntent().getStringExtra("uImage");
        bindActivity();
        initView();
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.kunleen.wechat.login");
        LoginBroadcastReceiver receiver = new LoginBroadcastReceiver();
        //注册广播接收
        registerReceiver(receiver, filter);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        Utils.setStatusTextColor(false, this);
    }

    private void initView() {
        iv_follow = findViewById(R.id.iv_follow);
        iv_follow.setOnClickListener(this);
        if (!isMySelf) {
            findViewById(R.id.sv_user).setVisibility(View.GONE);
            findViewById(R.id.iv_setting).setVisibility(View.GONE);
            findViewById(R.id.rg_menu).setVisibility(View.GONE);
            findViewById(R.id.ll_arrow).setVisibility(View.GONE);
            getFollow();
        } else {
            findViewById(R.id.ll_first_page).setVisibility(View.GONE);
            findViewById(R.id.rl_user).setOnClickListener(this);
            findViewById(R.id.iv_follow).setVisibility(View.GONE);
        }
        rl_mine_head = findViewById(R.id.rl_mine_head);
        rl_mine_head.setPadding(0, SNApplication.statusBar, 0, 0);
        iv_arrow = findViewById(R.id.iv_arrow);
        iv_arrow1 = findViewById(R.id.iv_arrow1);
        rg_user = findViewById(R.id.rg_user);
        findViewById(R.id.iv_back).setOnClickListener(this);
        vp_user = findViewById(R.id.vp_user);
        vp_user.setOffscreenPageLimit(4);
        vp_user.setOverScrollMode(ScrollView.OVER_SCROLL_NEVER);
        UserPageAdapter adapter = new UserPageAdapter(this, uid);
        vp_user.setAdapter(adapter);
        vp_user.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                rg_user.check(rg_user.getChildAt(position).getId());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        rg_user.setOnCheckedChangeListener((group, checkedId) -> {
                    for (int i = 0; i < group.getChildCount(); i++) {
                        if (group.getChildAt(i).getId() == checkedId) {
                            vp_user.setCurrentItem(i);
                        }
                    }
                }
        );
        adapter.setLoadReadyListener((position, Count) -> {
            switch (position) {
                case 1: {
                    ((TextView) findViewById(R.id.rb_bbs)).setText("帖子\n" + Count);
                    break;
                }
                case 2: {
                    ((TextView) findViewById(R.id.rb_reply)).setText("回复\n" + Count);
                    break;
                }
                case 3: {
                    ((TextView) findViewById(R.id.rb_follow)).setText("关注\n" + Count);
                    break;
                }
                case 4: {
                    ((TextView) findViewById(R.id.rb_fans)).setText("粉丝\n" + Count);
                    break;
                }
            }
        });
        tv_user_name = findViewById(R.id.tv_user_name);
        tv_user_name.setText(uName);
        GlideCacheUtil.LoadImage(UserDisplayActivity.this, findViewById(R.id.iv_user_incon), uImage, true);
        rg_menu = findViewById(R.id.rg_menu);
        rg_menu.setOnCheckedChangeListener((group, checkedId) -> {
            for (int i = 0; i < group.getChildCount(); i++) {
                if (group.getChildAt(i).getId() == checkedId) {
                    if (i == 0) {
                        findViewById(R.id.sv_user).setVisibility(View.VISIBLE);
                        findViewById(R.id.ll_first_page).setVisibility(View.GONE);
                        iv_arrow.setVisibility(View.VISIBLE);
                        iv_arrow1.setVisibility(View.INVISIBLE);
                    } else if (i == 1 && AppUtils.isLogin(UserDisplayActivity.this)) {
                        findViewById(R.id.sv_user).setVisibility(View.GONE);
                        findViewById(R.id.ll_first_page).setVisibility(View.VISIBLE);
                        iv_arrow.setVisibility(View.INVISIBLE);
                        iv_arrow1.setVisibility(View.VISIBLE);
                    } else {
                        group.check(R.id.rb_information);
                    }
                }
            }
        });
        iv_setting = findViewById(R.id.iv_setting);
        iv_setting.setOnClickListener(this);
        bt_changepass = findViewById(R.id.bt_changepass);
        bt_changepass.setOnClickListener(this);
        iv_head = findViewById(R.id.iv_user_incon);
        tv_user_name = findViewById(R.id.tv_user_name);
        tv_bindph = findViewById(R.id.bt_bindphone);
        findViewById(R.id.ll_bdph).setOnClickListener(this);
        findViewById(R.id.ll_bdwc).setOnClickListener(this);
        findViewById(R.id.ll_bdwb).setOnClickListener(this);
        findViewById(R.id.ll_bdqq).setOnClickListener(this);
        tv_bindwechat = findViewById(R.id.bt_bindwechat);
        tv_bindqq = findViewById(R.id.bt_bindqq);
        tv_bindqq.setOnClickListener(this);
        tv_bindweibo = findViewById(R.id.bt_bindweibo);
        tv_bindweibo.setOnClickListener(this);
        rl_mine_head = findViewById(R.id.rl_mine_head);
//        rl_mine_head.setPadding(0, SNApplication.statusBar, 0, 0);
        findViewById(R.id.bt_callus).setOnClickListener(this);
    }

    public void getFollow() {
        if (SNApplication.userInfo.getUserInfo() != null) {
            TRequest<ReqUidFuid> request = new TRequest<>();
            ReqUidFuid fuid = new ReqUidFuid();
            fuid.setUid(SNApplication.userInfo.getUserInfo().getUid() + "");
            fuid.setFuid(uid);
            request.setParam(fuid, "2117", "1");
            Observable<Response_CheckFollow> observable = RetrofitClient.getInstance().getService(HttpService.class).CheckFollow(request);
            sendRequest(observable, response_checkFollow -> {
                if (response_checkFollow.isFollew()) {
                    iv_follow.setImageResource(R.mipmap.button_zjm_yiguanzhu);
                    iv_follow.setTag(0);
                } else {
                    iv_follow.setImageResource(R.mipmap.button_zjm_guanzhu);
                    iv_follow.setTag(1);
                }
            }, throwable -> ToastUtils.showToast(throwable.getMessage()));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (SNApplication.userInfo.getUserInfo() != null && Integer.parseInt(uid) == SNApplication.userInfo.getUserInfo().getUserInfo().getUid()) {
            bind(tv_bindph);
            if (SNApplication.userInfo.getUserInfo().getList() != null && SNApplication.userInfo.getUserInfo().getList().size() != 0) {
                for (int i = 0; i < SNApplication.userInfo.getUserInfo().getList().size(); i++) {
                    if (SNApplication.userInfo.getUserInfo().getList().get(i).getUserType() == 1) {
                        bind(tv_bindqq);
                    } else if (SNApplication.userInfo.getUserInfo().getList().get(i).getUserType() == 2) {
                        bind(tv_bindwechat);
                    } else if (SNApplication.userInfo.getUserInfo().getList().get(i).getUserType() == 3) {
                        bind(tv_bindweibo);
                    }
                }
            }
            GlideCacheUtil.LoadImage(this, iv_head, SNApplication.userInfo.getUserInfo().getUserInfo().getHeadImage(), true);
            tv_user_name.setText(SNApplication.userInfo.getUserInfo().getUserName());
        }
    }

    private void unbind() {
        unbind(tv_bindph);
        unbind(tv_bindqq);
        unbind(tv_bindwechat);
        unbind(tv_bindweibo);
    }

    private void unbind(TextView view) {
        view.setText("未绑定");
        view.setTextColor(getResources().getColor(R.color.font_color));
    }

    private void bind(TextView view) {
        view.setText("已绑定");
        view.setTextColor(getResources().getColor(R.color.blue_font));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_follow: {
                followOrNot((Integer) v.getTag());
                break;
            }
            case R.id.iv_back: {
                finish();
                break;
            }
            case R.id.iv_setting: {
                startActivity(new Intent(this, SettingActivity.class));
                break;
            }
            case R.id.rl_user: {
                if (AppUtils.isLogin(UserDisplayActivity.this)) {
                    startActivityForResult(new Intent(this, UserDetailActivity.class), 100);
                }
                break;
            }
            case R.id.bt_changepass: {
                if (AppUtils.isLogin(this)) {
                    this.startActivity(new Intent(this, ModifyPasswordActivity.class));
                }
                break;
            }
            case R.id.ll_bdph: {
                if (tv_bindph.getText().toString().equals("未绑定") && AppUtils.isLogin(this)) {
                } else {
                    findViewById(R.id.ll_bdph).setOnClickListener(null);
                }
                break;
            }
            case R.id.ll_bdwc: {
                if (tv_bindwechat.getText().toString().equals("未绑定")) {
                    SNApplication.WechatLogin = true;
//                    AppUtils.showLoadingDialog(this);
                    WechatLoginHelper.doLogin(this, wechatLoginCallBack);
                } else {
                    findViewById(R.id.ll_bdwc).setOnClickListener(null);
                }
                break;
            }
            case R.id.ll_bdwb: {
                if (tv_bindweibo.getText().toString().equals("未绑定")) {
                    SNApplication.WechatLogin = true;
//                    AppUtils.showLoadingDialog(this);
                    weiboLoginHelper = new WeiboLoginHelper(this, weiboLoginCallBack);
                    weiboLoginHelper.login();
                } else {
                    findViewById(R.id.ll_bdwc).setOnClickListener(null);
                }

                break;
            }
            case R.id.ll_bdqq: {
                ToastUtils.showToast("暂不支持QQ登录！");
                break;
            }
            case R.id.bt_callus: {
                Intent intent = new Intent(this, WebView1Activity.class);
                intent.putExtra("url", AppConfig.CONNECTUS);
                intent.putExtra("title", "联系我们");
                this.startActivity(intent);
                break;
            }
        }
    }

    private void followOrNot(int type) {
        if (AppUtils.isLogin(this)) {
            if (type == 1) {
                TRequest<ReqUerIdFuserId> request = new TRequest<>();
                ReqUerIdFuserId reqUidFuid = new ReqUerIdFuserId();
                reqUidFuid.setFuserId(Integer.parseInt(uid));
                reqUidFuid.setUserId(SNApplication.userInfo.getUserInfo().getUid());
                request.setParam(reqUidFuid, "2113", "1");
                sendRequest(RetrofitClient.getInstance().getService(HttpService.class).FollowUser(request), new Consumer<TResponse>() {
                    @Override
                    public void accept(TResponse tResponse) throws Exception {
                        ToastUtils.showToast("关注成功！");
                        iv_follow.setImageResource(R.mipmap.button_zjm_yiguanzhu);
                        iv_follow.setTag(0);
                    }
                }, throwable -> ToastUtils.showToast(throwable.getMessage()));
            } else if (type == 0) {
                TRequest<ReqUidFuid> request = new TRequest<>();
                ReqUidFuid reqUidFuid = new ReqUidFuid();
                reqUidFuid.setFuid(uid);
                reqUidFuid.setUid(SNApplication.userInfo.getUserInfo().getUid() + "");
                request.setParam(reqUidFuid, "2114", "1");
                sendRequest(RetrofitClient.getInstance().getService(HttpService.class).UnFollowUser(request), new Consumer<TResponse>() {
                    @Override
                    public void accept(TResponse tResponse) throws Exception {
                        ToastUtils.showToast("取消关注成功！");
                        iv_follow.setImageResource(R.mipmap.button_zjm_guanzhu);
                        iv_follow.setTag(1);
                    }
                }, throwable -> ToastUtils.showToast(throwable.getMessage()));
            }
        }
    }

    WechatLoginHelper.WechatLoginCallBack wechatLoginCallBack = new WechatLoginHelper.WechatLoginCallBack() {
        @Override
        public void onSuccess() {
            AppUtils.hideLoadingDialog();
            ThridLogin(WechatLoginHelper.openid, 2);
            WechatLoginHelper.weixinAPI = null;
        }

        @Override
        public void onFailure() {
            ToastUtils.showToast("操作失败，请重试");
            AppUtils.hideLoadingDialog();
            WechatLoginHelper.weixinAPI = null;
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 300) {
            findViewById(R.id.ll_bdph).setOnClickListener(this);
            findViewById(R.id.ll_bdwc).setOnClickListener(this);
            findViewById(R.id.ll_bdqq).setOnClickListener(this);
            findViewById(R.id.ll_bdwb).setOnClickListener(this);
            iv_head.setImageResource(R.mipmap.button_head_image);
            tv_user_name.setText("点击登录");
            unbind();
            rg_menu.check(R.id.rb_information);
        } else if (resultCode == 201) {
            UserPageAdapter adapter = new UserPageAdapter(this, SNApplication.userInfo.getUserInfo().getUid() + "");
            vp_user.setAdapter(adapter);
        }
        if (weiboLoginHelper != null && weiboLoginHelper.getSsoHandler() != null) {
            weiboLoginHelper.getSsoHandler().authorizeCallBack(requestCode, resultCode, data);
        }
    }

    private void ThridLogin(String id, int type) {
        TRequest<TReqThirdLogin> request = new TRequest<>();
        TReqThirdLogin login = new TReqThirdLogin();
        login.setOpenId(id);
        login.setUserType(type);
        request.setParam(login, "1105", "1");
        Observable<TResponse_Login> tResponse_loginObservable = RetrofitClient.getInstance().getService(HttpService.class).ThirdLogin(request);
        sendRequest(tResponse_loginObservable, tResponse_login -> {
            if (tResponse_login.getUser() != null) {
                SNApplication.userInfo.setUserInfo(tResponse_login.getUser());
                ShareUtils.putString("userInfo", new Gson().toJson(SNApplication.userInfo));
                bind(tv_bindph);
                onResume();
                UserPageAdapter adapter = new UserPageAdapter(this, SNApplication.userInfo.getUserInfo().getUid() + "");
                vp_user.setAdapter(adapter);
            } else {
                Intent intent = new Intent(this, ChangeUserinfoActivity.class);
                intent.putExtra("type", 1);
                intent.putExtra("openId", id);
                startActivity(intent);
            }
        }, throwable -> ToastUtils.showToast(throwable.getMessage()));
        onResume();
    }

    WeiboLoginHelper.WeiboLoginCallBack weiboLoginCallBack = new WeiboLoginHelper.WeiboLoginCallBack() {
        @Override
        public void onSuccess(String id, String nickName, String headUrl) {
            AppUtils.hideLoadingDialog();
            ThridLogin(id, 3);
        }

        @Override
        public void onFailure() {
        }
    };

    //这个方法会取得accesstoken  和openID
    public void getToken(String code) {
        HashMap<String, String> map = new HashMap<>();
        map.put("appid", WechatLoginHelper.APP_ID);
        map.put("secret", WechatLoginHelper.SECRET_KEY);
        map.put("code", code);
        map.put("grant_type", "authorization_code");
        Observable<Respon_Wechat> respon_weiboObservable = RetrofitClient.getInstance().getService(HttpService.class).WeChatInfo(map);
        sendRequest(respon_weiboObservable, respon_wechat -> {
            WechatLoginHelper.openid = respon_wechat.getOpenid();
            String _token = respon_wechat.getAccess_token();
            getUserInfo(_token, WechatLoginHelper.openid);
        });
    }

    //获取到token和openID之后，调用此接口得到身份信息
    private void getUserInfo(String token, String openID) {
//        String url = "https://api.weixin.qq.com/sns/userinfo?access_token=" +token+"&openid=" +openID;
        HashMap<String, String> map = new HashMap<>();
        map.put("access_token", token);
        map.put("openid", openID);
        map.put("lang", "zh_CN");
        Observable<Respon_WechatUserInfo> respon_weiboObservable = RetrofitClient.getInstance().getService(HttpService.class).WeChatUserInfo(map);
        sendRequest(respon_weiboObservable, respon_wechatUserInfo -> {
            WechatLoginHelper.nickName = respon_wechatUserInfo.getNickname();
            WechatLoginHelper.sexType = respon_wechatUserInfo.getSex();
            WechatLoginHelper.headImgUrl = respon_wechatUserInfo.getHeadimgurl();
            if (WechatLoginHelper.loginCallBack != null) {
                WechatLoginHelper.loginCallBack.onSuccess();
            }
        });
    }

    public class LoginBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("com.kunleen.wechat.login")) {
                String code = intent.getStringExtra("code");
                getToken(code);
            }
        }
    }
}
