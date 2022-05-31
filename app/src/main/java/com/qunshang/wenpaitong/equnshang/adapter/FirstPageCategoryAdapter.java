package com.qunshang.wenpaitong.equnshang.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import com.qunshang.wenpaitong.R;
import com.qunshang.wenpaitong.equnshang.activity.AMallSearchResultV3Activity;
import com.qunshang.wenpaitong.equnshang.data.AMallV3FirstCategoryPageBean;
import com.qunshang.wenpaitong.equnshang.utils.CommonUtil;

public class FirstPageCategoryAdapter extends RecyclerView.Adapter<AMallV3MainCategoryAdapter.ViewHolder> {

    public List<AMallV3FirstCategoryPageBean.DataBean.CategoryBean> data;

    public Context context;

    public LayoutInflater inflater;


    public FirstPageCategoryAdapter(Context context,List<AMallV3FirstCategoryPageBean.DataBean.CategoryBean> data){
        this.context = context;
        this.data = data;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public AMallV3MainCategoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AMallV3MainCategoryAdapter.ViewHolder(inflater.inflate(R.layout.item_first_page_category,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull AMallV3MainCategoryAdapter.ViewHolder holder, int position) {
        Glide.with(context).load(data.get(position).getIcon()).into(holder.image);
        holder.title.setText(data.get(position).getName());
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AMallSearchResultV3Activity.class);
                intent.putExtra("categoryId",data.get(position).getCategoryId());
                context.startActivity(intent);
            }
        });
        ViewGroup.LayoutParams layoutParams = holder.layout.getLayoutParams();
        layoutParams.width = (int) (CommonUtil.getScreenWidthInPx(context) * 0.2);
        holder.layout.setLayoutParams(layoutParams);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView image;

        TextView title;

        LinearLayout layout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            layout = itemView.findViewById(R.id.layout);
            image = itemView.findViewById(R.id.image);
            title = itemView.findViewById(R.id.title);
        }
    }

}
