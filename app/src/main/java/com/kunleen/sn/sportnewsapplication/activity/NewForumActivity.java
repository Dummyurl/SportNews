package com.kunleen.sn.sportnewsapplication.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.kunleen.sn.sportnewsapplication.R;
import com.kunleen.sn.sportnewsapplication.app.SNApplication;
import com.kunleen.sn.sportnewsapplication.base.BaseActivity;
import com.kunleen.sn.sportnewsapplication.custom.CircleImageView;
import com.kunleen.sn.sportnewsapplication.custom.TitleBarView;
import com.kunleen.sn.sportnewsapplication.glide.GlideApp;
import com.kunleen.sn.sportnewsapplication.network.RetrofitClient;
import com.kunleen.sn.sportnewsapplication.network.bean.ReqChangeImg;
import com.kunleen.sn.sportnewsapplication.network.bean.ReqCreateForum;
import com.kunleen.sn.sportnewsapplication.network.bean.TRequest;
import com.kunleen.sn.sportnewsapplication.network.bean.TResponse;
import com.kunleen.sn.sportnewsapplication.network.service.HttpService;
import com.kunleen.sn.sportnewsapplication.utils.AppUtils;
import com.kunleen.sn.sportnewsapplication.utils.DevicesUtils;
import com.kunleen.sn.sportnewsapplication.utils.FileUtils;
import com.kunleen.sn.sportnewsapplication.utils.StringUtils;
import com.kunleen.sn.sportnewsapplication.utils.ToastUtils;

import java.io.File;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;

