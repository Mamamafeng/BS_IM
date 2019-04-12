package com.example.asus.bs_im.model.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.asus.bs_im.model.dao.UserAccountTable;

public class UserAccount extends SQLiteOpenHelper {

    //构造
    public UserAccount(Context context) {
        super(context, "useraccout.db", null, 1);
    }
    //创建数据库时调用
    @Override
    public void onCreate(SQLiteDatabase db) {
        //db.execSQL("CREATE TABLE IF NOT EXISTS UserAccount (name text, ID text primary key, nick text, picture text)");
        db.execSQL(UserAccountTable.CREATE_TABLE);
    }
    //更新数据库时调用
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
