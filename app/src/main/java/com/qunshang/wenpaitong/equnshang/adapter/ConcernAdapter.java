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

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;
import com.qunshang.wenpaitong.R;
import com.qunshang.wenpaitong.equnshang.activity.PersonalHomepageActivity;
import com.qunshang.wenpaitong.equnshang.activity.V300PublisherMainHomeActivity;
import com.qunshang.wenpaitong.equnshang.data.BaseHttpBean;
import com.qunshang.wenpaitong.equnshang.data.ConcernData;
import com.qunshang.wenpaitong.equnshang.data.DoConcernBean;
import com.qunshang.wenpaitong.equnshang.data.NewConcernBean;
import com.qunshang.wenpaitong.equnshang.data.UserInfoViewModel;
import com.qunshang.wenpaitong.equnshang.data.def.FollowChangeBean;
import com.qunshang.wenpaitong.equnshang.model.ApiManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.qunshang.wenpaitong.equnshang.utils.StringUtils;

public class ConcernAdapter extends RecyclerView.Adapter<ConcernAdapter.ViewHolder> {

    public List<NewConcernBean.DataBean> data = new ArrayList<NewConcernBean.DataBean>();

    public Context context;

    public LayoutInflater inflater;

    public ConcernAdapter(Context context){
        this.context = context;
        this.data = data;
        inflater = LayoutInflater.from(context);
        if (!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void update(FollowChangeBean bean) {

        if (data.size() == 0){
            return;
        }
        int pos = -1;
        for (int i = 0;i < data.size();i++){
            if (data.get(i).getAgencyId() == bean.getId()){
                pos = i;
            }
        }
        if (pos == -1){
            return;
        }
        if (bean.isFollow()) {
            data.get(pos).setStatus(1);
        } else {
            data.get(pos).setStatus(0);
        }
        notifyDataSetChanged();
    }

    public void add(List<NewConcernBean.DataBean> data) {
        this.data.addAll(data);
        StringUtils.log("一共有" + data.size() + "个" + "item");
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.item_concern,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        NewConcernBean.DataBean dataBean = data.get(position);
        Glide.with(context).load(dataBean.getAgencyAvatar()).into(holder.imageView);
        holder.username.setText(dataBean.getAgencyName());
        holder.time.setText(dataBean.getAgencyDescription());
        if (dataBean.getStatus() == 0){
            holder.button.setText("已关注");
        } else {
            holder.button.setText("关注");
        }

        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ApiManager.Companion.getManager().getApiWenBanTong_ZhangJun().concernCollegeOrUnConcern(UserInfoViewModel.INSTANCE.getUserId(), dataBean.getAgencyId())
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<BaseHttpBean<String>>() {
                            @Override
                            public void accept(BaseHttpBean<String> stringBaseHttpBean) throws Throwable {
                                if (stringBaseHttpBean != null){
                                    if (stringBaseHttpBean.getCode() == 200){
                                        FollowChangeBean followChangeBean = new FollowChangeBean();
                                        followChangeBean.setId(dataBean.getAgencyId());
                                        if ("1".equals(stringBaseHttpBean.getData())){
                                            holder.button.setText("已关注");
                                            followChangeBean.setFollow(false);
                                        } else {
                                            holder.button.setText("关注");
                                            followChangeBean.setFollow(true);
                                        }
                                        EventBus.getDefault().post(followChangeBean);
                                    }
                                }
                            }
                        });
            }
        });
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =
                        new Intent(context, V300PublisherMainHomeActivity.class);
                intent.putExtra("agencyId", dataBean.getAgencyId());
                context.startActivity(intent);
            }
        });
        /*ConcernData.DataBean dataBean = data.get(position);
        Glide.with(context).load(dataBean.getHeadimage_url()).into(holder.imageView);
        holder.username.setText(dataBean.getUname());
        holder.time.setText(dataBean.getCreate_time());
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PersonalHomepageActivity.class);
                intent.putExtra("userId",String.valueOf(dataBean.getFollowId()));
                context.startActivity(intent);
            }
        });
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ApiManager.Companion.getInstance().getApiPersonalTest().loadDoConcern(UserInfoViewModel.INSTANCE.getUserId(), String.valueOf(dataBean.getFollowId()))
                .enqueue(new Callback<DoConcernBean>() {
                    @Override
                    public void onResponse(Call<DoConcernBean> call, Response<DoConcernBean> response) {
                        if (response != null){
                            DoConcernBean body = response.body();
                            boolean is = body.isData();
                            if (!is){
                                holder.button.setText("取消关注");
                            } else {
                                holder.button.setText("关注");
                            }
                            FollowChangeBean followChangeBean = new FollowChangeBean();
                            followChangeBean.setId(dataBean.getFollowId());
                            followChangeBean.setFollow(is);
                            EventBus.getDefault().post(followChangeBean);
                        }
                    }

                    @Override
                    public void onFailure(Call<DoConcernBean> call, Throwable t) {

                    }
                });
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        ImageView imageView;

        TextView username;

        TextView time;

        TextView button;

        View layout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageview_concern);
            username = itemView.findViewById(R.id.name_concern);
            time = itemView.findViewById(R.id.time_concern);
            layout = itemView.findViewById(R.id.layout);
            button = itemView.findViewById(R.id.button_concern);
        }
    }
}
