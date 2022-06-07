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
//import com.qunshang.wenpaitong.equnshang.activity.BMallProductDetailActivity;
import com.qunshang.wenpaitong.equnshang.data.MyBuyOrderBean;

public class MyBuyOrderProductsAdapter extends RecyclerView.Adapter<MyBuyOrderProductsAdapter.ViewHolder> {

    public MyBuyOrderBean.DataBean.Product data;

    public Context context;

    public LayoutInflater inflater;

    public MyBuyOrderProductsAdapter(Context context, MyBuyOrderBean.DataBean.Product data){
        this.context = context;
        this.data = data;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.item_my_buy_order_detail_products,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MyBuyOrderBean.DataBean.Product.SkuList bean = this.data.getSkuList().get(position);
        Glide.with(context).load(bean.getPosterUrl()).into(holder.image_store);
        holder.product_name.setText(data.getProductName());
        //holder.product_spec.setText(bean.getValue());
        holder.product_count.setText("购买量：" + bean.getNumber());
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent intent = new Intent(context, BMallProductDetailActivity.class);
                intent.putExtra("productId",data.getProductId());
                context.startActivity(intent);*/
            }
        });
        holder.product_price.setText("价格：￥" + String.valueOf(bean.getPrice() * Integer.parseInt(bean.getNumber())));
    }

    @Override
    public int getItemCount() {
        return data.getSkuList().size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        ImageView image_store;

        TextView product_name;

        TextView product_count;

        TextView product_price;

        View layout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image_store = itemView.findViewById(R.id.image_store);
            product_name = itemView.findViewById(R.id.product_name);
            product_count = itemView.findViewById(R.id.product_count);
            product_price = itemView.findViewById(R.id.product_price);
            layout = itemView.findViewById(R.id.layout);
        }
    }

}
