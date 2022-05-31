package com.qunshang.wenpaitong.equnshang.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.qunshang.wenpaitong.R;
import com.qunshang.wenpaitong.equnshang.activity.ProductDetailActivity;
import com.qunshang.wenpaitong.equnshang.data.AMallProductBeanV2;
import com.qunshang.wenpaitong.equnshang.data.UserInfoViewModel;

import java.util.List;

public class AMallAdapterV2 extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public List<AMallProductBeanV2.DataBean> data;

    public Context context;

    public LayoutInflater inflater;

    public static final int TYPE_VIP = 0;

    public static final int TYPE_PRODUCT = 1;

    public static final int TYPE_CONTROL = 2;

    public static final int TYPE_DISACCOUNT = 3;

    public AMallAdapterV2(Context context,List<AMallProductBeanV2.DataBean> data){
        this.context = context;
        this.data = data;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_VIP){
            return new VIPViewHolder(inflater.inflate(R.layout.item_amall_vip,parent,false));
        } else if (viewType == TYPE_CONTROL){
            return new ControlPriceViewHolder(inflater.inflate(R.layout.item_control_price,parent,false));
        } else if (viewType == TYPE_DISACCOUNT){
            return new DisAccountViewHolder(inflater.inflate(R.layout.item_midautumn,parent,false));
        }

        else {
            return new ProductViewHolder(inflater.inflate(R.layout.item_amall_products,parent,false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        AMallProductBeanV2.DataBean bean = data.get(position);
        if (holder instanceof VIPViewHolder){
            Glide.with(context).load(bean.getProductPosterUrl()).into(((VIPViewHolder) holder).image_product);
            ((VIPViewHolder) holder).name_product.setText(bean.getProductName());
            ((VIPViewHolder) holder).price_product.setText("￥" + String.valueOf(bean.getPrice()));
            ((VIPViewHolder) holder).amall_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ProductDetailActivity.class);
                    intent.putExtra("productId",bean.getProductId());
                    context.startActivity(intent);
                }
            });
        } else if (holder instanceof ProductViewHolder) {
            Glide.with(context).load(bean.getProductPosterUrl()).into(((ProductViewHolder) holder).image_product);
            ((ProductViewHolder) holder).name_product.setText(bean.getProductName());
            ((ProductViewHolder) holder).price_product.setText("￥" + bean.getPrice());
            //((ProductViewHolder) holder).price_product.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
            ((ProductViewHolder) holder).group_price.setText("￥" + bean.getVipGroupPrice());
            ((ProductViewHolder) holder).current_count_group.setText("已拼团" + bean.getCurrent() + "件");
            ((ProductViewHolder) holder).total_count_group.setText("共" + bean.getTotal() + "件");
            ((ProductViewHolder) holder).group_progress.setProgress(bean.getCurrent());
            ((ProductViewHolder) holder).group_progress.setMax(bean.getTotal());
            ((ProductViewHolder) holder).groupfanprice.setText("￥" + bean.getPrice());
            ((ProductViewHolder) holder).amall_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ProductDetailActivity.class);
                    intent.putExtra("productId",bean.getProductId());
                    context.startActivity(intent);
                }
            });
            if (UserInfoViewModel.INSTANCE.getUserInfo().getIs_vip() >= 2){
                ((ProductViewHolder) holder).label_text.setText("会员零售价");
                ((ProductViewHolder) holder).price_product.setText("￥" + bean.getVipPrice());
            }

            if (UserInfoViewModel.INSTANCE.getUserInfo().getIs_vip() < 2
                    //& UserInfoViewModel.INSTANCE.getUserInfo().getIs_partner() < 2
            ){
                ((ProductViewHolder) holder).line.setVisibility(View.GONE);
                ((ProductViewHolder) holder).label_fansprice.setVisibility(View.GONE);
                ((ProductViewHolder) holder).groupfanprice.setVisibility(View.GONE);
            }

        } else if (holder instanceof ControlPriceViewHolder){
            Glide.with(context).load(bean.getProductPosterUrl()).into(((ControlPriceViewHolder) holder).image_product);
            ((ControlPriceViewHolder) holder).name_product.setText(bean.getProductName());
            ((ControlPriceViewHolder) holder).price_product.setText("￥" + bean.getPrice());
            ((ControlPriceViewHolder) holder).price_minPrice.setText("预估到手最低价￥" + (bean.getVipGroupPrice() ));
            ((ControlPriceViewHolder) holder).monthCount.setText("月销量 " + bean.getSale() + "件");
            ((ControlPriceViewHolder) holder).amall_layout.setOnClickListener(v -> {
                Intent intent = new Intent(context, ProductDetailActivity.class);
                intent.putExtra("productId",bean.getProductId());
                context.startActivity(intent);
            });
        } else if (holder instanceof DisAccountViewHolder) {
            Glide.with(context).load(bean.getProductPosterUrl()).into(((DisAccountViewHolder) holder).image_product);
            ((DisAccountViewHolder) holder).name_product.setText(bean.getProductName());
            ((DisAccountViewHolder) holder).price_product.setText("￥" + bean.getPrice());
            ((DisAccountViewHolder) holder).group_price.setText("￥" + bean.getVipGroupPrice());
            ((DisAccountViewHolder) holder).current_count_group.setText("已拼团" + bean.getCurrent() + "件");
            ((DisAccountViewHolder) holder).total_count_group.setText("共" + bean.getTotal() + "件");
            ((DisAccountViewHolder) holder).group_progress.setProgress(bean.getCurrent());
            ((DisAccountViewHolder) holder).amall_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ProductDetailActivity.class);
                    intent.putExtra("productId", bean.getProductId());
                    context.startActivity(intent);
                }
            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        int id = data.get(position).getProductId();
        if (id <= 2){
            return TYPE_VIP;
        } else if (data.get(position).getIsControlPrice() == 1){
            return TYPE_CONTROL;
        } else if (data.get(position).getDiscount() != null){
            return TYPE_DISACCOUNT;
        } else {
            return TYPE_PRODUCT;
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class VIPViewHolder extends RecyclerView.ViewHolder{

        ImageView image_product;

        TextView name_product;

        TextView price_product;

        ViewGroup amall_layout;

        public VIPViewHolder(@NonNull View itemView) {
            super(itemView);
            image_product = itemView.findViewById(R.id.image_product);
            name_product = itemView.findViewById(R.id.name_product);
            price_product = itemView.findViewById(R.id.price_product);
            amall_layout = itemView.findViewById(R.id.amall_layout);
        }
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder{

        ImageView image_product;

        TextView name_product;

        TextView price_product;

        TextView group_price;

        ProgressBar group_progress;

        TextView current_count_group;

        TextView total_count_group;

        ViewGroup amall_layout;

        TextView label_text;

        TextView label_fansprice;

        TextView groupfanprice;

        View line;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            image_product = itemView.findViewById(R.id.image_product);
            name_product = itemView.findViewById(R.id.name_product);
            price_product = itemView.findViewById(R.id.price_product);
            group_price = itemView.findViewById(R.id.group_price);
            group_progress = itemView.findViewById(R.id.group_progress);
            current_count_group = itemView.findViewById(R.id.current_count_group);
            total_count_group = itemView.findViewById(R.id.total_count_group);
            amall_layout = itemView.findViewById(R.id.amall_layout);
            groupfanprice = itemView.findViewById(R.id.fangroupprice);
            label_text = itemView.findViewById(R.id.label_text);
            label_fansprice = itemView.findViewById(R.id.label_fanprice);
            line = itemView.findViewById(R.id.line_fansprice);
        }
    }

    public static class ControlPriceViewHolder extends RecyclerView.ViewHolder{

        ImageView image_product;

        TextView name_product;

        TextView price_product;

        TextView price_minPrice;

        TextView monthCount;

        ViewGroup amall_layout;

        public ControlPriceViewHolder(@NonNull View itemView) {
            super(itemView);
            image_product = itemView.findViewById(R.id.image_product);
            name_product = itemView.findViewById(R.id.name_product);
            price_product = itemView.findViewById(R.id.price_product);
            price_minPrice = itemView.findViewById(R.id.preprice);
            monthCount = itemView.findViewById(R.id.monthcount);
            amall_layout = itemView.findViewById(R.id.amall_layout);
        }
    }

    public static class DisAccountViewHolder extends RecyclerView.ViewHolder{

        ImageView image_product;

        TextView name_product;

        TextView price_product;

        TextView group_price;

        ProgressBar group_progress;

        TextView current_count_group;

        TextView total_count_group;

        ViewGroup amall_layout;

        public DisAccountViewHolder(@NonNull View itemView) {
            super(itemView);
            image_product = itemView.findViewById(R.id.image_product);
            name_product = itemView.findViewById(R.id.name_product);
            price_product = itemView.findViewById(R.id.price_product);
            group_price = itemView.findViewById(R.id.group_price);
            group_progress = itemView.findViewById(R.id.group_progress);
            current_count_group = itemView.findViewById(R.id.current_count_group);
            total_count_group = itemView.findViewById(R.id.total_count_group);
            amall_layout = itemView.findViewById(R.id.amall_layout);
        }
    }

}
