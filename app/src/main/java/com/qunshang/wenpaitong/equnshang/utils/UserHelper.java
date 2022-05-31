package com.qunshang.wenpaitong.equnshang.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.qunshang.wenpaitong.equnshang.data.BrowserHistoryBean;
import com.qunshang.wenpaitong.equnshang.data.UserInfoViewModel;

import java.util.ArrayList;

public class UserHelper {

    public static String getCurrentLoginUser(Context context){
        if (context == null){
            return "";
        }
        SharedPreferences mSpf;
        mSpf = context.getSharedPreferences("user",Context.MODE_PRIVATE);
        String info = mSpf.getString("username","");
        return info;
    }

    public static boolean getCurrentLogoutingStatus(Context context){
        if (context == null){
            return false;
        }
        SharedPreferences mSpf;
        mSpf = context.getSharedPreferences("logoutstatus",Context.MODE_PRIVATE);
        boolean info = mSpf.getBoolean("status",false);
        if (info){
            String userId = mSpf.getString("userId","-100");
            if (UserInfoViewModel.INSTANCE.getUserId().equals(userId)){
                return info;
            }
        }
        return info;
    }

    public static void setCurrentLogoutingStatus(Context context,boolean info){
        SharedPreferences mSpf;
        mSpf = context.getSharedPreferences("logoutstatus",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mSpf.edit();
        editor.putBoolean("status",info);
        editor.putString("userId",UserInfoViewModel.INSTANCE.getUserId());
        editor.commit();
    }

    public static boolean getPushStatus(Context context){
        if (context == null){
            return false;
        }
        SharedPreferences mSpf;
        mSpf = context.getSharedPreferences("push",Context.MODE_PRIVATE);
        boolean info = mSpf.getBoolean("status",true);
        return info;
    }

    public static void setPushStatus(Context context,boolean info){
        SharedPreferences mSpf;
        mSpf = context.getSharedPreferences("push",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mSpf.edit();
        editor.putBoolean("status",info);
        //editor.putString("userId",UserInfoViewModel.INSTANCE.getUserId());
        editor.commit();
    }

    //是否是第一次进入拼采购商城
    public static boolean getIsFirstEnterProduce(Context context){
        SharedPreferences mSpf;
        mSpf = context.getSharedPreferences("pincaigou",Context.MODE_PRIVATE);
        String info = mSpf.getString("isFirst","");
        if (StringUtils.isEmpty(info)){
            return true;
        }
        return false;
    }

    public static void setIsFirstEnterProduce(Context context,String str){
        SharedPreferences mSpf;
        mSpf = context.getSharedPreferences("pincaigou",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mSpf.edit();
        editor.putString("isFirst",str);
        editor.commit();
    }

    public static boolean isLogin(Context context){
        String currentUser = getCurrentLoginUser(context);
        if (StringUtils.isEmpty(currentUser)){
            return false;
        } else if (UserInfoViewModel.INSTANCE.getUserInfo() == null){
            return false;
        }
        else {
            return true;
        }
    }

    public static void setCurrentLoginUser(Context context,String username){
        SharedPreferences mSpf;
        mSpf = context.getSharedPreferences("user",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mSpf.edit();
        editor.putString("username",username);
        editor.commit();
    }

    public static ArrayList<BrowserHistoryBean.DataBean.ProductList> analyseBrowseList(BrowserHistoryBean bean){

        ArrayList<BrowserHistoryBean.DataBean.ProductList> lists = new ArrayList<>();

        for (int i = 0;i < bean.data.size();i++){
            for (int j = 0;j < bean.data.get(i).getProductList().size();j++){
                BrowserHistoryBean.DataBean.ProductList ele = bean.data.get(i).getProductList().get(j);
                if (j == 0){
                    ele.setDate(bean.data.get(i).getDate());
                }
                lists.add(ele);
            }
        }
        return lists;
    }

    public static void clear(Context context){
        SharedPreferences mSpf;
        mSpf = context.getSharedPreferences("user",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mSpf.edit();
        editor.clear().commit();
    }

}
