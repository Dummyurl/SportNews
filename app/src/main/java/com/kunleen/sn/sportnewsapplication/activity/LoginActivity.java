package com.kunleen.sn.sportnewsapplication.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.kunleen.sn.sportnewsapplication.R;
import com.kunleen.sn.sportnewsapplication.app.SNApplication;
import com.kunleen.sn.sportnewsapplication.base.BaseActivity;
import com.kunleen.sn.sportnewsapplication.custom.TitleBarView;
import com.kunleen.sn.sportnewsapplication.login.WechatLoginHelper;
import com.kunleen.sn.sportnewsapplication.login.WeiboLoginHelper;
import com.kunleen.sn.sportnewsapplication.network.RetrofitClient;
import com.kunleen.sn.sportnewsapplication.network.bean.ReqLogin;
import com.kunleen.sn.sportnewsapplication.network.bean.ReqPhoneCode;
import com.kunleen.sn.sportnewsapplication.network.bean.Respon_Wechat;
import com.kunleen.sn.sportnewsapplication.network.bean.Respon_WechatUserInfo;
import com.kunleen.sn.sportnewsapplication.network.bean.TReqThirdLogin;
import com.kunleen.sn.sportnewsapplication.network.bean.TRequest;
import com.kunleen.sn.sportnewsapplication.network.bean.TResponse;
import com.kunleen.sn.sportnewsapplication.network.bean.TResponse_Login;
import com.kunleen.sn.sportnewsapplication.network.service.HttpService;
import com.kunleen.sn.sportnewsapplication.utils.AppUtils;
import com.kunleen.sn.sportnewsapplication.utils.PatternUtils;
import com.kunleen.sn.sportnewsapplication.utils.ShareUtils;
import com.kunleen.sn.sportnewsapplication.utils.ToastUtils;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;


