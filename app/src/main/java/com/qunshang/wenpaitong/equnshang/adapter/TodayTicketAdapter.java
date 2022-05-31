package com.qunshang.wenpaitong.equnshang.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.qunshang.wenpaitong.R;
import com.qunshang.wenpaitong.equnshang.data.TodayTicketBean;

import java.util.List;

public class TodayTicketAdapter extends RecyclerView.Adapter<TodayTicketAdapter.TicketViewHolder>{

    public List<TodayTicketBean.DataBean> data;

    public Context context;

    public LayoutInflater inflater;

    //public static final int TYPE_HAS = 700;

    //public static final int TYPE_HASNO = 800;

    public TodayTicketAdapter(Context context, List<TodayTicketBean.DataBean> data){
        this.context = context;
        this.data = data;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public TicketViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //if (viewType == TYPE_HAS){
        return new TicketViewHolder(inflater.inflate(R.layout.item_ticket_has,parent,false));
        /*} else {
            return new HasNoViewHolder(inflater.inflate(R.layout.item_ticket_hasno,parent,false));
        }*/
    }

    @Override
    public void onBindViewHolder(@NonNull TicketViewHolder holder, int position) {
        //if (holder instanceof TicketViewHolder){
        if (position < data.size()){
            TodayTicketBean.DataBean bean = data.get(position);
            holder.ticketno.setText(bean.getNumber());
            if (bean.getStatus() == 0){
                holder.ticketno.setBackground(context.getDrawable(R.mipmap.jiangquanweishiyong));
            } else {
                holder.ticketno.setBackground(context.getDrawable(R.mipmap.jiangquanyishiyong));
            }
        } else {
            holder.ticketno.setText("待获取");
            holder.ticketno.setBackground(context.getDrawable(R.drawable.bg_ticket_daihuoqu));
        }

        //} /*else if (holder instanceof HasNoViewHolder){

        //}
    }

    @Override
    public int getItemCount() {
        return data.size() + 1;
    }

   /* @Override
    public int getItemViewType(int position) {
        if (position < data.size()){
            return TYPE_HAS;
        } else {
            return TYPE_HASNO;
        }
    }*/

    public static class TicketViewHolder extends RecyclerView.ViewHolder{

        TextView ticketno;

        public TicketViewHolder(@NonNull View itemView) {
            super(itemView);
            ticketno = itemView.findViewById(R.id.ticketno);
        }
    }

    /*public class HasNoViewHolder extends RecyclerView.OwnerViewHolder{

        TextView waitforget;

        public HasNoViewHolder(@NonNull View itemView) {
            super(itemView);
            waitforget = itemView.findViewById(R.id.waitforget);
        }
    }*/

}
