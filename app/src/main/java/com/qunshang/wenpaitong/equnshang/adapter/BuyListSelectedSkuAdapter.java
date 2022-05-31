package com.qunshang.wenpaitong.equnshang.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import com.qunshang.wenpaitong.R;
import com.qunshang.wenpaitong.equnshang.data.ProductBean;

public class BuyListSelectedSkuAdapter extends RecyclerView.Adapter<BuyListSelectedSkuAdapter.ViewHolder> {

    public ArrayList<ProductBean.DataBean.SkuList> data = new ArrayList<>();

    public Context context;

    public LayoutInflater inflater;

    public BuyListSelectedSkuAdapter(Context context){
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.item_buy_product_selected_sku,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ProductBean.DataBean.SkuList bean = data.get(position);
        holder.sku.setText(bean.getValue());
        holder.count.setText(String.valueOf(bean.getBuyCount()));
        holder.close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                data.remove(position);
                notifyDataSetChanged();
            }
        });
    }

    public void add(ProductBean.DataBean.SkuList receiveddata,int count){
        for (int i = 0;i < this.data.size();i++){
            ProductBean.DataBean.SkuList currentsku = this.data.get(i);
            if (currentsku.getId().equals(receiveddata.getId())){
                //int newcount = this.data.get(i).getBuyCount() + receiveddata.getBuyCount();
                currentsku.addCount(count);
                notifyDataSetChanged();
                return;
            }
        }
        receiveddata.setBuyCount(count);
        this.data.add(receiveddata);
        notifyDataSetChanged();
    }

    public ArrayList<ProductBean.DataBean.SkuList> getSelectedSkus(){
        return data;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        TextView sku;

        TextView count;

        ImageView close;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            sku = itemView.findViewById(R.id.sku);
            count = itemView.findViewById(R.id.count);
            close = itemView.findViewById(R.id.close);
        }
    }

}
