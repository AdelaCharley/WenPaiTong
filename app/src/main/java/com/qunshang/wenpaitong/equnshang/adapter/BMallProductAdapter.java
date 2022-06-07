package com.qunshang.wenpaitong.equnshang.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.qunshang.wenpaitong.R;
//import com.qunshang.wenpaitong.equnshang.activity.BMallProductDetailActivity;
import com.qunshang.wenpaitong.equnshang.data.BMallProductBean;
import com.qunshang.wenpaitong.equnshang.utils.CommonUtil;

import java.util.List;

public class BMallProductAdapter extends RecyclerView.Adapter<BMallProductAdapter.ViewHolder> {

    public List<BMallProductBean.DataBean> data;

    public Context context;

    public LayoutInflater inflater;

    public BMallProductAdapter(Context context,List<BMallProductBean.DataBean> data){
        this.context = context;
        this.data = data;
        inflater = LayoutInflater.from(context);
        Log.i("zhangjunooo",data.size() + "");
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.item_bmall_product,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BMallProductBean.DataBean bean = data.get(position);
        Glide.with(context).load(bean.getProductPosterUrl()).into(holder.image);
        holder.name.setText(bean.getProductName());
        holder.price.setText("￥" + bean.getPrice());
        holder.layout.setOnClickListener(v -> {
            /*Intent intent = new Intent(context, BMallProductDetailActivity.class);
            intent.putExtra("productId",bean.getProductId());
            context.startActivity(intent);*/
        });
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) holder.root.getLayoutParams();
        if (data.size() >= 2){
            if (position == data.size() - 1 || position == data.size() - 2) {
                if (data.size() % 2 == 0){
                    layoutParams.setMargins(
                            layoutParams.leftMargin,
                            layoutParams.topMargin,
                            layoutParams.rightMargin,
                            CommonUtil.dp2px(holder.root.getContext(), 30)
                    );
                } else {
                    if (position == data.size() - 1){
                        layoutParams.setMargins(
                                layoutParams.leftMargin,
                                layoutParams.topMargin,
                                layoutParams.rightMargin,
                                CommonUtil.dp2px(holder.root.getContext(), 30)
                        );
                    }
                }
            } else {
                layoutParams.setMargins(
                        layoutParams.leftMargin,
                        layoutParams.topMargin,
                        layoutParams.rightMargin,
                        0
                );
            }
        } else {
            layoutParams.setMargins(
                    layoutParams.leftMargin,
                    layoutParams.topMargin,
                    layoutParams.rightMargin,
                    CommonUtil.dp2px(holder.root.getContext(), 30)
            );
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        ImageView image;

        TextView name;

        TextView price;

        ViewGroup layout;

        View root;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            name = itemView.findViewById(R.id.videoname);
            price = itemView.findViewById(R.id.price);
            root = itemView.findViewById(R.id.root);
            layout = itemView.findViewById(R.id.layout);
        }
    }

}
