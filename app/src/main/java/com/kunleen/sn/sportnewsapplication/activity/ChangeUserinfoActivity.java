package com.kunleen.sn.sportnewsapplication.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.kunleen.sn.sportnewsapplication.R;
import com.kunleen.sn.sportnewsapplication.app.SNApplication;
import com.kunleen.sn.sportnewsapplication.base.BaseActivity;
import com.kunleen.sn.sportnewsapplication.custom.TitleBarView;
import com.kunleen.sn.sportnewsapplication.network.RetrofitClient;
import com.kunleen.sn.sportnewsapplication.network.bean.ReqChangeAddress;
import com.kunleen.sn.sportnewsapplication.network.bean.ReqChangeName;
import com.kunleen.sn.sportnewsapplication.network.bean.ReqLogin;
import com.kunleen.sn.sportnewsapplication.network.bean.ReqPhoneCode;
import com.kunleen.sn.sportnewsapplication.network.bean.ReqRegist;
import com.kunleen.sn.sportnewsapplication.network.bean.TRequest;
import com.kunleen.sn.sportnewsapplication.network.bean.TResponse;
import com.kunleen.sn.sportnewsapplication.network.bean.TResponse_Empty;
import com.kunleen.sn.sportnewsapplication.network.bean.TResponse_Login;
import com.kunleen.sn.sportnewsapplication.network.service.HttpService;
import com.kunleen.sn.sportnewsapplication.rx.RxHelper;
import com.kunleen.sn.sportnewsapplication.utils.AppUtils;
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

public class ChangeUserinfoActivity extends BaseActivity implements View.OnClickListener {
    public static final String TAG = "ChangeUserinfoActivity";
    TitleBarView titleBarView;
    EditText et_change_nickname, et_bind_code, et_bind_phone;
    TextView btn_bind_getcode;
    View iv_clear, btn_change, ll_change_nickname, ll_bind_phone;
    private int type;
    private String openId;
    public int mTime = 120;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        type = getIntent().getIntExtra("type", 0);
        DevicesUtils.setWindowStatusBarColor(this, R.color.white);
        setContentView(R.layout.activity_change_userinfo);
        initTitle();
        initWindow(this);
        bindActivity();
        if (type == 0) {
            ll_change_nickname = findViewById(R.id.ll_change_nickname);
            ll_change_nickname.setVisibility(View.VISIBLE);
            initChangeNameView();
        } else if (type == 1) {
            ll_bind_phone = findViewById(R.id.ll_bind_phone);
            ll_bind_phone.setVisibility(View.VISIBLE);
            initBindView();
            openId = getIntent().getStringExtra("openId");
        }
    }

    private void initBindView() {
        et_bind_code = findViewById(R.id.et_bind_code);
        btn_bind_getcode = findViewById(R.id.btn_bind_getcode);
        et_bind_phone = findViewById(R.id.et_bind_phone);
        btn_bind_getcode.setOnClickListener(this);
        btn_change = findViewById(R.id.btn_change);
        btn_change.setOnClickListener(this);
    }

    private void initChangeNameView() {
        et_change_nickname = findViewById(R.id.et_change_nickname);
        iv_clear = findViewById(R.id.iv_clear);
        btn_change = findViewById(R.id.btn_change);
        btn_change.setOnClickListener(this);
        iv_clear.setOnClickListener(this);
    }

    private void initTitle() {
        titleBarView = findViewById(R.id.titlebar);
        titleBarView.setPadding(0, SNApplication.statusBar, 0, 0);
        if (type == 0) {
            titleBarView.setTitle("修改昵称");
        } else if (type == 1) {
            titleBarView.setTitle("绑定手机");
        }
        titleBarView.setActivity(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_clear: {
                et_change_nickname.setText("");
                break;
            }
            case R.id.btn_change: {
                if (type == 0) {
                    ChangeName();
                } else if (type == 1) {
                    bindLogin();
                }
                break;
            }
            case R.id.btn_bind_getcode: {
                getCode();
                break;
            }
        }
    }

    private void bindLogin() {
        TRequest<ReqRegist> req = new TRequest<>();
        ReqRegist reqRegist = new ReqRegist();
        if (PatternUtils.isCode(et_bind_code.getText().toString()) && PatternUtils.isChinaPhoneLegal(et_bind_phone.getText().toString())) {
            reqRegist.setMobile(et_bind_phone.getText().toString());
            reqRegist.setCode(et_bind_code.getText().toString());
            reqRegist.setOpenId(openId);
            reqRegist.setPassword("123456abcdef");
            reqRegist.setUserName(et_bind_phone.getText().toString());
        } else {
            ToastUtils.showToast("数据格式错误，请检查后再试！");
            return;
        }
        req.setParam(reqRegist, "1101", "1");
        Observable<TResponse_Login> login = RetrofitClient.getInstance().getService(HttpService.class).Regist(req);
        sendRequest(login, tResponse_login -> {
            SNApplication.userInfo.setUserInfo(tResponse_login.getUser());
            finish();
        }, throwable -> ToastUtils.showToast(throwable.getMessage()));
    }

    private void ChangeName() {
        AppUtils.showLoadingDialog(this);
        TRequest<ReqChangeName> tRequest = new TRequest<>();
        ReqChangeName reqChangeName = new ReqChangeName();
        reqChangeName.setUid(SNApplication.userInfo.getUserInfo().getUid());
        reqChangeName.setUserName(et_change_nickname.getText().toString());
        tRequest.setParam(reqChangeName, "1121", "1");
        Observable<TResponse> tResponse_emptyObservable = RetrofitClient.getInstance().getService(HttpService.class).ChangeName(tRequest);
        sendRequest(tResponse_emptyObservable, new Consumer<TResponse>() {
            @Override
            public void accept(TResponse tResponse) throws Exception {
                AppUtils.hideLoadingDialog();
                ToastUtils.showToast("昵称修改成功！");
                SNApplication.userInfo.getUserInfo().setUserName(et_change_nickname.getText().toString());
                finish();
            }
        }, throwable -> ToastUtils.showToast(throwable.getMessage()));

    }

    private void getCode() {
        String num = et_bind_phone.getText().toString();
        if (PatternUtils.isChinaPhoneLegal(num)) {
            TRequest<ReqPhoneCode> req = new TRequest<>();
            ReqPhoneCode reqPhoneLogin = new ReqPhoneCode();
            reqPhoneLogin.setMobile(num);
            reqPhoneLogin.setType("4");
            req.setParam(reqPhoneLogin, "1104", "1");
            Observable<TResponse> loginCode = RetrofitClient.getInstance().getService(HttpService.class).getLoginCode(req);
            sendRequest(loginCode, tResponse -> {
                codeTime();
                btn_bind_getcode.setOnClickListener(null);
            }, throwable -> ToastUtils.showToast(throwable.getMessage()));
        } else {
            ToastUtils.showToast("请输入正确的手机号码！");
        }
    }

    private void codeTime() {
        Observable.interval(1, TimeUnit.SECONDS).take(120).map(new Function<Long, Long>() {

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
                if (btn_bind_getcode != null) {
                    btn_bind_getcode.setText(value + "秒");
                }
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {
                btn_bind_getcode.setOnClickListener(ChangeUserinfoActivity.this);
                btn_bind_getcode.setText("获取验证码");
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ShareUtils.putString("userInfo", new Gson().toJson(SNApplication.userInfo));
    }
}
