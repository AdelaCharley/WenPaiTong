package com.qunshang.wenpaitong.equnshang.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import com.qunshang.wenpaitong.R;
import com.qunshang.wenpaitong.equnshang.data.DiscountTicketBean;
import com.qunshang.wenpaitong.equnshang.utils.DialogUtil;

public class ChooseDiscountTicketAdapter extends RecyclerView.Adapter<ChooseDiscountTicketAdapter.ViewHolder> {

    List<DiscountTicketBean.DataBean> data;

    Context context;

    LayoutInflater inflater;

    public static final int TYPE_CHOOSE_TICKET_RESULT = 36;

    int index = 0;

    double price;

    public ChooseDiscountTicketAdapter(Context context, List<DiscountTicketBean.DataBean> data,double price){
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
        if (!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this);
        }
        this.price = price;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.item_choose_discount_ticket,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DiscountTicketBean.DataBean bean = data.get(position);
        holder.price.setText("￥" + (int)bean.getPrice());
        holder.product_name.setText(bean.getRelationName());
        holder.time.setText(bean.getOverTime());
        if ("product".equals(bean.getUseType())){
            holder.label.setText("店铺优惠券");
        } else if ("all".equals(bean.getUseType())){
            holder.label.setText("全场优惠券");
        }
        if (index == position){
            holder.check.setImageDrawable(context.getDrawable(R.mipmap.btn_login_select_true));
        }
        holder.check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (price < bean.getMinPoint()){
                    DialogUtil.showWarnDialog(context,"该优惠券不符合条件");
                    return;
                }
                if (index != position){
                    int currentindex = index;
                    index = position;
                    notifyItemChanged(currentindex);
                    notifyItemChanged(index);
                    EventBus.getDefault().post(data.get(index));
                }
            }
        });
        holder.minPoint.setText("满" + bean.getMinPoint() + "可用");
        if (data.size() != 0 & isfirst){
            EventBus.getDefault().post(data.get(index));
            //ToastUtil.toast(context,"初始化了" + data.get(index).getPrice());
            isfirst = false;
        }
    }

    boolean isfirst = true;

    public DiscountTicketBean.DataBean getSelectedTicket(){
        if (data.size() == 0){
            return null;
        }
        return data.get(index);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void def(String str){}

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        ImageView check;

        TextView price;

        TextView label;

        TextView product_name;

        TextView minPoint;

        TextView time;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            check = itemView.findViewById(R.id.check);
            price = itemView.findViewById(R.id.price);
            label = itemView.findViewById(R.id.label);
            product_name = itemView.findViewById(R.id.product_name);
            minPoint = itemView.findViewById(R.id.minPoint);
            time = itemView.findViewById(R.id.time);
        }
    }

}
