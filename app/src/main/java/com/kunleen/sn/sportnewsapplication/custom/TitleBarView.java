package com.kunleen.sn.sportnewsapplication.custom;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kunleen.sn.sportnewsapplication.R;
import com.kunleen.sn.sportnewsapplication.activity.HomeActivity;


public class TitleBarView extends LinearLayout {

    private LinearLayout mTopBack;
    public TextView mTvBack;
    public TextView mTvTitle;
    public ImageView mIvRight;
    private ImageView top_mid_icon;
    public TextView mTvRight;
    private Activity mActivity;
    private boolean isNotClickLeftButton = false;
    private View rl_bg;
    public TitleClickCallBack clickCallBack = null;

    public boolean isBackGoHome = false;

    public TitleBarView(Context context) {
        super(context);
    }

    public TitleBarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.title_bar_view, this, true);
        mTopBack = (LinearLayout) this.findViewById(R.id.top_back_btn);
        mTvBack = (TextView) this.findViewById(R.id.top_back_tv);
        rl_bg = this.findViewById(R.id.rl_bg);
        mTvTitle = (TextView) this.findViewById(R.id.top_title);
        mTvRight = (TextView) this.findViewById(R.id.top_right_tv);
        mIvRight = (ImageView) this.findViewById(R.id.top_right_btn);
        top_mid_icon = (ImageView) this.findViewById(R.id.top_mid_icon);
        mTopBack.setOnClickListener(onClickListener);
        mTvRight.setOnClickListener(onClickListener);
        mIvRight.setOnClickListener(onClickListener);
        isBackGoHome = false;
    }

    private OnClickListener onClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            switch (v.getId()) {
                case R.id.top_back_btn:
                    if (mActivity != null && !isNotClickLeftButton) {
                        if (isBackGoHome) {
                            mActivity.startActivity(new Intent(mActivity, HomeActivity.class));
                        } else {
                            mActivity.finish();
                        }
                    } else if (clickCallBack != null) {
                        clickCallBack.clickLeft();
                    }
                    break;
                case R.id.top_right_tv:
                    if (clickCallBack != null) {
                        clickCallBack.clickRight();
                    }
                    break;
                case R.id.top_right_btn:
                    if (clickCallBack != null) {
                        clickCallBack.clickRight();
                    }
                    break;
            }
        }
    };


    public void displayMiddleIcon() {
        top_mid_icon.setVisibility(View.VISIBLE);
        mTvTitle.setVisibility(View.GONE);
    }

    public void setActivity(Activity activity) {
        this.mActivity = activity;
    }

    public void setNotClickButton() {
        isNotClickLeftButton = true;
    }

    public void setTitle(String title) {
        mTvTitle.setText(title);
    }

    public void hideLeftButton(boolean d) {
        if (d) {
            mTopBack.setVisibility(View.GONE);
        } else {
            mTopBack.setVisibility(View.VISIBLE);
        }
    }

    public void hideRightButton(boolean d) {

        if (d) {
            mIvRight.setVisibility(View.GONE);
        } else {
            mIvRight.setVisibility(View.VISIBLE);
        }
    }

    public void hideMiddleText(boolean d) {

        if (d) {
            mTvTitle.setVisibility(View.GONE);
        } else {
            mTvTitle.setVisibility(View.VISIBLE);
        }
    }

    public void setClickCallBack(TitleClickCallBack callBack) {
        clickCallBack = callBack;

    }


    public void setRightText(String text) {
        mTvRight.setText(text);
        mTvRight.setVisibility(VISIBLE);
    }


    public interface TitleClickCallBack {
        void clickLeft();

        void clickRight();

    }

    @Override
    public void setAlpha(float alpha) {
        super.setAlpha(alpha);
    }

    public void setBackGoHome(boolean b) {
        isBackGoHome = b;
    }

}