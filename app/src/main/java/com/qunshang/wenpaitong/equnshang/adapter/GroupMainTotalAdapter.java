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
import com.qunshang.wenpaitong.equnshang.data.TotalIncomeBean;
import com.qunshang.wenpaitong.equnshang.utils.StringUtils;

import java.util.List;

public class GroupMainTotalAdapter extends RecyclerView.Adapter<GroupMainTotalAdapter.ViewHolder> {

    public List<TotalIncomeBean.DataBean.Data> data;

    public Context context;

    public LayoutInflater inflater;

    public GroupMainTotalAdapter(Context context,List<TotalIncomeBean.DataBean.Data> data){
        this.context = context;
        this.data = data;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.item_group_main_total,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TotalIncomeBean.DataBean.Data data = this.data.get(position);
        if (!StringUtils.isEmpty(data.getGroupOwnerImage())){
            Glide.with(context).load(data.getGroupOwnerImage()).into(holder.image);
        }
        if (!StringUtils.isEmpty(data.getGroupOwnerName())){
            holder.name.setText(data.getGroupOwnerName());
        }
        holder.count.setText(data.getProductNum() + "款商品");
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, GroupMainDetailActivity.class);
                intent.putExtra("name",data.getGroupOwnerName());
                intent.putExtra("id",data.getGroupOwnerId());
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

        TextView count;

        ViewGroup layout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            name = itemView.findViewById(R.id.videoname);
            count = itemView.findViewById(R.id.productcount);
            layout = itemView.findViewById(R.id.layout);
        }
    }

}
