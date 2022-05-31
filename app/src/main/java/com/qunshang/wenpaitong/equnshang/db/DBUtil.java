package com.qunshang.wenpaitong.equnshang.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

public class DBUtil {

    public static DBHelper helper;

    public static DBHelper getHelper(Context context){
        if (helper == null){
            helper = new DBHelper(context);
        }
        return helper;
    }

    public static void insertData(Context context,String product_name) {
        ContentValues values = new ContentValues();
        values.put(DBHelper.NAME, product_name);
        getHelper(context).getWritableDatabase().insert(DBHelper.TABLE_NAME, null, values);
    }

    public static void insertAMallV3Data(Context context,String product_name) {
        ContentValues values = new ContentValues();
        values.put(DBHelper.NAME, product_name);
        getHelper(context).getWritableDatabase().insert(DBHelper.AMALLV3_TABLE_NAME, null, values);
    }

    // 表名
    // 删除条件
    // 满足删除的值
    public static void deleteData(Context context,String product_name) {
        int count = getHelper(context).getWritableDatabase().delete(DBHelper.TABLE_NAME, DBHelper.NAME + " = ?", new String[]{product_name});
    }

    public static void deleteAMALLV3Data(Context context,String product_name) {
        int count = getHelper(context).getWritableDatabase().delete(DBHelper.AMALLV3_TABLE_NAME, DBHelper.NAME + " = ?", new String[]{product_name});
    }

    public static void deteleAll(Context context){
        getHelper(context).getWritableDatabase().delete(DBHelper.TABLE_NAME, null, null);
    }

    public static void deteleAMALLV3All(Context context){
        getHelper(context).getWritableDatabase().delete(DBHelper.AMALLV3_TABLE_NAME, null, null);
    }

    // 表名
    // 修改后的数据
    // 修改条件
    // 满足修改的值
    /*private void updateData() {
        ContentValues values = new ContentValues();
        values.put(DBHelper.NAME, "小茗同学");
        values.put(DBHelper.AGE, 18);
        int count = mDatabase
                .update(DBHelper.TABLE_NAME, values, DBHelper.NAME + " = ?", new String[]{"鹿晗"});
        Toast.makeText(this, "修改成功：" + count, Toast.LENGTH_SHORT).show();
    }*/

    // 表名
    // 查询字段
    // 查询条件
    // 满足查询的值
    // 分组
    // 分组筛选关键字
    // 排序
    public static List<String> queryData(Context context) {
        List<String> strings = new ArrayList<>();
        Cursor cursor = getHelper(context).getWritableDatabase().query(DBHelper.TABLE_NAME,
                new String[]{DBHelper.NAME},
                null,
                null,
                null,
                null,
                null);// 注意空格！

        int nameIndex = cursor.getColumnIndex(DBHelper.NAME);
        //int ageIndex = cursor.getColumnIndex(DBHelper.AGE);
        while (cursor.moveToNext()) {
            String name = cursor.getString(nameIndex);
            //String age = cursor.getString(ageIndex);
            strings.add(name);
        }

        return strings;

    }

    public static List<String> queryAMALLV3Data(Context context) {
        List<String> strings = new ArrayList<>();
        Cursor cursor = getHelper(context).getWritableDatabase().query(DBHelper.AMALLV3_TABLE_NAME,
                new String[]{DBHelper.NAME},
                null,
                null,
                null,
                null,
                null);// 注意空格！

        int nameIndex = cursor.getColumnIndex(DBHelper.NAME);
        //int ageIndex = cursor.getColumnIndex(DBHelper.AGE);
        while (cursor.moveToNext()) {
            String name = cursor.getString(nameIndex);
            //String age = cursor.getString(ageIndex);
            strings.add(name);
        }

        return strings;

    }

}
