package com.qunshang.wenpaitong.equnshang.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.kongzue.dialog.interfaces.OnDialogButtonClickListener;
import com.kongzue.dialog.util.BaseDialog;
import com.kongzue.dialog.v3.MessageDialog;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.qunshang.wenpaitong.R;
import com.qunshang.wenpaitong.equnshang.activity.AMallV3ProductDetailActivity;
import com.qunshang.wenpaitong.equnshang.activity.AfterSaleDetailActivity;
import com.qunshang.wenpaitong.equnshang.activity.ApplyAfterSaleActivity;
import com.qunshang.wenpaitong.equnshang.activity.DoApplyAfterSaleActivity;
import com.qunshang.wenpaitong.equnshang.activity.ExpressDetailActivityV2;
import com.qunshang.wenpaitong.equnshang.activity.GoPayActivityV2;
import com.qunshang.wenpaitong.equnshang.activity.OrderActivityV2;
import com.qunshang.wenpaitong.equnshang.activity.OrderDetailActivityV2;
import com.qunshang.wenpaitong.equnshang.activity.PinTuanDetailActivity;
import com.qunshang.wenpaitong.equnshang.data.BaseHttpBean;
import com.qunshang.wenpaitong.equnshang.data.OrderBean;
import com.qunshang.wenpaitong.equnshang.model.ApiManager;
import com.qunshang.wenpaitong.equnshang.utils.CommonUtil;
import com.qunshang.wenpaitong.equnshang.utils.KTUtil;
import com.qunshang.wenpaitong.equnshang.utils.StringUtils;

public class OrderAdapterV2 extends RecyclerView.Adapter<OrderAdapterV2.ViewHolder> {

    public List<OrderBean.DataBean> data;

    public Context context;

    public LayoutInflater inflater;

