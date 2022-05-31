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
import com.qunshang.wenpaitong.equnshang.activity.AMallV3CategoryActivity;
import com.qunshang.wenpaitong.equnshang.activity.FirstCategoryPageActivity;
import com.qunshang.wenpaitong.equnshang.data.AMallV3MainBean;

public class AMallV3MainCategoryAdapter extends RecyclerView.Adapter<AMallV3MainCategoryAdapter.ViewHolder> {

    public List<AMallV3MainBean.DataBean.CategoryBean> data;

    public Context context;

    public LayoutInflater inflater;


    public AMallV3MainCategoryAdapter(Context context,List<AMallV3MainBean.DataBean.CategoryBean> data){
        this.context = context;
        this.data = data;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public AMallV3MainCategoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.item_amallv3_main_categorys,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull AMallV3MainCategoryAdapter.ViewHolder holder, int position) {
        Glide.with(context).load(data.get(position).getIcon()).into(holder.image);
        holder.title.setText(data.get(position).getName());
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (data.get(position).getCategoryId() == 0 | data.get(position).getName().equals("更多")){
                    Intent intent = new Intent(context, AMallV3CategoryActivity.class);
                    context.startActivity(intent);
                    return;
                }
                Intent intent = new Intent(context, FirstCategoryPageActivity.class);
                intent.putExtra("categoryId",data.get(position).getCategoryId());
                intent.putExtra("name",data.get(position).getName());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView image;

        TextView title;

        View layout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            layout = itemView.findViewById(R.id.layout);
            image = itemView.findViewById(R.id.image);
            title = itemView.findViewById(R.id.title);
        }
    }

}
