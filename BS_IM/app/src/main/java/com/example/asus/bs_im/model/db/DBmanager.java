package com.example.asus.bs_im.model.db;

import android.content.Context;

import com.example.asus.bs_im.model.dao.ContactTable;
import com.example.asus.bs_im.model.dao.ContactTableDao;
import com.example.asus.bs_im.model.dao.InvitationTable;
import com.example.asus.bs_im.model.dao.InvitationTableDao;

//联系人和邀请信息表的操作类的管理类
public class DBmanager {
    private final DBhelper dBhelper;
    private final ContactTableDao contactTableDao;
    private final InvitationTableDao invitationTableDao;
    public DBmanager(Context context,String name) {
         dBhelper = new DBhelper(context,name);

        contactTableDao = new ContactTableDao(dBhelper);
        invitationTableDao = new InvitationTableDao(dBhelper);
    }
    //获取联系人表操作类对象
    public ContactTableDao getContactTableDao() {
        return contactTableDao;
    }
    //获取邀请表操作类对象
    public InvitationTableDao getInvitationTableDao() {
        return invitationTableDao;
    }

    public void close() {
        dBhelper.close();
    }
}
