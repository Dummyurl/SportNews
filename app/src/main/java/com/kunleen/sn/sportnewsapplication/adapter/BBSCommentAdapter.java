package com.kunleen.sn.sportnewsapplication.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.kunleen.sn.sportnewsapplication.R;
import com.kunleen.sn.sportnewsapplication.activity.UserDisplayActivity;
import com.kunleen.sn.sportnewsapplication.network.bean.Response_BBSComment;
import com.kunleen.sn.sportnewsapplication.utils.GlideCacheUtil;


public class BBSCommentAdapter extends ListBaseAdapter<Response_BBSComment.Rows> {
    public BBSCommentAdapter(Activity context) {
        super(context);
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.news_comment;
    }

    @Override
    public void onBindItemHolder(SuperViewHolder holder, int position) {
        Response_BBSComment.Rows rows = getDataList().get(position);
        holder.getView(R.id.ll_good).setVisibility(View.GONE);
        ((TextView) holder.getView(R.id.tv_comment_username)).setText(rows.getUserName());
        ((TextView) holder.getView(R.id.tv_comment_comment_time)).setText(rows.getCreateTime());
        ((TextView) holder.getView(R.id.tv_comment_content)).setText(rows.getTxt());
        GlideCacheUtil.LoadImage(mContext, holder.getView(R.id.cir_comment_head), rows.getHeadImage(), true);
        holder.getView(R.id.cir_comment_head).setOnClickListener(v -> {
            Intent intent = new Intent(mContext, UserDisplayActivity.class);
            intent.putExtra("uid", rows.getUserId() + "");
            intent.putExtra("uName", rows.getUserName());
            intent.putExtra("uImage", rows.getHeadImage());
            mContext.startActivity(intent);
        });
    }
}
