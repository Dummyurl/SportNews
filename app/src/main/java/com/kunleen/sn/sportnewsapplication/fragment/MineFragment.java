package com.kunleen.sn.sportnewsapplication.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.kunleen.sn.sportnewsapplication.AppConfig;
import com.kunleen.sn.sportnewsapplication.R;
import com.kunleen.sn.sportnewsapplication.activity.ChangeUserinfoActivity;
import com.kunleen.sn.sportnewsapplication.activity.FindPasswordActivity;
import com.kunleen.sn.sportnewsapplication.activity.LoginActivity;
import com.kunleen.sn.sportnewsapplication.activity.ModifyPasswordActivity;
import com.kunleen.sn.sportnewsapplication.activity.SettingActivity;
import com.kunleen.sn.sportnewsapplication.activity.UserDetailActivity;
import com.kunleen.sn.sportnewsapplication.activity.WebView1Activity;
import com.kunleen.sn.sportnewsapplication.app.SNApplication;
import com.kunleen.sn.sportnewsapplication.base.BaseActivity;
import com.kunleen.sn.sportnewsapplication.base.BaseFragment;
import com.kunleen.sn.sportnewsapplication.custom.CircleImageView;
import com.kunleen.sn.sportnewsapplication.login.WechatLoginHelper;
//import com.kunleen.sn.sportnewsapplication.login.WeiboLoginHelper;
import com.kunleen.sn.sportnewsapplication.login.WeiboLoginHelper;
import com.kunleen.sn.sportnewsapplication.network.RetrofitClient;
import com.kunleen.sn.sportnewsapplication.network.bean.TReqThirdLogin;
import com.kunleen.sn.sportnewsapplication.network.bean.TRequest;
import com.kunleen.sn.sportnewsapplication.network.bean.TResponse;
import com.kunleen.sn.sportnewsapplication.network.bean.TResponse_Login;
import com.kunleen.sn.sportnewsapplication.network.service.HttpService;
import com.kunleen.sn.sportnewsapplication.utils.AppUtils;
import com.kunleen.sn.sportnewsapplication.utils.DevicesUtils;
import com.kunleen.sn.sportnewsapplication.utils.ShareUtils;
import com.kunleen.sn.sportnewsapplication.utils.ToastUtils;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


public class MineFragment extends BaseFragment implements View.OnClickListener {
    public static final String TAG = "MineFragment_Log";
    View iv_setting, rl_user, bt_changepass, rl_mine_head;
    CircleImageView iv_head;
    TextView tv_user_name, tv_bindph, tv_bindwechat, tv_bindqq, tv_bindweibo;
    private View ContentView;
    private WeiboLoginHelper weiboLoginHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_mine, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ContentView = view;
        iv_setting = view.findViewById(R.id.iv_setting);
        iv_setting.setOnClickListener(this);
        rl_user = view.findViewById(R.id.rl_user);
        rl_user.setOnClickListener(this);
        bt_changepass = view.findViewById(R.id.bt_changepass);
        bt_changepass.setOnClickListener(this);
        iv_head = view.findViewById(R.id.iv_user_incon);
        tv_user_name = view.findViewById(R.id.tv_user_name);
        tv_bindph = view.findViewById(R.id.bt_bindphone);
        view.findViewById(R.id.ll_bdph).setOnClickListener(this);
        view.findViewById(R.id.ll_bdwc).setOnClickListener(this);
        view.findViewById(R.id.ll_bdwb).setOnClickListener(this);
        view.findViewById(R.id.ll_bdqq).setOnClickListener(this);
        tv_bindwechat = view.findViewById(R.id.bt_bindwechat);
        tv_bindqq = view.findViewById(R.id.bt_bindqq);
        tv_bindqq.setOnClickListener(this);
        tv_bindweibo = view.findViewById(R.id.bt_bindweibo);
        tv_bindweibo.setOnClickListener(this);
        rl_mine_head = view.findViewById(R.id.rl_mine_head);
//        rl_mine_head.setPadding(0, SNApplication.statusBar, 0, 0);
        view.findViewById(R.id.bt_callus).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_setting: {
                getActivity().startActivity(new Intent(getActivity(), SettingActivity.class));
                break;
            }
            case R.id.rl_user: {
                if (SNApplication.userInfo.getUserInfo() == null) {
                    getActivity().startActivity(new Intent(getActivity(), LoginActivity.class));
                } else {
                    getActivity().startActivity(new Intent(getActivity(), UserDetailActivity.class));
                }
                break;
            }
            case R.id.bt_changepass: {
                if (SNApplication.userInfo.getUserInfo() != null) {
                    getActivity().startActivity(new Intent(getActivity(), ModifyPasswordActivity.class));
                    break;
                } else {
                    getActivity().startActivity(new Intent(getActivity(), LoginActivity.class));
                }
                break;
            }
            case R.id.ll_bdph: {
                if (tv_bindph.getText().toString().equals("未绑定")) {
                    getActivity().startActivity(new Intent(getActivity(), LoginActivity.class));
                } else {
                    ContentView.findViewById(R.id.ll_bdph).setOnClickListener(null);
                }
                break;
            }
            case R.id.ll_bdwc: {
                if (tv_bindwechat.getText().toString().equals("未绑定")) {
                    SNApplication.WechatLogin = true;
                    AppUtils.showLoadingDialog(getActivity());
                    WechatLoginHelper.doLogin((BaseActivity) getActivity(), wechatLoginCallBack);
                } else {
                    ContentView.findViewById(R.id.ll_bdwc).setOnClickListener(null);
                }
                break;
            }
            case R.id.ll_bdwb: {
                if (tv_bindweibo.getText().toString().equals("未绑定")) {
                    SNApplication.WechatLogin = true;
                    AppUtils.showLoadingDialog(getActivity());
                    weiboLoginHelper = new WeiboLoginHelper(getActivity(), weiboLoginCallBack);
                    weiboLoginHelper.login();
                } else {
                    ContentView.findViewById(R.id.ll_bdwc).setOnClickListener(null);
                }

                break;
            }
            case R.id.ll_bdqq: {
                ToastUtils.showToast("暂不支持QQ登录！");
                break;
            }
            case R.id.bt_callus: {
                Intent intent = new Intent(getActivity(), WebView1Activity.class);
                intent.putExtra("url", AppConfig.CONNECTUS);
                intent.putExtra("title", "联系我们");
                getActivity().startActivity(intent);
                break;
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (weiboLoginHelper != null && weiboLoginHelper.getSsoHandler() != null) {
            weiboLoginHelper.getSsoHandler().authorizeCallBack(requestCode, resultCode, data);
        }
    }

