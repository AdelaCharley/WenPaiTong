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
import com.qunshang.wenpaitong.equnshang.activity.GroupMainDetailActivity;
import com.qunshang.wenpaitong.equnshang.data.GroupMainListBean;
import com.qunshang.wenpaitong.equnshang.utils.StringUtils;

import java.util.List;

public class GroupMainMainAdapter extends RecyclerView.Adapter<GroupMainMainAdapter.ViewHolder> {

    public List<GroupMainListBean.DataBean> data;

    public Context context;

    public LayoutInflater inflater;

    public GroupMainMainAdapter(Context context,List<GroupMainListBean.DataBean> data){
        this.context = context;
        this.data = data;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.item_group_main_mainlist,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        GroupMainListBean.DataBean bean = data.get(position);
        if (!StringUtils.isEmpty(bean.getImage())){
            Glide.with(context).load(bean.getImage()).into(holder.imageView);
        }
        if (!StringUtils.isEmpty(bean.getName())){
            holder.textView.setText(bean.getName());
        }
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, GroupMainDetailActivity.class);
                intent.putExtra("id",bean.getId());
                intent.putExtra("name",bean.getName());
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

        TextView textView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.icon);
            textView = itemView.findViewById(R.id.videoname);
        }
    }

}
