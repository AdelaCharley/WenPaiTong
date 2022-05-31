package com.qunshang.wenpaitong.equnshang.view;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import com.qunshang.wenpaitong.R;
import com.qunshang.wenpaitong.equnshang.Constants;
import com.qunshang.wenpaitong.equnshang.activity.AccountSettingActivity;
import com.qunshang.wenpaitong.equnshang.activity.AddressActivity;
import com.qunshang.wenpaitong.equnshang.activity.AddressActivityV2;
import com.qunshang.wenpaitong.equnshang.activity.ManageActivity;
import com.qunshang.wenpaitong.equnshang.activity.OrderActivity;
import com.qunshang.wenpaitong.equnshang.activity.OrderActivityV2;
import com.qunshang.wenpaitong.equnshang.activity.RenMaiActivity;
import com.qunshang.wenpaitong.equnshang.data.UserInfoViewModel;

import com.lxj.xpopup.core.AttachPopupView;
public class MyAttachPopupView extends AttachPopupView {

    public MyAttachPopupView(@NonNull Context context) {
        super(context);
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.item_pop_add;
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        ViewGroup layout_order = findViewById(R.id.layout_order);
        ViewGroup layout_video = findViewById(R.id.layout_video);
        ViewGroup layout_address = findViewById(R.id.layout_address);
        ViewGroup layout_renmai = findViewById(R.id.layout_renmai);
        ViewGroup layout_setting = findViewById(R.id.layout_setting);
        TextView renmaiorguanli = findViewById(R.id.text_renmaiormanage);
        layout_order.setOnClickListener(v -> {
            if (Constants.Companion.isNewPinHaoHuo()){
                Intent intent = new Intent(getContext(), OrderActivityV2.class);
                getContext().startActivity(intent);
            } else {
                Intent intent = new Intent(getContext(), OrderActivity.class);
                getContext().startActivity(intent);
            }
            dismiss();
        });
        layout_video.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), ManageActivity.class);
            intent.putExtra("index",3);
            getContext().startActivity(intent);
            dismiss();
        });
        layout_address.setOnClickListener(v -> {
            if (Constants.Companion.isNewPinHaoHuo()){
                Intent intent = new Intent(getContext(), AddressActivityV2.class);
                intent.putExtra("isSelect",false);
                getContext().startActivity(intent);
            } else {
                Intent intent = new Intent(getContext(), AddressActivity.class);
                intent.putExtra("isSelect",false);
                getContext().startActivity(intent);
            }
            dismiss();
        });

        if(UserInfoViewModel.INSTANCE.getUserInfo().getIs_partner() >= 2){
            renmaiorguanli.setText("我的管理");
        } else {
            renmaiorguanli.setText("我的人脉");
        }
        layout_renmai.setOnClickListener(v -> {
            if(UserInfoViewModel.INSTANCE.getUserInfo().getIs_partner() >= 2){
                Intent intent = new Intent(getContext(), ManageActivity.class);
                intent.putExtra("index",0);
                getContext().startActivity(intent);
            } else {
                Intent intent = new Intent(getContext(), RenMaiActivity.class);
                getContext().startActivity(intent);
            }
            dismiss();
        });
        layout_setting.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), AccountSettingActivity.class);
            getContext().startActivity(intent);
            dismiss();
        });
        if ((UserInfoViewModel.INSTANCE.getUserInfo().getIs_vip() >= 2) | (UserInfoViewModel.INSTANCE.getUserInfo().getIs_partner() >= 2)){

        } else {
            layout_video.setVisibility(View.GONE);
        }
    }
}
