package com.kunleen.sn.sportnewsapplication.activity;

import android.app.ActivityManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.kunleen.sn.sportnewsapplication.R;
import com.kunleen.sn.sportnewsapplication.adapter.BBSImageDataAdapter;
import com.kunleen.sn.sportnewsapplication.app.SNApplication;
import com.kunleen.sn.sportnewsapplication.base.BaseActivity;
import com.kunleen.sn.sportnewsapplication.custom.TitleBarView;
import com.kunleen.sn.sportnewsapplication.network.RetrofitClient;
import com.kunleen.sn.sportnewsapplication.network.bean.ReqNewBBS;
import com.kunleen.sn.sportnewsapplication.network.bean.TRequest;
import com.kunleen.sn.sportnewsapplication.network.service.HttpService;
import com.kunleen.sn.sportnewsapplication.utils.AppUtils;
import com.kunleen.sn.sportnewsapplication.utils.DevicesUtils;
import com.kunleen.sn.sportnewsapplication.utils.FileUtils;
import com.kunleen.sn.sportnewsapplication.utils.ToastUtils;
import com.mabeijianxi.jianxiexpression.ExpressionGridFragment;
import com.mabeijianxi.jianxiexpression.ExpressionShowFragment;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import me.nereo.multi_image_selector.MultiImageSelector;

import static com.kunleen.sn.sportnewsapplication.utils.DevicesUtils.hideKeyboard;

