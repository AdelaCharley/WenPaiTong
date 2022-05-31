package com.qunshang.wenpaitong.equnshang.activity;

import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.View;

import com.bumptech.glide.Glide;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;
import com.qunshang.wenpaitong.R;
import com.qunshang.wenpaitong.databinding.ActivityV300PublisherMainHomeBinding;
import com.qunshang.wenpaitong.equnshang.adapter.BasePagerAdapter;
import com.qunshang.wenpaitong.equnshang.data.BaseHttpBean;
import com.qunshang.wenpaitong.equnshang.data.UserInfoViewModel;
import com.qunshang.wenpaitong.equnshang.data.def.FollowChangeBean;
import com.qunshang.wenpaitong.equnshang.fragment.CollegeListFragment;
import com.qunshang.wenpaitong.equnshang.fragment.PublishFragment;
import com.qunshang.wenpaitong.equnshang.model.ApiManager;
import com.qunshang.wenpaitong.equnshang.utils.DialogUtil;
import com.qunshang.wenpaitong.equnshang.utils.StringUtils;

public class V300PublisherMainHomeActivity extends BaseActivity {

    ActivityV300PublisherMainHomeBinding binding;

    int agencyId = 0;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void update(FollowChangeBean bean) {

        if (bean.isFollow()) {
            binding.yiguanzhu.setText("关注");
            binding.yiguanzhu.setBackground(getResources().getDrawable(R.drawable.bg_v300_publisher_guanzhu));
            binding.yiguanzhu.setTextColor(getResources().getColor(R.color.white));   //未关注
        } else {
            binding.yiguanzhu.setText("已关注");
            binding.yiguanzhu.setBackground(getResources().getDrawable(R.drawable.bg_v300_publisher_guanzhu_grey));
            binding.yiguanzhu.setTextColor(getResources().getColor(R.color.grey));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this);
        }
        binding = ActivityV300PublisherMainHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.toolbarBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        agencyId = getIntent().getIntExtra("agencyId",0);
        StringUtils.log("zhege shi " + agencyId);
        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(new CollegeListFragment(agencyId,true));
        fragments.add(new PublishFragment(String.valueOf(agencyId)));
        ArrayList<String> titles = new ArrayList<>();
                titles.add("文章");
                titles.add("视频");
        BasePagerAdapter pagerAdapter = new BasePagerAdapter(getSupportFragmentManager(), fragments, titles);
        binding.viewpager.setAdapter(pagerAdapter);
        binding.tablayout.setupWithViewPager(binding.viewpager);
        binding.yiguanzhu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                concern();
            }
        });
        loadPublisherCenter();
    }

    public void loadPublisherCenter(){
        ApiManager.Companion.getManager().getApiWenBanTong_ZhangJun().loadPublisherCenter(UserInfoViewModel.INSTANCE.getUserId(), agencyId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(publisherCenterBeanBaseHttpBean -> {
                    if (publisherCenterBeanBaseHttpBean != null){
                        if (publisherCenterBeanBaseHttpBean.getCode() == 200){
                            Glide.with(V300PublisherMainHomeActivity.this).load(publisherCenterBeanBaseHttpBean.getData().getAgencyAvatar()).into(binding.userimage);
                            binding.username.setText(publisherCenterBeanBaseHttpBean.getData().getAgencyName());
                            binding.jianjie.setText(publisherCenterBeanBaseHttpBean.getData().getAgencyDescription());
                            binding.guanzhuresnhu.setText(String.valueOf(publisherCenterBeanBaseHttpBean.getData().getFanNum()));
                            binding.tiezishu.setText(String.valueOf(publisherCenterBeanBaseHttpBean.getData().getNewsNum()));
                            binding.huozan.setText(String.valueOf(publisherCenterBeanBaseHttpBean.getData().getTotalUpNum()));
                            if (publisherCenterBeanBaseHttpBean.getData().isFocus() == 1){
                                binding.yiguanzhu.setText("已关注");
                                binding.yiguanzhu.setBackground(getResources().getDrawable(R.drawable.bg_v300_publisher_guanzhu_grey));
                                binding.yiguanzhu.setTextColor(getResources().getColor(R.color.grey));
                            } else {
                                binding.yiguanzhu.setText("关注");
                                binding.yiguanzhu.setBackground(getResources().getDrawable(R.drawable.bg_v300_publisher_guanzhu));
                                binding.yiguanzhu.setTextColor(getResources().getColor(R.color.white));
                            }
                        }
                    }
                });
    }
    public void concern(){
        ApiManager.Companion.getManager().getApiWenBanTong_ZhangJun().concernCollegeOrUnConcern(UserInfoViewModel.INSTANCE.getUserId(), agencyId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<BaseHttpBean<String>>() {
                    @Override
                    public void accept(BaseHttpBean<String> stringBaseHttpBean) throws Throwable {
                        if (stringBaseHttpBean != null){
                            if (stringBaseHttpBean.getCode() == 200){
                                FollowChangeBean followChangeBean = new FollowChangeBean();
                                followChangeBean.setId(agencyId);
                                if ("1".equals(stringBaseHttpBean.getData())){
                                    binding.yiguanzhu.setText("已关注");
                                    DialogUtil.toast(V300PublisherMainHomeActivity.this,"关注成功");
                                    binding.yiguanzhu.setBackground(getResources().getDrawable(R.drawable.bg_v300_publisher_guanzhu_grey));
                                    binding.yiguanzhu.setTextColor(getResources().getColor(R.color.grey));
                                    followChangeBean.setFollow(false);
                                } else {
                                    binding.yiguanzhu.setText("关注");
                                    DialogUtil.toast(V300PublisherMainHomeActivity.this,"取消关注");
                                    binding.yiguanzhu.setBackground(getResources().getDrawable(R.drawable.bg_v300_publisher_guanzhu));
                                    binding.yiguanzhu.setTextColor(getResources().getColor(R.color.white));
                                    followChangeBean.setFollow(true);
                                }
                                EventBus.getDefault().post(followChangeBean);
                            }
                        }
                    }
                });
    }
}