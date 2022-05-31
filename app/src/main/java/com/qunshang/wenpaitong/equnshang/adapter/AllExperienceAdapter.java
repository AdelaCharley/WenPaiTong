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

import java.util.Arrays;
import java.util.List;

import com.qunshang.wenpaitong.R;
import com.qunshang.wenpaitong.equnshang.activity.ExperienceActivity;
import com.qunshang.wenpaitong.equnshang.data.AllExperienceBean;
import com.qunshang.wenpaitong.equnshang.utils.KTUtil;

public class AllExperienceAdapter extends RecyclerView.Adapter<AllExperienceAdapter.ViewHolder> {
    public List<AllExperienceBean.DataBean> data;

    public Context context;

    public LayoutInflater inflater;

    public AllExperienceAdapter(Context context,List<AllExperienceBean.DataBean> data){
        this.context = context;
        this.data = data;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public AllExperienceAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.item_all_experience,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull AllExperienceAdapter.ViewHolder holder, int position) {
        AllExperienceBean.DataBean bean = data.get(position);
        Glide.with(context).load(bean.getUserImage()).into(holder.headimage);
        holder.name.setText(bean.getUname());
        holder.content.setText(bean.getContent());
        List<String> detailimages = KTUtil.Companion.filterList(Arrays.asList(bean.getImageUrl().split(",").clone()));
        if (detailimages.size() != 0){
            Glide.with(context).load(detailimages.get(0)).into(holder.image);
        }
        holder.time.setText(bean.getCreateTime());
        holder.commentcount.setText(String.valueOf(bean.getCommentNum()));
        holder.upcount.setText(String.valueOf(bean.getUpNum()));
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ExperienceActivity.class);
                intent.putExtra("experienceid",bean.getExperienceId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        ImageView headimage;

        TextView name;

        TextView content;

        ImageView image;

        TextView time;

        TextView commentcount;

        ImageView commenticon;

        TextView upcount;

        ImageView iv_zan;

        View layout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            headimage = itemView.findViewById(R.id.userimage);
            name = itemView.findViewById(R.id.username);
            content = itemView.findViewById(R.id.content);
            image = itemView.findViewById(R.id.image);
            time = itemView.findViewById(R.id.time);
            commentcount = itemView.findViewById(R.id.commentcount);
            commenticon = itemView.findViewById(R.id.commenticon);
            upcount = itemView.findViewById(R.id.upcount);
            iv_zan = itemView.findViewById(R.id.iv_zan);
            layout = itemView.findViewById(R.id.layout);
        }
    }

}