public class LoginActivity extends BaseActivity implements View.OnClickListener {
    public static final String TAG = "LoginActivity_TAG";
    TitleBarView titleBarView;
    View tv_right, btn_login, ll_phone, ll_password, tv_login_wechat, tv_login_qq;
    TextView btn_login_getcode, tv_login_weibo;
    EditText ed_phone, et_login_code, et_login_password;
    RadioGroup rg_login;
    CheckBox cb_password;
    private int mTime = 120;
    /**
     * 封装了 "access_token"，"expires_in"，"refresh_token"，并提供了他们的管理功能
     */
    private WeiboLoginHelper weiboLoginHelper;
    private Disposable disposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        bindActivity();
        initTitlebar();
        initView();
        initWindow(this);
        //实例化IntentFilter对象
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.kunleen.wechat.login");
        LoginBroadcastReceiver receiver = new LoginBroadcastReceiver();
        //注册广播接收
        registerReceiver(receiver, filter);
    }

    private void initView() {
        btn_login_getcode = findViewById(R.id.btn_login_getcode);
        btn_login_getcode.setOnClickListener(this);
        ed_phone = findViewById(R.id.et_login_phone);
        btn_login = findViewById(R.id.btn_login);
        btn_login.setOnClickListener(this);
        et_login_code = findViewById(R.id.et_login_code);
        rg_login = findViewById(R.id.rg_login);
        ll_phone = findViewById(R.id.ll_phone);
        ll_password = findViewById(R.id.ll_password);
        cb_password = findViewById(R.id.cb_password);
        et_login_password = findViewById(R.id.et_login_password);
        tv_login_weibo = findViewById(R.id.tv_login_weibo);
        tv_login_wechat = findViewById(R.id.tv_login_wechat);
        tv_login_qq = findViewById(R.id.tv_login_qq);
        tv_login_qq.setOnClickListener(this);
        tv_login_wechat.setOnClickListener(this);
        tv_login_weibo.setOnClickListener(this);
        cb_password.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                et_login_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
            } else {
                et_login_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            }
        });
        rg_login.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.rb_phone: {
                    ll_phone.setVisibility(View.VISIBLE);
                    ll_password.setVisibility(View.GONE);
                    break;
                }
                case R.id.rb_password: {
                    ll_phone.setVisibility(View.GONE);
                    ll_password.setVisibility(View.VISIBLE);
                    break;
                }
            }
        });
    }

    private void initTitlebar() {
        titleBarView = findViewById(R.id.titlebar);
        titleBarView.setTitle("登录");
        titleBarView.setRightText("注册");
        titleBarView.setActivity(this);
//        titleBarView.setPadding(0, SNApplication.statusBar, 0, 0);
        tv_right = titleBarView.findViewById(R.id.top_right_tv);
        tv_right.setOnClickListener(this);
    }

    private void codeTime() {
        Observable<Long> interval = Observable.interval(1, TimeUnit.SECONDS);
        disposable = interval.take(120).map(aLong -> mTime - aLong).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<Long>() {
            @Override
            public void accept(Long aLong) throws Exception {
                if (btn_login_getcode != null) {
                    btn_login_getcode.setText(aLong + "秒");
                }
                if (aLong == 1) {
                    btn_login_getcode.setOnClickListener(LoginActivity.this);
                    btn_login_getcode.setText("获取验证码");
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.top_right_tv: {
                LoginActivity.this.startActivity(new Intent(LoginActivity.this, RegistActivity.class));
                finish();
                break;
            }
            case R.id.btn_login_getcode: {
                String num = ed_phone.getText().toString();
                if (PatternUtils.isChinaPhoneLegal(num)) {
                    TRequest<ReqPhoneCode> req = new TRequest<>();
                    ReqPhoneCode reqPhoneLogin = new ReqPhoneCode();
                    reqPhoneLogin.setMobile(num);
                    reqPhoneLogin.setType("3");
                    req.setParam(reqPhoneLogin, "1104", "1");
                    Observable<TResponse> loginCode = RetrofitClient.getInstance().getService(HttpService.class).getLoginCode(req);
                    sendRequest(loginCode, tResponse -> {
                        codeTime();
                        btn_login_getcode.setOnClickListener(null);
                    }, throwable -> ToastUtils.showToast(throwable.getMessage()));
                } else {
                    ToastUtils.showToast("请输入正确的手机号码！");
                }
                break;
            }
            case R.id.btn_login: {
                TRequest<ReqLogin> req = new TRequest<>();
                ReqLogin reqLogin = new ReqLogin();
                reqLogin.setMobile(ed_phone.getText().toString());
                if (rg_login.getCheckedRadioButtonId() == R.id.rb_phone && PatternUtils.isCode(et_login_code.getText().toString()) && PatternUtils.isChinaPhoneLegal(ed_phone.getText().toString())) {
                    reqLogin.setCode(et_login_code.getText().toString());
                } else if (rg_login.getCheckedRadioButtonId() == R.id.rb_password) {
                    reqLogin.setPassword(et_login_password.getText().toString());
                } else {
                    ToastUtils.showToast("数据格式错误，请检查后再试！");
                    return;
                }
                req.setParam(reqLogin, "1102", "1");
                Observable<TResponse_Login> login = RetrofitClient.getInstance().getService(HttpService.class).Login(req);
                sendRequest(login, tResponse_login -> {
                    SNApplication.userInfo.setUserInfo(tResponse_login.getUser());
                    setResult(201);
                    finish();
                }, throwable -> ToastUtils.showToast(throwable.getMessage()));
                break;
            }
            case R.id.tv_login_weibo: {
                weiboLoginHelper = new WeiboLoginHelper(LoginActivity.this, weiboLoginCallBack);
                weiboLoginHelper.login();
                break;
            }
            case R.id.tv_login_qq: {
                ToastUtils.showToast("QQ登录功能暂未开放！");
                break;
            }
            case R.id.tv_login_wechat: {
                SNApplication.WechatLogin = true;
//                AppUtils.showLoadingDialog(this);
                WechatLoginHelper.doLogin(LoginActivity.this, wechatLoginCallBack);
                break;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (weiboLoginHelper.getSsoHandler() != null) {
            weiboLoginHelper.getSsoHandler().authorizeCallBack(requestCode, resultCode, data);
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
    WeiboLoginHelper.WeiboLoginCallBack weiboLoginCallBack = new WeiboLoginHelper.WeiboLoginCallBack() {
        @Override
        public void onSuccess(String id, String nickName, String headUrl) {
            AppUtils.hideLoadingDialog();
            ThridLogin(id, 3);
//            ToastUtils.showToast("id:" + id + "---nickName:" + nickName + "----headURL:" + headUrl);
        }

        @Override
        public void onFailure() {
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (disposable != null) {
            disposable.dispose();
            disposable = null;
        }
        ShareUtils.putString("userInfo", new Gson().toJson(SNApplication.userInfo));
//        WechatLoginHelper.getInstance().destory();
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
            } else {
                Intent intent = new Intent(LoginActivity.this, ChangeUserinfoActivity.class);
                intent.putExtra("type", 1);
                intent.putExtra("openId", id);
                LoginActivity.this.startActivity(intent);
            }
            setResult(201);
            finish();
        }, throwable -> ToastUtils.showToast(throwable.getMessage()));
    }

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
