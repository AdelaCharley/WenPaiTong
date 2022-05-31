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
import com.qunshang.wenpaitong.R;

import com.qunshang.wenpaitong.equnshang.data.UserPartnerBean;
import com.qunshang.wenpaitong.equnshang.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class ManagePartnerContentAdapter extends RecyclerView.Adapter<ManagePartnerContentAdapter.ViewHolder> {

    public List<UserPartnerBean.DataBean.PartnerData> data = new ArrayList<>();

    public Context context;

    public LayoutInflater inflater;

    public ManagePartnerContentAdapter(Context context,List<UserPartnerBean.DataBean.PartnerData> data){
        this.context = context;
        this.data = data;
        inflater = LayoutInflater.from(context);
    }

    public ManagePartnerContentAdapter(Context context){
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public void add(List<UserPartnerBean.DataBean.PartnerData> data){
        this.data.addAll(data);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ManagePartnerContentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ManagePartnerContentAdapter.ViewHolder(inflater.inflate(R.layout.item_manage_user,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ManagePartnerContentAdapter.ViewHolder holder, int position) {
        UserPartnerBean.DataBean.PartnerData data = this.data.get(position);
        Glide.with(context).load(data.getHeadimage_url()).into(holder.image);
        if (!StringUtils.isEmpty(data.getName())){
            holder.name.setText(data.getName());
        } else {
            if (!StringUtils.isEmpty(data.getUname())){
                holder.name.setText(data.getUname());
            }
        }
        holder.uid.setText(data.getUtel());
        if (!StringUtils.isEmpty(data.getCreate_time())){
            String time[] = data.getCreate_time().split(" ");
            if (time.length == 2){
                holder.time_day.setText(time[0]);
                holder.time_time.setText(time[1]);
            }
        }

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        ImageView image;

        TextView name;

        TextView uid;

        TextView time_day;

        TextView time_time;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            name = itemView.findViewById(R.id.uname);
            uid = itemView.findViewById(R.id.uid);
            time_day = itemView.findViewById(R.id.time_day);
            time_time = itemView.findViewById(R.id.time_time);
        }
    }

}
