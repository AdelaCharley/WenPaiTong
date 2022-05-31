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

import com.qunshang.wenpaitong.R;
import com.qunshang.wenpaitong.equnshang.activity.StoreDetailActivity;
import com.qunshang.wenpaitong.equnshang.data.FocusManufactuerBean;
import com.qunshang.wenpaitong.equnshang.data.UserInfoViewModel;
import com.qunshang.wenpaitong.equnshang.data.def.ManufactureChangeBean;
import com.qunshang.wenpaitong.equnshang.model.ApiManager;
import com.qunshang.wenpaitong.equnshang.data.BaseHttpBean;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FocusManufactuerAdapter extends RecyclerView.Adapter<FocusManufactuerAdapter.ViewHolder> {

    public List<FocusManufactuerBean.DataBean> data;

    public Context context;

    public LayoutInflater inflater;

    public FocusManufactuerAdapter(Context context,List<FocusManufactuerBean.DataBean> data){
        this.context = context;
        this.data = data;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.item_focus_manufactuer,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FocusManufactuerBean.DataBean bean = data.get(position);
        Glide.with(context).load(bean.getImageUrl()).into(holder.image_focus);
        holder.name_focus.setText(bean.getName());
        holder.count_focus.setText(bean.getNumber() + "人关注");
        holder.layout.setOnClickListener(v -> {
            Intent intent = new Intent(context, StoreDetailActivity.class);
            intent.putExtra("manfactureId",bean.getManuId());
            context.startActivity(intent);
        });
        holder.button_focus.setOnClickListener(v ->
                ApiManager.Companion.getInstance().getApiPersonalTest().focusMan(UserInfoViewModel.INSTANCE.getUserId(), String.valueOf(bean.getManuId()))
                .enqueue(new Callback<BaseHttpBean<Boolean>>() {
                    @Override
                    public void onResponse(Call<BaseHttpBean<Boolean>> call, Response<BaseHttpBean<Boolean>> response) {
                        if (!response.body().getData()){
                            holder.button_focus.setText("取消关注");
                        } else {
                            holder.button_focus.setText("关注");
                        }
                        ManufactureChangeBean followChangeBean = new ManufactureChangeBean();
                        followChangeBean.setId(bean.getManuId());
                        followChangeBean.setFollow(response.body().getData());
                        EventBus.getDefault().post(followChangeBean);
                    }

                    @Override
                    public void onFailure(Call<BaseHttpBean<Boolean>> call, Throwable t) {

                    }
                }));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        ImageView image_focus;

        TextView name_focus;

        TextView count_focus;

        TextView button_focus;

        View layout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image_focus = itemView.findViewById(R.id.image_focus);
            name_focus = itemView.findViewById(R.id.name_focus);
            count_focus = itemView.findViewById(R.id.count_focus);
            button_focus = itemView.findViewById(R.id.button_focus);
            layout = itemView.findViewById(R.id.layout);
        }
    }

}
