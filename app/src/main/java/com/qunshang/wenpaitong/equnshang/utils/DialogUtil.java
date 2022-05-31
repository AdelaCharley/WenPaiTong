package com.qunshang.wenpaitong.equnshang.utils;

import android.content.Context;
import android.text.SpannableString;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.kongzue.dialog.v3.TipDialog;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.enums.PopupAnimation;

import com.qunshang.wenpaitong.equnshang.data.MyPhysicalStoreBean;
import com.qunshang.wenpaitong.equnshang.data.VersionBean;
import com.qunshang.wenpaitong.equnshang.view.CustomerDialog;
import com.qunshang.wenpaitong.equnshang.view.SignInDialog;
import com.qunshang.wenpaitong.equnshang.view.dialog.UpdateDialog;

public class DialogUtil {

    private Context context;

    public DialogUtil(Context context){
        this.context = context;
    }

    public static void toast(Context context,String content){
        Toast.makeText(context, content, Toast.LENGTH_SHORT).show();
    }

    public static void toast(Context context, SpannableString content){
        Toast.makeText(context, content, Toast.LENGTH_SHORT).show();
    }

    public static void showCustomDialog(Context context,String key){
        CustomerDialog customPopup = new CustomerDialog(context, key);
        new XPopup.Builder(context)
                            .popupAnimation(PopupAnimation.ScaleAlphaFromCenter)
                .autoOpenSoftInput(false)
                .asCustom(customPopup)
                .show();
    }

    public static void showSharePhysicalDialog(Context context, MyPhysicalStoreBean bean){
        SignInDialog customPopup = new SignInDialog(context, bean);
        new XPopup.Builder(context)
                .popupAnimation(PopupAnimation.ScaleAlphaFromCenter)
                .autoOpenSoftInput(false)
                .asCustom(customPopup)
                .show();
    }

    public static void showSuccessDialog(Context context,String str){
        TipDialog.show((AppCompatActivity) context,str, TipDialog.TYPE.SUCCESS);
    }

    public static void showErrorDialog(Context context,String str){
        TipDialog.show((AppCompatActivity) context,str, TipDialog.TYPE.ERROR);
    }

    public static void showWarnDialog(Context context,String str){
        TipDialog.show((AppCompatActivity) context,str, TipDialog.TYPE.WARNING);
    }

    public static UpdateDialog showUpdateDialog(Context context,VersionBean bean){
        if (dialog == null){
            dialog = new UpdateDialog(context,bean);
        }
        if (!dialog.isShow()){
            new XPopup.Builder(context)
                    .dismissOnTouchOutside(false)
                    .dismissOnBackPressed(false)
                    .popupAnimation(PopupAnimation.ScaleAlphaFromCenter)
                    .autoOpenSoftInput(false)
                    .asCustom(dialog)
                    .show();
        }
        return dialog;
    }

    public static void refreshUpdateDialog(int progress){
        if (dialog != null && dialog.isShow()){
            dialog.update(progress);
        }
    }

    public static void dismissDialog(){
        if (dialog != null && dialog.isShow()){
            dialog.dismiss();
        }
    }

    static UpdateDialog dialog;

}
