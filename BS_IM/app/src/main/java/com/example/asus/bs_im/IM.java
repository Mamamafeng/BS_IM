package com.example.asus.bs_im;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.example.asus.bs_im.model.Model;
import com.example.asus.bs_im.model.bean.UserInfo;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMOptions;
import com.hyphenate.easeui.controller.EaseUI;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.utils.EaseCommonUtils;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;


public class IM extends Application {

    private Map<String, EaseUser> contactList;
    public static Context context;

    private EaseUI easeUI;
    @Override
    public void onCreate() {
        super.onCreate();

        EMOptions options = new EMOptions();
        options.setAcceptInvitationAlways(false); //好友添加，需要同意后接收邀请
        options.setAutoAcceptGroupInvitation(false); //群邀请，需同意
        //初始化easeUI
        EaseUI.getInstance().init(this,options);

        //初始化数据模型层
        Model.getInstence().init(this);
        //初始化全局上下文对象
        context = this;
        //设置EaseUI 里的 fragment 及相关控件显示用户头像和昵称
        easeUI = EaseUI.getInstance();
        easeUI.setUserProfileProvider(new EaseUI.EaseUserProfileProvider() {
            @Override
            public EaseUser getUser(String username) {
                return getUserInfo(username);
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Class.forName("com.mysql.jdbc.Driver");
                    java.sql.Connection connection = DriverManager.getConnection("jdbc:mysql://192.168.79.2/character set","root","661617");
                    String sql = "select * from account";
                    Statement statement = connection.createStatement();
                    ResultSet resultSet = statement.executeQuery(sql);
                    while (resultSet.next()){
                        String name = resultSet.getString("usernick");
                        Log.d("feng",name);
                    }
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    public static Context getGlobalContext(){
        return  context;
    }

    private EaseUser getUserInfo(String username){
        EaseUser easeUser = null;
        //本人
        if (username.equals(EMClient.getInstance().getCurrentUser())){
            easeUser = new EaseUser(username);
            easeUser.setAvatar(String.valueOf(Model.getInstence().getUserAccountTableDao().getPictureFromSql(username)));
            easeUser.setNick(Model.getInstence().getUserAccountTableDao().getAccountInfo(username).getNick());
            return easeUser;
        }
        //好友
        if (contactList != null && contactList.containsKey(username)){
            easeUser = contactList.get(username);
        }else {
            List<UserInfo> contacts = Model.getInstence().getdBmanager().getContactTableDao().getContacts();
            if (contacts == null && contacts.size() <= 0){
                for(UserInfo userInfo : contacts){
                    easeUser = new EaseUser(userInfo.getName());
                    contactList.put(userInfo.getName(),easeUser);
                }
                easeUser = contactList.get(username);
            }
        }
        //不是好友
        if (easeUser == null){
            easeUser = new EaseUser(username);
            EaseCommonUtils.setUserInitialLetter(easeUser);
        }else{
            if (TextUtils.isEmpty(easeUser.getNick())){
                easeUser.setNick(easeUser.getUsername());
            }
        }

        return easeUser;
    }

}
