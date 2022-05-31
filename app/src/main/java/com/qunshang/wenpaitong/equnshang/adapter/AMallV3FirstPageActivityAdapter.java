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
import com.qunshang.wenpaitong.equnshang.activity.AMallV3ProductDetailActivity;
import com.qunshang.wenpaitong.equnshang.data.AMallV3FirstCategoryPageBean;

public class AMallV3FirstPageActivityAdapter extends RecyclerView.Adapter<AMallV3FirstPageActivityAdapter.ViewHolder> {

    public List<AMallV3FirstCategoryPageBean.DataBean.ActivityBean.ProductListBean> data;

    public Context context;

    public LayoutInflater inflater;


    public AMallV3FirstPageActivityAdapter(Context context,List<AMallV3FirstCategoryPageBean.DataBean.ActivityBean.ProductListBean> data){
        this.context = context;
        this.data = data;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.item_amallv3_first_page_activity,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(context).load(data.get(position).getProductImg()).into(holder.image);
        holder.title.setText(data.get(position).getProductName());
        holder.subtitle.setText(data.get(position).getMinorTitle());
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AMallV3ProductDetailActivity.class);
                intent.putExtra("productId", data.get(position).getProductId());
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

        TextView title;

        TextView subtitle;

        View layout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            layout = itemView.findViewById(R.id.layout);
            image = itemView.findViewById(R.id.image);
            title = itemView.findViewById(R.id.title);
            subtitle = itemView.findViewById(R.id.subtitle);
        }
    }

}
