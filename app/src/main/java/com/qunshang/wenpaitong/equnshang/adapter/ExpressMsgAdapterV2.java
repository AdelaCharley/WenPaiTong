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
import com.qunshang.wenpaitong.equnshang.data.ExpressBean;

public class ExpressMsgAdapterV2 extends RecyclerView.Adapter<ExpressMsgAdapterV2.ViewHolder> {

    public List<ExpressBean.DataBean.ExpressMsg> data;

    public Context context;

    public LayoutInflater inflater;

    public ExpressMsgAdapterV2(Context context,List<ExpressBean.DataBean.ExpressMsg> data){
        this.context = context;
        this.data = data;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.item_express_msgv2,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.info.setText(data.get(position).getContext());
        holder.time.setText(data.get(position).getTime());
        if (position == 0){
            holder.circle.setBackground(context.getDrawable(R.drawable.bg_express_circle_red));
            holder.time.setTextColor(context.getColor(R.color.color_text_isdef_true));
            holder.info.setTextColor(context.getColor(R.color.color_text_isdef_true));
        } else {
            holder.circle.setBackground(context.getDrawable(R.drawable.bg_express_circle_grey));
            holder.time.setTextColor(context.getColor(R.color.color_text_isdef_false));
            holder.info.setTextColor(context.getColor(R.color.color_text_isdef_false));
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        TextView info;

        TextView time;

        View circle;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            info = itemView.findViewById(R.id.info);
            time = itemView.findViewById(R.id.time);
            circle = itemView.findViewById(R.id.circle);
        }
    }

}