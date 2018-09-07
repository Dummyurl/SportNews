package com.kunleen.sn.sportnewsapplication.custom;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.kunleen.sn.sportnewsapplication.R;
import com.kunleen.sn.sportnewsapplication.fragment.FirstPageFragment;
import com.kunleen.sn.sportnewsapplication.utils.DevicesUtils;

/**
 * 自定义popupWindow
 */

public class SharePopupWindow extends PopupWindow {
    private View mView;
    private View ll;
    Activity mContext;

    public SharePopupWindow(Activity context, View.OnClickListener itemsOnClick) {
        super(context);
        mContext = context;
        initView(context, itemsOnClick);
    }

    private void initView(final Activity context, View.OnClickListener itemsOnClick) {
        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mView = mInflater.inflate(R.layout.sharepop_layout, null);
        mView.findViewById(R.id.rl_share).setOnClickListener(v -> animDis());
        LinearLayout wechatquan = mView.findViewById(R.id.ll_weichat);
        LinearLayout friendster = mView.findViewById(R.id.ll_wechatf);
        LinearLayout weibo = mView.findViewById(R.id.ll_weibo);
        LinearLayout qqFriend = mView.findViewById(R.id.ll_qq);
        TextView canaleTv = mView.findViewById(R.id.share_cancle);
        ll = mView.findViewById(R.id.ll);
        canaleTv.setOnClickListener(v -> animDis());
        //设置按钮监听
        wechatquan.setOnClickListener(itemsOnClick);
        friendster.setOnClickListener(itemsOnClick);
        weibo.setOnClickListener(itemsOnClick);
        qqFriend.setOnClickListener(itemsOnClick);
        //设置SelectPicPopupWindow的View
        this.setContentView(mView);
        //设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(WindowManager.LayoutParams.FILL_PARENT);
        //设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        //设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        //设置PopupWindow可触摸
        this.setTouchable(true);
        //设置非PopupWindow区域是否可触摸
        this.setOutsideTouchable(true);
        //设置SelectPicPopupWindow弹出窗体动画效果'
        TranslateAnimation animation = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF,
                0,
                Animation.RELATIVE_TO_SELF,
                0,
                Animation.RELATIVE_TO_SELF,
                1,
                Animation.RELATIVE_TO_SELF,
                0);
        animation.setFillAfter(false);
        animation.setDuration(200);
        animation.setInterpolator(new LinearInterpolator());
        ll.startAnimation(animation);
//        this.setAnimationStyle(R.style.popwin_anim_style);
        //实例化一个ColorDrawable颜色为半透明
//        ColorDrawable dw = new ColorDrawable(0x4f000000);
//        //设置SelectPicPopupWindow弹出窗体的背景
//        this.setBackgroundDrawable(dw);
//        backgroundAlpha(context, 0.5f);//0.0-1.0

    }

    void animDis() {
        TranslateAnimation animation = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF,
                0,
                Animation.RELATIVE_TO_SELF,
                0,
                Animation.RELATIVE_TO_SELF,
                0,
                Animation.RELATIVE_TO_SELF,
                1);
        animation.setFillAfter(false);
        animation.setDuration(200);
        animation.setInterpolator(new LinearInterpolator());
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                dismiss();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        ll.startAnimation(animation);
        setOnDismissListener(() -> {
            DevicesUtils.setWindowStatusBarColor(mContext, 0xff000000);
        });
    }

    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     */
    public void backgroundAlpha(Activity context, float bgAlpha) {
        WindowManager.LayoutParams lp = context.getWindow().getAttributes();
        lp.alpha = bgAlpha;
        context.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        context.getWindow().setAttributes(lp);
    }
}