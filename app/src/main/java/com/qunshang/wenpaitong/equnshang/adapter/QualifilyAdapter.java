package com.qunshang.wenpaitong.equnshang.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import com.qunshang.wenpaitong.R;
import com.qunshang.wenpaitong.equnshang.utils.StringUtils;

public class QualifilyAdapter extends RecyclerView.Adapter<QualifilyAdapter.ViewHolder> {

    public List<String> data;

    public Context context;

    public LayoutInflater inflater;

    public QualifilyAdapter(Context context,List<String> data){
        this.context = context;
        this.data = data;
        inflater = LayoutInflater.from(context);

    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.item_qualifily_images,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //holder.image.setText(String.valueOf(position));
        Glide.with(context).load(data.get(position)).into(holder.image);
    }

    @Override
    public int getItemCount() {
        StringUtils.log("yigongyou" + data.size());
        return data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        ImageView image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
        }
    }

}