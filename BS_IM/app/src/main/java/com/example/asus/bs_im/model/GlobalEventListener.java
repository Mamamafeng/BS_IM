package com.example.asus.bs_im.model;

import android.content.Context;
import android.content.Intent;
import android.provider.SyncStateContract;
import android.support.v4.content.LocalBroadcastManager;

import com.example.asus.bs_im.model.bean.GroupInfo;
import com.example.asus.bs_im.model.bean.InvitationInfo;
import com.example.asus.bs_im.model.bean.UserInfo;
import com.example.asus.bs_im.utils.Constant;
import com.example.asus.bs_im.utils.SpUtils;
import com.hyphenate.EMContactListener;
import com.hyphenate.EMConversationListener;
import com.hyphenate.EMGroupChangeListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.adapter.EMABase;
import com.hyphenate.chat.adapter.EMACallback;

//全局事件监听
public class GlobalEventListener {

    private Context mContext;
    private LocalBroadcastManager localBroadcastManager;
    private final EMGroupChangeListener groupChangeListener = new EMGroupChangeListener() {
        @Override
        public void onInvitationReceived(String groupId, String groupName, String inviter, String reason) {
            //数据库更新
            InvitationInfo invitationInfo = new InvitationInfo();
            invitationInfo.setInvitationReason(reason);
            invitationInfo.setGroupInfo(new GroupInfo(groupName,groupId,inviter));
            invitationInfo.setInvitationStatus(InvitationInfo.InvitationStatusn.NEW_GROUP_INVITATION);
            Model.getInstence().getdBmanager().getInvitationTableDao().addInvitation(invitationInfo);
            //红点
            SpUtils.getInstance().save(SpUtils.IS_NEW_INVITE,true);
            //发送广播
            localBroadcastManager.sendBroadcast(new Intent(Constant.GROUP_INVITE_CHANGED));
        }

        @Override
        public void onApplicationReceived(String groupId, String groupName, String applicant, String reason) {
            InvitationInfo invitationInfo = new InvitationInfo();
            invitationInfo.setInvitationReason(reason);
            invitationInfo.setGroupInfo(new GroupInfo(groupName,groupId,applicant));
            invitationInfo.setInvitationStatus(InvitationInfo.InvitationStatusn.NEW_GROUP_APPLICATION);
            Model.getInstence().getdBmanager().getInvitationTableDao().addInvitation(invitationInfo);
            //红点
            SpUtils.getInstance().save(SpUtils.IS_NEW_INVITE,true);
            //发送广播
            localBroadcastManager.sendBroadcast(new Intent(Constant.GROUP_INVITE_CHANGED));
        }

        @Override
        public void onApplicationAccept(String groupId, String groupName, String accepter) {
            InvitationInfo invitationInfo = new InvitationInfo();
            invitationInfo.setGroupInfo(new GroupInfo(groupName,groupId,accepter));
            invitationInfo.setInvitationStatus(InvitationInfo.InvitationStatusn.GROUP_APPLICATION_ACCEPTED);
            Model.getInstence().getdBmanager().getInvitationTableDao().addInvitation(invitationInfo);
            //红点
            SpUtils.getInstance().save(SpUtils.IS_NEW_INVITE,true);
            //发送广播
            localBroadcastManager.sendBroadcast(new Intent(Constant.GROUP_INVITE_CHANGED));
        }

        @Override
        public void onApplicationDeclined(String groupId, String groupName, String decliner, String reason) {
            InvitationInfo invitationInfo = new InvitationInfo();
            invitationInfo.setInvitationReason(reason);
            invitationInfo.setGroupInfo(new GroupInfo(groupName,groupId,decliner));
            invitationInfo.setInvitationStatus(InvitationInfo.InvitationStatusn.GROUP_APPLICATION_REJECTED);
            Model.getInstence().getdBmanager().getInvitationTableDao().addInvitation(invitationInfo);
            //红点
            SpUtils.getInstance().save(SpUtils.IS_NEW_INVITE,true);
            //发送广播
            localBroadcastManager.sendBroadcast(new Intent(Constant.GROUP_INVITE_CHANGED));
        }

        @Override
        public void onInvitationAccepted(String groupId, String inviter, String reason) {
            InvitationInfo invitationInfo = new InvitationInfo();
            invitationInfo.setInvitationReason(reason);
            invitationInfo.setGroupInfo(new GroupInfo(groupId,groupId,inviter));
            invitationInfo.setInvitationStatus(InvitationInfo.InvitationStatusn.GROUP_INVITATION_ACCEPTED);
            Model.getInstence().getdBmanager().getInvitationTableDao().addInvitation(invitationInfo);
            //红点
            SpUtils.getInstance().save(SpUtils.IS_NEW_INVITE,true);
            //发送广播
            localBroadcastManager.sendBroadcast(new Intent(Constant.GROUP_INVITE_CHANGED));
        }

        @Override
        public void onInvitationDeclined(String groupId, String inviter, String reason) {
            InvitationInfo invitationInfo = new InvitationInfo();
            invitationInfo.setInvitationReason(reason);
            invitationInfo.setGroupInfo(new GroupInfo(groupId,groupId,inviter));
            invitationInfo.setInvitationStatus(InvitationInfo.InvitationStatusn.GROUP_INVITATION_REJECT);
            Model.getInstence().getdBmanager().getInvitationTableDao().addInvitation(invitationInfo);
            //红点
            SpUtils.getInstance().save(SpUtils.IS_NEW_INVITE,true);
            //发送广播
            localBroadcastManager.sendBroadcast(new Intent(Constant.GROUP_INVITE_CHANGED));
        }

        @Override
        public void onUserRemoved(String groupId, String groupName) {

        }

        @Override
        public void onGroupDestroyed(String groupId, String groupName) {

        }

        @Override
        public void onAutoAcceptInvitationFromGroup(String groupId, String inviter, String inviteMessage) {
            InvitationInfo invitationInfo = new InvitationInfo();
            invitationInfo.setInvitationReason(inviteMessage);
            invitationInfo.setGroupInfo(new GroupInfo(groupId,groupId,inviter));
            invitationInfo.setInvitationStatus(InvitationInfo.InvitationStatusn.INVITATION_ACCEPT);
            Model.getInstence().getdBmanager().getInvitationTableDao().addInvitation(invitationInfo);
            //红点
            SpUtils.getInstance().save(SpUtils.IS_NEW_INVITE,true);
            //发送广播
            localBroadcastManager.sendBroadcast(new Intent(Constant.GROUP_INVITE_CHANGED));
        }
    };

