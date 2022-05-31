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
import com.qunshang.wenpaitong.R;
import com.qunshang.wenpaitong.equnshang.data.MyCommentBean;
import com.qunshang.wenpaitong.equnshang.utils.CommonUtil;

import java.util.List;

public class MyCommentAdapter extends RecyclerView.Adapter<MyCommentAdapter.ViewHolder> {

    public List<MyCommentBean.DataBean> data;

    public Context context;

    public LayoutInflater inflater;

    public MyCommentAdapter(Context context,List<MyCommentBean.DataBean> data){
        this.context = context;
        this.data = data;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public MyCommentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyCommentAdapter.ViewHolder(inflater.inflate(R.layout.item_my_comment,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyCommentAdapter.ViewHolder holder, int position) {
        MyCommentBean.DataBean dataBean = data.get(position);
        Glide.with(context).load(dataBean.getHeadimage()).into(holder.imageView);
        holder.username.setText(dataBean.getUname());
        holder.time.setText(dataBean.getTime());
        holder.content.setText(dataBean.getCotent());
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) holder.layout.getLayoutParams();
        if (position == data.size() - 1) {
            layoutParams.setMargins(
                    layoutParams.leftMargin,
                    layoutParams.topMargin,
                    layoutParams.rightMargin,
                    CommonUtil.dp2px(holder.layout.getContext(), 30)
            );
        } else {
            layoutParams.setMargins(
                    layoutParams.leftMargin,
                    layoutParams.topMargin,
                    layoutParams.rightMargin,
                    0
            );
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        ImageView imageView;

        TextView username;

        TextView time;

        TextView content;

        View layout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            layout = itemView.findViewById(R.id.layout);
            imageView = itemView.findViewById(R.id.image_comment);
            username = itemView.findViewById(R.id.username_comment);
            time = itemView.findViewById(R.id.time_comment);
            content = itemView.findViewById(R.id.content_comment);
        }
    }

}