public class CreateBBSActivity extends BaseActivity implements View.OnClickListener, ExpressionGridFragment.ExpressionClickListener, ExpressionGridFragment.ExpressionDeleteClickListener {
    RecyclerView rec_photo_list;
    private boolean isEmogiShow = false;
    private FrameLayout fl_emogi;
    private ExpressionShowFragment expressionShowFragment;
    private TranslateAnimation hideAnimation, showAnimation;
    private EditText et_bbs_content, et_bbs_title;
    private int CurrentId;
    private ArrayList<String> mSelectPath = new ArrayList<>();
    private static final int REQUEST_IMAGE = 2;
    private BBSImageDataAdapter dataAdapter;
    private boolean keyboardShown;
    private int supportSoftInputHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_bbs);
        initView();
        initWindow(this);
    }

    private void initView() {
        initTitle();
        rec_photo_list = findViewById(R.id.rec_photo_list);
        initRec();
        findViewById(R.id.iv_expression).setOnClickListener(this);
        findViewById(R.id.iv_add_photo).setOnClickListener(this);
        fl_emogi = findViewById(R.id.fl_emogi);
        View bbs = findViewById(R.id.ll_bbs);
        et_bbs_title = findViewById(R.id.et_bbs_title);
        et_bbs_content = findViewById(R.id.et_bbs_content);
        et_bbs_title.setOnFocusChangeListener(mOnFocusChangeListener);
        et_bbs_content.setOnFocusChangeListener(mOnFocusChangeListener);
        bbs.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
//                Rect r = new Rect();
//                //获取当前界面可视部分
//                CreateBBSActivity.this.getWindow().getDecorView().getWindowVisibleDisplayFrame(r);
//                //获取屏幕的高度
//                int screenHeight = CreateBBSActivity.this.getWindow().getDecorView().getRootView().getHeight();
//                //此处就是用来获取键盘的高度的， 在键盘没有弹出的时候 此高度为0 键盘弹出的时候为一个正数
//                int heightDifference = screenHeight - r.bottom;
//                Log.d("heightDiff", "isEmogiShow: " + isEmogiShow + "\nheightDifference:" + heightDifference);
//                if (heightDifference > 0) {
//                    if (!isEmogiShow) {
//                        fl_emogi.setVisibility(View.GONE);
//                        isEmogiShow = false;
//                    }
//                } else {
//                    if (SNApplication.supportSoftInputHeight != DevicesUtils.getSupportSoftInputHeight(CreateBBSActivity.this)) {
//                        bbs.getViewTreeObserver().removeGlobalOnLayoutListener(this);
//                        SNApplication.supportSoftInputHeight = DevicesUtils.getSupportSoftInputHeight(CreateBBSActivity.this) + DevicesUtils.getVirtualBarHeigh(CreateBBSActivity.this);
//                        fl_emogi.getLayoutParams().height = SNApplication.supportSoftInputHeight;
//                        fl_emogi.requestLayout();
//                        bbs.getViewTreeObserver().addOnGlobalLayoutListener(this);
//                        fl_emogi.setVisibility(View.GONE);
//                        isEmogiShow = false;
//                    } else {
//                        fl_emogi.getLayoutParams().height = SNApplication.supportSoftInputHeight;
//                        fl_emogi.requestLayout();
//                    }
//                    if (isEmogiShow) {
//                        fl_emogi.setVisibility(View.GONE);
//                        isEmogiShow = false;
//                    }
//                }
                keyboardShown = DevicesUtils.isKeyboardShown(bbs);
                if (fl_emogi != null) {
                    if (keyboardShown) {
                        if (isEmogiShow) {
                            hideExpress();
                        }
                        if (supportSoftInputHeight != DevicesUtils.getSupportSoftInputHeight(CreateBBSActivity.this)) {
//                            iv_emogi.setImageResource(R.drawable.fabu_biaoqing_icon);
                            isEmogiShow = false;
                            bbs.getViewTreeObserver(). removeGlobalOnLayoutListener(this);
                            supportSoftInputHeight = DevicesUtils.getSupportSoftInputHeight(CreateBBSActivity.this);
                            fl_emogi.getLayoutParams().height = supportSoftInputHeight;
                            fl_emogi.requestLayout();
                            fl_emogi.setVisibility(View.GONE);
                            bbs.getViewTreeObserver().addOnGlobalLayoutListener(this);
                        }
                    } else {
                        if (!isEmogiShow) {
                            hideExpress();
                        }
                    }
                }
            }
        });
    }



    private void initRec() {
        GridLayoutManager manager = new GridLayoutManager(this, 3);
        rec_photo_list.setLayoutManager(manager);
        rec_photo_list.setNestedScrollingEnabled(false);
        dataAdapter = new BBSImageDataAdapter(this);
        rec_photo_list.setAdapter(dataAdapter);
        dataAdapter.getDataList().add("");
        dataAdapter.notifyDataSetChanged();
        dataAdapter.setmOnPlaceHolderClick(() -> {
            if (dataAdapter.getImageSize() < 3) {
                pickImage();
            } else {
                ToastUtils.showToast("最多只能添加三张图");
            }
        });
    }

    View.OnFocusChangeListener mOnFocusChangeListener = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (hasFocus) {
                CurrentId = v.getId();
            }
        }
    };

    private void initTitle() {
        TitleBarView titleBarView = findViewById(R.id.titlebar);
        titleBarView.setActivity(this);
        titleBarView.setTitle("发布新帖");
        titleBarView.setRightText("发布");
        titleBarView.findViewById(R.id.top_right_tv).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_expression:
                hideKeyboard(this);
                if (isEmogiShow) {
                    hideExpress();
                    isEmogiShow = false;
                } else {
                    new Handler().postDelayed(() -> replaceEmogi(), 200);
                }
                break;
            case R.id.iv_add_photo: {
                if (dataAdapter.getImageSize() < 3) {
                    pickImage();
                } else {
                    ToastUtils.showToast("最多只能添加三张图");
                }
                break;
            }
            case R.id.top_right_tv: {
                if (AppUtils.isLogin(CreateBBSActivity.this))
                    doCreat();
                break;
            }
        }
    }

    private void doCreat() {
        if (((EditText) findViewById(R.id.et_bbs_title)).getText().toString().trim().equals("")) {
            ToastUtils.showToast("标题不能为空");
            return;
        }
        if (((EditText) findViewById(R.id.et_bbs_content)).getText().toString().trim().equals("")) {
            ToastUtils.showToast("内容不能为空");
            return;
        }
        TRequest<ReqNewBBS> request = new TRequest<>();
        ReqNewBBS bbs = new ReqNewBBS();
        bbs.setTitle(((EditText) findViewById(R.id.et_bbs_title)).getText().toString());
        bbs.setInputTxt(((EditText) findViewById(R.id.et_bbs_content)).getText().toString().trim());
        bbs.setCid(getIntent().getStringExtra("cid"));
        bbs.setUserId(SNApplication.userInfo.getUserInfo().getUid() + "");
        bbs.setImgType("png");
        bbs.setImgCont(dataAdapter.getImageCode());
        request.setParamImg(bbs, "1125", "1");
        AppUtils.showLoadingDialog(this);
        sendRequest(RetrofitClient.getInstance().getService(HttpService.class).NewBBS(request), tResponse -> {
            AppUtils.hideLoadingDialog();
            ToastUtils.showToast("发表成功");
            setResult(RESULT_OK);
            finish();
        }, throwable -> {
            ToastUtils.showToast(throwable.getMessage());
            AppUtils.hideLoadingDialog();
        });
    }

    private void pickImage() {
        MultiImageSelector selector = MultiImageSelector.create(CreateBBSActivity.this);
        selector.showCamera(true);
        if (dataAdapter.getImageSize() < 2) {
            selector.count(3 - dataAdapter.getImageSize());
            selector.multi();
        } else {
            selector.single();
        }
        selector.start(CreateBBSActivity.this, REQUEST_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == RESULT_OK) {
                mSelectPath = data.getStringArrayListExtra(MultiImageSelector.EXTRA_RESULT);
                for (int i = 0; i < mSelectPath.size(); i++) {
                    Logger.t("imageList").d(mSelectPath.get(i));
                }
//                new Thread(() -> {
//                    toBitmap(mSelectPath);
//                    runOnUiThread(() -> ToastUtils.showToast("压缩完毕"));
//                }).start();
                dataAdapter.addAll(mSelectPath);
            }
        }
    }

    private List<Bitmap> toBitmap(ArrayList<String> mSelectPath) {
        List<Bitmap> bitmaps = new ArrayList<>();
        for (int i = 0; i < mSelectPath.size(); i++) {
            Bitmap bitmap = BitmapFactory.decodeFile(mSelectPath.get(i));
            bitmap = FileUtils.compressImage(bitmap, 50);
            bitmaps.add(bitmap);
        }
        return bitmaps;
    }


    /**
     * 表情显示
     */
    private void replaceEmogi() {
        isEmogiShow = true;
        showExpress();
        if (expressionShowFragment == null) {
            expressionShowFragment = ExpressionShowFragment.newInstance();
            getSupportFragmentManager().beginTransaction().replace(R.id.fl_emogi, ExpressionShowFragment.newInstance()).commit();
        }
    }

    private void showExpress() {
        fl_emogi.setVisibility(View.VISIBLE);

        if (showAnimation == null) {
            showAnimation = new TranslateAnimation(
                    Animation.RELATIVE_TO_SELF,
                    0,
                    Animation.RELATIVE_TO_SELF,
                    0,
                    Animation.RELATIVE_TO_SELF,
                    1,
                    Animation.RELATIVE_TO_SELF,
                    0);
            showAnimation.setFillAfter(false);
            showAnimation.setDuration(200);
            showAnimation.setInterpolator(new LinearInterpolator());
            showAnimation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    fl_emogi.clearAnimation();
                    isEmogiShow = true;
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        }
        fl_emogi.startAnimation(showAnimation);
    }

    private void hideExpress() {
        fl_emogi.setVisibility(View.GONE);
        isEmogiShow = false;
//        if (hideAnimation == null) {
//            hideAnimation = new TranslateAnimation(
//                    Animation.RELATIVE_TO_SELF,
//                    0,
//                    Animation.RELATIVE_TO_SELF,
//                    0,
//                    Animation.RELATIVE_TO_SELF,
//                    0,
//                    Animation.RELATIVE_TO_SELF,
//                    1);
//            hideAnimation.setFillAfter(false);
//            hideAnimation.setDuration(200);
//            hideAnimation.setInterpolator(new LinearInterpolator());
//            hideAnimation.setAnimationListener(new Animation.AnimationListener() {
//                @Override
//                public void onAnimationStart(Animation animation) {
//
//                }
//
//                @Override
//                public void onAnimationEnd(Animation animation) {
//                    fl_emogi.setVisibility(View.GONE);
//                    fl_emogi.clearAnimation();
//                }
//
//                @Override
//                public void onAnimationRepeat(Animation animation) {
//
//                }
//            });
//        }
//        if (isEmogiShow) {
//            fl_emogi.startAnimation(hideAnimation);
//        }
    }

    @Override
    public void onBackPressed() {
        if (isEmogiShow) {
            hideExpress();
            hideKeyboard(this);
            isEmogiShow = false;
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void expressionDeleteClick(View v) {
        ExpressionShowFragment.delete(findViewById(CurrentId));
    }

    /**
     * 这里必须实现表情点击后才能把具体表情传入edittext
     *
     * @param str
     */
    @Override
    public void expressionClick(String str) {
        ExpressionShowFragment.input(findViewById(CurrentId), str);
    }

}