    public GlobalEventListener(Context context) {
        mContext = context;
        //创建广播管理者对象
        localBroadcastManager = LocalBroadcastManager.getInstance(mContext);

        //注册监听，联系人变化
        EMClient.getInstance().contactManager().setContactListener(contactListener);

        //注册监听，群变化
        EMClient.getInstance().groupManager().addGroupChangeListener(groupChangeListener);

    }

    private final EMContactListener contactListener = new EMContactListener() {
        @Override
        public void onContactAdded(String s) {

            Model.getInstence().getdBmanager().getContactTableDao().saveContact(new UserInfo(s),true);
            //发送联系人变化的广播
            localBroadcastManager.sendBroadcast(new Intent(Constant.CONTACT_CHANGE));
        }

        @Override
        public void onContactDeleted(String s) {

            Model.getInstence().getdBmanager().getContactTableDao().deleteContact(s);
            Model.getInstence().getdBmanager().getInvitationTableDao().removeInvitation(s);

            localBroadcastManager.sendBroadcast(new Intent(Constant.CONTACT_CHANGE));
        }

        @Override
        public void onContactInvited(String id, String reason) {
            //数据库更新
            InvitationInfo invitation = new InvitationInfo();
            invitation.setUserInfo(new UserInfo(id));
            invitation.setInvitationReason(reason);
            invitation.setInvitationStatus(InvitationInfo.InvitationStatusn.NEW_INVITATION);
            Model.getInstence().getdBmanager().getInvitationTableDao().addInvitation(invitation);
            //UI变化，提示邀请信息
            SpUtils.getInstance().save(SpUtils.IS_NEW_INVITE,true);
            //发送邀请信息变化的广播
            localBroadcastManager.sendBroadcast(new Intent(Constant.CONTACT_INVITATION_CHANGED));
        }

        @Override
        public void onContactAgreed(String s) {
            InvitationInfo invitation = new InvitationInfo();
            invitation.setUserInfo(new UserInfo(s));
            invitation.setInvitationStatus(InvitationInfo.InvitationStatusn.INVITATION_ACCEPT);
            Model.getInstence().getdBmanager().getInvitationTableDao().addInvitation(invitation);

            SpUtils.getInstance().save(SpUtils.IS_NEW_INVITE,true);

            localBroadcastManager.sendBroadcast(new Intent(Constant.CONTACT_INVITATION_CHANGED));
        }

        @Override
        public void onContactRefused(String s) {
            SpUtils.getInstance().save(SpUtils.IS_NEW_INVITE,true);
            localBroadcastManager.sendBroadcast(new Intent(Constant.CONTACT_INVITATION_CHANGED));
        }
    };
}
