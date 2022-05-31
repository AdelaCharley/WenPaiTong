package com.qunshang.wenpaitong.equnshang.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.qunshang.wenpaitong.R;
import com.qunshang.wenpaitong.equnshang.activity.BaseActivity;
import com.qunshang.wenpaitong.equnshang.data.VideoType;
import com.qunshang.wenpaitong.databinding.FragmentLotteryBinding;
import com.qunshang.wenpaitong.equnshang.adapter.BasePagerAdapter;
import com.qunshang.wenpaitong.equnshang.model.ApiManager;
import com.qunshang.wenpaitong.equnshang.utils.TimeUtil;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * 抽奖页面
 * create by 何姝霖
 */
public class LotteryFragment extends MyBaseFragment {

    private FragmentLotteryBinding binding;

    ArrayList<BaseVideoFragment> fragments = new ArrayList<>();

    BasePagerAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentLotteryBinding.inflate(inflater);
        return binding.getRoot();
    }

    public static int activityId = -9;

    public static String msg = "";

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        ApiManager.Companion.getInstance().getApiLotteryTest().getVIPDayActivityId().enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.body() == null){
                    return;
                }
                try {
                    String jsonstr = response.body().string();
                    JSONObject object = new JSONObject(jsonstr);
                    msg = object.getString("msg");
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
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    private void initView(){
        BaseActivity activity = (BaseActivity) requireActivity();
        activity.setSystemBarColor(R.color.black);
        activity.changeToDefaultButTranslucent();
        fragments.add(new LastLotteryVideoFragment(VideoType.Companion.getTYPE_LASTLOTTERY()));
        fragments.add(new ToDayLotteryVideoFragment(VideoType.Companion.getTYPE_NOWDAYLOTTERY()));
        fragments.add(new WeekLotteryVideoFragment(VideoType.Companion.getTYPE_WEEKLOTTERY()));
        fragments.add(new VipdayLotteryVideoFragment(VideoType.Companion.getTYPE_VIPDAY()));
        ArrayList<String> titles = new ArrayList<String>();
        titles.add("往期抽奖");
        titles.add("今日抽奖");
        titles.add("每周任务");
        titles.add("会员日");
                //String[]{"往期抽奖 ", "今日抽奖 ","每周任务","会员日"};
        adapter = new BasePagerAdapter(getChildFragmentManager(), fragments, titles);
        binding.vpLottery.setOffscreenPageLimit(4);
        binding.vpLottery.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                fragments.get(curPos).pause();
                curPos = position;
                if (fragments.get(position).getIsDataLoaded()){
                    fragments.get(position).start();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        binding.vpLottery.setAdapter(adapter);

        binding.tablayout.setupWithViewPager(binding.vpLottery);
        if (TimeUtil.Companion.getDayOfWeek() == 4){
            binding.vpLottery.setCurrentItem(3);
        } else {
            binding.vpLottery.setCurrentItem(1);
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        if (isHidden()){
            fragments.get(binding.vpLottery.getCurrentItem()).pause();
        } else {
            BaseActivity activity = (BaseActivity) requireActivity();
            activity.setSystemBarColor(R.color.black);
            activity.changeToDefaultButTranslucent();
            fragments.get(binding.vpLottery.getCurrentItem()).start();
        }
    }


    public static int curPos = 0;

}