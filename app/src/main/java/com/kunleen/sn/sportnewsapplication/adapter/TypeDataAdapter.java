package com.kunleen.sn.sportnewsapplication.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kunleen.sn.sportnewsapplication.R;
import com.kunleen.sn.sportnewsapplication.network.bean.Response_ForumType;
import com.kunleen.sn.sportnewsapplication.network.bean.TResponse;
import com.kunleen.sn.sportnewsapplication.network.bean.TResponse_ForumPage;
import com.kunleen.sn.sportnewsapplication.utils.GlideCacheUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by ysy on 2018/3/14.
 */

public class TypeDataAdapter extends ListBaseAdapter<Response_ForumType.mList> {

    public TypeDataAdapter(Activity context) {
        super(context);
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.layout_forum_rv_item;
    }

    @Override
    public void onBindItemHolder(SuperViewHolder holder, int position) {
        GlideCacheUtil.LoadImage(mContext, holder.getView(R.id.iv_forum_icon), mDataList.get(position).getImageUrl(), false);
        ((TextView) holder.getView(R.id.tv_forum_name)).setText(mDataList.get(position).getCname());
    }


}
