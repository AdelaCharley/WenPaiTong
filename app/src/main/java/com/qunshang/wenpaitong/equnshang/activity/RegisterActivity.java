package com.qunshang.wenpaitong.equnshang.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
;

import com.google.gson.Gson;
import com.kongzue.dialog.v3.TipDialog;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//import lombok.SneakyThrows;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.qunshang.wenpaitong.R;
import com.qunshang.wenpaitong.databinding.ActivityRegisterBinding;
import com.qunshang.wenpaitong.equnshang.data.BaseHttpBean;
import com.qunshang.wenpaitong.equnshang.data.UserInfoViewModel;
import com.qunshang.wenpaitong.equnshang.data.UserMsgBean;
import com.qunshang.wenpaitong.equnshang.data.VCodeBean;
import com.qunshang.wenpaitong.equnshang.model.ApiManager;
import com.qunshang.wenpaitong.equnshang.utils.DialogUtil;
import com.qunshang.wenpaitong.equnshang.utils.UserHelper;

/**
 * 登录Fragment
 * create by 何姝霖
 */
public class RegisterActivity extends BaseActivity implements View.OnClickListener{
    private final String TAG = "TEST_LoginFragment";
    private final int oneSecond = 1000;
    private final ApiManager apiManager = ApiManager.Companion.getInstance();
    private ActivityRegisterBinding binding;
    /*控件*/
    private Button btnRegister;
    private TextView tvGetVCode;

    /*接口的Query参数*/
    private String mPhone;
    private String mVCode;
    private String mBizId;

    private UserMsgBean.UserInfoBean userMsg;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.toolbar.toolbarBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        binding.toolbar.toolbarTitle.setText("");
        agreeProtocol(binding.tvProtocol,7,16, new Intent(this, UserProtocolActivity.class)); //用户协议
        agreeProtocol(binding.tvProtocol,17,26, new Intent(this, PrivacyProtocolActivity.class));//隐私协议
        btnRegister = binding.btnRegister;
        tvGetVCode = binding.tvGetVCode;


