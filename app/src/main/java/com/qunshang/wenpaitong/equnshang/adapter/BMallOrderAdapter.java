package com.qunshang.wenpaitong.equnshang.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.kongzue.dialog.interfaces.OnDialogButtonClickListener;
import com.kongzue.dialog.util.BaseDialog;
import com.kongzue.dialog.v3.BottomMenu;
import com.kongzue.dialog.v3.MessageDialog;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.qunshang.wenpaitong.R;
import com.qunshang.wenpaitong.equnshang.activity.AddressActivityV2;
import com.qunshang.wenpaitong.equnshang.activity.BMallProductDetailActivity;
import com.qunshang.wenpaitong.equnshang.activity.ExpressDetailActivity;
import com.qunshang.wenpaitong.equnshang.activity.GoPayActivity;
import com.qunshang.wenpaitong.equnshang.activity.BMallOrdersActivity;
import com.qunshang.wenpaitong.equnshang.activity.MyBuyOrderDetailActivity;
import com.qunshang.wenpaitong.equnshang.data.BaseHttpBean;
import com.qunshang.wenpaitong.equnshang.data.MyBuyBean;
import com.qunshang.wenpaitong.equnshang.model.ApiManager;
import com.qunshang.wenpaitong.equnshang.utils.CommonUtil;
import com.qunshang.wenpaitong.equnshang.utils.StringUtils;
import com.qunshang.wenpaitong.equnshang.utils.DialogUtil;

public class BMallOrderAdapter extends RecyclerView.Adapter<BMallOrderAdapter.BuyViewHolder> {

    public List<MyBuyBean.DataBean> data;

    public Context context;

    public LayoutInflater inflater;

