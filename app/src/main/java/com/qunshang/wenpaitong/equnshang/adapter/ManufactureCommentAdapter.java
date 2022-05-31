package com.qunshang.wenpaitong.equnshang.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import com.bumptech.glide.Glide;
import com.qunshang.wenpaitong.R;
import com.qunshang.wenpaitong.equnshang.activity.LoginActivity;
import com.qunshang.wenpaitong.equnshang.data.BaseHttpBean;
import com.qunshang.wenpaitong.equnshang.data.UserInfoViewModel;
import com.qunshang.wenpaitong.equnshang.model.ApiManager;
import com.qunshang.wenpaitong.equnshang.utils.UserHelper;
import com.qunshang.wenpaitong.equnshang.view.BaseRvViewHolder;
import com.qunshang.wenpaitong.equnshang.utils.NumUtil;
import com.qunshang.wenpaitong.equnshang.data.CommentBean;

import java.util.List;
import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.qunshang.wenpaitong.equnshang.data.BaseHttpBean;
import com.qunshang.wenpaitong.equnshang.model.ApiManager;
import com.qunshang.wenpaitong.equnshang.utils.NumUtil;
import com.qunshang.wenpaitong.equnshang.view.BaseRvViewHolder;

/**
 * create by libo
 * create on 2020-05-24
 * modifier 何姝霖
 * last-modified 2021-08-05
 */
public class ManufactureCommentAdapter extends BaseRvAdapter<CommentBean.DataDTO, ManufactureCommentAdapter.CommentViewHolder> {

    public ManufactureCommentAdapter(Context context, List<CommentBean.DataDTO> datas) {
        super(context, datas);
    }


    @Override
    protected void onBindData(CommentViewHolder holder, CommentBean.DataDTO commentData, int position) {
        Glide.with(getContext()).load(commentData.getUserHeadimageUrl()).into(holder.ivHead);
        holder.tvNickname.setText(commentData.getUserName());
        holder.tvContent.setText(commentData.getContent());
        holder.tvLikecount.setText(NumUtil.numberFilter(commentData.getLikeNum()));
        holder.tvTime.setText(commentData.getCreateTime());
        if (commentData.getIsUp() !=0){//已经给评论点过赞了
            //holder.like.setChecked(true);
            commentData.setLiked(true);
            holder.like.setImageDrawable(getContext().getResources().getDrawable(R.mipmap.btn_comment_like_true));
            //Toast.makeText(getContext(), "可以选中了", Toast.LENGTH_SHORT).show();
        }
        holder.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!UserHelper.isLogin(getContext())){
                    getContext().startActivity(new Intent(getContext(), LoginActivity.class));
                    return;
                }
                //Toast.makeText(getContext(), "好吧", Toast.LENGTH_SHORT).show();
                ApiManager.Companion.getInstance().getApiVideoTest().likeComment(UserInfoViewModel.INSTANCE.getUserId(), commentData.getReplyId().toString()).enqueue(new Callback<BaseHttpBean<Integer>>() {
                    @Override
                    public void onResponse(Call<BaseHttpBean<Integer>> call, Response<BaseHttpBean<Integer>> response) {
                        Log.i("zhangjuniii","onResponese");
                        commentData.setLiked(!commentData.isLiked());
                        if (commentData.isLiked()){
                            holder.like.setImageDrawable(getContext().getResources().getDrawable(R.mipmap.btn_comment_like_true));
                            holder.tvLikecount.setText(NumUtil.numberFilter(Integer.parseInt(holder.tvLikecount.getText().toString()) + 1));
                        } else {
                            holder.like.setImageDrawable(getContext().getResources().getDrawable(R.mipmap.btn_comment_like_false));
                            holder.tvLikecount.setText(NumUtil.numberFilter(Integer.parseInt(holder.tvLikecount.getText().toString()) - 1));
                        }
                    }

                    @Override
                    public void onFailure(Call<BaseHttpBean<Integer>> call, Throwable t) {

                    }
                });
            }
        });
        /*if (commentData.getIsUp() !=0){
            holder.like.setChecked(true);
            Toast.makeText(getContext(), "可以选中了", Toast.LENGTH_SHORT).show();
        }
        holder.like.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Toast.makeText(getContext(), "好吧", Toast.LENGTH_SHORT).show();
                ApiManager.Companion.getInstance().getApiVideoTest().likeManufactureComment(UserInfoViewModel.INSTANCE.getUserId(), commentData.getReplyId().toString()).enqueue(new Callback<BaseHttpBean<Integer>>() {
                    @Override
                    public void onResponse(Call<BaseHttpBean<Integer>> call, Response<BaseHttpBean<Integer>> response) {
                        Log.i("zhangjuniii","onResponese");
                        if (isChecked){
                            holder.tvLikecount.setText(NumUtil.numberFilter(Integer.parseInt(holder.tvLikecount.getText().toString()) + 1));
                        } else {
                            holder.tvLikecount.setText(NumUtil.numberFilter(Integer.parseInt(holder.tvLikecount.getText().toString()) - 1));
                        }
                    }

                    @Override
                    public void onFailure(Call<BaseHttpBean<Integer>> call, Throwable t) {

                    }
                });
            }
        });*/

    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.item_comment, parent, false);
        return new CommentViewHolder(view);
    }

    public static class CommentViewHolder extends BaseRvViewHolder {
        @BindView(R.id.iv_item_comment_head)
        ImageView ivHead;
        @BindView(R.id.tv_item_comment_name)
        TextView tvNickname;
        @BindView(R.id.tv_item_comment_content)
        TextView tvContent;
        @BindView(R.id.tv_item_comment_likecount)
        TextView tvLikecount;
        @BindView(R.id.tv_item_comment_time)
        TextView tvTime;
        @BindView(R.id.btn_item_comment_like)
        ImageView like;

        public CommentViewHolder(View itemView) {
            super(itemView);
        }

    }
}
