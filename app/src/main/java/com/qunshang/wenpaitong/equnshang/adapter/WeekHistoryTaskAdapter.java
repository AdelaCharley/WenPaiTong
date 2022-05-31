package com.qunshang.wenpaitong.equnshang.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.kongzue.dialog.v3.CustomDialog;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.qunshang.wenpaitong.R;
import com.qunshang.wenpaitong.equnshang.activity.WeekHistoryActivity;
import com.qunshang.wenpaitong.equnshang.activity.WeekPrizeHistoryActivity;
import com.qunshang.wenpaitong.equnshang.data.HelpListBean;
import com.qunshang.wenpaitong.equnshang.data.WeekHistoryTaskBean;
import com.qunshang.wenpaitong.equnshang.model.ApiManager;
import com.qunshang.wenpaitong.equnshang.utils.TimeUtil;

public class WeekHistoryTaskAdapter extends RecyclerView.Adapter<WeekHistoryTaskAdapter.ViewHolder> {

    public List<WeekHistoryTaskBean.DataBean> data;

    public Context context;

    public LayoutInflater inflater;

    public WeekHistoryTaskAdapter(Context context,List<WeekHistoryTaskBean.DataBean> data) {
        this.context = context;
        this.data = data;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.item_week_task,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        WeekHistoryTaskBean.DataBean bean = this.data.get(position);
        Glide.with(context).load(bean.getPrizeImgSrc()).into(holder.image);
        holder.name.setText(bean.getPrizeName());
        holder.helpnum.setText(bean.getHelpNum() + "人为我助力");
        holder.seedetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showHelpList(bean.getTaskUserRelationId());
            }
        });
        if (bean.getStatus() == 3){
            holder.status.setImageDrawable(context.getDrawable(R.mipmap.history_yishixiao));//已失效，但应该是红色的
            holder.time.setText("剩余领取时间:");
            holder.remaintime.setVisibility(View.VISIBLE);
            holder.helpnum.setVisibility(View.GONE);
            Date endTime = TimeUtil.Companion.getDateByString(bean.getTaskTime());
            if (endTime.getTime() < System.currentTimeMillis()){
                holder.remaintime.setText("00:00:00");
            } else {
                Timer timer = new Timer();
                TimerTask timerTask = new TimerTask() {
                    @Override
                    public void run() {
                        AppCompatActivity activity = (AppCompatActivity) context;
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (WeekHistoryActivity.Companion.isEnd()){
                                    try {
                                        timer.cancel();
                                    } catch (Exception e){
                                        e.printStackTrace();
                                    }
                                    return;
                                }
                                holder.remaintime.setText(TimeUtil.Companion.getTimeRemainByDayString(bean.getTaskTime()));
                            }
                        });
                    }
                };
                timer.schedule(timerTask,1000,1000);
            }
        } else if (bean.getStatus() == 4){
            holder.status.setImageDrawable(context.getDrawable(R.mipmap.history_weiwancheng));//应该是未完成
            holder.remaintime.setVisibility(View.GONE);
            String[] splittime = bean.getTaskTime().split(" ");

            if (splittime.length > 1){
                holder.time.setText(splittime[0]);
            }

        } else if (bean.getStatus() == 2){
            holder.status.setImageDrawable(context.getDrawable(R.mipmap.history_yiwancheng));//这里应该是已完成
            String[] splittime = bean.getTaskTime().split(" ");

            if (splittime.length > 1){
                holder.time.setText(splittime[0]);
            }
            holder.remaintime.setVisibility(View.GONE);
            holder.helpnum.setVisibility(View.GONE);
            holder.seedetail.setText("查看详情");
            holder.seedetail.setOnClickListener(v -> {
                Intent intent = new Intent(context, WeekPrizeHistoryActivity.class);
                intent.putExtra("taskUserRelationId",bean.getTaskUserRelationId());
                context.startActivity(intent);
            });
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void showHelpList(String relationId){
        CustomDialog.show((AppCompatActivity) context, R.layout.dialog_help, new CustomDialog.OnBindView() {
                    @Override
                    public void onBind(CustomDialog dialog, View v) {
                        ApiManager.Companion.getManager().getApiLotteryTest().loadHistoryHelpList(relationId).enqueue(new Callback<HelpListBean>() {
                            @Override
                            public void onResponse(Call<HelpListBean> call, Response<HelpListBean> response){
                                if (response.body() == null){
                                    return;
                                }
                                TextView cancel = v.findViewById(R.id.cancel);
                                cancel.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialog.doDismiss();
                                    }
                                });
                                View line_top = v.findViewById(R.id.line_top);
                                if (response
                                .body().getData().size() > 0){
                                    line_top.setVisibility(View.GONE);
                                }
                                RecyclerView list = v.findViewById(R.id.list);
                                list.setAdapter(new HelpListAdapter(context,response.body().getData()));
                            }

                            @Override
                            public void onFailure(Call<HelpListBean> call, Throwable t) {

                            }
                        });
                    }
                }



                /*object : CustomDialog.OnBindView{
            override fun onBind(dialog: CustomDialog?, v: View?) {
                val image = v!!.findViewById<ImageView>(R.id.image)
                        val close = v.findViewById<ImageView>(R.id.close)
                        close.setOnClickListener {
                    dialog?.doDismiss()
                }
                image.setOnClickListener {
                    ApiManager.getInstance().getApiVideoTest().receiveTicket(UserInfoViewModel.getUserId()).enqueue(object : Callback<BaseHttpBean<Int>>{
                        override fun onResponse(
                                call: Call<BaseHttpBean<Int>>,
                        response: Response<BaseHttpBean<Int>>
                        ) {
                            if (response.body() == null){
                                return
                            }
                            dialog?.doDismiss()
                            ToastUtil.toast(context,response.body()!!.msg)
                            if (response.body()!!.code == 200){
                                val intent = Intent(context,DailyLotteryTicketActivity::class.java)
                                startActivity(intent)
                                receive_ticket.visibility = View.GONE
                            }
                        }

                        override fun onFailure(call: Call<BaseHttpBean<Int>>, t: Throwable) {

                        }

                    })
                }

            }

        }*/).setCancelable(true);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        ImageView image;

        TextView name;

        ImageView status;

        TextView time;

        TextView helpnum;

        TextView seedetail;

        TextView remaintime;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.image = itemView.findViewById(R.id.image);
            this.name = itemView.findViewById(R.id.videoname);
            status = itemView.findViewById(R.id.status);
            time = itemView.findViewById(R.id.time);
            helpnum = itemView.findViewById(R.id.helpnum);
            seedetail = itemView.findViewById(R.id.seelist);
            remaintime = itemView.findViewById(R.id.remaintime);
        }
    }

}
