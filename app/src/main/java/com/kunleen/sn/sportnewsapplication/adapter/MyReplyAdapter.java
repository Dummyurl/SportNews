package com.kunleen.sn.sportnewsapplication.adapter;

import android.app.Activity;
import android.content.Context;
import android.widget.TextView;

import com.kunleen.sn.sportnewsapplication.R;
import com.kunleen.sn.sportnewsapplication.network.bean.TResponse_MyReply;

/**
 * Created by ysy on 2018/3/15.
 */

public class MyReplyAdapter extends ListBaseAdapter<TResponse_MyReply.Row> {
    public MyReplyAdapter(Activity context) {
        super(context);
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.news_reply_layout;
    }

    @Override
    public void onBindItemHolder(SuperViewHolder holder, int position) {
        ((TextView) holder.getView(R.id.tv_reply)).setText(mDataList.get(position).getTxt());
        ((TextView) holder.getView(R.id.tv_reply_title)).setText("原帖：" + mDataList.get(position).getNtitle());
    }
}
