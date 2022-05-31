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
import com.qunshang.wenpaitong.equnshang.data.AMallQuestionBean;

public class CommonQuestionAdapter extends RecyclerView.Adapter<CommonQuestionAdapter.ViewHolder> {

    public List<AMallQuestionBean.DataBean> data;

    public Context context;

    public LayoutInflater inflater;

    public CommonQuestionAdapter(Context context,List<AMallQuestionBean.DataBean> data){
        this.context = context;
        this.data = data;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.item_commonquestion,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AMallQuestionBean.DataBean bean = this.data.get(position);
        holder.child.setText(bean.getTitle());
        holder.childs.setAdapter(new CommonChildQuestionAdapter(context,bean.getChildren()));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        TextView child;

        RecyclerView childs;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            child = itemView.findViewById(R.id.questionname);
            childs = itemView.findViewById(R.id.childquestions);
        }
    }

}
