package com.qunshang.wenpaitong.equnshang.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lxj.xpopup.XPopup;

import java.util.List;

import com.qunshang.wenpaitong.R;
import com.qunshang.wenpaitong.equnshang.data.ProductBeanV2;
import com.qunshang.wenpaitong.equnshang.view.ServiceDialog;

public class AMallV3ServiceLabelAdapter extends RecyclerView.Adapter<AMallV3ServiceLabelAdapter.ViewHolder> {

    public List<ProductBeanV2.DataBean.Service> data;

    public Context context;

    public LayoutInflater inflater;

    public AMallV3ServiceLabelAdapter(Context context,List<ProductBeanV2.DataBean.Service> data){
        this.context = context;
        this.data = data;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.item_amallv3_label_service,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.content.setText(data.get(position).getName());
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new XPopup.Builder(context)
                        .moveUpToKeyboard(false) //如果不加这个，评论弹窗会移动到软键盘上面
                        .enableDrag(true)
                        .isDestroyOnDismiss(true) //对于只使用一次的弹窗，推荐设置这个
                        //                        .isThreeDrag(true) //是否开启三阶拖拽，如果设置enableDrag(false)则无效
                        .asCustom(new ServiceDialog(context,data))
                        .show();
            }
        });
    }

    @Override
    public int getItemCount() {
        if (data.size() > 3){
            return 3;
        } else {
            return data.size();
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        TextView content;

        View layout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            layout = itemView.findViewById(R.id.layout);
            content = itemView.findViewById(R.id.content);
        }
    }

}
