package com.kunleen.sn.sportnewsapplication.custom.HorizontalNavigationBar;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.kunleen.sn.sportnewsapplication.network.bean.Channel;


/**
 * 水平滚动条
 * <p>
 *
 * @author liuyk
 */
public class MyHorizontalNavigationBar extends HorizontalNavigationBar<Channel> {
    private int lastX;
    private int lastY;

    public MyHorizontalNavigationBar(Context paramContext) {
        super(paramContext);
    }

    public MyHorizontalNavigationBar(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
    }

    public MyHorizontalNavigationBar(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
        super(paramContext, paramAttributeSet, paramInt);
    }

    @Override
    public void renderingItemView(HorizontalNavigationItemView itemView, int index, int currentPosition) {
        Channel channel = getItem(index);
        itemView.setChannelTitle(channel.getChannelName());
//        itemView.setChannelPic(channel.getChannelPic(), channel.getChannelPic_checked());










































        itemView.setChecked(index == currentPosition);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        int x = (int) ev.getRawX();
        int y = (int) ev.getRawY();
        int dealtX = 0;
        int dealtY = 0;

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                dealtX = 0;
                dealtY = 0;
                // 保证子View能够接收到Action_move事件
                getParent().requestDisallowInterceptTouchEvent(true);
                break;
            case MotionEvent.ACTION_MOVE:
                dealtX += Math.abs(x - lastX);
                dealtY += Math.abs(y - lastY);

                // 这里是够拦截的判断依据是左右滑动，读者可根据自己的逻辑进行是否拦截
                if (dealtX >= dealtY) {
                    getParent().requestDisallowInterceptTouchEvent(true);
                } else {
                    getParent().requestDisallowInterceptTouchEvent(false);
                }
                lastX = x;
                lastY = y;
                break;
            case MotionEvent.ACTION_CANCEL:
                break;
            case MotionEvent.ACTION_UP:
                break;

        }
        return super.dispatchTouchEvent(ev);
    }
}
