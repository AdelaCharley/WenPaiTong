package com.qunshang.wenpaitong.equnshang.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.qunshang.wenpaitong.R;
import com.qunshang.wenpaitong.equnshang.activity.VideoPrivateSettingActivity;
import com.qunshang.wenpaitong.equnshang.data.BaseHttpBean;
import com.qunshang.wenpaitong.equnshang.data.EditVideoBean;
import com.qunshang.wenpaitong.equnshang.fragment.EditVideoFragment;
import com.qunshang.wenpaitong.equnshang.model.ApiManager;
import com.kongzue.dialog.v3.BottomMenu;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditVideoAdapter extends RecyclerView.Adapter<EditVideoAdapter.ViewHolder> {

    public List<EditVideoBean.DataBean> data;

    public Context context;

    public LayoutInflater inflater;

    EditVideoFragment fragment;

    public EditVideoAdapter(Context context, List<EditVideoBean.DataBean> data, EditVideoFragment fragment){
        this.context = context;
        this.data = data;
        this.fragment = fragment;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.item_edit_video,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(context).load(data.get(position).getVideo_poster_url()).into(holder.image);
        holder.title.setText(data.get(position).getVideo_desc());
        holder.time.setText(data.get(position).getCreate_time());
        holder.delete.setOnClickListener(v ->
                BottomMenu.show((AppCompatActivity) context, new String[]{"确认删除", "设为私密作品"}, (text, index) -> {
                    /*if (index == 2){

                    }*/
                    if (index == 0){
                        ApiManager.Companion.getInstance().getApiPersonalTest().deleteVideo(data.get(position).getId()).enqueue(new Callback<BaseHttpBean<Integer>>() {
                            @Override
                            public void onResponse(Call<BaseHttpBean<Integer>> call, Response<BaseHttpBean<Integer>> response) {
                                if (response.body() == null){
                                    return;
                                }
                                /*data.remove(holder.getBindingAdapterPosition());
                                notifyItemRemoved(holder.getBindingAdapterPosition());*/
                                fragment.load();
                            }

                            @Override
                            public void onFailure(Call<BaseHttpBean<Integer>> call, Throwable t) {

                            }
                        });
                    }
                    if (index == 1){
                        goToActivity(position);
                    }
                }).setTitle("你的作品将永久删除，无法找回。确认删除？"));
        holder.edit.setOnClickListener(v -> {
            goToActivity(position);
        });
        if ("0".equals(data.get(position).getIs_private())){
            holder.isprivate.setVisibility(View.GONE);
        } else {
            holder.isprivate.setVisibility(View.VISIBLE);
        }
    }

    public void update(VideoPrivateSettingActivity.VideoPrivateData data){
        /*if ("0".equals(data.get(data).getIs_private())){
            data.get(pos).setIs_private("1");
        } else {
            data.get(pos).setIs_private("0");
        }*/
        this.data.get(data.getPosition()).setIs_private(data.getIsprivate());
        this.data.get(data.getPosition()).setVideo_desc(data.getDesc());
        notifyItemChanged(data.getPosition());
    }

    public void goToActivity(int position){
        Intent intent = new Intent(context, VideoPrivateSettingActivity.class);
        intent.putExtra("position",position);
        intent.putExtra("data",data.get(position));
        context.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        ImageView image;

        TextView title;

        TextView time;

        AppCompatImageView edit;

        AppCompatImageView delete;

        TextView isprivate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            title = itemView.findViewById(R.id.tittestr);
            time = itemView.findViewById(R.id.time);
            edit = itemView.findViewById(R.id.edit);
            delete = itemView.findViewById(R.id.delete);
            isprivate = itemView.findViewById(R.id.isprivate);
        }
    }

}