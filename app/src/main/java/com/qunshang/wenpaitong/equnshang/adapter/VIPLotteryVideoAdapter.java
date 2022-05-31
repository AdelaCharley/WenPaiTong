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

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.qunshang.wenpaitong.R;
import com.qunshang.wenpaitong.equnshang.activity.LoginActivity;
import com.qunshang.wenpaitong.equnshang.activity.VipDayActivity;
import com.qunshang.wenpaitong.equnshang.data.LotteryVideoBean;
import com.qunshang.wenpaitong.equnshang.fragment.LotteryFragment;
import com.qunshang.wenpaitong.equnshang.utils.DialogUtil;
import com.qunshang.wenpaitong.equnshang.utils.StringUtils;
import com.qunshang.wenpaitong.equnshang.utils.UserHelper;
import com.qunshang.wenpaitong.equnshang.view.BaseRvViewHolder;
import com.qunshang.wenpaitong.equnshang.view.LikeView;
import com.qunshang.wenpaitong.equnshang.view.VipLotteryControllerView;


/**
 * 视频播放页的适配器
 * create by libo
 * create on 2020-05-20
 * modifier 何姝霖
 * last-modified 2021-07-16
 */
public class VIPLotteryVideoAdapter extends BaseRvAdapter<LotteryVideoBean.DataBean, VIPLotteryVideoAdapter.LotteryViewHolder> {

    public VIPLotteryVideoAdapter(Context context, List<LotteryVideoBean.DataBean> datas) {
        super(context, datas);

    }

    @Override
    protected void onBindData(LotteryViewHolder holder, LotteryVideoBean.DataBean dataBean, int position) {
        holder.ivCover.setVisibility(View.VISIBLE);
        holder.controllerView.setVideoData(dataBean);
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
        Glide.with(getContext())
                .load(dataBean.getVideoPosterUrl())
                .into(holder.ivCover);
        holder.gotovip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!UserHelper.isLogin(getContext())){
                    getContext().startActivity(new Intent(getContext(),LoginActivity.class));
                    return;
                }
                if (LotteryFragment.activityId <= 0){
                    DialogUtil.toast(getContext(),LotteryFragment.msg);
                    return;
                }
                Intent intent = new Intent(getContext(), VipDayActivity.class);
                intent.putExtra("activityId",LotteryFragment.activityId);
                getContext().startActivity(intent);
            }
        });
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
    }

    @NonNull
    @Override
    public LotteryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.item_vip_lottery_video, parent, false);
        return new LotteryViewHolder(view);
    }

    public static class LotteryViewHolder extends BaseRvViewHolder {
        @BindView(R.id.lv_item_video_likeview)
        LikeView likeView;
        @BindView(R.id.cv_item_video_controller)
        VipLotteryControllerView controllerView;
        @BindView(R.id.iv_item_video_cover)
        ImageView ivCover;
        @BindView(R.id.gotovip)
        ImageView gotovip;
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
