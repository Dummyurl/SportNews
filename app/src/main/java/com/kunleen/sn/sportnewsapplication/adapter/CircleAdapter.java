package com.kunleen.sn.sportnewsapplication.adapter;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.kunleen.sn.sportnewsapplication.R;
import com.kunleen.sn.sportnewsapplication.network.bean.Response_FirstCircle;
import com.kunleen.sn.sportnewsapplication.utils.GlideCacheUtil;

/**
 * Created by ysy on 2018/3/30.
 */

public class CircleAdapter extends AbsRecyclerAdapter<Response_FirstCircle.Rows> {
    public CircleAdapter(Context context, int resId) {
        super(context, resId);
    }

    @Override
    public void bindDatas(MyViewHolder holder, Response_FirstCircle.Rows data) {
        ((TextView) holder.getView(R.id.tv_forum_name)).setText(data.getCname());
        GlideCacheUtil.LoadImage(context, (ImageView) holder.getView(R.id.iv_forum_icon), data.getLittleImage(), false);
    }
}
