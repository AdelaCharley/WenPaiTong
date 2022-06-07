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

import com.bumptech.glide.Glide;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.qunshang.wenpaitong.R;
/*import com.qunshang.wenpaitong.equnshang.activity.AfterSaleDetailActivity;
import com.qunshang.wenpaitong.equnshang.activity.OrderDetailActivityV2;*/
import com.qunshang.wenpaitong.equnshang.activity.PinTuanDetailActivity;
import com.qunshang.wenpaitong.equnshang.data.AMallV3MessageBean;
import com.qunshang.wenpaitong.equnshang.data.BaseHttpBean;
import com.qunshang.wenpaitong.equnshang.data.UserInfoViewModel;
import com.qunshang.wenpaitong.equnshang.model.ApiManager;
import com.qunshang.wenpaitong.equnshang.utils.StringUtils;

public class AMallV3MessageAdapter extends RecyclerView.Adapter<AMallV3MessageAdapter.ViewHolder> {

    public List<AMallV3MessageBean.DataBean> data = new ArrayList<>();

    public Context context;

    public LayoutInflater inflater;

    public void add(List<AMallV3MessageBean.DataBean> data) {
        this.data.addAll(data);
        StringUtils.log("一共有" + data.size() + "个" + "item");
        notifyDataSetChanged();
    }

    public void remove() {
        this.data.removeAll(data);

    }

    public AMallV3MessageAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void def(String s) {
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.item_amallv3_message, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AMallV3MessageBean.DataBean bean = data.get(position);
        holder.title.setText(bean.getTitle());
        holder.date.setText(bean.getCreateTime());
        holder.content.setText(bean.getContext());
        Glide.with(context).load(bean.getSkuPic()).into(holder.image);
        if (bean.getIsCheck() == 0) {//未读
            holder.red.setVisibility(View.VISIBLE);
        } else {
            holder.red.setVisibility(View.GONE);
        }
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ApiManager.Companion.getInstance().getApiAMallV3().readMessage(UserInfoViewModel.INSTANCE.getUserId(), bean.getOrderNoticeId()).enqueue(new Callback<BaseHttpBean<String>>() {
                    @Override
                    public void onResponse(Call<BaseHttpBean<String>> call, Response<BaseHttpBean<String>> response) {
                        if (response.body() == null) {
                            return;
                        }
                        if (response.body().getCode() == 200) {
                            bean.setIsCheck(1);
                            notifyItemChanged(position);
                            EventBus.getDefault().post("messagerefresh");
                        }
                    }

                    @Override
                    public void onFailure(Call<BaseHttpBean<String>> call, Throwable t) {

                    }
                });
                switch (bean.getJumpType()) {
                    case "order":
                        /*Intent intent = new Intent(context, OrderDetailActivityV2.class);
                        intent.putExtra("id", data.get(position).getOrderId());
                        context.startActivity(intent);*/
                        break;
                    case "group":
                        Intent intent1 = new Intent(context, PinTuanDetailActivity.class);
                        intent1.putExtra("orderGroupSn", bean.getOrderGroupSn());
                        context.startActivity(intent1);
                        break;
                    case "afterSale":
                        /*Intent intent2 = new Intent(context, AfterSaleDetailActivity.class);
                        intent2.putExtra("afterSaleSn", bean.getAfterSaleSn());
                        context.startActivity(intent2);*/
                        break;
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView title;

        TextView date;

        ImageView image;

        TextView content;

        View layout;

        View red;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            date = itemView.findViewById(R.id.date);
            image = itemView.findViewById(R.id.image);
            content = itemView.findViewById(R.id.content);
            layout = itemView.findViewById(R.id.layout);
            red = itemView.findViewById(R.id.circle_red);
        }
    }

}
