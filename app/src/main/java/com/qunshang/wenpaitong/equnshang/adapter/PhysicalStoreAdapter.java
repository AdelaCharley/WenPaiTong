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
import com.qunshang.wenpaitong.equnshang.activity.LoginActivity;
import com.qunshang.wenpaitong.equnshang.activity.PhysicalStoreDetailActivity;
import com.qunshang.wenpaitong.equnshang.data.PhysicalStoreBean;
import com.qunshang.wenpaitong.equnshang.utils.CommonUtil;
import com.qunshang.wenpaitong.equnshang.utils.StringUtils;
import com.qunshang.wenpaitong.equnshang.utils.UserHelper;

public class PhysicalStoreAdapter extends RecyclerView.Adapter<PhysicalStoreAdapter.ViewHolder> {

    public List<PhysicalStoreBean.DataBean> data;

    public Context context;

    public LayoutInflater inflater;


    public PhysicalStoreAdapter(Context context,List<PhysicalStoreBean.DataBean> data){
        this.context = context;
        this.data = data;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.item_physical_stores,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PhysicalStoreBean.DataBean dataBean = data.get(position);
        Glide.with(context).load(dataBean.getVendorHeadImg()).into(holder.image);
        holder.title.setText(dataBean.getVendorName());
        holder.subtitle.setText(dataBean.getVendorDesc());
        if (StringUtils.isEmpty(dataBean.getTag())){
            holder.tag.setVisibility(View.GONE);
        } else {
            holder.tag.setText(dataBean.getTag());
        }
        holder.distance.setText(dataBean.getDistance());
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*if (!UserHelper.isLogin(holder.layout.getContext())) {
                    holder.layout.getContext().startActivity(new Intent(holder.layout.getContext(), LoginActivity.class));
                    return;
                }*/
                Intent intent = new Intent(context, PhysicalStoreDetailActivity.class);
                intent.putExtra("vendorId",dataBean.getVendorId());
                context.startActivity(intent);
            }
        });

        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) holder.layout.getLayoutParams();
        if (position == data.size() - 1) {
            layoutParams.setMargins(
                    layoutParams.leftMargin,
                    layoutParams.topMargin,
                    layoutParams.rightMargin,
                    CommonUtil.dp2px(holder.layout.getContext(), 30)
            );
        } else {
            layoutParams.setMargins(
                    layoutParams.leftMargin,
                    layoutParams.topMargin,
                    layoutParams.rightMargin,
                    0
            );
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        ImageView image;

        TextView title;

        TextView subtitle;

        TextView tag;

        TextView distance;

        View layout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            title = itemView.findViewById(R.id.vendortitle);
            subtitle = itemView.findViewById(R.id.subtitle);
            tag = itemView.findViewById(R.id.tag);
            distance = itemView.findViewById(R.id.distance);
            layout = itemView.findViewById(R.id.layout);
        }
    }

}
