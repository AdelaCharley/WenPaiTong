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

import java.util.List;

import com.qunshang.wenpaitong.R;
import com.qunshang.wenpaitong.equnshang.activity.WatchVideoActivity;
import com.qunshang.wenpaitong.equnshang.data.VendorVideoBean;

public class StoreVideoAdapter extends RecyclerView.Adapter<StoreVideoAdapter.ViewHolder> {

    public List<VendorVideoBean.DataBean> data;

    public Context context;

    public LayoutInflater inflater;

    public StoreVideoAdapter(Context context,List<VendorVideoBean.DataBean> data){
        this.context = context;
        this.data = data;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.item_store_video,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        VendorVideoBean.DataBean bean = data.get(position);
        Glide.with(context).load(bean.getVideoPosterUrl()).into(holder.image);
        holder.title.setText(bean.getTitle());
        holder.time.setText(bean.getCreateTime());

        holder.image.setOnClickListener(v -> {
            Intent intent = new Intent(context, WatchVideoActivity.class);
            intent.putExtra("data", bean);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        ImageView image;

        TextView title;

        TextView time;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            time = itemView.findViewById(R.id.time);
            image = itemView.findViewById(R.id.image);
        }
    }

}
