package com.qunshang.wenpaitong.equnshang.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.HashSet;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.qunshang.wenpaitong.R;
import com.qunshang.wenpaitong.equnshang.Constants;
import com.qunshang.wenpaitong.equnshang.activity.AMallV3ProductDetailActivity;
import com.qunshang.wenpaitong.equnshang.activity.BrowseHistoryActivityV2;
import com.qunshang.wenpaitong.equnshang.data.AMallV3ProductHistoryBean;
import com.qunshang.wenpaitong.equnshang.model.ApiManager;

public class BrowseHistoryAdapterV2 extends RecyclerView.Adapter<BrowseHistoryAdapterV2.ViewHolder> {

    public List<AMallV3ProductHistoryBean.DataBean> data;

    public Context context;

    public LayoutInflater inflater;

    public static final int TYPE_VIP = 0;

    public static final int TYPE_PRODUCT = 1;

    HashSet<Integer> set;

    public BrowseHistoryAdapterV2(Context context,List<AMallV3ProductHistoryBean.DataBean> data){
        this.context = context;
        this.data = data;
        Log.i(Constants.Companion.getLogtag(),"一共有" + data.size());
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public BrowseHistoryAdapterV2.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.item_browse_history_productv2,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull BrowseHistoryAdapterV2.ViewHolder holder, int position) {
        AMallV3ProductHistoryBean.DataBean bean = data.get(position);
        //AMallV3ProductStarBean.DataBean bean = data.get(position);
        Glide.with(context).load(bean.getProductPosterUrl()).into(holder.image);
        holder.name.setText(bean.getProductName());
        holder.price.setText("" + bean.getVipGroupPrice());
        holder.originalprice.setText("￥" + bean.getMarketPrice());
        holder.amall_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AMallV3ProductDetailActivity.class);
                intent.putExtra("productId",bean.getProductId());
                context.startActivity(intent);
            }
        });
        holder.sale.setText("已售 " + bean.getSale());
        holder.status.setText(bean.getTag());
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ApiManager.Companion.getInstance().getApiAMallV3().deleteMyHistory(String.valueOf(bean.getViewId())).enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                               if (response.body() == null){
                                   return;
                               }
                                                            BrowseHistoryActivityV2 activityV2 = (BrowseHistoryActivityV2) context;
                                                                                                                        ((BrowseHistoryActivityV2) context).loadData();
                                                                                                                                 }

                                                                                                                                 @Override
                                                                                                                                 public void onFailure(Call<ResponseBody> call, Throwable t) {

                                                                                                                                 }
                                                                                                                             });
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        ImageView image;

        TextView name;

        TextView price;

        TextView originalprice;

        ViewGroup amall_layout;

        TextView sale;

        TextView delete;

        TextView status;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            amall_layout = itemView.findViewById(R.id.layout);
            image = itemView.findViewById(R.id.image);
            name = itemView.findViewById(R.id.name);
            price = itemView.findViewById(R.id.price);
            status = itemView.findViewById(R.id.status);
            sale = itemView.findViewById(R.id.sale);
            originalprice = itemView.findViewById(R.id.originalprice);
            delete = itemView.findViewById(R.id.delete);
        }
    }

}
