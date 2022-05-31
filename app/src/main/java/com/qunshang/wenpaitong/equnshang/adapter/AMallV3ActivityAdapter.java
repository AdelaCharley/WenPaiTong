package com.qunshang.wenpaitong.equnshang.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.util.List;

import com.qunshang.wenpaitong.R;
import com.qunshang.wenpaitong.equnshang.activity.WebViewActivity;
import com.qunshang.wenpaitong.equnshang.data.AMallV3MainBean;

public class AMallV3ActivityAdapter extends RecyclerView.Adapter<AMallV3ActivityAdapter.ViewHolder> {

    public List<AMallV3MainBean.DataBean.ActivityBean> data;

    public Context context;

    public LayoutInflater inflater;


    public AMallV3ActivityAdapter(Context context,List<AMallV3MainBean.DataBean.ActivityBean> data){
        this.context = context;
        this.data = data;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public AMallV3ActivityAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AMallV3ActivityAdapter.ViewHolder(inflater.inflate(R.layout.item_amallv3_main_activity,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull AMallV3ActivityAdapter.ViewHolder holder, int position) {
        Glide.with(context).load(data.get(position).getActivityUrl()).into(holder.image);
        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //页面跳转，h5
                Log.d("shulinr", "click " + holder.image);
                Intent intent = new Intent(context, WebViewActivity.class);

                String type = "";
                if ("new".equals(data.get(position).getType())){
                    type = "新品";
                } else if ("hot".equals(data.get(position).getType())){
                    type = "热销";
                } else {
                    type = "";
                }
                intent.putExtra("title",type);
                intent.putExtra("url",data.get(position).getJumpUrl());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView image;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
        }
    }

}
