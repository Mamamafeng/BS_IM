package com.example.asus.bs_im.controller.fragment;


import android.content.Intent;
import android.support.v4.app.Fragment;

import com.example.asus.bs_im.controller.Activity.ChatActivity;
import com.example.asus.bs_im.model.Model;
import com.example.asus.bs_im.utils.SpUtils;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.controller.EaseUI;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.model.EaseNotifier;
import com.hyphenate.easeui.ui.EaseConversationListFragment;

import java.util.List;

import static com.hyphenate.easeui.utils.EaseUserUtils.getUserInfo;

public class ConversationListFragment extends EaseConversationListFragment {

    private EaseUI easeUI;
    private EMMessageListener MessageListener = new EMMessageListener() {
        @Override
        public void onMessageReceived(List<EMMessage> list) {
            for(EMMessage message : list){
                message.setMsgTime(System.currentTimeMillis());
                String userName = message.getStringAttribute("userName","");
                String userPic = message.getStringAttribute("userPic","");
                byte[] logo = userPic.getBytes();
                String ID = message.getFrom();
                Model.getInstence().getUserAccountTableDao().reNick(ID,userName);
                Model.getInstence().getUserAccountTableDao().setPictureToSql(ID,SpUtils.getInstance().pictureToBitmap(logo));
                EaseUser easeUser = getUserInfo(ID);
                easeUser.setNick(userName);
                easeUser.setAvatar(userPic);
                notifyAll();
                //设置本地消息推送通知
                if(!easeUI.hasForegroundActivies()){
                    getNotifier().onNewMsg(message);
                }
            }

            refresh();
        }

        @Override
        public void onCmdMessageReceived(List<EMMessage> list) {

        }

        @Override
        public void onMessageReadAckReceived(List<EMMessage> list) {

        }

        @Override
        public void onMessageDeliveryAckReceived(List<EMMessage> list) {

        }

        @Override
        public void onMessageChanged(EMMessage emMessage, Object o) {

        }
    };

    private EaseNotifier getNotifier() {
        return easeUI.getNotifier();
    }

    @Override
    protected void initView() {
        super.initView();
        setConversationListItemClickListener(new EaseConversationListItemClickListener() {
            @Override
            public void onListItemClicked(EMConversation conversation) {
                Intent intent = new Intent(getActivity(), ChatActivity.class);
                intent.putExtra(EaseConstant.EXTRA_USER_ID,conversation.conversationId());
                if (conversation.getType() == EMConversation.EMConversationType.GroupChat){
                    intent.putExtra(EaseConstant.EXTRA_CHAT_TYPE,EaseConstant.CHATTYPE_GROUP);
                }
                startActivity(intent);
            }
        });
        EMClient.getInstance().chatManager().addMessageListener(MessageListener);
    }
}
