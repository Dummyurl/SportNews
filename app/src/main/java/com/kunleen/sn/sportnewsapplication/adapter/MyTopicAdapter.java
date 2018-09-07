package com.kunleen.sn.sportnewsapplication.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.kunleen.sn.sportnewsapplication.R;
import com.kunleen.sn.sportnewsapplication.activity.BBSDetailActivity;
import com.kunleen.sn.sportnewsapplication.activity.ForumListActivity;
import com.kunleen.sn.sportnewsapplication.network.bean.TResponse_MyTopic;

/**
 * Created by ysy on 2018/3/15.
 */

public class MyTopicAdapter extends ListBaseAdapter<TResponse_MyTopic.Row> {
    public MyTopicAdapter(Activity context) {
        super(context);
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.news_topic_layout;
    }

    @Override
    public void onBindItemHolder(SuperViewHolder holder, int position) {
        holder.getView(R.id.iv_topic_state).setVisibility(View.GONE);
        if (mDataList.get(position).getStatus() == 0) {
            holder.getView(R.id.iv_topic_state).setVisibility(View.VISIBLE);
        } else if (mDataList.get(position).getStatus() == 2) {
            ((ImageView) holder.getView(R.id.iv_topic_state)).setImageResource(R.mipmap.icon_qz_weitongg);
        }
        ((TextView) holder.getView(R.id.tv_news_title)).setText(mDataList.get(position).getTitle());
    }
}
