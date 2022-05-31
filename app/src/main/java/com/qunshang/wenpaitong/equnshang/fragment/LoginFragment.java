package com.qunshang.wenpaitong.equnshang.fragment;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
;
import androidx.appcompat.app.AppCompatActivity;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.InputFilter;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.qunshang.wenpaitong.equnshang.activity.BaseActivity;
import com.qunshang.wenpaitong.equnshang.activity.BindInviterActivity;
import com.qunshang.wenpaitong.equnshang.activity.LoginWithPasswordActivity;
import com.qunshang.wenpaitong.equnshang.activity.RegisterActivity;
import com.qunshang.wenpaitong.equnshang.activity.PrivacyProtocolActivity;
import com.qunshang.wenpaitong.equnshang.data.UserMsgBean;
import com.qunshang.wenpaitong.equnshang.activity.UserProtocolActivity;
import com.qunshang.wenpaitong.equnshang.data.VCodeBean;
import com.qunshang.wenpaitong.equnshang.data.UserInfoViewModel;
import com.qunshang.wenpaitong.R;
import com.qunshang.wenpaitong.databinding.FragmentLoginBinding;
import com.qunshang.wenpaitong.equnshang.data.BaseHttpBean;
import com.qunshang.wenpaitong.equnshang.model.ApiManager;
import com.qunshang.wenpaitong.equnshang.utils.DialogUtil;
import com.qunshang.wenpaitong.equnshang.utils.StringUtils;
import com.qunshang.wenpaitong.equnshang.utils.UserHelper;
import com.google.gson.Gson;
import com.kongzue.dialog.v3.TipDialog;
import com.kongzue.dialog.v3.WaitDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 登录Fragment
 * create by 何姝霖
 */

public class LoginFragment extends MyBaseFragment implements View.OnClickListener{

    private final int oneSecond = 1000;

    private final ApiManager apiManager = ApiManager.Companion.getInstance();

    private FragmentLoginBinding binding;

    private Button btnLogin;
    private Button btnRegister;
    private TextView tvGetVCode;

