package com.example.asus.bs_im.model.dao;

import java.sql.Blob;

//用户信息的表
public class UserAccountTable {

    public static final String TABLE_NAME = "useraccount";
    public static final String NAME = "name";
    public static final String ID = "id";
    public static final String NICK = "nick";
    public static final String PICTURE = "picture";
    public static final String PASSWORD = "password";

    public static final String CREATE_TABLE = "create table " +
            TABLE_NAME + " (" +
            NAME + " text," +
            ID + " text primary key," +
            PICTURE + " blob," +
            PASSWORD + " text, " +
            NICK + " text);";
 }
