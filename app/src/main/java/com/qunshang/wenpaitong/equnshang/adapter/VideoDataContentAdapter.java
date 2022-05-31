package com.qunshang.wenpaitong.equnshang.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.qunshang.wenpaitong.R;
import com.qunshang.wenpaitong.equnshang.data.VideoContentDataBean;
import java.util.List;

public class VideoDataContentAdapter extends RecyclerView.Adapter<VideoDataContentAdapter.ViewHolder> {

    public List<VideoContentDataBean.DataBean> data;

    public Context context;

    public LayoutInflater inflater;

    public VideoDataContentAdapter(Context context,List<VideoContentDataBean.DataBean> data){
        this.context = context;
        this.data = data;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.item_video_data_content,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        VideoContentDataBean.DataBean bean = data.get(position);
        holder.title.setText(bean.getVideo_desc());
        holder.count_star.setText(String.valueOf(bean.getLikeCount()));
        holder.count_up.setText(String.valueOf(bean.getUpCount()));
        holder.count_comment.setText(String.valueOf(bean.getReplyCount()));
        holder.count_share.setText(String.valueOf(bean.getShareCount()));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        //ImageView imageView;

        //TextView textView;

        TextView title;

        TextView count_star;

        TextView count_up;

        TextView count_comment;

        TextView count_share;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            //imageView = itemView.findViewById(R.id.image_like_video);
            //textView = itemView.findViewById(R.id.text_like_video);
            title = itemView.findViewById(R.id.tittestr);
            count_star =  itemView.findViewById(R.id.count_star);
            count_up = itemView.findViewById(R.id.count_up);
            count_comment = itemView.findViewById(R.id.count_comment);
            count_share = itemView.findViewById(R.id.count_share);
        }
    }
}