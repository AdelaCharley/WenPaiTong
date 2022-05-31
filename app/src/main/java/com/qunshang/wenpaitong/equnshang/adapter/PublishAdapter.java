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

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import com.qunshang.wenpaitong.R;
import com.qunshang.wenpaitong.equnshang.activity.VideoListActivity;
import com.qunshang.wenpaitong.equnshang.data.RecommendVideoBean;
import com.qunshang.wenpaitong.equnshang.data.def.FollowChangeBean;
import com.qunshang.wenpaitong.equnshang.utils.CommonUtil;
import com.qunshang.wenpaitong.equnshang.utils.VideoDataUtil;

import java.util.ArrayList;
import java.util.List;

public class PublishAdapter extends RecyclerView.Adapter<PublishAdapter.ViewHolder> {

    public ArrayList<RecommendVideoBean.DataBean> data = new ArrayList<>();

    public Context context;

    public LayoutInflater inflater;

    public PublishAdapter(Context contex){
        this.context = contex;
        inflater = LayoutInflater.from(context);
        if (!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void update(FollowChangeBean bean) {
        if (data.size() == 0){
            return;
        }
        for (int i = 0; i < data.size();i++) {
            if (data.get(i).getAgencyId() == bean.getId()) {
                if (bean.isFollow()) {
                    data.get(i).setIsFocus(0);
                } else {
                    data.get(i).setIsFocus(222);
                }
            }
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PublishAdapter.ViewHolder(inflater.inflate(R.layout.item_publish_video_list,parent,false));
    }

    public void addData(List<RecommendVideoBean.DataBean> data){
        this.data.addAll(data);
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(context).load(data.get(position).getVideoPosterUrl()).into(holder.imageView);
        holder.textView.setText(data.get(position).getCreateTime());
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
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams)holder.imageView.getLayoutParams();
        if (position %2 == 0){
            layoutParams.setMargins(0,0, CommonUtil.dp2px(context,2),layoutParams.bottomMargin);
            holder.imageView.setLayoutParams(layoutParams);
        } else {
            layoutParams.setMargins(CommonUtil.dp2px(context,2),0, 0,layoutParams.bottomMargin);
            holder.imageView.setLayoutParams(layoutParams);
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        ImageView imageView;
        TextView textView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_up_video);
            textView = itemView.findViewById(R.id.tv_create_time);
        }
    }
}
