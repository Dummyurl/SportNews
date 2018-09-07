package com.kunleen.sn.sportnewsapplication.custom.HorizontalNavigationBar;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Checkable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kunleen.sn.sportnewsapplication.R;


/**
 * 水平滚动栏的子Item
 * <p>
 *
 * @author liuyk
 */
public class HorizontalNavigationItemView extends LinearLayout implements Checkable {
    //分割线颜色
    protected int mSplitColor = Color.parseColor("#ff0bb731");

    private View mItemView;
    private View mChannelSplit;
    private TextView mChannelTitle;
    //    private ImageView mChannelPic;
    private int channelPic;
    private int ChannelPic_checked;
    /**
     * 是否有下划线
     */
    private boolean isChannelSplit;

    /**
     * 是否选中
     */
    private boolean isChecked;

    public HorizontalNavigationItemView(Context context) {
        this(context, null);
    }

    public HorizontalNavigationItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {
        //attachToRoot(默认true)决定了，root是否 是resource的父对象
        this.mItemView = LayoutInflater.from(getContext()).inflate(R.layout.horizontal_bar_layout, this);
        mChannelTitle = (TextView) this.mItemView.findViewById(R.id.horizontal_bar_channel_title);
//        mChannelPic = (ImageView) this.mItemView.findViewById(R.id.horizontal_bar_channel_pic);
        mChannelSplit = this.mItemView.findViewById(R.id.horizontal_bar_channel_split);
    }

    /**
     * 设置标题
     *
     * @param channelTitle 标题
     */
    public void setChannelTitle(String channelTitle) {
        mChannelTitle.setText(channelTitle);

    }

    public void setChannelPic(int resId, int resId_checked) {
        channelPic = resId;
        ChannelPic_checked = resId_checked;
//        mChannelPic.setVisibility(VISIBLE);
//        mChannelPic.setImageResource(resId);

    }

    public boolean isChannelSplit() {
        return isChannelSplit;
    }

    public void setChannelSplit(boolean channelSplit) {
        isChannelSplit = channelSplit;
    }

    @Override
    public void setChecked(boolean checked) {
        isChecked = checked;
        if (isChannelSplit) {//是否有下划线
            mChannelSplit.setVisibility(View.VISIBLE);
//            mChannelPic.setVisibility(View.GONE);
        } else {
            mChannelTitle.setVisibility(View.GONE);
            mChannelSplit.setVisibility(View.GONE);
        }
        if (checked) {//是否被选中
            mChannelSplit.setBackgroundColor(mSplitColor);
            mChannelTitle.setTextColor(mSplitColor);
//            mChannelPic.setImageResource(ChannelPic_checked);
        } else {
            mChannelTitle.setTextColor(Color.BLACK);
//            mChannelPic.setImageResource(channelPic);
            mChannelSplit.setVisibility(INVISIBLE);
        }
    }

    public void setSplitColor(int resId) {
        mSplitColor = resId;
    }

    @Override
    public boolean isChecked() {
        return isChecked;
    }

    @Override
    public void toggle() {
        setChecked(!isChecked);
    }
}
