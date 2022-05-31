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
import com.qunshang.wenpaitong.equnshang.activity.PrizeInfoDetailActivity;
import com.qunshang.wenpaitong.equnshang.data.VIPDayPrizeBudget;

public class VIPDayPrizesAdapter extends RecyclerView.Adapter<VIPDayPrizesAdapter.ViewHolder> {

    public List<VIPDayPrizeBudget.DataBean.PrizeBean> data;

    public Context context;

    public LayoutInflater inflater;

    public VIPDayPrizesAdapter(Context context,List<VIPDayPrizeBudget.DataBean.PrizeBean> data) {
        this.context = context;
        this.data = data;
        inflater = LayoutInflater.from(context);
    }
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.item_prize_budget,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        VIPDayPrizeBudget.DataBean.PrizeBean bean = data.get(position);
        holder.kind.setText(bean.getKind());
        Glide.with(context).load(bean.getUrl()).into(holder.icon);
        holder.name.setText(bean.getName());
        holder.desc.setText(bean.getDesc());
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position == 0){
                    return;
                }
                Intent intent = new Intent(context, PrizeInfoDetailActivity.class);
                intent.putExtra("prizeId", Integer.parseInt(bean.getPrizeId()));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        TextView kind;

        ImageView icon;

        TextView name;

        TextView desc;

        View layout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            layout = itemView.findViewById(R.id.layout);
            kind = itemView.findViewById(R.id.prizekind);
            icon = itemView.findViewById(R.id.prize_icon);
            name = itemView.findViewById(R.id.prize_name);
            desc = itemView.findViewById(R.id.prize_desc);
        }
    }

}