public class NewForumActivity extends BaseActivity implements View.OnClickListener {
    private int type;
    private int ClassfyId = -1;
    private File tempFile;
    private Uri uri;
    CircleImageView cir_forum_icon, cir_forum_background;
    String iconCode = "", backgroundCode = "", cname;
    int PhotoType = 0;
    public static final int ICON = 1;
    public static final int BACKGROUND = 2;
    TextView tv_type;
    boolean isChange = false;
    private String cName, icon_url, background_url, description, cid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createCameraTempFile(savedInstanceState);
        setContentView(R.layout.activity_new_forum);
        DevicesUtils.setWindowStatusBarColor(this, R.color.white);
        initView();
        bindActivity();
        isChange = getIntent().getBooleanExtra("isChange", false);
        if (isChange) {
            cid = getIntent().getStringExtra("cid");
            cName = getIntent().getStringExtra("cname");
            EditText editText = findViewById(R.id.et_forum_name);
            editText.setText(cName);
            icon_url = getIntent().getStringExtra("icon_url");
            background_url = getIntent().getStringExtra("background_url");
            description = getIntent().getStringExtra("description");
            ((EditText) findViewById(R.id.et_forum_description)).setText(description);
            GlideApp.with(this).load(icon_url).fitCenter().into(cir_forum_icon);
            GlideApp.with(this).load(background_url).fitCenter().into(cir_forum_background);
            editText.setFocusable(false);
            editText.setFocusableInTouchMode(false); // user touches widget on phone with touch screen
            editText.setClickable(false);
            tv_type.setText("分类不可更换");
            findViewById(R.id.rl_forum_type).setOnClickListener(null);
        }
    }

    private void initView() {
        findViewById(R.id.rl_forum_icon).setOnClickListener(this);
        findViewById(R.id.rl_forum_background).setOnClickListener(this);
        findViewById(R.id.rl_forum_type).setOnClickListener(this);
        findViewById(R.id.btn_create).setOnClickListener(this);
        cir_forum_icon = findViewById(R.id.cir_forum_icon);
        cir_forum_background = findViewById(R.id.cir_forum_background);
        tv_type = findViewById(R.id.tv_type);
        TitleBarView titleBarView = findViewById(R.id.titlebar);
        titleBarView.setActivity(this);
        titleBarView.setTitle("创建圈子");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_forum_icon: {
                type = 2;
                PhotoType = ICON;
                AppUtils.uploadHeadImage(this, tempFile);
                break;
            }
            case R.id.rl_forum_background: {
                type = 2;
                PhotoType = BACKGROUND;
                AppUtils.uploadHeadImage(this, tempFile);
                break;
            }
            case R.id.rl_forum_type: {
                Intent intent = new Intent(NewForumActivity.this, TypeActivity.class);
                intent.putExtra("type", "type");
                startActivityForResult(intent, 200);
                break;
            }
            case R.id.btn_create: {
                if (AppUtils.isLogin(this)) {

                } else if (((EditText) findViewById(R.id.et_forum_name)).getText().toString().trim().equals("")) {
                    ToastUtils.showToast("请输入圈子名称");
                    return;
                } else if (((EditText) findViewById(R.id.et_forum_description)).getText().toString().trim().equals("")) {
                    ToastUtils.showToast("请输入圈子描述");
                    return;
                } else if (iconCode == null || iconCode.trim().equals("")) {
                    ToastUtils.showToast("请选择圈子图标");
                    return;
                } else if (backgroundCode == null || backgroundCode.trim().equals("")) {
                    ToastUtils.showToast("请选择圈子背景图");
                    return;
                } else if (cname == null || cname.trim().equals("") || ClassfyId == -1) {
                    ToastUtils.showToast("请选择圈子分类");
                    return;
                }
                doCreate();
                break;
            }
        }
    }

    private void doCreate() {
        TRequest<ReqCreateForum> request = new TRequest<>();
        ReqCreateForum create = new ReqCreateForum();
        create.setClassfyId(ClassfyId + "");
        create.setDescription(((EditText) findViewById(R.id.et_forum_description)).getText().toString().trim());
        create.setLittleImgCont(iconCode);
        create.setLittleImgId(0 + "");
        create.setLittleImgtype("png");
        create.setLittleImgName(System.currentTimeMillis() + "img");
        create.setTopImgName(System.currentTimeMillis() - 1 + "img");
        create.setTopImgCont(backgroundCode);
        create.setTopImgType("png");
        create.setUserId(SNApplication.userInfo.getUserInfo().getUid());
        if (isChange) {
            create.setcId(cid);
            create.setCname("");
            create.setClassfyName("");
        } else {
            create.setClassfyName(cname);
            create.setcId("0");
            create.setCname(((EditText) findViewById(R.id.et_forum_name)).getText().toString().trim());
        }
        request.setParamImg(create, "1124", "1");
        Observable<TResponse> observable = RetrofitClient.getInstance().getService(HttpService.class).CreatForum(request);
        sendRequest(observable, tResponse -> {
//                tResponse ->
            SNApplication.ForumRefrsh = true;
            ToastUtils.showToast(tResponse.getMessage());
            finish();
        }, throwable -> ToastUtils.showToast(throwable.getMessage()));
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        switch (requestCode) {
            case AppUtils.REQUEST_CAPTURE: //调用系统相机返回
                if (resultCode == RESULT_OK) {
                    AppUtils.gotoClipActivity(this, Uri.fromFile(tempFile), type);
                }
                break;
            case AppUtils.REQUEST_PICK:  //调用系统相册返回
                if (resultCode == RESULT_OK) {
                    Uri uri = intent.getData();
                    if (PhotoType == BACKGROUND) {
                        StringBuffer buffer = new StringBuffer();
                        GlideApp.with(this).load(uri).into(cir_forum_background);
                        String cropImagePath = FileUtils.getRealFilePathFromUri(getApplicationContext(), uri);
                        String compath = cropImagePath + "compressPic.jpg";
                        try {
                            buffer.append(FileUtils.encodeBase64File(FileUtils.compressImage(cropImagePath, compath, 30)));
                            backgroundCode = buffer.toString();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        AppUtils.gotoClipActivity(this, uri, type);
                    }
                }
                break;
            case AppUtils.REQUEST_CROP_PHOTO:  //剪切图片返回
                if (resultCode == RESULT_OK) {
                    final Uri uri = intent.getData();
                    if (uri == null) {
                        return;
                    }
                    this.uri = uri;
                    String cropImagePath = FileUtils.getRealFilePathFromUri(getApplicationContext(), uri);
                    try {
                        if (PhotoType == ICON) {
                            iconCode = FileUtils.encodeBase64File(cropImagePath);
                            GlideApp.with(this).load(uri).into(cir_forum_icon);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
//                    Bitmap bitMap = BitmapFactory.decodeFile(cropImagePath);
//                    doModifyHeadImage(cropImagePath);
                }
                break;
            case 200: {
                if (intent != null) {
                    ClassfyId = intent.getIntExtra("cid", -1);
                    cname = intent.getStringExtra("cname");
                    tv_type.setText(cname);
                }
                break;
            }
        }
    }

    /**
     * 创建调用系统照相机待存储的临时文件
     *
     * @param savedInstanceState
     */
    private void createCameraTempFile(Bundle savedInstanceState) {
        if (savedInstanceState != null && savedInstanceState.containsKey("myHeadFile")) {
            tempFile = (File) savedInstanceState.getSerializable("myHeadFile");
        } else {
            tempFile = new File(FileUtils.checkDirPath(Environment.getExternalStorageDirectory().getPath() + "/image/"),
                    System.currentTimeMillis() + ".jpg");
        }
    }

}
