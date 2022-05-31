package com.qunshang.wenpaitong.equnshang.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
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
import com.qunshang.wenpaitong.equnshang.activity.AddressActivity;
import com.qunshang.wenpaitong.equnshang.activity.ExpressDetailActivity;
import com.qunshang.wenpaitong.equnshang.activity.GoPayActivity;
import com.qunshang.wenpaitong.equnshang.activity.OrderActivity;
import com.qunshang.wenpaitong.equnshang.activity.OrderDetailActivity;
import com.qunshang.wenpaitong.equnshang.activity.ProductDetailActivity;
import com.qunshang.wenpaitong.equnshang.data.BaseHttpBean;
import com.qunshang.wenpaitong.equnshang.data.OrderBean;
import com.qunshang.wenpaitong.equnshang.model.ApiManager;
import com.qunshang.wenpaitong.equnshang.utils.DialogUtil;

public class OrderAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public List<OrderBean.DataBean> data;

    public Context context;

    public LayoutInflater inflater;

    public static final int TYPE_WAITFORPAY = 10;

    public static final int TYPE_WAITFORGROUP = 20;//暂时写成已关闭的，之后我再给改回来。

    public static final int TYPE_WAITFORDELIVER = 30;

    public static final int TYPE_WAITFORGET = 40;

    public static final int TYPE_ALEADYOVER = 50;

    public static final int TYPE_ALEADYCLOSE = 60;

    public OrderAdapter(Context context,List<OrderBean.DataBean> data){
        this.context = context;
        this.data = data;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder = null;
        switch (viewType){
            case TYPE_WAITFORPAY:
                holder = new WaitForPayViewHolder(inflater.inflate(R.layout.item_wait_for_pay,parent,false));
                break;
            case TYPE_WAITFORGROUP:
                holder = new WaitForGroupViewHolder(inflater.inflate(R.layout.item_wait_for_group,parent,false));
                break;
            case TYPE_WAITFORDELIVER:
                holder = new WaitForDeliverViewHolder(inflater.inflate(R.layout.item_wait_for_deliver,parent,false));
                break;
            case TYPE_WAITFORGET:
                holder = new WaitForGetViewHolder(inflater.inflate(R.layout.item_wait_for_get,parent,false));
                break;
            case TYPE_ALEADYOVER:
                holder = new WaitForAleadyOverViewHolder(inflater.inflate(R.layout.item_aleady_over,parent,false));
                break;
            case TYPE_ALEADYCLOSE:
                holder = new AleadyCloseViewHolder(inflater.inflate(R.layout.item_aleady_close,parent,false));
                break;
            default:
                holder = new WaitForAleadyOverViewHolder(inflater.inflate(R.layout.item_aleady_over,parent,false));
                break;
        }
        return holder;
    }

    public void goToDetailActivity(int position){
        Intent intent = new Intent(context, OrderDetailActivity.class);
        intent.putExtra("id",data.get(position).getOrderId());
        context.startActivity(intent);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        OrderBean.DataBean bean = data.get(position);
        if (holder instanceof WaitForPayViewHolder){
            Glide.with(context).load(bean.getProductPosterUrl()).into(((WaitForPayViewHolder) holder).image_product);
            ((WaitForPayViewHolder) holder).product_name.setText(bean.getProductName());
            ((WaitForPayViewHolder) holder).product_count.setText("x" + String.valueOf(bean.getProductSkuNumber()));
            ((WaitForPayViewHolder) holder).product_price.setText("￥" + String.valueOf(bean.getPrice()));
            ((WaitForPayViewHolder) holder).layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    goToDetailActivity(position);
                }
            });
            ((WaitForPayViewHolder) holder).cancelorder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ApiManager.Companion.getInstance().getApiMallTest().cancelOrder(String.valueOf(bean.getOrderId())).enqueue(new Callback<BaseHttpBean<Integer>>() {
                        @Override
                        public void onResponse(Call<BaseHttpBean<Integer>> call, Response<BaseHttpBean<Integer>> response) {
                            if (response.body() != null){
                                if (response.body().getCode() == 200){
                                    Toast.makeText(context, "取消订单成功", Toast.LENGTH_SHORT).show();
                                    OrderAdapter.this.data.remove(position);
                                    notifyItemRemoved(position);
                                } else {
                                    Toast.makeText(context, "取消失败" + response.body().getMsg(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<BaseHttpBean<Integer>> call, Throwable t) {

                        }
                    });
                }
            });
            ((WaitForPayViewHolder) holder).seedetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    goToDetailActivity(position);
                }
            });
            ((WaitForPayViewHolder) holder).gotopay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, GoPayActivity.class);
                    intent.putExtra("price",String.valueOf(bean.getPrice()));
                    intent.putExtra("orderId",String.valueOf(bean.getOrderId()));
                    intent.putExtra("orderType","amall");
                    context.startActivity(intent);
                }
            });
            ((WaitForPayViewHolder) holder).more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BottomMenu.show((AppCompatActivity) context, new String[]{"修改地址"}, (text, index) -> {
                        if (index == 0){
                            OrderActivity.Companion.setOrderId(data.get(position).getOrderId());
                            Intent intent = new Intent(context, AddressActivity.class);
                            ((AppCompatActivity) context).startActivityForResult(intent, OrderActivity.Companion.getTYPE_CHOOSE_ADDRESS());
                        }
                    });
                }
            });
            ((WaitForPayViewHolder) holder).store_name.setText(bean.getManufactureName());
        } else if (holder instanceof WaitForDeliverViewHolder){
            ((WaitForDeliverViewHolder) holder).status.setText("等待商家发货");
            Glide.with(context).load(bean.getProductPosterUrl()).into(((WaitForDeliverViewHolder) holder).image_product);
            ((WaitForDeliverViewHolder) holder).product_name.setText(bean.getProductName());
            ((WaitForDeliverViewHolder) holder).product_count.setText("x" + String.valueOf(bean.getProductSkuNumber()));
            ((WaitForDeliverViewHolder) holder).product_price.setText("￥" + String.valueOf(bean.getPrice()));
            ((WaitForDeliverViewHolder) holder).layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    goToDetailActivity(position);
                }
            });
            ((WaitForDeliverViewHolder) holder).seedetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    goToDetailActivity(position);
                }
            });
            ((WaitForDeliverViewHolder) holder).store_name.setText(bean.getManufactureName());
            ((WaitForDeliverViewHolder) holder).get_work_point.setText(bean.getCredit());
        } else if (holder instanceof WaitForGetViewHolder){
            Glide.with(context).load(bean.getProductPosterUrl()).into(((WaitForGetViewHolder) holder).image_product);
            ((WaitForGetViewHolder) holder).product_name.setText(bean.getProductName());
            ((WaitForGetViewHolder) holder).product_count.setText("x" + String.valueOf(bean.getProductSkuNumber()));
            ((WaitForGetViewHolder) holder).product_price.setText("￥" + String.valueOf(bean.getPrice()));
            ((WaitForGetViewHolder) holder).layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    goToDetailActivity(position);
                }
            });
            ((WaitForGetViewHolder) holder).store_name.setText(bean.getManufactureName());
            ((WaitForGetViewHolder) holder).get_work_point.setText(bean.getCredit());
            ((WaitForGetViewHolder) holder).confirmorder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MessageDialog.show((AppCompatActivity) context, "", "确定已收到货？", "确认", "取消").setOkButton(new OnDialogButtonClickListener() {
                                @Override
                                public boolean onClick(BaseDialog baseDialog, View v) {
                                    ApiManager.Companion.getInstance().getApiMallTest().confirmReceipt(String.valueOf(bean.getOrderId())).enqueue(new Callback<BaseHttpBean<Integer>>() {
                                        @Override
                                        public void onResponse(Call<BaseHttpBean<Integer>> call, Response<BaseHttpBean<Integer>> response) {
                                            baseDialog.doDismiss();
                                            if (response.body() != null){
                                                if (response.body().getCode() == 200){
                                                    Toast.makeText(context, "确认收货成功", Toast.LENGTH_SHORT).show();
                                                    data.remove(position);
                                                    notifyItemRemoved(position);
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
            });
            ((WaitForGetViewHolder) holder).more.setVisibility(View.GONE);
            ((WaitForGetViewHolder) holder).seelogistics.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ExpressDetailActivity.class);
                    intent.putExtra("orderId",bean.getOrderId());
                    intent.putExtra("type","amall");
                    context.startActivity(intent);
                }
            });
            ((WaitForGetViewHolder) holder).status.setText("商家已发货");
            ((WaitForGetViewHolder) holder).desc.setText("已付款，确认收货后15天内入账");
        } else if (holder instanceof WaitForAleadyOverViewHolder){
            /*Glide.with(context).load(bean.getProductPosterUrl()).into(((WaitForAleadyOverViewHolder) holder).image_product);
            ((WaitForAleadyOverViewHolder) holder).product_name.setText(bean.getProductName());
            ((WaitForAleadyOverViewHolder) holder).product_count.setText(String.valueOf(bean.getProductSkuNumber()));
            ((WaitForAleadyOverViewHolder) holder).product_price.setText(String.valueOf(bean.getPrice()));*/
            Glide.with(context).load(bean.getProductPosterUrl()).into(((WaitForAleadyOverViewHolder) holder).image_product);
            ((WaitForAleadyOverViewHolder) holder).product_name.setText(bean.getProductName());
            ((WaitForAleadyOverViewHolder) holder).product_count.setText("x" + String.valueOf(bean.getProductSkuNumber()));
            ((WaitForAleadyOverViewHolder) holder).product_price.setText("￥" + String.valueOf(bean.getPrice()));
            ((WaitForAleadyOverViewHolder) holder).layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    goToDetailActivity(position);
                }
            });

            ((WaitForAleadyOverViewHolder) holder).status.setText("交易成功");
            ((WaitForAleadyOverViewHolder) holder).store_name.setText(bean.getManufactureName());
            ((WaitForAleadyOverViewHolder) holder).get_work_point.setText(bean.getCredit());
            ((WaitForAleadyOverViewHolder) holder).buyagain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ProductDetailActivity.class);
                    intent.putExtra("productId",bean.getProductId());
                    context.startActivity(intent);
                }
            });
            ((WaitForAleadyOverViewHolder) holder).desc.setText("已确认收货");
            if (bean.getProductId() == 1 | bean.getProductId() == 2){
                ((WaitForAleadyOverViewHolder) holder).buyagain.setVisibility(View.GONE);
                ((WaitForAleadyOverViewHolder) holder).seeexpress.setText("查看详情");
                ((WaitForAleadyOverViewHolder) holder).seeexpress.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        goToDetailActivity(position);
                    }
                });
            } else {
                ((WaitForAleadyOverViewHolder) holder).buyagain.setVisibility(View.VISIBLE);
                ((WaitForAleadyOverViewHolder) holder).seeexpress.setText("查看物流");
                ((WaitForAleadyOverViewHolder) holder).seeexpress.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, ExpressDetailActivity.class);
                        intent.putExtra("orderId",bean.getOrderId());
                        intent.putExtra("type","amall");
                        context.startActivity(intent);
                    }
                });
            }
            ((WaitForAleadyOverViewHolder) holder).more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BottomMenu.show((AppCompatActivity) context, new String[]{"删除订单"}, (text, index) -> {
                        if (index == 0){
                            MessageDialog.show((AppCompatActivity) context, "", "确认删除？", "删除订单", "再想想")
                                    .setOkButton(new OnDialogButtonClickListener() {
                                        @Override
                                        public boolean onClick(BaseDialog baseDialog, View v) {
                                            ApiManager.Companion.getInstance().getApiMallTest().deleteAMallOrder(String.valueOf(data.get(position).getOrderId())).enqueue(new Callback<BaseHttpBean<Integer>>() {
                                                @Override
                                                public void onResponse(Call<BaseHttpBean<Integer>> call, Response<BaseHttpBean<Integer>> response) {
                                                    if (response.body() == null){
                                                        return;
                                                    }
                                                    if (response.body().getCode() == 200){
                                                        //ToastUtil.toast(context,"删除订单");
                                                        OrderAdapter.this.data.remove(position);
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
            });
        } else if (holder instanceof WaitForGroupViewHolder){
            Glide.with(context).load(bean.getProductPosterUrl()).into(((WaitForGroupViewHolder) holder).image_product);
            ((WaitForGroupViewHolder) holder).status.setText("等待成团中");
            ((WaitForGroupViewHolder) holder).product_name.setText(bean.getProductName());
            ((WaitForGroupViewHolder) holder).product_count.setText("x" + String.valueOf(bean.getProductSkuNumber()));
            ((WaitForGroupViewHolder) holder).product_price.setText("￥" + String.valueOf(bean.getPrice()));
            ((WaitForGroupViewHolder) holder).layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    goToDetailActivity(position);
                }
            });

            ((WaitForGroupViewHolder) holder).seedetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    goToDetailActivity(position);
                }
            });
            ((WaitForGroupViewHolder) holder).modifyaddress.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    OrderActivity.Companion.setOrderId(data.get(position).getOrderId());
                    Intent intent = new Intent(context, AddressActivity.class);
                    ((AppCompatActivity) context).startActivityForResult(intent, OrderActivity.Companion.getTYPE_CHOOSE_ADDRESS());
                }
            });
            ((WaitForGroupViewHolder) holder).store_name.setText(bean.getManufactureName());
            ((WaitForGroupViewHolder) holder).get_work_point.setText(bean.getCredit());
            ((WaitForGroupViewHolder) holder).desc.setText("已付款，确认收货后15天内入账");
        } else if (holder instanceof AleadyCloseViewHolder){
            Glide.with(context).load(bean.getProductPosterUrl()).into(((AleadyCloseViewHolder) holder).image_product);
            ((AleadyCloseViewHolder) holder).product_name.setText(bean.getProductName());
            ((AleadyCloseViewHolder) holder).product_count.setText("x" + String.valueOf(bean.getProductSkuNumber()));
            ((AleadyCloseViewHolder) holder).product_price.setText("￥" + String.valueOf(bean.getPrice()));
            ((AleadyCloseViewHolder) holder).layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    goToDetailActivity(position);
                }
            });
            ((AleadyCloseViewHolder) holder).seedetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    goToDetailActivity(position);
                }
            });
            ((AleadyCloseViewHolder) holder).store_name.setText(bean.getManufactureName());
            ((AleadyCloseViewHolder) holder).get_work_point.setText(bean.getCredit());
            ((AleadyCloseViewHolder) holder).status.setText("订单已关闭");
            ((AleadyCloseViewHolder) holder).buyagain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ProductDetailActivity.class);
                    intent.putExtra("productId",bean.getProductId());
                    context.startActivity(intent);
                }
            });
            ((AleadyCloseViewHolder) holder).desc.setText("订单关闭，无法获得工分");
            ((AleadyCloseViewHolder) holder).more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BottomMenu.show((AppCompatActivity) context, new String[]{"删除订单"}, (text, index) -> {
                        if (index == 0){
                            MessageDialog.show((AppCompatActivity) context, "", "确认删除？", "删除订单", "再想想")
                                    .setOkButton(new OnDialogButtonClickListener() {
                                        @Override
                                        public boolean onClick(BaseDialog baseDialog, View v) {
                                            ApiManager.Companion.getInstance().getApiMallTest().deleteAMallOrder(String.valueOf(data.get(position).getOrderId())).enqueue(new Callback<BaseHttpBean<Integer>>() {
                                                @Override
                                                public void onResponse(Call<BaseHttpBean<Integer>> call, Response<BaseHttpBean<Integer>> response) {
                                                    if (response.body() == null){
                                                        return;
                                                    }
                                                    if (response.body().getCode() == 200){
                                                        DialogUtil.toast(context,"删除订单");
                                                        OrderAdapter.this.data.remove(position);
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
            });
        }
        Log.i("zhangjuniii","onBindView");
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

    public static class AleadyCloseViewHolder extends RecyclerView.ViewHolder{

        ImageView image_product;

        TextView product_name;

        TextView product_count;

        TextView product_price;

        //Button seeticket;

        TextView seedetail;

        ViewGroup layout;

        TextView get_work_point;

        TextView store_name;

        TextView status;

        TextView more;

        TextView desc;

        TextView buyagain;

        public AleadyCloseViewHolder(@NonNull View itemView) {
            super(itemView);
            image_product = itemView.findViewById(R.id.image_product);
            product_name = itemView.findViewById(R.id.product_name);
            product_count = itemView.findViewById(R.id.product_count);
            product_price = itemView.findViewById(R.id.product_price);
            //seeticket = itemView.findViewById(R.id.seeticket);
            seedetail = itemView.findViewById(R.id.seedetail);
            layout = itemView.findViewById(R.id.item_order_layout);
            get_work_point = itemView.findViewById(R.id.get_work_point);
            store_name = itemView.findViewById(R.id.store_name);
            status = itemView.findViewById(R.id.status);
            more = itemView.findViewById(R.id.more);
            desc = itemView.findViewById(R.id.desc);
            buyagain = itemView.findViewById(R.id.buyagain);
        }
    }

    public static class WaitForAleadyOverViewHolder extends RecyclerView.ViewHolder{

        ImageView image_product;

        TextView product_name;

        TextView product_count;

        TextView product_price;

        //Button seeticket;

        TextView seeexpress;

        ViewGroup layout;

        TextView get_work_point;

        TextView store_name;

        TextView status;

        TextView more;

        TextView desc;

        TextView buyagain;

        public WaitForAleadyOverViewHolder(@NonNull View itemView) {
            super(itemView);
            image_product = itemView.findViewById(R.id.image_product);
            product_name = itemView.findViewById(R.id.product_name);
            product_count = itemView.findViewById(R.id.product_count);
            product_price = itemView.findViewById(R.id.product_price);
            //seeticket = itemView.findViewById(R.id.seeticket);
            seeexpress = itemView.findViewById(R.id.seedetail);
            layout = itemView.findViewById(R.id.item_order_layout);
            get_work_point = itemView.findViewById(R.id.get_work_point);
            store_name = itemView.findViewById(R.id.store_name);
            status = itemView.findViewById(R.id.status);
            more = itemView.findViewById(R.id.label3);
            desc = itemView.findViewById(R.id.desc);
            buyagain = itemView.findViewById(R.id.buyagain);
        }
    }

    public static class WaitForGetViewHolder extends RecyclerView.ViewHolder{

        ImageView image_product;

        TextView product_name;

        TextView store_name;

        TextView product_count;

        TextView product_price;

        TextView seedetail;

        TextView seelogistics;

        TextView confirmorder;//确认收货

        ViewGroup layout;

        TextView get_work_point;

        TextView more;

        TextView status;

        TextView desc;

        public WaitForGetViewHolder(@NonNull View itemView) {
            super(itemView);
            store_name = itemView.findViewById(R.id.store_name);
            image_product = itemView.findViewById(R.id.image_product);
            product_name = itemView.findViewById(R.id.product_name);
            product_count = itemView.findViewById(R.id.product_count);
            product_price = itemView.findViewById(R.id.product_price);
            seedetail = itemView.findViewById(R.id.seedetail);
            seelogistics = itemView.findViewById(R.id.seelogstics);
            confirmorder = itemView.findViewById(R.id.confirmorder);
            layout = itemView.findViewById(R.id.item_order_layout);
            get_work_point = itemView.findViewById(R.id.get_work_point);
            more = itemView.findViewById(R.id.label3);
            status = itemView.findViewById(R.id.status);
            desc = itemView.findViewById(R.id.desc);
        }
    }

    public static class WaitForDeliverViewHolder extends RecyclerView.ViewHolder{

        ImageView image_product;

        TextView product_name;

        TextView product_count;

        TextView product_price;

        //Button seeticket;

        TextView seedetail;

        ViewGroup layout;

        TextView get_work_point;

        TextView store_name;

        TextView status;

        public WaitForDeliverViewHolder(@NonNull View itemView) {
            super(itemView);
            image_product = itemView.findViewById(R.id.image_product);
            product_name = itemView.findViewById(R.id.product_name);
            product_count = itemView.findViewById(R.id.product_count);
            product_price = itemView.findViewById(R.id.product_price);
            //seeticket = itemView.findViewById(R.id.seeticket);
            seedetail = itemView.findViewById(R.id.seedetail);
            layout = itemView.findViewById(R.id.item_order_layout);
            get_work_point = itemView.findViewById(R.id.get_work_point);
            store_name = itemView.findViewById(R.id.store_name);
            status = itemView.findViewById(R.id.status);
        }
    }

    public static class WaitForGroupViewHolder extends RecyclerView.ViewHolder{

        ImageView image_product;

        TextView product_name;

        TextView product_count;

        TextView product_price;

        //Button cancelorder;

        TextView seedetail;

        TextView modifyaddress;

        TextView get_work_point;

        TextView store_name;

        ViewGroup layout;

        TextView status;

        TextView desc;

        public WaitForGroupViewHolder(@NonNull View itemView) {
            super(itemView);
            image_product = itemView.findViewById(R.id.image_product);
            product_name = itemView.findViewById(R.id.product_name);
            product_count = itemView.findViewById(R.id.product_count);
            product_price = itemView.findViewById(R.id.product_price);
            seedetail = itemView.findViewById(R.id.seedetail);
            modifyaddress = itemView.findViewById(R.id.modifyaddress);
            layout = itemView.findViewById(R.id.item_order_layout);
            get_work_point = itemView.findViewById(R.id.get_work_point);
            store_name = itemView.findViewById(R.id.store_name);
            status = itemView.findViewById(R.id.status);
            desc = itemView.findViewById(R.id.desc);
        }
    }

    public static class WaitForPayViewHolder extends RecyclerView.ViewHolder{

        ImageView image_product;

        TextView product_name;

        TextView product_count;

        TextView product_price;

        TextView cancelorder;

        TextView seedetail;

        TextView gotopay;

        TextView store_name;

        ViewGroup layout;

        TextView more;

        public WaitForPayViewHolder(@NonNull View itemView) {
            super(itemView);
            image_product = itemView.findViewById(R.id.image_product);
            product_name = itemView.findViewById(R.id.product_name);
            product_count = itemView.findViewById(R.id.product_count);
            product_price = itemView.findViewById(R.id.product_price);
            cancelorder = itemView.findViewById(R.id.cancelorder);
            seedetail = itemView.findViewById(R.id.seedetail);
            gotopay = itemView.findViewById(R.id.gotopay);
            layout = itemView.findViewById(R.id.item_order_layout);
            store_name = itemView.findViewById(R.id.store_name);
            more = itemView.findViewById(R.id.label3);
        }
    }

}
