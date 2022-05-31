package com.qunshang.wenpaitong.equnshang.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.qunshang.wenpaitong.R;
import com.qunshang.wenpaitong.equnshang.utils.SubSamplImageUtil;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;

import java.util.List;

public class ProductImageDetailAdapter extends RecyclerView.Adapter<ProductImageDetailAdapter.ViewHolder> {

    public List<String> data;

    public Context context;

    public LayoutInflater inflater;

    boolean isShow = false;

    public ProductImageDetailAdapter(Context context,List<String> data){
        this.context = context;
        this.data = data;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.item_product_detail,parent,false));
    }

    public void refreshWithShow(boolean isShow){
        this.isShow = isShow;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (isShow){
            SubSamplImageUtil.Companion.loadLargeImage(context, data.get(position), holder.imageView);
        }
        if (position == 0){
            holder.text.setVisibility(View.VISIBLE);
        } else {
            holder.text.setVisibility(View.GONE);
        }
        //holder.setIsRecyclable(false);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        TextView text;

        SubsamplingScaleImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            text = itemView.findViewById(R.id.text);
            imageView = itemView.findViewById(R.id.product_detail_image);
        }
    }

}
