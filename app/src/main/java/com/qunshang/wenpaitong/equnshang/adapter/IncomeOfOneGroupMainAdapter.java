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
import com.qunshang.wenpaitong.R;
import com.qunshang.wenpaitong.equnshang.activity.GroupMainIncomeDetailActivity;
import com.qunshang.wenpaitong.equnshang.data.IncomeOfOneGroupMainBean;

import java.util.List;

public class IncomeOfOneGroupMainAdapter extends RecyclerView.Adapter<IncomeOfOneGroupMainAdapter.ViewHolder> {

    public List<IncomeOfOneGroupMainBean.DataBean> data;

    public Context context;

    public LayoutInflater inflater;

    public IncomeOfOneGroupMainAdapter(Context context,List<IncomeOfOneGroupMainBean.DataBean> data){
        this.context = context;
        this.data = data;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.item_income_of_one_groupmain,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        IncomeOfOneGroupMainBean.DataBean bean = data.get(position);
        Glide.with(context).load(bean.getProductImage()).into(holder.image);
        holder.name.setText(bean.getProductName());
        holder.total.setText(bean.getProductIncome() + "元");
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, GroupMainIncomeDetailActivity.class);
                intent.putExtra("data",bean);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        ImageView image;

        TextView name;

        TextView total;

        View layout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            name = itemView.findViewById(R.id.videoname);
            total = itemView.findViewById(R.id.total);
            layout = itemView.findViewById(R.id.layout);

        }
    }

}
