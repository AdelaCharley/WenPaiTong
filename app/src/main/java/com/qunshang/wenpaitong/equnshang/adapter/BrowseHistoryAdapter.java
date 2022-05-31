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
import com.qunshang.wenpaitong.equnshang.data.BrowserHistoryBean;
import com.qunshang.wenpaitong.equnshang.data.UserInfoViewModel;
import com.qunshang.wenpaitong.equnshang.utils.StringUtils;

import java.util.ArrayList;

public class BrowseHistoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public ArrayList<BrowserHistoryBean.DataBean.ProductList> data;

    public Context context;

    public LayoutInflater inflater;

    public static final int TYPE_VIP = 0;

    public static final int TYPE_PRODUCT = 1;

    public BrowseHistoryAdapter(Context context,ArrayList<BrowserHistoryBean.DataBean.ProductList> data){
        this.context = context;
        this.data = data;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getItemViewType(int position) {
        int id = data.get(position).getProductId();
        if (id == 1 | id == 2){
            return TYPE_VIP;
        }
        return TYPE_PRODUCT;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_VIP){
            return new VIPBrowseViewHolder(inflater.inflate(R.layout.item_browse_history_vip,parent,false));
        }
        return new BrowseViewHolder(inflater.inflate(R.layout.item_browse_history_product,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        BrowserHistoryBean.DataBean.ProductList bean = data.get(position);
        if (holder instanceof BrowseViewHolder){
            Glide.with(context).load(bean.getProductPosterUrl()).into(((BrowseViewHolder) holder).image_product);
            ((BrowseViewHolder) holder).name_product.setText(bean.getProductName());
            ((BrowseViewHolder) holder).price_product.setText("￥" + bean.getPrice());
            //((ProductViewHolder) holder).price_product.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
            ((BrowseViewHolder) holder).group_price.setText("￥" + bean.getVipGroupPrice());
            ((BrowseViewHolder) holder).current_count_group.setText("已拼团" + bean.getCurrent() + "件");
            ((BrowseViewHolder) holder).total_count_group.setText("共" + bean.getTotal() + "件");
            ((BrowseViewHolder) holder).group_progress.setProgress(bean.getCurrent());
            ((BrowseViewHolder) holder).group_progress.setMax(bean.getTotal());
            ((BrowseViewHolder) holder).groupfanprice.setText("￥" + bean.getPrice());
            if (UserInfoViewModel.INSTANCE.getUserInfo().getIs_vip() >= 2){
                ((BrowseViewHolder) holder).labeltext.setText("会员零售价");
                ((BrowseViewHolder) holder).price_product.setText("￥" + String.valueOf(bean.getVipPrice()));
            }
            ((BrowseViewHolder) holder).amall_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ProductDetailActivity.class);
                    intent.putExtra("productId",bean.getProductId());
                    context.startActivity(intent);
                }
            });
            if (!StringUtils.isEmpty(bean.getDate())){
                ((BrowseViewHolder) holder).date.setVisibility(View.VISIBLE);
                ((BrowseViewHolder) holder).line.setVisibility(View.VISIBLE);
                //((BrowseViewHolder) holder).top.setVisibility(View.VISIBLE);
                ((BrowseViewHolder) holder).date.setText(bean.getDate());
            } else {
                //((BrowseViewHolder) holder).top.setVisibility(View.GONE);
                ((BrowseViewHolder) holder).date.setVisibility(View.GONE);
                ((BrowseViewHolder) holder).line.setVisibility(View.GONE);
            }

            if (UserInfoViewModel.INSTANCE.getUserInfo().getIs_vip() < 2 & UserInfoViewModel.INSTANCE.getUserInfo().getIs_partner() < 2){
                ((BrowseViewHolder) holder).label_fans_line.setVisibility(View.GONE);
                ((BrowseViewHolder) holder).label_fansprice.setVisibility(View.GONE);
                ((BrowseViewHolder) holder).groupfanprice.setVisibility(View.GONE);
            }
        } else if (holder instanceof VIPBrowseViewHolder){
            Glide.with(context).load(bean.getProductPosterUrl()).into(((VIPBrowseViewHolder) holder).image_product);
            ((VIPBrowseViewHolder) holder).name_product.setText(bean.getProductName());
            ((VIPBrowseViewHolder) holder).price_product.setText("￥" + String.valueOf(bean.getPrice()));
            ((VIPBrowseViewHolder) holder).amall_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ProductDetailActivity.class);
                    intent.putExtra("productId",bean.getProductId());
                    context.startActivity(intent);
                }
            });
            if (!StringUtils.isEmpty(bean.getDate())){
                ((VIPBrowseViewHolder) holder).date.setVisibility(View.VISIBLE);
                ((VIPBrowseViewHolder) holder).line.setVisibility(View.VISIBLE);
                //((VIPBrowseViewHolder) holder).top.setVisibility(View.VISIBLE);
                ((VIPBrowseViewHolder) holder).date.setText(bean.getDate());
            } else {
                ((VIPBrowseViewHolder) holder).date.setVisibility(View.GONE);
                ((VIPBrowseViewHolder) holder).line.setVisibility(View.GONE);
                //((VIPBrowseViewHolder) holder).top.setVisibility(View.GONE);
            }
        }
        /*Glide.with(context).load(bean.getProductPosterUrl()).into(holder.image_product);
        holder.name_product.setText(bean.getProductName());
        holder.price_product.setText(String.valueOf(bean.getPrice()));
        holder.group_price.setText(String.valueOf(bean.getVipGroupPrice()));
        holder.current_count_group.setText("已拼团" + bean.getCurrent() + "件");
        holder.total_count_group.setText("共" + bean.getTotal() + "件");
        holder.group_progress.setProgress(bean.getCurrent());
        holder.amall_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ProductDetailActivity.class);
                intent.putExtra("productId",bean.getProductId());
                context.startActivity(intent);
            }
        });*/
        /*if (!StringUtils.isEmpty(bean.getDate())){
            holder.top.setVisibility(View.VISIBLE);
            holder.date.setText(bean.getDate());
        } else {
            holder.top.setVisibility(View.GONE);
        }*/
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
    
    public static class VIPBrowseViewHolder extends RecyclerView.ViewHolder{

        ImageView image_product;

        TextView name_product;

        TextView price_product;

        ViewGroup amall_layout;

        //ViewGroup top;

        TextView date;

        View line;
        
        public VIPBrowseViewHolder(@NonNull View itemView) {
            super(itemView);
            image_product = itemView.findViewById(R.id.image_product);
            name_product = itemView.findViewById(R.id.name_product);
            price_product = itemView.findViewById(R.id.price_product);
            amall_layout = itemView.findViewById(R.id.amall_layout);
            //top = itemView.findViewById(R.id.top);
            date = itemView.findViewById(R.id.date);
            line = itemView.findViewById(R.id.line);
        }
    }

    public static class BrowseViewHolder extends RecyclerView.ViewHolder{

        ImageView image_product;

        TextView name_product;

        TextView price_product;

        TextView group_price;

        ProgressBar group_progress;

        TextView current_count_group;

        TextView total_count_group;

        ViewGroup amall_layout;

        TextView groupfanprice;

        TextView labeltext;

        //ViewGroup top;

        TextView date;

        View line;

        View label_fans_line;

        TextView label_fansprice;

        public BrowseViewHolder(@NonNull View itemView) {
            super(itemView);
            image_product = itemView.findViewById(R.id.image_product);
            name_product = itemView.findViewById(R.id.name_product);
            price_product = itemView.findViewById(R.id.price_product);
            group_price = itemView.findViewById(R.id.group_price);
            group_progress = itemView.findViewById(R.id.group_progress);
            current_count_group = itemView.findViewById(R.id.current_count_group);
            total_count_group = itemView.findViewById(R.id.total_count_group);
            amall_layout = itemView.findViewById(R.id.amall_layout);
            //top = itemView.findViewById(R.id.top);
            date = itemView.findViewById(R.id.date);
            groupfanprice = itemView.findViewById(R.id.fangroupprice);
            labeltext = itemView.findViewById(R.id.label_text);
            line = itemView.findViewById(R.id.line);
            label_fansprice = itemView.findViewById(R.id.label_fanprice);
            label_fans_line = itemView.findViewById(R.id.line_fansprice);
        }
    }

}
