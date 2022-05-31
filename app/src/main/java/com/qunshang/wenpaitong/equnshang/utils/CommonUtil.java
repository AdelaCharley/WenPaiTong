package com.qunshang.wenpaitong.equnshang.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;

import java.io.File;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.qunshang.wenpaitong.equnshang.data.BaseHttpBean;
import com.qunshang.wenpaitong.equnshang.data.UserInfoViewModel;
import com.qunshang.wenpaitong.equnshang.model.ApiManager;

/**
 * @author wj
 */

public class CommonUtil {

    public CommonUtil() {
    }

    public static final int page = 10;//每页多少个视频

    /*public static ArrayList<RecommendVideoBean.DataBean> filterRecommendData(ArrayList<RecommendVideoBean.DataBean> data,int pos){

        ArrayList<RecommendVideoBean.DataBean> list = new ArrayList<>();

        int index = pos/page + 1;//第几个100   假设pos是80  ,index是1
        int start = (index - 1) * page;   //start   0
        int end = 0;
        if (data.size() > index * page){
            end = index * page;
        } else {
            end = data.size();
        }
        StringUtils.log("start是" + start);
        StringUtils.log("end是" + end);
        for (int i = start;i < end;i++){
            list.add(data.get(i));
        }
        return list;
    }

    public static String computePrice(String price){

        StringBuffer buffer = new StringBuffer();

        String[] split = price.split(",");
        for (int i = 0;i < split.length;i++){
            buffer.append(split[i]);
        }
        return buffer.toString();
    }*/

    /**
     * dp转成px
     */
    public static int dp2px(@NonNull Context context, int dp) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        StringUtils.log("像素数量是" + metrics.widthPixels);
        StringUtils.log("1dp是" + ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT) + "px");
        return (int) (dp * ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static int getScreenWidthInPx(@NonNull Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeightInPx(@NonNull Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }

    public static void hideKeyboard(View view){
        InputMethodManager imm = (InputMethodManager) view.getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(),0);
        }
    }

    public static void showKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) view.getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            view.requestFocus();
            imm.showSoftInput(view, 0);
        }
    }

    public static String getVersionName(Context context){
        PackageManager manager = context.getPackageManager();
        String name = null;
        try {
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            name = info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return name;
    }

    public static int getVersionCode(Context context){
        PackageManager manager = context.getPackageManager();
        int code = 0;
        try {
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            code = info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return code;
    }

    public static void installAPK(String filePath,Context context) {
        File apk = new File(filePath);

        Intent intent = new Intent(Intent.ACTION_VIEW);

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        Uri uri = FileProvider.getUriForFile(context, "com.qunshang.wenpaitong.fileProvider", apk);

        intent.setDataAndType(uri, "application/vnd.android.package-archive");

        context.startActivity(intent);
        /*File apkFile = new File(filePath);
        if (!apkFile.exists()) {
            return;
        }
        Intent intent = new Intent(Intent.ACTION_VIEW);
//      安装完成后，启动app（源码中少了这句话）

        if (null != apkFile) {
            try {
                //兼容7.0
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    Uri contentUri = FileProvider.getUriForFile(context, context.getPackageName() + ".fileProvider", apkFile);
                    intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
                    //兼容8.0
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        boolean hasInstallPermission = context.getPackageManager().canRequestPackageInstalls();
                        if (!hasInstallPermission) {
                            startInstallPermissionSettingActivity(context);
                            return;
                        }
                    }
                } else {
                    intent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                }
                if (context.getPackageManager().queryIntentActivities(intent, 0).size() > 0) {
                    context.startActivity(intent);
                }
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }*/
    }

    public static void startInstallPermissionSettingActivity(Context context) {
        //注意这个是8.0新API
        Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static void doCompleteTask(int type){
        StringUtils.log("当前的type是" + type );
        ApiManager.Companion.getInstance().getApiMainTest().completeTask(UserInfoViewModel.INSTANCE.getUserId(), type).enqueue(new Callback<BaseHttpBean<String>>() {
            @Override
            public void onResponse(Call<BaseHttpBean<String>> call, Response<BaseHttpBean<String>> response) {
                if (response.body() != null){
                    if (response.body().getCode() == 200){
                        StringUtils.log("陈宫个了");
                    } else {
                        StringUtils.log("code" + response.body().getCode() + "msg" + response.body().getMsg() + "data" + response.body().getData());
                    }
                }
            }

            @Override
            public void onFailure(Call<BaseHttpBean<String>> call, Throwable t) {

            }
        });
    }

}
