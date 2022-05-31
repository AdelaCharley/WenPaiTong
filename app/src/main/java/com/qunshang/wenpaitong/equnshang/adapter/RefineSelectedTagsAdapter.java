package com.qunshang.wenpaitong.equnshang.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.qunshang.wenpaitong.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

public class RefineSelectedTagsAdapter extends RecyclerView.Adapter<RefineSelectedTagsAdapter.ViewHolder> {

    public List<String> tags;

    public Context context;

    public LayoutInflater inflater;

    public RefineSelectedTagsAdapter(Context context,List<String> data){
        this.context = context;
        this.tags = data;
        inflater = LayoutInflater.from(context);
        if (!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.item_selected_tags,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tag.setText(tags.get(position));
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tags.remove(position);
                notifyDataSetChanged();
            }
        });
    }

    public String getSelectedTags(){
        StringBuilder tagstr = new StringBuilder();
        for (int i = 0; i< tags.size();i++){
            tagstr.append(tags.get(i));
            if (!(i == tags.size() -1)){
                tagstr.append(",");
            }
        }
        return tagstr.toString();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void add(String tag){
        if (tags.size() == 3){
            return;
        }
        if (!isRepeat(tag)){
            tags.add(tag);
            notifyDataSetChanged();
        }
    }

    public boolean isRepeat(String tag){
        if (tags.size() == 0){
            return false;
        }
        for (int i = 0;i < tags.size();i++){
            String j = tags.get(i);
            if (j.equals(tag)){
                return true;
            }
        }
        return false;
    }

    @Override
    public int getItemCount() {
        return Math.min(tags.size(), 3);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        TextView tag;

        ViewGroup layout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tag = itemView.findViewById(R.id.tag);
            layout = itemView.findViewById(R.id.layout);
        }
    }

}