package com.example.asus.bs_im;

import android.app.Application;
import android.content.Context;
import android.content.SearchRecentSuggestionsProvider;
import android.text.TextUtils;

import com.example.asus.bs_im.model.Model;
import com.example.asus.bs_im.utils.SpUtils;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMOptions;
import com.hyphenate.easeui.controller.EaseUI;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.utils.EaseCommonUtils;

import java.util.Map;


public class IM extends Application {

    private Map<String, EaseUser> contactList;
    public static Context context;
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
            contactList = (Map<String, EaseUser>) Model.getInstence().getdBmanager().getContactTableDao().getContacts();
            easeUser = contactList.get(username);
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
