package com.qunshang.wenpaitong.equnshang.adapter;

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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.kongzue.dialog.v3.CustomDialog;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.qunshang.wenpaitong.R;
import com.qunshang.wenpaitong.equnshang.Constants;
import com.qunshang.wenpaitong.equnshang.activity.DailyLotteryTicketActivity;
import com.qunshang.wenpaitong.equnshang.activity.PrizeInfoDetailActivity;
import com.qunshang.wenpaitong.equnshang.data.BaseHttpBean;
import com.qunshang.wenpaitong.equnshang.data.ToDayLotteryActivityBean;
import com.qunshang.wenpaitong.equnshang.data.UserInfoViewModel;
import com.qunshang.wenpaitong.equnshang.model.ApiManager;
import com.qunshang.wenpaitong.equnshang.utils.StringUtils;
import com.qunshang.wenpaitong.equnshang.utils.TimeUtil;
import com.qunshang.wenpaitong.equnshang.utils.DialogUtil;

public class ToDayLotteryActivityAdapter extends RecyclerView.Adapter<ToDayLotteryActivityAdapter.ViewHolder> {

    public List<ToDayLotteryActivityBean.DataBean> data;

    public DailyLotteryTicketActivity context;

    public LayoutInflater inflater;

    public ToDayLotteryActivityAdapter(DailyLotteryTicketActivity context,List<ToDayLotteryActivityBean.DataBean> data) {
        this.context = context;
        this.data = data;
        inflater = LayoutInflater.from(context.requireContext());
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.item_today_lottery_activity,parent,false));
    }

    public void goToDetail(int prizeId){
        Intent intent = new Intent(context.requireContext(), PrizeInfoDetailActivity.class);
        intent.putExtra("prizeId",prizeId);
        context.startActivity(intent);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ToDayLotteryActivityBean.DataBean bean = data.get(position);
        Glide.with(context).load(bean.getPrizeImageUrl()).into(holder.prize_image);
        holder.prize_name.setText(bean.getDescription());

        holder.prize_value.setText("奖品价值：￥" + bean.getPrizePrice());
        if (bean.getTags().size() > 0){
            holder.tags_one.setText(bean.getTags().get(0));
        }
        if (bean.getTags().size() > 1){
            holder.tags_two.setText(bean.getTags().get(1));
        }
        if (bean.getTags().size() > 2){
            holder.tags_three.setText(bean.getTags().get(2));
        }
        holder.prize_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToDetail(bean.getPrizeId());
            }
        });
        holder.prize_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToDetail(bean.getPrizeId());
            }
        });
        //如果活动已经结束
        if (isEnd(TimeUtil.Companion.getDateByString(bean.getEndTime()))){
            Log.i(Constants.Companion.getLogtag(),"po" + position);
            holder.parcount.setText(bean.getPeopleNumber() + "人中奖");
            holder.waittime.setVisibility(View.INVISIBLE);
            holder.label.setVisibility(View.INVISIBLE);
            holder.prizetime.setVisibility(View.VISIBLE);
            holder.prizetime.setText("开奖时间： " + getTime(bean.getEndTime()));

            LinearLayoutManager manager = new LinearLayoutManager(context.requireContext());
            manager.setOrientation(LinearLayoutManager.VERTICAL);
            holder.tickets.setLayoutManager(manager);

            if (bean.getStatus() == 3){//用户没有参与抽奖
                holder.prizeresult.setVisibility(View.INVISIBLE);
                holder.joinorgrey.setVisibility(View.VISIBLE);
                holder.joinorgrey.setText("未参与");
                holder.joinorgrey.setBackground(context.requireActivity().getDrawable(R.drawable.bg_grey_rectangle_4dp));
                //holder.ticketno.setVisibility(View.INVISIBLE);
            }
            if (bean.getStatus() == 2){//用户中奖
                holder.joinorgrey.setVisibility(View.INVISIBLE);
                holder.prizeresult.setVisibility(View.VISIBLE);
                holder.prizeresult.setText("恭 喜 您 中 奖");
                holder.tickets.setAdapter(new ToDayLotteryActivityTicketAdapter(context.requireContext(),bean.getParticipateNumberList()));
            }
            if (bean.getStatus() == 1){//用户中奖
                holder.joinorgrey.setVisibility(View.INVISIBLE);
                holder.prizeresult.setVisibility(View.VISIBLE);
                holder.prizeresult.setText("未中奖，就差一点点");
                //holder.ticketno.setText(bean.getParticipateNumberList().get(0).getNumber());
                holder.tickets.setAdapter(new ToDayLotteryActivityTicketAdapter(context.requireContext(),bean.getParticipateNumberList()));
            }
        } else {
            Log.i(Constants.Companion.getLogtag(),"pofjdkjfkd" + position);
            holder.parcount.setText(bean.getPeopleNumber() + "人参加");
            LinearLayoutManager manager = new LinearLayoutManager(context.requireContext());
            manager.setOrientation(LinearLayoutManager.VERTICAL);
            holder.tickets.setLayoutManager(manager);

            holder.waittime.setVisibility(View.VISIBLE);
            holder.label.setVisibility(View.VISIBLE);
            holder.prizetime.setVisibility(View.INVISIBLE);
            //holder.waittime.setText("1:00");

            Timer timer = new Timer();
            timers.add(timer);
            //TimerTask timertask = null;
            final TimerTask timertask = new TimerTask(){
                @Override
                public void run() {
                    try {
                        AppCompatActivity activity = (AppCompatActivity) context.requireActivity();
                        activity.runOnUiThread(() -> {
                            String remaintime = TimeUtil.Companion.getRemainTimeString(bean.getEndTime());
                            Log.i("当前的position是" + position,remaintime);
                            holder.waittime.setText(remaintime);
                            if ("00:00:00".equals(remaintime)){
                                timer.cancel();
                            }
                        });
                    } catch (Exception e){
                        timer.cancel();
                    }
                }
            };
            timer.schedule(timertask,1000,1000);

            if (bean.getStatus() == 3){
                holder.prizeresult.setVisibility(View.INVISIBLE);
                holder.joinorgrey.setVisibility(View.VISIBLE);
                holder.joinorgrey.setText("立即参与抽奖");
                holder.joinorgrey.setOnClickListener(v -> showJoinDialog(position));
                //holder.ticketno.setVisibility(View.GONE);
            }
            if (bean.getStatus() == 1){//用户中奖
                holder.joinorgrey.setVisibility(View.VISIBLE);
                holder.prizeresult.setVisibility(View.INVISIBLE);
                holder.joinorgrey.setText("继续参与抽奖");
                holder.joinorgrey.setOnClickListener(v -> showJoinDialog(position));
                //holder.ticketno.setText(bean.getParticipateNumberList().get(0).getNumber());
                holder.tickets.setAdapter(new ToDayLotteryActivityTicketAdapter(context.requireContext(),bean.getParticipateNumberList()));
            }
        }
    }


    public void finish() {
        //DialogUtil.toast(context,"啊毁了");
        for (int i = 0;i < timers.size();i++){
            try {
                timers.get(i).cancel();
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public List<Timer> timers = new ArrayList<>();

    public void showJoinDialog(int position){
        CustomDialog.show((AppCompatActivity) context.requireActivity(), R.layout.layout_choose_ticket_goactivity, new CustomDialog.OnBindView() {
                    @Override
                    public void onBind(CustomDialog dialog, View view) {
                        TextView cancel = view.findViewById(R.id.cancel);
                        cancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.doDismiss();
                            }
                        });

                        ImageView add = view.findViewById(R.id.add);
                        ImageView cut = view.findViewById(R.id.cut);
                        TextView count = view.findViewById(R.id.count);
                        TextView confirm = view.findViewById(R.id.confirm);

                        TextView ticketremain = view.findViewById(R.id.ticket_remain);
                        ticketremain.setText("您还剩余" + DailyLotteryTicketActivity.Companion.getCount() + "张抽奖券");
                        if (DailyLotteryTicketActivity.Companion.getCount() == 0){
                            count.setText(String.valueOf(DailyLotteryTicketActivity.Companion.getCount()));
                        } else {
                            count.setText("1");
                        }



                        add.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (DailyLotteryTicketActivity.Companion.getCount() > Integer.parseInt(count.getText().toString())){
                                    count.setText(String.valueOf(Integer.parseInt(count.getText().toString()) + 1));
                                }
                            }
                        });
                        cut.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (Integer.parseInt(count.getText().toString()) > 1){
                                    if (DailyLotteryTicketActivity.Companion.getCount() > 0){
                                        count.setText(String.valueOf(Integer.parseInt(count.getText().toString()) - 1));
                                    }
                                }
                            }
                        });
                        confirm.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (
                                        Integer.parseInt(count.getText().toString()) != 0
                                ){
                                    ApiManager.Companion.getInstance().getApiLotteryTest().
                                            betBumber(UserInfoViewModel.INSTANCE.getUserId(),
                                                    String.valueOf(data.get(position).getActivityId()),count.getText().toString()).enqueue(new Callback<BaseHttpBean<Integer>>() {
                                        @Override
                                        public void onResponse(Call<BaseHttpBean<Integer>> call, Response<BaseHttpBean<Integer>> response) {
                                            dialog.doDismiss();
                                            if (response.body() != null){
                                                Gson gson = new Gson();
                                                String s = gson.toJson(response.body());
                                                StringUtils.log(s);
                                                //DialogUtil.toast(context,response.body().getMsg());
                                                if (response.body().getCode() == 200){
                                                    DailyLotteryTicketActivity activity = context;
                                                    activity.loadTickets();
                                                    activity.loadLotteryActivity();
                                                } else {
                                                    DialogUtil.toast(context.requireContext(),response.body().getMsg());
                                                }
                                            } else {
                                                DialogUtil.toast(context.requireContext(),"奖券数量不对");
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<BaseHttpBean<Integer>> call, Throwable t) {

                                        }
                                    });
                                } else {
                                    DialogUtil.toast(context.requireContext(),"剩余奖券不足");
                                }
                            }
                        });
                    }
                }).setCancelable(true);
    }

    //endtime中有一个字符串,根据空格将其分割,获取除掉日期之外的时间
    public String getTime(String endTime){
        String[] s = endTime.split(" ");
        if (s.length == 2){
            return s[1];
        }
        return "";
    }

    //判断该活动是否已经结束
    public boolean isEnd(Date date){
        //Date date1 = new Date();
        return System.currentTimeMillis() > date.getTime();
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        TextView prizetime;

        TextView waittime;

        TextView parcount;

        ImageView prize_image;

        TextView prize_name;

        TextView joinorgrey;

        TextView prize_value;

        TextView tags_one;

        TextView tags_two;

        TextView tags_three;

        TextView label;

        RecyclerView tickets;

        TextView prizeresult;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            prizetime = itemView.findViewById(R.id.prizetime);
            waittime = itemView.findViewById(R.id.waittime);
            parcount = itemView.findViewById(R.id.parcount);
            prize_image = itemView.findViewById(R.id.prize_image);
            prize_name = itemView.findViewById(R.id.prize_name);
            joinorgrey = itemView.findViewById(R.id.joinorgrey);
            prize_value = itemView.findViewById(R.id.prize_value);
            tags_one = itemView.findViewById(R.id.tags_one);
            tags_two = itemView.findViewById(R.id.tags_two);
            tags_three = itemView.findViewById(R.id.tags_three);
            label = itemView.findViewById(R.id.label);
            tickets = itemView.findViewById(R.id.tickets);
            prizeresult = itemView.findViewById(R.id.prizeresult);
        }
    }

}
