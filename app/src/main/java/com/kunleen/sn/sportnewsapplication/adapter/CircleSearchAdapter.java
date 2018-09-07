package com.kunleen.sn.sportnewsapplication.adapter;

import android.app.Activity;
import android.widget.TextView;

import com.kunleen.sn.sportnewsapplication.R;
import com.kunleen.sn.sportnewsapplication.network.bean.CircleBean;
import com.kunleen.sn.sportnewsapplication.utils.GlideCacheUtil;

/**
 * Created by ysy on 2018/4/10.
 */

public class CircleSearchAdapter extends ListBaseAdapter<CircleBean> {
    public CircleSearchAdapter(Activity context) {
        super(context);
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.layout_forum_rv_item;
    }

    @Override
    public void onBindItemHolder(SuperViewHolder holder, int position) {
        GlideCacheUtil.LoadImage(mContext, holder.getView(R.id.iv_forum_icon), mDataList.get(position).getLittleImage(), true);
        ((TextView) holder.getView(R.id.tv_forum_name)).setText(mDataList.get(position).getCname());
    }
}
