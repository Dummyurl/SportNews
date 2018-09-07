package com.kunleen.sn.sportnewsapplication.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.kunleen.sn.sportnewsapplication.R;
import com.kunleen.sn.sportnewsapplication.app.SNApplication;
import com.kunleen.sn.sportnewsapplication.base.BaseActivity;
import com.kunleen.sn.sportnewsapplication.network.RetrofitClient;
import com.kunleen.sn.sportnewsapplication.network.bean.ReqCommentGood;
import com.kunleen.sn.sportnewsapplication.network.bean.TRequest;
import com.kunleen.sn.sportnewsapplication.network.bean.TRespon_Comment;
import com.kunleen.sn.sportnewsapplication.network.bean.TResponse;
import com.kunleen.sn.sportnewsapplication.network.bean.TResponse_Empty;
import com.kunleen.sn.sportnewsapplication.network.service.HttpService;
import com.kunleen.sn.sportnewsapplication.utils.ToastUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


public class CommentAdapter extends ListBaseAdapter<TRespon_Comment.Rows> {
    public CommentAdapter(Activity context) {
        super(context);
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.news_comment;
    }

    @Override
    public void onBindItemHolder(SuperViewHolder holder, int position) {
        TRespon_Comment.Rows rows = getDataList().get(position);
        Glide.with(mContext).load(rows.getHeadImage()).apply(new RequestOptions().centerCrop().placeholder(R.mipmap.img_loading1).diskCacheStrategy(DiskCacheStrategy.RESOURCE).skipMemoryCache(false)).into((ImageView) holder.getView(R.id.cir_comment_head));
        ((TextView) holder.getView(R.id.tv_comment_username)).setText(rows.getUserName());
        ((TextView) holder.getView(R.id.tv_comment_comment_time)).setText(rows.getCreateTime());
        ((TextView) holder.getView(R.id.tv_comment_good)).setText(rows.getGood() + "");
        try {
            ((TextView) holder.getView(R.id.tv_comment_content)).setText(URLDecoder.decode(rows.getDiscussTxt(), "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        ImageView iv_zan = holder.getView(R.id.iv_comment_good);
        View view = holder.getView(R.id.ll_good);
        view.setOnClickListener(v -> {
            TRequest<ReqCommentGood> request = new TRequest<>();
            ReqCommentGood reqCommentGood = new ReqCommentGood();
            reqCommentGood.setId(rows.getDiscussId() + "");
            reqCommentGood.setType("1");
            request.setParam(reqCommentGood, "1118", "1");

            Observable<TResponse> observable = RetrofitClient.getInstance().getService(HttpService.class).CommentGood(request);
            ((BaseActivity) mContext).sendRequest(observable, new Consumer<TResponse>() {
                @Override
                public void accept(TResponse tResponse) throws Exception {
                    iv_zan.setImageResource(R.mipmap.button_xqy_zanx2);
                    iv_zan.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.dianzan_anim));
                    view.setOnClickListener(null);
                    ((TextView) holder.getView(R.id.tv_comment_good)).setText(rows.getGood() + 1 + "");
                    ((TextView) holder.getView(R.id.tv_comment_good)).setTextColor(Color.parseColor("#ff0bb731"));

                }
            }, new Consumer<Throwable>() {
                @Override
                public void accept(Throwable throwable) throws Exception {
                    ToastUtils.showToast(throwable.getMessage());
                }
            });
        });
    }
}
