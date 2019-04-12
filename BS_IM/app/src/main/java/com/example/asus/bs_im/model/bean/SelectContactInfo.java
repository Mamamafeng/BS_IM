package com.example.asus.bs_im.model.bean;


public class SelectContactInfo {
    private UserInfo userInfo;
    private boolean isChecked;


    public SelectContactInfo(UserInfo userInfo, boolean isChecked) {
        this.userInfo = userInfo;
        this.isChecked = isChecked;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    @Override
    public String toString() {
        return "SelectContactInfo{" +
                "userInfo=" + userInfo +
                ", isChecked=" + isChecked +
                '}';
    }
}
