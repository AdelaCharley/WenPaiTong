package com.qunshang.wenpaitong.equnshang.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.qunshang.wenpaitong.R;
import com.qunshang.wenpaitong.equnshang.data.ProductBean;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

public class SpecsDialogAdapter extends RecyclerView.Adapter<SpecsDialogAdapter.ViewHolder> {

    public List<ProductBean.DataBean.SkuList> data;

    public Context context;

    public LayoutInflater inflater;

    int currentIndex = 0;

    public SpecsDialogAdapter(Context context,List<ProductBean.DataBean.SkuList> data){
        this.context = context;
        this.data = data;
        inflater = LayoutInflater.from(context);
        if (!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.item_specs_dialogs_adapter,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.text.setText(data.get(position).getValue());
        holder.text.setOnClickListener(v -> {
            if (position == currentIndex){
                return;
            }
            EventBus.getDefault().post(data.get(position));
            currentIndex = position;
            notifyDataSetChanged();
        });
        if (currentIndex == position){
            holder.text.setTextColor(context.getColor(R.color.white));
            holder.text.setBackground(context.getDrawable(R.drawable.background_button_line_blue));
        } else {
            holder.text.setTextColor(context.getColor(R.color.black));
            holder.text.setBackground(context.getDrawable(R.drawable.background_button_line_grey));
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void def(Integer integer){

    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        TextView text;

        public ViewHolder(@NonNull View view) {
            super(view);
            text = view.findViewById(R.id.text);
        }
    }

}
