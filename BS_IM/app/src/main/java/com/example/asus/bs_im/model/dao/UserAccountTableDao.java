package com.example.asus.bs_im.model.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import com.example.asus.bs_im.R;
import com.example.asus.bs_im.model.bean.UserInfo;
import com.example.asus.bs_im.model.db.UserAccount;

import java.io.ByteArrayOutputStream;

//UserAccountTable 的增删改查操作--用户信息表的操作类
public class UserAccountTableDao {

    private final UserAccount userAccount;

    public UserAccountTableDao(Context context) {
        userAccount = new UserAccount(context);

    }
    //添加用户
    public void addAccount (UserInfo userInfo){
        //获取数据库
        SQLiteDatabase database = userAccount.getReadableDatabase();
        //封装用户信息
        ContentValues values = new ContentValues();
        values.put(UserAccountTable.NAME,userInfo.getName());
        values.put(UserAccountTable.ID,userInfo.getUserId());
        values.put(UserAccountTable.PICTURE,userInfo.getPicture());
        values.put(UserAccountTable.NICK,userInfo.getNick());
        values.put(UserAccountTable.PASSWORD,userInfo.getPassword());

        database.replace(UserAccountTable.TABLE_NAME,null,values);
    }
    //获取用户信息
    public UserInfo getAccountInfo(String id){
        //获取数据库
        SQLiteDatabase database = userAccount.getReadableDatabase();
        //查询
        String sql = "select * from " + UserAccountTable.TABLE_NAME + " where " + UserAccountTable.ID + " =? ";
        Cursor cursor = database.rawQuery(sql, new String[]{id});
        UserInfo userInfo = null;
        if (cursor.moveToNext()) {
            userInfo = new UserInfo();
            //封装
            userInfo.setName(cursor.getString(cursor.getColumnIndex(UserAccountTable.NAME)));
            userInfo.setUserId(cursor.getString(cursor.getColumnIndex(UserAccountTable.ID)));
            userInfo.setNick(cursor.getString(cursor.getColumnIndex(UserAccountTable.NICK)));
            userInfo.setPicture(cursor.getBlob(cursor.getColumnIndex(UserAccountTable.PICTURE)));
            userInfo.setPassword(cursor.getString(cursor.getColumnIndex(UserAccountTable.PASSWORD)));
        }
        //关闭
        cursor.close();
        return userInfo;
    }

    /*public void updateAccount(UserInfo userInfo){

    }*/

    //根据ID获取数据库中用户头像并返回为二进制字节数组
    public byte[] getPictureFromSql(String id) {
        byte[] blob = null;
        SQLiteDatabase database = userAccount.getReadableDatabase();
        String sql = " select * from " + UserAccountTable.TABLE_NAME + " where " + UserAccountTable.ID + " =?";
        Cursor cursor = database.rawQuery(sql, new String[]{id});
        while (cursor.moveToNext()){
            blob = cursor.getBlob(cursor.getColumnIndexOrThrow(UserAccountTable.PICTURE));
        }
        cursor.close();
        return blob;
    }

    //将得到的图片转为二进制存到数据库
    public long setPictureToSql(String id,Bitmap bitmap) {
        if (bitmap == null){
            return -1;
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        SQLiteDatabase database = userAccount.getReadableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(UserAccountTable.PICTURE,byteArrayOutputStream.toByteArray());
        long replace = database.update(UserAccountTable.TABLE_NAME,contentValues,UserAccountTable.ID + " =?",new String[]{id});
        return replace;
    }

    public void reNick(String id, String nick){
        SQLiteDatabase database = userAccount.getReadableDatabase();
        ContentValues value = new ContentValues();
        value.put(UserAccountTable.NICK,nick);
        database.update(UserAccountTable.TABLE_NAME,value,UserAccountTable.ID + " =? ",new String[]{id});
    }

    public String getPassWord(String id){
        SQLiteDatabase database = userAccount.getReadableDatabase();
        String sql = " select * from " + UserAccountTable.TABLE_NAME + " where " + UserAccountTable.ID + " =?";
        Cursor cursor = database.rawQuery(sql, new String[]{id});
        String string = cursor.getString(cursor.getColumnIndex(UserAccountTable.PASSWORD));
        return string;
    }
}
