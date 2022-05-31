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
import com.qunshang.wenpaitong.equnshang.activity.AfterSaleDetailActivity;
import com.qunshang.wenpaitong.equnshang.activity.AfterSaleSendExpressActivity;
import com.qunshang.wenpaitong.equnshang.data.ExchangeBean;
import com.qunshang.wenpaitong.equnshang.utils.CommonUtil;

public class ExchangeAdapter extends RecyclerView.Adapter<ExchangeAdapter.ViewHolder> {

    public List<ExchangeBean.DataBean> data;

    public Context context;

    public LayoutInflater inflater;

    public ExchangeAdapter(Context context, List<ExchangeBean.DataBean> data){
        this.context = context;
        this.data = data;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ExchangeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.item_exchange,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ExchangeAdapter.ViewHolder holder, int position) {
        ExchangeBean.DataBean bean = data.get(position);
        holder.ordernum.setText("售后单号：" + bean.getOrderSn());
        switch (bean.getType()){
            case 10:
                holder.type_text.setText("退款");
                holder.setdeliver.setVisibility(View.GONE);
                holder.seedetail.setVisibility(View.VISIBLE);
                break;
            case 20:
                holder.type_text.setText("退货退款");
                if (bean.getStatus() == 220){
                    holder.setdeliver.setVisibility(View.VISIBLE);
                } else {
                    holder.setdeliver.setVisibility(View.GONE);
                }

                holder.seedetail.setVisibility(View.VISIBLE);
                break;
            case 30:
                if (bean.getStatus() == 320){
                    holder.setdeliver.setVisibility(View.VISIBLE);
                } else {
                    holder.setdeliver.setVisibility(View.GONE);
                }
                holder.type_text.setText("换货");

                holder.seedetail.setVisibility(View.VISIBLE);
                break;
        }
        Glide.with(context).load(bean.getSkuPic()).into(holder.image);
        holder.product_name.setText(bean.getProductName());
        holder.sku.setText("规格：" + bean.getSkuInfo());
        holder.count.setText("x " + bean.getProductQuantity());
        holder.status_text.setText(bean.getStatusText());
        holder.seedetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seeAfterSaleDetailActivity(position);
            }
        });
        holder.setdeliver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDeliver(position);
            }
        });
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) holder.layout.getLayoutParams();
        if (position == data.size() - 1){
            layoutParams.setMargins(layoutParams.leftMargin,layoutParams.topMargin,layoutParams.rightMargin, CommonUtil.dp2px(context,30));
        } else {
            layoutParams.setMargins(layoutParams.leftMargin,layoutParams.topMargin,layoutParams.rightMargin, 0);
        }
    }

    public void setDeliver(int position){
        Intent intent = new Intent(context, AfterSaleSendExpressActivity.class);
        intent.putExtra("afterSaleSn",data.get(position).getAfterSaleSn());
        intent.putExtra("address",data.get(position).getVendorAddress());
        intent.putExtra("skuPic",data.get(position).getSkuPic());
        intent.putExtra("productName",data.get(position).getProductName());
        intent.putExtra("productSinglePrice",data.get(position).getProductSinglePrice());
        intent.putExtra("productQuantity",data.get(position).getProductQuantity());
        intent.putExtra("skuInfo",data.get(position).getSkuInfo());
        context.startActivity(intent);
    }

    public void seeAfterSaleDetailActivity(int position){
        Intent intent = new Intent(context, AfterSaleDetailActivity.class);
        intent.putExtra("afterSaleSn",data.get(position).getAfterSaleSn());
        context.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder{

        TextView ordernum;

        TextView type_text;

        ImageView image;

        TextView product_name;

        TextView sku;

        TextView count;

        TextView status_text;

        TextView setdeliver;

        TextView seedetail;

        View layout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ordernum = itemView.findViewById(R.id.ordernum);
            type_text = itemView.findViewById(R.id.type_text);
            image = itemView.findViewById(R.id.image);
            product_name = itemView.findViewById(R.id.product_name);
            sku = itemView.findViewById(R.id.sku);
            layout = itemView.findViewById(R.id.layout);
            count = itemView.findViewById(R.id.count);
            status_text = itemView.findViewById(R.id.status_text);
            setdeliver = itemView.findViewById(R.id.setdeliver);
            seedetail = itemView.findViewById(R.id.seedetail);
        }
    }

}
