package com.qunshang.wenpaitong.equnshang.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import com.qunshang.wenpaitong.R;
import com.qunshang.wenpaitong.equnshang.data.HelpListBean;

public class HelpListAdapter extends RecyclerView.Adapter<HelpListAdapter.ViewHolder> {

    public List<HelpListBean.DataBean> data;

    public Context context;

    public LayoutInflater inflater;

    public HelpListAdapter(Context context,List<HelpListBean.DataBean> data){
        this.context = context;
        this.data = data;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public HelpListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new HelpListAdapter.ViewHolder(inflater.inflate(R.layout.item_help_list,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull HelpListAdapter.ViewHolder holder, int position) {
        HelpListBean.DataBean bean = data.get(position);
        holder.num.setText(String.valueOf(position + 1));
        holder.name.setText(bean.getUserName());
        holder.time.setText(bean.getHelpTime());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        TextView num;

        TextView name;

        TextView time;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            num = itemView.findViewById(R.id.num);
            name = itemView.findViewById(R.id.videoname);
            time = itemView.findViewById(R.id.time);
        }
    }

}
