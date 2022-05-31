package com.qunshang.wenpaitong.equnshang.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import com.qunshang.wenpaitong.R;
import com.qunshang.wenpaitong.equnshang.data.ProductBeanV2;

public class SkuDialogAdapterV4 extends RecyclerView.Adapter<SkuDialogAdapterV4.ViewHolder> {

    public List<ProductBeanV2.DataBean.SpecList> data;

    public Context context;

    public LayoutInflater inflater;

    int currentIndex = 0;

    public SkuDialogAdapterV4(Context context,List<ProductBeanV2.DataBean.SpecList> data){
        this.context = context;
        this.data = data;
        inflater = LayoutInflater.from(context);
        if (!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.item_sku_dialog_v4,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ProductBeanV2.DataBean.SpecList spec = data.get(position);
        holder.skuname.setText(spec.getName());
        holder.skus.setAdapter(new SpecDialogAdapterV4(context,spec.getList(),position,data.size()));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void def(Integer integer){

    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        TextView skuname;

        RecyclerView skus;

        public ViewHolder(@NonNull View view) {
            super(view);
            skuname = view.findViewById(R.id.skuname);
            skus = view.findViewById(R.id.skus);
        }
    }

}