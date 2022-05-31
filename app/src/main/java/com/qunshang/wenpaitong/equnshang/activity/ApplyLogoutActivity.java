package com.qunshang.wenpaitong.equnshang.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.kongzue.dialog.v3.WaitDialog;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.qunshang.wenpaitong.databinding.ActivityApplyLogoutBinding;
import com.qunshang.wenpaitong.equnshang.data.BaseHttpBean;
import com.qunshang.wenpaitong.equnshang.data.UserInfoViewModel;
import com.qunshang.wenpaitong.equnshang.model.ApiManager;

public class ApplyLogoutActivity extends BaseActivity {

    ActivityApplyLogoutBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityApplyLogoutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.title.toolbarBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        binding.title.toolbarTitle.setText("申请注销账号");
        binding.logoutnow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                WaitDialog.show(ApplyLogoutActivity.this, "正在载入...");
                ApiManager.Companion.getInstance().getApiPersonalTest().submitLogOutApply(UserInfoViewModel.INSTANCE.getUserId()).enqueue(new Callback<BaseHttpBean<String>>() {
                    @Override
                    public void onResponse(Call<BaseHttpBean<String>> call, Response<BaseHttpBean<String>> response) {
                        WaitDialog.dismiss();
                        if (response.body() == null){
                            return;
                        }
                        if (response.body().getCode() == 200){
                            Intent intent = new Intent(ApplyLogoutActivity.this,ApplyLogoutingActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }

                    @Override
                    public void onFailure(Call<BaseHttpBean<String>> call, Throwable t) {

                    }
                });
                /*Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                WaitDialog.dismiss();
                                UserHelper.setCurrentLogoutingStatus(ApplyLogoutActivity.this,true);
                                Intent intent = new Intent(ApplyLogoutActivity.this,ApplyLogoutingActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        });
                    }
                });

                thread.start();*/

            }
        });
    }
}