    WeiboLoginHelper.WeiboLoginCallBack weiboLoginCallBack = new WeiboLoginHelper.WeiboLoginCallBack() {
        @Override
        public void onSuccess(String id, String nickName, String headUrl) {
            AppUtils.hideLoadingDialog();
            ThridLogin(id, 3);
//            ToastUtils.showToast("id:" + id + "---nickName:" + nickName + "----headURL:" + headUrl);
        }

        @Override
        public void onFailure() {
            AppUtils.hideLoadingDialog();
        }
    };
    WechatLoginHelper.WechatLoginCallBack wechatLoginCallBack = new WechatLoginHelper.WechatLoginCallBack() {
        @Override
        public void onSuccess() {
            AppUtils.hideLoadingDialog();
            ThridLogin(WechatLoginHelper.openid, 2);
        }

        @Override
        public void onFailure() {
            ToastUtils.showToast("操作失败，请重试");
            AppUtils.hideLoadingDialog();
        }
    };

    private void ThridLogin(String id, int type) {
        TRequest<TReqThirdLogin> request = new TRequest<>();
        TReqThirdLogin login = new TReqThirdLogin();
        login.setOpenId(id);
        login.setUserType(type);
        request.setParam(login, "1105", "1");
        Observable<TResponse_Login> tResponse_loginObservable = RetrofitClient.getInstance().getService(HttpService.class).ThirdLogin(request);
        ((BaseActivity) getActivity()).sendRequest(tResponse_loginObservable, tResponse_login -> {
            if (tResponse_login.getUser() != null) {
                SNApplication.userInfo.setUserInfo(tResponse_login.getUser());
                ShareUtils.putString("userInfo", new Gson().toJson(SNApplication.userInfo));
                bind(tv_bindph);
                onResume();
            } else {
                Intent intent = new Intent(getActivity(), ChangeUserinfoActivity.class);
                intent.putExtra("type", 1);
                intent.putExtra("openId", id);
                getActivity().startActivity(intent);
            }
        }, throwable -> ToastUtils.showToast(throwable.getMessage()));
        onResume();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (SNApplication.userInfo.getUserInfo() != null) {
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
            Glide.with(getActivity()).load(SNApplication.userInfo.getUserInfo().getUserInfo().getHeadImage()).apply(new RequestOptions().centerCrop().placeholder(R.mipmap.wd_touxiang).error(R.mipmap.wd_touxiang)).into(iv_head);
            tv_user_name.setText(SNApplication.userInfo.getUserInfo().getUserName());
        } else {
            ContentView.findViewById(R.id.ll_bdph).setOnClickListener(this);
            ContentView.findViewById(R.id.ll_bdwc).setOnClickListener(this);
            ContentView.findViewById(R.id.ll_bdqq).setOnClickListener(this);
            ContentView.findViewById(R.id.ll_bdwb).setOnClickListener(this);
            iv_head.setImageResource(R.mipmap.button_head_image);
            tv_user_name.setText("点击登录");
            unbind();
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


//    @Override
//    public void setUserVisibleHint(boolean isVisibleToUser) {
//        if (canFresh) {
//            if (SNApplication.userInfo.getUserInfo() != null) {
//                Log.i(TAG, isVisibleToUser + "1");
//                Glide.with(getActivity()).load(SNApplication.userInfo.getUserInfo().getUserInfo().getHeadImage()).apply(new RequestOptions().centerCrop().placeholder(R.mipmap.img_loading1)).into(iv_head);
//                tv_user_name.setText(SNApplication.userInfo.getUserInfo().getUserName());
//            } else {
//                Log.i(TAG, isVisibleToUser + "2");
//                iv_head.setImageResource(R.mipmap.button_head_image);
//                tv_user_name.setText("点击登录");
//            }
//        }
//    }
}
