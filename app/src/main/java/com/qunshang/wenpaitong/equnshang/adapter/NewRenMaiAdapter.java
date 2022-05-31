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

import java.util.ArrayList;
import java.util.List;
import com.qunshang.wenpaitong.R;
import com.qunshang.wenpaitong.equnshang.data.NewRenMaiBean;
import com.qunshang.wenpaitong.equnshang.utils.StringUtils;

public class NewRenMaiAdapter extends RecyclerView.Adapter<NewRenMaiAdapter.ViewHolder> {
    public List<NewRenMaiBean.DataBean> data = new ArrayList<>();

    public Context context;

    public LayoutInflater inflater;

    public NewRenMaiAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public void add(List<NewRenMaiBean.DataBean> data) {
        this.data.addAll(data);
        //StringUtils.log("一共有" + data.size() + "个" + "item");
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public NewRenMaiAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.item_new_renmai, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        NewRenMaiBean.DataBean dataBean = data.get(position);
        Glide.with(context).load(dataBean.getUserAvatar()).into(holder.imageView);
        holder.username.setText(dataBean.getUserName() + " " + dataBean.getUserTel());
        holder.time.setText(dataBean.getCreateTime());
        if (dataBean.getIsPartner() == 0){
            holder.button.setText("粉丝");
        } else if (dataBean.getIsPartner() == 2){
            holder.button.setText("店主");
        } else if (dataBean.getIsPartner() == 3){
            holder.button.setText("主任");
        } else if (dataBean.getIsPartner() == 4){
            holder.button.setText("总裁");
        } else {
            holder.button.setText("其他");
        }
        //holder.button.setText("0");
    }

    @Override
    public int getItemCount() {
        if (data == null){
            return 0;
        }
        return data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;

        TextView username;

        TextView time;

        TextView button;

        View layout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageview_concern);
            username = itemView.findViewById(R.id.name_concern);
            time = itemView.findViewById(R.id.time_concern);
            layout = itemView.findViewById(R.id.layout);
            button = itemView.findViewById(R.id.button_concern);
        }
    }
}
