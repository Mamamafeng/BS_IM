package com.example.asus.bs_im.model.dao;

//联系人信息的表
public class ContactTable {
    public static final String TABLE_NAME = "contacttable";

    public static final String CONTACT_ID = "id";
    public static final String CONTACT_NAME = "name";
    public static final String CONTACT_NICK = "nick";
    public static final String CONTACT_PICTURE = "picture";
    public static final String IS_CONTACT = "is_contact";

    public static final String CREATE_TABLE = "create table "
            + TABLE_NAME + " ( "
            + CONTACT_ID + " text primary key,"
            + CONTACT_NAME + " text,"
            + CONTACT_NICK + " text,"
            + CONTACT_PICTURE + " blob,"
            + IS_CONTACT + " integer );";
}