        btnRegister.setOnClickListener(this);
        tvGetVCode.setOnClickListener(this);
        binding.btnAgree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isChecked = !isChecked;
                if (isChecked){
                    binding.btnAgree.setImageDrawable(getDrawable(R.mipmap.btn_login_select_true));
                } else {
                    binding.btnAgree.setImageDrawable(getDrawable(R.mipmap.btn_login_select_false));
                }
            }
        });
    }


    /**
     *  给文本添加超链接,点击后跳转到指定页面
     * @param textView  带设置的文本控件
     * @param start1    开始位置（包括）
     * @param end1      结束位置（不包括）
     */
    private void agreeProtocol(@NotNull TextView textView, int start1, int end1, Intent intent) {
        CharSequence text = textView.getText();
        SpannableStringBuilder builder = new SpannableStringBuilder(text);

        ClickableSpan click = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                startActivity(intent);
            }

            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
                ds.setColor(getColor(R.color.blue));
            }
        };

        builder.setSpan(click, start1, end1,0);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        textView.setText(builder);
    }

    @Override
    public void onClick(View v) {
        mPhone = binding.etEnterPhone.getText().toString().trim();

        /*点击“注册”按钮，并判断是否完整正确填写该页信息*/
        if (btnRegister.equals(v) && isFillComplete()){

            mVCode = binding.etEnterVCode.getText().toString();
            apiManager.getApiLoginTest()
                    .getRegisterUserMsg(mPhone, mVCode, mBizId)
                    .enqueue(new retrofit2.Callback<BaseHttpBean>() {
                //@SneakyThrows
                @Override
                public void onResponse(@NotNull retrofit2.Call<BaseHttpBean> call, @NotNull retrofit2.Response<BaseHttpBean> response) {
                    BaseHttpBean user = response.body();
                    switch (Objects.requireNonNull(user).getCode()){
                        case 200:
                            Gson gson = new Gson();
                            String str = gson.toJson(user);
                            userMsg = gson.fromJson(str,UserMsgBean.class).data;
                            UserInfoViewModel.INSTANCE.setUserId(userMsg.getUid().toString());
                            UserInfoViewModel.INSTANCE.setUserInfo(userMsg);
                            UserHelper.setCurrentLoginUser(RegisterActivity.this,userMsg.getUid().toString());
                            DialogUtil.toast(RegisterActivity.this,"账号注册成功，稍候请前往登录页面进行登录");
                            Intent intent = new Intent(RegisterActivity.this,BindInviterActivity.class);
                            startActivity(intent);
                            //toNewFragment(new EnterRefereeFragment());
                            break;
                        case 201:
                            showWarnDialog("账号已注册，请直接登录");
                            //ToastUtil.toast(RegisterActivity.this,"账号已注册，请直接登录");
                            break;
                        case 202:
                            showErrorDialog("验证码错误，请重新获取");
                            //ToastUtil.toast(RegisterActivity.this,"验证码错误，请重新获取");
                            break;
                    }
                }
                @Override
                public void onFailure(@NotNull retrofit2.Call<BaseHttpBean> call, @NotNull Throwable t) {
                    Log.e(TAG, "onFailure: ", t);
                }
            });
        }

        /*点击“获取验证码“ ，发送验证码，间隔60s可再次点击*/
        else if (tvGetVCode.equals(v) && isRightPhone()){
            toGetVCode();
            //倒计时

            
        }
    }

    /**
     * 获取验证码成功后，开始倒计时
     */

    private void startCodeTimer(){
        new CountDownTimer(60 * oneSecond, oneSecond) {
            @Override
            public void onTick(long millisUntilFinished) {
                tvGetVCode.setClickable(false);
                tvGetVCode.setText(String.format(getResources().getString(R.string.hint_login_get_vCode), millisUntilFinished/oneSecond));
            }
            @Override
            public void onFinish() {
                tvGetVCode.setClickable(true);
                tvGetVCode.setText("获取验证码");
            }
        }.start();
    }

    private void toGetVCode(){

        apiManager.getApiLoginTestOld()
                .getRegisterVCode(mPhone)
                .enqueue(new Callback<ResponseBody>() {
                    //@SneakyThrows
                    @Override
                    public void onResponse(@NotNull retrofit2.Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                        try {
                            String jsonStr = response.body().string();
                            if (jsonStr.contains("\"BizId\":")) {
                                mBizId = new Gson().fromJson(jsonStr, VCodeBean.class).getBizId();
                            } else {
                                showErrorDialog("手机号不正确");
                                //ToastUtil.toast(RegisterActivity.this,"手机号不正确");
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        startCodeTimer();

                    }
                    @Override
                    public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                        Log.e(TAG, "onFailure: ", t);
                    }
                });

    }

    /**
     * 判断是否完整填写该页信息
     * 需填写信息：手机号、验证码、勾选阅读同意
     */
    private boolean isFillComplete() {
        boolean isFillComplete = false;
        mVCode = binding.etEnterVCode.getText().toString().trim();
        if (isRightPhone()){
            if (mVCode.isEmpty() ){
                showWarnDialog("请输入验证码");
                //ToastUtil.toast(RegisterActivity.this,"请输入验证码");
            }
            else if (!isChecked) {
                showWarnDialog("请阅读并同意页面协议");
                //ToastUtil.toast(RegisterActivity.this,"请阅读并同意页面协议");
            }
            else {
                isFillComplete = true;
            }
        }

        return isFillComplete;
    }

    boolean isChecked = false;

    public void showSuccessDialog(String str){
        TipDialog.show(this,str, TipDialog.TYPE.SUCCESS);
    }

    public void showErrorDialog(String str){
        TipDialog.show(this,str, TipDialog.TYPE.ERROR);
    }

    public void showWarnDialog(String str){
        TipDialog.show(this,str, TipDialog.TYPE.WARNING);
    }

    /**
     * 验证手机号是否正确
     * 手机号为空，提示：“请输入手机号”
     * 手机号不正确，提示：“手机号不正确”
     */
    private boolean isRightPhone() {
        if (mPhone.isEmpty()){
            showWarnDialog("请输入手机号");
            //ToastUtil.toast(RegisterActivity.this,"请输入手机号");
            return false;
        }

        String regexPhone = "^1[3-9]\\d{9}$";
        Pattern p=Pattern.compile(regexPhone);
        Matcher m=p.matcher(mPhone);

        if (!m.matches()) {
            showWarnDialog("手机号不正确");
            //ToastUtil.toast(RegisterActivity.this,"手机号不正确");
            return false;
        } else {
            return true;
        }
    }

}