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
import com.qunshang.wenpaitong.equnshang.data.ProductBeanV2;

public class AMallV3DetailShopAdapter extends RecyclerView.Adapter<AMallV3DetailShopAdapter.ViewHolder> {

    public List<ProductBeanV2.DataBean.Manufacture.Product> data;

    public Context context;

    public LayoutInflater inflater;

    public AMallV3DetailShopAdapter(Context context,List<ProductBeanV2.DataBean.Manufacture.Product> data){
        this.context = context;
        this.data = data;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.item_amallv3_detail_shop,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ProductBeanV2.DataBean.Manufacture.Product bean = data.get(position);
        Glide.with(context).load(bean.getPosterUrl()).into(holder.image);
        holder.name.setText(bean.getProductName());
        holder.price.setText("￥" + bean.getVipGroupPrice() + "元");
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AMallV3ProductDetailActivity.class);
                intent.putExtra("productId", bean.getProductId());
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

        View layout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            name = itemView.findViewById(R.id.name);
            price = itemView.findViewById(R.id.price);
            layout = itemView.findViewById(R.id.layout);
        }
    }

}
