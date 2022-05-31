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

public class AMallAdapterV3 extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    public List<AMallV3SearchProductBean.DataBean.ProductBean> data = new ArrayList<>();

    public Context context;

    public LayoutInflater inflater;

    public boolean isShowBottomLine = false;

    public AMallAdapterV3(Context context,List<AMallV3SearchProductBean.DataBean.ProductBean> data){
        this.context = context;
        this.data = data;
        inflater = LayoutInflater.from(context);
    }

    public AMallAdapterV3(Context context){
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public void addData(List<AMallV3SearchProductBean.DataBean.ProductBean> data){
        this.data.addAll(data);
        notifyDataSetChanged();
    }

    public void clearData(){
        this.data = new ArrayList<>();
        notifyDataSetChanged();
    }

    public void showisShowBottomLine(boolean b){
        this.isShowBottomLine = b;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == data.size()){
            return TYPE_BOTTOM;
        }
        return TYPE_NORMAL;
    }

    public int TYPE_NORMAL = 2;

    public int TYPE_BOTTOM = 3;

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_NORMAL){
            return new ViewHolder(inflater.inflate(R.layout.item_amall_productsv3,parent,false));
        }
        return new BottomViewHolder(inflater.inflate(R.layout.item_i_have_line,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolder){
            AMallV3SearchProductBean.DataBean.ProductBean bean = data.get(position);
            ViewHolder holder1 = (ViewHolder) holder;
            Glide.with(context).load(bean.getProductPosterUrl()).into(holder1.image);
            holder1.name.setText(bean.getProductName());
            holder1.price.setText("" + String.valueOf(bean.getVipGroupPrice()));
            holder1.originalprice.setText("￥" + String.valueOf(bean.getMarketPrice()));
            holder1.amall_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, AMallV3ProductDetailActivity.class);
                    intent.putExtra("productId",bean.getProductId());
                    context.startActivity(intent);
                }
            });
            holder1.sale.setText("已售" + String.valueOf(bean.getSale()));
            holder1.tag.setText(bean.getTag());
        }
        if (holder instanceof BottomViewHolder){
            if (isShowBottomLine){
                ((BottomViewHolder) holder).layout.setVisibility(View.VISIBLE);
            } else {
                ((BottomViewHolder) holder).layout.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public int getItemCount() {
        return data.size() + 1;
    }

    public static class BottomViewHolder extends RecyclerView.ViewHolder{

        View layout;

        public BottomViewHolder(@NonNull View itemView) {
            super(itemView);
            layout = itemView.findViewById(R.id.layout);
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        ImageView image;

        TextView name;

        TextView price;

        TextView originalprice;

        ViewGroup amall_layout;

        TextView sale;

        TextView tag;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            amall_layout = itemView.findViewById(R.id.layout);
            image = itemView.findViewById(R.id.image);
            name = itemView.findViewById(R.id.name);
            price = itemView.findViewById(R.id.price);
            sale = itemView.findViewById(R.id.sale);
            originalprice = itemView.findViewById(R.id.originalprice);
            tag = itemView.findViewById(R.id.tag);
        }
    }

}
