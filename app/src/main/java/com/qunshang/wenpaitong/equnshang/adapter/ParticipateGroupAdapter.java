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
import java.util.List;

import com.qunshang.wenpaitong.R;
import com.qunshang.wenpaitong.equnshang.data.UserInfo;

public class ParticipateGroupAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public List<UserInfo> data;

    public Context context;

    public LayoutInflater inflater;

    public static final int USER = 56;

    public static final int NO_USER = 69;

    public ParticipateGroupAdapter(Context context,List<UserInfo> data){
        this.context = context;
        this.data = data;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getItemViewType(int position) {
        if (position == data.size()){
            return NO_USER;
        } else {
            return USER;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == USER){
            return new UserViewHolder(inflater.inflate(R.layout.item_particapate_user,parent,false));
        } else {
            return new NoUserViewHolder(inflater.inflate(R.layout.item_particapate_nouser,parent,false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (position < data.size()){
            if (holder instanceof UserViewHolder){
                Glide.with(context).load(data.get(position).getHeadImage()).into(((UserViewHolder) holder).image);
                if (position == 0){
                    ((UserViewHolder) holder).label.setVisibility(View.VISIBLE);
                } else {
                    ((UserViewHolder) holder).label.setVisibility(View.GONE);
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        if (data.size() == 1){
            return 2;
        }
        return data.size();
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder{

        ImageView image;

        TextView label;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            label = itemView.findViewById(R.id.label);
        }
    }

    public static class NoUserViewHolder extends RecyclerView.ViewHolder{

        public NoUserViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

}
