package com.kunleen.sn.sportnewsapplication.custom;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.kunleen.sn.sportnewsapplication.R;
import com.kunleen.sn.sportnewsapplication.utils.StringUtils;

/**
 * 自定义加载对话框
 * Created by lixingjun on 7/16/16.
 */
public class LoadingDialog extends Dialog {

    /**
     * 创建并显示等待对话框
     *
     * @param context
     * @return
     */
    public static LoadingDialog getInstance(Context context) {

        //使用带文字的构造体创建dialog
        LoadingDialog dialog = new LoadingDialog(context);
        //设置不能按屏幕或物理返回键退出
//        dialog.setCancelable(false);
        // 设置点击屏幕Dialog不消失
        dialog.setCanceledOnTouchOutside(false);
        //设置
//        dialog.setOnCancelListener(new OnCancelListener() {
//
//            @Override
//            public void onCancel(DialogInterface arg0) {
//                // TODO Auto-generated method stub
//
//                //执行取消操作，可以中断耗时操作
//            }
//        });

        //设置取消回调
//        if (cancelListener != null)
//            dialog.setOnCancelListener(cancelListener);
        dialog.setMessage("加载中...");

        return dialog;
    }

    public LoadingDialog(Context context, String message) {
        // TODO Auto-generated constructor stub
        //使用刚才定义好dialog风格
        super(context, R.style.loading_dialog_style);
    }

    public LoadingDialog(Context context) {
        this(context, "");
        // TODO Auto-generated constructor stub
    }

    public void setMessage(String msg) {
        //可以选择性添加等待对话框的内容文本
        TextView tv = (TextView) findViewById(R.id.loading_dialog_tv);
        if (tv != null) {
            if (!StringUtils.isEmptyString(msg)) {
                tv.setText(msg);
            } else {
                tv.setVisibility(View.GONE);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_loading);

    }
}
