package com.qunshang.wenpaitong.equnshang.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.qunshang.wenpaitong.R;
import com.qunshang.wenpaitong.equnshang.activity.AMallV3ProductDetailActivity;
import com.qunshang.wenpaitong.equnshang.activity.ProductDetailActivity;
import com.qunshang.wenpaitong.equnshang.data.UserInfoViewModel;
import com.qunshang.wenpaitong.equnshang.data.UserProductCollectionBean;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

public class ProductCollectionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public List<UserProductCollectionBean.DataBean> data;

    public Context context;

    public LayoutInflater inflater;

    public HashSet<Integer> selectedForDelete;//这个地方有一个神奇的bug，每当我请求玩网络后，不管HashSet中之前有多少数据，请求之后HashSet都会被置位空.
    //但是当我把他转为非静态后没有用，一样会强行变成空.

    boolean isAllSelected = false;

    boolean isCheckBoxVisable = false;

    public ProductCollectionAdapter(Context context,List<UserProductCollectionBean.DataBean> data){
        this.context = context;
        this.data = data;
        inflater = LayoutInflater.from(context);
        if (!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this);
        }
        selectedForDelete = new HashSet<>();
    }

    public static final int TYPE_VIP = 0;

    public static final int TYPE_PRODUCT = 1;

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
            return new VIPProductViewHolder(inflater.inflate(R.layout.item_user_product_collection_vip,parent,false));
        }
        return new ProductViewHolder(inflater.inflate(R.layout.item_user_product_collection,parent,false));
    }


    public void updateAfterAllSelectedOrUnSeleted(){
        isAllSelected = !isAllSelected;
        notifyDataSetChanged();
    }

    public void updateAfterRemove(){
        Iterator<Integer> it = selectedForDelete.iterator();
        while (it.hasNext()) {
            Integer str = it.next();
            int id = str;

            for (int i = 0;i < data.size();i++){
                UserProductCollectionBean.DataBean bean = data.get(i);
                if (bean.getId() == id){
                    data.remove(i);
                    notifyItemRemoved(i);
                }
            }
        }
        selectedForDelete = new HashSet<>();
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        UserProductCollectionBean.DataBean bean = data.get(position);
        if (holder instanceof ProductViewHolder){
            Glide.with(context).load(bean.getProductPosterUrl()).into(((ProductViewHolder) holder).image_product);
            ((ProductViewHolder) holder).name_product.setText(bean.getProductName());
            ((ProductViewHolder) holder).price_product.setText("￥" + String.valueOf(bean.getPrice()));
            ((ProductViewHolder) holder).group_price.setText("￥" + String.valueOf(bean.getVipGroupPrice()));
            ((ProductViewHolder) holder).current_count_group.setText("已拼团" + bean.getCurrent() + "件");
            ((ProductViewHolder) holder).total_count_group.setText("共" + bean.getTotal() + "件");
            ((ProductViewHolder) holder).group_progress.setProgress(bean.getCurrent());
            ((ProductViewHolder) holder).user_product_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ProductDetailActivity.class);
                    intent.putExtra("productId",bean.getProductId());
                    context.startActivity(intent);
                }
            });
            if (UserInfoViewModel.INSTANCE.getUserInfo().getIs_vip() >= 2){
                ((ProductViewHolder) holder).labeltext.setText("会员零售价");
                ((ProductViewHolder) holder).price_product.setText("￥" + String.valueOf(bean.getVipPrice()));
            }
            ((ProductViewHolder) holder).groupfanprice.setText("￥" + bean.getPrice());
            if (isCheckBoxVisable){
                ((ProductViewHolder) holder).checkBox.setVisibility(View.VISIBLE);
                ((ProductViewHolder) holder).checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked){
                            selectedForDelete.add(bean.getId());
                        } else {
                            selectedForDelete.remove(bean.getId());
                        }
                    }
                });
            } else {
                ((ProductViewHolder) holder).checkBox.setVisibility(View.GONE);
            }
            if (isAllSelected){
                ((ProductViewHolder) holder).checkBox.setChecked(true);
            } else {
                ((ProductViewHolder) holder).checkBox.setChecked(false);
            }
        } else if (holder instanceof VIPProductViewHolder){
            Glide.with(context).load(bean.getProductPosterUrl()).into(((VIPProductViewHolder) holder).image_product);
           ((VIPProductViewHolder) holder).name_product.setText(bean.getProductName());
            ((VIPProductViewHolder) holder).price_product.setText("￥" + String.valueOf(bean.getPrice()));
            ((VIPProductViewHolder) holder).amall_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ProductDetailActivity.class);
                    intent.putExtra("productId",bean.getProductId());
                    context.startActivity(intent);
                }
            });
            if (isCheckBoxVisable){
                ((VIPProductViewHolder) holder).checkBox.setVisibility(View.VISIBLE);
                ((VIPProductViewHolder) holder).checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked){
                            selectedForDelete.add(bean.getId());
                        } else {
                            selectedForDelete.remove(bean.getId());
                        }
                    }
                });
            } else {
                ((VIPProductViewHolder) holder).checkBox.setVisibility(View.GONE);
            }
            if (isAllSelected){
                ((VIPProductViewHolder) holder).checkBox.setChecked(true);
            } else {
                ((VIPProductViewHolder) holder).checkBox.setChecked(false);
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void makeCheckBoxVisable(String string){
        //Toast.makeText(context, "我收到了", Toast.LENGTH_SHORT).show();
        if (string.equals("user_product_checkbox")){
            /*for (int i = 0;i < data.size();i++){
                data.get(i).setCheckBoxVisble(true);
            }*/
            isCheckBoxVisable = !isCheckBoxVisable;
            notifyDataSetChanged();
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class VIPProductViewHolder extends RecyclerView.ViewHolder{

        ImageView image_product;

        TextView name_product;

        TextView price_product;

        ViewGroup amall_layout;

        CheckBox checkBox;

        public VIPProductViewHolder(@NonNull View itemView) {
            super(itemView);
            image_product = itemView.findViewById(R.id.image_product);
            name_product = itemView.findViewById(R.id.name_product);
            price_product = itemView.findViewById(R.id.price_product);
            amall_layout = itemView.findViewById(R.id.layout);
            checkBox = itemView.findViewById(R.id.user_product_checkbox);
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

        ViewGroup user_product_layout;

        TextView groupfanprice;

        CheckBox checkBox;

        TextView labeltext;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            image_product = itemView.findViewById(R.id.image_product);
            name_product = itemView.findViewById(R.id.name_product);
            price_product = itemView.findViewById(R.id.price_product);
            group_price = itemView.findViewById(R.id.group_price);
            group_progress = itemView.findViewById(R.id.group_progress);
            current_count_group = itemView.findViewById(R.id.current_count_group);
            total_count_group = itemView.findViewById(R.id.total_count_group);
            user_product_layout = itemView.findViewById(R.id.amall_layout);
            checkBox = itemView.findViewById(R.id.user_product_checkbox);
            groupfanprice = itemView.findViewById(R.id.fangroupprice);
            labeltext = itemView.findViewById(R.id.label_text);
        }

    }

}
