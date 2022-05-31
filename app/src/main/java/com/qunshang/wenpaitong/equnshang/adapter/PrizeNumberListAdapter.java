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
import com.qunshang.wenpaitong.equnshang.data.PrizeNumberListBean;

public class PrizeNumberListAdapter extends RecyclerView.Adapter<PrizeNumberListAdapter.ViewHolder> {

    public List<PrizeNumberListBean.DataBean.NumberBean> data;

    public Context context;

    public LayoutInflater inflater;

    public PrizeNumberListAdapter(Context context,List<PrizeNumberListBean.DataBean.NumberBean> data){
        this.context = context;
        this.data = data;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.item_number_list,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.number.setText(data.get(position).getNumber());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        TextView number;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            number = itemView.findViewById(R.id.number);
        }
    }

}
