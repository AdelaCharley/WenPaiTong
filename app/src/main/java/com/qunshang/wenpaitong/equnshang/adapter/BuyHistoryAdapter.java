package com.qunshang.wenpaitong.equnshang.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import com.qunshang.wenpaitong.R;
import com.qunshang.wenpaitong.equnshang.data.BuyHistoryBean;

public class BuyHistoryAdapter extends RecyclerView.Adapter <BuyHistoryAdapter.ViewHolder> {

    public List<BuyHistoryBean.DataBean> data;

    public Context context;

    public LayoutInflater inflater;

    public BuyHistoryAdapter(Context context,List<BuyHistoryBean.DataBean> data){
        this.context = context;
        this.data = data;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.item_buy_history,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BuyHistoryBean.DataBean bean = data.get(position);
        holder.name.setText(bean.getName());
        holder.buytime.setText(bean.getPaymentTime());
        holder.price.setText(bean.getPrice());
        holder.expiredtime.setText(bean.getExpireTime());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView name;

        TextView buytime;

        TextView price;

        TextView expiredtime;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            buytime = itemView.findViewById(R.id.buytime);
            price = itemView.findViewById(R.id.price);
            expiredtime = itemView.findViewById(R.id.expiredtime);
        }
    }

}
