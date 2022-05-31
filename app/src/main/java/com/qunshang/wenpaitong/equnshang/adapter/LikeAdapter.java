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
import com.qunshang.wenpaitong.R;
import com.qunshang.wenpaitong.equnshang.activity.VideoListActivity;
import com.qunshang.wenpaitong.equnshang.data.RecommendVideoBean;
import com.qunshang.wenpaitong.equnshang.utils.CommonUtil;
import com.qunshang.wenpaitong.equnshang.utils.VideoDataUtil;

import java.util.ArrayList;

public class LikeAdapter extends RecyclerView.Adapter<LikeAdapter.ViewHolder> {

    public ArrayList<RecommendVideoBean.DataBean> data;

    public Context context;

    public LayoutInflater inflater;

    public LikeAdapter(Context context, ArrayList<RecommendVideoBean.DataBean> data){
        this.context = context;
        this.data = data;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.item_like,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(context).load(data.get(position).getVideoPosterUrl()).into(holder.imageView);
        holder.textView.setText(data.get(position).getVideoDesc());
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, VideoListActivity.class);
                //intent.putExtra("data", CommonUtil.filterRecommendData(data,position));
                VideoDataUtil.Companion.setVideoData(data);
                intent.putExtra("position",position);
                context.startActivity(intent);
            }
        });
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) holder.root.getLayoutParams();
        if (data.size() >= 2){
            if (position == data.size() - 1 || position == data.size() - 2) {
                if (data.size() % 2 == 0){
                    layoutParams.setMargins(
                            layoutParams.leftMargin,
                            layoutParams.topMargin,
                            layoutParams.rightMargin,
                            CommonUtil.dp2px(holder.root.getContext(), 30)
                    );
                } else {
                    if (position == data.size() - 1){
                        layoutParams.setMargins(
                                layoutParams.leftMargin,
                                layoutParams.topMargin,
                                layoutParams.rightMargin,
                                CommonUtil.dp2px(holder.root.getContext(), 30)
                        );
                    }
                }
            } else {
                layoutParams.setMargins(
                        layoutParams.leftMargin,
                        layoutParams.topMargin,
                        layoutParams.rightMargin,
                        0
                );
            }
        } else {
            layoutParams.setMargins(
                    layoutParams.leftMargin,
                    layoutParams.topMargin,
                    layoutParams.rightMargin,
                    CommonUtil.dp2px(holder.root.getContext(), 30)
            );
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        ImageView imageView;

        TextView textView;

        View root;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            root = itemView.findViewById(R.id.root);
            imageView = itemView.findViewById(R.id.image_like_video);
            textView = itemView.findViewById(R.id.text_like_video);
        }
    }
}