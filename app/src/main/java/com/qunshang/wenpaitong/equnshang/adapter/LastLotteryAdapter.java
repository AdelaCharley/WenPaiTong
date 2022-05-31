package com.qunshang.wenpaitong.equnshang.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.youth.banner.Banner;
import com.youth.banner.adapter.BannerImageAdapter;
import com.youth.banner.holder.BannerImageHolder;
import com.youth.banner.indicator.CircleIndicator;

import com.qunshang.wenpaitong.R;
import com.qunshang.wenpaitong.equnshang.activity.LastLotteryActivity;
import com.qunshang.wenpaitong.equnshang.activity.WinInfoActivity;
import com.qunshang.wenpaitong.equnshang.data.LastLotteryResultBean;
import com.qunshang.wenpaitong.equnshang.data.SwipeDataBean;
import com.qunshang.wenpaitong.equnshang.utils.DialogUtil;
import com.qunshang.wenpaitong.equnshang.utils.StringUtils;
import com.qunshang.wenpaitong.equnshang.utils.TimeUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

//这个地方的TimerTask存在严重缺陷，当activity退出的时候，timertask依然运行，并且recyclerview中不可视的view也在刷新。
public class LastLotteryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public List<LastLotteryResultBean.DataBean> data = new ArrayList<>();

    public Context context;

    public LayoutInflater inflater;

    SwipeDataBean bean;

    public LastLotteryAdapter(Context context){
        this.context = context;
        //this.data = data;
        inflater = LayoutInflater.from(context);
    }

    public void add(List<LastLotteryResultBean.DataBean> datas){
        if (datas == null){
            return;
        }
        this.data = datas;
        notifyDataSetChanged();
    }

    public void addHeader(SwipeDataBean bean){
        this.bean = bean;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == 0){
            return new BannerViewHolder(inflater.inflate(R.layout.item_last_lottery_banner,parent,false));
        }
        return new LastLotteryViewHolder(inflater.inflate(R.layout.item_last_lottery_result,parent,false));
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0){
            return 0;
        } else {
            return 1;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        if (holder instanceof BannerViewHolder){
            if (bean == null){
                return;
            }
            Log.i("zhangjunii",bean.getData().toString());
            BannerImageAdapter<SwipeDataBean.DataBean> adapter = new BannerImageAdapter<SwipeDataBean.DataBean>(bean.getData()) {
                @Override
                public void onBindView(BannerImageHolder holder, SwipeDataBean.DataBean data, int position, int size) {
                    Glide.with(context)
                    .load(data.getImageUrl())
                    //.apply(RequestOptions.bitmapTransform(RoundedCorners(30)))
                    .into(holder.imageView);
                }
            };
            ((BannerViewHolder) holder).banner
                    .setAdapter(adapter)
                    .addBannerLifecycleObserver((LifecycleOwner) context)
                    .setIndicator(new CircleIndicator(context));
        }

        else if (holder instanceof LastLotteryViewHolder){
            LastLotteryResultBean.DataBean bean = data.get(position - 1);
            Glide.with(context).load(bean.getImageUrl()).into(((LastLotteryViewHolder) holder).url);
            ((LastLotteryViewHolder) holder).name.setText(bean.getTitle());
            StringUtils.log("当前的status是" + bean.getStatus());
            String[] time = bean.getTime().split(" ");
            if (time.length == 2){
                ((LastLotteryViewHolder) holder).time.setText(time[0]);
            }
            ((LastLotteryViewHolder) holder).count.setText(bean.getParticipants() + "人参加");
            ((LastLotteryViewHolder) holder).status.setImageDrawable(context.getResources().getDrawable(getDrawable(bean.getStatus())));//1，未中奖 4 已失效 3已中奖 2待领取
            if (bean.getStatus() == 1) {
                ((LastLotteryViewHolder) holder).seedetail.setVisibility(View.INVISIBLE);
            }
            else if (bean.getStatus() == 4) {
                ((LastLotteryViewHolder) holder).seedetail.setVisibility(View.INVISIBLE);
            }
            else if (bean.getStatus() == 2) {
                //StringUtils.log("当亲pos是" + position + "fjdk22222");
                ((LastLotteryViewHolder) holder).seedetail.setVisibility(View.VISIBLE);
                ((LastLotteryViewHolder) holder).time.setText("剩余领取时间");
                ((LastLotteryViewHolder) holder).seedetail.setText("填  写  中  奖  信  息");
                ((LastLotteryViewHolder) holder).seedetail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    /*Intent intent = new Intent(context, SupplementActivity.class);
                    intent.putExtra("data",bean);
                    context.startActivity(intent);*/
                        Intent intent = new Intent(context, WinInfoActivity.class);
                        intent.putExtra("activityId",bean.getActivityId());
                        context.startActivity(intent);
                    }
                });
                ((LastLotteryViewHolder) holder).count.setText("");
                Timer timer = new Timer();
                timers.add(timer);
                //TimerTask timertask = null;
                final TimerTask timertask = new TimerTask(){
                    @Override
                    public void run() {
                        AppCompatActivity activity = (AppCompatActivity) context;
                        activity.runOnUiThread(() -> {
                            if (LastLotteryActivity.Companion.isEnd()){
                                return;
                            }
                            //String remaintime = TimeUtil.Companion.getRemainTimeString(bean.getTime());
                            String remaintime = TimeUtil.Companion.getLastLotteryRemainTIme(bean.getTime());
                            Log.i("当前的position是" + position,remaintime);
                            ((LastLotteryViewHolder) holder).count.setText(remaintime);
                            if ("00:00:00".equals(remaintime)){
                                timer.cancel();
                            }
                        });
                    }
                };
                timer.schedule(timertask,1000,1000);
            }
            else if (bean.getStatus() == 3){
                ((LastLotteryViewHolder) holder).seedetail.setText("查 看 中 奖 信 息");
                ((LastLotteryViewHolder) holder).seedetail.setVisibility(View.VISIBLE);
                ((LastLotteryViewHolder) holder).seedetail.setOnClickListener(v -> {
                    Intent intent = new Intent(context, WinInfoActivity.class);
                    intent.putExtra("activityId",bean.getActivityId());
                    context.startActivity(intent);
                });
            } else {
                ((LastLotteryViewHolder) holder).seedetail.setVisibility(View.INVISIBLE);
            }
        }
    }

    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        for (int i = 0;i < timers.size();i++){
            try {
                timers.get(i).cancel();
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public List<Timer> timers = new ArrayList<>();

    public int getDrawable(int status){
        if (status == 1){
            return R.mipmap.weizhongjiang;
        } else if (status == 2){
            return R.mipmap.dailingqu;
        } else if (status == 4){
            return R.mipmap.yishixiao;
        } else if (status == 3){
            return R.mipmap.yizhongjiang;
        }
        else {
            return R.mipmap.weizhongjiang;
        }
    }

    @Override
    public int getItemCount() {
        return data.size() + 1;
    }

    public static class BannerViewHolder extends RecyclerView.ViewHolder {

        Banner<SwipeDataBean.DataBean, BannerImageAdapter<SwipeDataBean.DataBean>> banner;

        public BannerViewHolder(@NonNull View itemView) {
            super(itemView);
            banner = itemView.findViewById(R.id.banner);
        }
    }

    public static class LastLotteryViewHolder extends RecyclerView.ViewHolder{

        ImageView url;

        TextView name;

        TextView time;

        TextView count;

        ImageView status;

        TextView seedetail;

        public LastLotteryViewHolder(@NonNull View itemView) {
            super(itemView);
            url = itemView.findViewById(R.id.last_lottery_image_url);
            name = itemView.findViewById(R.id.lottery_product_name);
            time = itemView.findViewById(R.id.last_lottery_time);
            count = itemView.findViewById(R.id.parians);
            status = itemView.findViewById(R.id.status);
            seedetail = itemView.findViewById(R.id.seelotterydetail);
        }
    }

}


