package com.qunshang.wenpaitong.equnshang.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;
import com.qunshang.wenpaitong.R;
import com.qunshang.wenpaitong.equnshang.data.AMallV3CategoryFirstBean;
import com.qunshang.wenpaitong.equnshang.fragment.AMallV3CategoryFragment;

public class AMallV3CategoryFirstAdapter extends RecyclerView.Adapter<AMallV3CategoryFirstAdapter.ViewHolder> {

    public List<AMallV3CategoryFirstBean.DataBean> data;

    public Context context;

    public LayoutInflater inflater;

    int currentIndex = 0;

    public AMallV3CategoryFirstAdapter(Context context,List<AMallV3CategoryFirstBean.DataBean> data){
        this.context = context;
        this.data = data;
        inflater = LayoutInflater.from(context);
        if (!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this);
        }
    }

    public void unbind(){
        if (EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().unregister(this);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void def(String str){}

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.item_amallv3_category_first, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AMallV3CategoryFirstBean.DataBean bean = data.get(position);
        if (currentIndex == position){
            holder.categoryname.setText(bean.getName());
            holder.label.setVisibility(View.VISIBLE);
            holder.layout.setBackground(context.getDrawable(R.drawable.bg_amallv3_firstcategory_layout));
            AMallV3CategoryFragment.CategoryData refreshdata = new AMallV3CategoryFragment.CategoryData();
            refreshdata.setCode(300);
            holder.categoryname.setTypeface(null, Typeface.BOLD);
            refreshdata.setIndex(currentIndex);
            EventBus.getDefault().post(refreshdata);
        } else {
            holder.categoryname.setText(bean.getName());
            holder.label.setVisibility(View.GONE);
            holder.categoryname.setTypeface(null,Typeface.NORMAL);
            holder.layout.setBackgroundColor(context.getColor(R.color.bg_grey));
        }
        holder.layout.setOnClickListener(v -> {
            if (position != currentIndex){
                int oldpos = currentIndex;
                currentIndex = position;
                notifyItemChanged(oldpos);
                notifyItemChanged(currentIndex);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView categoryname;

        View label;

        View layout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryname = itemView.findViewById(R.id.categoryname);
            label = itemView.findViewById(R.id.label);
            layout = itemView.findViewById(R.id.layout);
        }
    }

}
