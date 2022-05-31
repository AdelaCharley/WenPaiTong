package com.qunshang.wenpaitong.equnshang.adapter;

import android.content.Context;
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
import com.qunshang.wenpaitong.equnshang.data.ProductBeanV2;

public class ProductDetailSpecAdapterV2 extends RecyclerView.Adapter<ProductDetailSpecAdapterV2.ViewHolder> {

    public List<ProductBeanV2.DataBean.SkuList> data;

    public Context context;

    public LayoutInflater inflater;


    public ProductDetailSpecAdapterV2(Context context, List<ProductBeanV2.DataBean.SkuList> data){
        this.context = context;
        this.data = data;
        inflater = LayoutInflater.from(context);
        Log.i("zhangjuniii","一共有" + data.size() + "个数据");
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.item_product_detail_skuv2,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(context).load(data.get(position).getImage()).into(holder.image);
        Log.i("zhangjuniii","当前第" + position + "个数据");
    }

    @Override
    public int getItemCount() {
        return Math.min(data.size(), 4);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        ImageView image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
        }
    }

}