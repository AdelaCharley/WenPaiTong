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
import com.qunshang.wenpaitong.equnshang.utils.VideoDataUtil;

import java.util.ArrayList;

public class SearchVideoAdapter extends RecyclerView.Adapter<SearchVideoAdapter.ViewHolder> {

    public ArrayList<RecommendVideoBean.DataBean> data;

    public Context context;

    public LayoutInflater inflater;

    public SearchVideoAdapter(Context context,ArrayList<RecommendVideoBean.DataBean> data){
        this.context = context;
        this.data = data;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.item_search_video,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(context).load(data.get(position).getVideoPosterUrl()).into(holder.imageView);
        holder.textView.setText(data.get(position).getVideoDesc());
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, VideoListActivity.class);
                //intent.putExtra("data",data);
                VideoDataUtil.Companion.setVideoData(data);
                intent.putExtra("position",position);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        ImageView imageView;

        TextView textView;

        ViewGroup layout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_up_video);
            textView = itemView.findViewById(R.id.text_up_video);
            layout = itemView.findViewById(R.id.layout);
        }
    }
}
