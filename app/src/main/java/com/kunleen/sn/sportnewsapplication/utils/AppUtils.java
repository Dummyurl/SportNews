package com.kunleen.sn.sportnewsapplication.utils;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.kunleen.sn.sportnewsapplication.R;
import com.kunleen.sn.sportnewsapplication.activity.LoginActivity;
import com.kunleen.sn.sportnewsapplication.app.SNApplication;
import com.kunleen.sn.sportnewsapplication.custom.LoadingDialog;
import com.kunleen.sn.sportnewsapplication.exception.URLException;
import com.kunleen.sn.sportnewsapplication.utils.imageupload.ClipImageActivity;
import com.sina.weibo.sdk.api.ImageObject;

import java.io.File;
import java.util.List;
import java.util.Locale;

/**
 * Created by ysy on 2018/1/15.
 */

public class AppUtils {
    //请求相机
    public static final int REQUEST_CAPTURE = 100;
    //请求相册
    public static final int REQUEST_PICK = 101;
    //请求截图
    public static final int REQUEST_CROP_PHOTO = 102;

    private AppUtils() {
        throw new AssertionError();
    }

    /**
     * 创建多媒体（网页）消息对象。
     *
     * @return 多媒体（网页）消息对象。
     */
    public static ImageObject getWebpageObj(Bitmap bitmap) {
        ImageObject imageObject = new ImageObject();
        imageObject.setImageObject(bitmap);
        return imageObject;

    }

    public static boolean isLogin(Activity activity) {
        if (SNApplication.userInfo.getUserInfo() == null) {
            activity.startActivityForResult(new Intent(activity, LoginActivity.class), 101);
//            ToastUtils.showToast("请先登录！");
            return false;
        } else {
            return true;
        }
    }

    /**
     * 获取上下文对象
     *
     * @return 上下文对象
     */
    public static Context getContext() {
        return SNApplication.getContext();
    }

    /**
     * 获取全局handler
     *
     * @return 全局handler
     */
    public static Handler getHandler() {
        return SNApplication.getHandler();
    }

    /**
     * 获取主线程id
     *
     * @return 主线程id
     */
    public static int getMainThreadId() {
        return SNApplication.getMainThreadId();
    }

    /**
     * 获取版本名称
     */
    public static String getAppVersionName(Context context) {
        String versionName = "";
        try {
            // ---get the package info---
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            versionName = pi.versionName;
            if (versionName == null || versionName.length() <= 0) {
                return "";
            }
        } catch (Exception e) {
            Log.e("VersionInfo", "Exception", e);
        }
        return versionName;
    }

    /**
     * 获取版本号
     */
    public static int getAppVersionCode(Context context) {
        int versioncode = -1;
        try {
            // ---get the package info---
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            versioncode = pi.versionCode;
        } catch (Exception e) {
            Log.e("VersionInfo", "Exception", e);
        }
        return versioncode;
    }

