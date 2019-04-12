package com.example.asus.bs_im.model.bean;

import java.sql.Blob;
import java.util.Arrays;

//用户信息
public class UserInfo {

    private String name;//名称
    private String userId;//用户ID
    private String nick;//用户昵称
    private byte[] picture;//用户头像
    private String password;//用户密码

    @Override
    public String toString() {
        return "UserInfo{" +
                "name='" + name + '\'' +
                ", userId='" + userId + '\'' +
                ", nick='" + nick + '\'' +
                ", picture=" + Arrays.toString(picture) +
                ", password='" + password + '\'' +
                '}';
    }

    public UserInfo(String userId) {
        this.name = userId;
        this.userId = userId;
        this.nick = userId;
        this.picture = userId.getBytes();
    }

    public UserInfo(String userId, String password) {
        this.userId = userId;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userid) {
        this.userId = userid;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public byte[] getPicture() {
        return picture;
    }

    public void setPicture(byte[] picture) {
        this.picture = picture;
    }

    public UserInfo() {
    }
}
