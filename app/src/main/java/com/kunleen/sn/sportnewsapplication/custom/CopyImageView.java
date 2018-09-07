package com.kunleen.sn.sportnewsapplication.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by ysy on 2016/11/14.
 */

public class CopyImageView extends ImageView implements Cloneable{

    public CopyImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public CopyImageView(Context context) {
        super(context);
    }

    public CopyImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public CopyImageView clone()  {
        try {
            return ((CopyImageView) super.clone());
        }catch (CloneNotSupportedException e){
            return null;
        }
    }
}
