package com.kunleen.sn.sportnewsapplication.adapter;

import android.app.Activity;
import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.kunleen.sn.sportnewsapplication.R;
import com.kunleen.sn.sportnewsapplication.network.bean.Response_FollowBeFollow;
import com.kunleen.sn.sportnewsapplication.utils.GlideCacheUtil;

/**
 * Created by ysy on 2018/3/27.
 */

public class FollowAdapter extends ListBaseAdapter<Response_FollowBeFollow.Rows> {
    public FollowAdapter(Activity context) {
        super(context);
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.layout_forum_rv_item;
    }

    @Override
    public void onBindItemHolder(SuperViewHolder holder, int position) {
        Response_FollowBeFollow.Rows rows = getDataList().get(position);
        ((TextView) holder.getView(R.id.tv_forum_name)).setText(rows.getUserName() + "");
        GlideCacheUtil.LoadImage(mContext, holder.getView(R.id.iv_forum_icon), rows.getHeadImage(), true);
    }
}
