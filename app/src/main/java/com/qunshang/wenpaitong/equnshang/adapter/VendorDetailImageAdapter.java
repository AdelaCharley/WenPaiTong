package com.qunshang.wenpaitong.equnshang.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import com.qunshang.wenpaitong.R;
import com.qunshang.wenpaitong.equnshang.activity.ViewImagesActivity;
import com.qunshang.wenpaitong.equnshang.data.VendorDetailBean;

public class VendorDetailImageAdapter extends RecyclerView.Adapter<VendorDetailImageAdapter.ViewHolder> {

    public ArrayList<VendorDetailBean.DataBean.Image> data;

    public Context context;

    public LayoutInflater inflater;

    public VendorDetailImageAdapter(Context context,ArrayList<VendorDetailBean.DataBean.Image> data) {
        this.context = context;
        this.data = data;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public VendorDetailImageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new VendorDetailImageAdapter.ViewHolder(inflater.inflate(R.layout.item_vendor_detail_image,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(context).load(data.get(position).getPictureUrl()).into(holder.image);
        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, ViewImagesActivity.class);
                intent.putExtra("images", data);
                intent.putExtra("index", position);
                context.startActivity(intent);
            }
        });
    }



    @Override
    public int getItemCount() {
        if (data.size() > 3){
            return 3;
        }
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);

        }
    }

}
