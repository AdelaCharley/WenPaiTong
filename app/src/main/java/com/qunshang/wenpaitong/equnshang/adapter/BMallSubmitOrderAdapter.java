package com.qunshang.wenpaitong.equnshang.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import com.qunshang.wenpaitong.R;
import com.qunshang.wenpaitong.equnshang.data.ProductBean;

public class BMallSubmitOrderAdapter extends RecyclerView.Adapter<BMallSubmitOrderAdapter.ViewHolder> {

    public List<ProductBean.DataBean.SkuList> data;

    public Context context;

    public LayoutInflater inflater;

    private ProductBean bean;

    public List<ProductBean.DataBean.SkuList> getData(){
        return data;
    }

    public BMallSubmitOrderAdapter(Context context,List<ProductBean.DataBean.SkuList> data,ProductBean bean){
        this.context = context;
        this.data = data;
        inflater = LayoutInflater.from(context);
        this.bean = bean;
        if (!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.item_bmall_submit_order,parent,false));
    }

    public Double getTotalPrice(){
        if (data == null){
            return 0.0;
        }
        Double price = 0.0;
        for (int i = 0;i < data.size();i++){
            price += data.get(i).getPrice() * data.get(i).getBuyCount();
        }
        return price;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ProductBean.DataBean.SkuList sku = data.get(position);
        holder.number.setText(String.valueOf(sku.getBuyCount()));
        holder.bigprice.setText("￥" + sku.getPrice());
        holder.product_name.setText(bean.getData().getProductName());
        holder.sku_image.setText(sku.getValue());
        Glide.with(context).load(sku.getImage()).into(holder.store_image);
        holder.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sku.addCount(1);
                holder.number.setText(String.valueOf(sku.getBuyCount()));
                updatePrice();
            }
        });
        holder.cut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sku.getBuyCount() <= sku.getMinQuantity()) {
                    return;
                }
                sku.addCount(-1);
                holder.number.setText(String.valueOf(sku.getBuyCount()));
                updatePrice();
            }
        });
        if (position == data.size() - 1){
            updatePrice();
        }
    }

    public void updatePrice(){
        EventBus.getDefault().post(String.valueOf(getTotalPrice()));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void def(Double def){ }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        ImageView store_image;

        TextView product_name;

        TextView sku_image;

        TextView bigprice;

        TextView cut;

        TextView number;

        TextView add;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            store_image = itemView.findViewById(R.id.store_image);
            product_name = itemView.findViewById(R.id.product_name);
            sku_image = itemView.findViewById(R.id.sku_image);
            bigprice = itemView.findViewById(R.id.bigprice);
            cut = itemView.findViewById(R.id.cut);
            number = itemView.findViewById(R.id.number);
            add = itemView.findViewById(R.id.add);
        }
    }

}
