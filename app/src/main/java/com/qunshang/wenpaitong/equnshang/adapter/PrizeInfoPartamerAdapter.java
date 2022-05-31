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
import com.qunshang.wenpaitong.equnshang.data.PrizeInfoDetailBean;

public class PrizeInfoPartamerAdapter extends RecyclerView.Adapter<PrizeInfoPartamerAdapter.ViewHolder> {

    public List<PrizeInfoDetailBean.DataBean.Parameter> data;

    public Context context;

    public LayoutInflater inflater;

    public PrizeInfoPartamerAdapter(Context context, List<PrizeInfoDetailBean.DataBean.Parameter> data){
        this.context = context;
        this.data = data;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.item_prize_info_partamer,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PrizeInfoDetailBean.DataBean.Parameter parameter = data.get(position);
        holder.key.setText(parameter.getKey());
        holder.value.setText(parameter.getValue());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        TextView key;

        TextView value;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            key = itemView.findViewById(R.id.key);
            value = itemView.findViewById(R.id.value);
        }
    }

}
