package com.qunshang.wenpaitong.equnshang.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;
import com.qunshang.wenpaitong.R;
import com.qunshang.wenpaitong.equnshang.activity.ExpressDetailActivityV2;
import com.qunshang.wenpaitong.equnshang.activity.SubmitWenBanTongAddressActivity;
import com.qunshang.wenpaitong.equnshang.activity.WenBanTongApplyExchangeActivity;
import com.qunshang.wenpaitong.equnshang.activity.WenBanTongOrderDetailActivity;
import com.qunshang.wenpaitong.equnshang.data.WenBanTongOrderDetailBean;
import com.qunshang.wenpaitong.equnshang.data.WenBanTongOrderListBean;
import com.qunshang.wenpaitong.equnshang.utils.CommonUtil;
import com.qunshang.wenpaitong.equnshang.utils.DialogUtil;
import com.qunshang.wenpaitong.equnshang.utils.StringUtils;

public class WenBanTongOrderListAdapter extends RecyclerView.Adapter<WenBanTongOrderListAdapter.ViewHolder> {

    public List<WenBanTongOrderListBean.DataBean> data = new ArrayList<>();

    public Context context;

    public LayoutInflater inflater;

    public WenBanTongOrderListAdapter(Context context){
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public WenBanTongOrderListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.item_wenbantong_order_list,parent,false));
    }

    public void remove() {
        this.data.removeAll(data);

    }

    public void goToDetailActivity(int position){
        Intent intent = new Intent(context, WenBanTongOrderDetailActivity.class);
        intent.putExtra("orderSn",data.get(position).getOrder().getOrderSn());
        context.startActivity(intent);
    }

    public void add(List<WenBanTongOrderListBean.DataBean> datas){
        if (datas == null){
            return;
        }
        this.data.addAll(datas);
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull WenBanTongOrderListAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        WenBanTongOrderListBean.DataBean bean = data.get(position);
        holder.ordernum.setText("订单号：" + bean.getOrder().getOrderSn());
        Glide.with(context).load(bean.getProduct().getProductPoster()).into(holder.image);
        holder.product_name.setText(bean.getProduct().getProductName());
        holder.sku.setText("￥" + bean.getProduct().getOrderProductPrice());
        holder.count.setText("x " + bean.getOrder().getProductQuantity());
        holder.status_text.setText("实付：￥" + bean.getOrder().getPayAmount());
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) holder.layout.getLayoutParams();
        if (position == data.size() - 1){
            layoutParams.setMargins(layoutParams.leftMargin,layoutParams.topMargin,layoutParams.rightMargin, CommonUtil.dp2px(context,30));
        } else {
            layoutParams.setMargins(layoutParams.leftMargin,layoutParams.topMargin,layoutParams.rightMargin, 0);
        }
        switch (bean.getOrder().getOrderStatus()){
            case 20:
                holder.type_text.setText("已支付");
                holder.label_line3.setVisibility(View.VISIBLE);
                holder.gopay.setVisibility(View.VISIBLE);
                holder.tihuo.setVisibility(View.VISIBLE);
                holder.seeexpress.setVisibility(View.GONE);
                break;
            case 30:
                holder.type_text.setText("已提货");
                holder.label_line3.setVisibility(View.GONE);
                holder.gopay.setVisibility(View.GONE);
                holder.tihuo.setVisibility(View.GONE);
                holder.seeexpress.setVisibility(View.VISIBLE);
                break;
            case 35:
                holder.type_text.setText("退款中");
                holder.label_line3.setVisibility(View.GONE);
                holder.gopay.setVisibility(View.GONE);
                holder.seeexpress.setVisibility(View.GONE);
                holder.tihuo.setVisibility(View.GONE);
                break;
            case 40:
                holder.label_line3.setVisibility(View.GONE);
                holder.gopay.setVisibility(View.GONE);
                holder.type_text.setText("已退款");
                holder.seeexpress.setVisibility(View.GONE);
                holder.tihuo.setVisibility(View.GONE);
                break;
            default:
                holder.label_line3.setVisibility(View.GONE);
                holder.gopay.setVisibility(View.GONE);
                holder.type_text.setText("未支付");
                holder.seeexpress.setVisibility(View.GONE);
                holder.tihuo.setVisibility(View.GONE);
                break;
        }
        holder.seeexpress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (StringUtils.isEmpty(bean.getOrder().getDeliveryCode())){
                    DialogUtil.toast(context,"平台暂未发货");
                } else {
                    Intent intent = new Intent(context, ExpressDetailActivityV2.class);
                    intent.putExtra("type","wenbantong");
                    intent.putExtra("orderSn",bean.getOrder().getOrderSn());
                    context.startActivity(intent);
                }
            }
        });
        holder.gopay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goPay(position);
            }
        });
        holder.tihuo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, SubmitWenBanTongAddressActivity.class);
                intent.putExtra("orderSn",bean.getOrder().getOrderSn());
                context.startActivity(intent);
            }
        });
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToDetailActivity(position);
            }
        });
    }

    public void goPay(int positon){
        Intent intent = new Intent(context, WenBanTongApplyExchangeActivity.class);
        WenBanTongOrderDetailBean.DataBean dataBean = new WenBanTongOrderDetailBean.DataBean();
        dataBean.setOrder(data.get(positon).getOrder());
        dataBean.setCompany(data.get(positon).getCompany());
        dataBean.setProduct(data.get(positon).getProduct());
        intent.putExtra("data",dataBean);
        context.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        TextView ordernum;

        TextView type_text;

        ImageView image;

        TextView product_name;

        TextView sku;

        TextView count;

        TextView status_text;

        TextView gopay;

        ConstraintLayout layout;

        View label_line3;

        TextView tihuo;

        TextView seeexpress;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            seeexpress = itemView.findViewById(R.id.seeexpress);
            tihuo = itemView.findViewById(R.id.tihuo);
            ordernum = itemView.findViewById(R.id.ordernum);
            type_text = itemView.findViewById(R.id.type_text);
            image = itemView.findViewById(R.id.image);
            product_name = itemView.findViewById(R.id.product_name);
            sku = itemView.findViewById(R.id.sku);
            count = itemView.findViewById(R.id.count);
            status_text = itemView.findViewById(R.id.status_text);
            gopay = itemView.findViewById(R.id.gopay);
            layout = itemView.findViewById(R.id.layout);
            label_line3 = itemView.findViewById(R.id.label_line3);
        }
    }

}
