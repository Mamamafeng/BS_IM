package com.example.asus.bs_im.model.bean;

public class InvitationInfo {
    private UserInfo userInfo;
    private GroupInfo groupInfo;

    private String invitationReason;
    private InvitationStatusn invitationStatus;

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public GroupInfo getGroupInfo() {
        return groupInfo;
    }

    public void setGroupInfo(GroupInfo groupInfo) {
        this.groupInfo = groupInfo;
    }

    public String getInvitationReason() {
        return invitationReason;
    }

    public void setInvitationReason(String invitationReason) {
        this.invitationReason = invitationReason;
    }

    public InvitationStatusn getInvitationStatus() {
        return invitationStatus;
    }

    public void setInvitationStatus(InvitationStatusn invitationStatus) {
        this.invitationStatus = invitationStatus;
    }

    public InvitationInfo() {
    }

    public InvitationInfo(UserInfo userInfo, GroupInfo groupInfo, String invitationReason, InvitationStatusn invitationStatus) {
        this.userInfo = userInfo;
        this.groupInfo = groupInfo;
        this.invitationReason = invitationReason;
        this.invitationStatus = invitationStatus;
    }

    @Override
    public String toString() {
        return "InvitationInfo{" +
                "userInfo=" + userInfo +
                ", groupInfo=" + groupInfo +
                ", invitationReason='" + invitationReason + '\'' +
                ", invitationStatus=" + invitationStatus +
                '}';
    }

    public enum InvitationStatusn {
        //联系人邀请状态
        NEW_INVITATION,       //新邀请
        INVITATION_ACCEPT,      //接受邀请
        INVITATION_ACCEPTED_BY_PEER,        //邀请被接受

        //群邀请状态
        NEW_GROUP_INVITATION,       //新的群邀请
        ACCEPTE_GROUP_INVITATION,       //接受群邀请
        REJECT_GROUP_INVITATION,        //拒绝群邀请
        GROUP_INVITATION_REJECT,       //群邀请被对方拒绝
        GROUP_INVITATION_ACCEPTED,      //群邀请被对方接受


        NEW_GROUP_APPLICATION,      //新的申请加群
        ACCEPTE_GROUP_APPLICATION,      //批准加群申请
        REJECT_GROUP_APPLICATION,       //拒绝加群申请
        GROUP_APPLICATION_ACCEPTED,     //加群申请被批准
        GROUP_APPLICATION_REJECTED,     //加群申请被批准

    }
}
