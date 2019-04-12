package com.example.asus.bs_im.controller.Activity;


import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.view.View;

import com.example.asus.bs_im.R;
import com.example.asus.bs_im.model.Model;
import com.example.asus.bs_im.model.bean.UserInfo;
import com.example.asus.bs_im.model.dao.UserAccountTable;
import com.example.asus.bs_im.utils.Constant;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.ui.EaseChatFragment;
import com.hyphenate.easeui.widget.chatrow.EaseCustomChatRowProvider;

public class ChatActivity extends FragmentActivity {
    private String itemId;
    private EaseChatFragment easeChatFragment;
    private LocalBroadcastManager broadcastManager;
    private int chatType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        initData();

        initListener();
    }

    private void initListener() {
        easeChatFragment.setChatFragmentListener(new EaseChatFragment.EaseChatFragmentHelper() {
            @Override
            public void onSetMessageAttributes(EMMessage message) {
                // 通过扩展属性，将userPic和userName发送出去。
                String userPic = String.valueOf(Model.getInstence().getUserAccountTableDao().getPictureFromSql(EMClient.getInstance().getCurrentUser()));
                if (!TextUtils.isEmpty(userPic)) {
                    message.setAttribute("userPic", userPic);
                }
                String userName = Model.getInstence().getUserAccountTableDao().getAccountInfo(EMClient.getInstance().getCurrentUser()).getNick();
                if (!TextUtils.isEmpty(userName)) {
                    message.setAttribute("userName", userName);
                }
            }

            @Override
            public void onEnterToChatDetails() {
                Intent intent = new Intent(ChatActivity.this, GroupDetailsActivity.class);
                intent.putExtra(Constant.GROUP_ID,itemId);
                startActivity(intent);
            }
            //点击头像
            @Override
            public void onAvatarClick(String username) {

            }
            //长按头像
            @Override
            public void onAvatarLongClick(String username) {

            }
            //点击聊天内容
            @Override
            public boolean onMessageBubbleClick(EMMessage message) {
                return false;
            }
            //长按聊天内容
            @Override
            public void onMessageBubbleLongClick(EMMessage message) {

            }

            @Override
            public boolean onExtendMenuItemClick(int itemId, View view) {
                return false;
            }

            @Override
            public EaseCustomChatRowProvider onSetCustomChatRowProvider() {
                return null;
            }
        });
        if (chatType == EaseConstant.CHATTYPE_GROUP){
            //注册退群广播
            BroadcastReceiver exitGroupReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    if (itemId.equals(intent.getStringExtra(Constant.GROUP_ID))){
                        finish();
                    }
                }
            };
            broadcastManager.registerReceiver(exitGroupReceiver,new IntentFilter(Constant.EXIT_GROUP));
        }
    }

    private void initData() {
        easeChatFragment = new EaseChatFragment();

        itemId = getIntent().getStringExtra(EaseConstant.EXTRA_USER_ID);

        easeChatFragment.setArguments(getIntent().getExtras());
        //替换fragment
        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fl_chat, easeChatFragment).commit();

        //监听解散群和推出群的广播
        //获取广播管理者
        broadcastManager = LocalBroadcastManager.getInstance(ChatActivity.this);
        chatType = getIntent().getExtras().getInt(EaseConstant.EXTRA_CHAT_TYPE);

    }
}
