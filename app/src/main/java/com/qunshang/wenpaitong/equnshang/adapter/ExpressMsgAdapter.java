package com.qunshang.wenpaitong.equnshang.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.qunshang.wenpaitong.R;
import com.qunshang.wenpaitong.equnshang.data.ExpressBean;

import java.util.List;

public class ExpressMsgAdapter extends RecyclerView.Adapter<ExpressMsgAdapter.ViewHolder> {

    public List<ExpressBean.DataBean.ExpressMsg> data;

    public Context context;

    public LayoutInflater inflater;

    public ExpressMsgAdapter(Context context,List<ExpressBean.DataBean.ExpressMsg> data){
        this.context = context;
        this.data = data;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.item_express_msg,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.info.setText(data.get(position).getContext());
        holder.time.setText(data.get(position).getTime());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        TextView info;

        TextView time;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            info = itemView.findViewById(R.id.info);
            time = itemView.findViewById(R.id.time);
        }
    }

}
