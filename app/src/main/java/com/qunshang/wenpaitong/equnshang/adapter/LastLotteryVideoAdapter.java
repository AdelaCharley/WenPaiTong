package com.qunshang.wenpaitong.equnshang.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.qunshang.wenpaitong.R;
import com.qunshang.wenpaitong.equnshang.activity.LoginActivity;
import com.qunshang.wenpaitong.equnshang.data.LotteryVideoBean;
import com.qunshang.wenpaitong.equnshang.fragment.LastLotteryVideoFragment;
import com.qunshang.wenpaitong.equnshang.utils.StringUtils;
import com.qunshang.wenpaitong.equnshang.utils.UserHelper;
import com.qunshang.wenpaitong.equnshang.view.BaseRvViewHolder;
import com.qunshang.wenpaitong.equnshang.view.LikeView;
import com.qunshang.wenpaitong.equnshang.view.LastLotteryControllerView;
import com.xin.marquee.text.view.MarqueeTextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * 视频播放页的适配器
 * create by libo
 * create on 2020-05-20
 * modifier 何姝霖
 * last-modified 2021-07-16
 */
public class LastLotteryVideoAdapter extends BaseRvAdapter<LotteryVideoBean.DataBean, LastLotteryVideoAdapter.LotteryViewHolder> {


    public LastLotteryVideoAdapter(Context context, List<LotteryVideoBean.DataBean> datas) {
        super(context, datas);
    }

    @Override
    protected void onBindData(LotteryViewHolder holder, LotteryVideoBean.DataBean dataBean, int position) {
        holder.controllerView.setVideoData(dataBean);
        holder.ivCover.setVisibility(View.VISIBLE);
        if (dataBean.getIsUp() != 0){
            ImageView image_ivup = holder.controllerView.findViewById(R.id.ivUp);
            image_ivup.setImageResource(R.mipmap.btn_main_up_true);
        } else {
            ImageView image_ivup = holder.controllerView.findViewById(R.id.ivUp);
            image_ivup.setImageResource(R.mipmap.btn_main_up_false);
        }
        if (dataBean.getIsLike() != 0){
            ImageView image_like = holder.controllerView.findViewById(R.id.cbLike);
            image_like.setImageResource(R.mipmap.btn_comment_like_true);
        } else {
            ImageView image_like = holder.controllerView.findViewById(R.id.cbLike);
            image_like.setImageResource(R.mipmap.btn_main_like_false);
        }
        if (StringUtils.isEmpty(UserHelper.getCurrentLoginUser(getContext()))){
            holder.login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), LoginActivity.class);
                    getContext().startActivity(intent);
                }
            });
        } else {
            holder.layoug_login.setVisibility(View.GONE);
        }
        Glide.with(getContext())
                .load(dataBean.getVideoPosterUrl())
                .into(holder.ivCover);
        holder.textView.setText(LastLotteryVideoFragment.Companion.getLotterier());
        holder.textView.start();
    }

    @NonNull
    @Override
    public LotteryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.item_last_lottery_video, parent, false);
        return new LotteryViewHolder(view);
    }

    public static class LotteryViewHolder extends BaseRvViewHolder {
        @BindView(R.id.lv_item_video_likeview)
        LikeView likeView;
        @BindView(R.id.cv_item_video_controller)
        LastLotteryControllerView controllerView;
        @BindView(R.id.iv_item_video_cover)
        ImageView ivCover;

        @BindView(R.id.lotterytext)
        MarqueeTextView textView;

        @BindView(R.id.layout_not_login)
        View layoug_login;

        @BindView(R.id.login)
        TextView login;

        public LotteryViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }
}