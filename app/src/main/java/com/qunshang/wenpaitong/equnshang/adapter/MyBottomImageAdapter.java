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

public abstract class MyBottomImageAdapter<T> extends BannerAdapter<T, MyBottomImageAdapter.MyBannerViewHolder> {

    Context context;

    public MyBottomImageAdapter(Context context, List<T> mData) {
        super(mData);
        this.context = context;
    }

    @Override
    public MyBannerViewHolder onCreateHolder(ViewGroup parent, int viewType) {
        return new MyBannerViewHolder(LayoutInflater.from(context).inflate(R.layout.item_mybanner_bottom_image,parent,false));
    }

    public static class MyBannerViewHolder extends LastLotteryAdapter.BannerViewHolder{

        ImageView imageView;

        public MyBannerViewHolder(@NonNull View itemView) {
            super(itemView);
            this.imageView = itemView.findViewById(R.id.image);
        }
    }
}