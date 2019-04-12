package com.example.asus.bs_im.model.dao;

//邀请信息的表
public class InvitationTable {
    public static final String TABLE_NAME = "invitationtable";

    public static final String COL_USER_ID = "user_id";
    public static final String COL_USER_NAME = "user_name";

    public static final String COL_GROUP_NAME = "group_name";
    public static final String COL_GROUP_ID = "group_id";

    public static final String C0L_REASON = "reason";
    public static final String COL_STATUS = "status";

    public static final String CREATE_TABLE = "create table "
            + TABLE_NAME + " ("
            + COL_USER_ID + " text primary key,"
            + COL_USER_NAME + " text,"
            + COL_GROUP_ID + " text,"
            + COL_GROUP_NAME + " text,"
            + C0L_REASON + " text,"
            + COL_STATUS + " integer);";


}
