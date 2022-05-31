package com.qunshang.wenpaitong.equnshang.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    // 数据库文件名
    public static final String DB_NAME = "products_search";
    // 数据库表名
    public static final String TABLE_NAME = "product";
    // 数据库版本号
    public static final int DB_VERSION = 2;

    public static final String NAME = "name";

    public static final String _id = "_id";

    public static final String AMALLV3_TABLE_NAME = "amallv3";

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    // 当数据库文件创建时，执行初始化操作，并且只执行一次
    @Override
    public void onCreate(SQLiteDatabase db) {
        // 建表
        String sql = "create table " +
                TABLE_NAME +
                "(_id integer primary key autoincrement, " +
                NAME + " varchar" +
                ")";

        String amallv3sql = "create table " +
                AMALLV3_TABLE_NAME +
                "(_id integer primary key autoincrement, " +
                NAME + " varchar" +
                ")";

        db.execSQL(amallv3sql);
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion == 1){
            String amallv3sql = "create table " +
                    AMALLV3_TABLE_NAME +
                    "(_id integer primary key autoincrement, " +
                    NAME + " varchar" +
                    ")";

            db.execSQL(amallv3sql);
        }
    }



}