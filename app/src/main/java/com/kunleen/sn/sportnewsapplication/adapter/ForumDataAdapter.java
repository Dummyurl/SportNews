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
import com.kunleen.sn.sportnewsapplication.network.bean.TResponse_ForumPage;
import com.kunleen.sn.sportnewsapplication.utils.GlideCacheUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by ysy on 2018/3/14.
 */

public class ForumDataAdapter extends RecyclerView.Adapter<SuperViewHolder> {
    public Activity mContext;
    public LayoutInflater mInflater;
    private List<TResponse_ForumPage.forumitem> mDataList = new ArrayList<>();
    onItemClickListener mOnItemClickListener;

    public ForumDataAdapter(Activity context) {
        mContext = context;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public SuperViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.layout_forum_rv_item, parent, false);
        return new SuperViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(SuperViewHolder holder, int position) {
        holder.getView(R.id.rv_forum_item).setOnClickListener(v -> {
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(v, position);
            }
        });
        if (mDataList.get(position).getcId() != -1) {
            GlideCacheUtil.LoadImageInWifi(mContext, holder.getView(R.id.iv_forum_icon), mDataList.get(position).getLittleImage());
        } else {
            ((ImageView) holder.getView(R.id.iv_forum_icon)).setImageResource(R.mipmap.icon_qz_cjxq);
        }
        ((TextView) holder.getView(R.id.tv_forum_name)).setText(mDataList.get(position).getCname());
        holder.getView(R.id.iv_state).setVisibility(View.GONE);
        if (mDataList.get(position).getStatus() == 0) {
            holder.getView(R.id.iv_state).setVisibility(View.VISIBLE);
        } else if (mDataList.get(position).getStatus() == 2) {
            holder.getView(R.id.iv_state).setVisibility(View.VISIBLE);
            ((ImageView) holder.getView(R.id.iv_state)).setImageResource(R.mipmap.icon_qz_wtg);
        }
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    public List<TResponse_ForumPage.forumitem> getDataList() {
        return mDataList;
    }

    public void setDataList(Collection<TResponse_ForumPage.forumitem> list) {
        this.mDataList.clear();
        this.mDataList.addAll(list);
    }

    public void addAll(Collection<TResponse_ForumPage.forumitem> list) {
        int lastIndex = this.mDataList.size();
        if (this.mDataList.addAll(list)) {
            notifyItemRangeInserted(lastIndex, list.size());
        }
    }

    public void remove(int position) {
        this.mDataList.remove(position);
        notifyItemRemoved(position);
        if (position != (getDataList().size())) { // 如果移除的是最后一个，忽略
            notifyItemRangeChanged(position, this.mDataList.size() - position);
        }
    }

    public void clear() {
        mDataList.clear();
        notifyDataSetChanged();
    }

    public interface onItemClickListener {
        void onItemClick(View view, int position);
    }


    public void setOnItemClickLitsener(onItemClickListener mOnItemClickLitsener) {
        mOnItemClickListener = mOnItemClickLitsener;
    }
}
