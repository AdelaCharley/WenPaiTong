package com.qunshang.wenpaitong.equnshang.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.qunshang.wenpaitong.R;
import com.qunshang.wenpaitong.equnshang.activity.ToDayLotteryShareActivity;
import com.qunshang.wenpaitong.equnshang.data.ToDayInviteListBean;
import com.qunshang.wenpaitong.equnshang.data.UserInfoViewModel;

import java.util.List;

public class InviteListAapter extends RecyclerView.Adapter<InviteListAapter.ViewHolder> {

    public List<ToDayInviteListBean.DataBean> data;

    public Context context;

    public LayoutInflater inflater;

    public InviteListAapter(Context context,List<ToDayInviteListBean.DataBean> data){
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
            //holder.userimage.setImageDrawable(context.getDrawable(R.mipmap.ic_launcher));
            holder.userimage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ToDayLotteryShareActivity.class);
                    context.startActivity(intent);
                }
            });
            holder.username.setVisibility(View.INVISIBLE);
        }

    }

    @Override
    public int getItemCount() {
        int spanCount = 10;
        if (UserInfoViewModel.INSTANCE.getUserInfo().getIs_vip() < 2){
            spanCount = 10;
        }
        return spanCount;
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
