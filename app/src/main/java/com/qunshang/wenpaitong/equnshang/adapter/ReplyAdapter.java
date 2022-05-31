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
import com.qunshang.wenpaitong.equnshang.activity.ReplyActivity;
import com.qunshang.wenpaitong.equnshang.data.BaseHttpBean;
import com.qunshang.wenpaitong.equnshang.data.ReplyBean;
import com.qunshang.wenpaitong.equnshang.data.UserInfoViewModel;
import com.qunshang.wenpaitong.equnshang.model.ApiManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.qunshang.wenpaitong.equnshang.data.BaseHttpBean;
import com.qunshang.wenpaitong.equnshang.data.UserInfoViewModel;
import com.qunshang.wenpaitong.equnshang.model.ApiManager;

public class ReplyAdapter extends RecyclerView.Adapter<ReplyAdapter.ViewHolder> {

    public List<ReplyBean.DataBean> data;

    public Context context;

    public LayoutInflater inflater;

    public ReplyAdapter(Context context,List<ReplyBean.DataBean> data) {
        this.context = context;
        this.data = data;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ReplyAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.item_reply,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ReplyAdapter.ViewHolder holder, int position) {

        ReplyBean.DataBean currentpo = data.get(position);
        Glide.with(context).load(currentpo.getHeadimage_url()).into(holder.user_image);
        holder.username.setText(currentpo.getUname());

        //ReplyBean.DataBean bean = data.get(position);
        /*Glide.with(context).load(bean.getHeadimage_url()).into(holder.user_image);
        holder.username.setText(bean.getUname());*/
        holder.content.setText(currentpo.getContent());
        holder.time.setText(currentpo.getCreateTime());
        holder.upcount.setText("赞" + currentpo.getUp_num());
        holder.up.setOnClickListener(new View.OnClickListener() {//执行点赞操作
            @Override
            public void onClick(View v) {

            }
        });
        holder.close.setOnClickListener(new View.OnClickListener() {//执行删除评论操作
            @Override
            public void onClick(View v) {

            }
        });

        holder.up.setOnClickListener(new View.OnClickListener() {//执行点赞操作
            @Override
            public void onClick(View v) {
                ApiManager.Companion.getInstance().getApiVideoTest().upComment(String.valueOf(currentpo.getId()), UserInfoViewModel.INSTANCE.getUserId()).enqueue(new Callback<BaseHttpBean<Integer>>() {
                    @Override
                    public void onResponse(Call<BaseHttpBean<Integer>> call, Response<BaseHttpBean<Integer>> response) {
                        if (response.body() != null){
                            if (response.body().getCode() == 200){
                                holder.up.setImageResource(R.mipmap.btn_main_up_true);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<BaseHttpBean<Integer>> call, Throwable t) {

                    }
                });
            }
        });
        if (currentpo.getIsUp() != 0){
            holder.up.setImageResource(R.mipmap.btn_main_up_true);
        }

        /*holder.layout_reply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToReplyActivity(currentpo.getId());
            }
        });*/
    }

    private void goToReplyActivity(int id){

        Intent intent = new Intent(context, ReplyActivity.class);
        intent.putExtra("id",id);
        context.startActivity(intent);

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView user_image;

        TextView username;

        TextView content;

        //ViewGroup layout_reply;

        TextView time;

        TextView upcount;

        ImageView up;

        ImageView close;

        /*ViewGroup layout_replys;

        TextView reply_username;

        TextView reply_comments;

        TextView reply_count;*/

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            user_image = itemView.findViewById(R.id.user_image);
            username = itemView.findViewById(R.id.username);
            content = itemView.findViewById(R.id.content);
            //layout_reply = itemView.findViewById(R.id.layout_reply);
            time = itemView.findViewById(R.id.time);
            upcount = itemView.findViewById(R.id.upcount);
            up = itemView.findViewById(R.id.up);
            close = itemView.findViewById(R.id.close);
            /*layout_replys = itemView.findViewById(R.id.layout_replys);
            reply_username = itemView.findViewById(R.id.reply_username);
            reply_comments = itemView.findViewById(R.id.reply_comments);
            reply_count = itemView.findViewById(R.id.reply_count);*/
        }
    }

}
