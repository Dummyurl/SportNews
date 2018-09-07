package com.kunleen.sn.sportnewsapplication.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.kunleen.sn.sportnewsapplication.R;
import com.kunleen.sn.sportnewsapplication.utils.FileUtils;
import com.kunleen.sn.sportnewsapplication.utils.GlideCacheUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by ysy on 2018/3/14.
 */

public class BBSImageDataAdapter extends RecyclerView.Adapter<SuperViewHolder> {
    public Context mContext;
    public LayoutInflater mInflater;
    private List<String> mDataList = new ArrayList<>();
    onItemClickListener mOnItemClickListener;
    onPlaceHolderClick mOnPlaceHolderClick;

    public BBSImageDataAdapter(Context context) {
        mContext = context;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setmOnPlaceHolderClick(onPlaceHolderClick onPlaceHolderClick) {
        mOnPlaceHolderClick = onPlaceHolderClick;
    }

    @Override
    public SuperViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.layout_bbsphoto_item, parent, false);
        return new SuperViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(SuperViewHolder holder, int position) {
        holder.getView(R.id.rl_bbs_photo).setOnClickListener(v -> {
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(v, position);
            }
        });

        if (!mDataList.get(position).equals("")) {
            holder.getView(R.id.iv_cancel).setVisibility(View.VISIBLE);
            GlideCacheUtil.LoadImage(mContext, holder.getView(R.id.iv_photo), mDataList.get(position), false);
            holder.getView(R.id.iv_photo).setOnClickListener(null);
            holder.getView(R.id.iv_cancel).setOnClickListener(v -> {
                mDataList.remove(position);
                clearPlaceHolder();
                if (getImageSize() == 0) {
                    mDataList.clear();
                }
                if (getImageSize() < 3) {
                    mDataList.add("");
                }
                notifyDataSetChanged();
            });
        } else {
            ((ImageView) holder.getView(R.id.iv_photo)).setImageResource(R.mipmap.button_cjxt_tianjiatp);
            holder.getView(R.id.iv_photo).setOnClickListener(v -> {
                if (mOnPlaceHolderClick != null) {
                    mOnPlaceHolderClick.onPlaceHolderClick();
                }
            });
            holder.getView(R.id.iv_cancel).setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    public List<String> getDataList() {
        return mDataList;
    }

    public int getImageSize() {
        int i = 0;
        for (String str : mDataList) {
            if (!str.equals("")) {
                i++;
            }
        }
        return i;
    }

    private void clearPlaceHolder() {
        for (int i = mDataList.size() - 1; i > 0; i--) {
            if (mDataList.get(i).equals("")) {
                mDataList.remove(i);
                break;
            }
        }
    }

    public String getImageCode() {

        if (getImageSize() != 0) {
            StringBuffer buffer = new StringBuffer();
            for (int i = 0; i < getImageSize(); i++) {
                try {
                    String compath = mDataList.get(i).substring(0, mDataList.get(i).length() - 4) + "compressPic.jpg";
                    buffer.append(FileUtils.encodeBase64File(FileUtils.compressImage(mDataList.get(i), compath, 30)));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (i != getImageSize() - 1) {
                    buffer.append(";;");
                }
            }
            return buffer.toString();
        } else {
            return "";
        }
    }

    public void setDataList(Collection<String> list) {
        this.mDataList.clear();
        this.mDataList.addAll(list);
    }

    public void addAll(Collection<String> list) {
        clearPlaceHolder();
        if (getImageSize() == 0) {
            mDataList.clear();
        }
        if (this.mDataList.addAll(list)) {
            if (getImageSize() < 3) {
                mDataList.add("");
            }
        }
        notifyDataSetChanged();
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

    public interface onPlaceHolderClick {
        void onPlaceHolderClick();
    }

    public void setOnItemClickLitsener(onItemClickListener mOnItemClickLitsener) {
        mOnItemClickListener = mOnItemClickLitsener;
    }
}
