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
import com.qunshang.wenpaitong.equnshang.activity.GroupOwnerProductDetailActivity;
import com.qunshang.wenpaitong.equnshang.data.GroupMainProductBean;

import java.util.List;

public class GroupMainDetailShopAdapter extends RecyclerView.Adapter<GroupMainDetailShopAdapter.ViewHolder> {

    public List<GroupMainProductBean.DataBean> data;

    public Context context;

    public LayoutInflater inflater;

    public GroupMainDetailShopAdapter(Context context,List<GroupMainProductBean.DataBean> data){
        this.context = context;
        this.data = data;
        inflater = LayoutInflater.from(context);
        //Toast.makeText(context, "yigong" + data.size(), Toast.LENGTH_SHORT).show();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.item_group_main_detail_shop,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        GroupMainProductBean.DataBean bean = data.get(position);
        Glide.with(context).load(bean.getImage()).into(holder.imageView);
        holder.name.setText(bean.getName());
        holder.price.setText("￥" + bean.getPrice());
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, GroupOwnerProductDetailActivity.class);
                intent.putExtra("productId",bean.getProductId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        ImageView imageView;

        TextView name;

        TextView price;

        ViewGroup layout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image);
            name = itemView.findViewById(R.id.videoname);
            price = itemView.findViewById(R.id.price);
            layout = itemView.findViewById(R.id.layout);
        }
    }

}
