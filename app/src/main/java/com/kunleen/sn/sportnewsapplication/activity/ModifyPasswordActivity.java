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
import com.kunleen.sn.sportnewsapplication.network.bean.ReqModifyPassword;
import com.kunleen.sn.sportnewsapplication.network.bean.ReqPhoneCode;
import com.kunleen.sn.sportnewsapplication.network.bean.TRequest;
import com.kunleen.sn.sportnewsapplication.network.bean.TResponse;
import com.kunleen.sn.sportnewsapplication.network.bean.TResponse_Empty;
import com.kunleen.sn.sportnewsapplication.network.service.HttpService;
import com.kunleen.sn.sportnewsapplication.rx.RxHelper;
import com.kunleen.sn.sportnewsapplication.utils.DevicesUtils;
import com.kunleen.sn.sportnewsapplication.utils.ToastUtils;
import com.kunleen.sn.sportnewsapplication.utils.Utils;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class ModifyPasswordActivity extends BaseActivity implements View.OnClickListener {
    public static final String TAG = "ModifyPassword_LOG";
    TitleBarView titleBarView;
    TextView tv_num, tv_code;
    View btn_modify;
    EditText et_modify_code, et_modify_password;
    String phoneNumber;
    int mTime = 120;
    private Disposable disposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_password);
        DevicesUtils.setWindowStatusBarColor(this, R.color.white);//设置状态栏颜色
        initTitlebar();
        phoneNumber = SNApplication.userInfo.getUserInfo().getMobile();
        String substring = phoneNumber.substring(3, 7);
        substring = phoneNumber.replaceAll(substring, "****");
        tv_num = findViewById(R.id.tv_phonenum);
        tv_num.setText("请使用注册手机号" + substring + "接受验证码");
        tv_code = findViewById(R.id.tv_modify_code);
        tv_code.setOnClickListener(this);
        btn_modify = findViewById(R.id.btn_modify);
        btn_modify.setOnClickListener(this);
        et_modify_code = findViewById(R.id.et_modify_code);
        et_modify_password = findViewById(R.id.et_modify_password);
        bindActivity();
        initWindow(this);
    }

    private void initTitlebar() {
        titleBarView = findViewById(R.id.titlebar);
        titleBarView.setPadding(0, SNApplication.statusBar, 0, 0);
        titleBarView.setTitle("修改密码");
        titleBarView.setActivity(this);
    }

    public void getCode() {
        TRequest<ReqPhoneCode> req = new TRequest<>();
        ReqPhoneCode reqPhoneLogin = new ReqPhoneCode();
        reqPhoneLogin.setMobile(phoneNumber);
        reqPhoneLogin.setType("2");
        req.setParam(reqPhoneLogin, "1104", "1");
        Observable<TResponse> loginCode = RetrofitClient.getInstance().getService(HttpService.class).getLoginCode(req);
        sendRequest(loginCode, new Consumer<TResponse>() {
            @Override
            public void accept(TResponse tResponse) throws Exception {
                codeTime();
                tv_code.setOnClickListener(null);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                ToastUtils.showToast(throwable.getMessage());
            }
        });
    }

//    private void codeTime() {
//        Observable.interval(1, TimeUnit.SECONDS).take(120).map(new Function<Long, Long>() {
//            @Override
//            public Long apply(Long aLong) throws Exception {
//                return mTime - aLong;
//            }
//        }).compose(RxHelper.<Long>rxSchedulerHelper()).subscribe(new Observer<Long>() {
//            @Override
//            public void onSubscribe(Disposable d) {
//
//            }
//
//            @Override
//            public void onNext(Long value) {
//                if (tv_code != null) {
//                    tv_code.setText(value + "秒");
//                }
//            }
//
//            @Override
//            public void onError(Throwable e) {
//
//            }
//
//            @Override
//            public void onComplete() {
//                tv_code.setOnClickListener(ModifyPasswordActivity.this);
//                tv_code.setText("获取验证码");
////                ToastUtils.showToast("验证码超时，如果登录请重新获取！");
//            }
//        });
//    }

    private void codeTime() {
        Observable<Long> interval = Observable.interval(1, TimeUnit.SECONDS);
        disposable = interval.take(120).map(aLong -> mTime - aLong).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<Long>() {
            @Override
            public void accept(Long aLong) throws Exception {
                if (tv_code != null) {
                    tv_code.setText(aLong + "秒");
                }
                if (aLong == 1) {
                    tv_code.setOnClickListener(ModifyPasswordActivity.this);
                    tv_code.setText("获取验证码");
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_modify_code: {
                getCode();
                break;
            }
            case R.id.btn_modify: {
                if (et_modify_code.getText().toString().equals("")) {
                    ToastUtils.showToast("请输入验证码！");
                    return;
                } else if (et_modify_password.getText().toString().equals("")) {
                    ToastUtils.showToast("请输入新密码！");
                    return;
                }
                modify();
                break;
            }
        }
    }

    private void modify() {
        TRequest<ReqModifyPassword> request = new TRequest<>();
        ReqModifyPassword modifyPassword = new ReqModifyPassword();
        modifyPassword.setMobile(phoneNumber);
        modifyPassword.setUid(SNApplication.userInfo.getUserInfo().getUid() + "");
        modifyPassword.setOldPwd(et_modify_code.getText().toString());
        modifyPassword.setNewPwd(et_modify_password.getText().toString());
        modifyPassword.setType(2);
        request.setParam(modifyPassword, "1106", "1");
        Log.i(TAG, new Gson().toJson(request));
        Observable<TResponse> observable = RetrofitClient.getInstance().getService(HttpService.class).ModifyPassword(request);
        sendRequest(observable, tResponse -> {
            ToastUtils.showToast("密码修改成功!");
            finish();
        }, throwable -> ToastUtils.showToast(throwable.getMessage()));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (disposable != null) {
            disposable.dispose();
            disposable = null;
        }
    }
}
