package com.kunleen.sn.sportnewsapplication.login;

/**
 * qq登录协助类
 * Created by lixingjun on 8/4/16.
 */
public class QQLoginHelper {

//    private final static String QQ_APP_ID = "1105477853";//记得androidManifest.xml文件也需要更新
//
//    //qq第三方登录
//    public Tencent mTencent;
//    public IUiListener loginListener;
//    private IUiListener userInfoListener;
//    private String scope;
//    private UserInfo userInfo;
//    private String QQ_openID;
//
//
//    private Activity activity = null;
//
//    public QQLoginCallBack qqLoginCallBack = null;
//
//    /**
//     * qq登录 begin
//     ****/
//    public void login(LoginActivity act, QQLoginCallBack callBack) {
//        this.activity = act;
//        qqLoginCallBack = callBack;
//        mTencent = Tencent.createInstance(QQ_APP_ID, activity);
//        //要所有权限，不用再次申请增量权限，这里不要设置成get_user_info,add_t
//        scope = "all";
//        loginListener = new IUiListener() {
//
//            @Override
//            public void onError(UiError arg0) {
//                // TODO Auto-generated method stub
//                if (qqLoginCallBack != null) {
//                    qqLoginCallBack.onFailure();
//                }
//            }
//
//            /**
//             * {"ret":0,"pay_token":"D3D678728DC580FBCDE15722B72E7365",
//             * "pf":"desktop_m_qq-10000144-android-2002-",
//             * "query_authority_cost":448,
//             * "authority_cost":-136792089,
//             * "openid":"015A22DED93BD15E0E6B0DDB3E59DE2D",
//             * "expires_in":7776000,
//             * "pfkey":"6068ea1c4a716d4141bca0ddb3df1bb9",
//             * "msg":"",
//             * "access_token":"A2455F491478233529D0106D2CE6EB45",
//             * "login_cost":499}
//             */
//            @Override
//            public void onComplete(Object value) {
//                // TODO Auto-generated method stub
//
//                System.out.println("有数据返回..");
//                if (value == null) {
//                    return;
//                }
//
//                try {
//                    String jsonStr = value.toString();
//                    JSONObject jo = JSON.parseObject(jsonStr);
//                    String msg = jo.getString("msg");
//
//                    System.out.println("json=" + String.valueOf(jo));
//
//                    System.out.println("msg=" + msg);
////                    if ("sucess".equals(msg)) {
////                    Toast.makeText(context, "登录成功",
////                            Toast.LENGTH_LONG).show();
//
//                    String openID = jo.getString("openid");
//                    String accessToken = jo.getString("access_token");
//                    String expires = jo.getString("expires_in");
//                    mTencent.setOpenId(openID);
//                    mTencent.setAccessToken(accessToken, expires);
//                    userInfo = new UserInfo(activity, mTencent.getQQToken());
//                    userInfo.getUserInfo(userInfoListener);
////                    }
//
//                } catch (Exception e) {
//                    // TODO: handle exception
//                }
//
//            }
//
//            @Override
//            public void onCancel() {
//                // TODO Auto-generated method stub
//                if (qqLoginCallBack != null) {
//                    qqLoginCallBack.onFailure();
//                }
//            }
//        };
//
//        userInfoListener = new IUiListener() {
//
//            @Override
//            public void onError(UiError arg0) {
//                // TODO Auto-generated method stub
//                if (qqLoginCallBack != null) {
//                    qqLoginCallBack.onFailure();
//                }
//            }
//
//            /**
//             * {"ret":0,"msg":"","is_lost":0,"nickname":"天边","gender":"男","province":"北京","city":"通州","figureurl":"http:\/\/qzapp.qlogo.cn\/qzapp\/1105447927\/05891C059A37B1732F7CF079E8877C25\/30","figureurl_1":"http:\/\/qzapp.qlogo.cn\/qzapp\/1105447927\/05891C059A37B1732F7CF079E8877C25\/50","figureurl_2":"http:\/\/qzapp.qlogo.cn\/qzapp\/1105447927\/05891C059A37B1732F7CF079E8877C25\/100","figureurl_qq_1":"http:\/\/q.qlogo.cn\/qqapp\/1105447927\/05891C059A37B1732F7CF079E8877C25\/40","figureurl_qq_2":"http:\/\/q.qlogo.cn\/qqapp\/1105447927\/05891C059A37B1732F7CF079E8877C25\/100","is_yellow_vip":"0","vip":"0","yellow_vip_level":"0","level":"0","is_yellow_year_vip":"0"}
//             */
//            @Override
//            public void onComplete(Object arg0) {
//                // TODO Auto-generated method stub
//                if (arg0 == null) {
//                    return;
//                }
//                try {
//                    String jsonStr = arg0.toString();
////                    JSONObject jo = (JSONObject) arg0;
//                    JSONObject jo = JSON.parseObject(jsonStr);
//                    int ret = jo.getIntValue("ret");
//                    System.out.println("json=" + String.valueOf(jo));
//                    if (ret == 100030) {
//                        //权限不够，需要增量授权
//                        Runnable r = new Runnable() {
//                            public void run() {
//                                mTencent.reAuth(activity, "all", new IUiListener() {
//
//                                    @Override
//                                    public void onError(UiError arg0) {
//                                        // TODO Auto-generated method stub
//                                    }
//
//                                    @Override
//                                    public void onComplete(Object arg0) {
//                                        // TODO Auto-generated method stub
//                                    }
//
//                                    @Override
//                                    public void onCancel() {
//                                        // TODO Auto-generated method stub
//
//                                    }
//                                });
//                            }
//                        };
//
//                        activity.runOnUiThread(r);
//                    } else {
//                        String openid = mTencent.getOpenId();
//                        String nickName = jo.getString("nickname");
//                        String url = jo.getString("figureurl_qq_1");
//                        String gender = jo.getString("gender");
//                        Toast.makeText(activity, "你好，" + nickName, Toast.LENGTH_LONG).show();
//                        if (qqLoginCallBack != null) {
//                            qqLoginCallBack.onSuccess(openid, nickName, url);
//                        }
//                    }
//
//                } catch (Exception e) {
//                    // TODO: handle exception
//                }
//
//
//            }
//
//            @Override
//            public void onCancel() {
//                // TODO Auto-generated method stub
//                if (qqLoginCallBack != null) {
//                    qqLoginCallBack.onFailure();
//                }
//            }
//        };
//
//        if (!mTencent.isSessionValid()) {
//            mTencent.login(activity, scope, loginListener);
//        }
//    }
//
//
//    public interface QQLoginCallBack {
//        void onSuccess(String id, String nickName, String headUrl);
//
//        void onFailure();
//    }
//    /***qq登录 end***/
}
