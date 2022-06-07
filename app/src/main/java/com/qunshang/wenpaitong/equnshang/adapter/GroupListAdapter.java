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
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import com.qunshang.wenpaitong.R;
import com.qunshang.wenpaitong.equnshang.activity.AMallV3ProductDetailActivity;
//import com.qunshang.wenpaitong.equnshang.activity.OrderDetailActivityV2;
import com.qunshang.wenpaitong.equnshang.activity.PinTuanDetailActivity;
import com.qunshang.wenpaitong.equnshang.data.MyGroupBean;
import com.qunshang.wenpaitong.equnshang.utils.CommonUtil;

public class GroupListAdapter extends RecyclerView.Adapter<GroupListAdapter.ViewHolder> {

    public List<MyGroupBean.DataBean> data;

    public Context context;

    public LayoutInflater inflater;

    public GroupListAdapter(Context context,List<MyGroupBean.DataBean> data){
        this.context = context;
        this.data = data;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.item_group_list,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MyGroupBean.DataBean bean = data.get(position);
        Glide.with(context).load(bean.getSkuPic()).into(holder.image);
        holder.name.setText(bean.getProductName());
        holder.price.setText("￥" + String.valueOf(bean.getPayAmount()));
        switch (bean.getStatus()){
            case 10:
                holder.status.setText("拼团中");
                holder.groupdetail.setVisibility(View.VISIBLE);
                holder.orderdetail.setVisibility(View.VISIBLE);
                holder.again.setVisibility(View.GONE);
                break;
            case 20:
                holder.status.setText("拼团成功");
                holder.groupdetail.setVisibility(View.VISIBLE);
                holder.orderdetail.setVisibility(View.VISIBLE);
                holder.again.setVisibility(View.GONE);
                break;
            case 30:
                holder.status.setText("拼团成功");
                holder.groupdetail.setVisibility(View.VISIBLE);
                holder.orderdetail.setVisibility(View.VISIBLE);
                holder.again.setVisibility(View.GONE);
                break;
            case 40:
                holder.status.setText("拼团失败");
                holder.groupdetail.setVisibility(View.VISIBLE);
                holder.orderdetail.setVisibility(View.VISIBLE);
                holder.again.setVisibility(View.VISIBLE);
                break;
        }
        holder.groupdetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("grouplist", "当前的状态是" + data.get(position).getStatus());
                Intent intent = new Intent(context,PinTuanDetailActivity.class);
                intent.putExtra("orderGroupSn",bean.getOrderGroupSn());
                context.startActivity(intent);
            }
        });
        holder.orderdetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent intent = new Intent(context, OrderDetailActivityV2.class);
                intent.putExtra("id",data.get(position).getOrderId());
                context.startActivity(intent);*/
            }
        });
        holder.again.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AMallV3ProductDetailActivity.class);
                intent.putExtra("productId", bean.getProductId());
                context.startActivity(intent);
            }
        });
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) holder.layout.getLayoutParams();
        if (position == data.size() - 1){
            layoutParams.setMargins(layoutParams.leftMargin,layoutParams.topMargin,layoutParams.rightMargin, CommonUtil.dp2px(context,30));
        } else {
            layoutParams.setMargins(layoutParams.leftMargin,layoutParams.topMargin,layoutParams.rightMargin, 0);
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        ImageView image;

        TextView name;

        TextView label;

        TextView price;

        TextView status;

        TextView orderdetail;

        TextView groupdetail;

        TextView again;

        ConstraintLayout layout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            layout = itemView.findViewById(R.id.layout);
            image = itemView.findViewById(R.id.image);
            name = itemView.findViewById(R.id.name);
            label = itemView.findViewById(R.id.label);
            price = itemView.findViewById(R.id.price);
            status = itemView.findViewById(R.id.status);
            orderdetail = itemView.findViewById(R.id.orderdetail);
            groupdetail = itemView.findViewById(R.id.groupdetail);
            again = itemView.findViewById(R.id.again);
        }
    }

}
