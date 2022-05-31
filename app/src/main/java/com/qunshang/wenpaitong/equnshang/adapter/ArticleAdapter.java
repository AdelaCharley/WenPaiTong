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
import com.qunshang.wenpaitong.equnshang.activity.ArticleDetailActivity;
import com.qunshang.wenpaitong.equnshang.data.GroupMainHomePageInfoBean;

import java.util.List;

public class ArticleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public List<GroupMainHomePageInfoBean.DataBean.ArticleBean> data;

    public Context context;

    public LayoutInflater inflater;

    private final int TYPE_ZERO = -9;

    private final int TYPE_SINGLE = -85;

    private final int TYPE_MULTI = -84;

    public ArticleAdapter(Context context,List<GroupMainHomePageInfoBean.DataBean.ArticleBean> data){
        this.context = context;
        this.data = data;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_ZERO){
            return new NoImageViewHolder(inflater.inflate(R.layout.item_noimageviewholder,parent,false));
        }
        if (viewType == TYPE_SINGLE){
            return new SingleImageViewHolder(inflater.inflate(R.layout.item_oneimageviewholder,parent,false));
        }
        else {
            return new MultiImageViewHolder(inflater.inflate(R.layout.item_multiimageviewholder,parent,false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        GroupMainHomePageInfoBean.DataBean.ArticleBean bean = data.get(position);
        if (holder instanceof NoImageViewHolder){
            ((NoImageViewHolder) holder).content.setText(bean.getTitle());
            ((NoImageViewHolder) holder).time.setText(bean.getCreateTime());
            ((NoImageViewHolder) holder).layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    goToDetail(position);
                }
            });
        }
        if (holder instanceof SingleImageViewHolder){
            ((SingleImageViewHolder) holder).content.setText(bean.getTitle());
            ((SingleImageViewHolder) holder).time.setText(bean.getCreateTime());
            Glide.with(context).load(bean.getImage().get(0)).into(((SingleImageViewHolder) holder).image);
            ((SingleImageViewHolder) holder).layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    goToDetail(position);
                }
            });
        }
        if (holder instanceof MultiImageViewHolder){
            ((MultiImageViewHolder) holder).content.setText(bean.getTitle());
            ((MultiImageViewHolder) holder).time.setText(bean.getCreateTime());
            if (bean.getImage().size() > 0){
                Glide.with(context).load(bean.getImage().get(0)).into(((MultiImageViewHolder) holder).first);
            }
            if (bean.getImage().size() > 1){
                Glide.with(context).load(bean.getImage().get(1)).into(((MultiImageViewHolder) holder).second);
            }
            if (bean.getImage().size() > 2){
                Glide.with(context).load(bean.getImage().get(2)).into(((MultiImageViewHolder) holder).third);
            }
            ((MultiImageViewHolder) holder).layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    goToDetail(position);
                }
            });
        }
    }

    public void goToDetail(int pos){
        GroupMainHomePageInfoBean.DataBean.ArticleBean bean = data.get(pos);
        Intent intent = new Intent(context, ArticleDetailActivity.class);
        intent.putExtra("name",bean.getTitle());
        intent.putExtra("id",bean.getId());
        context.startActivity(intent);
    }

    @Override
    public int getItemViewType(int position) {
        GroupMainHomePageInfoBean.DataBean.ArticleBean bean = data.get(position);
        if (bean.getImage().size() == 0){
            return TYPE_ZERO;
        } else if (bean.getImage().size() == 1){
            return TYPE_SINGLE;
        } else {
            return TYPE_MULTI;
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class SingleImageViewHolder extends RecyclerView.ViewHolder{

        TextView content;

        TextView time;

        ImageView image;

        ViewGroup layout;

        public SingleImageViewHolder(@NonNull View itemView) {
            super(itemView);
            content = itemView.findViewById(R.id.content);
            time = itemView.findViewById(R.id.time);
            image = itemView.findViewById(R.id.image);
            layout = itemView.findViewById(R.id.layout);
        }
    }

    public static class MultiImageViewHolder extends RecyclerView.ViewHolder{

        TextView content;

        TextView time;

        ImageView first;

        ImageView second;

        ImageView third;

        ViewGroup layout;

        public MultiImageViewHolder(@NonNull View itemView) {
            super(itemView);
            content = itemView.findViewById(R.id.content);
            time = itemView.findViewById(R.id.time);
            first = itemView.findViewById(R.id.image_first);
            second = itemView.findViewById(R.id.image_second);
            third = itemView.findViewById(R.id.image_third);
            layout = itemView.findViewById(R.id.layout);
        }
    }

    public static class NoImageViewHolder extends RecyclerView.ViewHolder{

        TextView content;

        TextView time;

        ViewGroup layout;

        public NoImageViewHolder(@NonNull View itemView) {
            super(itemView);
            content = itemView.findViewById(R.id.content);
            time = itemView.findViewById(R.id.time);
            layout = itemView.findViewById(R.id.layout);
        }
    }

}
