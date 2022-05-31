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
import com.qunshang.wenpaitong.equnshang.data.NewPartnerBean;

import java.util.List;

public class NewPartnerAdapter extends RecyclerView.Adapter<NewPartnerAdapter.ViewHolder> {

    public List<NewPartnerBean.DataBean> data;

    public Context context;

    public LayoutInflater inflater;

    public NewPartnerAdapter(Context context,List<NewPartnerBean.DataBean> data){
        this.context = context;
        this.data = data;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.item_new_partner,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(context).load(data.get(position).getHeadimage()).into(holder.image);
        holder.name.setText(data.get(position).getUname());
        holder.time.setText(data.get(position).getRegtime());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        ImageView image;

        TextView name;

        TextView time;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            name = itemView.findViewById(R.id.videoname);
            time = itemView.findViewById(R.id.time);
        }
    }

}
