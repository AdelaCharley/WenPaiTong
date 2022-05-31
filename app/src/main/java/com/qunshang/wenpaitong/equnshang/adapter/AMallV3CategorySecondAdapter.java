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
import com.qunshang.wenpaitong.equnshang.activity.AMallSearchResultV3Activity;
import com.qunshang.wenpaitong.equnshang.data.AMallV3CategoryFirstBean;

public class AMallV3CategorySecondAdapter extends RecyclerView.Adapter<AMallV3CategorySecondAdapter.ViewHolder> {

    public List<AMallV3CategoryFirstBean.DataBean> data;

    public Context context;

    public LayoutInflater inflater;

    public AMallV3CategorySecondAdapter(Context context,List<AMallV3CategoryFirstBean.DataBean> data){
        this.context = context;
        this.data = data;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.item_amallv3_second_category,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(context).load(data.get(position).getIcon()).into(holder.image);
        holder.categoryname.setText(data.get(position).getName());
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AMallSearchResultV3Activity.class);
                intent.putExtra("categoryId",data.get(position).getCategoryId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView categoryname;

        ImageView image;

        View layout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryname = itemView.findViewById(R.id.name);
            image = itemView.findViewById(R.id.image);
            layout = itemView.findViewById(R.id.layout);
        }
    }

}
