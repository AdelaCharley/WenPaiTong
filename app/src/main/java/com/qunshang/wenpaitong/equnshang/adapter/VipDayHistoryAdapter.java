package com.qunshang.wenpaitong.equnshang.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import com.qunshang.wenpaitong.R;
import com.qunshang.wenpaitong.equnshang.activity.ChooseAddressActivity;
import com.qunshang.wenpaitong.equnshang.activity.ExperienceActivity;
import com.qunshang.wenpaitong.equnshang.activity.PublishExperienceActivity;
import com.qunshang.wenpaitong.equnshang.activity.VipDayHistoryActivity;
import com.qunshang.wenpaitong.equnshang.data.VipDayHistoryBean;
import com.qunshang.wenpaitong.equnshang.utils.CommonUtil;
import com.qunshang.wenpaitong.equnshang.utils.DialogUtil;
import com.qunshang.wenpaitong.equnshang.utils.StringUtils;

public class VipDayHistoryAdapter extends RecyclerView.Adapter<VipDayHistoryAdapter.ViewHolder> {

    public List<VipDayHistoryBean.DataBean> data;

    public Context context;

    public LayoutInflater inflater;

    public VipDayHistoryAdapter(Context context, List<VipDayHistoryBean.DataBean> data){
        this.context = context;
        this.data = data;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.item_vipday_history,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        VipDayHistoryBean.DataBean bean = data.get(position);
        if (!StringUtils.isEmpty(bean.getImageUrl())){
            Glide.with(context).load(bean.getImageUrl()).into(holder.prize_image);
        } else {
            Glide.with(context).load(bean.getCoverUrl()).into(holder.prize_image);
        }
        if (StringUtils.isEmpty(bean.getName())){
            holder.prize_name.setText("会员日");
        } else {
            holder.prize_name.setText(bean.getName());
        }
        holder.win_number.setText(bean.getNumber());
        holder.win_date.setText(bean.getTime());
        switch (bean.getStatus()){
            case 0:
                holder.status.setImageDrawable(context.getDrawable(R.mipmap.weizhongjiang));
                holder.line.setVisibility(View.INVISIBLE);
                holder.done.setVisibility(View.GONE);
                break;
            case 1:
                holder.line.setVisibility(View.VISIBLE);
                holder.done.setVisibility(View.VISIBLE);
                holder.status.setImageDrawable(context.getDrawable(R.mipmap.yizhongjiang));
                if (bean.getType() == 1){
                    holder.done.setText("联系客服");
                    holder.done.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            DialogUtil.showCustomDialog(context,"vip");
                        }
                    });
                } else {
                    holder.done.setText("填写收货地址");
                    holder.done.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            VipDayHistoryActivity.Companion.setRelationId(bean.getRelationId());
                            Intent intent = new Intent(context, ChooseAddressActivity.class);
                            ((AppCompatActivity) context).startActivityForResult(intent, VipDayHistoryActivity.Companion.getTYPE_RECEIVE_TICKET());
                        }
                    });
                }
                break;
            case 2:
                holder.line.setVisibility(View.VISIBLE);
                holder.done.setVisibility(View.VISIBLE);
                holder.status.setImageDrawable(context.getDrawable(R.mipmap.yizhongjiang));
                if (bean.getExperienceId() == 0){
                    if ("0".equals(bean.getSendStatus())){//为0的时候代表礼品派发中，否则是发布心得
                        holder.done.setText("礼品派发中");
                    } else {
                        holder.done.setText("发布心得");
                        holder.done.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(context, PublishExperienceActivity.class);
                                //intent.putExtra("experienceid",bean.data.experienceId)
                                intent.putExtra("prizeId",bean.getPrizeId());
                                intent.putExtra("type",2);
                                intent.putExtra("relationId",Long.parseLong(String.valueOf(bean.getRelationId())));
                                intent.putExtra("imageUrl",bean.getImageUrl());
                                intent.putExtra("winTime",bean.getTime());
                                context.startActivity(intent);
                            }
                        });
                    }

                } else {
                    holder.done.setText("我的心得发布");
                    holder.done.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(context, ExperienceActivity.class);
                            //intent.putExtra("experienceid",bean.data.experienceId)
                            intent.putExtra("experienceid",bean.getExperienceId());
                            context.startActivity(intent);
                        }
                    });
                }
                break;
            case 3:
                holder.status.setImageDrawable(context.getDrawable(R.mipmap.yishixiao));
                holder.line.setVisibility(View.INVISIBLE);
                holder.done.setVisibility(View.GONE);
                break;
        }
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) holder.layout.getLayoutParams();
        if (position == data.size() - 1) {
            layoutParams.setMargins(
                    layoutParams.leftMargin,
                    layoutParams.topMargin,
                    layoutParams.rightMargin,
                    CommonUtil.dp2px(holder.layout.getContext(), 30)
            );
        } else {
            layoutParams.setMargins(
                    layoutParams.leftMargin,
                    layoutParams.topMargin,
                    layoutParams.rightMargin,
                    0
            );
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        ImageView prize_image;

        TextView prize_name;

        TextView win_number;

        TextView win_date;

        ImageView status;

        View line;

        TextView done;

        View layout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            layout = itemView.findViewById(R.id.layout);
            prize_image = itemView.findViewById(R.id.img_poster);
            prize_name = itemView.findViewById(R.id.tv_name);
            win_number = itemView.findViewById(R.id.tv_number);
            win_date = itemView.findViewById(R.id.tv_date);
            status = itemView.findViewById(R.id.img_state);
            line = itemView.findViewById(R.id.view);
            done = itemView.findViewById(R.id.btn);
        }
    }

}