    public BMallOrderAdapter(Context context, List<MyBuyBean.DataBean> data){
        this.context = context;
        this.data = data;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public BuyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new BuyViewHolder(inflater.inflate(R.layout.item_buy_list,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull BuyViewHolder holder, int position) {
        MyBuyBean.DataBean bean = data.get(position);
        if (!StringUtils.isEmpty(bean.getManufactureName())){
            holder.store_name.setText(bean.getManufactureName());
        }
        if (!StringUtils.isEmpty(bean.getProductPosterUrl())){
            Glide.with(context).load(bean.getProductPosterUrl()).into(holder.image_product);
        }
        if (!StringUtils.isEmpty(bean.getProductName())) {
            holder.product_name.setText(bean.getProductName());
        }
        holder.product_price.setText("￥" + String.valueOf(bean.getPrice()));
        switch (bean.getOrderStatus()){
            case 60: //订单已关闭
                holder.cancelorder.setVisibility(View.GONE);
                holder.seeexpress.setVisibility(View.GONE);
                holder.seedetail.setVisibility(View.VISIBLE);

                holder.buyagain.setVisibility(View.VISIBLE);

                holder.confirmdeliver.setVisibility(View.GONE);
                holder.gopay.setVisibility(View.GONE);

                holder.more.setVisibility(View.VISIBLE);

                holder.status.setText(bean.getCreateTime() + " 订单已关闭");
                break;
            case 50://交易成功
                holder.cancelorder.setVisibility(View.GONE);
                holder.seeexpress.setVisibility(View.VISIBLE);

                holder.seedetail.setVisibility(View.GONE);
                holder.buyagain.setVisibility(View.VISIBLE);

                holder.confirmdeliver.setVisibility(View.GONE);
                holder.gopay.setVisibility(View.GONE);
                holder.more.setVisibility(View.VISIBLE);
                holder.status.setText(bean.getCreateTime() + " 交易成功");
                break;
            case 40://商家已发货
                holder.cancelorder.setVisibility(View.GONE);
                holder.seeexpress.setVisibility(View.VISIBLE);

                holder.seedetail.setVisibility(View.VISIBLE);

                holder.buyagain.setVisibility(View.GONE);
                holder.confirmdeliver.setVisibility(View.VISIBLE);

                holder.gopay.setVisibility(View.GONE);
                holder.more.setVisibility(View.GONE);
                holder.status.setText(bean.getCreateTime() + " 商家已发货");
                break;
            case 30://等待商家发货
                holder.cancelorder.setVisibility(View.GONE);
                holder.seeexpress.setVisibility(View.GONE);
                holder.seedetail.setVisibility(View.VISIBLE);

                holder.buyagain.setVisibility(View.GONE);
                holder.confirmdeliver.setVisibility(View.GONE);
                holder.gopay.setVisibility(View.GONE);
                holder.more.setVisibility(View.GONE);
                holder.status.setText(bean.getCreateTime() + " 等待商家发货");
                break;
            case 10://待支付
                holder.cancelorder.setVisibility(View.VISIBLE);

                holder.seeexpress.setVisibility(View.GONE);
                holder.seedetail.setVisibility(View.VISIBLE);

                holder.buyagain.setVisibility(View.GONE);
                holder.confirmdeliver.setVisibility(View.GONE);
                holder.gopay.setVisibility(View.VISIBLE);
                holder.more.setVisibility(View.VISIBLE);
                holder.status.setText(bean.getCreateTime() + " 等待支付中");
                break;
            case 73:
                holder.cancelorder.setVisibility(View.GONE);

                holder.seeexpress.setVisibility(View.GONE);
                holder.seedetail.setVisibility(View.VISIBLE);

                holder.buyagain.setVisibility(View.GONE);
                holder.confirmdeliver.setVisibility(View.GONE);
                holder.gopay.setVisibility(View.GONE);
                holder.more.setVisibility(View.GONE);
                holder.status.setText(bean.getCreateTime() + " 退款成功");
                break;
        }
        holder.cancelorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelorder(position);
            }
        });
        holder.seeexpress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seeexpress(position);
            }
        });
        holder.seedetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seedetail(position);
            }
        });
        holder.buyagain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buyagain(position);
            }
        });
        holder.confirmdeliver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmdeliver(position);
            }
        });
        holder.gopay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gopay(position);
            }
        });
        holder.more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seemore(position);
            }
        });
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) holder.root.getLayoutParams();
        if (position == data.size() - 1) {
            layoutParams.setMargins(
                    layoutParams.leftMargin,
                    layoutParams.topMargin,
                    layoutParams.rightMargin,
                    CommonUtil.dp2px(holder.root.getContext(), 30)
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

    public void seemore(int position){
        if (data.get(position).getOrderStatus() == 10){
            BottomMenu.show((AppCompatActivity) context, new String[]{"修改地址"}, (text, index) -> {
                if (index == 0){
                    BMallOrdersActivity.Companion.setOrderId(data.get(position).getOrderId());
                    Intent intent = new Intent(context, AddressActivityV2.class);
                    ((AppCompatActivity) context).startActivityForResult(intent, BMallOrdersActivity.Companion.getTYPE_CHOOSE_ADDRESS());
                }
            });
        } else {
            BottomMenu.show((AppCompatActivity) context, new String[]{"删除订单"}, (text, index) -> {
                if (index == 0){
                    MessageDialog.show((AppCompatActivity) context, "", "确认删除？", "删除订单", "再想想")
                            .setOkButton(new OnDialogButtonClickListener() {
                                @Override
                                public boolean onClick(BaseDialog baseDialog, View v) {
                                    ApiManager.Companion.getInstance().getApiMallTest().deleteBMallOrder(String.valueOf(data.get(position).getOrderId())).enqueue(new Callback<BaseHttpBean<Integer>>() {
                                        @Override
                                        public void onResponse(Call<BaseHttpBean<Integer>> call, Response<BaseHttpBean<Integer>> response) {
                                            if (response.body() == null){
                                                return;
                                            }
                                            if (response.body().getCode() == 200){
                                                DialogUtil.toast(context,"删除订单");
                                                BMallOrderAdapter.this.data.remove(position);
                                                notifyItemRemoved(position);
                                            } else {
                                                DialogUtil.toast(context,response.body().getMsg());
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
            });
        }

    }

    public void gopay(int position) {
        Intent intent = new Intent(context, GoPayActivity.class);
        intent.putExtra("price", String.valueOf(data.get(position).getPrice()));
        intent.putExtra("orderId", String.valueOf(data.get(position).getOrderId()));
        intent.putExtra("orderType", "bmall");
        context.startActivity(intent);
    }

    public void confirmdeliver(int position){
        ApiManager.Companion.getInstance().getApiMallTest().confirmBMallOrder(String.valueOf(data.get(position).getOrderId())).enqueue(new Callback<BaseHttpBean<Integer>>() {
            @Override
            public void onResponse(Call<BaseHttpBean<Integer>> call, Response<BaseHttpBean<Integer>> response) {
                if (response.body() == null){
                    return;
                }
                if (response.body().getCode() == 200){
                    DialogUtil.toast(context,"确认收货成功");
                    BMallOrderAdapter.this.data.get(position).setOrderStatus(50);
                    notifyItemChanged(position);
                }
            }

            @Override
            public void onFailure(Call<BaseHttpBean<Integer>> call, Throwable t) {

            }
        });
    }

    public void buyagain(int position){
        Intent intent = new Intent(context, BMallProductDetailActivity.class);
        intent.putExtra("productId",data.get(position).getProductId());
        context.startActivity(intent);
    }

    public void seeexpress(int position){
        Intent intent = new Intent(context, ExpressDetailActivity.class);
        intent.putExtra("orderId",data.get(position).getOrderId());
        intent.putExtra("type","bmall");
        context.startActivity(intent);
    }

    public void cancelorder(int position){
        MessageDialog.show((AppCompatActivity) context, "", "确认取消订单？", "取消订单", "再想想")
                .setOkButton(new OnDialogButtonClickListener() {
                    @Override
                    public boolean onClick(BaseDialog baseDialog, View v) {
                        ApiManager.Companion.getInstance().getApiMallTest().cancelBMallOrder(String.valueOf(data.get(position).getOrderId())).enqueue(new Callback<BaseHttpBean<Integer>>() {
                            @Override
                            public void onResponse(Call<BaseHttpBean<Integer>> call, Response<BaseHttpBean<Integer>> response) {
                                if (response.body() == null){
                                    return;
                                }
                                if (response.body().getCode() == 200){
                                    DialogUtil.toast(context,"取消订单成功");
                                    BMallOrderAdapter.this.data.get(position).setOrderStatus(60);
                                    notifyItemChanged(position);
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

    public void seedetail(int position){
        Intent intent = new Intent(context, MyBuyOrderDetailActivity.class);
        intent.putExtra("id",data.get(position).getOrderId());
        context.startActivity(intent);
    }



    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class BuyViewHolder extends RecyclerView.ViewHolder{

        TextView store_name;

        TextView status;

        ImageView image_product;

        TextView product_name;

        TextView product_price;

        TextView more;

        TextView cancelorder;

        TextView seeexpress;

        TextView seedetail;

        TextView buyagain;

        TextView confirmdeliver;

        TextView gopay;

        View root;

        public BuyViewHolder(@NonNull View itemView) {
            super(itemView);
            root = itemView.findViewById(R.id.root);
            store_name = itemView.findViewById(R.id.store_name);
            status = itemView.findViewById(R.id.status);
            image_product = itemView.findViewById(R.id.image_product);
            product_name = itemView.findViewById(R.id.product_name);
            product_price = itemView.findViewById(R.id.product_price);
            more = itemView.findViewById(R.id.more);
            cancelorder = itemView.findViewById(R.id.cancelorder);
            seeexpress = itemView.findViewById(R.id.seeexpress);
            seedetail = itemView.findViewById(R.id.seedetail);
            buyagain = itemView.findViewById(R.id.buyagain);
            confirmdeliver = itemView.findViewById(R.id.confirmdeliver);
            gopay = itemView.findViewById(R.id.gopay);
        }
    }

}
