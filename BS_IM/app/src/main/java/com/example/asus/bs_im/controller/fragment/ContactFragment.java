package com.example.asus.bs_im.controller.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.asus.bs_im.R;
import com.example.asus.bs_im.controller.Activity.AddContactActivity;
import com.example.asus.bs_im.controller.Activity.ChatActivity;
import com.example.asus.bs_im.controller.Activity.GroupListActivity;
import com.example.asus.bs_im.controller.Activity.InvitationActivity;
import com.example.asus.bs_im.model.Model;
import com.example.asus.bs_im.model.bean.UserInfo;
import com.example.asus.bs_im.utils.Constant;
import com.example.asus.bs_im.utils.SpUtils;
import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.ui.EaseContactListFragment;
import com.hyphenate.exceptions.HyphenateException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ContactFragment extends EaseContactListFragment {

    private ImageView redPoint;
    private LinearLayout contact_invite;
    private LinearLayout contact_group;
    private LocalBroadcastManager localBroadcastManager;
    private BroadcastReceiver ContactInviteChangedReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            redPoint.setVisibility(View.VISIBLE);
            SpUtils.getInstance().save(SpUtils.IS_NEW_INVITE,true);
        }
    };
    private BroadcastReceiver ContactsChangedReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            refreshContact();
        }
    };
    private String username;
    private BroadcastReceiver GroupInviteChangedReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            redPoint.setVisibility(View.VISIBLE);
            SpUtils.getInstance().save(SpUtils.IS_NEW_INVITE,true);
        }
    };

    @Override
    //初始化View
    protected void initView() {
        super.initView();

        titleBar.setRightImageResource(R.drawable.em_add);

        View headerView = View.inflate(getActivity(),R.layout.contact_headerview,null);
        listView.addHeaderView(headerView);

        redPoint = headerView.findViewById(R.id.red_point);

        contact_invite = headerView.findViewById(R.id.contact_invite);

        contact_group = headerView.findViewById(R.id.contact_group);

        setContactListItemClickListener(new EaseContactListItemClickListener() {
            @Override
            public void onListItemClicked(EaseUser user) {
                if(user == null){
                    return;
                }
                Intent intent = new Intent(getActivity(),ChatActivity.class);
                intent.putExtra(EaseConstant.EXTRA_USER_ID,user.getUsername());
                startActivity(intent);
            }
        });

        contact_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),GroupListActivity.class);
                startActivity(intent);
            }
        });
    }

    //页面逻辑的处理
    @Override
    protected void setUpView() {
        super.setUpView();
        titleBar.setRightLayoutClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),AddContactActivity.class);
                startActivity(intent);
            }
        });
        //初始化邀请信息的显示
        boolean isNewInvite = SpUtils.getInstance().getBoolean(SpUtils.IS_NEW_INVITE, false);
        redPoint.setVisibility(isNewInvite ? View.VISIBLE : View.GONE);

        //邀请信息条目点击事件
        contact_invite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redPoint.setVisibility(View.GONE);
                SpUtils.getInstance().save(SpUtils.IS_NEW_INVITE, false);
                Intent intent = new Intent(getActivity(),InvitationActivity.class);
                startActivity(intent);
            }
        });

        //注册广播
        localBroadcastManager = LocalBroadcastManager.getInstance(getActivity());
        localBroadcastManager.registerReceiver(ContactInviteChangedReceiver,new IntentFilter(Constant.CONTACT_INVITATION_CHANGED));
        localBroadcastManager.registerReceiver(ContactsChangedReceiver,new IntentFilter(Constant.CONTACT_CHANGE));
        localBroadcastManager.registerReceiver(GroupInviteChangedReceiver,new IntentFilter(Constant.GROUP_INVITE_CHANGED));
        //从环信服务器获取所有的联系人信息
        getContact();
        //绑定ListView和Contextmenu
        registerForContextMenu(listView);

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        int position = ((AdapterView.AdapterContextMenuInfo) menuInfo).position;
        EaseUser easeUser = (EaseUser) listView.getItemAtPosition(position);
        username = easeUser.getUsername();
        getActivity().getMenuInflater().inflate(R.menu.delete,menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.delete_contact){
            delete();
            return true;
        }
        return super.onContextItemSelected(item);
    }

    private void delete() {
        Model.getInstence().getallthreadpool().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    EMClient.getInstance().contactManager().deleteContact(username);
                    Model.getInstence().getdBmanager().getContactTableDao().deleteContact(username);
                    if (getActivity() == null )
                        return;
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getActivity(),"删除成功",Toast.LENGTH_SHORT).show();
                            refreshContact();
                        }
                    });
                } catch (HyphenateException e) {
                    e.printStackTrace();
                    if (getActivity() == null )
                        return;
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getActivity(),"删除失败",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    private void getContact() {
        Model.getInstence().getallthreadpool().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    //获取所有好友的ID
                    List<String> allContactsFromServer = EMClient.getInstance().contactManager().getAllContactsFromServer();
                    //判断
                    if (allContactsFromServer != null && allContactsFromServer.size() >= 0){
                        List<UserInfo> list = new ArrayList<>();
                        for (String id : allContactsFromServer){
                            UserInfo userInfo = new UserInfo(id);
                            list.add(userInfo);
                        }
                        //保存好友信息到本地数据库
                        Model.getInstence().getdBmanager().getContactTableDao().saveContacts(list,true);
                        //刷新页面
                        if (getActivity() == null){
                            return;
                        }
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                refreshContact();
                            }
                        });
                    }

                } catch (HyphenateException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void refreshContact() {
        List<UserInfo> contacts = Model.getInstence().getdBmanager().getContactTableDao().getContacts();
        if (contacts != null && contacts.size() >= 0){
            //设置数据
            Map<String, EaseUser> contactsMap = new HashMap<>();
            //数据转换==List<UserInfo>--Map<String, EaseUser>
            for (UserInfo contact : contacts){
                EaseUser easeUser = new EaseUser(contact.getUserId());
                contactsMap.put(contact.getUserId(),easeUser);
            }
            setContactsMap(contactsMap);
            //刷新页面
            refresh();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        localBroadcastManager.unregisterReceiver(ContactInviteChangedReceiver);
        localBroadcastManager.unregisterReceiver(ContactsChangedReceiver);
        localBroadcastManager.unregisterReceiver(GroupInviteChangedReceiver);
    }
}
