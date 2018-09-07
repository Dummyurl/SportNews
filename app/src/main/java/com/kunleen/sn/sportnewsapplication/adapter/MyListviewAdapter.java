package com.kunleen.sn.sportnewsapplication.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.google.gson.Gson;
import com.kunleen.sn.sportnewsapplication.R;
import com.kunleen.sn.sportnewsapplication.activity.ForumListActivity;
import com.kunleen.sn.sportnewsapplication.activity.MoreForumActivity;
import com.kunleen.sn.sportnewsapplication.activity.NewForumActivity;
import com.kunleen.sn.sportnewsapplication.network.bean.TResponse;
import com.kunleen.sn.sportnewsapplication.network.bean.TResponse_ForumPage;
import com.kunleen.sn.sportnewsapplication.network.bean.TResponse_NewsDetail;
import com.kunleen.sn.sportnewsapplication.utils.AppUtils;
import com.kunleen.sn.sportnewsapplication.utils.DevicesUtils;
import com.kunleen.sn.sportnewsapplication.utils.ToastUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ysy on 2018/2/24.
 */

public class MyListviewAdapter extends BaseAdapter {
    private Activity context;
    private List<TResponse_ForumPage.forum> data = new ArrayList<>();
    Map<Integer, ForumDataAdapter> recLoad = new HashMap<>();

    public void setData(List<TResponse_ForumPage.forum> forums) {
        data.clear();
        data.addAll(forums);
        recLoad = new HashMap<>();
        notifyDataSetChanged();
    }

    public MyListviewAdapter(Activity context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public TResponse_ForumPage.forum getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.layout_forum_item, viewGroup, false);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        ((TextView) viewHolder.getView(R.id.tv_forum_name)).setText(data.get(position).getClassfyName());
        RecyclerView rv = (RecyclerView) viewHolder.getView(R.id.rv_forum);
        if (recLoad.get(position) == null) {
            initRec(rv, position);
        } else {
            rv.setAdapter(recLoad.get(position));
        }
        if (data.get(position).getClassfyId() == -1) {
            viewHolder.getView(R.id.tv_more_forum).setVisibility(View.GONE);
        } else {
            viewHolder.getView(R.id.tv_more_forum).setVisibility(View.VISIBLE);
        }
        viewHolder.getView(R.id.tv_more_forum).setOnClickListener(v -> {
            if (DevicesUtils.isNetworkAvailable(context)) {
                Intent intent = new Intent(context, MoreForumActivity.class);
                intent.putExtra("classy", data.get(position).getClassfyId());
                context.startActivity(intent);
            }else{
                ToastUtils.showToast("网络状况不佳，请连接网络后再试！");
            }
        });
        return view;
    }

    public void clear() {
        data.clear();
        notifyDataSetChanged();
    }

    private void initRec(RecyclerView rv, int position) {
        GridLayoutManager manager = new GridLayoutManager(context, 4);
        rv.setLayoutManager(manager);
        rv.setNestedScrollingEnabled(false);
        ForumDataAdapter dataAdapter = new ForumDataAdapter(context);
        rv.setAdapter(dataAdapter);
        dataAdapter.setDataList(data.get(position).getCirclelist());
        dataAdapter.setOnItemClickLitsener((view, position1) -> {
            if (data.get(position).getCirclelist().get(position1).getcId() != -1) {
                Intent intent = new Intent(context, ForumListActivity.class);
                intent.putExtra("cid", data.get(position).getCirclelist().get(position1).getcId() + "");
                intent.putExtra("classfyId", data.get(position).getCirclelist().get(position1).getClassfyId() + "");
                context.startActivity(intent);
            } else {
                if (AppUtils.isLogin((Activity) context)) {
                    Intent intent = new Intent(context, NewForumActivity.class);
                    context.startActivity(intent);
                }
            }
        });
        recLoad.put(position, dataAdapter);
    }


    public class ViewHolder {
        View layoutView;
        Map<Integer, View> mapCache = new HashMap<>();

        public ViewHolder(View layoutView) {
            this.layoutView = layoutView;
        }

        public View getView(int id) {
            View view = null;
            if (mapCache.containsKey(id)) {
                view = mapCache.get(id);
            } else {
                view = layoutView.findViewById(id);
                mapCache.put(id, view);
            }
            return view;
        }
    }
}
