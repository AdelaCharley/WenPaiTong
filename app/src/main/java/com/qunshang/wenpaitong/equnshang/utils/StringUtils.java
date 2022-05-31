package com.qunshang.wenpaitong.equnshang.utils;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.qunshang.wenpaitong.equnshang.Constants;
import com.qunshang.wenpaitong.equnshang.data.ProductBean;
import com.qunshang.wenpaitong.equnshang.data.ProductBeanV2;
import com.qunshang.wenpaitong.equnshang.data.WenBanTongProductBean;
import com.qunshang.wenpaitong.equnshang.view.ProductsDialogV3;
import com.qunshang.wenpaitong.equnshang.view.ProductsDialogV4;

public class StringUtils {

    public static boolean isEmpty(String text){
        if (text == null){
            return true;
        } else if ("".equals(text)){
            return true;
        } else if ("null".equals(text)){
            return true;
        }
        return false;
    }

    public static String addZeroForNum(String str,int strLength) {
        int strLen =str.length();
        if (strLen <strLength) {
            while (strLen< strLength) {
                StringBuffer sb = new StringBuffer();
                sb.append("0").append(str);
                str= sb.toString();
                strLen= str.length();
            }
        }

        return str;
    }

    public static String loadSpecStringTitles(List<ProductBean.DataBean.AttributeList> data){
        StringBuilder builder = new StringBuilder();
        for (int i = 0;i < data.size();i++){
            builder.append(data.get(i).getTitle() + ",");
        }
        if (builder.length() != 0){
            builder.deleteCharAt(builder.length() - 1);
        }
        return builder.toString();
    }

    public static String loadSpecStringTitlesV2(List<ProductBeanV2.DataBean.AttributeList> data){
        StringBuilder builder = new StringBuilder();
        for (int i = 0;i < data.size();i++){
            builder.append(data.get(i).getTitle() + ",");
        }
        if (builder.length() != 0){
            builder.deleteCharAt(builder.length() - 1);
        }
        return builder.toString();
    }

    public static String loadWenBanTongSpecStringTitlesV2(List<WenBanTongProductBean.DataBean.AttributeList> data){
        StringBuilder builder = new StringBuilder();
        for (int i = 0;i < data.size();i++){
            builder.append(data.get(i).getAttributeName() + ",");
        }
        if (builder.length() != 0){
            builder.deleteCharAt(builder.length() - 1);
        }
        return builder.toString();
    }

    public static String analyseSetToString(HashSet<Integer> strings){
        Log.i("zhangjuniii","当前seize是" + strings.size());
        StringBuilder builder = new StringBuilder();
        Iterator<Integer> it = strings.iterator();
        while (it.hasNext()) {
            Integer str = it.next();
            builder.append(str + ",");
        }
        if (builder.length() != 0){
            builder.deleteCharAt(builder.length() - 1);
        }
        Log.i("zhangjuniii","当前seize是" + strings.size());
        Log.i("zhangjuniii",builder.toString());
        return builder.toString();
    }

    public static void log(String log){
        Log.i(Constants.Companion.getLogtag(), log);
    }

    public static String analyseListToString(ArrayList<String> strings){
        Log.i("zhangjuniii","当前seize是" + strings.size());
        StringBuilder builder = new StringBuilder();
        Iterator<String> it = strings.iterator();
        while (it.hasNext()) {
            String str = it.next();
            builder.append(str + ",");
        }
        if (builder.length() != 0){
            builder.deleteCharAt(builder.length() - 1);
        }
        Log.i("zhangjuniii","当前seize是" + strings.size());
        Log.i("zhangjuniii",builder.toString());
        return builder.toString();
    }

    public static boolean isRightPhone(String phone) {
        if (phone.isEmpty()){
            //toast.setShortToast("请输入手机号");
            return false;
        }

        String regexPhone = "^1[3-9]\\d{9}$";
        Pattern p=Pattern.compile(regexPhone);
        Matcher m=p.matcher(phone);

        if (!m.matches()) {
            //toast.setShortToast("手机号不正确");
            return false;
        } else {
            return true;
        }
    }

