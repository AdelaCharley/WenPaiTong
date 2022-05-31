package com.qunshang.wenpaitong.equnshang.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import com.qunshang.wenpaitong.R;
import com.qunshang.wenpaitong.equnshang.activity.ProductDetailActivity;
import com.qunshang.wenpaitong.equnshang.activity.StoreDetailActivity;
import com.qunshang.wenpaitong.equnshang.data.DiscountTicketBean;
import com.qunshang.wenpaitong.equnshang.utils.StringUtils;

public class DiscountAdapter extends RecyclerView.Adapter<DiscountAdapter.DiscountViewHolder>  {

    public List<DiscountTicketBean.DataBean> data;

    public Context context;

    public LayoutInflater inflater;

    public DiscountAdapter(Context context, List<DiscountTicketBean.DataBean> data){
        this.context = context;
        this.data = data;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public DiscountViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DiscountViewHolder(inflater.inflate(R.layout.item_discount_ticket,parent,false));
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull DiscountViewHolder holder, int position) {
        DiscountTicketBean.DataBean bean = data.get(position);
        holder.price.setText("￥" + (int)bean.getPrice());
        holder.product_name.setText(bean.getRelationName());
        holder.time.setText(bean.getOverTime());
        if (bean.getUseStatus() == 10){
            holder.gouse.setVisibility(View.VISIBLE);
            holder.aluse.setVisibility(View.INVISIBLE);
            holder.gouse.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    goUse(bean);
                }
            });
        } else if (bean.getUseStatus() == 20){
            holder.gouse.setVisibility(View.INVISIBLE);
            holder.aluse.setVisibility(View.VISIBLE);
            holder.price.setTextColor(R.color.grey_light);
            holder.label.setTextColor(R.color.grey_light);
            holder.product_name.setTextColor(R.color.grey_light);
            holder.time.setTextColor(R.color.grey_light);
            //holder.price.setTextColor(R.color.text_light_grey);
            //holder.label.setTextColor(R.color.text_light_grey);
            //holder.product_name.setTextColor(R.color.text_light_grey);
            //holder.time.setTextColor(R.color.text_light_grey);
        } else if (bean.getUseStatus() == 30){
            holder.price.setTextColor(R.color.grey_light);
            holder.label.setTextColor(R.color.grey_light);
            holder.product_name.setTextColor(R.color.grey_light);
            holder.time.setTextColor(R.color.grey_light);
            holder.gouse.setVisibility(View.INVISIBLE);
            holder.aluse.setVisibility(View.VISIBLE);
            holder.aluse.setText("已失效");
        }
    }

    public void goUse(DiscountTicketBean.DataBean bean){
        if (!StringUtils.isEmpty(bean.getUseType())){
            switch (bean.getUseType()){
                case "product" :
                    Intent intent = new Intent(context, ProductDetailActivity.class);
                    intent.putExtra("productId",bean.getRelationId());
                    context.startActivity(intent);
                    break;
                case "manufacturer":
                    Intent intent1 = new Intent(context,StoreDetailActivity.class);
                    intent1.putExtra("manfactureId",bean.getRelationId());
                    context.startActivity(intent1);
                    break;
                case "":
                    break;
            }
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    public static class DiscountViewHolder extends RecyclerView.ViewHolder{

        TextView price;

        TextView product_name;

        TextView label;

        TextView time;

        TextView gouse;

        TextView aluse;

        public DiscountViewHolder(@NonNull View itemView) {
            super(itemView);
            price = itemView.findViewById(R.id.price);
            product_name = itemView.findViewById(R.id.product_name);
            time = itemView.findViewById(R.id.time);
            label = itemView.findViewById(R.id.label);
            gouse = itemView.findViewById(R.id.gouse);
            aluse = itemView.findViewById(R.id.aluse);
        }
    }

}