    /**
     * 获取IMEI
     *
     * @param context
     * @return
     */
    public static String getIMEI(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context
                .TELEPHONY_SERVICE);
        return tm.getDeviceId();
    }

    /**
     * 显示软键盘
     */
    public static void openSoftInput(EditText et) {
        InputMethodManager inputMethodManager = (InputMethodManager) et.getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.showSoftInput(et, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * 隐藏软键盘
     */
    public static void hideSoftInput(EditText et) {
        InputMethodManager inputMethodManager = (InputMethodManager) et.getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(et.getWindowToken(), InputMethodManager
                .HIDE_NOT_ALWAYS);
    }

    /**
     * 获取SD卡路径
     *
     * @return 如果sd卡不存在则返回null
     */
    public static File getSDPath() {
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState().equals(Environment
                .MEDIA_MOUNTED);   //判断sd卡是否存在
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory();//获取跟目录
        }
        return sdDir;
    }

    /**
     * 安装文件
     *
     * @param data
     */
    public static void promptInstall(Context context, Uri data) {
        Intent promptInstall = new Intent(Intent.ACTION_VIEW)
                .setDataAndType(data, "application/vnd.android.package-archive");
        // FLAG_ACTIVITY_NEW_TASK 可以保证安装成功时可以正常打开 app
        promptInstall.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(promptInstall);
    }

    public static void copy2clipboard(Context context, String text) {
        ClipboardManager cm = (ClipboardManager) context.getSystemService(Context
                .CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("clip", text);
        cm.setPrimaryClip(clip);
    }

    /**
     * 判断是否运行在主线程
     *
     * @return true：当前线程运行在主线程
     * fasle：当前线程没有运行在主线程
     */
    public static boolean isRunOnUIThread() {
        // 获取当前线程id, 如果当前线程id和主线程id相同, 那么当前就是主线程
        int myTid = android.os.Process.myTid();
        if (myTid == getMainThreadId()) {
            return true;
        }
        return false;
    }

    /**
     * 显示已有的等待对话框
     *
     * @param msg
     */
    public static void showLoadingDialog(Context context, DialogInterface.OnCancelListener cancelListener, String msg) {
        try {
            if (SNApplication.loadingDialog == null) {
                SNApplication.loadingDialog = LoadingDialog.getInstance(context);
            }
            if (!SNApplication.loadingDialog.isShowing()) {
                SNApplication.loadingDialog.dismiss();
            }

            //设置取消回调
            if (cancelListener != null)
                SNApplication.loadingDialog.setOnCancelListener(cancelListener);
            if (!StringUtils.isEmptyString(msg))
                SNApplication.loadingDialog.setMessage(msg);
            SNApplication.loadingDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showLoadingDialog(Context context) {
        showLoadingDialog(context, null, null);
    }

    /**
     * 隐藏等待对话框
     *
     * @param
     */
    public static void hideLoadingDialog() {
        try {
            if (SNApplication.loadingDialog != null && SNApplication.loadingDialog.isShowing()) {
                SNApplication.loadingDialog.dismiss();
            }
            SNApplication.loadingDialog = null;
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 运行在主线程
     *
     * @param r 运行的Runnable对象
     */
    public static void runOnUIThread(Runnable r) {
        if (isRunOnUIThread()) {
            // 已经是主线程, 直接运行
            r.run();
        } else {
            // 如果是子线程, 借助handler让其运行在主线程
            getHandler().post(r);
        }
    }

    /**
     * 上传图片的方式选择界面，相机，相册
     */
    public static void uploadHeadImage(final Activity activity, final File tempFile) {
        View view = LayoutInflater.from(activity).inflate(R.layout.popup_win_select, null);
        TextView btnCarema = view.findViewById(R.id.btn_camera);
        TextView btnPhoto = view.findViewById(R.id.btn_photo);
        TextView btnCancel = view.findViewById(R.id.btn_cancel);
        final PopupWindow popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setBackgroundDrawable(activity.getResources().getDrawable(android.R.color.transparent));
        popupWindow.setOutsideTouchable(true);
        View parent = LayoutInflater.from(activity).inflate(R.layout.activity_main, null);
        popupWindow.showAtLocation(parent, Gravity.BOTTOM, 0, 0);
        btnCarema.setOnClickListener(v -> {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempFile));
            activity.startActivityForResult(intent, REQUEST_CAPTURE);
            popupWindow.dismiss();
        });
        btnPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到调用系统图库
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                activity.startActivityForResult(Intent.createChooser(intent, "请选择图片"), REQUEST_PICK);
                popupWindow.dismiss();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
    }

    /**
     * 打开截图界面
     *
     * @param uri
     */
    public static void gotoClipActivity(Activity activity, Uri uri, int type) {
        if (uri == null) {
            return;
        }
        Intent intent = new Intent();
        intent.setClass(activity, ClipImageActivity.class);
        intent.putExtra("type", type);
        intent.setData(uri);
        activity.startActivityForResult(intent, AppUtils.REQUEST_CROP_PHOTO);
    }

    /* 判断WIFI网络是否可用
    *
    * @param context
    * @return
   */
    public static boolean isWifiConnected(Context context) {
        if (context != null) {
// 获取手机所有连接管理对象(包括对wi-fi,net等连接的管理)
            ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
// 获取NetworkInfo对象
            NetworkInfo networkInfo = manager.getActiveNetworkInfo();
//判断NetworkInfo对象是否为空 并且类型是否为WIFI
            if (networkInfo != null && networkInfo.getType() == ConnectivityManager.TYPE_WIFI)
                return networkInfo.isAvailable();
        }
        return false;
    }

    public static boolean isWeiboInstalled(@NonNull Context context) {
        PackageManager pm;
        if ((pm = context.getApplicationContext().getPackageManager()) == null) {
            return false;
        }
        List<PackageInfo> packages = pm.getInstalledPackages(0);
        for (PackageInfo info : packages) {
            String name = info.packageName.toLowerCase(Locale.ENGLISH);
            if ("com.sina.weibo".equals(name)) {
                return true;
            }
        }
        return false;
    }

    public static void OutToSysBrowser(String url, Context context) {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        try {
            if (url != null && !url.equals("")) {
                Uri content_url = Uri.parse(url);
                intent.setData(content_url);
                context.startActivity(intent);
            }
        } catch (Exception e) {
            Log.i("URLException", e.getMessage());
        }
    }
}
