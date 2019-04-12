package com.example.asus.bs_im.model.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.asus.bs_im.model.bean.GroupInfo;
import com.example.asus.bs_im.model.bean.InvitationInfo;
import com.example.asus.bs_im.model.bean.UserInfo;
import com.example.asus.bs_im.model.db.DBhelper;

import java.util.ArrayList;
import java.util.List;

//InvitationTable 的增删改查操作--邀请表的操作类
public class InvitationTableDao {

    private DBhelper mhelper;
    public InvitationTableDao(DBhelper dBhelper) {
        mhelper = dBhelper;
    }

    public void addInvitation(InvitationInfo invitationInfo){
        SQLiteDatabase db = mhelper.getReadableDatabase();
        ContentValues valus = new ContentValues();
        valus.put(InvitationTable.C0L_REASON,invitationInfo.getInvitationReason());
        valus.put(InvitationTable.COL_STATUS,invitationInfo.getInvitationStatus().ordinal());

        UserInfo userInfo = invitationInfo.getUserInfo();
        if (userInfo != null){
            //联系人
            valus.put(InvitationTable.COL_USER_ID,invitationInfo.getUserInfo().getUserId());
            valus.put(InvitationTable.COL_USER_NAME,invitationInfo.getUserInfo().getName());
        }else{
            //群主
            valus.put(InvitationTable.COL_GROUP_ID,invitationInfo.getGroupInfo().getGroupId());
            valus.put(InvitationTable.COL_GROUP_NAME,invitationInfo.getGroupInfo().getGroupName());
            valus.put(InvitationTable.COL_USER_ID,invitationInfo.getGroupInfo().getGroup_invitePerson());
        }
        db.replace(InvitationTable.TABLE_NAME,null,valus);
    }

    public List<InvitationInfo> getInvitation(){
        SQLiteDatabase db = mhelper.getReadableDatabase();
        String sql = " select * from " + InvitationTable.TABLE_NAME;
        Cursor cursor = db.rawQuery(sql, null);

        List<InvitationInfo> invitationInfos = new ArrayList<>();
        while (cursor.moveToNext()){
            InvitationInfo invitationInfo = new InvitationInfo();

            invitationInfo.setInvitationReason(cursor.getString(cursor.getColumnIndex(InvitationTable.C0L_REASON)));
            invitationInfo.setInvitationStatus(initInvitationStatus(cursor.getInt(cursor.getColumnIndex(InvitationTable.COL_STATUS))));

            String groupid = cursor.getString(cursor.getColumnIndex(InvitationTable.COL_GROUP_ID));
            if (groupid != null){
                //群组
                GroupInfo groupInfo = new GroupInfo();
                groupInfo.setGroupId(cursor.getString(cursor.getColumnIndex(InvitationTable.COL_GROUP_ID)));
                groupInfo.setGroupName(cursor.getString(cursor.getColumnIndex(InvitationTable.COL_GROUP_NAME)));
                groupInfo.setGroup_invitePerson(cursor.getString(cursor.getColumnIndex(InvitationTable.COL_USER_ID)));
                invitationInfo.setGroupInfo(groupInfo);
            }else{
                //联系人
                UserInfo userInfo = new UserInfo();
                userInfo.setUserId(cursor.getString(cursor.getColumnIndex(InvitationTable.COL_USER_ID)));
                userInfo.setName(cursor.getString(cursor.getColumnIndex(InvitationTable.COL_USER_NAME)));
                userInfo.setNick(cursor.getString(cursor.getColumnIndex(InvitationTable.COL_USER_NAME)));
                invitationInfo.setUserInfo(userInfo);
            }
            invitationInfos.add(invitationInfo);
        }
        cursor.close();
        return invitationInfos;
    }

    public InvitationInfo.InvitationStatusn initInvitationStatus(int status){
        if (status == InvitationInfo.InvitationStatusn.NEW_INVITATION.ordinal()){
            return InvitationInfo.InvitationStatusn.NEW_INVITATION;
        }
        if (status == InvitationInfo.InvitationStatusn.INVITATION_ACCEPT.ordinal()){
            return InvitationInfo.InvitationStatusn.INVITATION_ACCEPT;
        }
        if (status == InvitationInfo.InvitationStatusn.ACCEPTE_GROUP_APPLICATION.ordinal()){
            return InvitationInfo.InvitationStatusn.ACCEPTE_GROUP_APPLICATION;
        }
        if (status == InvitationInfo.InvitationStatusn.ACCEPTE_GROUP_INVITATION.ordinal()){
            return InvitationInfo.InvitationStatusn.ACCEPTE_GROUP_INVITATION;
        }
        if (status == InvitationInfo.InvitationStatusn.GROUP_APPLICATION_ACCEPTED.ordinal()){
            return InvitationInfo.InvitationStatusn.GROUP_APPLICATION_ACCEPTED;
        }
        if (status == InvitationInfo.InvitationStatusn.GROUP_APPLICATION_REJECTED.ordinal()){
            return InvitationInfo.InvitationStatusn.GROUP_APPLICATION_REJECTED;
        }
        if (status == InvitationInfo.InvitationStatusn.INVITATION_ACCEPTED_BY_PEER.ordinal()){
            return InvitationInfo.InvitationStatusn.INVITATION_ACCEPTED_BY_PEER;
        }
        if (status == InvitationInfo.InvitationStatusn.NEW_GROUP_APPLICATION.ordinal()){
            return InvitationInfo.InvitationStatusn.NEW_GROUP_APPLICATION;
        }
        if (status == InvitationInfo.InvitationStatusn.NEW_GROUP_INVITATION.ordinal()){
            return InvitationInfo.InvitationStatusn.NEW_GROUP_INVITATION;
        }
        if (status == InvitationInfo.InvitationStatusn.REJECT_GROUP_APPLICATION.ordinal()){
            return InvitationInfo.InvitationStatusn.REJECT_GROUP_APPLICATION;
        }
        if (status == InvitationInfo.InvitationStatusn.REJECT_GROUP_INVITATION.ordinal()){
            return InvitationInfo.InvitationStatusn.REJECT_GROUP_INVITATION;
        }
        if (status == InvitationInfo.InvitationStatusn.GROUP_INVITATION_REJECT.ordinal()){
            return InvitationInfo.InvitationStatusn.GROUP_INVITATION_REJECT;
        }
        if (status == InvitationInfo.InvitationStatusn.GROUP_INVITATION_ACCEPTED.ordinal()){
            return InvitationInfo.InvitationStatusn.GROUP_INVITATION_ACCEPTED;
        }
        return null;
    }

    public void removeInvitation(String id){
        if (id == null)
            return;
        SQLiteDatabase db = mhelper.getReadableDatabase();

        db.delete(InvitationTable.TABLE_NAME,InvitationTable.COL_USER_ID + " =?",new String[]{id});
    }

    public void updateInvitationStatus(InvitationInfo.InvitationStatusn invitationStatusn,String id){
        if (id == null)
            return;
        SQLiteDatabase db = mhelper.getReadableDatabase();

        ContentValues value = new ContentValues();
        value.put(InvitationTable.COL_STATUS,invitationStatusn.ordinal());
        db.update(InvitationTable.TABLE_NAME,value,InvitationTable.COL_USER_ID + " =?",new String[]{id});
    }
}