    public static String getBriefAddress(String detail){
        String[] split = detail.split(",");
        if (split.length < 2 && split.length > 0){
            return split[0];
        } else if (split.length >= 3){
            return split[0] + ","  + split[1] + "," + split[2];
        } else {
            return detail;
        }
    }

    public static String getDetail(String detail){
        String[] split = detail.split(",");
        if (split.length < 2 && split.length > 0){
            return split[0];
        } else if (split.length > 3){
            String result = "";

            for (int i = 3;i < split.length;i++){
                result += split[i];
            }
            return result;
        } else {
            return detail;
        }
    }

    public static HashSet<String> getUseableSku(int index,String value){//这里假设index为0
        HashSet<String> results = new HashSet<>();
        if (index == ProductsDialogV3.Companion.getBean().getData().getSpecList().size() - 1){
            return null;
        }
        ProductBean bean = ProductsDialogV3.Companion.getBean();
        List<String> spec = bean.getData().getSpecList().get(index).getList();
        List<ProductBean.DataBean.SkuList> preUseableSku = bean.getData().getSkuList();
        for (ProductBean.DataBean.SkuList sku : preUseableSku){
            String[] values = sku.getValue().split(",");
            if (value.equals(values[index])){
                Log.i("zhangjunstring","specvalue是" + value);
                Log.i("zhangjunstring","value是" + values[index]);
                results.add(values[index + 1]);
            }
        }
        return results;
    }



    public static ProductBean.DataBean.SkuList getSelectedSku(){
        StringBuilder builder = new StringBuilder();
        for (int i = 0;i < ProductsDialogV3.words.length;i++){
            builder.append(ProductsDialogV3.words[i] + ",");
        }
        if (builder.length() != 0){
            builder.deleteCharAt(builder.length() - 1);
        }
        String words = builder.toString();
        List<ProductBean.DataBean.SkuList> skus = ProductsDialogV3.Companion.getBean().getData().getSkuList();
        for (int i = 0;i < skus.size();i++){
            if (skus.get(i).getValue().equals(words)){
                return skus.get(i);
            }
        }
        return null;
    }

    public static HashSet<String> getUseableSkuV2(int index,String value){//这里假设index为0
        HashSet<String> results = new HashSet<>();
        if (index == ProductsDialogV4.Companion.getBean().getData().getSpecList().size() - 1){
            return null;
        }
        ProductBeanV2 bean = ProductsDialogV4.Companion.getBean();
        List<String> spec = bean.getData().getSpecList().get(index).getList();
        List<ProductBeanV2.DataBean.SkuList> preUseableSku = bean.getData().getSkuList();
        for (ProductBeanV2.DataBean.SkuList sku : preUseableSku){
            String[] values = sku.getValue().split(",");
            if (value.equals(values[index])){
                Log.i("zhangjunstring","specvalue是" + value);
                Log.i("zhangjunstring","value是" + values[index]);
                results.add(values[index + 1]);
            }
        }
        return results;
    }

    public static ProductBeanV2.DataBean.SkuList getSelectedSkuV2(){
        StringBuilder builder = new StringBuilder();
        for (int i = 0;i < ProductsDialogV4.words.length;i++){
            builder.append(ProductsDialogV4.words[i] + ",");
        }
        if (builder.length() != 0){
            builder.deleteCharAt(builder.length() - 1);
        }
        String words = builder.toString();
        List<ProductBeanV2.DataBean.SkuList> skus = ProductsDialogV4.Companion.getBean().getData().getSkuList();
        for (int i = 0;i < skus.size();i++){
            if (skus.get(i).getValue().equals(words)){
                return skus.get(i);
            }
        }
        return null;
    }

}
