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
import com.qunshang.wenpaitong.equnshang.activity.BMallProductDetailActivity;
import com.qunshang.wenpaitong.equnshang.activity.ProductDetailActivity;
import com.qunshang.wenpaitong.equnshang.data.StoreDataBean;
import com.qunshang.wenpaitong.equnshang.data.UserInfoViewModel;

import java.util.List;

public class ManufactureProductAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public List<StoreDataBean.DataBean.Product> data;

    public Context context;

    public LayoutInflater inflater;

    private String type;

    int TYPE_VIP = 3;

    int TYPE_AMALL = 9;

    int TYPE_BMALL = 5;

    public ManufactureProductAdapter(Context context,List<StoreDataBean.DataBean.Product> data,String type){
        this.context = context;
        this.data = data;
        inflater = LayoutInflater.from(context);
        this.type = type;
    }

    @Override
    public int getItemViewType(int position) {
        int productId = data.get(position).getProductId();
        if (productId == 1 | productId == 2){
            return TYPE_VIP;
        }
        if (type.equals("amall")){
            return TYPE_AMALL;
        }
        return TYPE_BMALL;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_VIP){
            return new VIPViewHolder(inflater.inflate(R.layout.item_amall_vip,parent,false));
        } else if (viewType == TYPE_AMALL){
            return new ProductViewHolder(inflater.inflate(R.layout.item_amall_products,parent,false));
        }
        return new BMallProductViewHolder(inflater.inflate(R.layout.item_bmall_store_product,parent,false));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        StoreDataBean.DataBean.Product bean = data.get(position);

        if (holder instanceof ProductViewHolder){
            Glide.with(context).load(bean.getProductPosterUrl()).into(((ProductViewHolder) holder).image_product);
            ((ProductViewHolder) holder).name_product.setText(bean.getProductName());
            ((ProductViewHolder) holder).price_product.setText("￥" + String.valueOf(bean.getPrice()));
            ((ProductViewHolder) holder).group_price.setText("￥" + String.valueOf(bean.getVipGroupPrice()));
            ((ProductViewHolder) holder).current_count_group.setText("已拼团" + bean.getCurrent() + "件");
            ((ProductViewHolder) holder).total_count_group.setText("共" + bean.getTotal() + "件");
            ((ProductViewHolder) holder).group_progress.setProgress(bean.getCurrent());
            ((ProductViewHolder) holder).group_progress.setMax(bean.getTotal());
            ((ProductViewHolder) holder).groupfanprice.setText("￥" + String.valueOf(bean.getPrice()));
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
                ((ProductViewHolder) holder).price_product.setText("￥" + String.valueOf(bean.getVipPrice()));
            } else {
                ((ProductViewHolder) holder).line.setVisibility(View.GONE);
                ((ProductViewHolder) holder).label_fansprice.setVisibility(View.GONE);
                ((ProductViewHolder) holder).groupfanprice.setVisibility(View.GONE);
            }
            if (UserInfoViewModel.INSTANCE.getUserInfo().getIs_vip() < 2 & UserInfoViewModel.INSTANCE.getUserInfo().getIs_partner() < 2){
                ((ProductViewHolder) holder).line.setVisibility(View.GONE);
                ((ProductViewHolder) holder).label_fansprice.setVisibility(View.GONE);
                ((ProductViewHolder) holder).groupfanprice.setVisibility(View.GONE);
            }
        }

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
        }

        if (holder instanceof BMallProductViewHolder){
            Glide.with(context).load(bean.getProductPosterUrl()).into(((BMallProductViewHolder) holder).image);
            ((BMallProductViewHolder) holder).name.setText(bean.getProductName());
            ((BMallProductViewHolder) holder).price.setText("￥" + bean.getPurchasePrice() + "起");
            ((BMallProductViewHolder) holder).layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, BMallProductDetailActivity.class);
                    intent.putExtra("productId",bean.getProductId());
                    context.startActivity(intent);
                }
            });
        }

    }

    public static class BMallProductViewHolder extends RecyclerView.ViewHolder{

        ImageView image;

        TextView name;

        TextView price;

        View layout;

        public BMallProductViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image_product);
            name = itemView.findViewById(R.id.name_product);
            price = itemView.findViewById(R.id.price);
            layout = itemView.findViewById(R.id.amall_layout);
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

        TextView groupfanprice;

        View line;

        TextView label_fansprice;

        TextView label_text;

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
            line = itemView.findViewById(R.id.line_fansprice);
            label_fansprice = itemView.findViewById(R.id.label_fanprice);
            label_text = itemView.findViewById(R.id.label_text);
        }
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

}
