package com.kunleen.sn.sportnewsapplication.adapter;

import android.app.Activity;
import android.content.Context;
import android.widget.TextView;

import com.kunleen.sn.sportnewsapplication.R;
import com.kunleen.sn.sportnewsapplication.network.bean.Response_ForumList;


public class ForumListAdapter extends ListBaseAdapter<Response_ForumList.Row> {
    public ForumListAdapter(Activity context) {
        super(context);
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.layout_forum_list_item;
    }

    @Override
    public void onBindItemHolder(SuperViewHolder holder, int position) {
        Response_ForumList.Row rows = getDataList().get(position);
        ((TextView) holder.getView(R.id.tv_forum_title)).setText(rows.getTitle());
        ((TextView) holder.getView(R.id.tv_forum_good)).setText(rows.getGood() + "");
        ((TextView) holder.getView(R.id.tv_forum_bad)).setText(rows.getBad() + "");
        ((TextView) holder.getView(R.id.tv_forum_comment)).setText(rows.getReply() + "");
    }
}
