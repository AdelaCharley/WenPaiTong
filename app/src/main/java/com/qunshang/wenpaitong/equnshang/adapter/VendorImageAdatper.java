package com.qunshang.wenpaitong.equnshang.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import com.qunshang.wenpaitong.R;
import com.qunshang.wenpaitong.equnshang.activity.ViewImagesActivity;
import com.qunshang.wenpaitong.equnshang.data.VendorDetailBean;
import com.qunshang.wenpaitong.equnshang.data.VendorPictureBean;

public class VendorImageAdatper extends RecyclerView.Adapter<VendorImageAdatper.ViewHolder> {

    public ArrayList<VendorPictureBean.DataBean> data;

    public Context context;

    public LayoutInflater inflater;

    public VendorImageAdatper(Context context, ArrayList<VendorPictureBean.DataBean> data) {
        this.context = context;
        this.data = data;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.item_vendor_image,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(context).load(data.get(position).getPictureUrl()).into(holder.image);
        holder.time.setText(data.get(position).getCreateTime());
        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (images.size() == 0){
                    for (int i = 0;i < data.size();i++){
                        VendorDetailBean.DataBean.Image image = new VendorDetailBean.DataBean.Image();
                        image.setPictureUrl(data.get(i).getPictureUrl());
                        images.add(image);
                    }
                }
                Intent intent = new Intent(context, ViewImagesActivity.class);
                intent.putExtra("images", images);
                intent.putExtra("index", position);
                context.startActivity(intent);
            }
        });
        holder.title.setText(data.get(position).getTitle());
    }

    ArrayList<VendorDetailBean.DataBean.Image> images = new ArrayList<>();


    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView image;

        TextView time;

        TextView title;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            time = itemView.findViewById(R.id.time);
            title = itemView.findViewById(R.id.title);
        }
    }

}
