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
import com.qunshang.wenpaitong.equnshang.data.WenBanTongProductBean;

public class WenBanTongProductSpecAdapter extends RecyclerView.Adapter<WenBanTongProductSpecAdapter.ViewHolder> {

    public List<WenBanTongProductBean.DataBean.AttributeList> data;

    public Context context;

    public LayoutInflater inflater;

    public WenBanTongProductSpecAdapter(Context context,List<WenBanTongProductBean.DataBean.AttributeList> data){
        this.context = context;
        this.data = data;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.item_wenbantong_product_spec,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.spec_name.setText(data.get(position).getAttributeName());
        holder.spec_value.setText(data.get(position).getValue());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView spec_name;

        TextView spec_value;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            spec_name = itemView.findViewById(R.id.spec_name);
            spec_value = itemView.findViewById(R.id.spec_value);
        }
    }
}