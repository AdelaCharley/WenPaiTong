package com.qunshang.wenpaitong.equnshang.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import com.qunshang.wenpaitong.R;
import com.qunshang.wenpaitong.equnshang.data.UserInfoViewModel;
import com.qunshang.wenpaitong.equnshang.data.WeekPrizeHistoryBean;

public class WeekPrizeHistoryHelpAdapter extends RecyclerView.Adapter<WeekPrizeHistoryHelpAdapter.ViewHolder> {

    public List<WeekPrizeHistoryBean.DataBean.HelpInfo> data;

    public Context context;

    public LayoutInflater inflater;

    public WeekPrizeHistoryHelpAdapter(Context context,List<WeekPrizeHistoryBean.DataBean.HelpInfo> data){
        this.context = context;
        this.data = data;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.item_today_lottery_invite_list,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        if (position < data.size()){
            holder.username.setText(data.get(position).getUserName());
            holder.username.setVisibility(View.VISIBLE);
            Glide.with(context).load(data.get(position).getHeadimageUrl()).into(holder.userimage);
        } else {
            holder.userimage.setOnClickListener(v -> {

            });
            holder.username.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        if (UserInfoViewModel.INSTANCE.getUserInfo().getIs_vip() >= 2){
            return data.size();
        }
        return data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        ImageView userimage;

        TextView username;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            userimage = itemView.findViewById(R.id.userimage);
            username = itemView.findViewById(R.id.username);
        }
    }

}