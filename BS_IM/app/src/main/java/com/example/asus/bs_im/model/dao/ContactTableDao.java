package com.example.asus.bs_im.model.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.asus.bs_im.model.Model;
import com.example.asus.bs_im.model.bean.UserInfo;
import com.example.asus.bs_im.model.db.DBhelper;

import java.util.ArrayList;
import java.util.List;

//ContactTable 的增删改查操作--联系人表的操作类
public class ContactTableDao {
    private DBhelper mhelper;       //获取DBhelper对象

    //构造函数
    public ContactTableDao(DBhelper dbhelper) {
        mhelper = dbhelper;
    }

    //获取多个联系人信息
    public List<UserInfo> getContacts(){
        SQLiteDatabase database = mhelper.getReadableDatabase();
        String sql = "select * from " + ContactTable.TABLE_NAME + " where " + ContactTable.IS_CONTACT + "=1";
        Cursor cursor = database.rawQuery(sql, null);

        List<UserInfo> userInfos = new ArrayList<>();

        while (cursor.moveToNext()){
            UserInfo userInfo = new UserInfo();
            userInfo.setName(cursor.getString(cursor.getColumnIndex(ContactTable.CONTACT_NAME)));
            userInfo.setUserId(cursor.getString(cursor.getColumnIndex(ContactTable.CONTACT_ID)));
            userInfo.setNick(cursor.getString(cursor.getColumnIndex(ContactTable.CONTACT_NICK)));
            userInfo.setPicture(cursor.getBlob(cursor.getColumnIndex(ContactTable.CONTACT_PICTURE)));

            userInfos.add(userInfo);
        }

        cursor.close();

        return userInfos;
    }

    //通过环信ID获取联系人
    public UserInfo getContactById(String id){
        if (id == null){
            return null;
        }

        SQLiteDatabase db = mhelper.getReadableDatabase();
        String sql = "select * from " + ContactTable.TABLE_NAME + " where " + ContactTable.CONTACT_ID + " =?";
        Cursor cursor = db.rawQuery(sql, new String[]{id});

        UserInfo userInfo = null;
        if (cursor.moveToNext()){
            userInfo = new UserInfo();
            userInfo.setName(cursor.getString(cursor.getColumnIndex(ContactTable.CONTACT_NAME)));
            userInfo.setUserId(cursor.getString(cursor.getColumnIndex(ContactTable.CONTACT_ID)));
            userInfo.setNick(cursor.getString(cursor.getColumnIndex(ContactTable.CONTACT_NICK)));
            userInfo.setPicture(cursor.getBlob(cursor.getColumnIndex(ContactTable.CONTACT_PICTURE)));

            cursor.close();
        }
        return userInfo;
    }

    //通过环信ID获取多个联系人
    public List<UserInfo> getContactsById(List<String> ids){
        if (ids == null || ids.size()<=0){
            return null;
        }

        List<UserInfo> users = new ArrayList<>();
        for (String id:ids){
            UserInfo contact = getContactById(id);
            users.add(contact);
        }
        return users;
    }

    //保存联系人
    public void saveContact(UserInfo user,boolean isMyContact){
        if (user == null){
            return;
        }
        SQLiteDatabase db = mhelper.getReadableDatabase();

        ContentValues values = new ContentValues();
        values.put(ContactTable.CONTACT_ID,user.getUserId());
        UserInfo accountInfo = Model.getInstence().getUserAccountTableDao().getAccountInfo(user.getUserId());
        values.put(ContactTable.CONTACT_NAME,accountInfo.getName());
        values.put(ContactTable.CONTACT_NICK,accountInfo.getNick());
        values.put(ContactTable.CONTACT_PICTURE,accountInfo.getPicture());
        values.put(ContactTable.IS_CONTACT,isMyContact ? 1:0);

        db.replace(ContactTable.TABLE_NAME,null,values);
    }

    //保存多个联系人
    public void saveContacts(List<UserInfo> contacts,boolean isMyContact){
        if (contacts == null||contacts.size() <= 0){
            return;
        }

        for (UserInfo contact:contacts){
            saveContact(contact,isMyContact);
        }
    }

    //删除联系人
    public void deleteContact(String id){
        if (id == null)
            return;
        SQLiteDatabase db = mhelper.getReadableDatabase();
        db.delete(ContactTable.TABLE_NAME,ContactTable.CONTACT_ID + "=?",new String[]{id});
    }
}
