package com.example.asus.bs_im.model.db;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.asus.bs_im.model.dao.ContactTable;
import com.example.asus.bs_im.model.dao.InvitationTable;

public class DBhelper extends SQLiteOpenHelper {
    public DBhelper(Context context, String name) {
        super(context, name,null,1);
    }

    public DBhelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //创建联系人的表
        db.execSQL(ContactTable.CREATE_TABLE);
        //创建邀请信息的表
        db.execSQL(InvitationTable.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
