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
import com.qunshang.wenpaitong.equnshang.data.ProductBeanV2;

public class SpecAdapterV2 extends RecyclerView.Adapter<SpecAdapterV2.ViewHolder> {

    public List<ProductBeanV2.DataBean.AttributeList> data;

    public Context context;

    public LayoutInflater inflater;

    public SpecAdapterV2(Context context,List<ProductBeanV2.DataBean.AttributeList> data){
        this.context = context;
        this.data = data;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.item_specv2,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.spec_name.setText(data.get(position).getTitle());
        holder.spec_value.setText(data.get(position).getText());
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