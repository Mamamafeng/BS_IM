package com.example.asus.bs_im.model.bean;

public class GroupInfo {
    private String groupName;
    private String groupId;
    private String group_invitePerson;

    public GroupInfo() {
    }

    public GroupInfo(String groupName, String groupId, String group_invatePerson) {
        this.groupName = groupName;
        this.groupId = groupId;
        this.group_invitePerson = group_invatePerson;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getGroup_invitePerson() {
        return group_invitePerson;
    }

    public void setGroup_invitePerson(String group_invatePersom) {
        this.group_invitePerson = group_invatePersom;
    }

    @Override
    public String toString() {
        return "GroupInfo{" +
                "groupName='" + groupName + '\'' +
                ", groupId='" + groupId + '\'' +
                ", group_invatePersom='" + group_invitePerson + '\'' +
                '}';
    }
}