    public OrderAdapterV2(Context context, List<OrderBean.DataBean> data){
        this.context = context;
        this.data = data;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public OrderAdapterV2.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.item_orderv2,parent,false));
    }

    public void goToDetailActivity(int position){
        Intent intent = new Intent(context, OrderDetailActivityV2.class);
        intent.putExtra("id",data.get(position).getOrderId());
        intent.putExtra("order",data.get(position));
        context.startActivity(intent);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderAdapterV2.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        OrderBean.DataBean bean = data.get(position);
        holder.ordernum.setText("订单号：" + bean.getOrderSn());
        Glide.with(context).load(bean.getProductPosterUrl()).into(holder.image);
        holder.product_name.setText(bean.getProductName());
        holder.sku.setText("规格：" + bean.getProductSkuName());
        holder.get_work_point.setText(bean.getCredit());
        holder.desc.setText(bean.getGetCreditTime());
        holder.count.setText("x " + bean.getProductSkuNumber());
        if (bean.getFreightAmount() == 0){
            holder.status_text.setText("实付：￥" + bean.getPayAmount() + "(免运费)");
        } else {
            holder.status_text.setText("实付：￥" + bean.getPayAmount() + "(含运费：￥" + bean.getFreightAmount() + ")");
        }
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) holder.layout.getLayoutParams();
        if (position == data.size() - 1){
            layoutParams.setMargins(layoutParams.leftMargin,layoutParams.topMargin,layoutParams.rightMargin, CommonUtil.dp2px(context,30));
        } else {
            layoutParams.setMargins(layoutParams.leftMargin,layoutParams.topMargin,layoutParams.rightMargin, 0);
        }
        switch (bean.getStatus()){
            case 10:
                holder.type_text.setText("待付款");
                holder.seegroupdetail.setVisibility(View.GONE);
                holder.gotoinvite.setVisibility(View.GONE);
                holder.applyexchangemoney.setVisibility(View.GONE);
                holder.seeexpress.setVisibility(View.GONE);
                holder.confrim.setVisibility(View.GONE);
                holder.applyshouhou.setVisibility(View.GONE);
                holder.buyagain.setVisibility(View.GONE);
                holder.cancelorder.setVisibility(View.VISIBLE);
                holder.gopay.setVisibility(View.VISIBLE);
                break;
            case 20:
                holder.type_text.setText("待成团");
                holder.seegroupdetail.setVisibility(View.VISIBLE);
                holder.gotoinvite.setVisibility(View.VISIBLE);
                holder.applyexchangemoney.setVisibility(View.GONE);
                holder.seeexpress.setVisibility(View.GONE);
                holder.confrim.setVisibility(View.GONE);
                holder.applyshouhou.setVisibility(View.GONE);
                holder.buyagain.setVisibility(View.GONE);
                holder.cancelorder.setVisibility(View.GONE);
                holder.gopay.setVisibility(View.GONE);
                break;
            case 30:
                holder.seegroupdetail.setVisibility(View.GONE);
                holder.gotoinvite.setVisibility(View.GONE);
                holder.applyexchangemoney.setVisibility(View.VISIBLE);
                holder.seeexpress.setVisibility(View.GONE);
                holder.confrim.setVisibility(View.GONE);
                holder.applyshouhou.setVisibility(View.GONE);
                holder.buyagain.setVisibility(View.GONE);
                holder.cancelorder.setVisibility(View.GONE);
                holder.gopay.setVisibility(View.GONE);
                holder.type_text.setText("待发货");
                break;
            case 40:
                holder.type_text.setText("待收货");
                holder.seegroupdetail.setVisibility(View.GONE);
                holder.gotoinvite.setVisibility(View.GONE);
                holder.applyexchangemoney.setVisibility(View.GONE);
                holder.seeexpress.setVisibility(View.VISIBLE);
                holder.confrim.setVisibility(View.VISIBLE);
                holder.applyshouhou.setVisibility(View.GONE);
                holder.buyagain.setVisibility(View.GONE);
                holder.cancelorder.setVisibility(View.GONE);
                holder.gopay.setVisibility(View.GONE);
                break;
            case 50:
                holder.seegroupdetail.setVisibility(View.GONE);
                holder.gotoinvite.setVisibility(View.GONE);
                holder.applyexchangemoney.setVisibility(View.GONE);
                holder.seeexpress.setVisibility(View.GONE);
                holder.confrim.setVisibility(View.GONE);
                holder.applyshouhou.setVisibility(View.VISIBLE);
                holder.buyagain.setVisibility(View.GONE);
                holder.cancelorder.setVisibility(View.GONE);
                holder.gopay.setVisibility(View.GONE);
                holder.type_text.setText("已完成");
                break;
            case 60:
                holder.seegroupdetail.setVisibility(View.GONE);
                holder.gotoinvite.setVisibility(View.GONE);
                holder.applyexchangemoney.setVisibility(View.GONE);
                holder.seeexpress.setVisibility(View.GONE);
                holder.confrim.setVisibility(View.GONE);
                holder.applyshouhou.setVisibility(View.GONE);
                holder.buyagain.setVisibility(View.VISIBLE);
                holder.cancelorder.setVisibility(View.GONE);
                holder.gopay.setVisibility(View.GONE);
                holder.type_text.setText("已关闭");
                break;
            case 70:
                holder.seegroupdetail.setVisibility(View.GONE);
                holder.gotoinvite.setVisibility(View.GONE);
                holder.applyexchangemoney.setVisibility(View.GONE);
                holder.seeexpress.setVisibility(View.GONE);
                holder.confrim.setVisibility(View.GONE);
                holder.applyshouhou.setVisibility(View.GONE);
                holder.buyagain.setVisibility(View.VISIBLE);
                holder.cancelorder.setVisibility(View.GONE);
                holder.gopay.setVisibility(View.GONE);
                holder.type_text.setText("已取消");
                break;
        }
        holder.seegroupdetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seeGroupDetail(position);
            }
        });
        holder.gotoinvite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToInvite(position);
            }
        });

        if (!StringUtils.isEmpty(data.get(position).getAfterSaleSn())){
            holder.applyexchangemoney.setText("退款详情");
        } else {
            holder.applyexchangemoney.setText("申请退款");
        }
        holder.applyexchangemoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                applyExchangeMoney(position);
            }
        });

        holder.seeexpress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seeExpress(position);
            }
        });
        holder.confrim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirm(position);
            }
        });

        if (!StringUtils.isEmpty(data.get(position).getAfterSaleSn())){
            holder.applyshouhou.setText("售后详情");
        } else {
            holder.applyshouhou.setText("申请售后");
        }
        holder.applyshouhou.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                applyShouHou(position);
            }
        });
        holder.buyagain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buyAgain(position);
            }
        });
        holder.cancelorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelOrder(position);
            }
        });
        holder.gopay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goPay(position);
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
        Intent intent = new Intent(context, GoPayActivityV2.class);
        intent.putExtra("price",String.valueOf(data.get(positon).getPayAmount()));
        intent.putExtra("orderId",String.valueOf(data.get(positon).getOrderId()));
        intent.putExtra("orderType","amall");
        context.startActivity(intent);
    }

    public void cancelOrder(int position){
        MessageDialog.show((AppCompatActivity) context, "", "确认取消订单？", "取消订单", "再想想")
                .setOkButton(new OnDialogButtonClickListener() {
                    @Override
                    public boolean onClick(BaseDialog baseDialog, View v) {
                        ApiManager.Companion.getInstance().getApiMallTest().cancelOrder(String.valueOf(data.get(position).getOrderId())).enqueue(new Callback<BaseHttpBean<Integer>>() {
                            @Override
                            public void onResponse(Call<BaseHttpBean<Integer>> call, Response<BaseHttpBean<Integer>> response) {
                                if (response.body() != null){
                                    if (response.body().getCode() == 200){
                                        Toast.makeText(context, "取消订单成功", Toast.LENGTH_SHORT).show();
                                        OrderActivityV2 activityV2 = (OrderActivityV2) context;
                                        activityV2.initView();
                                    } else {
                                        Toast.makeText(context, "取消失败" + response.body().getMsg(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<BaseHttpBean<Integer>> call, Throwable t) {

                            }
                        });
                        return false;
                    }
                });
    }

    public void buyAgain(int position){
        Intent intent = new Intent(context, AMallV3ProductDetailActivity.class);
        intent.putExtra("productId",data.get(position).getProductId());
        context.startActivity(intent);
    }

    public void applyShouHou(int position){
        if (!StringUtils.isEmpty(data.get(position).getAfterSaleSn())){
            Intent intent = new Intent(context, AfterSaleDetailActivity.class);
            intent.putExtra("afterSaleSn",data.get(position).getAfterSaleSn());
            context.startActivity(intent);
            return;
        }
        Intent intent = new Intent(context, ApplyAfterSaleActivity.class);
        intent.putExtra("orderId",data.get(position).getOrderId());
        context.startActivity(intent);
    }

    public void confirm(int position){
        MessageDialog.show((AppCompatActivity) context, "", "确定已收到货？", "确认", "取消").setOkButton(new OnDialogButtonClickListener() {
            @Override
            public boolean onClick(BaseDialog baseDialog, View v) {
                ApiManager.Companion.getInstance().getApiMallTest().confirmReceipt(String.valueOf(data.get(position).getOrderId())).enqueue(new Callback<BaseHttpBean<Integer>>() {
                    @Override
                    public void onResponse(Call<BaseHttpBean<Integer>> call, Response<BaseHttpBean<Integer>> response) {
                        baseDialog.doDismiss();
                        if (response.body() != null){
                            if (response.body().getCode() == 200){
                                Toast.makeText(context, "确认收货成功", Toast.LENGTH_SHORT).show();
                                OrderActivityV2 activityV2 = (OrderActivityV2) context;
                                activityV2.initView();
                            } else {
                                Toast.makeText(context, "确认收货失败" + response.body().getMsg(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<BaseHttpBean<Integer>> call, Throwable t) {

                    }
                });
                return true;
            }
        });
    }

    public void seeExpress(int position){
        Intent intent = new Intent(context, ExpressDetailActivityV2.class);
        intent.putExtra("orderId",data.get(position).getOrderId());
        intent.putExtra("type","amall");
        context.startActivity(intent);
    }

    public void applyExchangeMoney(int position){
        if (StringUtils.isEmpty(data.get(position).getAfterSaleSn())){
            Intent intent = new Intent(context, DoApplyAfterSaleActivity.class);
            intent.putExtra("orderId",data.get(position).getOrderId());
            intent.putExtra("type",10);
            context.startActivity(intent);
        } else {
            Intent intent = new Intent(context, AfterSaleDetailActivity.class);
            intent.putExtra("afterSaleSn",data.get(position).getAfterSaleSn());
            context.startActivity(intent);
        }
    }

    public void seeGroupDetail(int position){
        Intent intent = new Intent(context, PinTuanDetailActivity.class);
        intent.putExtra("orderGroupSn",data.get(position).getOrderGroupSn());
        context.startActivity(intent);
    }

    public void goToInvite(int position){
        KTUtil.Companion.doShare(context,data.get(position).getProductPosterUrl(),data.get(position).getOrderGroupSn(),data.get(position).getPayAmount(),
                data.get(position).getProductName(),data.get(position).getProductSkuName());
    }

    @Override
    public int getItemViewType(int position) {
        OrderBean.DataBean bean = data.get(position);
        return bean.getStatus();
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

        TextView seegroupdetail;

        TextView gotoinvite;

        TextView applyexchangemoney;

        TextView seeexpress;

        TextView confrim;

        TextView applyshouhou;

        TextView buyagain;

        TextView cancelorder;

        TextView gopay;

        ConstraintLayout layout;

        TextView get_work_point;

        TextView desc;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ordernum = itemView.findViewById(R.id.ordernum);
            type_text = itemView.findViewById(R.id.type_text);
            image = itemView.findViewById(R.id.image);
            product_name = itemView.findViewById(R.id.product_name);
            sku = itemView.findViewById(R.id.sku);
            count = itemView.findViewById(R.id.count);
            get_work_point = itemView.findViewById(R.id.get_work_point);
            status_text = itemView.findViewById(R.id.status_text);
            seegroupdetail = itemView.findViewById(R.id.seegroupdetail);
            gotoinvite = itemView.findViewById(R.id.gotoinvite);
            applyexchangemoney = itemView.findViewById(R.id.applyexchangemoney);
            seeexpress = itemView.findViewById(R.id.seeexpress);
            confrim = itemView.findViewById(R.id.confrim);
            applyshouhou = itemView.findViewById(R.id.applyshouhou);
            buyagain = itemView.findViewById(R.id.buyagain);
            cancelorder = itemView.findViewById(R.id.cancelorder);
            gopay = itemView.findViewById(R.id.gopay);
            desc = itemView.findViewById(R.id.desc);
            layout = itemView.findViewById(R.id.layout);
        }
    }

}