package com.qunshang.wenpaitong.equnshang.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.qunshang.wenpaitong.R;
import com.qunshang.wenpaitong.equnshang.data.IncomeDetailBean;

import java.util.List;

public class IncomeDetailAdapter extends RecyclerView.Adapter<IncomeDetailAdapter.ViewHolder> {

    public List<IncomeDetailBean.DataBean> data;

    public Context context;

    public LayoutInflater inflater;

    public IncomeDetailAdapter(Context context,List<IncomeDetailBean.DataBean> data){
        this.context = context;
        this.data = data;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.item_income_detail,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        IncomeDetailBean.DataBean bean = data.get(position);
        holder.name.setText(bean.getOrderName());
        holder.time.setText(bean.getOrderTime());
        holder.totalprice.setText(String.valueOf(bean.getProfitMoney()));
        holder.price.setText(String.valueOf(bean.getOrderMoney()));
        holder.username.setText(bean.getUserName());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        TextView name;

        TextView time;

        TextView totalprice;

        TextView username;

        TextView price;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.videoname);
            time = itemView.findViewById(R.id.time);
            totalprice = itemView.findViewById(R.id.total_price);
            username = itemView.findViewById(R.id.username);
            price = itemView.findViewById(R.id.price);
        }
    }

}
