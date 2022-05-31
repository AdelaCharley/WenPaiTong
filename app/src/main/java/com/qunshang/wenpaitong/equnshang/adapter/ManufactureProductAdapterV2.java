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

import java.util.ArrayList;
import java.util.List;

import com.qunshang.wenpaitong.R;
import com.qunshang.wenpaitong.equnshang.activity.AMallV3ProductDetailActivity;
import com.qunshang.wenpaitong.equnshang.data.AMallV3SearchProductBean;

public class ManufactureProductAdapterV2 extends RecyclerView.Adapter<ManufactureProductAdapterV2.ViewHolder> {

    public List<AMallV3SearchProductBean.DataBean.ProductBean> data = new ArrayList<>();

    public Context context;

    public LayoutInflater inflater;

    public ManufactureProductAdapterV2(Context context,List<AMallV3SearchProductBean.DataBean.ProductBean> data){
        this.context = context;
        this.data = data;
        inflater = LayoutInflater.from(context);
    }

    public ManufactureProductAdapterV2(Context context){
        this.context = context;
        this.data = data;
        inflater = LayoutInflater.from(context);
    }

    public void removeAll(){
        this.data = new ArrayList<>();
    }

    public void add(List<AMallV3SearchProductBean.DataBean.ProductBean> data){
        if (data == null){
            return;
        }
        this.data.addAll(data);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.item_manufacture_productv2, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AMallV3SearchProductBean.DataBean.ProductBean bean = data.get(position);
        Glide.with(context).load(bean.getProductPosterUrl()).into(holder.image);
        holder.name.setText(bean.getProductName());
        holder.price.setText("" + String.valueOf(bean.getVipGroupPrice()));
        holder.originalprice.setText("￥" + String.valueOf(bean.getMarketPrice()));
        holder.label.setText(bean.getTag());
        holder.amall_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AMallV3ProductDetailActivity.class);
                intent.putExtra("productId",bean.getProductId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        ImageView image;

        TextView name;

        TextView price;

        TextView originalprice;

        ViewGroup amall_layout;

        TextView label;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            label = itemView.findViewById(R.id.label);
            amall_layout = itemView.findViewById(R.id.layout);
            image = itemView.findViewById(R.id.image);
            name = itemView.findViewById(R.id.name);
            price = itemView.findViewById(R.id.price);
            originalprice = itemView.findViewById(R.id.originalprice);
        }
    }

}
