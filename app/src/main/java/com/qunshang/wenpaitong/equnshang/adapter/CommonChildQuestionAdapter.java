package com.qunshang.wenpaitong.equnshang.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import com.qunshang.wenpaitong.R;
import com.qunshang.wenpaitong.equnshang.activity.QuestionDetailActivity;
import com.qunshang.wenpaitong.equnshang.data.AMallQuestionBean;

public class CommonChildQuestionAdapter extends RecyclerView.Adapter<CommonChildQuestionAdapter.ViewHolder> {

    public List<AMallQuestionBean.DataBean.QuestionChildrenBean> data;

    public Context context;

    public LayoutInflater inflater;

    public CommonChildQuestionAdapter(Context context,List<AMallQuestionBean.DataBean.QuestionChildrenBean> data){
        this.context = context;
        this.data = data;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.item_common_child_question,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AMallQuestionBean.DataBean.QuestionChildrenBean bean = this.data.get(position);
        holder.child.setText(bean.getTitle());
        if (position == data.size() - 1){
            holder.line.setVisibility(View.GONE);
        }
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, QuestionDetailActivity.class);
                intent.putExtra("title",bean.getTitle());
                intent.putExtra("content",bean.getContent());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (this.data == null){
            return 0;
        }
        return data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        TextView child;

        View line;

        ViewGroup layout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            child = itemView.findViewById(R.id.childquestions_content);
            line = itemView.findViewById(R.id.line);
            layout = itemView.findViewById(R.id.layout);
        }
    }

}
