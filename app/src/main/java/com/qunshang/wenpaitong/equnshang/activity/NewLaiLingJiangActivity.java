package com.qunshang.wenpaitong.equnshang.activity;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.graphics.Typeface;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;

import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.qunshang.wenpaitong.databinding.ActivityNewLaiLingJiangBinding;
import com.qunshang.wenpaitong.equnshang.adapter.BasePagerAdapter;
import com.qunshang.wenpaitong.equnshang.interfaces.OnClickDetailListener;
import com.qunshang.wenpaitong.equnshang.model.ApiManager;
import com.qunshang.wenpaitong.equnshang.utils.KTUtil;
import com.qunshang.wenpaitong.equnshang.utils.StringUtils;
import com.qunshang.wenpaitong.equnshang.utils.TimeUtil;

public class NewLaiLingJiangActivity extends BaseActivity {

    ActivityNewLaiLingJiangBinding binding;

    public static int activityId = -9;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNewLaiLingJiangBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.top.toolbarBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        binding.top.toolbarTitle.setText("来领奖");

        ApiManager.Companion.getInstance().getApiLotteryTest().getVIPDayActivityId().enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.body() == null){
                    return;
                }
                try {
                    String jsonstr = response.body().string();
                    JSONObject object = new JSONObject(jsonstr);
                    //msg = object.getString("msg");
                    switch (object.getInt("code")){
                        case 200:
                            activityId = object.getInt("data")
                            ;
                            break;
                        case 201:
                            activityId = object.getInt("data");
                            break;
                        case 203:
                            if (TimeUtil.Companion.getDayOfWeek() >=1 & TimeUtil.Companion.getDayOfWeek() <= 3){
                                activityId = object.getJSONObject("data").getInt("nextId");
                            }
                            if (TimeUtil.Companion.getDayOfWeek() >= 4 & TimeUtil.Companion.getDayOfWeek() <= 7){
                                activityId = object.getJSONObject("data").getInt("pastId");
                            }
                            break;
                    }
                    ArrayList<Fragment> fragments = new ArrayList<Fragment>();
                    DailyLotteryTicketActivity frg = new DailyLotteryTicketActivity();
                    StringUtils.log("分解掉看就" + frg.isAdded());
                    fragments.add(frg);
                    fragments.add(new WeekActivitysActivity());
                    fragments.add(new VipDayActivity(activityId));
                    ArrayList<String> titles = new ArrayList<String>();
                    titles.add("每日抽奖");
                    titles.add("每周任务");
                    titles.add("会员日");
                    BasePagerAdapter apagerAdapter = new BasePagerAdapter(getSupportFragmentManager(), fragments, titles);
                    binding.viewpager.setAdapter(apagerAdapter);
                    binding.viewpager.setOffscreenPageLimit(3);
                    binding.tablayout.setupWithViewPager(binding.viewpager);

                    binding.viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                        @Override
                        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                        }

                        @Override
                        public void onPageSelected(int position) {
                            switch (position){
                                case 0:
                                    showMeiri();
                                    break;
                                case 1:
                                    showMeiZhou();
                                    break;
                                case 2:
                                    showHuiYuan();
                                    break;
                            }
                        }

                        @Override
                        public void onPageScrollStateChanged(int state) {

                        }
                    });
                    if (TimeUtil.Companion.getDayOfWeek() == 4){
                        binding.viewpager.setCurrentItem(2);
                        binding.viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                            @Override
                            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                            }

                            @Override
                            public void onPageSelected(int position) {

                                if (position == 0){
                                    KTUtil.Companion.showGoVipDayDialog(NewLaiLingJiangActivity.this, new OnClickDetailListener() {
                                        @Override
                                        public void onClick() {
                                            binding.viewpager.setCurrentItem(2);
                                        }
                                    });
                                }
                            }

                            @Override
                            public void onPageScrollStateChanged(int state) {

                            }
                        });
                    }
                    binding.meierichoujiang.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showMeiZhou();
                            binding.viewpager.setCurrentItem(0);
                        }
                    });
                    binding.meizhourenwu.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showMeiZhou();
                            binding.viewpager.setCurrentItem(1);
                        }
                    });
                    binding.huiyuanri.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showHuiYuan();
                            binding.viewpager.setCurrentItem(2);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    public void showMeiri(){
        binding.meierichoujiang.setTextSize(TypedValue.COMPLEX_UNIT_DIP,18);
        binding.meierichoujiang.setTypeface(Typeface.DEFAULT_BOLD);
        binding.meizhourenwu.setTextSize(TypedValue.COMPLEX_UNIT_DIP,14);
        binding.meizhourenwu.setTypeface(Typeface.DEFAULT);
        binding.huiyuanri.setTextSize(TypedValue.COMPLEX_UNIT_DIP,14);
        binding.huiyuanri.setTypeface(Typeface.DEFAULT);
    }

    public void showMeiZhou(){
        binding.meizhourenwu.setTextSize(TypedValue.COMPLEX_UNIT_DIP,18);
        binding.meizhourenwu.setTypeface(Typeface.DEFAULT_BOLD);
        binding.meierichoujiang.setTextSize(TypedValue.COMPLEX_UNIT_DIP,14);
        binding.meierichoujiang.setTypeface(Typeface.DEFAULT);
        binding.huiyuanri.setTextSize(TypedValue.COMPLEX_UNIT_DIP,14);
        binding.huiyuanri.setTypeface(Typeface.DEFAULT);
    }

    public void showHuiYuan(){
        binding.huiyuanri.setTextSize(TypedValue.COMPLEX_UNIT_DIP,18);
        binding.huiyuanri.setTypeface(Typeface.DEFAULT_BOLD);
        binding.meizhourenwu.setTextSize(TypedValue.COMPLEX_UNIT_DIP,14);
        binding.meizhourenwu.setTypeface(Typeface.DEFAULT);
        binding.meierichoujiang.setTextSize(TypedValue.COMPLEX_UNIT_DIP,14);
        binding.meierichoujiang.setTypeface(Typeface.DEFAULT);
    }

}