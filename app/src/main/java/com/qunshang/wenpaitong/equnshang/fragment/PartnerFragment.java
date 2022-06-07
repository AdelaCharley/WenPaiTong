package com.qunshang.wenpaitong.equnshang.fragment;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
;
import androidx.appcompat.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kongzue.dialog.v3.CustomDialog;

import com.qunshang.wenpaitong.R;
import com.qunshang.wenpaitong.equnshang.Constants;
/*import com.qunshang.wenpaitong.equnshang.activity.AMallActivity;*/
import com.qunshang.wenpaitong.equnshang.activity.AMallActivityV3_NewYear;
import com.qunshang.wenpaitong.equnshang.activity.BaseActivity;
import com.qunshang.wenpaitong.equnshang.activity.LoginActivity;
import com.qunshang.wenpaitong.equnshang.activity.ProcureActivity;
import com.qunshang.wenpaitong.equnshang.activity.ProductDetailActivity;
import com.qunshang.wenpaitong.equnshang.activity.QunShangCollegeActivity;
import com.qunshang.wenpaitong.equnshang.activity.WenBanTongActivity;
import com.qunshang.wenpaitong.equnshang.data.UserInfoViewModel;
import com.qunshang.wenpaitong.equnshang.data.VideoType;
import com.qunshang.wenpaitong.equnshang.utils.UserHelper;

/**
 * 合伙人页面
 * create by 何姝霖
 */
public class PartnerFragment extends MyBaseFragment {
    public PartnerFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_partner, container, false);
    }

    SampleVideoFragment fragment = new SampleVideoFragment(VideoType.Companion.getTYPE_PIONEER());

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        BaseActivity activity = (BaseActivity) requireActivity();
        //activity.setSystemBarColor(R.color.black);
        activity.changeToGreyButTranslucent();
        getChildFragmentManager().beginTransaction().add(R.id.container,fragment).commit();
        TextView procureshop = view.findViewById(R.id.procureshop);
        procureshop.setOnClickListener(v -> {
            if (!UserHelper.isLogin(requireContext())){
                startActivity(new Intent(requireContext(), LoginActivity.class));
                return;
            }
            if (UserInfoViewModel.INSTANCE.getUserInfo().getIs_partner() >= 2){
                Intent intent = new Intent(getContext(), ProcureActivity.class);
                startActivity(intent);
            } else {
                CustomDialog.show((AppCompatActivity) requireActivity(), R.layout.dialog_partner, new CustomDialog.OnBindView() {
                    @Override
                    public void onBind(CustomDialog dialog, View v) {
                        //dialog.doDismiss();
                        ImageView knowdetail = v.findViewById(R.id.knowdetail);
                        knowdetail.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.doDismiss();
                                Intent intent = new Intent(requireContext(), ProductDetailActivity.class);
                                intent.putExtra("productId", 2);
                                requireContext().startActivity(intent);
                            }
                        });
                    }
                });

            }
        });
        TextView qunshangcollege = view.findViewById(R.id.qunshangcollege);
        qunshangcollege.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), QunShangCollegeActivity.class);
            startActivity(intent);
        });
        TextView partners = view.findViewById(R.id.partners);
        partners.setOnClickListener(v -> {
            if (!UserHelper.isLogin(requireContext())){
                startActivity(new Intent(requireContext(), LoginActivity.class));
                return;
            }
            Intent intent = new Intent(getContext(), WenBanTongActivity.class);
            startActivity(intent);
        });
        TextView amall = view.findViewById(R.id.amall);
        amall.setOnClickListener(v -> {
            if (!UserHelper.isLogin(requireContext())){
                startActivity(new Intent(requireContext(), LoginActivity.class));
                return;
            }
            if (Constants.Companion.isNewPinHaoHuo()){
                Intent intent = new Intent(requireContext(),
                        AMallActivityV3_NewYear.class);
                startActivity(intent);
            } else {
                /*Intent intent = new Intent(requireContext(),
                        AMallActivity.class);
                startActivity(intent);*/
            }
        });
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        if (isHidden()){
            fragment.pause();
        } else {
            BaseActivity activity = (BaseActivity) requireActivity();
            activity.changeToGreyButTranslucent();
            fragment.start();
        }
    }
}