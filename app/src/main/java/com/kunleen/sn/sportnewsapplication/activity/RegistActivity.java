package com.kunleen.sn.sportnewsapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.kunleen.sn.sportnewsapplication.AppConfig;
import com.kunleen.sn.sportnewsapplication.R;
import com.kunleen.sn.sportnewsapplication.app.SNApplication;
import com.kunleen.sn.sportnewsapplication.base.BaseActivity;
import com.kunleen.sn.sportnewsapplication.custom.TitleBarView;
import com.kunleen.sn.sportnewsapplication.network.RetrofitClient;
import com.kunleen.sn.sportnewsapplication.network.bean.ReqPhoneCode;
import com.kunleen.sn.sportnewsapplication.network.bean.ReqRegist;
import com.kunleen.sn.sportnewsapplication.network.bean.TRequest;
import com.kunleen.sn.sportnewsapplication.network.bean.TResponse;
import com.kunleen.sn.sportnewsapplication.network.bean.TResponse_Empty;
import com.kunleen.sn.sportnewsapplication.network.bean.TResponse_Login;
import com.kunleen.sn.sportnewsapplication.network.service.HttpService;
import com.kunleen.sn.sportnewsapplication.rx.RxHelper;
import com.kunleen.sn.sportnewsapplication.utils.DevicesUtils;
import com.kunleen.sn.sportnewsapplication.utils.PatternUtils;
import com.kunleen.sn.sportnewsapplication.utils.ShareUtils;
import com.kunleen.sn.sportnewsapplication.utils.ToastUtils;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class RegistActivity extends BaseActivity implements View.OnClickListener {
    public static final String TAG = "RegistActivity_LOG";
    TitleBarView titleBar;
    TextView tv_reg_getcode;
    EditText et_phone, et_reg_password, et_reg_code;
    private boolean isGetCode = false;
    private int mTime = 120;
    View btn_reg;
    CheckBox cb_rules, cb_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);
        initTitleBar();
        DevicesUtils.setWindowStatusBarColor(this, R.color.white);//设置状态栏颜色
        initView();
        initWindow(this);
    }

    private void initView() {
        findViewById(R.id.tv_register_rule).setOnClickListener(this);
        tv_reg_getcode = findViewById(R.id.tv_reg_getcode);
        tv_reg_getcode.setOnClickListener(this);
        btn_reg = findViewById(R.id.btn_reg);
        btn_reg.setOnClickListener(this);
        cb_rules = findViewById(R.id.cb_rules);
        et_phone = findViewById(R.id.et_reg_phone);
        et_reg_password = findViewById(R.id.et_reg_password);
        et_reg_code = findViewById(R.id.et_reg_code);
        cb_password = findViewById(R.id.cb_password);
        cb_password.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    et_reg_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                } else {
                    et_reg_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
            }
        });
    }

    private void initTitleBar() {
        titleBar = findViewById(R.id.titlebar);
        titleBar.setActivity(this);
        titleBar.setTitle("注册");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ShareUtils.putString("userInfo", new Gson().toJson(SNApplication.userInfo));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_reg_getcode: {
                String num = et_phone.getText().toString();
                if (PatternUtils.isChinaPhoneLegal(num)) {
                    TRequest<ReqPhoneCode> req = new TRequest<>();
                    ReqPhoneCode reqPhoneLogin = new ReqPhoneCode();
                    reqPhoneLogin.setMobile(num);
                    reqPhoneLogin.setType("1");
                    req.setParam(reqPhoneLogin, "1104", "1");
                    Observable<TResponse> loginCode = RetrofitClient.getInstance().getService(HttpService.class).getLoginCode(req);
                    sendRequest(loginCode, new Consumer<TResponse>() {
                        @Override
                        public void accept(TResponse tResponse) throws Exception {
                            codeTime();
                            isGetCode = true;
                            tv_reg_getcode.setOnClickListener(null);
                        }
                    }, throwable -> ToastUtils.showToast(throwable.getMessage()));
                } else {
                    ToastUtils.showToast("请输入正确的手机号码！");
                }
                break;
            }
            case R.id.btn_reg: {
                if (cb_rules.isChecked()) {
                    if (PatternUtils.isChinaPhoneLegal(et_phone.getText().toString())) {
                        if (PatternUtils.isPassword(et_reg_password.getText().toString())) {
                            if (PatternUtils.isCode(et_reg_code.getText().toString())) {
                                TRequest<ReqRegist> req = new TRequest<>();
                                ReqRegist reqPhoneLogin = new ReqRegist();
                                reqPhoneLogin.setUserName(et_phone.getText().toString());
                                reqPhoneLogin.setMobile(et_phone.getText().toString());
                                reqPhoneLogin.setCode(et_reg_code.getText().toString());
                                reqPhoneLogin.setPassword(et_reg_password.getText().toString());
                                req.setParam(reqPhoneLogin, "1101", "1");
                                Observable<TResponse_Login> loginCode = RetrofitClient.getInstance().getService(HttpService.class).Regist(req);
                                sendRequest(loginCode, new Consumer<TResponse_Login>() {
                                    @Override
                                    public void accept(TResponse_Login tResponse_login) throws Exception {
                                        SNApplication.userInfo.setUserInfo(tResponse_login.getUser());
                                        finish();
                                    }
                                }, throwable -> ToastUtils.showToast(throwable.getMessage()));
                            } else {
                                ToastUtils.showToast("验证码必须是6位纯数字！");
                            }
                        } else {
                            ToastUtils.showToast("密码必须是6到16位数字+英文字符的密码！");
                        }
                    } else {
                        ToastUtils.showToast("手机号码格式错误！");
                    }
                } else {
                    ToastUtils.showToast("请阅读《用户协议和隐私条款》后再进行注册！");
                }
                break;
            }
            case R.id.tv_register_rule: {
                Intent intent = new Intent(RegistActivity.this, WebView1Activity.class);
                intent.putExtra("url", AppConfig.UserAgreement);
                intent.putExtra("title", "用户注册协议");
                startActivity(intent);
                break;
            }
        }
    }

    private void codeTime() {
        Observable.interval(1, TimeUnit.SECONDS).take(mTime).map(new Function<Long, Long>() {
            @Override
            public Long apply(Long aLong) throws Exception {
                return mTime - aLong;
            }
        }).compose(RxHelper.<Long>rxSchedulerHelper()).subscribe(new Observer<Long>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Long value) {
                if (tv_reg_getcode != null) {
                    tv_reg_getcode.setText(value + "秒");
                }
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {
                tv_reg_getcode.setOnClickListener(RegistActivity.this);
                isGetCode = false;
                tv_reg_getcode.setText("获取验证码");
                ToastUtils.showToast("验证码超时，如果登录请重新获取！");
            }
        });
    }
}

