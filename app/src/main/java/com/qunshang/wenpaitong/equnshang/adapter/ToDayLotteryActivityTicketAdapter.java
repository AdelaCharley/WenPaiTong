package com.qunshang.wenpaitong.equnshang.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.qunshang.wenpaitong.R;

import com.qunshang.wenpaitong.equnshang.data.ToDayLotteryActivityBean;

import java.util.List;

public class ToDayLotteryActivityTicketAdapter extends RecyclerView.Adapter<ToDayLotteryActivityTicketAdapter.ViewHolder> {

    public List<ToDayLotteryActivityBean.DataBean.ParticipateNumberList> data;

    public Context context;

    public LayoutInflater inflater;

    public ToDayLotteryActivityTicketAdapter(Context context,List<ToDayLotteryActivityBean.DataBean.ParticipateNumberList> data) {
        this.context = context;
        this.data = data;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ToDayLotteryActivityTicketAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.item_today_lottery_ticket_activity,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ToDayLotteryActivityTicketAdapter.ViewHolder holder, int position) {
        holder.no.setText(data.get(position).getNumber());
        if (data.get(position).getNumber_status() == 1){
            holder.no.setBackground(context.getDrawable(R.mipmap.bg_weizhongjiang));
        } else if (data.get(position).getNumber_status() == 2){
            holder.no.setBackground(context.getDrawable(R.mipmap.bg_jiangquan_today));
        }
    }


    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView no;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            no = itemView.findViewById(R.id.no);

        }
    }

}