package com.qunshang.wenpaitong.equnshang.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import com.qunshang.wenpaitong.R;
import com.qunshang.wenpaitong.equnshang.activity.AMallSearchResultV3Activity;
import com.qunshang.wenpaitong.equnshang.activity.AMallV3SearchActivity;

public class AMallV3SearchHistoryAdapter extends RecyclerView.Adapter<AMallV3SearchHistoryAdapter.ViewHolder> {

    public List<String> data;

    public Context context;

    public LayoutInflater inflater;

    public AMallV3SearchHistoryAdapter(Context context,List<String> data){
        this.context = context;
        this.data = data;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.item_search_amallv3_history,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.text.setText(data.get(position));
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AMallSearchResultV3Activity.class);
                intent.putExtra("keyword",data.get(position));
                context.startActivity(intent);
            }
        });
        holder.close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AMallV3SearchActivity activity = (AMallV3SearchActivity) context;
                activity.deleteOneThenUpdate(data.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView text;

        ImageView close;

        View layout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            text = itemView.findViewById(R.id.text);
            close = itemView.findViewById(R.id.close);
            layout = itemView.findViewById(R.id.layout);
        }
    }

}