    /*接口的Query参数*/
    private String mPhone;
    private String mVCode;
    private String mBizId;

    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this);
        }
    }

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        BaseActivity activity = (BaseActivity) requireActivity();
        activity.setSystemBarColor(R.color.white);
        binding = FragmentLoginBinding.inflate(inflater,container,false);

        agreeProtocol(binding.tvProtocol,7,13, new Intent(getContext(), UserProtocolActivity.class)); //用户协议
        agreeProtocol(binding.tvProtocol,14,20, new Intent(getContext(), PrivacyProtocolActivity.class));//隐私协议

        btnLogin = binding.btnLogin;
        btnRegister = binding.btnRegister;
        tvGetVCode = binding.tvGetVCode;

        btnLogin.setOnClickListener(this);
        btnRegister.setOnClickListener(this);
        tvGetVCode.setOnClickListener(this);
        binding.btnAgree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isChecked = !isChecked;
                if (isChecked){
                    binding.btnAgree.setImageDrawable(getContext().getDrawable(R.mipmap.btn_login_select_true));
                } else {
                    binding.btnAgree.setImageDrawable(getContext().getDrawable(R.mipmap.btn_login_select_false));
                }
            }
        });
        binding.imageMimaDenglu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), LoginWithPasswordActivity.class);
                startActivity(intent);
            }
        });
        binding.etEnterPhone.addTextChangedListener(new MyWatcher());
        binding.etEnterVCode.addTextChangedListener(new MyWatcher());
        InputFilter filter1 = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                // 判断是否输入空格
                String str = "1234567890";
                if (str.contains(source)){
                    return source;
                }
                return "";
            }
        };
        InputFilter filters[] = new InputFilter[]{filter1};
        InputFilter filter2 = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                /*String str = "1234567890";
                if (str.contains(source)){
                    return source;
                }
                return "";*/
                String regexPhone = "^[0-9]+$";
                Pattern p=Pattern.compile(regexPhone);
                Matcher m=p.matcher(source);

                if (!m.matches()) {
                    //util.setShortToast("手机号不正确");
                    return "";
                } else {
                    return source;
                }
                // 判断是否输入空格
                /*String regexPhone = "[a-zA-Z0-9]{6,12}";
                Pattern p=Pattern.compile(regexPhone);
                Matcher m=p.matcher(mPhone);

                if (!m.matches()) {
                    //util.setShortToast("手机号不正确");
                    return "";
                } else {
                    return source;
                }*/
            }
        };
        InputFilter filters2[] = new InputFilter[]{filter2};
        binding.etEnterPhone.setFilters(filters);
        binding.etEnterVCode.setFilters(filters2);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        /*binding.etEnterPhone.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                binding.etEnterPhone.requestFocus();
            }
        });*/
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
                getContext().startActivity(intent);
            }

            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
                ds.setColor(requireActivity().getColor(R.color.blue));

            }
        };
        builder.setSpan(click, start1, end1,0);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        textView.setText(builder);
    }


    @Override
    public void onClick(View v) {
        mPhone = binding.etEnterPhone.getText().toString().trim();

        if (btnLogin.equals(v) && isFillComplete()) {
            toLogin();
        }

        else if (btnRegister.equals(v)){
            getContext().startActivity(new Intent(getContext(), RegisterActivity.class));
        }

        else if (tvGetVCode.equals(v) && isRightPhone()){
            toGetVCode();
        }
    }
    

    /**
     * 登录，并返回用户信息
     */
    private void toLogin() {
        WaitDialog.show((AppCompatActivity) requireActivity(),"正在登录").show();
        mVCode = binding.etEnterVCode.getText().toString();
        apiManager.getApiLoginTest()
                .getLoginUserMsg(mPhone, mVCode, mBizId,null)
                .enqueue(new Callback<BaseHttpBean>() {
            //@SneakyThrows
            @Override
            public void onResponse(@NotNull Call<BaseHttpBean> call, @NotNull Response<BaseHttpBean> response) {
                WaitDialog.dismiss();
                if (response.body() == null){
                    return;
                }
                BaseHttpBean user = response.body();
                switch (user.getCode()){
                    case 200:
                        Gson gson = new Gson();
                        String str = gson.toJson(user);
                        UserMsgBean.UserInfoBean userdata = gson.fromJson(str,UserMsgBean.class).data;
                        UserInfoViewModel.INSTANCE.setUserInfo(userdata);
                        UserInfoViewModel.INSTANCE.setUserId(userdata.getUid().toString());
                        UserHelper.setCurrentLoginUser(getContext(),UserInfoViewModel.INSTANCE.getUserId());
                        //util.setShortToast("登录成功");
                        showSuccessDialog("登录成功");
                        if (timer != null){
                            timer.cancel();
                        }
                        if (userdata.getIsExist() == 0){
                            Intent intent = new Intent(getContext(), BindInviterActivity.class);
                            startActivity(intent);
                        } else {
                            loginSuccess();
                            /*Intent intent = new Intent(getContext(), MainActivity.class);
                                    //.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);*/
                        }
                        /*getParentFragmentManager().beginTransaction().replace(R.id.container, new PersonalFragment()).commit();*/

                        break;
                    case 201:
                        showWarnDialog("未注册，请注册后再登录");
                        //util.setShortToast("未注册，请注册后再登录");
                        break;
                    case 202:
                        showWarnDialog("验证码错误，请确认后输入");
                        //util.setShortToast("验证码错误，请确认后输入");
                        break;
                }
            }
            @Override
            public void onFailure(retrofit2.Call<BaseHttpBean> call, Throwable t) {

            }
        });
    }

    void loginSuccess(){
        EventBus.getDefault().post("loginsuccess");
        requireActivity().finish();
    }

    /**
     * 获取验证码
     */
    private void toGetVCode(){
        if (!isRightPhone()){
            DialogUtil.showWarnDialog(requireActivity(),"请输入正确的手机号");
            return;
        }
        apiManager.getApiLoginTestOld()
                //原本是getApiLoginTestOld
                .getLoginVCode(mPhone)
                .enqueue(new Callback<ResponseBody>() {
                    //@SneakyThrows
                    @Override
                    public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                        if (response.body() == null){
                            return;
                        }
                        String jsonStr = null;
                        try {
                            jsonStr = response.body().string();
                            if (jsonStr.contains("\"BizId\":")) {
                                mBizId = new Gson().fromJson(jsonStr, VCodeBean.class).getBizId();
                            } else {
                                //showWarnDialog("手机号不正确");
                                //util.setShortToast("手机号不正确");
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        startTimer();

                    }
                    @Override
                    public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {

                    }
                });
    }

    CountDownTimer timer;

    @Override
    public void onDetach() {
        super.onDetach();
        if (timer != null){
            timer.cancel();
        }
    }

    public void startTimer(){
        timer = new CountDownTimer(60 * oneSecond, oneSecond) {
            @Override
            public void onTick(long millisUntilFinished) {
                try {
                    tvGetVCode.setClickable(false);
                    tvGetVCode.setText(String.format(getResources().getString(R.string.hint_login_get_vCode), millisUntilFinished/oneSecond));
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
            @Override
            public void onFinish() {
                try {
                    tvGetVCode.setClickable(true);
                    tvGetVCode.setText("获取验证码");
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        };
        timer.start();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLoginSuccess(String str){
        if ("loginsuccess".equals(str)) {
            requireActivity().finish();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        try {
            timer.cancel();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 判断是否完整填写该页信息
     * 需填写信息：手机号、验证码、勾选阅读同意
     */
    private boolean isFillComplete() {
        boolean isFillComplete = false;
        mVCode = binding.etEnterVCode.getText().toString().trim();
        /*if (isRightPhone()){

        }*/
        if (mVCode.isEmpty() ){
            showWarnDialog("请输入验证码");
            //util.setShortToast("请输入验证码");
        }
        else if (!isChecked) {
            showWarnDialog("请阅读并同意页面协议");
            //util.setShortToast("请阅读并同意页面协议");
        }
        else {
            isFillComplete = true;
        }

        return isFillComplete;
    }

    boolean isChecked = false;

    public void showSuccessDialog(String str){
        TipDialog.show((AppCompatActivity) requireActivity(),str, TipDialog.TYPE.SUCCESS);
    }

    public void showWarnDialog(String str){
        TipDialog.show((AppCompatActivity) requireActivity(),str, TipDialog.TYPE.WARNING);
    }

    /**
     * 验证手机号是否正确
     * 手机号为空，提示：“请输入手机号”
     * 手机号不正确，提示：“手机号不正确”
     */
    private boolean isRightPhone() {
        if (mPhone.isEmpty()){
            showWarnDialog("请输入手机号");
            //util.setShortToast("请输入手机号");
            return false;
        }

        String regexPhone = "^1[3-9]\\d{9}$";
        Pattern p=Pattern.compile(regexPhone);
        Matcher m=p.matcher(mPhone);

        if (!m.matches()) {
            showWarnDialog("手机号不正确");
            //util.setShortToast("手机号不正确");
            return false;
        } else {
            return true;
        }
    }

    private boolean isRightCode(){
        if (StringUtils.isEmpty(mTCode)){
            return false;
        }
        if (mTCode.length() == 6){
            return true;
        }
        return false;
    }

    private boolean isPhoneRight() {//这个是不会弹框的
        if (mPhone.isEmpty()){
            return false;
        }
        String regexPhone = "^1[3-9]\\d{9}$";
        Pattern p=Pattern.compile(regexPhone);
        Matcher m=p.matcher(mPhone);

        if (!m.matches()) {
            return false;
        } else {
            return true;
        }
    }

    String mTCode;

    public class MyWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            mPhone = binding.etEnterPhone.getText().toString().trim();
            mTCode = binding.etEnterVCode.getText().toString().trim();
            if (isPhoneRight() && isRightCode()){
                binding.btnLogin.setClickable(true);
                binding.btnLogin.setBackground(requireActivity().getDrawable(R.drawable.btn_login_bg_rectangle_blue));
            } else {
                binding.btnLogin.setClickable(false);
                binding.btnLogin.setBackground(requireActivity().getDrawable(R.drawable.btn_login_bg_rectangle_greyblue));
            }
        }
    }

}