package com.qunshang.wenpaitong.equnshang.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.youth.banner.adapter.BannerAdapter;

import java.util.List;

import com.qunshang.wenpaitong.R;

public abstract class My16dpBannerImageAdapter<T> extends BannerAdapter<T, My16dpBannerImageAdapter.MyBannerViewHolder> {

    Context context;

    public My16dpBannerImageAdapter(Context context,List<T> mData) {
        super(mData);
        this.context = context;
    }

    @Override
    public MyBannerViewHolder onCreateHolder(ViewGroup parent, int viewType) {
        return new MyBannerViewHolder(LayoutInflater.from(context).inflate(R.layout.item__16dp_mybanner_image,parent,false));
    }

    public static class MyBannerViewHolder extends LastLotteryAdapter.BannerViewHolder{

        public ImageView imageView;

        public MyBannerViewHolder(@NonNull View itemView) {
            super(itemView);
            this.imageView = itemView.findViewById(R.id.image);
        }
    }
}